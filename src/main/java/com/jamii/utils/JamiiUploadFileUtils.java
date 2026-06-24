package com.jamii.utils;

import com.jamii.sysconfigs.FileServerConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;

/**
 * Utility class for file upload operations including compression.
 * 
 * <p>This class handles converting multipart files to regular files,
 * compressing them, and saving them to the file server.</p>
 */
@Service
public class JamiiUploadFileUtils {

    @Autowired
    protected JamiiFileUtils jamiiFileUtils;

    private MultipartFile multipartFile1 ;
    private File file1;
    private String destDirectory ;
    private String systemFilename;

    /**
     * Gets the multipart file to upload.
     * @return the multipart file
     */
    public MultipartFile getMultipartFile1() {
        return multipartFile1;
    }

    /**
     * Sets the multipart file to upload.
     * @param multipartFile1 the multipart file to set
     */
    public void setMultipartFile1(MultipartFile multipartFile1) {
        this.multipartFile1 = multipartFile1;
    }

    /**
     * Gets the converted file.
     * @return the converted file
     */
    public File getFile1() {
        return file1;
    }

    /**
     * Sets the converted file.
     * @param file1 the converted file to set
     */
    public void setFile1(File file1) {
        this.file1 = file1;
    }

    /**
     * Gets the destination directory.
     * @return the destination directory
     */
    public String getDestDirectory() {
        return destDirectory;
    }

    /**
     * Sets the destination directory.
     * @param destDirectory the destination directory to set
     */
    public void setDestDirectory(String destDirectory) {
        this.destDirectory = destDirectory;
    }

    /**
     * Gets the system filename.
     * @return the system filename
     */
    public String getSystemFilename() {
        return systemFilename;
    }

    /**
     * Sets the system filename.
     * @param systemFilename the system filename to set
     */
    public void setSystemFilename(String systemFilename) {
        this.systemFilename = systemFilename;
    }

    /**
     * Converts a multipart file to a regular file.
     * @param multipartFile the multipart file to convert
     * @return the converted file
     * @throws IOException if an I/O error occurs
     */
    public File convertMultipartFileToFile( MultipartFile multipartFile ) throws IOException {

        File file = new File( FileServerConfigs.FILE_CACHING_STORE + File.separator + multipartFile.getOriginalFilename( ) );

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        }

        return file;
    }

    /**
     * Saves the file by converting, compressing, and storing it.
     * @throws Exception if an error occurs during save operation
     */
    public void save( ) throws Exception {

        // Convert MultipartFile to a regular file before compression
        if( getFile1( ) == null ){
            setFile1( convertMultipartFileToFile( getMultipartFile1( ) ) );
        }
        jamiiFileUtils.compress( getFile1( ).getAbsolutePath( ), getDestDirectory( ) , getSystemFilename(),true );
        jamiiFileUtils.delete( getFile1().getAbsolutePath( ) );
    }

    /**
     * Compresses PNG files.
     * @deprecated Will move to compressing files using the JamiiFileUtils.compress() method so all files going forward will be stored in zip files
     * @return true if compression and deletion succeeded
     * @throws IOException if an I/O error occurs
     */
    @Deprecated
    private boolean compressPNG( ) throws IOException {
        String fileLocation = getFile1( ).getAbsolutePath( );
        FileInputStream fis = new FileInputStream( fileLocation );
        FileOutputStream fos = new FileOutputStream(getDestDirectory( ) + File.separator + getSystemFilename( )  );
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

    /**
     * Compresses JPEG files.
     * @deprecated Will move to compressing files using the JamiiFileUtils.compress() method so all files going forward will be stored in zip files
     * @return true if compression and deletion succeeded
     * @throws IOException if an I/O error occurs
     */
    @Deprecated
    private boolean compressJPEG( ) throws IOException {
        String fileLocation = getFile1( ).getAbsolutePath( );
        FileInputStream fis = new FileInputStream( fileLocation );
        FileOutputStream fos = new FileOutputStream(getDestDirectory( ) + File.separator +getSystemFilename( ) ) ;
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
}
