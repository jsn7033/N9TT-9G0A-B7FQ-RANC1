package com.suraksha;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.CheckInternet.CheckInternet;
import com.SessionManager.SessionManager;
import com.core.BaseFragment;
import com.webservice.Service1;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class BroadcastFragment extends BaseFragment{


    ListView lvpost;
    ImageView ivcircle, ivshare;
    LocationManager locationManager;
    boolean isGPSEnabled = false;
    String lat = "", lon = "";

    static String msg;
    Button btn_new_broadcast;
    ArrayList<String> albid, aluname, alcity, albmedal, alsmedal, algmedal, albcontent, albimage,
            aluseful, aluseless, aldate, postimage, likeStatus, msgMobile;
    Service1 ws;
    ProgressDialog progress;
    SessionManager session;
    String postcat = "";
    CheckInternet checkInternet;


    private String MSG_ID = "id", UserName = "UserName", residence_city = "residence_city", bronze_badges_count = "bronze_badges_count",
            silver_badges_count = "silver_badges_count", gold_badges_count = "gold_badges_count",
            broadcast_content = "broadcast_content", broadcast_image = "broadcast_image", useful_count = "useful_count",
            useless_count = "useless_count", broadcast_date = "broadcast_date", photo_ftp = "photo_ftp",
            LikeStatus = "LikeStatus", MSG_mobile = "mobile";
    RelativeLayout ree, broadcastContainer;

    public BroadcastFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.activity_broadcast_fragment, container, false);
        ws = new Service1();
        ree = (RelativeLayout) view.findViewById(R.id.footer);
        checkInternet = new CheckInternet(getActivity());


        ree.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                getActivity().overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
                calling();
                return false;
            }
        });
        session = new SessionManager(getActivity());
        lvpost = (ListView) view.findViewById(R.id.lvPost);
        ivcircle = (ImageView) view.findViewById(R.id.ivcircle);
        ivshare = (ImageView) view.findViewById(R.id.ivShare);
        albid = new ArrayList<String>();
        aluname = new ArrayList<String>();
        alcity = new ArrayList<String>();
        albmedal = new ArrayList<String>();
        alsmedal = new ArrayList<String>();
        algmedal = new ArrayList<String>();
        albcontent = new ArrayList<String>();
        albimage = new ArrayList<String>();
        aluseful = new ArrayList<String>();
        aluseless = new ArrayList<String>();
        aldate = new ArrayList<String>();
        postimage = new ArrayList<String>();
        likeStatus = new ArrayList<String>();
        msgMobile = new ArrayList<String>();
        btn_new_broadcast = (Button) view.findViewById(R.id.btnnewbroadcast);
        broadcastContainer = (RelativeLayout) view.findViewById(R.id.broadcastContainer);
        btn_new_broadcast.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getActivity(), Add_new_broadcast.class);
                startActivity(i);

            }
        });
        //spinner = (ProgressBar)view.findViewById(R.id.progressBar1);
        ivcircle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                ConnectivityManager mgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = mgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                boolean connectedmobile = networkInfo != null && networkInfo.isConnected();
                NetworkInfo networkInfo2 = mgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                boolean connectedwifi = networkInfo2 != null && networkInfo2.isConnected();
                if (connectedmobile || connectedwifi) {
                    show("Please enable GPS for accuracy");
                    Intent i = new Intent(getActivity(), Mapplaces.class);
                    startActivity(i);

                } else {
                    show("Please enable internet connection");
                }


            }
        });
        // button for sending location  of user to control center
        ivshare.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sharedialog();// dialog to sending the location
            }
        });

        try {
            postcat = getArguments().getString("postcat");
        } catch (Exception e) {

        }

// checking internet connectivity
        if (checkInternet.isNetworkAvailable()) {
            getBdroicastmessage ds = new getBdroicastmessage(postcat);// method for getting all message
            ds.execute();
        } else {
            Toast.makeText(getActivity(), "Internet not available", Toast.LENGTH_LONG).show();
        }


        return view;
    }


    private class getBdroicastmessage extends AsyncTask<Void, Void, Void> {

        JSONArray ds;
        JSONObject dss = null;
        String value = "", POSTCAT = "";

        getBdroicastmessage(String postcat) {
            this.POSTCAT = postcat;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(getActivity());
            progress.setMessage("Loading...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                dss = ws.FetchBroadcasts(session.GetMobileNo(), POSTCAT);//calling web service to geting the all broadcast message
                Iterator iter = dss.keys();
                while (iter.hasNext()) {

                    String key = (String) iter.next();
                    if (key.equals("Value")) {
                        value = dss.getString(key);
                        ds = new JSONArray(value);
                    }
                }

            } catch (Exception e) {
                e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progress.dismiss();//otp:I3XW13,MobileNo:
            broadcastContainer.setVisibility(View.VISIBLE);
            if (ds != null) {
                if (ds.length() > 0) {
                    try {
                        for (int i = 0; i < ds.length(); i++) {
                            JSONObject obj;
                            obj = ds.getJSONObject(i);


                            try {
                                albid.add(obj.getString(MSG_ID));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                aluname.add(obj.getString(UserName));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                alcity.add(obj.getString(residence_city));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                albmedal.add(obj.getString(bronze_badges_count));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                alsmedal.add(obj.getString(silver_badges_count));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                algmedal.add(obj.getString(gold_badges_count));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                albcontent.add(obj.getString(broadcast_content));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                postimage.add(obj.getString(broadcast_image));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                aluseful.add(obj.getString(useful_count));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                aluseless.add(obj.getString(useless_count));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                aldate.add(obj.getString(broadcast_date));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                albimage.add(obj.getString(photo_ftp));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                likeStatus.add(obj.getString(LikeStatus));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                msgMobile.add(obj.getString(MSG_mobile));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                    BroadcastCustomList adapter = new BroadcastCustomList(getActivity(), albid, aluname, alcity, albmedal, alsmedal, algmedal,
                            albcontent, albimage, aluseful, aluseless, aldate, postimage, likeStatus, msgMobile);

                    lvpost.setAdapter(adapter);
                    adapter.notifyDataSetChanged();


                }

            }

            super.onPostExecute(result);
        }

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


// chicking the gps status
                    if (!isGPSEnabled) {
                        show("Please enable GPS");// message for enable gps
                    } else if (!checkInternet.isNetworkAvailable()) {
                        show("Please enable internet connection");// message fo enable internet
                    } else {

                        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                        // Define a listener that responds to location updates
                        LocationListener locationListener = new LocationListener() {
                            public void onLocationChanged(Location location) {
                                // Called when a new location is found by the network location provider.
                                if (Build.VERSION.SDK_INT >= 23 &&
                                        ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                        ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                lat = Double.toString(location.getLatitude());
                                lon = Double.toString(location.getLongitude());

                                msg = "My location is: http://maps.google.com/?q=" + lat + "," + lon + " \n Sent by Howzat SOS App \n click to download http://www.howzatsos.com/download ";
                                // method for share location though email
                                shareIt(msg);


                                locationManager.removeUpdates(this);
                                locationManager = null;

                            }

                            public void onStatusChanged(String provider, int status, Bundle extras) {
                            }

                            public void onProviderEnabled(String provider) {
                            }

                            public void onProviderDisabled(String provider) {
                            }
                        };
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 120000, 100, locationListener);
                    }
                } else {
                    //String playstorelink="https://play.google.com/store/apps/details?id=com.aviary.android.feather";
                    String playstorelink = "HowZat sos App click link to download http://www.howzatsos.com/download";
                    shareIt(playstorelink);
                }
            }
        });
        builder.show();
    }

    //function for sharing location on social media
    private void shareIt(String link) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = link;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "HowZat SOS App");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void show(String str) {
        Toast.makeText(getActivity(), str, Toast.LENGTH_LONG).show();
    }

    // this method to call a mobile
    public void calling() {
        try {

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + session.Getmobilno1()));
            startActivity(callIntent);
        } catch (Exception e) {
            String ex = e.toString();
        }

    }
}
