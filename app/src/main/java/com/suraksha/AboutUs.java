package com.suraksha;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.core.BaseFragment;

/**
 * Created by RTH0102001 on 27-04-2016.
 */
public class AboutUs extends BaseActivity {

    @Override
    public void onBackPressed() {

        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstnceState) {
        super.onCreate(savedInstnceState);
        setContentView(R.layout.about_us);
        Toolbar mToolbar = loadToolbar("About Us");
        mToolbar.setLogo(R.drawable.howzaticon_);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();


                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}


//public class AboutUs extends Fragment {
//
//
//    View view;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        view = inflater.inflate(R.layout.about_us, container, false);
//
//        return view;
//    }
//}
