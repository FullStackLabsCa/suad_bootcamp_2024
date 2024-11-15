package io.reactivestax.service;


import io.reactivestax.factory.BeanFactory;
import io.reactivestax.types.contract.MessageSender;
import io.reactivestax.types.dto.Trade;
import io.reactivestax.utility.ApplicationPropertiesUtils;
import io.reactivestax.utility.Utility;
import io.reactivestax.utility.messaging.RabbitMQMessageSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesStringFormat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessagePublisherServiceTest {

    @Mock
    BeanFactory beanFactory;

    @Mock
    ApplicationPropertiesUtils applicationPropertiesUtils;

    @Mock
    RabbitMQMessageSender messageSender;

    @Mock
    private Trade trade;

    @Mock
    private Utility utility;

    @InjectMocks
    MessagePublisherService messagePublisherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        messagePublisherService = MessagePublisherService.getInstance();
    }

    @Test
    void getInstance() {

        MessagePublisherService messagePublisherServiceInstance = MessagePublisherService.getInstance();
        MessagePublisherService messagePublisherServiceInstance1 = MessagePublisherService.getInstance();
        assertEquals(messagePublisherServiceInstance, messagePublisherServiceInstance1);
    }

    @Test
    void testFigureTheNextQueueRoundRobin() {

        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class);
             MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class);
             MockedStatic<RabbitMQMessageSender> mockedMQ = Mockito.mockStatic(RabbitMQMessageSender.class);
        ) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("trade.distribution.criteria")).thenReturn("tradeIdentifier");
            mocked.when(() -> readFromApplicationPropertiesStringFormat("trade.distribution.algorithm")).thenReturn("round-robin");
            mocked.when(() -> readFromApplicationPropertiesStringFormat("messaging.technology")).thenReturn("rabbitmq");
            mocked.when(() -> readFromApplicationPropertiesStringFormat("queue.name")).thenReturn("testQueue");
            mockedMQ.when(RabbitMQMessageSender::getInstance).thenReturn(messageSender);
            mockedUtility.when(Utility::roundRobin).thenReturn(1);


            when(trade.getTradeIdentifier()).thenReturn("trade123");
            when(messageSender.sendMessageToQueue(any(), any())).thenReturn(true);

            // Act
            messagePublisherService.figureTheNextQueue(trade);

            // Assert
            mockedUtility.verify(Utility::roundRobin, times(1));
            verify(messageSender, times(1)).sendMessageToQueue("testQueue0", "trade123");
        }
    }


    @Test
    void testFigureTheNextQueueRandom() {

        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class);
             MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class);
             MockedStatic<RabbitMQMessageSender> mockedMQ = Mockito.mockStatic(RabbitMQMessageSender.class);
        ) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("trade.distribution.criteria")).thenReturn("tradeIdentifier");
            mocked.when(() -> readFromApplicationPropertiesStringFormat("trade.distribution.algorithm")).thenReturn("random");
            mocked.when(() -> readFromApplicationPropertiesStringFormat("messaging.technology")).thenReturn("rabbitmq");
            mocked.when(() -> readFromApplicationPropertiesStringFormat("queue.name")).thenReturn("testQueue");
            mockedMQ.when(RabbitMQMessageSender::getInstance).thenReturn(messageSender);
            mockedUtility.when(Utility::random).thenReturn(1);

            when(trade.getTradeIdentifier()).thenReturn("trade123");
            when(messageSender.sendMessageToQueue(any(), any())).thenReturn(true);

            // Act
            messagePublisherService.figureTheNextQueue(trade);

            // Assert
            mockedUtility.verify(Utility::random, times(1));
            verify(messageSender, times(1)).sendMessageToQueue("testQueue0", "trade123");
        }
    }

    @Test
    void testQueueMessageSenderNull() {

        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("trade.distribution.criteria")).thenReturn("tradeIdentifier");
            mocked.when(() -> readFromApplicationPropertiesStringFormat("messaging.technology")).thenReturn("rabbitmq");
            mocked.when(() -> readFromApplicationPropertiesStringFormat("queue.name")).thenReturn("testQueue");

            when(trade.getTradeIdentifier()).thenReturn("trade123");

            // Act
            messagePublisherService.figureTheNextQueue(trade);

            // Assert
            // Verify no interaction with messageSender as it's null
            verify(messageSender, never()).sendMessageToQueue(anyString(), anyString());
        }
    }
}