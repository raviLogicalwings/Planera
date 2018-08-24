package com.planera.mis.planera2.activities.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.models.Patches;


import java.util.List;

public class BasicCustomAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater inflater;
    private List<Patches> patchesList;


    public BasicCustomAdapter(Context mContext, List<Patches> patchesList){
        this.mContext = mContext;
        this.patchesList = patchesList;
    }

    @Override
    public int getCount() {
        if (patchesList!= null){
            return patchesList.size();
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
        private TextView basicText;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View myView = null;
        try {
            Holder holder;
            myView = convertView;

            if (myView == null) {
                inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                myView = inflater.inflate(R.layout.basic_layout_spinner, null);

                holder = new Holder();
                holder.basicText = myView.findViewById(R.id.basic_text);

                myView.setTag(holder);
            } else {
                holder = (Holder) myView.getTag();
            }
                holder.basicText.setText(patchesList.get(i).getPatchName());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return myView;
    }

}