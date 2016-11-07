package com.suraksha;

import android.content.Context;

import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class UploadImages
{

	Context context;


	String Foldername;
	String Imagename;
	String Imagepath;


	static final String FTP_HOST = "104.238.126.224";
	static final String FTP_USER = "rlftp";
	static final String FTP_PASS = "bruv2zewrU";


	public UploadImages(String name, String path)
	{
		this.Imagename = name;
		this.Imagepath = path;

		SendPhotos();
		

	}

	void SendPhotos()
	{
		try
		{
			try
			{
				boolean flag = false;
				FTPClient localFTPClient;
				localFTPClient = new FTPClient();
				try
				{
					localFTPClient.connect(InetAddress.getByName(FTP_HOST));
					boolean bool3 = localFTPClient.login(FTP_USER, FTP_PASS);
					System.out.println("path...." + bool3);
					localFTPClient.enterLocalPassiveMode();
					localFTPClient.setFileType(2);

					localFTPClient.cwd("/djnni dll/bills");
					flag = localFTPClient.storeFile(Imagename, new FileInputStream(new File(Imagepath)));

				}
				catch (SocketException e)
				{
					// TODO Auto-generated catch block
					System.out.println("path...." + e);
					e.printStackTrace();

				}
				catch (UnknownHostException e)
				{
					// TODO Auto-generated catch block
					System.out.println("path...." + e);
					e.printStackTrace();

				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					System.out.println("path...." + e);
					e.printStackTrace();
				}
			}
			catch (Exception e)
			{

			}

		}
		catch (Exception e) {
        // TODO: handle exception
        String a = e.toString();
        }
        }

        }
