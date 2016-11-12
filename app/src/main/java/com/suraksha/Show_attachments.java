package com.suraksha;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class Show_attachments extends Activity {

	String[] listItems = {"exploring", "android", 
            "list", "activities"};
	 Connection conn=null;
	 Statement stmnt=null;
	 Databaseclass db;
	 String umobile;
	 ArrayList<String> listfilename;
	 ArrayList<String> listpath;
	 ListView listattach;
	 Attachmentlist adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_attachments);
		db= new Databaseclass(Show_attachments.this);		
		 listattach = (ListView) findViewById(R.id.list);
		  listfilename = new ArrayList<String>();
		  listpath = new ArrayList<String>();
		  listfilename=db.getallattachments();
		  adapter = new Attachmentlist(Show_attachments.this, listfilename,0);
		    
		    listattach.setAdapter(adapter);
		    listattach.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		      @Override
		      public void onItemClick(AdapterView<?> parent, final View view,
		          final int position, long id) {
		    	  
		    	  openattachmentdialog(position);
		     }

		    });
		  }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_attachments, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the HomeActivity/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	  
	  //function to get names of all attachments
	  public void getattachmentname()
	  {
		  try {
				String driver="net.sourceforge.jtds.jdbc.Driver";
				Class.forName(driver).newInstance();
				 String connString="jdbc:jtds:sqlserver://110.232.255.114:1433/howzat;encrypt=false;user=sa;password=pass@123;";
				 String username="sa";
				 String password="pass@123";

	            conn=DriverManager.getConnection(connString,username,password);
	            stmnt = conn.createStatement();
	           
	            ArrayList<String> temp=new ArrayList<String>();
	            temp=db.getnamemobile();
	            umobile=temp.get(1).toString();
	           ResultSet rs=stmnt.executeQuery("SELECT filename FROM Chat_details WHERE filename is not null and sender='"+umobile+"'");
	           
	           while(rs.next()){
	        	 listfilename.add(rs.getString(1));           
	           }
	         
		      }
			 catch (Exception e) {
				 
				 String m=e.toString();
				 
				 
	             e.printStackTrace();
	          }
	          finally {
	             if (stmnt != null) try { stmnt.close(); } catch(Exception e) {}
	             if (conn != null) try { conn.close(); } catch(Exception e) {}
	          }
	  }
	  
	// function to open dialog options
		
		private void openattachmentdialog(final int pos) {
			final CharSequence[] items = {"Open","Delete","Cancel" };

			AlertDialog.Builder builder = new AlertDialog.Builder(Show_attachments.this);
			builder.setTitle("Select option");
			builder.setItems(items, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int item) {
					if (items[item].equals("Open")) 
					{
						  String name=listattach.getItemAtPosition(pos).toString();
				    	  ArrayList<String> t=new ArrayList<String>();
				    	  t=db.getfilepath(name);
				    	  String filepath=t.get(0).toString();
				    	  File file = new File(filepath);
				          Intent intent = new Intent(Intent.ACTION_VIEW);
				    	  if(filepath.endsWith(".pdf"))
				    	  {
				    		  intent.setDataAndType(Uri.fromFile(file),"text/*");
				    	  }
				    	  else if(filepath.endsWith(".jpg") || filepath.endsWith(".jpeg") || filepath.endsWith(".png") || filepath.endsWith(".JPG") || filepath.endsWith(".JPEG") || filepath.endsWith(".PNG"))
				    	  {
				    		  intent.setDataAndType(Uri.fromFile(file), "image/*");
				    	  }
				    	  else if(filepath.endsWith(".doc") || filepath.endsWith(".docx") || filepath.endsWith(".ppt") || filepath.endsWith(".pptx") || filepath.endsWith(".xls") || filepath.endsWith(".xlsx"))
				    	  {
				    		  intent.setDataAndType(Uri.fromFile(file), "text/*");
				    	  }
				    	  intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				          startActivity(intent);
					} 
					else if(items[item].equals("Delete"))
					{
						String name=listattach.getItemAtPosition(pos).toString();
						db.deleteattachment(name);
						listfilename.remove(pos);
				    	adapter.notifyDataSetChanged();
					}
					
					else if (items[item].equals("Cancel")) {
						dialog.dismiss();
					}
				}
			});
			builder.show();
		}
}
