package com.planera.mis.planera2.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.models.MRs;

import java.util.List;

public class UserListPatchesAdapter extends RecyclerView.Adapter<UserListPatchesAdapter.ListHolder>{
    private Context context;
    private List<MRs> mrDataList;
    private View view;

    private int lastSelection = -1;

    private OnUserSelectionListener listener;

    public UserListPatchesAdapter(Context context, OnUserSelectionListener listener){
        this.context = context;
        this.listener = listener;
    }
    public void setMrDataList(List<MRs> mrDataList) {
        this.mrDataList = mrDataList;
    }

    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.items_joint_users, parent, false);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListHolder holder, int position) {
        holder.textUserName.setText(mrDataList.get(holder.getAdapterPosition()).getFirstName());
        holder.radioSelectUser.setChecked(lastSelection == position);
    }

    @Override
    public int getItemCount() {
       if (mrDataList.size()>0)
           return mrDataList.size();
       else
           return 0;

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public View getView() {
        return view;
    }

    @Override
    public void onViewRecycled(@NonNull ListHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView textUserName;
        private RadioButton radioSelectUser;
        ListHolder(View itemView) {
            super(itemView);
            textUserName =  itemView.findViewById(R.id.text_user_name_joint);
            radioSelectUser = itemView.findViewById(R.id.radio_joint_user);
            radioSelectUser.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.radio_joint_user:
                    lastSelection = getAdapterPosition();
                    listener.onUserSelection(lastSelection);
                    notifyDataSetChanged();
                    break;
            }
        }
    }

    public interface OnUserSelectionListener{
        void onUserSelection(int pos);
    }
}
