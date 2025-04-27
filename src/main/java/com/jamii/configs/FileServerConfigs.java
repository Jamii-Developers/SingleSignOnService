package com.jamii.configs;

import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;
import java.io.InputStream;

public class FileServerConfigs {

    /*
    Local PC Connection settings Update as needed
     */
    public static final String USER_IMAGE_STORE     = "F:\\serverFTP\\userfileuploads";
    public static final String FILE_CACHING_STORE   = "F:\\serverFTP\\cache";

    /*
    FTP Server connection Settings
     */
    protected static final String FTP_Server_Host = "ftp-jamiix.alwaysdata.net";
    protected static final String FTP_Server_Root_Directory = "/home/jamiix/users";
    protected static final String FTP_Server_Password = ".:B%Wrfv.TW6rY!)jXRV";
    protected static final Integer FTP_Server_Port = 21 ;
    protected static final String FTP_Server_User = "jamiix_system";

    public boolean uploadFile(String remoteFilePath, InputStream inputStream) {

        FTPClient ftpClient = new FTPClient();

        try {
            ftpClient.connect( FTP_Server_Host, FTP_Server_Port );
            boolean success = ftpClient.login( FTP_Server_User, FTP_Server_Password);

            if (!success) {
                System.out.println("Could not login to the server");
                return false;
            }

            ftpClient.enterLocalPassiveMode( ); // Important for firewall issues
            ftpClient.setFileType( FTPClient.BINARY_FILE_TYPE );

            boolean done = ftpClient.storeFile( remoteFilePath, inputStream );
            inputStream.close();
            ftpClient.logout();
            ftpClient.disconnect();

            return done;

        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

}
