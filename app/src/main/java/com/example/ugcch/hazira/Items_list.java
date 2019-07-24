package com.example.ugcch.hazira;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class Items_list extends RecyclerView.Adapter<Items_list.ViewHolder>{
    Context context;
    ArrayList<Item_list> items_list;
    public Items_list(Context context, ArrayList<Item_list> items_list) {
        this.context = context;
        this.items_list = items_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v  = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_lists,parent,false);
        ViewHolder vv = new ViewHolder(v);
        return vv;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item_list  ii = items_list.get(position);
        holder.txtSrno.setText(ii.getSRNO());
        holder.txtItemcod.setText(ii.getITEMCODE());
        holder.txtAd_qty.setText(ii.getADJ_QTY());
        holder.txtBookcode.setText(ii.getBOOKCODE());
        holder.txtDocno.setText(ii.getDOCNO());
        holder.txtDocDate.setText(ii.getDOCDATE());
        holder.txtYYYYMM.setText(ii.getYYYYMM());
        String IMPORT_DOCNO = ii.getIMPORT_DOCNO();

    }

    @Override
    public int getItemCount() {
        return items_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSrno,txtItemcod, txtAd_qty, txtDocno, txtBookcode, txtYYYYMM,txtDocDate;
        public ViewHolder(View itemView) {
            super(itemView);
            txtSrno = (TextView)itemView.findViewById(R.id.srno);
            txtItemcod = (TextView)itemView.findViewById(R.id.item_code);;
            txtAd_qty = (TextView)itemView.findViewById(R.id.sd_qty);
            txtDocno = (TextView)itemView.findViewById(R.id.doc_no);
            txtBookcode = (TextView)itemView.findViewById(R.id.book_code);
            txtYYYYMM = (TextView)itemView.findViewById(R.id.yyyymm);
            txtDocDate = (TextView)itemView.findViewById(R.id.docDate);
        }
    }
}
