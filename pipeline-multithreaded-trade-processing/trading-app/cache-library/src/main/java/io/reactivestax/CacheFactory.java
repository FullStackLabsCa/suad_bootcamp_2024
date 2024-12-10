package io.reactivestax;

import io.reactivestax.contract.EvictionPolicy;
import io.reactivestax.eviction.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class CacheFactory<K, V> {

    final Map<String, AtomicBoolean> evictionTasks = new ConcurrentHashMap<>();
    final List<Cache<K, V>> fifoCacheList = new ArrayList<>();
    final List<Cache<K, V>> lruCacheList = new ArrayList<>();
    final List<Cache<K, V>> rrCacheList = new ArrayList<>();
    final List<Cache<K, V>> sizeBasedCacheList = new ArrayList<>();
    final List<Cache<K, V>> ttlCacheList = new ArrayList<>();


    public void applyEvictionPolicy(Cache<K, V> cache, EvictionPolicy<K, V> policy, long evictionInterval) {
        List<Cache<K, V>> caches = checkInstance(policy);
        caches.add(cache);
        String uniqueKey = generateUniqueKey(policy);
            evictionTasks.computeIfAbsent(uniqueKey, key -> {
            log.info("hit the threads...");
            return DemonThreadService.spawnDemonOnEviction(caches, policy, evictionInterval);
        });
    }

    public List<Cache<K, V>> checkInstance(EvictionPolicy<K, V> policy) {
        if (policy instanceof FIFOEvictionPolicy) {
            return fifoCacheList;
        } else if (policy instanceof LRUEvictionPolicy<K, V>) {
            return lruCacheList;
        } else if (policy instanceof RandomReplacementEvictionPolicy<K, V>) {
            return rrCacheList;
        } else if (policy instanceof SizedBasedEvictionPolicy<K, V>) {
            return sizeBasedCacheList;
        } else if (policy instanceof TTLEvictionPolicy<K, V>) {
            return ttlCacheList;
        }
        return Collections.emptyList();
    }

    String generateUniqueKey(EvictionPolicy<K, V> policy) {
        return policy.getClass().getName();
    }
}
