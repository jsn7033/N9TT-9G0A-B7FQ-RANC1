package com.suraksha;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devsmart.android.ui.HorizontalListView;

public class BadgesFragment extends Fragment {

	RelativeLayout badgeLayout, healthLayout, personalLayout,
			ProfessionalLayout;

	com.devsmart.android.ui.HorizontalListView hlList;
	GridAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.activity_badges_fragment,
				container, false);

		badgeLayout = (RelativeLayout) view.findViewById(R.id.badgeContainer);
		healthLayout = (RelativeLayout) view.findViewById(R.id.healthContainer);
		ProfessionalLayout = (RelativeLayout) view
				.findViewById(R.id.professionalContainer);
		personalLayout = (RelativeLayout) view
				.findViewById(R.id.personalContainer);

		badgeLayout.setVisibility(View.VISIBLE);
		healthLayout.setVisibility(View.GONE);
		ProfessionalLayout.setVisibility(View.GONE);
		personalLayout.setVisibility(View.GONE);

		hlList = (HorizontalListView) view.findViewById(R.id.rlTabs);
		adapter = new GridAdapter(getActivity(), 0);
		hlList.setAdapter(adapter);
		hlList.setOnItemClickListener(item_Click);
		return view;
	}

	OnItemClickListener item_Click = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			switch (position) {
			case 0:
				badgeLayout.setVisibility(View.VISIBLE);
				healthLayout.setVisibility(View.GONE);
				ProfessionalLayout.setVisibility(View.GONE);
				personalLayout.setVisibility(View.GONE);
				adapter.setItemSelected(position);
				break;
			case 1:
				badgeLayout.setVisibility(View.GONE);
				healthLayout.setVisibility(View.GONE);
				ProfessionalLayout.setVisibility(View.GONE);
				personalLayout.setVisibility(View.VISIBLE);
				adapter.setItemSelected(position);
				break;
			case 2:
				badgeLayout.setVisibility(View.GONE);
				healthLayout.setVisibility(View.GONE);
				ProfessionalLayout.setVisibility(View.VISIBLE);
				personalLayout.setVisibility(View.GONE);
				adapter.setItemSelected(position);
				break;
			case 3:
				healthLayout.setVisibility(View.VISIBLE);
				badgeLayout.setVisibility(View.GONE);
				ProfessionalLayout.setVisibility(View.GONE);
				personalLayout.setVisibility(View.GONE);
				adapter.setItemSelected(position);
				break;
			}
		}
	};

	private class GridAdapter extends ArrayAdapter<String> {

		Context context;
		private int _position;
		int image[] = { R.drawable.ic_person1, R.drawable.ic_person1,
				R.drawable.ic_professional, R.drawable.ic_health };
		int image1[] = { R.drawable.ic_person, R.drawable.ic_person,
				R.drawable.ic_professional, R.drawable.ic_health };
		private String title[] = {
				getResources().getString(R.string.badges_title),
				getResources().getString(R.string.personal_title),
				getResources().getString(R.string.professional_text),
				getResources().getString(R.string.health_text) };

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
}
