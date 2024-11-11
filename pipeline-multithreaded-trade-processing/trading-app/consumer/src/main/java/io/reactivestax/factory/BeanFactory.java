package io.reactivestax.factory;

import io.reactivestax.types.contract.QueueLoader;
import io.reactivestax.types.contract.repository.*;
import io.reactivestax.types.exception.InvalidPersistenceTechnologyException;
import io.reactivestax.utility.messaging.RabbitMQLoader;
import io.reactivestax.repository.hibernate.HibernateJournalEntryRepository;
import io.reactivestax.repository.hibernate.HibernateSecuritiesReferenceRepository;
import io.reactivestax.repository.hibernate.HibernateTradePositionRepository;
import io.reactivestax.repository.jdbc.JDBCJournalEntryRepository;
import io.reactivestax.repository.jdbc.JDBCSecuritiesReferenceRepository;
import io.reactivestax.repository.jdbc.JDBCTradePayloadRepository;
import io.reactivestax.repository.hibernate.HibernateTradePayloadRepository;
import io.reactivestax.repository.jdbc.JDBCTradePositionRepository;
import io.reactivestax.utility.DBUtils;
import io.reactivestax.utility.HibernateUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;

import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesIntegerFormat;
import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesStringFormat;


@Slf4j
public class BeanFactory {

    private BeanFactory() {
    }

    @Getter
    private static final LinkedBlockingQueue<String> chunksFileMappingQueue = new LinkedBlockingQueue<>();
    @Getter
    private static final List<LinkedBlockingDeque<String>> QUEUE_LIST = new ArrayList<>();
    private static final Integer QUEUES_NUMBER = 0;


    private static final String RABBITMQ_MESSAGING_TECHNOLOGY = "rabbitmq";
    private static final String IN_MEMORY_MESSAGING_TECHNOLOGY = "inmemory";

    private static final String HIBERNATE_PERSISTENCE_TECHNOLOGY = "hibernate";
    private static final String JDBC_PERSISTENCE_TECHNOLOGY = "jdbc";
    private static final String ERROR_MESSAGE = "Invalid messaging technology";


    public static QueueLoader getQueueSetUp() throws FileNotFoundException {
        String messagingTechnology = readFromApplicationPropertiesStringFormat("messaging.technology");
        if (RABBITMQ_MESSAGING_TECHNOLOGY.equals(messagingTechnology)) {
            return RabbitMQLoader.getInstance();
        } else if (IN_MEMORY_MESSAGING_TECHNOLOGY.equals(messagingTechnology)) {
            return null;
        } else {
            throw new InvalidPersistenceTechnologyException(ERROR_MESSAGE);
        }
    }


    public static PayloadRepository getTradePayloadRepository() {
        Map<String, Supplier<PayloadRepository>> payloadRepositoryMap = new HashMap<>();
        payloadRepositoryMap.put(HIBERNATE_PERSISTENCE_TECHNOLOGY, HibernateTradePayloadRepository::getInstance);
        payloadRepositoryMap.put(JDBC_PERSISTENCE_TECHNOLOGY, JDBCTradePayloadRepository::getInstance);

        Optional<String> optionalPersistenceTechnology = Optional.ofNullable(readFromApplicationPropertiesStringFormat("persistence.technology"));
        return optionalPersistenceTechnology
                .map(payloadRepositoryMap::get)
                .map(Supplier::get)
                .orElseThrow(() -> new InvalidPersistenceTechnologyException(ERROR_MESSAGE));

    }

    public static SecuritiesReferenceRepository getLookupSecuritiesRepository() {
        Map<String, Supplier<SecuritiesReferenceRepository>> payloadRepositoryMap = new HashMap<>();
        payloadRepositoryMap.put(HIBERNATE_PERSISTENCE_TECHNOLOGY, HibernateSecuritiesReferenceRepository::getInstance);
        payloadRepositoryMap.put(JDBC_PERSISTENCE_TECHNOLOGY, JDBCSecuritiesReferenceRepository::getInstance);

        Optional<String> optionalPersistenceTechnology = Optional.ofNullable(readFromApplicationPropertiesStringFormat("persistence.technology"));
        return optionalPersistenceTechnology
                .map(payloadRepositoryMap::get)
                .map(Supplier::get)
                .orElseThrow(() -> new InvalidPersistenceTechnologyException(ERROR_MESSAGE));
    }

    public static JournalEntryRepository getJournalEntryRepository() {

        Map<String, Supplier<JournalEntryRepository>> payloadRepositoryMap = new HashMap<>();
        payloadRepositoryMap.put(HIBERNATE_PERSISTENCE_TECHNOLOGY, HibernateJournalEntryRepository::getInstance);
        payloadRepositoryMap.put(JDBC_PERSISTENCE_TECHNOLOGY, JDBCJournalEntryRepository::getInstance);

        Optional<String> optionalPersistenceTechnology = Optional.ofNullable(readFromApplicationPropertiesStringFormat("persistence.technology"));
        return optionalPersistenceTechnology
                .map(payloadRepositoryMap::get)
                .map(Supplier::get)
                .orElseThrow(() -> new InvalidPersistenceTechnologyException(ERROR_MESSAGE));
    }

    public static PositionRepository getPositionsRepository() {

        Map<String, Supplier<PositionRepository>> payloadRepositoryMap = new HashMap<>();
        payloadRepositoryMap.put(HIBERNATE_PERSISTENCE_TECHNOLOGY, HibernateTradePositionRepository::getInstance);
        payloadRepositoryMap.put(JDBC_PERSISTENCE_TECHNOLOGY, JDBCTradePositionRepository::getInstance);

        Optional<String> optionalPersistenceTechnology = Optional.ofNullable(readFromApplicationPropertiesStringFormat("persistence.technology"));
        return optionalPersistenceTechnology
                .map(payloadRepositoryMap::get)
                .map(Supplier::get)
                .orElseThrow(() -> new InvalidPersistenceTechnologyException(ERROR_MESSAGE));
    }


    public static TransactionUtil getTransactionUtil() {
        Map<String, Supplier<TransactionUtil>> transactionUtilMap = new HashMap<>();
        transactionUtilMap.put(HIBERNATE_PERSISTENCE_TECHNOLOGY, HibernateUtil::getInstance);
        transactionUtilMap.put(JDBC_PERSISTENCE_TECHNOLOGY, DBUtils::getInstance);

        Optional<String> optionalPersistenceTechnology = Optional.ofNullable(readFromApplicationPropertiesStringFormat("persistence.technology"));
        return optionalPersistenceTechnology
                .map(transactionUtilMap::get)
                .map(Supplier::get)
                .orElseThrow(() -> new InvalidPersistenceTechnologyException(ERROR_MESSAGE));

    }


    //current version is using queueList
    //Make sure to call this method to get the queues before launching the queues in chunkProcessor.
    public static List<LinkedBlockingDeque<String>> addToQueueList() throws IOException {
        for (int i = 0; i < readFromApplicationPropertiesIntegerFormat("queue.count"); i++) {
            QUEUE_LIST.add(new LinkedBlockingDeque<>());
        }
        return QUEUE_LIST;
    }

    public static void setChunksFileMappingQueue(String fileName) {
        chunksFileMappingQueue.add(fileName);
    }

}
