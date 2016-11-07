package com.suraksha;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Add_circles extends Activity {

	Databaseclass db;
	EditText txtcirclename;
	Button btndone;
    static String cname;
	 Connection conn=null;
	 Statement stmnt=null;
	 Statement stmnt2=null;
	 static String umobile;
	 private int cid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_circles);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();		
 		StrictMode.setThreadPolicy(policy); 
		db=new Databaseclass(Add_circles.this);
		txtcirclename=(EditText)findViewById(R.id.txtcirclename);
		btndone=(Button)findViewById(R.id.btndone);
		btndone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String circlename=txtcirclename.getText().toString();
				if(TextUtils.isEmpty(circlename))
				{
					txtcirclename.setError("Please enter a circle name");
				}
				else
				{
				createcircle();
				Intent i=new Intent(Add_circles.this,Add_members.class);
				startActivity(i);
				}
				
			}
		});
			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_circles, menu);
		return true;
	}
	
	//function to create circle in database
	public void createcircle()
	{
		try {
			String driver="net.sourceforge.jtds.jdbc.Driver";
			Class.forName(driver).newInstance();
			 String connString="jdbc:jtds:sqlserver://110.232.255.114:1433/howzat;encrypt=false;user=sa;password=pass@123;";
			 String username="sa";
			 String password="pass@123";
             conn=DriverManager.getConnection(connString,username,password);
             stmnt = conn.createStatement();
             stmnt2 = conn.createStatement();            
            String abc=conn.toString();
            ArrayList<String> temp=new ArrayList<String>();
            temp=db.getnamemobile();
            umobile=temp.get(1).toString();
            cname=txtcirclename.getText().toString();
         
            String SQL = "INSERT INTO circle_main(cname,cadmin) VALUES('" + cname + "', '" + umobile+"');";
            stmnt.execute(SQL);
            ResultSet rs=stmnt.executeQuery("SELECT circleid FROM CIRCLE_MAIN WHERE cname='"+cname+"' and cadmin='"+umobile+"'");
            
            while(rs.next())
            {  
         	 cid=rs.getInt(1);           
            }
            String SQL2 = "INSERT INTO circle_details(circleid,cname,mobileno,user_type,location_sharing) VALUES("+cid+",'"+cname+"','"+umobile+"','admin','1');";
            stmnt2.execute(SQL2);
		
	}
		 catch (Exception e) {
			 Toast.makeText(this, "circle already exists!", Toast.LENGTH_LONG).show();
             e.printStackTrace();
          }
          finally {
             if (stmnt != null) try { stmnt.close(); } catch(Exception e) {}
             if (conn != null) try { conn.close(); } catch(Exception e) {}
          }
	}
	
	@Override
	public void onBackPressed() {
		
		Intent i=new Intent(Add_circles.this,Mapplaces.class);
		startActivity(i);
	}

}
