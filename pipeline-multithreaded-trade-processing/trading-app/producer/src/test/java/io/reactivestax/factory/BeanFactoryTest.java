//package io.reactivestax.factory;
//
//import io.reactivestax.repository.hibernate.HibernateTradePayloadRepository;
//import io.reactivestax.repository.jdbc.JDBCTradePayloadRepository;
//import io.reactivestax.types.contract.MessageSender;
//import io.reactivestax.types.contract.repository.PayloadRepository;
//import io.reactivestax.types.contract.repository.TransactionUtil;
//import io.reactivestax.types.exceptions.InvalidPersistenceTechnologyException;
//import io.reactivestax.utility.ApplicationPropertiesUtils;
//import io.reactivestax.utility.database.DBUtils;
//import io.reactivestax.utility.database.HibernateUtil;
//import io.reactivestax.utility.messaging.RabbitMQMessageSender;
//import org.junit.Test;
//import org.mockito.MockedStatic;
//import org.mockito.Mockito;
//
//import java.io.IOException;
//
//import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesStringFormat;
//import static org.junit.Assert.*;
//
//public class BeanFactoryTest {
//
//    @Test
//    public void shouldReturnMQInstance() throws IOException {
//        RabbitMQMessageSender instance = RabbitMQMessageSender.getInstance();
//        MessageSender messageSenderInstance = BeanFactory.getQueueMessageSender();
//        assertEquals("shouldReturnMQInstance", instance, messageSenderInstance);
//    }
//
//    @Test
//    public void shouldReturnNullInstanceForInMemoryQueue() throws IOException {
//        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
//            mocked.when(() -> readFromApplicationPropertiesStringFormat("messaging.technology")).thenReturn("inmemory");
//            MessageSender messageSenderInstance = BeanFactory.getQueueMessageSender();
//            assertNull(messageSenderInstance);
//        }
//    }
//
//    @Test
//    public void whenTechnologyIsUnknownShouldThrowException() {
//        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
//            mocked.when(() -> readFromApplicationPropertiesStringFormat("persistence.technology")).thenReturn("oracle");
//            assertThrows(InvalidPersistenceTechnologyException.class, BeanFactory::getQueueMessageSender);
//        }
//    }
//
//
//    @Test
//    public void shouldReturnHibernateTradeRepositoryInstance() throws IOException {
//        HibernateTradePayloadRepository instance = HibernateTradePayloadRepository.getInstance();
//        PayloadRepository hibernateInstance = BeanFactory.getTradePayloadRepository();
//        assertEquals("shouldReturnHibernateInstance", instance, hibernateInstance);
//    }
//
//    @Test
//    public void whenTechnologyIsJdbcShouldReturnJdbcInstance() throws IOException {
//        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
//            mocked.when(() -> readFromApplicationPropertiesStringFormat("persistence.technology")).thenReturn("jdbc");
//            JDBCTradePayloadRepository instance = JDBCTradePayloadRepository.getInstance();
//            PayloadRepository tradePayloadRepository = BeanFactory.getTradePayloadRepository();
//            assertEquals("shouldReturnJDBCInstance", instance, tradePayloadRepository);
//        }
//    }
//
//
//    @Test
//    public void whenTechnologyIsUnknownInTradePayloadRepositoryShouldThrowInvalidPersistenceException() {
//        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
//            mocked.when(() -> readFromApplicationPropertiesStringFormat("persistence.technology")).thenReturn("oracle");
//            assertThrows(InvalidPersistenceTechnologyException.class, BeanFactory::getTradePayloadRepository);
//        }
//    }
//
//    @Test
//    public void shouldReturnHibernateTransactionUtilInstance() throws IOException {
//        HibernateUtil instance = HibernateUtil.getInstance();
//        TransactionUtil hibernateUtilInstance = BeanFactory.getTransactionUtil();
//        assertEquals("shouldReturnHibernateInstance", instance, hibernateUtilInstance);
//    }
//
//    @Test
//    public void shouldReturnJdbcTransactionUtilInstance() throws IOException {
//        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
//            mocked.when(() -> readFromApplicationPropertiesStringFormat("persistence.technology")).thenReturn("jdbc");
//            DBUtils instance = DBUtils.getInstance();
//            TransactionUtil dbUtils = BeanFactory.getTransactionUtil();
//            assertEquals("shouldReturnJdbcInstance", dbUtils, instance);
//        }
//    }
//
//    @Test
//    public void whenTechnologyIsUnknownInTransactionUtilShouldThrowInvalidPersistenceException() {
//        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
//            mocked.when(() -> readFromApplicationPropertiesStringFormat("persistence.technology")).thenReturn("oracle");
//            assertThrows(InvalidPersistenceTechnologyException.class, BeanFactory::getTransactionUtil);
//        }
//    }
//
//    @Test
//    public void shouldAddPassFileNameToQueue() {
//        BeanFactory.setChunksFileMappingQueue("trade_chunks_1");
//        assertFalse(BeanFactory.getChunksFileMappingQueue().isEmpty());
//    }
//}
