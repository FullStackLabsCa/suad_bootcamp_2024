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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.stream.Stream;

import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesStringFormat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BeanFactoryTest {

    static final String HIBERNATE = "hibernate";
    static final String JDBC = "jdbc";

    @Test
     void shouldReturnMQInstance() {
        QueueLoader instance = RabbitMQLoader.getInstance();
        QueueLoader messageSenderInstance = BeanFactory.getQueueSetUp();
        Assertions.assertEquals(instance, messageSenderInstance, "shouldReturnMQInstance");
    }


    @Test
    void whenTechnologyIsUnknownShouldThrowException() {
        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("persistence.technology")).thenReturn("oracle");
            assertThrows(InvalidPersistenceTechnologyException.class, BeanFactory::getQueueSetUp);
        }
    }


    @ParameterizedTest
    @MethodSource("tradePayloadProvider")
    void whenTechnologyIsJdbcShouldReturnJdbcInstance(String persistenceTechnology, PayloadRepository repositoryInstance) {
        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("persistence.technology")).thenReturn(persistenceTechnology);
            PayloadRepository tradePayloadRepository = BeanFactory.getTradePayloadRepository();
            Assertions.assertEquals(repositoryInstance, tradePayloadRepository);
        }
    }

    private static Stream<Arguments> tradePayloadProvider() {
        return Stream.of(
                Arguments.of(HIBERNATE, HibernateTradePayloadRepository.getInstance()),
                Arguments.of(JDBC, JDBCTradePayloadRepository.getInstance())
        );
    }


    @Test
    void whenTechnologyIsUnknownInTradePayloadRepositoryShouldThrowInvalidPersistenceException() {
        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("persistence.technology")).thenReturn("oracle");
            assertThrows(InvalidPersistenceTechnologyException.class, BeanFactory::getTradePayloadRepository);
        }
    }


    @ParameterizedTest
    @MethodSource("utilProvider")
     void shouldReturnJdbcTransactionUtilInstance(String persistenceTechnology, TransactionUtil transactionUtilInstance) {
        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("persistence.technology")).thenReturn(persistenceTechnology);
            TransactionUtil transactionUtil = BeanFactory.getTransactionUtil();
            Assertions.assertEquals(transactionUtilInstance, transactionUtil, "shouldReturnJdbcInstance");
        }
    }

    private static Stream<Arguments> utilProvider() {
        return Stream.of(
                Arguments.of("hibernate", HibernateUtil.getInstance()),
                Arguments.of("jdbc", DBUtils.getInstance()));
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

    @ParameterizedTest
    @MethodSource("securitiesReferenceProvider")
     void whenTechnologyIsJdbcShouldReturnJdbcSecuritiesReferenceInstance(String persistenceTechnology, SecuritiesReferenceRepository securitiesReferenceInstance) {
        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("persistence.technology")).thenReturn(persistenceTechnology);
            SecuritiesReferenceRepository lookUpSecurityRepository = BeanFactory.getLookupSecuritiesRepository();
            Assertions.assertEquals(securitiesReferenceInstance, lookUpSecurityRepository, "shouldReturnJDBCInstance");
        }
    }

    private static Stream<Arguments> securitiesReferenceProvider() {
        return Stream.of(
                Arguments.of(HIBERNATE, HibernateSecuritiesReferenceRepository.getInstance()),
                Arguments.of(JDBC, JDBCSecuritiesReferenceRepository.getInstance()));
    }



    @Test
     void whenTechnologyIsUnknownInLookUSecuritiesShouldThrowInvalidPersistenceException() {
        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("persistence.technology")).thenReturn("oracle");
            assertThrows(InvalidPersistenceTechnologyException.class, BeanFactory::getLookupSecuritiesRepository);
        }
    }


    @ParameterizedTest
    @MethodSource("journalEntryProvider")
     void whenTechnologyIsJdbcShouldReturnJournalEntryRepositoryInstance(String persistenceTechnology, JournalEntryRepository journalEntryInstance) {
        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("persistence.technology")).thenReturn(persistenceTechnology);
            JournalEntryRepository journalEntryRepository = BeanFactory.getJournalEntryRepository();
            Assertions.assertEquals(journalEntryInstance, journalEntryRepository);
        }
    }

    private static Stream<Arguments> journalEntryProvider() {
        return Stream.of(
                Arguments.of(HIBERNATE, HibernateJournalEntryRepository.getInstance()),
                Arguments.of(JDBC, JDBCJournalEntryRepository.getInstance()));
    }



    @Test
     void whenTechnologyIsUnknownInJournalEntryShouldThrowInvalidPersistenceException() {
        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("persistence.technology")).thenReturn("oracle");
            assertThrows(InvalidPersistenceTechnologyException.class, BeanFactory::getJournalEntryRepository);
        }
    }


    @ParameterizedTest
    @MethodSource("positionProvider")
     void whenTechnologyIsJdbcShouldReturnJdbcPositionInstance(String persistenceTechnology, PositionRepository positionRepositoryInstance) {
        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("persistence.technology")).thenReturn(persistenceTechnology);
            PositionRepository positionRepository = BeanFactory.getPositionsRepository();
            Assertions.assertEquals(positionRepository, positionRepositoryInstance, "shouldReturnJDBCInstance");
        }
    }

    private static Stream<Arguments> positionProvider() {
        return Stream.of(
                Arguments.of(HIBERNATE, HibernateTradePositionRepository.getInstance()),
                Arguments.of(JDBC, JDBCTradePositionRepository.getInstance()));
    }


    @Test
     void whenTechnologyIsUnknownInTradePositionShouldThrowInvalidPersistenceException() {
        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("persistence.technology")).thenReturn("oracle");
            assertThrows(InvalidPersistenceTechnologyException.class, BeanFactory::getPositionsRepository);
        }
    }


}
