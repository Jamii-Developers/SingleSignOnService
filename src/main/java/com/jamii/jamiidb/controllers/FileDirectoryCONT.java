package com.jamii.jamiidb.controllers;

import com.jamii.jamiidb.model.FileDirectoryTBL;
import com.jamii.jamiidb.model.FileTableOwnerTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.repo.FileDirectoryREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class FileDirectoryCONT {

    @Autowired
    private
    FileDirectoryREPO fileDirectoryREPO;

    public FileDirectoryTBL createFileDirectory( UserLoginTBL userLoginTBL, FileTableOwnerTBL fileTableOwnerTBL, String uidirectory ){
        FileDirectoryTBL fileDirectoryTBL = new FileDirectoryTBL( );
        fileDirectoryTBL.setUserloginid( userLoginTBL );
        fileDirectoryTBL.setFiletableownerid( fileTableOwnerTBL );
        fileDirectoryTBL.setUidirectory( uidirectory );
        fileDirectoryTBL.setLastupdated( LocalDateTime.now( ) );
        return  this.fileDirectoryREPO.save( fileDirectoryTBL );
    }

    public Optional<FileDirectoryTBL> fetch(UserLoginTBL userLoginTBL, FileTableOwnerTBL fileTableOwnerTBL ){
        return this.fileDirectoryREPO.findByUserloginidAndFiletableownerid( userLoginTBL, fileTableOwnerTBL ).stream( ).findFirst( );
    }

    public void update( FileDirectoryTBL fileDirectoryTBL ){
        this.fileDirectoryREPO.save( fileDirectoryTBL );
    }
}
