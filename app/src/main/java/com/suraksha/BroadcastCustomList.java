package com.suraksha;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import com.AppHelper.RoundedImageView;
import com.SessionManager.SessionManager;
import com.webservice.Service1;

import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class BroadcastCustomList extends ArrayAdapter<String> {

    private String likesus = "";
    private String USER_NAME = "UserName";
    private String COMMENT = "Comment";
    private String imgUrl = "http://104.238.126.224/djnni%20dll/bills/";

    ProgressDialog progress;
    Service1 ws;
    SessionManager session;
    private final Activity context;
    int checkall = 0;
    ListView lv;
    Dialog dialog;
    ArrayList<String> albid, aluname, alcity, albmedal, alsmedal, algmedal, albcontent, albimage, aluseful, aluseless, aldate, postimage, likeStatus, msgMobile;

    static Context appContext;
    static Toast toast = null;

    public BroadcastCustomList(Activity context, ArrayList<String> albid, ArrayList<String> aluname,
                               ArrayList<String> alcity, ArrayList<String> albmedal, ArrayList<String> alsmedal,
                               ArrayList<String> algmedal, ArrayList<String> albcontent, ArrayList<String> albimage,
                               ArrayList<String> aluseful, ArrayList<String> aluseless, ArrayList<String> aldate, ArrayList<String> postimage,
                               ArrayList<String> likeStatus, ArrayList<String> msgMobilea) {
        super(context, R.layout.broadcast_list_row, albid);
        this.context = context;
        this.albid = albid;
        this.aluname = aluname;
        this.alcity = alcity;
        this.albmedal = albmedal;
        this.alsmedal = alsmedal;
        this.algmedal = algmedal;
        this.albcontent = albcontent;
        this.albimage = albimage;
        this.aluseful = aluseful;
        this.aluseless = aluseless;
        this.aldate = aldate;
        this.postimage = postimage;
        this.likeStatus = likeStatus;
        this.msgMobile = msgMobilea;
    }



    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.broadcast_list_row, null, true);










        final TextView txtname = (TextView) rowView.findViewById(R.id.profileName);
        final TextView txtstate = (TextView) rowView.findViewById(R.id.profileState);
        TextView txtbronze = (TextView) rowView.findViewById(R.id.tvBronze);
        TextView txtsilver = (TextView) rowView.findViewById(R.id.tvSilver);
        TextView txtgold = (TextView) rowView.findViewById(R.id.tvGold);
        TextView txtcontent = (TextView) rowView.findViewById(R.id.tvImagePost);
        final TextView txtuseful = (TextView) rowView.findViewById(R.id.usefulCount);
        final TextView txtuseless = (TextView) rowView.findViewById(R.id.uselessCount);
        TextView txtdate = (TextView) rowView.findViewById(R.id.tvTime);
        ImageView imgprofile = (ImageView) rowView.findViewById(R.id.profileImg);
        final ImageView postimageview = (ImageView) rowView.findViewById(R.id.ivpostimage);
        TextView mUseFulcountTxt = (TextView) rowView.findViewById(R.id.useful_count);
        TextView mUserLesscountTxt = (TextView) rowView.findViewById(R.id.uselee_count);
        View mUserFullView = rowView.findViewById(R.id.userfullsaperator);
        View mUserLessView = rowView.findViewById(R.id.userlesssaperator);
        LinearLayout mUseFullMainLayout = (LinearLayout) rowView.findViewById(R.id.useful_layout);
        LinearLayout mUseLessMainLayout = (LinearLayout) rowView.findViewById(R.id.useless_layout);
        ImageView mTick = (ImageView) rowView.findViewById(R.id.tick);
        ImageView mCross = (ImageView) rowView.findViewById(R.id.cross);


        final TextView documentText = (TextView) rowView.findViewById(R.id.tvdocument);
        ImageView docDownload = (ImageView) rowView.findViewById(R.id.ivPostImage1);
        ImageButton imageviecomment = (ImageButton) rowView.findViewById(R.id.imageviecomment);

        final TextView usefulText = (TextView) rowView.findViewById(R.id.usefulText);
        final TextView uselessText = (TextView) rowView.findViewById(R.id.uselessText);











        txtname.setText(aluname.get(position).toString());
        txtstate.setText(alcity.get(position).toString());
        txtbronze.setText(albmedal.get(position).toString());
        txtsilver.setText(alsmedal.get(position).toString());
        txtgold.setText(algmedal.get(position).toString());
        txtcontent.setText(albcontent.get(position).toString());
        txtuseful.setText(aluseful.get(position).toString());
        txtuseless.setText(aluseless.get(position).toString());
        txtdate.setText(aldate.get(position).toString());
        String broadcast_id = albid.get(position).toString();
        likesus = likeStatus.get(position).toString();





        String fileName = postimage.get(position).toString();
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png")) {

            documentText.setText(postimage.get(position).toString());
            try {
                Picasso.with(context).load(imgUrl + postimage.get(position).toString())
                        .placeholder(context.getResources().getDrawable(R.drawable.circle_img_bgr))
                        .into(postimageview);
            } catch (Exception e) {

            }

        } else if (!fileName.equals("")) {
            postimageview.setVisibility(View.GONE);
            LinearLayout viewdocName = (LinearLayout) rowView.findViewById(R.id.llGallery);
            viewdocName.setVisibility(View.VISIBLE);
            documentText.setText(postimage.get(position).toString());
        } else {
            postimageview.setVisibility(View.GONE);
            LinearLayout viewdocName = (LinearLayout) rowView.findViewById(R.id.llGallery);
            viewdocName.setVisibility(View.GONE);
        }

        try {
            Picasso.with(context).load(imgUrl + albimage.get(position).toString())
                    .placeholder(context.getResources().getDrawable(R.drawable.circle_img_bgr))
                    .transform(new RoundedImageView(50, 4))
                    .resize(100, 100)
                    .centerCrop()
                    .into(imgprofile);
        } catch (Exception e) {

        }

        Log.d("tagd", "Broadcast CustomList called");

        final Context ctx = this.context;
        session = new SessionManager(ctx);
        ArrayList<String> likess = likeStatus;

        final String broad_id = broadcast_id;
        LinearLayout ll = (LinearLayout) rowView.findViewById(R.id.useful_capsule);
        ll.setClickable(true);
        ll.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                int idd = position;

                String likesttus = likeStatus.get(idd).toString();

                if (likesttus.equals("0")) {
                    sendComment(broad_id, "1");
                    likeStatus.set(idd, "1");

                    txtuseful.setTextColor(context.getResources().getColor(R.color.blue));
                    ((TextView) (v.findViewById(R.id.usefulCount))).setText(Integer.toString(Integer.parseInt((((TextView) (v.findViewById(R.id.usefulCount))).getText().toString())) + 1));
                } else {
                    Toast.makeText(context, "All ready click", Toast.LENGTH_LONG).show();
                }

            }
        });

        final LinearLayout ul = (LinearLayout) rowView.findViewById(R.id.useless_capsule);
        ul.setClickable(true);
        ul.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int idd = position;

                String likesttus = likeStatus.get(idd).toString();

                if (likesttus.equals("0")) {
                    sendComment(broad_id, "2");
                    likeStatus.set(idd, "2");

                    txtuseless.setTextColor(context.getResources().getColor(R.color.blue));
                    ((TextView) (v.findViewById(R.id.uselessCount))).setText(Integer.toString(Integer.parseInt((((TextView) (v.findViewById(R.id.uselessCount))).getText().toString())) + 1));


                } else {
                    Toast.makeText(context, "All ready click", Toast.LENGTH_LONG).show();
                }
            }
        });


        if (likesus.equals("1")) {


            txtuseful.setTextColor(context.getResources().getColor(R.color.blue));

        } else if (likesus.equals("2")) {


            txtuseless.setTextColor(context.getResources().getColor(R.color.blue));
        }
        docDownload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadDoc bd = new downloadDoc(documentText.getText().toString());
                bd.execute();
            }
        });
        postimageview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewSaveImage(documentText.getText().toString(), postimageview.getDrawable(), txtname.getText().toString());
            }
        });
        imageviecomment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewCommentList ds = new viewCommentList(broad_id, txtname.getText().toString(), txtstate.getText().toString());
                ds.execute();
            }
        });

        txtname.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ds = new Intent(context, BroadCastMessageUserDetail.class);
                ds.putExtra("mobile", msgMobile.get(position).toString());
                ds.putExtra("userName", txtname.getText().toString());
                context.startActivity(ds);
            }
        });

        return rowView;
    }


    public void sendComment(final String cid, final String hflag) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_usefull_useless);
        dialog.setTitle("Comment");
        dialog.setCancelable(false);
        final EditText Textcomment = (EditText) dialog.findViewById(R.id.editTextcomment);


        final Button buttonskip = (Button) dialog.findViewById(R.id.buttonskip);
        final Button buttonSend = (Button) dialog.findViewById(R.id.buttonSend);

        buttonSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dialog.dismiss();
                sendBdroicastmessage ds = new sendBdroicastmessage(cid, hflag, Textcomment.getText().toString());
                ds.execute();
            }

        });


        buttonskip.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sendBdroicastmessage ds = new sendBdroicastmessage(cid, hflag, Textcomment.getText().toString());
                ds.execute();
            }

        });

        dialog.show();
    }


    private class viewCommentList extends AsyncTask<Void, Void, Void> {

        JSONArray ds;

        JSONObject dss = null;

        String value = "", ID = "", PRO_NAME = "", CONTENT = "";

        viewCommentList(String id, String hflag, String comment) {
            this.ID = id;
            this.PRO_NAME = hflag;
            this.CONTENT = comment;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(context);
            progress.setMessage("Loading...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                ws = new Service1();
                dss = ws.FetchComments(ID);
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
            progress.dismiss();//otp:I3XW13,MobileNo:


            ArrayList<HashMap<String, String>> List1 = new ArrayList<HashMap<String, String>>();

            if (ds != null) {

                if (ds.length() > 0) {
                    viewdialodcomment();
                    try {

                        for (int i = 0; i < ds.length(); i++) {
                            JSONObject obj;
                            obj = ds.getJSONObject(i);

                            String useNamee = obj.getString("UserName");
                            if (!useNamee.equals("")) {
                                String comments = obj.getString("Comment");
                                if (!comments.equals("")) {

                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put(USER_NAME, useNamee);
                                    map.put(COMMENT, comments);

                                    List1.add(map);
                                }
                            }

                        }

                    } catch (Exception e) {

                        e.printStackTrace();
                    }


                    if (List1.size() > 0) {
                        SimpleAdapter adapter = new SimpleAdapter(context, List1, R.layout.view_comment_list_item, new String[]
                                {USER_NAME, COMMENT}, new int[]{R.id.textuserName, R.id.txtcomment});
                        lv.setAdapter(adapter);
                    } else {
                        dialog.dismiss();
                        Toast.makeText(context, "No Comment", Toast.LENGTH_LONG).show();
                    }

                } else {

                    Toast.makeText(context, "No Comment", Toast.LENGTH_LONG).show();
                }
            } else {

            }


            super.onPostExecute(result);
        }
    }


    public void viewdialodcomment() {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.view_comment_list);
        dialog.setTitle("All Comment");
        lv = (ListView) dialog.findViewById(R.id.lvcomment);

        final ImageView imgView = (ImageView) dialog.findViewById(R.id.imageViewpostmsg);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    public void viewSaveImage(final String imageName, final Drawable image, String uname) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.view_save_image);
        dialog.setTitle(uname);

        final ImageView imgView = (ImageView) dialog.findViewById(R.id.imageViewpostmsg);


        final Button buttonskip = (Button) dialog.findViewById(R.id.btnimgcancel);
        final Button btnsaveimg = (Button) dialog.findViewById(R.id.btnsaveimg);

        imgView.setImageDrawable(image);
        btnsaveimg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dialog.dismiss();
                downloadDoc bd = new downloadDoc(imageName);
                bd.execute();
            }

        });


        buttonskip.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }

        });

        dialog.show();
    }


    private class sendBdroicastmessage extends AsyncTask<Void, Void, Void> {
        ArrayList<HashMap<String, String>> resList = new ArrayList<HashMap<String, String>>();
        JSONArray ds;

        JSONObject dss = null;

        String value = "", ID = "", HFLAG = "", COMMENT = "";

        sendBdroicastmessage(String id, String hflag, String comment) {
            this.ID = id;
            this.HFLAG = hflag;
            this.COMMENT = comment;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(context);
            progress.setMessage("Sending...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                ws = new Service1();//(String mobile,String broadcastID,String hitFlag,String comment)
                dss = ws.UpdateBroadCastHit(session.GetMobileNo(), ID, HFLAG, COMMENT);
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
            progress.dismiss();//otp:I3XW13,MobileNo:
            Toast.makeText(context, "comment sent", Toast.LENGTH_LONG).show();
            checkall = 0;
            if (HFLAG.equals("0")) {
                // txtuseful.setText(aluseful.get(position).toString());
                //  txtuseless.setText(aluseless.get(position).toString());

            } else if (HFLAG.equals("1")) {

            }
            super.onPostExecute(result);
        }

    }


    private class downloadDoc extends AsyncTask<Void, Void, Void> {
        ArrayList<HashMap<String, String>> resList = new ArrayList<HashMap<String, String>>();
        JSONArray ds;

        JSONObject dss = null;

        String value = "", filename = "";

        downloadDoc(String comment) {

            this.filename = comment;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(context);
            progress.setMessage("Downloading...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                DownloadDacument ds = new DownloadDacument(filename);

            } catch (Exception e) {
                e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progress.dismiss();//otp:I3XW13,MobileNo:
            Toast.makeText(context, "Download completed", Toast.LENGTH_LONG).show();
            super.onPostExecute(result);
        }

    }


}
