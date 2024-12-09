package io.reactivestax.eviction;

import io.reactivestax.Cache;
import io.reactivestax.contract.EvictionPolicy;
import lombok.extern.slf4j.Slf4j;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Slf4j
public class RandomReplacementEvictionPolicy<K,V> implements EvictionPolicy<K,V>{

    private final Random random = new Random();
    @Override
    public void evict(Cache<K, V> cache) {
         List<K> keys = new ArrayList<>(cache.keys());
        if(!keys.isEmpty()){
             K randomKey = keys.get(random.nextInt(keys.size()));
             cache.remove(randomKey);
            log.info("Evicted (RRE): {}", randomKey);
         }
    }
}
