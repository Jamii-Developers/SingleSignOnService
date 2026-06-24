package com.jamii.databaseconfig.repo;

import com.jamii.databaseconfig.model.ClientCommunicationTBL;
import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data JPA repository for {@link ClientCommunicationTBL} entities.
 * 
 * <p>This repository provides CRUD operations for client communication records stored in the database.</p>
 */
public interface ClientCommunicationREPO
        extends CrudRepository<ClientCommunicationTBL, Integer>
{}
