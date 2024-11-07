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

    public static Trade prepareTrade(String payload) {
        String[] payloads = payload.split(",");
        return new Trade(payloads[0],
                payloads[1],
                payloads[2],
                payloads[3],
                payloads[4],
                Integer.parseInt(payloads[5]),
                Double.parseDouble(payloads[6]),
                Integer.parseInt(payloads[5]));
    }


}
