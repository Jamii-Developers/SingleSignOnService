package com.jamii.responses;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jamii.Utils.JamiiConstants;
import com.jamii.webapi.jamiidb.model.UserLoginTBL;

public class UserLoginRESP {

    private String MSGTYPE = JamiiConstants.RESPONSE_TYPE_USERLOGIN_MESSAGE;
    private String USER_KEY;

    public String getMSGTYPE() {
        return MSGTYPE;
    }

    public String getUSER_KEY() {
        return USER_KEY;
    }

    public void setUSER_KEY(String USER_KEY) {
        this.USER_KEY = USER_KEY;
    }

    public String getJSONRESP( ){

        GsonBuilder builder = new GsonBuilder( );
        builder.setPrettyPrinting( );
        Gson gson = builder.create( );
        String response = gson.toJson( this );
        return gson.toJson( this );
    }
}
