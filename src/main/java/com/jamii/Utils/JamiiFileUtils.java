package com.jamii.Utils;

import com.jamii.configs.FileServerConfigs;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.zip.DeflaterOutputStream;

public class JamiiFileUtils {

    public static final String PNG_CONTENT_TYPE         = "image/png";
    public static final String PNG_EXTENSION            = ".png";
    public static final String JPEG_CONTENT_TYPE        = "image/jpeg";
    public static final String JPEG_EXTENSION           = ".jpeg";

    public static String getFileExtension( String ContentType ){
        if( JamiiStringUtils.equals( ContentType, PNG_CONTENT_TYPE ) ){
            return PNG_EXTENSION;
        }

        if( JamiiStringUtils.equals( ContentType, JPEG_CONTENT_TYPE ) ){
            return JPEG_EXTENSION;
        }
        return "";
    }

}
