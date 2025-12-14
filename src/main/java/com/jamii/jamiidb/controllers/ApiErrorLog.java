package com.jamii.jamiidb.controllers;

import com.jamii.jamiidb.model.ApiErrorLogTBL;
import com.jamii.jamiidb.repo.ApiErrorLogREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ApiErrorLog
{

    /**
     * Error types that can be saved in the database
     */
    public static final Integer ERROR_TYPE_EXCEPTION = 1;

    //Creating a table object to reference when creating data for that table
    public ApiErrorLogTBL data = new ApiErrorLogTBL();
    public ArrayList<ApiErrorLogTBL> dataList = new ArrayList<>();
    @Autowired private ApiErrorLogREPO apiErrorLogREPO;

    public ApiErrorLog() {}

    public void save()
    {
        data = apiErrorLogREPO.save(data);
    }

    public void saveAll()
    {
        Iterable<ApiErrorLogTBL> datalist = this.apiErrorLogREPO.saveAll(dataList);
        dataList.clear();
        datalist.forEach(x -> dataList.add(x));
    }
}
