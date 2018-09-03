package com.planera.mis.planera2.activities.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.models.Plans;

import java.util.List;

public class PlanListAdapter  extends RecyclerView.Adapter<PlanListAdapter.MyPlansHolder>{
   private View view;
   private Context context;
   private List<Plans> plansList;
   private OnItemClickListener onItemClickListener;

    public PlanListAdapter( Context context, List<Plans> plansList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.plansList = plansList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MyPlansHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view   = LayoutInflater.from(context).inflate(R.layout.item_plans_view, viewGroup, false);
        return new MyPlansHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPlansHolder myPlansHolder, int i) {
        bindItems(i, myPlansHolder);
    }

    @Override
    public int getItemCount() {
        if(plansList.size()>0){
            return plansList.size();
        }
        else{
            return 0;

        }
    }

    public class MyPlansHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView textPlanPatchName;
        private TextView textPlanDate;
        private TextView textPlanCalls;
        private TextView textPlanRemark;
        private ImageView imgPlanDelete;
        private ImageView imgPlanEdit;


        public MyPlansHolder(@NonNull View itemView) {
            super(itemView);

            textPlanPatchName = itemView.findViewById(R.id.text_plan_patch_name);
            textPlanDate = itemView.findViewById(R.id.text_plan_date);
            textPlanCalls = itemView.findViewById(R.id.text_plan_calls);
            textPlanRemark = itemView.findViewById(R.id.text_plan_remark);
            imgPlanDelete = itemView.findViewById(R.id.img_plan_delete);
            imgPlanEdit = itemView.findViewById(R.id.img_plan_edit);

            imgPlanEdit.setOnClickListener(this);
            imgPlanDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.img_plan_delete:
                    onItemClickListener.onItemCLick(getAdapterPosition(), v);
                    break;
                case R.id.img_plan_edit:
                    onItemClickListener.onItemCLick(getAdapterPosition(), v);
                    break;
            }
        }
    }


    public void bindItems(int pos, MyPlansHolder parentView){
        parentView.textPlanCalls.setText("Calls "+plansList.get(pos).getCalls());
        parentView.textPlanPatchName.setText(plansList.get(pos).getPatchName());
        parentView.textPlanRemark.setText(plansList.get(pos).getRemark());
        parentView.textPlanDate.setText(plansList.get(pos).getMonth()+":"+plansList.get(pos).getYear());
    }


    public interface OnItemClickListener{

        void onItemCLick(int position, View view);
    }
}
