package com.jamii.jamiidb.model.AUXUtils;

import com.google.gson.Gson;

public class ApiErrorLog_AUX_DATA
{

    private String stackTrace;

    public String getStackTrace()
    {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace)
    {
        this.stackTrace = stackTrace;
    }

    public String converToJSON()
    {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
