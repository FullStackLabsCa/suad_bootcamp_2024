package io.reactivestax;

import io.reactivestax.service.ConsumerSubmitterService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesIntegerFormat;
import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesStringFormat;


@Slf4j
public class TradeConsumerRunner {

    public static void main(String[] args) throws Exception {
        startConsumer();
    }

    private static void startConsumer() throws IOException {
        log.info("Starting in Consumer Mode...");
        ExecutorService executorService = Executors.newFixedThreadPool(readFromApplicationPropertiesIntegerFormat("trade.processor.thread.poolSize"));

        IntStream.range(0, readFromApplicationPropertiesIntegerFormat("queue.count")).forEach(i ->
                ConsumerSubmitterService.startConsumer(executorService, readFromApplicationPropertiesStringFormat("queue.name") + i)
        );
    }
}



