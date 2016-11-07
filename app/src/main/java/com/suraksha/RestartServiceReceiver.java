package com.suraksha;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.SessionManager.SessionManager;

public class RestartServiceReceiver extends BroadcastReceiver
{

    private static final String TAG = "RestartServiceReceiver";
    SessionManager sessionManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "onReceive");
        sessionManager=new SessionManager(context.getApplicationContext());
        if (sessionManager.Getshechfeature().equals("true") && !sessionManager.isMyServiceRunning(Shaker_Service_updated.class)) {
//            context.getApplicationContext().startService(new Intent(context.getApplicationContext(), ShakeDetector.class));
            context.getApplicationContext().startService(new Intent(context.getApplicationContext(), Shaker_Service_updated.class));
        }
    }

}


