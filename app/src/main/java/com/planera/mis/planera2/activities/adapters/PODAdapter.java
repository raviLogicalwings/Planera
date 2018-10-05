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
import android.widget.Toast;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.controller.DataController;
import com.planera.mis.planera2.activities.models.Brands;
import com.planera.mis.planera2.activities.models.InputOrders;
import com.planera.mis.planera2.activities.utils.AppConstants;
import com.planera.mis.planera2.activities.utils.PreferenceConnector;

import java.util.ArrayList;
import java.util.List;


public class PODAdapter extends RecyclerView.Adapter<PODAdapter.MyPobHolder>{
   private Context context;
   private View holderView;
   private List<Brands> brandsList;
   private PODTextChangeListener podTextChangeListener;
    private List<InputOrders> POBOrdersList;
    private InputOrders orders;
    private PreferenceConnector connector;

    public PODAdapter(Context context, List<Brands> brandsList, PODTextChangeListener podTextChangeListener) {
        this.context = context;
        this.brandsList = brandsList;
        this.podTextChangeListener = podTextChangeListener;
    }

    public PODAdapter(){

    }

    @Override
    public MyPobHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        holderView = LayoutInflater.from(context).inflate(R.layout.item_pob_detalis,parent,  false);
        return new MyPobHolder(holderView);
    }

    @Override
    public void onBindViewHolder(MyPobHolder holder, int position) {
        POBOrdersList = new ArrayList<>();
        orders = new InputOrders();
        connector = new PreferenceConnector(context);

        bindValues(holder, position);


    }

    @Override
    public int getItemCount() {
        if (brandsList.size()>0){
           return brandsList.size();
        }
        else{
            return 0;
        }
    }

    public void bindValues(MyPobHolder myPobHolder, int pos){
        if (brandsList!= null){
            Brands brands = brandsList.get(pos);
            myPobHolder.textPodProductName.setText(brands.getName());
            myPobHolder.editPodProductValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String changedText = s.toString().trim();
                    if(!changedText.equals("")) {
                        orders = new InputOrders();
                        orders.setQuantity(s.toString().trim());
                        orders.setProductId(brands.getProductId()+"");
                        orders.setInputId(connector.getInteger(AppConstants.KEY_INPUT_ID) + "");
                        POBOrdersList.add(orders);

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                }
            });
            setPOBOrdersList(POBOrdersList);
        }
    }

    public List<InputOrders> getPOBOrdersList() {
        return DataController.getmInstance().getOrderPODList();
    }

    public void setPOBOrdersList(List<InputOrders> POBOrdersList) {
        DataController.getmInstance().setOrderPODList(POBOrdersList);
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
