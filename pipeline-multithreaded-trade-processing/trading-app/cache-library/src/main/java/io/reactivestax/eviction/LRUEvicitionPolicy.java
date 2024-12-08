package io.reactivestax.eviction;

import io.reactivestax.Cache;
import io.reactivestax.contract.EvictionPolicy;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;

@Slf4j
public class LRUEvicitionPolicy<K, V> implements EvictionPolicy<K,V> {
    @Override
    public void evict(Cache<K, V> cache) {
        cache.getCacheData().entrySet().stream()
                .min(Comparator.comparing(entry -> entry.getValue().getLastAccessTime()))
                .ifPresent(entry -> {
                    cache.getCacheData().remove(entry.getKey());
                    log.info("Evicted (LRU): {}", entry.getKey());
                });
    }
}
