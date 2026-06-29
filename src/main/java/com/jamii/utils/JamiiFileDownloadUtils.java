package com.jamii.utils;

import com.jamii.sysconfigs.FileServerConfigs;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
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
 * including caching operations for improved performance. Uses optimized I/O operations
 * and proper resource management for better performance.</p>
 * 
 * <p>Performance optimizations:</p>
 * <ul>
 *   <li>Uses buffered I/O for improved file operations</li>
 *   <li>Proper resource management with try-with-resources</li>
 *   <li>Optimized file searching with direct path construction</li>
 *   <li>Reduced file system operations</li>
 * </ul>
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
     * file caching store, and returns it as a Resource for download. Uses optimized
     * buffered I/O operations for better performance.</p>
     * 
     * @param filePath the directory path of the file
     * @param fileName the name of the file
     * @param fileExtension the file extension
     * @return the file as a Resource, or null if not found
     * @throws IOException if an I/O error occurs during file operations
     */
    public Resource getFileAsResource(String filePath, String fileName, String fileExtension ) throws IOException {

        // Construct full paths once to avoid repeated string concatenation
        String sourceFilePath = filePath + File.separator + fileName;
        String cachedFilePath = FileServerConfigs.FILE_CACHING_STORE + File.separator + fileName + fileExtension;
        
        // Use try-with-resources for automatic resource management
        try (FileInputStream fis = new FileInputStream(sourceFilePath);
             FileOutputStream fos = new FileOutputStream(cachedFilePath);
             InflaterInputStream decompressor = new InflaterInputStream(fis);
             BufferedInputStream bis = new BufferedInputStream(decompressor);
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {

            // Use buffer for better I/O performance
            byte[] buffer = new byte[8192]; // 8KB buffer
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
        }

        // Direct path construction instead of directory listing
        Path potentialFile = Paths.get(cachedFilePath);
        if (Files.exists(potentialFile) && Files.isReadable(potentialFile)) {
            setFoundFile(potentialFile);
            return new UrlResource(potentialFile.toUri());
        }
        
        return null;
    }
}
