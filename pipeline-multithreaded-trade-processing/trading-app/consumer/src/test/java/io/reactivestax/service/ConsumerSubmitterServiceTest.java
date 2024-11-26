package io.reactivestax.service;

import io.reactivestax.task.FileTradeProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ConsumerSubmitterServiceTest {

    @Mock
    ExecutorService mockExecutorService;

    @Mock
    FileTradeProcessor mockFileTradeProcessor;

    @Captor
    private ArgumentCaptor<?> taskCaptor;

    @InjectMocks
    @Spy
    ConsumerSubmitterService mockConsumerSubmitterService;

    @Test
    void testGetInstance() {
        ConsumerSubmitterService instance = ConsumerSubmitterService.getInstance();
        ConsumerSubmitterService instance1 = ConsumerSubmitterService.getInstance();
        assertEquals(instance, instance1);
    }

    @Test
    void startConsumer() {

        String queueName = "queue0";
        mockConsumerSubmitterService.startConsumer(mockExecutorService, queueName);

        ArgumentCaptor<FileTradeProcessor> captor = ArgumentCaptor.forClass(FileTradeProcessor.class);
        verify(mockExecutorService).submit(captor.capture());

        // Extract and validate the captured FileTradeProcessor
        FileTradeProcessor submittedTask = captor.getValue();
        assertNotNull(submittedTask, "Submitted task should not be null");
        assertEquals(queueName, submittedTask.getQueueName(), "Queue name should match");
    }
}