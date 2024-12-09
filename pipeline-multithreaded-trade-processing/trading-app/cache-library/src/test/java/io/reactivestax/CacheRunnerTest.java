package io.reactivestax;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CacheRunnerTest {

    @Test
    void testRunFlow() {

        // Arrange
        CacheRunner performance = new CacheRunner();

        Cache<Integer, String> ttlCache = new Cache<>();
        // Act
        performance.runFlow();

        ttlCache.put(1, new CacheEntry<>("updated", 1000));
        ttlCache.put(2, new CacheEntry<>("updateChecked", 1500));

        assertEquals(2, ttlCache.size());
    }

}