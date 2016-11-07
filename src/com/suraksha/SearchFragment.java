package com.suraksha;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devsmart.android.ui.HorizontalListView;
import com.webservice.BotSearchResultsTable;
import com.webservice.JSONParser;
import com.webservice.Service1;
import com.webservice.UserDetailsTable;

public class SearchFragment extends Fragment {

	HorizontalListView hlList;
	ListView listsearch;
	RelativeLayout searchLayout, broadcastLayout, classiffiedLayout;
	EditText etsearch;
	Button btnsearch;
	DiscussArrayAdapter chatadapter;
	String searchtext;
	Databaseclass db;
	String mobile;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.activity_search_fragment,
				container, false);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();		
 		StrictMode.setThreadPolicy(policy);
		db=new Databaseclass(getActivity());
		ArrayList<String> temp=new ArrayList<String>();
        temp=db.getnamemobile();
        mobile=temp.get(1).toString();
		searchLayout = (RelativeLayout) view.findViewById(R.id.searchContainer);
		 broadcastLayout=(RelativeLayout)view.findViewById(R.id.broadcastContainer);
		 classiffiedLayout =(RelativeLayout)view.findViewById(R.id.classifiedContainer);
		 listsearch=(ListView)view.findViewById(R.id.listsearch);
		 etsearch=(EditText)view.findViewById(R.id.etSearch);
		 btnsearch=(Button)view.findViewById(R.id.btnSend);
		
		//////////////////////////////////////////
		 chatadapter = new DiscussArrayAdapter(getActivity(), R.layout.comment_row);
		 listsearch.setAdapter(chatadapter);
		// chatadapter.add(new OneComment(true, "Hi! How may I assist you","Afzal",""));
		 btnsearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				searchtext=etsearch.getText().toString().trim();
				 Time today = new Time(Time.getCurrentTimezone());
					today.setToNow();
					String str=today.monthDay+"/"+(today.month+1)+"/"+today.year+" "+today.format("%k:%M");
					
				if(TextUtils.isEmpty(searchtext) || searchtext.trim().equals(""))
					{
						etsearch.setError("Please type something");
					}
					else
					{
				chatadapter.add(new OneComment(false, searchtext,"Afzal",str));
				listsearch.setSelection(listsearch.getAdapter().getCount()-1); // used to focus last message			    
				etsearch.setText("");
				new AsyncBotSearchDetails().execute(searchtext,mobile);
				//
					}
				
			}
		});		
		/////////////////////////////////////////
		return view;
	}

	
	
	protected class AsyncBotSearchDetails extends AsyncTask<String,Void,ArrayList<BotSearchResultsTable>>
	{

		@Override
		protected ArrayList<BotSearchResultsTable> doInBackground(String... params) {
			// TODO Auto-generated method stub
			//android.os.Debug.waitForDebugger();
			ArrayList<BotSearchResultsTable> searchDetail=null;
			//RestAPI api = new RestAPI();
			Service1 api=new Service1();
			try 
			{
				
				JSONObject jsonObj = api.getbotresults(params[0],params[1]);
				JSONParser parser = new JSONParser();
				searchDetail = parser.parseBotSearchDetails(jsonObj);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.d("AsyncUserDetails", e.getMessage());

			}
			
			return searchDetail;
		}
		
		
		@Override
		protected void onPostExecute(ArrayList<BotSearchResultsTable> result) {
			// TODO Auto-generated method stub			
			for (int i = 0; i < result.size(); i++) {
				//data.add(result.get(i).getNo() + " " + result.get(i).getName());
				chatadapter.add(new OneComment(true, result.get(i).getResult_desc()+"\n"+result.get(i).getLink(),"Afzal",""));
				
			}
			
			listsearch.setSelection(listsearch.getAdapter().getCount()-1);
			
		}
		
	}
	
}
