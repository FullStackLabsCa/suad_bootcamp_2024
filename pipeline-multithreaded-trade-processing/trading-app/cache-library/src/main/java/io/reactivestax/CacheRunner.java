package io.reactivestax;

import io.reactivestax.eviction.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CacheRunner {

    public void runFlow() {
        CacheFactory factory = new CacheFactory();

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


        Cache<Integer, String> sizeBasedCache = new Cache<>();
        sizeBasedCache.put(1, new CacheEntry<>("oracle"));
        sizeBasedCache.put(2, new CacheEntry<>("postgres"));
        sizeBasedCache.put(3, new CacheEntry<>("mysql", -1));

        Cache<Integer, String> randomCache = new Cache<>();
        randomCache.put(1, new CacheEntry<>("docker"));
        randomCache.put(2, new CacheEntry<>("kubernetes"));
        randomCache.put(3, new CacheEntry<>("aws"));

        factory.applyEvictionPolicy(ttlCache, new TTLEvictionPolicy<>(), 2000);
        factory.applyEvictionPolicy(fifoCache, new FIFOEvictionPolicy<>(), 3000);
        //checking the demon thread call
        ttlCache.put(4, new CacheEntry<>("updated", 3000));
        ttlCache.put(2, new CacheEntry<>("updated", 4000));

        factory.applyEvictionPolicy(lruCache, new LREvictionPolicy<>(), 2000);
        factory.applyEvictionPolicy(ttlCache, new TTLEvictionPolicy<>(), 2000);
        factory.applyEvictionPolicy(sizeBasedCache, new SizedBasedEvictionPolicy<>(), 3000);
        factory.applyEvictionPolicy(randomCache, new RandomReplacementEvictionPolicy<>(), 2000);

        Cache<Integer, String> ttlCache1 = new Cache<>();
        ttlCache1.put(1, new CacheEntry<>("json", 3000));
        ttlCache1.put(2, new CacheEntry<>("soap", 3500));
        factory.applyEvictionPolicy(ttlCache1, new TTLEvictionPolicy<>(), 2000);
    }

    public static void main(String[] args) throws InterruptedException {
        new CacheRunner().runFlow();
        Thread.sleep(60000);
    }

}
