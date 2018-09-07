package com.planera.mis.planera2.activities.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.planera.mis.planera2.R;


public class PODAdapter extends RecyclerView.Adapter<PODAdapter.MyPobHolder> {
   private Context context;
   private View holderView;

    public PODAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MyPobHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        holderView = LayoutInflater.from(context).inflate(R.layout.item_pob_detalis,parent,  false);
        return new MyPobHolder(holderView);
    }

    @Override
    public void onBindViewHolder(MyPobHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 6;
    }

    class MyPobHolder extends RecyclerView.ViewHolder {
        private TextView textPodProductName;
        private EditText editPodProductValue;


        MyPobHolder(View itemView) {
            super(itemView);
            textPodProductName = itemView.findViewById(R.id.text_pod_product_name);
            editPodProductValue = itemView.findViewById(R.id.edit_pod_product_value);
        }
    }
}
