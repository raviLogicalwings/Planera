package com.planera.mis.planera2.fragments;

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
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.planera.mis.planera2.FragmentDialog.UsersListDialog;
import com.planera.mis.planera2.R;
import com.planera.mis.planera2.Retrofit.ApiClient;
import com.planera.mis.planera2.Retrofit.ApiInterface;
import com.planera.mis.planera2.activities.AddInputActivity;
import com.planera.mis.planera2.activities.MainActivity;
import com.planera.mis.planera2.activities.ProductCategoryActivity;
import com.planera.mis.planera2.adapters.VisitsAdapter;
import com.planera.mis.planera2.models.AMs;
import com.planera.mis.planera2.models.AdminPlan;
import com.planera.mis.planera2.models.MRs;
import com.planera.mis.planera2.models.UserPlan;
import com.planera.mis.planera2.models.UserPlanListRespnce;
import com.planera.mis.planera2.utils.AppConstants;
import com.planera.mis.planera2.utils.InternetConnection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;
import static com.planera.mis.planera2.utils.AppConstants.JOINT_WORKING;
import static com.planera.mis.planera2.utils.AppConstants.KEY_JOINT_USER;
import static com.planera.mis.planera2.utils.AppConstants.PATCH_ID;

public class HomeFragment extends BaseFragment implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener, UsersListDialog.OnSelectUserListener,
        SwipeRefreshLayout.OnRefreshListener ,
        MainActivity.OnSearchQueryListener {


    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    public static final int INTERVAL = 1000 * 60;
    public static final int FASTEST_INTERVAL = 1000 * 60;

    private RecyclerView visitList;
    private Button buttonHomeRetry;
    private BottomNavigationView navigationView;
    private LinearLayout linearHomeNoInternet;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View mainView;

    private List<MRs> jointUserList;
    private List<UserPlan> plansList;
    private List<UserPlan> tempPlanList;


    private ApiInterface apiInterface;
    public  HomeFragment instance;
    private VisitsAdapter adapter;
    private UsersListDialog.OnSelectUserListener listener;
    private MainActivity.OnSearchQueryListener searchQueryListener;
    private GoogleApiClient googleApiClient;
    private Location mLocation;
    private LocationRequest mLocationRequest;
    private Bundle bundle;


    private int isInLocation;
    private double lat;
    private double lng;
    private boolean isPermissionCancelled = false;
    private int isJoint;


    public HomeFragment() {
    }


    @Override
    protected void initUi() {
        super.initUi();

        visitList = mainView.findViewById(R.id.visitList);
        linearHomeNoInternet = mainView.findViewById(R.id.linear_home_no_internet);
        buttonHomeRetry = mainView.findViewById(R.id.button_home_retry);
        navigationView = Objects.requireNonNull(getActivity()).findViewById(R.id.navigation);
        swipeRefreshLayout = mainView.findViewById(R.id.pullToRefresh);


        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(this);
        buttonHomeRetry.setOnClickListener(this);


        navigationView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {
        super.initData();
        apiInterface = ApiClient.getInstance();
        mLocationRequest = new LocationRequest();
        jointUserList = new ArrayList<>();
        bundle = new Bundle();
        setUpGClient();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        try {
            ((MainActivity) getActivity()).setSearchQueryListener(this);

        } catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement OnAddFriendListener");
        }
    }

    public void getAllPlansList(String token) {


        Call<UserPlanListRespnce> call = apiInterface.userPlanList(token);

        call.enqueue(new Callback<UserPlanListRespnce>() {
            @Override
            public void onResponse(@NonNull Call<UserPlanListRespnce> call, @NonNull Response<UserPlanListRespnce> response) {
                swipeRefreshLayout.setRefreshing(false);
                assert response.body() != null;
                if (response.body().getStatusCode() == AppConstants.RESULT_OK) {
                    plansList = response.body().getData();
                    if (plansList != null) {
                        Log.e("Plans", new Gson().toJson(response.body().getData()));
                        initAdapter((ArrayList<UserPlan>) plansList, visitList);

                    } else {
                        linearHomeNoInternet.setVisibility(View.VISIBLE);
                        Snackbar.make(rootView, "No Plan Available", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    linearHomeNoInternet.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(@NonNull Call<UserPlanListRespnce> call, @NonNull Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                linearHomeNoInternet.setVisibility(View.VISIBLE);
                buttonHomeRetry.setVisibility(View.VISIBLE);
                visitList.setVisibility(View.GONE);
                navigationView.setVisibility(View.GONE);
            }
        });
    }


    public void initAdapter(ArrayList<UserPlan> plansData, RecyclerView recyclerView) {
        for (UserPlan plans : plansData)
        {
            if (!plans.getChemistsId().equals("0")){
                plans.setDist(calculateDistance(mLocation, plans.getChemistLatitude(), plans.getChemistLongitude()));
            }
            else{
                plans.setDist(calculateDistance(mLocation, plans.getDoctorLatitude(), plans.getDoctorLongitude()));
            }
        }

     adapter = new VisitsAdapter(mContext, mLocation, new VisitsAdapter.OnVisitAdapterClickListener() {
            @Override
            public void onVisitClick(View view, int position, boolean isDoctor, double dist) {
                switch (view.getId()) {
                case R.id.button_check_in:
                    gotoScheduleTimeActivity(position, isDoctor, dist);
                    break;

            }
            }

            @Override
            public void onJointClick(int position) {
                UsersListDialog dialog = new UsersListDialog();
                bundle.putInt(PATCH_ID, plansList.get(position).getPatchId());
                dialog.setArguments(bundle);
                dialog.setTargetFragment(HomeFragment.this, 1);
                if (getFragmentManager() != null) {
                    dialog.show(getFragmentManager(),"Select User");
                }
            }
        });

        adapter.setList(plansList);
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
        getFusedLocationProviderClient(mContext).removeLocationUpdates(new LocationCallback());

    }

    public void setAdapter(RecyclerView recyclerView, VisitsAdapter adapter) {
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


    private void getMyLocation() {
        if (googleApiClient != null) {
            if (googleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    mLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
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

    public void checkLocationSettings() {
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


                    int permissionLocation = ContextCompat
                            .checkSelfPermission(mContext,
                                    Manifest.permission.ACCESS_FINE_LOCATION);
                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                        getMyLocation();
                    }
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    try {

                        status.startResolutionForResult(getActivity(),
                                REQUEST_CHECK_SETTINGS_GPS);
                    } catch (IntentSender.SendIntentException e) {
                        Toasty.error(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

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
                        isPermissionCancelled = true;
                        Toasty.warning(mContext, "Please Turn on location, it's required to go ahead. ", Toast.LENGTH_LONG).show();

                        break;
                }
                break;
        }
    }

    public void gotoScheduleTimeActivity(int pos, boolean isDoctor, double dist) {

        if (Math.round(dist) <= 1.0) {
            isInLocation = AppConstants.USER_IN_LOCATION;
        } else {
            isInLocation = AppConstants.USER_NOT_IN_LOCATION;
        }
        Intent intent = new Intent(mContext, AddInputActivity.class);
        connector.setBoolean(AppConstants.KEY_ROLE, isDoctor);
        intent.putExtra(AppConstants.KEY_ROLE, isDoctor);
        intent.putExtra(AppConstants.KEY_IN_LOCATION, isInLocation);
        intent.putExtra(AppConstants.CUSTOMER_NAME, getCustomerName(pos, plansList, isDoctor));
        intent.putExtra(AppConstants.VISIT_DATE, plansList.get(pos).getMonth() + "/" + plansList.get(pos).getYear());
        intent.putExtra(AppConstants.LATITUDE, lat);
        intent.putExtra(AppConstants.DOCTOR_ID, plansList.get(pos).getDoctorId());
        intent.putExtra(AppConstants.CHEMIST_ID, plansList.get(pos).getChemistsId());
        intent.putExtra(AppConstants.LONGITUDE, lng);
        intent.putExtra(AppConstants.KEY_PLAN_ID, plansList.get(pos).getPlanId());
        intent.putExtra(AppConstants.KEY_USER_ID, plansList.get(pos).getUserId());
        intent.putExtra(AppConstants.KEY_IS_JOINT, isJoint);
        bundle.putSerializable(KEY_JOINT_USER, (Serializable) jointUserList);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_check_in:
                Intent intent = new Intent(getContext(), ProductCategoryActivity.class);
                mContext.startActivity(intent);
                break;

            case R.id.button_home_retry:
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().detach(HomeFragment.this).attach(HomeFragment.this).commit();
                }
                break;

        }

    }

    private float calculateDistance(Location mLocation, double lat2, double lon2) {
        float[] results = new float[1];
        Location.distanceBetween(mLocation.getLatitude(), mLocation.getLongitude(),
                lat2, lon2,
                results);
        return results[0] / 1000;
    }


    public String getCustomerName(int pos, List<UserPlan> listPlans, boolean isDoctor) {
        String customerName;
        if (isDoctor) {
            customerName = listPlans.get(pos).getDoctorFirstName()
                    + " " + listPlans.get(pos).getDoctorMiddleName()
                    + " " + listPlans.get(pos).getDoctorLastName();
            lat = listPlans.get(pos).getDoctorLatitude();
            lng = listPlans.get(pos).getDoctorLongitude();

        } else {
            customerName = String.valueOf(listPlans.get(pos).getChemistFirstName())
                    + String.valueOf( listPlans.get(pos).getChemistMiddleName() )
                    +String.valueOf( listPlans.get(pos).getChemistLastName() );
            lat = listPlans.get(pos).getChemistLatitude();
            lng = listPlans.get(pos).getChemistLongitude();

        }

        return customerName;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (!isPermissionCancelled) {
            checkLocationSettings();
        }
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
        if (InternetConnection.isNetworkAvailable(mContext)){
            getAllPlansList(token);
        }
        else
        {
            Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSelectUser(List<MRs>  mrs) {
        if (mrs != null && mrs.size() > 0){
//            adapter.notifyJointUiUpdate();
//            adapter.notifyDataSetChanged();
            isJoint = JOINT_WORKING;
            jointUserList = mrs;
        }

    }

    private void filter(String s) {
        tempPlanList = new ArrayList<>();

        for(UserPlan up : plansList){
            if ((up.getDoctorFirstName()
                    + " " +up.getDoctorMiddleName() + " " +
                    up.getDoctorLastName()).toLowerCase().contains(s.toLowerCase())){
                tempPlanList.add(up);
            }

            adapter.updateList(tempPlanList);

        }

    }

    // TODO: Update argument type and name
    @Override
    public void onRefresh() {
    getAllPlansList(token);
    }

    @Override
    public void onSearchQuery(String query) {
        filter(query);
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
