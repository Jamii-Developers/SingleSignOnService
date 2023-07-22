package com.jamii.webapi.activeDirectory.data;

import javax.print.attribute.standard.JobOriginatingUserName;

public class CreateNewUserDataStruct {

    public CreateNewUserDataStruct( ) { }

    private String USERNAME;
    private String EMAIL_ADDRESS;
    private String FIRST_NAME;
    private String LAST_NAME;
    private String PASSWORD_SALT;
    private String ACTIVE;

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getEMAIL_ADDRESS() {
        return EMAIL_ADDRESS;
    }

    public void setEMAIL_ADDRESS(String EMAIL_ADDRESS) {
        this.EMAIL_ADDRESS = EMAIL_ADDRESS;
    }

    public String getFIRST_NAME() {
        return FIRST_NAME;
    }

    public void setFIRST_NAME(String FIRST_NAME) {
        this.FIRST_NAME = FIRST_NAME;
    }

    public String getLAST_NAME() {
        return LAST_NAME;
    }

    public void setLAST_NAME(String LAST_NAME) {
        this.LAST_NAME = LAST_NAME;
    }

    public String getPASSWORD_SALT() {
        return PASSWORD_SALT;
    }

    public void setPASSWORD_SALT(String PASSWORD_SALT) {
        this.PASSWORD_SALT = PASSWORD_SALT;
    }

    public String getACTIVE() {
        return ACTIVE;
    }

    public void setACTIVE(String ACTIVE) {
        this.ACTIVE = ACTIVE;
    }
}
