package com.planera.mis.planera2.activities.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.ScheduleTimeActivity;

import java.util.List;

public class VisitsAdapter extends RecyclerView.Adapter<VisitsAdapter.VisitItemHolder> {
    private Context mContext;
    private List<Integer> list;
    private View view;


    public VisitsAdapter(Context context){
        mContext = context;
    }
    @Override
    public VisitItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_visit_doctors, parent, false);
        return new VisitItemHolder(view);
    }

    @Override
    public void onBindViewHolder(VisitItemHolder holder, int position) {
        holder.buttonCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentProduct = new Intent(mContext, ScheduleTimeActivity.class);
                mContext.startActivity(intentProduct);
            }
        });


    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class VisitItemHolder extends RecyclerView.ViewHolder {
        private  Button buttonCheckIn;
        public VisitItemHolder(View itemView) {
            super(itemView);
            buttonCheckIn= itemView.findViewById(R.id.button_check_in);
        }
    }
}
