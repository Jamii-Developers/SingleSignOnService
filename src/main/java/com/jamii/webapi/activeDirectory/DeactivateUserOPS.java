package com.jamii.webapi.activeDirectory;

import com.jamii.requests.DeactivateUserREQ;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class DeactivateUserOPS extends activeDirectoryAbstract {

    private DeactivateUserREQ deactivateUserREQ;

    public DeactivateUserREQ getDeactivateUserREQ() {
        return deactivateUserREQ;
    }

    public void setDeactivateUserREQ(DeactivateUserREQ deactivateUserREQ) {
        this.deactivateUserREQ = deactivateUserREQ;
    }

    public DeactivateUserOPS( ) { }

    @Override
    public void processRequest() throws Exception {

    }

    @Override
    public ResponseEntity<HashMap<String, String>> response() {
        return null;
    }
}
