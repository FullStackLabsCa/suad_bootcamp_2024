package io.reactivestax.eviction;

import io.reactivestax.Cache;
import io.reactivestax.contract.EvictionPolicy;
import lombok.extern.slf4j.Slf4j;
import java.util.List;


@Slf4j
public class FIFOEvictionPolicy<K, V> implements EvictionPolicy<K, V> {
    @Override
    public void evict(List<Cache<K, V>> cacheList) {
        cacheList.forEach(data ->
                data.getCacheData().entrySet().stream()
                        .findFirst()
                        .ifPresent(entry -> {
                            data.getCacheData().remove(entry.getKey());
                            log.info("Evicted (FIFO): {}", entry.getKey());
                        }));
    }
}
