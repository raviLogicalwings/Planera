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
import com.planera.mis.planera2.activities.controller.DataController;
import com.planera.mis.planera2.activities.models.GiftsData;
import com.planera.mis.planera2.activities.models.InputGift;
import com.planera.mis.planera2.activities.utils.PreferenceConnector;

import java.util.ArrayList;
import java.util.List;

public class GiftsAdapter extends RecyclerView.Adapter<GiftsAdapter.MyGiftHolder> {
    private Context context;
    private View holderView;
    private List<GiftsData> giftsData;
    private OnItemClickListener onItemClickListener;
    private InputGift inputGift;
    private PreferenceConnector connector;
    private List<InputGift> inputGiftList;

    public GiftsAdapter(Context context, List<GiftsData> giftsData) {
        this.context = context;
        this.giftsData = giftsData;
    }

    public GiftsAdapter(){

    }
    @Override
    public MyGiftHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        holderView = LayoutInflater.from(context).inflate(R.layout.item_gifts,parent,  false);
        return new MyGiftHolder(holderView);
    }

    @Override
    public void onBindViewHolder(MyGiftHolder holder, int position) {
        inputGift = new InputGift();
        GiftsData giftsDataObj = giftsData.get(position);
        inputGiftList = new ArrayList<>();
        connector = new PreferenceConnector(context);
        setGiftsData(position, holder, giftsData, giftsDataObj);
    }



    @Override
    public int getItemCount() {
       if(giftsData!=null){
           return giftsData.size();
       }
       else{
           return 0;
       }
    }

    public void setGiftsData(int position, MyGiftHolder holder, List<GiftsData> giftsData, GiftsData giftsDataObj){
        holder.textGift.setText(giftsDataObj.getName());
        holder.editGiftQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String quantityGift = s.toString().trim();
                boolean isUpdated = false;
                if (!quantityGift.equals("")) {

                     if (inputGiftList.size()>0){
                        for (int i = 0; i < inputGiftList.size(); i++) {
                            InputGift d = inputGiftList.get(i);
                            if (Integer.parseInt(d.getGiftId()) == giftsDataObj.getGiftId()) {
                                inputGiftList.get(i).setGiftId(giftsDataObj.getGiftId() + "");
                                inputGiftList.get(i).setQuantity(quantityGift);
                                isUpdated = true;
                            }
                            else{
//
                            }
                        }
                        if (!isUpdated){
                            inputGift = new InputGift();
                            inputGift.setQuantity(quantityGift);
                            inputGift.setGiftId(giftsDataObj.getGiftId() + "");
                            inputGiftList.add(inputGift);
                        }


                    }
                    else{
                         inputGift = new InputGift();
                         inputGift.setQuantity(quantityGift);
                         inputGift.setGiftId(giftsDataObj.getGiftId() + "");
                         inputGiftList.add(inputGift);
                     }

                }

//                GiftsData gifts = giftsData.get(position);
//                if (!quantityGift.equals("")){
//                    inputGift = new InputGift();
//                    inputGift.setQuantity(quantityGift);
//                    inputGift.setInputId(connector.getInteger(AppConstants.KEY_INPUT_ID)+"");
//                    inputGift.setGiftId(gifts.getGiftId()+"");
//                    inputGiftList.add(inputGift);
//
//                }

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

    public class MyGiftHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
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



    public interface OnItemClickListener{

        void onItemClick(View view, int position);
    }
}
