package com.suraksha;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.SessionManager.SessionManager;
import com.webservice.Service1;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by RTH0102001 on 03-03-2016.
 */
public class RedemedHistory extends Activity{

    ListView lv;
    Service1 ws;
    SessionManager session;
    ProgressDialog progress;
    private String OFFER_NAME="OfferName";
    private String OFFER_PRICE="TotalCost";
    private String OFFER_DISCRIP="OfferDescription";
    private String OFFER_ID="OfferID";
    private String OFFER_DATE="PurchaseDate";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_offer_list);
        ws = new Service1();
        session = new SessionManager(RedemedHistory.this);
        lv = (ListView) findViewById(R.id.list_viewOffer);

        viewOfferList ds = new viewOfferList();
        ds.execute();

    }
    @Override
    public void onBackPressed(){

        finish();
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

    private class viewOfferList extends AsyncTask<Void, Void, Void> {

        JSONArray ds;

        JSONObject dss=null ;

        String value="" ;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(RedemedHistory.this);
            progress.setMessage("Loading...");
            progress.setCancelable(false);
            progress.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            try{

                dss=ws.FetchUserPurchasesHistory(session.GetMobileNo());
                Iterator iter = dss.keys();
                while(iter.hasNext()){

                    String key = (String)iter.next();
                    if(key.equals("Value")) {
                        value = dss.getString(key);
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
            progress.dismiss();//otp:I3XW13,MobileNo:


            ArrayList<HashMap<String, String>> List1 = new ArrayList<HashMap<String, String>>();

            if (ds != null) {

                if (ds.length() > 0) {

                    try {

                        for (int i = 0; i < ds.length(); i++) {
                            JSONObject obj;
                            obj = ds.getJSONObject(i);

                            String offrName =   obj.getString(OFFER_NAME );
                            String offer_disp =   obj.getString(OFFER_DISCRIP );
                            String offer_price =   obj.getString(OFFER_PRICE );
                            String offer_id =   obj.getString( OFFER_ID);
                            String offerdate =   obj.getString( OFFER_DATE);

                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put(OFFER_NAME, offrName);
                            map.put(OFFER_DISCRIP, offer_disp);
                            map.put(OFFER_PRICE, offer_price);
                            map.put(OFFER_ID, offer_id);
                            map.put(OFFER_DATE, offerdate);

                            List1.add(map);


                        }

                    }catch(Exception e){

                        e.printStackTrace();
                    }



                    SimpleAdapter adapter = new SimpleAdapter(RedemedHistory.this, List1, R.layout.redemed_history_list_item, new String[]
                            {OFFER_NAME, OFFER_DISCRIP,OFFER_PRICE,OFFER_ID,OFFER_DATE}, new int[]{R.id.txtoffer_name, R.id.txtdiscription,
                            R.id.txtcastcradit, R.id.txtofferid,R.id.txtofferdatee});
                    lv.setAdapter(adapter);
                }else{

                    Toast.makeText(RedemedHistory.this, "No History", Toast.LENGTH_LONG).show();
                }


            }else{

            }


            super.onPostExecute(result);
        }
    }



}
