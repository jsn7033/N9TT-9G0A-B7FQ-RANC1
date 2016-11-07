package com.suraksha;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.json.JSONObject;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.webservice.Service1;

import android.widget.PopupMenu;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.telephony.SmsManager;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.suraksha.R.string;
public class Home extends Activity implements SwipeInterface{
	private ListView lvItems;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private FrameLayout container;
	
	//////////////////////////////////////////////////////
	public static Bitmap bitmapcamera;
	private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
	private String mCurrentPhotoPath;
	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";
	 String sid;
	 private ProgressDialog prgDialog;
     public static final int progress_bar_type = 0;
	static int turnaroundcount=0;
	public static String adid;
    ArrayList<String> listadid,listadtitle,listaddesc;
	List<ArrayList<String>> listmsg;
	String chatmsg,searchad;
	int currentapiVersion;
	DiscussArrayAdapter chatadapter;
	ArrayAdapter<String> aa;
	int REQUEST_CAMERA=2,SELECT_FILE=1,PICK_FILE=3;
	private Boolean exit = false;
	private boolean shortPress = false;
	boolean isGPSEnabled = false;
	boolean isNetworkEnabled = false;
	OTP_verification otpv;	
	static String name,mobile;
	public String umobile;
	public String uname;
	Databaseclass db;
	ImageButton btnstart;
	ImageButton btnshowmylocation;
	ImageButton btnshare;
	ImageButton btnnotification;
	public static Bitmap bm;
	public static Uri imguri;
	public static Uri camuri;
	ImageButton button1; 
	ImageButton btnpostad;
	EditText txtchat;
	ImageButton btnchat;
	ListView listchat;
	ListView listad;
	static String num1,num2,num3,msg;
	int i,j;
	Button b1;
	Cursor cur;
	ArrayList<String> namelistArrayListhome=new ArrayList<String>();
	ArrayList<String> numberlistArrayListhome=new ArrayList<String>();
	 Connection conn=null;
	 Statement stmnt=null;
	 Statement stmnt1=null;
	 String lat="", lon="";
	 private ProgressBar spinner;
	 int count=0;
	 ImageButton attach;
	 ImageButton search;
	 FTPClient mFtpClient;
	 private ProgressDialog dialog;
	 LocationManager locationManager;
	 attachment_dialog ad;
	 public Bitmap adphoto;
	 public int adflag;
	 public String adcaption;
	 public static String imgpath;
	 public static String camimgpath;
	 String n,m;
	 Preloader p;
	 ArrayList<String> almsg;
	public static ArrayList<String> alpath;
	 ArrayList<String> alcaption;
	 ArrayList<String> aldate;
	 ArrayList<String> alsender;
	 ///////////////declaration for attachment////////////////////
	// Stores names of traversed directories
		ArrayList<String> str = new ArrayList<String>();
		// Check if the first level of the directory structure is the one showing
		private Boolean firstLvl = true;
		private static final String TAG = "F_PATH";
		
		private File path = new File(Environment.getExternalStorageDirectory() + "");
		private String chosenFile;
		private static final int DIALOG_LOAD_FILE = 1000;
		ListAdapter adapter;
	 ///////////////////////////////////
	 
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BroadcastCustomList.appContext=getApplicationContext();
		setContentView(R.layout.activity_main);
		container=(FrameLayout) findViewById(R.id.container);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		
		lvItems = (ListView) findViewById(R.id.lvMenu);
		lvItems.setAdapter(new MenuItemAdapter(Home.this, 0));
		
		Fragment fr = new HomeActivity();
		FragmentManager fm = getFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.container, fr);
		fragmentTransaction.commit();
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_navigation, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				//getActionBar().setTitle("Howzat");
				getActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>HowZat </font>"));
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				//getActionBar().setTitle("Howzat");
				getActionBar().setIcon(null);
				getActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>HowZat </font>"));
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerToggle.setDrawerIndicatorEnabled(true);
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			// selectFrag(2);
		}

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(getResources().getColor(R.color.purple)));

		lvItems.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				selectFrag(position);
				Log.d("tagd", "Clicked in a view");
				mDrawerLayout.closeDrawer(lvItems);
			}
		});
		
		
	}
		
       

	
	
	// function to check GPS on/off status
	  private void showGPSDisabledAlertToUser()
	  {
	        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Home.this); //changed
	        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
	        .setCancelable(false)
	        .setPositiveButton("Goto Settings Page To Enable GPS",
	                new DialogInterface.OnClickListener(){
	            public void onClick(DialogInterface dialog, int id){
	                Intent callGPSSettingIntent = new Intent(
	                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	                startActivity(callGPSSettingIntent);
	            }
	        });
	        alertDialogBuilder.setNegativeButton("Cancel",
	                new DialogInterface.OnClickListener(){
	            public void onClick(DialogInterface dialog, int id){
	                dialog.cancel();
	                Intent i=new Intent(Home.this,Home.class);
	    			startActivity(i);
	            }
	        });
	        AlertDialog alert = alertDialogBuilder.create();
	        alert.show();
	    }
	
	
	
	//function for sharing location on social media
	private void shareIt(String link) {
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		String shareBody = link;
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "HowZat SOS App");
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		startActivity(Intent.createChooser(sharingIntent, "Share via"));
		}
	
	//Function for calling
    public void calling(String num)
    {
    	
    	String number=num;
        Intent callIntent = new Intent(Intent.ACTION_CALL);  
        callIntent.setData(Uri.parse("tel:"+number));  
        startActivity(callIntent);
        
    }

    //Function to send sms
	public void sendsms(String m,String n1,String n2,String n3)
	{
		try
		{
		 Intent intent=new Intent(getApplicationContext(),Home.class);  
         PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);  
           
         //Get the SmsManager instance and call the sendTextMessage method to send message  
         SmsManager sms=SmsManager.getDefault();  
         sms.sendTextMessage(n1, null, m, pi,null); 
         sms.sendTextMessage(n2, null, m, pi,null); 
         sms.sendTextMessage(n3, null, m, pi,null); 
           
         Toast.makeText(getApplicationContext(), "Message Sent successfully!",  
             Toast.LENGTH_LONG).show();  
		}
		catch (Exception e) {
		String error=e.toString();
		Toast.makeText(getApplicationContext(),error,  
	             Toast.LENGTH_LONG).show();
		}
		
	}

	/////////////////////////////////////////////////////////////////////////////
	
	/////////////////////////////////////////////////////////////////////////////
	@Override
	public void onBackPressed() {
		
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
        
	}
	
	//function for volume key down press alert
	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
	        shortPress = false;
	        //Toast.makeText(this, "longPress", Toast.LENGTH_LONG).show();
	        Intent intent = new Intent(Home.this, CountdownActivity.class);  //changed
     	   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
     	   startActivity(intent);
	        return true;
	    }
	    //Just return false because the super call does always the same (returning false)
	    return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
	        if(event.getAction() == KeyEvent.ACTION_DOWN){
	            event.startTracking();
	            if(event.getRepeatCount() == 0){
	                shortPress = true;
	            }
	            return true;
	        }
	    }
	    return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
	        if(shortPress){
	          //  Toast.makeText(this, "shortPress", Toast.LENGTH_LONG).show();
	        } else {
	            //Don't handle longpress here, because the user will have to get his finger back up first
	        }
	        shortPress = false;
	        return true;
	    }
	    return super.onKeyUp(keyCode, event);
	}
	
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}*/
	/*@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.settings:
	            Intent i1=new Intent(Home.this,Settings.class);
	            startActivity(i1);
	            return true;
	        case R.id.help:
	        	Intent i2=new Intent(Home.this,Help.class);
	            startActivity(i2);
	            return true;
	        case R.id.exit:
       	    	Intent intent = new Intent();
       	        intent.setAction(Intent.ACTION_MAIN);
       	        intent.addCategory(Intent.CATEGORY_HOME);
       	        startActivity(intent);
       	        return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}*/


	//function to swipe from left to right
	@Override
	public void left2right(View v) {
		switch(v.getId()){
        case R.id.startlayout:
           // Toast.makeText(this, "right to left", Toast.LENGTH_LONG).show();
            
			 locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
			// getting GPS status
	        isGPSEnabled = locationManager
	                .isProviderEnabled(LocationManager.GPS_PROVIDER);

	        ConnectivityManager mgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    	NetworkInfo networkInfo = mgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	    	boolean connectedmobile = networkInfo != null && networkInfo.isConnected();
	    	NetworkInfo networkInfo2=mgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	    	boolean connectedwifi=networkInfo2 != null && networkInfo2.isConnected();
			// getting GPS status
	        isGPSEnabled = locationManager
	                .isProviderEnabled(LocationManager.GPS_PROVIDER);

	        if(!connectedmobile && !connectedwifi)
	        {
	        	show("Please enable internet connection");
	        }
	        else if (connectedmobile || connectedwifi)
	        {
	        
	            
	        //  Intent i=new Intent(Home.this,CountdownActivity.class);
	    	//	startActivity(i);
	            
	            String num;
	            num=db.getservernumber();
	            final Vibrator vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
	            vibe.vibrate(100);
	            Intent callIntent = new Intent(Intent.ACTION_CALL);  
	            callIntent.setData(Uri.parse("tel:"+num));  
	            startActivity(callIntent);
	        }
	        
	       
	        else
	        {
	        	Toast.makeText(Home.this, "Please check your internet connection", Toast.LENGTH_LONG).show();
	            showGPSDisabledAlertToUser();
	        	// Acquire a reference to the system Location Manager
	    		locationManager = (LocationManager) Home.this.getSystemService(Context.LOCATION_SERVICE);
	    		// Define a listener that responds to location updates
	    		LocationListener locationListener = new LocationListener() {
	    			public void onLocationChanged(Location location) {
	    				// Called when a new location is found by the network location provider.
	    				lat = Double.toString(location.getLatitude());
	    				lon = Double.toString(location.getLongitude());
	    				String link="http://maps.google.com/?q="+lat+","+lon;
	    				////////////////////////
	    				ArrayList<String> l1=new ArrayList<String>();				
	    				db.getReadableDatabase();
	    				l1=db.getmobileno();
	    				num1= l1.get(0).toString();
	    				num2= l1.get(1).toString();
	    				num3= l1.get(2).toString();
	    				msg=l1.get(3).toString()+"location:"+link;				
	    			    sendsms(msg,num1,num2,num3);
	    				////////////////////////////
	    					////////////////////////////
		    				db.getReadableDatabase();
		    				String num=db.getservernumber();
		    				calling(num);
		    				////////////////////////////
	    				
	    			}

	    			public void onStatusChanged(String provider, int status, Bundle extras) {}
	    			public void onProviderEnabled(String provider) {}
	    			public void onProviderDisabled(String provider) {}
	    		};
	    		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 120000, 100, locationListener);
	        }
		

        break;
		}
		
	}


	
	
	
	public void show(String str)
	{
		
		
		Toast.makeText(Home.this, str, Toast.LENGTH_LONG).show();
	}
	/////////////////////////////////////////////////////
	// function to pick gallery images
	
	private void selectImage() {
		final CharSequence[] items = {"Choose from gallery","Camera","Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
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
	
	//function to pick camera images
	 public void selectimagefromcamera()
	 {
		 Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		 String currentDateandTime = sdf.format(new Date());
		 
			File f = new File(android.os.Environment
					.getExternalStorageDirectory(),currentDateandTime);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
			startActivityForResult(intent,REQUEST_CAMERA);

			
	 }
	
	
	//function to get response of file picker
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CAMERA) {
				
				try {
					   //Bundle extras = data.getExtras();
				       // Bitmap imageBitmap = (Bitmap) extras.get("data");
				        handleBigCameraPhoto();
					Intent i=new Intent(Home.this,attachment_dialog.class);
					startActivity(i);
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
					String tempPath = getPath(imguri, Home.this);					
					BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
					bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
					bitmapcamera=null;
					camimgpath=null;
					//chatadapter.add(new OneComment(false, bm, 1));
					Intent i=new Intent(Home.this,attachment_dialog.class);
					startActivity(i);
							  
				}
				else
				{
					Toast.makeText(this, "Please select a valid file", Toast.LENGTH_LONG).show();
				}
			
			}
			else if(requestCode== PICK_FILE)
			{
				
				String path = getRealPathFromURI( data.getData());
				if(path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".png") || path.endsWith(".doc") || path.endsWith(".docx") || path.endsWith(".ppt") || path.endsWith(".pptx") || path.endsWith(".xls") || path.endsWith(".xlsx") || path.endsWith(".pdf"))
				{
					File f=new File(path);
					String filename=f.getName();
					
					//db.insertchatdetails(null,filename,path);
					connnectingwithFTP("182.50.130.34", "howzatv1", "Rannlab@123");
				    uploadFile(mFtpClient,f,filename);
				    insertchatdetails(filename, 1);
				
					  
				}
				else
				{
					Toast.makeText(this, "Please select a valid file", Toast.LENGTH_LONG).show();
				}
			
				
			}
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
	/////////////////////////////////////
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
	
	
	/////////////////////////////////////////////////////
	//code to display msg from server
	private void addItems() 
	{
		try {
		chatadapter.add(new OneComment(true, "Hi! Welcome to howzat sos network","HowZat SOS Network",""));		
		listmsg=db.getmessagefromlocal();
		if(listmsg.size()>0)
		{
		almsg=listmsg.get(0);
		alpath=listmsg.get(1);
		alcaption=listmsg.get(2);
		aldate=listmsg.get(3);
		alsender=listmsg.get(4);
		boolean check;
		String filePath,cap,dt;
		//getpreviouschat();
		for(int i=0;i<almsg.size();i++)
		{   
			if(almsg.get(i)==null)
			{
				filePath = alpath.get(i);
				cap=alcaption.get(i);
				dt=aldate.get(i);
				Bitmap bitmap = BitmapFactory.decodeFile(filePath);
				chatadapter.add(new OneComment(false, bitmap,cap, 1,uname,dt));
			}
			else
			{
				if(alsender.get(i).toString().equals("server"))
				{
				dt=aldate.get(i);
				chatadapter.add(new OneComment(true,almsg.get(i).toString(),"HowZat SOS Network",dt));
				}
				else
				{
					dt=aldate.get(i);
					chatadapter.add(new OneComment(false,almsg.get(i).toString(),uname,dt));
				}
			}
		}
		}
		listchat.setSelection(listchat.getAdapter().getCount()-1);
		}
		catch(Exception e)
		{
			String ex=e.toString();
		}
		
	}
	
	//function to hide keyboard
	public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
	
	//function to insert chat messages into database
	
	public void insertchatdetails(String chatmessage,int flag)
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
              
            
            if(flag==0)
            {
            String SQL = "INSERT INTO Chat_details(message,sender,read_unread_status,reciever) VALUES('" + chatmessage + "', '" + umobile + "','0', 'server');";
            stmnt.execute(SQL);
            }
            else
            {
            	String SQL = "INSERT INTO Chat_details(filename,sender,read_unread_status,reciever) VALUES('" + chatmessage + "', '" + umobile + "','0', 'server');";
                stmnt.execute(SQL);
            }
            
		
	}
		 catch (Exception e) {
             e.printStackTrace();
          }
          finally {
             if (stmnt != null) try { stmnt.close(); } catch(Exception e) {}
             if (conn != null) try { conn.close(); } catch(Exception e) {}
          }
	}
	
	
	//function to establish ftp connection and switch working directory to server
	public void connnectingwithFTP(String ip, String userName, String pass) {  
        boolean status = false;  
        try {  
             mFtpClient = new FTPClient();  
             mFtpClient.setConnectTimeout(10 * 1000);  
             mFtpClient.connect(InetAddress.getByName(ip));  
             status = mFtpClient.login(userName, pass);  
             mFtpClient.setFileType(FTP.BINARY_FILE_TYPE);
             mFtpClient.changeWorkingDirectory(""+umobile+"/others");
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
	@Override
	public void right2left(View v) {
		// TODO Auto-generated method stub
		
	}  
	
	// function to display dialog box to sharing
	
		private void sharedialog() {
			final CharSequence[] items = {"Share my location","Share this app"};

			AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
			builder.setTitle("Choose to share");
			builder.setItems(items, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int item) {
					if (items[item].equals("Share my location")) {

						locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
						 // getting GPS status
				        isGPSEnabled = locationManager
				                .isProviderEnabled(LocationManager.GPS_PROVIDER);

				        ConnectivityManager mgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				    	NetworkInfo networkInfo = mgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				    	boolean connectedmobile = networkInfo != null && networkInfo.isConnected();
				    	NetworkInfo networkInfo2=mgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				    	boolean connectedwifi=networkInfo2 != null && networkInfo2.isConnected();
						// getting GPS status
				        isGPSEnabled = locationManager
				                .isProviderEnabled(LocationManager.GPS_PROVIDER);

				      if(!connectedmobile && !connectedwifi)
				      {
				    	  show("Please enable internet connection");
				      }
				        else
				        {
				        	spinner.setVisibility(View.VISIBLE);
				       
				        	
				        	// Acquire a reference to the system Location Manager
				    		locationManager = (LocationManager) Home.this.getSystemService(Context.LOCATION_SERVICE);
				    		// Define a listener that responds to location updates
				    		LocationListener locationListener = new LocationListener() {
				    			public void onLocationChanged(Location location) {
				    				// Called when a new location is found by the network location provider.
				    				lat = Double.toString(location.getLatitude());
				    				lon = Double.toString(location.getLongitude());    				
				    				////////////////////////
				    				
				    				msg="My location is: http://maps.google.com/?q="+lat+","+lon+" \n Sent by Howzat SOS App \n click to download http://www.howzatsos.com/download ";	
				    				spinner.setVisibility(View.GONE);
				    				shareIt(msg);
				    				locationManager.removeUpdates(this);
				    		        locationManager=null;
				    				////////////////////////////
				    				
				    				////////////////////////////
				    				
				    			
				    			}

				    		    public void onStatusChanged(String provider, int status, Bundle extras) {}
				    			public void onProviderEnabled(String provider) {}
				    			public void onProviderDisabled(String provider) {}
				    		};
				    		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 120000, 100, locationListener);
				        }
					} 
					else
					{
						//String playstorelink="https://play.google.com/store/apps/details?id=com.aviary.android.feather";
						 String playstorelink="HowZat sos App click link to download http://www.howzatsos.com/download";
						shareIt(playstorelink);
					}
				}
			});
			builder.show();
		}
		
		//function to get search results from server
		public void getsearchresults()
		{
			try {
				
	            
				String driver="net.sourceforge.jtds.jdbc.Driver";
				Class.forName(driver).newInstance();
				 String connString="jdbc:jtds:sqlserver://110.232.255.114:1433/howzat;encrypt=false;user=sa;password=pass@123;";
				 String username="sa";
				 String password="pass@123";		

	            conn=DriverManager.getConnection(connString,username,password);
	            stmnt = conn.createStatement();
	            ArrayList<String> temp=new ArrayList<String>();
	            temp=db.getnamemobile();
	            umobile=temp.get(1).toString();
	            listadid=new ArrayList<String>();
	            listadtitle=new ArrayList<String>();
	            listaddesc=new ArrayList<String>();
	           
	            ResultSet rs2=stmnt.executeQuery("select search_id from SEARCH_DETAILS where search_userid='"+umobile+"' and search_keyword='"+searchad+"'");
	            while(rs2.next()){
		            
	            	sid=rs2.getString(1);          	
	            
	            }
	            ResultSet rs=stmnt.executeQuery("select ad_id,ad_categoryname,ad_description from SEARCH_RESULTS where userid='"+umobile+"' and search_id='"+sid+"'");
	           
	            while(rs.next()){
	            
	            	listadid.add(rs.getString(1));
	            	listadtitle.add(rs.getString(2));
	            	listaddesc.add(rs.getString(3));
	            
	            }
	          
	            conn.close();
		}
			
			 catch (Exception e) {
	             e.printStackTrace();
	          }
	          finally {
	             if (stmnt != null) try { stmnt.close(); } catch(Exception e) {}
	             if (conn != null) try { conn.close(); } catch(Exception e) {}
	          }
		}
		
		//function to insert contacts in database
		public void insertcontact()
	     {
				try {
					String driver="net.sourceforge.jtds.jdbc.Driver";
					Class.forName(driver).newInstance();
					String connString="jdbc:jtds:sqlserver://110.232.255.114:1433/howzat;encrypt=false;user=sa;password=pass@123;";
					 String username="sa";
					 String password="pass@123";

		            conn=DriverManager.getConnection(connString,username,password);
		            stmnt = conn.createStatement();
		            //namelistArrayListhome=p.namelistArrayList;
		            //numberlistArrayListhome=p.numberlistArrayList;
		            
		            String abc=conn.toString();
		            String sql;
		            for(int i=0;i<namelistArrayListhome.size();i++)
		            {
		            	n=namelistArrayListhome.get(i).toString();
		            	m=numberlistArrayListhome.get(i).toString();
		            	sql="INSERT INTO CONTACT_DETAILS(user_contactno,name,mobile) VALUES('"+umobile+"','"+n+"','"+m+"')";
		            	stmnt.execute(sql);
		            }
		            
				
			}
				 catch (Exception e) {
		             e.printStackTrace();
		          }
		          finally {
		             if (stmnt != null) try { stmnt.close(); } catch(Exception e) {}
		             if (conn != null) try { conn.close(); } catch(Exception e) {}
		          }
				
	     }
		
		//function to fetch contacts from phone book
		public void fetchcontact()
		{
			Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null,ContactsContract.Contacts.DISPLAY_NAME + " ASC");
			int i,j;
			while (phones.moveToNext())
			{
			  i=phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
			  j=phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
			  String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
			  String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			  namelistArrayListhome.add(phones.getString(i));
			  numberlistArrayListhome.add(phones.getString(j));					
			}
			 // Async2 task2=new Async2();
		     // task2.execute();
			//servercontact();
			
			phones.close();
		}
		
		////////////////////////////////////////////////////
		private class Async2 extends AsyncTask<Void, Void,Void>{
			
			
			@Override
			protected Void doInBackground(Void... params) {
				// android.os.Debug.waitForDebugger();
				try {
					String driver="net.sourceforge.jtds.jdbc.Driver";
					Class.forName(driver).newInstance();
					String connString="jdbc:jtds:sqlserver://110.232.255.114:1433/howzat;encrypt=false;user=sa;password=pass@123;";
					 String username="sa";
					 String password="pass@123";

		            conn=DriverManager.getConnection(connString,username,password);
		            stmnt = conn.createStatement();
		            String abc=conn.toString();
		            String sql;
		            int size;
		            for(int i=0;i<namelistArrayListhome.size();i++)
		            {
		            	try 
		            	{
		            	size=namelistArrayListhome.size();
		            	n=namelistArrayListhome.get(i).toString();
		            	m=numberlistArrayListhome.get(i).toString();
		            	sql="INSERT INTO CONTACT_DETAILS(user_contactno,name,mobile) VALUES('"+umobile+"','"+n+"','"+m+"')";
		            	stmnt.execute(sql);
		            	}
		            	catch (Exception e) {
				             e.printStackTrace();
				          }
		            }
		           
				
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
		
		//function to insert search query
				public void insertsearchquery(String query)
			     {
						try {
							String driver="net.sourceforge.jtds.jdbc.Driver";
							Class.forName(driver).newInstance();
							String connString="jdbc:jtds:sqlserver://110.232.255.114:1433/howzat;encrypt=false;user=sa;password=pass@123;";
							 String username="sa";
							 String password="pass@123";

				            conn=DriverManager.getConnection(connString,username,password);
				            stmnt = conn.createStatement();				            
				            String abc=conn.toString();
				            String sql="INSERT INTO SEARCH_DETAILS(search_keyword,search_userid) VALUES('"+query+"','"+umobile+"')";
				            stmnt.execute(sql);				
						
					}
						 catch (Exception e) {
				             e.printStackTrace();
				          }
				          finally {
				             if (stmnt != null) try { stmnt.close(); } catch(Exception e) {}
				             if (conn != null) try { conn.close(); } catch(Exception e) {}
				          }
						
			     }
	//code for searching asynchronously
				 private class Searchtask extends AsyncTask<String, Integer, String> {
			    	   @Override
			    	   protected void onPreExecute() 
			    	   {  	
			    	      super.onPreExecute();
			    	      showDialog(progress_bar_type);
			    	      
			    	      
			    	   }
			    	 
			    	   @Override
			    	   protected String doInBackground(String... params) {
			    	// android.os.Debug.waitForDebugger();
			    		   insertsearchquery(searchad);
			    		   getsearchresults();
						return null;
			    	      
			    	   }
			    	 
			    	   @Override
			    	   protected void onProgressUpdate(Integer... values) {
			    	      super.onProgressUpdate(values);
			    	   }
			    	 
			    	   @Override
			    	   protected void onPostExecute(String result) {
			    	      super.onPostExecute(result);
			    	      
						    CustomList adapter = new CustomList(Home.this, listadtitle,listaddesc,0);
						    listad.setAdapter(adapter);
						    listad.setVisibility(View.VISIBLE);
						    listchat.setVisibility(View.GONE);
			    	      dismissDialog(progress_bar_type);
			    	   }
			    	   }

				// Show Dialog Box with Progress bar
				    @Override
				    protected Dialog onCreateDialog(int id) {
				        switch (id) {
				        case progress_bar_type:
				            prgDialog = new ProgressDialog(this);
				            prgDialog.setMessage("Searching...");
				            prgDialog.setIndeterminate(true);
				            prgDialog.setCancelable(false);
				            prgDialog.show();
				            return prgDialog;
				        default:
				            return null;
				        }
				    }
				    
////////////////////////code for camera capture image//////////////////////////////////////
				    
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
					    mediaScanIntent.setData(camuri);
					    this.sendBroadcast(mediaScanIntent);
				    	}
				    	catch(Exception e)
				    	{
				    		String ex=e.toString();
				    	}
				}
//////////////////////////end of code for camera//////////////////////////////////////////////////////////////////
				    
				    @Override
					protected void onPostCreate(Bundle savedInstanceState) {
						super.onPostCreate(savedInstanceState);
						// Sync the toggle state after onRestoreInstanceState has occurred.
						mDrawerToggle.syncState();
					}

					@Override
					public void onConfigurationChanged(Configuration newConfig) {
						super.onConfigurationChanged(newConfig);
						// Pass any configuration change to the drawer toggls
						mDrawerToggle.onConfigurationChanged(newConfig);
					}

					@Override
					public boolean onCreateOptionsMenu(Menu menu) {
						getMenuInflater().inflate(R.menu.activity_main, menu);
						return true;
					}

					@Override
					public boolean onOptionsItemSelected(MenuItem item) {

						if (mDrawerToggle.onOptionsItemSelected(item)) {
							return true;
						}
						else if(item.getItemId() == R.id.menu_filter){
							  int actionBarHeight = getActionBar().getHeight()*2;
							popupWindow(actionBarHeight);
						}else if(item.getItemId() == R.id.menu_notification){
							
						}

						return super.onOptionsItemSelected(item);
					}

					Fragment fr = null;

					public void selectFrag(int position) {
						if (position == 0) {
							fr = new HomeActivity();
						} else if (position == 1) {
							fr = new BadgesFragment();
						} 
						 else if (position == 3) {
								Intent i=new Intent(Home.this,Ad_form.class);
								startActivity(i);
							} 
						 else if (position == 4) {
								Intent i=new Intent(Home.this,Mapplaces.class);
								startActivity(i);
							}
						 else if (position == 5) {
								Intent i=new Intent(Home.this,Settings.class);
								startActivity(i);
							} 
						 else if (position == 7) {
								Intent i=new Intent(Home.this,Help.class);
								startActivity(i);
							} 
						else {

						}

						if (fr != null) {
							goneMenu();
							FragmentManager fm = getFragmentManager();
							FragmentTransaction fragmentTransaction = fm.beginTransaction();
							fragmentTransaction.replace(R.id.container, fr);
							fragmentTransaction.commit();
						}
					}

					private void goneMenu() {
						lvItems.setVisibility(View.GONE);
					}

					private class MenuItemAdapter extends ArrayAdapter<String> {

						Context context;
						int Image[] = { R.drawable.ic_home, R.drawable.ic_person1,
								R.drawable.ic_dicount, R.drawable.ic_note,
								R.drawable.ic_circlemenu, R.drawable.ic_setting,
								R.drawable.ic_networkmenu, R.drawable.ic_question,
								R.drawable.ic_operator, R.drawable.ic_notification_menu };

						String Title[] = { getResources().getString(string.menu_home_text),
								getResources().getString(string.menu_profile_text),
								getResources().getString(string.menu_offer_text),
								getResources().getString(string.menu_post_text),
								getResources().getString(string.menu_circle_text),
								getResources().getString(string.menu_setting_text),
								getResources().getString(string.menu_share_text),
								getResources().getString(string.menu_help_text),
								getResources().getString(string.menu_about_text),
								getResources().getString(string.menu_notification_text) };

						public MenuItemAdapter(Context context, int resource) {
							super(context, resource);
							// TODO Auto-generated constructor stub
							this.context = context;
						}

						@Override
						public int getCount() {
							// TODO Auto-generated method stub
							return Title.length;
						}

						@Override
						public View getView(int position, View convertView, ViewGroup parent) {
							// TODO Auto-generated method stub
							ViewHolder holder = null;
							View view = convertView;
							if (view == null) {
								LayoutInflater vi = LayoutInflater.from(getContext());
								view = vi.inflate(R.layout.menu_row, null);

								holder = new ViewHolder();
								holder.tvMenu = (TextView) view.findViewById(R.id.tvItem);
								holder.ivMenu = (ImageView) view.findViewById(R.id.ivItem);
								view.setTag(holder);
							} else
								holder = (ViewHolder) view.getTag();
							holder.tvMenu.setText(Title[position]);
							holder.ivMenu.setImageResource(Image[position]);
							return view;
						}

						private class ViewHolder {
							private ImageView ivMenu;
							private TextView tvMenu;
						}
					}

					private void popupWindow(int offsetY) {
						// // TODO Auto-generated method stub

						// Inflate the popup_layout.xml
						RelativeLayout viewGroup = (RelativeLayout) findViewById(R.id.popup);
						LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						View layout = layoutInflater.inflate(R.layout.filterscreen_popup,
								viewGroup);

						// Creating the PopupWindow
						final PopupWindow popup = new PopupWindow();
						popup.setContentView(layout);
						popup.setWidth(LayoutParams.MATCH_PARENT);
						popup.setHeight(LayoutParams.MATCH_PARENT);
						popup.setFocusable(true);

						// Clear the default translucent background
						popup.setBackgroundDrawable(new BitmapDrawable());

						// Get offsets from onWindowChange Listner and replace hardcoded value
						// with getted values from listner
						// so popup appear excatly on filter icon position
						// 70 give me desired result change the value WRT screen resolution
						// Displaying the popup at the specified location, + offsets.
						popup.showAsDropDown(layout,0,offsetY);
					}
}
