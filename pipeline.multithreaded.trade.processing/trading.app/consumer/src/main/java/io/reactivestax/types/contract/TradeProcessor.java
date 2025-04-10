package io.reactivestax.types.contract;

public interface TradeProcessor {
    void processTrade(String queueName) throws Exception;
}
