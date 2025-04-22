package io.reactivestax.utility.messaging;

import io.reactivestax.types.contract.MessageSender;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesStringFormat;


@Slf4j
public class RabbitMQMessageSender implements MessageSender {

    private static RabbitMQMessageSender instance;

    public static synchronized RabbitMQMessageSender getInstance() {
        if (instance == null) {
            instance = new RabbitMQMessageSender();
        }
        return instance;
    }

    @Override
    public Boolean sendMessageToQueueOrTopic(String queueOrTopicName, String message, String accountNumber) {
        try {
            RabbitMQUtils.getRabbitMQChannel().basicPublish(
                    readFromApplicationPropertiesStringFormat("queue.exchange.name"),
                    queueOrTopicName,
                    null,
                    message.getBytes(StandardCharsets.UTF_8)
            );
            log.info(" [x] Sent  {}  with routing key {} ", message, queueOrTopicName);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

}
