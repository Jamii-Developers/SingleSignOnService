package com.jamii.webapi.activeDirectory.data;

import org.springframework.http.ResponseEntity;

public class UserLoginStruct{

    public UserLoginStruct() { }

    private ResponseEntity< String > RESPONSE;
    private String USERNAME;
    private String PASSWORD;
    private String AUTHENTICATIONTOKEN;
    private String DEVICEID;
    private String USERID;

    public ResponseEntity<String> getRESPONSE() {
        return RESPONSE;
    }

    public void setRESPONSE(ResponseEntity<String> RESPONSE) {
        this.RESPONSE = RESPONSE;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public String getAUTHENTICATIONTOKEN() {
        return AUTHENTICATIONTOKEN;
    }

    public void setAUTHENTICATIONTOKEN(String AUTHENTICATIONTOKEN) {
        this.AUTHENTICATIONTOKEN = AUTHENTICATIONTOKEN;
    }

    public String getDEVICEID() {
        return DEVICEID;
    }

    public void setDEVICEID(String DEVICEID) {
        this.DEVICEID = DEVICEID;
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }
}
