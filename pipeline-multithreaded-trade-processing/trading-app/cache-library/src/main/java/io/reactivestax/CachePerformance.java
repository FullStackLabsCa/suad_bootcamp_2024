package io.reactivestax;

import io.reactivestax.eviction.FIFOEvicitionPolicy;
import io.reactivestax.eviction.LRUEvicitionPolicy;
import io.reactivestax.eviction.TTLEvictionPolicy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CachePerformance {


    public void runFlow() {
        CacheFactory<Integer, String> factory = new CacheFactory<>();
        Cache<Integer, String> ttlCache = factory.createCacheWithEviction(new TTLEvictionPolicy<>(), 2000);
        ttlCache.put(1, new CacheEntry<>("physics", 2000));
        ttlCache.put(2, new CacheEntry<>("chemistry", 4000));
        ttlCache.put(3, new CacheEntry<>("english", -1));


        Cache<Integer, String> lruCache = factory.createCacheWithEviction(new LRUEvicitionPolicy<>(), 2000);
        lruCache.put(1, new CacheEntry<>("math", 3000));
        ttlCache.put(2, new CacheEntry<>("biology", 5000));
        ttlCache.put(3, new CacheEntry<>("tech-stream", -1));


        Cache<Integer, String> fifoCache = factory.createCacheWithEviction(new FIFOEvicitionPolicy<>(), 2000);
        fifoCache.put(1, new CacheEntry<>("java"));
        fifoCache.put(2, new CacheEntry<>("kotlin"));

    }

    public static void main(String[] args) throws InterruptedException {
        new CachePerformance().runFlow();
        Thread.sleep(60000);
    }
}
