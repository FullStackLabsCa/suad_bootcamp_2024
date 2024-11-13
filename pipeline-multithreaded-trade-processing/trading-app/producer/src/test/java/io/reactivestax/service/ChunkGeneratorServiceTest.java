package io.reactivestax.service;

import io.reactivestax.utility.ApplicationPropertiesUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesStringFormat;
import static org.junit.jupiter.api.Assertions.*;

class ChunkGeneratorServiceTest {

    @Test
    void generateAndSubmitChunks() throws IOException {
        String filePath = readFromApplicationPropertiesStringFormat("trade.file.path");
        Integer chunksNumber = ChunkGeneratorService.getInstance().generateAndSubmitChunks(filePath, 5);
        assertEquals(5, chunksNumber);
    }

    @Test
    void generateAndSubmitChunks_IOException() {
        ApplicationPropertiesUtils.setApplicationResource("application-test.properties");
        String filePath = readFromApplicationPropertiesStringFormat("test.trade.file");
        assertThrows(FileNotFoundException.class, () -> {
            ChunkGeneratorService.getInstance().generateAndSubmitChunks(filePath, 2);
        });
    }
}