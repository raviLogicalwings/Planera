package com.planera.mis.planera2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.models.Doctors;


import java.util.List;

public class DoctorsListAdapter extends RecyclerView.Adapter<DoctorsListAdapter.MyDoctorsHolder> {
   private Context mContext;
   private List<Doctors> doctorsList;
   private OnItemClickListener onItemClickListener;
   protected View view;

    public DoctorsListAdapter(Context mContext, List<Doctors> doctorsList, OnItemClickListener onItemClickListener ) {
        this.mContext = mContext;
        this.doctorsList = doctorsList;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MyDoctorsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.item_doctors_view, parent, false);
        return new MyDoctorsHolder(view);
    }

    @Override
    public void onBindViewHolder(MyDoctorsHolder holder, int position) {
        loadItems(position, holder);

    }

    @Override
    public int getItemCount() {
        if(doctorsList!=null){
            return doctorsList.size();
        }
        else {
            return 0;
        }
    }

    public class MyDoctorsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView textDoctorName;
        private TextView textDoctorEmail;
        private TextView textQualification;
        private TextView textDoctorContact;
        private ImageView imageDelete;
        private ImageView imageEdit;

        public MyDoctorsHolder(View itemView) {
            super(itemView);
            imageDelete = itemView.findViewById(R.id.img_doctor_delete);
            imageEdit = itemView.findViewById(R.id.img_doctor_edit);
            textDoctorName = itemView.findViewById(R.id.text_doctor_name);
            textDoctorEmail = itemView.findViewById(R.id.text_doctor_email);
            textQualification = itemView.findViewById(R.id.text_qualification);
            textDoctorContact = itemView.findViewById(R.id.text_doctor_contact);

            imageEdit.setOnClickListener(this);
            imageDelete.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.img_doctor_delete:
                    onItemClickListener.onItemClick(view, doctorsList.get(getAdapterPosition()));
                    break;
                case R.id.img_doctor_edit:
                    onItemClickListener.onItemClick(view, doctorsList.get(getAdapterPosition()));
                    break;
            }
        }
    }

    public void updateList(List<Doctors> doctorsList){
        this.doctorsList = doctorsList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener{
         void onItemClick(View view, Doctors doctors);
    }



    public void loadItems(int position, MyDoctorsHolder holder){
        holder.textDoctorContact.setText(doctorsList.get(position).getPhone());
        holder.textDoctorEmail.setText(doctorsList.get(position).getEmail());
        holder.textDoctorName.setText(doctorsList.get(position).getFirstName()+" "+
        doctorsList.get(position).getMiddleName()+ " " +doctorsList.get(position).getLastName());
        holder.textQualification.setText(doctorsList.get(position).getQualifications());

    }
}
