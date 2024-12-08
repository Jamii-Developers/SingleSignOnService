package com.jamii.jamiidb.controllers;

import com.jamii.jamiidb.model.FileTableOwnerTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.repo.FileTableOwnerREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FileTableOwnerCONT {

    @Autowired
    private FileTableOwnerREPO fileTableOwnerREPO ;

    public FileTableOwnerTBL add( FileTableOwnerTBL fileTableOwnerTBL  ){
        return this.fileTableOwnerREPO.save( fileTableOwnerTBL ) ;
    }

    public Optional<FileTableOwnerTBL>fetch( UserLoginTBL user , String filename ){
        return this.fileTableOwnerREPO.findByUserloginidAndSystemfilename( user , filename  ).stream( ).findFirst( );
    }

    public void update( FileTableOwnerTBL fileInformation ) {
        this.fileTableOwnerREPO.save( fileInformation ) ;
    }

}
