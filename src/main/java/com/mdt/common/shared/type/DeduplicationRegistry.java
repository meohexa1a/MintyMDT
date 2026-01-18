package com.mdt.common.shared.type;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.NonNull;

import java.util.concurrent.TimeUnit;

public final class DeduplicationRegistry<K> {
    private final Cache<@NonNull K, Boolean> cache;

    // !----------------------------------------------------------------!

    public DeduplicationRegistry(long expireAfterMillis, long maxSize) {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(expireAfterMillis, TimeUnit.MILLISECONDS)
                .maximumSize(maxSize)
                .build();
    }

    // !----------------------------------------------------------------!

    public boolean markIfNew(@NonNull K key) {
        if (cache.getIfPresent(key) != null) return false;

        cache.put(key, Boolean.TRUE);
        return true;
    }
}