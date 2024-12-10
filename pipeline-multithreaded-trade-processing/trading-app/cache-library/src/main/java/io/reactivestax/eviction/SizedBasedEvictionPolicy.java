package io.reactivestax.eviction;

import io.reactivestax.Cache;
import io.reactivestax.CacheEntry;
import io.reactivestax.contract.EvictionPolicy;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class SizedBasedEvictionPolicy<K, V> implements EvictionPolicy<K, V> {

    private final int maxSize = 2;

    @Override
    public void evict(List<Cache<K, V>> cache) {
        cache.forEach(cacheData -> {
            while (cacheData.size() > maxSize) {
                Iterator<Map.Entry<K, CacheEntry<V>>> iterator = cacheData.getCacheData().entrySet().iterator();
                if (iterator.hasNext()) {
                    Map.Entry<K, CacheEntry<V>> entry = iterator.next();
                    cacheData.remove(entry.getKey());
                    log.info("Evicted (SBE): {}", entry.getKey());
                }
            }
        });
    }
}
