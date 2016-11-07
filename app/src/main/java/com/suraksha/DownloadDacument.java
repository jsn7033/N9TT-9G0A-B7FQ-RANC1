package com.suraksha;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by RTH0102001 on 26-02-2016.
 */
public class DownloadDacument {


    static final String FTP_HOST = "104.238.126.224";
    static final String FTP_USER = "rlftp";
    static final String FTP_PASS = "bruv2zewrU";
    String FILENAME="";
    String IMAGE_DIRECTORY_NAME="Howzat Douwnload";
    private String imgUrl = "http://104.238.126.224/djnni%20dll/bills/";
    DownloadDacument(String filename){
        this.FILENAME=filename;
        downloaddoc();
    }
      void downloaddoc(){
    try
    {
        FTPClient con = new FTPClient();
        con.connect(FTP_HOST);

        if (con.login( FTP_USER,FTP_PASS))
        {
            con.enterLocalPassiveMode(); // important!
            con.setFileType(FTP.BINARY_FILE_TYPE);
            try {

            }catch (Exception e){
                e.getStackTrace();
            }


              File mediaStorageDir = new File(Environment.getExternalStorageDirectory()+ "/DCIM/", FILENAME);
              OutputStream out = new FileOutputStream(mediaStorageDir);
           //    Bitmap fd=   decodeFile(myDir);
            //   sd.setImageBitmap(fd);


            //  StreamResult result = new StreamResult(new File(android.os.Environment.getExternalStorageDirectory(),"08012016140302.jpg"));
            boolean result = con.retrieveFile(imgUrl+FILENAME, out);
            out.close();
            if (result) Log.v("download result", "succeeded");
            con.logout();
            con.disconnect();
        }
    }
    catch (Exception e)
    {
        Log.v("download result","failed");
        e.printStackTrace();
    }

      }
}
