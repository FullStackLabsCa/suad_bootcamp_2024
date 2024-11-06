package io.reactivestax.utility;

import com.rabbitmq.client.Channel;
import io.reactivestax.utility.messaging.RabbitMQUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class RabbitMQUtilsTest {

    @Test
    public void testRabbitMQChannelAndSettingChannelInThreadLocal() throws IOException, TimeoutException {
//        ConnectionFactory connectionFactory = mock(ConnectionFactory.class);
//        Connection connection = mock(Connection.class);
//        Channel channel = mock(Channel.class);
//
//        when(connectionFactory.newConnection()).thenReturn(connection);
//        when(connection.createChannel()).thenReturn(channel);
//
//        Channel rabbitMQChannel = RabbitMQUtils.getRabbitMQChannel();
//        assertSame(channel,rabbitMQChannel);
//
//        verify(connectionFactory).newConnection();
//        verify(connection).createChannel();
        Channel rabbitMQChannel = RabbitMQUtils.getRabbitMQChannel();
        assertNotNull(rabbitMQChannel);
        Channel rabbitMQChannel1 = RabbitMQUtils.getRabbitMQChannel();
        assertNotNull(rabbitMQChannel1);
        assertSame(rabbitMQChannel, rabbitMQChannel1);

    }
}
