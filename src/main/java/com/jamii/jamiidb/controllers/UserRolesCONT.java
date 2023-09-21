package com.jamii.jamiidb.controllers;

import com.jamii.jamiidb.repo.UserDataREPO;
import com.jamii.jamiidb.repo.UserRolesREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class UserRolesCONT {

    @Autowired
    private UserRolesREPO userRolesREPO;
}
