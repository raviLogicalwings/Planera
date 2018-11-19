package com.planera.mis.planera2.adapters;

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
import com.planera.mis.planera2.controller.DataController;
import com.planera.mis.planera2.models.DataItem;
import com.planera.mis.planera2.models.GiftsData;
import com.planera.mis.planera2.models.InputGift;
import com.planera.mis.planera2.utils.PreferenceConnector;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class GiftsAdapter extends RecyclerView.Adapter<GiftsAdapter.MyGiftHolder> {
    private Context context;
    private View holderView;
    private List<GiftsData> giftsData;
    private OnItemClickListener onItemClickListener;
    private InputGift inputGift;
    private PreferenceConnector connector;
    private List<InputGift> inputGiftList;
    private DataItem previousInputUpdate;

    public GiftsAdapter(Context context, List<GiftsData> giftsData, DataItem previousInputUpdate) {
        this.context = context;
        this.giftsData = giftsData;
        this.previousInputUpdate = previousInputUpdate;
    }

    public GiftsAdapter() {

    }

    @Override
    public MyGiftHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        holderView = LayoutInflater.from(context).inflate(R.layout.item_gifts, parent, false);
        return new MyGiftHolder(holderView);
    }

    @Override
    public void onBindViewHolder(MyGiftHolder holder, int position) {
        inputGift = new InputGift();
        GiftsData giftsDataObj = giftsData.get(position);
        inputGiftList = new ArrayList<>();
        if (inputGiftList == null){
            Toasty.success(context, "List is null", Toast.LENGTH_LONG).show();
        }
        connector = new PreferenceConnector(context);
        setGiftsData(position, holder, giftsData, giftsDataObj, previousInputUpdate);
    }


    @Override
    public int getItemCount() {
        if (giftsData != null) {
            return giftsData.size();
        } else {
            return 0;
        }
    }

    private void setGiftsData(int position, MyGiftHolder holder, List<GiftsData> giftsData, GiftsData giftsDataObj, DataItem previousInputUpdate) {
        holder.textGift.setText(giftsDataObj.getName());
        if (previousInputUpdate != null && previousInputUpdate.getGiftDetails() != null) {
            inputGiftList = previousInputUpdate.getGiftDetails();
            setInputGiftList(inputGiftList);
            for (int i = 0; i < previousInputUpdate.getGiftDetails().size(); i++) {
                if (previousInputUpdate.getGiftDetails().get(i).getGiftId().equals(giftsDataObj.getGiftId() + "")) {
                    String samples = previousInputUpdate.getGiftDetails().get(i).getGiftQuantity() + "";
                    holder.editGiftQuantity.setText(samples);
                }
            }
        }
        holder.editGiftQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String quantityGift = s.toString().trim();
                boolean isUpdated = false;


                    if (inputGiftList.size() > 0) {
                        for (int i = 0; i < inputGiftList.size(); i++) {
                            InputGift d = inputGiftList.get(i);
                            if (Integer.parseInt(d.getGiftId()) == giftsDataObj.getGiftId()) {
                                inputGiftList.get(i).setGiftId(giftsDataObj.getGiftId() + "");
                                if (!quantityGift.equals("")) {
                                    inputGiftList.get(i).setQuantity(quantityGift);
                                }
                                else{
                                    inputGiftList.remove(i);

                            }
                                isUpdated = true;
                        }



                    }
                        if (!isUpdated) {
                            if (!quantityGift.equals("")) {
                                inputGift = new InputGift();
                                inputGift.setQuantity(quantityGift);
                                inputGift.setGiftId(giftsDataObj.getGiftId() + "");
                                inputGiftList.add(inputGift);
                            }
                        }

                }
                else {
                    if (!quantityGift.equals("")) {
                        inputGift = new InputGift();
                        inputGift.setQuantity(quantityGift);
                        inputGift.setGiftId(giftsDataObj.getGiftId() + "");
                        inputGiftList.add(inputGift);
                    }
                }

//
                setInputGiftList(inputGiftList);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    public List<InputGift> getInputGiftList() {
        return DataController.getmInstance().getInputGiftList();
    }

    public void setInputGiftList(List<InputGift> inputGiftList) {
        DataController.getmInstance().setInputGiftList(inputGiftList);
    }

    public class MyGiftHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private EditText editGiftQuantity;
        private TextView textGift;

        public MyGiftHolder(View itemView) {
            super(itemView);
            editGiftQuantity = itemView.findViewById(R.id.edit_gift_quantity);
            textGift = itemView.findViewById(R.id.text_gift);

        }

        @Override
        public void onClick(View view) {
        }
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position);
    }
}
