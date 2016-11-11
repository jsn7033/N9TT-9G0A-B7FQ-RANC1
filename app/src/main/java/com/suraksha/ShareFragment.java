package com.suraksha;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.AppHelper.GPSTracker;
import com.CheckInternet.CheckInternet;
import com.core.BaseFragment;

/**
 * Created by RTH0102001 on 27-04-2016.
 */
public class ShareFragment extends BaseFragment {
    Button shareButton;
    LocationManager locationManager;
    boolean isGPSEnabled = false;
    String lat = "", lon = "";
    CheckInternet checkInternet;
    static String msg;
    GPSTracker gps;

    AppTitleCallback mAppTitleCallback;


    public interface AppTitleCallback {
        void title(String title);
    }


    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);

        if (context instanceof Home) {
            mAppTitleCallback = (AppTitleCallback) context;
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppTitleCallback.title(getString(R.string.menu_share_text));
    }



//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.share);
//
//        Toolbar mToolbar = loadToolbar("ShareFragment");
//        setSupportActionBar(mToolbar);
//        mToolbar.setLogo(R.drawable.howzaticon_);
//        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//        checkInternet = new CheckInternet(ShareFragment.this);
//        shareButton = (Button) findViewById(R.id.shareButton);
//        gps = new GPSTracker(ShareFragment.this);
//        shareButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sharedialog();
//            }
//        });
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.share, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shareButton = (Button) view.findViewById(R.id.shareButton);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        checkInternet = new CheckInternet(getActivity());
        gps = new GPSTracker(getActivity());
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedialog();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                getActivity().finish();


                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sharedialog() {
        final CharSequence[] items = {"Share my location", "Share this app"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose to share");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Share my location")) {

                    locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
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
        startActivity(Intent.createChooser(sharingIntent, "ShareFragment via"));
    }

    public void show(String str) {
        Toast.makeText(getActivity(), str, Toast.LENGTH_LONG).show();
    }
}


//public class ShareFragment extends Fragment{
//
//    View view;
//
//        Button shareButton;
//    LocationManager locationManager;
//    boolean isGPSEnabled = false;
//    String lat = "", lon = "";
//    CheckInternet checkInternet;
//    static String msg;
//    GPSTracker gps;
//
//
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        view = inflater.inflate(R.layout.share, container, false);
//
//                checkInternet = new CheckInternet(getActivity());
//        shareButton = (Button) view.findViewById(R.id.shareButton);
//        gps = new GPSTracker(getActivity());
//        shareButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sharedialog();
//            }
//        });
//
//
//        return view;
//    }
//
//        private void sharedialog() {
//        final CharSequence[] items = {"ShareFragment my location", "ShareFragment this app"};
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(ShareFragment.this);
//        builder.setTitle("Choose to share");
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                if (items[item].equals("ShareFragment my location")) {
//
//                    locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//                    // getting GPS status
//                    isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//
//                    if (!isGPSEnabled) {
//                        show("Please enable GPS");
//                    } else if (!checkInternet.isNetworkAvailable()) {
//                        show("Please enable internet connection");
//                    } else {
//                        msg = "My location is: http://maps.google.com/?q=" + gps.getLongitude() + "," + gps.getLongitude() + " \n Sent by Howzat SOS App \n click to download http://www.howzatsos.com/download ";
//                        shareIt(msg);
//
//                    }
//                } else {
//
//                    String playstorelink = "HowZat sos App click link to download http://www.howzatsos.com/download";
//                    shareIt(playstorelink);
//                }
//            }
//        });
//        builder.show();
//    }
//
//        public void show(String str) {
//        Toast.makeText(getActivity(), str, Toast.LENGTH_LONG).show();
//    }
//
//        private void shareIt(String link) {
//        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//        sharingIntent.setType("text/plain");
//        String shareBody = link;
//        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "HowZat SOS App");
//        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
//        startActivity(Intent.createChooser(sharingIntent, "ShareFragment via"));
//    }
//
//}