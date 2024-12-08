package io.reactivestax;

import io.reactivestax.contract.EvictionPolicy;
import io.reactivestax.contract.Operation;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
public class Cache<K, V> implements Operation<K, V> {

    @Getter
    public ConcurrentHashMap<K, CacheEntry<V>> cacheData = new ConcurrentHashMap<>();
    private final EvictionPolicy<K, V> evictionPolicy;

    public Cache(String policyType) {
        this.evictionPolicy = CacheFactory.getEvictionPolicy(policyType, cacheData, 2000);
    }

    //Make sure to make the cacheEntry object with the TTL
    @Override
    public void put(K key, CacheEntry<V> cacheEntry) {
        cacheData.put(key, cacheEntry);
        evictionPolicy.onAdd(key);
    }

    @Override
    public CacheEntry<V> get(K key) {
        cacheData.get(key).setTtl(cacheData.get(key).getTtl());
        return cacheData.get(key);
    }

    @Override
    public boolean remove(K key) {
        if (cacheData.containsKey(key)) {
            if (cacheData.get(key).getTtl() == -1) {
                return false;
            }
            cacheData.remove(key);
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return cacheData.size();
    }

    @Override
    public void clear() {
        cacheData.clear();
    }

    @Override
    public Set<K> keys() {
        return cacheData.keySet();
    }
}