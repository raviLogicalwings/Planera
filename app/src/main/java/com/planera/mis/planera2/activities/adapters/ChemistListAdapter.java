package com.planera.mis.planera2.activities.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.models.Chemists;


import java.util.List;

public class ChemistListAdapter extends RecyclerView.Adapter<ChemistListAdapter.MyChemistHolder> {
    private Context mContext;
    private List<Chemists> chemists;
    private OnItemClickListener onItemClickListener;
    protected View view;

    public ChemistListAdapter(Context mContext, List<Chemists> chemists, OnItemClickListener onItemClickListener ) {
        this.mContext = mContext;
        this.chemists = chemists;
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
        if(chemists!=null){
            return chemists.size();
        }
        else {
            return 0;
        }
    }

    public class MyChemistHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView textChemistName;
        private TextView textChemistEmail;
        private TextView textQualification;
        private TextView textChemistContact;
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
                    onItemClickListener.onItemClick(view, getAdapterPosition());
                    break;
                case R.id.img_chemist_edit:
                    onItemClickListener.onItemClick(view, getAdapterPosition());
                    break;
            }
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }



    public void loadItems(int position, MyChemistHolder holder){
        holder.textChemistContact.setText(chemists.get(position).getPhone());
        holder.textChemistEmail.setText(chemists.get(position).getEmail());
        holder.textChemistName.setText(chemists.get(position).getFirstName()+" "+
                chemists.get(position).getMiddleName()+ " " +chemists.get(position).getLastName());
        holder.textQualification.setText(chemists.get(position).getCompanyName());

    }
}
