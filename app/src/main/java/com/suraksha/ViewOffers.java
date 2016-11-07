package com.suraksha;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.SessionManager.SessionManager;
import com.webservice.Service1;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by RTH0102001 on 02-03-2016.
 */
public class ViewOffers extends BaseActivity {

    ListView lv;
    Service1 ws;
    SessionManager session;
    ProgressDialog progress;
    private String OFFER_NAME = "OfferName";
    private String OFFER_PRICE = "TotalCost";
    private String OFFER_DISCRIP = "OfferDescription";
    private String OFFER_ID = "OfferID";
    private String coinbal = "0", offer = "";

    private String BasesCoinBalance = "coin_balance";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_offer_list);
        ws = new Service1();
        session = new SessionManager(ViewOffers.this);
        lv = (ListView) findViewById(R.id.list_viewOffer);


        Toolbar mToolbar = loadToolbar("View Offers");
        setSupportActionBar(mToolbar);
        mToolbar.setLogo(R.drawable.howzaticon_);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


//  is.putExtra("offer", "profile");
        try {
            Intent in = getIntent();
            offer = in.getStringExtra("offer");
            if (offer.equals("profile")) {
                coinbal = in.getStringExtra("coinbalance");

            } else {

            }

        } catch (Exception e) {

        }
        viewOfferList ds = new viewOfferList();
        ds.execute();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView offerid = (TextView) view.findViewById(R.id.txtofferid);
                TextView offername = (TextView) view.findViewById(R.id.txtoffer_name);
                TextView oferprice = (TextView) view.findViewById(R.id.txtcastcradit);
                TextView txtdiscription= (TextView) view.findViewById(R.id.txtdiscription);
//                perchasedialoag(offerid.getText().toString(), offername.getText().toString(), String.valueOf(2),txtdiscription.getText().toString());
                if (Integer.parseInt(coinbal) > Integer.parseInt(oferprice.getText().toString())) {
                    int restcoin = Integer.parseInt(coinbal) - Integer.parseInt(oferprice.getText().toString());
                    perchasedialoag(offerid.getText().toString(), offername.getText().toString(), String.valueOf(restcoin),txtdiscription.getText().toString());
                } else {

                    Toast.makeText(getApplicationContext(), "You don't have enough balance to purchase", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public void onBackPressed() {

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


    public void perchasedialoag(final String offerId, final String offerName, final String price,String discription) {
        final Dialog dialog = new Dialog(ViewOffers.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_are_you_purchase_offer);

        dialog.setTitle("Sure to purchase");
        dialog.setCancelable(false);
        final TextView textoffrName = (TextView) dialog.findViewById(R.id.textoffrName);
        final TextView descTxt= (TextView) dialog.findViewById(R.id.textoffrName_desc);
        textoffrName.setText(offerName);
        descTxt.setText(discription);
        final Button btncanceloffer = (Button) dialog.findViewById(R.id.btncanceloffer);
        final Button btnpuchaseoffer = (Button) dialog.findViewById(R.id.btnpuchaseoffer);

        btnpuchaseoffer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dialog.dismiss();
                purchaseOffer des = new purchaseOffer(offerId, price);
                des.execute();
            }

        });


        btncanceloffer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }

        });

        dialog.show();
    }


    private class getCoins extends AsyncTask<Void, Void, Void> {

        JSONArray ds;

        JSONObject dss = null;
        String value = "", OFFERID = "", PRICE = "";


        @Override
        protected Void doInBackground(Void... params) {
            try {

                dss = ws.FetchUserDetails(session.GetMobileNo());
                Iterator iter = dss.keys();
                while (iter.hasNext()) {

                    String key = (String) iter.next();
                    if (key.equals("Value")) {
                        value = dss.getString(key);

                        ds = new JSONArray(value);
                    }
                }
                if (ds != null) {

                    if (ds.length() > 0) {

                        try {

                            for (int i = 0; i < ds.length(); i++) {
                                JSONObject obj;
                                obj = ds.getJSONObject(i);

                                coinbal = obj.getString(BasesCoinBalance);
                                if (coinbal.equals("")) {
                                    coinbal = "0";
                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                }
            } catch (Exception e) {
                e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progress.dismiss();

            super.onPostExecute(result);
        }

    }


    private class purchaseOffer extends AsyncTask<Void, Void, Void> {

        JSONArray ds;

        JSONObject dss = null;
        String value = "", OFFERID = "", PRICE = "";

        purchaseOffer(String offerId, String price) {
            this.OFFERID = offerId;
            this.PRICE = price;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(ViewOffers.this);
            progress.setMessage("Loading...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                dss = ws.InsertUserOffers(session.GetMobileNo(), OFFERID);
                Iterator iter = dss.keys();
                while (iter.hasNext()) {

                    String key = (String) iter.next();
                    if (key.equals("Value")) {
                        value = dss.getString(key);

                    }
                }

            } catch (Exception e) {
                e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (value.equals("true")) {
                updateCoin ds = new updateCoin(PRICE);
                ds.execute();
            } else {
                progress.dismiss();
                Toast.makeText(getApplicationContext(), "Offer Not purchase", Toast.LENGTH_LONG).show();

            }
            super.onPostExecute(result);
        }
    }


    private class updateCoin extends AsyncTask<Void, Void, Void> {

        JSONObject dss = null;
        String value = "", RESTCOIN = "";

        updateCoin(String COIN) {
            this.RESTCOIN = COIN;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                dss = ws.UpdateCoinBalance(session.GetMobileNo(), RESTCOIN);

                Iterator iter = dss.keys();
                while (iter.hasNext()) {

                    String key = (String) iter.next();
                    if (key.equals("Value")) {
                        value = dss.getString(key);
                    }
                }
            } catch (Exception e) {
                e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progress.dismiss();//otp:I3XW13,MobileNo:
            if (value.equals("true")) {
                Toast.makeText(ViewOffers.this, "Offer Purchase Successfully", Toast.LENGTH_LONG).show();

                finish();
            }
            super.onPostExecute(result);
        }
    }


    private class viewOfferList extends AsyncTask<Void, Void, Void> {

        JSONArray ds;

        JSONObject dss = null;

        String value = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(ViewOffers.this);
            progress.setMessage("Loading...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                dss = ws.FetchOffers();
                Iterator iter = dss.keys();
                while (iter.hasNext()) {

                    String key = (String) iter.next();
                    if (key.equals("Value")) {
                        value = dss.getString(key);
                        ds = new JSONArray(value);
                    }
                }

            } catch (Exception e) {
                e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {


            ArrayList<HashMap<String, String>> List1 = new ArrayList<HashMap<String, String>>();

            if (ds != null) {

                if (ds.length() > 0) {

                    try {

                        for (int i = 0; i < ds.length(); i++) {
                            JSONObject obj;
                            obj = ds.getJSONObject(i);

                            String offrName = obj.getString(OFFER_NAME);
                            String offer_disp = obj.getString(OFFER_DISCRIP);
                            String offer_price = obj.getString(OFFER_PRICE);
                            String offer_id = obj.getString(OFFER_ID);

                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put(OFFER_NAME, offrName);
                            map.put(OFFER_DISCRIP, offer_disp);
                            map.put(OFFER_PRICE, offer_price);
                            map.put(OFFER_ID, offer_id);

                            List1.add(map);


                        }

                    } catch (Exception e) {

                        e.printStackTrace();
                    }


                    SimpleAdapter adapter = new SimpleAdapter(ViewOffers.this, List1, R.layout.view_offer_list_item, new String[]
                            {OFFER_NAME, OFFER_DISCRIP, OFFER_PRICE, OFFER_ID}, new int[]{R.id.txtoffer_name, R.id.txtdiscription,
                            R.id.txtcastcradit, R.id.txtofferid});
                    lv.setAdapter(adapter);
                } else {

                    Toast.makeText(ViewOffers.this, "No Offer", Toast.LENGTH_LONG).show();
                }


            } else {

            }

            if (!offer.equals("profile")) {
                getCoins dsa = new getCoins();
                dsa.execute();
            } else {
                progress.dismiss();
            }
            super.onPostExecute(result);
        }
    }


}
