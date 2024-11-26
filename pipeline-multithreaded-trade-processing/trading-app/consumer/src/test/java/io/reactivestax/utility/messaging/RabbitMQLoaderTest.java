package io.reactivestax.utility.messaging;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.DeliverCallback;
import io.reactivestax.utility.RabbitMQUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RabbitMQLoaderTest {

    @InjectMocks
    @Spy
    RabbitMQLoader rabbitMQLoader;

    @Mock
    Channel mockChannel;

    @Test
    void getInstance() {
        RabbitMQLoader instance = RabbitMQLoader.getInstance();
        RabbitMQLoader instance1 = RabbitMQLoader.getInstance();
        assertSame(instance, instance1);
    }

    @Test
    void testConsumeMessage() throws IOException, TimeoutException {
        try (MockedStatic<RabbitMQLoader> rabbitMQLoaderMockedStatic = Mockito.mockStatic(RabbitMQLoader.class);
             MockedStatic<RabbitMQUtils> rabbitMQUtilsMockedStatic = Mockito.mockStatic(RabbitMQUtils.class);) {
            rabbitMQLoaderMockedStatic.when(RabbitMQLoader::getInstance).thenReturn(rabbitMQLoader);
            rabbitMQUtilsMockedStatic.when(RabbitMQUtils::getRabbitMQChannel).thenReturn(mockChannel);
            String queueName = "testQueue";
            rabbitMQLoader.consumeMessage(queueName);
            verify(mockChannel).basicConsume(anyString(),anyBoolean(), (DeliverCallback) any(), (CancelCallback) any());
        }
    }


}
