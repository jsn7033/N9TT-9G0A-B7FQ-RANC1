package com.suraksha;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class Mapplaces extends FragmentActivity
{	
	private final String TAG = getClass().getSimpleName();
	private GoogleMap mMap;
	private String[] places;
	private LocationManager locationManager;
	private Location loc;
	private double lat,lon;
	private LatLng mylocation;
	private LatLng memlocation;
	private Spinner spinnercircle;
	static String circlename;
	private String umobile;
	private String uname;
	static int cid;
	Connection conn=null;
	Statement stmnt=null;
	Databaseclass db;
	ArrayList<String> circlelist;
	ArrayList<String> circleidlist;
	ArrayList<String> locationlist;
	ArrayList<String> memberlist;
	ArrayList<String> membernamelist;
	ArrayList<String> locationsharinglist;
	private String[] dataObjects=new String[]{};
	ArrayList<String> data;
	HorizontialListView lv;
	ToggleButton tb;
	int locationflag;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapplaces);
		db=new Databaseclass(Mapplaces.this);
		locationlist=new ArrayList<String>();
		circlelist=new ArrayList<String>();
		circleidlist=new ArrayList<String>();
		memberlist=new ArrayList<String>();
		membernamelist=new ArrayList<String>();
		locationsharinglist=new ArrayList<String>();
		tb=(ToggleButton)findViewById(R.id.toggleButton1);
		data=new ArrayList<String>();
		lv = (HorizontialListView) findViewById(R.id.listview);		
		spinnercircle=(Spinner)findViewById(R.id.spinnercircle);
	//	getcirclenames();
		spinnercircle.setAdapter(new ArrayAdapter<String>(Mapplaces.this,
                android.R.layout.simple_spinner_item, circlelist));
		initCompo();
		places = getResources().getStringArray(R.array.places);
		//currentLocation();		
		spinnercircle.setOnItemSelectedListener(new OnItemSelectedListener() {
         
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				try
				{
					circlename=spinnercircle.getItemAtPosition(arg2).toString();
					if(circlename.equalsIgnoreCase("Create new circle"))
					{
						Intent i=new Intent(Mapplaces.this,Add_circles.class);
						startActivity(i);
					}
					else if(circlename.equalsIgnoreCase("Choose a circle"))
					{
						currentLocation();
					}
					else if(circlename.equalsIgnoreCase("Social"))
					{
						Intent i=new Intent(Mapplaces.this,Social_circle.class);
						startActivity(i);
					}
					else
					{   
						memberlist.clear();
					    membernamelist.clear();
					    locationsharinglist.clear();
						data.clear();
						mMap.clear();						
						cid=Integer.parseInt(circleidlist.get(arg2).toString());
						locationflag=Integer.parseInt(locationlist.get(arg2).toString());
						if(locationflag==1)
						{
							tb.setChecked(true);
						}
						else
						{
							tb.setChecked(false);
						}
						circlememberlocation(cid);						
						//dataObjects[1]="Add new";
						data.add("Add new");					
						lv.setAdapter(mAdapter);
					    currentLocation();
					//	dataObjects=new String[]{};
						
					}
				}
				catch(Exception ex)
				{
					String e=ex.toString();
				}
				
				finally {
		            
		          }
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}	
	 
	//adapter for managing member's list at the bottom  
	private BaseAdapter mAdapter = new BaseAdapter() {

		
			private OnClickListener imageButtonClicked = new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				ImageButton b = (ImageButton)v;
				int imgbtnid=b.getId();
				final String name=(String)v.getTag();
				if(name=="addnew")
				{
					Intent i=new Intent(Mapplaces.this,Add_members.class);
			    	startActivity(i);
				}
				else
				{
				int blocked=checkifblocked(name);	
				AlertDialog.Builder builder = new AlertDialog.Builder(Mapplaces.this);
				if(blocked==1)
				{
					builder.setMessage("Do you want to unblock this user?");
					builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						  public void onClick(DialogInterface dialog, int id) {
							   unblockuser(name);
							  }
							});
				}
				else
				{
					builder.setMessage("Do you want to block this user?");
					builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						  public void onClick(DialogInterface dialog, int id) {
							   blockuser(name);
							  }
							});
				}
				
				
				builder.setNegativeButton("Change photo", new DialogInterface.OnClickListener() {
					  public void onClick(DialogInterface dialog, int id) {
						  imagepicker();
						  }
						});
				builder.show();
				}
			   
			}
		};
		@Override
		public int getCount() {
			//return dataObjects.length;
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			View retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem, null);
			//TextView title = (TextView) retval.findViewById(R.id.title);
		
			ImageButton img=(ImageButton)retval.findViewById(R.id.image);
			if(data.get(position).toString()!="Add new")
			{
				img.setBackgroundResource(R.drawable.circlemember);
				img.setTag(memberlist.get(position).toString());
			}
			else
			{
				img.setBackgroundResource(R.drawable.addnew);
				img.setTag("addnew");
			}
			
			img.setOnClickListener(imageButtonClicked);
			Button button = (Button) retval.findViewById(R.id.clickbutton);
           	
            button.setText(data.get(position).toString());
			return retval;
		}
		
	};

	public void show()
	{
		Toast.makeText(this, "hello", Toast.LENGTH_LONG).show();	
	}
	
	//code to load the map with police stations and hospitals asynchronously
	private class GetPlaces extends AsyncTask<Void, Void, ArrayList<Place>> {

		private ProgressDialog dialog;
		private Context context;
		private String places;
		private int flag=0;

		public GetPlaces(Context context, String places) {
			String temp;
			this.context = context;
			this.places = places;
			temp=this.places;
			if(temp.equals("hospital"))
			{
				flag=1;
			}
				
		}

		@Override
		protected void onPostExecute(ArrayList<Place> result) 
		{
			super.onPostExecute(result);
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			for (int i = 0; i < result.size(); i++) {
				if(flag==1)
				{
				mMap.addMarker(new MarkerOptions()
						.title(result.get(i).getName())
						.position(
								new LatLng(result.get(i).getLatitude(), result
										.get(i).getLongitude()))
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.hospitalpin))
						.snippet(result.get(i).getVicinity()));
				}
				else
				{
					mMap.addMarker(new MarkerOptions()
					.title(result.get(i).getName())
					.position(
							new LatLng(result.get(i).getLatitude(), result
									.get(i).getLongitude()))
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.policepin))
					.snippet(result.get(i).getVicinity()));
				}
			}
			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(new LatLng(result.get(0).getLatitude(), result
							.get(0).getLongitude())) // Sets the center of the map to
											// Mountain View
					.zoom(14) // Sets the zoom
					.tilt(30) // Sets the tilt of the camera to 30 degrees
					.build(); // Creates a CameraPosition from the builder
			//mMap.animateCamera(CameraUpdateFactory
				//	.newCameraPosition(cameraPosition));
			
			
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(context);
			dialog.setCancelable(false);
			dialog.setMessage("Loading..");
			dialog.isIndeterminate();
			dialog.show();
		}

		@Override
		protected ArrayList<Place> doInBackground(Void... arg0) {
			//android.os.Debug.waitForDebugger();
			PlacesService service = new PlacesService(
					"AIzaSyBXRbV8R2q0HxkIwHQggcJkXYxb1aUS2gc");
			ArrayList<Place> findPlaces = service.findPlaces(loc.getLatitude(), // 28.632808
					loc.getLongitude(), places); // 77.218276

			for (int i = 0; i < findPlaces.size(); i++) 
			{

				Place placeDetail = findPlaces.get(i);
			}
			return findPlaces;
		}

	}

	private void initCompo() {
		mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
				.getMap();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	//function to show circle members location
	 private void circlememberlocation(int id)
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
	                               	
	            
	            ResultSet rs=stmnt.executeQuery("Select c.mobileno,u.name,c.location_sharing from circle_details c inner join user_details u on c.mobileno=u.mobile where c.mobileno!='"+umobile+"' and c.circleid="+id+"");
	            while(rs.next())
	            {
	            	memberlist.add(rs.getString(1));
	            	membernamelist.add(rs.getString(2));
	            	locationsharinglist.add(rs.getString(3));
	            }
	            String t1,t2,t3;
	          
	            for(int i=0;i<memberlist.size();i++)
	            {
	            	t1=memberlist.get(i).toString();
	            	t2=membernamelist.get(i).toString();
	            	t3=locationsharinglist.get(i).toString();
	            	int t=Integer.parseInt(t3);
	                //dataObjects[i]=membernamelist.get(i).toString();
	            	if(t==1)
	            	{
	            	data.add(membernamelist.get(i).toString());
	            	ResultSet rst=stmnt.executeQuery("SELECT user_mobileno from blocked_user where user_mobileno='"+t1+"' and blocked_mobileno='"+umobile+"'");
	            	while(!rst.next())
		             {
	            		ResultSet rs1=stmnt.executeQuery("SELECT lat,lon FROM LOCATION_UPLOAD WHERE currentdate = (SELECT MAX(currentdate) FROM LOCATION_UPLOAD where user_mobileno='"+t1+"')");
	   	             while(rs1.next())
	   	             {
	   	            	 getmemberlocation(rs1.getString(1),rs1.getString(2),t2);
	   	             }
		             }
	            	
	            	}
	            	else
	            	{
	            		data.add(membernamelist.get(i).toString());
	            	}
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
	
	 //function to display location of circle members
	 private void getmemberlocation(String lat,String lon,String name)
	 {
		 double lati=Double.parseDouble(lat);
		 double longi=Double.parseDouble(lon);		 
		 memlocation=new LatLng(lati, longi);
		 mMap.addMarker(new MarkerOptions().position(memlocation).title(""+name+""));
		
	 }
	 
	//function to show my current location
	private void currentLocation() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		//String provider = locationManager
		//		.getBestProvider(new Criteria(), false);
		String provider=LocationManager.NETWORK_PROVIDER;
		Location location = locationManager.getLastKnownLocation(provider);
        
		if (location == null) {
			locationManager.requestLocationUpdates(provider, 0, 0, listener);
		} else {
			loc = location;
			lat=loc.getLatitude();
			lon=loc.getLongitude();
			
			mylocation = new LatLng(lat,lon);				
		    mMap.addMarker(new MarkerOptions().position(mylocation).title("Me").icon(BitmapDescriptorFactory
					.fromResource(R.drawable.map_icon)));
			
			CameraUpdate update = CameraUpdateFactory.newLatLngZoom(mylocation, 14);
			mMap.animateCamera(update);
			new GetPlaces(Mapplaces.this, places[4].toLowerCase().replace(
					"-", "_")).execute();
			new GetPlaces(Mapplaces.this, places[6].toLowerCase().replace(
					"-", "_")).execute();
			
			
			//data.clear();
		}

	}
	//code for getting the current location
	private LocationListener listener = new LocationListener() 
	{

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		@Override
		public void onProviderEnabled(String provider) {

		}

		@Override
		public void onProviderDisabled(String provider) {

		}

		@Override
		public void onLocationChanged(Location location) {
		
			loc = location;
			locationManager.removeUpdates(listener);
			locationManager=null;
		}
	};
	
	//function to populate the spinner with all the circle names in which the user is associated
	public void getcirclenames()
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
            uname=temp.get(0).toString();
            umobile=temp.get(1).toString();
            circleidlist.add("00");
            circlelist.add("Choose a circle");
            locationlist.add("1");
            int tempid;
            ResultSet rs=stmnt.executeQuery("select circleid,cname,location_sharing from circle_details where mobileno='"+umobile+"'");
            while(rs.next())
            {
                tempid=rs.getInt(1);
            	circleidlist.add(""+tempid);
            	circlelist.add(rs.getString(2));
            	locationlist.add(rs.getString(3));
            	
            
            }
            circlelist.add("Create new circle");
		conn.close();
	}
		
		 catch (Exception e) {
             e.printStackTrace();
          }
          finally 
          {
             if (stmnt != null) try { stmnt.close(); } catch(Exception e) {}
             if (conn != null) try { conn.close(); } catch(Exception e) {}
          }
		
	}
	
	@Override
	public void onBackPressed() 
	{
		
		Intent i=new Intent(Mapplaces.this,HomeActivity.class);
		startActivity(i);
	}
	//code for location sharing on/off
	public void onToggleClicked(View view) {
	    // Is the toggle on?
	    boolean on = ((ToggleButton) view).isChecked();
	    
	    if (on) {
	    	updatelocationsharingstatus("1");
	    	
	    } else {
	    	
	    	updatelocationsharingstatus("0");
	    
	      
	    }
	}
	//function to change location sharing status
	public void updatelocationsharingstatus(String status)
	{
		try {
			String driver="net.sourceforge.jtds.jdbc.Driver";
			Class.forName(driver).newInstance();
			 String connString="jdbc:jtds:sqlserver://110.232.255.114:1433/howzat;encrypt=false;user=sa;password=pass@123;";
			 String username="sa";
			 String password="pass@123";	
            conn=DriverManager.getConnection(connString,username,password);
            stmnt = conn.createStatement();
            String SQL="update circle_details set location_sharing='"+status+"' where mobileno='"+umobile+"' and circleid="+cid+"";
            stmnt.execute(SQL);         
		conn.close();
		}
		
		 catch (Exception e) {
             e.printStackTrace();
          }
          finally 
          {
             if (stmnt != null) try { stmnt.close(); } catch(Exception e) {}
             if (conn != null) try { conn.close(); } catch(Exception e) {}
          }
	}
	
	
	//function to block a user
		public void blockuser(String blocknum)
		{
			try {
				
				String driver="net.sourceforge.jtds.jdbc.Driver";
				Class.forName(driver).newInstance();
				 String connString="jdbc:jtds:sqlserver://110.232.255.114:1433/howzat;encrypt=false;user=sa;password=pass@123;";
				 String username="sa";
				 String password="pass@123";		
				 
	            conn=DriverManager.getConnection(connString,username,password);
	            stmnt = conn.createStatement();
	            String SQL="insert into blocked_user(user_mobileno,blocked_mobileno) values('"+umobile+"','"+blocknum+"')";
	            stmnt.execute(SQL);
	            
			conn.close();
		}
			
			 catch (Exception e) {
	             e.printStackTrace();
	          }
	          finally 
	          {
	             if (stmnt != null) try { stmnt.close(); } catch(Exception e) {}
	             if (conn != null) try { conn.close(); } catch(Exception e) {}
	          }
		}
	
		//function to unblock a user
				public void unblockuser(String blocknum)
				{
			try {
						
			            
						String driver="net.sourceforge.jtds.jdbc.Driver";
						Class.forName(driver).newInstance();
						 String connString="jdbc:jtds:sqlserver://110.232.255.114:1433/howzat;encrypt=false;user=sa;password=pass@123;";
						 String username="sa";
						 String password="pass@123";		
						 
			            conn=DriverManager.getConnection(connString,username,password);
			            stmnt = conn.createStatement();
			            String SQL="delete from blocked_user where user_mobileno='"+umobile+"' and blocked_mobileno='"+blocknum+"'";
			            stmnt.execute(SQL);
			            
					conn.close();
				}
					
					 catch (Exception e) {
			             e.printStackTrace();
			          }
			          finally 
			          {
			             if (stmnt != null) try { stmnt.close(); } catch(Exception e) {}
			             if (conn != null) try { conn.close(); } catch(Exception e) {}
			          }
				}
		
		
		//function to check if user is blocked
		
		public int checkifblocked(String block)
		{
			int flag=0;
			try
			{
			String driver="net.sourceforge.jtds.jdbc.Driver";
			Class.forName(driver).newInstance();
			 String connString="jdbc:jtds:sqlserver://110.232.255.114:1433/howzat;encrypt=false;user=sa;password=pass@123;";
			 String username="sa";
			 String password="pass@123";		
			 
            conn=DriverManager.getConnection(connString,username,password);
            stmnt = conn.createStatement();
            ResultSet rs=stmnt.executeQuery("select user_mobileno from blocked_user where user_mobileno='"+umobile+"' and blocked_mobileno='"+block+"'");
            while(rs.next())
            {
            	flag++;
            }
            
		conn.close();
	}
		
		 catch (Exception e) {
             e.printStackTrace();
          }
          finally 
          {
             if (stmnt != null) try { stmnt.close(); } catch(Exception e) {}
             if (conn != null) try { conn.close(); } catch(Exception e) {}
          }
			return flag;
		}
		
		//function to pick images for user
				private void imagepicker()
				{
					final CharSequence[] items = {"Choose from gallery","Cancel" };

					AlertDialog.Builder builder = new AlertDialog.Builder(Mapplaces.this);
					builder.setTitle("Choose");
					builder.setItems(items, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int item) {
							if (items[item].equals("Choose from gallery")) 
							{
								
								Intent intent = new Intent(
							    Intent.ACTION_PICK,
								android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
								//android.provider.MediaStore.Files
								//android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
								intent.setType("image/*");
								startActivityForResult(
								Intent.createChooser(intent, "Select File"),1);
							} 
							
							else if (items[item].equals("Cancel")) {
								dialog.dismiss();
							}
						}
					});
					builder.show();
				}

				//response to image picker
				 @Override
					protected void onActivityResult(int requestCode, int resultCode, Intent data) {
						super.onActivityResult(requestCode, resultCode, data);
						if (resultCode == RESULT_OK) {
							if (requestCode == 1) {
								/*Uri selectedImageUri = data.getData();						
								String tempPath = getPath(selectedImageUri, Mapplaces.this);
								db.updatephoto(tempPath);
								Bitmap bm;
								BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
								bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
								
								bm = Bitmap.createScaledBitmap(bm, 256, 256, true);
								f=new File(tempPath);
								filename=f.getName();
								imgprofile.setImageBitmap(bm);*/
								
							}
						}
					}
				 public String getPath(Uri uri, Activity activity) {
						String[] projection = { MediaColumns.DATA };
						Cursor cursor = activity
								.managedQuery(uri, projection, null, null, null);
						int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
						cursor.moveToFirst();
						return cursor.getString(column_index);
					}
	
	

}
