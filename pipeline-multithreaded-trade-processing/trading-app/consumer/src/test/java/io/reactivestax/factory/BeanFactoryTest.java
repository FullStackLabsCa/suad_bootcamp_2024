package io.reactivestax.factory;

import io.reactivestax.repository.hibernate.HibernateJournalEntryRepository;
import io.reactivestax.repository.hibernate.HibernateSecuritiesReferenceRepository;
import io.reactivestax.repository.hibernate.HibernateTradePayloadRepository;
import io.reactivestax.repository.hibernate.HibernateTradePositionRepository;
import io.reactivestax.repository.jdbc.JDBCJournalEntryRepository;
import io.reactivestax.repository.jdbc.JDBCSecuritiesReferenceRepository;
import io.reactivestax.repository.jdbc.JDBCTradePayloadRepository;
import io.reactivestax.repository.jdbc.JDBCTradePositionRepository;
import io.reactivestax.types.contract.QueueLoader;
import io.reactivestax.types.contract.repository.*;
import io.reactivestax.types.exception.InvalidPersistenceTechnologyException;
import io.reactivestax.utility.ApplicationPropertiesUtils;
import io.reactivestax.utility.database.DBUtils;
import io.reactivestax.utility.database.HibernateUtil;
import io.reactivestax.utility.messaging.RabbitMQLoader;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesStringFormat;
import static junit.framework.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BeanFactoryTest {

    @Test
    public void shouldReturnMQInstance() {
        QueueLoader instance = RabbitMQLoader.getInstance();
        QueueLoader messageSenderInstance = BeanFactory.getQueueSetUp();
        assertEquals("shouldReturnMQInstance", instance, messageSenderInstance);
    }

//    @Test
//    public void shouldReturnNullInstanceForInMemoryQueue() {
//        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
//            mocked.when(() -> readFromApplicationPropertiesStringFormat("messaging.technology")).thenReturn("inmemory");
//            MessageSender messageSenderInstance = BeanFactory.getQueueMessageSender();
//            assertNull(messageSenderInstance);
//        }
//    }

    @Test
    public void whenTechnologyIsUnknownShouldThrowException() {
        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("persistence.technology")).thenReturn("oracle");
            assertThrows(InvalidPersistenceTechnologyException.class, BeanFactory::getQueueSetUp);
        }
    }


    @Test
    public void shouldReturnHibernateTradeRepositoryInstance()  {
        HibernateTradePayloadRepository instance = HibernateTradePayloadRepository.getInstance();
        PayloadRepository hibernateInstance = BeanFactory.getTradePayloadRepository();
        assertEquals("shouldReturnHibernateInstance", instance, hibernateInstance);
    }

    @Test
    public void whenTechnologyIsJdbcShouldReturnJdbcInstance() {
        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("persistence.technology")).thenReturn("jdbc");
            JDBCTradePayloadRepository instance = JDBCTradePayloadRepository.getInstance();
            PayloadRepository tradePayloadRepository = BeanFactory.getTradePayloadRepository();
            assertEquals("shouldReturnJDBCInstance", instance, tradePayloadRepository);
        }
    }


    @Test
    public void whenTechnologyIsUnknownInTradePayloadRepositoryShouldThrowInvalidPersistenceException() {
        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("persistence.technology")).thenReturn("oracle");
            assertThrows(InvalidPersistenceTechnologyException.class, BeanFactory::getTradePayloadRepository);
        }
    }

    @Test
    public void shouldReturnHibernateTransactionUtilInstance() {
        HibernateUtil instance = HibernateUtil.getInstance();
        TransactionUtil hibernateUtilInstance = BeanFactory.getTransactionUtil();
        assertEquals("shouldReturnHibernateInstance", instance, hibernateUtilInstance);
    }

    @Test
    public void shouldReturnJdbcTransactionUtilInstance() {
        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("persistence.technology")).thenReturn("jdbc");
            DBUtils instance = DBUtils.getInstance();
            TransactionUtil dbUtils = BeanFactory.getTransactionUtil();
            assertEquals("shouldReturnJdbcInstance", dbUtils, instance);
        }
    }

    @Test
    public void whenTechnologyIsUnknownInTransactionUtilShouldThrowInvalidPersistenceException() {
        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("persistence.technology")).thenReturn("oracle");
            assertThrows(InvalidPersistenceTechnologyException.class, BeanFactory::getTransactionUtil);
        }
    }

    @Test
    public void shouldAddPassFileNameToQueue() {
        BeanFactory.setChunksFileMappingQueue("trade_chunks_1");
        assertFalse(BeanFactory.getChunksFileMappingQueue().isEmpty());
    }

    @Test
    public void shouldReturnHibernateSecuritiesLookUpRepositoryInstance()  {
        HibernateSecuritiesReferenceRepository instance = HibernateSecuritiesReferenceRepository.getInstance();
        SecuritiesReferenceRepository hibernateInstance = BeanFactory.getLookupSecuritiesRepository();
        assertEquals("shouldReturnHibernateInstance", instance, hibernateInstance);
    }

    @Test
    public void whenTechnologyIsJdbcShouldReturnJdbcSecuritiesReferenceInstance() {
        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("persistence.technology")).thenReturn("jdbc");
            JDBCSecuritiesReferenceRepository instance = JDBCSecuritiesReferenceRepository.getInstance();
            SecuritiesReferenceRepository lookUpSecurityRepository = BeanFactory.getLookupSecuritiesRepository();
            assertEquals("shouldReturnJDBCInstance", instance, lookUpSecurityRepository);
        }
    }


    @Test
    public void whenTechnologyIsUnknownInLookUSecuritiesShouldThrowInvalidPersistenceException() {
        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("persistence.technology")).thenReturn("oracle");
            assertThrows(InvalidPersistenceTechnologyException.class, BeanFactory::getLookupSecuritiesRepository);
        }
    }


    @Test
    public void shouldReturnHibernateJournalEntryRepositoryInstance()  {
        HibernateJournalEntryRepository instance = HibernateJournalEntryRepository.getInstance();
        JournalEntryRepository hibernateInstance = BeanFactory.getJournalEntryRepository();
        assertEquals("shouldReturnHibernateInstance", instance, hibernateInstance);
    }

    @Test
    public void whenTechnologyIsJdbcShouldReturnJournalEntryRepositoryInstance() {
        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("persistence.technology")).thenReturn("jdbc");
            JDBCJournalEntryRepository  instance = JDBCJournalEntryRepository.getInstance();
            JournalEntryRepository lookUpSecurityRepository = BeanFactory.getJournalEntryRepository();
            assertEquals("shouldReturnJDBCInstance", instance, lookUpSecurityRepository);
        }
    }


    @Test
    public void whenTechnologyIsUnknownInJournalEntryShouldThrowInvalidPersistenceException() {
        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("persistence.technology")).thenReturn("oracle");
            assertThrows(InvalidPersistenceTechnologyException.class, BeanFactory::getJournalEntryRepository);
        }
    }

    @Test
    public void shouldReturnHibernatePositionRepositoryInstance()  {
        HibernateTradePositionRepository instance = HibernateTradePositionRepository.getInstance();
        PositionRepository hibernateInstance = BeanFactory.getPositionsRepository();
        assertEquals("shouldReturnHibernateInstance", instance, hibernateInstance);
    }

    @Test
    public void whenTechnologyIsJdbcShouldReturnJdbcPositionInstance() {
        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("persistence.technology")).thenReturn("jdbc");
            JDBCTradePositionRepository  instance = JDBCTradePositionRepository.getInstance();
            PositionRepository lookUpSecurityRepository = BeanFactory.getPositionsRepository();
            assertEquals("shouldReturnJDBCInstance", instance, lookUpSecurityRepository);
        }
    }


    @Test
    public void whenTechnologyIsUnknownInTradePositionShouldThrowInvalidPersistenceException() {
        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("persistence.technology")).thenReturn("oracle");
            assertThrows(InvalidPersistenceTechnologyException.class, BeanFactory::getPositionsRepository);
        }
    }


}
