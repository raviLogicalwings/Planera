package com.planera.mis.planera2.activities.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.models.DataItem;
import com.planera.mis.planera2.activities.utils.PreferenceConnector;

import java.util.List;

public class InputListAdapter extends RecyclerView.Adapter<InputListAdapter.MyInputItemHolder> {
    private Context context;
    private View mView;
    private List<DataItem> inputListItems;
    public PreferenceConnector connector;
    private OnInputItemClickListener onInputItemClickListener;
    private DataItem item;



    public InputListAdapter(){

    }


    public InputListAdapter(Context context, List<DataItem> inputListItems, OnInputItemClickListener onInputItemClickListener) {
        this.context = context;
        this.inputListItems = inputListItems;
        this.onInputItemClickListener  = onInputItemClickListener;

    }

    @NonNull
    @Override
    public MyInputItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        mView = LayoutInflater.from(context).inflate(R.layout.item_input_list, parent, false);
        return new MyInputItemHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyInputItemHolder holder, int position) {
        bindItemsWithView(holder, position);
    }

    @Override
    public int getItemCount() {
        if (inputListItems != null) {
            return inputListItems.size();
        } else {
            return 0;
        }
    }


    public void bindItemsWithView(MyInputItemHolder holder, int pos) {

        item = inputListItems.get(pos);
        holder.textDateInput.setText(inputListItems.get(pos).getStartTime());
        holder.textVisitCounter.setText(inputListItems.get(pos).getVisitedRank()+"");
        if(inputListItems.get(pos).getDoctorId() == 0){
            holder.textNameInput.setText(inputListItems.get(pos).getChemistName());
        }
        else{
            holder.textNameInput.setText(inputListItems.get(pos).getDoctorName());
        }

    }


    public class MyInputItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView textNameInput;
        private TextView textDateInput;
        private Button buttonEditInput;
        private TextView textVisitCounter;



        MyInputItemHolder(View itemView) {
            super(itemView);
            textVisitCounter = itemView.findViewById(R.id.text_visit_counter);
            textNameInput = itemView.findViewById(R.id.text_name_input);
            textDateInput = itemView.findViewById(R.id.text_date_input);
            buttonEditInput = itemView.findViewById(R.id.button_edit_input);

            buttonEditInput.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.button_edit_input:
                    onInputItemClickListener.onInputItemClick(inputListItems.get(getAdapterPosition()));
                    break;
            }
        }
    }

    public interface OnInputItemClickListener{
        void onInputItemClick(DataItem item);
    }

}
