package io.reactivestax.service;

import io.reactivestax.task.FileTradeProcessor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Slf4j
public class ConsumerSubmitterService {

    private ConsumerSubmitterService() {
    }

    private static ConsumerSubmitterService instance;

    public static synchronized ConsumerSubmitterService getInstance() {
        if (instance == null) {
            instance = new ConsumerSubmitterService();
        }
        return instance;
    }

    public void startConsumer(ExecutorService executorService, String queueName) {
        FileTradeProcessor consumerTask = new FileTradeProcessor(queueName);
        executorService.submit(consumerTask);
        addShutdownHook(executorService);
    }


    private void addShutdownHook(ExecutorService executorService) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutdown signal received. Stopping consumer...");
            executorService.shutdownNow();
            log.info("Consumer stopped.");
        }));
    }
}
