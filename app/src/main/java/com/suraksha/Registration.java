package com.suraksha;


import java.util.Iterator;


import android.app.Activity;
import android.app.ProgressDialog;
 import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.CheckInternet.CheckInternet;
import com.webservice.Service1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Registration extends Activity implements OnClickListener {


	Button register;
	EditText name;
	EditText mobile;
    ProgressDialog progress;
    CheckInternet checknet;
    Service1 ws;
    @Override
    public void onBackPressed(){

        finish();
    }
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

	    ws=new Service1();
        checknet=new CheckInternet(Registration.this);
		mobile=(EditText)findViewById(R.id.txtage);
		register = (Button) findViewById(R.id.btnupdate);

		register.setOnClickListener(this);


	}

    private class sendOtp extends AsyncTask<Void, Void, Void> {
        JSONArray ds=null;

        JSONObject RegList=null ;
        String MOBILE="";
        String value="",OTP="",MOB="";
        sendOtp(String mob){
            this.MOBILE=mob;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(Registration.this);
            progress.setMessage("Loading...");
            progress.setCancelable(false);
            progress.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            try{//otp:AA1GDH,MobileNo:
              RegList=ws.GetOTP(MOBILE);
                Iterator iter = RegList.keys();
                while(iter.hasNext()){
                    String key = (String)iter.next();
                    if(key.equals("Value")) {
                        value = RegList.getString(key);
                        ds=new JSONArray(value);
                    }
                }

            }catch(Exception e){
                e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
          progress.dismiss();
            if(ds!=null) {
                if (ds.length() > 0) {
                    try {
                        for (int i = 0; i < ds.length(); i++) {
                            JSONObject obj;
                            obj = ds.getJSONObject(i);
                            OTP = obj.getString("otp");
                            MOB = obj.getString("MobileNo");

                        }

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }

                    Intent i = new Intent(Registration.this, OTP_verification.class);
                    i.putExtra("otp", OTP);
                    i.putExtra("mobile", MOBILE);
                    i.putExtra("otpmob", MOB);
                    startActivity(i);
                    finish();

                }

            }
            super.onPostExecute(result);
        }

    }
     @Override
	public void onClick(View v) {
			if(mobile.getText().toString().trim().length()==10) {
            if (checknet.isNetworkAvailable()) {

                    sendOtp ds = new sendOtp(mobile.getText().toString());
                    ds.execute();
                } else {
                    Toast.makeText(this, "Please check internet connection", Toast.LENGTH_LONG).show();
                }
            }else{
                mobile.setError("Please enter a valid number");
            }
	    	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.registration, menu);
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


}
