package com.jamii.administrative.controllers;

import com.jamii.administrative.model.ApiErrorLogTBL;
import com.jamii.administrative.repo.ApiErrorLogREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Service component for managing API error log records in the database.
 * 
 * <p>This class provides CRUD operations for the {@link ApiErrorLogTBL} entity,
 * allowing the application to log and retrieve error information for debugging and monitoring purposes.</p>
 * 
 * <p>Key functionality:</p>
 * <ul>
 *     <li>Save individual error records</li>
 *     <li>Save multiple error records in batch</li>
 * </ul>
 */
@Component
public class ApiErrorLog
{

    /**
     * Error type constant for exception errors.
     */
    public static final Integer ERROR_TYPE_EXCEPTION = 1;

    /**
     * Single error record instance for creating new entries.
     */
    public ApiErrorLogTBL data;
    
    /**
     * List of error records for batch operations.
     */
    public ArrayList<ApiErrorLogTBL> dataList;
    
    @Autowired private ApiErrorLogREPO apiErrorLogREPO;

    /**
     * Default constructor.
     */
    public ApiErrorLog() {}

    /**
     * Saves the current error record to the database.
     * 
     * <p>The record is saved using the {@link ApiErrorLogREPO} and the saved entity
     * (with generated ID) is stored back in the {@code data} field.</p>
     */
    public void save()
    {
        data = apiErrorLogREPO.save(data);
    }

    /**
     * Saves all error records in the {@code dataList} to the database in a batch operation.
     * 
     * <p>After saving, the list is cleared and repopulated with the saved entities
     * (including any generated IDs).</p>
     */
    public void saveAll()
    {
        Iterable<ApiErrorLogTBL> datalist = this.apiErrorLogREPO.saveAll(dataList);
        dataList.clear();
        datalist.forEach(x -> dataList.add(x));
    }
}
