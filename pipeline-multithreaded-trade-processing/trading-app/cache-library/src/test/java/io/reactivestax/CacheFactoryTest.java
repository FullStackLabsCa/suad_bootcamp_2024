package io.reactivestax;

import io.reactivestax.contract.EvictionPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CacheFactoryTest {

    private CacheFactory cacheFactory;
    private Cache<String, String> mockCache;
    private EvictionPolicy<String, String> mockPolicy;
    private DemonThreadService  mockDemonThreadService;

    @BeforeEach
    void setUp() {
        cacheFactory = new CacheFactory();
        mockCache = mock(Cache.class);
        mockPolicy = mock(EvictionPolicy.class);
        mockDemonThreadService = mock(DemonThreadService.class);
    }

    @Test
    @DisplayName("Should register eviction policy and start eviction thread")
    void testApplyEvictionPolicy() throws InterruptedException {
        long evictionInterval = 50L;

        cacheFactory.applyEvictionPolicy(mockCache, mockPolicy, evictionInterval);

        Thread.sleep(evictionInterval * 2);
        verify(mockDemonThreadService).spawnDemonOnEviction(mockCache, mockPolicy, evictionInterval);
    }


    @Test
    @DisplayName("Should generate unique key correctly")
    void testGenerateUniqueKey() {
        String uniqueKey = cacheFactory.generateUniqueKey(mockPolicy);
        assertEquals(mockPolicy.getClass().getName(), uniqueKey);
    }

    @Test
    @DisplayName("Should not create duplicate eviction tasks for the same policy")
    void testAvoidDuplicateEvictionTasks() {
        long evictionInterval = 50L;

        cacheFactory.applyEvictionPolicy(mockCache, mockPolicy, evictionInterval);
        cacheFactory.applyEvictionPolicy(mockCache, mockPolicy, evictionInterval);

        // Ensure only one task/thread is created
        assertEquals(1, cacheFactory.evictionTasks.size());
    }

}