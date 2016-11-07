package com.suraksha;





import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CountdownActivity extends Activity implements OnClickListener {

	final int mId=100;
	public Vibrator vibrator;
	private CountDownTimer cdt;	
	private long starttime;
	private final long duration = 1 * 1000;
	
	private boolean flag = false;
	private Button btn_cancel;
	public TextView tv_timer;
	Databaseclass db;
	ArrayList<String> list;
	int currentapiVersion = android.os.Build.VERSION.SDK_INT;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_countdown);
		if(currentapiVersion>android.os.Build.VERSION_CODES.HONEYCOMB)
		{
			setFinishOnTouchOutside(false);
		}
		try
		{
		db=new Databaseclass(CountdownActivity.this);
		list= new ArrayList<String>();
		list=db.getsettings();
		int temp=Integer.parseInt(list.get(2).toString());
		if(temp==0)
		{
			starttime=temp+5;
		}
		else if(temp==1)
		{
			starttime=temp+9;
		}
		else if(temp==2)
		{
			starttime=temp+13;
		}
		starttime=starttime*1000;
		
		btn_cancel = (Button) this.findViewById(R.id.button);
		btn_cancel.setOnClickListener(this);		
		tv_timer = (TextView) this.findViewById(R.id.timer);		
		cdt = new MyCountDownTimer(starttime, duration);
		
		tv_timer.setText(tv_timer.getText() + String.valueOf(starttime / 1000));		
		cdt.start();
		flag = true;
		long pattern[] = { 0, 100, 200, 300, 400 };
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(pattern,0);
		}
		catch(Exception e)
		{
			String ex=e.toString();
		}

	}

	

	public class MyCountDownTimer extends CountDownTimer {
		public MyCountDownTimer(long startTime, long interval) {
			super(startTime, interval);
		}

		
		@Override
		public void onFinish() {
			try
			{
			vibrator.cancel();
		//	notification();
			Intent i=new Intent(CountdownActivity.this,Sensorcallingevent.class);
			startActivity(i);
			}
			catch(Exception e)
			{
				String ex=e.toString();
			}
			
			
		}

		@Override
		public void onTick(long millisUntilFinished) {
			//Saniye olarak goster  1 saniye = 1000 millis
			tv_timer.setText("" + millisUntilFinished / 1000);
		}
	}

	@Override
	public void onClick(View v) {
		if (!flag) {
			cdt.start();
			flag = true;
			btn_cancel.setText("Cancel");
		} else {
			cdt.cancel();
			flag = false;
			//btn_cancel.setText("YENIDEN BASLAT");
			vibrator.cancel();
			Intent i=new Intent(CountdownActivity.this,Home.class);
			startActivity(i);
		}
	}
	
	@Override
	public void onBackPressed() {
		cdt.cancel();
		flag = false;
	
		vibrator.cancel();
		Intent i=new Intent(CountdownActivity.this,Home.class);
		startActivity(i);
	
	}
	
	/*
	public void notification()
	{
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(CountdownActivity.this)
		        .setSmallIcon(R.drawable.secure1)
		        .setContentTitle("Howzat panic alert activated")
		        .setContentText("Your location has been sent to registered mobile numbers");
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(CountdownActivity.this, Home.class);

		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(CountdownActivity.this);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(Home.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
		    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(mId, mBuilder.build());
	}
	*/
	
	

}
