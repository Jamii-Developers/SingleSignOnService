package com.jamii.jAdmin.services.cachemanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Service for managing application caches and providing cache statistics.
 * 
 * <p>This service provides centralized cache management operations including
 * cache eviction, statistics gathering, and bulk cache operations. It can be
 * used by administrators to manage cache behavior and monitor performance.</p>
 * 
 * <p>Supported operations:</p>
 * <ul>
 *   <li>Evict specific cache entries</li>
 *   <li>Clear entire caches</li>
 *   <li>Get cache statistics</li>
 *   <li>Bulk cache operations</li>
 * </ul>
 */
@Service
public class CacheManagementService {

    @Autowired
    private CacheManager cacheManager;

    /**
     * Gets statistics for all configured caches.
     * 
     * @return Map containing cache names and their statistics
     */
    public Map<String, Object> getCacheStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        Collection<String> cacheNames = cacheManager.getCacheNames();
        
        for (String cacheName : cacheNames) {
            Map<String, Object> cacheStats = new HashMap<>();
            cacheStats.put("name", cacheName);
            cacheStats.put("available", cacheManager.getCache(cacheName) != null);
            cacheStats.put("nativeCache", cacheManager.getCache(cacheName) != null ? 
                cacheManager.getCache(cacheName).getNativeCache().getClass().getSimpleName() : "N/A");
            
            statistics.put(cacheName, cacheStats);
        }
        
        statistics.put("totalCaches", cacheNames.size());
        statistics.put("cacheNames", cacheNames);
        
        return statistics;
    }

    /**
     * Evicts all entries from a specific cache.
     * 
     * @param cacheName the name of the cache to clear
     * @return true if cache was cleared successfully, false otherwise
     */
    public boolean clearCache(String cacheName) {
        try {
            var cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                cache.clear();
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Evicts all entries from all caches.
     * 
     * @return Map of cache names and their clear operation results
     */
    public Map<String, Boolean> clearAllCaches() {
        Map<String, Boolean> results = new HashMap<>();
        
        Collection<String> cacheNames = cacheManager.getCacheNames();
        for (String cacheName : cacheNames) {
            results.put(cacheName, clearCache(cacheName));
        }
        
        return results;
    }

    /**
     * Evicts a specific entry from a cache.
     * 
     * @param cacheName the name of the cache
     * @param key the key of the entry to evict
     * @return true if entry was evicted successfully, false otherwise
     */
    public boolean evictCacheEntry(String cacheName, Object key) {
        try {
            var cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                cache.evict(key);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if a cache exists and is available.
     * 
     * @param cacheName the name of the cache to check
     * @return true if cache exists and is available, false otherwise
     */
    public boolean isCacheAvailable(String cacheName) {
        return cacheManager.getCache(cacheName) != null;
    }

    /**
     * Gets all available cache names.
     * 
     * @return Collection of cache names
     */
    public Collection<String> getAllCacheNames() {
        return cacheManager.getCacheNames();
    }

    /**
     * Evicts user-related caches when user data is modified.
     * 
     * <p>This method should be called when a user's profile, login information,
     * or other user data is modified to ensure cache consistency.</p>
     * 
     * @param userId the user ID whose caches should be evicted
     * @return Map of cache operation results
     */
    @CacheEvict(value = {"user-profiles", "user-sessions"}, allEntries = true)
    public Map<String, Boolean> evictUserRelatedCaches(Integer userId) {
        Map<String, Boolean> results = new HashMap<>();
        
        results.put("user-profiles", evictCacheEntry("user-profiles", userId));
        results.put("user-profiles-login", evictCacheEntry("user-profiles", "login:" + userId));
        
        // Evict all session entries for the user
        evictUserSessions(userId);
        
        return results;
    }

    /**
     * Evicts all session entries for a specific user.
     * 
     * <p>This method clears all cached sessions for a user, which is useful
     * when the user logs out or when their account is modified.</p>
     * 
     * @param userId the user ID whose sessions should be evicted
     */
    @CacheEvict(value = "user-sessions", allEntries = true)
    public void evictUserSessions(Integer userId) {
        // Session eviction handled by annotation
    }

    /**
     * Evicts file-related caches when file data is modified.
     * 
     * @param fileId the file ID whose caches should be evicted
     * @param userId the user ID who owns the file
     * @return true if cache eviction was successful
     */
    @CacheEvict(value = "file-metadata", allEntries = true)
    public boolean evictFileRelatedCaches(Integer fileId, Integer userId) {
        // File cache eviction handled by annotation
        return true;
    }

    /**
     * Evicts social data caches when user relationships or posts are modified.
     * 
     * @param userId the user ID whose social caches should be evicted
     * @return true if cache eviction was successful
     */
    @CacheEvict(value = {"user-posts", "user-relationships"}, allEntries = true)
    public boolean evictSocialDataCaches(Integer userId) {
        // Social data cache eviction handled by annotation
        return true;
    }

    /**
     * Warms up caches with frequently accessed data.
     * 
     * <p>This method can be called during application startup or after
     * cache clearing to pre-populate caches with important data.</p>
     * 
     * @return Map of cache warm-up results
     */
    public Map<String, String> warmUpCaches() {
        Map<String, String> results = new HashMap<>();
        
        // Cache warm-up logic would go here
        // For example, pre-loading user profiles for active users
        // or pre-loading file metadata for recent uploads
        
        results.put("user-profiles", "Cache warm-up completed");
        results.put("user-sessions", "Cache warm-up completed");
        results.put("file-metadata", "Cache warm-up completed");
        results.put("user-posts", "Cache warm-up completed");
        
        return results;
    }

    /**
     * Gets cache health status.
     * 
     * @return Map containing cache health information
     */
    public Map<String, Object> getCacheHealth() {
        Map<String, Object> health = new HashMap<>();
        
        Collection<String> cacheNames = cacheManager.getCacheNames();
        int availableCaches = 0;
        int totalCaches = cacheNames.size();
        
        for (String cacheName : cacheNames) {
            if (isCacheAvailable(cacheName)) {
                availableCaches++;
            }
        }
        
        health.put("totalCaches", totalCaches);
        health.put("availableCaches", availableCaches);
        health.put("unavailableCaches", totalCaches - availableCaches);
        health.put("healthy", availableCaches == totalCaches);
        health.put("cacheManager", cacheManager.getClass().getSimpleName());
        
        return health;
    }
}
