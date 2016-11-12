package com.suraksha;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

// view the ad form post detail
public class Adsearch_details extends BaseActivity {


    private TextView txttitle, txtcategory, txtcategoryname, txtlocation, txtdescription, txtaddress, txtcontact, txtemail, txtwebsite;
    Button btnchat;
    Connection conn = null;
    Statement stmnt = null;
    private ImageView imageView;
    private String imgBaseUrl = "http://104.238.126.224/djnni%20dll/bills/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addetails_search);

        Toolbar mToolbar = loadToolbar("Share");
        setSupportActionBar(mToolbar);
        mToolbar.setLogo(R.drawable.howzaticon_);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        txttitle = (TextView) findViewById(R.id.txttitle);
        txtcategory = (TextView) findViewById(R.id.txtcategory);
        txtcategoryname = (TextView) findViewById(R.id.txtcategoryname);
        txtlocation = (TextView) findViewById(R.id.txtlocation);
        txtdescription = (TextView) findViewById(R.id.txtdescription);
        txtaddress = (TextView) findViewById(R.id.txtaddress);
        txtcontact = (TextView) findViewById(R.id.txtcontact);
        txtemail = (TextView) findViewById(R.id.txtemail);
        txtwebsite = (TextView) findViewById(R.id.txtwebsite);
        imageView = (ImageView) findViewById(R.id.imageView1);

        Intent intent = getIntent();// getting data from the ClassifiedFragment class and setting vslue to text view
        txttitle.setText(intent.getStringExtra("title"));
        txtcategory.setText(intent.getStringExtra("category"));
        txtcategoryname.setText(intent.getStringExtra("categoryname"));
        txtlocation.setText(intent.getStringExtra("location"));
        txtdescription.setText(intent.getStringExtra("description"));
        txtaddress.setText(intent.getStringExtra("address"));
        txtcontact.setText(intent.getStringExtra("contact"));
        txtemail.setText(intent.getStringExtra("email"));
        txtwebsite.setText(intent.getStringExtra("website"));


        String imageUrl =imgBaseUrl +  intent.getStringExtra("image");

        Picasso.with(this).load(imageUrl).placeholder(R.drawable.no_image).error(R.drawable.no_image).into(imageView);


        btnchat = (Button) findViewById(R.id.btnchat);
        btnchat.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                show("Chat feature is coming soon!!");

            }
        });


    }

    public void show(String str) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.adsearch_details, menu);
        return true;
    }

    @Override
    public void onBackPressed() {

        finish();
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


}
