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
import com.planera.mis.planera2.models.AMs;

import java.util.List;

public class AreaManagersAdapter extends RecyclerView.Adapter<AreaManagersAdapter.MyViewHolder> {
    private View view;
    private List<AMs> listAreaManagers;
    private Context context;
    private OnSelcetAreaManagerListener listener;

    private int lastSelection = -1;


    public AreaManagersAdapter(Context context, OnSelcetAreaManagerListener listener){
        this.listener = listener;
        this.context = context;

    }

    public void setListAreaManagers(List<AMs> listAreaManagers) {
        this.listAreaManagers = listAreaManagers;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.item_joint_am, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textAMName.setText(listAreaManagers.get(holder.getAdapterPosition()).getFirstName());

        /**
         *
         * Random Selection form recycler view(SELECT ONLY ONE ITEM)
         * */
        holder.radioButtonAreaManagers.setChecked(lastSelection == position);
    }

    @Override
    public int getItemCount() {
        if (listAreaManagers != null && listAreaManagers.size()>0 ){
            return listAreaManagers.size();
        }
        else
            return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView textAMName;
        private RadioButton radioButtonAreaManagers;
        MyViewHolder(View itemView) {
            super(itemView);

            textAMName = itemView.findViewById(R.id.text_user_name_joint);
            radioButtonAreaManagers = itemView.findViewById(R.id.radio_joint_user);
            radioButtonAreaManagers.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.radio_joint_user:
                    lastSelection = getAdapterPosition();
                    listener.onSelect(lastSelection);
                    notifyDataSetChanged();
                    break;
            }
        }
    }


    public interface OnSelcetAreaManagerListener{
        void onSelect(int position);
    }
}
