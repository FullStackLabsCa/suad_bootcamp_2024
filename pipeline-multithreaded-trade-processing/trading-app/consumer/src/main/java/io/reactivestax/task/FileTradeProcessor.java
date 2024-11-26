package io.reactivestax.task;

import io.reactivestax.service.TradeProcessorService;
import lombok.Getter;

import java.util.concurrent.Callable;


public class FileTradeProcessor implements Callable<Void> {

    @Getter
    String queueName;

    public FileTradeProcessor(String queueName) {
        this.queueName = queueName;
    }

    @Override
    public Void call() throws Exception {
        TradeProcessorService.getInstance().processTrade(queueName);
        return null;
    }
}
