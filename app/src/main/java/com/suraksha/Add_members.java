package com.suraksha;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class Add_members extends Activity {

	Add_circles ac;
	static String mobile;
	static String circlename;
	private int cid;
	Button btnaddmember;
	ImageButton btnpick;
	ListView listchat;
	ArrayList<String> todoItems;
	ArrayList<String> memberlist;
    ArrayAdapter<String> aa;
	private final static int REQUEST_CONTACTPICKER = 1;
	 Connection conn=null;
	 Statement stmnt=null;
	 Mapplaces mp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_members);
		mobile=ac.umobile;
		circlename=ac.cname;
		
		todoItems = new ArrayList<String>();
		memberlist=new ArrayList<String>();
	    listchat=(ListView)findViewById(R.id.listView1);
	    aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);
	    listchat.setAdapter(aa);   
		
		btnpick=(ImageButton)findViewById(R.id.btnpick);
		btnaddmember=(Button)findViewById(R.id.btnaddmember);
		listchat.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		      @Override
		      public void onItemClick(AdapterView<?> parent, final View view,
		          final int position, long id) {
		    	  
		    	  openattachmentdialog(position);
		     }

		    });
		btnpick.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) 
			{
				  Intent intent = new Intent(Intent.ACTION_PICK, 
				           ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
				  startActivityForResult(intent, REQUEST_CONTACTPICKER);
				
			}
		});
		
		btnaddmember.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(circlename==null)
				{
					if(memberlist.isEmpty())
					{
					show();
					}
					else
					{
						insertcircledetails(0);
						
					}
				}
				else
				{
					
					if(memberlist.isEmpty())
					{
					show();
					}
					else
					{
						insertcircledetails(1);
						
					}
				}
				
				
				
			}
		});
	}

	//function to display alert
	public void show()
	{
		Toast.makeText(Add_members.this, "Please choose atleast one contact", Toast.LENGTH_LONG).show();
	}
	//function to insert circle details
	 public void insertcircledetails(int flag)
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
	         
	            if(flag==1)
	            {
	           ResultSet rs=stmnt.executeQuery("SELECT circleid FROM CIRCLE_MAIN WHERE cname='"+circlename+"' and cadmin='"+mobile+"'");
	           
	           while(rs.next())
	           {  
	        	 cid=rs.getInt(1);           
	           }
	            }
	            else
	            {
	            	cid=mp.cid;
	            	circlename=mp.circlename;
	            }
	           String temp;
	            for(int i=0;i<memberlist.size();i++)
	            {
	            	temp=memberlist.get(i).toString();
	            String SQL = "INSERT INTO CIRCLE_DETAILS(circleid,cname,mobileno,user_type,location_sharing) VALUES("+cid+",'"+circlename+"','"+temp+"','member','0');";
	            stmnt.execute(SQL);
	            }
	            Intent i=new Intent(Add_members.this,Mapplaces.class);
				startActivity(i);
	            
	                      
			
		}
			 catch (Exception e) 
			 {
				 
				 Toast.makeText(this, "Member already exists in this circle", Toast.LENGTH_LONG).show();
				 String m=e.toString();
				 
				 
	             e.printStackTrace();
	          }
	          finally {
	             if (stmnt != null) try { stmnt.close(); } catch(Exception e) {}
	             if (conn != null) try { conn.close(); } catch(Exception e) {}
	          }
			
		
		 
	 }
	 
	 //response method corresponding the contacts picked by the user
	public void onActivityResult(int reqCode, int resultCode, Intent data)
	   {
		 
	    if (reqCode == REQUEST_CONTACTPICKER) {
	   
	        if (resultCode == RESULT_OK) {
	        
	            Uri contactUri = data.getData();
	           
	            String[] projection = {Phone.NUMBER,Phone.DISPLAY_NAME};

	            Cursor cursor = getContentResolver()
	                    .query(contactUri, projection, null, null, null);
	            cursor.moveToFirst();

	            int column = cursor.getColumnIndex(Phone.NUMBER);
	            int column2=cursor.getColumnIndex(Phone.DISPLAY_NAME);
	            String name=cursor.getString(column2);
	            String number = cursor.getString(column);
	            number=number.replace("-", "");
	            number=number.replace(" ", "");
	            number=number.replace("+91", "");
	            if(number.length()==10)
	            {
	            	number="0"+number;
	            }
	            
				todoItems.add(name+""+"\n"+number);	
				memberlist.add(number);
				aa.notifyDataSetChanged(); 
	           
	        }
	    }
	   }
	
	// function to open dialog options
	
				private void openattachmentdialog(final int pos) {
					final CharSequence[] items = {"Delete","Cancel" };

					AlertDialog.Builder builder = new AlertDialog.Builder(Add_members.this);
					builder.setTitle("Select option");
					builder.setItems(items, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int item) {
							if(items[item].equals("Delete"))
							{
								todoItems.remove(pos);
								memberlist.remove(pos);
						    	aa.notifyDataSetChanged(); 
							}
							
							else if (items[item].equals("Cancel")) {
								dialog.dismiss();
							}
						}
					});
					builder.show();
				}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_members, menu);
		return true;
	}

}
