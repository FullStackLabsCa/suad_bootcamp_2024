package io.reactivestax;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CachePerformance {

    public Cache<Integer, String> cache = new Cache<>("LRU");


    public void addData() {

        //TTL based implementation
        CacheEntry<String> physics = new CacheEntry<>("physics", 2000L);
        CacheEntry<String> chemistry = new CacheEntry<>("chemistry", 4000L);
        CacheEntry<String> math = new CacheEntry<>("math", 6000L);
        CacheEntry<String> biology = new CacheEntry<>("biology");
        cache.put(1, physics);
        log.info("is expired 1: {}", cache.getCacheData().get(1).isExpired());
        cache.put(2, chemistry);
        log.info("is expired 2: {}", cache.getCacheData().get(2).isExpired());
        cache.put(3, math);
        log.info("is expired 3: {}", cache.getCacheData().get(3).isExpired());
        cache.put(4, biology);
        log.info("is expired 4: {}", cache.getCacheData().get(3).isExpired());
    }



    public void runFlow() {
        addData();
        log.info("Size is : {}", cache.size());

    }

    public static void main(String[] args) {
        new CachePerformance().runFlow();
        // Keep the main thread alive
        try {
            Thread.sleep(700000); // Keep the application running for 30 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
