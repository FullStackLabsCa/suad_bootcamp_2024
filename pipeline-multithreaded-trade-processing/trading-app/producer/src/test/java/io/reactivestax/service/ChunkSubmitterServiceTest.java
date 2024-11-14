package io.reactivestax.service;

import io.reactivestax.factory.BeanFactory;
import io.reactivestax.utility.ApplicationPropertiesUtils;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import junit.runner.Version;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesIntegerFormat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ChunkSubmitterServiceTest {

    @Mock
    private ExecutorService executorService;

    @Spy
    private ChunkProcessorService chunkProcessorService;

    @InjectMocks
    private ChunkSubmitterService chunkSubmitterService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        System.out.println("JUnit version is: " + Version.id());
    }


    @Test
    void testGetInstance() {
        ChunkSubmitterService instance = ChunkSubmitterService.getInstance();
        ChunkSubmitterService instance1 = ChunkSubmitterService.getInstance();
        Assertions.assertEquals(instance, instance1);
    }

    @Test
    void testProcessChunk_SubmitChunksIsCalled() throws Exception {
        int threadCount = readFromApplicationPropertiesIntegerFormat("chunk.processor.thread.count");
        LinkedBlockingQueue<String> linkedBlockingQueue = new LinkedBlockingQueue<>();
        linkedBlockingQueue.put(anyString());
        try (MockedStatic<BeanFactory> mockedBeanFactory = Mockito.mockStatic(BeanFactory.class)) {
            try (MockedStatic<ChunkProcessorService> mockedChunkProcessorService = Mockito.mockStatic(ChunkProcessorService.class)) {
                mockedChunkProcessorService.when(ChunkProcessorService::getInstance).thenReturn(chunkProcessorService);
                mockedBeanFactory.when(BeanFactory::getChunksFileMappingQueue).thenReturn(linkedBlockingQueue);

                chunkSubmitterService.submitChunks();

                verify(executorService, atLeastOnce()).submit(any(Runnable.class));
//        mockedBeanFactory.verify(BeanFactory.getChunksFileMappingQueue(), atLeastOnce()).getChunksFileMappingQueue()
                verify(chunkProcessorService, times(threadCount))
                        .processChunks(anyString());
            }
        }

    }
}