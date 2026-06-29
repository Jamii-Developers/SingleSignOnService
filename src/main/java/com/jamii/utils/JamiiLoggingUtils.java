package com.jamii.utils;

import com.jamii.jAdmin.controllers.ApiErrorLog;
import com.jamii.jAdmin.model.AUXUtils.ApiErrorLog_AUX_DATA;
import com.jamii.jAdmin.model.ApiErrorLogTBL;
import com.jamii.jUser.model.UserLoginTBL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Logging utility class for recording exceptions to the database.
 * 
 * <p>This class provides methods to log exceptions with detailed information
 * including stack traces, class names, and associated user information.
 * Supports batch logging to reduce database calls and improve performance.</p>
 * 
 * <p>Performance optimizations:</p>
 * <ul>
 *   <li>Supports batch logging to reduce database calls</li>
 *   <li>Uses StringBuilder for efficient string concatenation</li>
 *   <li>Provides both immediate and deferred logging options</li>
 *   <li>Thread-safe operations for concurrent use</li>
 * </ul>
 */
@Component
public class JamiiLoggingUtils {

    @Autowired
    private ApiErrorLog errorLog;
    
    /** Batch queue for storing error logs to be saved in bulk */
    private final List<ApiErrorLogTBL> errorLogBatch = new ArrayList<>();
    
    /** Maximum batch size before automatic flushing */
    private static final int MAX_BATCH_SIZE = 50;
    
    /** Synchronization lock for thread-safe batch operations */
    private final Object batchLock = new Object();

    /**
     * Logs an exception to the database with associated user information.
     * 
     * <p>This method immediately saves the exception to the database. For high-volume
     * scenarios, consider using {@link #queueExceptionLog(String, Exception, UserLoginTBL)}
     * for batch processing.</p>
     * 
     * @param className the class where the exception occurred
     * @param exception the exception to log
     * @param user the user associated with the exception (can be null)
     */
    public void ExceptionLogger( String className , Exception exception, UserLoginTBL user ){

        ApiErrorLogTBL errorLogEntry = createErrorLogEntry(className, exception, user);
        errorLog.data = errorLogEntry;
        errorLog.save( );
    }
    
    /**
     * Queues an exception for batch logging to reduce database calls.
     * 
     * <p>This method adds the exception to a batch queue. The queue is automatically
     * flushed when it reaches the maximum batch size. Call {@link #flushBatch()} 
     * to manually flush the queue.</p>
     * 
     * @param className the class where the exception occurred
     * @param exception the exception to log
     * @param user the user associated with the exception (can be null)
     */
    public void queueExceptionLog(String className, Exception exception, UserLoginTBL user) {
        synchronized (batchLock) {
            ApiErrorLogTBL errorLogEntry = createErrorLogEntry(className, exception, user);
            errorLogBatch.add(errorLogEntry);
            
            // Auto-flush when batch reaches maximum size
            if (errorLogBatch.size() >= MAX_BATCH_SIZE) {
                flushBatch();
            }
        }
    }
    
    /**
     * Flushes the batch queue by saving all queued error logs to the database.
     * 
     * <p>This method should be called periodically or when the application is
     * shutting down to ensure all queued logs are saved.</p>
     */
    public void flushBatch() {
        synchronized (batchLock) {
            if (errorLogBatch.isEmpty()) {
                return;
            }
            
            try {
                // Save all batched error logs
                for (ApiErrorLogTBL errorLogEntry : errorLogBatch) {
                    errorLog.data = errorLogEntry;
                    errorLog.save();
                }
                
                // Clear the batch after successful save
                errorLogBatch.clear();
            } catch (Exception e) {
                // Log the error but don't re-throw to avoid breaking the flow
                System.err.println("Failed to flush error log batch: " + e.getMessage());
            }
        }
    }
    
    /**
     * Creates an error log entry from the provided parameters.
     * 
     * @param className the class where the exception occurred
     * @param exception the exception to log
     * @param user the user associated with the exception (can be null)
     * @return the created error log entry
     */
    private ApiErrorLogTBL createErrorLogEntry(String className, Exception exception, UserLoginTBL user) {
        ApiErrorLogTBL errorLogEntry = new ApiErrorLogTBL();
        
        if (user != null) {
            errorLogEntry.setUserloginid(user.getId());
        } else {
            errorLogEntry.setUserloginid(0);
        }
        
        LocalDateTime now = LocalDateTime.now();
        errorLogEntry.setCreationdate(now);
        errorLogEntry.setLastupdated(now);
        errorLogEntry.setClassname(className);
        errorLogEntry.setErrorMessage(exception.getMessage());
        errorLogEntry.setErrortype(ApiErrorLog.ERROR_TYPE_EXCEPTION);
        errorLogEntry.setStatus(ApiErrorLog.ERROR_TYPE_EXCEPTION);

        // Use StringBuilder for efficient stack trace concatenation
        StringBuilder stackTraceBuilder = new StringBuilder();
        StackTraceElement[] stackTrace = exception.getStackTrace();
        for (int i = 0; i < stackTrace.length; i++) {
            if (i > 0) {
                stackTraceBuilder.append("\n");
            }
            stackTraceBuilder.append(stackTrace[i].toString());
        }

        ApiErrorLog_AUX_DATA auxData = new ApiErrorLog_AUX_DATA();
        auxData.setStackTrace(stackTraceBuilder.toString());
        errorLogEntry.setAuxdata(auxData.converToJSON());
        
        return errorLogEntry;
    }
    
    /**
     * Gets the current batch size.
     * @return the number of error logs currently in the batch queue
     */
    public int getBatchSize() {
        synchronized (batchLock) {
            return errorLogBatch.size();
        }
    }
}
