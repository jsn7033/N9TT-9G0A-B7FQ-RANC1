package com.suraksha;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;





import android.net.ConnectivityManager;
import android.os.Bundle;
//import android.provider.Telephony.Sms.Conversations;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class Settings extends Activity {

	private SensorManager sensorManager;
	Databaseclass db;
	Button btncontact,btnattachment,btnpostad;
	Button btnpersonaldetails;
	ArrayList<String> listsetting;
	ToggleButton btntoggleshake;
	ToggleButton btntoggle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		db=new Databaseclass(Settings.this);
		listsetting=new ArrayList<String>();
		db.getReadableDatabase();
		listsetting=db.getsettings();
		btnpostad=(Button)findViewById(R.id.btnpostad);
		int sensitivity=Integer.parseInt(listsetting.get(1).toString());
		int timer=Integer.parseInt(listsetting.get(2).toString());
		final TextView reading = (TextView) findViewById(R.id.texttimer);
        SeekBar seekBar = (SeekBar)findViewById(R.id.seekbar);       
        seekBar.setMax(100);        
        seekBar.setProgress(sensitivity);
        SeekBar seekBartimer = (SeekBar)findViewById(R.id.seekbartimer);        
        seekBartimer.setMax(2);
        if(timer==0)
        {
        	int temp=5;
        	reading.setText("Timer Duration: "+temp+"sec"); 	 
        }
        else if(timer==1)
        {
        	int temp=10;
        	reading.setText("Timer Duration: "+temp+"sec"); 	 
        }
        else if(timer==2)
        {
        	int temp=15;
        	reading.setText("Timer Duration: "+temp+"sec"); 	 
        }
        seekBartimer.setProgress(timer);
        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
   
   @Override
   public void onStopTrackingTouch(SeekBar seekBar) {
   }
   
   @Override
   public void onStartTrackingTouch(SeekBar seekBar) {
    // TODO Auto-generated method stub
   }
   
   @Override
   public void onProgressChanged(SeekBar seekBar, int progress,
     boolean fromUser) {
	   String temp=progress+"";
	   db.updatesensitivity(temp);	
	  
   }
  });
        seekBartimer.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
        	   
        	   @Override
        	   public void onStopTrackingTouch(SeekBar seekBar) {
        	   }
        	   
        	   @Override
        	   public void onStartTrackingTouch(SeekBar seekBar) {
        	    // TODO Auto-generated method stub
        	   }
        	   
        	   @Override
        	   public void onProgressChanged(SeekBar seekBar, int progress,
        	     boolean fromUser) {
        		   if(progress==0)
        		   {
        			   String temp=progress+"";
        			   db.updatetimer(temp);
        			   progress+=5;
            	       reading.setText("Timer Duration: "+progress+"sec"); 
            	      
            	      
        		   }
        		   else if(progress==1)
        		   {
        			   String temp=progress+"";
        			   db.updatetimer(temp);
        			   progress+=9;
            	       reading.setText("Timer Duration: "+progress+"sec"); 
        		   }
        		   else if(progress==2)
        		   {
        			   String temp=progress+"";
        			   db.updatetimer(temp);
        			   progress+=13;
            	       reading.setText("Timer Duration: "+progress+"sec"); 
        		   }
        		 
        	   }
        	  });
		
		/////////////
		btncontact=(Button)findViewById(R.id.btnupdate);
		
		btntoggle=(ToggleButton)findViewById(R.id.togglebtn);
		btntoggleshake=(ToggleButton)findViewById(R.id.togglebtnsensor);
		btnattachment=(Button)findViewById(R.id.btnshowattachment);
		db.getReadableDatabase();
		String status=db.getshakestatus();
		
		if(status.equals("true"))
		{
			//btntoggleshake.setEnabled(true);
			btntoggleshake.setChecked(true);
		}
		else
		{
			btntoggleshake.setChecked(false);
			 stopService(new Intent(Settings.this, Shaker_Service_updated.class));
			//btntoggleshake.setEnabled(false);
		}
		btnpersonaldetails=(Button)findViewById(R.id.btnpersonal);
		btncontact.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i=new Intent(Settings.this,Fill_emergency_numbers.class);
    			startActivity(i);
				
			}
		});
		
		btnpersonaldetails.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i=new Intent(Settings.this,Updatepersonaldetail.class);
    			startActivity(i);
				
			}
		});
		btnattachment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				Intent i=new Intent(Settings.this,Show_attachments.class);
    			startActivity(i);
				
			}
		});
		btnpostad.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i=new Intent(Settings.this,Ad_form.class);
				startActivity(i);
				
			}
		});
	}

	public void onCheckboxClicked(View view) {
	    // Is the view now checked?
	    boolean checked = ((ToggleButton) view).isChecked();
	    
	    // Check which checkbox was clicked
	    switch(view.getId()) {
	        case R.id.togglebtn:
	            if (checked)
	            {
	            	setMobileDataEnabled(getBaseContext(), true);
	            	
	              
	            }
	            else
	            {
	            	
	            }
	           
	            break;
	    }
	}
	
	private void setMobileDataEnabled(Context context, boolean enabled) {
		try
		{
	    final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    final Class conmanClass = Class.forName(conman.getClass().getName());
	    final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
	    iConnectivityManagerField.setAccessible(true);
	    final Object iConnectivityManager = iConnectivityManagerField.get(conman);
	    final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
	    final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
	    setMobileDataEnabledMethod.setAccessible(true);
	    setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
		}
		catch (Exception e) {
			String x=e.toString();
		}
	}
	
	public void onToggleClicked(View view) {
	    // Is the toggle on?
	    boolean on = ((ToggleButton) view).isChecked();
	    
	    if (on) {
	    	db.updateshakestatus("true");
	    	startService(new Intent(Settings.this, Shaker_Service_updated.class)); 
	    } else {
	    	db.updateshakestatus("false");
	        stopService(new Intent(Settings.this, Shaker_Service_updated.class));   
	          
	    }
	}
	@Override
	public void onBackPressed() 
	{
		
		Intent i=new Intent(Settings.this,Home.class);
		startActivity(i);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

}
