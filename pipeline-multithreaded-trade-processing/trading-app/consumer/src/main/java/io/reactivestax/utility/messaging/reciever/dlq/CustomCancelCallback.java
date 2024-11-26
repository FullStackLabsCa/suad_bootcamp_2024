package io.reactivestax.utility.messaging.reciever.dlq;

import com.rabbitmq.client.CancelCallback;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class CustomCancelCallback implements CancelCallback {

    @Override
    public void handle(String consumerTag) {
        log.info("Consumer {} cancelled." , consumerTag);
    }
}
