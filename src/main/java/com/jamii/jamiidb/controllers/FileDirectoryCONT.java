package com.jamii.jamiidb.controllers;

import com.jamii.jamiidb.model.FileDirectoryTBL;
import com.jamii.jamiidb.model.FileTableOwnerTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.repo.FileDirectoryREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FileDirectoryCONT {

    @Autowired
    private
    FileDirectoryREPO fileDirectoryREPO;

    public FileDirectoryTBL createFileDirectory( UserLoginTBL userLoginTBL, FileTableOwnerTBL fileTableOwnerTBL, String uidirectory ){
        FileDirectoryTBL fileDirectoryTBL = new FileDirectoryTBL( );
        fileDirectoryTBL.setUserloginid( userLoginTBL );
        fileDirectoryTBL.setFiletableownderid( fileTableOwnerTBL );
        fileDirectoryTBL.setUidirectory( uidirectory );
        fileDirectoryTBL.setLastupdated( LocalDateTime.now( ) );
        return  this.fileDirectoryREPO.save( fileDirectoryTBL );
    }
}
