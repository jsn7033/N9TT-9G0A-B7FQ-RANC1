package com.suraksha;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

import com.SessionManager.SessionManager;

// this is the first activity
public class Preloader extends Activity {


	private CountDownTimer lTimer;

	SessionManager sessionManager;
	@Override
	public void onBackPressed(){

		finish();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preloader);
		sessionManager=new SessionManager(Preloader.this);

		 lTimer = new CountDownTimer(3000, 1000) {
	            public void onFinish() {
	                startScreen();    //this is method for go to next activity     after fineshing timer
	            }
	            @Override
	            public void onTick(long millisUntilFinished) {
	                // TODO Auto-generated method stub
	            }
	        }.start();
	}
//
	private void startScreen() {

		// if user all ready login call this method
		if(sessionManager.GetLogin().equals("Yes")){
			//checking the service satatus
			if (sessionManager.Getshechfeature().equals("true") && !sessionManager.isMyServiceRunning(Shaker_Service_updated.class)) {
				 getApplicationContext().startService(new Intent(Preloader.this.getApplicationContext(), Shaker_Service_updated.class));
			}
			// go to home page
			Intent i = new Intent(Preloader.this, HomeActivity.class);
				startActivity(i);
				finish();

		}else {

			Intent lIntent = new Intent();
			lIntent.setClass(Preloader.this, Landingpage.class);
			startActivity(lIntent);
			finish();
		}
    }	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.preloader, menu);
		return true;
	}

}
