package io.reactivestax;


import io.reactivestax.service.ChunkGeneratorService;
import io.reactivestax.service.ChunkSubmitterService;
import lombok.extern.slf4j.Slf4j;


import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesIntegerFormat;
import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesStringFormat;

@Slf4j
public class TradeProducerRunner {

    public static void main(String[] args) throws Exception {
        startProducer();
    }

    private static void startProducer() throws Exception {
        log.info("Starting in Producer Mode...");

        ChunkGeneratorService chunkGeneratorService = ChunkGeneratorService.getInstance();
        chunkGeneratorService.generateAndSubmitChunks(readFromApplicationPropertiesStringFormat("trade.file.path"),
                        readFromApplicationPropertiesIntegerFormat("chunks.count"));

        //process chunks
        ChunkSubmitterService.getInstance().submitChunks();
    }
}


