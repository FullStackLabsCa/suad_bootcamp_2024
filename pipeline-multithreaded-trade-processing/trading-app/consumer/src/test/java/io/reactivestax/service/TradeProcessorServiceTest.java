package io.reactivestax.service;

import io.reactivestax.factory.BeanFactory;
import io.reactivestax.types.contract.QueueLoader;
import io.reactivestax.types.contract.repository.JournalEntryRepository;
import io.reactivestax.types.contract.repository.PayloadRepository;
import io.reactivestax.types.contract.repository.PositionRepository;
import io.reactivestax.types.contract.repository.SecuritiesReferenceRepository;
import io.reactivestax.types.dto.Trade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Optional;
import java.util.stream.Stream;

import static io.reactivestax.utility.Utility.prepareTrade;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TradeProcessorServiceTest {


    @Mock
    PayloadRepository payloadRepository;


    @Mock
    SecuritiesReferenceRepository securitiesReferenceRepository;

    @Mock
    JournalEntryRepository journalEntryRepository;

    @Mock
    PositionRepository positionRepository;

    @Mock
    QueueLoader queueLoader;

    @InjectMocks
    @Spy
    TradeProcessorService tradeProcessorService;

    @Test
    void checkSingleTon() {
        TradeProcessorService instance = TradeProcessorService.getInstance();
        TradeProcessorService instance1 = TradeProcessorService.getInstance();
        assertEquals(instance, instance1);
    }


//    @Test
    void testProcessTrade() throws Exception {
        final String queueName = "queue0";
        try (MockedStatic<BeanFactory> beanFactoryMockedStatic = Mockito.mockStatic(BeanFactory.class)) {
            beanFactoryMockedStatic.when(BeanFactory::getQueueSetUp).thenReturn(queueLoader);
            tradeProcessorService.processTrade(queueName);
            verify(queueLoader, atLeastOnce()).consumeMessage(queueName);
        }
    }


    @Test
    void testProcessJournalWithPosition() throws Exception {
        final String testTradeId = "TDB-000-ABC";
        final String payload = "TDB_00005000,2024-09-22 16:18:29,TDB_CUST_1763690,FB,SELL,706,617.9";
        Trade trade = prepareTrade(payload);

        try (MockedStatic<BeanFactory> beanFactoryMockedStatic = Mockito.mockStatic(BeanFactory.class)) {
            beanFactoryMockedStatic.when(BeanFactory::getTradePayloadRepository).thenReturn(payloadRepository);
            beanFactoryMockedStatic.when(BeanFactory::getLookupSecuritiesRepository).thenReturn(securitiesReferenceRepository);
            beanFactoryMockedStatic.when(BeanFactory::getJournalEntryRepository).thenReturn(journalEntryRepository);
            when(payloadRepository.readTradePayloadByTradeId(testTradeId)).thenReturn(Optional.of(payload));
            when(securitiesReferenceRepository.lookUpSecurities("FB")).thenReturn(true);
            doNothing().when(tradeProcessorService).executePositionTransaction(trade);
            //act
            tradeProcessorService.processTrade(testTradeId);
            verify(journalEntryRepository, atLeastOnce()).saveJournalEntry(any());
            verify(payloadRepository, atLeastOnce()).updateJournalStatus(testTradeId);
            verify(payloadRepository, atLeastOnce()).updateJournalStatus(testTradeId);
        }
    }


    @Test
    void testProcessJournalWithPositionWithInvalidCUSIP() throws Exception {
        final String testTradeId = "TDB-000-ABC";
        final String payload = "TDB_00005000,2024-09-22 16:18:29,TDB_CUST_1763690,FB,SELL,706,617.9";
        Trade trade = prepareTrade(payload);

        try (MockedStatic<BeanFactory> beanFactoryMockedStatic = Mockito.mockStatic(BeanFactory.class)) {
            beanFactoryMockedStatic.when(BeanFactory::getTradePayloadRepository).thenReturn(payloadRepository);
            beanFactoryMockedStatic.when(BeanFactory::getLookupSecuritiesRepository).thenReturn(securitiesReferenceRepository);
            beanFactoryMockedStatic.when(BeanFactory::getJournalEntryRepository).thenReturn(journalEntryRepository);
            when(payloadRepository.readTradePayloadByTradeId(testTradeId)).thenReturn(Optional.of(payload));
            when(securitiesReferenceRepository.lookUpSecurities("V")).thenReturn(false);
            doNothing().when(tradeProcessorService).executePositionTransaction(trade);
            //act
            assertThrows(RuntimeException.class, () -> tradeProcessorService.processTrade(testTradeId));
        }
    }

    @Test
    void testProcessJournalWithPosition_SQLException() throws Exception {
        final String testTradeId = "TDB-000-ABC";
        final String payload = "TDB_00005000,2024-09-22 16:18:29,TDB_CUST_1763690,FB,SELL,706,617.9";

        try (MockedStatic<BeanFactory> beanFactoryMockedStatic = Mockito.mockStatic(BeanFactory.class)) {
            beanFactoryMockedStatic.when(BeanFactory::getTradePayloadRepository).thenReturn(payloadRepository);
            beanFactoryMockedStatic.when(BeanFactory::getLookupSecuritiesRepository).thenReturn(securitiesReferenceRepository);
            beanFactoryMockedStatic.when(BeanFactory::getJournalEntryRepository).thenReturn(journalEntryRepository);
            when(payloadRepository.readTradePayloadByTradeId(testTradeId)).thenReturn(Optional.of(payload));
            when(securitiesReferenceRepository.lookUpSecurities("FB")).thenReturn(true);
            doThrow(new SQLException("Simulated Exception")).when(payloadRepository).updateLookUpStatus(Mockito.anyString());
            assertThrows(RuntimeException.class, () -> tradeProcessorService.processTrade(testTradeId));
        }
    }

    @ParameterizedTest
    @MethodSource("positionExecutionProvider")
    void testExecutePositionTransaction(Integer cusip, int upsertCount, int insertCount) throws SQLException {
        final String payload = "TDB_00005000,2024-09-22 16:18:29,TDB_CUST_1763690,FB,SELL,706,617.9";
        Trade trade = prepareTrade(payload);
        try (MockedStatic<BeanFactory> beanFactoryMockedStatic = Mockito.mockStatic(BeanFactory.class)) {
            beanFactoryMockedStatic.when(BeanFactory::getPositionsRepository).thenReturn(positionRepository);
            when(positionRepository.getCusipVersion(any())).thenReturn(cusip);
            tradeProcessorService.executePositionTransaction(trade);
            verify(positionRepository, times(upsertCount)).upsertPosition(trade, 1);
            verify(positionRepository, times(insertCount)).insertPosition(trade);
        }
    }

    private static Stream<Arguments> positionExecutionProvider() {
        return Stream.of(
                Arguments.of(1, 1, 0),
                Arguments.of(null, 0, 1));
    }
}