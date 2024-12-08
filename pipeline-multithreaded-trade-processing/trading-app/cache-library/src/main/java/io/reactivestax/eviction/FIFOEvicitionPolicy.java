package io.reactivestax.eviction;

import io.reactivestax.Cache;
import io.reactivestax.contract.EvictionPolicy;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class FIFOEvicitionPolicy<K, V> implements EvictionPolicy<K,V> {
    @Override
    public void evict(Cache<K, V> cache) {
        cache.getCacheData().entrySet().stream()
                .findFirst()
                .ifPresent(entry -> {
                    cache.getCacheData().remove(entry.getKey());
                    log.info("Evicted (FIFO): {}", entry.getKey());
                });
    }
}
