package io.reactivestax.utility.messaging;

import com.rabbitmq.client.Channel;
import io.reactivestax.utility.RabbitMQUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

 class RabbitMQUtilsTest {

    @Test
     void testRabbitMQChannelAndSettingChannelInThreadLocal() throws IOException, TimeoutException {
        Channel rabbitMQChannel = RabbitMQUtils.getRabbitMQChannel();
        assertNotNull(rabbitMQChannel);
        Channel rabbitMQChannel1 = RabbitMQUtils.getRabbitMQChannel();
        assertNotNull(rabbitMQChannel1);
        assertSame(rabbitMQChannel, rabbitMQChannel1);

    }
}
