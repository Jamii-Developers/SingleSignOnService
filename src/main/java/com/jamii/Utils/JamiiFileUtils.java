package com.jamii.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class JamiiFileUtils {

    @Autowired
    protected JamiiLoggingUtils jamiiLoggingUtils;

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

    /**
     * This method will be used to compress files into a zip file.
     *
     * @param sourceFilePath
     * @param zipOutputFilePath
     * @param zipFileName
     * @throws Exception
     */
    public void compress( String sourceFilePath, String zipOutputFilePath, String zipFileName ) throws Exception {

            File sourceFile = new File(sourceFilePath);

            if ( !directoryExists(zipOutputFilePath)) {
                createDirectory(zipOutputFilePath);
            }

            String finalZip = zipOutputFilePath + File.separator + zipFileName;

            try (FileOutputStream fos = new FileOutputStream(finalZip);
                 ZipOutputStream zipOut = new ZipOutputStream(fos)) {

                ZipEntry zipEntry = new ZipEntry(sourceFile.getName());
                zipOut.putNextEntry(zipEntry);

                byte[] bytes = Files.readAllBytes(Paths.get(sourceFilePath));
                zipOut.write(bytes, 0, bytes.length);
            }
    }

    public void compress( String sourceFilePath, String zipOutputFilePath, String zipFileName, boolean addZipExtention ) throws Exception{
        if( addZipExtention ){
            zipFileName = zipFileName + ".zip";
        }
        compress( sourceFilePath, zipOutputFilePath, zipFileName );
    }

    public boolean directoryExists( String directory ){
        Path path = Paths.get( directory );
        return Files.exists( path ) && Files.isDirectory( path );
    }

    public void createDirectory( String directory ) throws IOException {
        Path path = Paths.get( directory );
        Files.createDirectory( path );
    }

    public void delete( String filePath ) {
        File file = new File( filePath );
        file.delete();
    }
}
