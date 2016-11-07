package com.suraksha;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Calendar;

import com.webservice.Service1;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class Fill_user_details extends Activity implements OnClickListener {

	Registration reg;
	Button btnnext;
	EditText txtname;
	TextView txtmobile;
	EditText txtemail;
	RadioButton rbgenderm;
	RadioButton rbgenderf;
	EditText txtdob;
	EditText txtbloodgroup;
	EditText txtaddress;
	RadioButton rbemployedyes;
	RadioButton rbemployedno;
	EditText txtprofession;
	TextView tmp;
	static String iotp;
	static String iname;
	static String imobile;
	static String iemail;
	static String igender="Male";
	static String idob;
	static String ibloodgroup;
	static String iaddress;
	static String iemployed;
	static String iprofession;	
	static String iiname;
	static String iiemail;
	static String iidob;
	static String iibloodgroup;
	static String iiaddress;
	static String iiprofession;	
	static String servernum;
	Databaseclass dbobj;
	Connection conn=null;
	Statement stmnt=null;
	
	 private Calendar cal;
	 private int day;
	 private int month;
	 private int year;
	 private ProgressDialog prgDialog;
     // Progress Dialog type (0 - for Horizontal progress bar)
     public static final int progress_bar_type = 0;
     public static final int progress_bar_type2 = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fill_user_details);
         cal = Calendar.getInstance();
         day = cal.get(Calendar.DAY_OF_MONTH);
         month = cal.get(Calendar.MONTH);
         year = cal.get(Calendar.YEAR);
   
 		dbobj = new Databaseclass(Fill_user_details.this);
 	
 		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();		
 		StrictMode.setThreadPolicy(policy); 
 		
 		txtname=(EditText)findViewById(R.id.txtuname);
 		txtmobile=(TextView)findViewById(R.id.txtmobile);
 		btnnext=(Button)findViewById(R.id.btnupdate);
 		btnnext.setOnClickListener(this);
 		
 		servernum=reg.servernum;
 		iotp=reg.uotp;
 		//iname=reg.uname;
 		imobile=reg.umobile;
 		
 		//txtname.setText(iname);
 		txtmobile.setText(imobile);
 		txtemail=(EditText)findViewById(R.id.txtemailid);
 		rbgenderm=(RadioButton)findViewById(R.id.radio_male);
 		rbgenderf=(RadioButton)findViewById(R.id.radio_female);
 		txtdob=(EditText)findViewById(R.id.txtdob);
 		txtdob.setText(day+"/"+month+"/"+year);
 		txtdob.setOnTouchListener(new OnTouchListener() {
			
 			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
 				showDialog(progress_bar_type2);
				return false;
			}

			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
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
            		iname=txtname.getText().toString()+imobile.substring(7,11);
                    iemail=txtemail.getText().toString();
                    idob=txtdob.getText().toString(); 
                    new Inserttask().execute(imobile,iname,iemail,igender,idob);
            		
           // insertuserdetails();
         
//			Intent i=new Intent(Fill_user_details.this,Fill_emergency_numbers.class);
//			startActivity(i);
    	    	}
            	else
    			{
    				
    				Toast.makeText(this, "Please check internet connection", Toast.LENGTH_LONG).show();
    				
    			}
            }
		}
		
		if(v.getId()==txtdob.getId())
		{
			showDialog(progress_bar_type2);
		}
	}
	
	 @Override
	 @Deprecated
	 protected Dialog onCreateDialog(int id) {
		 switch(id)
		 {
	 case progress_bar_type:
         prgDialog = new ProgressDialog(this);
         prgDialog.setMessage("Please wait...");
         prgDialog.setIndeterminate(false);
         prgDialog.setCancelable(true);
         prgDialog.show();
         return prgDialog;
	 case progress_bar_type2:
		 return new DatePickerDialog(this, datePickerListener, year, month, day);
     default:
         return null;
		 }
	  
	 }

	 private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
	  public void onDateSet(DatePicker view, int selectedYear,
	    int selectedMonth, int selectedDay) {
		  String day=""+selectedDay;
		  String month=""+(selectedMonth+1);
		
		  if(day.length()==1)
		  {
			  day="0"+day;
		  }
		  if(month.length()==1)
		  {
			  month="0"+month;
		  }
	   txtdob.setText(day + "/" + month+ "/"
	     + selectedYear);
	   
	  }
	 };

	
	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    
	    // Check which radio button was clicked
	    switch(view.getId()) 
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
	
	//function to insert user details 
	void insertuserdetails()
	{
		try {
			String driver="net.sourceforge.jtds.jdbc.Driver";
			Class.forName(driver).newInstance();
			 String connString="jdbc:jtds:sqlserver://110.232.255.114:1433/howzat;encrypt=false;user=sa;password=pass@123;";
			 String username="sa";
			 String password="pass@123";

            conn=DriverManager.getConnection(connString,username,password);
            stmnt = conn.createStatement();
            
            String abc=conn.toString();
            iname=txtname.getText().toString()+imobile.substring(7,11);
            iemail=txtemail.getText().toString();
            idob=txtdob.getText().toString();     
            
          //String SQL = "INSERT INTO TEMP1 VALUES(3,'" + s + "', '" + s1 + "', '" + s2 + "');";
           String SQL = "UPDATE USER_DETAILS set name='"+iname+"',emailid='"+iemail+"',gender='"+igender+"',dob='"+idob+"' WHERE mobile='"+imobile+"'";
           // stmnt = conn.createStatement();
            stmnt.execute(SQL);
		
	}
		 catch (Exception e) {
             e.printStackTrace();
          }
          finally {
             if (stmnt != null) try { stmnt.close(); } catch(Exception e) {}
             if (conn != null) try { conn.close(); } catch(Exception e) {}
          }
		
	}
	
	// Show Dialog Box with Progress bar
    /*@Override
    protected Dialog onCreateDialog(int id) {
        switch (id) 
        {
        case progress_bar_type:
            prgDialog = new ProgressDialog(this);
            prgDialog.setMessage("Please wait...");
            prgDialog.setIndeterminate(false);
            prgDialog.setCancelable(true);
            prgDialog.show();
            return prgDialog;
        default:
            return null;
        }
    }*/
	private class Inserttask extends AsyncTask<String, Integer, String> {
	 	   @Override
	 	   protected void onPreExecute() 
	 	   {  	
	 	      super.onPreExecute();
	 	      showDialog(progress_bar_type);
	 	      
	 	      
	 	   }
	 	 
	 	   @Override
	 	   protected String doInBackground(String... params) {
	 		    //android.os.Debug.waitForDebugger();
	 		  // deletecontact(params[0]);
	 		   Service1 svc=new Service1();
	 		   try {
				svc.insertpersonaldetails(params[0],params[1],params[2], params[3],params[4]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				return null;
	 	      
	 	   }
	 	 
	 	   @Override
	 	   protected void onProgressUpdate(Integer... values) {
	 	      super.onProgressUpdate(values);
	 	   }
	 	 
	 	   @Override
	 	   protected void onPostExecute(String result) {
	 	      super.onPostExecute(result);
	 	     
	 	     Intent i=new Intent(Fill_user_details.this,Fill_emergency_numbers.class);
			 startActivity(i);
	 	      dismissDialog(progress_bar_type);
	 	   }
	 	   }
	
	


	


}
