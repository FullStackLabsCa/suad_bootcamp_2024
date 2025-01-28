package io.reactivestax.contract;

import io.reactivestax.CacheEntry;

import java.util.concurrent.ConcurrentHashMap;

public interface EvictionPolicy<K, V> {

    void onAcess(K key);

    void onAdd(K key);

    void onRemove(K key);

    K evict();

    Runnable spawnDaemonThread(ConcurrentHashMap<K, CacheEntry<V>> cache, long checkInterval);
}
