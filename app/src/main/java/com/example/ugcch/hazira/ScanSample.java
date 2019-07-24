package com.example.ugcch.hazira;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ugcch.hazira.login.UserSessionManager;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;

public class ScanSample extends AppCompatActivity {
Button button;
    IntentIntegrator qrScan;
    String s1;
    String tokenDetials;
    String answer;
    public  static final int RequestPermissionCode  = 1 ;
    AlertDialog alertDialogAndroid;

    UserSessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_sample);
        EnableRuntimePermission();
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                System.out.println("You are connected to a WiFi Network");
            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                System.out.println("You are connected to a Mobile Network");
            showDailog();
        }
        else {
            answer = "check your internet connection";
            Toast.makeText(getApplicationContext(), answer, Toast.LENGTH_LONG).show();
        }

        session = new UserSessionManager(getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        String email = user.get(UserSessionManager.KEY_NAME);
        Toast.makeText(getApplicationContext(),email.toString(),Toast.LENGTH_SHORT).show();

        qrScan = new IntentIntegrator(this);
        button = (Button)findViewById(R.id.button2);
        button.setVisibility(View.GONE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrScan.initiateScan();
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {

                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();

            } else {
                s1 = result.getContents().toString();
                if(!s1.isEmpty()){
                    Intent i = new Intent(getApplicationContext(), Emp_Detalis.class);
                    i.putExtra("ss",s1);
                    startActivity(i);
                }
            }
        }
    }
    private void showDailog() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(ScanSample.this);
        View mView = layoutInflaterAndroid.inflate(R.layout.alert_pick_items, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(ScanSample.this);
        alertDialogBuilderUserInput.setView(mView);
        final EditText scanValues= (EditText) mView.findViewById(R.id.userInputDialog);
        ImageButton btn11 = (ImageButton)mView.findViewById(R.id.checkbox);
        alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();

        scanValues.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(final Editable s) {
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        alertDialogAndroid.dismiss();
                        tokenDetials = s.toString();
                        Intent i = new Intent(getApplicationContext(), Emp_Detalis.class);
                        i.putExtra("ss",tokenDetials);
                        startActivity(i);

                    }
                },7000);

            }
        });

    }
    private void EnableRuntimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(ScanSample.this, Manifest.permission.CAMERA)){
            Toast.makeText(ScanSample.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(ScanSample.this,new String[]{
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
                    Toast.makeText(ScanSample.this,"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        alertDialogAndroid.dismiss();
        session.logoutUser();
        ScanSample.this.finish();
    }
}
