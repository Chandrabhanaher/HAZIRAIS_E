package com.example.ugcch.hazira;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    EditText txtTk_no,txtCom_Locatio, txtQty_Pro_No, txtSeam_no, txtAWS_Class, txtShop, txtStation, txtWel_Psno,txtDate_Time,txtSqno;
    Button btnScan;
    FloatingActionButton fab;
    ScrollView scrollView;
    private IntentIntegrator qrScan;
    public  static final int RequestPermissionCode  = 1 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtTk_no = (EditText)findViewById(R.id.to_no);
        txtCom_Locatio = (EditText)findViewById(R.id.co_loc);
        txtQty_Pro_No = (EditText)findViewById(R.id.qulity_poj_no);
        txtSeam_no =  (EditText)findViewById(R.id.seam_no);
        txtAWS_Class = (EditText)findViewById(R.id.aws_class);
        txtShop = (EditText)findViewById(R.id.shop);
        txtStation = (EditText)findViewById(R.id.station);
        txtWel_Psno = (EditText)findViewById(R.id.welder_psno);
        txtDate_Time = (EditText)findViewById(R.id.date_time);
        txtSqno = (EditText)findViewById(R.id.seq_no);

        txtTk_no.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        txtCom_Locatio.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        txtQty_Pro_No.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        txtSeam_no.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        txtAWS_Class.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        txtShop.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        txtStation.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        txtWel_Psno.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        txtDate_Time.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        //txtDate_Time.setText(dateFormat.format(date));

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tokan_no = txtTk_no.getText().toString();
                String qty_pro_no = txtQty_Pro_No.getText().toString();
                String seam_no = txtSeam_no.getText().toString();
                String wel_psno = txtWel_Psno.getText().toString();
                String aws_class = txtAWS_Class.getText().toString();
                String date_time = txtDate_Time.getText().toString();
                if((tokan_no.isEmpty()) && (qty_pro_no.isEmpty())&&(seam_no.isEmpty())&&(wel_psno.isEmpty())&&(aws_class.isEmpty())){
                      Snackbar.make(view, "Enter all fields", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                }else{
                    Intent i = new Intent(getApplicationContext(),Emp_Detalis.class);
                    i.putExtra("tokan_no",tokan_no);
                    i.putExtra("qty_pro_no",qty_pro_no);
                    i.putExtra("seam_no",seam_no);
                    i.putExtra("aws_class",aws_class);
                    i.putExtra("wel_psno",wel_psno);
                    i.putExtra("date_time",date_time);
                    startActivity(i);
                }


            }
        });
        qrScan = new IntentIntegrator(this);
        scrollView = (ScrollView)findViewById(R.id.ss1);
        scrollView.setVisibility(View.GONE);
        btnScan = (Button)findViewById(R.id.button);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrScan.initiateScan();
            }
        });
        EnableRuntimePermission();
    }
    private void EnableRuntimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA)){
            Toast.makeText(MainActivity.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }
    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
                    // Toast.makeText(Home.this,"Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this,"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {

                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();

            } else {

                    String rs = result.getContents().toString();
                    System.out.println("QR Scann : "+rs);
                    String[] items = rs.split("\\s*\\|\\s*");
                    System.out.println("tokan: "+items[0]);
                    System.out.println("location "+items[1]);
                    System.out.println("quality_project"+items[2]);
                    System.out.println("seam "+items[3]);
                    System.out.println("flux_aws "+items[4]);
                    System.out.println("shop "+items[5]);
                    System.out.println("station "+items[6]);
                    System.out.println("psno "+items[7]);
                    System.out.println("req_date-time "+items[8]);
                    System.out.println("sequence_no "+items[9]);

                    txtTk_no.setText(items[0]);
                    txtCom_Locatio.setText(items[1]);
                    txtQty_Pro_No.setText(items[2]);
                    txtSeam_no.setText(items[3]);
                    txtAWS_Class.setText(items[4]);
                    txtShop.setText(items[5]);
                    txtStation.setText(items[6]);
                    txtWel_Psno.setText(items[7]);
                    txtDate_Time.setText(items[8]);
                    txtSqno.setText(items[9]);

                    if((txtTk_no !=null) && (txtCom_Locatio != null)){
                        scrollView.setVisibility(View.VISIBLE);
                        fab.setVisibility(View.VISIBLE);
                        btnScan.setVisibility(View.GONE);
                    }
            }
        }
    }

}
