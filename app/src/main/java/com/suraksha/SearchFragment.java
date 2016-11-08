package com.suraksha;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.devsmart.android.ui.HorizontalListView;

// searching for the  ad form post  thi is not working
public class SearchFragment extends Fragment {

    HorizontalListView hlList;
    ListView listsearch;
    RelativeLayout searchLayout, broadcastLayout, classiffiedLayout;
    EditText etsearch;
    Button btnsearch;
    DiscussArrayAdapter chatadapter;
    String searchtext;
    Databaseclass db;
    String mobile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.activity_search_fragment,
                container, false);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        db = new Databaseclass(getActivity());
        ArrayList<String> temp = new ArrayList<String>();
        temp = db.getnamemobile();
        searchLayout = (RelativeLayout) view.findViewById(R.id.searchContainer);
        broadcastLayout = (RelativeLayout) view.findViewById(R.id.broadcastContainer);
        classiffiedLayout = (RelativeLayout) view.findViewById(R.id.classifiedContainer);
        listsearch = (ListView) view.findViewById(R.id.listsearch);
        etsearch = (EditText) view.findViewById(R.id.etSearch);
        btnsearch = (Button) view.findViewById(R.id.btnSend);

        //////////////////////////////////////////
        chatadapter = new DiscussArrayAdapter(getActivity(), R.layout.comment_row);
        listsearch.setAdapter(chatadapter);
        // chatadapter.add(new OneComment(true, "Hi! How may I assist you","Afzal",""));
        btnsearch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                searchtext = etsearch.getText().toString().trim();
                Time today = new Time(Time.getCurrentTimezone());
                today.setToNow();
                String str = today.monthDay + "/" + (today.month + 1) + "/" + today.year + " " + today.format("%k:%M");

                if (TextUtils.isEmpty(searchtext) || searchtext.trim().equals("")) {
                    etsearch.setError("Please type something");
                } else {
                    chatadapter.add(new OneComment(false, searchtext, "Afzal", str));
                    listsearch.setSelection(listsearch.getAdapter().getCount() - 1); // used to focus last message
                    etsearch.setText("");
                    //	new AsyncBotSearchDetails().execute(searchtext,mobile);
                    //
                }

            }
        });
        /////////////////////////////////////////
        return view;
    }


}
