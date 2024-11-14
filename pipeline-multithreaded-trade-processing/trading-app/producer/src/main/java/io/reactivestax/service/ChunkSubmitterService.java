package io.reactivestax.service;

import io.reactivestax.types.contract.ChunkProcessor;
import io.reactivestax.types.contract.repository.PayloadRepository;
import io.reactivestax.types.contract.repository.TransactionUtil;
import io.reactivestax.types.dto.Trade;
import io.reactivestax.factory.BeanFactory;
import lombok.extern.slf4j.Slf4j;


import java.io.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;

import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesIntegerFormat;

@Slf4j
public class ChunkSubmitterService {


    private static ChunkSubmitterService instance;


    public static ChunkSubmitterService getInstance() {
        if (instance == null) {
            instance = new ChunkSubmitterService();
        }
        return instance;
    }

    public void submitChunks() throws Exception {
        int chunkProcessorThreadPoolSize = readFromApplicationPropertiesIntegerFormat("chunk.processor.thread.count");
        ChunkProcessorService chunkProcessorService = ChunkProcessorService.getInstance();
        ExecutorService executorService = Executors.newFixedThreadPool(chunkProcessorThreadPoolSize);

        IntStream.range(0, chunkProcessorThreadPoolSize).forEach(i -> executorService.submit(() -> {
            try {
                String chunkFileName = BeanFactory.getChunksFileMappingQueue().take();
                chunkProcessorService.processChunks(chunkFileName);
            } catch (Exception e) {
                log.info("error while insert into trade payloads {}", e.getMessage());
                throw new RuntimeException(e);
            }
        }));
    }

}
