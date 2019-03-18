package com.planera.mis.planera2.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.planera.mis.planera2.R;
import com.planera.mis.planera2.Retrofit.ApiClient;
import com.planera.mis.planera2.Retrofit.ApiInterface;
import com.planera.mis.planera2.activities.ActivityUpdateDoctor;
import com.planera.mis.planera2.adapters.DoctorsListAdapter;
import com.planera.mis.planera2.models.Doctors;
import com.planera.mis.planera2.models.DoctorsListResponce;
import com.planera.mis.planera2.models.MainResponse;
import com.planera.mis.planera2.utils.AppConstants;
import com.planera.mis.planera2.utils.InternetConnection;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class DoctorsFragment extends BaseFragment implements SearchView.OnQueryTextListener{

    public static DoctorsFragment instance;
    private View view;
    private ApiInterface apiInterface;
    private List<Doctors> doctorsList;
    private RecyclerView listViewDoctors;
    private LinearLayout visibleLayout;
    private SearchView searchViewDoctor;
    private int selectedDoctor;
    private DoctorsListAdapter adapter;
    private LinearLayout linearNoData, linearNoInternet;
    private Button buttonRetry;
    public DoctorsFragment() {

    }

    public static DoctorsFragment newInstance() {
        if (instance == null) {
            instance = new DoctorsFragment();
        }

        return instance;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_doctors, container, false);
        initUi();
        initData();
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        apiInterface = ApiClient.getInstance();
        if (token != null) {
            if (InternetConnection.isNetworkAvailable(mContext)){
                getDoctorsList(token);
            }
            else{
                Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void initUi() {
        super.initUi();
        listViewDoctors = view.findViewById(R.id.list_doctors);
        linearNoData = view.findViewById(R.id.linear_no_data);
        linearNoInternet = view.findViewById(R.id.linear_no_internet);
        buttonRetry = view.findViewById(R.id.button_retry);
        searchViewDoctor = view.findViewById(R.id.search_view_doctor);
        searchViewDoctor.setActivated(true);
        searchViewDoctor.onActionViewExpanded();
        searchViewDoctor.setIconified(false);
        searchViewDoctor.clearFocus();
        visibleLayout = view.findViewById(R.id.visible_layout);

        searchViewDoctor.setOnQueryTextListener(this);

        buttonRetry.setOnClickListener(v -> {
            if (getFragmentManager() != null) {
                getFragmentManager().beginTransaction().detach(DoctorsFragment.this).attach(DoctorsFragment.this).commit();
            }
        });
    }


    public void getDoctorsList(String token) {
        processDialog.showDialog(mContext, false);
        Call<DoctorsListResponce> call = apiInterface.doctorsList(token);
        call.enqueue(new Callback<DoctorsListResponce>() {
            @Override
            public void onResponse(Call<DoctorsListResponce> call, Response<DoctorsListResponce> response) {
                processDialog.dismissDialog();
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.body().getStatusCode() == AppConstants.RESULT_OK) {
                    doctorsList = response.body().getData();
                    if (doctorsList != null) {
                        visibleLayout.setVisibility(View.VISIBLE);
                        System.out.println(doctorsList.size());
                        initAdapter(doctorsList, listViewDoctors);
                    }
                } else {
                    linearNoData.setVisibility(View.VISIBLE);
                    visibleLayout.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<DoctorsListResponce> call, Throwable t) {
                processDialog.dismissDialog();
                linearNoInternet.setVisibility(View.VISIBLE);
                buttonRetry.setVisibility(View.VISIBLE);
            }
        });

    }

    public void initAdapter(List<Doctors> list, RecyclerView recyclerView) {

        adapter = new DoctorsListAdapter(mContext, list, (view, doctors) -> {
            switch (view.getId()) {
                case R.id.img_doctor_delete:
                    popupDialog(token, doctors.getDoctorId());
                    break;

                case R.id.img_doctor_edit:
                    doctorDetailsForUpdate(doctors);
                    DoctorsFragment.this.getActivity().finish();

                    break;

            }
        });

        setAdapter(recyclerView, adapter);

    }


    public void doctorDetailsForUpdate(Doctors doctors) {
        selectedDoctor = doctors.getDoctorId();
        Intent intentDoctorCall = new Intent(mContext, ActivityUpdateDoctor.class);
        intentDoctorCall.putExtra(AppConstants.UPDATE_DOCTOR_KEY, selectedDoctor);
        intentDoctorCall.putExtra(AppConstants.FIRST_NAME, doctors.getFirstName());
        intentDoctorCall.putExtra(AppConstants.MIDDLE_NAME, doctors.getMiddleName());
        intentDoctorCall.putExtra(AppConstants.LAST_NAME, doctors.getLastName());
        intentDoctorCall.putExtra(AppConstants.PHONE, doctors.getPhone());
        intentDoctorCall.putExtra(AppConstants.EMAIL, doctors.getEmail());
        intentDoctorCall.putExtra(AppConstants.PREFERRED_MEET_TIME, doctors.getPreferredMeetTime());
        intentDoctorCall.putExtra(AppConstants.MEET_FREQUENCY, doctors.getMeetFrequency());
        intentDoctorCall.putExtra(AppConstants.PATCH_ID, doctors.getPatchId());
        intentDoctorCall.putExtra(AppConstants.QUALIFICATION, doctors.getQualifications());
        intentDoctorCall.putExtra(AppConstants.SPECIALIZATION, doctors.getSpecializations());
        intentDoctorCall.putExtra(AppConstants.DOB, doctors.getDOB());
        intentDoctorCall.putExtra(AppConstants.ADDRESS1, doctors.getAddress1());
        intentDoctorCall.putExtra(AppConstants.ADDRESS2, doctors.getAddress2());
        intentDoctorCall.putExtra(AppConstants.ADDRESS3, doctors.getAddress3());
        intentDoctorCall.putExtra(AppConstants.ADDRESS4, doctors.getAddress4());
        intentDoctorCall.putExtra(AppConstants.DISTRICT, doctors.getDistrict());
        intentDoctorCall.putExtra(AppConstants.STATE, doctors.getState());
        intentDoctorCall.putExtra(AppConstants.CITY, doctors.getCity());
        intentDoctorCall.putExtra(AppConstants.PINCODE, doctors.getPincode());
        mContext.startActivity(intentDoctorCall);

    }

    public void setAdapter(RecyclerView recyclerView, DoctorsListAdapter adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    @Override
    public void onDetach() {
        super.onDetach();

    }

    void filter(String text){
        List<Doctors> temp = new ArrayList<>();
        for(Doctors d: doctorsList){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if((d.getFirstName()+" "+d.getLastName()).toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }

            if(d.getPatchName().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }
        }
        adapter.updateList(temp);
    }


    public void popupDialog(String token, int doctorId) {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();

        alertDialog.setMessage("Are you sure you want to delete this?");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (dialogInterface, i) -> {
            dialogInterface.cancel();

            if (InternetConnection.isNetworkAvailable(mContext))
            {
                deleteDoctorApi(token, doctorId);
            }
            else
            {
                Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
            }
        });


        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", (dialog, which) -> {
            dialog.cancel();
        });

        alertDialog.show();

    }


    public void deleteDoctorApi(String token, int doctorId) {
        processDialog.showDialog(mContext, false);
        Call<MainResponse> call = apiInterface.deleteDoctor(token, doctorId);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                processDialog.dismissDialog();
                if (response.body().getStatusCode() == AppConstants.RESULT_OK) {
                    if (InternetConnection.isNetworkAvailable(mContext)){
                        getDoctorsList(token);
                    }
                    else
                        {
                        Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
                    }
                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                processDialog.dismissDialog();

                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        filter(newText);
        return false;
    }
}
