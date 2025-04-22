package io.reactivestax.types.contract;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface MessageSender {
    Boolean sendMessageToQueueOrTopic(String queueOrTopicName, String message, String accountNumber) throws IOException, TimeoutException, InterruptedException;
}