package com.suraksha;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.AppHelper.GPSTracker;
import com.CheckInternet.CheckInternet;

/**
 * Created by RTH0102001 on 27-04-2016.
 */
public class Share extends BaseActivity {
    Button shareButton;
    LocationManager locationManager;
    boolean isGPSEnabled = false;
    String lat = "", lon = "";
    CheckInternet checkInternet;
    static String msg;
    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share);

        Toolbar mToolbar = loadToolbar("Share");
        setSupportActionBar(mToolbar);
        mToolbar.setLogo(R.drawable.howzaticon_);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        checkInternet = new CheckInternet(Share.this);
        shareButton = (Button) findViewById(R.id.shareButton);
        gps = new GPSTracker(Share.this);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedialog();
            }
        });
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();


                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sharedialog() {
        final CharSequence[] items = {"Share my location", "Share this app"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Share.this);
        builder.setTitle("Choose to share");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Share my location")) {

                    locationManager = (LocationManager) Share.this.getSystemService(Context.LOCATION_SERVICE);
                    // getting GPS status
                    isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                    if (!isGPSEnabled) {
                        show("Please enable GPS");
                    } else if (!checkInternet.isNetworkAvailable()) {
                        show("Please enable internet connection");
                    } else {
                        msg = "My location is: http://maps.google.com/?q=" + gps.getLongitude() + "," + gps.getLongitude() + " \n Sent by Howzat SOS App \n click to download http://www.howzatsos.com/download ";
                        shareIt(msg);

                    }
                } else {

                    String playstorelink = "HowZat sos App click link to download http://www.howzatsos.com/download";
                    shareIt(playstorelink);
                }
            }
        });
        builder.show();
    }

    private void shareIt(String link) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = link;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "HowZat SOS App");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void show(String str) {
        Toast.makeText(Share.this, str, Toast.LENGTH_LONG).show();
    }
}
