package com.jamii.utils;

import com.jamii.sysconfigs.FileServerConfigs;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.InflaterInputStream;

/**
 * Utility class for file download operations.
 * 
 * <p>This class handles decompressing and retrieving files from the file server,
 * including caching operations for improved performance.</p>
 */
public class JamiiFileDownloadUtils {



    /**
     * The path of the found file after processing.
     */
    private Path foundFile;

    /**
     * Gets the found file path.
     * @return the found file path
     */
    public Path getFoundFile() {
        return foundFile;
    }

    /**
     * Sets the found file path.
     * @param foundFile the found file path to set
     */
    public void setFoundFile(Path foundFile) {
        this.foundFile = foundFile;
    }

    /**
     * Gets a file as a Spring Resource after decompressing it.
     * 
     * <p>This method decompresses a file from the source path, caches it in the
     * file caching store, and returns it as a Resource for download.</p>
     * @param filePath the directory path of the file
     * @param fileName the name of the file
     * @param fileExtension the file extension
     * @return the file as a Resource, or null if not found
     * @throws IOException if an I/O error occurs during file operations
     */
    public Resource getFileAsResource(String filePath, String fileName, String fileExtension ) throws IOException {

        FileInputStream fis = new FileInputStream( filePath + File.separator + fileName );
        FileOutputStream fos =new FileOutputStream( FileServerConfigs.FILE_CACHING_STORE + File.separator + fileName + fileExtension ) ;
        InflaterInputStream decompressor = new InflaterInputStream( fis );

        int data;
        while((data=decompressor.read())!=-1)
        {
            fos.write(data);
        }

        //close the files
        decompressor.close();
        fis.close();
        fos.flush();
        fos.close();

        Path dirPath = Paths.get( FileServerConfigs.FILE_CACHING_STORE );

        Files.list(dirPath).forEach(file -> {
            if (file.getFileName( ).toString( ).startsWith( fileName ) ) {
                setFoundFile( file ) ;
            }
        });

        if ( getFoundFile( ) != null) {
            return new UrlResource( getFoundFile( ).toUri());
        }
        return null;
    }
}
