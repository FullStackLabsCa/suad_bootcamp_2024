package io.reactivestax.service;

import io.reactivestax.factory.BeanFactory;
import io.reactivestax.types.contract.ChunkProcessor;
import io.reactivestax.types.contract.repository.PayloadRepository;
import io.reactivestax.types.contract.repository.TransactionUtil;
import io.reactivestax.types.dto.Trade;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;

import static io.reactivestax.utility.Utility.prepareTrade;

@Slf4j
public class ChunkProcessorService implements ChunkProcessor {


    private static ChunkProcessorService instance;


    public static ChunkProcessorService getInstance() {
        if (instance == null) {
            instance = new ChunkProcessorService();
        }
        return instance;
    }

    @Override
    public void processChunks(String filePath) throws Exception {
        PayloadRepository tradePayloadRepository = BeanFactory.getTradePayloadRepository();
        TransactionUtil transactionUtil = BeanFactory.getTransactionUtil();
        MessagePublisherService messagePublisherService = MessagePublisherService.getInstance();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.
                    lines().
                    skip(1). //skipping the header
                    forEach(line -> {
                try {
                    Trade trade = prepareTrade(line);
                    transactionUtil.startTransaction();
                    tradePayloadRepository.insertTradeIntoTradePayloadTable(line);
                    transactionUtil.commitTransaction();
                    messagePublisherService.figureTheNextQueue(trade);
                } catch (Exception e) {
                    log.error("error while processing the trade");
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
