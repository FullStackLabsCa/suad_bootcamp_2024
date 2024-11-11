package io.reactivestax.service;

import io.reactivestax.types.dto.Trade;
import io.reactivestax.utility.Utility;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import static io.reactivestax.factory.BeanFactory.getQueueMessageSender;
import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesStringFormat;
import static io.reactivestax.utility.Utility.roundRobin;

@Slf4j
public class MessagePublisherService {

    private MessagePublisherService() {
    }

    // Get the queue number, or assign one in a round-robin or random manner based on application-properties
    public static void figureTheNextQueue(Trade trade) throws IOException {

        ConcurrentHashMap<String, Integer> queueDistributorMap = new ConcurrentHashMap<>();
        String distributionCriteria = readFromApplicationPropertiesStringFormat("trade.distribution.criteria");
        boolean useMap = Boolean.parseBoolean(readFromApplicationPropertiesStringFormat("trade.distribution.use.map"));
        String distributionAlgorithm = readFromApplicationPropertiesStringFormat("trade.distribution.algorithm");

        IntSupplier queueNumberSupplier = () -> {
            if ("round-robin".equals(distributionAlgorithm)) return roundRobin();
            else if ("random".equals(distributionAlgorithm)) return Utility.random();
            return 0;
        };

        Supplier<String> distribution = () -> {
            if ("accountNumber".equals(distributionCriteria)) return trade.getAccountNumber();
            return trade.getTradeIdentifier();
        };

        int partitionNumber = useMap ? queueDistributorMap.computeIfAbsent(distribution.get(),
                k -> queueNumberSupplier.getAsInt()) : queueNumberSupplier.getAsInt();

        String queueName = readFromApplicationPropertiesStringFormat("queue.name") + (partitionNumber - 1);

        Optional.ofNullable(getQueueMessageSender())
                .ifPresent(messageSender -> {
                    try {
                        messageSender.sendMessageToQueue(queueName, trade.getTradeIdentifier());
                    } catch (IOException | TimeoutException | InterruptedException e) {
                        log.info("Assigned trade ID {} to queue {} {}", trade.getTradeIdentifier(), trade.getTradeIdentifier(), partitionNumber);
                        throw new RuntimeException(e.getMessage());
                    }
                });
    }

}
