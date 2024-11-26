package io.reactivestax.task;

import io.reactivestax.service.TradeProcessorService;
import io.reactivestax.types.contract.TradeProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FileTradeProcessorTest {


    @Mock
    TradeProcessorService tradeProcessorService;

    @Test
    void testCall() throws Exception {

        String queueName = "queue1";
        FileTradeProcessor fileTradeProcessor = new FileTradeProcessor(queueName);

        try (MockedStatic<TradeProcessorService> tradeProcessorServiceMockedStatic = Mockito.mockStatic(TradeProcessorService.class)) {
            tradeProcessorServiceMockedStatic.when(TradeProcessorService::getInstance).thenReturn(tradeProcessorService);
            fileTradeProcessor.call();
            verify(tradeProcessorService, times(1)).processTrade(queueName);
        }
    }
}