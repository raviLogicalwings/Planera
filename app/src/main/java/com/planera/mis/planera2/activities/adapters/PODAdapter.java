package com.planera.mis.planera2.activities.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.models.Brands;

import java.util.List;


public class PODAdapter extends RecyclerView.Adapter<PODAdapter.MyPobHolder>{
   private Context context;
   private View holderView;
   private List<Brands> brandsList;
   private PODTextChangeListener podTextChangeListener;

    public PODAdapter(Context context, List<Brands> brandsList, PODTextChangeListener podTextChangeListener) {
        this.context = context;
        this.brandsList = brandsList;
        this.podTextChangeListener = podTextChangeListener;
    }

    @Override
    public MyPobHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        holderView = LayoutInflater.from(context).inflate(R.layout.item_pob_detalis,parent,  false);
        return new MyPobHolder(holderView);
    }

    @Override
    public void onBindViewHolder(MyPobHolder holder, int position) {

        bindValues(holder, position);


    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public void bindValues(MyPobHolder myPobHolder, int pos){
        if (brandsList!= null){
            myPobHolder.textPodProductName.setText(brandsList.get(pos).getName());
            myPobHolder.editPodProductValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    podTextChangeListener.onPODTextChanged(myPobHolder, pos);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }


    public class MyPobHolder extends RecyclerView.ViewHolder {
        private TextView textPodProductName;
        public EditText editPodProductValue;


        MyPobHolder(View itemView) {
            super(itemView);
            textPodProductName = itemView.findViewById(R.id.text_pod_product_name);
            editPodProductValue = itemView.findViewById(R.id.edit_pod_product_value);
        }
    }

    public interface PODTextChangeListener{
        void onPODTextChanged( MyPobHolder holder, int pos);
    }
}
