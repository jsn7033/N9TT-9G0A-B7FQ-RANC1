package com.suraksha;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import com.webservice.Service1;
public class GetBroadCastList extends AsyncTask{

	@Override
	protected Object doInBackground(Object... params) {
		
		// TODO Auto-generated method stub
		Service1 service=new Service1();
		JSONObject broadcastList=null;
		try {
			Log.d("tagd", "Inside asynctask");
			broadcastList=service.getbroadcastmessages();
			Log.d("tagd", "LIST:");
			Log.d("tagd", broadcastList.toString());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return broadcastList;
	}

}
