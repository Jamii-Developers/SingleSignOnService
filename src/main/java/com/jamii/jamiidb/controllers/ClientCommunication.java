package com.jamii.jamiidb.controllers;

import com.jamii.jamiidb.model.ClientCommunicationTBL;
import com.jamii.jamiidb.repo.ClientCommunicationREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ClientCommunication
{

    /**
     * These types indicate the client communication that is being submitted by the client
     */
    public final static Integer TYPE_OF_THOUGHT_CONTACT_SUPPORT = 1;
    public final static Integer TYPE_OF_THOUGHT_REVIEW = 2;

    //Creating a table object to reference when creating data for that table
    public ClientCommunicationTBL data = new ClientCommunicationTBL();
    public ArrayList<ClientCommunicationTBL> dataList = new ArrayList<>();
    @Autowired private ClientCommunicationREPO clientCommunicationREPO;

    /**
     * This method has been replaced with he call of the newer method named save( )
     *
     * @param record
     * @return
     */
    @Deprecated
    public ClientCommunicationTBL save(ClientCommunicationTBL record)
    {
        return this.clientCommunicationREPO.save(record);
    }

    public void save()
    {
        data = this.clientCommunicationREPO.save(data);
    }

    public void saveAll()
    {
        Iterable<ClientCommunicationTBL> datalist = this.clientCommunicationREPO.saveAll(dataList);
        dataList.clear();
        datalist.forEach(x -> dataList.add(x));
    }
}
