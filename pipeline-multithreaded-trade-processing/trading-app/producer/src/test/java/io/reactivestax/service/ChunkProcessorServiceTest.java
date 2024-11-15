package io.reactivestax.service;

import io.reactivestax.factory.BeanFactory;
import io.reactivestax.types.contract.repository.PayloadRepository;
import io.reactivestax.types.contract.repository.TransactionUtil;
import io.reactivestax.types.dto.Trade;

import lombok.val;
import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InOrder;

import java.io.BufferedReader;

import static io.reactivestax.utility.Utility.prepareTrade;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

import org.junit.contrib.java.lang.system.SystemOutRule;

@ExtendWith(MockitoExtension.class)
class ChunkProcessorServiceTest {


    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Mock
    PayloadRepository payloadRepository;

    @Mock
    BufferedReader bufferedReader;

    @Mock
    TransactionUtil transactionUtil;

    @Mock
    private MessagePublisherService messagePublisherService;


    @Spy
    @InjectMocks
    private ChunkProcessorService chunkProcessorService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInstance() {
        ChunkProcessorService instance = ChunkProcessorService.getInstance();
        ChunkProcessorService instance1 = ChunkProcessorService.getInstance();
        Assertions.assertEquals(instance, instance1);
    }

//    @Test
//    void testSubmitChunk_Exception() throws Exception {
//        // Mock ExecutorService and simulate the exception
//        String testFilePath = "/Users/Suraj.Adhikari/sources/student-mode-programs/suad-bootcamp-2024/pipeline-multithreaded-trade-processing/trading-app/producer/src/main/resources/files/" + "test_trades_chunk.csv";
//        LinkedBlockingQueue<String> linkedBlockingQueue = new LinkedBlockingQueue<>();
//        linkedBlockingQueue.put(testFilePath);
//
//        try (MockedStatic<BeanFactory> mockedBeanFactory = Mockito.mockStatic(BeanFactory.class)) {
//            mockedBeanFactory.when(BeanFactory::getChunksFileMappingQueue).thenReturn(linkedBlockingQueue);
//            ExecutorService executorService = Executors.
//                    newSingleThreadExecutor();
//

    /// /            doThrow(Exception.class).when(chunkProcessorService).insertTradeIntoTradePayloadTable(anyString());
//
//            CountDownLatch latch = new CountDownLatch(1);
//
//            doAnswer(invocation ->{
//                throw new Exception("Simulated Exception");
//            }).when(ChunkSubmitterService.getInstance()).insertTradeIntoTradePayloadTable(anyString());
//
//            assertThrows(RuntimeException.class, () -> ChunkSubmitterService.getInstance().submitChunks());
//
//            latch.await();
//        }
//    }
    @Test
    void testProcessChunks() throws Exception {
        //setup
        String line = "TDB_00000000,2024-09-19 22:16:18,TDB_CUST_5214938,V,SELL,683,638.02";
        Trade trade = prepareTrade(line);
        String testFilePath = "/Users/Suraj.Adhikari/sources/student-mode-programs/suad-bootcamp-2024/pipeline-multithreaded-trade-processing/trading-app/producer/src/main/resources/files/" + "test_trades_chunk.csv";
        try (MockedStatic<BeanFactory> mockedBeanFactory = Mockito.mockStatic(BeanFactory.class);
             MockedStatic<MessagePublisherService> mockedPublisherService = Mockito.mockStatic(MessagePublisherService.class)) {
            mockedBeanFactory.when(BeanFactory::getTradePayloadRepository).thenReturn(payloadRepository);
            mockedBeanFactory.when(BeanFactory::getTransactionUtil).thenReturn(transactionUtil);

            //Since it is singleton and getInstance() is static method we have to access through the mockstatic
            mockedPublisherService.when(MessagePublisherService::getInstance).thenReturn(messagePublisherService);

            //ACT
            chunkProcessorService.processChunks(testFilePath);

            //Assert
            verify(transactionUtil, atLeastOnce()).startTransaction();
            verify(payloadRepository, atLeastOnce()).insertTradeIntoTradePayloadTable(anyString());
            verify(transactionUtil, atLeastOnce()).commitTransaction();
            verify(messagePublisherService, atLeastOnce()).figureTheNextQueue(trade);

            //verifying the order of the execution
            InOrder inOrder = inOrder(transactionUtil, payloadRepository, transactionUtil, messagePublisherService);
            inOrder.verify(transactionUtil).startTransaction();
            inOrder.verify(payloadRepository).insertTradeIntoTradePayloadTable(anyString());
            inOrder.verify(transactionUtil).commitTransaction();
            inOrder.verify(messagePublisherService).figureTheNextQueue(any());

        }
    }


    @Test
    void testProcessChunks_Exception() throws Exception {
        //setup
        String testFilePath = "/Users/Suraj.Adhikari/sources/student-mode-programs/suad-bootcamp-2024/pipeline-multithreaded-trade-processing/trading-app/producer/src/main/resources/files/" + "test_trades_chunk.csv";

//            String testFilePath = FileLoaderUtil.getInstance().loadFileFromResources("test_trades_chunk.csv");
        try (MockedStatic<BeanFactory> mockedBeanFactory = Mockito.mockStatic(BeanFactory.class);
             MockedStatic<MessagePublisherService> mockedPublisherService = Mockito.mockStatic(MessagePublisherService.class)
        ) {
            mockedBeanFactory.when(BeanFactory::getTradePayloadRepository).thenReturn(payloadRepository);
            mockedBeanFactory.when(BeanFactory::getTransactionUtil).thenReturn(transactionUtil);

            mockedPublisherService.when(MessagePublisherService::getInstance).thenReturn(messagePublisherService);

            doThrow(new RuntimeException("Simulated Exception")).when(payloadRepository).insertTradeIntoTradePayloadTable(Mockito.anyString());

            assertThrows(RuntimeException.class, () -> chunkProcessorService.processChunks(testFilePath));
        }
    }
}