package com.planera.mis.planera2.activities.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.models.Territories;

import java.util.List;

public class TerritoryAdapter extends RecyclerView.Adapter<TerritoryAdapter.MyTerritoryHolder> {
private Context mContext;
public View view;
private List<Territories> territoryList;
private OnItemClickListener onItemClickListener;

    public TerritoryAdapter(Context context, List<Territories> territoryList, OnItemClickListener onItemClickListener){
        mContext = context;
        this.territoryList = territoryList;
        this.onItemClickListener = onItemClickListener;
    }
    @Override
    public MyTerritoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.item_territory_list, parent, false);
        return new MyTerritoryHolder(view);
    }

    @Override
    public void onBindViewHolder(MyTerritoryHolder holder, int position) {
        holder.textTerritory.setText(territoryList.get(position).getTerritoryName());
        holder.textState.setText(territoryList.get(position).getStateName());

    }

    @Override
    public int getItemCount() {
        if (territoryList !=null){
            return territoryList.size();
        }
       else{
            return 0;
        }
    }

    public class MyTerritoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView textTerritory;
        private TextView textState;
        private ImageView imgDelete;
        private ImageView imgEdit;


        public MyTerritoryHolder(View itemView) {
            super(itemView);
            textTerritory = itemView.findViewById(R.id.text_territory);
            textState = itemView.findViewById(R.id.text_state);
            imgDelete = itemView.findViewById(R.id.img_delete);
            imgEdit = itemView.findViewById(R.id.img_edit);
            textTerritory.setVisibility(View.VISIBLE);

            imgDelete.setOnClickListener(this);
            imgEdit.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.img_delete:
                    onItemClickListener.onItemClick(view, getAdapterPosition());
                    break;
                case R.id.img_edit:
                    onItemClickListener.onItemClick(imgEdit,getAdapterPosition());
                    break;
            }
        }
    }

    public interface OnItemClickListener{

       void onItemClick(View view, int position);
    }
}
