package io.reactivestax.service;

import io.reactivestax.factory.BeanFactory;
import io.reactivestax.utility.ApplicationPropertiesUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import junit.runner.Version;

import java.util.concurrent.*;

import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesIntegerFormat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ChunkSubmitterServiceTest {

    @Mock
    private ExecutorService mockExecutorService;
    @Mock
    private ChunkProcessorService chunkProcessorService;

    @Mock
    private Future<?> mockFuture;


    private LinkedBlockingQueue<String> mockQueue;


//    @InjectMocks
//    private ChunkSubmitterService chunkSubmitterService = ChunkSubmitterService.getInstance();

//
//    @BeforeEach
//    public void setUp() throws ExecutionException, InterruptedException {
//        MockitoAnnotations.openMocks(this);
//        System.out.println("JUnit version is: " + Version.id());
//
//        try (applicationPropertiesUtilsMock =Mockito.mockStatic(ApplicationPropertiesUtils.class)
//        beanFactoryMock = Mockito.mockStatic(BeanFactory.class)){
//
//            // Mock ApplicationPropertiesUtils to return a specific thread count
//            applicationPropertiesUtilsMock.when(() -> ApplicationPropertiesUtils.readFromApplicationPropertiesIntegerFormat("chunk.processor.thread.count"))
//                    .thenReturn(2);
//
//            // Set up a mock queue and make BeanFactory return it
//            mockQueue = mock(LinkedBlockingQueue.class);
//            beanFactoryMock.when(BeanFactory::getChunksFileMappingQueue).thenReturn(mockQueue);
//            // Configure the executor service and future to avoid real execution
//            when(mockExecutorService.submit(any(Runnable.class))).thenReturn(any());
//            when(mockFuture.get()).thenReturn(null);  // Future completes immediately
//        }
//    }

    @Test
    void testGetInstance() {
        ChunkSubmitterService instance = ChunkSubmitterService.getInstance();
        ChunkSubmitterService instance1 = ChunkSubmitterService.getInstance();
        Assertions.assertEquals(instance, instance1);
    }

    @Test
    void testProcessChunk_SubmitChunksIsCalled() throws Exception {
        int threadCount = readFromApplicationPropertiesIntegerFormat("chunk.processor.thread.count");
        String chunksPath = "/Users/Suraj.Adhikari/sources/student-mode-programs/suad-bootcamp-2024/pipeline-multithreaded-trade-processing/trading-app/producer/src/main/resources/files/" + "trades_chunk_1.csv";

        try (MockedStatic<BeanFactory> mockedBeanFactory = Mockito.mockStatic(BeanFactory.class);
             MockedStatic<ApplicationPropertiesUtils> mockedPropertiesUtils = Mockito.mockStatic(ApplicationPropertiesUtils.class);
             MockedStatic<ChunkProcessorService> mockedChunkProcessorService = Mockito.mockStatic(ChunkProcessorService.class)) {
            mockedChunkProcessorService.when(ChunkProcessorService::getInstance).thenReturn(chunkProcessorService);

            when(mockExecutorService.submit(any(Runnable.class))).thenReturn(mock(Future.class));

            LinkedBlockingQueue<String> linkedBlockingQueue = new LinkedBlockingQueue<>();
            linkedBlockingQueue.put(chunksPath);
            mockedBeanFactory.when(BeanFactory::getChunksFileMappingQueue).thenReturn(linkedBlockingQueue);
            when(linkedBlockingQueue.take()).thenReturn(chunksPath);
            // Do nothing when processChunks is called
            doNothing().when(chunkProcessorService).processChunks(chunksPath);
            ChunkSubmitterService.getInstance().submitChunks();

            verify(chunkProcessorService, times(threadCount))
                    .processChunks(anyString());

        }
    }
}