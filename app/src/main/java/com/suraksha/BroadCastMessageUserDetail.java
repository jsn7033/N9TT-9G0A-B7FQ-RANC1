package com.suraksha;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.AppHelper.RoundedImageView;
import com.CheckInternet.CheckInternet;
import com.webservice.Service1;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by RTH0102001 on 27-04-2016.
 */
public class BroadCastMessageUserDetail extends Activity{
    private  String MobileNo="mobile";
    private String perusername="UserName";
    private  String perfname="FirstName";

    private String perlname="LastName";
    private String peremailid="emailid";
    private  String pergender="gender";
    private String perdob="dob";
    private  String permaritalsts="maritalstatus";
    private  String perfatname="fathername";
    private String perfatmob="fathermobile";
    private String persspouname="spousename";
    private  String perspoumob="spousemobile";
    private  String peraddress="address";
    private  String percity="residence_city";
    private  String perstate="residence_state";
    private String imgUrl = "http://104.238.126.224/djnni%20dll/bills/";
    private String  BasesCoinBalance="coin_balance";
    private String badgesGold="gold_badges_count";
    private String adgeSilver="silver_badges_count";
    private String BageseBronze="bronze_badges_count";
    ImageView msguser_profile;
    TextView txtmsguserfullname,msguserAddress,msgusermobile,coinbalance,tvGoldCount,tvSilverCount,tvBronzeCount,
            msguserName,msguseremail,msgusergender,msguserdob,msgusermaritalstus,msguserfatherName,msguserfatherMobile,
    msguserspouseName,msguserspousemobile;
    //,userEmailId,msg_userGender,msg_userDob,msg_userMaritalStatus, msg_userFathername,msg_userFatherMobile,msg_userSpouseName,msg_userSpouseMobie

    Service1 ws;
    CheckInternet checknet;
    ProgressDialog progress;
    RelativeLayout badgeContainer;
    LinearLayout personallayout;
    Button btnpersonal,btnbages;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.broadcastmessage_userdetail);

        msguserName=(TextView)findViewById(R.id.msguserName);
        msguseremail=(TextView)findViewById(R.id.msguseremail);
        msgusergender=(TextView)findViewById(R.id.msgusergender);
        msguserdob=(TextView)findViewById(R.id.msguserdob);
        msgusermaritalstus=(TextView)findViewById(R.id.msgusermaritalstus);
        msguserfatherName=(TextView)findViewById(R.id.msguserfatherName);
        msguserfatherMobile=(TextView)findViewById(R.id.msguserfatherMobile);
        msguserspouseName=(TextView)findViewById(R.id.msguserspouseName);
        msguserspousemobile=(TextView)findViewById(R.id.msguserspousemobile);





        btnpersonal=(Button)findViewById(R.id.btnpersonal);
        btnbages=(Button)findViewById(R.id.btnbages);

        badgeContainer=(RelativeLayout)findViewById(R.id.badgeContainer);
        personallayout=(LinearLayout)findViewById(R.id.personallayout);

        msguser_profile=(ImageView)findViewById(R.id.msguser_profile);
        msguserAddress=(TextView)findViewById(R.id.msguserAddress);
        txtmsguserfullname=(TextView)findViewById(R.id.txtmsguserfullname);
        msgusermobile=(TextView)findViewById(R.id.msgusermobile);
        //bages

        coinbalance=(TextView)findViewById(R.id.ivCurrency);
        tvGoldCount=(TextView)findViewById(R.id.tvGoldCount);
        tvSilverCount=(TextView)findViewById(R.id.tvSilverCount);
        tvBronzeCount=(TextView)findViewById(R.id.tvBronzeCount);

        ws=new Service1();
        checknet=new CheckInternet(BroadCastMessageUserDetail.this);
        Intent dd=getIntent();
        String mmobilr=dd.getStringExtra("mobile");
        String userName=dd.getStringExtra("userName");
        BroadCastMessageUserDetail.this.setTitle(userName);
        if(checknet.isNetworkAvailable()) {
            getBroadcastMessageUserDetails dss = new getBroadcastMessageUserDetails(mmobilr);
            dss.execute();
        }else {
            Toast.makeText(BroadCastMessageUserDetail.this,"Internet Not Available",Toast.LENGTH_LONG).show();
        }

        btnbages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                badgeContainer.setVisibility(View.VISIBLE);
                personallayout.setVisibility(View.GONE);
            }
        });
        btnpersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personallayout.setVisibility(View.VISIBLE);
                badgeContainer.setVisibility(View.GONE);
            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/HomeActivity button
            case android.R.id.home:
                finish();


                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private class getBroadcastMessageUserDetails extends AsyncTask<Void, Void, Void> {

        JSONObject responce=null ;
        String value="" ,imgName="",MOB="";
        JSONArray ds=null;
        getBroadcastMessageUserDetails(String mob){
            this.MOB=mob;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(BroadCastMessageUserDetail.this);
            progress.setMessage("Loading...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try{
                responce=ws.FetchUserDetails(MOB);
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

            if(ds!=null) {
                if (ds.length() > 0) {
                    try {
                        for (int i = 0; i < ds.length(); i++) {
                            JSONObject obj;

                            obj = ds.getJSONObject(i);

                            msguserAddress.setText(obj.getString(peraddress)+" "+obj.getString(percity)+"\n "+obj.getString(perstate));
                            msguserName.setText(obj.getString(perusername));
                            msguseremail.setText(obj.getString(peremailid));
                            msgusergender.setText(obj.getString(pergender));
                            msguserdob.setText(obj.getString(perdob));
                            msgusermaritalstus.setText(obj.getString(permaritalsts));
                            msguserfatherName.setText(obj.getString(perfatname));
                            msguserfatherMobile.setText(obj.getString(perfatmob));
                            msguserspouseName.setText(obj.getString(persspouname));
                            msguserspousemobile.setText(obj.getString(perspoumob));


                            msgusermobile.setText(obj.getString(MobileNo));
                            txtmsguserfullname.setText(obj.getString(perfname)+"  "+obj.getString(perlname));

                            imgName=obj.getString("photo_ftp");
                            coinbalance.setText(obj.getString(BasesCoinBalance));
                            tvGoldCount.setText(obj.getString(badgesGold));
                            tvSilverCount.setText(obj.getString(adgeSilver));
                            tvBronzeCount.setText( obj.getString(BageseBronze));

                        }
                    } catch ( Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        Picasso.with(BroadCastMessageUserDetail.this).load(imgUrl +  imgName)
                                .placeholder(BroadCastMessageUserDetail.this.getResources().getDrawable(R.drawable.circle_img_bgr))
                                .transform(new RoundedImageView(50, 4))
                                .resize(100, 100)

                                .into(msguser_profile);
                    } catch (Exception e) {
                    }
                }
            }

            super.onPostExecute(result);
        }

    }





}
