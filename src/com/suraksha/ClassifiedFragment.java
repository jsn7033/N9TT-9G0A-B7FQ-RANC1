package com.suraksha;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.test.suitebuilder.annotation.Suppress;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.webservice.AdSearchResultsTable;
import com.webservice.BotSearchResultsTable;
import com.webservice.JSONParser;
import com.webservice.Service1;
import com.suraksha.SearchFragment.AsyncBotSearchDetails;

public class ClassifiedFragment extends Fragment {

	ListView listad;
	Button btnsend;
	EditText etsearch;
	
	Databaseclass db;
	String mobile;
	public static final int progress_bar_type = 0;
	private ProgressDialog prgDialog;
	ArrayList<String> listadid,listadtitle,listaddesc;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.activity_classified_fragment,
				container, false);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();		
 		StrictMode.setThreadPolicy(policy);
		AdView admob=(AdView)view.findViewById(R.id.adView);
		admob.loadAd(new AdRequest.Builder().build());
		db=new Databaseclass(getActivity());
		ArrayList<String> temp=new ArrayList<String>();
        temp=db.getnamemobile();
        mobile=temp.get(1).toString();
		btnsend=(Button) view.findViewById(R.id.btnSend);
		etsearch=(EditText) view.findViewById(R.id.etSearch);
		btnsend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String searchad=etsearch.getText().toString().trim();
				
				if(TextUtils.isEmpty(searchad) || searchad.trim().equals(""))
					{
						etsearch.setError("Please enter a keyword to search");
					}
					else
					{ 
						listadid.clear();
						listaddesc.clear();
						listadtitle.clear();
						new AsyncAdSearchDetails().execute(searchad,mobile);						    
					}
			}
		});
		listad = (ListView) view.findViewById(R.id.addsList);
		listadid=new ArrayList<String>();
        listadtitle=new ArrayList<String>();
        listaddesc=new ArrayList<String>();
		//listad.setAdapter(new AddAdapter(getActivity(), 0));
		
		return view;
	}

	private class AddAdapter extends ArrayAdapter<String> {

		Context context;
		int image[] = { R.drawable.ic_broadcaster, R.drawable.ic_search,
				R.drawable.ic_map };

		public AddAdapter(Context context, int resource) {
			super(context, resource);
			// TODO Auto-generated constructor stub
			this.context=context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return image.length;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View v = convertView;
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.adds_row, parent, false);
				holder.img = (ImageView) v.findViewById(R.id.ivAdds);
				v.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.img.setImageResource(image[position]);

			return v;
		}
	}

	private static class ViewHolder {
		ImageView img;
	}
	
	/*private class Searchtask extends AsyncTask<String, Integer, String> {
 	   @Override
 	   protected void onPreExecute() 
 	   {  	
 	      super.onPreExecute();
 	      //showDialog(progress_bar_type);
 	     prgDialog = new ProgressDialog(getActivity());
         prgDialog.setMessage("Searching...");
         prgDialog.setIndeterminate(true);
         prgDialog.setCancelable(false);
         prgDialog.show();
 	      
 	      
 	   }
 	 
 	   @Override
 	   protected String doInBackground(String... params) {
 	// android.os.Debug.waitForDebugger();
 		   insertsearchquery(searchad);
 		   getsearchresults();
			return null;
 	      
 	   }
 	 
 	   @Override
 	   protected void onProgressUpdate(Integer... values) {
 	      super.onProgressUpdate(values);
 	   }
 	 
 	   @Override
 	   protected void onPostExecute(String result) {
 	      super.onPostExecute(result);
 	      
			    CustomList adapter = new CustomList(Home.this, listadtitle,listaddesc,0);
			    listad.setAdapter(adapter);
			    listad.setVisibility(View.VISIBLE);
			    listchat.setVisibility(View.GONE);
 	     prgDialog.dismiss();
 	   }
 	   }
	
	//function to insert search query
	public void insertsearchquery(String query)
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
	            String sql="INSERT INTO SEARCH_DETAILS(search_keyword,search_userid) VALUES('"+query+"','"+umobile+"')";
	            stmnt.execute(sql);				
			
		}
			 catch (Exception e) {
	             e.printStackTrace();
	          }
	          finally {
	             if (stmnt != null) try { stmnt.close(); } catch(Exception e) {}
	             if (conn != null) try { conn.close(); } catch(Exception e) {}
	          }
			
     }*/
	//async for ads
	protected class AsyncAdSearchDetails extends AsyncTask<String,Void,ArrayList<AdSearchResultsTable>>
	{

		@Override
	 	   protected void onPreExecute() 
	 	   {  	
	 	      super.onPreExecute();
	 	      //showDialog(progress_bar_type);
	 	     prgDialog = new ProgressDialog(getActivity());
	         prgDialog.setMessage("Searching...");
	         prgDialog.setIndeterminate(true);
	         prgDialog.setCancelable(false);
	         prgDialog.show();
	 	      
	 	      
	 	   }
		@Override
		protected ArrayList<AdSearchResultsTable> doInBackground(String... params) {
			// TODO Auto-generated method stub
			//android.os.Debug.waitForDebugger();
			ArrayList<AdSearchResultsTable> searchDetail=null;
			//RestAPI api = new RestAPI();
			Service1 api=new Service1();
			try 
			{
				
				JSONObject jsonObj = api.getadresults(params[0], params[1]);
				JSONParser parser = new JSONParser();
				searchDetail = parser.parseAdSearchDetails(jsonObj);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.d("AsyncUserDetails", e.getMessage());

			}
			
			return searchDetail;
		}
		
		
		@Override
		protected void onPostExecute(ArrayList<AdSearchResultsTable> result) {
			// TODO Auto-generated method stub		
			
			for (int i = 0; i < result.size(); i++) {
				
				listadid.add(result.get(i).getAd_id());
            	listadtitle.add(result.get(i).getAd_categoryname());
            	listaddesc.add(result.get(i).getAd_description());
				
			}
			
			 CustomList adapter = new CustomList(getActivity(), listadtitle,listaddesc,0);
			 listad.setAdapter(adapter);
			 
			prgDialog.dismiss();
			 //listsearch.setSelection(listsearch.getAdapter().getCount()-1);
			
		}
		
	}
	

	
}
