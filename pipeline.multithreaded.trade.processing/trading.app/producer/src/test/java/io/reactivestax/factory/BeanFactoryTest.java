package io.reactivestax.factory;

import io.reactivestax.repository.hibernate.HibernateTradePayloadRepository;
import io.reactivestax.repository.jdbc.JDBCTradePayloadRepository;
import io.reactivestax.types.contract.MessageSender;
import io.reactivestax.types.contract.repository.PayloadRepository;
import io.reactivestax.types.contract.repository.TransactionUtil;
import io.reactivestax.types.exceptions.InvalidPersistenceTechnologyException;
import io.reactivestax.utility.ApplicationPropertiesUtils;
import io.reactivestax.utility.database.DBUtils;
import io.reactivestax.utility.database.HibernateUtil;
import io.reactivestax.utility.messaging.RabbitMQMessageSender;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;


import java.util.stream.Stream;

import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesStringFormat;
import static org.junit.Assert.*;


public class BeanFactoryTest {


    static final String HIBERNATE = "hibernate";
    static final String JDBC = "jdbc";

    @Test
    public void shouldReturnMQInstance() {
        RabbitMQMessageSender instance = RabbitMQMessageSender.getInstance();
        MessageSender messageSenderInstance = BeanFactory.getQueueMessageSender();
        assertEquals("shouldReturnMQInstance", instance, messageSenderInstance);
    }


    @Test
    public void whenTechnologyIsUnknownShouldThrowException() {
        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("persistence.technology")).thenReturn("oracle");
            assertThrows(InvalidPersistenceTechnologyException.class, BeanFactory::getQueueMessageSender);
        }
    }


    @Test
    public void shouldReturnHibernateTradeRepositoryInstance()  {
        HibernateTradePayloadRepository instance = HibernateTradePayloadRepository.getInstance();
        PayloadRepository hibernateInstance = BeanFactory.getTradePayloadRepository();
        assertEquals("shouldReturnHibernateInstance", instance, hibernateInstance);
    }

    @ParameterizedTest
    @MethodSource("tradePayloadProvider")
     void whenTechnologyIsJdbcShouldReturnJdbcInstance(String persistenceTechnology, PayloadRepository repositoryInstance) {
        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("persistence.technology")).thenReturn(persistenceTechnology);
            PayloadRepository tradePayloadRepository = BeanFactory.getTradePayloadRepository();
            assertEquals( repositoryInstance, tradePayloadRepository);
        }
    }

    private static Stream<Arguments> tradePayloadProvider() {
        return Stream.of(
                Arguments.of(HIBERNATE, HibernateTradePayloadRepository.getInstance()),
                Arguments.of(JDBC, JDBCTradePayloadRepository.getInstance())
        );
    }



    @Test
    public void whenTechnologyIsUnknownInTradePayloadRepositoryShouldThrowInvalidPersistenceException() {
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
}
