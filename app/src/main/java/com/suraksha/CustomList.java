package com.suraksha;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomList extends ArrayAdapter<String>{
private final Activity context;
private ArrayList<String> title;
private ArrayList<String> desc;
private int imageId;

public CustomList(Activity context,ArrayList<String> adtitle,ArrayList<String> addesc,int imageId) {
super(context, R.layout.adsearchlist, adtitle);
this.context = context;
this.title = adtitle;
this.desc= addesc;
this.imageId = imageId;
}
@Override
public View getView(int position, View view, ViewGroup parent) {
LayoutInflater inflater = context.getLayoutInflater();
View rowView= inflater.inflate(R.layout.adsearchlist, null, true);
TextView txtTitle = (TextView) rowView.findViewById(R.id.txttitle);
TextView txtDesc = (TextView) rowView.findViewById(R.id.txtdesc);
ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
txtTitle.setText(title.get(position).toString());
txtDesc.setText(desc.get(position).toString());
return rowView;
}
}
