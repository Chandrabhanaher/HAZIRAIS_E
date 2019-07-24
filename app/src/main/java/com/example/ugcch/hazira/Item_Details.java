package com.example.ugcch.hazira;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ugcch.hazira.login.Login;
import com.example.ugcch.hazira.login.UserSessionManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Item_Details extends AppCompatActivity implements ItemAdapter.OnItemClickListener {
    RecyclerView item_list;
    List<Items> itemsList;
    ItemAdapter itemAdapter;
    RequestQueue queue;

    ArrayList<String> listdata = new ArrayList<String>();

    String DOCNO1,tokan_no,seam_no,aws_class,wel_psno,req_date_time, batch_no;
    String qty_pro_no;
    String token_trans_date;
    String BOOKCODE1 = "IS0010010002";
    public ArrayList<Items> mylist = new ArrayList<>();
    String DOCDATE1,YYYYMM1;
    String to_loc;
    String INV_TYPE = "I";

    String TRANDATE ;
    String locations;
    private String deptno;
    String emp_code;
    Button fab;
    UserSessionManager session;
    String email;

    String itemsss ;
    String po ;
    String qtyss ;
    private String ltNo;
    private String sup_batch;
    ProgressDialog progressDoalog;
    Handler h = new Handler();
    AlertDialog alertDialogAndroid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item__details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        session = new UserSessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        email = user.get(UserSessionManager.KEY_NAME);
        queue = Volley.newRequestQueue(this);

        Bundle b = getIntent().getExtras();
        tokan_no = b.getString("tokan_no");

        qty_pro_no = b.getString("qty_pro_no");
        seam_no = b.getString("seam_no");
        aws_class = b.getString("aws_class");
        wel_psno = b.getString("wel_psno");
        to_loc = b.getString("to_loc");
        locations = b.getString("location");
        deptno = b.getString("deptno");
        emp_code = b.getString("emp_code");
        token_trans_date = b.getString("token_trans_date");
        req_date_time = b.getString("req_date_time");
        batch_no = b.getString("batch_no");


        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyyMM");
        DOCDATE1 = dateFormat.format(date);
        YYYYMM1 = dateFormat1.format(date);

        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        TRANDATE = dateFormat2.format(date);

        itemsList = new ArrayList<>();
        item_list = (RecyclerView)findViewById(R.id.item_list);
        item_list.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        item_list.setLayoutManager(layoutManager);

        allItemsList(tokan_no,seam_no,aws_class,wel_psno,locations);

        fab = (Button) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDoalog = new ProgressDialog(Item_Details.this);
                progressDoalog.setMax(100);
                progressDoalog.setMessage("Please Wait....");
                progressDoalog.setTitle("Loading data ...");
                progressDoalog.show();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onShoeDocNo();
                    }
                },100);

            }
        });
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
                    DOCNO1  = dno.toString();
                    if(!DOCNO1.isEmpty()){
                        Toast.makeText(getApplicationContext(),"DOC No."+DOCNO1+"\n Project No."+qty_pro_no+
                                "\n TRANSDATE "+token_trans_date+"\n LOT No. "+ltNo,Toast.LENGTH_LONG).show();

                        System.out.println("DOC No."+DOCNO1+"\n Project No."+qty_pro_no+"\n TRANSDATE "+token_trans_date+"\n LOT No. "+ltNo);

                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                onInsertInv_Grn();
                                onInsertInvTRAN(itemsss,po,qtyss,ltNo,sup_batch,token_trans_date);
                                insertInv_TranHead(BOOKCODE1,DOCNO1,YYYYMM1,TRANDATE,locations,deptno,to_loc,emp_code);
                                onUpdate_flux_seq(qtyss,TRANDATE,email,req_date_time);
                                progressDoalog.dismiss();
                            }
                        },100);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDoalog.dismiss();
                Log.e("TAG", "Error: " + error.getMessage());
                NetworkResponse networkResponse = error.networkResponse;
                Toast.makeText(getApplicationContext(),"Network error",Toast.LENGTH_SHORT).show();
                if (networkResponse != null) {
                    Log.e("Status code", String.valueOf(networkResponse.statusCode));
                }
            }
        });
        RequestQueue rq = Volley.newRequestQueue(Item_Details.this);
        rq.add(arrayRequest);
    }

    private void onUpdate_flux_seq(final String qtyss, final String trandate, final String email, final String req_date_time) {
        String update_flux = Config.UPDATE_FLUX_SEQ;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, update_flux, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response !=null){
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONArray array = object.getJSONArray("result");
                        JSONObject object1 = array.getJSONObject(0);
                        String ss = object1.getString("m");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Flux Server error: " + error.getMessage());
                NetworkResponse networkResponse = error.networkResponse;
                Toast.makeText(getApplicationContext(),"Flux Server error",Toast.LENGTH_SHORT).show();
                if (networkResponse != null) {
                    Log.e("Status code", String.valueOf(networkResponse.statusCode));
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> stringMap = new HashMap<>();
                stringMap.put("ISSUEDQTY",qtyss);
                stringMap.put("ISSUEDDATE",trandate);
                stringMap.put("ISSUEDBY",email);
                stringMap.put("REQUESTDATE",req_date_time);
                return stringMap;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Item_Details.this);
        requestQueue.add(stringRequest);
    }

    private void insertInv_TranHead(final String bookcode1, final String docno1, final String yyyymm1, final String trandate, final String locations, final String deptno, final String to_loc, final String emp_code) {
        String url = Config.INV_TRNHEAD;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response != null){
                    try{
                        JSONObject object  = new JSONObject(response);
                        JSONArray array = object.getJSONArray("result");
                        JSONObject data = array.getJSONObject(0);
                        String ms = data.getString("msg");
                        //Toast.makeText(Item_Details.this,ms.toString(),Toast.LENGTH_LONG).show();
                        onLast_Key_Update(DOCNO1);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDoalog.dismiss();
                Log.e("Volley error",error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> pp = new HashMap<>();
                pp.put("TRANCODE",bookcode1);
                pp.put("DOCNO", docno1);
                pp.put("YYYYMM",yyyymm1);
                pp.put("TRANDATE",trandate);
                pp.put("LOCATION",locations);
                pp.put("DEPTNO",deptno);
                pp.put("TO_LOC",to_loc);
                pp.put("EMP_CODE",emp_code);
                pp.put("ENTRY_USER",email);
                return pp;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void onLast_Key_Update(final String docno1) {
        String url = Config.LAST_KEY_NO;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response != null){
                    try {
                        JSONObject object  = new JSONObject(response);
                        JSONArray array = object.getJSONArray("result");
                        JSONObject data = array.getJSONObject(0);
                        String ms = data.getString("m");
                        //Toast.makeText(Item_Details.this,ms.toString(),Toast.LENGTH_LONG).show();
                      /*  Intent i = new Intent(getApplicationContext(),ScanSample.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);*/
                        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(Item_Details.this);
                        View mView = layoutInflaterAndroid.inflate(R.layout.successfully, null);
                        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Item_Details.this);
                        alertDialogBuilderUserInput.setView(mView);
                        alertDialogAndroid = alertDialogBuilderUserInput.create();
                        alertDialogAndroid.show();
                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                alertDialogAndroid.dismiss();
                                logout();
                            }
                        },600);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDoalog.dismiss();
                Log.e("Volley error",error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> stringStringMap = new HashMap<>();
                stringStringMap.put("DOCNO",docno1);
                return stringStringMap;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Item_Details.this);
        requestQueue.add(stringRequest);
    }

    private void logout() {
        session.logoutUser();
        Item_Details.this.finish();
        System.exit(0);

    }

    private void allItemsList(final String tokan_no, final String seam_no, final String aws_class, final String wel_psno,final String locations) {

        String url = Config.ITEM_LIST;
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest request1 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                if (response != null) {
                    pDialog.hide();
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i <= 0; i++) {
                            JSONObject object = array.getJSONObject(i);
                            Items ii = new Items();
                            ii.setBRCHCODE(object.getString("BRCHCODE"));
                            ii.setITEMCODE(object.getString("ITEMCODE"));
                            ii.setSTOCK(object.getString("STOCK"));
                            ii.setTO_STOCK(object.getString("TO_STOCK"));
                            ii.setSEAM_NUMBER_1(object.getString("SEAM_NUMBER_1"));
                            ii.setTOKEN_NUMBER(object.getString("TOKEN_NUMBER"));
                            ii.setITEM_SIZE(object.getString("ITEM_SIZE"));
                            ii.setAWS_CLASS(object.getString("AWS_CLASS"));
                            ii.setLOCATION(object.getString("LOCATION"));
                            ii.setLT_LOT_NO(object.getString("LT_LOT_NO"));
                            ii.setSUPP_BATCH_NO(object.getString("SUPP_BATCH_NO"));
                            itemsList.add(ii);
                        }
                        itemAdapter = new ItemAdapter(Item_Details.this, itemsList);
                        item_list.setAdapter(itemAdapter);
                        itemAdapter.setOnItemClickListener(Item_Details.this);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(Item_Details.this);
                        View mView = layoutInflaterAndroid.inflate(R.layout.wrong, null);
                        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Item_Details.this);
                        alertDialogBuilderUserInput.setView(mView);
                        alertDialogAndroid = alertDialogBuilderUserInput.create();
                        alertDialogAndroid.show();
                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                alertDialogAndroid.dismiss();
                                logout();
                                session.logoutUser();
                                finish();
                            }
                        }, 5000);

                    }
                }else {
                    LayoutInflater layoutInflaterAndroid = LayoutInflater.from(Item_Details.this);
                    View mView = layoutInflaterAndroid.inflate(R.layout.wrong, null);
                    AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Item_Details.this);
                    alertDialogBuilderUserInput.setView(mView);
                    alertDialogAndroid = alertDialogBuilderUserInput.create();
                    alertDialogAndroid.show();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            alertDialogAndroid.dismiss();
                            logout();
                            /*Toast.makeText(getApplicationContext(),"Data not found",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),Login.class));*/
                            session.logoutUser();
                            finish();
                        }
                    },600);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // alertDialogAndroid.dismiss();
                Log.e("TAG", "Error: " + error.getMessage());
                pDialog.hide();
                NetworkResponse networkResponse = error.networkResponse;
                Toast.makeText(getApplicationContext(),"Server error",Toast.LENGTH_SHORT).show();
                if (networkResponse != null) {
                    Log.e("Status code", String.valueOf(networkResponse.statusCode));
                   /* Intent i = new Intent(getApplicationContext(),ScanSample.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);*/
                    session.logoutUser();
                    finish();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> pp = new HashMap<>();
                pp.put("token_no",tokan_no);
                pp.put("seam_no",seam_no);
                pp.put("aws_class",aws_class);
                pp.put("wel_psno",wel_psno);
                pp.put("location",locations);
                pp.put("batch_no",batch_no);
                return pp;
            }
        };
//        resend request in volley libraries
        request1.setRetryPolicy(new DefaultRetryPolicy(40000,1,1));
        RequestQueue vv = Volley.newRequestQueue(Item_Details.this);
        vv.add(request1);
    }

    private void displayLoader() {
        progressDoalog = new ProgressDialog(Item_Details.this);
//        progressDoalog.setMax(60000);
        progressDoalog.setMessage("Please Wait....");
        progressDoalog.setTitle("Loading data ...");
        progressDoalog.show();

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(new Runnable() {

            @Override
            public void run() {
                progressDoalog.dismiss();
            }
        },60000);
    }

    @Override
    public void onItemClick(int position) {
        Items p = itemsList.get(position);
        p.setSelected(!p.isSelected());

        if (p.isSelected()) {

            itemsss = p.getITEMCODE();
            po = p.getPostion();

            qtyss = p.getText();
            final String sk = p.getSTOCK();
            int m = Integer.parseInt(qtyss);
            int s = Integer.parseInt(sk);

            if(m<=s){
                fab.setVisibility(View.VISIBLE);
            }else{
                Toast.makeText(getApplicationContext(),"Check available stock",Toast.LENGTH_LONG).show();
            }
            ltNo = p.getLT_LOT_NO();
            Toast.makeText(getApplicationContext(),"LOT No. "+ltNo,Toast.LENGTH_LONG).show();
            sup_batch = p.getSUPP_BATCH_NO();

            mylist.add(p);
        }else {
            mylist.remove(p);
        }
    }
    private void onInsertInvTRAN(final String item, final String po, final String qty,final String ltNo,final String sup_batch, final String token_trans_date) {
        String url = Config.INVTRANS;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response !=null){
                    try {
                        JSONArray array = new JSONArray(response);
                        for(int j=0; j<array.length(); j++){
                            JSONObject object = array.getJSONObject(j);
                            String me = object.getString("msg");
                            //Toast.makeText(getApplicationContext(),me.toString(),Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDoalog.dismiss();
                Log.e("TAG", "Error: " + error.getMessage());
                NetworkResponse networkResponse = error.networkResponse;
                Toast.makeText(getApplicationContext(),"Server error",Toast.LENGTH_SHORT).show();
                if (networkResponse != null) {
                    Log.e("Status code", String.valueOf(networkResponse.statusCode));
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> object1 = new HashMap<>();
                object1.put("BOOKCODE", BOOKCODE1);
                object1.put("DOCDATE", DOCDATE1);
                object1.put("DOCNO", DOCNO1);
                object1.put("SRNO", po);
                object1.put("ITEMCODE",item);
                object1.put("QTY", qty);
                object1.put("INV_TYPE",INV_TYPE);
                object1.put("LOCATION",locations);
                object1.put("YYYYMM",YYYYMM1);
                object1.put("PROJECT_NO",qty_pro_no);
                object1.put("TOKAN_NUMBER",tokan_no);
                object1.put("AWS_CLASS",aws_class);
                object1.put("WELDER_CODE",wel_psno);
                object1.put("SEAM_NUMBER",seam_no);
                object1.put("SUPP_BATCH_NO",sup_batch);
                object1.put("LT_LOT_NO",ltNo);
                object1.put("TOKAN_DATE",token_trans_date);
                return object1;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Item_Details.this);
        requestQueue.add(stringRequest);
    }
    private void onInsertInv_Grn() {
        String url = Config.TEMP_DETAILS;
        final JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.POST, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray response) {
                if(response != null){
                    for(int i=0; i<response.length();i++){
                        JSONObject json = null;
                        JSONObject object = new JSONObject();

                        try {
                            json = response.getJSONObject(i);
                            object.put("BOOKCODE",BOOKCODE1);
                            object.put("DOCDATE",DOCDATE1);
                            object.put("DOCNO",DOCNO1);
                            object.put("SRNO",json.getString("SRNO"));
                            object.put("ITEMCODE", json.getString("ITEMCODE"));
                            object.put("QTY",json.getString("QTY"));
                            object.put("YYYYMM",YYYYMM1);
                            object.put("BOOKCODE2",json.getString("BOOKCODE"));
                            object.put("DOCNO2",json.getString("DOCNO"));
                            object.put("DOCDATE2",json.getString("DOCDATE"));
                            object.put("YYYYMM2",json.getString("YYYYMM"));
                            object.put("IMPORT_DOCNO",json.getString("IMPORT_DOCNO"));
                            object.put("LOT_NO",json.getString("LOT_NO"));

                            System.out.println(object);

                            JsonObjectRequest arrayRequest1 = new JsonObjectRequest(Request.Method.POST, Config.INV_GRN, object, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    if(response !=null){
                                        try {
                                            String ddd = response.getString("message");
                                            //Toast.makeText(getApplicationContext(),response.getString("message"),Toast.LENGTH_SHORT).show();
                                            onUpdate();
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
                                    Toast.makeText(getApplicationContext(),"Server error2",Toast.LENGTH_SHORT).show();
                                    if (networkResponse != null) {
                                        Log.e("Status code", String.valueOf(networkResponse.statusCode));
                                    }

                                }
                            });
                            RequestQueue requestQueue = Volley.newRequestQueue(Item_Details.this);
                            requestQueue.add(arrayRequest1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"adjust is not available",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDoalog.dismiss();
                Log.e("TAG", "Error: " + error.getMessage());
                NetworkResponse networkResponse = error.networkResponse;
                Toast.makeText(getApplicationContext(),"Server error",Toast.LENGTH_SHORT).show();
                if (networkResponse != null) {
                    Log.e("Status code", String.valueOf(networkResponse.statusCode));
                }
            }
        });
        queue.add(arrayRequest);
    }
    private void onUpdate() {
        String url = Config.UPDATE_INVTRANS;
        JsonObjectRequest objectRequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response != null){
                    try {
                        String me = response.getString("message");
                        //Toast.makeText(getApplicationContext(),me.toString(),Toast.LENGTH_SHORT).show();
                        logout();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDoalog.dismiss();
                Log.e("Volley Error",error.toString());
            }
        });
        RequestQueue queue = Volley.newRequestQueue(Item_Details.this);
        queue.add(objectRequest);
    }

    @Override
    public void onBackPressed() {
        session.logoutUser();
//        super.onBackPressed();
//        Intent i = new Intent(getApplicationContext(),ScanSample.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(i);
//        this.finish();
        session.logoutUser();
    }
}
