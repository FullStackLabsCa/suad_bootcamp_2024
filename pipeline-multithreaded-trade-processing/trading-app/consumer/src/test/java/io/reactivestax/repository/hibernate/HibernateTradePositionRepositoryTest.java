package io.reactivestax.repository.hibernate;

import io.reactivestax.repository.hibernate.entity.Position;
import io.reactivestax.types.dto.Trade;
import io.reactivestax.utility.database.HibernateUtil;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaPredicate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import jakarta.persistence.criteria.CriteriaQuery;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import nl.altindag.log.LogCaptor;

import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigInteger;
import java.sql.SQLException;

import static io.reactivestax.utility.Utility.prepareTrade;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class HibernateTradePositionRepositoryTest {
    @Mock
    Session mockSession;

    @Mock
    HibernateUtil mockHibernateUtil;

    @Mock
    Transaction mockTransaction;

    @Captor
    ArgumentCaptor<Position> argumentCaptor;

    @Mock
    private CriteriaBuilder mockCriteriaBuilder;

    @Mock
    private CriteriaQuery<Position> mockCriteriaQuery;

    @Mock
    private Root<Position> mockRoot;

    @Mock
    private Predicate mockPredicate;

    private Trade mockTrade;
    private Position mockPosition;

//    @Mock
//    private Query<Position> mockQuery;




    @Test
    void getInstance() {
        HibernateTradePositionRepository instance = HibernateTradePositionRepository.getInstance();
        HibernateTradePositionRepository instance1 = HibernateTradePositionRepository.getInstance();
        assertEquals(instance1, instance);
    }

    @Test
    void testInsertPosition() throws SQLException {
        try (MockedStatic<HibernateUtil> hibernateUtilMockedStatic = Mockito.mockStatic(HibernateUtil.class)) {
            hibernateUtilMockedStatic.when(HibernateUtil::getInstance).thenReturn(mockHibernateUtil);
            Mockito.when(mockHibernateUtil.getConnection()).thenReturn(mockSession);
            when(mockSession.beginTransaction()).thenReturn(mockTransaction);

            String payload = "TDB_00000000,2024-09-19 22:16:18,TDB_CUST_5214938,V,SELL,683,638.02";
            Trade trade = prepareTrade(payload);
            // Mocking the behavior of session

            boolean isPositionInserted = HibernateTradePositionRepository.getInstance().insertPosition(trade);


            verify(mockSession, atLeastOnce()).persist(argumentCaptor.capture());
            Position capturedPosition = argumentCaptor.getValue();

            assertEquals(trade.getAccountNumber(), capturedPosition.getAccountNumber());
            assertEquals(trade.getCusip(), capturedPosition.getCusip());
            assertEquals(BigInteger.valueOf(trade.getPosition()), capturedPosition.getPosition());
            assertEquals(trade.getDirection(), capturedPosition.getDirection());
            verify(mockTransaction, times(1)).commit();
            assertTrue(isPositionInserted);
        }

    }

    @Test
    void testInsertPosition_Exception() throws SQLException {
        try (MockedStatic<HibernateUtil> hibernateUtilMockedStatic = Mockito.mockStatic(HibernateUtil.class);
             LogCaptor logCaptor = LogCaptor.forClass(HibernateTradePositionRepository.class);) {

            String payload = "TDB_00000000,2024-09-19 22:16:18,TDB_CUST_5214938,V,SELL,683,638.02";
            Trade trade = prepareTrade(payload);

            hibernateUtilMockedStatic.when(HibernateUtil::getInstance).thenReturn(mockHibernateUtil);
            Mockito.when(mockHibernateUtil.getConnection()).thenReturn(mockSession);
            when(mockSession.beginTransaction()).thenReturn(mockTransaction);
            doThrow(new RuntimeException("Transaction Failed")).when(mockTransaction).commit();

            boolean isPositionInserted = HibernateTradePositionRepository.getInstance().insertPosition(trade);
            verify(mockTransaction, times(1)).rollback();
            assertFalse(isPositionInserted);
            assertTrue(logCaptor.getErrorLogs().contains("Transaction Failed"));
        }
    }

    @Test
    void testUpdatePosition() {
        try (MockedStatic<HibernateUtil> hibernateUtilMockedStatic = Mockito.mockStatic(HibernateUtil.class)) {
            hibernateUtilMockedStatic.when(HibernateUtil::getInstance).thenReturn(mockHibernateUtil);


            String payload = "TDB_00000000,2024-09-19 22:16:18,TDB_CUST_5214938,V,SELL,683,638.02";
            Trade trade = prepareTrade(payload);

//            mockTrade = new Trade("12345", "CUSIP123", 10, "BUY");
//            mockPosition = new Position("12345", "CUSIP123", BigInteger.valueOf(100), "BUY", 1);

            HibernateCriteriaBuilder mockCriteriaBuilder = mock(HibernateCriteriaBuilder.class);
            CriteriaQuery<Position> mockCriteriaQuery = mock(CriteriaQuery.class);
            Root<Position> mockRoot = mock(Root.class);
            Predicate mockPredicate = mock(Predicate.class);
            Query<Position> mockQuery = mock(Query.class);


            // Setup mocks for session behavior

            when(mockSession.beginTransaction()).thenReturn(mockTransaction);
//            when(mockSession.getCriteriaBuilder()).thenReturn(mock(mockCriteriaBuilder));

//            when(mockCriteriaBuilder.createQuery(Position.class)).thenReturn((JpaCriteriaQuery<Position>) mockCriteriaQuery);
//            when(mockCriteriaQuery.from(Position.class)).thenReturn(mockRoot);

//            when(mockCriteriaBuilder.equal(mockRoot.get("accountNumber"), trade.getAccountNumber()))
//                    .thenReturn((JpaPredicate) mockPredicate);
//            when(mockCriteriaBuilder.equal(mockRoot.get("cusip"), trade.getCusip()))
//                    .thenReturn((JpaPredicate) mockPredicate);
//            when(mockCriteriaBuilder.equal(mockRoot.get("version"), 1))
//                    .thenReturn((JpaPredicate) mockPredicate);
            when(mockCriteriaQuery.select(mockRoot)).thenReturn(mockCriteriaQuery);
            when(mockSession.createQuery(mockCriteriaQuery)).thenReturn(mockQuery);
            when(mockQuery.uniqueResult()).thenReturn(mockPosition);
            when(mockSession.beginTransaction()).thenReturn(mockTransaction);


            HibernateTradePositionRepository.getInstance().updatePosition(trade, 1);

            verify(mockSession, atLeastOnce()).beginTransaction();
            verify(mockSession, atLeastOnce()).persist(any());
            verify(mockTransaction,atLeastOnce()).commit();

        }
    }

    @Test
    void testGetCusipVersion() {
        try(MockedStatic<HibernateUtil> hibernateUtilMockedStatic = mockStatic(HibernateUtil.class)){
            hibernateUtilMockedStatic.when(HibernateUtil::getInstance).thenReturn(mockSession);
//            trade.setAccountNumber("12345");
//            trade.setCusip("AAPL");

            // Mock the session and criteria builder behavior
//            when(mockSession.getCriteriaBuilder()).thenReturn(mockCriteriaBuilder);
//            when(mockCriteriaBuilder.createQuery(Integer.class)).thenReturn(mockCriteriaQuery);
//            when(mockCriteriaQuery.from(Position.class)).thenReturn(mockRoot);
//            when(mockCriteriaQuery.select(mockRoot.get("version"))).thenReturn(mockCriteriaQuery);
//            when(mockCriteriaBuilder.equal(mockRoot.get("accountNumber"), trade.getAccountNumber()))
//                    .thenReturn(mockAccountNumberPredicate);
//            when(mockCriteriaBuilder.equal(mockRoot.get("cusip"), trade.getCusip()))
//                    .thenReturn(mockCusipPredicate);
//            when(mockCriteriaBuilder.and(mockAccountNumberPredicate, mockCusipPredicate))
//                    .thenReturn(mockAccountNumberPredicate); // Combined predicate
//            when(mockCriteriaQuery.where(mockAccountNumberPredicate)).thenReturn(mockCriteriaQuery);
//            when(mockSession.createQuery(mockCriteriaQuery)).thenReturn(() -> 1); // Mock query result
//
//            TradeService tradeService = new TradeService();
//            tradeService.setSession(mockSession);
//
//            Integer version = tradeService.getCusipVersion(trade);
//
//            assertNotNull(version);
//            assertEquals(1, version);

        }
    }
}