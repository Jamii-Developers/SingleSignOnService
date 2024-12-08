package com.jamii.Utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamii.requests.userServices.fileManagementREQ.UserFileUploadServicesREQ;
import org.springframework.web.multipart.MultipartFile;

public class JamiiMapperUtils {

    public static Object mapObject( Object payload, Class<?> TargetClass) {
        ObjectMapper objectMapper = new ObjectMapper( );
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.convertValue( payload, TargetClass);
    }


    public static UserFileUploadServicesREQ mapUploadFileObject(String userKey, String deviceKey, String sessionKey, MultipartFile file) {
        UserFileUploadServicesREQ req = new UserFileUploadServicesREQ( );
        req.setSessionKey( sessionKey );
        req.setDeviceKey( deviceKey );
        req.setUserKey( userKey );
        req.setUploadfile( file );
        return req;
    }
}
