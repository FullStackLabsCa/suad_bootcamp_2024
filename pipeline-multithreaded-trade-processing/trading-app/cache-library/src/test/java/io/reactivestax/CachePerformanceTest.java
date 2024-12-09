package io.reactivestax;

import io.reactivestax.eviction.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CachePerformanceTest {

    private CacheFactory<Integer, String> mockFactory;
    private Cache<Integer, String> mockCache;
    private TTLEvictionPolicy<Integer, String> ttlPolicy;
    private FIFOEvictionPolicy<Integer, String> fifoPolicy;
    private LREvictionPolicy<Integer, String> lruPolicy;
    private SizedBasedEvictionPolicy<Integer, String> sizePolicy;
    private RandomReplacementEvictionPolicy<Integer, String> randomPolicy;

    @BeforeEach
    void setUp() {
        // Create mocks for the test
        mockFactory = mock(CacheFactory.class);
        mockCache = mock(Cache.class);
        ttlPolicy = mock(TTLEvictionPolicy.class);
        fifoPolicy = mock(FIFOEvictionPolicy.class);
        lruPolicy = mock(LREvictionPolicy.class);
        sizePolicy = mock(SizedBasedEvictionPolicy.class);
        randomPolicy = mock(RandomReplacementEvictionPolicy.class);
    }

    @Test
    void testRunFlow() {
        // Arrange
        CachePerformance performance = new CachePerformance();

        Cache<Integer, String> ttlCache = new Cache<>();
        Cache<Integer, String> fifoCache = new Cache<>();
        Cache<Integer, String> lruCache = new Cache<>();
        Cache<Integer, String> sizeBasedCache = new Cache<>();
        Cache<Integer, String> randomCache = new Cache<>();

        CacheFactory<Integer, String> factory = spy(new CacheFactory<>(new EvictionManager<>()));

        // Act
        performance.runFlow();

        // Assert that eviction policies are applied for each cache
        verify(factory, times(1)).applyEvictionPolicy(ttlCache, new TTLEvictionPolicy<>(), 2000);
        verify(factory, times(1)).applyEvictionPolicy(fifoCache, new FIFOEvictionPolicy<>(), 3000);
        verify(factory, times(1)).applyEvictionPolicy(lruCache, new LREvictionPolicy<>(), 2000);
        verify(factory, times(1)).applyEvictionPolicy(sizeBasedCache, new SizedBasedEvictionPolicy<>(), 3000);
        verify(factory, times(1)).applyEvictionPolicy(randomCache, new RandomReplacementEvictionPolicy<>(), 2000);

        // Additional checks for demon-thread eviction
        ttlCache.put(4, new CacheEntry<>("updated", 3000));
        ttlCache.put(2, new CacheEntry<>("updated", 4000));
    }

}