package com.suraksha;

import java.sql.Connection;
import java.sql.Statement;

import com.SessionManager.SessionManager;
import com.webservice.Service1;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// in this activity call from two class one is Fill_user_detail and anathor is setting class
public class Fill_emergency_numbers extends Activity implements OnClickListener {

	Button btnnext1;
	EditText txtno1;
	EditText txtno2;
	EditText txtno3;
	static String no1;
	static String no2;
	static String no3;

	static String fmobile;

	Connection conn=null;
	Statement stmnt=null;	 
	Button btnsave;
	Button btnaddcontacts;
	SessionManager sessionManager;
	String setting="";
	ProgressDialog progress;
    @Override
    public void onBackPressed(){
		finish();
    }
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fill_emergency_numbers);
        sessionManager=new SessionManager(getApplicationContext());
		btnaddcontacts=(Button)findViewById(R.id.btnaddcontact);
		btnnext1=(Button)findViewById(R.id.btnupdate);
		btnsave=(Button)findViewById(R.id.btnSave);
		txtno1=(EditText)findViewById(R.id.txtemailid);
		txtno2=(EditText)findViewById(R.id.txtage);
		txtno3=(EditText)findViewById(R.id.txtbloodgp);
		try{
			Intent iss=getIntent();
		 	setting=iss.getStringExtra("setting"); // recieving data from Fill_user_detail class
		}catch (Exception e){

		}
		//checking
		if (setting.equals("setting")) {
			txtno1.setText(sessionManager.Getmobilno1());
			txtno2.setText(sessionManager.Getmobilno2());
			txtno3.setText(sessionManager.Getmobilno3());
			btnnext1.setText("Save Change");
			btnsave.setVisibility(View.GONE);
		}
		fmobile=sessionManager.GetMobileNo();

		btnnext1.setOnClickListener(this);
		btnsave.setOnClickListener(this);
		btnaddcontacts.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fill_emergency_numbers, menu);
		return true;
	}

	@Override
	public void onClick(View v) {/// method for saving details
		// TODO Auto-generated method stub
		if(v.getId()==btnnext1.getId())
		{
			ConnectivityManager mgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    	NetworkInfo networkInfo = mgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	    	boolean connectedmobile = networkInfo != null && networkInfo.isConnected();
	    	NetworkInfo networkInfo2=mgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	    	boolean connectedwifi=networkInfo2 != null && networkInfo2.isConnected();  	
			
			no1=txtno1.getText().toString();
			no2=txtno2.getText().toString();
			no3=txtno3.getText().toString();
			
			
		if(TextUtils.isEmpty(no1) || no1.length()!=10)
			{
				txtno1.setError("Please enter a valid number");
			}
		
		    
			else
			{

				if(connectedwifi || connectedmobile) {
                	if (TextUtils.isEmpty(no2)) {
						no2 = "";
					}
					if (TextUtils.isEmpty(no3)) {
						no3 = "";
					}

					new Inserttask().execute(fmobile, no1, no2, no3);  // this method is save or update emergancy number
 		    	}
				else
				{					
					Toast.makeText(this, "Please check internet connection", Toast.LENGTH_LONG).show();
				}
			} 

		}
		if(v.getId()==btnsave.getId()) {
			sessionManager.Savepreferences("login","yes");
			Intent i = new Intent(Fill_emergency_numbers.this, HomeActivity.class);
			startActivity(i);
			finish();
		}
		
		
	}

 	private class Inserttask extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = new ProgressDialog(Fill_emergency_numbers.this);
			progress.setMessage("Loading...");
			progress.setCancelable(false);
			progress.show();
		}
	 	 
	 	   @Override
	 	   protected String doInBackground(String... params) {
	 	     Service1 svc=new Service1();
	 		   try {
				svc.InsertUpdateUserEmergencyContacts(params[0],params[1],params[2], params[3]);//calling web service
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				return null;
	 	      
	 	   }
	 	 

	 	   @Override
	 	   protected void onPostExecute(String result) {
			   progress.dismiss();

			   sessionManager.Savepreferences("mobilno1",no1);
			   sessionManager.Savepreferences("mobilno2",no2);
			   sessionManager.Savepreferences("mobilno3", no3);
			   if (setting.equals("setting")) {

				   finish();
			   } else {
				   sessionManager.Savepreferences("login","yes");

				   Intent i = new Intent(Fill_emergency_numbers.this, HomeActivity.class);
				   startActivity(i);
				   finish();
			   }
	 	      super.onPostExecute(result);



	 	   }
	 	   }



}
