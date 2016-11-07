package com.suraksha;

import java.util.ArrayList;

import org.json.JSONObject;

import com.webservice.AdSearchResultsTable;
import com.webservice.BroadcastResultsTable;
import com.webservice.JSONParser;
import com.webservice.Service1;
import com.suraksha.ClassifiedFragment.AsyncAdSearchDetails;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class BroadcastFragment extends Fragment {

	private ProgressDialog prgDialog;
	ListView lvpost;
	ImageView ivcircle,ivshare;
	LocationManager locationManager;
	boolean isGPSEnabled = false;
	String lat="", lon="";
	 private ProgressBar spinner;
	 static String msg;
	 Button btn_new_broadcast;
	 ArrayList<String> albid,aluname,alcity,albmedal,alsmedal,algmedal,albcontent,albimage,aluseful,aluseless,aldate;
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	View view = inflater.inflate(R.layout.activity_broadcast_fragment, container,false);
	lvpost=(ListView)view.findViewById(R.id.lvPost);
	ivcircle=(ImageView)view.findViewById(R.id.ivcircle);
	ivshare=(ImageView)view.findViewById(R.id.ivShare);
	albid=new ArrayList<String>();
	aluname=new ArrayList<String>();
	alcity=new ArrayList<String>();
	albmedal=new ArrayList<String>();
	alsmedal=new ArrayList<String>();
	algmedal=new ArrayList<String>();
	albcontent=new ArrayList<String>();
	albimage=new ArrayList<String>();
	aluseful=new ArrayList<String>();
	aluseless=new ArrayList<String>();
	aldate=new ArrayList<String>();
	btn_new_broadcast=(Button)view.findViewById(R.id.btnnewbroadcast);
	btn_new_broadcast.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			Intent i=new Intent(getActivity(),Add_new_broadcast.class);
 			startActivity(i);
			
		}
	});
	//spinner = (ProgressBar)view.findViewById(R.id.progressBar1);	
	ivcircle.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) 
		{
			
			locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);				
			ConnectivityManager mgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
	    	NetworkInfo networkInfo = mgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	    	boolean connectedmobile = networkInfo != null && networkInfo.isConnected();
	    	NetworkInfo networkInfo2=mgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	    	boolean connectedwifi=networkInfo2 != null && networkInfo2.isConnected();
	        if(connectedmobile || connectedwifi)
	        {
	        
	        	/*dialog = new ProgressDialog(Home.this);
				dialog.setCancelable(false);
				dialog.setMessage("Loading..");
				dialog.isIndeterminate();
				dialog.show();*/
	        	show("Please enable GPS for accuracy");
				Intent i=new Intent(getActivity(),Mapplaces.class);
     			startActivity(i);
     			/*if(dialog.isShowing())
     			{
     				dialog.dismiss();
     			}*/
     			
     			
	        }
	        else
	        {
	        	show("Please enable internet connection");
	        }
	        
		
		}
	});
	
ivshare.setOnClickListener(new View.OnClickListener() {   
public void onClick(View v) { 
	
	sharedialog();
 

}
});

new AsyncBroadcastDetails().execute("broadcast");
	return view;
}

//function to display dialog box to sharing

		private void sharedialog() {
			final CharSequence[] items = {"Share my location","Share this app"};

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Choose to share");
			builder.setItems(items, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int item) {
					if (items[item].equals("Share my location")) {

						locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
						 // getting GPS status
				        isGPSEnabled = locationManager
				                .isProviderEnabled(LocationManager.GPS_PROVIDER);

				        ConnectivityManager mgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
				    	NetworkInfo networkInfo = mgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				    	boolean connectedmobile = networkInfo != null && networkInfo.isConnected();
				    	NetworkInfo networkInfo2=mgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				    	boolean connectedwifi=networkInfo2 != null && networkInfo2.isConnected();
						// getting GPS status
				        isGPSEnabled = locationManager
				                .isProviderEnabled(LocationManager.GPS_PROVIDER);

				      if(!connectedmobile && !connectedwifi)
				      {
				    	  show("Please enable internet connection");
				      }
				        else
				        {
				        //	spinner.setVisibility(View.VISIBLE);
				       
				        	
				        	// Acquire a reference to the system Location Manager
				    		locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
				    		// Define a listener that responds to location updates
				    		LocationListener locationListener = new LocationListener() {
				    			public void onLocationChanged(Location location) {
				    				// Called when a new location is found by the network location provider.
				    				lat = Double.toString(location.getLatitude());
				    				lon = Double.toString(location.getLongitude());    				
				    				////////////////////////
				    				
				    				msg="My location is: http://maps.google.com/?q="+lat+","+lon+" \n Sent by Howzat SOS App \n click to download http://www.howzatsos.com/download ";	
				    				//spinner.setVisibility(View.GONE);
				    				shareIt(msg);
				    				locationManager.removeUpdates(this);
				    		        locationManager=null;
				    				////////////////////////////
				    				
				    				////////////////////////////
				    				
				    			
				    			}

				    		    public void onStatusChanged(String provider, int status, Bundle extras) {}
				    			public void onProviderEnabled(String provider) {}
				    			public void onProviderDisabled(String provider) {}
				    		};
				    		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 120000, 100, locationListener);
				        }
					} 
					else
					{
						//String playstorelink="https://play.google.com/store/apps/details?id=com.aviary.android.feather";
						 String playstorelink="HowZat sos App click link to download http://www.howzatsos.com/download";
						shareIt(playstorelink);
					}
				}
			});
			builder.show();
		}
		
		//function for sharing location on social media
		private void shareIt(String link) {
			Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
			sharingIntent.setType("text/plain");
			String shareBody = link;
			sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "HowZat SOS App");
			sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
			startActivity(Intent.createChooser(sharingIntent, "Share via"));
			}
		
		public void show(String str)
		{			
			Toast.makeText(getActivity(), str, Toast.LENGTH_LONG).show();
		}
		
		//code to fetch data for broadcast
		
		protected class AsyncBroadcastDetails extends AsyncTask<String,Void,ArrayList<BroadcastResultsTable>>
		{

			@Override
		 	   protected void onPreExecute() 
		 	   {  	
		 	     super.onPreExecute();
		 	      //showDialog(progress_bar_type);
		 	     prgDialog = new ProgressDialog(getActivity());
		         prgDialog.setMessage("Loading...");
		         prgDialog.setIndeterminate(true);
		         prgDialog.setCancelable(true);
		         prgDialog.show();
		 	      
		 	      
		 	   }
			@Override
			protected ArrayList<BroadcastResultsTable> doInBackground(String... params) {
				// TODO Auto-generated method stub
			//android.os.Debug.waitForDebugger();
				ArrayList<BroadcastResultsTable> broadcastDetail=null;
				//RestAPI api = new RestAPI();
				Service1 api=new Service1();
				try 
				{
					
					JSONObject jsonObj = api.getbroadcastmessages();
					JSONParser parser = new JSONParser();
					broadcastDetail = parser.parseBroadcastDetails(jsonObj);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.d("AsyncUserDetails", e.getMessage());

				}
				
				return broadcastDetail;
			}
			
			
			@Override
			protected void onPostExecute(ArrayList<BroadcastResultsTable> result) {
				// TODO Auto-generated method stub		
				
				for (int i = 0; i < result.size(); i++) {
					
				
	            	albid.add(result.get(i).getBroadcast_id());
	            	aluname.add(result.get(i).getName());
	            	alcity.add(result.get(i).getResidence_city());
	            	albmedal.add(result.get(i).getBronze_badges_count());
	            	alsmedal.add(result.get(i).getSilver_badges_count());
	            	algmedal.add(result.get(i).getGold_badges_count());
	            	albcontent.add(result.get(i).getBroadcast_content());
	            	albimage.add(result.get(i).getBroadcast_image());
	            	aluseful.add(result.get(i).getUseful_count());
	            	aluseless.add(result.get(i).getUseless_count());
	            	aldate.add(result.get(i).getBroadcast_date());
					
				}
				
				 BroadcastCustomList adapter = new BroadcastCustomList(getActivity(), albid, aluname, alcity, albmedal, alsmedal, algmedal, albcontent, albimage, aluseful, aluseless, aldate);
				 lvpost.setAdapter(adapter);
				 
				prgDialog.dismiss();
				 //listsearch.setSelection(listsearch.getAdapter().getCount()-1);
				
			}
			
		}
		
		
		
}
