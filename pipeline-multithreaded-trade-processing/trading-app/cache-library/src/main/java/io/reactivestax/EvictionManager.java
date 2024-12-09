package io.reactivestax;

import io.reactivestax.contract.EvictionPolicy;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class EvictionManager<K, V> {

    final Map<String, AtomicBoolean> evictionTasks = new ConcurrentHashMap<>();
    final Map<String, Thread> evictionThreads = new ConcurrentHashMap<>();


    public void registerEvictionPolicy(Cache<K, V> cache, EvictionPolicy<K, V> policy, long evictionInterval) {

        String uniqueKey = generateUniqueKey(policy);

        evictionTasks.computeIfAbsent(uniqueKey, key -> {
            log.info("hit the threads...");
            AtomicBoolean isRunning = new AtomicBoolean(true);

            Thread evictionThread = new Thread(() -> {
                while (isRunning.get()) {
                    try {
                        policy.evict(cache);
                        Thread.sleep(evictionInterval);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        isRunning.set(false);
                        log.warn("Eviction thread interrupted");
                    }
                }
            });
            evictionThread.setDaemon(true);
            evictionThread.start();
            evictionThreads.put(uniqueKey, evictionThread);
            return isRunning;
        });
    }

    String generateUniqueKey(EvictionPolicy<K, V> policy) {
        return policy.getClass().getName();
    }
}
