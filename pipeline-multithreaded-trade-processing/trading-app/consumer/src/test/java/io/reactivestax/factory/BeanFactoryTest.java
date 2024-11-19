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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesStringFormat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BeanFactoryTest {

    @Test
    public void shouldReturnMQInstance() {
        QueueLoader instance = RabbitMQLoader.getInstance();
        QueueLoader messageSenderInstance = BeanFactory.getQueueSetUp();
        Assertions.assertEquals(instance, messageSenderInstance, "shouldReturnMQInstance");
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
    void whenTechnologyIsUnknownShouldThrowException() {
        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("persistence.technology")).thenReturn("oracle");
            assertThrows(InvalidPersistenceTechnologyException.class, BeanFactory::getQueueSetUp);
        }
    }


    @Test
    void shouldReturnHibernateTradeRepositoryInstance() {
        HibernateTradePayloadRepository instance = HibernateTradePayloadRepository.getInstance();
        PayloadRepository hibernateInstance = BeanFactory.getTradePayloadRepository();
        Assertions.assertEquals(instance, hibernateInstance, "shouldReturnHibernateInstance");
    }

    @Test
    void whenTechnologyIsJdbcShouldReturnJdbcInstance() {
        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("persistence.technology")).thenReturn("jdbc");
            JDBCTradePayloadRepository instance = JDBCTradePayloadRepository.getInstance();
            PayloadRepository tradePayloadRepository = BeanFactory.getTradePayloadRepository();
            Assertions.assertEquals(instance, tradePayloadRepository, "shouldReturnJDBCInstance");
        }
    }


    @Test
    void whenTechnologyIsUnknownInTradePayloadRepositoryShouldThrowInvalidPersistenceException() {
        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("persistence.technology")).thenReturn("oracle");
            assertThrows(InvalidPersistenceTechnologyException.class, BeanFactory::getTradePayloadRepository);
        }
    }

    @Test
     void shouldReturnHibernateTransactionUtilInstance() {
        HibernateUtil instance = HibernateUtil.getInstance();
        TransactionUtil hibernateUtilInstance = BeanFactory.getTransactionUtil();
        Assertions.assertEquals(instance, hibernateUtilInstance, "shouldReturnHibernateInstance");
    }

    @Test
     void shouldReturnJdbcTransactionUtilInstance() {
        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("persistence.technology")).thenReturn("jdbc");
            DBUtils instance = DBUtils.getInstance();
            TransactionUtil dbUtils = BeanFactory.getTransactionUtil();
            Assertions.assertEquals(dbUtils, instance, "shouldReturnJdbcInstance");
        }
    }

    @Test
     void whenTechnologyIsUnknownInTransactionUtilShouldThrowInvalidPersistenceException() {
        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("persistence.technology")).thenReturn("oracle");
            assertThrows(InvalidPersistenceTechnologyException.class, BeanFactory::getTransactionUtil);
        }
    }

    @Test
     void shouldAddPassFileNameToQueue() {
        BeanFactory.setChunksFileMappingQueue("trade_chunks_1");
        assertFalse(BeanFactory.getChunksFileMappingQueue().isEmpty());
    }

    @Test
     void shouldReturnHibernateSecuritiesLookUpRepositoryInstance() {
        HibernateSecuritiesReferenceRepository instance = HibernateSecuritiesReferenceRepository.getInstance();
        SecuritiesReferenceRepository hibernateInstance = BeanFactory.getLookupSecuritiesRepository();
        Assertions.assertEquals(instance, hibernateInstance, "shouldReturnHibernateInstance");
    }

    @Test
     void whenTechnologyIsJdbcShouldReturnJdbcSecuritiesReferenceInstance() {
        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("persistence.technology")).thenReturn("jdbc");
            JDBCSecuritiesReferenceRepository instance = JDBCSecuritiesReferenceRepository.getInstance();
            SecuritiesReferenceRepository lookUpSecurityRepository = BeanFactory.getLookupSecuritiesRepository();
            Assertions.assertEquals(instance, lookUpSecurityRepository, "shouldReturnJDBCInstance");
        }
    }


    @Test
     void whenTechnologyIsUnknownInLookUSecuritiesShouldThrowInvalidPersistenceException() {
        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("persistence.technology")).thenReturn("oracle");
            assertThrows(InvalidPersistenceTechnologyException.class, BeanFactory::getLookupSecuritiesRepository);
        }
    }


    @Test
     void shouldReturnHibernateJournalEntryRepositoryInstance() {
        HibernateJournalEntryRepository instance = HibernateJournalEntryRepository.getInstance();
        JournalEntryRepository hibernateInstance = BeanFactory.getJournalEntryRepository();
        Assertions.assertEquals(instance, hibernateInstance, "shouldReturnHibernateInstance");
    }

    @Test
     void whenTechnologyIsJdbcShouldReturnJournalEntryRepositoryInstance() {
        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("persistence.technology")).thenReturn("jdbc");
            JDBCJournalEntryRepository instance = JDBCJournalEntryRepository.getInstance();
            JournalEntryRepository lookUpSecurityRepository = BeanFactory.getJournalEntryRepository();
            Assertions.assertEquals(instance, lookUpSecurityRepository, "shouldReturnJDBCInstance");
        }
    }


    @Test
     void whenTechnologyIsUnknownInJournalEntryShouldThrowInvalidPersistenceException() {
        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("persistence.technology")).thenReturn("oracle");
            assertThrows(InvalidPersistenceTechnologyException.class, BeanFactory::getJournalEntryRepository);
        }
    }

    @Test
     void shouldReturnHibernatePositionRepositoryInstance() {
        HibernateTradePositionRepository instance = HibernateTradePositionRepository.getInstance();
        PositionRepository hibernateInstance = BeanFactory.getPositionsRepository();
        Assertions.assertEquals(instance, hibernateInstance, "shouldReturnHibernateInstance");
    }

    @Test
     void whenTechnologyIsJdbcShouldReturnJdbcPositionInstance() {
        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("persistence.technology")).thenReturn("jdbc");
            JDBCTradePositionRepository instance = JDBCTradePositionRepository.getInstance();
            PositionRepository lookUpSecurityRepository = BeanFactory.getPositionsRepository();
            Assertions.assertEquals(instance, lookUpSecurityRepository, "shouldReturnJDBCInstance");
        }
    }


    @Test
     void whenTechnologyIsUnknownInTradePositionShouldThrowInvalidPersistenceException() {
        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("persistence.technology")).thenReturn("oracle");
            assertThrows(InvalidPersistenceTechnologyException.class, BeanFactory::getPositionsRepository);
        }
    }


}
