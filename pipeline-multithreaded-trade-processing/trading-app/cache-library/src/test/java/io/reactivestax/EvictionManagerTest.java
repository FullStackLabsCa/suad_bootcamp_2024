package io.reactivestax;

import io.reactivestax.contract.EvictionPolicy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EvictionManagerTest {

    private EvictionManager<String, String> evictionManager;
    private Cache<String, String> mockCache;
    private EvictionPolicy<String, String> mockPolicy;

    @BeforeEach
    void setUp() {
        evictionManager = new EvictionManager<>();
        mockCache = mock(Cache.class);
        mockPolicy = mock(EvictionPolicy.class);
    }

    @Test
    @DisplayName("Should register eviction policy and start eviction thread")
    void testRegisterEvictionPolicy() throws InterruptedException {
        long evictionInterval = 50L;

        evictionManager.registerEvictionPolicy(mockCache, mockPolicy, evictionInterval);

        // Ensure eviction is invoked at least once
        Thread.sleep(evictionInterval * 2);
        verify(mockPolicy, atLeast(1)).evict(mockCache);
    }

    @Test
    @DisplayName("Should not create duplicate eviction tasks for the same policy")
    void testAvoidDuplicateEvictionTasks() {
        long evictionInterval = 50L;

        evictionManager.registerEvictionPolicy(mockCache, mockPolicy, evictionInterval);
        evictionManager.registerEvictionPolicy(mockCache, mockPolicy, evictionInterval);

        // Ensure only one task/thread is created
        assertEquals(1, evictionManager.evictionThreads.size());
        assertEquals(1, evictionManager.evictionTasks.size());
    }

    @Test
    @DisplayName("Should handle interrupted exception in eviction thread")
    void testHandleThreadInterruption() throws InterruptedException {
        long evictionInterval = 100L;

        evictionManager.registerEvictionPolicy(mockCache, mockPolicy, evictionInterval);

        String uniqueKey = mockPolicy.getClass().getName();
        Thread evictionThread = evictionManager.evictionThreads.get(uniqueKey);

        assertNotNull(evictionThread);

        // Interrupt the thread
        evictionThread.interrupt();

        // Allow some time for the thread to terminate
        Thread.sleep(evictionInterval);

        // Ensure the thread is no longer running
        AtomicBoolean isRunning = evictionManager.evictionTasks.get(uniqueKey);
        assertFalse(isRunning.get());
    }

    @Test
    @DisplayName("Should generate unique key correctly")
    void testGenerateUniqueKey() {
        String uniqueKey = evictionManager.generateUniqueKey(mockPolicy);
        assertEquals(mockPolicy.getClass().getName(), uniqueKey);
    }

    @AfterEach
    void tearDown() {
        // Stop all eviction threads to ensure no background tasks are running
        evictionManager.evictionThreads.values().forEach(Thread::interrupt);
    }

}