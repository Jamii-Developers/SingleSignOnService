package com.jamii.responses.activeDirectory;

import com.jamii.responses.AbstractResponses;

public class DeactivateUserRESP extends AbstractResponses {

    public DeactivateUserRESP( ) {
        this.UI_SUBJECT = "Account Deactivation Successful!";
        this.UI_MESSAGE = "Your account has been deactivated!";
    }
}
