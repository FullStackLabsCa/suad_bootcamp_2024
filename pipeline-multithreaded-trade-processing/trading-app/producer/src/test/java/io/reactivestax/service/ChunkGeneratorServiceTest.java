package io.reactivestax.service;

import io.reactivestax.utility.ApplicationPropertiesUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesStringFormat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ChunkGeneratorServiceTest {

    @Mock
    FileReader fileReader;

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