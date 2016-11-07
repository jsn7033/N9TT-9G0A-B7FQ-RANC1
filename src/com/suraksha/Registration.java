package com.suraksha;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.webservice.Service1;

public class Registration extends Activity implements OnClickListener {

	public static final String DB_NAME = "suraksha_db";
	 	
	Databaseclass dbobj;
	Button register;
	EditText name;
	EditText mobile;
	EditText otp;
	static String uname;
	static String umobile; 
	public static String dotp;
	static String uotp;
	Button verify;
	static String servernum;
	public static String servernum1;
	Button btnnewuser;
	static String macaddress;	
	private final String url = "jdbc:sqlserver://";
	 Connection conn=null;
	 Statement stmnt=null;
	 Statement stmnt1=null;	 
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();		
		StrictMode.setThreadPolicy(policy);
	    dbobj = new Databaseclass(Registration.this);
	
		dbobj.getReadableDatabase();
		int status=dbobj.status();
		if(status!=0)
		{
			Intent i=new Intent(Registration.this,Home.class);
			startActivity(i);			
		}
		
		mobile=(EditText)findViewById(R.id.txtage);		
		register = (Button) findViewById(R.id.btnupdate);
			 
		register.setOnClickListener(this);
			
		
	}
	
	
	@Override
	public void onClick(View v) 
	{	
		if(v.getId()==register.getId()){
			ConnectivityManager mgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    	NetworkInfo networkInfo = mgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	    	boolean connectedmobile = networkInfo != null && networkInfo.isConnected();
	    	NetworkInfo networkInfo2=mgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	    	boolean connectedwifi=networkInfo2 != null && networkInfo2.isConnected();
	    	
	    	WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = manager.getConnectionInfo();
			macaddress = info.getMacAddress();
	    	umobile=mobile.getText().toString();
			
			if(TextUtils.isEmpty(umobile) || umobile.length()!=10)
			{
				mobile.setError("Please enter a valid number");
				
			}
			else
			{
				if(connectedwifi || connectedmobile)
		    	{
					umobile="0"+umobile;
			int [] randomnum=generateRandomNumber();
			uotp=""+randomnum[0]+randomnum[1]+randomnum[2]+randomnum[3];
			Service1 sv=new Service1();
			try {
				String result=(sv.insertregdetails(umobile, uotp, macaddress)).toString();
				Log.d("tagd", "result="+result);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	//insertregdetails();		  
			
	    	Intent i=new Intent(Registration.this,OTP_verification.class);
	    	startActivity(i);			
	    	
	    	String msg="Dear%20User,%20"+uotp+"%20is%20your%20one%20time%20password%20for%20HOWZAT%20SOS.";
			if(GlobalConfig.debug_mode){
				Toast.makeText(getApplicationContext(), "OTP:"+uotp, Toast.LENGTH_LONG);
			}else{
				new RequestTask().execute("http://smsbusiness.in/nxm/api/pushsms.php?usr=howzat&pwd=Howzat123&sndr=HOWZAT&mobile="+umobile+"&text="+msg+"");
				
			}
			
		    	}
				else
				{					
					Toast.makeText(this, "Please check internet connection", Toast.LENGTH_LONG).show();					
				}
				
			}
		 
		
	    	}
		
	
	    	
	    	}
	
	
	//function to insert otp details in registration table temporarily
	public void insertregdetails()
	{
		try {
			String driver="net.sourceforge.jtds.jdbc.Driver";
			Class.forName(driver).newInstance();
			 String connString="jdbc:jtds:sqlserver://110.232.255.114:1433/howzat;encrypt=false;user=sa;password=pass@123;";
			 String username="sa";
			 String password="pass@123";
             conn=DriverManager.getConnection(connString,username,password);
             stmnt = conn.createStatement();
             stmnt1 = conn.createStatement();            
            String abc=conn.toString();
            
            
          //  String SQL = "INSERT INTO TEMP1 VALUES(3,'" + s + "', '" + s1 + "', '" + s2 + "');";
            String SQL = "INSERT INTO REGISTRATION(mobile,otp,mac_address) VALUES('" + umobile + "', '" + uotp + "', '"+macaddress+"');";
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
	//function to generate random number
	private int[] generateRandomNumber()  
	 {  
	      int rnd;  
	      Random rand=new Random();  
	      int[] randNo = new int[4];  
	      ArrayList numbers =new ArrayList();  
	      for (int k=0 ; k<4;k++)  
	      {  
	           rnd = rand.nextInt(8) + 1;  
	           if(k==0)  
	           {  
	                randNo[0] = rnd;  
	                numbers.add(randNo[0]);  
	           }  
	           else  
	           {  
	                while(numbers.contains(new Integer(rnd)))  
	                {  
	                     rnd = rand.nextInt(8) + 1;  
	                }  
	                randNo[k] = rnd;  
	                numbers.add(randNo[k]);  
	           }  
	      } 
	      return randNo;
	 }  

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registration, menu);
		return true;
	}
	
	public void sendotp(String... URL)
	{
		try
		{
		HttpClient httpclient = new DefaultHttpClient();
	    HttpResponse response = httpclient.execute(new HttpGet(URL[0]));
	    StatusLine statusLine = response.getStatusLine();
	    if(statusLine.getStatusCode() == HttpStatus.SC_OK){
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        response.getEntity().writeTo(out);
	        out.close();
	        String responseString = out.toString();
	        //..more logic
	    } else{
	        //Closes the connection.
	        response.getEntity().getContent().close();
	        throw new IOException(statusLine.getReasonPhrase());
	    }
		}
		catch (Exception e) {
			// TODO: handle exception
			String x=e.toString();
		}
		
	}
	
	//code to send OTP to mobile phones
	class RequestTask extends AsyncTask<String, String, String>{

		
		protected String doInBackground(String... uri) 
		{
			//android.os.Debug.waitForDebugger();
			
			HttpClient client=new DefaultHttpClient();
			HttpResponse response;
			String responsestring=null;
			try{
				response=client.execute(new HttpGet(uri[0]));
				StatusLine line=response.getStatusLine();
				if(line.getStatusCode()==HttpStatus.SC_OK){
					 ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responsestring = out.toString();
              //  Toast.makeText(getApplicationContext(), "success", 20).show();
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(line.getReasonPhrase());
            }
				
				
				
			}catch(Exception e){
				e.printStackTrace();
			}
			return responsestring;
		}
	
	
	
}
	

	

}
