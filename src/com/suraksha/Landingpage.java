package com.suraksha;

import java.util.ArrayList;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Landingpage extends Activity {

	 public static final String DB_NAME = "suraksha_db";
	 private static final String TABLE_NAME = "user_details";
	 private static final String name1 = "name";
	 private static final String mobile1 = "mobile";
	 private static final String email="emailid";
	 private static final String gender="gender";
	 private static final String age="age";
	 private static final String bloodgroup="bloodgroup";
	 private static final String address="address";
	 private static final String employed="employed";
	 private static final String profession="profession";
	 private static final String mobileno1="mobileno1";
	 private static final String mobileno2="mobileno2";
	 private static final String mobileno3="mobileno3";
	 private static final String message1="message1";
	 private static final String message2="message2";
	 private static final String defaultno="defaultnumber";
	 private static final String defaultmsg="defaultmessage";
	 private static final String serverno="contactno";
	 private static final String fname="fname";
	 private static final String lname="lname";
	 private static final String dob="dob";
	 private static final String maritalstatus="maritalstatus";
	 private static final String fathername="fathername";
	 private static final String fathermobile="fathermobile";
	 private static final String spousename="spousename";
	 private static final String spousemobile="spousemobile";
	 private static final String company="company";
	 private static final String insurance="insurance";
	 private static final String policyno="policyno";
	 private static final String healthremark="health_remark";
	 private static final String rcity="rcity";
	 private static final String rstate="rstate";
	 private static final String rpin="rpin";
	 private static final String oaddress="oaddress";
	 private static final String ocity="ocity";
	 private static final String ostate="ostate";
	 private static final String opin="opin";	
	 private static final String mobiledata="mobile_data_status";
	 private static final String shakestatus="shake_sensor_status";
	 private static final String sensitivity="sensor_sensitivity";
	 private static final String timer="timer_duration";
	 private static final String photo="photo";
	 private static final String vehicle="vehicle";
	 ArrayList<String> namelistArrayList;
	 ArrayList<String> numberlistArrayList;
	 TextView txtterms;
	
	Databaseclass dbobj;
	Button btncontinue;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_landingpage);
		
		SQLiteDatabase db = openOrCreateDatabase("db1",MODE_PRIVATE,null);
		db.execSQL("create table IF NOT EXISTS " + TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + name1+ " TEXT," + mobile1 + " TEXT,"+ email+ " TEXT,"+ gender+ " TEXT,"+ age+ " TEXT,"+ bloodgroup+ " TEXT,"+ address+ " TEXT,"+ employed+ " TEXT,"+ profession+ " TEXT,"+ mobileno1+ " TEXT,"+ mobileno2+ " TEXT,"+ mobileno3+ " TEXT,"+ message1+ " TEXT,"+ message2+" TEXT,"+defaultno+" TEXT,"+defaultmsg+" TEXT,"+serverno+" TEXT,"+fname+" TEXT,"+lname+" TEXT,"+dob+" TEXT,"+maritalstatus+" TEXT,"+fathername+" TEXT,"+fathermobile+" TEXT,"+spousename+" TEXT,"+spousemobile+" TEXT,"+company+" TEXT,"+insurance+" TEXT,"+policyno+" TEXT,"+healthremark+" TEXT,"+rcity+" TEXT,"+rstate+" TEXT,"+rpin+" TEXT,"+oaddress+" TEXT,"+ocity+" TEXT,"+ostate+" TEXT,"+opin+" TEXT,"+mobiledata+" TEXT,"+shakestatus+" TEXT,"+sensitivity+" TEXT,"+timer+" TEXT,"+photo+" TEXT,"+vehicle+" TEXT)");
		db.execSQL("create table IF NOT EXISTS Chat_details(message TEXT,filename TEXT,path TEXT,tag TEXT,caption TEXT,currentdate TEXT,sender TEXT)");
		
		dbobj = new Databaseclass(Landingpage.this);
		dbobj.getReadableDatabase();
		int status=dbobj.status();
		if(status!=0)
		{
			Intent i=new Intent(Landingpage.this,Home.class);
			startActivity(i);			
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
				
			}
		});
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.landingpage, menu);
		return true;
	}
	
	//function to fetch contacts from phone book
	public void fetchcontact()
	{
		Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null,ContactsContract.Contacts.DISPLAY_NAME + " ASC");
		int i,j;
		while (phones.moveToNext())
		{
		  i=phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
		  j=phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
		  String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
		  String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
		  namelistArrayList.add(phones.getString(i));
		  numberlistArrayList.add(phones.getString(j));					
		}
		 // Async2 task2=new Async2();
	     // task2.execute();
		//servercontact();
		
		phones.close();
	}

}
