package com.suraksha;


import java.sql.Connection;
import java.sql.Statement;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class Preloader extends Activity {

	public static String servernum;
	
	Connection conn=null;
	Statement stmnt=null;	
	//Statement stmnt1=null;
	private CountDownTimer lTimer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preloader);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();		
		StrictMode.setThreadPolicy(policy); 
		
		 lTimer = new CountDownTimer(3000, 1000) {
	            public void onFinish() {
	                closeScreen();
	            }
	            @Override
	            public void onTick(long millisUntilFinished) {
	                // TODO Auto-generated method stub
	            }
	        }.start();	        
	}

	private void closeScreen() {
        Intent lIntent = new Intent();
        lIntent.setClass(this,Landingpage.class);
        startActivity(lIntent);
        finish();
    }	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.preloader, menu);
		return true;
	}

}
