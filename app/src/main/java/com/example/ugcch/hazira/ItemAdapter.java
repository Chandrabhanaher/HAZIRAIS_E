package com.example.ugcch.hazira;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ugcch.hazira.login.UserSessionManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    Context context;
    List<Items> itemsList;
    private OnItemClickListener mListener;
    ArrayList<Item_list> items_list;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    private static int SPLASH_TIME_OUT = 1000;
    Handler handler = new Handler();
    UserSessionManager session;
    AlertDialog alertDialogAndroid;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public ItemAdapter(Context context, List<Items> itemsList) {
        this.context = context;
        this.itemsList=itemsList;
        session = new UserSessionManager(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items,parent,false);
        return new ViewHolder(v,context,itemsList);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Items ii = itemsList.get(position);
        holder.txtBrch_no.setText(ii.getBRCHCODE());
        holder.txtItem_code.setText(ii.getITEMCODE());

        holder.txtStock.setText(ii.getSTOCK());
        holder.txtTotalStok.setText(ii.getTO_STOCK());
        String ss = ii.getSTOCK();
        if(ss.isEmpty()){
            Toast.makeText(context.getApplicationContext(),"Stock is not Available",Toast.LENGTH_SHORT).show();
        }
        final String dd = ii.getSEAM_NUMBER_1();

        holder.txtToken_No.setText(ii.getTOKEN_NUMBER());
        String ll = ii.getITEM_SIZE();
        holder.txtAWS_class.setText(ii.getAWS_CLASS());
        holder.txtLoc.setText(ii.getLOCATION());
        String lot = ii.getLT_LOT_NO();
        String betch = ii.getSUPP_BATCH_NO();
        holder.myCheckBox.setChecked(ii.isSelected());

        String SS = String.valueOf(position+1);
        if(!SS.isEmpty()){
            ii.setPostion(SS);
            String p = ii.getPostion();
            holder.txtSrno.setText(p);
        }else{
            System.out.println("Error");
        }
        holder.btnAdj.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Items p = itemsList.get(position);

                final String item = p.getITEMCODE();
                final String loc = p.getLOCATION();
                final String brch = p.getBRCHCODE();

                final String sk = p.getSTOCK();
                final String qt = p.getText();
                final String bt = p.getSUPP_BATCH_NO();
                final String lt_no = p.getLT_LOT_NO();

                if(qt.isEmpty()){
                    Toast.makeText(context.getApplicationContext(),"Enter QTY!",Toast.LENGTH_SHORT).show();
                }else {
                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            int stk = Integer.parseInt(sk);
                            int qty = Integer.parseInt(qt);
                            if(qty <= stk){
                                System.out.println("hello valid no");
                                holder.btnAdj.setClickable(false);
                                holder.btnAdj.setBackgroundColor(R.drawable.chang_color);
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.TEMP_INVGRN_INSERT, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                if(response != null){
                                                    try{
                                                        JSONArray array = new JSONArray(response);
                                                        for(int i=0; i<array.length(); i++){
                                                            JSONObject object = array.getJSONObject(i);
                                                            String mess = object.getString("message");
                                                            String su = "success";
                                                            if(mess.equals(su)){
                                                                //  =================================================== Show List Items in inv_grn ====================================================
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
                                                                                            il.setLOT_NO(result.getString("LOT_NO"));
                                                                                            items_list.add(il);
                                                                                        }
                                                                                        adapter = new Items_list(context.getApplicationContext(),items_list);
                                                                                        holder.listView.setAdapter(adapter);
                                                                                        holder.listView.setVisibility(View.VISIBLE);
                                                                                        holder.myCheckBox.setVisibility(View.VISIBLE);
                                                                                    } catch (JSONException e) {
                                                                                        e.printStackTrace();
                                                                                    }
                                                                                }else {
                                                                                    LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
                                                                                    View mView = layoutInflaterAndroid.inflate(R.layout.wrong1, null);
                                                                                    AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
                                                                                    alertDialogBuilderUserInput.setView(mView);
                                                                                    alertDialogAndroid = alertDialogBuilderUserInput.create();
                                                                                    alertDialogAndroid.show();
                                                                                    Handler h = new Handler();
                                                                                    h.postDelayed(new Runnable() {
                                                                                        @Override
                                                                                        public void run() {
                                                                                            alertDialogAndroid.dismiss();
                                                                                            session.logoutUser();
                                                                                        }
                                                                                    },500);
                                                                                }
                                                                            }
                                                                        }, new Response.ErrorListener() {
                                                                    @Override
                                                                    public void onErrorResponse(VolleyError error) {
                                                                        Log.e("TAG", "Error: " + error.getMessage());
                                                                        NetworkResponse networkResponse = error.networkResponse;
                                                                        Toast.makeText(context,"Network error",Toast.LENGTH_SHORT).show();
                                                                        if (networkResponse != null) {
                                                                            Log.e("Status code", String.valueOf(networkResponse.statusCode));
                                                                        }
                                                                    }
                                                                });
                                                                RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
                                                                queue.add(request);
//  ================================================= End Temp invgrn list================================================
                                                            }else{
                                                                Toast.makeText(context,"adjust is not available", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }else{
                                                    Toast.makeText(context,"adjust is not available", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Log.e("TAG", "Error: " + error.getMessage());
                                                NetworkResponse networkResponse = error.networkResponse;
                                                Toast.makeText(context,"Network error",Toast.LENGTH_SHORT).show();
                                                if (networkResponse != null) {
                                                    Log.e("Status code", String.valueOf(networkResponse.statusCode));
                                                }
                                            }
                                        }){
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> pa = new HashMap<>();
                                                pa.put("itemcode",item.toString());
                                                pa.put("brch",brch.toString());
                                                pa.put("loc",loc.toString());
                                                pa.put("qty",qt.toString());
                                                pa.put("batch_no",bt.toString());
                                                pa.put("LT_LOT_NO",lt_no.toString());
                                                return pa;
                                            }
                                        };
                                        RequestQueue rq = Volley.newRequestQueue(context);
                                        rq.add(stringRequest);
                                    }
                                },SPLASH_TIME_OUT);
                            }else{
                                System.out.println("no is not valid");
                                Toast.makeText(context.getApplicationContext(),"Enter Available Stock",Toast.LENGTH_LONG).show();
                            }
                        }
                    },500);
                }
            }
        });

        holder.txtQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {

                final Items p = itemsList.get(position);
                p.setText(s.toString());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.btnAdj.setVisibility(View.VISIBLE);
                    }
                },1500);
            }
        });
    }


    @Override
    public int getItemCount() {
        return itemsList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener{
        TextView txtBrch_no,txtItem_code,txtUnit,txtStock,txtLoc,txtToken_No,txtAWS_class,txtSrno,txtTotalStok;
        CheckBox myCheckBox;
        Context context;
        List<Items> itemsList;
        RecyclerView listView;
        AutoCompleteTextView txtQty;
        Button btnAdj;
        TextView txtConform;
        public ViewHolder(View itemView,Context context,List<Items> itemsList) {
            super(itemView);
            this.context = context;
            this.itemsList = itemsList;

            txtBrch_no = (TextView)itemView.findViewById(R.id.brch_no);
            txtItem_code = (TextView)itemView.findViewById(R.id.item_code);
            txtUnit = (TextView)itemView.findViewById(R.id.unit);
            txtStock = (TextView)itemView.findViewById(R.id.stk);
            txtTotalStok = (TextView)itemView.findViewById(R.id.to_stk);
            txtQty = (AutoCompleteTextView)itemView.findViewById(R.id.qty);
            txtLoc = (TextView)itemView.findViewById(R.id.location);
            txtToken_No = (TextView)itemView.findViewById(R.id.ton_no);
            txtAWS_class = (TextView)itemView.findViewById(R.id.aws_class);

            myCheckBox = (CheckBox)itemView.findViewById(R.id.myCheckBox);
            myCheckBox.setOnCheckedChangeListener(this);
            myCheckBox.setVisibility(View.INVISIBLE);
            txtSrno =  (TextView)itemView.findViewById(R.id.srNo);

            btnAdj = (Button)itemView.findViewById(R.id.djqty);
            txtConform = itemView.findViewById(R.id.conform);
            btnAdj.setVisibility(View.GONE);

            listView = (RecyclerView)itemView.findViewById(R.id.item_listss);
            listView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(context);
            listView.setLayoutManager(layoutManager);

        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            final int position = getAdapterPosition();
            final Items ii = itemsList.get(position);

            final String ss = ii.getText();

            if (isChecked){
                if(!ss.isEmpty()){
                    StringRequest arrayRequest = new StringRequest(Config.TEMP_STOCK, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response != null){
                                JSONObject object = null;
                                String stock="";
                                try {
                                    object = new JSONObject(response);
                                    JSONArray array = object.getJSONArray("result");
                                    JSONObject object1 = array.getJSONObject(0);
                                    stock = object1.getString("stock");
                                    if(stock =="null"){
                                        Toast.makeText(context.getApplicationContext(),"Stock is not available ",Toast.LENGTH_SHORT).show();
                                    }else{
                                        int s = Integer.parseInt(stock);
                                        int q =Integer.parseInt(ss);
                                        if(q == s){
                                            Toast.makeText(context.getApplicationContext(),"available stock : "+s,Toast.LENGTH_SHORT).show();
                                            if (mListener != null) {

                                                if (position != RecyclerView.NO_POSITION) {
                                                    mListener.onItemClick(position);
                                                    myCheckBox.setVisibility(View.INVISIBLE);
                                                    txtConform.setVisibility(View.VISIBLE);
                                                    txtConform.setTextColor(Color.parseColor("#008000"));
                                                }
                                            }
                                        }else{
                                            myCheckBox.setChecked(false);
                                            txtConform.setVisibility(View.GONE);
                                            Toast.makeText(context,"please check item stock !", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(context.getApplicationContext(),"Stock is not available ",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("TAG", "Error: " + error.getMessage());
                            NetworkResponse networkResponse = error.networkResponse;
                            Toast.makeText(context,"Network error",Toast.LENGTH_SHORT).show();
                            if (networkResponse != null) {
                                Log.e("Status code", String.valueOf(networkResponse.statusCode));
                            }
                        }
                    });
                    RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
                    requestQueue.add(arrayRequest);
                }else{
                    myCheckBox.setChecked(false);
                    Toast.makeText(context,"Enter QTY !", Toast.LENGTH_SHORT).show();
                }

            }
        }

    }

}
