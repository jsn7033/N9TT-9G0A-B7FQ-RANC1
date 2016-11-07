
package com.suraksha;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.SessionManager.SessionManager;

public class BootCompletedIntentReceiver extends BroadcastReceiver {
	SessionManager sessionManager;
	@Override
	public void onReceive(Context context, Intent intent) {
		sessionManager=new SessionManager(context.getApplicationContext());
		if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {

			if (sessionManager.Getshechfeature().equals("true") && !sessionManager.isMyServiceRunning(Shaker_Service_updated.class)) {
				context.startService(new Intent(context.getApplicationContext(), Shaker_Service_updated.class));
//				context.startService(new Intent(context.getApplicationContext(), ShakeDetector.class));


			}


		}
	}
}
