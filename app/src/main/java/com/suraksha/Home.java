package com.suraksha;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.core.BaseFragment;
import com.suraksha.R.string;

// this is the navigation activity
public class Home extends BaseActivity {
    private ListView lvItems;
    private DrawerLayout mDrawerLayout;
    private android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    Toolbar mToolbar;
    FrameLayout container;

    /**
     * select position of drawer item
     */
    private int drawerselectedPos = 0;


    private boolean doubleBackToExitPressedOnce = false;


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else {


            if (doubleBackToExitPressedOnce) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Click Again to Exit", Toast.LENGTH_SHORT).show();


        }
    }

    MenuItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BroadcastCustomList.appContext = getApplicationContext();
        setContentView(R.layout.activity_main);
        container = (FrameLayout) findViewById(R.id.container);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToolbar = loadToolbar("HowZat");
        mToolbar.setLogo(R.drawable.howzaticon_);
        lvItems = (ListView) findViewById(R.id.lvMenu);
        adapter = new MenuItemAdapter(Home.this, 0);
        lvItems.setAdapter(adapter);


        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this, mDrawerLayout,
                mToolbar,
//                R.drawable.ic_navigation, // nav menu toggle icon
                R.string.app_name, // nav drawer open - description for
                // accessibility
                R.string.app_name // nav drawer close - description for

        ) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
//                getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>HowZat </font>"));
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getActionBar().setTitle("Howzat");
//                getSupportActionBar().setIcon(null);
//                getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>HowZat </font>"));
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            SelectItem(0);
        }
//        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>HowZat </font>"));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.purple)));

        lvItems.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                SelectItem(position);
                drawerselectedPos = position;
                adapter.notifyDataSetChanged();
                Log.d("tagd", "Clicked in a view");
                mDrawerLayout.closeDrawer(lvItems);
            }
        });


    }


    public void SelectItem(int possition) {

        android.support.v4.app.Fragment fr = null;
        switch (possition) {

            case 0:
                fr = new HomeActivity();// this is the home Activity and it is fragment class
                break;
            case 1:
                fr = new BadgesFragment();
               /* Intent iss = new Intent(Home.this, BadgesFragment.class);
                startActivity(iss);*/


                break;
            case 2:
                fr = new ViewOffers();

             /*   Intent i = new Intent(Home.this, ViewOffers.class);
                i.putExtra("offer", "");
                startActivity(i);*/
                break;
            case 3:
                fr = new Ad_form();
              /*  Intent ia = new Intent(Home.this, Ad_form.class);
                startActivity(ia);*/


                break;
            case 4:
                fr = new Settings();
                Intent ix = new Intent(Home.this, Settings.class);
                startActivity(ix);
                break;
            case 5:
                Intent ixxa = new Intent(Home.this, Share.class);
                startActivity(ixxa);
                break;
            case 6:
                Intent ixx = new Intent(Home.this, Help.class);
                startActivity(ixx);
                break;
            case 7:
                Intent xx = new Intent(Home.this, AboutUs.class);
                startActivity(xx);

                break;

            default:
                break;
        }
        if (fr != null) {
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.container, fr);
            fragmentTransaction.commit();
        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else if (item.getItemId() == R.id.menu_filter) {
            int actionBarHeight = getSupportActionBar().getHeight() * 2;
            popupWindow(actionBarHeight);
        }

        return super.onOptionsItemSelected(item);
    }


    private class MenuItemAdapter extends ArrayAdapter<String> {

        Context context;
        int Image[] = {R.drawable.ic_home, R.drawable.ic_person1,
                R.drawable.ic_dicount, R.drawable.ic_note, R.drawable.ic_setting,
                R.drawable.ic_networkmenu, R.drawable.ic_question,
                R.drawable.ic_operator};

        String Title[] = {getResources().getString(string.menu_home_text),
                getResources().getString(string.menu_profile_text),
                getResources().getString(string.menu_offer_text),
                getResources().getString(string.menu_post_text),
                getResources().getString(string.menu_setting_text),
                getResources().getString(string.menu_share_text),
                getResources().getString(string.menu_help_text),
                getResources().getString(string.menu_about_text)};

        public MenuItemAdapter(Context context, int resource) {
            super(context, resource);
            // TODO Auto-generated constructor stub
            this.context = context;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return Title.length;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder holder = null;
            View view = convertView;
            if (view == null) {
                LayoutInflater vi = LayoutInflater.from(getContext());
                view = vi.inflate(R.layout.menu_row, null);

                holder = new ViewHolder();
                holder.tvMenu = (TextView) view.findViewById(R.id.tvItem);
                holder.mSelectView = view.findViewById(R.id.selectedview);
                holder.mMainLayout = (RelativeLayout) view.findViewById(R.id.mainlayout);
                holder.ivMenu = (ImageView) view.findViewById(R.id.ivItem);
                view.setTag(holder);
            } else
                holder = (ViewHolder) view.getTag();
            holder.tvMenu.setText(Title[position]);
            holder.ivMenu.setImageResource(Image[position]);

            if (position == drawerselectedPos) {
                holder.mMainLayout.setBackgroundColor(Color.parseColor("#f4eefa"));
                holder.mSelectView.setVisibility(View.VISIBLE);
            } else {
                holder.mMainLayout.setBackgroundColor(Color.parseColor("#ffffff"));
                holder.mSelectView.setVisibility(View.GONE);
            }


            return view;
        }

        private class ViewHolder {
            private ImageView ivMenu;
            private TextView tvMenu;
            private View mSelectView;
            private RelativeLayout mMainLayout;
        }
    }


    public void popupWindow(int offsetY) {
        // // TODO Auto-generated method stub

        // Inflate the popup_layout.xml
        RelativeLayout viewGroup = (RelativeLayout) findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.filterscreen_pupp,
                viewGroup);
        Button mostusefull = (Button) layout.findViewById(R.id.btn2);
        Button recentpost = (Button) layout.findViewById(R.id.btn3);
        Button myusefullpost = (Button) layout.findViewById(R.id.btn4);
        Button mypost = (Button) layout.findViewById(R.id.btn5);

        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow();
        popup.setContentView(layout);
        popup.setWidth(LayoutParams.MATCH_PARENT);
        popup.setHeight(LayoutParams.MATCH_PARENT);
        popup.setFocusable(true);
        popup.setOutsideTouchable(true);

        // Clear the default translucent background
        popup.setBackgroundDrawable(new BitmapDrawable());


        popup.showAsDropDown(layout, 0, 0);
        mostusefull.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
                postcategory("Most Useful Post");

            }
        });

        recentpost.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
                postcategory("Recent Post");

            }
        });

        myusefullpost.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
                postcategory("My Useful Post");

            }
        });

        mypost.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
                postcategory("My Post");

            }
        });

    }

    public void postcategory(String pcat) {

        Fragment fragment = null;

        fragment = new BroadcastFragment();
        Bundle args = new Bundle();
        args.putString("postcat", pcat);

        fragment.setArguments(args);
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.mainContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}

