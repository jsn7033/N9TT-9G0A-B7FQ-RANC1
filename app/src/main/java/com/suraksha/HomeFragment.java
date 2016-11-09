package com.suraksha;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.devsmart.android.ui.HorizontalListView;

//this activity is home activity wich is first call BroadcastFragment by default
public class HomeFragment extends Fragment {

    TabLayout tabs;

    AppTitleCallback mAppTitleCallback;


    public interface AppTitleCallback {
        void title(String title);
    }


    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);

        if (context instanceof Home) {
            mAppTitleCallback = (AppTitleCallback) context;
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppTitleCallback.title(getString(R.string.title_activity_home));
    }

    int[] tabDrawables = {};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_home, container, false);
        tabs = (TabLayout) view.findViewById(R.id.tabs);
//		hlList = (HorizontalListView) view.findViewById(R.id.rlTabs);
//		adapter = new GridAdapter(getActivity(), 0);// custom adapter for set horizental list view
//		hlList.setAdapter(adapter);
//		hlList.setOnItemClickListener(item_Click);


        tabs.addTab(tabs.newTab().setText("BROADCAST"), true);
        tabs.addTab(tabs.newTab().setText("SEARCH"));
        tabs.addTab(tabs.newTab().setText("CLASSIFIED"));

//		selectFrag(0);

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                Fragment fr = null;
                if (tab.getPosition() == 0) {
                    fr = new BroadcastFragment(); // this fragmet showing the all broadcast message
                } else if (tab.getPosition() == 1) {
                    fr = new SearchFragment();// searching its not working
                } else if (tab.getPosition() == 2) {
                    fr = new ClassifiedFragment();// this fragmet work to search the ad post item
                }

                if (fr != null) {
                    loadFragment(fr);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


        loadFragment(new BroadcastFragment());

        return view;
    }

    private void setupTabIcons() {
        tabs.getTabAt(0).setIcon(tabDrawables[0]);
        tabs.getTabAt(1).setIcon(tabDrawables[1]);
        tabs.getTabAt(2).setIcon(tabDrawables[2]);
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        getActivity().getMenuInflater().inflate(R.menu.activity_main, menu);
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//       if (item.getItemId() == R.id.menu_filter) {
//            int actionBarHeight = getActivity().getSupportActionBar().getHeight() * 2;
//            popupWindow(actionBarHeight);
//        }
//
//        return super.onOptionsItemSelected(item);
//    }




//	OnItemClickListener item_Click = new OnItemClickListener() {
//
//		@Override
//		public void onItemClick(AdapterView<?> parent, View view, int position,
//				long id) {
//			// TODO Auto-generated method stub
//			adapter.setItemSelected(position);
//			selectFrag(position);
//		}
//	};

//	private class GridAdapter extends ArrayAdapter<String> {
//
//		Context context;
//		private int _position;
//		int image[] = { R.drawable.ic_broadcaster, R.drawable.ic_search,
//				R.drawable.ic_map };
//		int image1[] = { R.drawable.ic_broadcaster, R.drawable.ic_search,
//				R.drawable.ic_map };
//		private String title[] = {
//				getResources().getString(R.string.broadcast_text),
//				getResources().getString(R.string.search_text),
//				getResources().getString(R.string.classified_text) };
//
//		public GridAdapter(Context context, int resource) {
//			super(context, resource);
//			// TODO Auto-generated constructor stub
//			this.context = context;
//		}
//
//		@Override
//		public int getCount() {
//			// TODO Auto-generated method stub
//			return title.length;
//		}
//
//		protected void setItemSelected(int position) {
//			_position = position;
//			notifyDataSetChanged();
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			// TODO Auto-generated method stub
//			View v = convertView;
//			ViewHolder holder = null;
//			if (v == null) {
//				holder = new ViewHolder();
//				LayoutInflater inflater = (LayoutInflater) context
//						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//				v = inflater.inflate(R.layout.profile_row, parent, false);
//				holder.title = (TextView) v.findViewById(R.id.btn1);
//				holder.img = (ImageView) v.findViewById(R.id.img1);
//				holder.tab_purple_box = (View) v
//						.findViewById(R.id.tabPurpleLine);
//				holder.tab_white_box = (View) v.findViewById(R.id.tabWhiteLine);
//				v.setTag(holder);
//			} else {
//				holder = (ViewHolder) v.getTag();
//			}
//			if (_position == position) {
//				holder.title.setText(title[position]);
//				holder.title.setTextColor(getResources().getColor(
//						R.color.purple));
//				holder.img.setImageResource(image1[position]);
//				holder.tab_purple_box.setVisibility(View.VISIBLE);
//				holder.tab_white_box.setVisibility(View.GONE);
//			} else {
//				holder.title.setText(title[position]);
//				holder.img.setImageResource(image[position]);
//				holder.tab_purple_box.setVisibility(View.GONE);
//				holder.tab_white_box.setVisibility(View.VISIBLE);
//			}
//			return v;
//		}
//	}
//
//	private static class ViewHolder {
//		TextView title;
//		View tab_purple_box, tab_white_box;
//		ImageView img;
//	}
//
//	Fragment fr = null;
//
//	public void selectFrag(int position) {
//		if (position == 0) {
//			fr = new BroadcastFragment(); // this fragmet showing the all broadcast message
//		} else if (position == 1) {
//			fr=null;
//			fr = new SearchFragment();// searching its not working
//		} else if (position == 2) {
//			fr=null;
//			fr = new ClassifiedFragment();// this fragmet work to search the ad post item
//		}
//
//
//		if (fr != null) {
//			FragmentManager fm = getFragmentManager();
//			fm.popBackStack();
//			FragmentTransaction fragmentTransaction = fm.beginTransaction();
//			fragmentTransaction.add(R.id.mainContainer, fr);
//			fragmentTransaction.addToBackStack(null);
//			fragmentTransaction.commit();
//		}
//	}
//
//	@Override
//	public void onDestroyView() {
//		super.onDestroyView();
//	}

    public void loadFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        fm.popBackStack();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.mainContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
