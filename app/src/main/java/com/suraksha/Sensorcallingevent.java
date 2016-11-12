package com.suraksha;


import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.CheckInternet.CheckInternet;
import com.SessionManager.SessionManager;
import com.webservice.Service1;

public class Sensorcallingevent extends Activity {

	LocationManager locationManager;
	static String  msg,maplink,umobile;
	private ProgressBar spinner;
	private TextView tv;
	Connection conn=null;
	Statement stmnt=null;
	String num;
	SessionManager session;
	CheckInternet checknet;
	Service1 ws;
	String lat="", lon="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensorcallingevent);

		session=new SessionManager(Sensorcallingevent.this);

		checknet=new CheckInternet(Sensorcallingevent.this);
		spinner = (ProgressBar)findViewById(R.id.progressBar1);
		ws=new Service1();
		tv=(TextView)findViewById(R.id.textView1);
		spinner.setVisibility(View.VISIBLE);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		locationManager = (LocationManager)  Sensorcallingevent.this.getSystemService(Context.LOCATION_SERVICE);
		boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if(!checknet.isNetworkAvailable()){
			showcheckInternet();
		}else if(!isGPSEnabled){
			showGPSDisabledAlertToUser();
		}else {
			try {

				LocationListener locationListener = new LocationListener() {
					public void onLocationChanged(Location location) {
						if ( Build.VERSION.SDK_INT >= 23 &&
								ContextCompat.checkSelfPermission(Sensorcallingevent.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
								ContextCompat.checkSelfPermission(Sensorcallingevent.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
							return;
						}
						try {
							lat = Double.toString(location.getLatitude());
							lon = Double.toString(location.getLongitude());

							ArrayList<String> l1 = new ArrayList<String>();
							ArrayList<String> l2 = new ArrayList<String>();


							boolean a = (session.Getmobilno1().length() == 1);
							boolean b = (session.Getmobilno2().length() == 1);
							boolean c = (session.Getmobilno3().length() == 1);

							if (!a) {
								l2.add(session.Getmobilno1());
							}
							if (!b) {
								l2.add(session.Getmobilno2());
							}
							if (!c) {
								l2.add(session.Getmobilno3());
							}

							maplink = "http://maps.google.com/?q=" + lat + "," + lon;
							msg = "Please help! location: http://maps.google.com/?q=" + lat + "," + lon;


							umobile = session.GetMobileNo();

							num = session.Getmobilno1();
							insertalertLocation fd=new insertalertLocation();
							fd.execute();


							sendsms(msg, l2);
							AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
							audio.setRingerMode(1);
							CountDownTimer Timer;
							Timer = new CountDownTimer(5000, 5000) {
								public void onFinish() {
									calling(num);
									session.Savepreferences("shakefeature", "true");
									startService(new Intent(Sensorcallingevent.this, Shaker_Service_updated.class));
//									startService(new Intent(Sensorcallingevent.this, ShakeDetector.class));
								}

								@Override
								public void onTick(long millisUntilFinished) {
									// TODO Auto-generated method stub
								}
							}.start();
							locationManager.removeUpdates(this);
							locationManager = null;

							spinner.setVisibility(View.GONE);
							tv.setText("SOS Alert has been sent");
							session.Savepreferences("startSencer", "");
						} catch (Exception e) {
							calling(num);
						}


					}

					public void onStatusChanged(String provider, int status, Bundle extras) {
					}

					public void onProviderEnabled(String provider) {

					}

					public void onProviderDisabled(String provider) {

					}
				};
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1500000, 100, locationListener);
			} catch (Exception e) {
				String ex = e.toString();
			}


		}
	
	}

	//function to insert panic alert details


	private class insertalertLocation extends AsyncTask<Void, Void, Void> {


		@Override
		protected Void doInBackground(Void... params) {
			try{
				 ws.InsertAlertLocation(umobile,msg, lat, lon);
			 }catch(Exception e){
				e.getMessage();
			}
			return null;
		}


	}


    public void calling(String num)
    {
		if ( Build.VERSION.SDK_INT >= 23 &&
				ContextCompat.checkSelfPermission(Sensorcallingevent.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ) {
			return;
		}
    	try{
    	String number=num;
        Intent callIntent = new Intent(Intent.ACTION_CALL);  
        callIntent.setData(Uri.parse("tel:" + number));
			 startActivity(callIntent);

    	}
    	catch(Exception e){
    		String ex=e.toString();
    	}
        
    }

    //Function to send sms
	public void sendsms(String m,ArrayList<String> l)
	{
		try
		{
		 Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
         PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);  
           
         //Get the SmsManager instance and call the sendTextMessage method to send message  
         SmsManager sms=SmsManager.getDefault();
         for(int i=0;i<l.size();i++){
        	 sms.sendTextMessage(l.get(i).toString(), null, m, pi,null);
         }
         
           
         Toast.makeText(getApplicationContext(), "Message Sent successfully!",Toast.LENGTH_LONG).show();
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
		        .setPositiveButton("Goto SettingsFragment Page To Enable GPS",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
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
	private void showcheckInternet(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Sensorcallingevent.this);
		alertDialogBuilder.setMessage("Internet not available in your device. Would you like to enable it?")
				.setCancelable(false)
				.setPositiveButton("Goto SettingsFragment Page To Enable Data",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent callGPSSettingIntent = new Intent(
										android.provider.Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
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
		 getMenuInflater().inflate(R.menu.sensorcallingevent, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();

				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		finish();

	}

}
