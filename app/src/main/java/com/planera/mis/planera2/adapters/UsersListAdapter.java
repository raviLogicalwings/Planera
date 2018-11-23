package com.planera.mis.planera2.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.models.UserData;

import java.util.List;

public class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.MyUsersHolder>{
    private OnItemClickListener onItemClickListener;
    private View view;
    private Context mContext;
    private List<UserData> userDataList;


    public UsersListAdapter(Context mContext, List<UserData> list,  OnItemClickListener onItemClickListener) {
        this.userDataList = list;
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
       if(userDataList.size()>0){
           return userDataList.size();
       }
       else{
           return 0;
       }
    }

    public void updateList(List<UserData> userDataList){
        this.userDataList = userDataList;
        notifyDataSetChanged();
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
                    onItemClickListener.onItemClick(v, userDataList.get(getAdapterPosition()));
                    break;

                case R.id.img_user_edit:
                    onItemClickListener.onItemClick(v, userDataList.get(getAdapterPosition()));
                    break;

            }

        }
    }


    public void bindView(MyUsersHolder holder, int pos){
        String userName = userDataList.get(pos).getFirstName();
        if (userDataList.get(pos).getMiddleName() != null){
            userName += userDataList.get(pos).getMiddleName();
        }
        if (userDataList.get(pos).getLastName() != null){
            userName += userDataList.get(pos).getLastName();
        }
        holder.textUserName.setText(userName);
        holder.textUserEmail.setText(userDataList.get(pos).getEmail1());
        holder.textUserContact.setText(userDataList.get(pos).getPhone1());
        holder.textQualification.setText(userDataList.get(pos).getQualifications());
    }



    public interface OnItemClickListener{
        void onItemClick(View view, UserData user);
    }
}
