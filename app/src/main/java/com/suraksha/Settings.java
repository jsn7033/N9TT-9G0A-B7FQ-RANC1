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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.SessionManager.SessionManager;
import com.webservice.Service1;

public class Settings extends BaseActivity {

	Databaseclass db;
	 Button btncontact;
	ArrayList<String> listsetting;
	ToggleButton btntoggleshake;
	ToggleButton btntoggle;
    SessionManager session;
    Service1 ws;

	@Override
	protected void onResume() {
		super.onResume();

//		String status=session.Getshechfeature();
//
//		if(status.equals("true")){
//			btntoggleshake.setChecked(true);
//			stopService();
//			checkIfServiceRunning();
//		}
//		else if(status.equals("false")){
//			btntoggleshake.setChecked(false);
//		}
	}

	@Override
	public void onBackPressed(){
		finish();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);


		Toolbar mToolbar = loadToolbar("Settings");
		setSupportActionBar(mToolbar);
		mToolbar.setLogo(R.drawable.howzaticon_);
		mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
        session=new SessionManager(Settings.this);
        ws=new Service1();


		db=new Databaseclass(Settings.this);
		listsetting=new ArrayList<String>();
		db.getReadableDatabase();
	 	final TextView reading = (TextView) findViewById(R.id.texttimer);
        SeekBar SenserseekBar = (SeekBar)findViewById(R.id.seekbar);
		SenserseekBar.setMax(30);
		String sencitivity=session.GetsencerSencitityValue();
		if(sencitivity.equals("")){
			sencitivity="0";
		}
		SenserseekBar.setProgress(Integer.parseInt(sencitivity));

        SeekBar seekBartimer = (SeekBar)findViewById(R.id.seekbartimer);        
        seekBartimer.setMax(2);

		String timer=session.GetseekBartimerValue();

		if(timer.equals("")){
			timer="0";
		}
		if(timer.equals("0")){
			int temp=5;
			reading.setText("Timer Duration: "+temp+"sec");
		}
		else if(timer.equals("1")){
			int temp=10;
			reading.setText("Timer Duration: "+temp+"sec");
		}
		else if(timer.equals("2")){
			int temp=15;
			reading.setText("Timer Duration: "+temp+"sec");
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
	   String temp=progress+"";
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
        		   if(progress==0)
        		   {

					   session.Savepreferences("seekBartimerValue", String.valueOf(progress));
        			//   db.updatetimer(temp);
        			   progress+=5;
            	       reading.setText("Timer Duration: "+progress+"sec"); 
            	      
            	      
        		   }
        		   else if(progress==1)
        		   {

					   session.Savepreferences("seekBartimerValue", String.valueOf(progress));
        			//   db.updatetimer(temp);
        			   progress+=9;
            	       reading.setText("Timer Duration: "+progress+"sec"); 
        		   }
        		   else if(progress==2)
        		   {

					   session.Savepreferences("seekBartimerValue", String.valueOf(progress));
        		//	   db.updatetimer(temp);
        			   progress+=13;
            	       reading.setText("Timer Duration: "+progress+"sec"); 
        		   }
        		 
        	   }
        	  });
		
		/////////////
		btncontact=(Button)findViewById(R.id.btnupdate);

		btntoggle=(ToggleButton)findViewById(R.id.togglebtn);

        if(!session.GetisChecked().equals("0")){
            btntoggle.setChecked(true);
        }else{
            btntoggle.setChecked(false);
        }

		btntoggleshake=(ToggleButton)findViewById(R.id.togglebtnsensor);




		btncontact.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i=new Intent(Settings.this,Fill_emergency_numbers.class);
				i.putExtra("setting","setting");
    			startActivity(i);
			}
		});
	}

	public void onCheckboxClicked(View view) {
	    boolean checked = ((ToggleButton) view).isChecked();
	    switch(view.getId()) {
	        case R.id.togglebtn:
	            if (checked){
	            	session.Savepreferences("isChecked","0");
                    onOffNotifcation ds=new onOffNotifcation("1");
                    ds.execute();
	            }
	            else{
                    session.Savepreferences("isChecked","1");
                    onOffNotifcation ds=new onOffNotifcation("0");
                    ds.execute();
	            }
	           
	            break;
	    }
	}

    private class onOffNotifcation extends AsyncTask<Void, Void, Void> {
        String FLAGVALU="";
        onOffNotifcation(String flag){
            this.FLAGVALU=flag;
        }
        @Override
        protected Void doInBackground(Void... params) {
            try{
                 ws.UpdateUserNotificationSetting(session.GetMobileNo(),FLAGVALU);
               }catch(Exception e){
                e.getMessage();
            }
            return null;
        }
    }

    private void setMobileDataEnabled(Context context, boolean enabled) {
		try
		{
	    final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    final Class conmanClass = Class.forName(conman.getClass().getName());
	    final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
	    iConnectivityManagerField.setAccessible(true);
	    final Object iConnectivityManager = iConnectivityManagerField.get(conman);
	    final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
	    final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
	    setMobileDataEnabledMethod.setAccessible(true);
	    setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
		}
		catch (Exception e) {
			String x=e.toString();
		}
	}

	public void onToggleClicked(View view) {

	    boolean on = ((ToggleButton) view).isChecked();
	    
	    if (on) {
			startActivity(new Intent(this, ShakeSetting.class));
			session.Savepreferences("shakefeature","true");
//	    	startService(new Intent(Settings.this, Shaker_Service_updated.class));
	    } else {
			session.Savepreferences("shakefeature", "false");
	        stopService(new Intent(Settings.this, Shaker_Service_updated.class));
//			stopService(new Intent(Settings.this, ShakeDetector.class));
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
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

	public void stopService(){
		if (ShakeDetector.isRunning) {
			Intent intent = new Intent(this, ShakeDetector.class);
			stopService(intent);
		}
	}

	public void checkIfServiceRunning() {
		if (!ShakeDetector.isRunning) {
			Intent intent = new Intent(this, ShakeDetector.class);
			startService(intent);
		}
	}
}
