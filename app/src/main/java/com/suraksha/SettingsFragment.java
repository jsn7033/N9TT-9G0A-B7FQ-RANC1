package com.suraksha;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;


import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.provider.Telephony.Sms.Conversations;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.SessionManager.SessionManager;
import com.core.BaseFragment;
import com.squareup.picasso.Picasso;
import com.webservice.Service1;

public class SettingsFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {

    Databaseclass db;
    Button btncontact;
    ArrayList<String> listsetting;
    Switch btntoggleshake;
    Switch btntoggle;
    SessionManager session;
    Service1 ws;


    AppTitleCallback mAppTitleCallback;

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        switch (compoundButton.getId()) {
            case R.id.notifiationtogglebtn:

                if (b) {
                    session.Savepreferences("isChecked", "0");
                    onOffNotifcation ds = new onOffNotifcation("1");
                    ds.execute();
                } else {
                    session.Savepreferences("isChecked", "1");
                    onOffNotifcation ds = new onOffNotifcation("0");
                    ds.execute();
                }

                break;
            case R.id.togglebtnsensor:
                if (b) {
                    startActivity(new Intent(getActivity(), ShakeSetting.class));
//                    session.Savepreferences("shakefeature", "true");
                } else {
//                    session.Savepreferences("shakefeature", "false");
                    getActivity().stopService(new Intent(getActivity(), Shaker_Service_updated.class));
                }
                break;
        }

    }


    public interface AppTitleCallback {
        void title(String title);
    }


    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);

        if (context instanceof HomeActivity) {
            mAppTitleCallback = (AppTitleCallback) context;
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppTitleCallback.title(getString(R.string.menu_setting_text));
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    //    @Override
//    public void onResume() {
//        super.onResume();
//
//        String status = session.Getshechfeature();
//
//        if (status.equals("true")) {
//            btntoggleshake.setChecked(true);
//        } else if (status.equals("false")) {
//            btntoggleshake.setChecked(false);
//        }
//    }


//    @Override
//    public void onResume() {
//        super.onResume();
//
//        String status = session.Getshechfeature();
//
//        if (status.equals("true")) {
//            btntoggleshake.setChecked(true);
//        } else if (status.equals("false")) {
//            btntoggleshake.setChecked(false);
//        }
//
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_settings,
                container, false);

        session = new SessionManager(getActivity());
        ws = new Service1();


        db = new Databaseclass(getActivity());
        listsetting = new ArrayList<String>();
        db.getReadableDatabase();
        final TextView reading = (TextView) view.findViewById(R.id.texttimer);
        SeekBar SenserseekBar = (SeekBar) view.findViewById(R.id.seekbar);
        SenserseekBar.setMax(30);
        String sencitivity = session.GetsencerSencitityValue();
        if (sencitivity.equals("")) {
            sencitivity = "0";
        }
        SenserseekBar.setProgress(Integer.parseInt(sencitivity));

        SeekBar seekBartimer = (SeekBar) view.findViewById(R.id.seekbartimer);
        seekBartimer.setMax(2);

        String timer = session.GetseekBartimerValue();

        if (timer.equals("")) {
            timer = "0";
        }
        if (timer.equals("0")) {
            int temp = 5;
            reading.setText("Timer Duration: " + temp + "sec");
        } else if (timer.equals("1")) {
            int temp = 10;
            reading.setText("Timer Duration: " + temp + "sec");
        } else if (timer.equals("2")) {
            int temp = 15;
            reading.setText("Timer Duration: " + temp + "sec");
        }

        seekBartimer.setProgress(Integer.parseInt(timer));


        SenserseekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                String temp = progress + "";
                session.Savepreferences("sencerSencitityValue", temp);

            }
        });
        seekBartimer.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if (progress == 0) {

                    session.Savepreferences("seekBartimerValue", String.valueOf(progress));
                    //   db.updatetimer(temp);
                    progress += 5;
                    reading.setText("Timer Duration: " + progress + "sec");


                } else if (progress == 1) {

                    session.Savepreferences("seekBartimerValue", String.valueOf(progress));
                    //   db.updatetimer(temp);
                    progress += 9;
                    reading.setText("Timer Duration: " + progress + "sec");
                } else if (progress == 2) {

                    session.Savepreferences("seekBartimerValue", String.valueOf(progress));
                    //	   db.updatetimer(temp);
                    progress += 13;
                    reading.setText("Timer Duration: " + progress + "sec");
                }

            }
        });

        /////////////
        btncontact = (Button) view.findViewById(R.id.btnupdate);

        btntoggle = (Switch) view.findViewById(R.id.notifiationtogglebtn);

        if (!session.GetisChecked().equals("0")) {
            btntoggle.setChecked(true);
        } else {
            btntoggle.setChecked(false);
        }

        btntoggle.setOnCheckedChangeListener(this);

        btntoggleshake = (Switch) view.findViewById(R.id.togglebtnsensor);

        btntoggleshake.setOnCheckedChangeListener(this);


        btncontact.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Fill_emergency_numbers.class);
                i.putExtra("setting", "setting");
                startActivity(i);
            }
        });
        return view;
    }

    private class onOffNotifcation extends AsyncTask<Void, Void, Void> {
        String FLAGVALU = "";

        onOffNotifcation(String flag) {
            this.FLAGVALU = flag;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                ws.UpdateUserNotificationSetting(session.GetMobileNo(), FLAGVALU);
            } catch (Exception e) {
                e.getMessage();
            }
            return null;
        }
    }

    private void setMobileDataEnabled(Context context, boolean enabled) {
        try {
            final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final Class conmanClass = Class.forName(conman.getClass().getName());
            final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
            iConnectivityManagerField.setAccessible(true);
            final Object iConnectivityManager = iConnectivityManagerField.get(conman);
            final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
            final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);
            setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
        } catch (Exception e) {
            String x = e.toString();
        }
    }


    public void stopService() {
        if (ShakeDetector.isRunning) {
            Intent intent = new Intent(getActivity(), Shaker_Service_updated.class);
            getActivity().stopService(intent);
        }
    }

    public void checkIfServiceRunning() {
        if (!ShakeDetector.isRunning) {
            Intent intent = new Intent(getActivity(), Shaker_Service_updated.class);
            getActivity().startService(intent);
        }
    }
}
