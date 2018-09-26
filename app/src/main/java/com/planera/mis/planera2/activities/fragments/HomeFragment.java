package com.planera.mis.planera2.activities.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.Gson;
import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.AddInputActivity;
import com.planera.mis.planera2.activities.ProductCategoryActivity;
import com.planera.mis.planera2.activities.Retrofit.ApiClient;
import com.planera.mis.planera2.activities.Retrofit.ApiInterface;
import com.planera.mis.planera2.activities.adapters.VisitsAdapter;
import com.planera.mis.planera2.activities.models.UserPlan;
import com.planera.mis.planera2.activities.models.UserPlanListRespnce;
import com.planera.mis.planera2.activities.utils.AppConstants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class HomeFragment extends BaseFragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {
    public static HomeFragment instance;

    private OnFragmentInteractionListener mListener;
    private RecyclerView visitList;
    private View mainView;
    private ApiInterface apiInterface;
    private List<UserPlan> plansList;
    private LocationRequest mLocationRequest;
    private final static int REQUEST_CHECK_SETTINGS_GPS=0x1;
    private Location mLocation;
    public static final int INTERVAL = 1000 * 60;
    public static final int FASTEST_INTERVAL = 1000 *30;
    int  isInLocation;
    double lat;
    double lng;
    private GoogleApiClient googleApiClient;


    public HomeFragment() {
    }

//    public static HomeFragment getInstance() {
//        if (instance == null) {
//            instance = new HomeFragment();
//        }
//        return instance;
//    }


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
        setUpGClient();

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



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onPause() {
        super.onPause();
        googleApiClient.disconnect();

    }

    @Override
    public void onResume() {
        super.onResume();
        googleApiClient.connect();
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

    private synchronized void setUpGClient() {
        googleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }


    private void getMyLocation(){
        if(googleApiClient!=null) {
            if (googleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    mLocation =                     LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
//                    onLocationChanged(mLocation);
                    mLocationRequest = new LocationRequest();
                    mLocationRequest.setInterval(INTERVAL);
                    mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
                    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


                    LocationServices.FusedLocationApi
                            .requestLocationUpdates(googleApiClient, mLocationRequest, this);
                }
            }
        }
    }


    public void checkLocationSettings(){
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi
                        .checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(result1 -> {
            final Status status = result1.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    // All location settings are satisfied.
                    // You can initialize location requests here.
                    int permissionLocation = ContextCompat
                            .checkSelfPermission(mContext,
                                    Manifest.permission.ACCESS_FINE_LOCATION);
                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                       getMyLocation();
                    }
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    // Location settings are not satisfied.
                    // But could be fixed by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        // Ask to turn on GPS automatically
                        status.startResolutionForResult(getActivity(),
                                REQUEST_CHECK_SETTINGS_GPS);
                    } catch (IntentSender.SendIntentException e) {
                        Log.e("Location Setting", e.getMessage());
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    // Location settings are not satisfied.
                    // However, we have no way
                    // to fix the
                    // settings so we won't show the dialog.
                    // finish();
                    break;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS_GPS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        getMyLocation();
                        break;
                    case Activity.RESULT_CANCELED:

                        break;
                }
                break;
        }
    }

    public void gotoScheduleTimeActivity(int pos, boolean isDoctor, float dist){

        if(dist<=1){
          isInLocation = AppConstants.USER_IN_LOCATION;
        }
        else{
            isInLocation = AppConstants.USER_NOT_IN_LOCATION;
        }
        Intent intent = new Intent(mContext, AddInputActivity.class);
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checkLocationSettings();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        Log.e("Current Location", mLocation.getLatitude()+" "+mLocation.getLongitude());
        getAllPlansList(token);

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
