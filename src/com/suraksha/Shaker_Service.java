package com.suraksha;

import java.util.ArrayList;

import android.os.CountDownTimer;
import android.os.IBinder;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class Shaker_Service extends Service implements SensorEventListener{
    private static final String TAG = "Shaker_Service";
    private SensorManager sensorManager;    
    Databaseclass db;
    private long lastUpdate;
    ArrayList<String> list;
    private float sensorspeed;
    float gravity[];
    CountDownTimer Timer;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
       // Toast.makeText(this, "", Toast.LENGTH_LONG).show();
    	db=new Databaseclass(Shaker_Service.this);
    	list=new ArrayList<String>();
    	lastUpdate = System.currentTimeMillis();
    	 sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
         // add listener. The listener will be HelloAndroid (this) class
         sensorManager.registerListener(this, 
                 sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                 SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onDestroy() {
       // Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
    	 sensorManager.unregisterListener(this);
    	   
    }
    
   

    @Override
    public void onStart(Intent intent, int startid) {


       
        
    }

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		try
		{
		
		gravity=new float[5];
		
		sensorspeed=10;
		list=db.getsettings();
    	int speed=Integer.parseInt(list.get(1).toString());
    	/*if(speed>50)
    	{
    		speed=100-speed;
    	}
    	else if(speed<50)
    	{
    		speed=100-speed;
    	}*/
    	
    	
    	
    	for(int i=0;i<--speed;i++)
    	{
    	sensorspeed+=0.8;
    	}
    	
		
		   if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER)
		   {
			   
			   	  float x = event.values[0];
			      float y = event.values[1];
			      float z = event.values[2];
			      mAccelLast = mAccelCurrent;
			      mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
			      float delta = mAccelCurrent - mAccelLast;
			      mAccel = mAccel * 0.9f + delta; // perform low-cut filter
			      final float alpha=0.8f;
		          double netForce=event.values[0]*event.values[0];  
		          netForce=event.values[1]*event.values[1];  
		          netForce=event.values[2]*event.values[2];  
		          netForce = SensorManager.GRAVITY_EARTH-Math.sqrt(netForce);
		          long actualTime = System.currentTimeMillis();
              
               if (mAccel>sensorspeed-2)
               {
            	   try{ 
            		   stopService(new Intent(Shaker_Service.this, Shaker_Service.class));
            		   Intent intent = new Intent(Shaker_Service.this, CountdownActivity.class); //chnaged
                	   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                	   startActivity(intent);
//                	   sensorManager.unregisterListener(Shaker_Service.this);
                	   
                	   Timer = new CountDownTimer(5000, 5000)                	   
                	   {
       		            public void onFinish() 
       		            {
       		            		/* sensorManager.registerListener(Shaker_Service.this, 
       		                     sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
       		                     SensorManager.SENSOR_DELAY_NORMAL);*/
       		            		startService(new Intent(Shaker_Service.this, Shaker_Service.class));
       		            }
       		            @Override
       		            public void onTick(long millisUntilFinished) {
       		                // TODO Auto-generated method stub
       		            }
       		        }.start();
               }
               catch(Exception e)
               {
            	   String ex=e.toString();
               }
               }
           } 
		}
		catch(Exception e)
		{
			String ex=e.toString();
		}
	}
}
