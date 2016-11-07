package com.suraksha;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class FileChooserDialog implements OnItemClickListener,
		OnClickListener {
	
	private static final String TAG = "FileChooserDialog";
	
	private static final String PARENT = "..";
	
	private List<File> entries = new ArrayList<File>();
	
	private File currentDir;
	private Context context;
	private AlertDialog alertDialog;
	private ListView listView;
	private Result result;
	
	/**
	 * Constructor
	 * 
	 * @param context 
	 * @param result
	 * @param startDir source of start directory
	 * 
	 * @see Result
	 */
	public FileChooserDialog(Context context, Result result, String startDir) {
		this.context = context;
		this.result = result;

		if (startDir != null && !startDir.equals("")) {
			currentDir = new File(startDir);
		} else {
			currentDir = Environment.getExternalStorageDirectory();
		}

		listDirectories();
	
		/*ShowFileAdapter adapter = new ShowFileAdapter(R.layout.listitem_row_textview);
        
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.choosedir_title);
		builder.setAdapter(adapter, this);
		
		builder.setNegativeButton(this.context.getResources().getString(R.string.action_close),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		alertDialog = builder.create();
		listView = alertDialog.getListView();
		listView.setOnItemClickListener(this);
		alertDialog.show(); */
	}
	
	
	/**
	 * List all directories and files from the current directory
	 */
	private void listDirectories() {
		entries.clear();

		// Get files from directory
		File[] files = currentDir.listFiles();

		// Add the PARENT entry
		if (currentDir.getParent() != null) {
			entries.add(new File(PARENT));
		}

		if (files != null) {
			for (File file : files) {
				//filter hidden files
				if (!file.getName().startsWith(".")) {
					entries.add(file);
				}
			}
		}

		Collections.sort(entries, new Comparator<File>() {
			@SuppressLint("DefaultLocale")
			public int compare(File f1, File f2) {
				return f1.getName().toLowerCase().compareTo(f2.getName().toLowerCase());
			}
		});
	}

	

	@Override
	public void onItemClick(AdapterView<?> adapter, View list, int pos, long id) {
		/*
		Log.v(TAG, "[onItemClick] entering with " + currentDir);
		
		if (pos < 0 || pos >= entries.size()) {
			return;
		}

		final File file = entries.get(pos);
		if (file.isFile()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle(R.string.confirmation_title);
			builder.setMessage(context.getResources().getString(R.string.confirmation_message).replace("{FILE}", file.getName()));
			
			builder.setPositiveButton(context.getResources().getString(R.string.action_open), 
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Log.d(TAG, "[onItemClick] [onClick] opening file " + file.getName());
							alertDialog.cancel();
							result.onChooseDirectory(file);
						}
					});
			
			builder.setNegativeButton(context.getResources().getString(R.string.action_cancel),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});

			AlertDialog alertDialog = builder.create();
			alertDialog.show();
		} else if (file.getName().equals(PARENT)) {
			currentDir = currentDir.getParentFile();
			setRecursiveDirectory();
		} else {
			currentDir = entries.get(pos);
			setRecursiveDirectory();
		}
		*/
	}

	private void setRecursiveDirectory() {
		listDirectories();
		//ShowFileAdapter adapter = new ShowFileAdapter(R.layout.listitem_row_textview);
		//listView.setAdapter(adapter);
	}

	public void onClick(DialogInterface dialog, int which) {
		Log.v(TAG, "[onClick] entering with which[" + which + "]");
	}
	
	
	/**
	 * You must implement the Result interface for get the response
	 *
	 */
	public interface Result {
		void onChooseDirectory(File file);
	}
	

	/**
	 * Adapter to present the files and directories at the view
	 *
	 */
	/*
	public class ShowFileAdapter extends ArrayAdapter<File> {
		
		public ShowFileAdapter(int resourceID) {
			super(context, resourceID, entries);
		}

		// This function is called to show each view item
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			TextView item = (TextView) super.getView(position, convertView, parent);

			if (entries.get(position) == null || entries.get(position).getName().equals(PARENT)) {				
				item.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_parent_dir), null, null, null);
				item.setText(PARENT);
			} else {
				File file = entries.get(position);
                
				item.setText(file.getName());
				if (file.isDirectory()) {
					item.setCompoundDrawablePadding(6);
					item.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_directory), null, null, null);
				} else if (file.isFile()) {
					item.setCompoundDrawablePadding(6);
					item.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_file), null, null, null);
				} else {
					Log.w(TAG, "[getView] file not recognized [" + file + "]");
				}
			}
			return item;
		}
	}  */
}
