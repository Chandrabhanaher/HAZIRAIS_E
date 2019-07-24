package com.example.ugcch.hazira.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ugcch.hazira.Config;
import com.example.ugcch.hazira.Item_Details;
import com.example.ugcch.hazira.R;
import com.example.ugcch.hazira.ScanSample;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    EditText empid;
    EditText password;
    boolean doubleBackToExitPressedOnce = false;
    Button login;

    private ProgressDialog pDialog;

    private String empid1;
    private String password1;
    UserSessionManager session;
    private boolean loggedIn = false;
    Handler handler = new Handler();
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
            StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        queue = Volley.newRequestQueue(Login.this);
        session = new UserSessionManager(getApplicationContext());
        session.isUserLoggedIn();

        empid=(EditText)findViewById(R.id.input_empid);
        password=(EditText)findViewById(R.id.input_password);

        empid.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        password.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
       //password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        login=(Button)findViewById(R.id.btn_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
                        if(networkInfo != null && networkInfo.isConnected()){

                            empid1 = empid.getText().toString().trim();
                            password1 = password.getText().toString().trim();

                            if ((!empid1.isEmpty()) && (!password1.isEmpty())) {
                                empid.setError(null);
                                password.setError(null);
                                loginUser();
                            }else{
                                empid.setError("Enter User Code");
                                password.setError("Enter Password");
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_LONG).show();
                        }
                    }
                },500);
            }
        });
    }

    private void loginUser() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            request.put("empid1", empid1 );
            request.put("password1", password1 );

            System.out.println(request);

        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest( Request.Method.POST, Config.LOGIN, request,
                new Response.Listener<JSONObject>() {
                    @Override

                    public void onResponse(JSONObject response){
                        pDialog.dismiss();
                        try {

                            if (response.getInt("status") == 1) {
                                Toast.makeText( getApplicationContext(), response.getString( "message" ), Toast.LENGTH_SHORT ).show();
                                session.createUserLoginSession(empid1);
                                Intent i =new Intent(getApplicationContext(),ScanSample.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();
                            }else if(response.getInt("status") == 2) {
                                Toast.makeText( getApplicationContext(), response.getString( "message" ), Toast.LENGTH_SHORT ).show();
                            }else if(response.getInt("status") == 3){
                                Toast.makeText( getApplicationContext(), response.getString( "message" ), Toast.LENGTH_SHORT ).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText( getApplicationContext(), "not get response", Toast.LENGTH_SHORT ).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText( getApplicationContext(), "Server Error ", Toast.LENGTH_SHORT ).show();
            }
        } );
        jsArrayRequest.setRetryPolicy(new DefaultRetryPolicy(1000,1,1));
        queue.add(jsArrayRequest);
    }

    private void displayLoader () {
        pDialog = new ProgressDialog(Login.this);
        pDialog.setMessage( "Authenticate.. Please wait..." );
        pDialog.setIndeterminate( false );
        pDialog.setCancelable( false );
        pDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            this.finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 1000);
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        loggedIn = sharedPreferences.getBoolean(Config.LOGGED_SHARED_PREF, false);
        if(loggedIn){
            Intent i = new Intent(getApplicationContext(),ScanSample.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }*/
}
