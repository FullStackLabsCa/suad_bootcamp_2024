package io.reactivestax.component;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import io.reactivestax.contract.TradeProcessor;
import io.reactivestax.domain.Trade;
import io.reactivestax.hikari.DataSource;
import io.reactivestax.infra.Infra;
import io.reactivestax.repository.CsvTradeProcessorRepository;
import io.reactivestax.repository.hibernate.crud.JournalEntryCRUD;
import io.reactivestax.repository.hibernate.crud.TradePayloadCRUD;
import io.reactivestax.repository.hibernate.crud.TradePositionCRUD;
import io.reactivestax.utils.RabbitMQUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

import static io.reactivestax.infra.Infra.readFromApplicationPropertiesStringFormat;

@Slf4j
public class CsvTradeProcessor implements Runnable, TradeProcessor {
    private final LinkedBlockingDeque<String> dlQueue = new LinkedBlockingDeque<>();
    static AtomicInteger countSec = new AtomicInteger(0);
    private final String queueName;


    public CsvTradeProcessor(String queueName) {
        this.queueName = queueName;
    }


    @Override
    public void run() {
        try {
            processTrade();
        } catch (Exception e) {
            CsvTradeProcessor.log.info("trade processor:  {}", e.getMessage());
        }
    }

    @Override
    public void processTrade() throws Exception {
        CsvTradeProcessorRepository csvTradeProcessorRepository = new CsvTradeProcessorRepository(DataSource.getConnection());
        String exchangeName = readFromApplicationPropertiesStringFormat("rabbitMQ.exchange.name");
        try (Channel channel = RabbitMQUtils.getInstance().createChannel()) {

            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT);
            // Bind the queue to all three partitions
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, exchangeName, queueName);

            log.info(" [*] Waiting for messages in '{}'.", queueName);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String tradeId = new String(delivery.getBody(), StandardCharsets.UTF_8);
                log.info(" [x] Received '{}' with routing key '{}'", tradeId, delivery.getEnvelope().getRoutingKey());
                try {
                    processJournalWithPosition(tradeId, csvTradeProcessorRepository);
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    log.info("Position insertion successful ===========>");
                } catch (Exception e) {
                    log.error("Journal Entry and position processing failed, retrying");
                    channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);
                }
            };
            channel.basicConsume(
                    queueName,
                    false,          //second parameter false means auto-acknowledge is false
                    deliverCallback,
                    consumerTag ->
                    {
                    });
            // Use a CountDownLatch to wait indefinitely
            CountDownLatch latch = new CountDownLatch(1);
            latch.await(); // This will block the main thread forever until countDown() is called
        }
    }

    private void processJournalWithPosition(String tradeId, CsvTradeProcessorRepository csvTradeProcessorRepository) {
        String payload = TradePayloadCRUD.readTradePayloadByTradeId(tradeId);
        String[] payloads = payload.split(",");
        Trade trade = new Trade(
                payloads[0],
                payloads[1],
                payloads[2],
                payloads[3],
                payloads[4],
                Integer.parseInt(payloads[5]),
                Double.parseDouble(payloads[6]),
                Integer.parseInt(payloads[5])
        );
        log.info("result journal{}", payload);
        try {
            if (!csvTradeProcessorRepository.lookUpSecurityByCUSIP(trade.getCusip())) {
                log.warn("No security found....");
                dlQueue.put(trade.getTradeIdentifier());
                log.debug("times {} {}", trade.getCusip(), countSec.incrementAndGet());
            } else {
                JournalEntryCRUD.persistJournalEntry(trade);
                processPosition(trade);
            }

        } catch (SQLException e) {
            log.error(e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error(e.getMessage());
        }
    }


    public void processPosition(Trade trade) {
        Integer version = TradePositionCRUD.getCusipVersion(trade);
        if (version != null) {
            TradePositionCRUD.updatePosition(trade, version);
        } else {
            TradePositionCRUD.persistPosition(trade);
        }
    }

}
