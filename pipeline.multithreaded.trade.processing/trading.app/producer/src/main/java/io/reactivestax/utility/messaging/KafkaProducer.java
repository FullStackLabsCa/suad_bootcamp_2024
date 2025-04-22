package io.reactivestax.utility.messaging;

import io.reactivestax.types.contract.MessageSender;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

@Slf4j
public class KafkaProducer implements MessageSender {

    private final Producer<String, String> producer;

    private volatile static KafkaProducer instance;

    public static KafkaProducer getInstance() {
        if (instance == null) {
            synchronized (KafkaProducer.class) {
                if (instance == null) {
                    instance = new KafkaProducer();
                }
            }
        }
        return instance;
    }


    private KafkaProducer() {
        // Configure Kafka Producer properties
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.ACKS_CONFIG, "all");

        // Create Kafka Producer
        this.producer = new org.apache.kafka.clients.producer.KafkaProducer<>(props);
    }


    @Override
    public Boolean sendMessageToQueueOrTopic(String topic, String message, String accountNumber) {
        ProducerRecord<String, String> record = new ProducerRecord<>("trading-topic",accountNumber, message);
        Future<RecordMetadata> future = producer.send(record, (metadata, exception) -> {
            if (exception == null) {
                log.info("Sent to {} | partition= {} | offset= {}",
                        metadata.topic(), metadata.partition(), metadata.offset());
            } else {
                log.error("Error sending message: {} ", exception.getMessage());
            }
        });
        return true;
    }
}
