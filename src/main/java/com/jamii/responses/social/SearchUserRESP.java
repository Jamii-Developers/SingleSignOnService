package com.jamii.responses.social;

import com.jamii.responses.AbstractResponses;

import java.util.List;

public class SearchUserRESP extends AbstractResponses {

    public SearchUserRESP( ) {
        this.UI_SUBJECT = "Search was successful!";
        this.UI_MESSAGE = "These are the members found";
    }

    private List<String> USER_KEY;
    private List<String> USERNAME;
    private List<String> FIRSTNAME;
    private List<String> MIDDLENAME;
    private List<String> LASTNAME;

    public List<String> getUSER_KEY() {
        return USER_KEY;
    }

    public List<String> getUSERNAME() {
        return USERNAME;
    }

    public List<String> getFIRSTNAME() {
        return FIRSTNAME;
    }

    public List<String> getMIDDLENAME() {
        return MIDDLENAME;
    }

    public List<String> getLASTNAME() {
        return LASTNAME;
    }
}
