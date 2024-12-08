package io.reactivestax;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CachePerformance {

    private volatile boolean isRunning = true;

    public Cache<Integer, String> cache = new Cache<>();


    public void addData() {
        //TTL based implementation
        CacheEntry<String> physics = new CacheEntry<>("physics", 2000L);
        CacheEntry<String> chemistry = new CacheEntry<>("chemistry", 4000L);
        CacheEntry<String> math = new CacheEntry<>("math", 6000L);
        CacheEntry<String> biology = new CacheEntry<>("biology");
        cache.put(1, physics);
        log.info("is expired 1: {}", cache.getCacheData().get(1).isExpired());
        cache.put(2, chemistry);
        log.info("is expired 2: {}", cache.getCacheData().get(2).isExpired());
        cache.put(3, math);
        log.info("is expired 3: {}", cache.getCacheData().get(3).isExpired());
        cache.put(4, biology);
        log.info("is expired 4: {}", cache.getCacheData().get(3).isExpired());
    }



    public void runFlow() {
        addData();
        log.info("Size is : {}", cache.size());
        spawnDemonThread();

    }

    public static void main(String[] args) {
        CachePerformance cachePerformance = new CachePerformance();
        cachePerformance.startApplication(65000); // Run for 65 secon
    }

    public void startApplication(long runtimeMillis) {
        spawnDemonThread();
        try {
            Thread.sleep(runtimeMillis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        stopDaemonThread();
    }

    public void stopDaemonThread() {
        isRunning = false;
    }

    public void spawnDemonThread(){
        Thread cleanupThread = new Thread(() -> {
            while (true) {
                try {
                    cleanupExpiredEntries();
                    sleep(2000); // Check every 2 seconds
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        cleanupThread.setDaemon(true);
        cleanupThread.start();
    }

    protected void sleep(long millis) throws InterruptedException {
        Thread.sleep(millis);
    }

    public void cleanupExpiredEntries() {
        cache.cacheData.entrySet().removeIf(entry -> {
            boolean expired = entry.getValue().isExpired();
            if (expired) {
                log.info("Removed after expiring the TTL: {}", entry.getValue().getValue());
            }
            return expired;
        });
    }

}
