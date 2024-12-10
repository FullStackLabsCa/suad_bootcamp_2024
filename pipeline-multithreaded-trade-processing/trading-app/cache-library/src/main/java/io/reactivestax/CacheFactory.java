package io.reactivestax;

import io.reactivestax.contract.EvictionPolicy;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class CacheFactory {

    final Map<String, AtomicBoolean> evictionTasks = new ConcurrentHashMap<>();

    public <K, V> void applyEvictionPolicy(Cache<K, V> cache, EvictionPolicy<K, V> policy, long evictionInterval) {
        String uniqueKey = generateUniqueKey(cache, policy);
        evictionTasks.computeIfAbsent(uniqueKey, key -> {
            log.info("hit the threads...");
            return  DemonThreadService.spawnDemonOnEviction(cache, policy, evictionInterval);
        });
    }

    <K, V> String generateUniqueKey(Cache<K, V> cache,EvictionPolicy<K, V> policy) {
        return cache.getCacheData().hashCode() + policy.getClass().getName();
    }
}
