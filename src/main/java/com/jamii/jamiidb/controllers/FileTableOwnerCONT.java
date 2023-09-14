package com.jamii.jamiidb.controllers;

import com.jamii.jamiidb.model.FileTableOwnerTBL;
import com.jamii.jamiidb.repo.FileTableOwnerREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileTableOwnerCONT {

    @Autowired
    private FileTableOwnerREPO fileTableOwnerREPO ;

    public FileTableOwnerTBL add( FileTableOwnerTBL fileTableOwnerTBL  ){
        return this.fileTableOwnerREPO.save( fileTableOwnerTBL ) ;
    }
}
