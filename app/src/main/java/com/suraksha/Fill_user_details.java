package com.suraksha;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import com.SessionManager.SessionManager;
import com.webservice.Service1;


import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
// in this class user fill his detail
public class Fill_user_details extends Activity implements OnClickListener {

	Registration reg;
	Button btnnext;
	EditText txtname;
	TextView txtmobile;
	EditText txtemail;
	RadioButton rbgenderm;
	RadioButton rbgenderf;
	EditText txtdob;


	static String iname;
	static String imobile;
	static String iemail;
	static String igender="Male";
	static String idob;


	static String iiname;
	static String iiemail;
	static String iidob;

	SessionManager session;
	Service1 ws;
	ProgressDialog progress;

	@Override
	public void onBackPressed(){

		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fill_user_details);
		session=new SessionManager(getApplicationContext());


		ws=new Service1();
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		txtname=(EditText)findViewById(R.id.txtuname);
		txtmobile=(TextView)findViewById(R.id.txtmobile);
		btnnext=(Button)findViewById(R.id.btnupdate);
		btnnext.setOnClickListener(this);

		SessionManager session=new SessionManager(Fill_user_details.this);
		imobile=session.GetMobileNo();

		txtmobile.setText(imobile);
		txtemail=(EditText)findViewById(R.id.txtemailid);
		rbgenderm=(RadioButton)findViewById(R.id.radio_male);
		rbgenderf=(RadioButton)findViewById(R.id.radio_female);
		txtdob=(EditText)findViewById(R.id.txtdob);

		// this method is set to date
		SetCalenderToTextbox(txtdob);


	}
	public void SetCalenderToTextbox(EditText Txt) {
		final Calendar myCalendar = Calendar.getInstance();
		final EditText Txtdate  = Txt;
		final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
								  int dayOfMonth) {
				// TODO Auto-generated method stub
				myCalendar.set(Calendar.YEAR, year);
				myCalendar.set(Calendar.MONTH, monthOfYear);
				myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				UpdateDateText(Txtdate , myCalendar);
			}

		};

		Txt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new DatePickerDialog(Fill_user_details.this, date, myCalendar
						.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
						myCalendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});

	}



	void UpdateDateText(EditText Txt , Calendar myCalendar){
		String myFormat = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
		Txt.setText(sdf.format(myCalendar.getTime()));
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.fill_user_details, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==btnnext.getId())
		{
			ConnectivityManager mgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = mgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			boolean connectedmobile = networkInfo != null && networkInfo.isConnected();
			NetworkInfo networkInfo2=mgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			boolean connectedwifi=networkInfo2 != null && networkInfo2.isConnected();


			iiname=txtname.getText().toString();
			iiemail=txtemail.getText().toString();
			iidob=txtdob.getText().toString();


			if(TextUtils.isEmpty(iiname))
			{
				txtname.setError("Please enter a valid name");
			}
			else if(iiname.contains(" "))
			{
				txtname.setError("username doesn't contain blank spaces");
			}
			else if(TextUtils.isEmpty(iiemail))
			{
				txtemail.setError("Please enter a valid EmailID");
			}
			else if(TextUtils.isEmpty(iidob))
			{
				txtdob.setError("Please enter a valid DOB");
			}

			else
			{
				if(connectedwifi || connectedmobile)
				{
					iname=txtname.getText().toString()+imobile.substring(6,10);
					iemail=txtemail.getText().toString();
					idob=txtdob.getText().toString();
					new Inserttask().execute( );  // method is use to save user detail

				}
				else
				{

					Toast.makeText(this, "Please check internet connection", Toast.LENGTH_LONG).show();

				}
			}
		}


	}






	public void onRadioButtonClicked(View view) {
		boolean checked = ((RadioButton) view).isChecked();   switch(view.getId())
		{
			case R.id.radio_male:
				if (checked)
					igender=rbgenderm.getText().toString();
				break;
			case R.id.radio_female:
				if (checked)
					igender=rbgenderf.getText().toString();
				break;

		}
	}
	private class   Inserttask extends AsyncTask<Void, Void, Void> {
		JSONArray ds=null;

		JSONObject RegList=null ;
		String MOBILE="";
		String value="" ;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = new ProgressDialog(Fill_user_details.this);
			progress.setMessage("Loading...");
			progress.setCancelable(false);
			progress.show();
		}
		@Override
		protected Void doInBackground(Void... params) {
			try{
				RegList=ws.InsertUpdateUserDetails(imobile, iname, iemail, igender, idob); //calling web service to save detail
				Iterator iter = RegList.keys();
				while(iter.hasNext()){
					String key = (String)iter.next();
					if(key.equals("Value")) {
						value = RegList.getString(key);

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
			if(value.equals("true")) {  // after successfull go to next class to save emergency number
				session.Savepreferences("login","userd");

				Intent i = new Intent(Fill_user_details.this, Fill_emergency_numbers.class);
				i.putExtra("setting", "setthghing");//sendind data to  Fill_emergency_numbers class
				startActivity(i);
				finish();
			}
			super.onPostExecute(result);
		}

	}


}
