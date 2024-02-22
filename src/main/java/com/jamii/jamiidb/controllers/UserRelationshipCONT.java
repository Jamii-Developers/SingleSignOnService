package com.jamii.jamiidb.controllers;

import com.jamii.jamiidb.repo.UserRelationshipREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserRelationshipCONT {

    @Autowired
    private UserRelationshipREPO userRelationshipREPO;
}
