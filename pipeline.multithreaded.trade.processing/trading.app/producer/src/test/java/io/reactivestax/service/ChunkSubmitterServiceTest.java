package io.reactivestax.service;

import io.reactivestax.factory.BeanFactory;
import io.reactivestax.utility.ApplicationPropertiesUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ChunkSubmitterServiceTest {


    @Mock
    private ChunkProcessorService chunkProcessorService;

    @InjectMocks
    private ChunkSubmitterService chunkSubmitterService;


    @Test
    void testGetInstance() {
        ChunkSubmitterService instance = ChunkSubmitterService.getInstance();
        ChunkSubmitterService instance1 = ChunkSubmitterService.getInstance();
        Assertions.assertEquals(instance, instance1);
    }

    @Test
    void testSubmitChunks() throws Exception {
        String chunksPath = "test_trades_chunk.csv";
        try (MockedStatic<ApplicationPropertiesUtils> mockedPropertiesUtils = Mockito.mockStatic(ApplicationPropertiesUtils.class);
             MockedStatic<ChunkProcessorService> chunkProcessorServiceMockedStatic = Mockito.mockStatic(ChunkProcessorService.class)) {
            mockedPropertiesUtils.when(() -> ApplicationPropertiesUtils.readFromApplicationPropertiesIntegerFormat("chunk.processor.thread.count"))
                    .thenReturn(5);
            chunkProcessorServiceMockedStatic.when(ChunkProcessorService::getInstance).thenReturn(chunkProcessorService);
            Mockito.doNothing().when(chunkProcessorService).processChunks(anyString());
            BeanFactory.setChunksFileMappingQueue(chunksPath);
            chunkSubmitterService.submitChunks();
            verify(chunkProcessorService, atLeastOnce()).processChunks(anyString());
        }
    }
}