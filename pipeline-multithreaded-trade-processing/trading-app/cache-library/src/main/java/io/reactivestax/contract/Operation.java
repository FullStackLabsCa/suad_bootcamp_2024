package io.reactivestax.contract;

import io.reactivestax.CacheEntry;

import java.util.Set;

public interface Operation<K, V> {
    void put(K key, CacheEntry<V> value);

    CacheEntry<V> get(K key);

    boolean remove(K key);

    int size();

    void clear();

    Set<K> keys();
}
