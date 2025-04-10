package io.reactivestax.task;

import io.reactivestax.types.contract.QueueLoader;
import lombok.Getter;

import java.util.concurrent.Callable;

import static io.reactivestax.factory.BeanFactory.getQueueSetUp;


public class FileTradeProcessor implements Callable<Void> {

    @Getter
    String queueName;

    public FileTradeProcessor(String queueName) {
        this.queueName = queueName;
    }

    @Override
    public Void call() throws Exception {
        QueueLoader queueLoader = getQueueSetUp();
        assert queueLoader != null;
        queueLoader.consumeMessage(queueName);
        return null;
    }
}
