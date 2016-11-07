package com.suraksha;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class Ad_form extends Activity {

	Spinner adtype,adcategory,adproducttype,adusedproduct,adstate,adcity,adscope;
	EditText adcategoryname,adtitle,addescription,adkeyword,adaddress,adcontactno,ademailid,adwebsite;
	Button btnsubmitad;
	ImageButton btnattachad;
	String type,category,categoryname,producttype,productused,state,city,scope,postedby,title,description,keyword,address,contactno,emailid,website,image;
	ArrayList<String> listcity;
	ArrayAdapter<String> adcity_adapter,adusedproduct_adapter;
	int REQUEST_CAMERA=2,SELECT_FILE=1;
	Databaseclass db;
	Connection conn=null;
	Statement stmnt=null;
	String connString;
	String username;
	String password;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ad_form);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();		
 		StrictMode.setThreadPolicy(policy);
 		connString=this.getString(R.string.connstring);
 		username=this.getString(R.string.usr);
 		password=this.getString(R.string.pwd);
 		db=new Databaseclass(Ad_form.this);
		adtype=(Spinner)findViewById(R.id.spinneradtype);
		adcategory=(Spinner)findViewById(R.id.spinneradcategory);
		adcategoryname=(EditText)findViewById(R.id.txtcategory);
		adproducttype=(Spinner)findViewById(R.id.spinnerproduct);
		adusedproduct=(Spinner)findViewById(R.id.spinnerusedproduct);
		adtitle=(EditText)findViewById(R.id.txttitle);
		addescription=(EditText)findViewById(R.id.txtdescription);
		adaddress=(EditText)findViewById(R.id.txtaddress);
		adstate=(Spinner)findViewById(R.id.spinnerstate);
		adcity=(Spinner)findViewById(R.id.spinnercity);
		adcontactno=(EditText)findViewById(R.id.txtcontactno);
		ademailid=(EditText)findViewById(R.id.txtemailid);
		adwebsite=(EditText)findViewById(R.id.txtwebsite);
		adkeyword=(EditText)findViewById(R.id.txtkeyword);
		adscope=(Spinner)findViewById(R.id.spinnerscope);
		btnsubmitad=(Button)findViewById(R.id.btnsubmitad);
		btnattachad=(ImageButton)findViewById(R.id.btnattachad);
		listcity=new ArrayList<String>();
		ArrayAdapter<CharSequence> adtype_adapter = ArrayAdapter.createFromResource(this,R.array.ad_type, android.R.layout.simple_spinner_item);
		adtype_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adtype.setAdapter(adtype_adapter);
		ArrayAdapter<CharSequence> adcategory_adapter = ArrayAdapter.createFromResource(this,R.array.ad_category, android.R.layout.simple_spinner_item);
		adcategory_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adcategory.setAdapter(adcategory_adapter);
		ArrayAdapter<CharSequence> adproduct_adapter = ArrayAdapter.createFromResource(this,R.array.product_type, android.R.layout.simple_spinner_item);
		adproduct_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adproducttype.setAdapter(adproduct_adapter);
		ArrayAdapter<CharSequence> adscope_adapter = ArrayAdapter.createFromResource(this,R.array.ad_scope, android.R.layout.simple_spinner_item);
		adscope_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adscope.setAdapter(adscope_adapter);
		ArrayAdapter<CharSequence> adstate_adapter = ArrayAdapter.createFromResource(this,R.array.ad_state, android.R.layout.simple_spinner_item);
		adstate_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adstate.setAdapter(adstate_adapter);
		
		adproducttype.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if(arg2==1)
				{
				adusedproduct.setVisibility(View.VISIBLE);
				String[] select={"Choose","New","Used"};
				adusedproduct_adapter = new ArrayAdapter<String>(Ad_form.this,android.R.layout.simple_spinner_item,select);
				}
				else
				{
					adusedproduct.setVisibility(View.GONE);
					String[] select={"Choose"};
					adusedproduct_adapter = new ArrayAdapter<String>(Ad_form.this,android.R.layout.simple_spinner_item,select);
				}
				
				adusedproduct_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				adusedproduct.setAdapter(adusedproduct_adapter);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
				
			}
		});
		adstate.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if(arg2==0)
				{
					String[] select={"Select City"};
					adcity_adapter = new ArrayAdapter<String>(Ad_form.this,android.R.layout.simple_spinner_item,select);
				}
				else
				{
				listcity.clear();
				fetchcitynames(arg2);
				adcity_adapter = new ArrayAdapter<String>(Ad_form.this,android.R.layout.simple_spinner_item, listcity);
				}
				adcity_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				adcity.setAdapter(adcity_adapter);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
				
			}
		});
		btnsubmitad.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				insertaddetails();
				
			}
		});
		btnattachad.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				imagepicker();
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ad_form, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	//function to fetch city names from database
	public void fetchcitynames(int stateid)
	{
		try {
			String driver="net.sourceforge.jtds.jdbc.Driver";
			Class.forName(driver).newInstance();
			
            conn=DriverManager.getConnection(connString,username,password);
            stmnt = conn.createStatement();
            
            ResultSet rs=stmnt.executeQuery("select DistrictName from Districts where StateId="+stateid+"");
            while(rs.next())
            {
            	listcity.add(rs.getString(1));          	
            
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
	}
	
	//function to insert ad details in database
	
	public void insertaddetails()
	{
		try {
			
            type=adtype.getSelectedItem().toString();
            category=adcategory.getSelectedItem().toString();
            categoryname=adcategoryname.getText().toString();
            producttype=adproducttype.getSelectedItem().toString();
            productused=adusedproduct.getSelectedItem().toString();
            title=adtitle.getText().toString();
            description=addescription.getText().toString();
            keyword=adkeyword.getText().toString();
            address=adaddress.getText().toString();
            contactno=adcontactno.getText().toString();
            emailid=ademailid.getText().toString();
            website=adwebsite.getText().toString();
            state=adstate.getSelectedItem().toString();
            city=adcity.getSelectedItem().toString();
            scope=adscope.getSelectedItem().toString();
            
            if(TextUtils.isEmpty(categoryname))
			{
				adcategoryname.setError("This field is compulsary");				
			}
            else if(TextUtils.isEmpty(title))
            {
            	adtitle.setError("This field is compulsary");
            }
            else if(TextUtils.isEmpty(description))
            {
            	addescription.setError("This field is compulsary");
            }
            else if(TextUtils.isEmpty(keyword))
            {
            	adkeyword.setError("This field is compulsary");
            }
            else if(TextUtils.isEmpty(address))
            {
            	adaddress.setError("This field is compulsary");
            }
            else if(TextUtils.isEmpty(contactno))
            {
            	adcontactno.setError("This field is compulsary");
            }
            else if(TextUtils.isEmpty(emailid))
            {
            	ademailid.setError("This field is compulsary");
            }
            else if(TextUtils.isEmpty(website))
            {
            	adwebsite.setError("This field is compulsary");
            }
            else
            {
            	String driver="net.sourceforge.jtds.jdbc.Driver";
    			Class.forName(driver).newInstance();
    				
                conn=DriverManager.getConnection(connString,username,password);
                stmnt = conn.createStatement();
            ArrayList<String> t1=new ArrayList<String>();
            t1=db.getnamemobile();
            postedby=t1.get(1).toString();
            image="No image";         
            
            String SQL="INSERT INTO AD_DETAILS(ad_type,ad_category,ad_categoryname,ad_title,ad_productused,ad_titlename,ad_description,ad_keywords,ad_address,ad_contactno,ad_email,ad_website,ad_state,ad_city,ad_scope,ad_image,ad_postedby) values('"+type+"','"+category+"','"+categoryname+"','"+title+"','"+producttype+"','"+productused+"','"+description+"','"+keyword+"','"+address+"','"+contactno+"','"+emailid+"','"+website+"','"+state+"','"+city+"','"+scope+"','"+image+"','"+postedby+"')";
            stmnt.execute(SQL);        
            Toast.makeText(Ad_form.this, "Ad submitted successfully", Toast.LENGTH_LONG).show();
            Intent i=new Intent(Ad_form.this,Home.class);
            startActivity(i);
            
            conn.close();
            }
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
	
	//function to pick images for ads
	private void imagepicker() {
		final CharSequence[] items = {"Choose from gallery","Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(Ad_form.this);
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
							Intent.createChooser(intent, "Select File"),SELECT_FILE);
				} 
				
				else if(items[item].equals("Camera"))
				{
					selectimagefromcamera();
				}
				
				else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}
	
	//function to pick camera images
		 public void selectimagefromcamera()
		 {
			 Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
			 String currentDateandTime = sdf.format(new Date());
			 
				File f = new File(android.os.Environment
						.getExternalStorageDirectory(),currentDateandTime);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
				startActivityForResult(intent,REQUEST_CAMERA);

				
		 }
		 
		 //response to image picker
		 @Override
			protected void onActivityResult(int requestCode, int resultCode, Intent data) {
				super.onActivityResult(requestCode, resultCode, data);
				if (resultCode == RESULT_OK) {
					if (requestCode == REQUEST_CAMERA) {
						File f = new File(Environment.getExternalStorageDirectory()
								.toString());
						for (File temp : f.listFiles()) {
							if (temp.getName().equals("temp.jpg")) {
								f = temp;
								break;
							}
						}
						try {
							Bitmap bm;
							BitmapFactory.Options btmapOptions = new BitmapFactory.Options();

							bm = BitmapFactory.decodeFile(f.getAbsolutePath(),
									btmapOptions);

							// bm = Bitmap.createScaledBitmap(bm, 70, 70, true);
							btnattachad.setImageBitmap(bm);

							String path = android.os.Environment
									.getExternalStorageDirectory()
									+ File.separator
									+ "Phoenix" + File.separator + "default";
							f.delete();
							OutputStream fOut = null;
							File file = new File(path, String.valueOf(System
									.currentTimeMillis()) + ".jpg");
							try {
								fOut = new FileOutputStream(file);
								bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
								fOut.flush();
								fOut.close();
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							} catch (Exception e) {
								e.printStackTrace();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else if (requestCode == SELECT_FILE) {
						Uri selectedImageUri = data.getData();

						String tempPath = getPath(selectedImageUri, Ad_form.this);
						Bitmap bm;
						BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
						bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
						 bm = Bitmap.createScaledBitmap(bm, 70, 70, true);
						btnattachad.setImageBitmap(bm);
						
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
