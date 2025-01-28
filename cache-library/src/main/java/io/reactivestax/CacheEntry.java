package io.reactivestax;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class CacheEntry<V> {

    private V value;
    private long ttl;
    private long lastAccessTime;

    public CacheEntry(V value, long ttl) {
        this.value = value;
        this.ttl = ttl;
        this.lastAccessTime = System.currentTimeMillis();
    }

    public CacheEntry(V value) {
        this(value, 60000); // Default ttl of 60 seconds
    }

    public boolean isExpired() {
        log.info("value and ttl : {} : {} ", value, ttl);
        return ttl > 0 && (System.currentTimeMillis() > lastAccessTime + ttl);
    }
}
