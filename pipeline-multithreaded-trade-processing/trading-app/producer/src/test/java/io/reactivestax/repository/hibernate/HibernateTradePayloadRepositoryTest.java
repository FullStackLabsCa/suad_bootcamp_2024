package io.reactivestax.repository.hibernate;

import io.reactivestax.factory.BeanFactory;
import io.reactivestax.repository.hibernate.entity.TradePayload;
import io.reactivestax.service.MessagePublisherService;
import io.reactivestax.types.dto.Trade;
import io.reactivestax.types.enums.LookUpStatusEnum;
import io.reactivestax.types.enums.PostedStatusEnum;
import io.reactivestax.types.enums.ValidityStatusEnum;
import io.reactivestax.utility.database.HibernateUtil;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static io.reactivestax.utility.Utility.prepareTrade;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class HibernateTradePayloadRepositoryTest {

    @Mock
    HibernateUtil hibernateUtil;

    @Mock
    Session session;

    @Mock
    TradePayload tradePayload;

    @Captor
    ArgumentCaptor<TradePayload> tradePayloadCaptor;

    @InjectMocks
    HibernateTradePayloadRepository hibernateTradePayloadRepository;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void getInstance() {
        HibernateTradePayloadRepository instance = HibernateTradePayloadRepository.getInstance();
        HibernateTradePayloadRepository instance1 = HibernateTradePayloadRepository.getInstance();
        assertEquals(instance, instance1);
    }

    @Test
    void insertTradeIntoTradePayloadTable() throws Exception {
//        Mockito.when(hibernateUtil.getInstance().getConnection()).thenReturn(session);

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
            assertEquals(String.valueOf(ValidityStatusEnum.VALID), capturedPayload.getValidityStatus());
            assertEquals("All field present ", capturedPayload.getStatusReason());
            assertEquals(String.valueOf(LookUpStatusEnum.FAIL), capturedPayload.getLookupStatus());
            assertEquals(String.valueOf(PostedStatusEnum.NOT_POSTED), capturedPayload.getJeStatus());
            assertEquals(payload, capturedPayload.getPayload());
            assertEquals(result.get(), capturedPayload.getTradeId());

        }
    }
}