package io.reactivestax.repository.hibernate;

import io.reactivestax.repository.hibernate.entity.Position;
import io.reactivestax.repository.hibernate.entity.SecuritiesReference;
import io.reactivestax.repository.hibernate.entity.TradePayload;
import io.reactivestax.types.contract.repository.PositionRepository;
import io.reactivestax.types.dto.Trade;
import io.reactivestax.utility.database.HibernateUtil;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    @InjectMocks
    HibernateTradePositionRepository hibernateTradePositionRepository;

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
             LogCaptor logCaptor = LogCaptor.forClass(HibernateTradePositionRepository.class)) {

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
    void testUpsertPosition() {
        HibernateUtil.setConfigResource("hibernate-h2.cfg.xml");
        String payload = "TDB_00000003,2024-09-19 22:16:18,TDB_CUST_5214938,GOOGL,SELL,683,638.02";
        Trade trade = prepareTrade(payload);
        insertCusipData(trade, 1);
        boolean isUpsertPosition = HibernateTradePositionRepository.getInstance().upsertPosition(trade, 1);
        assertTrue(isUpsertPosition);
        cleanUp();
    }


    @Test
    void testGetCusipVersion() {
        HibernateUtil.setConfigResource("hibernate-h2.cfg.xml");
        String payload = "TDB_00000003,2024-09-19 22:16:18,TDB_CUST_5214938,GOOGL,SELL,683,638.02";
        Trade trade = prepareTrade(payload);
        insertCusipData(trade,1);
        Integer cusipVersion = HibernateTradePositionRepository.getInstance().getCusipVersion(trade);
        assertEquals(1, cusipVersion);
        cleanUp();
    }

    void insertCusipData(Trade trade, int version) {
        Session session = HibernateUtil.getInstance().getConnection();
        Transaction transaction = session.beginTransaction();
        Position position = Position.builder()
                .accountNumber(trade.getAccountNumber())
                .cusip(trade.getCusip())
                .direction(trade.getDirection())
                .position(BigInteger.valueOf(trade.getPosition()))
                .version(version)
                .build();
        session.persist(position);
        session.flush();
        transaction.commit();
    }

    public void cleanUp() {
        Session session = HibernateUtil.getInstance().getConnection();
        session.beginTransaction();
        session.createQuery("DELETE FROM Position").executeUpdate();
        session.getTransaction().commit();
    }

}