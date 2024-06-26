package com.jamii.jamiidb.controllers;

import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.model.UserRelationshipTBL;
import com.jamii.jamiidb.repo.UserRelationshipREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

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

    public List< UserRelationshipTBL > fetch(UserLoginTBL sender, UserLoginTBL receiver, int type, int status ){
        return userRelationshipREPO.findBySenderidAndReceiveridAndTypeAndStatus( sender, receiver ,type, status);
    }

    public List< UserRelationshipTBL > fetch( UserLoginTBL sender, UserLoginTBL receiver,int type ){
        return userRelationshipREPO.findBySenderidAndReceiveridAndType( sender, receiver ,type );
    }

    public List< UserRelationshipTBL > fetch( UserLoginTBL sender, UserLoginTBL receiver ){
        return userRelationshipREPO.findBySenderidAndReceiverid( sender, receiver );
    }

    public List< UserRelationshipTBL > fetch( UserLoginTBL sender, int type, int status ){
        return userRelationshipREPO.findBySenderidOrReceiverid( sender.getId( ), sender.getId( ) ,type, status );
    }

    public List< UserRelationshipTBL > fetchFollowers( UserLoginTBL sender, int type, int status ){
        return userRelationshipREPO.findByReceiveridAndTypeAndStatus( sender, type, status );
    }

    public void update( UserRelationshipTBL record ){
        userRelationshipREPO.save( record );
    }

}
