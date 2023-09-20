package com.jamii.services.clientCommunication;

import com.jamii.responses.JamiiErrorsMessagesRESP;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public abstract class clientCommunicationAbstract {

        protected String JamiiError;
        protected JamiiErrorsMessagesRESP jamiiErrorsMessagesRESP = null;

        public void reset( ){
            this.JamiiError = "";
            this.jamiiErrorsMessagesRESP = new JamiiErrorsMessagesRESP( );
        }

        public abstract void processRequest( ) throws IOException;

        public ResponseEntity< ? > getResponse( ){

            StringBuilder response = new StringBuilder( ) ;
            if( !JamiiError.isEmpty( ) ){
                response.append ( this.JamiiError );
            }

            return new ResponseEntity<>( response.toString(), HttpStatus.OK );
        }


}
