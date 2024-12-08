package io.reactivestax.contract;

import io.reactivestax.CacheEntry;

@FunctionalInterface
public interface EvictionCheck<K, V> {
    boolean shouldEvict(K key, CacheEntry<V> entry);
}
