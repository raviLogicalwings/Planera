package com.planera.mis.planera2.activities.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.planera.mis.planera2.R;


public class POBAdapter extends RecyclerView.Adapter<POBAdapter.MyPobHolder> {
   private Context context;
   private View holderView;

    public POBAdapter(Context context) {
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

    public class MyPobHolder extends RecyclerView.ViewHolder {
        public MyPobHolder(View itemView) {
            super(itemView);
        }
    }
}
