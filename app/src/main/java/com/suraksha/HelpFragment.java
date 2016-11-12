package com.suraksha;

import com.core.BaseFragment;
import com.squareup.picasso.Picasso;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class HelpFragment extends BaseFragment {

    Button btn;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_help);
//        //AdView ad=(AdView)findViewById(R.id.adView);
//        //	ad.loadAd(new AdRequest.Builder().build());
//
//        Toolbar mToolbar = loadToolbar("HelpFragment");
//        setSupportActionBar(mToolbar);
//        mToolbar.setLogo(R.drawable.howzaticon_);
//        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//        Button btn = (Button) findViewById(R.id.btnhelp);
//
//    }


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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mAppTitleCallback.title(getString(R.string.menu_help_text));
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_help, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBackGroundImage = (ImageView) view.findViewById(R.id.bg_image);
        btn = (Button) view.findViewById(R.id.btnhelp);
    }

    ImageView mBackGroundImage;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Picasso.with(getActivity()).load(R.drawable.bghome)
                .fit()
                .into(mBackGroundImage);

        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://www.howzatsos.com/usermanual"));
                startActivity(intent);

            }
        });
    }


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

}


//public class HelpFragment extends Fragment {
//
//    View view;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        view = inflater.inflate(R.layout.activity_help, container, false);
//
//                Button btn = (Button) view.findViewById(R.id.btnhelp);
//        btn.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_VIEW);
//                intent.addCategory(Intent.CATEGORY_BROWSABLE);
//                intent.setData(Uri.parse("http://www.howzatsos.com/usermanual"));
//                startActivity(intent);
//
//            }
//        });
//
//
//        return view;
//    }
//
//}
