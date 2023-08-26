package com.jamii.webapi.jamiidb.controllers;

import com.jamii.webapi.jamiidb.repo.UserDataREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDataCONT {

    @Autowired
    private UserDataREPO userDataREPO;
}
