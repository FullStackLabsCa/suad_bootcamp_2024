package io.reactivestax.utility;

import io.reactivestax.types.dto.Trade;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesIntegerFormat;

public class Utility {

    public static AtomicInteger roundRobinIndex = new AtomicInteger(0);

    public static int getNumberOfQueues() {
        try {
            return readFromApplicationPropertiesIntegerFormat("queue.count");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int roundRobin() {
        return roundRobinIndex.incrementAndGet() % getNumberOfQueues() + 1;
    }

    public static int random() {
        return ThreadLocalRandom.current().nextInt(1, getNumberOfQueues() + 1);
    }

    public static boolean checkValidity(String[] split) {
        return (split[0] != null && split[1] != null && split[2] != null && split[3] != null && split[4] != null && split[5] != null);
    }

    public static Trade prepareTrade(String payload) {
        String[] payloads = payload.split(",");

        return Trade.builder()
                .tradeIdentifier(payloads[0])
                .tradeDateTime(payloads[1])
                .accountNumber(payloads[2])
                .cusip(payloads[3])
                .direction(payloads[4])
                .quantity(Integer.parseInt(payloads[5]))
                .price(Double.parseDouble(payloads[6]))
                .position(Integer.parseInt(payloads[5]))
                .build();
    }

}
