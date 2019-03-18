package com.planera.mis.planera2.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.planera.mis.planera2.FragmentDialog.UsersListDialog;
import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.AddInputActivity;
import com.planera.mis.planera2.activities.UserInputListActivity;
import com.planera.mis.planera2.models.DataItem;
import com.planera.mis.planera2.models.MRs;
import com.planera.mis.planera2.utils.AppConstants;
import com.planera.mis.planera2.utils.PreferenceConnector;

import java.util.List;

public class InputListAdapter extends RecyclerView.Adapter<InputListAdapter.MyInputItemHolder> {
    private int lastPos = -1;
    private List<DataItem> inputListItems;
    private OnInputItemClickListener onInputItemClickListener;
    public PreferenceConnector connector;
    private UsersListDialog dialogSelectUser;
    private View mView;
    private Context context;
    private DataItem item;
    private List<MRs> jointUserList;



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


    private void bindItemsWithView(MyInputItemHolder holder, int pos) {
        item = inputListItems.get(pos);
            if (inputListItems.get(holder.getAdapterPosition()).getIsJoint() == AppConstants.JOINT_WORKING)
            {
                holder.textIsJoint.setVisibility(View.VISIBLE);
                holder.textReplaceUser.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.textIsJoint.setVisibility(View.GONE);
                holder.textReplaceUser.setVisibility(View.GONE);
            }


        holder.textDateInput.setText(new AddInputActivity().convertIntoDD_MM_YYYY(item.getVisitDate()));
        holder.textTimeInput.setText(item.getStartTime());
        if (inputListItems.get(holder.getAdapterPosition()).getJointUserList() != null &&
                inputListItems.get(holder.getAdapterPosition()).getJointUserList().size() > 0  )
        {
//            holder.textJointAmName.setText(inputListItems.get(holder.getAdapterPosition()).getJointUserList().get(1).getFirstName());
            holder.textJointMrName.setVisibility(View.VISIBLE);
            holder.textJointMrName.setText(inputListItems.get(holder.getAdapterPosition()).getJointUserList().get(0).getFirstName());
            if(inputListItems.get(holder.getAdapterPosition()).getJointUserList().size()>  0){
                holder.textJointAmName.setVisibility(View.VISIBLE);
                holder.textJointAmName.setText(inputListItems.get(holder.getAdapterPosition()).getJointUserList().get(1).getFirstName());

            }
        }
//        holder.textVisitCounter.setText(item.getVisitedRank());

        if(item.getDoctorId() == 0){
            holder.textNameInput.setText(item.getChemistName());
        }
        else{
            holder.textNameInput.setText(item.getDoctorName());
        }

    }

    public void setJointUserList(List<MRs> jointUserList) {
        inputListItems.get(lastPos).setJointUserList(jointUserList);
    }

    public class MyInputItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView textNameInput;
        private TextView textDateInput;
        private Button buttonEditInput;
        private TextView textTimeInput;
        private TextView textVisitCounter;
        private TextView textIsJoint;
        private TextView textReplaceUser;
        private TextView textJointMrName;
        private TextView textJointAmName;



        MyInputItemHolder(View itemView) {
            super(itemView);
            textVisitCounter = itemView.findViewById(R.id.text_visit_counter);
            textNameInput = itemView.findViewById(R.id.text_name_input);
            textDateInput = itemView.findViewById(R.id.text_date_input);
            buttonEditInput = itemView.findViewById(R.id.button_edit_input);
            textTimeInput = itemView.findViewById(R.id.text_time_input);
            textIsJoint = itemView.findViewById(R.id.text_joint_info);
            textReplaceUser = itemView.findViewById(R.id.text_replace_user);
            textJointAmName = itemView.findViewById(R.id.text_joint_am_name);
            textJointMrName = itemView.findViewById(R.id.text_joint_mr_name);

            textReplaceUser.setOnClickListener(this);
            buttonEditInput.setOnClickListener(this);

            textJointMrName.setVisibility(View.GONE);
            textJointAmName.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.button_edit_input:
                    onInputItemClickListener.onInputItemClick(inputListItems.get(getAdapterPosition()));
                    break;

                case R.id.text_replace_user:
                    lastPos = getAdapterPosition();
                    replaceUser(inputListItems.get(lastPos).getIsJoint(),
                            inputListItems.get(lastPos).getPatchId());
                    break;
            }
        }
    }

    private void replaceUser(int isJoint, int patchId) {
        dialogSelectUser = new UsersListDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.KEY_IS_JOINT, isJoint);
        bundle.putInt(AppConstants.PATCH_ID, patchId);
        dialogSelectUser.setArguments(bundle);
        dialogSelectUser.show(((UserInputListActivity)context).getSupportFragmentManager(), "Replace User");

    }

    public interface OnInputItemClickListener{
        void onInputItemClick(DataItem item);
    }

}
