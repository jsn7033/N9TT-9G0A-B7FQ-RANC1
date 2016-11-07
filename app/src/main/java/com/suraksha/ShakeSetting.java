package com.suraksha;

import android.*;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


/**
 * Ask user to shake his device and stores the input provided by sensor.
 *
 * author JSN
 */
import com.SessionManager.SessionManager;

public class ShakeSetting extends Activity implements View.OnClickListener, ShakeEventManager.ShakeListener {

    SessionManager mSessionManager;
    static int counter = 0;
    public static final String REGISTER_SHAKE = "register_shake";

    private Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake_setting);
        init();
        registerListener();

    }

    public void init() {
        btnStart = (Button) findViewById(R.id.btn_start);
        btnStart.setTag("start");

        mSessionManager = new SessionManager(this);
        mSessionManager.setFromSetting(true);
    }

    public void registerListener() {
        registerReceiver(broadcastReceiver, new IntentFilter(REGISTER_SHAKE));
        btnStart.setOnClickListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSessionManager.setFromSetting(false);

        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkIfServiceRunning() {
        if (!ShakeDetector.isRunning) {
            Intent intent = new Intent(this, Shaker_Service_updated.class);
            startService(intent);
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (counter < 15) {
                counter++;
                btnStart.setText("Continue");
//                btnStart.setTag("continue");
            } else if (counter == 15) {
                mSessionManager.Savepreferences("shakefeature", "true");
                btnStart.setText("Done");
                btnStart.setTag("done");
                counter = 0;

                try {
                    unregisterReceiver(broadcastReceiver);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };


    ShakeEventManager sd;

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_start:

                String tag = (String) view.getTag();

                if (tag.equals("start")) {
                    btnStart.setText("Continue");
                  startShake();
                } else if (tag.equals("done")) {
                    mSessionManager.setFromSetting(false);
                    int permission = ContextCompat.checkSelfPermission(ShakeSetting.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permission == PackageManager.PERMISSION_GRANTED) {
                        sd.saveToDb();
                        startService(new Intent(ShakeSetting.this, Shaker_Service_updated.class));
                        Toast.makeText(this, "Shake feature activated", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                        }
                    }
                }

                break;

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case 101:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startService(new Intent(ShakeSetting.this, Shaker_Service_updated.class));
                    sd.saveToDb();
                    finish();
                }
        }

    }



    public static final String TAG="Shake Listener";
    @Override
    public void onShake() {
        Log.i(TAG, "onShake: triggered");

        if (counter < 30) {
            counter++;
        } else if (counter ==30) {
            mSessionManager.Savepreferences("shakefeature", "true");
            btnStart.setText("Done");
            btnStart.setTag("done");
            counter = 0;
            sd.deregister();
        }
    }

    public void startShake(){
        sd = new ShakeEventManager();
        sd.enableRecordingValues(true);
        sd.setListener(this);
        sd.init(this);
    }
}
