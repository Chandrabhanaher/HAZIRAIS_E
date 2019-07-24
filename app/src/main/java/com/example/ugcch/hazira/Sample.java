package com.example.ugcch.hazira;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Sample extends AppCompatActivity {
    ArrayList<Item_list> items_list;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (RecyclerView)findViewById(R.id.item_lists);
        listView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(layoutManager);
        ShowList();
    }

    private void ShowList() {
        String url = Config.TEMP_DETAILS;
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response != null){
                            items_list = new ArrayList<>();
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int r=0; r<jsonArray.length(); r++){
                                    JSONObject result = jsonArray.getJSONObject(r);
                                    Item_list il = new Item_list();
                                    il.setSRNO(result.getString("SRNO"));
                                    il.setITEMCODE(result.getString("ITEMCODE"));
                                    il.setADJ_QTY(result.getString("QTY"));
                                    il.setBOOKCODE(result.getString("BOOKCODE"));
                                    il.setDOCNO(result.getString("DOCNO"));
                                    il.setDOCDATE(result.getString("DOCDATE"));
                                    il.setYYYYMM(result.getString("YYYYMM"));
                                    il.setIMPORT_DOCNO(result.getString("IMPORT_DOCNO"));

                                    items_list.add(il);
                                }
                                adapter = new Items_list(getApplicationContext(),items_list);
                                listView.setAdapter(adapter);
                                listView.setVisibility(View.VISIBLE);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(getApplicationContext(),"not found adj. items",Toast.LENGTH_SHORT).show();
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
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

}
