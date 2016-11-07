package com.suraksha;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.SessionManager.SessionManager;

import com.webservice.Service1;

// searching the ad form data activity
public class ClassifiedFragment extends Fragment {

    ListView listad;
    Button btnsend;
    EditText etsearch;

    Databaseclass db;
    String mobile;
    ArrayList<String> listadid,listadtitle,listaddesc;

    ProgressDialog progress;
    Service1 ws;
    SessionManager sessionManager;
    private String CL_TYPE="ad_type";
    private String CL_CATEGORY="ad_category";
    private String CL_CAT_NAME="ad_categoryname";
    private String CL_TITLE="ad_title";
    private String CL_PRO_USED="ad_productused";
    private String CL_TITLE_NAME="ad_titlename";
    private String CL_DISCRIPTION="ad_description";
    private String CL_KEYWORD="ad_keywords";
    private String CL_ADDRESS="ad_address";
    private String CL_CONTACT_NUM="ad_contactno";
    private String CL_EMAIL_ID="ad_email";
    private String CL_WEBSITE="ad_website";
    private String CL_STATE="ad_state";
    private String CL_CITY="ad_city";
    private String CL_SCOPE="ad_scope";
    private String CL_IMAGE="ad_image";
    private String CL_POSTBY="ad_postedby";
    private String CL_DATE="ad_date";
    private String CL_STATUS="ad_status";

    TextView noresultfound;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.activity_classified_fragment,container, false);
        ws=new Service1(); // initilize web service
        sessionManager=new SessionManager(getActivity()); // initilise share prefrance class

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //AdView admob=(AdView)view.findViewById(R.id.adView);
        //admob.loadAd(new AdRequest.Builder().build());
        db=new Databaseclass(getActivity());
        mobile=sessionManager.GetMobileNo();
        btnsend=(Button) view.findViewById(R.id.btnSend);
        etsearch=(EditText) view.findViewById(R.id.etSearch);
        btnsend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String searchad=etsearch.getText().toString().trim();

                if(TextUtils.isEmpty(searchad) || searchad.trim().equals(""))
                {
                    etsearch.setError("Please enter a keyword to search");
                }
                else{
                    getAdDetail ds=new getAdDetail(searchad);// getting the searching data method
                    ds.execute();
                }
            }
        });
        listad = (ListView) view.findViewById(R.id.addsList);
        noresultfound=(TextView)view.findViewById(R.id.textView);
        listadid=new ArrayList<String>();
        listadtitle=new ArrayList<String>();
        listaddesc=new ArrayList<String>();

        listad.setOnItemClickListener(new AdapterView.OnItemClickListener() {// list view item click
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView title=(TextView)view.findViewById(R.id.tvTitle);
                TextView category=(TextView)view.findViewById(R.id.txtcatgory);
                TextView categoryname=(TextView)view.findViewById(R.id.txtcatName);
                TextView location=(TextView)view.findViewById(R.id.txtcity);
                TextView keywords=(TextView)view.findViewById(R.id.txtkeyword);
                TextView description=(TextView)view.findViewById(R.id.tvDescription);
                TextView address=(TextView)view.findViewById(R.id.txtaddress);
                TextView contact=(TextView)view.findViewById(R.id.txtcontactno);
                TextView email=(TextView)view.findViewById(R.id.txtemail);
                TextView website=(TextView)view.findViewById(R.id.txtwebsite);
                // call Adsearch_detail class for the view the ad form post detail
                Intent in=new Intent(getActivity(),Adsearch_details.class);
                in.putExtra("title",title.getText().toString());    // putting data to send  another class Adsearch
                in.putExtra("category",category.getText().toString());
                in.putExtra("categoryname",categoryname.getText().toString());
                in.putExtra("keywords",keywords.getText().toString());
                in.putExtra("description",description.getText().toString());
                in.putExtra("address",address.getText().toString());
                in.putExtra("contact",contact.getText().toString());
                in.putExtra("email",email.getText().toString());
                in.putExtra("website",website.getText().toString());
                in.putExtra("location",location.getText().toString());
                startActivity(in);
            }
        });
        return view;
    }


    private class getAdDetail extends AsyncTask<Void, Void, Void> {

        JSONObject responce=null ;
        String serch="",value="";
        getAdDetail(String sa){
            this.serch=sa;
        }


        JSONArray ds=null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(getActivity());
            progress.setMessage("Loading...");
            progress.setCancelable(false);
            progress.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            try{
                responce=     ws.SearchAdDetails(serch,mobile);// calling web service
                Iterator iter = responce.keys();
                while(iter.hasNext()){
                    String key = (String)iter.next();
                    if(key.equals("Value")) {
                        value = responce.getString(key);
                        ds=new JSONArray(value);
                    }
                }
            }catch(Exception e){
                e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            progress.dismiss();
            ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String, String>>();// intilisae the hash map list
            if(ds!=null) {
                if (ds.length() > 0) {
                    try {
                        noresultfound.setVisibility(View.GONE);
                        for (int i = 0; i < ds.length(); i++) {
                            JSONObject obj;

                            obj = ds.getJSONObject(i);
                            String  type  = obj.getString(CL_TYPE);
                            String    category = obj.getString(CL_CATEGORY);
                            String  categoryName  = obj.getString(CL_CAT_NAME);
                            String    title = obj.getString(CL_TITLE);
                            String  pro_used  = obj.getString(CL_PRO_USED);
                            String    title_name = obj.getString(CL_TITLE_NAME);//
                            String  discription  = obj.getString(CL_DISCRIPTION);//
                            String    keyword = obj.getString(CL_KEYWORD);
                            String  address  = obj.getString(CL_ADDRESS);
                            String    contact_num = obj.getString(CL_CONTACT_NUM);
                            String  email_id  = obj.getString(CL_EMAIL_ID);
                            String    website = obj.getString(CL_WEBSITE);
                            String  state  = obj.getString(CL_STATE);
                            String    city = obj.getString(CL_CITY);
                            String  scope  = obj.getString(CL_SCOPE);
                            String    image = obj.getString(CL_IMAGE);
                            String  post_by  = obj.getString(CL_POSTBY);
                            String    date = obj.getString(CL_DATE);
                            String  status  = obj.getString(CL_STATUS);

                            HashMap<String,String> map=new HashMap<String,String>();
                            map.put(CL_TYPE,type);
                            map.put(CL_CATEGORY,category);
                            map.put(CL_CAT_NAME,categoryName);
                            map.put(CL_TITLE,title);
                            map.put(CL_PRO_USED,pro_used);
                            map.put(CL_TITLE_NAME,title_name);
                            map.put(CL_DISCRIPTION,discription);
                            map.put(CL_KEYWORD,keyword);
                            map.put(CL_ADDRESS,address);
                            map.put(CL_CONTACT_NUM,contact_num);
                            map.put(CL_EMAIL_ID,email_id);
                            map.put(CL_WEBSITE,website);
                            map.put(CL_STATE,state);
                            map.put(CL_CITY,city);
                            map.put(CL_SCOPE,scope);
                            map.put(CL_IMAGE,image);

                            map.put(CL_POSTBY,post_by);
                            map.put(CL_DATE,date);
                            map.put(CL_STATUS,status);
                            list.add(map);


                        }

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
// setting data in simple adapter
                    SimpleAdapter adapter=new SimpleAdapter(getActivity(),list,R.layout.adds_row,new String[]{CL_TITLE_NAME,
                            CL_DISCRIPTION,CL_TYPE,CL_CATEGORY,CL_CAT_NAME,CL_TITLE,CL_PRO_USED,CL_KEYWORD,CL_ADDRESS,
                            CL_CONTACT_NUM,CL_EMAIL_ID,CL_WEBSITE,CL_STATE,CL_CITY,CL_SCOPE,CL_IMAGE,CL_POSTBY,CL_DATE,CL_STATUS},
                            new int[]{R.id.tvTitle,R.id.tvDescription,R.id.txttype,R.id.txtcatgory,R.id.txtcatName,
                                    R.id.txtTitle,R.id.txtproductuse,R.id.txtkeyword,R.id.txtaddress,R.id.txtcontactno,R.id.txtemail,
                                    R.id.txtwebsite,R.id.txtstate,R.id.txtcity,R.id.txtscope,R.id.txtimgName,R.id.txtpostBy,
                                    R.id.txtdate,R.id.txtstatus});
                    listad.setAdapter(adapter);

                }else{
                    listad.setVisibility(View.GONE);//  in case data is not avalable
                    noresultfound.setVisibility(View.VISIBLE);
                }
            }

            super.onPostExecute(result);
        }

    }
}