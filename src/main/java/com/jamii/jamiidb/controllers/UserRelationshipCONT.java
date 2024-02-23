package com.jamii.jamiidb.controllers;

import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.model.UserRelationshipTBL;
import com.jamii.jamiidb.repo.UserRelationshipREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class UserRelationshipCONT {

    @Autowired
    private UserRelationshipREPO userRelationshipREPO;

    public void add( UserLoginTBL sender, UserLoginTBL receiver, int type, int status ){

        UserRelationshipTBL newUserRelationship = new UserRelationshipTBL( );
        newUserRelationship.setSenderid( sender );
        newUserRelationship.setReceiverid( receiver );
        newUserRelationship.setType( type );
        newUserRelationship.setStatus( status );
        newUserRelationship.setDateupdated( LocalDateTime.now( ) );

        userRelationshipREPO.save( newUserRelationship );
    }

    public Optional< UserRelationshipTBL > fetch( UserLoginTBL sender, UserLoginTBL reciever,int type, int status ){
        return userRelationshipREPO.findBySenderidAndReceiveridAndTypeAndStatus( sender, reciever ,type, status).stream( ).findFirst( );
    }

    public void update( UserRelationshipTBL record ){
        userRelationshipREPO.save( record );
    }

}
