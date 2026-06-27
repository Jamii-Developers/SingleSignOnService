package com.jamii.jAdmin.model.AUXUtils;

import com.google.gson.Gson;

/**
 * Utility class for structuring auxiliary data for API error logs.
 * 
 * <p>This class is used to store additional information about API errors,
 * such as stack traces, in a structured format that can be serialized to JSON.</p>
 */
public class ApiErrorLog_AUX_DATA
{

    /**
     * Stack trace information for the error.
     */
    private String stackTrace;

    /**
     * Gets the stack trace.
     * @return the stack trace
     */
    public String getStackTrace()
    {
        return stackTrace;
    }

    /**
     * Sets the stack trace.
     * @param stackTrace the stack trace to set
     */
    public void setStackTrace(String stackTrace)
    {
        this.stackTrace = stackTrace;
    }

    /**
     * Converts this object to a JSON string representation.
     * @return JSON string representation of this object
     */
    public String converToJSON()
    {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
