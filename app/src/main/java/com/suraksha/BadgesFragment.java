package com.suraksha;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.AppHelper.RoundedImageView;
import com.CheckInternet.CheckInternet;
import com.SessionManager.SessionManager;
import com.core.BaseFragment;
import com.utilites.Utilites;
import com.webservice.Service1;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

//this class repersent the  the profile detail
public class BadgesFragment extends BaseFragment {


    ///-------------personal Details----------------
    private String MobileNo = "mobile";
    private String perusername = "UserName";
    private String perfname = "FirstName";

    private String perlname = "LastName";
    private String peremailid = "emailid";
    private String pergender = "gender";
    private String perdob = "dob";
    private String permaritalsts = "maritalstatus";
    private String perfatname = "fathername";
    private String perfatmob = "fathermobile";
    private String persspouname = "spousename";
    private String perspoumob = "spousemobile";
    private String peraddress = "address";
    private String percity = "residence_city";
    private String perstate = "residence_state";

    private String proruemp = "employed";
    private String proprofission = "profession";
    private String procompeny = "company";
    private String proofcaddrs = "office_address";
    private String procity = "office_city";
    private String prostate = "office_state";
    private String propincode = "office_pin";
///---------------------------------health------------------

    private String helbdgroup = "bloodgroup";
    private String helmedinc = "insurance";
    private String helpolicyno = "policyno";
    private String helremark = "health_remark";
    private String helvech = "vehicle_no1";


    private String BasesCoinBalance = "coin_balance";
    private String badgesGold = "gold_badges_count";
    private String adgeSilver = "silver_badges_count";
    private String BageseBronze = "bronze_badges_count";

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private static final String IMAGE_DIRECTORY_NAME = "Djnni";

    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Uri fileUri;


    int image[] = {R.drawable.ic_person1, R.drawable.ic_person1,
            R.drawable.ic_professional, R.drawable.ic_health};
    int image1[] = {R.drawable.ic_person, R.drawable.ic_person,
            R.drawable.ic_professional, R.drawable.ic_health};

    RelativeLayout badgeLayout, healthLayout, personalLayout, ProfessionalLayout;

    //    com.devsmart.android.ui.HorizontalListView hlList;
    GridAdapter adapter;
    Service1 ws;
    ProgressDialog progress;
    CheckInternet checknet;
    SessionManager sessionManager;
    ImageView badgeProfile;
    TextView profileName, stateName, coinbalance, tvGoldCount, tvSilverCount, tvBronzeCount;
    Button btnEdit, btnViewOffer, btnRedeme;
    EditText etusername, etmobile, etfname, etlname, etemailid, etdob, etfathername, etfathermobile, etspousename, etspousemobile, etaddress, etrcity;
    Spinner etgender, etmarital;
    EditText etrstate;
    Button btnpersonal;
    EditText etBloodGroup, etinsurance, etpolicyno, ethealth;
    EditText etvehicleno;
    Button btnupdateHealth;
    EditText etemployed, etprofession, etcompany, etoaddress, etocity, etostate, etopincode;
    Button btnprofession;
    ImageView mEditProfileIv;

    private String imgUrl = "http://104.238.126.224/djnni%20dll/bills/";

    LinearLayout badgeFooter;

    String[] genderlist = new String[]{"Select", "Male", "Female"};
    String[] maritalList = new String[]{"Select", "Single", "Married", "UnMarried", "divorced"};
    ArrayAdapter<String> gendrAdapter, maritAdapter;


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

    public static void showOptionDialog(Context context, String title, final String[] genderlist, final EditText editText) {

        android.support.v7.app.AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new android.support.v7.app.AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new android.support.v7.app.AlertDialog.Builder(context);
        }

        builder.setTitle(title);

        ArrayList<String> currList = new ArrayList<>();
        for (int i = 0; i < currList.size(); i++) {
/*
            builder.setSingleChoiceItems(genderlist, i, null)
                    .setPositiveButton("Select", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int position = ((android.support.v7.app.AlertDialog) dialog).getListView().getCheckedItemPosition();
                            String selectedTExt = genderlist[position];
                            editText.setText(selectedTExt);

                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

//            }
*/
            builder.setSingleChoiceItems(genderlist, 0, null)
                    .setPositiveButton("Select", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int pos = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                            String selectedTExt = genderlist[pos];

                            editText.setText(selectedTExt);

                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

        }

        android.support.v7.app.AlertDialog dialog = builder.create();//AlertDialog dialog; create like this outside onClick
        dialog.show();

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
        mAppTitleCallback.title(getString(R.string.profile));
    }

    TabLayout tabs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_badges_fragment, container, false);
        sessionManager = new SessionManager(getActivity());
        checknet = new CheckInternet(getActivity());//initilise the checking internet status
        ws = new Service1();
        tabs = (TabLayout) view.findViewById(R.id.tabs);
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
       /* Toolbar mToolbar = loadToolbar("Profile");
        setSupportActionBar(mToolbar);
        mToolbar.setLogo(R.drawable.howzaticon_);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/

        badgeLayout = (RelativeLayout) view.findViewById(R.id.badgeContainer);
        healthLayout = (RelativeLayout) view.findViewById(R.id.healthContainer);
        ProfessionalLayout = (RelativeLayout) view.findViewById(R.id.professionalContainer);
        personalLayout = (RelativeLayout) view.findViewById(R.id.personalContainer);
        badgeFooter = (LinearLayout) view.findViewById(R.id.badgeFooter);
        mEditProfileIv = (ImageView) view.findViewById(R.id.editprofile);
        mEditProfileIv.setColorFilter(Color.parseColor("#ffffff"));

        badgeLayout.setVisibility(View.VISIBLE);
        healthLayout.setVisibility(View.GONE);
        ProfessionalLayout.setVisibility(View.GONE);
        personalLayout.setVisibility(View.GONE);

        badgeProfile = (ImageView) view.findViewById(R.id.badgeProfile);
        profileName = (TextView) view.findViewById(R.id.profileName);
        stateName = (TextView) view.findViewById(R.id.stateName);

        btnEdit = (Button) view.findViewById(R.id.btnEdit);
        btnViewOffer = (Button) view.findViewById(R.id.btnViewOffernew);
        btnRedeme = (Button) view.findViewById(R.id.btnRedemenew);

        btnViewOffer.setClickable(true);
        btnRedeme.setClickable(true);

        btnpersonal = (Button) view.findViewById(R.id.btnpersonal);
        btnprofession = (Button) view.findViewById(R.id.btnprofessional);
        btnupdateHealth = (Button) view.findViewById(R.id.btnupdateHealth);


        //bages
        coinbalance = (TextView) view.findViewById(R.id.ivCurrency);
        tvGoldCount = (TextView) view.findViewById(R.id.tvGoldCount);
        tvSilverCount = (TextView) view.findViewById(R.id.tvSilverCount);
        tvBronzeCount = (TextView) view.findViewById(R.id.tvBronzeCount);
        //personal
        etusername = (EditText) view.findViewById(R.id.etusername);
        etmobile = (EditText) view.findViewById(R.id.etmobile);
        etfname = (EditText) view.findViewById(R.id.etfname);
        etlname = (EditText) view.findViewById(R.id.etlname);
        etemailid = (EditText) view.findViewById(R.id.etemailid);

        etgender = (Spinner) view.findViewById(R.id.etgender);
        etdob = (EditText) view.findViewById(R.id.etdob);
        showOptionDialog(getActivity(), "dsdfs", maritalList, etdob);

        etmarital = (Spinner) view.findViewById(R.id.etmarital);

        etfathername = (EditText) view.findViewById(R.id.etfathername);
        etfathermobile = (EditText) view.findViewById(R.id.etfathermobile);
        etspousename = (EditText) view.findViewById(R.id.etspousename);
        etspousemobile = (EditText) view.findViewById(R.id.etspousemobile);
        etaddress = (EditText) view.findViewById(R.id.etaddress);
        etrcity = (EditText) view.findViewById(R.id.etrcity);
        etrstate = (EditText) view.findViewById(R.id.etrstate);

//health
        etBloodGroup = (EditText) view.findViewById(R.id.etBloodGroup);
        etinsurance = (EditText) view.findViewById(R.id.etinsurance);
        etpolicyno = (EditText) view.findViewById(R.id.etpolicyno);
        ethealth = (EditText) view.findViewById(R.id.ethealth);
        etvehicleno = (EditText) view.findViewById(R.id.etvehicleno);
//professional
        etemployed = (EditText) view.findViewById(R.id.etemployed);
        etprofession = (EditText) view.findViewById(R.id.etprofession);
        etoaddress = (EditText) view.findViewById(R.id.etoaddress);
        etcompany = (EditText) view.findViewById(R.id.etcompany);
        etocity = (EditText) view.findViewById(R.id.etocity);
        etostate = (EditText) view.findViewById(R.id.etostate);
        etopincode = (EditText) view.findViewById(R.id.etopincode);


//        hlList = (HorizontalListView) view.findViewById(R.id.rlTabs);
//        adapter = new GridAdapter(getActivity(), 0);// set Adapter on horizental list view
//        hlList.setAdapter(adapter);
//        hlList.setOnItemClickListener(item_Click);
        SetCalenderToTextbox(etdob);// method for calender on edit text

// set array Adapter on the spinner for select gender
        gendrAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, genderlist);
        gendrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etgender.setAdapter(gendrAdapter);


// set array Adapter on the spinner for select marital status
        maritAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, maritalList);
        maritAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etmarital.setAdapter(maritAdapter);

// btn for gettting personal detail of user
        btnpersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



//                dsadsadad
//                        sadfsad
//                        sdfsd

                // method to getting personal detail
                updatepersonal ds = new updatepersonal(etmobile.getText().toString(), etusername.getText().toString(),
                        etfname.getText().toString(), etlname.getText().toString(), etemailid.getText().toString(),
                        etgender.getSelectedItem().toString(), etdob.getText().toString(), etmarital.getSelectedItem().toString(),
                        etfathername.getText().toString(), etfathermobile.getText().toString(), etspousename.getText().toString(),
                        etspousemobile.getText().toString(), etaddress.getText().toString(), etrcity.getText().toString(),
                        etrstate.getText().toString());
                ds.execute();
            }
        });
        //button for getting professional detail

        btnprofession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateprofessional des = new updateprofessional(etmobile.getText().toString(), etemployed.getText().toString(),
                        etprofession.getText().toString(), etcompany.getText().toString(), etoaddress.getText().toString(),
                        etocity.getText().toString(), etostate.getText().toString(), etopincode.getText().toString());
                des.execute();
            }
        });
        //
        btnupdateHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatehealth ds = new updatehealth(etmobile.getText().toString(), etBloodGroup.getText().toString(),
                        etpolicyno.getText().toString(), ethealth.getText().toString(), etvehicleno.getText().toString(), "", "",
                        etinsurance.getText().toString());
                ds.execute();
            }
        });
        mEditProfileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateImage();
            }
        });

        btnViewOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checknet.isNetworkAvailable()) {
                    Intent is = new Intent(getActivity(), ViewOffersFragment.class);
                    is.putExtra("coinbalance", coinbalance.getText().toString());
                    is.putExtra("offer", "profile");
                    startActivity(is);
                } else {
                    Toast.makeText(getActivity(), "Internet Not Available", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnRedeme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checknet.isNetworkAvailable()) {
                    Intent is = new Intent(getActivity(), RedemedHistory.class);
                    getActivity().startActivity(is);
                } else {
                    Toast.makeText(getActivity(), "Internet Not Available", Toast.LENGTH_LONG).show();
                }
            }
        });

        if (checknet.isNetworkAvailable()) {
            getAllUserDetails fd = new getAllUserDetails();
            fd.execute();
        } else {
            Toast.makeText(getActivity(), "Internet Not Available", Toast.LENGTH_LONG).show();
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);


        setTabs();
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        badgeLayout.setVisibility(View.VISIBLE);
                        healthLayout.setVisibility(View.GONE);
                        ProfessionalLayout.setVisibility(View.GONE);
                        personalLayout.setVisibility(View.GONE);
                        badgeFooter.setVisibility(View.VISIBLE);
//                        adapter.setItemSelected(position);
                        break;
                    case 1:
                        badgeLayout.setVisibility(View.GONE);
                        healthLayout.setVisibility(View.GONE);
                        ProfessionalLayout.setVisibility(View.GONE);
                        personalLayout.setVisibility(View.VISIBLE);
                        badgeFooter.setVisibility(View.GONE);
//                        adapter.setItemSelected(position);
                        break;
                    case 2:
                        badgeLayout.setVisibility(View.GONE);
                        healthLayout.setVisibility(View.GONE);
                        ProfessionalLayout.setVisibility(View.VISIBLE);
                        personalLayout.setVisibility(View.GONE);
                        badgeFooter.setVisibility(View.GONE);
//                        adapter.setItemSelected(position);
                        break;
                    case 3:
                        healthLayout.setVisibility(View.VISIBLE);
                        badgeLayout.setVisibility(View.GONE);
                        ProfessionalLayout.setVisibility(View.GONE);
                        personalLayout.setVisibility(View.GONE);
                        badgeFooter.setVisibility(View.GONE);
//                        adapter.setItemSelected(position);
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }


    private void setTabs() {

        tabs.addTab(tabs.newTab().setText("BROADCAST"));
        tabs.addTab(tabs.newTab().setText("PERSONAL"));
        tabs.addTab(tabs.newTab().setText("PROFESSIONAL"));
        tabs.addTab(tabs.newTab().setText("HEALTH"));


        RelativeLayout mTabMainlayout = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        TextView mTabText = (TextView) mTabMainlayout.findViewById(R.id.tab_text);
        ImageView mImageview = (ImageView) mTabMainlayout.findViewById(R.id.tabimage);
        mTabText.setText("BADGES");
        mImageview.setImageResource(image1[0]);
        tabs.getTabAt(0).setCustomView(mTabMainlayout);

        RelativeLayout mTabMainlayout1 = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        TextView mTabText1 = (TextView) mTabMainlayout1.findViewById(R.id.tab_text);
        ImageView mImageview1 = (ImageView) mTabMainlayout1.findViewById(R.id.tabimage);
        mTabText1.setText("PERSONAL");
        mImageview1.setImageResource(image1[1]);
        tabs.getTabAt(1).setCustomView(mTabMainlayout1);

        RelativeLayout mTabMainlayout2 = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        TextView mTabText2 = (TextView) mTabMainlayout2.findViewById(R.id.tab_text);
        ImageView mImageview2 = (ImageView) mTabMainlayout2.findViewById(R.id.tabimage);
        mTabText2.setText("PROFESSIONAL");
        mImageview2.setImageResource(image1[2]);
        tabs.getTabAt(2).setCustomView(mTabMainlayout2);

        RelativeLayout mTabMainlayout3 = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        TextView mTabText3 = (TextView) mTabMainlayout3.findViewById(R.id.tab_text);
        ImageView mImageview3 = (ImageView) mTabMainlayout3.findViewById(R.id.tabimage);
        mTabText3.setText("HEALTH");
        mImageview3.setImageResource(image1[3]);
        tabs.getTabAt(3).setCustomView(mTabMainlayout3);
    }


    /*   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badges_fragment);


       
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
*/

    public void SetCalenderToTextbox(EditText Txt) {
        final Calendar myCalendar = Calendar.getInstance();
        final EditText Txtdate = Txt;
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                UpdateDateText(Txtdate, myCalendar);
            }

        };

        Txt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }


    void UpdateDateText(EditText Txt, Calendar myCalendar) {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Txt.setText(sdf.format(myCalendar.getTime()));
    }


    private void updateImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                try {
                    String selectedImagePath = fileUri.getPath();
                    Log.d("Path", selectedImagePath);
                    File f = new File(selectedImagePath);
                    SendPhotos sendPhotos = new SendPhotos(selectedImagePath);
                    sendPhotos.execute();
                } catch (Exception e) {
                    // TODO: handle exception
                }

            }
        } else if (resultCode == getActivity().RESULT_CANCELED) {

            String a = "";
        }
    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            badgeProfile.setImageBitmap(photo);
        }


        Uri selectedImageUri = data.getData();
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getActivity().managedQuery(selectedImageUri, projection, null, null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String selectedImagePath = cursor.getString(column_index);

        SendPhotos ds = new SendPhotos(selectedImagePath);
        ds.execute();


    }

    public Uri getOutputMediaFileUri(int type) {

        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/DCIM/", IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }


    private class SendPhotos extends AsyncTask<Void, Void, Void> {
        String pPath = "", Imagename = "";

        SendPhotos(String Path) {
            this.pPath = Path;
        }

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
            try {
                Imagename = "IMG" + sessionManager.GetUserid() + "_" + new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date()) + ".jpg";
                UploadImages uploadImages = new UploadImages(Imagename, pPath);
            } catch (Exception e) {
                // TODO: handle exception
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            sessionManager.Savepreferences("imgname", Imagename);

            UploadfileNameServer DSS = new UploadfileNameServer(Imagename);
            DSS.execute();

            super.onPostExecute(result);
        }
    }

    private class UploadfileNameServer extends AsyncTask<Void, Void, Void> {
        String pName = "";
        JSONObject jobj = null;

        UploadfileNameServer(String name) {
            this.pName = name;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                jobj = ws.UpdateProfileImage(sessionManager.GetMobileNo(), pName);
            } catch (Exception e) {
                // TODO: handle exception
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progress.dismiss();
            Toast.makeText(getActivity(), "Image Updated", Toast.LENGTH_LONG).show();
            try {
                Picasso.with(getActivity()).load(imgUrl + pName)
                        .placeholder(getActivity().getResources().getDrawable(R.drawable.circle_img_bgr))
                        .transform(new RoundedImageView(50, 4))
                        .resize(100, 100)
                        .centerCrop()
                        .into(badgeProfile);
            } catch (Exception e) {

            }
            super.onPostExecute(result);
        }
    }


    private class getAllUserDetails extends AsyncTask<Void, Void, Void> {

        JSONObject responce = null;
        String value = "", imgName = "";
        JSONArray ds = null;

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
            try {


                responce = ws.FetchUserDetails(sessionManager.GetMobileNo());
                Iterator iter = responce.keys();
                while (iter.hasNext()) {
                    String key = (String) iter.next();
                    if (key.equals("Value")) {
                        value = responce.getString(key);
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
            progress.dismiss();

            if (ds != null) {
                if (ds.length() > 0) {
                    try {
                        for (int i = 0; i < ds.length(); i++) {
                            JSONObject obj;

                            obj = ds.getJSONObject(i);
                            etusername.setText(obj.getString(perusername));
                            etmobile.setText(obj.getString(MobileNo));
                            etfname.setText(obj.getString(perfname));
                            etlname.setText(obj.getString(perlname));
                            etemailid.setText(obj.getString(peremailid));
                            String genderitem = obj.getString(pergender);
                            if (!genderitem.equals(null)) {
                                int spinnerPosition = gendrAdapter.getPosition(genderitem);
                                etgender.setSelection(spinnerPosition);
                            }
                            etdob.setText(obj.getString(perdob));
                            String martal = obj.getString(permaritalsts);

                            if (!martal.equals(null)) {
                                int spinnerPosition = maritAdapter.getPosition(martal);
                                etmarital.setSelection(spinnerPosition);
                            }
                            etfathername.setText(obj.getString(perfatname));
                            etfathermobile.setText(obj.getString(perfatmob));
                            etspousename.setText(obj.getString(persspouname));
                            etspousemobile.setText(obj.getString(perspoumob));
                            etaddress.setText(obj.getString(peraddress));
                            etrcity.setText(obj.getString(percity));

                            etrstate.setText(obj.getString(perstate));


                            //profession
                            etemployed.setText(obj.getString(proruemp));
                            etprofession.setText(obj.getString(proprofission));
                            etcompany.setText(obj.getString(procompeny));
                            etoaddress.setText(obj.getString(proofcaddrs));
                            etocity.setText(obj.getString(procity));
                            etostate.setText(obj.getString(prostate));
                            etopincode.setText(obj.getString(propincode));

                            //health EditText

                            etBloodGroup.setText(obj.getString(helbdgroup));
                            etinsurance.setText(obj.getString(helmedinc));
                            etpolicyno.setText(obj.getString(helpolicyno));
                            ethealth.setText(obj.getString(helremark));
                            etvehicleno.setText(obj.getString(helvech));

                            profileName.setText(etfname.getText().toString() + " " + etlname.getText().toString());
                            stateName.setText(etrcity.getText().toString() + " " + etrstate.getText().toString());

                            String coinbb = obj.getString(BasesCoinBalance);
                            String goldcoin = obj.getString(badgesGold);
                            String silcoin = obj.getString(adgeSilver);
                            String bronjcoin = obj.getString(BageseBronze);
                            if (coinbb.equals("")) {
                                coinbb = "0";
                            }
                            if (goldcoin.equals("")) {
                                goldcoin = "0";
                            }
                            if (silcoin.equals("")) {
                                silcoin = "0";
                            }
                            if (bronjcoin.equals("")) {
                                bronjcoin = "0";
                            }
                            coinbalance.setText(coinbb);
                            tvGoldCount.setText(goldcoin);
                            tvSilverCount.setText(silcoin);
                            tvBronzeCount.setText(bronjcoin);

                            imgName = obj.getString("photo_ftp");

                            sessionManager.Savepreferences("mobilno1", obj.getString("mobileno1"));
                            sessionManager.Savepreferences("mobilno2", obj.getString("mobileno2"));
                            sessionManager.Savepreferences("mobilno3", obj.getString("mobileno3"));
                            // sessionManager.Savepreferences("MobileNo",obj.getString(MobileNo));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        Picasso.with(getActivity()).load(imgUrl + imgName)
                                .placeholder(getActivity().getResources().getDrawable(R.drawable.circle_img_bgr))
                                .transform(new RoundedImageView(50, 4))
                                .resize(100, 100)
                                .centerCrop()
                                .into(badgeProfile);
                    } catch (Exception e) {

                    }
                }
            }
            super.onPostExecute(result);
        }
    }


    private class updateprofessional extends AsyncTask<Void, Void, Void> {

        JSONObject responce = null;
        String value = "";
        JSONArray ds = null;
        String mobile = "", employed = "", profession = "", company = "", office_address = "", office_city = "",
                office_state = "", office_pin = "";

        updateprofessional(String mobile1, String employed1, String profession1, String company1, String office_address1,
                           String office_city1, String office_state1, String office_pin1) {
            this.mobile = mobile1;
            this.employed = employed1;
            this.profession = profession1;
            this.company = company1;
            this.office_address = office_address1;
            this.office_city = office_city1;
            this.office_state = office_state1;
            this.office_pin = office_pin1;

        }

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
            try {

                //(String mobile,String employed,String profession,String company,String office_address,String office_city,
                // String office_state,String office_pin)

                responce = ws.UpdateUserProfessionalDetails(mobile, employed, profession, company, office_address, office_city,
                        office_state, office_pin);
                Iterator iter = responce.keys();
                while (iter.hasNext()) {
                    String key = (String) iter.next();
                    if (key.equals("Value")) {
                        value = responce.getString(key);

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
            progress.dismiss();


            Toast.makeText(getActivity(), "Profession Detail Updated Successfully", Toast.LENGTH_LONG).show();

            super.onPostExecute(result);
        }

    }


    private class updatepersonal extends AsyncTask<Void, Void, Void> {

        JSONObject responce = null;
        String value = "";
        JSONArray ds = null;
        String mobile = "", UserName = "", firstName = "", lastName = "", emailID = "", gender = "",
                dob = "", maritalstatus = "", fathername = "", fathermobile = "", spousename = "", spousemobile = "",
                address = "", residence_city = "", residence_state = "";

        updatepersonal(String mobile1, String UserName1, String firstName1, String lastName1, String emailID1, String gender1,
                       String dob1, String maritalstatus1, String fathername1, String fathermobile1, String spousename1, String spousemobile1,
                       String address1, String residence_city1, String residence_state1) {
            this.mobile = mobile1;
            this.UserName = UserName1;
            this.firstName = firstName1;
            this.lastName = lastName1;
            this.emailID = emailID1;
            this.gender = gender1;
            this.dob = dob1;
            this.maritalstatus = maritalstatus1;
            this.fathername = fathername1;
            this.fathermobile = fathermobile1;
            this.spousename = spousename1;
            this.spousemobile = spousemobile1;
            this.address = address1;
            this.residence_city = residence_city1;
            this.residence_state = residence_state1;


        }

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
            try {

                //  String mobile,String UserName,String firstName,String lastName,String emailID,String gender,
                //   String dob,String maritalstatus,String fathername,String fathermobile,String spousename,String spousemobile,
                //   String address,String residence_city,String residence_state)

                responce = ws.UpdateUserPersonalDetails(mobile, UserName, firstName, lastName, emailID, gender,
                        dob, maritalstatus, fathername, fathermobile, spousename, spousemobile,
                        address, residence_city, residence_state);
                Iterator iter = responce.keys();
                while (iter.hasNext()) {
                    String key = (String) iter.next();
                    if (key.equals("Value")) {
                        value = responce.getString(key);
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

            if (value.equals("true"))
                Toast.makeText(getActivity(), "Profile Updated Successfully", Toast.LENGTH_LONG).show();
            profileName.setText(firstName + " " + lastName);
            stateName.setText(residence_state);
            super.onPostExecute(result);
        }

    }


    private class updatehealth extends AsyncTask<Void, Void, Void> {

        JSONObject responce = null;
        String value = "";
        JSONArray ds = null;
        String mobile = "", bloodgroup = "", policyno = "", health_remark = "", vehicle_no1 = "",
                vehicle_no2 = "", vehicle_no3 = "", insurance = "";

        updatehealth(String mobile1, String bloodgroup1, String policyno1, String health_remark1, String vehicle_no11,
                     String vehicle_no21, String vehicle_no31, String insurance1) {
            this.mobile = mobile1;
            this.bloodgroup = bloodgroup1;
            this.policyno = policyno1;
            this.health_remark = health_remark1;
            this.vehicle_no1 = vehicle_no1;
            this.vehicle_no2 = vehicle_no21;
            this.vehicle_no3 = vehicle_no31;
            this.insurance = insurance1;

        }

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
            try {

                //(String mobile,String bloodgroup,String policyno,String health_remark,String vehicle_no1,
                // String vehicle_no2,String vehicle_no3,String insurance) throws Exception {

                responce = ws.UpdateUserHealthDetails(mobile, bloodgroup, policyno, health_remark, vehicle_no1,
                        vehicle_no2, vehicle_no3, insurance);
                Iterator iter = responce.keys();
                while (iter.hasNext()) {
                    String key = (String) iter.next();
                    if (key.equals("Value")) {
                        value = responce.getString(key);

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
            progress.dismiss();

            Toast.makeText(getActivity(), "Health Detail Updated Successfully", Toast.LENGTH_LONG).show();

            super.onPostExecute(result);
        }

    }


    OnItemClickListener item_Click = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // TODO Auto-generated method stub
            switch (position) {
                case 0:
                    badgeLayout.setVisibility(View.VISIBLE);
                    healthLayout.setVisibility(View.GONE);
                    ProfessionalLayout.setVisibility(View.GONE);
                    personalLayout.setVisibility(View.GONE);
                    badgeFooter.setVisibility(View.VISIBLE);
                    adapter.setItemSelected(position);
                    break;
                case 1:
                    badgeLayout.setVisibility(View.GONE);
                    healthLayout.setVisibility(View.GONE);
                    ProfessionalLayout.setVisibility(View.GONE);
                    personalLayout.setVisibility(View.VISIBLE);
                    badgeFooter.setVisibility(View.GONE);
                    adapter.setItemSelected(position);
                    break;
                case 2:
                    badgeLayout.setVisibility(View.GONE);
                    healthLayout.setVisibility(View.GONE);
                    ProfessionalLayout.setVisibility(View.VISIBLE);
                    personalLayout.setVisibility(View.GONE);
                    badgeFooter.setVisibility(View.GONE);
                    adapter.setItemSelected(position);
                    break;
                case 3:
                    healthLayout.setVisibility(View.VISIBLE);
                    badgeLayout.setVisibility(View.GONE);
                    ProfessionalLayout.setVisibility(View.GONE);
                    personalLayout.setVisibility(View.GONE);
                    badgeFooter.setVisibility(View.GONE);
                    adapter.setItemSelected(position);
                    break;
            }
        }
    };

    private class GridAdapter extends ArrayAdapter<String> {

        Context context;
        private int _position;
        int image[] = {R.drawable.ic_person1, R.drawable.ic_person1,
                R.drawable.ic_professional, R.drawable.ic_health};
        int image1[] = {R.drawable.ic_person, R.drawable.ic_person,
                R.drawable.ic_professional, R.drawable.ic_health};
        private String title[] = {
                getResources().getString(R.string.badges_title),
                getResources().getString(R.string.personal_title),
                getResources().getString(R.string.professional_text),
                getResources().getString(R.string.health_text)};

        public GridAdapter(Context context, int resource) {
            super(context, resource);
            // TODO Auto-generated constructor stub
            this.context = context;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return title.length;
        }

        protected void setItemSelected(int position) {
            _position = position;
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View v = convertView;
            ViewHolder holder = null;
            if (v == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.profile_row, parent, false);
                holder.title = (TextView) v.findViewById(R.id.btn1);
                holder.img = (ImageView) v.findViewById(R.id.img1);
                holder.tab_purple_box = (View) v
                        .findViewById(R.id.tabPurpleLine);
                holder.tab_white_box = (View) v.findViewById(R.id.tabWhiteLine);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }
            if (_position == position) {
                holder.title.setText(title[position]);
                holder.title.setTextColor(getResources().getColor(
                        R.color.purple));
                holder.img.setImageResource(image1[position]);
                holder.tab_purple_box.setVisibility(View.VISIBLE);
                holder.tab_white_box.setVisibility(View.GONE);
            } else {
                holder.title.setText(title[position]);
                holder.img.setImageResource(image[position]);
                holder.tab_purple_box.setVisibility(View.GONE);
                holder.tab_white_box.setVisibility(View.VISIBLE);
            }
            return v;
        }
    }

    private static class ViewHolder {

        TextView title;
        View tab_purple_box, tab_white_box;
        ImageView img;
    }


}
