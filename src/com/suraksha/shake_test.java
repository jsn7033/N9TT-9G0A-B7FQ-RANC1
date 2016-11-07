package com.suraksha;

import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class shake_test extends Service implements SensorEventListener {

	private SensorManager sensorManager;
	private long lastUpdate;
	 Databaseclass db;
	 ArrayList<String> list;
	    private float sensorspeed;
	Boolean bool = false, bool1 = false;
	CountDownTimer Timer;
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
    public void onCreate() {
		db=new Databaseclass(shake_test.this);
    	list=new ArrayList<String>();
		/*sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
		                SensorManager.SENSOR_DELAY_NORMAL);*/
		lastUpdate = System.currentTimeMillis();

    }
	@Override
	public void onSensorChanged(SensorEvent event) {
		 if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
	            float[] values = event.values;
	            // Movement
	            float x = values[0];
	            float y = values[1];
	            float z = values[2];

	            float accelationSquareRoot = (x * x + y * y + z * z)
	                    / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
//	            System.out.println("accelationSquareRoot is:- "
//	                    + accelationSquareRoot);
	            Log.d("speed", accelationSquareRoot+"");
	            long actualTime = System.currentTimeMillis();
	            
	            sensorspeed=0;
	    		list=db.getsettings();
	        	int speed=Integer.parseInt(list.get(1).toString());
	        	if(speed>50)
	        	{
	        		speed=100-speed;
	        	}
	        	else if(speed<50)
	        	{
	        		speed=100-speed;
	        	}
	        	
	        	for(int i=0;i<--speed;i++)
	        	{
	        	sensorspeed+=0.8;
	        	}
	            
	            
	            if (accelationSquareRoot >= sensorspeed) //
	            {
	            	stopService(new Intent(shake_test.this, Shaker_Service.class));
	                bool1 = false;
	                if (bool == false) {
	                    if (actualTime - lastUpdate < 3000) {
	                        return;
	                    }
	                    lastUpdate = actualTime;
	                    //Here Define Method for OnStartShaking
	                    
	                       Intent intent = new Intent(shake_test.this, CountdownActivity.class); //chnaged
	                	   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	                	   startActivity(intent);
	                	   Timer = new CountDownTimer(5000, 5000)                	   
	                	   {
	       		            public void onFinish() 
	       		            {
	       		            		/* sensorManager.registerListener(Shaker_Service.this, 
	       		                     sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
	       		                     SensorManager.SENSOR_DELAY_NORMAL);*/
	       		            		startService(new Intent(shake_test.this, Shaker_Service.class));
	       		            }
	       		            @Override
	       		            public void onTick(long millisUntilFinished) {
	       		                // TODO Auto-generated method stub
	       		            }
	       		        }.start();
	                    bool = true;
	                } else {
	                   return;
	                }
	            } else {
	                bool = false;
	               // System.out.println("Jay Not Stuffing");
	                Log.d("remark", "nothing");
	                if (bool1 == false) {
	                    if (actualTime - lastUpdate < 4000) {
	                        return;
	                    }
	                    lastUpdate = actualTime;
	                    //Here Define Method for OnStopShaking
	                    bool1 = true;
	                } else {
	                    return;
	                }
	            }
	        }
		
	}
	
	@Override
    public void onStart(Intent intent, int startid) {


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
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
