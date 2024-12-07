package com.jamii.Utils;

import com.jamii.configs.FileServerConfigs;
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

public class JamiiFileDownloadUtils {



    private Path foundFile;

    public Path getFoundFile() {
        return foundFile;
    }

    public void setFoundFile(Path foundFile) {
        this.foundFile = foundFile;
    }

    public Resource getFileAsResource(String filePath, String fileName, String fileExtension ) throws IOException {


        FileInputStream fis = new FileInputStream( filePath + File.separator + fileName );
        FileOutputStream fos =new FileOutputStream( FileServerConfigs.FILE_CACHING_STORE + File.separator + fileName+fileExtension ) ;
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
