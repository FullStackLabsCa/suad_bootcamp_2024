package io.reactivestax;

import io.reactivestax.contract.EvictionPolicy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CacheFactory<K,V> {

    private final EvictionManager<K, V> evictionManager;

    public CacheFactory(EvictionManager<K, V> evictionManager) {
        this.evictionManager = evictionManager;
    }

    public void applyEvictionPolicy(Cache<K, V> cache, EvictionPolicy<K, V> policy, long evictionInterval) {
        evictionManager.registerEvictionPolicy(cache, policy, evictionInterval);
    }
}
