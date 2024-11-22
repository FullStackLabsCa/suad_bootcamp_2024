package io.reactivestax;

import io.reactivestax.service.ChunkGeneratorService;
import io.reactivestax.service.ChunkSubmitterService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TradeProducerRunnerTest {

    @Mock
    ChunkGeneratorService mockChunkGeneratorService;

    @Mock
    ChunkSubmitterService mockChunkSubmitterService;
    //
    @InjectMocks
    TradeProducerRunner tradeProducerRunner;


    @Test
    void testMain() throws Exception {


        try (MockedStatic<ChunkGeneratorService> mockChunkGenerator = Mockito.mockStatic(ChunkGeneratorService.class);
             MockedStatic<ChunkSubmitterService> mockChunkSubmitter = Mockito.mockStatic(ChunkSubmitterService.class)) {

            mockChunkGenerator.when(ChunkGeneratorService::getInstance).thenReturn(mockChunkGeneratorService);
            mockChunkSubmitter.when(ChunkSubmitterService::getInstance).thenReturn(mockChunkSubmitterService);

            Mockito.when(ChunkGeneratorService.getInstance()).thenReturn(mockChunkGeneratorService);
            Mockito.when(ChunkSubmitterService.getInstance()).thenReturn(mockChunkSubmitterService);
            tradeProducerRunner.main(new String[]{});
            verify(mockChunkGeneratorService).generateAndSubmitChunks(anyString(), anyInt());
            verify(mockChunkSubmitterService).submitChunks();
        }
    }
}