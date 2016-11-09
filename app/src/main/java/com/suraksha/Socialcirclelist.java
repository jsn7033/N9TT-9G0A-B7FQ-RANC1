package com.suraksha;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class Socialcirclelist extends ArrayAdapter<String>{
private final Activity context;
private ArrayList<String> name=new ArrayList<String>();
private ArrayList<String> number=new ArrayList<String>();
private ArrayList<String> flag=new ArrayList<String>();


public Socialcirclelist(Activity context,ArrayList<String> aname,ArrayList<String> anumber,ArrayList<String> aflag) {
super(context, R.layout.socialcirclelist, aname);
this.context = context;
this.name = aname;
this.number= anumber;
this.flag=aflag;
}
@Override
public View getView(int position, View view, ViewGroup parent) {
LayoutInflater inflater = context.getLayoutInflater();
View rowView= inflater.inflate(R.layout.socialcirclelist, null, true);
TextView txtname = (TextView) rowView.findViewById(R.id.txtname);
TextView txtnumber = (TextView) rowView.findViewById(R.id.txtmobile);
TextView btninvite=(TextView) rowView.findViewById(R.id.btninvite);
TextView txtadded=(TextView) rowView.findViewById(R.id.txtadded);
txtname.setText(name.get(position));
txtnumber.setText(number.get(position));
if(flag.get(position).toString().equals("1"))
{
	btninvite.setText("added");
	btninvite.setEnabled(false);
	}
else
{
	btninvite.setText("invite");
	btninvite.setEnabled(true);
}
btninvite.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		
		shareIt();
	}
});

return rowView;
}
//function for sharing location on social media
	private void shareIt() {
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		String shareBody = "Hi, I am using HOWZAT SOS App, a 24x7 Free Emergency Assistance App. A true friend with you always. Click to Download: http://www.howzatsos.com/download";
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "HowZat SOS App");
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(Intent.createChooser(sharingIntent, "ShareFragment via"));
		}
}
