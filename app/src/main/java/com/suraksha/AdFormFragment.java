package com.suraksha;

import java.io.File;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.AppHelper.ResizeImage;
import com.SessionManager.SessionManager;
import com.core.BaseFragment;
import com.webservice.Service1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdFormFragment extends BaseFragment {

    Spinner adtype, adcategory, adproducttype, adusedproduct, adstate, adcity, adscope;
    EditText adcategoryname, adtitle, addescription, adkeyword, adaddress, adcontactno, ademailid, adwebsite;
    Button btnsubmitad;
    ImageButton btnattachad;
    String type, category, categoryname, ad_titlename, productused, state, city, scope, postedby, title, description, keyword, address, contactno, emailid, website, Imagename = "";
    ArrayList<String> listcity;
    ArrayAdapter<String> adcity_adapter, adusedproduct_adapter;

    String selectedImagePath = "";
    Service1 ws;
    ProgressDialog progress;
    SessionManager session;


    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final String IMAGE_DIRECTORY_NAME = "Howzat";
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Uri fileUri;


    AppTitleCallback mAppTitleCallback;


    public interface AppTitleCallback {
        void title(String title);
    }


    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);

        if (context instanceof Home) {
            mAppTitleCallback = (AppTitleCallback) context;
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppTitleCallback.title(getString(R.string.menu_post_text));
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_ad_form_phase2, container, false);

        ws = new Service1();
        session = new SessionManager(getActivity());
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        adtype = (Spinner) view.findViewById(R.id.spinneradtype);
        adcategory = (Spinner) view.findViewById(R.id.spinneradcategory);
        adcategoryname = (EditText) view.findViewById(R.id.txtcategory);
        adproducttype = (Spinner) view.findViewById(R.id.spinnerproduct);
        adusedproduct = (Spinner) view.findViewById(R.id.spinnerusedproduct);
        adtitle = (EditText) view.findViewById(R.id.txttitle);
        addescription = (EditText) view.findViewById(R.id.txtdescription);
        adaddress = (EditText) view.findViewById(R.id.txtaddress);
        adstate = (Spinner) view.findViewById(R.id.spinnerstate);
        adcity = (Spinner)view. findViewById(R.id.spinnercity);
        adcontactno = (EditText)view. findViewById(R.id.txtcontactno);
        ademailid = (EditText)view. findViewById(R.id.txtemailid);
        adwebsite = (EditText)view. findViewById(R.id.txtwebsite);
        adkeyword = (EditText)view. findViewById(R.id.txtkeyword);
        adscope = (Spinner) view.findViewById(R.id.spinnerscope);
        btnsubmitad = (Button)view. findViewById(R.id.btnsubmitad);
        btnattachad = (ImageButton)view. findViewById(R.id.btnattachad);
        listcity = new ArrayList<String>();
        ArrayAdapter<CharSequence> adtype_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.ad_type, android.R.layout.simple_spinner_item);
        adtype_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adtype.setAdapter(adtype_adapter);
        ArrayAdapter<CharSequence> adcategory_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.ad_category, android.R.layout.simple_spinner_item);
        adcategory_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adcategory.setAdapter(adcategory_adapter);
        ArrayAdapter<CharSequence> adproduct_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.product_type, android.R.layout.simple_spinner_item);
        adproduct_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adproducttype.setAdapter(adproduct_adapter);
        ArrayAdapter<CharSequence> adscope_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.ad_scope, android.R.layout.simple_spinner_item);
        adscope_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adscope.setAdapter(adscope_adapter);
        ArrayAdapter<CharSequence> adstate_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.ad_state, android.R.layout.simple_spinner_item);
        adstate_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adstate.setAdapter(adstate_adapter);

        adproducttype.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (arg2 == 1) {
                    adusedproduct.setVisibility(View.VISIBLE);
                    String[] select = {"Choose", "New", "Used"};
                    adusedproduct_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, select);
                } else {
                    adusedproduct.setVisibility(View.GONE);
                    String[] select = {"Choose"};
                    adusedproduct_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, select);
                }

                adusedproduct_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                adusedproduct.setAdapter(adusedproduct_adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {


            }
        });
        adstate.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (arg2 == 0) {
                    String[] select = {"Select City"};
                    adcity_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, select);
                    adcity_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    adcity.setAdapter(adcity_adapter);
                } else {
                    getCityList ds = new getCityList(arg2);
                    ds.execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {


            }
        });
        btnsubmitad.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                insertaddetails();

            }
        });
        btnattachad.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                selectImage();

            }
        });
        
        return view;
    }

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_form_phase2);

        Toolbar mToolbar = loadToolbar("Post new Ad");
        setSupportActionBar(mToolbar);
        mToolbar.setLogo(R.drawable.howzaticon_);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
       
    }*/


    private class getCityList extends AsyncTask<Void, Void, Void> {

        JSONObject responce = null;
        String value = "";
        int stid;

        getCityList(int stId) {
            this.stid = stId;
        }


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
//[{"city_name":"Daman and Diu"}]
                responce = ws.FetchCities(String.valueOf(stid));
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
            listcity.clear();
            if (ds != null) {
                if (ds.length() > 0) {
                    try {
                        for (int i = 0; i < ds.length(); i++) {
                            JSONObject obj;
                            obj = ds.getJSONObject(i);
                            String citys = obj.getString("city_name");
                            listcity.add(citys);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    adcity_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listcity);

                    adcity_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    adcity.setAdapter(adcity_adapter);

                }
            }
            super.onPostExecute(result);
        }

    }



 /*   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getActivity(). getMenuInflater().inflate(R.menu.ad_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();


                return true;
        }
        return super.onOptionsItemSelected(item);
    }
*/

    public void insertaddetails() {
        try {

            type = adtype.getSelectedItem().toString();
            category = adcategory.getSelectedItem().toString();
            categoryname = adcategoryname.getText().toString();
            productused = adproducttype.getSelectedItem().toString();
            ad_titlename = adusedproduct.getSelectedItem().toString();
            title = adtitle.getText().toString();
            description = addescription.getText().toString();
            keyword = adkeyword.getText().toString();
            address = adaddress.getText().toString();
            contactno = adcontactno.getText().toString();
            emailid = ademailid.getText().toString();
            website = adwebsite.getText().toString();
            state = adstate.getSelectedItem().toString();
            city = adcity.getSelectedItem().toString();
            scope = adscope.getSelectedItem().toString();

            if (TextUtils.isEmpty(categoryname)) {
                adcategoryname.setError("This field is compulsory");
            } else if (TextUtils.isEmpty(title)) {
                adtitle.setError("This field is compulsory");
            } else if (TextUtils.isEmpty(description)) {
                addescription.setError("This field is compulsory");
            } else if (TextUtils.isEmpty(keyword)) {
                adkeyword.setError("This field is compulsory");
            } else if (TextUtils.isEmpty(address)) {
                adaddress.setError("This field is compulsory");
            } else if (TextUtils.isEmpty(contactno)) {
                adcontactno.setError("This field is compulsory");
            } else if (TextUtils.isEmpty(emailid)) {
                ademailid.setError("This field is compulsory");
            } else if (!isValidEmail(emailid)) {
                ademailid.setError("Enter Valid Email Id");
            } else if (TextUtils.isEmpty(website)) {
                adwebsite.setError("This field is compulsory");
            } else {

                postedby = session.GetMobileNo();


                InsertAdDetail ds = new InsertAdDetail();
                ds.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


    private class InsertAdDetail extends AsyncTask<Void, Void, Void> {

        JSONObject responce = null;


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
                responce = ws.InsertAdDetails(type, category, categoryname, title, productused, ad_titlename, description, keyword, address, contactno,
                        emailid, website, state, city, scope, Imagename, postedby, "2012-01-01", "");
                Iterator iter = responce.keys();
                String key = (String) iter.next();
                String value = responce.getString(key);
                ds = new JSONArray(value);
            } catch (Exception e) {
                e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            progress.dismiss();
            Toast.makeText(getActivity(), "Ad submitted successfully", Toast.LENGTH_LONG).show();
            SendPhotos sendPhotos = new SendPhotos(Imagename, selectedImagePath);
            sendPhotos.execute();
            Intent i = new Intent(getActivity(), Home.class);
            startActivity(i);

            super.onPostExecute(result);
        }

    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

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
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
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
                    selectedImagePath = fileUri.getPath();

                    Log.d("Path", selectedImagePath);

                    File f = new File(selectedImagePath);

                    Imagename = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date()) + ".jpg";
                    ResizeImage rsimage = new ResizeImage(selectedImagePath);


                    Bitmap bm;
                    BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                    bm = BitmapFactory.decodeFile(selectedImagePath,
                            btmapOptions);
                    btnattachad.setImageBitmap(bm);

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
        Uri selectedImageUri = data.getData();
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getActivity().managedQuery(selectedImageUri, projection, null, null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        Imagename = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date()) + ".jpg";
        selectedImagePath = cursor.getString(column_index);
        ResizeImage rsimage = new ResizeImage(selectedImagePath);

        Bitmap bm;
        BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
        bm = BitmapFactory.decodeFile(selectedImagePath,
                btmapOptions);
        btnattachad.setImageBitmap(bm);


    }

    public Uri getOutputMediaFileUri(int type) {

        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), IMAGE_DIRECTORY_NAME);
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

        String pName = "";
        String pPath = "";

        SendPhotos(String name, String Path) {
            this.pName = name;
            this.pPath = Path;
        }


        @Override
        protected Void doInBackground(Void... params) {
            try {

                //  String Imagename = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date())+".jpg";
                UploadImages uploadImages = new UploadImages(pName, pPath);
            } catch (Exception e) {
                // TODO: handle exception
            }
            return null;
        }

    }


}
