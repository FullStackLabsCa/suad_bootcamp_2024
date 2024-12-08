package io.reactivestax.contract;

import io.reactivestax.Cache;
import io.reactivestax.CacheEntry;

import java.util.concurrent.ConcurrentHashMap;

public interface EvictionPolicy<K, V> {
 void evict(Cache<K, V> cache);
}
