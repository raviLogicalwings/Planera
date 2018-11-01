package com.planera.mis.planera2.activities.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.models.UserPlan;

import java.util.List;

public class VisitsAdapter extends RecyclerView.Adapter<VisitsAdapter.VisitItemHolder> {
    private Context mContext;
    private Location location;
    private List<UserPlan> planList;
    private View view;
    private OnVisitAdapterClickListener onVisitAdapterClickListener;
    boolean isDoctor;
    float dist;


    public VisitsAdapter(Context context, List<UserPlan> planList, Location location, OnVisitAdapterClickListener onVisitAdapterClickListener) {
        mContext = context;
        this.planList = planList;
        this.location = location;
        this.onVisitAdapterClickListener = onVisitAdapterClickListener;

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

        if(planList.get(position).getChemistsId().equals("0")){
            if (planList.get(position).getDoctorFirstName() != null) {
                viewHolder.textName.setText(planList.get(position).getDoctorFirstName()
                        + " " + planList.get(position).getDoctorMiddleName() + " " +
                        planList.get(position).getDoctorLastName());
            String firstChar = getFirstName(planList.get(position).getDoctorFirstName());
            viewHolder.textNameFirstLetter.setText(firstChar.toUpperCase());

                dist = calculateDistance(location,
                        planList.get(position).getDoctorLatitude(),
                        planList.get(position).getDoctorLongitude());
                viewHolder.textDistance.setText(Math.round(dist) + " KM");
            }
        }
        else {
                viewHolder.textName.setText(planList.get(position).getChemistFirstName() + " " +
                        planList.get(position).getChemistMiddleName() + " " +
                        planList.get(position).getChemistLastName());
                String firstChar = getFirstName(planList.get(position).getChemistFirstName());
                viewHolder.textNameFirstLetter.setText(firstChar.toUpperCase());


                float dist = calculateDistance(location, planList.get(position).getChemistLatitude(), planList.get(position).getChemistLongitude());
                viewHolder.textDistance.setText(Math.round(dist) + " KM");


        }
        viewHolder.ratingBarDoctor.setRating(3.5f);
        viewHolder.textStatus.setText("");
        viewHolder.textAddress.setText(planList.get(position).getPatchName() + ", " +
                planList.get(position).getTerritoryName());

    }


    class VisitItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textNameFirstLetter;
        private TextView textName;
        private TextView textAddress;
        private TextView textStatus;
        private Button buttonCheckIn;
        private TextView textDistance;
        private ImageView imageCurrentLocationStatus;
        private RatingBar ratingBarDoctor;


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

            buttonCheckIn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_check_in:
                    boolean decideRole = whichRole(getAdapterPosition());
                    onVisitAdapterClickListener.onVisitClick(v, getAdapterPosition(), decideRole, dist);
                    break;
            }

        }
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
        void onVisitClick(View view, int position, boolean isDoctor, float dist);
    }
}
