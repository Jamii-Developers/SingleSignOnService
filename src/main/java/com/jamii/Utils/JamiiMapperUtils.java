package com.jamii.Utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JamiiMapperUtils {

    public static Object mapObject(Object payload, Class<?> TargetClass) {
        ObjectMapper objectMapper = new ObjectMapper( );
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.convertValue( payload, TargetClass);
    }
}
