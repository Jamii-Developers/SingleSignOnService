package com.jamii.jAdmin;

import com.jamii.jAdmin.services.cachemanagement.CacheManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST peer for cache management operations.
 * 
 * <p>This peer provides administrative endpoints for managing
 * application caches. All endpoints require administrative privileges.</p>
 * 
 * <p>Available operations:</p>
 * <ul>
 *   <li>View cache statistics and health</li>
 *   <li>Clear specific caches</li>
 *   <li>Evict cache entries</li>
 *   <li>Warm up caches</li>
 * </ul>
 */
@RestController
@RequestMapping("/jadmin/cache")
@PreAuthorize("hasRole('ADMIN')")
public class CacheManagementController {

    @Autowired
    private CacheManagementService cacheManagementService;

    /**
     * Gets statistics for all configured caches.
     * 
     * @return Map containing cache statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getCacheStatistics() {
        Map<String, Object> statistics = cacheManagementService.getCacheStatistics();
        return ResponseEntity.ok(statistics);
    }

    /**
     * Gets cache health status.
     * 
     * @return Map containing cache health information
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getCacheHealth() {
        Map<String, Object> health = cacheManagementService.getCacheHealth();
        return ResponseEntity.ok(health);
    }

    /**
     * Gets all available cache names.
     * 
     * @return Collection of cache names
     */
    @GetMapping("/names")
    public ResponseEntity<java.util.Collection<String>> getAllCacheNames() {
        java.util.Collection<String> cacheNames = cacheManagementService.getAllCacheNames();
        return ResponseEntity.ok(cacheNames);
    }

    /**
     * Clears all entries from a specific cache.
     * 
     * @param cacheName the name of the cache to clear
     * @return Success message
     */
    @DeleteMapping("/{cacheName}")
    public ResponseEntity<Map<String, String>> clearCache(@PathVariable String cacheName) {
        boolean success = cacheManagementService.clearCache(cacheName);
        
        Map<String, String> response = new java.util.HashMap<>();
        if (success) {
            response.put("message", "Cache '" + cacheName + "' cleared successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Failed to clear cache '" + cacheName + "' or cache not found");
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Clears all entries from all caches.
     * 
     * @return Map of cache clear operation results
     */
    @DeleteMapping("/all")
    public ResponseEntity<Map<String, Boolean>> clearAllCaches() {
        Map<String, Boolean> results = cacheManagementService.clearAllCaches();
        return ResponseEntity.ok(results);
    }

    /**
     * Evicts a specific entry from a cache.
     * 
     * @param cacheName the name of the cache
     * @param key the key of the entry to evict
     * @return Success message
     */
    @DeleteMapping("/{cacheName}/entry/{key}")
    public ResponseEntity<Map<String, String>> evictCacheEntry(
            @PathVariable String cacheName, 
            @PathVariable String key) {
        
        boolean success = cacheManagementService.evictCacheEntry(cacheName, key);
        
        Map<String, String> response = new java.util.HashMap<>();
        if (success) {
            response.put("message", "Cache entry evicted successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Failed to evict cache entry or cache not found");
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Evicts user-related caches for a specific user.
     * 
     * @param userId the user ID whose caches should be evicted
     * @return Map of cache operation results
     */
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Map<String, Boolean>> evictUserCaches(@PathVariable Integer userId) {
        Map<String, Boolean> results = cacheManagementService.evictUserRelatedCaches(userId);
        return ResponseEntity.ok(results);
    }

    /**
     * Evicts file-related caches for a specific file.
     * 
     * @param fileId the file ID whose caches should be evicted
     * @param userId the user ID who owns the file
     * @return Success message
     */
    @DeleteMapping("/file/{fileId}/user/{userId}")
    public ResponseEntity<Map<String, String>> evictFileCaches(
            @PathVariable Integer fileId, 
            @PathVariable Integer userId) {
        
        boolean success = cacheManagementService.evictFileRelatedCaches(fileId, userId);
        
        Map<String, String> response = new java.util.HashMap<>();
        if (success) {
            response.put("message", "File caches evicted successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Failed to evict file caches");
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Evicts social data caches for a specific user.
     * 
     * @param userId the user ID whose social caches should be evicted
     * @return Success message
     */
    @DeleteMapping("/social/{userId}")
    public ResponseEntity<Map<String, String>> evictSocialCaches(@PathVariable Integer userId) {
        boolean success = cacheManagementService.evictSocialDataCaches(userId);
        
        Map<String, String> response = new java.util.HashMap<>();
        if (success) {
            response.put("message", "Social data caches evicted successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Failed to evict social data caches");
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Warms up caches with frequently accessed data.
     * 
     * @return Map of cache warm-up results
     */
    @PostMapping("/warmup")
    public ResponseEntity<Map<String, String>> warmUpCaches() {
        Map<String, String> results = cacheManagementService.warmUpCaches();
        return ResponseEntity.ok(results);
    }

    /**
     * Checks if a specific cache is available.
     * 
     * @param cacheName the name of the cache to check
     * @return Cache availability status
     */
    @GetMapping("/{cacheName}/available")
    public ResponseEntity<Map<String, Object>> checkCacheAvailability(@PathVariable String cacheName) {
        boolean available = cacheManagementService.isCacheAvailable(cacheName);
        
        Map<String, Object> response = new java.util.HashMap<>();
        response.put("cacheName", cacheName);
        response.put("available", available);
        
        return ResponseEntity.ok(response);
    }
}
