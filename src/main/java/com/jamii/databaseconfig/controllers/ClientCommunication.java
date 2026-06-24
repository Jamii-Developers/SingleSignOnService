package com.jamii.databaseconfig.controllers;

import com.jamii.databaseconfig.model.ClientCommunicationTBL;
import com.jamii.databaseconfig.repo.ClientCommunicationREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Service component for managing client communication records in the database.
 * 
 * <p>This class handles storage and retrieval of client-submitted communications,
 * including support requests and reviews.</p>
 * 
 * <p>Key functionality:</p>
 * <ul>
 *     <li>Save individual communication records</li>
 *     <li>Save multiple communication records in batch</li>
 * </ul>
 */
@Component
public class ClientCommunication
{

    /**
     * Communication type constant for support contact requests.
     */
    public final static Integer TYPE_OF_THOUGHT_CONTACT_SUPPORT = 1;
    
    /**
     * Communication type constant for client reviews.
     */
    public final static Integer TYPE_OF_THOUGHT_REVIEW = 2;

    /**
     * Single communication record instance for creating new entries.
     */
    public ClientCommunicationTBL data = new ClientCommunicationTBL();
    
    /**
     * List of communication records for batch operations.
     */
    public ArrayList<ClientCommunicationTBL> dataList = new ArrayList<>();
    @Autowired private ClientCommunicationREPO clientCommunicationREPO;

    /**
     * Saves a specific communication record to the database.
     * 
     * @param record the communication record to save
     * @return the saved communication record with generated ID
     * @deprecated Use {@link #save()} instead which operates on the {@code data} field
     */
    @Deprecated
    public ClientCommunicationTBL save(ClientCommunicationTBL record)
    {
        return this.clientCommunicationREPO.save(record);
    }

    /**
     * Saves the current communication record from the {@code data} field to the database.
     * 
     * <p>The saved entity (with generated ID) is stored back in the {@code data} field.</p>
     */
    public void save()
    {
        data = this.clientCommunicationREPO.save(data);
    }

    /**
     * Saves all communication records in the {@code dataList} to the database in a batch operation.
     * 
     * <p>After saving, the list is cleared and repopulated with the saved entities
     * (including any generated IDs).</p>
     */
    public void saveAll()
    {
        Iterable<ClientCommunicationTBL> datalist = this.clientCommunicationREPO.saveAll(dataList);
        dataList.clear();
        datalist.forEach(x -> dataList.add(x));
    }
}
