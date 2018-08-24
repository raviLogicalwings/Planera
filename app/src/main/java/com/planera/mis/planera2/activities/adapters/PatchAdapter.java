package com.planera.mis.planera2.activities.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.models.Patches;



import java.util.List;

public class PatchAdapter extends RecyclerView.Adapter<PatchAdapter.MyPatchHolder> {
    private Context mContext;
    public View view;
    private List<Patches> patchesList;
    public OnItemClickListener onItemClickListener;

    public PatchAdapter(Context context, List<Patches> patchesList, OnItemClickListener onItemClickListener){
        mContext = context;
        this.patchesList = patchesList;
        this.onItemClickListener = onItemClickListener;
    }
    @Override
    public MyPatchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.item_territory_list, parent, false);
        return new MyPatchHolder(view);
    }

    @Override
    public void onBindViewHolder(MyPatchHolder holder, int position) {
        holder.textPatch.setText(patchesList.get(position).getPatchName());
        holder.textTerritory.setText(patchesList.get(position).getTerritoryName());

    }

    @Override
    public int getItemCount() {
        if (patchesList !=null){
            return patchesList.size();
        }
        else{
            return 0;
        }
    }

    public class MyPatchHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView textPatch;
        private TextView textTerritory;
        private ImageView imgDelete;
        private ImageView imgEdit;


        public MyPatchHolder(View itemView) {
            super(itemView);
            textPatch = itemView.findViewById(R.id.text_territory);
            textTerritory = itemView.findViewById(R.id.text_state);
            imgDelete = itemView.findViewById(R.id.img_delete);
            imgEdit = itemView.findViewById(R.id.img_edit);
            textPatch.setVisibility(View.VISIBLE);

            imgDelete.setOnClickListener(this);
            imgEdit.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.img_delete:
                    onItemClickListener.onItemView(view, getAdapterPosition());
                    break;
                case R.id.img_edit:
                    onItemClickListener.onItemView(view, getAdapterPosition());
                    break;
            }
        }
    }

    public interface OnItemClickListener{
        public void onItemView(View view, int position);
    }
}
