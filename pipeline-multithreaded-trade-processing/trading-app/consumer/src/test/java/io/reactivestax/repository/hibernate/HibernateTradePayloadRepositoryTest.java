package io.reactivestax.repository.hibernate;

import io.reactivestax.repository.hibernate.entity.TradePayload;
import io.reactivestax.types.dto.Trade;
import io.reactivestax.types.enums.LookUpStatusEnum;
import io.reactivestax.types.enums.PostedStatusEnum;
import io.reactivestax.types.enums.StatusReasonEnum;
import io.reactivestax.types.enums.ValidityStatusEnum;
import io.reactivestax.utility.database.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static io.reactivestax.utility.Utility.prepareTrade;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class HibernateTradePayloadRepositoryTest {

    @Mock
    HibernateUtil hibernateUtil;

    @Mock
    Session session;

    @Mock
    Transaction transaction;

    @Captor
    ArgumentCaptor<TradePayload> tradePayloadCaptor;

    @Mock
    Query<TradePayload> query;


    @Test
    void getInstance() {
        HibernateTradePayloadRepository instance = HibernateTradePayloadRepository.getInstance();
        HibernateTradePayloadRepository instance1 = HibernateTradePayloadRepository.getInstance();
        assertEquals(instance, instance1);
    }

    @Test
    void insertTradeIntoTradePayloadTable() throws Exception {

        try (MockedStatic<HibernateUtil> mockedHibernateUtil = Mockito.mockStatic(HibernateUtil.class)) {
            //Since it is singleton and getInstance() is static method we have to access through the mockstatic
            mockedHibernateUtil.when(HibernateUtil::getInstance).thenReturn(hibernateUtil);
            Mockito.when(hibernateUtil.getConnection()).thenReturn(session);

            String payload = "TDB_00000000,2024-09-19 22:16:18,TDB_CUST_5214938,V,SELL,683,638.02";
            Trade trade = prepareTrade(payload);

            // Mocking the behavior of session
            Mockito.doNothing().when(session).persist(any(TradePayload.class));

            // Act
            Optional<String> result = HibernateTradePayloadRepository.getInstance().insertTradeIntoTradePayloadTable(payload);

            // Assert
            verify(session, times(1)).persist(tradePayloadCaptor.capture()); // Capture the persisted object
            TradePayload capturedPayload = tradePayloadCaptor.getValue();

            // Verify TradePayload fields
            assertEquals(trade.getTradeIdentifier(), capturedPayload.getTradeId());
            assertEquals(ValidityStatusEnum.VALID, capturedPayload.getValidityStatus());
            assertEquals(StatusReasonEnum.ALL_FIELDS_PRESENT, capturedPayload.getStatusReason());
            assertEquals(LookUpStatusEnum.FAIL, capturedPayload.getLookupStatus());
            assertEquals(PostedStatusEnum.NOT_POSTED, capturedPayload.getJeStatus());
            assertEquals(payload, capturedPayload.getPayload());
            result.ifPresent(tradeId -> assertEquals(tradeId, capturedPayload.getTradeId()));

        }
    }

    @Test
    void testUpdateLookupStatus() {

        try (MockedStatic<HibernateUtil> mockedHibernateUtil = Mockito.mockStatic(HibernateUtil.class)) {
            //Since it is singleton and getInstance() is static method we have to access through the mockstatic
            mockedHibernateUtil.when(HibernateUtil::getInstance).thenReturn(hibernateUtil);
            Mockito.when(hibernateUtil.getConnection()).thenReturn(session);


            String tradeId = "TDB_00000000";
            TradePayload mockTradePayload = TradePayload.builder()
                    .tradeId(tradeId)
                    .build();

            mockTradePayload.setTradeId(tradeId);
            mockTradePayload.setLookupStatus(LookUpStatusEnum.FAIL);

            when(session.createQuery("FROM TradePayload WHERE tradeId = :tradeId", TradePayload.class)).thenReturn(query);
            when(session.getTransaction()).thenReturn(transaction);
            when(query.setParameter("tradeId", tradeId)).thenReturn(query);
            when(query.stream()).thenReturn(Stream.of(mockTradePayload));

            // Act
            HibernateTradePayloadRepository.getInstance().updateLookUpStatus(tradeId);

            // Assert
            verify(session, times(1)).beginTransaction();
            verify(query, times(1)).setParameter("tradeId", tradeId);
            verify(query, times(1)).stream();
            verify(session, times(1)).persist(tradePayloadCaptor.capture());
            verify(transaction, times(1)).commit(); // Verify transaction commit

            TradePayload capturedPayload = tradePayloadCaptor.getValue();
            assertEquals(LookUpStatusEnum.PASS, capturedPayload.getLookupStatus());
        }
    }


    @Test
    void testUpdateJournalStatus() {

        try (MockedStatic<HibernateUtil> mockedHibernateUtil = Mockito.mockStatic(HibernateUtil.class)) {
            //Since it is singleton and getInstance() is static method we have to access through the mockstatic
            mockedHibernateUtil.when(HibernateUtil::getInstance).thenReturn(hibernateUtil);
            Mockito.when(hibernateUtil.getConnection()).thenReturn(session);

            String tradeId = "TDB_00000001";
            TradePayload mockTradePayload = TradePayload.builder()
                    .tradeId(tradeId)
                    .build();
            mockTradePayload.setTradeId(tradeId);
            mockTradePayload.setLookupStatus(LookUpStatusEnum.FAIL);

            when(session.createQuery("FROM TradePayload WHERE tradeId = :tradeId", TradePayload.class)).thenReturn(query);
            when(session.getTransaction()).thenReturn(transaction);
            when(query.setParameter("tradeId", tradeId)).thenReturn(query);
            when(query.stream()).thenReturn(Stream.of(mockTradePayload));

            // Act
            HibernateTradePayloadRepository.getInstance().updateJournalStatus(tradeId);

            // Assert
            verify(session, times(1)).beginTransaction();
            verify(query, times(1)).setParameter("tradeId", tradeId);
            verify(query, times(1)).stream();
            verify(session, times(1)).persist(tradePayloadCaptor.capture());
            verify(transaction, times(1)).commit(); // Verify transaction commit

            TradePayload capturedPayload = tradePayloadCaptor.getValue();
            assertEquals(PostedStatusEnum.POSTED, capturedPayload.getJeStatus());
        }
    }

    @Test
    void testReadTradePayloadByTradeId() {
        HibernateUtil.setConfigResource("hibernate-h2.cfg.xml");
        HibernateUtil hibernateInstance = HibernateUtil.getInstance();
        String payload = "TDB_00000003,2024-09-19 22:16:18,TDB_CUST_5214938,GOOGL,SELL,683,638.02";
        HibernateTradePayloadRepository instance = HibernateTradePayloadRepository.getInstance();
        hibernateInstance.startTransaction();
        instance.insertTradeIntoTradePayloadTable(payload);
        hibernateInstance.commitTransaction();
        Optional<String> retrievedPayload = instance.readTradePayloadByTradeId("TDB_00000003");
        assertEquals(payload, retrievedPayload.get());
        cleanUp();
    }

    void cleanUp() {
        Session session = HibernateUtil.getInstance().getConnection();
        session.beginTransaction();
        session.createQuery("DELETE FROM TradePayload").executeUpdate();
        session.getTransaction().commit();
    }
}