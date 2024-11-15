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

//    @Mock
//    private BeanFactory beanFactory;

    @InjectMocks
    private ChunkSubmitterService chunkSubmitterService = ChunkSubmitterService.getInstance();

    private MockedStatic<ApplicationPropertiesUtils> applicationPropertiesUtilsMock;
    private MockedStatic<BeanFactory> beanFactoryMock;


    @BeforeEach
    public void setUp() throws ExecutionException, InterruptedException {
        MockitoAnnotations.openMocks(this);
        System.out.println("JUnit version is: " + Version.id());

        applicationPropertiesUtilsMock = Mockito.mockStatic(ApplicationPropertiesUtils.class);
        beanFactoryMock = Mockito.mockStatic(BeanFactory.class);

        // Mock ApplicationPropertiesUtils to return a specific thread count
        applicationPropertiesUtilsMock.when(() -> ApplicationPropertiesUtils.readFromApplicationPropertiesIntegerFormat("chunk.processor.thread.count"))
                .thenReturn(2);

        // Set up a mock queue and make BeanFactory return it
        mockQueue = mock(LinkedBlockingQueue.class);
        beanFactoryMock.when(BeanFactory::getChunksFileMappingQueue).thenReturn(mockQueue);
        // Configure the executor service and future to avoid real execution
        when(mockExecutorService.submit(any(Runnable.class))).thenReturn(any());
        when(mockFuture.get()).thenReturn(null);  // Future completes immediately
    }


    @Test
    void testGetInstance() {
        ChunkSubmitterService instance = ChunkSubmitterService.getInstance();
        ChunkSubmitterService instance1 = ChunkSubmitterService.getInstance();
        Assertions.assertEquals(instance, instance1);
    }

    @Test
    void testProcessChunk_SubmitChunksIsCalled() throws Exception {
//        int threadCount = readFromApplicationPropertiesIntegerFormat("chunk.processor.thread.count");
//
//        try (MockedStatic<BeanFactory> mockedBeanFactory = Mockito.mockStatic(BeanFactory.class)) {
//            try (MockedStatic<ChunkProcessorService> mockedChunkProcessorService = Mockito.mockStatic(ChunkProcessorService.class)) {
//                mockedChunkProcessorService.when(ChunkProcessorService::getInstance).thenReturn(chunkProcessorService);
//
////                LinkedBlockingQueue<String> linkedBlockingQueue = new LinkedBlockingQueue<>();
////                linkedBlockingQueue.put(anyString());
////                mockedBeanFactory.when(()-> BeanFactory.getChunksFileMappingQueue().take()).thenReturn(any());
//
//
//                chunkSubmitterService.submitChunks();
//
//                verify(executorService, atLeastOnce()).submit(any(Runnable.class));
////        mockedBeanFactory.verify(BeanFactory.getChunksFileMappingQueue(), atLeastOnce()).getChunksFileMappingQueue()
//                verify(chunkProcessorService, times(threadCount))
//                        .processChunks(anyString());
//            }
//        }


        when(mockQueue.take()).thenReturn("chunkFile1", "chunkFile2");

        // Do nothing when processChunks is called
        doNothing().when(chunkProcessorService).processChunks(anyString());

        // Call the method to test
        chunkSubmitterService.submitChunks();

        // Verify processChunks was called twice (once for each chunk file)
        verify(chunkProcessorService, times(2)).processChunks(anyString());

    }
}