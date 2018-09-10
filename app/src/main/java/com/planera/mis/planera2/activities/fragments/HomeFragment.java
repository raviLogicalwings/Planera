package com.planera.mis.planera2.activities.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.gson.Gson;
import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.ProductCategoryActivity;
import com.planera.mis.planera2.activities.Retrofit.ApiClient;
import com.planera.mis.planera2.activities.Retrofit.ApiInterface;
import com.planera.mis.planera2.activities.ScheduleTimeActivity;
import com.planera.mis.planera2.activities.adapters.VisitsAdapter;
import com.planera.mis.planera2.activities.models.UserPlan;
import com.planera.mis.planera2.activities.models.UserPlanListRespnce;
import com.planera.mis.planera2.activities.utils.AppConstants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class HomeFragment extends BaseFragment implements View.OnClickListener {
    public static HomeFragment instance;

    private OnFragmentInteractionListener mListener;
    private RecyclerView visitList;
    private View mainView;
    private ApiInterface apiInterface;
    private List<UserPlan> plansList;
    private LocationRequest mLocationRequest;
    private Location mLocation;
    public static final int INTERVAL = 1000 * 60;
    public static final int FASTEST_INTERVAL = 1000 *30;
    private double visitLat;
    private double visitLong;
    int  isInLocation;
    double lat;
    double lng;
    int doctorId;
    int chemsitId;
    int planId;
    int userId;


    public HomeFragment() {
    }

    public static HomeFragment getInstance() {
        if (instance == null) {
            instance = new HomeFragment();
        }
        return instance;
    }


    @Override
    protected void initUi() {
        super.initUi();
        visitList = mainView.findViewById(R.id.visitList);


    }

    @Override
    protected void initData() {
        super.initData();
        apiInterface = ApiClient.getInstance();
        mLocationRequest = new LocationRequest();
        getLastLocation();
        startLocationUpdates();
        getAllPlansList(token);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }


    public void getAllPlansList(String token) {

        processDialog.showDialog(mContext, false);
        Call<UserPlanListRespnce> call = apiInterface.userPlanList(token);

        call.enqueue(new Callback<UserPlanListRespnce>() {
            @Override
            public void onResponse(Call<UserPlanListRespnce> call, Response<UserPlanListRespnce> response) {
                processDialog.dismissDialog();
                if (response.body().getStatusCode() == AppConstants.RESULT_OK) {
                    plansList = response.body().getData();

                    Log.e(" UserPlanList : ", new Gson().toJson(plansList));
                    if (plansList != null) {
                        initAdapter(plansList, visitList);


//                       visitLat =   plansList.get(1).getChemistLatitude();
//                      visitLong =  plansList.get(1).getChemistLongitude();
                    } else {
                        Snackbar.make(rootView, "No Plan Available", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<UserPlanListRespnce> call, Throwable t) {
                processDialog.dismissDialog();
                Snackbar.make(rootView, t.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }


    public void initAdapter(List<UserPlan> plansData, RecyclerView recyclerView) {
        VisitsAdapter adapter = new VisitsAdapter(mContext, plansData, mLocation, (view, position, isDoctor, dist) -> {
            switch (view.getId()){
                case R.id.button_check_in:
                    gotoScheduleTimeActivity(position, isDoctor, dist);
                    break;

            }
        });
        setAdapter(recyclerView, adapter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_home, container, false);
        initUi();
        initData();
        return mainView;
    }


    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        SettingsClient settingsClient = LocationServices.getSettingsClient(mContext);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        getFusedLocationProviderClient(mContext).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }

    public void onLocationChanged(Location location) {
        // New location has now been determined

        mLocation = location;

    }

    public void getLastLocation() {
        // Get last known recent location using new Google Play Services SDK (v11+)
        FusedLocationProviderClient locationClient = getFusedLocationProviderClient(mContext);

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        onLocationChanged(location);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d("MapDemoActivity", "Error trying to get last GPS location");
                    e.printStackTrace();
                });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onPause() {
        super.onPause();
        getFusedLocationProviderClient(mContext).removeLocationUpdates(new LocationCallback());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        getFusedLocationProviderClient(mContext).removeLocationUpdates(new LocationCallback());

    }

    public void setAdapter(RecyclerView recyclerView, VisitsAdapter adapter){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.hasFixedSize();

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void gotoScheduleTimeActivity(int pos, boolean isDoctor, float dist){

        if(dist<=1){
          isInLocation = AppConstants.USER_IN_LOCATION;
        }
        else{
            isInLocation = AppConstants.USER_NOT_IN_LOCATION;
        }
        Intent intent = new Intent(mContext, ScheduleTimeActivity.class);
        intent.putExtra(AppConstants.KEY_ROLE, isDoctor);
        intent.putExtra(AppConstants.KEY_IN_LOCATION, isInLocation);
        intent.putExtra(AppConstants.CUSTOMER_NAME, getCustomerName(pos, plansList, isDoctor));
        intent.putExtra(AppConstants.VISIT_DATE, plansList.get(pos).getMonth()+"/"+plansList.get(pos).getYear());
        intent.putExtra(AppConstants.LATITUDE, lat);
        intent.putExtra(AppConstants.DOCTOR_ID, plansList.get(pos).getDoctorId());
        intent.putExtra(AppConstants.CHEMIST_ID, plansList.get(pos).getChemistsId());
        intent.putExtra(AppConstants.LONGITUDE, lng);
        intent.putExtra(AppConstants.KEY_PLAN_ID, plansList.get(pos).getPlanId());
        intent.putExtra(AppConstants.KEY_USER_ID, plansList.get(pos).getUserId());
        startActivity(intent);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_check_in:
                Intent intent = new Intent(getContext(), ProductCategoryActivity.class);
                getActivity().startActivity(intent);
                break;
        }

    }


   public String getCustomerName(int pos, List<UserPlan> listPlans, boolean isDoctor){
        String customerName;
       if(isDoctor){
           customerName = listPlans.get(pos).getDoctorFirstName()
                   +" "+listPlans.get(pos).getDoctorMiddleName()
                   +" "+listPlans.get(pos).getDoctorLastName();
           lat = listPlans.get(pos).getDoctorLatitude();
           lng = listPlans.get(pos).getDoctorLongitude();

       }
       else{
           customerName = listPlans.get(pos).getChemistFirstName()
                   +" "+listPlans.get(pos).getChemistMiddleName()
                   +" "+listPlans.get(pos).getChemistLastName();
           lat = listPlans.get(pos).getChemistLatitude();
           lng = listPlans.get(pos).getChemistLongitude();

       }

        return customerName;
   }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
