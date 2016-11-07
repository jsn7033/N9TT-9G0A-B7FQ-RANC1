package com.suraksha;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Social_circle extends Activity {

	Databaseclass db;
	ListView lv;
	static ArrayList<String> namelistArrayListhome=new ArrayList<String>();
	ArrayList<String> numberlistArrayListhome=new ArrayList<String>();
	ArrayList<String> flaglistArrayListhome=new ArrayList<String>();
	Socialcirclelist scl;
	Connection conn=null;
	 Statement stmnt=null;
	 ArrayAdapter<String> adapter;
	 private ProgressDialog prgDialog;
       // Progress Dialog type (0 - for Horizontal progress bar)
       public static final int progress_bar_type = 0;
	@Override
		protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.social_circle);		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();		
 		StrictMode.setThreadPolicy(policy);
 		db=new Databaseclass(Social_circle.this);
		lv=(ListView)findViewById(R.id.listsocial);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		      @Override
		      public void onItemClick(AdapterView<?> parent, final View view,
		          final int position, long id) {
		    	  
		    	  openattachmentdialog(position);
		     }

		    });
		new PostTask().execute("contact");
		//fetchcontact();
		
		
	}

	// Show Dialog Box with Progress bar
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) 
        {
        case progress_bar_type:
            prgDialog = new ProgressDialog(this);
            prgDialog.setMessage("Please wait...");
            prgDialog.setIndeterminate(false);
            prgDialog.setCancelable(true);
            prgDialog.show();
            return prgDialog;
        default:
            return null;
        }
    }
    
    private class PostTask extends AsyncTask<String, Integer, String> {
    	   @Override
    	   protected void onPreExecute() 
    	   {  	
    	      super.onPreExecute();
    	      showDialog(progress_bar_type);
    	      
    	      
    	   }
    	 
    	   @Override
    	   protected String doInBackground(String... params) {
    		   fetchcontact();
			return null;
    	      
    	   }
    	 
    	   @Override
    	   protected void onProgressUpdate(Integer... values) {
    	      super.onProgressUpdate(values);
    	   }
    	 
    	   @Override
    	   protected void onPostExecute(String result) {
    	      super.onPostExecute(result);
    	      scl = new Socialcirclelist(Social_circle.this, namelistArrayListhome,numberlistArrayListhome,flaglistArrayListhome);
    		    lv.setAdapter(scl);
    	      dismissDialog(progress_bar_type);
    	   }
    	   }
	 
    //delete contacts
    private class Deletetask extends AsyncTask<String, Integer, String> {
 	   @Override
 	   protected void onPreExecute() 
 	   {  	
 	      super.onPreExecute();
 	      showDialog(progress_bar_type);
 	      
 	      
 	   }
 	 
 	   @Override
 	   protected String doInBackground(String... params) {
 		  //android.os.Debug.waitForDebugger();
 		   deletecontact(params[0]);
			return null;
 	      
 	   }
 	 
 	   @Override
 	   protected void onProgressUpdate(Integer... values) {
 	      super.onProgressUpdate(values);
 	   }
 	 
 	   @Override
 	   protected void onPostExecute(String result) {
 	      super.onPostExecute(result);
 	     		
 	     /*adapter = new ArrayAdapter<String>(Social_circle.this,
	              android.R.layout.simple_list_item_1, android.R.id.text1, namelistArrayListhome);
		lv.setAdapter(adapter);*/
 	     scl = new Socialcirclelist(Social_circle.this, namelistArrayListhome,numberlistArrayListhome,flaglistArrayListhome);
		    lv.setAdapter(scl);
 		
 	      dismissDialog(progress_bar_type);
 	   }
 	   }
	

	//function to fetch contacts from database
	public void fetchcontact()
	{   

		try {
            // Establish the connection.
			String driver="net.sourceforge.jtds.jdbc.Driver";
			Class.forName(driver).newInstance();
			 String connString="jdbc:jtds:sqlserver://110.232.255.114:1433/howzat;encrypt=false;user=sa;password=pass@123;";
			 String username="sa";
			 String password="pass@123";			

            conn=DriverManager.getConnection(connString,username,password);
            stmnt = conn.createStatement();
            ArrayList<String> tm=new ArrayList<String>();
            String mob;
            tm=db.getnamemobile();
            mob=tm.get(1).toString();
            
            
            ResultSet rs=stmnt.executeQuery("Select name,mobilec,howzat_user_flag from CONTACT_DETAILS where user_contactno='"+mob+"' and mobilec is not NULL");
            while(rs.next())
            {
            	
            	namelistArrayListhome.add(rs.getString(1));
            	numberlistArrayListhome.add(rs.getString(2));
            	flaglistArrayListhome.add(rs.getString(3));
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
	}
	
	//delete function
	public void deletecontact(String num)
	{   

		try {
            // Establish the connection.
			String driver="net.sourceforge.jtds.jdbc.Driver";
			Class.forName(driver).newInstance();
			 String connString="jdbc:jtds:sqlserver://110.232.255.114:1433/howzat;encrypt=false;user=sa;password=pass@123;";
			 String username="sa";
			 String password="pass@123";			

            conn=DriverManager.getConnection(connString,username,password);
            stmnt = conn.createStatement();
            ArrayList<String> tm=new ArrayList<String>();
            String mob;
            String deletemob;
            deletemob=num;
            tm=db.getnamemobile();
            mob=tm.get(1).toString();
            
            
            String query="delete from CONTACT_DETAILS where user_contactno='"+mob+"' and mobilec='"+deletemob+"'";
            stmnt.execute(query);
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
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_social, menu);
		return true;
	}
	
	// function to open dialog options
	
			private void openattachmentdialog(final int pos) {
				final CharSequence[] items = {"Delete","Cancel" };

				AlertDialog.Builder builder = new AlertDialog.Builder(Social_circle.this);
				builder.setTitle("Select option");
				builder.setItems(items, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int item) {
						if(items[item].equals("Delete"))
						{
							try
							{
							String mb=numberlistArrayListhome.get(pos).toString();
							namelistArrayListhome.remove(pos);
							numberlistArrayListhome.remove(pos);
							flaglistArrayListhome.remove(pos);
							new Deletetask().execute(mb);
							
					    	adapter.notifyDataSetChanged();
							}
							catch(Exception e)
							{
								String ex=e.toString();
							}
						}
						
						else if (items[item].equals("Cancel")) {
							dialog.dismiss();
						}
					}
				});
				builder.show();
			}

}
