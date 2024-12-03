package io.reactivestax;

import io.reactivestax.service.ChunkGeneratorService;
import io.reactivestax.service.ChunkSubmitterService;
import io.reactivestax.utility.ApplicationPropertiesUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesIntegerFormat;
import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesStringFormat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TradeProducerRunnerTest {

    @Mock
    ChunkGeneratorService mockChunkGeneratorService;

    @Mock
    ChunkSubmitterService mockChunkSubmitterService;


    @Test
    void testMain() throws Exception {
        String tradeFilePath = "/Users/Suraj.Adhikari/Downloads/trades.csv";

        try (MockedStatic<ChunkGeneratorService> mockChunkGenerator = Mockito.mockStatic(ChunkGeneratorService.class);
             MockedStatic<ChunkSubmitterService> mockChunkSubmitter = Mockito.mockStatic(ChunkSubmitterService.class);
             MockedStatic<ApplicationPropertiesUtils> mockStaticApplicationProperties = Mockito.mockStatic(ApplicationPropertiesUtils.class);
        ) {
            mockChunkGenerator.when(ChunkGeneratorService::getInstance).thenReturn(mockChunkGeneratorService);
            mockChunkSubmitter.when(ChunkSubmitterService::getInstance).thenReturn(mockChunkSubmitterService);
            mockStaticApplicationProperties.when(()-> readFromApplicationPropertiesStringFormat("trade.file.path")).thenReturn(tradeFilePath);
            mockStaticApplicationProperties.when(()-> readFromApplicationPropertiesIntegerFormat("chunks.count")).thenReturn(10);

            Mockito.when(ChunkGeneratorService.getInstance()).thenReturn(mockChunkGeneratorService);
            Mockito.when(ChunkSubmitterService.getInstance()).thenReturn(mockChunkSubmitterService);
            TradeProducerRunner.main(new String[]{});
            verify(mockChunkGeneratorService).generateAndSubmitChunks(anyString(), anyInt());
            verify(mockChunkSubmitterService).submitChunks();
        }
    }
}