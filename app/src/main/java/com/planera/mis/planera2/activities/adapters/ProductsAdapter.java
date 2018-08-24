package com.planera.mis.planera2.activities.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.models.Brands;

import java.util.List;


public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyProductsAdapter> {
    private static final String TAG = "States Adapter";
    public View view;
    private Context context;
    private List<Brands> brandsList;
    private OnItemClickListener onItemClickListener;


    public ProductsAdapter(Context mContext, List<Brands> list, OnItemClickListener onItemClickListener){
        context = mContext;
        brandsList = list;
        this.onItemClickListener = onItemClickListener;
    }
    @Override
    public MyProductsAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.item_list_products, parent, false);
        return new MyProductsAdapter(view);
    }

    @Override
    public void onBindViewHolder(MyProductsAdapter holder, int position) {
        String stateStr = brandsList.get(position).getName();
        Log.e(TAG, "onBindViewHolder: "+stateStr);
        holder.textProduct.setText(stateStr);


    }

    @Override
    public int getItemCount() {
        if(brandsList!=null){
            return brandsList.size();
        }
        else {
            return 0;
        }
    }

    public class MyProductsAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textProduct;
        private ImageView imgDelete;
        private ImageView imgEdit;



        public MyProductsAdapter(View itemView) {
            super(itemView);
            textProduct = itemView.findViewById(R.id.text_product);
            imgDelete = itemView.findViewById(R.id.img_delete);
            imgEdit = itemView.findViewById(R.id.img_edit);

            imgDelete.setOnClickListener(this);
            imgEdit.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.img_delete:
                    onItemClickListener.onItemClick(getAdapterPosition(), view);
                    break;

                case R.id.img_edit:
                    onItemClickListener.onItemClick(getAdapterPosition(), view);
                    break;
            }

        }
    }

    public interface OnItemClickListener{

        void onItemClick(int position, View view);
    }
}
