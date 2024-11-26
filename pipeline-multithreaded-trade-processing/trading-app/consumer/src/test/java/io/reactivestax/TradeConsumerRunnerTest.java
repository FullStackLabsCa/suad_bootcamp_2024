package io.reactivestax;

import io.reactivestax.service.ConsumerSubmitterService;
import io.reactivestax.utility.ApplicationPropertiesUtils;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.concurrent.ExecutorService;

import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesIntegerFormat;
import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesStringFormat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class TradeConsumerRunnerTest {

    @Test
    void testStartConsumer() throws Exception {
        try (MockedStatic<ApplicationPropertiesUtils> applicationPropertiesUtilsMockedStatic = mockStatic(ApplicationPropertiesUtils.class);
             MockedStatic<ConsumerSubmitterService> consumerSubmitterMock = mockStatic(ConsumerSubmitterService.class)) {
            ConsumerSubmitterService mockInstance = mock(ConsumerSubmitterService.class);

            // Mock `getInstance` to return the mockInstance
            consumerSubmitterMock.when(ConsumerSubmitterService::getInstance).thenReturn(mockInstance);


            applicationPropertiesUtilsMockedStatic.when(() -> readFromApplicationPropertiesStringFormat("queue.name"))
                    .thenReturn("testQueue");

            applicationPropertiesUtilsMockedStatic.when(() -> readFromApplicationPropertiesIntegerFormat("queue.count"))
                    .thenReturn(3);

            applicationPropertiesUtilsMockedStatic.when(() -> readFromApplicationPropertiesIntegerFormat("trade.processor.thread.poolSize"))
                    .thenReturn(2);

            doNothing().when(mockInstance).startConsumer(any(ExecutorService.class), anyString());

            TradeConsumerRunner.main(new String[]{});

            // Verify interactions
            verify(mockInstance, times(3)).startConsumer(Mockito.any(), anyString());
        }
    }
}