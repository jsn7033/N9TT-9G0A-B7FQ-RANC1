package com.suraksha;

import android.graphics.Bitmap;

public class OneComment {
	public boolean left;
	public String comment;
	public String sender;
	public Bitmap image;
	public String date;
	public int flag;
	public String caption;

	public OneComment(boolean left, String comment,String sender,String date) {
		super();
		this.left = left;
		this.comment = comment;
		this.sender=sender;
		this.date=date;
	}
	public OneComment(boolean left,Bitmap image,String caption,int flag,String sender,String date)
	{
		super();
		this.left = left;
		this.image = image;
		this.flag=flag;
		this.caption=caption;
		this.sender=sender;
		this.date=date;
	}

}