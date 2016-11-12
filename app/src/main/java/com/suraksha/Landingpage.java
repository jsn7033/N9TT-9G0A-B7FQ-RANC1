package com.suraksha;


import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.SessionManager.SessionManager;
public class Landingpage extends Activity {



	 TextView txtterms;
	

	Button btncontinue;
    ProgressDialog progress;
    SessionManager sessionManager;
	@Override
	public void onBackPressed(){

		finish();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_landingpage);
        sessionManager=new SessionManager(Landingpage.this);
        Databaseclass	dbobj = new Databaseclass(Landingpage.this);
		dbobj.getReadableDatabase();

		// checking user detail fill or not
        if(sessionManager.GetLogin().equals("reg")){
			Intent i=new Intent(Landingpage.this,Fill_user_details.class);
			startActivity(i);
			finish();
			//checking emergency number fill or not
		}else if(sessionManager.GetLogin().equals("userd")){
			Intent i=new Intent(Landingpage.this,Fill_emergency_numbers.class);
			i.putExtra("setting","kuchb");
			startActivity(i);
			finish();
			// checking user is login or not
		}else if(sessionManager.GetLogin().equals("yes")){
			Intent i=new Intent(Landingpage.this,HomeActivity.class);
			startActivity(i);
			finish();
		}
		txtterms=(TextView)findViewById(R.id.txtterms);
		txtterms.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
		        intent.setAction(Intent.ACTION_VIEW);
		        intent.addCategory(Intent.CATEGORY_BROWSABLE);
		        intent.setData(Uri.parse("http://www.howzatsos.com/terms"));
		        startActivity(intent);
				
			}
		});
		btncontinue=(Button)findViewById(R.id.button1);
		btncontinue.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i=new Intent(Landingpage.this,Registration.class);
				startActivity(i);
				finish();
				
			}
		});
	}





    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.landingpage, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			// Respond to the action bar's Up/HomeActivity button
			case android.R.id.home:
				finish();


				return true;
		}
		return super.onOptionsItemSelected(item);
	}


}
