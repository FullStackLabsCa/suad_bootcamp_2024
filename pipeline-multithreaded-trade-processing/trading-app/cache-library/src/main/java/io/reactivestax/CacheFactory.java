package io.reactivestax;

import io.reactivestax.contract.EvictionPolicy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CacheFactory<K,V> {

    public Cache<K, V> createCacheWithEviction(EvictionPolicy<K, V> policy, long evictionInterval) {
        Cache<K, V> cache = new Cache<>();
        Thread evictionThread = new Thread(() -> {
            while(true){
                try{
                    policy.evict(cache);
                    Thread.sleep(evictionInterval);
                }catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                    log.warn("Eviction thread interrupted");
                    break;
                }
            }
        });

        evictionThread.setDaemon(true);
        evictionThread.start();
        return cache;
    }
}
