package io.reactivestax;

import io.reactivestax.contract.EvictionPolicy;
import io.reactivestax.eviction.*;

import java.util.concurrent.ConcurrentHashMap;

public class CacheFactory {

    public static <K, V> EvictionPolicy<K, V> getEvictionPolicy(String policyType, ConcurrentHashMap<K, CacheEntry<V>> cache, long checkInterval) {
        EvictionPolicy<K, V> policy = null;
        switch (policyType.toUpperCase()) {
            case "LRU" -> new LruEvictionPolicy<>();
            case "FIFO" -> new FifoEvictionPolicy<>();
            case "LFU" -> new LfuEvictionPolicy<>();
            case "RR" -> new RandomEvictionPolicy<>();
            case "SIZE" -> new SizeBasedEvictionPolicy<>();
            // Add other eviction policies here
            default -> throw new IllegalArgumentException("Unknown policy: " + policyType);
        }

        // Assign daemon thread
        Thread daemonThread = new Thread(policy.spawnDaemonThread(cache, checkInterval));
        daemonThread.setDaemon(true); // Make it a daemon thread
        daemonThread.start();

        return policy;
    }
}
