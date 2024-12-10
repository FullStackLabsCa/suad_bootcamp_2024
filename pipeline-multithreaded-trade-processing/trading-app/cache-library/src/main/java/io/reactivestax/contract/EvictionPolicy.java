package io.reactivestax.contract;

import io.reactivestax.Cache;

import java.util.List;

public interface EvictionPolicy<K, V> {
 void evict(List<Cache<K, V>> cacheList);
}
