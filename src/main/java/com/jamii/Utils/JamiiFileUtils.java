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

    private MultipartFile multipartFile1 ;
    private File file1;
    private String destDirectory ;
    private String systemFilename;

    public static final String PNG_CONTENT_TYPE     = "image/png";
    public static final String PNG_EXTENSION        = ".png";


    public MultipartFile getMultipartFile1() {
        return multipartFile1;
    }

    public void setMultipartFile1(MultipartFile multipartFile1) {
        this.multipartFile1 = multipartFile1;
    }

    public File getFile1() {
        return file1;
    }

    public void setFile1(File file1) {
        this.file1 = file1;
    }

    public String getDestDirectory( ) {
        return destDirectory;
    }

    public void setDestDirectory(String destDirectory) {
        this.destDirectory = destDirectory;
    }

    public String getSystemFilename() {
        return systemFilename;
    }

    public void setSystemFilename(String systemFilename) {
        this.systemFilename = systemFilename;
    }

    public File convertMultipartFileToFile( MultipartFile multipartFile ) throws IOException {
        File file = new File( FileServerConfigs.FILE_CACHING_STORE+ File.separator +multipartFile.getOriginalFilename( ) );

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        }

        return file;
    }

    public boolean save( ) {
        try {
            // Convert MultipartFile to a regular file before compression
            if( getFile1( ) == null ){
                setFile1( convertMultipartFileToFile( getMultipartFile1( ) ) );
            }
            saveFile( ) ;
            return true ;
        }catch( IOException  e) {
            e.printStackTrace( );
            return false ;
        }

    }

    public void saveFile( ) throws IOException {

        if( JamiiStringUtils.equals( Objects.requireNonNull( getMultipartFile1( ).getContentType( ) ), "image/png" ) ){
            System.out.println( getMultipartFile1().getOriginalFilename( ) + " has been compressed : "+ compressPNG( ) );
            return;
        }

    }

    private boolean compressPNG( ) throws IOException {
        String fileLocation = getFile1( ).getAbsolutePath( );
        FileInputStream fis = new FileInputStream( fileLocation );
        FileOutputStream fos = new FileOutputStream(getDestDirectory( ) + File.separator +getSystemFilename( ) + getFileExtension( ) );
        DeflaterOutputStream compressor = new DeflaterOutputStream(fos);
        int contents;
        while ((contents=fis.read())!=-1){
            compressor.write(contents);
        }

        compressor.close( );
        fis.close();
        fos.flush();
        fos.close();
        return getFile1( ).delete( );
    }

    private String getFileExtension( ){
        return getFileExtension( getMultipartFile1( ).getContentType( ) );
    }

    public static String getFileExtension( String ContentType ){
        if( JamiiStringUtils.equals( ContentType, "image/png" ) ){
            return ".png";
        }
        return "";
    }

}
