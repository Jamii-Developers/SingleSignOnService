package com.jamii.jamiidb.repo;

import com.jamii.jamiidb.model.DeviceInformationTBL;
import com.jamii.jamiidb.model.FileDirectoryTBL;
import org.springframework.data.repository.CrudRepository;

public interface FileDirectoryREPO extends CrudRepository<FileDirectoryTBL, Integer> {
}
