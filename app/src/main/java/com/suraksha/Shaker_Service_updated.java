

package com.suraksha;

import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.SessionManager.SessionManager;

public class Shaker_Service_updated extends Service implements ShakeEventManager.ShakeListener {

    SessionManager session;

//    private float sensorspeed;
//    private SensorManager sensorManager;
//    private static final int MIN_MOVEMENTS = 1;
//
//    private static final int MAX_SHAKE_DURATION = 500;
//
//    private float[] mGravity = {0.0f, 0.0f, 0.0f};
//    private float[] mLinearAcceleration = {0.0f, 0.0f, 0.0f};
//
//    private static final int X = 0;
//    private static final int Y = 1;
//    private static final int Z = 2;
//
//
//    long startTime = 0;
//    int moveCount = 1;

    ShakeEventManager sd;
    public static final String TAG="Shaker_Service_updated";

    @Override
    public void onCreate() {
        session = new SessionManager(Shaker_Service_updated.this);

        Log.i(TAG, "onCreate: service started");

//		sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
//		sensorManager.registerListener(this,
//				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
//				SensorManager.SENSOR_DELAY_NORMAL);


        SQLiteDatabase db = DbHandler.getInstance(this).getDbObj(0);
        double maxValue = DBDao.getInstance().getMaxValue(db);
        Log.i(TAG, "Max Value: " + maxValue);
        sd = new ShakeEventManager();
        sd.setMOV_THRESHOLD(maxValue);
        sd.enableRecordingValues(false);
        sd.setListener(this);
        sd.init(this);

//        isRunning = true;


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    //    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        return START_STICKY;
//    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        sd.deregister();
//		sensorManager.unregisterListener(this);
    }

//	@Override
//	public void onAccuracyChanged(Sensor arg0, int arg1) {
//	}

//	@Override
//	public void onSensorChanged(SensorEvent event) {


//		setCurrentAcceleration(event);
//		float maxLinearAcceleration = getMaxCurrentLinearAcceleration();
//
//		sensorspeed=4;
//
//		String cencerSencer=session.GetsencerSencitityValue();
//		if(cencerSencer.equals("")){
//			cencerSencer="5";
//		}
//		int speed=Integer.parseInt(cencerSencer);
//		if(speed<3){
//			speed=5;
//		}
//
//		for(int i=0;i<speed;i++){
//			sensorspeed+=0.3;
//		}
//		if (maxLinearAcceleration > sensorspeed) {
//			long now = System.currentTimeMillis();
//			if (startTime == 0) {
//				startTime = now;
//			}
//
//			long elapsedTime = now - startTime;
//
//			if (elapsedTime > MAX_SHAKE_DURATION) {
//				resetShakeDetection();
//				sensorspeed=4;
//			}
//			else {
//				moveCount++;
//				if (moveCount > MIN_MOVEMENTS) {
//
//					Intent intent = new Intent(Shaker_Service_updated.this, CountdownActivity.class);
//					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					startActivity(intent);
//
//
//					resetShakeDetection();
//
//				}
//			}
//		}

//	}

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onShake() {
//        Intent intent = new Intent(Shaker_Service_updated.this, CountdownActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);


        if(session.isFromSettingShake()){
            sendBroadcast(new Intent(ShakeSetting.REGISTER_SHAKE));
        }else{

//            SQLiteDatabase db = DbHandler.getInstance(this).getDbObj(0);
//            double maxValue = DBDao.getInstance().getMaxValue(db);
//            sd.setMOV_THRESHOLD(maxValue);

            Intent intent = new Intent(Shaker_Service_updated.this, CountdownActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

//            sendBroadcast(new Intent("trigger_shake_action"));
        }


    }
//	private void setCurrentAcceleration(SensorEvent event) {
//
//		final float alpha = 0.8f;
//
//		mGravity[X] = alpha * mGravity[X] + (1 - alpha) * event.values[X];
//		mGravity[Y] = alpha * mGravity[Y] + (1 - alpha) * event.values[Y];
//		mGravity[Z] = alpha * mGravity[Z] + (1 - alpha) * event.values[Z];
//		mLinearAcceleration[X] = event.values[X] - mGravity[X];
//		mLinearAcceleration[Y] = event.values[Y] - mGravity[Y];
//		mLinearAcceleration[Z] = event.values[Z] - mGravity[Z];
//
//
//	}
//
//	private float getMaxCurrentLinearAcceleration() {
//		float maxLinearAcceleration = mLinearAcceleration[X];
//
//		if (mLinearAcceleration[Y] > maxLinearAcceleration) {
//			maxLinearAcceleration = mLinearAcceleration[Y];
//		}
//
//		if (mLinearAcceleration[Z] > maxLinearAcceleration) {
//			maxLinearAcceleration = mLinearAcceleration[Z];
//		}
//		return maxLinearAcceleration;
//	}
//
//	private void resetShakeDetection() {
//		startTime = 0;
//		moveCount = 0;
//	}
}

