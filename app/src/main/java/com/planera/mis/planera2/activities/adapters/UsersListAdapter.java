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
import com.planera.mis.planera2.activities.models.UserData;

import java.util.List;

public class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.MyUsersHolder>{
    private OnItemClickListener onItemClickListener;
    private View view;
    private Context mContext;
    private List<UserData> list;


    public UsersListAdapter(Context mContext, List<UserData> list,  OnItemClickListener onItemClickListener) {
        this.list  = list;
        this.mContext = mContext;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MyUsersHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(mContext).inflate(R.layout.item_users_view, viewGroup, false);

        return new MyUsersHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyUsersHolder myUsersHolder, int i) {
        bindView(myUsersHolder, i);

    }


    @Override
    public int getItemCount() {
       if(list.size()>0){
           return list.size();
       }
       else{
           return 0;
       }
    }

    public class MyUsersHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        private TextView textUserName;
        private TextView textUserEmail;
        private TextView textQualification;
        private TextView textUserContact;
        private ImageView imgUserDelete;
        private ImageView imgUserEdit;



        public MyUsersHolder(@NonNull View itemView) {
            super(itemView);
            textUserName = view.findViewById(R.id.text_user_name);
            textUserEmail = view.findViewById(R.id.text_user_email);
            textQualification = view.findViewById(R.id.text_qualification);
            textUserContact = view.findViewById(R.id.text_user_contact);
            imgUserDelete = view.findViewById(R.id.img_user_delete);
            imgUserEdit = view.findViewById(R.id.img_user_edit);

            imgUserDelete.setOnClickListener(this);
            imgUserEdit.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.img_user_delete:
                    onItemClickListener.onItemClick(v, getAdapterPosition());
                    break;

                case R.id.img_user_edit:
                    onItemClickListener.onItemClick(v, getAdapterPosition());
                    break;

            }

        }
    }


    public void bindView(MyUsersHolder holder, int pos){
        holder.textUserName.setText(list.get(pos).getFirstName()+" "+ list.get(pos).getMiddleName()+" "+list.get(pos).getLastName());
        holder.textUserEmail.setText(list.get(pos).getEmail1());
        holder.textUserContact.setText(list.get(pos).getPhone1());
        holder.textQualification.setText(list.get(pos).getQualifications());
    }


    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
}
