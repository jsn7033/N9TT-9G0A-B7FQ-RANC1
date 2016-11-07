package com.suraksha;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.format.Time;
import android.widget.TextView;
import android.widget.Toast;

public class MyService extends Service 
{	
	Databaseclass db;
	boolean isGPSEnabled = false;	
	boolean isNetworkEnabled = false;
	LocationManager locationManager;
	private static final String TAG = "MyService";
	String lat="", lon="";
	TextView tv;
	//Timer mTimer;
	long REFRESH_TIME=60*1000;
	Connection conn=null;
	 Statement stmnt=null;
	 String name,mobile;
	 ArrayList<String> l1;
	 private Handler mHandler = new Handler();
	    // timer handling
	    private Timer mTimer = null;
	    ArrayList<String> messagelist=new ArrayList<String>();
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		db=  new Databaseclass(MyService.this);
		db.getReadableDatabase();
		l1=new ArrayList<String>();
		l1=db.getnamemobile();
		//name=l1.get(0).toString();
		mobile=l1.get(1).toString();
		 
		 if(mTimer != null) {
	            mTimer.cancel();
	        } else {
	            // recreate new
	            mTimer = new Timer();
	        }
		 mTimer.scheduleAtFixedRate(new TimerTask()
		 
		 
		 {
			
		     
			 
			 @Override
			 public void run(){
				 
				 mHandler.post(new Runnable() {
					 
		                @Override
		                public void run() {
		                	try
		                	{
		        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();		
				StrictMode.setThreadPolicy(policy);
				
				
				    	locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
				    	boolean isGPSEnabled = locationManager
			                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
				    	if(!isGPSEnabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
		    			{
		    				location_notification();
		    			}
				    	String status=db.getshakestatus();
	    				
	    				/*if(status.equals("true") && !isMyServiceRunning(Shaker_Service.class))
	    				{
	    					startService(new Intent(MyService.this, Shaker_Service.class));
	    				}
	    				if(status.equals("true") && !isMyServiceRunning(Shaker_Service.class))
	    				{
	    					stopService(new Intent(MyService.this, Shaker_Service.class));
	    					startService(new Intent(MyService.this, Shaker_Service.class));
	    				}*/
				    	if(status.equals("true") && !isMyServiceRunning(shake_test.class))
	    				{
	    					startService(new Intent(MyService.this, shake_test.class));
	    				}
	    				if(status.equals("true") && !isMyServiceRunning(shake_test.class))
	    				{
	    					stopService(new Intent(MyService.this, shake_test.class));
	    					startService(new Intent(MyService.this, shake_test.class));
	    				}
				    	else
				    	{
				        	// Acquire a reference to the system Location Manager
				    		locationManager = (LocationManager) MyService.this.getSystemService(Context.LOCATION_SERVICE);
				    		// Define a listener that responds to location updates
				    		LocationListener locationListener = new LocationListener() {
				    			public void onLocationChanged(Location location) {
				    				// Called when a new location is found by the network location provider.
				    				lat = Double.toString(location.getLatitude());
				    				lon = Double.toString(location.getLongitude());
				    				String loc=lat;
				    				insertlocation();
				    				getmessagefromserver();
				    				
				    				
				    				//locationManager.removeUpdates(this);
				    		        //locationManager=null;
				    				
				    				
				    			}

				    		    public void onStatusChanged(String provider, int status, Bundle extras) {}
				    			public void onProviderEnabled(String provider) {}
				    			public void onProviderDisabled(String provider) {}
				    		};
				    		
				    		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,10000, 1000, locationListener);
		                	}
		                	
		                	}
		                	
		                	catch(Exception e)
		                	{
		                		String ex=e.toString();
		                	}

							
		                }
		                
		            });
			 }	 
		 }, 0, REFRESH_TIME);
		
		
				
		
	}

	
	@Override
	public void onDestroy() {
		Toast.makeText(this, "Service has been Stopped", Toast.LENGTH_LONG).show();
		sendBroadcast(new Intent("YouWillNeverKillMe"));
		//player.stop();
	}
	
	@Override
	public void onStart(Intent intent, int startid) {
		//Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();
		
		//player.start();
	}
	//function to fetch message from server
	public void getmessagefromserver()
	{
		try {
			String driver="net.sourceforge.jtds.jdbc.Driver";
			Class.forName(driver).newInstance();
			 String connString="jdbc:jtds:sqlserver://110.232.255.114:1433/howzat;encrypt=false;user=sa;password=pass@123;";
			 String username="sa";
			 String password="pass@123";

			    db.getReadableDatabase();
				l1=new ArrayList<String>();
				l1=db.getnamemobile();				
				mobile=l1.get(1).toString();	
            conn=DriverManager.getConnection(connString,username,password);
            stmnt = conn.createStatement();
            String abc=conn.toString();
            int counter=0;
          ///////////
            ResultSet rs=stmnt.executeQuery("select message from Chat_details where sender='server' and reciever='"+mobile+"' and read_unread_status='0'");
            while(rs.next()){
	            
            	messagelist.add(rs.getString(1));
            	counter++;         	
            
            }
            String SQL = "UPDATE Chat_details set read_unread_status='1' where sender='server' and reciever='"+mobile+"' and read_unread_status='0'";
	        stmnt.execute(SQL);
	        String msg;
	        for(int i=0;i<messagelist.size();i++)
	        {
	        msg=messagelist.get(i).toString();	
	        db.insertnewmessage(msg);
	        }
            if(counter>0)
            {
            	notification(counter);
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
	 //function to insert current location
	 public void insertlocation()
		{
			try {
				String driver="net.sourceforge.jtds.jdbc.Driver";
				Class.forName(driver).newInstance();
				 String connString="jdbc:jtds:sqlserver://110.232.255.114:1433/howzat;encrypt=false;user=sa;password=pass@123;";
				 String username="sa";
				 String password="pass@123";

				    db.getReadableDatabase();
					l1=new ArrayList<String>();
					l1=db.getnamemobile();				
					mobile=l1.get(1).toString();
				 
	            conn=DriverManager.getConnection(connString,username,password);
	            stmnt = conn.createStatement();
	          
	                     
	            ////////////
	            Time today = new Time(Time.getCurrentTimezone());
	            today.setToNow();
	            String datetime=today.monthDay+"/"+(today.month+1)+"/"+today.year+" "+today.format("%k:%M:%S");
	           
	            ///////////
	            String SQL = "INSERT INTO LOCATION_UPLOAD(lat,lon,user_mobileno) VALUES('" + lat + "', '"+ lon + "', '"+mobile+"');";
	          
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
	 
	 //display notification
	 public void notification(int c)
		{
			NotificationCompat.Builder mBuilder =
			        new NotificationCompat.Builder(MyService.this)
			        .setSmallIcon(R.drawable.howzaticon)
			        .setContentTitle("HowZat SOS")
			        .setOngoing(true)
			        .setDefaults(Notification.DEFAULT_ALL)
			        .setAutoCancel(true)
			        .setContentText(c+" new message from HowZat SOS");
					
			// Creates an explicit intent for an Activity in your app
			Intent resultIntent = new Intent(MyService.this, Home.class);
			
        
			// The stack builder object will contain an artificial back stack for the
			// started Activity.
			// This ensures that navigating backward from the Activity leads out of
			// your application to the Home screen.
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(MyService.this);
			// Adds the back stack for the Intent (but not the Intent itself)
			stackBuilder.addParentStack(Home.class);
			// Adds the Intent that starts the Activity to the top of the stack
			stackBuilder.addNextIntent(resultIntent);
			PendingIntent resultPendingIntent =
			        stackBuilder.getPendingIntent(
			            0,
			            PendingIntent.FLAG_UPDATE_CURRENT
			        );
			mBuilder.setContentIntent(resultPendingIntent);
			NotificationManager mNotificationManager =
			    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			// mId allows you to update the notification later on.
			mNotificationManager.notify(100, mBuilder.build());
		}
	 
	 //notification for location off
	 public void location_notification()
		{
			NotificationCompat.Builder mBuilder =
			        new NotificationCompat.Builder(MyService.this)
			        .setSmallIcon(R.drawable.howzaticon)
			        .setContentTitle("Location service is disabled")			        
			        .setDefaults(Notification.DEFAULT_LIGHTS)
			        .setAutoCancel(true)
			        .setContentText("Touch to enable your location");
					
			// Creates an explicit intent for an Activity in your app
			//Intent resultIntent = new Intent(Track.this, Home.class);
			Intent resultIntent = new Intent(
                 android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        
			// The stack builder object will contain an artificial back stack for the
			// started Activity.
			// This ensures that navigating backward from the Activity leads out of
			// your application to the Home screen.
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(MyService.this);
			// Adds the back stack for the Intent (but not the Intent itself)
			stackBuilder.addParentStack(Home.class);
			// Adds the Intent that starts the Activity to the top of the stack
			stackBuilder.addNextIntent(resultIntent);
			PendingIntent resultPendingIntent =
			        stackBuilder.getPendingIntent(
			            0,
			            PendingIntent.FLAG_UPDATE_CURRENT
			        );
			mBuilder.setContentIntent(resultPendingIntent);
			NotificationManager mNotificationManager =
			    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			// mId allows you to update the notification later on.
			mNotificationManager.notify(100, mBuilder.build());
		}
	 //function to check whether a service is running or not
	 private boolean isMyServiceRunning(Class<?> serviceClass) {
		    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
		        if (serviceClass.getName().equals(service.service.getClassName())) {
		            return true;
		        }
		    }
		    return false;
		}
		
}
