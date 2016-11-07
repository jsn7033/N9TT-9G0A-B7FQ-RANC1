package com.suraksha;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.Time;

public class Databaseclass extends SQLiteOpenHelper {

	 private static final int DB_VERSION = 1;
	 public static final String DB_NAME = "db1";
	 private static final String TABLE_NAME = "user_details";
	 private static final String TABLE_NAME2 = "Chat_details";
	 private static final String name = "name";
	 private static final String mobile = "mobile";
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
	 private static final String servercontact="contactno";
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
	 private static final String healthremark="health_remark";
	 private static final String vehicle="vehicle";
	 private static final String createtable3="CREATE TABLE IF NOT EXISTS mark_details(broadcast_id VARCHAR(25));";
	
	 public Databaseclass(Context context) {
		super(context,DB_NAME, null, DB_VERSION);
		
	}
	@Override
	public void onCreate(SQLiteDatabase db) 
	{
	
		String query1= "create table IF NOT EXISTS " + TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + name+ " TEXT," + mobile + " TEXT,"+ email+ " TEXT,"+ gender+ " TEXT,"+ age+ " TEXT,"+ bloodgroup+ " TEXT,"+ address+ " TEXT,"+ employed+ " TEXT,"+ profession+ " TEXT,"+ mobileno1+ " TEXT,"+ mobileno2+ " TEXT,"+ mobileno3+ " TEXT,"+ message1+ " TEXT,"+ message2+ " TEXT)";
		db.execSQL(query1);
		db.execSQL(createtable3);
	}
	
	//function to update other details of a user
	public void insertnewuser(String uname,String umobile,String uemail,String ugender,String udob,String no1,String no2,String no3)
	{
		 SQLiteDatabase db = this.getWritableDatabase();
		  ContentValues values = new ContentValues();
		  values.put(name, uname);
		  values.put(mobile,umobile);
		  values.put(email,uemail);
		  values.put(gender,ugender);
		  values.put(dob,udob);
		 
		  values.put(mobileno1,no1);
		  values.put(mobileno2,no2);
		  values.put(mobileno3,no3);
		  
		  values.put(defaultno,no1);
		 
		//  db.insert(TABLE_NAME, null, values);
		
		  
		  String where = "id=1";
		  try{    
			    db.update(TABLE_NAME, values, where, null);
			    db.close();
			}
			catch (Exception e)
			{
			    String error =  e.getMessage().toString();
			}
		
	}
	
	//function to get the mobile number of user
	public ArrayList<String> getmobileno()
	{
		ArrayList<String> a1=new ArrayList<String>();
		 Set<String> set1 = new HashSet<String>();
		  String selectQuery = "select mobileno1,mobileno2,mobileno3 from " + TABLE_NAME;
		  SQLiteDatabase db = this.getReadableDatabase();
		  Cursor cursor = db.rawQuery(selectQuery, null);
		  if (cursor.moveToFirst()) {
		   do {
		   a1.add(cursor.getString(0));
		   a1.add(cursor.getString(1));
		   a1.add(cursor.getString(2));
		  
		   } while (cursor.moveToNext());
		  }
		  cursor.close();
		  db.close();
		  return a1;
		
	}
	
	public ArrayList<String> getmessage()
	{
		ArrayList<String> a1=new ArrayList<String>();
		 Set<String> set1 = new HashSet<String>();
		  String selectQuery = "select message1,message2 from " + TABLE_NAME;
		  SQLiteDatabase db = this.getReadableDatabase();
		  Cursor cursor = db.rawQuery(selectQuery, null);
		  if (cursor.moveToFirst()) {
		   do {
		   a1.add(cursor.getString(0));
		   a1.add(cursor.getString(1));
	
		   } while (cursor.moveToNext());
		  }
		  cursor.close();
		  db.close();
		  return a1;
		
	}
	
	//function to get name and mobile of a user
	public ArrayList<String> getnamemobile()
	{
		ArrayList<String> a1=new ArrayList<String>();
		 Set<String> set1 = new HashSet<String>();
		  String selectQuery = "select name,mobile from " + TABLE_NAME;
		  SQLiteDatabase db = this.getReadableDatabase();
		  Cursor cursor = db.rawQuery(selectQuery, null);
		  if (cursor.moveToFirst()) {
		   do 
		   {
		   a1.add(cursor.getString(0));
		   a1.add(cursor.getString(1));	
		   } while (cursor.moveToNext());
		  }
		  cursor.close();
		  db.close();
		  return a1;
		
	}
	
	public int status()
	{
		int id=0;
		 String selectQuery = "select id from " + TABLE_NAME;
		  SQLiteDatabase db = this.getReadableDatabase();
		  Cursor cursor = db.rawQuery(selectQuery, null);
		  if (cursor.moveToFirst()) {
			   do {
			  id=cursor.getInt(0);
			   } while (cursor.moveToNext());
			  }
			  cursor.close();
			  db.close();
			  return id;
		
	}
	
	//function to check if user has come for the first time
	public int checkforfirsttime()
	{
		int id=0;
		String temp;
		 String selectQuery = "select mobileno1 from " + TABLE_NAME;
		  SQLiteDatabase db = this.getReadableDatabase();
		  Cursor cursor = db.rawQuery(selectQuery, null);
		  if (cursor.moveToFirst()) {
			   do {
			  temp=cursor.getString(0);
			  if(temp==null)
			  {
				  
			  }
			  else
			  {
				  id++;
			  }
			   } while (cursor.moveToNext());
			  }
			  cursor.close();
			  db.close();
			  return id;
		
	}
	
	//function to update contact numbers
	public void updatecontactnumber(String x,String y)
	{
		  SQLiteDatabase db = this.getWritableDatabase();
		  ContentValues values = new ContentValues();
		  values.put(x,y);
		  
		  String where = "id=1";
		  try{    
			    db.update(TABLE_NAME, values, where, null);
			    db.close();
			}
			catch (Exception e)
			{
			    String error =  e.getMessage().toString();
			}
		
	}
	public void updatemessage(String cname,String cmsg,String dname,String dmsg)
	{
		  SQLiteDatabase db = this.getWritableDatabase();
		  ContentValues values = new ContentValues();
		  values.put(cname,cmsg);
		  values.put(dname,dmsg);
		  
		  String where = "id=1";
		  try{    
			    db.update(TABLE_NAME, values, where, null);
			    db.close();
			}
			catch (Exception e){
			    String error =  e.getMessage().toString();
			}
		
	}
	
	
	//function to insert server contact in device
	public void updateservercontact(String num)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		  ContentValues values = new ContentValues();
		  values.put(servercontact,num);
		  String where = "id=1";
		  try{    
			    db.update(TABLE_NAME, values, where, null);
			    db.close();
			}
			catch (Exception e){
			    String error =  e.getMessage().toString();
			}
	}
	
	//function to get server contact number
	
	public String getservernumber()
	{
		String num = null;
		 String selectQuery = "select contactno from " + TABLE_NAME;
		  SQLiteDatabase db = this.getReadableDatabase();
		  Cursor cursor = db.rawQuery(selectQuery, null);
		  if (cursor.moveToFirst()) {
			   do {
			  num=cursor.getString(0);
			   } while (cursor.moveToNext());
			  }
			  cursor.close();
			  db.close();
			  return num;
		
	}
	
	//function to fetch personal details
	
	public ArrayList<String> fetchpersonaldetails()
	{
		ArrayList<String> a1=new ArrayList<String>();
		 
		  String selectQuery = "select id,name,mobile,fname,lname,emailid,dob,bloodgroup,fathername,fathermobile,spousename,spousemobile,profession,company,insurance,policyno,health_remark,address,rcity,rstate,rpin,oaddress,ocity,ostate,opin,vehicle from " + TABLE_NAME;
		  SQLiteDatabase db = this.getReadableDatabase();
		  Cursor cursor = db.rawQuery(selectQuery, null);
		  if (cursor.moveToFirst()) {
		   do {
		   a1.add(cursor.getString(0));
		   a1.add(cursor.getString(1));
		   a1.add(cursor.getString(2));
		   a1.add(cursor.getString(3));
		   a1.add(cursor.getString(4));
		   a1.add(cursor.getString(5));
		   a1.add(cursor.getString(6));
		   a1.add(cursor.getString(7));
		   a1.add(cursor.getString(8));
		   a1.add(cursor.getString(9));
		   a1.add(cursor.getString(10));
		   a1.add(cursor.getString(11));
		   a1.add(cursor.getString(12));
		   a1.add(cursor.getString(13));
		   a1.add(cursor.getString(14));
		   a1.add(cursor.getString(15));
		   a1.add(cursor.getString(16));
		   a1.add(cursor.getString(17));
		   a1.add(cursor.getString(18));
		   a1.add(cursor.getString(19));
		   a1.add(cursor.getString(20));
		   a1.add(cursor.getString(21));
		   a1.add(cursor.getString(22));
		   a1.add(cursor.getString(23));
		   a1.add(cursor.getString(24));
		   a1.add(cursor.getString(25));
		   
		  
		  
		   
	
		   } while (cursor.moveToNext());
		  }
		  cursor.close();
		  db.close();
		  return a1;
		
	}
	
	//insert existing details in local database
public void insertexistinguserdetails(ArrayList<String> al)
	
	{
		 SQLiteDatabase db = this.getWritableDatabase();
		  ContentValues values = new ContentValues();
		  values.put(mobile,al.get(0).toString());		  
		  values.put(shakestatus,al.get(1).toString());
		  values.put(sensitivity,al.get(2).toString());
		  values.put(timer,al.get(3).toString());
		  values.put(servercontact,al.get(4).toString());
		 // values.put(key, value)
		  
		 /* values.put(mobile,al.get(1).toString());
		  values.put(gender,al.get(2).toString());
		  values.put(age,al.get(3).toString());
		  values.put(bloodgroup,al.get(4).toString());
		  values.put(address,al.get(5).toString());
		  values.put(employed,al.get(6).toString());
		  values.put(profession,al.get(7).toString());
		  values.put(mobileno1,al.get(8).toString());
		  values.put(mobileno2,al.get(9).toString());
		  values.put(mobileno3,al.get(10).toString());
		  values.put(message1,al.get(11).toString());
		  values.put(message2,al.get(12).toString());
		  values.put(defaultno,al.get(13).toString());
		  values.put(defaultmsg,al.get(14).toString());
		  
		  values.put(fname,al.get(15).toString());
		  values.put(lname,al.get(16).toString());
		
		  values.put(dob,al.get(17).toString());
		
		  values.put(maritalstatus,al.get(18).toString());
		  values.put(fathername,al.get(19).toString());
		  values.put(fathermobile,al.get(20).toString());
		  values.put(spousename,al.get(21).toString());
		  values.put(spousemobile,al.get(22).toString());
		  
		  values.put(company,al.get(23).toString());
		  values.put(insurance,al.get(24).toString());
		  values.put(policyno,al.get(25).toString());
		
		  values.put(rcity,al.get(26).toString());
		  values.put(rstate,al.get(27).toString());
		  values.put(rpin,al.get(28).toString());
		  values.put(oaddress,al.get(29).toString());
		  values.put(ocity,al.get(30).toString());
		  values.put(ostate,al.get(31).toString());
		  values.put(opin,al.get(32).toString()); */
		 
		  try{    
			  db.insert(TABLE_NAME, null, values);
			  db.close();
			}
			catch (Exception e){
			    String error =  e.getMessage().toString();
			}
	}
	

//insert existing details in local database
public void insertexistinguserdetailssecond(ArrayList<String> al)

{
	 SQLiteDatabase db = this.getWritableDatabase();
	  ContentValues values = new ContentValues();
	  values.put(mobile,al.get(0).toString());		  
	  values.put(shakestatus,al.get(1).toString());
	  values.put(sensitivity,al.get(2).toString());
	  values.put(timer,al.get(3).toString());
	  values.put(servercontact,al.get(4).toString());
	 // values.put(key, value)
	 
	  values.put(mobileno1,al.get(5).toString());
	  values.put(mobileno2,al.get(6).toString());
	  values.put(mobileno3,al.get(7).toString());
	 
	  values.put(name,al.get(8).toString());
	  values.put(fname,al.get(8).toString());
	  values.put(email,al.get(9).toString());
	  values.put(dob,al.get(10).toString());
	  
	 
	  try{    
		  db.insert(TABLE_NAME, null, values);
		  db.close();
		}
		catch (Exception e){
		    String error =  e.getMessage().toString();
		}
}

	//function to update personal details
	public void updatepersonaldetails(ArrayList<String> al)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		  ContentValues values = new ContentValues();
		  values.put(fname,al.get(0).toString());
		  values.put(lname,al.get(1).toString());
		  values.put(email,al.get(2).toString());
		  values.put(dob,al.get(3).toString());
		  values.put(bloodgroup,al.get(4).toString());
		  values.put(fathername,al.get(5).toString());
		  values.put(fathermobile,al.get(6).toString());
		  values.put(spousename,al.get(7).toString());
		  values.put(spousemobile,al.get(8).toString());
		  values.put(profession,al.get(9).toString());
		  values.put(company,al.get(10).toString());
		  values.put(insurance,al.get(11).toString());
		  values.put(policyno,al.get(12).toString());
		  values.put(healthremark,al.get(13).toString());
		  values.put(address,al.get(14).toString());
		  values.put(rcity,al.get(15).toString());
		  values.put(rstate,al.get(16).toString());
		  values.put(rpin,al.get(17).toString());
		  values.put(oaddress,al.get(18).toString());
		  values.put(ocity,al.get(19).toString());
		  values.put(ostate,al.get(20).toString());
		  values.put(opin,al.get(21).toString());
		  values.put(vehicle,al.get(22).toString());
		  String where = "id=1";
		  try{    
			    db.update(TABLE_NAME, values, where, null);
			    db.close();
			}
			catch (Exception e){
			    String error =  e.getMessage().toString();
			}
		 
	}
	//function to fetch setting details
	public ArrayList<String> getsettings()
	{
		ArrayList<String> a1=new ArrayList<String>();
		 
		 
		  String selectQuery = "select shake_sensor_status,sensor_sensitivity,timer_duration from " + TABLE_NAME;
		  SQLiteDatabase db = this.getReadableDatabase();
		  Cursor cursor = db.rawQuery(selectQuery, null);
		  if (cursor.moveToFirst()) {
		   do {
		   a1.add(cursor.getString(0));
		   a1.add(cursor.getString(1));
		   a1.add(cursor.getString(2));
	
		   } while (cursor.moveToNext());
		  }
		  cursor.close();
		  db.close();
		  return a1;
		
	}

	//function to update timer
	public void updatetimer(String num)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		  ContentValues values = new ContentValues();
		  values.put(timer,num);
		  String where = "id=1";
		  try{    
			    db.update(TABLE_NAME, values, where, null);
			    db.close();
			}
			catch (Exception e){
			    String error =  e.getMessage().toString();
			}
	}
	//function to update sensitivity
	public void updatesensitivity(String num)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		  ContentValues values = new ContentValues();
		  values.put(sensitivity,num);
		  String where = "id=1";
		  try{    
			    db.update(TABLE_NAME, values, where, null);
			    db.close();
			}
			catch (Exception e){
			    String error =  e.getMessage().toString();
			}
	}
	
	//function to get shake status
	
	public String getshakestatus()
	{
		String num = null;
		 String selectQuery = "select shake_sensor_status from " + TABLE_NAME;
		  SQLiteDatabase db = this.getReadableDatabase();
		  Cursor cursor = db.rawQuery(selectQuery, null);
		  if (cursor.moveToFirst()) {
			   do {
			  num=cursor.getString(0);
			   } while (cursor.moveToNext());
			  }
			  cursor.close();
			  db.close();
			  return num;
		
	}
	

	//function to update shake status
	public void updateshakestatus(String num)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		  ContentValues values = new ContentValues();
		  values.put(shakestatus,num);
		  String where = "id=1";
		  try{    
			    db.update(TABLE_NAME, values, where, null);
			    db.close();
			}
			catch (Exception e){
			    String error =  e.getMessage().toString();
			}
	}
	//function to insert chat details in database
	public void insertchatdetails(String msg,String file,String path,String tag,String caption,String date,String sender)
	{
		 SQLiteDatabase db = this.getWritableDatabase();
		  ContentValues values = new ContentValues();
		  values.put("message",msg);		  
		  values.put("filename",file);
		  values.put("path",path);
		  values.put("tag", tag);
		  values.put("caption", caption);
		  values.put("currentdate", date);
		  values.put("sender", sender);
		 
		 
		  try{    
			  db.insert(TABLE_NAME2, null, values);
			  db.close();
			}
			catch (Exception e){
			    String error =  e.getMessage().toString();
			}
	}
	//function to file path from database
	public ArrayList<String> getfilepath(String name)
	{
		ArrayList<String> a1=new ArrayList<String>();
		 
		 
		  String selectQuery = "select path from "+TABLE_NAME2+" where filename is not null and filename='"+name+"'";
		  SQLiteDatabase db = this.getReadableDatabase();
		  Cursor cursor = db.rawQuery(selectQuery, null);
		  if (cursor.moveToFirst()) {
		   do {
		   a1.add(cursor.getString(0)); 
		   
	
		   } while (cursor.moveToNext());
		  }
		  cursor.close();
		  db.close();
		  return a1;
		
	}
	
	//function to find file path using tag from database
		public String getfilepathwithtag(Object tag)
		{
			String p = null;
			 
			  String selectQuery = "select path from "+TABLE_NAME2+" where filename is not null and tag="+tag+"";
			  SQLiteDatabase db = this.getReadableDatabase();
			  Cursor cursor = db.rawQuery(selectQuery, null);
			  if (cursor.moveToFirst()) {
			   do {
			   p=cursor.getString(0); 
			   
		
			   } while (cursor.moveToNext());
			  }
			  cursor.close();
			  db.close();
			  return p;
			
		}
	//function to get all files name
		public ArrayList<String> getallattachments()
		{
			ArrayList<String> a1=new ArrayList<String>();
			 
			 
			  String selectQuery = "select filename from "+TABLE_NAME2+" where filename is not null";
			  SQLiteDatabase db = this.getReadableDatabase();
			  Cursor cursor = db.rawQuery(selectQuery, null);
			  if (cursor.moveToFirst()) {
			   do {
			   a1.add(cursor.getString(0)); 
			     } while (cursor.moveToNext());
			  }
			  cursor.close();
			  db.close();
			  return a1;
			
		}
		//function to get all files datetime
				public ArrayList<String> getallattachmentsdatetime()
				{
					  ArrayList<String> a1=new ArrayList<String>();
					 
					 
					  String selectQuery = "select TEXT(msgdate) from "+TABLE_NAME2+" where filename is not null";
					  SQLiteDatabase db = this.getReadableDatabase();
					  Cursor cursor = db.rawQuery(selectQuery, null);
					  if (cursor.moveToFirst()) {
					   do {
					   a1.add(cursor.getString(0)); 
					     } while (cursor.moveToNext());
					  }
					  cursor.close();
					  db.close();
					  return a1;
					
				}
		
		//function to get messages from data
		public List<ArrayList<String>> getmessagefromlocal()
		{
			ArrayList<String> a1=new ArrayList<String>();
			ArrayList<String> a2=new ArrayList<String>();
			ArrayList<String> a3=new ArrayList<String>();
			ArrayList<String> a5=new ArrayList<String>();
			ArrayList<String> a6=new ArrayList<String>();
			List<ArrayList<String>> a4 = new ArrayList<ArrayList<String>>();
			
			 
			  String selectQuery = "select message,path,caption,currentdate,sender from "+TABLE_NAME2+"";
			  SQLiteDatabase db = this.getReadableDatabase();
			  Cursor cursor = db.rawQuery(selectQuery, null);
			  if (cursor.moveToFirst()) {
			   do {
			   a1.add(cursor.getString(0)); 
			   a2.add(cursor.getString(1)); 
			   a3.add(cursor.getString(2)); 
			   a5.add(cursor.getString(3));
			   a6.add(cursor.getString(4));
			   
		
			   } while (cursor.moveToNext());
			   a4.add(a1);
			   a4.add(a2);
			   a4.add(a3);
			   a4.add(a5);
			   a4.add(a6);
			   
			  }
			  cursor.close();
			  db.close();
			  return a4;
			
		}
		
		//function to delete attachments from database
		public void deleteattachment(String name)
		{
			try{
				SQLiteDatabase db = this.getWritableDatabase();
				db.delete(TABLE_NAME2,"filename='" + name+"'", null);
				 db.close();
			}
			catch(Exception e)
			{
				String ex=e.toString();
			}
			
		}
		
		//function to insert image in database
		public void updatephoto(String path)
		{
			  SQLiteDatabase db = this.getWritableDatabase();
			  ContentValues values = new ContentValues();
			  values.put("photo",path);
			  
			  String where = "id=1";
			  try{    
				    db.update(TABLE_NAME, values, where, null);
				    db.close();
				}
				catch (Exception e){
				    String error =  e.getMessage().toString();
				}
			
		}
		//function to find photo path from database
				public String getprofilephoto()
				{
					String p = null;
					 
					  String selectQuery = "select photo from "+TABLE_NAME+"";
					  SQLiteDatabase db = this.getReadableDatabase();
					  Cursor cursor = db.rawQuery(selectQuery, null);
					  if (cursor.moveToFirst()) {
					   do {
					   p=cursor.getString(0); 				   
				
					   } while (cursor.moveToNext());
					  }
					  cursor.close();
					  db.close();
					  return p;
					
				}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	
		
	}
	
	//function to insert new chat message in database
	public void insertnewmessage(String msg) 
	{
		Time today = new Time(Time.getCurrentTimezone());
		today.setToNow();
		String str=today.monthDay+"/"+(today.month+1)+"/"+today.year+" "+today.format("%k:%M");
		SQLiteDatabase db = this.getWritableDatabase();
		  ContentValues values = new ContentValues();
		  values.put("message",msg);		  
		  values.put("filename","null");
		  values.put("path","null");
		  values.put("tag", "null");
		  values.put("caption", "null");
		  values.put("currentdate", str);
		  values.put("sender", "server");
		 
		 
		  try{    
			  db.insert(TABLE_NAME2, null, values);
			  db.close();
			}
			catch (Exception e){
			    String error =  e.getMessage().toString();
			}
		
	}

}
