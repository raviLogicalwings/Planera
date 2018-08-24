package com.planera.mis.planera2.activities.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.ActivityAddDoctor;
import com.planera.mis.planera2.activities.Retrofit.ApiClient;
import com.planera.mis.planera2.activities.Retrofit.ApiInterface;
import com.planera.mis.planera2.activities.adapters.DoctorsListAdapter;
import com.planera.mis.planera2.activities.models.Doctors;
import com.planera.mis.planera2.activities.models.DoctorsListResponce;
import com.planera.mis.planera2.activities.models.MainResponse;
import com.planera.mis.planera2.activities.utils.AppConstants;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class DoctorsFragment extends BaseFragment{

    public static DoctorsFragment instance;
    private View view;
    private ApiInterface apiInterface;
    private List<Doctors> doctorsList;
    private RecyclerView listViewDoctors;
    private LinearLayout layoutNoData;
    private int selectedDoctor;

    public DoctorsFragment() {

    }

    public static DoctorsFragment newInstance() {
        if(instance == null){
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
        view =  inflater.inflate(R.layout.fragment_doctors, container, false);
        initUi();
        initData();
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        apiInterface = ApiClient.getInstance();
        if(token!=null){
            getDoctorsList(token);
        }
    }

    @Override
    protected void initUi() {
        super.initUi();
        listViewDoctors = view.findViewById(R.id.list_doctors);
        layoutNoData = view.findViewById(R.id.layout_no_data);
    }




    public void getDoctorsList(String token){
        processDialog.showDialog(mContext, false);
        Call<DoctorsListResponce> call = apiInterface.doctorsList(token);
        call.enqueue(new Callback<DoctorsListResponce>() {
            @Override
            public void onResponse(Call<DoctorsListResponce> call, Response<DoctorsListResponce> response) {
                processDialog.dismissDialog();
                Log.e(TAG, "onResponse: "+new Gson().toJson(response.body()));
                if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                    doctorsList = response.body().getData();
                    if (doctorsList.size()>0) {
                        System.out.println(doctorsList.size());
                        listViewDoctors.setVisibility(View.VISIBLE);
                        initAdapter(doctorsList, listViewDoctors);
                    }
                    {
                        listViewDoctors.setVisibility(View.GONE);
                        layoutNoData.setVisibility(View.VISIBLE);
                    }

            }


            }

            @Override
            public void onFailure(Call<DoctorsListResponce> call, Throwable t) {
                processDialog.dismissDialog();
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void initAdapter(List<Doctors> list, RecyclerView recyclerView){
        DoctorsListAdapter adapter = new DoctorsListAdapter(mContext, list, (view, position) -> {
            switch (view.getId()){
                case R.id.img_doctor_delete:
                    deleteDoctorApi(token, doctorsList.get(position).getDoctorId());
                    break;

                case R.id.img_doctor_edit:
                    doctorDetailsForUpdate(position, doctorsList);

                    break;

            }
        });

        setAdapter(recyclerView, adapter);

    }


    public void doctorDetailsForUpdate(int pos, List<Doctors> doctorsData){
        selectedDoctor= doctorsData.get(pos).getDoctorId();
        Intent intentDoctorCall = new Intent(mContext, ActivityAddDoctor.class);
        intentDoctorCall.putExtra(AppConstants.UPDATE_DOCTOR_KEY, selectedDoctor);
        intentDoctorCall.putExtra(AppConstants.FIRST_NAME, doctorsData.get(pos).getFirstName());
        intentDoctorCall.putExtra(AppConstants.MIDDLE_NAME, doctorsData.get(pos).getMiddleName());
        intentDoctorCall.putExtra(AppConstants.LAST_NAME, doctorsData.get(pos).getLastName());
        intentDoctorCall.putExtra(AppConstants.PHONE, doctorsData.get(pos).getPhone());
        intentDoctorCall.putExtra(AppConstants.EMAIL, doctorsData.get(pos).getEmail());
        intentDoctorCall.putExtra(AppConstants.QUALIFICATION, doctorsData.get(pos).getQualifications());
        intentDoctorCall.putExtra(AppConstants.SPECIALIZATION, doctorsData.get(pos).getSpecializations());
        intentDoctorCall.putExtra(AppConstants.DOB, doctorsData.get(pos).getDOB());
        intentDoctorCall.putExtra(AppConstants.ADDRESS1, doctorsData.get(pos).getAddress1());
        intentDoctorCall.putExtra(AppConstants.ADDRESS2, doctorsData.get(pos).getAddress2());
        intentDoctorCall.putExtra(AppConstants.ADDRESS3, doctorsData.get(pos).getAddress3());
        intentDoctorCall.putExtra(AppConstants.ADDRESS4, doctorsData.get(pos).getAddress4());
        intentDoctorCall.putExtra(AppConstants.DISTRICT, doctorsData.get(pos).getDistrict());
        intentDoctorCall.putExtra(AppConstants.STATE, doctorsData.get(pos).getState());
        intentDoctorCall.putExtra(AppConstants.CITY, doctorsData.get(pos).getCity());
        intentDoctorCall.putExtra(AppConstants.PINCODE, doctorsData.get(pos).getPincode());
        intentDoctorCall.putExtra(AppConstants.UPDATE_DOCTOR_KEY, selectedDoctor);
        mContext.startActivity(intentDoctorCall);

    }
    public void setAdapter(RecyclerView recyclerView, DoctorsListAdapter adapter){
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



    public void deleteDoctorApi(String token, int doctorId){
        processDialog.showDialog(mContext, false);
        Call<MainResponse> call = apiInterface.deleteDoctor(token, doctorId);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                processDialog.dismissDialog();
                if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                    getFragmentManager().beginTransaction().detach(DoctorsFragment.this).attach(DoctorsFragment.this).commit();
                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
                else{
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
}
