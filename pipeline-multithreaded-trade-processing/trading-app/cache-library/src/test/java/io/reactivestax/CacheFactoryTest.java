package io.reactivestax;

import io.reactivestax.contract.EvictionPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class CacheFactoryTest {

    private CacheFactory<String, String> cacheFactory;
    private Cache<String, String> mockCache;
    private EvictionPolicy<String, String> mockPolicy;
    private EvictionManager<String, String> mockEvictionManager;

    @BeforeEach
    void setUp() {
        mockEvictionManager = mock(EvictionManager.class);
        cacheFactory = new CacheFactory<>(mockEvictionManager);
        mockCache = mock(Cache.class);
        mockPolicy = mock(EvictionPolicy.class);
    }

    @Test
    void testApplyEvictionPolicy() {
        long evictionInterval = 1000L;

        // Call the method under test
        cacheFactory.applyEvictionPolicy(mockCache, mockPolicy, evictionInterval);

        // Verify the interaction with EvictionManager
        verify(mockEvictionManager, times(1))
                .registerEvictionPolicy(mockCache, mockPolicy, evictionInterval);
        verifyNoMoreInteractions(mockEvictionManager);
    }

}