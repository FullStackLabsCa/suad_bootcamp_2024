package io.reactivestax.utility;

import io.reactivestax.repository.hibernate.entity.TradePayload;
import io.reactivestax.types.enums.LookUpStatusEnum;
import io.reactivestax.types.enums.PostedStatusEnum;
import io.reactivestax.types.enums.StatusReasonEnum;
import io.reactivestax.types.enums.ValidityStatusEnum;
import io.reactivestax.utility.database.HibernateUtil;
import org.hibernate.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.*;

import static org.junit.Assert.*;

@ExtendWith(MockitoExtension.class)
class HibernateUtilTest {

    @BeforeEach
    void setUp() {
        HibernateUtil.setConfigResource("hibernate-h2.cfg.xml");
    }

    @Test
    void testSessionFactoryAndConnection() {
        HibernateUtil instance = HibernateUtil.getInstance();
        Session session = instance.getConnection();
        Session threadLocalSession = HibernateUtil.getThreadLocalSession().get();
        Assertions.assertNotNull(threadLocalSession, "Thread local session should");
        Assertions.assertNotNull(session, "Session should not be null");
    }


    @Test
    void testSingleInstanceCreation() {
        HibernateUtil instance = HibernateUtil.getInstance();
        HibernateUtil instance1 = HibernateUtil.getInstance();
        Assertions.assertEquals(instance.hashCode(), instance1.hashCode());
    }

    @Test
    void testTransactionCommit() {
        HibernateUtil instance = HibernateUtil.getInstance();
        Session session = instance.getConnection();

        try {
            instance.startTransaction();

            TradePayload tradePayload = TradePayload.builder()
                    .tradeId("1")
                    .validityStatus(ValidityStatusEnum.VALID)
                    .statusReason(StatusReasonEnum.ALL_FIELDS_PRESENT)
                    .lookupStatus(LookUpStatusEnum.FAIL)
                    .jeStatus(PostedStatusEnum.NOT_POSTED)
                    .payload("")
                    .build();

            session.persist(tradePayload);
            instance.commitTransaction();

            Assertions.assertTrue(session.contains("Trade payload should be in session", tradePayload));
            TradePayload retrievedTradePayload = instance.getConnection().createQuery("FROM TradePayload WHERE tradeId = :tradeId", TradePayload.class)
                    .setParameter("tradeId", "1")
                    .uniqueResult();
            Assertions.assertNotNull(retrievedTradePayload);
            Assertions.assertEquals(tradePayload, retrievedTradePayload, "trade payload and retrieved value should be equal");
        } catch (Exception e) {
            instance.rollbackTransaction();
        } finally {
            cleanUp();
        }
    }

    @Test
    void testTransactionRollback() {
        HibernateUtil instance = HibernateUtil.getInstance();
        Session session = instance.getConnection();
        session.getTransaction().begin();
        instance.rollbackTransaction();
        TradePayload retrievedTrade = instance.getConnection().get(TradePayload.class, 0L);
        Assertions.assertNull(retrievedTrade, "not saved trade should return null");
    }


    @Test
    void testCloseConnection() {
        Session session = HibernateUtil.getInstance().getConnection();
        Assertions.assertNotNull(session);
        Assertions.assertTrue(session.isOpen(), "session should be open");
        session.close();
        Assertions.assertFalse(session.isOpen(), "Session should be closed");
        HibernateUtil.getThreadLocalSession().remove();
        Session currentSession = HibernateUtil.getThreadLocalSession().get();
        Assertions.assertNull(currentSession, "Thread Local should no longer hold the session ");
    }

    @Test
    void testThreadLocalSessionIsolation() throws InterruptedException {

        ConcurrentHashMap<String, Session> sessionsByThread = new ConcurrentHashMap<>();
        CountDownLatch latch = new CountDownLatch(1);
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        //Runnable that each threads will execute
        Runnable task = () -> {
            try {
                latch.await();
                HibernateUtil instance = HibernateUtil.getInstance();
                Session session = instance.getConnection();
                Assertions.assertNotNull(session);
                sessionsByThread.put(Thread.currentThread().getName(), session);

            } catch (InterruptedException e) {
                Thread.currentThread().isInterrupted();
            }
        };

        //Start multiple threads
        for (int i = 0; i < 10; i++) {
            executorService.submit(task);
        }


        //Release the latch to start all threads
        latch.countDown();

        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

        for (Session session1 : sessionsByThread.values()) {
            for (Session session2 : sessionsByThread.values()) {
                if (session1 != session2) {
                    Assertions.assertNotSame(session1, session2);
                }
            }
        }
    }

    void cleanUp() {
        Session session = HibernateUtil.getInstance().getConnection();
        session.beginTransaction();
        session.createQuery("DELETE FROM TradePayload").executeUpdate();
        session.getTransaction().commit();
    }


}