package com.jamii.responses;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AbstractResponses {

    protected String UI_SUBJECT;
    protected String UI_MESSAGE;

    public String getJSONRESP( ){

        GsonBuilder builder = new GsonBuilder( );
        builder.setPrettyPrinting( );
        Gson gson = builder.create( );
        return gson.toJson( this );

    }

}
