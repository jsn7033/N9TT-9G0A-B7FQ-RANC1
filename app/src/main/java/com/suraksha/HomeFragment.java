package com.suraksha;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.core.BaseFragment;
import com.utilites.Constants;

//this activity is home activity wich is first call BroadcastFragment by default
public class HomeFragment extends BaseFragment {

    public TabLayout tabs;

    AppTitleCallback mAppTitleCallback;

    public interface AppTitleCallback {
        void title(String title);
    }


    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);

        if (context instanceof HomeActivity) {
            mAppTitleCallback = (AppTitleCallback) context;
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppTitleCallback.title(getString(R.string.title_activity_home));
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.activity_main, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
/*
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else*/
        if (item.getItemId() == R.id.menu_filter) {
            int actionBarHeight = ((HomeActivity) getActivity()).mToolbar.getHeight() * 2;
            popupWindow(actionBarHeight);
        }

        return super.onOptionsItemSelected(item);
    }

    public void popupWindow(int offsetY) {
        // // TODO Auto-generated method stub

        // Inflate the popup_layout.xml
        // RelativeLayout viewGroup = (RelativeLayout) findViewById(R.id.popup);
        // LayoutInflater layoutInflater = (LayoutInflater) getActivity.(Context.LAYOUT_INFLATER_SERVICE);
        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.filterscreen_pupp,
                null);
        Button mostusefull = (Button) layout.findViewById(R.id.btn2);
        Button recentpost = (Button) layout.findViewById(R.id.btn3);
        Button myusefullpost = (Button) layout.findViewById(R.id.btn4);
        Button mypost = (Button) layout.findViewById(R.id.btn5);
        Button cancel = (Button) layout.findViewById(R.id.cancelBtn);

        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow();
        popup.setContentView(layout);
        popup.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popup.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popup.setFocusable(true);
        popup.setOutsideTouchable(true);

        // Clear the default translucent background
        popup.setBackgroundDrawable(new BitmapDrawable());


        popup.showAsDropDown(layout, 0, 0);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();

            }
        });

        mostusefull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
                postcategory("Most Useful Post");

            }
        });

        recentpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
                postcategory("Recent Post");

            }
        });

        myusefullpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
                postcategory("My Useful Post");

            }
        });

        mypost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
                postcategory("My Post");

            }
        });

    }

    public void postcategory(String pcat) {

        Fragment fragment = null;
        tabs.setScrollPosition(0, 0f, true);
        ((HomeActivity) getActivity()).clearBackStack();
        fragment = new BroadcastFragment();
        Bundle args = new Bundle();
        args.putString("postcat", pcat);

        fragment.setArguments(args);
        android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
        //  fm.popBackStack();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.mainContainer, fragment);
        fragmentTransaction.commit();
    }


    int[] tabDrawables = {R.drawable.broadcast, R.drawable.search, R.drawable.map};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_home, container, false);
        tabs = (TabLayout) view.findViewById(R.id.tabs);
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabs.setScrollPosition(0, 0f, true);
//		hlList = (HorizontalListView) view.findViewById(R.id.rlTabs);
//		adapter = new GridAdapter(getActivity(), 0);// custom adapter for set horizental list view
//		hlList.setAdapter(adapter);
//		hlList.setOnItemClickListener(item_Click);


        setupTabIcons();

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

    public void refeshFragment(){

        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            if (fragment instanceof BroadcastFragment) {
                BroadcastFragment bf= (BroadcastFragment) fragment;
                bf.refreshFragment();
            }
            Log.e("onactivityresult", "activity");

        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            if (fragment instanceof BroadcastFragment) {
                if (requestCode == Constants.REQUEST_PERMISSION_FOR_LOCATION) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
            }
            Log.e("onactivityresult", "activity");

        }


    }

    private void setupTabIcons() {

        RelativeLayout mTabMainlayout = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        TextView mTabText = (TextView) mTabMainlayout.findViewById(R.id.tab_text);
        ImageView mImageview = (ImageView) mTabMainlayout.findViewById(R.id.tabimage);
        mTabText.setText("BROADCAST");

        tabs.addTab(tabs.newTab().setText("BROADCAST"));
        tabs.addTab(tabs.newTab().setText("SEARCH"));
        tabs.addTab(tabs.newTab().setText("CLASSIFIED"));

        mImageview.setImageResource(tabDrawables[0]);
        mImageview.setColorFilter(Color.parseColor("#8c8c8c"), PorterDuff.Mode.SRC_IN);
        tabs.getTabAt(0).setCustomView(mTabMainlayout);

        RelativeLayout mTabMainlayout1 = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        TextView mTabText1 = (TextView) mTabMainlayout1.findViewById(R.id.tab_text);
        ImageView mImageview1 = (ImageView) mTabMainlayout1.findViewById(R.id.tabimage);
        mTabText1.setText("SEARCH");
        mImageview1.setImageResource(tabDrawables[1]);
        mImageview1.setColorFilter(Color.parseColor("#8c8c8c"), PorterDuff.Mode.SRC_IN);
        tabs.getTabAt(1).setCustomView(mTabMainlayout1);

        RelativeLayout mTabMainlayout2 = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        TextView mTabText2 = (TextView) mTabMainlayout2.findViewById(R.id.tab_text);
        ImageView mImageview2 = (ImageView) mTabMainlayout2.findViewById(R.id.tabimage);
        mTabText2.setText("CLASSIFIED");
        mImageview2.setImageResource(tabDrawables[2]);
        mImageview2.setColorFilter(Color.parseColor("#8c8c8c"), PorterDuff.Mode.SRC_IN);
        tabs.getTabAt(2).setCustomView(mTabMainlayout2);
        ;


//        tabs.getTabAt(0).setIcon(tabDrawables[0]);
//        tabs.getTabAt(1).setIcon(tabDrawables[1]);
//        tabs.getTabAt(2).setIcon(tabDrawables[2]);

//        tabs.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#8c8c8c"), PorterDuff.Mode.SRC_IN);
//        tabs.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#8c8c8c"), PorterDuff.Mode.SRC_IN);
//        tabs.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#8c8c8c"), PorterDuff.Mode.SRC_IN);
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
        FragmentManager fm = getChildFragmentManager();
        fm.popBackStack();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.mainContainer, fragment);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
