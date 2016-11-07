package com.suraksha;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class Adsearch_details extends Activity {
	
	Home hm;
	TextView txttitle,txtcategory,txtcategoryname,txtlocation,txtdescription,txtaddress,txtcontact,txtemail,txtwebsite;
	String title,category,categoryname,location,keywords,description,address,contact,email,website;
	int adid;
	Button btnchat;
	Connection conn=null;
	Statement stmnt=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addetails_search);
		txttitle=(TextView)findViewById(R.id.txttitle);
		txtcategory=(TextView)findViewById(R.id.txtcategory);
		txtcategoryname=(TextView)findViewById(R.id.txtcategoryname);
		txtlocation=(TextView)findViewById(R.id.txtlocation);
		txtdescription=(TextView)findViewById(R.id.txtdescription);
		txtaddress=(TextView)findViewById(R.id.txtaddress);
		txtcontact=(TextView)findViewById(R.id.txtcontact);
		txtemail=(TextView)findViewById(R.id.txtemail);
		txtwebsite=(TextView)findViewById(R.id.txtwebsite);
		adid=Integer.parseInt(hm.adid);
		btnchat=(Button)findViewById(R.id.btnchat);		
		btnchat.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				show("Chat feature is coming soon!!");
				
			}
		});
		fetch_addetails(adid);
		
	}
	 public void show(String str)
	 {
		 Toast.makeText(this, str, Toast.LENGTH_LONG).show();
	 }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.adsearch_details, menu);
		return true;
	}
	
	//function to fetch description of given ad
	public void fetch_addetails(int id)
	{
		try {
			String driver="net.sourceforge.jtds.jdbc.Driver";
			Class.forName(driver).newInstance();
			 String connString="jdbc:jtds:sqlserver://110.232.255.114:1433/howzat;encrypt=false;user=sa;password=pass@123;";
			 String username="sa";
			 String password="pass@123";		

            conn=DriverManager.getConnection(connString,username,password);
            stmnt = conn.createStatement();
            
            ResultSet rs=stmnt.executeQuery("select ad_category,ad_categoryname,ad_city,ad_keywords,ad_title,ad_description,ad_address,ad_contactno,ad_email,ad_website from AD_DETAILS where ad_id="+adid+"");
            while(rs.next()){
            
            	category=rs.getString(1);
            	categoryname=rs.getString(2);
            	location=rs.getString(3);
            	keywords=rs.getString(4);
            	title=rs.getString(5);
            	description=rs.getString(6);
            	address=rs.getString(7);
            	contact=rs.getString(8);
            	email=rs.getString(9);
            	website=rs.getString(10);
            
            }
            txttitle.setText(title);
            txtcategory.setText(category);
            txtcategoryname.setText(categoryname);
            txtlocation.setText(location);
            txtdescription.setText(description);
            txtaddress.setText(address);
            txtcontact.setText(contact);
            txtemail.setText(email);
            txtwebsite.setText(website);
          
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

}
