package com.jamii.webapi.activeDirectory;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ReactivateUserOPS extends activeDirectoryAbstract{
    @Override
    public void processRequest() throws Exception {

    }

    @Override
    public ResponseEntity<HashMap<String, String>> response() {
        return null;
    }
}
