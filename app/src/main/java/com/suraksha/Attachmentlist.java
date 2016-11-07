package com.suraksha;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Attachmentlist extends ArrayAdapter<String>{
private final Activity context;
private ArrayList<String> title=new ArrayList<String>();
private ArrayList<String> date=new ArrayList<String>();
private final int imageId;

public Attachmentlist(Activity context,ArrayList<String> adtitle, int imageId) {
super(context, R.layout.attachmentlist, adtitle);
this.context = context;
this.title = adtitle;
this.date= adtitle;
this.imageId = imageId;
}
@Override
public View getView(int position, View view, ViewGroup parent) {
LayoutInflater inflater = context.getLayoutInflater();
View rowView= inflater.inflate(R.layout.attachmentlist, null, true);
TextView txtTitle = (TextView) rowView.findViewById(R.id.txttitle);

ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
txtTitle.setText(title.get(position));

return rowView;
}
}
