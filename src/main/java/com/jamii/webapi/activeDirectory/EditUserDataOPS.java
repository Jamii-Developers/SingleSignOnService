package com.jamii.webapi.activeDirectory;

import com.jamii.requests.EditUserDataREQ;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class EditUserDataOPS extends activeDirectoryAbstract{
    public EditUserDataOPS() {
    }

    private EditUserDataREQ editUserDataREQ;

    public EditUserDataREQ getEditUserDataREQ() {
        return editUserDataREQ;
    }

    public void setEditUserDataREQ(EditUserDataREQ editUserDataREQ) {
        this.editUserDataREQ = editUserDataREQ;
    }

    @Override
    public void processRequest() throws Exception {

    }

    @Override
    public ResponseEntity<HashMap<String, String>> response() {
        return null;
    }
}
