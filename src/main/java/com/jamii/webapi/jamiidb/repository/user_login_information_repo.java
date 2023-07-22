package com.jamii.webapi.jamiidb.repository;

import com.jamii.webapi.jamiidb.model.user_login_information_model;
import org.springframework.data.repository.CrudRepository;

public interface user_login_information_repo extends CrudRepository<user_login_information_model, Integer> {

}
