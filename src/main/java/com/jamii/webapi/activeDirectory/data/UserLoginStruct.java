package com.jamii.webapi.activeDirectory.data;

public class UserLoginStruct{

    public UserLoginStruct() { }

    private String RESPONSE;
    private String USERNAME;
    private String PASSWORD;
    private String DEVICE_ID;


    /**
     * @return String return the RESPONSE
     */
    public String getRESPONSE( ) {
        return RESPONSE;
    }

    /**
     * @param RESPONSE the RESPONSE to set
     */
    public void setRESPONSE( String RESPONSE ) {
        this.RESPONSE = RESPONSE;
    }

    /**
     * @return String return the USERNAME
     */
    public String getUSERNAME( ) {
        return USERNAME;
    }

    /**
     * @param USERNAME the USERNAME to set
     */
    public void setUSERNAME( String USERNAME ) {
        this.USERNAME = USERNAME;
    }

    /**
     * @return String return the PASSWORD
     */
    public String getPASSWORD( ) {
        return PASSWORD;
    }

    /**
     * @param PASSWORD the PASSWORD to set
     */
    public void setPASSWORD( String PASSWORD ) {
        this.PASSWORD = PASSWORD;
    }

    /**
     * @return String return the DEVICE_ID
     */
    public String getDEVICE_ID( ) {
        return DEVICE_ID;
    }

    /**
     * @param DEVICE_ID the DEVICE_ID to set
     */
    public void setDEVICE_ID( String DEVICE_ID ) {
        this.DEVICE_ID = DEVICE_ID;
    }

}
