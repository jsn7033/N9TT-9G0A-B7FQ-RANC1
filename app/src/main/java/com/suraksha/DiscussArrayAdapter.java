package com.suraksha;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DiscussArrayAdapter extends ArrayAdapter<OneComment> {

    private TextView messageleft, messageright, sender, caption, txtdate;
    private List<OneComment> listmsg = new ArrayList<OneComment>();
    private ImageView ivleft, ivright;
    private RelativeLayout wrapper_left, wrapper_right;
    private Context context;
    HomeActivity h;
    public String path;
    public String imgcaption;
    public Object indextag;
    Databaseclass db;


    @Override
    public void add(OneComment object) {
        listmsg.add(object);
        super.add(object);
    }

    public DiscussArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
        db = new Databaseclass(context);

    }

    public int getCount() {
        return this.listmsg.size();

    }


    public OneComment getItem(int index) {
        return this.listmsg.get(index);

    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.comment_row, parent, false);
        }

        wrapper_left = (RelativeLayout) row.findViewById(R.id.leftSideContainer);
        wrapper_right = (RelativeLayout) row.findViewById(R.id.rightSideContainer);
        OneComment coment = getItem(position);
        ivleft = (ImageView) row.findViewById(R.id.senderImg);
        ivright = (ImageView) row.findViewById(R.id.receiverImg);
        messageleft = (TextView) row.findViewById(R.id.tvLeftSide);
        messageright = (TextView) row.findViewById(R.id.tvRightSide);
        //sender = (TextView) row.findViewById(R.id.sender);
        //caption=(TextView)row.findViewById(R.id.caption);
        //txtdate=(TextView)row.findViewById(R.id.datetime);
        if (coment.flag == 1) {
            /*RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
			        RelativeLayout.LayoutParams.WRAP_CONTENT,
			        RelativeLayout.LayoutParams.WRAP_CONTENT);
			rlp.setMargins(50, 10, 0, 0);
			RelativeLayout.LayoutParams rlp1 = new RelativeLayout.LayoutParams(
			        RelativeLayout.LayoutParams.WRAP_CONTENT,
			        RelativeLayout.LayoutParams.WRAP_CONTENT);
			rlp1.setMargins(0, 10, 50, 0);
			rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			rlp1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			    sender.setText(coment.sender);
			    txtdate.setText(coment.date);
				iv.setVisibility(View.VISIBLE);
			    iv.setImageBitmap(coment.image);
				caption.setText(coment.caption);			
			    caption.setVisibility(View.VISIBLE);			  
			    wrapper.setBackgroundResource(coment.left ? R.drawable.bubble_yellow : R.drawable.bubble_green);
			    wrapper.setLayoutParams(coment.left ?rlp1:rlp);*/
        } else {
            RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            rlp.setMargins(50, 10, 0, 0);
            RelativeLayout.LayoutParams rlp1 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            rlp1.setMargins(0, 10, 50, 0);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            rlp1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            if (coment.left) {
                messageright.setText(coment.comment);
                ivright.setVisibility(View.GONE);
                wrapper_right.setGravity(Gravity.RIGHT);
                wrapper_right.setLayoutParams(rlp);
                wrapper_left.setVisibility(View.GONE);
                messageleft.setVisibility(View.GONE);
                ivleft.setVisibility(View.VISIBLE);
            } else {
                messageleft.setText(coment.comment);
                ivleft.setVisibility(View.GONE);
                wrapper_left.setGravity(Gravity.RIGHT);
                wrapper_left.setLayoutParams(rlp1);
                wrapper_right.setVisibility(View.GONE);
                messageright.setVisibility(View.GONE);
                ivright.setVisibility(View.VISIBLE);

            }

            ivleft.setImageBitmap(coment.image);

            //sender.setText(coment.sender);
            //txtdate.setText(coment.date);

            //caption.setVisibility(View.GONE);
            //wrapper.setBackgroundResource(coment.left ? R.drawable.bubble_yellow : R.drawable.bubble_green);
            // wrapper.setBackgroundResource(coment.left ? R.drawable.bubble_yellow : R.drawable.bubble_green);

        }



		/*iv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) 
            {   
            	Object pos=position;
            	int p;
            	if(pos!=null)
            	{
            	p=Integer.valueOf(String.valueOf(pos))-1;
            	String filepath=h.alpath.get(p);
		    	File file = new File(filepath);
            	Intent intent = new Intent(Intent.ACTION_VIEW);
            	intent.setDataAndType(Uri.fromFile(file), "image/*");
            	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent); 
            	}
            }
        });*/
        return row;
    }

    public void openonclick() {
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public void show() {

    }


}