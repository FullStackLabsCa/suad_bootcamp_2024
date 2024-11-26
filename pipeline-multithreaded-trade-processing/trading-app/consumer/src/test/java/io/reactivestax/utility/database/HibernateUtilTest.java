package io.reactivestax.utility.database;

import io.reactivestax.repository.hibernate.entity.TradePayload;
import io.reactivestax.types.enums.LookUpStatusEnum;
import io.reactivestax.types.enums.PostedStatusEnum;
import io.reactivestax.types.enums.ValidityStatusEnum;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

import static junit.framework.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.*;


public class HibernateUtilTest {

    @BeforeAll
    public static void setUp() {
        HibernateUtil.setConfigResource("hibernate-h2.cfg.xml");
    }

    @Test
    public void testSessionFactoryAndConnection() {
        HibernateUtil instance = HibernateUtil.getInstance();
        Session session = instance.getConnection();
        Session threadLocalSession = HibernateUtil.getThreadLocalSession().get();
        assertNotNull(threadLocalSession, "Thread local session should");
        assertNotNull(session, "Session should not be null");
    }


    @Test
    public void testSingleInstanceCreation() {
        HibernateUtil instance = HibernateUtil.getInstance();
        HibernateUtil instance1 = HibernateUtil.getInstance();
        assertEquals(instance.hashCode(), instance1.hashCode());
    }

    @Test
    public void testTransactionCommit() {
        HibernateUtil instance = HibernateUtil.getInstance();
        Session session = instance.getConnection();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            TradePayload tradePayload = new TradePayload();
            tradePayload.setTradeId("1");
            tradePayload.setValidityStatus(String.valueOf(ValidityStatusEnum.VALID));
            tradePayload.setStatusReason("All field present ");
            tradePayload.setLookupStatus(String.valueOf(LookUpStatusEnum.FAIL));
            tradePayload.setJeStatus(String.valueOf(PostedStatusEnum.NOT_POSTED));
            tradePayload.setPayload("");
            session.persist(tradePayload);
            transaction.commit();
            Assertions.assertTrue(session.contains("Trade payload should be in session", tradePayload));
            TradePayload retrievedTradePayload = session.createQuery("FROM TradePayload WHERE tradeId = :tradeId", TradePayload.class)
                    .setParameter("tradeId", "1")
                    .uniqueResult();
            assertNotNull(retrievedTradePayload);
            assertEquals(tradePayload, retrievedTradePayload, "trade payload and retrieved value should be equal");
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            cleanUp();
        }
    }

    @Test
     void testTransactionRollback() {
        HibernateUtil instance = HibernateUtil.getInstance();
        Session session = instance.getConnection();
        session.getTransaction().begin();
        TradePayload tradePayload = new TradePayload();
        session.getTransaction().rollback();
        TradePayload retrievedTrade = session.get(TradePayload.class, 0L);
        assertNull("not saved trade should return null", retrievedTrade);
    }


    @Test
     void testCloseConnection() {
        Session session = HibernateUtil.getInstance().getConnection();
        assertNotNull(session);
        Assertions.assertTrue(session.isOpen(), "session should be open");
        session.close();
        Assertions.assertFalse(session.isOpen(), "Session should be closed");
        HibernateUtil.getThreadLocalSession().remove();
        Session currentSession = HibernateUtil.getThreadLocalSession().get();
        assertNull("Thread Local should no longer hold the session ", currentSession);
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
                assertNotNull(session);
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
                    assertNotSame(session1, session2);
                }
            }
        }

    }


    public void cleanUp() {
        Session session = HibernateUtil.getInstance().getConnection();
        session.beginTransaction();
        session.createQuery("DELETE FROM TradePayload").executeUpdate();
        session.getTransaction().commit();
    }
}