package com.jamii.responses.activeDirectory;

import com.jamii.responses.AbstractResponses;

public class ChangePasswordRESP extends AbstractResponses {

    public ChangePasswordRESP ( ){
        this.UI_SUBJECT = "Success!";
        this.UI_MESSAGE = "Your password change was successful";
    }
}
