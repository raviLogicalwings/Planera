package com.planera.mis.planera2.activities.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.models.DataItem;

import java.util.List;

public class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.MyReportsHolder>{
    private List<DataItem> dataItemList;
    private Context context;
    private View view;
    private OnItemDeleteListener listener;


    public ReportListAdapter(List<DataItem> dataItemList, Context context, OnItemDeleteListener listener){
        this.dataItemList = dataItemList;
        this.context = context;
        this.listener = listener;
    }
    @NonNull
    @Override
    public MyReportsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(context).inflate(R.layout.item_view_reports, viewGroup, false);
        return new MyReportsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyReportsHolder myReportsAdapter, int i) {
        bindItems(myReportsAdapter, i);
    }


    public void bindItems(MyReportsHolder holder, int pos){
        if (dataItemList!= null){
            holder.textUserName.setText(dataItemList.get(pos).getChemistName());
            holder.textQualification.setText(dataItemList.get(pos).getEndDate());
            holder.textUserEmail.setText(dataItemList.get(pos).getStartDate());
            holder.textUserContact.setText(dataItemList.get(pos).getComment());
        }

    }

    @Override
    public int getItemCount() {
        if (dataItemList.size()>0){
            return dataItemList.size();
        }
        else{
            return 0;
        }
    }

    public class MyReportsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView textUserName;
        private TextView textUserEmail;
        private TextView textQualification;
        private TextView textUserContact;




        public MyReportsHolder(@NonNull View itemView) {
            super(itemView);

            textUserName = itemView.findViewById(R.id.text_user_name);
            textUserEmail = itemView.findViewById(R.id.text_user_email);
            textQualification = itemView.findViewById(R.id.text_qualification);
            textUserContact = itemView.findViewById(R.id.text_user_contact);



        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){

            }

        }
    }



    public interface OnItemDeleteListener{
        void onItemDelete(int pos);
    }
}
