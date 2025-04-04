package com.jamii.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

    /**
     * This method will be used to compress files into a zip file.
     *
     * @param sourceFilePath
     * @param zipOutputFilePath
     * @param zipFileName
     * @throws Exception
     */
    public static void compress( String sourceFilePath, String zipOutputFilePath, String zipFileName ) throws Exception{

        File sourceFile = new File(sourceFilePath);

        if( !directoryExists( zipOutputFilePath ) ){
            createDirectory( zipOutputFilePath );
        }

        StringBuilder finalZip = new StringBuilder( );
        finalZip.append( zipOutputFilePath );
        finalZip.append( File.separator );
        finalZip.append( zipFileName );

        try (FileOutputStream fos = new FileOutputStream( finalZip.toString( ) );
             ZipOutputStream zipOut = new ZipOutputStream(fos) ) {

            ZipEntry zipEntry = new ZipEntry( sourceFile.getName( ) );
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = Files.readAllBytes( Paths.get(sourceFilePath) );
            zipOut.write(bytes, 0, bytes.length);
        }
    }

    public static void compress( String sourceFilePath, String zipOutputFilePath, String zipFileName, boolean addZipExtention ) throws Exception{
        if( addZipExtention ){
            zipFileName = zipFileName + ".zip";
        }
        compress( sourceFilePath, zipOutputFilePath, zipFileName );
    }

    public static boolean directoryExists( String directory ){
        Path path = Paths.get( directory );
        return Files.exists( path ) && Files.isDirectory( path );
    }

    public static boolean createDirectory( String directory ){
        try{
            Path path = Paths.get( directory );
            Files.createDirectory( path );
            return true;
        }catch( Exception e){
            e.printStackTrace( );
            return false;
        }
    }

}
