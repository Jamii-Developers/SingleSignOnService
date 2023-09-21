package com.jamii.jamiidb.controllers;

import com.jamii.jamiidb.model.ClientCommunicationTBL;
import com.jamii.jamiidb.repo.ClientCommunicationREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientCommunicationCONT {

    @Autowired
    private ClientCommunicationREPO clientCommunicationREPO;

    public ClientCommunicationTBL save ( ClientCommunicationTBL record ){
        return this.clientCommunicationREPO.save( record );
    }
}
