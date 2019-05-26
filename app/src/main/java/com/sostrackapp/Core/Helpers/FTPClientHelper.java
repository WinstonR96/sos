package com.sostrackapp.Core.Helpers;

import android.util.Log;
import android.widget.Toast;

import com.sostrackapp.Core.Interfaces.IFtpCliente;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FTPClientHelper implements IFtpCliente {

    private static final String TAG = "FTPClientHelper";
    public FTPClient mFTPClient = null;

    public boolean ftpConnect(String host, String username, String password,
                              int port) {
        try {
            mFTPClient = new FTPClient();
            Log.i(TAG,"connecting to the host");
            // connecting to the host
            try{
                mFTPClient.connect(host, port);
            }catch (Exception e){
                Log.i(TAG,""+e.getMessage());
            }
            // now check the reply code, if positive mean connection success
            if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {
                // login using username & password
                Log.i(TAG,"login using username & password");
                boolean status = mFTPClient.login(username, password);

                /*
                 * Set File Transfer Mode
                 *
                 * To avoid corruption issue you must specified a correct
                 * transfer mode, such as ASCII_FILE_TYPE, BINARY_FILE_TYPE,
                 * EBCDIC_FILE_TYPE .etc. Here, I use BINARY_FILE_TYPE for
                 * transferring text, image, and compressed files.
                 */
                mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
                mFTPClient.enterLocalPassiveMode();

                return status;
            }else{
                Log.i(TAG,"Error");
            }
        } catch (Exception e) {
            //Log.d(TAG, "Error: could not connect to host " + host);
            Log.d(TAG,""+e.getMessage());
        }

        return false;
    }

    public boolean ftpConnectTesting(String host, String username, String password,
                              int port) {
        boolean status = false;
        try {
            mFTPClient = new FTPClient();
            // connecting to the host
            try{
                mFTPClient.connect(host, port);
                // now check the reply code, if positive mean connection success
                if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {
                    // login using username & password
                    status = mFTPClient.login(username, password);
                }else{
                    status = false;
                }
            }catch (Exception e){
                Log.e("ErrorConnection", e.getMessage());
                status = false;
            }

        } catch (Exception ex) {
            Log.e("ErrorConnection", "Excepcion: "+ex.getMessage());
            status = false;
        }

        return status;
    }

    public boolean ftpDisconnect() {
        try {
            mFTPClient.logout();
            mFTPClient.disconnect();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error occurred while disconnecting from ftp server.");
        }

        return false;
    }

    public String[] ftpPrintFilesList(String dir_path) {
        String[] fileList = null;
        try {
            FTPFile[] ftpFiles = mFTPClient.listFiles(dir_path);
            int length = ftpFiles.length;
            fileList = new String[length];
            for (int i = 0; i < length; i++) {
                String name = ftpFiles[i].getName();
                boolean isFile = ftpFiles[i].isFile();

                if (isFile) {
                    fileList[i] = "File :: " + name;
                    Log.i(TAG, "File : " + name);
                } else {
                    fileList[i] = "Directory :: " + name;
                    Log.i(TAG, "Directory : " + name);
                }
            }
            return fileList;
        } catch (Exception e) {
            e.printStackTrace();
            return fileList;
        }
    }

    public void ftpDescargarArchivo(String localFile, String hostFile) {
        try {
            Log.i("OBteniendo archivo", "Obteniendo archivo...");
            InputStream inputStream = mFTPClient.retrieveFileStream(hostFile);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(localFile));
            IOUtils.copy(inputStream, bufferedOutputStream);
            bufferedOutputStream.flush();
            inputStream.close();
            bufferedOutputStream.close();
            Log.i("Obteniendo_archivo","Descarga exitosa");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
