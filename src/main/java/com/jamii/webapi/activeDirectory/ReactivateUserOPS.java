package com.jamii.webapi.activeDirectory;

import com.jamii.requests.ReactivateUserREQ;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ReactivateUserOPS extends activeDirectoryAbstract{

    private ReactivateUserREQ reactivateUserREQ;

    public ReactivateUserREQ getReactivateUserREQ() {
        return reactivateUserREQ;
    }

    public void setReactivateUserREQ(ReactivateUserREQ reactivateUserREQ) {
        this.reactivateUserREQ = reactivateUserREQ;
    }

    @Override
    public void processRequest() throws Exception {

    }

    @Override
    public ResponseEntity<HashMap<String, String>> response() {
        return null;
    }
}
