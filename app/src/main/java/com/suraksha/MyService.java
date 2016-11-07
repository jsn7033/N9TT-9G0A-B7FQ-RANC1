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
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.text.format.Time;
import android.widget.TextView;
import android.widget.Toast;

import com.SessionManager.SessionManager;
import com.webservice.Service1;

public class MyService extends Service {
    Databaseclass db;
    LocationManager locationManager;
    private static final String TAG = "MyService";
    String lat = "", lon = "";

    long REFRESH_TIME = 60 * 1000;
    Connection conn = null;
    Statement stmnt = null;
    String name, mobile;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    SessionManager session;
    Service1 ws;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        session = new SessionManager(MyService.this);

        ws = new Service1();
        mobile = session.GetMobileNo();

        if (mTimer != null) {
            mTimer.cancel();
        } else {
            mTimer = new Timer();
        }
        mTimer.scheduleAtFixedRate(new TimerTask()


        {


            @Override
            public void run() {

                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);


                            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                            boolean isGPSEnabled = locationManager
                                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

                            String status = session.Getshechfeature();
                            if (status.equals("true") && !isMyServiceRunning(Shaker_Service_updated.class)) {
                                if (!isGPSEnabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                                    location_notification();
                                }
                                startService(new Intent(MyService.this, Shaker_Service_updated.class));
                            } else {
                                // insertSakeStatus();
                            }
                        } catch (Exception e) {
                            String ex = e.toString();
                        }
                    }
                });
            }
        }, 0, REFRESH_TIME);
    }

    public void insertSakeStatus() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(MyService.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MyService.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) MyService.this.getSystemService(Context.LOCATION_SERVICE);
        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                lat = Double.toString(location.getLatitude());
                lon = Double.toString(location.getLongitude());
                String loc = lat;
                insertalertLocation gf = new insertalertLocation();
                gf.execute();
                // getmessagefromserver()

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 1000, locationListener);

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

    private class insertalertLocation extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            try {
                ws.InsertAlertLocation(mobile, "sheker service is off", lat, lon);
            } catch (Exception e) {
                e.getMessage();
            }
            return null;
        }


    }


    //display notification
    public void notification(int c) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(MyService.this)
                        .setSmallIcon(R.drawable.howzaticon)
                        .setContentTitle("HowZat SOS")
                        .setOngoing(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true)
                        .setContentText(c + " new message from HowZat SOS");

        Intent resultIntent = new Intent(MyService.this, Home.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(MyService.this);
        stackBuilder.addParentStack(Home.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(100, mBuilder.build());
    }

    //notification for location off
    public void location_notification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(MyService.this)
                        .setSmallIcon(R.drawable.howzaticon)
                        .setContentTitle("Location service is disabled")
                        .setDefaults(Notification.DEFAULT_LIGHTS)
                        .setAutoCancel(true)
                        .setContentText("Touch to enable your location");

        Intent resultIntent = new Intent(
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(MyService.this);
        stackBuilder.addParentStack(Home.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(100, mBuilder.build());
    }

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
