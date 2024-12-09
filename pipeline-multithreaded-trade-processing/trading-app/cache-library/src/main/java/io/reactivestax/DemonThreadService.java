package io.reactivestax;

import io.reactivestax.contract.EvictionPolicy;
import lombok.extern.slf4j.Slf4j;


import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class DemonThreadService {
    public static <K, V>  AtomicBoolean spawnDemonOnEviction(Cache<K, V> cache, EvictionPolicy<K, V> policy, long evictionInterval) {
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
        return isRunning;
    }

}
