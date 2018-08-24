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
import com.planera.mis.planera2.activities.models.GiftsData;


import java.util.List;

public class AdminGiftAdapter extends RecyclerView.Adapter<AdminGiftAdapter.MyGiftHolder> {
    private static final String TAG = "States Adapter";
    public View view;
    private Context context;
    private List<GiftsData> giftsData;
    private OnItemClickListener onItemClickListener;


    public AdminGiftAdapter(Context mContext, List<GiftsData> list, OnItemClickListener onItemClickListener){
        context = mContext;
        giftsData = list;
        this.onItemClickListener = onItemClickListener;
    }
    @Override
    public MyGiftHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.item_list_gifts, parent, false);
        return new MyGiftHolder(view);
    }

    @Override
    public void onBindViewHolder(MyGiftHolder holder, int position) {
        String stateStr = giftsData.get(position).getName();
        Log.e(TAG, "onBindViewHolder: "+stateStr);
        holder.textGift.setText(stateStr);


    }

    @Override
    public int getItemCount() {
        if(giftsData!=null){
            return giftsData.size();
        }
        else {
            return 0;
        }
    }

    public class MyGiftHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textGift;
        private ImageView imgDelete;
        private ImageView imgEdit;



        public MyGiftHolder(View itemView) {
            super(itemView);
            textGift = itemView.findViewById(R.id.text_gift);
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
