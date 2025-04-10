package io.reactivestax.service;

import io.reactivestax.utility.ApplicationPropertiesUtils;
import io.reactivestax.utility.Utility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;

import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesStringFormat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ChunkGeneratorServiceTest {

    @Mock
    InputStream mockInputStream;


    @Test
    void generateAndSubmitChunks() throws IOException {
        String filePath = readFromApplicationPropertiesStringFormat("trade.file.path");
        Integer chunksNumber = ChunkGeneratorService.getInstance().generateAndSubmitChunks(filePath, 5);
        assertEquals(5, chunksNumber);
    }

    @Test
    void generateAndSubmitChunks_IOException() {
        ApplicationPropertiesUtils.setApplicationResource("application-test.properties");
        String filePath = "/simulated";
        try (MockedStatic<Utility> utilityMockedStatic = Mockito.mockStatic(Utility.class)) {
            utilityMockedStatic.when(() -> Utility.getResourceAsStream(ApplicationPropertiesUtils.class, filePath)).thenReturn(mockInputStream);
            assertThrows(FileNotFoundException.class, () -> {
                ChunkGeneratorService.getInstance().generateAndSubmitChunks(filePath, 2);
            });
        }
    }
}