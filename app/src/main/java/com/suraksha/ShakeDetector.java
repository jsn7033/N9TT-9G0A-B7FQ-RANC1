package com.suraksha;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

import com.SessionManager.SessionManager;

/**
 * This service is responsible to sense three times shake and inform to trigger alert
 */
public class ShakeDetector extends Service implements ShakeEventManager.ShakeListener{

    public static boolean isRunning = false;
    SessionManager mSessionManager;
    ShakeEventManager sd;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSessionManager = new SessionManager(this);
//        Log.i(TAG, "onCreate: ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
         super.onStartCommand(intent, flags, startId);

        SQLiteDatabase db = DbHandler.getInstance(this).getDbObj(0);
        double maxValue = DBDao.getInstance().getMaxValue(db);
        Log.i(TAG, "Max Value: " + maxValue);
        sd = new ShakeEventManager();
        sd.setMOV_THRESHOLD(maxValue);
        sd.enableRecordingValues(false);
        sd.setListener(this);
        sd.init(this);

        isRunning = true;

        return START_STICKY;
    }

    public static final String TAG="Shake Service";

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
        isRunning = false;
        sd.deregister();
    }

    @Override
    public void onShake() {
        if(mSessionManager.isFromSettingShake()){
            sendBroadcast(new Intent(ShakeSetting.REGISTER_SHAKE));
        }else{
            Intent intent = new Intent(this, CountdownActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

//            sendBroadcast(new Intent("trigger_shake_action"));
        }
    }
}
