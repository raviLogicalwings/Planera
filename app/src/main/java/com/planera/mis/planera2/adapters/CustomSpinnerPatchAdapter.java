package com.planera.mis.planera2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.models.Territories;


import java.util.List;

public class CustomSpinnerPatchAdapter extends BaseAdapter{
    private Context mContext;
    private List<Territories> territoriesList;
    private LayoutInflater inflater;



    public CustomSpinnerPatchAdapter(Context mContext, List<Territories> territoriesList) {
        this.mContext = mContext;
        this.territoriesList = territoriesList;
    }

    @Override
    public int getCount() {
        if (territoriesList!=null){
            return territoriesList.size();
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
        private TextView textViewPatch;
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
                holder.textViewPatch = (TextView) myView.findViewById(R.id.text_state);
                holder.imgDelete =  myView.findViewById(R.id.img_delete);
                holder.imgEdit =  myView.findViewById(R.id.img_edit);
                myView.setTag(holder);
            } else {
                holder = (Holder) myView.getTag();
            }

            holder.textViewPatch.setText(territoriesList.get(i).getTerritoryName());
            holder.imgDelete.setVisibility(View.GONE);
            holder.imgEdit.setVisibility(View.GONE);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return myView;
    }

}