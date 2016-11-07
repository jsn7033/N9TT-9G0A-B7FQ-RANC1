package com.suraksha;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.devsmart.android.ui.HorizontalListView;
//this activity is home activity wich is first call BroadcastFragment by default
public class HomeActivity extends Fragment {

	HorizontalListView hlList;
	GridAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.activity_home, container, false);
		hlList = (HorizontalListView) view.findViewById(R.id.rlTabs);
		adapter = new GridAdapter(getActivity(), 0);// custom adapter for set horizental list view
		hlList.setAdapter(adapter);
		selectFrag(0);
		hlList.setOnItemClickListener(item_Click);
		return view;
	}

	OnItemClickListener item_Click = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			adapter.setItemSelected(position);
			selectFrag(position);
		}
	};

	private class GridAdapter extends ArrayAdapter<String> {

		Context context;
		private int _position;
		int image[] = { R.drawable.ic_broadcaster, R.drawable.ic_search,
				R.drawable.ic_map };
		int image1[] = { R.drawable.ic_broadcaster, R.drawable.ic_search,
				R.drawable.ic_map };
		private String title[] = {
				getResources().getString(R.string.broadcast_text),
				getResources().getString(R.string.search_text),
				getResources().getString(R.string.classified_text) };

		public GridAdapter(Context context, int resource) {
			super(context, resource);
			// TODO Auto-generated constructor stub
			this.context = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return title.length;
		}

		protected void setItemSelected(int position) {
			_position = position;
			notifyDataSetChanged();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View v = convertView;
			ViewHolder holder = null;
			if (v == null) {
				holder = new ViewHolder();
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.profile_row, parent, false);
				holder.title = (TextView) v.findViewById(R.id.btn1);
				holder.img = (ImageView) v.findViewById(R.id.img1);
				holder.tab_purple_box = (View) v
						.findViewById(R.id.tabPurpleLine);
				holder.tab_white_box = (View) v.findViewById(R.id.tabWhiteLine);
				v.setTag(holder);
			} else {
				holder = (ViewHolder) v.getTag();
			}
			if (_position == position) {
				holder.title.setText(title[position]);
				holder.title.setTextColor(getResources().getColor(
						R.color.purple));
				holder.img.setImageResource(image1[position]);
				holder.tab_purple_box.setVisibility(View.VISIBLE);
				holder.tab_white_box.setVisibility(View.GONE);
			} else {
				holder.title.setText(title[position]);
				holder.img.setImageResource(image[position]);
				holder.tab_purple_box.setVisibility(View.GONE);
				holder.tab_white_box.setVisibility(View.VISIBLE);
			}
			return v;
		}
	}

	private static class ViewHolder {
		TextView title;
		View tab_purple_box, tab_white_box;
		ImageView img;
	}

	Fragment fr = null;

	public void selectFrag(int position) {
		if (position == 0) {
			fr = new BroadcastFragment(); // this fragmet showing the all broadcast message
		} else if (position == 1) {
			fr=null;
			fr = new SearchFragment();// searching its not working
		} else if (position == 2) {
			fr=null;
			fr = new ClassifiedFragment();// this fragmet work to search the ad post item
		}
		

		if (fr != null) {
			FragmentManager fm = getFragmentManager();
			fm.popBackStack();
			FragmentTransaction fragmentTransaction = fm.beginTransaction();
			fragmentTransaction.add(R.id.mainContainer, fr);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
		}
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}
}
