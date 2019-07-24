package com.example.ugcch.hazira;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ugcch.hazira.login.Login;
import com.example.ugcch.hazira.login.UserSessionManager;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Emp_Detalis extends AppCompatActivity implements View.OnClickListener {
    private  String tokan_no,qty_pro_no,seam_no,aws_class,wel_psno,date_time;
    private TextInputEditText txtDocNo,txtTranDate,txtDept,txtEmp_code,txtEmp_Name,txtNote,txtFromLoc;
    private EditText txtTk_no,txtCom_Locatio, txtQty_Pro_No, txtSeam_no, txtAWS_Class, txtShop, txtStation, txtWel_Psno,txtDate_Time,txtSqno;
    private EditText txtBatchNo;
    private RequestQueue rq;
    String dept;
    private MaterialBetterSpinner from_loc, too_loc,batchLists;
    private ArrayAdapter<String> fromAdapter;
    private  ArrayAdapter<String> toAdapter;
    private ArrayList<String> from_location;
    private ArrayList<String> to_location;

    private ArrayAdapter<String> batchAdapter;
    private ArrayList<String> batch_no;

    private JSONArray jsonArray;
    private JSONArray jsonArray1;
    private JSONArray jsonArray2;
    private String LOC_CODE;
    private String LOC_CODE1;

    private String batch_no_list;
    private String form_location;
    private String too_location;
    private String token_trans_date;
    private String baan_project;

    private IntentIntegrator qrScan;

    LinearLayout linearLayout;
    Button btnScan;
    FloatingActionButton fab;

    LinearLayout cccardView;
    RelativeLayout recyclerVi;
    String ss1;
    UserSessionManager session;
    Handler h = new Handler();
    AlertDialog alertDialogAndroid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp__detalis);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        session = new UserSessionManager(getApplicationContext());
        rq = Volley.newRequestQueue(Emp_Detalis.this);
        qrScan = new IntentIntegrator(this);
        cccardView = (LinearLayout)findViewById(R.id.lll);
        recyclerVi = (RelativeLayout)findViewById(R.id.ddd);
        recyclerVi.setVisibility(View.GONE);

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
        txtBatchNo = findViewById(R.id.batch_no);

        Bundle b = getIntent().getExtras();
        ss1 = b.getString("ss");
        if(ss1.isEmpty()){
            startActivity(new Intent(getApplicationContext(),ScanSample.class));
        }else{
            //onQRScann();
            showDailog();
        }
       /* Intent i = getIntent();
        tokan_no = i.getStringExtra("tokan_no");
        qty_pro_no = i.getStringExtra("qty_pro_no");
        seam_no = i.getStringExtra("seam_no");
        aws_class = i.getStringExtra("aws_class");
        wel_psno = i.getStringExtra("wel_psno");
        date_time = i.getStringExtra("date_time");*/

        Date date = new Date();
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");

        txtDept = (TextInputEditText)findViewById(R.id.dpt_code);
        txtEmp_code = (TextInputEditText)findViewById(R.id.emp_code);
        txtEmp_Name = (TextInputEditText)findViewById(R.id.emp_Name);
        txtNote = (TextInputEditText)findViewById(R.id.note);

        txtDocNo = (TextInputEditText)findViewById(R.id.docno);
        txtDocNo.setVisibility(View.GONE);

        txtTranDate = (TextInputEditText)findViewById(R.id.trnsdate);
        txtTranDate.setText(dateFormat2.format(date));

        txtFromLoc = findViewById(R.id.from_loc1);
        txtFromLoc.setText("CENTRAL FLUX AREA");

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String deNo = txtDept.getText().toString();
                String emp_no = txtEmp_code.getText().toString();
                String empName = txtEmp_Name.getText().toString();

                /*if(batch_no_list == null){
                    Toast.makeText(getApplicationContext(),"Please select Batch No.",Toast.LENGTH_SHORT).show();
                }else*/ if(too_location == null){
                    Toast.makeText(getApplicationContext(),"Please select to-location",Toast.LENGTH_SHORT).show();
                }else{
                    if((!deNo.isEmpty()) && (!emp_no.isEmpty()) && (!empName.isEmpty()) && (!token_trans_date.isEmpty())){

                        Intent i = new Intent(getApplicationContext(),Item_Details.class);
                        i.putExtra("docno",txtDocNo.getText().toString());
                        i.putExtra("tokan_no",txtTk_no.getText().toString());
                        i.putExtra("qty_pro_no",txtQty_Pro_No.getText().toString());
                        i.putExtra("seam_no",txtSeam_no.getText().toString());
                        i.putExtra("aws_class",txtAWS_Class.getText().toString());
                        i.putExtra("wel_psno",txtWel_Psno.getText().toString());
                        i.putExtra("to_loc",LOC_CODE1);

                        i.putExtra("location","16");
                        i.putExtra("deptno",txtDept.getText().toString());
                        i.putExtra("emp_code",txtEmp_code.getText().toString());
                        i.putExtra("token_trans_date",token_trans_date);
                        i.putExtra("req_date_time",txtDate_Time.getText().toString());
                        i.putExtra("batch_no",txtBatchNo.getText().toString());

                        startActivity(i);
                    }else{
                        Intent i = new Intent(getApplicationContext(),Emp_Detalis.class);
                        Toast.makeText(getApplicationContext(),"Enter Correct information",Toast.LENGTH_SHORT).show();
                        startActivity(i);
                    }
                }
            }
        });
        from_location = new ArrayList<>();
        from_loc = (MaterialBetterSpinner)findViewById(R.id.from_loc) ;
       /* fromAdapter = new ArrayAdapter<String>(Emp_Detalis.this,android.R.layout.simple_dropdown_item_1line,from_location);
        from_loc.setAdapter(fromAdapter);
        fromAdapter.notifyDataSetChanged();*/

        to_location = new ArrayList<>();
        too_loc = (MaterialBetterSpinner)findViewById(R.id.to_loc) ;
        toAdapter = new ArrayAdapter<String>(Emp_Detalis.this,android.R.layout.simple_dropdown_item_1line,to_location);
        too_loc.setAdapter(toAdapter);
        toAdapter.notifyDataSetChanged();

        from_loc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getFromLoc(position);
                form_location = from_loc.getText().toString();
            }
        });

        too_loc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getToLoc(position);
                too_location = too_loc.getText().toString();
            }
        });
//      Supp Batch No List
        batch_no = new ArrayList<>();
        batchLists = (MaterialBetterSpinner)findViewById(R.id.batch);
        batchAdapter = new ArrayAdapter<String>(Emp_Detalis.this,android.R.layout.simple_dropdown_item_1line,batch_no);
        batchLists.setAdapter(batchAdapter);
        batchAdapter.notifyDataSetChanged();

        batchLists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                batch_no_list = batchLists.getText().toString();
            }
        });
//        QR Code Scan

        linearLayout = (LinearLayout)findViewById(R.id.l1);
        btnScan = (Button)findViewById(R.id.button);
        btnScan.setOnClickListener(this);
        btnScan.setVisibility(View.GONE);

    }

    private void onQRScann() {
        qrScan.initiateScan();
    }

    private void showDailog() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(Emp_Detalis.this);
        View mView = layoutInflaterAndroid.inflate(R.layout.verify_qr, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Emp_Detalis.this);
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
                final Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String tokenDetials = s.toString();
                        alertDialogAndroid.dismiss();
                        if(tokenDetials.equals(ss1)){
                            recyclerVi.setVisibility(View.VISIBLE);
                            System.out.println("QR Scann : "+tokenDetials);
                            String[] items = tokenDetials.split("\\s*\\|\\s*");
                            System.out.println("tokan: "+items[0]);
                            System.out.println("location "+items[1]);
                            System.out.println("quality_project"+items[2]);
                            System.out.println("seam "+items[3]);
                            System.out.println("flux_aws "+items[4]);
                            System.out.println("shop "+items[5]);
                            System.out.println("station "+items[6]);
                            System.out.println("psno "+items[7]);
                            System.out.println("req_date_time "+items[8]);
                            System.out.println("sequence_no "+items[9]);

                            tokan_no = items[0].toString();
                            String location = items[1].toString();
                            qty_pro_no = items[2].toString();
                            seam_no = items[3].toString();
                            aws_class = items[4].toString();
                            String shop = items[5].toString();
                            String station = items[6].toString();
                            wel_psno = items[7].toString();
                            date_time = items[8].toString();
                            String sequence_no = items[9].toString();

                            String issue = items[10].toString();

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

                            String ii = "Issue";

                            if(ii.equals(issue)){
                                if((tokan_no !=null) && (location != null) && (qty_pro_no != null) && (seam_no !=  null) && (aws_class != null) && (wel_psno !=  null) && (date_time != null)){


                                    btnScan.setVisibility(View.GONE);
                                    onShoeDocNo();
                                    showLocations();
                                    toLocationss();
                                    showBatchNo(date_time);
                                    showBatch(date_time);
                                    emp_Details();
                                    creationDate(date_time);

                                    /*Toast.makeText(getApplicationContext(),"tokan : "+tokan_no+"\n location: "+location+"\n project No : "+qty_pro_no+"\n seam : "+
                                    seam_no+"\n flux_aws: "+aws_class+"\n shop : "+shop+"\n station :"+station+"\n psno : "+wel_psno+"\n req_date_time :"+date_time+"\n " +
                                    "sequence_no :"+sequence_no,Toast.LENGTH_LONG).show();*/
                                }
                            }else{
                                Toast.makeText(getApplicationContext(),"Not valid Issue Entry QR Code",Toast.LENGTH_LONG).show();
                                session.logoutUser();
                            }

                        }else {
                            fab.setVisibility(View.GONE);
                            recyclerVi.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"QR Code is not match",Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(),ScanSample.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            finish();
                        }
                    }
                },7000);

            }
        });
    }
    // Token Batch No.
    private void showBatch(final String date_time){
        String url = Config.RE_BATCH_NO;
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null){
                            String batch ="";
                            try {
                                JSONArray array = new JSONArray(response);
                                for (int i=0; i<array.length();i++){
                                    JSONObject jsonObject = array.getJSONObject(i);
                                    batch = jsonObject.getString("BATCH_NO");
                                }
                                txtBatchNo.setText(batch);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> pp = new HashMap<>();
                pp.put("date_time",date_time.toString());
                return pp;
            }
        };
        stringRequest2.setRetryPolicy(new DefaultRetryPolicy(10000,1,1));
        rq.add(stringRequest2);
    }

    private void creationDate(final String date_time) {
        String url = Config.TRANS_DATES;
        StringRequest srr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response != null){
                    try {
                        JSONArray array = new JSONArray(response);
                        for(int i=0; i<array.length();i++){
                            JSONObject jsonObject = array.getJSONObject(i);
                            String trs_date = jsonObject.getString("TRANSACTION_DATE");
                            token_trans_date = trs_date.toString();

                            Date dates = new Date();
                            SimpleDateFormat dateFormat3 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            String c_date = dateFormat3.format(dates);//System Date
                            Date d1 = null;
                            Date d2= null;
                            try{
                                d1 = dateFormat3.parse(c_date);
                                d2 = dateFormat3.parse(trs_date);

                                long diff = d1.getTime() - d2.getTime();

                                long diffSeconds = diff / 1000 % 60;
                                long diffMinutes = diff / (60 * 1000) % 60;
                                long diffHours = diff / (60 * 60 * 1000) % 24;
                                long diffDays = diff / (24 * 60 * 60 * 1000);

                                if(diffHours < 12) {
                                    System.out.print(diffHours + " Hours "+"\n Trans Date : "+token_trans_date+"\n Current Date: "+c_date);
                                }else{
                                   startActivity(new Intent(getApplicationContext(),Deactivate_Screen.class));
                                }
                            }catch (Exception ee){
                                ee.printStackTrace();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        /*h.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(getApplicationContext(),Deactivate_Screen.class));
                            }
                        },10);*/
                    }
                }else {
                    Toast.makeText(Emp_Detalis.this,"Wrong",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Volley Error",error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> pp = new HashMap<>();
                pp.put("date_time",date_time.toString());
                return pp;
            }
        };
        RequestQueue rq1 = Volley.newRequestQueue(Emp_Detalis.this);
        rq1.add(srr);
    }
    public void emp_Details() {
        String url = Config.EMP_DETAILS;
        fab.setVisibility(View.GONE);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()){
                    String detp_code="";
                    String emp_code="";
                    String emp_name = "";
                    String trans_date = "";
                    String baan_pro="";
                    try{
                        JSONArray array = new JSONArray(response);
                        for(int i=0; i<array.length(); i++){
                            JSONObject json = array.getJSONObject(i);
                            detp_code = json.getString("DEPT_CODE");
                            emp_code = json.getString("EMPLOYEE_CODE");
                            emp_name = json.getString("EMPLOYEE_NAME");
                            trans_date = json.getString("TRANSACTION_DATE");
                            baan_pro = json.getString("BAAN_PROJECT");
                        }
                        txtDept.setText(detp_code);
                        txtEmp_code.setText(emp_code);
                        txtEmp_Name.setText(emp_name);
//                        token_trans_date = trans_date.toString();
                        baan_project = baan_pro.toString();
                        recyclerVi.setVisibility(View.VISIBLE);
                        cccardView.setVisibility(View.VISIBLE);
                        linearLayout.setVisibility(View.VISIBLE);
                        fab.setVisibility(View.VISIBLE);
                    }catch (JSONException ee){
                        ee.printStackTrace();
                    }
                }else {
                    startActivity(new Intent(getApplicationContext(),Deactivate_Screen.class));
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Error: " + error.getMessage());
                NetworkResponse networkResponse = error.networkResponse;
                Toast.makeText(getApplicationContext(),"Network error",Toast.LENGTH_SHORT).show();
                btnScan.setVisibility(View.GONE);
                linearLayout.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
                cccardView.setVisibility(View.GONE);
                startActivity(new Intent(getApplicationContext(),ScanSample.class));
                if (networkResponse != null) {
                    Log.e("Status code", String.valueOf(networkResponse.statusCode));
                }
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> pp = new HashMap<>();
                pp.put("token_no",tokan_no);
                pp.put("seam_no",seam_no);
                pp.put("aws_class",aws_class);
                pp.put("wel_psno",wel_psno);
                return pp;
            }
        };
        RequestQueue queue  = Volley.newRequestQueue(Emp_Detalis.this);
        queue.add(request);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {

                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(Emp_Detalis.this);
                View mView = layoutInflaterAndroid.inflate(R.layout.wrong, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Emp_Detalis.this);
                alertDialogBuilderUserInput.setView(mView);
                alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.show();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        alertDialogAndroid.dismiss();
                    }
                },500);

            } else {

                String rs = result.getContents().toString();

                if(rs.equals(ss1)){
                    Toast.makeText(getApplicationContext(),"QR Code is match",Toast.LENGTH_SHORT).show();
                    recyclerVi.setVisibility(View.VISIBLE);
                    recyclerVi.setVisibility(View.VISIBLE);
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
                    System.out.println("req_date_time "+items[8]);
                    System.out.println("sequence_no "+items[9]);

                    tokan_no = items[0].toString();
                    String location = items[1].toString();
                    qty_pro_no = items[2].toString();
                    seam_no = items[3].toString();
                    aws_class = items[4].toString();
                    String shop = items[5].toString();
                    String station = items[6].toString();
                    wel_psno = items[7].toString();
                    date_time = items[8].toString();
                    String sequence_no = items[9].toString();

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

                    if((tokan_no !=null) && (location != null) && (qty_pro_no != null) && (seam_no !=  null) && (aws_class != null) && (wel_psno !=  null) &&
                            (date_time != null)){
                        btnScan.setVisibility(View.GONE);
                        //onShoeDocNo();
                        //showLocations();
                        //toLocationss();
                        //creationDate(date_time);
                        //emp_Details();

                       /* Toast.makeText(getApplicationContext(),"tokan : "+tokan_no+"\n location: "+location+"\n quality_project : "+qty_pro_no+"\n seam : "+
                                seam_no+"\n flux_aws: "+aws_class+"\n shop : "+shop+"\n station :"+station+"\n psno : "+
                                wel_psno+"\n req_date_time :"+date_time+"\n sequence_no :"+sequence_no,Toast.LENGTH_LONG).show();*/
                    }
                }else {
                    fab.setVisibility(View.GONE);
                    recyclerVi.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"QR Code is not match",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(),ScanSample.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }
            }
        }
    }

    private String getToLoc(int position) {
        try{
            JSONObject json = jsonArray1.getJSONObject(position);
            LOC_CODE1 = json.getString("LOC_CODE");
        }catch (JSONException e){
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(),"location code : "+LOC_CODE1.toString(),Toast.LENGTH_SHORT).show();
        return LOC_CODE1;
    }

    private String getFromLoc(int position) {
        try{
            JSONObject json = jsonArray.getJSONObject(position);
            LOC_CODE = json.getString("LOC_CODE");
        }catch(JSONException e){
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(),"location code : "+LOC_CODE.toString(),Toast.LENGTH_SHORT).show();
        return LOC_CODE;
    }

    private void onShoeDocNo() {
        String url = Config.DONO;
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET,url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String dno ="";
              try{
                  for(int i=0; i<response.length();i++){
                      JSONObject object = response.getJSONObject(i);
                      dno = object.getString("DOCNO");
                  }
                  txtDocNo.setText(dno);
                  txtDocNo.setVisibility(View.GONE);
              }catch (JSONException e){
                  e.printStackTrace();
              }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Error: " + error.getMessage());
                NetworkResponse networkResponse = error.networkResponse;
                Toast.makeText(getApplicationContext(),"Network error",Toast.LENGTH_SHORT).show();
                if (networkResponse != null) {
                    Log.e("Status code", String.valueOf(networkResponse.statusCode));
                }
            }
        });
        rq = Volley.newRequestQueue(Emp_Detalis.this);
        rq.add(arrayRequest);
    }



    @Override
    protected void onStart() {
        super.onStart();
//        showLocations();
//        toLocationss();
    }

    private void toLocationss() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.LOC_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null){
                    JSONObject object = null;
                    try{
                        object = new JSONObject(response);
                        jsonArray1 = object.getJSONArray("result");
                        showFromLo1(jsonArray1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Error: " + error.getMessage());
                NetworkResponse networkResponse = error.networkResponse;
                Toast.makeText(getApplicationContext(),"Network error",Toast.LENGTH_SHORT).show();
                if (networkResponse != null) {
                    Log.e("Status code", String.valueOf(networkResponse.statusCode));
                }
            }
        });
        rq.add(stringRequest);
    }

    private void showFromLo1(JSONArray jsonArray1) {
        for (int i=0; i<jsonArray1.length(); i++){
            try {
                JSONObject josn = jsonArray1.getJSONObject(i);
                to_location.add(josn.getString("LOC_DESC"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        too_loc.setAdapter(new ArrayAdapter<String>(Emp_Detalis.this,android.R.layout.simple_dropdown_item_1line,to_location));
    }

    private void showBatchNo(final String date_time) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BATCH_NO_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response != null){
                    JSONObject jsonObject = null;
                    try{
                        jsonObject = new JSONObject(response);
                        jsonArray2 = jsonObject.getJSONArray("result");
                        batchLst(jsonArray2);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Error: " + error.getMessage());
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    Log.e("Status code", String.valueOf(networkResponse.statusCode));
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> pp = new HashMap<>();
                pp.put("date_time",date_time.toString());
                return pp;
            }
        };
        rq.add(stringRequest);
    }

    private void batchLst(JSONArray jsonArray) {
        for (int i=0; i<jsonArray.length(); i++){
            try {
                JSONObject josn = jsonArray.getJSONObject(i);
                batch_no.add(josn.getString("SUPP_BATCH_NO"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        batchLists.setAdapter(new ArrayAdapter<String>(Emp_Detalis.this,android.R.layout.simple_dropdown_item_1line,batch_no));
    }

    private void showLocations() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.LOC_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null){
                    JSONObject object = null;
                    try{
                        object = new JSONObject(response);
                        jsonArray = object.getJSONArray("result");
                        showFromLo(jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Error: " + error.getMessage());
                NetworkResponse networkResponse = error.networkResponse;
                Toast.makeText(getApplicationContext(),"Network error",Toast.LENGTH_SHORT).show();
                if (networkResponse != null) {
                    Log.e("Status code", String.valueOf(networkResponse.statusCode));
                }
            }
        });
        rq.add(stringRequest);
    }

    private void showFromLo(JSONArray jsonArray) {
        for (int i=0; i<jsonArray.length(); i++){
            try {
                JSONObject josn = jsonArray.getJSONObject(i);
                from_location.add(josn.getString("LOC_DESC"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
      //  from_loc.setAdapter(new ArrayAdapter<String>(Emp_Detalis.this,android.R.layout.simple_dropdown_item_1line,from_location));
    }

    @Override
    public void onClick(View v) {
        qrScan.initiateScan();
    }

    @Override
    public void onBackPressed() {
        /*super.onBackPressed();
        Intent i = new Intent(getApplicationContext(),ScanSample.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        this.finish();*/
        session.logoutUser();
    }
}
