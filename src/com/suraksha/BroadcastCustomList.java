package com.suraksha;

import java.util.ArrayList;

import org.json.JSONObject;

import com.webservice.Service1;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BroadcastCustomList extends ArrayAdapter<String>{
private final Activity context;
ArrayList<String> albid,aluname,alcity,albmedal,alsmedal,algmedal,albcontent,albimage,aluseful,aluseless,aldate;
private int imageId;
static Context appContext;
static Toast toast=null;
public BroadcastCustomList(Activity context,ArrayList<String> albid,ArrayList<String> aluname,ArrayList<String> alcity,ArrayList<String> albmedal,ArrayList<String> alsmedal,ArrayList<String> algmedal,ArrayList<String> albcontent,ArrayList<String> albimage,ArrayList<String> aluseful,ArrayList<String> aluseless,ArrayList<String> aldate) {
super(context, R.layout.broadcast_list_row, albid);
this.context = context;
this.albid = albid;
this.aluname= aluname;
this.alcity=alcity;
this.albmedal=albmedal;
this.alsmedal=alsmedal;
this.algmedal=algmedal;
this.albcontent=albcontent;
this.albimage=albimage;
this.aluseful=aluseful;
this.aluseless=aluseless;
this.aldate = aldate;
}
@Override
public View getView(int position, View view, ViewGroup parent) {
LayoutInflater inflater = context.getLayoutInflater();
View rowView= inflater.inflate(R.layout.broadcast_list_row, null, true);
TextView txtname = (TextView) rowView.findViewById(R.id.profileName);
TextView txtstate = (TextView) rowView.findViewById(R.id.profileState);
TextView txtbronze = (TextView) rowView.findViewById(R.id.tvBronze);
TextView txtsilver = (TextView) rowView.findViewById(R.id.tvSilver);
TextView txtgold = (TextView) rowView.findViewById(R.id.tvGold);
TextView txtcontent = (TextView) rowView.findViewById(R.id.tvImagePost);
TextView txtuseful = (TextView) rowView.findViewById(R.id.usefulCount);
TextView txtuseless = (TextView) rowView.findViewById(R.id.uselessCount);
TextView txtdate = (TextView) rowView.findViewById(R.id.tvTime);
ImageView img=(ImageView) rowView.findViewById(R.id.ivPostImage1);
txtname.setText(aluname.get(position).toString());
txtstate.setText(alcity.get(position).toString());
txtbronze.setText(albmedal.get(position).toString());
txtsilver.setText(alsmedal.get(position).toString());
txtgold.setText(algmedal.get(position).toString());
txtcontent.setText(albcontent.get(position).toString());
txtuseful.setText(aluseful.get(position).toString());
txtuseless.setText(aluseless.get(position).toString());
txtdate.setText(aldate.get(position).toString());
String broadcast_id=albid.get(position).toString();
Log.d("tagd", "Broadcast CustomList called");
if(albimage.get(position).toString()=="No image")
{
	img.setVisibility(view.VISIBLE);
}
else
{
	img.setVisibility(view.GONE);
}
final Context ctx=this.context;
Databaseclass db=new Databaseclass(ctx);
SQLiteDatabase dbase=db.getReadableDatabase();
Cursor c=dbase.rawQuery("SELECT * FROM user_details;", null);
	String mobile=null;
if(c.getCount()>0){
	c.moveToFirst();
	mobile=c.getString(c.getColumnIndex("mobile"));
	Log.d("tagd", mobile);
}else{
	Log.d("tagd", "doesn't have data");
}
final String phone=mobile;
final String broad_id=broadcast_id;
LinearLayout ll=(LinearLayout) rowView.findViewById(R.id.useful_capsule);
ll.setClickable(true);
ll.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
//		((TextView)(v.findViewById(R.id.usefulCount))).setText(Integer.toString(Integer.parseInt((((TextView)(v.findViewById(R.id.usefulCount))).getText().toString()))+1));
		TextView usefulCount=(TextView) v.findViewById(R.id.usefulCount);
		//did this above code on purpose, it basically increments the counter on a single line, didn't want to write more than one line.
		String sql="SELECT * FROM mark_details WHERE broadcast_id='"+broad_id+"'";
		Databaseclass dbHelper=new Databaseclass(ctx);
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		Cursor c=db.rawQuery(sql, null);
		if(c.getCount()==0){
			//do a asynctask
			usefulCount.setText(Integer.toString(Integer.parseInt((usefulCount.getText().toString())+1)));
			sql="INSERT INTO mark_details (broadcast_id) VALUES ('"+broad_id+"')";
			db.execSQL(sql);
			AsyncTask<TextView, String, String> asyncTask=new AsyncTask<TextView, String, String>(){

				@Override
				protected String doInBackground(TextView... params) {
					// TODO Auto-generated method stub
					Log.d("tagd", "Inside Asynctask");
					Service1 service=new Service1();
					try {
//						check if broad_id is already there in mark_details
						service.insertusefuluselessdetails(broad_id, "1", phone);
						Log.d("tagd", "Done with marking");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Log.d("tagd",e.toString());
					}
					return null;
				}
			};
			asyncTask.execute(usefulCount);
		}else{
			Toast.makeText(appContext, "You have already marked this post", Toast.LENGTH_SHORT).show();
		}
	}
});
LinearLayout ul=(LinearLayout) rowView.findViewById(R.id.useless_capsule);
ul.setClickable(true);
ul.setOnClickListener(new OnClickListener() {
	@Override
	public void onClick(View v) {
		//((TextView)(v.findViewById(R.id.uselessCount))).setText(Integer.toString(Integer.parseInt((((TextView)(v.findViewById(R.id.uselessCount))).getText().toString()))+1));
		TextView uselessCount=(TextView) v.findViewById(R.id.uselessCount);
		String sql="SELECT * FROM mark_details WHERE broadcast_id='"+broad_id+"'";
		Databaseclass dbHelper=new Databaseclass(ctx);
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		Cursor c=db.rawQuery(sql, null);
		if(c.getCount()==0){
			uselessCount.setText(Integer.toString(Integer.parseInt((uselessCount.getText().toString())+1)));
			sql="INSERT INTO mark_details (broadcast_id) VALUES ('"+broad_id+"')";
			db.execSQL(sql);
			AsyncTask<TextView, String, String> asyncTask=new AsyncTask<TextView, String, String>(){

				@Override
				protected String doInBackground(TextView... params) {
					// TODO Auto-generated method stub
					Log.d("tagd", "Inside Asynctask");
					Service1 service=new Service1();
					try {
//						check if broad_id is already there in mark_details
						service.insertusefuluselessdetails(broad_id, "0", phone);
						Log.d("tagd", "Done with marking");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Log.d("tagd",e.toString());
					}
					return null;
				}
			};
			asyncTask.execute(uselessCount);
		}else{
				Toast.makeText(appContext, "You have already marked this post", Toast.LENGTH_SHORT).show();		
		}
		//did this above code on purpose, it basically increments the counter on a single line, didn't want to write more than one line.

	}
});
return rowView;
}
}
