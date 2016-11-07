package com.suraksha;

import java.io.ByteArrayOutputStream;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

import com.webservice.Service1;
import com.ticktick.imagecropper.CropImageActivity;
import com.ticktick.imagecropper.CropIntent;

public class Add_new_broadcast extends Activity{

	Home h;
	public static boolean cropflag=false;
	public static int flag;
	public static Bitmap photo;
	public static Uri uriimg;
	public static Uri uricam;
	public static Bitmap camphoto;
	public static Bitmap finalphoto;
	public static String caption;
	ImageView imgphoto;
	EditText txtmsg;
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
	 ImageButton btnattach;
	 float rotateangle=0;
	 public static final String CROPPED_IMAGE_FILEPATH = "/sdcard/"; 
	 public static final int REQUEST_CODE_PICK_IMAGE = 0x1;
	    public static final int REQUEST_CODE_IMAGE_CROPPER  = 0x2;
	    
	    Button btncancel,btnpost;
	    private ProgressDialog prgDialog;
	     public static final int progress_bar_type = 0;
	     int REQUEST_CAMERA=2,SELECT_FILE=1,PICK_FILE=3;
	     public static Bitmap bitmapcamera;
	 	private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
	 	private String mCurrentPhotoPath;
	 	private static final String JPEG_FILE_PREFIX = "IMG_";
		private static final String JPEG_FILE_SUFFIX = ".jpg";
		 public static String imgpath;
		 public static String camimgpath;
		 public static Uri camuri;
		 public static Bitmap bm;
		 public static Uri imguri;
	     
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_new_broadcast);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();		
 		StrictMode.setThreadPolicy(policy);
 		db=new Databaseclass(Add_new_broadcast.this);
 		btnpost=(Button)findViewById(R.id.btnsend);
 		txtmsg=(EditText)findViewById(R.id.etmsg);
 		imgphoto=(ImageView)findViewById(R.id.photo);
 		btnattach=(ImageButton)findViewById(R.id.btnattach);
		ArrayList<String> temp=new ArrayList<String>();
        temp=db.getnamemobile();
        mobile=temp.get(1).toString();
        btnpost.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String msg=txtmsg.getText().toString();
				if(TextUtils.isEmpty(msg))
				{
					txtmsg.setError("Type a message");
				}
				else
				{
				 if(bm!=null)
				 {
					 String encodedImage = getStringFromBitmap(bm);
					 new Inserttask().execute(mobile,msg,encodedImage,"local");
				 }	
				 else
				 {
				 new Inserttask().execute(mobile,msg,"No image","local");
				 }
				}
				
			}
		});
        btnattach.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				selectImage();
				
			}
		});
		/*db=new Databaseclass(Add_new_broadcast.this);	
		
		ArrayList<String> temp=new ArrayList<String>();
        temp=db.getnamemobile();
        mobile=temp.get(1).toString();
		photo=h.bm;
		uriimg=h.imguri;
		camphoto=h.bitmapcamera;
		uricam=h.camuri;
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
				if(photo==null)
				{
				path=h.camimgpath;
				}
				else
				{
				path=h.imgpath;	
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
				Intent i=new Intent(Add_new_broadcast.this,Home.class);
				startActivity(i);
				
			}
		});
		btnsend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				caption=txtcaption.getText().toString().trim();	
				if(photo==null)
				{
				path=h.camimgpath;
				}
				else
				{
				path=h.imgpath;	
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
				Intent i=new Intent(Add_new_broadcast.this,Home.class);
				startActivity(i); 
				
			}
		});*/
		
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
	
	 /*@Override
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
	    }*/
	 
	 private class Inserttask extends AsyncTask<String, Integer, String> {
	 	   @Override
	 	   protected void onPreExecute() 
	 	   {  	
	 	      super.onPreExecute();
	 	      showDialog(progress_bar_type);
	 	      
	 	      
	 	   }
	 	 
	 	   @Override
	 	   protected String doInBackground(String... params) {
	 		 //  android.os.Debug.waitForDebugger();
	 		  // deletecontact(params[0]);
	 		   Service1 svc=new Service1();
	 		   try {
				svc.insertbroadcast(params[0], params[1], params[2], params[3]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				return null;
	 	      
	 	   }
	 	 
	 	   @Override
	 	   protected void onProgressUpdate(Integer... values) {
	 	      super.onProgressUpdate(values);
	 	   }
	 	 
	 	   @Override
	 	   protected void onPostExecute(String result) {
	 	      super.onPostExecute(result);
	 	     Toast.makeText(Add_new_broadcast.this, "Message broadcasted", Toast.LENGTH_LONG).show();
	 	     Intent i=new Intent(Add_new_broadcast.this,Home.class);
			 startActivity(i);
	 	      dismissDialog(progress_bar_type);
	 	   }
	 	   }
	 
	 @Override
	 @Deprecated
	 protected Dialog onCreateDialog(int id) {
		 switch(id)
		 {
	 case progress_bar_type:
         prgDialog = new ProgressDialog(this);
         prgDialog.setMessage("Broadcasting...");
         prgDialog.setIndeterminate(false);
         prgDialog.setCancelable(true);
         prgDialog.show();
         return prgDialog;
     default:
         return null;
		 }
	  
	 }
	 
	 //code for attachment
	 private void selectImage() {
			final CharSequence[] items = {"Choose from gallery","Camera","Cancel" };

			AlertDialog.Builder builder = new AlertDialog.Builder(Add_new_broadcast.this);
			builder.setTitle("Select Photo");
			builder.setItems(items, new DialogInterface.OnClickListener() 
			{
				@Override
				public void onClick(DialogInterface dialog, int item) {
					if (items[item].equals("Choose from gallery")) 
					{
						
						Intent intent = new Intent(
					    Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						//android.provider.MediaStore.Files
						//android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
						intent.setType("image/*");
						startActivityForResult(
						Intent.createChooser(intent, "Select File"),SELECT_FILE);
					} 
					else if(items[item].equals("Choose files"))
					{
						  Intent intent = new Intent();
					      intent.setAction(Intent.ACTION_GET_CONTENT);
					      intent.setType("file/*");
					      startActivityForResult(intent,PICK_FILE);
					      }
					
					else if(items[item].equals("Camera"))
					{
						//selectimagefromcamera();
						dispatchTakePictureIntent(2);
					}
					
					else if (items[item].equals("Cancel")) {
						dialog.dismiss();
					}
				}
			});
			builder.show();
		}
	 private void dispatchTakePictureIntent(int actionCode) {

			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			switch(actionCode) {
			case 2:
				File f = null;
				
				try {
					f = setUpPhotoFile();
					mCurrentPhotoPath = f.getAbsolutePath();
					takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
				} catch (IOException e) {
					e.printStackTrace();
					f = null;
					mCurrentPhotoPath = null;
				}
				break;

			default:
				break;			
			} // switch

			startActivityForResult(takePictureIntent, REQUEST_CAMERA);
		}
	    private File setUpPhotoFile() throws IOException {
			
			File f = createImageFile();
			mCurrentPhotoPath = f.getAbsolutePath();
			
			return f;
		}
	    private File createImageFile() throws IOException {
	    	File imageF = null;
	    	try
	    	{
			// Create an image file name
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
			File albumF = getAlbumDir();
			imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
			camimgpath=imageF.getPath();
			imgpath=null;
			
	    	}
	    	catch(Exception e)
	    	{
	    		String ex=e.toString();
	    	}
	    	return imageF;
		}
	    private File getAlbumDir() {
	    	
			File storageDir = null;
			try
			{
			if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
				
				storageDir = mAlbumStorageDirFactory.getAlbumStorageDir("Howzat images");

				if (storageDir != null) {
					if (! storageDir.mkdirs()) {
						if (! storageDir.exists()){
							return null;
						}
					}
				}
				
			} else {
				//Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
			}
			}
			catch(Exception e)
			{
				String ex=e.toString();
			}
			return storageDir;
		}
	    private void galleryAddPic() {
	    	try {
		    Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
			File f = new File(mCurrentPhotoPath);
			
		    camuri = Uri.fromFile(f);
		  /*  String p=f.getPath();
		    Bitmap bitmaptest = BitmapFactory.decodeFile(p);
			imgphoto.setImageBitmap(bitmapcamera);*/
		    mediaScanIntent.setData(camuri);
		    this.sendBroadcast(mediaScanIntent);
	    	}
	    	catch(Exception e)
	    	{
	    		String ex=e.toString();
	    	}
	}
	    
	  //function to get response of file picker
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			
			
			if (resultCode == RESULT_OK) {
				if (requestCode == REQUEST_CAMERA) {
					
					try {
						   //Bundle extras = data.getExtras();
					       //Bitmap imageBitmap = (Bitmap) extras.get("data");
					       //imgphoto.setImageBitmap(imageBitmap);
					        handleBigCameraPhoto();
						
					} catch (Exception e) 
					{
						e.printStackTrace();
					}
				} 
				
				else if (requestCode == SELECT_FILE) 
				{
					
					String path = getRealPathFromURI( data.getData());
					imgpath=path;
					if(path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".png") || path.endsWith(".JPG") || path.endsWith(".JPEG") || path.endsWith(".PNG"))
					{
						imguri = data.getData();
						String tempPath = getPath(imguri, Add_new_broadcast.this);					
						BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
						bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
						bitmapcamera=null;
						camimgpath=null;
						imgphoto.setImageBitmap(bm);
						
								  
					}
					else
					{
						Toast.makeText(this, "Please select a valid file", Toast.LENGTH_LONG).show();
					}
				
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
				else if(requestCode== PICK_FILE)
				{
					
					String path = getRealPathFromURI( data.getData());
					if(path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".png") || path.endsWith(".doc") || path.endsWith(".docx") || path.endsWith(".ppt") || path.endsWith(".pptx") || path.endsWith(".xls") || path.endsWith(".xlsx") || path.endsWith(".pdf"))
					{
						File f=new File(path);
						String filename=f.getName();
						
					
						  
					}
					else
					{
						Toast.makeText(this, "Please select a valid file", Toast.LENGTH_LONG).show();
					}
				
					
				}
			}
		}
		
		  private void handleBigCameraPhoto() {
		    	try
		    	{

				if (mCurrentPhotoPath != null) 
				{
					setPic();
					galleryAddPic();
					mCurrentPhotoPath = null;
				}
		    	}
		    	catch(Exception e)
		    	{
		    		String ex=e.toString();
		    	}

			}
		    
		    private void setPic() {

				/* There isn't enough memory to open up more than a couple camera photos */
				/* So pre-scale the target bitmap into which the file is decoded */

				/* Get the size of the ImageView */
				

				/* Get the size of the image */
		    	try 
		    	{
				BitmapFactory.Options bmOptions = new BitmapFactory.Options();
				bmOptions.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
				int photoW = bmOptions.outWidth;
				int photoH = bmOptions.outHeight;
				
				

				/* Set bitmap options to scale the image decode target */
				bmOptions.inJustDecodeBounds = false;
				bmOptions.inPurgeable = true;

				/* Decode the JPEG file into a Bitmap */
				bitmapcamera = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
				
				bm=null;
		    	}
		    	catch(Exception e)
		    	{
		    		String ex=e.toString();
		    	}
				
			}
		    
		  //function to get the actual path of image being uploaded
			public String getRealPathFromURI (Uri contentUri)
			 {
			     String path = null;
			     String[] proj = { MediaStore.MediaColumns.DATA };

			        if("content".equalsIgnoreCase(contentUri.getScheme ()))
			            {
			                Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
			                if (cursor.moveToFirst()) 
			                {
			                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
			                    path = cursor.getString(column_index);
			                }
			                cursor.close();
			                return path;
			            }
			            else if("file".equalsIgnoreCase(contentUri.getScheme()))
			            {
			                return contentUri.getPath();
			            }
			            return null;
			  }
			
			//function to get the path of selected files being uploaded
			public String getPath(Uri uri, Activity activity) 
			{
				String[] projection = { MediaColumns.DATA };
				Cursor cursor = activity
						.managedQuery(uri, projection, null, null, null);
				int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
				cursor.moveToFirst();
				return cursor.getString(column_index);
			}
			
			//converting image to string
			 private String getStringFromBitmap(Bitmap bitmapPicture) {
			 /*
			 * This functions converts Bitmap picture to a string which can be
			 * JSONified.
			 * */
			 final int COMPRESSION_QUALITY = 100;
			 String encodedImage;
			 ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
			 bitmapPicture.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY,
			 byteArrayBitmapStream);
			 byte[] b = byteArrayBitmapStream.toByteArray();
			 encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
			 return encodedImage;
			 }
}
