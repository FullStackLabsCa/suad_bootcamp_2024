package io.reactivestax.eviction;

import io.reactivestax.Cache;
import io.reactivestax.contract.EvictionPolicy;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class TTLEvictionPolicy<K, V> implements EvictionPolicy<K, V> {

    @Override
    public void evict(List<Cache<K, V>> cache) {
        cache.forEach(cacheData ->
                cacheData.getCacheData().entrySet().removeIf(entry -> {
                    boolean expired = entry.getValue().isExpired();
                    if (expired) {
                        log.info("Evicted (TTL) : {}", entry.getKey());
                    }
                    return expired;
                }));
    }
}
