package io.reactivestax;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CacheTest {

    private Cache<Integer, String> cache;

    @BeforeEach
    void setUp() {
        cache = new Cache<>();
    }

    @Test
    void testPutAndGet() {
        CacheEntry<String> entry = new CacheEntry<>("TestValue", 1000L);
        cache.put(1, entry);
        assertEquals("TestValue", cache.get(1).getValue());
    }

    @Test
    void testRemove() {
        CacheEntry<String> entry = new CacheEntry<>("TestValue", 1000L);
        cache.put(1, entry);
        assertTrue(cache.remove(1));
        assertFalse(cache.cacheData.containsKey(1));
    }


        @Test
        void testRemoveKeyWithNegativeTTL() {
            // Create a Cache instance
            Cache<String, String> cache = new Cache<>();

            // Add a key-value pair with ttl = -1
            CacheEntry<String> cacheEntry = new CacheEntry<>("value1", -1); // TTL set to -1
            cache.put("key1", cacheEntry);

            // Attempt to remove the key
            boolean result = cache.remove("key1");

            // Assert that remove returns false
            assertFalse(result, "Remove should return false for key with ttl = -1");

            // Assert that the key still exists in the cache
            assertTrue(cache.cacheData.containsKey("key1"), "Key should not be removed when ttl = -1");
        }

    @Test
    void testRemoveNonExistentKey() {
        assertFalse(cache.remove(99));
    }

    @Test
    void testClear() {
        cache.put(1, new CacheEntry<>("Value1"));
        cache.put(2, new CacheEntry<>("Value2"));
        cache.clear();
        assertEquals(0, cache.size());
    }

    @Test
    void testKeys() {
        cache.put(1, new CacheEntry<>("Value1"));
        cache.put(2, new CacheEntry<>("Value2"));
        assertEquals(2, cache.keys().size());
    }

    @Test
    void testSize() {
        cache.put(1, new CacheEntry<>("Value1"));
        assertEquals(1, cache.size());
    }

}