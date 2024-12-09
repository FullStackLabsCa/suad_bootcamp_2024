package io.reactivestax.contract;

import io.reactivestax.Cache;

public interface EvictionPolicy<K, V> {
 void evict(Cache<K, V> cache);
}
