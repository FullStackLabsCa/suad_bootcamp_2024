package io.reactivestax.utility.messaging;

import com.rabbitmq.client.Channel;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class RabbitMQUtilsTest {

    @Test
    public void testRabbitMQChannelAndSettingChannelInThreadLocal() throws IOException, TimeoutException {
        Channel rabbitMQChannel = RabbitMQUtils.getRabbitMQChannel();
        assertNotNull(rabbitMQChannel);
        Channel rabbitMQChannel1 = RabbitMQUtils.getRabbitMQChannel();
        assertNotNull(rabbitMQChannel1);
        assertSame(rabbitMQChannel, rabbitMQChannel1);

    }
}
