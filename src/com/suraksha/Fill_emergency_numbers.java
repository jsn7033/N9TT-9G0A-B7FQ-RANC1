package com.suraksha;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;

import com.webservice.Service1;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
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

public class Fill_emergency_numbers extends Activity implements OnClickListener {

	Databaseclass dbobj;
	Fill_user_details fud;
	Button btnnext1;
	EditText txtno1;
	EditText txtno2;
	EditText txtno3;
	static String no1;
	static String no2;
	static String no3;
	static String fotp;
	static String fname;
	static String fmobile;
	static String aemail;
	static String agender;
	static String adob;
	static String abloodgroup;
	static String aaddress;
	static String aemployed;
	static String aprofession;
	Connection conn=null;
	Statement stmnt=null;	 
	Button btnsave;
	Button btnaddcontacts;
	int PICK_CONTACT;
	 String n1="mobileno1";
		String n2="mobileno2";
		String n3="mobileno3";
		private ProgressDialog prgDialog;
	     // Progress Dialog type (0 - for Horizontal progress bar)
	     public static final int progress_bar_type = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fill_emergency_numbers);	
		btnaddcontacts=(Button)findViewById(R.id.btnaddcontact);
		btnnext1=(Button)findViewById(R.id.btnupdate);
		btnsave=(Button)findViewById(R.id.btnpersonal);
		txtno1=(EditText)findViewById(R.id.txtemailid);
		txtno2=(EditText)findViewById(R.id.txtage);
		txtno3=(EditText)findViewById(R.id.txtbloodgp);		
		fotp=fud.iotp;
		fname=fud.iname;
		fmobile=fud.imobile;
		aemail=fud.iiemail;
		agender=fud.igender;
		adob=fud.iidob;
		abloodgroup=fud.iibloodgroup;
		aaddress=fud.iiaddress;
		aemployed=fud.iemployed;
		aprofession=fud.iiprofession;
		try {
		  dbobj = new Databaseclass(Fill_emergency_numbers.this);
		  dbobj.getReadableDatabase();
			int status=dbobj.checkforfirsttime();
			if(status!=0)
			{
				
				btnnext1.setVisibility(View.GONE);
				btnsave.setVisibility(View.VISIBLE);
				ArrayList<String> l1=new ArrayList<String>();				
				dbobj.getReadableDatabase();
				l1=dbobj.getmobileno();
				
				boolean a=(l1.get(0)==null);
				boolean b=(l1.get(1)==null);
				boolean c=(l1.get(2)==null);
				txtno1.setText(l1.get(0).toString());
				
				if(a)
				{
					txtno2.setText("");
				}
				if(b)
				{
					txtno3.setText("");
				}
				
			}
		}
		catch(Exception e)
		{
			String ex=e.toString();
		}
		
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
	public void onClick(View v) {
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
				no1="0"+no1;
				no2="0"+no2;
				no3="0"+no3;
				if(connectedwifi || connectedmobile)
		    	{
					
					if(TextUtils.isEmpty(no2))
					{
						no2=null;
					}
					if(TextUtils.isEmpty(no3))
					{
						no3=null;
					}	
			//insertmobileno();
			
					dbobj.insertnewuser(fname, fmobile, aemail, agender, adob, no1, no2, no3);
					new Inserttask().execute(no1,no2,no3,fmobile);
			
			
			Intent i=new Intent(Fill_emergency_numbers.this,Home.class);
			startActivity(i);
		    	}
				else
				{					
					Toast.makeText(this, "Please check internet connection", Toast.LENGTH_LONG).show();
				}
			} 

		}
		if(v.getId()==btnsave.getId())
		{
			
			no1=txtno1.getText().toString();
			no2=txtno2.getText().toString();
			no3=txtno3.getText().toString();
			if(TextUtils.isEmpty(no1) || no1.length()!=11)
			{
				txtno1.setError("Please enter a valid number");
			}
			else
			{
			if(TextUtils.isEmpty(no2))
			{
				no2=null;
			}
			if(TextUtils.isEmpty(no3))
			{
				no3=null;
			}	
			dbobj.updatecontactnumber(n1, no1);
			dbobj.updatecontactnumber(n2, no2);
			dbobj.updatecontactnumber(n3, no3);
			Toast.makeText(getApplicationContext(), "Updated successfully!",  
		             Toast.LENGTH_LONG).show();
			}
			
			
		}
		
		
	}
	
	//function to insert emergency mobile number
	public void insertmobileno()
	{
		
		try {
			String driver="net.sourceforge.jtds.jdbc.Driver";
			Class.forName(driver).newInstance();
			// String connString="jdbc:jtds:sqlserver://182.50.133.18:1433/rannlab;encrypt=fasle;user=rannlab;password=Letsrock@123;";
			 //String username="rannlab";
			// String password="Letsrock@123";
			 
			 String connString="jdbc:jtds:sqlserver://110.232.255.114:1433/howzat;encrypt=false;user=sa;password=pass@123;";
			 String username="sa";
			 String password="pass@123";

            conn=DriverManager.getConnection(connString,username,password);
            stmnt = conn.createStatement();
            
            String abc=conn.toString();
   
            String SQL = "UPDATE USER_DETAILS SET mobileno1='"+no1+"', mobileno2='"+no2+"', mobileno3='"+no3+"', defaultnumber='"+no1+"' WHERE mobile='" + fmobile + "'";
          
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
	
	///////////////////////////////////////////
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
				svc.insertemergencycontacts(params[0],params[1],params[2], params[3]);
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
	 	     
	 	     Intent i=new Intent(Fill_emergency_numbers.this,Home.class);
			 startActivity(i);
	 	      dismissDialog(progress_bar_type);
	 	   }
	 	   }

	@Override
	 @Deprecated
	 protected Dialog onCreateDialog(int id) {
		 switch(id)
		 {
	 case progress_bar_type:
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Preparing home screen...");
        prgDialog.setIndeterminate(false);
        prgDialog.setCancelable(true);
        prgDialog.show();
        return prgDialog;

    default:
        return null;
		 }
	
	}
	


}
