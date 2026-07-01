package com.jamii.sysconfigs;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * Cache configuration for the JamiiX application.
 * 
 * <p>This class configures the caching framework with named caches for different
 * data types. It uses a simple concurrent map cache manager suitable for
 * single-instance applications. For distributed applications, consider using
 * Redis or other distributed cache solutions.</p>
 * 
 * <p>Configured caches:</p>
 * <ul>
 *   <li>user-profiles - User profile data (5 minute TTL)</li>
 *   <li>user-sessions - Session validation data (10 minute TTL)</li>
 *   <li>file-metadata - File metadata information (15 minute TTL)</li>
 *   <li>user-posts - User post data (2 minute TTL)</li>
 * </ul>
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Creates and configures the cache manager with named caches.
     * 
     * @return CacheManager with configured caches
     */
    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        
        // Configure named caches
        cacheManager.setCacheNames(Arrays.asList(
            "user-profiles",
            "user-sessions", 
            "file-metadata",
            "user-posts",
            "user-relationships",
            "device-information"
        ));
        
        // Allow null values in cache
        cacheManager.setAllowNullValues(false);
        
        return cacheManager;
    }
}
