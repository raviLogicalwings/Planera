package com.planera.mis.planera2.activities.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
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
import com.planera.mis.planera2.activities.models.DataItem;
import com.planera.mis.planera2.activities.models.InputOrders;

import java.util.ArrayList;
import java.util.List;


public class PODAdapter extends RecyclerView.Adapter<PODAdapter.MyPobHolder> {
    private Context context;
    private View holderView;
    private List<Brands> brandsList;
    private PODTextChangeListener podTextChangeListener;
    private List<InputOrders> POBOrdersList;
    private InputOrders orders;
    private DataItem previousInputForUpdate;

    public PODAdapter(Context context, List<Brands> brandsList, PODTextChangeListener podTextChangeListener, DataItem previousInputForUpdate) {
        this.context = context;
        this.brandsList = brandsList;
        this.podTextChangeListener = podTextChangeListener;
        this.previousInputForUpdate = previousInputForUpdate;
    }

    public PODAdapter() {

    }

    @NonNull
    @Override
    public MyPobHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        holderView = LayoutInflater.from(context).inflate(R.layout.item_pob_detalis, parent, false);
        return new MyPobHolder(holderView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPobHolder holder, int position) {
        POBOrdersList = new ArrayList<>();
        orders = new InputOrders();

        bindValues(holder, position);


    }

    @Override
    public int getItemCount() {
        if (brandsList.size() > 0) {
            return brandsList.size();
        } else {
            return 0;
        }
    }

    public void bindValues(MyPobHolder myPobHolder, int pos) {
        Brands brands;

        if (brandsList != null) {

            brands = brandsList.get(pos);
            myPobHolder.textPodProductName.setText(brandsList.get(pos).getName());
            if (previousInputForUpdate != null && previousInputForUpdate.getProductDetails() != null) {
                POBOrdersList = previousInputForUpdate.getProductDetails();
                setPOBOrdersList(POBOrdersList);
                for (int i = 0; i < previousInputForUpdate.getProductDetails().size(); i++) {
                    if (previousInputForUpdate.getProductDetails().get(i).getProductId().equals(brands.getProductId() + "")) {
                        String qty = previousInputForUpdate.getProductDetails().get(i).getQuantity() + "";
                        myPobHolder.editPodProductValue.setText(qty);

                    }
                }
            }

            myPobHolder.editPodProductValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String changedText = s.toString().trim();
                    boolean isUpdated = false;
                    if (POBOrdersList.size() > 0) {
                        for (int i = 0; i < POBOrdersList.size(); i++) {
                            if (POBOrdersList.get(i).getProductId().equals(brands.getProductId() + "")) {
                                if (!changedText.equals("")) {
                                    POBOrdersList.get(i).setQuantity(changedText);
                                } else {
                                    POBOrdersList.remove(i);
                                }
                                isUpdated = true;
                            }
                        }
                        if (!isUpdated) {
                            if (!changedText.equals("")) {
                                orders = new InputOrders();
                                orders.setQuantity(changedText);
                                orders.setProductId(brands.getProductId() + "");
                                POBOrdersList.add(orders);
                            }
                        }


                    } else {
                        if (!changedText.equals("")) {
                            orders = new InputOrders();
                            orders.setQuantity(changedText);
                            orders.setProductId(brands.getProductId() + "");
                            POBOrdersList.add(orders);
                        }
                    }
                    setPOBOrdersList(POBOrdersList);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                }
            });
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

    public interface PODTextChangeListener {
        void onPODTextChanged(MyPobHolder holder, int pos);
    }
}
