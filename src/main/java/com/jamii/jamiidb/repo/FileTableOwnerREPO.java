package com.jamii.jamiidb.repo;

import com.jamii.jamiidb.model.DeviceInformationTBL;
import com.jamii.jamiidb.model.FileTableOwnerTBL;
import org.springframework.data.repository.CrudRepository;

public interface FileTableOwnerREPO extends CrudRepository<FileTableOwnerTBL, Integer> {
}
