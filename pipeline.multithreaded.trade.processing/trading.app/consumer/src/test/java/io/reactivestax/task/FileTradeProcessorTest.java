package io.reactivestax.task;

import io.reactivestax.factory.BeanFactory;
import io.reactivestax.types.contract.QueueLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileTradeProcessorTest {



    @Mock
    QueueLoader queueLoader;

    @Test
    void testCall() throws Exception {

        final String queueName = "queue0";
        try (MockedStatic<BeanFactory> beanFactoryMockedStatic = Mockito.mockStatic(BeanFactory.class)) {
            beanFactoryMockedStatic.when(BeanFactory::getQueueSetUp).thenReturn(queueLoader);
            FileTradeProcessor fileTradeProcessor = new FileTradeProcessor(queueName);
            fileTradeProcessor.call();
            verify(queueLoader, atLeastOnce()).consumeMessage(queueName);
        }
    }
}