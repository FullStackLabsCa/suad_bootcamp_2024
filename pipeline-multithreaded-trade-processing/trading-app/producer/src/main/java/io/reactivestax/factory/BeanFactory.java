package io.reactivestax.factory;

import io.reactivestax.repository.hibernate.HibernateTradePayloadRepository;
import io.reactivestax.repository.jdbc.JDBCTradePayloadRepository;
import io.reactivestax.types.contract.MessageSender;
import io.reactivestax.types.contract.repository.PayloadRepository;
import io.reactivestax.types.contract.repository.TransactionUtil;
import io.reactivestax.types.exceptions.InvalidPersistenceTechnologyException;
import io.reactivestax.utility.messaging.RabbitMQMessageSender;
import io.reactivestax.utility.database.DBUtils;
import io.reactivestax.utility.database.HibernateUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;

import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesStringFormat;


@Slf4j
public class BeanFactory {

    private BeanFactory() {
    }

    @Getter
    private static final LinkedBlockingQueue<String> chunksFileMappingQueue = new LinkedBlockingQueue<>();
    @Getter
    private static final List<LinkedBlockingDeque<String>> QUEUE_LIST = new ArrayList<>();
//    private static final Integer QUEUES_NUMBER;


    private static final String RABBITMQ_MESSAGING_TECHNOLOGY = "rabbitmq";
    private static final String IN_MEMORY_MESSAGING_TECHNOLOGY = "inmemory";

    private static final String HIBERNATE_PERSISTENCE_TECHNOLOGY = "hibernate";
    private static final String JDBC_PERSISTENCE_TECHNOLOGY = "jdbc";
    private static final String ERROR_MESSAGE = "Invalid messaging technology";



    public static MessageSender getQueueMessageSender() throws IOException {

        Map<String, Supplier<MessageSender>> messageSenderMap = new HashMap<>();
        messageSenderMap.put(RABBITMQ_MESSAGING_TECHNOLOGY, RabbitMQMessageSender::getInstance);
        messageSenderMap.put(IN_MEMORY_MESSAGING_TECHNOLOGY, null);
        Optional<String> optionalPersistenceTechnology = Optional.ofNullable(readFromApplicationPropertiesStringFormat("persistence.technology"));
       return optionalPersistenceTechnology
                .map(messageSenderMap::get)
                .map(Supplier::get)
                .orElseThrow(() -> new InvalidPersistenceTechnologyException(ERROR_MESSAGE));
    }


    public static PayloadRepository getTradePayloadRepository() throws IOException {
        Map<String, Supplier<PayloadRepository>> payloadRepositoryMap = new HashMap<>();
        payloadRepositoryMap.put(HIBERNATE_PERSISTENCE_TECHNOLOGY, HibernateTradePayloadRepository::getInstance);
        payloadRepositoryMap.put(JDBC_PERSISTENCE_TECHNOLOGY, JDBCTradePayloadRepository::getInstance);

        Optional<String> optionalPersistenceTechnology = Optional.ofNullable(readFromApplicationPropertiesStringFormat("persistence.technology"));
        return optionalPersistenceTechnology
                .map(payloadRepositoryMap::get)
                .map(Supplier::get)
                .orElseThrow(()->new InvalidPersistenceTechnologyException(ERROR_MESSAGE));

    }


    public static TransactionUtil getTransactionUtil() throws IOException {
        Map<String, Supplier<TransactionUtil>> transactionUtilMap = new HashMap<>();
        transactionUtilMap.put(HIBERNATE_PERSISTENCE_TECHNOLOGY, HibernateUtil::getInstance);
        transactionUtilMap.put(JDBC_PERSISTENCE_TECHNOLOGY, DBUtils::getInstance);

        Optional<String> optionalPersistenceTechnology = Optional.ofNullable(readFromApplicationPropertiesStringFormat("persistence.technology"));
       return optionalPersistenceTechnology
                .map(transactionUtilMap::get)
                .map(Supplier::get)
                .orElseThrow(()->new InvalidPersistenceTechnologyException(ERROR_MESSAGE));

    }

    public static void setChunksFileMappingQueue(String fileName) {
        chunksFileMappingQueue.add(fileName);
    }

}
