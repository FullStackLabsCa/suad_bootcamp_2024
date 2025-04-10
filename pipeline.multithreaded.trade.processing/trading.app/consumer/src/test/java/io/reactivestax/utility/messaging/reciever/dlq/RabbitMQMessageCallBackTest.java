package io.reactivestax.utility.messaging.reciever.dlq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Delivery;
import com.rabbitmq.client.Envelope;
import io.reactivestax.service.TradeProcessorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import com.rabbitmq.client.Channel;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class RabbitMQMessageCallBackTest {

    @Mock
    TradeProcessorService tradeProcessorService;

    @Mock
    Channel mockChannel;

    @Mock
    Delivery mockDelivery;

    @Mock
    Envelope mockEnvelope;

    @Mock
    AMQP.BasicProperties mockBasicProperties;

    final String message = "TBD000003";

    @Test
    void testHandle() throws IOException, SQLException {
        try (MockedStatic<TradeProcessorService> tradeProcessorServiceMockedStatic = Mockito.mockStatic(TradeProcessorService.class)) {
            tradeProcessorServiceMockedStatic.when(TradeProcessorService::getInstance).thenReturn(tradeProcessorService);


            Mockito.when(mockDelivery.getBody()).thenReturn(message.getBytes(StandardCharsets.UTF_8));
            Mockito.when(mockDelivery.getEnvelope()).thenReturn(mockEnvelope);
            Mockito.when(mockEnvelope.getDeliveryTag()).thenReturn(1L);

            RabbitMQMessageCallBack mqMessageCallBackInstance = new RabbitMQMessageCallBack(mockChannel, "queue1");

            // Call the handle method with proper arguments
            mqMessageCallBackInstance.handle("consumerTag1", mockDelivery);

            verify(tradeProcessorService, times(1)).processTrade("TBD000003");
            verify(mockChannel, times(1)).basicAck(1L, false);
        }
    }


    @Test
    void testHandle_Exception() throws SQLException, IOException {

        try (MockedStatic<TradeProcessorService> tradeProcessorServiceMockedStatic = Mockito.mockStatic(TradeProcessorService.class)) {
            tradeProcessorServiceMockedStatic.when(TradeProcessorService::getInstance).thenReturn(tradeProcessorService);
            Mockito.when(mockDelivery.getBody()).thenReturn(message.getBytes(StandardCharsets.UTF_8));
            Mockito.when(mockDelivery.getEnvelope()).thenReturn(mockEnvelope);
            Mockito.when(mockEnvelope.getDeliveryTag()).thenReturn(1L);
            Mockito.when(mockDelivery.getProperties()).thenReturn(mockBasicProperties);
            doThrow(new RuntimeException("Testing Retry Mechanism")).when(tradeProcessorService).processTrade(anyString());

            RabbitMQMessageCallBack mqMessageCallBackInstance = new RabbitMQMessageCallBack(mockChannel, "queue1");
            mqMessageCallBackInstance.handle("consumerTag1", mockDelivery);
            verify(mockBasicProperties, atLeastOnce()).getHeaders();
        }
    }

}