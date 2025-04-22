package io.reactivestax.types.contract;

import org.apache.kafka.common.TopicPartition;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

public interface QueueLoader {
    void consumeMessage(String queueName) throws IOException, TimeoutException, SQLException;
}
