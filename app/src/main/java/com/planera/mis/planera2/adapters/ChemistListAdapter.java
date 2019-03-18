package com.planera.mis.planera2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.models.Chemists;

import java.util.List;

public class ChemistListAdapter extends RecyclerView.Adapter<ChemistListAdapter.MyChemistHolder> {
    private Context mContext;
    private List<Chemists> chemistsList;
    private OnItemClickListener onItemClickListener;
    protected View view;

    public ChemistListAdapter(Context mContext, List<Chemists> chemists, OnItemClickListener onItemClickListener ) {
        this.mContext = mContext;
        this.chemistsList = chemists;
        this.onItemClickListener = onItemClickListener;
    }



    @Override
    public MyChemistHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.item_chemists_view, parent, false);
        return new MyChemistHolder(view);
    }

    @Override
    public void onBindViewHolder(MyChemistHolder holder, int position) {
        loadItems(position, holder);

    }

    @Override
    public int getItemCount() {
        if(chemistsList !=null){
            return chemistsList.size();
        }
        else {
            return 0;
        }
    }

    public void updateList(List<Chemists> chemists) {
        this.chemistsList = chemists;
        notifyDataSetChanged();
    }

    public class MyChemistHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView textChemistName;
        private TextView textChemistEmail;
        private TextView textQualification;
        private TextView textChemistContact;
        private TextView textPatchName;
        private ImageView imageDelete;
        private ImageView imageEdit;




        public MyChemistHolder(View itemView) {
            super(itemView);
            imageDelete = itemView.findViewById(R.id.img_chemist_delete);
            imageEdit = itemView.findViewById(R.id.img_chemist_edit);
            textChemistName = itemView.findViewById(R.id.text_chemist_name);
            textChemistEmail = itemView.findViewById(R.id.text_chemist_email);
            textQualification = itemView.findViewById(R.id.text_qualification);
            textChemistContact = itemView.findViewById(R.id.text_chemist_contact);

            imageEdit.setOnClickListener(this);
            imageDelete.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.img_chemist_delete:
                    onItemClickListener.onItemClick(view, chemistsList.get(getAdapterPosition()));
                    break;
                case R.id.img_chemist_edit:
                    onItemClickListener.onItemClick(view, chemistsList.get(getAdapterPosition()));
                    break;
            }
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view, Chemists chemists);
    }



    public void loadItems(int position, MyChemistHolder holder){
        holder.textChemistContact.setText(chemistsList.get(position).getPhone());
        holder.textChemistEmail.setText(chemistsList.get(position).getEmail());
        holder.textChemistName.setText(chemistsList.get(position).getFirstName()+" "+
                chemistsList.get(position).getMiddleName()+ " " + chemistsList.get(position).getLastName());
        holder.textQualification.setText(chemistsList.get(position).getCompanyName());

    }
}
