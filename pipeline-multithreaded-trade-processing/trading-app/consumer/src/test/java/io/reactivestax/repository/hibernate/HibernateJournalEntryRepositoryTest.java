package io.reactivestax.repository.hibernate;

import io.reactivestax.repository.hibernate.entity.JournalEntries;
import io.reactivestax.types.dto.Trade;
import io.reactivestax.utility.database.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;


import static io.reactivestax.utility.Utility.prepareTrade;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HibernateJournalEntryRepositoryTest {

    @Mock
    HibernateUtil hibernateUtil;

    @Mock
    Transaction transaction;

    @Mock
    Session session;

    @Captor
    ArgumentCaptor<JournalEntries> journalEntriesArgumentCaptor;

    @InjectMocks
    @Spy
    HibernateJournalEntryRepository hibernateJournalEntryRepository;


    @Test
    void getInstance() {
        HibernateJournalEntryRepository instance = HibernateJournalEntryRepository.getInstance();
        HibernateJournalEntryRepository instance1 = HibernateJournalEntryRepository.getInstance();
        assertEquals(instance, instance1);
    }

    @Test
    void saveJournalEntry() {

        try (MockedStatic<HibernateUtil> hibernateUtilMockedStatic = Mockito.mockStatic(HibernateUtil.class)) {
            hibernateUtilMockedStatic.when(HibernateUtil::getInstance).thenReturn(hibernateUtil);
            when(hibernateUtil.getConnection()).thenReturn(session);

            String payload = "TDB_00000000,2024-09-19 22:16:18,TDB_CUST_5214938,V,SELL,683,638.02";
            Trade trade = prepareTrade(payload);

            // Mocking the behavior of session
            Mockito.doNothing().when(session).persist(any(JournalEntries.class));
            when(session.beginTransaction()).thenReturn(transaction);

            // Act
            HibernateJournalEntryRepository.getInstance().saveJournalEntry(trade);

            // Assert
            verify(session, times(1)).persist(journalEntriesArgumentCaptor.capture()); // Capture the persisted object
            JournalEntries capturedJournalEntries = journalEntriesArgumentCaptor.getValue();

            // Verify TradePayload fields
            assertEquals(trade.getTradeIdentifier(), capturedJournalEntries.getTradeId());
            assertEquals(trade.getTradeDateTime(), capturedJournalEntries.getTradeDate());
            assertEquals(trade.getAccountNumber(), capturedJournalEntries.getAccountNumber());
            assertEquals(trade.getCusip(), capturedJournalEntries.getCusip());
            assertEquals(trade.getDirection(), capturedJournalEntries.getDirection());
            assertEquals(trade.getQuantity(), capturedJournalEntries.getQuantity());
            assertEquals(trade.getPrice(), capturedJournalEntries.getPrice());
            verify(transaction, atLeastOnce()).commit();

        }
    }

    @Test
    void testJournalEntry_Exception() {

        try (MockedStatic<HibernateUtil> hibernateUtilMockedStatic = Mockito.mockStatic(HibernateUtil.class)) {
            hibernateUtilMockedStatic.when(HibernateUtil::getInstance).thenReturn(hibernateUtil);
            when(hibernateUtil.getConnection()).thenReturn(session);
            when(session.beginTransaction()).thenReturn(transaction);
            doThrow(new RuntimeException("Transaction Failed")).when(transaction).commit();


            String payload = "TDB_00000000,2024-09-19 22:16:18,TDB_CUST_5214938,V,SELL,683,638.02";
            Trade trade = prepareTrade(payload);
            HibernateJournalEntryRepository.getInstance().saveJournalEntry(trade);

            verify(transaction, times(1)).rollback();
        }
    }

}