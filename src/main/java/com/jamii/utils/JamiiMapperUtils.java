package com.jamii.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamii.jDrive.requests.UserFileUploadServicesREQ;
import org.springframework.web.multipart.MultipartFile;

/**
 * Utility class for object mapping and conversion.
 * 
 * <p>This class provides methods for mapping objects between different types,
 * particularly for file upload request objects. Uses a static ObjectMapper instance
 * for optimal performance and thread safety.</p>
 * 
 * <p>Performance optimizations:</p>
 * <ul>
 *   <li>Uses a single static ObjectMapper instance to avoid repeated initialization</li>
 *   <li>Configured to ignore unknown properties for flexible mapping</li>
 *   <li>Thread-safe for concurrent use</li>
 * </ul>
 * 
 * <p>This is a utility class and cannot be instantiated.</p>
 */
public class JamiiMapperUtils {

    /**
     * Static ObjectMapper instance for optimal performance and thread safety.
     * Configured to ignore unknown properties for flexible object mapping.
     */
    private static final ObjectMapper OBJECT_MAPPER;
    
    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Private constructor to prevent instantiation of this utility class.
     * @throws UnsupportedOperationException if instantiation is attempted
     */
    private JamiiMapperUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Maps an object to a target class using the static Jackson ObjectMapper.
     * 
     * <p>This method provides thread-safe object mapping with optimal performance
     * by reusing a pre-configured ObjectMapper instance.</p>
     * 
     * @param payload the source object to map
     * @param TargetClass the target class to map to
     * @return the mapped object
     * @throws IllegalArgumentException if payload or TargetClass is null
     */
    public static Object mapObject( Object payload, Class<?> TargetClass) {
        if (payload == null) {
            throw new IllegalArgumentException("Payload cannot be null");
        }
        if (TargetClass == null) {
            throw new IllegalArgumentException("Target class cannot be null");
        }
        
        return OBJECT_MAPPER.convertValue( payload, TargetClass);
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
