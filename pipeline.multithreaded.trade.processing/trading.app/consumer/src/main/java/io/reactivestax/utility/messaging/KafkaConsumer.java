package io.reactivestax.utility.messaging;

import io.reactivestax.service.TradeProcessorService;
import io.reactivestax.types.contract.QueueLoader;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.sql.SQLException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@Slf4j
public class KafkaConsumer implements QueueLoader {

    private final Consumer<String, String> consumer;
    private volatile static KafkaConsumer instance;

    public static KafkaConsumer getInstance() {
        if (instance == null) {
            synchronized (KafkaConsumer.class) {
                if (instance == null) {
                    instance = new KafkaConsumer();
                }
                return instance;
            }
        }
        return instance;
    }

    private KafkaConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "trade-group");  // Use unique group id for each consumer
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        this.consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<>(props);
        this.consumer.subscribe(Collections.singleton("trading-topic"));
    }

    @Override
    public void consumeMessage(String queueName) throws SQLException {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    String message = record.value();
                    try {
                        TradeProcessorService.getInstance().processTrade(message); // Process the trade
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    log.info("Consumed from {} | partition {} | offset {} | key= {} | value= {}",
                            record.topic(), record.partition(), record.offset(), record.key(), message);
                }
                consumer.commitSync();
            }
    }

}
