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

public class Shaker_Service_updated extends Service implements SensorEventListener{

	Databaseclass db;
	ArrayList<String> list;
	 private float sensorspeed;
	private SensorManager sensorManager; 
	// Minimum acceleration needed to count as a shake movement
    private static final int MIN_SHAKE_ACCELERATION = 7;
    
    // Minimum number of movements to register a shake
    private static final int MIN_MOVEMENTS = 2;
    
    // Maximum time (in milliseconds) for the whole shake to occur
    private static final int MAX_SHAKE_DURATION = 500;
	
    // Arrays to store gravity and linear acceleration values
	private float[] mGravity = { 0.0f, 0.0f, 0.0f };
	private float[] mLinearAcceleration = { 0.0f, 0.0f, 0.0f };
	
	// Indexes for x, y, and z values
	private static final int X = 0;
	private static final int Y = 1;
	private static final int Z = 2;

	// OnShakeListener that will be notified when the shake is detected
	//private OnShakeListener mShakeListener;
	
	// Start time for the shake detection
	long startTime = 0;
	
	// Counter for shake movements
	int moveCount = 0;
	// Constructor that sets the shake listener
    /*public Shaker_Service_updated(OnShakeListener shakeListener) {
    	mShakeListener = shakeListener;
    }*/
	@Override
    public void onCreate() {
       // Toast.makeText(this, "", Toast.LENGTH_LONG).show();
    	/*db=new Databaseclass(Shaker_Service.this);
    	list=new ArrayList<String>();
    	lastUpdate = System.currentTimeMillis();*/
		db=new Databaseclass(Shaker_Service_updated.this);
		list=new ArrayList<String>();
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
	/*@Override
	   public int onStartCommand(Intent intent, int flags, int startId) {
	      // Let it continue running until it is stopped.
	     // Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
	      return START_STICKY;
	   }*/
	
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
// This method will be called when the accelerometer detects a change.
    	
    	// Call a helper method that wraps code from the Android developer site
    	setCurrentAcceleration(event);
         
        // Get the max linear acceleration in any direction
        float maxLinearAcceleration = getMaxCurrentLinearAcceleration();
        
        sensorspeed=4;
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
    	sensorspeed+=0.3;
    	}
        // Check if the acceleration is greater than our minimum threshold
        if (maxLinearAcceleration > sensorspeed) {
        	long now = System.currentTimeMillis();
        	
        	// Set the startTime if it was reset to zero
        	if (startTime == 0) {
        		startTime = now;
        	}
        	
        	long elapsedTime = now - startTime;
        	
        	// Check if we're still in the shake window we defined
        	if (elapsedTime > MAX_SHAKE_DURATION) {
        		// Too much time has passed. Start over!
        		resetShakeDetection();
        	}
        	else {
        		// Keep track of all the movements
        		moveCount++;
        		
        		// Check if enough movements have been made to qualify as a shake
        		if (moveCount > MIN_MOVEMENTS) {
        			// It's a shake! Notify the listener.
        			//mShakeListener.onShake();
        			 Intent intent = new Intent(Shaker_Service_updated.this, CountdownActivity.class); //chnaged
              	   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              	   startActivity(intent);
              	   
        			// Reset for the next one!
        			resetShakeDetection();
        		}
        	}
        }
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	private void setCurrentAcceleration(SensorEvent event) {
       	/*
    	 *  BEGIN SECTION from Android developer site. This code accounts for 
    	 *  gravity using a high-pass filter
    	 */
    	
    	// alpha is calculated as t / (t + dT)
        // with t, the low-pass filter's time-constant
        // and dT, the event delivery rate

        final float alpha = 0.8f;

        // Gravity components of x, y, and z acceleration
        mGravity[X] = alpha * mGravity[X] + (1 - alpha) * event.values[X];
        mGravity[Y] = alpha * mGravity[Y] + (1 - alpha) * event.values[Y];
        mGravity[Z] = alpha * mGravity[Z] + (1 - alpha) * event.values[Z];

        // Linear acceleration along the x, y, and z axes (gravity effects removed)
        mLinearAcceleration[X] = event.values[X] - mGravity[X];
        mLinearAcceleration[Y] = event.values[Y] - mGravity[Y];
        mLinearAcceleration[Z] = event.values[Z] - mGravity[Z];
        
        /*
         *  END SECTION from Android developer site
         */
    }
    
    private float getMaxCurrentLinearAcceleration() {
    	// Start by setting the value to the x value
    	float maxLinearAcceleration = mLinearAcceleration[X];
    	
    	// Check if the y value is greater
        if (mLinearAcceleration[Y] > maxLinearAcceleration) {
        	maxLinearAcceleration = mLinearAcceleration[Y];
        }
        
        // Check if the z value is greater
        if (mLinearAcceleration[Z] > maxLinearAcceleration) {
        	maxLinearAcceleration = mLinearAcceleration[Z];
        }
        
        // Return the greatest value
        return maxLinearAcceleration;
    }
    
    private void resetShakeDetection() {
    	startTime = 0;
    	moveCount = 0;
    }
	/*public interface OnShakeListener {
    	public void onShake();
    }*/
    
}
