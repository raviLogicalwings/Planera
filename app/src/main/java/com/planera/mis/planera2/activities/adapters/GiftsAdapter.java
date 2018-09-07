package com.planera.mis.planera2.activities.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.models.GiftsData;

import java.util.List;

public class GiftsAdapter extends RecyclerView.Adapter<GiftsAdapter.MyGiftHolder> {
    private Context context;
    private View holderView;
    private List<GiftsData> giftsData;
    private OnItemClickListener onItemClickListener;

    public GiftsAdapter(Context context, List<GiftsData> giftsData) {
        this.context = context;
        this.giftsData = giftsData;
    }
    @Override
    public MyGiftHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        holderView = LayoutInflater.from(context).inflate(R.layout.item_gifts,parent,  false);
        return new MyGiftHolder(holderView);
    }

    @Override
    public void onBindViewHolder(MyGiftHolder holder, int position) {
        setGiftsData(position, holder);
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

    public void setGiftsData(int position, MyGiftHolder holder){
        holder.textGift.setText(giftsData.get(position).getName());

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

        public void onItemClick(View view, int position);
    }
}
