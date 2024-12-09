package io.reactivestax;

import io.reactivestax.eviction.FIFOEvicitionPolicy;
import io.reactivestax.eviction.LRUEvicitionPolicy;
import io.reactivestax.eviction.TTLEvictionPolicy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CachePerformance {


    public void runFlow() {
        CacheFactory<Integer, String> factory = new CacheFactory<>();
        
        Cache<Integer, String> ttlCache = new Cache<>();
        ttlCache.put(1, new CacheEntry<>("physics", 5000));
        ttlCache.put(2, new CacheEntry<>("chemistry", 6000));
        ttlCache.put(3, new CacheEntry<>("english", 2000));

        Cache<Integer, String> fifoCache = new Cache<>();
        fifoCache.put(1, new CacheEntry<>("bio", 5000));
        fifoCache.put(2, new CacheEntry<>("math", 6000));
        fifoCache.put(3, new CacheEntry<>("engineering", -1));

        Cache<Integer, String> lruCache = new Cache<>();
        lruCache.put(1, new CacheEntry<>("java", 5000));
        lruCache.put(2, new CacheEntry<>("kotlin", 6000));

        factory.applyEvictionPolicy(ttlCache, new TTLEvictionPolicy<>(), 2000);
        factory.applyEvictionPolicy(fifoCache, new FIFOEvicitionPolicy<>(), 3000);
        ttlCache.put(4, new CacheEntry<>("updated", 3000));
        ttlCache.put(2, new CacheEntry<>("updated", 4000));
        factory.applyEvictionPolicy(lruCache, new LRUEvicitionPolicy<>(), 2000);
        factory.applyEvictionPolicy(ttlCache, new TTLEvictionPolicy<>(), 2000);

    }

    public static void main(String[] args) throws InterruptedException {
        new CachePerformance().runFlow();
        Thread.sleep(60000);
    }
}
