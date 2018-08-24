package com.planera.mis.planera2.activities.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.models.Brands;

import java.util.ArrayList;
import java.util.List;

public class BrandsAdapter extends RecyclerView.Adapter<BrandsAdapter.MyBrandsHolder> {
    private Context context;
    private View mView;
    private List<Brands> brandsList;
    private ArrayAdapter<CharSequence> adapter;


    public BrandsAdapter(Context context, List<Brands> brandsList){
        this.context = context;
        this.brandsList = brandsList;

    }
    @Override
    public MyBrandsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mView= LayoutInflater.from(context).inflate(R.layout.item_brand_tab, parent, false);
    return new MyBrandsHolder(mView);
    }

    @Override
    public void onBindViewHolder(MyBrandsHolder holder, int position) {
        setupAdapter(adapter, holder);


    }

    @Override
    public int getItemCount() {
        if (brandsList!=null){
            return brandsList.size();
        }
        else{
            return 0;
        }
    }

    private void setupAdapter(ArrayAdapter<CharSequence> arrayAdapter, MyBrandsHolder myBrandsHolder){
        arrayAdapter = ArrayAdapter.createFromResource(context,
                R.array.brand_category, android.R.layout.simple_spinner_item);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myBrandsHolder.spinner.setAdapter(arrayAdapter);
    }

    class MyBrandsHolder extends RecyclerView.ViewHolder {
        private Spinner spinner;

        MyBrandsHolder(View itemView) {
            super(itemView);
            spinner = itemView.findViewById(R.id.brands_dropdown);
        }
    }
}
