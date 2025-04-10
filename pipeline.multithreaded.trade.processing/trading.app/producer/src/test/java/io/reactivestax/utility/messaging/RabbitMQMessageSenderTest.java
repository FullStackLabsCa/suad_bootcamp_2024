package io.reactivestax.utility.messaging;

import com.rabbitmq.client.Channel;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;

public class RabbitMQMessageSenderTest {

    @Test
    public void testSingleton() {
        RabbitMQMessageSender instance = RabbitMQMessageSender.getInstance();
        assertNotNull(instance);
        RabbitMQMessageSender instance1 = RabbitMQMessageSender.getInstance();
        assertNotNull(instance1);
        assertSame(instance, instance1);
    }

    @Test
    public void returnTrueForSuccessMessageDelivery() throws IOException {

        Channel mockChannel = Mockito.mock(Channel.class);
        try (MockedStatic<RabbitMQUtils> mockedUtils = Mockito.mockStatic(RabbitMQUtils.class)) {
            mockedUtils.when(RabbitMQUtils::getRabbitMQChannel).thenReturn(mockChannel);

            // Stub the basicPublish method(replacing the real channel call with this piece of code)
            Mockito.doNothing().when(mockChannel).basicPublish(anyString(), anyString(), any(), any());

            // Call the method
            RabbitMQMessageSender rabbitMQMessageSender = RabbitMQMessageSender.getInstance();


            Boolean result = rabbitMQMessageSender.sendMessageToQueue("testQueue", "TradeId001");

            // Verify interactions and assertions
            //here when the real method is called the getMQChannel() method is mocked which we are verifying below with the mocked channel and real method
            Mockito.verify(mockChannel).basicPublish(anyString(), eq("testQueue"), any(), eq("TradeId001".getBytes(StandardCharsets.UTF_8)));
            assertTrue(result);

        }
    }

    @Test
    //testing the exception part by simulating that it throws exception with mock
    public void returnFalseForMessageDeliveryFailure() throws IOException {
        Channel mockChannel = Mockito.mock(Channel.class);
        try (MockedStatic<RabbitMQUtils> mockedUtils = Mockito.mockStatic(RabbitMQUtils.class)) {
            mockedUtils.when(RabbitMQUtils::getRabbitMQChannel).thenReturn(mockChannel);

            // Configure the mockChannel to throw an exception when basicPublish is called
            Mockito.doThrow(new IOException("Simulated Exception")).when(mockChannel)
                    .basicPublish(anyString(), anyString(), any(), any());

            Boolean booleanResult = RabbitMQMessageSender.getInstance().sendMessageToQueue("testQueue", "TradeId002");
            assertFalse(booleanResult);

        }
    }
}
