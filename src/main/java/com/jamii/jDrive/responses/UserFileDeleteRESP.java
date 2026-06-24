package com.jamii.jDrive.responses;

import com.jamii.abstractClasses.AbstractResponses;

public class UserFileDeleteRESP extends AbstractResponses {

    public UserFileDeleteRESP( ) {
        this.UI_SUBJECT = "File Delete Successfull!";
        this.UI_MESSAGE = "Your file has been moved to the trash bin. It will be fully deleted after 30 days!";
    }
}
