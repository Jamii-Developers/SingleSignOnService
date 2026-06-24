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

/**
 * Utility class for file operations including compression and file type handling.
 * 
 * <p>This class provides methods for compressing files into ZIP format,
 * managing directories, and determining file extensions from content types.</p>
 */
@Component
public class JamiiFileUtils {

    @Autowired
    protected JamiiLoggingUtils jamiiLoggingUtils;

    /**
     * PNG content type constant.
     */
    public static final String PNG_CONTENT_TYPE         = "image/png";
    
    /**
     * PNG file extension constant.
     */
    public static final String PNG_EXTENSION            = ".png";
    
    /**
     * JPEG content type constant.
     */
    public static final String JPEG_CONTENT_TYPE        = "image/jpeg";
    
    /**
     * JPEG file extension constant.
     */
    public static final String JPEG_EXTENSION           = ".jpeg";

    /**
     * Gets the file extension for a given content type.
     * @param ContentType the content type to look up
     * @return the file extension, or empty string if not found
     */
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
     * Compresses a file into a ZIP archive.
     *
     * @param sourceFilePath the path of the source file to compress
     * @param zipOutputFilePath the directory where the ZIP file will be created
     * @param zipFileName the name of the ZIP file
     * @throws Exception if an error occurs during compression
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

    /**
     * Compresses a file into a ZIP archive with optional .zip extension.
     * @param sourceFilePath the path of the source file to compress
     * @param zipOutputFilePath the directory where the ZIP file will be created
     * @param zipFileName the name of the ZIP file
     * @param addZipExtention whether to add .zip extension to the file name
     * @throws Exception if an error occurs during compression
     */
    public void compress( String sourceFilePath, String zipOutputFilePath, String zipFileName, boolean addZipExtention ) throws Exception{
        if( addZipExtention ){
            zipFileName = zipFileName + ".zip";
        }
        compress( sourceFilePath, zipOutputFilePath, zipFileName );
    }

    /**
     * Checks if a directory exists.
     * @param directory the directory path to check
     * @return true if the directory exists, false otherwise
     */
    public boolean directoryExists( String directory ){
        Path path = Paths.get( directory );
        return Files.exists( path ) && Files.isDirectory( path );
    }

    /**
     * Creates a directory.
     * @param directory the directory path to create
     * @throws IOException if an error occurs during directory creation
     */
    public void createDirectory( String directory ) throws IOException {
        Path path = Paths.get( directory );
        Files.createDirectory( path );
    }

    /**
     * Deletes a file.
     * @param filePath the path of the file to delete
     */
    public void delete( String filePath ) {
        File file = new File( filePath );
        file.delete();
    }
}
