package com.jamii.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamii.jDrive.requests.UserFileUploadServicesREQ;
import org.springframework.web.multipart.MultipartFile;

/**
 * Utility class for object mapping and conversion.
 * 
 * <p>This class provides methods for mapping objects between different types,
 * particularly for file upload request objects.</p>
 */
public class JamiiMapperUtils {

    /**
     * Maps an object to a target class using Jackson ObjectMapper.
     * @param payload the source object to map
     * @param TargetClass the target class to map to
     * @return the mapped object
     */
    public static Object mapObject( Object payload, Class<?> TargetClass) {
        ObjectMapper objectMapper = new ObjectMapper( );
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.convertValue( payload, TargetClass);
    }


    /**
     * Creates a file upload request object from individual parameters.
     * @param userKey the user key
     * @param deviceKey the device key
     * @param sessionKey the session key
     * @param file the multipart file to upload
     * @return the constructed file upload request object
     */
    public static UserFileUploadServicesREQ mapUploadFileObject(String userKey, String deviceKey, String sessionKey, MultipartFile file) {
        UserFileUploadServicesREQ req = new UserFileUploadServicesREQ( );
        req.setSessionKey( sessionKey );
        req.setDeviceKey( deviceKey );
        req.setUserKey( userKey );
        req.setUploadfile( file );
        return req;
    }
}
