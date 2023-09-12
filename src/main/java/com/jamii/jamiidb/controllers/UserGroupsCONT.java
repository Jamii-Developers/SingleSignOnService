package com.jamii.jamiidb.controllers;

import com.jamii.jamiidb.repo.UserGroupsREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserGroupsCONT {

    @Autowired
    private UserGroupsREPO userGroupsREPO;
}
