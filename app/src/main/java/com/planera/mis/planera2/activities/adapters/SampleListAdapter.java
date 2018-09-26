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

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.controller.DataController;
import com.planera.mis.planera2.activities.models.Brands;
import com.planera.mis.planera2.activities.models.InputOrders;
import com.planera.mis.planera2.activities.utils.AppConstants;
import com.planera.mis.planera2.activities.utils.PreferenceConnector;

import java.util.ArrayList;
import java.util.List;

public class SampleListAdapter extends RecyclerView.Adapter<SampleListAdapter.MySampleHolder> {
    private Context context;
    private View mView;
    private List<Brands> brandsList;
    public List<InputOrders> sampleListSelected;
    public PreferenceConnector connector;
    private InputOrders orders;
    private int oldPosition = -1;
    private OnItemFoundListener onItemFoundListener;





    public SampleListAdapter(Context context, List<Brands> brandsList) {
        this.context = context;
        this.brandsList = brandsList;

    }

    @NonNull
    @Override
    public MySampleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        mView = LayoutInflater.from(context).inflate(R.layout.item_brand_tab, parent, false);
        return new MySampleHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull MySampleHolder holder, int position) {
        sampleListSelected = new ArrayList<>();
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


    public void bindItemsWithView(MySampleHolder holder, int pos) {

        if (brandsList.get(pos).getIsBrand().equals(AppConstants.BRAND + "")) {
            holder.textBrandSampelName.setText(brandsList.get(pos).getName());


        }
        Brands brands = brandsList.get(pos);

        holder.editBrandSampleValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String changedText = s.toString().trim();
                if(changedText.equals("")) {
                    orders = new InputOrders();
                    orders.setProductId(brands.getProductId() + "");
                    orders.setIsSample("1");
                    orders.setQuantity(s.toString().trim());
                    orders.setInputId(connector.getInteger(AppConstants.KEY_INPUT_ID) + "");
                    sampleListSelected.add(orders);
                    DataController.getmInstance().setSampleListSelected(sampleListSelected);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    public class MySampleHolder extends RecyclerView.ViewHolder {
        private TextView textBrandSampelName;
        private EditText editBrandSampleValue;

        MySampleHolder(View itemView) {
            super(itemView);

            textBrandSampelName = (TextView) itemView.findViewById(R.id.text_brand_sampel_name);
            editBrandSampleValue = (EditText) itemView.findViewById(R.id.edit_brand_sample_value);
        }

    }

    public interface OnItemFoundListener{
        void onItemFound(List<InputOrders> listOfOrders);
    }
}
