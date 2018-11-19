package com.planera.mis.planera2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.models.States;


import java.util.List;

public class CustomSpinnerTerritoryAdapter extends BaseAdapter{
    private Context mContext;
    private List<States> statesList;
    private LayoutInflater inflater;



    public CustomSpinnerTerritoryAdapter(Context mContext, List<States> statesList) {
        this.mContext = mContext;
        this.statesList = statesList;
    }

    @Override
    public int getCount() {
       if (statesList!=null){
           return statesList.size();
       }
       else
           return 0;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class Holder{
        private TextView tvCountryName;
        private ImageView imgDelete;
        private ImageView imgEdit;



    }

    @Override    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View myView = null;
        try {
            Holder holder;
            myView = convertView;

            if (myView == null) {
                inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                myView = inflater.inflate(R.layout.item_states_list, null);

                holder = new Holder();
                holder.tvCountryName = (TextView) myView.findViewById(R.id.text_state);
                holder.imgDelete =  myView.findViewById(R.id.img_delete);
                holder.imgEdit =  myView.findViewById(R.id.img_edit);
                myView.setTag(holder);
            } else {
                holder = (Holder) myView.getTag();
            }

            holder.tvCountryName.setText(statesList.get(i).getName());
            holder.imgDelete.setVisibility(View.GONE);
            holder.imgEdit.setVisibility(View.GONE);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return myView;
    }

}