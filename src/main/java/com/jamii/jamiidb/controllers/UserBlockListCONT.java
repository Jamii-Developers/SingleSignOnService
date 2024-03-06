package com.jamii.jamiidb.controllers;

import com.jamii.jamiidb.model.UserBlockListTBL;
import com.jamii.jamiidb.model.UserLoginTBL;
import com.jamii.jamiidb.repo.UserBlockListREPO;

import java.util.List;

public class UserBlockListCONT {

    UserBlockListREPO userBlockListREPO;

    public List<UserBlockListTBL> fetch(UserLoginTBL userid, UserLoginTBL blockedid, Integer status) {
        return userBlockListREPO.findByUseridAndBlockedidAndStatus( userid, blockedid, status );
    }
}
