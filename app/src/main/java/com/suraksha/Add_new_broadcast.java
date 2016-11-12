package com.suraksha;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.AppHelper.ResizeImage;
import com.CheckInternet.CheckInternet;
import com.AppHelper.CheckAppPermission;
import com.SessionManager.SessionManager;
import com.webservice.Service1;

import com.ticktick.imagecropper.CropImageActivity;

public class Add_new_broadcast extends BaseActivity {


    public static boolean cropflag = false;
    public static int flag;
    public static Bitmap photo;


    ImageView imgphoto;
    EditText txtmsg;
    DiscussArrayAdapter da;
    Databaseclass db;
    public String path = "";

    Connection conn = null;
    Statement stmnt = null;

    String mobile;

    ImageButton btnattach;

    public static final String CROPPED_IMAGE_FILEPATH = "/sdcard/";
    public static final int REQUEST_CODE_PICK_IMAGE = 0x1;
    public static final int REQUEST_CODE_IMAGE_CROPPER = 0x2;

    Button btnpost;
    private ProgressDialog prgDialog;
    public static final int progress_bar_type = 0;
    int REQUEST_CAMERA = 2, SELECT_FILE = 1, PICK_FILE = 3;
    public static Bitmap bitmapcamera;


    public static String camimgpath;

    public static Bitmap bm;


    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private static final String IMAGE_DIRECTORY_NAME = "HowZak";


    private Uri fileUri;


    SessionManager session;
    CheckAppPermission checkAppPermission;
    CheckInternet checknet;
    LinearLayout llGallery;
    TextView tvdocument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_new_broadcast);

        Toolbar mToolbar = loadToolbar("Broadcast Message");
        mToolbar.setLogo(R.drawable.howzaticon_);
        mToolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        llGallery = (LinearLayout) findViewById(R.id.llGallery);
        tvdocument = (TextView) findViewById(R.id.tvdocument);

        checknet = new CheckInternet(Add_new_broadcast.this);
        session = new SessionManager(Add_new_broadcast.this);
        checkAppPermission = new CheckAppPermission(Add_new_broadcast.this);
        db = new Databaseclass(Add_new_broadcast.this);
        btnpost = (Button) findViewById(R.id.btnsend);
        txtmsg = (EditText) findViewById(R.id.etmsg);
        imgphoto = (ImageView) findViewById(R.id.photo);
        btnattach = (ImageButton) findViewById(R.id.btnattach);

        mobile = session.GetMobileNo();
        btnpost.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String msg = txtmsg.getText().toString();
                if (TextUtils.isEmpty(msg)) {
                    txtmsg.setError("Type a message");
                } else {

                    if (checknet.isNetworkAvailable()) {
                        new Inserttask(msg).execute();
                    } else {
                        Toast.makeText(Add_new_broadcast.this, "Internet not available", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });
        btnattach.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();

            }
        });
    }


    public void startCropImage(Uri uri, String newfilename) {
        Intent intent = new Intent(this, CropImageActivity.class);
        intent.setData(uri);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(CROPPED_IMAGE_FILEPATH + "cropped_" + newfilename)));
        intent.putExtra("aspectX", 2);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 240);
        intent.putExtra("maxOutputX", 640);
        intent.putExtra("maxOutputX", 480);
        startActivityForResult(intent, REQUEST_CODE_IMAGE_CROPPER);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.attachment_dialog, menu);
        return true;
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

    private class Inserttask extends AsyncTask<Void, Void, Void> {

        String MSG = "", Imagename = "";

        Inserttask(String msg) {
            this.MSG = msg;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);


        }

        @Override
        protected Void doInBackground(Void... params) {

            Service1 svc = new Service1();
            File f = new File(path);
            Imagename = session.GetUserid() + new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date()) + f.getName();
            try {//(mobile, msg, PI_NAME, "local")
                svc.InsertBroadCastDetails(mobile, MSG, Imagename, "local");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }


        @Override
        protected void onPostExecute(Void result) {


            if (path.endsWith(".doc") || path.endsWith(".docx") || path.endsWith(".ppt") || path.endsWith(".pptx") || path.endsWith(".xls") || path.endsWith(".xlsx") || path.endsWith(".pdf")) {


                SendPhotos ds = new SendPhotos(Imagename, path);
                ds.execute();
            } else {

                SendPhotos ds = new SendPhotos(Imagename, path);
                ds.execute();
            }


            Toast.makeText(Add_new_broadcast.this, "Message broadcasted", Toast.LENGTH_LONG).show();
            Intent i = new Intent(Add_new_broadcast.this, HomeActivity.class);
            startActivity(i);
            dismissDialog(progress_bar_type);
        }
    }


    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type:
                prgDialog = new ProgressDialog(this);
                prgDialog.setMessage("Broadcasting...");
                prgDialog.setIndeterminate(false);
                prgDialog.setCancelable(true);
                prgDialog.show();
                return prgDialog;
            default:
                return null;
        }

    }


    private void selectImage() {
        final CharSequence[] items = {"Choose Image", "Choose file", "Camera", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Add_new_broadcast.this);
        builder.setTitle("Select Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {


                if (Build.VERSION.SDK_INT >= 23) {
                    if (!checkAppPermission.checkPermissionForCamera()) {
                        checkAppPermission.requestPermissionForCamera();
                    } else if (!checkAppPermission.checkPermissionForExternalStorage()) {
                        checkAppPermission.requestPermissionForExternalStorage();
                    }
                }

                if (isSdReadable()) {
                    if (items[item].equals("Choose Image")) {

                        Intent intent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");

                        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                    } else if (items[item].equals("Choose file")) {

                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("file/*");
                        startActivityForResult(intent, PICK_FILE);
                    } else if (items[item].equals("Camera")) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                        startActivityForResult(intent, REQUEST_CAMERA);
                    } else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                } else {
                    Toast.makeText(Add_new_broadcast.this, "Storage not available", Toast.LENGTH_LONG).show();
                }

            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {

                try {
                    path = fileUri.getPath();


                    File f = new File(path);
                    BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                    bm = BitmapFactory.decodeFile(path, btmapOptions);
                    imgphoto.setImageBitmap(bm);

                    //+ f.getName().substring(f.getName().lastIndexOf("/") + 1, f.getName().length());
                    new ResizeImage(path);
                    //  SendPhotos sendPhotos = new SendPhotos(Imagename, selectedImagePath);
                    //  sendPhotos.execute();

                } catch (Exception e) {
                    // TODO: handle exception
                }
            } else if (requestCode == SELECT_FILE) {
                String FilePath = data.getData().getPath();
                path = getRealPathFromURI(data.getData());

                if (path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".png") || path.endsWith(".JPG") || path.endsWith(".JPEG") || path.endsWith(".PNG")) {


                    BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                    bm = BitmapFactory.decodeFile(path, btmapOptions);
                    bitmapcamera = null;
                    camimgpath = null;
                    new ResizeImage(path);
                    imgphoto.setImageBitmap(bm);
                } else {
                    Toast.makeText(this, "Please select a valid file", Toast.LENGTH_LONG).show();
                }

            } else if (requestCode == REQUEST_CODE_IMAGE_CROPPER) {

                Uri croppedUri = data.getExtras().getParcelable(MediaStore.EXTRA_OUTPUT);

                InputStream in = null;
                try {
                    in = getContentResolver().openInputStream(croppedUri);
                    Bitmap b = BitmapFactory.decodeStream(in);
                    imgphoto.setImageBitmap(b);
                    cropflag = true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == PICK_FILE) {

                path = getRealPathFromURI(data.getData());

                if (path.endsWith(".doc") || path.endsWith(".docx") || path.endsWith(".ppt") || path.endsWith(".pptx") || path.endsWith(".xls") || path.endsWith(".xlsx") || path.endsWith(".pdf")) {
                    File f = new File(path);


                    llGallery.setVisibility(View.VISIBLE);
                    tvdocument.setText(path);
                    imgphoto.setVisibility(View.GONE);
                } else {
                    Toast.makeText(this, "Please select a valid file", Toast.LENGTH_LONG).show();
                }


            }
        }
    }


    public Uri getOutputMediaFileUri(int type) {

        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/DCIM/", IMAGE_DIRECTORY_NAME);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

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

    public boolean isSdReadable() {

        boolean mExternalStorageAvailable = false;
        try {
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                mExternalStorageAvailable = true;
            } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                mExternalStorageAvailable = true;
            } else {

                mExternalStorageAvailable = false;
            }
        } catch (Exception ex) {

        }
        return mExternalStorageAvailable;
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
                UploadImages uploadImages = new UploadImages(pName, pPath);
            } catch (Exception e) {
                // TODO: handle exception
            }
            return null;
        }

    }


    //function to get the actual path of image being uploaded
    public String getRealPathFromURI(Uri contentUri) {
        String path = null;
        String[] proj = {MediaStore.MediaColumns.DATA};

        if ("content".equalsIgnoreCase(contentUri.getScheme())) {
            Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                path = cursor.getString(column_index);
            }
            cursor.close();
            return path;
        } else if ("file".equalsIgnoreCase(contentUri.getScheme())) {
            return contentUri.getPath();
        }
        return null;
    }


}