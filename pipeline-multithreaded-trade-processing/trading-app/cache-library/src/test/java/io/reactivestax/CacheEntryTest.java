package io.reactivestax;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class CacheEntryTest {
    @Test
    void testCacheEntryWithTtl() {
        CacheEntry<String> entry = new CacheEntry<>("TestValue", 1000L);
        assertEquals("TestValue", entry.getValue());
        assertTrue(entry.getTtl() > 0);
    }

    @Test
    void testCacheEntryWithoutTtl() {
        CacheEntry<String> entry = new CacheEntry<>("TestValue");
        assertEquals("TestValue", entry.getValue());
        assertEquals(60000L, entry.getTtl());
    }

    @Test
    void testIsExpired() throws InterruptedException {
        CacheEntry<String> entry = new CacheEntry<>("TestValue", 500L);
        Thread.sleep(600); // Wait for TTL to expire
        assertTrue(entry.isExpired());
    }

    @Test
    void testIsNotExpired() {
        CacheEntry<String> entry = new CacheEntry<>("TestValue", 2000L);
        assertFalse(entry.isExpired());
    }
}