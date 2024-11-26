package io.reactivestax.service;

import io.reactivestax.utility.ApplicationPropertiesUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesStringFormat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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
//
//    @Test
//    void generateAndSubmitChunks() throws IOException {
//
////            chunkGeneratorServiceMockedStatic.when(ChunkGeneratorService::getInstance).thenReturn(chunkGeneratorService);
//        ChunkGeneratorService chunkGeneratorService = Mockito.spy(ChunkGeneratorService.getInstance());
//        String filePath = readFromApplicationPropertiesStringFormat("trade.file.path");
//        BufferedReader mockReader = mock(BufferedReader.class);
//        when(chunkGeneratorService.createBufferReader(filePath)).thenReturn(mockReader);
//        when(mockReader.readLine())
//                .thenReturn("TDB_00000006,2024-09-23 23:29:47,TDB_CUST_2981923,FB,SELL,132,298.92"); // Simulated lines and EOF
//        Integer chunksNumber = chunkGeneratorService.generateAndSubmitChunks(filePath, 5);
//        assertEquals(5, chunksNumber);
//        verify(mockReader, times(1)).readLine();
//    }

    @Test
    void generateAndSubmitChunks_IOException() {
        ApplicationPropertiesUtils.setApplicationResource("application-test.properties");
        String filePath = readFromApplicationPropertiesStringFormat("test.trade.file");
        assertThrows(FileNotFoundException.class, () -> {
            ChunkGeneratorService.getInstance().generateAndSubmitChunks(filePath, 2);
        });
    }
}