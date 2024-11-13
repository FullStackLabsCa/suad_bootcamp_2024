package io.reactivestax.service;

import io.reactivestax.factory.BeanFactory;
import io.reactivestax.types.contract.repository.PayloadRepository;
import io.reactivestax.types.contract.repository.TransactionUtil;
import io.reactivestax.types.dto.Trade;
import io.reactivestax.utility.ApplicationPropertiesUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.BlockingQueue;

import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesIntegerFormat;
import static io.reactivestax.utility.Utility.prepareTrade;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChunkProcessorServiceTest {


    @Mock
    BeanFactory beanFactory;

    @Mock
    PayloadRepository payloadRepository;

    @Mock
    TransactionUtil transactionUtil;

    @Mock
    ApplicationPropertiesUtils applicationPropertiesUtils;

    @Mock
    private BlockingQueue<String> mockQueue;

    @Spy
    private MessagePublisherService messagePublisherService;


    @InjectMocks
    @Spy
    private ChunkProcessorService chunkProcessorService;

//    @BeforeEach
    public void setUp() {
        MockedStatic<BeanFactory> mockedBeanFactory = Mockito.mockStatic(BeanFactory.class);
        mockedBeanFactory.when(BeanFactory::getTradePayloadRepository).thenReturn(payloadRepository);
        mockedBeanFactory.when(BeanFactory::getTransactionUtil).thenReturn(transactionUtil);

        //Since it is singleton we have to access through the mockstatic
        MockedStatic<MessagePublisherService> mockedPublisherService = Mockito.mockStatic(MessagePublisherService.class);
        mockedPublisherService.when(MessagePublisherService::getInstance).thenReturn(messagePublisherService);
    }

    @Test
    void testInstance() {
        ChunkProcessorService instance = ChunkProcessorService.getInstance();
        ChunkProcessorService instance1 = ChunkProcessorService.getInstance();
        assertEquals(instance, instance1);
    }


    @Test
    void testProcessChunk_SubmitChunksIsCalled() throws Exception {
        int threadCount = ApplicationPropertiesUtils.readFromApplicationPropertiesIntegerFormat("chunk.processor.thread.count");
        chunkProcessorService.processChunk();
        verify(chunkProcessorService, times(threadCount))
                .submitChunks();
    }

//    @Test
//    void testSubmitChunk_Exception() {
//        doThrow(Exception.class).when(chunkProcessorService).submitChunks();
//        assertThrows(RuntimeException.class, () -> chunkProcessorService.processChunk());
//    }

    @Test
    void testInsertTradeIntoTradePayloadTable() throws Exception {
       //setup
        String line = "TDB_00000000,2024-09-19 22:16:18,TDB_CUST_5214938,V,SELL,683,638.02";
        Trade trade = prepareTrade(line);

        MockedStatic<BeanFactory> mockedBeanFactory = Mockito.mockStatic(BeanFactory.class);
        mockedBeanFactory.when(BeanFactory::getTradePayloadRepository).thenReturn(payloadRepository);
        mockedBeanFactory.when(BeanFactory::getTransactionUtil).thenReturn(transactionUtil);

        //Since it is singleton we have to access through the mockstatic
        MockedStatic<MessagePublisherService> mockedPublisherService = Mockito.mockStatic(MessagePublisherService.class);
        mockedPublisherService.when(MessagePublisherService::getInstance).thenReturn(messagePublisherService);


        //ACT

        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesIntegerFormat("chunk.processor.thread.count")).thenReturn(1);
            String testFilePath = "/Users/Suraj.Adhikari/sources/student-mode-programs/suad-bootcamp-2024/pipeline-multithreaded-trade-processing/trading-app/producer/src/main/resources/files/" + "test_trades_chunk.csv";
            chunkProcessorService.insertTradeIntoTradePayloadTable(testFilePath);
            //Assert

            verify(transactionUtil, atLeastOnce()).startTransaction();
            verify(payloadRepository, atLeastOnce()).insertTradeIntoTradePayloadTable(anyString());
            verify(transactionUtil, atLeastOnce()).commitTransaction();
            verify(messagePublisherService, atLeastOnce()).figureTheNextQueue(trade);

        }
    }


    @AfterEach
    void tearDown() {
    }

    @Test
    void processChunkTest() {

    }

    @Test
    void insertTradeIntoTradePayloadTable() throws Exception {
//        verify(chunkProcessorService.processChunk(), times(10)).
    }
}