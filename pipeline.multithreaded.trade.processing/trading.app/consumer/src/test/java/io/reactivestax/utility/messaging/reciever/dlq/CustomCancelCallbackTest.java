package io.reactivestax.utility.messaging.reciever.dlq;

import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


class CustomCancelCallbackTest {

    @Test
    void testHandle() {
        LogCaptor logCaptor = LogCaptor.forClass(CustomCancelCallback.class);
        new CustomCancelCallback().handle("testConsumerTag");
        assertTrue(logCaptor.getInfoLogs()
                .stream()
                .anyMatch(log -> log.contains("Consumer testConsumerTag cancelled."))
        );
    }
}