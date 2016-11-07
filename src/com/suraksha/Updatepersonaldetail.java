package com.suraksha;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Updatepersonaldetail extends Activity {

	TextView uname,umobile;
	ImageView imgprofile;
	private EditText fname,lname,emailid,dob,age,bloodgroup,fathername,fathermobile,spousename,spousemobile,profession,company,insurance,policy,raddress,rcity,rstate,rpin,oaddress,ocity,ostate,opin,healthremark,vehicle;
	private String imobile,ifname,ilname,iemailid,idob,iage,ibloodgroup,imarital,ifathername,ifathermobile,ispousename,ispousemobile,iprofession,icompany,iinsurance,ipolicy,ihealthremark,iraddress,ircity,irstate,irpin,ioaddress,iocity,iostate,iopin,ivehicle;
	RadioButton rbmale,rbfemale,radioempyes,radioempno;
	Spinner maritalstatus;
	Databaseclass db;
	ArrayList<String> l1;
	ArrayList<String> l2;
	Button btnupdate;
	private String igender="Male",iemployed="Yes";
	Connection conn=null;
	Statement stmnt=null;
	private Calendar cal;
	 private int day;
	 private int month;
	 private int year;
	 int SELECT_FILE=1;
	 FTPClient mFtpClient;
	 File f;
	 String filename;
	 String driver="net.sourceforge.jtds.jdbc.Driver";
	 String connString="jdbc:jtds:sqlserver://110.232.255.114:1433/howzat;encrypt=false;user=sa;password=pass@123;";
	 String username="sa";
	 String password="pass@123";
	 String profilepicpath;
	 public static String imgpath;
	 public static Uri imguri;
	 public static Bitmap bm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db=new Databaseclass(Updatepersonaldetail.this);
		setContentView(R.layout.activity_updatepersonaldetail);
		
		StrictMode.ThreadPolicy tpolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();		
 		StrictMode.setThreadPolicy(tpolicy); 
 		 cal = Calendar.getInstance();
         day = cal.get(Calendar.DAY_OF_MONTH);
         month = cal.get(Calendar.MONTH);
         year = cal.get(Calendar.YEAR);
		uname=(TextView)findViewById(R.id.txtuname);
		umobile=(TextView)findViewById(R.id.txtmobile);
		fname=(EditText)findViewById(R.id.txtfname);
		lname=(EditText)findViewById(R.id.txtlname);
		emailid=(EditText)findViewById(R.id.txtemailid);
		dob=(EditText)findViewById(R.id.txtdob);
		//age=(EditText)findViewById(R.id.txtage);
		bloodgroup=(EditText)findViewById(R.id.txtbloodgp);
		fathername=(EditText)findViewById(R.id.txtfathername);
		fathermobile=(EditText)findViewById(R.id.txtfmobile);
		spousename=(EditText)findViewById(R.id.txtspousename);
		spousemobile=(EditText)findViewById(R.id.txtsmobile);
		profession=(EditText)findViewById(R.id.txtprofession);
		company=(EditText)findViewById(R.id.txtcompany);
		insurance=(EditText)findViewById(R.id.txtinsurance);
		policy=(EditText)findViewById(R.id.txtpolicyno);
		healthremark=(EditText)findViewById(R.id.txthealthremark);
		vehicle=(EditText)findViewById(R.id.txtvehicle);
		raddress=(EditText)findViewById(R.id.txtraddress);
		rcity=(EditText)findViewById(R.id.txtrcity);
		rstate=(EditText)findViewById(R.id.txtrstate);
		rpin=(EditText)findViewById(R.id.txtrpincode);
		oaddress=(EditText)findViewById(R.id.txtoaddress);
		ocity=(EditText)findViewById(R.id.txtocity);
		ostate=(EditText)findViewById(R.id.txtostate);
		opin=(EditText)findViewById(R.id.txtopincode);		
		rbmale=(RadioButton)findViewById(R.id.radio_male);
		rbfemale=(RadioButton)findViewById(R.id.radio_female);
		radioempyes=(RadioButton)findViewById(R.id.radio_employedyes);
		radioempno=(RadioButton)findViewById(R.id.radio_employedno);
		maritalstatus=(Spinner)findViewById(R.id.spinnermarital);
		imgprofile=(ImageView)findViewById(R.id.imgprofilepic);
		///////////////////////////////////////////////////////////
		dob.setText(day+"/"+month+"/"+year);
 		dob.setOnTouchListener(new OnTouchListener() {
			
 			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				showDialog(0);
				return false;
			}

			
		});
 			List<String> list = new ArrayList<String>();
	        list.add("Married");
	        list.add("Unmarried");
	        
	         profilepicpath=db.getprofilephoto();
	         if(profilepicpath!=null)
	         {
	         Bitmap bmimg = BitmapFactory.decodeFile(profilepicpath);
	         imgprofile.setImageBitmap(bmimg);
	         }
	         
	        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
	                     (this, android.R.layout.simple_spinner_item,list);
	                      
	        dataAdapter.setDropDownViewResource
	                     (android.R.layout.simple_spinner_dropdown_item);
	                       
	        maritalstatus.setAdapter(dataAdapter);
		
		imgprofile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				imagepicker();
				
			}
		});
		btnupdate=(Button)findViewById(R.id.btnupdate);
		
		btnupdate.setOnClickListener(new OnClickListener() {
			
		
			@Override
			public void onClick(View arg0) {
				try
				{
					
					l2=new ArrayList<String>();					
					l2.add(fname.getText().toString());
					l2.add(lname.getText().toString());
					l2.add(emailid.getText().toString());
					l2.add(dob.getText().toString());
					l2.add(bloodgroup.getText().toString());
					l2.add(fathername.getText().toString());
					l2.add(fathermobile.getText().toString());
					l2.add(spousename.getText().toString());
					l2.add(spousemobile.getText().toString());
					l2.add(profession.getText().toString());
					l2.add(company.getText().toString());
					l2.add(insurance.getText().toString());
					l2.add(policy.getText().toString());
					l2.add(healthremark.getText().toString());
					l2.add(raddress.getText().toString());
					l2.add(rcity.getText().toString());
					l2.add(rstate.getText().toString());
					l2.add(rpin.getText().toString());
					l2.add(oaddress.getText().toString());
					l2.add(ocity.getText().toString());
					l2.add(ostate.getText().toString());
					l2.add(opin.getText().toString());
					l2.add(vehicle.getText().toString());
					db.updatepersonaldetails(l2);
					updateuserdetails();
					success();
				}
				catch (Exception e) 
				{
					String ex=e.toString();
				}
				
			};
		});
		///////////////////////////////////////////////////////////
		l1=new ArrayList<String>();
		db.getReadableDatabase();
		l1=db.fetchpersonaldetails();
		displaydetails();
		
	}
	
	 @Override
	 @Deprecated
	 protected Dialog onCreateDialog(int id) {
	  return new DatePickerDialog(this, datePickerListener, year, month, day);
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
	   dob.setText(day + "/" + month+ "/"
	     + selectedYear);
	  
	  }
	 };
	
	public void success()
	{
		 Toast.makeText(this, "Update Successfully", Toast.LENGTH_LONG).show();
	}
	
	//function to display user details
	public void displaydetails()
	{
		boolean t1;
		for(int i=0;i<l1.size();i++)
		{
			t1=l1.get(i)==null;
		
			
			if(t1==true)
			{
				l1.set(i, "");
			}
		}
		try
		{
	//	uid.setText(l1.get(0).toString());
		uname.setText(l1.get(1).toString());
		umobile.setText(l1.get(2).toString());
		fname.setText(l1.get(3).toString());
		lname.setText(l1.get(4).toString());
		emailid.setText(l1.get(5).toString());
		///////gender get(6)
		
		dob.setText(l1.get(6).toString());
		//age.setText(l1.get(8).toString());
		bloodgroup.setText(l1.get(7).toString());
		////marital status get(10)
		
		fathername.setText(l1.get(8).toString());
		fathermobile.setText(l1.get(9).toString());
		spousename.setText(l1.get(10).toString());
		spousemobile.setText(l1.get(11).toString());
		//employed get(15)
		
		profession.setText(l1.get(12).toString());
		company.setText(l1.get(13).toString());
		insurance.setText(l1.get(14).toString());
		policy.setText(l1.get(15).toString());
		healthremark.setText(l1.get(16).toString());
		raddress.setText(l1.get(17).toString());
		rcity.setText(l1.get(18).toString());
		rstate.setText(l1.get(19).toString());
		rpin.setText(l1.get(20).toString());
		oaddress.setText(l1.get(21).toString());
		ocity.setText(l1.get(22).toString());
		ostate.setText(l1.get(23).toString());
		opin.setText(l1.get(24).toString());
		vehicle.setText(l1.get(25).toString());
		
	
		}
		catch (Exception e) {
			String ex=e.toString();
		}
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.updatepersonaldetail, menu);
		return true;
	}
	
	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.radio_male:
	            if (checked)
	                igender=rbmale.getText().toString();
	            break;
	        case R.id.radio_female:
	            if (checked)
	            	  igender=rbfemale.getText().toString();
	            break;
	        case R.id.radio_employedyes:
	            if (checked)
	                iemployed=radioempyes.getText().toString();
	            break;
	        case R.id.radio_employedno:
	            if (checked)
	            	  iemployed=radioempno.getText().toString();
	            break;
	       
	    }
	}
	
	//function to update personal details
	
	void updateuserdetails()
	{
		try {			
			Class.forName(driver).newInstance();
            conn=DriverManager.getConnection(connString,username,password);
            stmnt = conn.createStatement();
            imobile=umobile.getText().toString();
            ifname=fname.getText().toString();
            ilname=lname.getText().toString();
            iemailid=emailid.getText().toString();
            idob=dob.getText().toString();
            //iage=age.getText().toString();
            ibloodgroup=bloodgroup.getText().toString();
            imarital=maritalstatus.getSelectedItem().toString();
            ifathername=fathername.getText().toString();
            ifathermobile=fathermobile.getText().toString();
            ispousename=spousename.getText().toString();
            ispousemobile=spousemobile.getText().toString();
            iprofession=profession.getText().toString();
            icompany=company.getText().toString();
            iinsurance=insurance.getText().toString();
            ipolicy=policy.getText().toString();
            ihealthremark=healthremark.getText().toString();
            ivehicle=vehicle.getText().toString();
            iraddress=raddress.getText().toString();
            ircity=rcity.getText().toString();
            irstate=rstate.getText().toString();
            irpin=rpin.getText().toString();
            ioaddress=oaddress.getText().toString();
            iocity=ocity.getText().toString();
            iostate=ostate.getText().toString();
            iopin=opin.getText().toString();
            // String SQL = "INSERT INTO TEMP1 VALUES(3,'" + s + "', '" + s1 + "', '" + s2 + "');";
           String SQL = "UPDATE USER_DETAILS set fname='"+ifname+"',lname='"+ilname+"',emailid='"+iemailid+"',gender='"+igender+"',dob='"+idob+"',bloodgroup='"+ibloodgroup+"',maritalstatus='"+imarital+"',fathername='"+ifathername+"',fathermobile='"+ifathermobile+"',spousename='"+ispousename+"',spousemobile='"+ispousemobile+"',employed='"+iemployed+"',profession='"+iprofession+"',company='"+icompany+"',insurance='"+iinsurance+"',policyno='"+ipolicy+"',health_remark='"+ihealthremark+"',address='"+iraddress+"',residence_city='"+ircity+"',residence_state='"+irstate+"',residence_pin='"+irpin+"',office_address='"+ioaddress+"',office_city='"+iocity+"',office_state='"+iostate+"',office_pin='"+iopin+"',vehicle_no1='"+ivehicle+"' WHERE mobile='"+imobile+"'";
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
	
	//function to pick images for ads
		private void imagepicker()
		{
			final CharSequence[] items = {"Choose from gallery","Cancel" };

			AlertDialog.Builder builder = new AlertDialog.Builder(Updatepersonaldetail.this);
			builder.setTitle("Choose");
			builder.setItems(items, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int item) {
					if (items[item].equals("Choose from gallery")) 
					{
						
						Intent intent = new Intent(
					    Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						//android.provider.MediaStore.Files
						//android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
						intent.setType("image/*");
						startActivityForResult(
						Intent.createChooser(intent, "Select File"),SELECT_FILE);
					} 
					
					else if (items[item].equals("Cancel")) {
						dialog.dismiss();
					}
				}
			});
			builder.show();
		}
		
		//response to image picker
		 @Override
			protected void onActivityResult(int requestCode, int resultCode, Intent data) {
				super.onActivityResult(requestCode, resultCode, data);
				if (resultCode == RESULT_OK) {
					if (requestCode == SELECT_FILE) {
						/*Uri selectedImageUri = data.getData();						
						String tempPath = getPath(selectedImageUri, Updatepersonaldetail.this);
						db.updatephoto(tempPath);
						Bitmap bm;
						BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
						bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
						
						bm = Bitmap.createScaledBitmap(bm, 256, 256, true);
						f=new File(tempPath);
						filename=f.getName();
						imgprofile.setImageBitmap(bm);*/
						
						String path = getRealPathFromURI( data.getData());
						imgpath=path;
						if(path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".png") || path.endsWith(".JPG") || path.endsWith(".JPEG") || path.endsWith(".PNG"))
						{
							imguri = data.getData();
							String tempPath = getPath(imguri, Updatepersonaldetail.this);					
							BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
							bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
							//bitmapcamera=null;
							//camimgpath=null;
							//chatadapter.add(new OneComment(false, bm, 1));
							Intent i=new Intent(Updatepersonaldetail.this,Profilepic_attachment_dialog.class);
							startActivity(i);
									  
						}
						else
						{
							Toast.makeText(this, "Please select a valid file", Toast.LENGTH_LONG).show();
						}
						
						
					}
				}
			}
		 public String getPath(Uri uri, Activity activity) {
				String[] projection = { MediaColumns.DATA };
				Cursor cursor = activity
						.managedQuery(uri, projection, null, null, null);
				int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
				cursor.moveToFirst();
				return cursor.getString(column_index);
			}
		 @Override
			public void onBackPressed() 
			{
				
				Intent i=new Intent(Updatepersonaldetail.this,Settings.class);
				startActivity(i);
			}
		 
		//function to get the actual path of image being uploaded
			public String getRealPathFromURI (Uri contentUri)
			 {
			     String path = null;
			     String[] proj = { MediaStore.MediaColumns.DATA };

			        if("content".equalsIgnoreCase(contentUri.getScheme ()))
			            {
			                Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
			                if (cursor.moveToFirst()) 
			                {
			                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
			                    path = cursor.getString(column_index);
			                }
			                cursor.close();
			                return path;
			            }
			            else if("file".equalsIgnoreCase(contentUri.getScheme()))
			            {
			                return contentUri.getPath();
			            }
			            return null;
			  }
		 
			//class to upload image
			
////////////////////////////////////////////////////
private class Async2 extends AsyncTask<Void, Void,Void>{


@Override
protected Void doInBackground(Void... params) {
//android.os.Debug.waitForDebugger();
try {				
connnectingwithFTP("110.232.255.114", "ftpuser", "ftp@1234");
uploadFile(mFtpClient,f,filename);

}
catch (Exception e) {
e.printStackTrace();
}
finally {
if (stmnt != null) try { stmnt.close(); } catch(Exception e) {}
if (conn != null) try { conn.close(); } catch(Exception e) {}
}
return null;

}


} 

public void connnectingwithFTP(String ip, String userName, String pass) {  
boolean status = false;  
try {  
mFtpClient = new FTPClient();  
mFtpClient.setConnectTimeout(10 * 1000);  
mFtpClient.connect(InetAddress.getByName(ip));  
status = mFtpClient.login(userName, pass);  
mFtpClient.setFileType(FTP.BINARY_FILE_TYPE);
mFtpClient.changeWorkingDirectory(""+imobile+"/others");
mFtpClient.enterLocalPassiveMode(); 

// }  
} catch (SocketException e) {  
e.printStackTrace();  
} catch (UnknownHostException e) {  
e.printStackTrace();  
} catch (IOException e) {  
e.printStackTrace();  
}  
} 
//function to upload file on ftp server
public void uploadFile(FTPClient ftpClient, File downloadFile,String serverfilePath) {  
try {

FileInputStream srcFileStream = new FileInputStream(downloadFile);  
boolean status = ftpClient.storeFile(serverfilePath,  
srcFileStream);  
srcFileStream.close(); 
mFtpClient.disconnect();
} 
catch (Exception e) {  
e.printStackTrace();  
}  
}

}
