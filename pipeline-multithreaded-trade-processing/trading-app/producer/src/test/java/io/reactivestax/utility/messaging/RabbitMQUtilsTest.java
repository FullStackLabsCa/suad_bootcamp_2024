package io.reactivestax.utility.messaging;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.reactivestax.utility.ApplicationPropertiesUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesStringFormat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RabbitMQUtilsTest {

    @Mock
    ConnectionFactory mockConnectionFactory;

    @Mock
    Connection mockConnection;

    @Mock
    Channel mockChannel;

    @Mock
    AMQP.Exchange.DeclareOk mockDeclare;

    @Test
    public void testRabbitMQChannelAndSettingChannelInThreadLocal() throws IOException, TimeoutException {
        Channel rabbitMQChannel = RabbitMQUtils.getRabbitMQChannel();
        assertNotNull(rabbitMQChannel);
        Channel rabbitMQChannel1 = RabbitMQUtils.getRabbitMQChannel();
        assertNotNull(rabbitMQChannel1);
        assertSame(rabbitMQChannel, rabbitMQChannel1);
    }


//    @Test
//    void testRabbitMQChannelAndSettingChannelInThreadLocal() throws IOException, TimeoutException {
////       when(new ConnectionFactory()).thenReturn(mockConnectionFactory);
//
//        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
//            mocked.when(() -> readFromApplicationPropertiesStringFormat("queue.host")).thenReturn("localhost");
//            mocked.when(() -> readFromApplicationPropertiesStringFormat("queue.username")).thenReturn("guest");
//            mocked.when(() -> readFromApplicationPropertiesStringFormat("queue.password")).thenReturn("guest");
//
//            when(mockConnectionFactory.newConnection()).thenReturn(mockConnection);
//            when(mockConnection.createChannel()).thenReturn(mockChannel);
//            when(mockChannel.exchangeDeclare(anyString(), anyString())).thenReturn(mockDeclare);
//            RabbitMQUtils.getRabbitMQChannel();
//            verify(mockConnectionFactory, atLeastOnce()).setHost(anyString());
//            verify(mockConnectionFactory, atLeastOnce()).setUsername(anyString());
//            verify(mockConnectionFactory, atLeastOnce()).setPassword(anyString());
//        }
//    }
}
