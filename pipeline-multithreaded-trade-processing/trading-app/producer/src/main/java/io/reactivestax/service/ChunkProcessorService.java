package io.reactivestax.service;

import io.reactivestax.types.contract.ChunkProcessor;
import io.reactivestax.types.contract.repository.PayloadRepository;
import io.reactivestax.types.contract.repository.TransactionUtil;
import io.reactivestax.types.dto.Trade;
import io.reactivestax.factory.BeanFactory;
import lombok.extern.slf4j.Slf4j;


import java.io.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesIntegerFormat;
import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesStringFormat;
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
    public void processChunk() throws Exception {
        int chunkProcessorThreadPoolSize = readFromApplicationPropertiesIntegerFormat("chunk.processor.thread.count");
        IntStream.range(0, chunkProcessorThreadPoolSize).forEach(i -> this.submitChunks());
    }


    public void submitChunks() {
        ExecutorService executorService = Executors.
                newFixedThreadPool(Integer.parseInt(readFromApplicationPropertiesStringFormat("chunk.processor.thread.count")));

        executorService.submit(() -> {
            try {
                String chunkFileName = BeanFactory.getChunksFileMappingQueue().take();
                insertTradeIntoTradePayloadTable(chunkFileName);
            } catch (Exception e) {
                log.info("error while insert into trade payloads {}", e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }


    public void insertTradeIntoTradePayloadTable(String filePath) throws Exception {
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
                    throw new RuntimeException(e);
                }
            });
        }
    }

}
