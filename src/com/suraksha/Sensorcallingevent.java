package com.suraksha;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Sensorcallingevent extends Activity {

	boolean isGPSEnabled = false;	
	boolean isNetworkEnabled = false;
	
	LocationManager locationManager;
	Databaseclass db;
	ImageButton btnstart;
	ImageButton btnshowmylocation;
	ImageButton btnshare;
	ImageButton startglow;
	Button button1;  
	EditText txtchat;
	Button btnchat;
	ListView listchat;
	static String num1,num2,num3,msg,maplink,umobile;
	private ProgressBar spinner;
	private TextView tv;
	Connection conn=null;
	Statement stmnt=null;
	String num;
	
	
	 String lat="", lon="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensorcallingevent);
		stopService(new Intent(Sensorcallingevent.this, Shaker_Service.class));
		try
		{
		db=new Databaseclass(Sensorcallingevent.this);
		spinner = (ProgressBar)findViewById(R.id.progressBar1);
		tv=(TextView)findViewById(R.id.textView1);
		spinner.setVisibility(View.VISIBLE);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();		
 		StrictMode.setThreadPolicy(policy); 
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		
        	// Acquire a reference to the system Location Manager
    		locationManager = (LocationManager) Sensorcallingevent.this.getSystemService(Context.LOCATION_SERVICE);
    		// Define a listener that responds to location updates
    		LocationListener locationListener = new LocationListener() {
    			public void onLocationChanged(Location location) {
    				
    				try
    				{
    				lat = Double.toString(location.getLatitude());
    				lon = Double.toString(location.getLongitude());    				
    				////////////////////////
    				ArrayList<String> l1=new ArrayList<String>();
    				ArrayList<String> l2=new ArrayList<String>();
    				db.getReadableDatabase();
    				l1=db.getmobileno();
    				boolean a=(l1.get(0).toString().length()==1);
    				boolean b=(l1.get(1).toString().length()==1);
    				boolean c=(l1.get(2).toString().length()==1);
    				
    				if(!a)
    				{
    					l2.add(l1.get(0).toString());
    				}
    				if(!b)
    				{
    					l2.add(l1.get(1).toString());
    				}
    				if(!c)
    				{
    					l2.add(l1.get(2).toString());
    				}
    				
    				maplink="http://maps.google.com/?q="+lat+","+lon;
    				msg="Please help! location: maps.google.com/?q="+lat+","+lon;				
    			    
    				ArrayList<String> list1=new ArrayList<String>();
    				list1=db.getnamemobile();
    				umobile=list1.get(1).toString();
    				db.getReadableDatabase();
    				num=db.getservernumber();
    				
    				insertalertlocation(umobile,lat,lon);
    				
    				sendsms(msg,l2);
    				AudioManager audio = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
    			    audio.setRingerMode(1);
    				CountDownTimer Timer;
    				Timer = new CountDownTimer(5000, 5000) {
    		            public void onFinish() 
    		            {
    		            	calling(num);
    		            }
    		            @Override
    		            public void onTick(long millisUntilFinished) {
    		                // TODO Auto-generated method stub
    		            }
    		        }.start();
    		        locationManager.removeUpdates(this);
    		        locationManager=null;
    		        
    				spinner.setVisibility(View.GONE);
    				tv.setText("SOS Alert has been sent");
    				}
    				catch(Exception e)
    				{
    					calling(num);
    				}
    				
    			
    			}

    		    public void onStatusChanged(String provider, int status, Bundle extras) {}
    			public void onProviderEnabled(String provider) {}
    			public void onProviderDisabled(String provider) {}
    		};
    		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1500000, 100, locationListener);
		}
		catch(Exception e)
		{
			String ex=e.toString();
		}
        
       
       
	
	}

	//function to insert panic alert details
	
	public void insertalertlocation(String num,String lati,String longi)
	{
		try {
			String driver="net.sourceforge.jtds.jdbc.Driver";
			Class.forName(driver).newInstance();
			 String connString="jdbc:jtds:sqlserver://110.232.255.114:1433/howzat;encrypt=false;user=sa;password=pass@123;";
			 String username="sa";
			 String password="pass@123";

            conn=DriverManager.getConnection(connString,username,password);
            stmnt = conn.createStatement();
           
          //  String SQL = "INSERT INTO TEMP1 VALUES(3,'" + s + "', '" + s1 + "', '" + s2 + "');";
           String SQL = "INSERT INTO ALERT_LOCATION(alert_mobileno,message,latitude,longitude) values('"+num+"','Please help','"+lati+"','"+longi+"')";
           // stmnt = conn.createStatement();
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
	//Function for calling
    public void calling(String num)
    {
    	try 
    	{
    	String number=num;
        Intent callIntent = new Intent(Intent.ACTION_CALL);  
        callIntent.setData(Uri.parse("tel:"+number));  
        startActivity(callIntent);
    	}
    	catch(Exception e)
    	{
    		String ex=e.toString();
    	}
        
    }

    //Function to send sms
	public void sendsms(String m,ArrayList<String> l)
	{
		try
		{
		 Intent intent=new Intent(getApplicationContext(),Home.class);  
         PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);  
           
         //Get the SmsManager instance and call the sendTextMessage method to send message  
         SmsManager sms=SmsManager.getDefault();
         for(int i=0;i<l.size();i++)
         {
        	 sms.sendTextMessage(l.get(i).toString(), null, m, pi,null);
         }
         
           
         Toast.makeText(getApplicationContext(), "Message Sent successfully!",  
             Toast.LENGTH_LONG).show();  
		}
		catch (Exception e) {
		String error=e.toString();
		Toast.makeText(getApplicationContext(),error,  
	             Toast.LENGTH_LONG).show();
		}
		
	}
	
	// function to check GPS on/off status
		  private void showGPSDisabledAlertToUser(){
		        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Sensorcallingevent.this);
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
		                
		            }
		        });
		        AlertDialog alert = alertDialogBuilder.create();
		        alert.show();
		    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sensorcallingevent, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
		Intent i=new Intent(Sensorcallingevent.this,Home.class);
		startActivity(i);
	
	}

}
