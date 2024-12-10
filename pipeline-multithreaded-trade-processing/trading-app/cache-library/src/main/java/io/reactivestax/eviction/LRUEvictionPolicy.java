package io.reactivestax.eviction;

import io.reactivestax.Cache;
import io.reactivestax.contract.EvictionPolicy;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;

@Slf4j
public class LRUEvictionPolicy<K, V> implements EvictionPolicy<K,V> {
    @Override
    public void evict(List<Cache<K, V>> cache) {
        cache.forEach(cacheData ->
                cacheData.getCacheData().entrySet().stream()
                .min(Comparator.comparing(entry -> entry.getValue().getLastAccessTime()))
                .ifPresent(entry -> {
                    cacheData.getCacheData().remove(entry.getKey());
                    log.info("Evicted (LRU): {}", entry.getKey());
                }));
    }
}
