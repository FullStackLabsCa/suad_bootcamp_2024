package io.reactivestax;


import io.reactivestax.service.ChunkGeneratorService;
import io.reactivestax.service.ChunkProcessorService;
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

        ChunkGeneratorService.getInstance().
                generateAndSubmitChunks(readFromApplicationPropertiesStringFormat("trade.file.path"),
                        readFromApplicationPropertiesIntegerFormat("chunks.count"));

        //process chunks
        ChunkProcessorService.getInstance().processChunk();
    }

}

