package com.planera.mis.planera2.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.planera.mis.planera2.R;
import com.planera.mis.planera2.models.UserPlan;
import com.planera.mis.planera2.utils.AppConstants;
import com.planera.mis.planera2.utils.PreferenceConnector;

import java.util.Collections;
import java.util.List;

public class VisitsAdapter extends RecyclerView.Adapter<VisitsAdapter.VisitItemHolder> {
    private Context mContext;
    private Location location;
    private List<UserPlan> planList;
    private PreferenceConnector connector;
    private View view;
    private VisitItemHolder holder;
    private OnVisitAdapterClickListener onVisitAdapterClickListener;
    boolean isDoctor;
    double dist;
    boolean decideRole;


    public VisitsAdapter(Context context,  Location location, OnVisitAdapterClickListener onVisitAdapterClickListener) {
        mContext = context;
        this.location = location;
        this.onVisitAdapterClickListener = onVisitAdapterClickListener;
        connector  = new PreferenceConnector(context);

    }

    @NonNull
    @Override
    public VisitItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_visit_doctors, parent, false);
        return new VisitItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitItemHolder holder, int position) {

        onBindItems(holder, position);
    }

    @Override
    public int getItemCount() {
        if (planList != null) {
            return planList.size();
        } else {
            return 0;
        }

    }


    @SuppressLint("SetTextI18n")
    private void onBindItems(VisitItemHolder viewHolder, int position) {
        dist = planList.get(position).getDist();
        holder = viewHolder;

        if (connector.getInteger(AppConstants.USER_TYPE) == AppConstants.MR)
        {
            viewHolder.textJointworking.setVisibility(View.GONE);
        }
        else{
            viewHolder.textJointworking.setVisibility(View.VISIBLE);
        }

        if(planList.get(position).getChemistsId().equals("0")){

            if (planList.get(position).getDoctorFirstName() != null) {
                viewHolder.textName.setText(planList.get(position).getDoctorFirstName()
                        + " " + planList.get(position).getDoctorMiddleName() + " " +
                        planList.get(position).getDoctorLastName());
            String firstChar = getFirstName(planList.get(position).getDoctorFirstName());
            viewHolder.textNameFirstLetter.setText(firstChar.toUpperCase());
                viewHolder.textDistance.setText(Math.round(planList.get(position).getDist()) + " KM");
            }
        }
        else {
                viewHolder.textName.setText(planList.get(position).getChemistFirstName() + " " +
                        planList.get(position).getChemistMiddleName() + " " +
                        planList.get(position).getChemistLastName());
                String firstChar = getFirstName(planList.get(position).getChemistFirstName());
                viewHolder.textNameFirstLetter.setText(firstChar.toUpperCase());


//                float dist = calculateDistance(location, planList.get(position).getChemistLatitude(), planList.get(position).getChemistLongitude());
                viewHolder.textDistance.setText(Math.round(planList.get(position).getDist()) + " KM");


        }
        viewHolder.ratingBarDoctor.setRating(3.5f);
        viewHolder.textStatus.setText("");
        viewHolder.textAddress.setText(planList.get(position).getPatchName() + ", " +
                planList.get(position).getTerritoryName());

//        notifyJointUiUpdate();

    }

    public void notifyJointUiUpdate() {
        holder.imageJointSuccess.setVisibility(View.VISIBLE);
        holder.textJointworking.setVisibility(View.GONE);

    }


    class VisitItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textNameFirstLetter;
        private TextView textName;
        private TextView textAddress;
        private TextView textStatus;
        private Button buttonCheckIn;
        private TextView textDistance;
        private ImageView imageCurrentLocationStatus;
        private ImageView imageJointSuccess;
        private RatingBar ratingBarDoctor;
        private TextView textJointworking;
        private LinearLayout layoutIsJoint;


        VisitItemHolder(View itemView) {
            super(itemView);
            ratingBarDoctor = itemView.findViewById(R.id.rating_bar_doctor);
            buttonCheckIn = itemView.findViewById(R.id.button_check_in);
            textNameFirstLetter = itemView.findViewById(R.id.text_name_first_letter);
            textName = itemView.findViewById(R.id.text_name);
            textAddress = itemView.findViewById(R.id.text_address);
            textStatus = itemView.findViewById(R.id.text_status);
            buttonCheckIn = itemView.findViewById(R.id.button_check_in);
            textDistance = itemView.findViewById(R.id.text_distance);
            imageCurrentLocationStatus = itemView.findViewById(R.id.image_current_location_status);
            imageJointSuccess = itemView.findViewById(R.id.icon_joint_success);
            textJointworking = itemView.findViewById(R.id.text_joint);
            layoutIsJoint = itemView.findViewById(R.id.layout_is_joint);
            imageCurrentLocationStatus.setOnClickListener(this);
            buttonCheckIn.setOnClickListener(this);
            textJointworking.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_check_in:
                     decideRole = whichRole(getAdapterPosition());
                    onVisitAdapterClickListener.onVisitClick(v, getAdapterPosition(), decideRole, dist);
                    break;
                case R.id.image_current_location_status:
                    showOnMap(getAdapterPosition());
                    break;

                case R.id.text_joint:
                    onVisitAdapterClickListener.onJointClick(getAdapterPosition());
                    break;
            }

        }
    }


   private void  showOnMap(int pos){
       decideRole = whichRole(pos);
        if (decideRole){
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?saddr="+location.getLatitude()+","+location.getLongitude()+"&daddr="+planList.get(pos).getDoctorLatitude()+","+planList.get(pos).getDoctorLongitude()+""));
            mContext.startActivity(intent);
        }
        else{
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?saddr="+location.getLatitude()+","+location.getLongitude()+"&daddr="+planList.get(pos).getChemistLatitude()+","+planList.get(pos).getChemistLongitude()+""));
            mContext.startActivity(intent);
        }

   }
    public void setList(List<UserPlan> planList){
        this.planList = planList;

        Collections.sort(planList, (item, t1) -> {
            double dist1;
            double dist2;
            dist1 = item.getDist();
            dist2 = t1.getDist();
            return Double.compare(dist1, dist2);
        });

        notifyDataSetChanged();
    }

    private boolean whichRole(int pos){

        if (planList.get(pos).getChemistsId().equals("0")){
           return isDoctor = true;
        }
        else{
           return isDoctor = false;
        }
    }


    public String getFirstName(String name){
            char ch[] = name.toCharArray();
            return ch[0]+"";
    }

    private float calculateDistance(Location mLocation, double lat2, double lon2) {
        float[] results = new float[1];
        Location.distanceBetween(mLocation.getLatitude(), mLocation.getLongitude(),
                lat2, lon2,
                results);
        return results[0] / 1000;
    }


    public interface OnVisitAdapterClickListener {
        void onVisitClick(View view, int position, boolean isDoctor, double dist);
        void onJointClick(int position);
    }
}
