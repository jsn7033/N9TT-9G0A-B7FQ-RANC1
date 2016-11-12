package com.suraksha;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.format.Time;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.ticktick.imagecropper.CropImageActivity;

public class attachment_dialog extends Activity{

	HomeActivity h;
	public static boolean cropflag=false;
	public static int flag;
	public static Bitmap photo;
	public static Uri uriimg;
	public static Uri uricam;
	public static Bitmap camphoto;
	public static Bitmap finalphoto;
	public static String caption;
	ImageView imgphoto;
	EditText txtcaption;
	Button btncancel,btnsend;
	DiscussArrayAdapter da;
	Databaseclass db;
	public String path;
	public String tag;
	String filename;
	Connection conn=null;
	 Statement stmnt=null;
	 Statement stmnt1=null;
	 FTPClient mFtpClient;
	 String mobile;
	 File f;
	 ImageButton btnrotate;
	 float rotateangle=0;
	 public static final String CROPPED_IMAGE_FILEPATH = "/sdcard/"; 
	 public static final int REQUEST_CODE_PICK_IMAGE = 0x1;
	    public static final int REQUEST_CODE_IMAGE_CROPPER  = 0x2;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.attachment_dialog);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();		
 		StrictMode.setThreadPolicy(policy); 
		db=new Databaseclass(attachment_dialog.this);	
		
		ArrayList<String> temp=new ArrayList<String>();
        temp=db.getnamemobile();
        mobile=temp.get(1).toString();



		imgphoto=(ImageView)findViewById(R.id.photo);
		txtcaption=(EditText)findViewById(R.id.caption);
		btncancel=(Button)findViewById(R.id.btncancel);
		btnsend=(Button)findViewById(R.id.btnsend);
		if(photo==null)
		{
			imgphoto.setImageBitmap(camphoto);
			finalphoto=camphoto;
		}
		else
		{
			imgphoto.setImageBitmap(photo);
			finalphoto=photo;			
		}
		btnrotate=(ImageButton)findViewById(R.id.btnrotate);
		btnrotate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(photo==null) {
				}
				else
				{

				}
			    f=new File(path);
				filename=f.getName();
				if(photo==null)
				{
					startCropImage(uricam,filename);
				}
				else
				{
					startCropImage(uriimg,filename);		
				}
				
				
			}
		});
		btncancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i=new Intent(attachment_dialog.this,HomeActivity.class);
				startActivity(i);
				
			}
		});
		btnsend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				caption=txtcaption.getText().toString().trim();	
				if(photo==null)
				{

				}
				else
				{

				}
			    f=new File(path);
				filename=f.getName();	
				Time today = new Time(Time.getCurrentTimezone());
				today.setToNow();
				String str=today.monthDay+"/"+(today.month+1)+"/"+today.year+" "+today.format("%k:%M");
				if(cropflag==true)
				{
				db.insertchatdetails(null,"cropped_"+filename,"/sdcard/"+"cropped_"+filename,tag,caption,str,mobile);
				cropflag=false;
				}
				else
				{
				db.insertchatdetails(null,filename,path,tag,caption,str,mobile);
				}
				
				Async2 task2=new Async2();
			    task2.execute();
				Intent i=new Intent(attachment_dialog.this,HomeActivity.class);
				startActivity(i); 
				
			}
		});
		
	}
	
	//class to upload image
	
////////////////////////////////////////////////////
private class Async2 extends AsyncTask<Void, Void,Void>{


@Override
protected Void doInBackground(Void... params) {
//android.os.Debug.waitForDebugger();
try {
	insertchatdetails(filename,caption);
	connnectingwithFTP("110.232.255.114", "ftpuser", "ftp@1234");
    uploadFile(mFtpClient,f,filename);
    

}
catch (Exception e) {
e.printStackTrace();
}
finally {
if (stmnt != null) try { stmnt.close(); } catch(Exception e) {}
if (conn != null) try { conn.close(); } catch(Exception e) {}
}
return null;

}


} 

public void connnectingwithFTP(String ip, String userName, String pass) {  
    boolean status = false;  
    try {  
         mFtpClient = new FTPClient();  
         mFtpClient.setConnectTimeout(10 * 1000);  
         mFtpClient.connect(InetAddress.getByName(ip));  
         status = mFtpClient.login(userName, pass);  
         mFtpClient.setFileType(FTP.BINARY_FILE_TYPE);
         mFtpClient.changeWorkingDirectory(""+mobile+"/others");
             mFtpClient.enterLocalPassiveMode(); 
       
        // }  
    } catch (SocketException e) {  
         e.printStackTrace();  
    } catch (UnknownHostException e) {  
         e.printStackTrace();  
    } catch (IOException e) {  
         e.printStackTrace();  
    }  
} 
//function to upload file on ftp server
 public void uploadFile(FTPClient ftpClient, File downloadFile,String serverfilePath) {  
     try {
    	 
          FileInputStream srcFileStream = new FileInputStream(downloadFile);  
          boolean status = ftpClient.storeFile(serverfilePath,  
                    srcFileStream);    
          srcFileStream.close(); 
          mFtpClient.disconnect();
     } 
     catch (Exception e) {  
          e.printStackTrace();  
     }  
}
 
//function to insert chat messages into database
	
	public void insertchatdetails(String chatmessage,String cap)
	{
		try {
			String driver="net.sourceforge.jtds.jdbc.Driver";
			Class.forName(driver).newInstance();
			 String connString="jdbc:jtds:sqlserver://110.232.255.114:1433/howzat;encrypt=false;user=sa;password=pass@123;";
			 String username="sa";
			 String password="pass@123";
            conn=DriverManager.getConnection(connString,username,password);
            stmnt = conn.createStatement();
            stmnt1 = conn.createStatement();            
           String abc=conn.toString();
            
           
           	String SQL = "INSERT INTO Chat_details(filename,caption,sender,read_unread_status,reciever) VALUES('" + chatmessage + "', '"+cap+"', '" + mobile + "','0', 'server');";
               stmnt.execute(SQL);
        
		
	}
		 catch (Exception e) {
            e.printStackTrace();
         }
         finally {
            if (stmnt != null) try { stmnt.close(); } catch(Exception e) {}
            if (conn != null) try { conn.close(); } catch(Exception e) {}
         }
	}
	
	 public void startCropImage( Uri uri,String newfilename ) {
	    	Intent intent = new Intent(this,CropImageActivity.class);
	    	intent.setData(uri);
	    	intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(new File(CROPPED_IMAGE_FILEPATH+"cropped_"+newfilename)));
	    	//intent.putExtra("aspectX",2);
	    	//intent.putExtra("aspectY",1);
	    	//intent.putExtra("outputX",320);
	    	//intent.putExtra("outputY",240);
	    	//intent.putExtra("maxOutputX",640);
	    	//intent.putExtra("maxOutputX",480);
	    	startActivityForResult(intent, REQUEST_CODE_IMAGE_CROPPER);
	    }
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.attachment_dialog, menu);
		return true;
	}
	
	 @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        if (resultCode != RESULT_OK) {
	            return;
	        }
	        if( requestCode == REQUEST_CODE_PICK_IMAGE ) {
	            //startCropImage(data.getData());
	        }
	        else if( requestCode == REQUEST_CODE_IMAGE_CROPPER ) {
	        	
	            Uri croppedUri = data.getExtras().getParcelable(MediaStore.EXTRA_OUTPUT);
		
	            InputStream in = null;
	            try {
			in = getContentResolver().openInputStream(croppedUri);
			Bitmap b = BitmapFactory.decodeStream(in);
			imgphoto.setImageBitmap(b);
			cropflag=true;
			//Toast.makeText(this,"Crop success，saved at"+CROPPED_IMAGE_FILEPATH,Toast.LENGTH_LONG).show();
	            } 
	            catch (FileNotFoundException e) {
	                e.printStackTrace();
	            }        	
	        }
	        super.onActivityResult(requestCode, resultCode, data);
	    }
}
