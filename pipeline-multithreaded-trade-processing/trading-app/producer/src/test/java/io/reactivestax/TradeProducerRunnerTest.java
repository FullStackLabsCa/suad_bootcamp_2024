package io.reactivestax;

import io.reactivestax.service.ChunkGeneratorService;
import io.reactivestax.service.ChunkSubmitterService;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.*;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

//@ExtendWith(MockitoExtension.class)
public class TradeProducerRunnerTest extends TestCase {

    @Mock
    ChunkGeneratorService mockChunkGeneratorService;

    @Mock
    ChunkSubmitterService mockChunkSubmitterService;
    //
    @InjectMocks
    TradeProducerRunner tradeProducerRunner;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testMain() throws Exception {


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