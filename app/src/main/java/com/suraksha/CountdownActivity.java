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

import com.SessionManager.SessionManager;

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
	static int activeInstances = 0;
	SessionManager session;
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_countdown);
		session=new SessionManager(CountdownActivity.this);
		String fdgd=session.GetsencerSencitityValue();
		if (isActive()) {
			CountdownActivity.this.finish();
		}else {
			if (currentapiVersion > android.os.Build.VERSION_CODES.HONEYCOMB) {
				setFinishOnTouchOutside(false);
			}
			try {

				String timee = session.GetseekBartimerValue();
				if (timee.equals("")) {
					timee = "0";
				}
				int temp = Integer.parseInt(timee);

				if (temp == 0) {
					starttime = temp + 5;
				} else if (temp == 1) {
					starttime = temp + 9;
				} else if (temp == 2) {
					starttime = temp + 13;
				}
				starttime = starttime * 1000;

				btn_cancel = (Button) this.findViewById(R.id.button);
				btn_cancel.setOnClickListener(this);
				tv_timer = (TextView) this.findViewById(R.id.timer);
				cdt = new MyCountDownTimer(starttime, duration);

				tv_timer.setText(tv_timer.getText() + String.valueOf(starttime / 1000));
				cdt.start();
				flag = true;
				long pattern[] = {0, 100, 200, 300, 400};
				vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				vibrator.vibrate(pattern, 0);
			} catch (Exception e) {
				String ex = e.toString();
			}
		}
	}




	static boolean isActive() {
		return (activeInstances > 0);
	}

	@Override
	public void onStart() {
		super.onStart();
		activeInstances++;
	}

	@Override
	public void onStop() {
		super.onStop();
		activeInstances--;
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

	public void onClick( View view) {
		if (!flag) {
			cdt.start();
			flag = true;
			btn_cancel.setText("Cancel");
		} else {
			cdt.cancel();
			flag = false;
			vibrator.cancel();
			CountdownActivity.this.finish();

		}
	}

	@Override
	public void onBackPressed() {
		cdt.cancel();
		flag = false;

		vibrator.cancel();
		CountdownActivity.this.finish();

	}



}
