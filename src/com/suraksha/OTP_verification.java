package com.suraksha;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OTP_verification extends Activity 
{
	//Databaseclass db;
	Databaseclass dbobj;
	Preloader pl;
	Registration reg;
	private static String dbusername;
	public static String dbmobileno;
	Button btnverify;
	EditText txtotp;
	static String mac;
	ArrayList<String> list1;
	private static String servercontact;
	private static String servercontact1;
	private static String pin;
	private static String finalotp; 
	private static String tempotp;
	private static int cid,cid1;
	FTPClient mFtpClient;
	private final String url = "jdbc:sqlserver://";
	   private ProgressDialog prgDialog;
       // Progress Dialog type (0 - for Horizontal progress bar)
       public static final int progress_bar_type = 0;
	 Connection conn=null;
	 Statement stmnt=null;
	 Statement stmnt2=null;
	 Statement stmnt3=null;
	 private ProgressDialog dialog;
	 String connString="jdbc:jtds:sqlserver://110.232.255.114:1433/howzat;encrypt=false;user=sa;password=pass@123;";
	 String username="sa";
	 String password="pass@123";
	/* String connString="jdbc:jtds:sqlserver://AFZAL:1433/howzat;encrypt=false;user=sa;password=afzal;instance=SQLEXPRESS";
	 String username="sa";
	 String password="afzal";*/
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_otp_verification);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();		
 		StrictMode.setThreadPolicy(policy); 
		
		list1=new ArrayList<String>();
		dbobj = new Databaseclass(OTP_verification.this);
		dbobj.getReadableDatabase();
		int status=dbobj.status();
		if(status!=0)
		{
			Intent i=new Intent(OTP_verification.this,Home.class);
			startActivity(i);			
		}
		//db = new Database(OTP_verification.this);
		btnverify=(Button)findViewById(R.id.btnupdate);
		txtotp=(EditText)findViewById(R.id.txtemailid);
		servercontact=pl.servernum;
		dbmobileno=reg.umobile;
		pin=reg.uotp;
		mac=reg.macaddress;
		btnverify.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				
				ConnectivityManager mgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		    	NetworkInfo networkInfo = mgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		    	boolean connectedmobile = networkInfo != null && networkInfo.isConnected();
		    	NetworkInfo networkInfo2=mgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		    	boolean connectedwifi=networkInfo2 != null && networkInfo2.isConnected();
				tempotp=txtotp.getText().toString();
				
				if(TextUtils.isEmpty(tempotp) || tempotp.length()!=4)
				{
					txtotp.setError("Please enter the 4-digit OTP");					
				}
				else if(!connectedwifi && !connectedmobile)
				{
					
					errormsg();
					
				}
				else
				{
					
				long x=Long.parseLong(tempotp);
				long y=Long.parseLong(pin);
						
				if(x==y)
				{
					new PostTask().execute("verify");
						
				}
				else if(x!=y)
				{
					if(GlobalConfig.debug_mode){
						Toast msg = Toast.makeText(getBaseContext(),
							      "Invalid OTP! Try "+y+" instead", Toast.LENGTH_LONG);
						msg.show();
						
					}else{
						Toast msg = Toast.makeText(getBaseContext(),
							      "Invalid OTP!..Please enter a valid OTP ", Toast.LENGTH_LONG);
						msg.show();
						
					}
					
				}
				}
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.otp_verification, menu);
		return true;
	}

	public void errormsg()
	{
		Toast.makeText(this, "Please check you internet connection", Toast.LENGTH_SHORT).show();
	}
	
	//function to read details of existing user
	public void readexistingdetails()
	{
		
		try {
            // Establish the connection.
			String driver="net.sourceforge.jtds.jdbc.Driver";
			Class.forName(driver).newInstance();
						

            conn=DriverManager.getConnection(connString,username,password);
            stmnt = conn.createStatement();
            
            
            
            list1.add(dbmobileno);            
            list1.add("false");
            list1.add("1");
            list1.add("1");                     	
            
            ResultSet rs2=stmnt.executeQuery("Select * from SERVER_CONTACT");
            while(rs2.next())
            {
            	list1.add(rs2.getString(1));
              
            }
           
            
            ResultSet rs3=stmnt.executeQuery("Select mobileno1,mobileno2,mobileno3,name,emailid,dob from USER_DETAILS where mobile='"+dbmobileno+"'");
            while(rs3.next())
            {
            	list1.add(rs3.getString(1));
            	list1.add(rs3.getString(2));
            	list1.add(rs3.getString(3));
            	list1.add(rs3.getString(4));
            	list1.add(rs3.getString(5));
            	list1.add(rs3.getString(6));
            	
              
            }
            dbobj.insertexistinguserdetailssecond(list1);           
           
		conn.close();
	}
		
		 catch (Exception e) {
             e.printStackTrace();
          }
          finally {
             if (stmnt != null) try { stmnt.close(); } catch(Exception e) {}
             if (conn != null) try { conn.close(); } catch(Exception e) {}
          }
		
		
	}
	
	//function to delete temporary details of user from registration table
	public void deleteentry()
	{
		try {
			String driver="net.sourceforge.jtds.jdbc.Driver";
			Class.forName(driver).newInstance();
			

            conn=DriverManager.getConnection(connString,username,password);
            stmnt = conn.createStatement();
            
   
            String SQL = "delete from REGISTRATION WHERE mobile='" + dbmobileno + "' and otp='" + pin + "'";
          
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

	//function to insert user details
	void insertmobileno()
	{
		try {
			String driver="net.sourceforge.jtds.jdbc.Driver";
			Class.forName(driver).newInstance();
			

            conn=DriverManager.getConnection(connString,username,password);
            stmnt = conn.createStatement();
            stmnt2=conn.createStatement();
            stmnt3=conn.createStatement();
           
           // String SQL = "INSERT INTO TEMP1 VALUES(3,'" + s + "', '" + s1 + "', '" + s2 + "');";
           String SQL = "INSERT INTO USER_DETAILS(mobile,mac_address) VALUES('" + dbmobileno + "', '" + mac + "');";
           String SQL2 = "INSERT INTO CIRCLE_MAIN(cname,cadmin) VALUES('Family','"+dbmobileno+"');";
           String SQL3 = "INSERT INTO CIRCLE_MAIN(cname,cadmin) VALUES('Social','"+dbmobileno+"');";
           stmnt.execute(SQL);
           stmnt2.execute(SQL2);
           stmnt2.execute(SQL3);
           ResultSet rs=stmnt.executeQuery("SELECT circleid FROM CIRCLE_MAIN WHERE cname='Family' and cadmin='"+dbmobileno+"'");
           ResultSet rs1=stmnt2.executeQuery("SELECT circleid FROM CIRCLE_MAIN WHERE cname='Social' and cadmin='"+dbmobileno+"'");
           
           while(rs.next())
           {  
        	 cid=rs.getInt(1);           
           }
           while(rs1.next())
           {  
        	 cid1=rs1.getInt(1);           
           }
            String SQL4 = "INSERT INTO CIRCLE_DETAILS(circleid,cname,mobileno,user_type,location_sharing) VALUES("+cid+",'Family','"+dbmobileno+"','admin','0');";
            stmnt3.execute(SQL4);
            String SQL5 = "INSERT INTO CIRCLE_DETAILS(circleid,cname,mobileno,user_type,location_sharing) VALUES("+cid1+",'Social','"+dbmobileno+"','admin','0');";
            stmnt3.execute(SQL5);
            list1.add(dbmobileno);
            list1.add("false");
            list1.add("1");
            list1.add("1");
            
                ResultSet rs2=stmnt.executeQuery("Select * from SERVER_CONTACT");
                while(rs2.next())
                {
                	list1.add(rs2.getString(1));
                    dbobj.insertexistinguserdetails(list1); 
                }
            
				Intent i=new Intent(OTP_verification.this,Fill_user_details.class);
				startActivity(i);
		
	}
		 catch (Exception e) {
			 
			 String m=checkforexisting();
			 boolean b=mac.equalsIgnoreCase(m);
			 if(b)
			 {
				 readexistingdetails();
				 Intent i=new Intent(OTP_verification.this,Home.class);
				 startActivity(i);
			 }
			 else
			 {
				 readexistingdetails();
				 updatemacaddress();
				 Intent i=new Intent(OTP_verification.this,Home.class);
				 startActivity(i);
				 
			 }
			 
             e.printStackTrace();
          }
          finally {
             if (stmnt != null) try { stmnt.close(); } catch(Exception e) {}
             if (conn != null) try { conn.close(); } catch(Exception e) {}
          }
		
	}
	
	//function to check for mac address before registration
	public String checkforexisting()
	{
		String mac=null;
		try {
			
            // Establish the connection.
			String driver="net.sourceforge.jtds.jdbc.Driver";
			Class.forName(driver).newInstance();
			 		

            conn=DriverManager.getConnection(connString,username,password);
            stmnt = conn.createStatement();
            ResultSet rs=stmnt.executeQuery("select mac_address from USER_DETAILS where mobile='"+dbmobileno+"'");
            while(rs.next()){
            
            	mac=rs.getString(1);
            
            }
		conn.close();
	}
		
		 catch (Exception e) {
             e.printStackTrace();
          }
          finally {
             if (stmnt != null) try { stmnt.close(); } catch(Exception e) {}
             if (conn != null) try { conn.close(); } catch(Exception e) {}
          }
		
		return mac;
	}
	
	//function to update mac address
	void updatemacaddress()
	{
		try {
			String driver="net.sourceforge.jtds.jdbc.Driver";
			Class.forName(driver).newInstance();
			

            conn=DriverManager.getConnection(connString,username,password);
            stmnt = conn.createStatement();
            
            // String SQL = "INSERT INTO TEMP1 VALUES(3,'" + s + "', '" + s1 + "', '" + s2 + "');";
           String SQL = "UPDATE USER_DETAILS SET mac_address='"+mac+"' where mobile='"+dbmobileno+"'";
           // stmnt = conn.createStatement();
           
            stmnt.execute(SQL);
           
		
	}
		catch(Exception e)
		{
			String ex=e.toString();
		}
	}
	
	//function to create user folder on ftp server
		public void connnectingwithFTP(String ip, String userName, String pass) {  
	        boolean status = false;  
	        try {  
	             mFtpClient = new FTPClient();  
	             mFtpClient.setConnectTimeout(10 * 1000);  
	             mFtpClient.connect(InetAddress.getByName(ip));  
	             status = mFtpClient.login(userName, pass);  
	             mFtpClient.setFileType(FTP.BINARY_FILE_TYPE);
	                 mFtpClient.enterLocalPassiveMode(); 
	                 mFtpClient.makeDirectory(""+dbmobileno+"/");
	                 mFtpClient.changeWorkingDirectory("/"+dbmobileno+"");
	                 
	                 mFtpClient.makeDirectory("others/");
	                 mFtpClient.disconnect();
	             
	        } catch (SocketException e) {  
	             e.printStackTrace();  
	        } catch (UnknownHostException e) {  
	             e.printStackTrace();  
	        } catch (IOException e) {  
	             e.printStackTrace();  
	        }  
	   }
		
		// Show Dialog Box with Progress bar
	    @Override
	    protected Dialog onCreateDialog(int id) {
	        switch (id) {
	        case progress_bar_type:
	            prgDialog = new ProgressDialog(this);
	            prgDialog.setMessage("Please wait, while we are verifying your mobile number");
	            prgDialog.setIndeterminate(false);
	            prgDialog.setCancelable(false);
	            prgDialog.show();
	            return prgDialog;
	        default:
	            return null;
	        }
	    }
	    
	    //function to perform task asynchronously
	    private class PostTask extends AsyncTask<String, Integer, String> {
	    	   @Override
	    	   protected void onPreExecute() 
	    	   {  	
	    	      super.onPreExecute();
	    	      showDialog(progress_bar_type);
	    	      
	    	   }
	    	 
	    	   @Override
	    	   protected String doInBackground(String... params) {
	    		  // android.os.Debug.waitForDebugger();
					deleteentry();
					insertmobileno();
					connnectingwithFTP("110.232.255.114", "ftpuser", "ftp@1234");
				return null;
	    	      
	    	   }
	    	 
	    	   @Override
	    	   protected void onProgressUpdate(Integer... values) 
	    	   {
	    	      super.onProgressUpdate(values);
	    	   }
	    	 
	    	   @Override
	    	   protected void onPostExecute(String result) {
	    	      super.onPostExecute(result);
	    	      
	    	      dismissDialog(progress_bar_type);
	    	   }
	    	   }
		
		
}

	

