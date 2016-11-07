package com.AppHelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ResizeImage {

	String Filepath;
	Context context;

	public ResizeImage(String path) {
		this.Filepath = path;
		
		ImageResizer(decodeFile(new File(Filepath)));
	}
	
	public Bitmap decodeFile(File f) {
		try {

			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			final int REQUIRED_SIZE = 200;

			int scale = 1;
			while (o.outWidth / scale / 2 >= REQUIRED_SIZE
					&& o.outHeight / scale / 2 >= REQUIRED_SIZE)
				scale *= 2;

			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			Bitmap bit1 = BitmapFactory.decodeStream(new FileInputStream(f),
                    null, o2);
			return bit1;
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	public void ImageResizer(Bitmap bitmap) {
		String root = Environment.getExternalStorageDirectory().toString();
		File file = new File(Filepath);
		if (file.exists()) {
			file.delete();
			SaveResized(file, bitmap);

		} else {

			SaveResized(file, bitmap);
		}
	}

	private void SaveResized(File file, Bitmap bitmap) {
		try {
			FileOutputStream out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
