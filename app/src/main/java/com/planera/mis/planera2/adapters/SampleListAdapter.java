package com.planera.mis.planera2.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.controller.DataController;
import com.planera.mis.planera2.models.Brands;
import com.planera.mis.planera2.models.DataItem;
import com.planera.mis.planera2.models.InputOrders;
import com.planera.mis.planera2.utils.AppConstants;
import com.planera.mis.planera2.utils.PreferenceConnector;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SampleListAdapter extends RecyclerView.Adapter<SampleListAdapter.MySampleHolder> {
    private Context context;
    private View mView;
    private List<Brands> brandsList;
    private List<InputOrders> sampleListSelected;
    public PreferenceConnector connector;
    private InputOrders orders;
    private DataItem dataItemForUpdate;
    private static final String NULL_STRING = "";
    private static final String SAMPLE_PRODUCT = "1";




    public SampleListAdapter(){}

    public SampleListAdapter(Context context, List<Brands> brandsList, DataItem dataItemForUpdate) {
        this.context = context;
        this.brandsList = brandsList;
        this.dataItemForUpdate = dataItemForUpdate;


    }

    @NonNull
    @Override
    public MySampleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        mView = LayoutInflater.from(context).inflate(R.layout.item_sample_view, parent, false);
        return new MySampleHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull MySampleHolder holder, int position) {
        sampleListSelected = new ArrayList<>();
        connector = new PreferenceConnector(context);
        bindItemsWithView(holder);
    }



    @Override
    public long getItemId(int position)
    {
        return position;
    }



    @Override
    public int getItemCount() {
        if (brandsList.size() > 0) {

            return brandsList.size();
        } else {
            return 0;
        }
    }



    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private void bindItemsWithView(MySampleHolder holder) {

        if(brandsList != null && brandsList.size()>0) {
            if (brandsList.get(holder.getAdapterPosition()).getIsBrand().equals(AppConstants.BRAND + NULL_STRING)) {
                holder.textBrandSampleName.setText(brandsList.get(holder.getAdapterPosition()).getName());


            Brands brands = brandsList.get(holder.getAdapterPosition());

            if (dataItemForUpdate != null && dataItemForUpdate.getProductDetails() != null) {
                sampleListSelected = dataItemForUpdate.getProductDetails();
                setSampleListSelected(sampleListSelected);
                for (int i = 0; i < dataItemForUpdate.getProductDetails().size(); i++) {
                    if (dataItemForUpdate.getProductDetails().get(i).getProductId().equals(brands.getProductId() + "")) {
                        String samples = dataItemForUpdate.getProductDetails().get(i).getQuantity() + "";
                        holder.editBrandSampleValue.setText(samples);
                    }


                }
            }

            holder.editBrandSampleValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    Log.e("Position+Value" ,holder.getAdapterPosition()+NULL_STRING);

                    String changedText = s.toString().trim();
                    boolean isUpdated = false;

                    if (sampleListSelected.size()>0){
                        for (int i= 0; i< sampleListSelected.size(); i++){
                            if (sampleListSelected.get(i).getProductId().equals(brands.getProductId()+"")){
                                if (changedText.equals(NULL_STRING)){
                                    sampleListSelected.remove(i);
                               }
                                else {
                                    sampleListSelected.get(i).setQuantity(changedText);
                                }
                                isUpdated = true;
                            }

                        }

                        if (!isUpdated){
                            if (!changedText.equals(NULL_STRING)) {
                                orders = new InputOrders();
                                orders.setProductId(brands.getProductId() + "");
                                orders.setIsSample(SAMPLE_PRODUCT);
                                orders.setQuantity(changedText);
                               orders.setInputId(connector.getInteger(AppConstants.KEY_INPUT_ID) + "");
                                sampleListSelected.add(orders);
                            }
                        }
                    }
                    else {
                        if (!changedText.equals(NULL_STRING)){
                            orders = new InputOrders();
                            orders.setProductId(brands.getProductId() + NULL_STRING);
                           orders.setIsSample(SAMPLE_PRODUCT);
                            orders.setQuantity(changedText);
                            orders.setInputId(connector.getInteger(AppConstants.KEY_INPUT_ID) + NULL_STRING);
                            sampleListSelected.add(orders);

                        }
                    }

                    setSampleListSelected(sampleListSelected);
                }
          });

            }

        }
    }



    public List<InputOrders> getSampleListSelected() {
        return DataController.getmInstance().getSampleListSelected();
    }

    public void setSampleListSelected(List<InputOrders> sampleListSelected) {
        DataController.getmInstance().setSampleListSelected(sampleListSelected);
    }

    class MySampleHolder extends RecyclerView.ViewHolder {
        private TextView textBrandSampleName;
        private EditText editBrandSampleValue;

        MySampleHolder(View itemView) {
            super(itemView);

            textBrandSampleName = itemView.findViewById(R.id.text_brand_sampel_name);
            editBrandSampleValue = itemView.findViewById(R.id.edit_brand_sample_value);
        }

    }

    public interface OnItemFoundListener{
        void onItemFound(List<InputOrders> listOfOrders);
    }
}
