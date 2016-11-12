package com.suraksha;

import java.util.Iterator;

import org.json.JSONObject;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.SessionManager.SessionManager;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.webservice.Service1;

public class OTP_verification extends Activity 
{

	public static String dbmobileno;
	Button btnverify;
	EditText txtotp;
	static String mac;

    Service1 ws;
    ProgressDialog progress;

    String otp="",otpMobile="";
    String GOOGLE_PROJECT_ID ="32390441410";
    SessionManager session;
    GoogleCloudMessaging gcm;
    @Override
    public void onBackPressed(){

        finish();
    }
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_otp_verification);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();		
 		StrictMode.setThreadPolicy(policy); 
		ws=new Service1();

        session =new SessionManager(getApplicationContext());
        Intent in=getIntent();
          otp=in.getStringExtra("otp");
        dbmobileno=in.getStringExtra("mobile");
        otpMobile=in.getStringExtra("otpmob");
        Toast.makeText(OTP_verification.this,otp,Toast.LENGTH_LONG).show();

		btnverify=(Button)findViewById(R.id.btnupdate);
		txtotp=(EditText)findViewById(R.id.txtemailid);


		btnverify.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {

                if(txtotp.getText().toString().equals(otp)){
                    if(otpMobile.equals("")){
                        user_registration ds=new user_registration( );
                        ds.execute();
                    }else{
                      //  user_registrationAllredy df=new user_registrationAllredy();
                        //df.execute();
                        session.Savepreferences("MobileNo",dbmobileno);
                        session.Savepreferences("login","yes");
                        Intent ds=new Intent(getApplicationContext(),HomeActivity.class);
                        startActivity(ds);
                        finish();

                    }

                }else{
                    Toast msg = Toast.makeText(getBaseContext(),
                            "Invalid OTP!..Please enter a valid OTP ", Toast.LENGTH_LONG);
                    msg.show();
                }
            }
		});
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


    private class user_registration extends AsyncTask<Void, Void, Void> {

        JSONObject responce=null ;
        String value="" ,regId1="";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(OTP_verification.this);
            progress.setMessage("Loading...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                gcm = GoogleCloudMessaging.getInstance(OTP_verification.this);
                regId1 = gcm.register(GOOGLE_PROJECT_ID);

            } catch (Exception e) {
                e.getMessage();
            }


            try{
                WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = manager.getConnectionInfo();
                mac  = info.getMacAddress();
                responce=ws.InsertUser(dbmobileno,mac ,regId1);
                Iterator iter = responce.keys();
                    String key = (String)iter.next();
                    value = responce.getString(key);


            }catch(Exception e){
                e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progress.dismiss();
            SessionManager session=new SessionManager(OTP_verification.this);
            session.Savepreferences("MobileNo",dbmobileno);
            session.Savepreferences("login","reg");
            Intent i=new Intent(OTP_verification.this,Fill_user_details.class);
            startActivity(i);
            finish();

            super.onPostExecute(result);
        }

    }


    private class user_registrationAllredy extends AsyncTask<Void, Void, Void> {

        JSONObject responce=null ;
        String value="" ,regId1="";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(OTP_verification.this);
            progress.setMessage("Loading...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {



            try{

                responce=ws.InsertUser(dbmobileno,mac ,regId1);
                Iterator iter = responce.keys();
                String key = (String)iter.next();
                value = responce.getString(key);


            }catch(Exception e){
                e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progress.dismiss();

            session.Savepreferences("MobileNo",dbmobileno);
            session.Savepreferences("login","yes");
            Intent ds=new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(ds);
            finish();

            super.onPostExecute(result);
        }

    }



}

	

