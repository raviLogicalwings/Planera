package com.planera.mis.planera2.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.models.States;

import java.util.List;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.MyStateHolder> {
    private static final String TAG = "States Adapter";
    public View view;
    private Context context;
    private List<States> stateList;
    private OnItemClickListener onItemClickListener;

    public StateAdapter(Context mContext, List<States> list, OnItemClickListener onItemClickListener){
        context = mContext;
        stateList = list;
        this.onItemClickListener = onItemClickListener;
    }
    @Override
    public MyStateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.item_states_list, parent, false);
        return new MyStateHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyStateHolder holder, final int position) {
        String stateStr = stateList.get(position).getName();
        Log.e(TAG, "onBindViewHolder: "+stateStr);
        holder.textState.setText(stateStr);

    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        if(stateList!=null){
           return stateList.size();
        }
        else {
            return 0;
        }
    }

    public class MyStateHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView textState;
        private ImageView imgDelete;
        private ImageView imgEdit;



        public MyStateHolder(View itemView) {
            super(itemView);
            textState = itemView.findViewById(R.id.text_state);
            imgDelete = itemView.findViewById(R.id.img_delete);
            imgEdit = itemView.findViewById(R.id.img_edit);

            imgDelete.setOnClickListener(this);
            imgEdit.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.img_delete:
                    onItemClickListener.onItemClick(getAdapterPosition(), imgDelete);
                    break;

                case R.id.img_edit:
                    onItemClickListener.onItemClick(getAdapterPosition(), imgEdit);
                    break;

            }
        }
    }

    public interface OnItemClickListener{

         void onItemClick(int postion, View view);
    }
}
