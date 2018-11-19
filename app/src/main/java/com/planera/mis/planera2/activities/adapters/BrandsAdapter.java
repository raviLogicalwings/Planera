package com.planera.mis.planera2.activities.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.controller.DataController;
import com.planera.mis.planera2.activities.models.Brands;
import com.planera.mis.planera2.activities.models.DataItem;
import com.planera.mis.planera2.activities.models.InputOrders;
import com.planera.mis.planera2.activities.utils.AppConstants;
import com.planera.mis.planera2.activities.utils.PreferenceConnector;

import java.util.ArrayList;
import java.util.List;

public class BrandsAdapter extends RecyclerView.Adapter<BrandsAdapter.MyBrandsHolder> {
    private Context context;
    private View mView;
    private List<Brands> brandsList;
    private List<String> brandLevelList;
    public List<InputOrders> orderListSelected;
    public PreferenceConnector connector;
    private InputOrders orders;
    private int oldPosition = -1;
    private OnItemFoundListener onItemFoundListener;
    private DataItem dataItemForUpdate;


    public BrandsAdapter() {

    }


    public BrandsAdapter(Context context, List<Brands> brandsList, List<String> intrestedLevel, DataItem dataItemForUpdate) {
        this.context = context;
        this.brandsList = brandsList;
        brandLevelList = intrestedLevel;
        this.dataItemForUpdate = dataItemForUpdate;

    }

    @NonNull
    @Override
    public MyBrandsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        mView = LayoutInflater.from(context).inflate(R.layout.item_brand_tab, parent, false);
        return new MyBrandsHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyBrandsHolder holder, int position) {
        orderListSelected = new ArrayList<>();
        orders = new InputOrders();
        connector = new PreferenceConnector(context);
        bindItemsWithView(holder, position);
    }

    @Override
    public int getItemCount() {
        if (brandsList != null) {
            return brandsList.size();
        } else {
            return 0;
        }
    }


    private void bindItemsWithView(MyBrandsHolder holder, int pos) {
        if (dataItemForUpdate != null) {
            if (brandsList.get(pos).getProductId() == dataItemForUpdate.getProductId()) {
                switch (dataItemForUpdate.getInterestedLevel()) {
                    case "1":
                        holder.spinner.setSelection(3);
                        break;
                    case "2":
                        holder.spinner.setSelection(2);
                        break;
                    case "3":
                        holder.spinner.setSelection(1);
                        break;
                    default:
                        holder.spinner.setSelection(0);
                }
            }
        }


        if (brandsList.get(pos).getIsBrand().equals(AppConstants.BRAND + "")) {
            holder.textBrandName.setText(brandsList.get(pos).getName());


        }
        Brands brands = brandsList.get(pos);

        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = brandLevelList.get(position);
                if (!value.equals("")) {
                    orders = new InputOrders();
                    orders.setIsSample("0");
                    orders.setProductId(brands.getProductId() + "");

                    switch (value) {
                        case "Low":
                            orders.setInterestedLevel("3");
                            break;
                        case "Regular":
                            orders.setInterestedLevel("2");
                            break;
                        case "Super":
                            orders.setInterestedLevel("1");
                            break;

                    }
                    orderListSelected.add(orders);
                    setOrderListSelected(orderListSelected);
                }


//                orders.setInterestedLevel(position+"");


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }


    private void setArrayAdapter(Spinner spinner) {

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (context, android.R.layout.simple_spinner_item,
                        brandLevelList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

    }

    public List<InputOrders> getOrderListSelected() {
        return DataController.getmInstance().getOrderListSelected();
    }

    public void setOrderListSelected(List<InputOrders> orderListSelected) {
        DataController.getmInstance().setOrderListSelected(orderListSelected);
    }

    public class MyBrandsHolder extends RecyclerView.ViewHolder {
        public Spinner spinner;
        TextView textBrandName;

        MyBrandsHolder(View itemView) {
            super(itemView);

            spinner = itemView.findViewById(R.id.brands_dropdown);
            textBrandName = itemView.findViewById(R.id.text_brand_name);
            setArrayAdapter(spinner);
        }

    }

    public interface OnItemFoundListener {
        void onItemFound(List<InputOrders> listOfOrders);
    }
}
