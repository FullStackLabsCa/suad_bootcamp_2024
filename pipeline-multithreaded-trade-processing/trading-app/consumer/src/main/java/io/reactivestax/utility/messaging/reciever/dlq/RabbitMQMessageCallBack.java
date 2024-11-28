package io.reactivestax.utility.messaging.reciever.dlq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import io.reactivestax.service.TradeProcessorService;
import io.reactivestax.types.enums.RabbitMQHeaders;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesIntegerFormat;
import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesStringFormat;

@Slf4j
public class RabbitMQMessageCallBack implements DeliverCallback {

    private final Channel channel;
    private final String queueName;
    static AtomicInteger messageCounter = new AtomicInteger(0);


    public RabbitMQMessageCallBack(Channel channel, String queueName) {
        this.channel = channel;
        this.queueName = queueName;
    }

    @Override
    public void handle(String consumerTag, Delivery delivery) throws IOException {
        String message = null;
        try {
            message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            log.info(" [x] Received: '{} ", message);
            TradeProcessorService.getInstance().processTrade(message);

            // Acknowledge successful processing
            int currentCount = messageCounter.incrementAndGet();
            log.info(" [x] Total messages consumed: {} from {}", currentCount, queueName);
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        } catch (Exception exception) {
            Map<String, Object> headers = delivery.getProperties().getHeaders();
            int retries = headers != null && headers.containsKey(RabbitMQHeaders.X_RETRIES.getHeaderKey())
                    ? (int) headers.get(RabbitMQHeaders.X_RETRIES.getHeaderKey())
                    : 0;
            retryMessagePublish(delivery, exception, retries, message);
        }
    }


    private void retryMessagePublish(Delivery delivery, Exception e, int retries, String message) throws IOException {
        if (retries >= readFromApplicationPropertiesIntegerFormat("max.retry.count")) {
            log.info(" [x] Max retries reached: {} . Discarding message: {}", retries, message);
            try {
                channel.basicPublish(RabbitMQHeaders.X_DLE.getHeaderKey(), "dead-routing-key", null, delivery.getBody());
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            } catch (Exception ex) {
                log.error(e.getMessage());
            }
        } else {
            AMQP.BasicProperties retryProps = new AMQP.BasicProperties.Builder()
                    .headers(Map.of(RabbitMQHeaders.X_RETRIES.getHeaderKey(), retries + 1)) // Increment retry count
                    .build();

            channel.basicPublish(readFromApplicationPropertiesStringFormat("queue.dlx.exchange"),
                    delivery.getEnvelope().getRoutingKey(), retryProps, delivery.getBody());
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            log.info(" [x] Retrying message: {} Retry # {}", message, (retries + 1));
        }
    }

}
