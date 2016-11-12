package com.suraksha;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.core.BaseFragment;
import com.squareup.picasso.Picasso;

/**
 * Created by RTH0102001 on 27-04-2016.
 */
//public class AboutUsFragment extends BaseFragment {
//
////    @Override
////    public void onBackPressed() {
////
////        finish();
////    }
////
////    @Override
////    protected void onCreate(Bundle savedInstnceState) {
////        super.onCreate(savedInstnceState);
////        setContentView(R.layout.about_us);
////        Toolbar mToolbar = loadToolbar("About Us");
////        mToolbar.setLogo(R.drawable.howzaticon_);
////        setSupportActionBar(mToolbar);
////        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                finish();
////            }
////        });
////    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            // Respond to the action bar's Up/HomeActivity button
//            case android.R.id.home:
//                finish();
//
//
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//}


public class AboutUsFragment extends BaseFragment {


    View view;

    ImageView mBackGroundImage;

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
        mAppTitleCallback.title(getString(R.string.menu_about_text));
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.about_us, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        mBackGroundImage= (ImageView) view.findViewById(R.id.background_img);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Picasso.with(getActivity()).load(R.drawable.preloader_back)
                .fit()
                .into(mBackGroundImage);

    }
}
