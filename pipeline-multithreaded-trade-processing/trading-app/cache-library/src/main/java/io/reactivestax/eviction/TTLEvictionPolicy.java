package io.reactivestax.eviction;

import io.reactivestax.Cache;
import io.reactivestax.contract.EvictionPolicy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TTLEvictionPolicy<K,V> implements EvictionPolicy<K,V>{

    @Override
    public void evict(Cache<K, V> cache) {
        cache.getCacheData().entrySet().removeIf(entry -> {
            boolean expired = entry.getValue().isExpired();
            if(expired){
                log.info("Evicted (TTL) : {}", entry.getKey());
            }
            return expired;
        });
    }
}
