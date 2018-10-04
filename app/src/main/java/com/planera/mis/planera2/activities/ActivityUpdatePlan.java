package com.planera.mis.planera2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.models.ChemistListResponse;
import com.planera.mis.planera2.activities.models.Chemists;
import com.planera.mis.planera2.activities.models.Doctors;
import com.planera.mis.planera2.activities.models.DoctorsListResponce;
import com.planera.mis.planera2.activities.models.MainResponse;
import com.planera.mis.planera2.activities.models.PatchListResponse;
import com.planera.mis.planera2.activities.models.Patches;
import com.planera.mis.planera2.activities.models.Plans;
import com.planera.mis.planera2.activities.models.UserData;
import com.planera.mis.planera2.activities.models.UserListResponse;
import com.planera.mis.planera2.activities.utils.AppConstants;
import com.planera.mis.planera2.activities.utils.InternetConnection;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ActivityUpdatePlan extends BaseActivity implements View.OnClickListener{
    private Spinner spinnerPlanDoctor;
    private Spinner spinnerPlanChemist;
    private Spinner spinnerPlanUser;
    private Spinner spinnerPlanPatch;
    private Spinner spinnerPlanMonth;
    private Spinner spinnerPlanYear;
    private EditText textPlanCall;
    private EditText textPlanRemark;
    private LinearLayout doctorSpinnerLayout;
    private LinearLayout chemistSpinnerLayout;
    private List<Patches> patchesList;
    private List<UserData> usersList;
    private List<Chemists> chemistsList;
    private List<Doctors> doctorsList;
    private RadioGroup radioGroupSelect;
    private RadioButton radioDoctor;
    private RadioButton radioChemist;
    private Button buttonAddPlan;
    private Plans plans;
    private List<String> months;
    private boolean isDoctorRadioChecked = true;
    int  patchId,  selectedMonth, planId;
    String chemistId, doctorId;
    String  userId,status;
    String  yearStr, callStr, remarkStr;
    private int runningYear;
    private List<String> years;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crete_plan);
        initUi();
        initData();
//        loadSpinners();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void uiValidation(){
        callStr = textPlanCall.getText().toString().trim();
        remarkStr = textPlanRemark.getText().toString().trim();
        plans = new Plans();
        if (isDoctorRadioChecked){
            doctorId = doctorsList.get(spinnerPlanDoctor.getSelectedItemPosition()).getDoctorId()+"";
            chemistId = null;
        }
        else if(!isDoctorRadioChecked){
            chemistId = chemistsList.get(spinnerPlanChemist.getSelectedItemPosition()).getChemistId()+"";
            doctorId = null;
        }
       if (TextUtils.isEmpty(callStr)){
            textPlanCall.requestFocus();
            textPlanCall.setError(getString(R.string.invalid_input));
        }
        else if (TextUtils.isEmpty(remarkStr)){
            textPlanRemark.requestFocus();
            textPlanRemark.setError(getString(R.string.invalid_input));
        }

        else{
            plans.setPlanId(planId+"");
            plans.setCalls(callStr);
            plans.setChemistsId(chemistId);
            plans.setDoctorId(doctorId);
            plans.setUserId(userId+"");
            plans.setPatchId(patchId+"");
            plans.setYear(yearStr);
            plans.setMonth(selectedMonth+"");
            plans.setRemark(remarkStr);
            plans.setStatus(status);

            if (InternetConnection.isNetworkAvailable(ActivityUpdatePlan.this)){
                updatePlanDetalisApi(token, plans);
            }
            else{
                Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public void initUi() {
        super.initUi();
        Intent intent = getIntent();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        radioGroupSelect = findViewById(R.id.radio_group_select);
        radioDoctor = findViewById(R.id.radio_doctor);
        radioChemist = findViewById(R.id.radio_chemist);


        doctorSpinnerLayout = findViewById(R.id.doctor_spinner_layout);
        chemistSpinnerLayout = findViewById(R.id.chemist_spinner_layout);

        spinnerPlanDoctor = findViewById(R.id.spinner_plan_doctor);
        spinnerPlanChemist = findViewById(R.id.spinner_plan_chemist);
        spinnerPlanUser = findViewById(R.id.spinner_plan_user);
        spinnerPlanPatch = findViewById(R.id.spinner_plan_patch);
        spinnerPlanMonth = findViewById(R.id.spinner_plan_month);
        spinnerPlanYear = findViewById(R.id.spinner_plan_year);


        textPlanCall = findViewById(R.id.text_plan_call);
        textPlanRemark = findViewById(R.id.text_plan_remark);
        buttonAddPlan = findViewById(R.id.button_add_plan);
        if(isDoctorRadioChecked) {
            radioDoctor.setChecked(isDoctorRadioChecked);
            chemistSpinnerLayout.setVisibility(View.GONE);
        }
        buttonAddPlan.setOnClickListener(this);
        getSupportActionBar().setTitle("Update Plan");
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        loadFormIntent(intent);
    }


    public void updatePlanDetalisApi(String token, Plans plans){
        Log.e("Update Responce", new Gson().toJson(plans));
        processDialog.showDialog(ActivityUpdatePlan.this, false);
        Call<MainResponse> call = apiInterface.updatePlanDetails(token, plans);

        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(@NonNull Call<MainResponse> call, @NonNull Response<MainResponse> response) {
                Log.e("Update Responce", new Gson().toJson(response.body()));
                processDialog.dismissDialog();
                if(response.isSuccessful()){
                    if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                        Intent intent = new Intent(ActivityUpdatePlan.this, SingleListActivity.class);
                        intent.putExtra(AppConstants.KEY_TOUCHED_FRAGMENT, AppConstants.PLAN_FRAGMENT);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                Log.e("Log ", t.getMessage());
                Snackbar.make(rootView, t.getMessage(), Snackbar.LENGTH_LONG).show();

            }
        });
    }
    @Override
    public void initData() {
        super.initData();
        loadSpinners();
        months = new ArrayList<>();
        months.add("Jan");
        months.add("Feb");
        months.add("Mar");
        months.add("Apr");
        months.add("May");
        months.add("Jun");
        months.add("Jul");
        months.add("Aug");
        months.add("Sep");
        months.add("Oct");
        months.add("Nov");
        months.add("Dec");
        setArrayAdapter(months, spinnerPlanMonth);

        runningYear = Calendar.getInstance().get(Calendar.YEAR);

        years = new ArrayList<>();
        years.add(runningYear+"");
        years.add(runningYear+1+"");
        years.add(runningYear+2+"");
        years.add(runningYear+3+"");
        years.add(runningYear+4+"");
        setArrayAdapter(years, spinnerPlanYear);

        spinnerPlanYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                yearStr = years.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                yearStr = years.get(parent.getSelectedItemPosition());
            }
        });

        spinnerPlanMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMonth = position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedMonth = parent.getSelectedItemPosition()+1;
            }
        });
        spinnerPlanUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userId = usersList.get(position).getUserId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                userId = usersList.get(parent.getSelectedItemPosition()).getUserId();

            }
        });
        spinnerPlanPatch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                patchId = patchesList.get(position).getPatchId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                patchId = patchesList.get(parent.getSelectedItemPosition()).getPatchId();
            }
        });



        radioGroupSelect.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case R.id.radio_chemist:
                    isDoctorRadioChecked = false;
                    chemistSpinnerLayout.setVisibility(View.VISIBLE);
                    doctorSpinnerLayout.setVisibility(View.GONE);
                    break;

                case R.id.radio_doctor:
                    isDoctorRadioChecked = true;
                    chemistSpinnerLayout.setVisibility(View.GONE);
                    doctorSpinnerLayout.setVisibility(View.VISIBLE);
                    break;

            }

        });

    }


    private void loadSpinners() {
        getChemistList(token);
        getDoctorsList(token);
        getUsersList(token);
        getPatchList(token);


    }



    public void getPatchList(String token){
//        DataController.getmInstance().showProcessDialog(ActivityUpdatePlan.this, false);
        Call<PatchListResponse> call = apiInterface.patchList(token);
        call.enqueue(new Callback<PatchListResponse>() {
            @Override
            public void onResponse(Call<PatchListResponse> call, Response<PatchListResponse> response) {
//               DataController.getmInstance().dismissProcessDialog();
                Log.e(TAG, "onResponse: PatchesList"+new Gson().toJson(response.body()));
                if (response != null) {
                    if (response.body().getStatusCode() == AppConstants.RESULT_OK) {
                        patchesList = response.body().getPatchesList();
                        List<String> stringPatchesList = new ArrayList<>();
                        for (int i=0; i<patchesList.size(); i++){
                            stringPatchesList.add(patchesList.get(i).getPatchName());
                        }
                        setArrayAdapter(stringPatchesList, spinnerPlanPatch);


                    } else {
                        Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<PatchListResponse> call, Throwable t) {
//                DataController.getmInstance().dismissProcessDialog();

                Toast.makeText(ActivityUpdatePlan.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void getDoctorsList(String token){
//        DataController.getmInstance().showProcessDialog(ActivityUpdatePlan.this, false);
        Call<DoctorsListResponce> call = apiInterface.doctorsList(token);
        call.enqueue(new Callback<DoctorsListResponce>() {
            @Override
            public void onResponse(Call<DoctorsListResponce> call, Response<DoctorsListResponce> response) {
//               DataController.getmInstance().dismissProcessDialog();
                Log.e(TAG, "onResponse: DoctorsList"+new Gson().toJson(response.body()));
                if (response.body().getStatusCode() == AppConstants.RESULT_OK) {
                    doctorsList = response.body().getData();
                    List<String> stringDoctorsList = new ArrayList<>();
                    for (int i=0; i<doctorsList.size(); i++){
                        stringDoctorsList.add(doctorsList.get(i).getFirstName()+" "+doctorsList.get(i).getLastName());
                    }
                    setArrayAdapter(stringDoctorsList, spinnerPlanDoctor);
                }
                else{
                    Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<DoctorsListResponce> call, Throwable t) {
//                DataController.getmInstance().dismissProcessDialog();
                Toast.makeText(ActivityUpdatePlan.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void getUsersList(String token){
//        DataController.getmInstance().showProcessDialog(ActivityUpdatePlan.this, false);
        Call<UserListResponse> call = apiInterface.usersList(token);
        call.enqueue(new Callback<UserListResponse>() {
            @Override
            public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {
//                DataController.getmInstance().dismissProcessDialog();
                Log.e(TAG, "onResponse: "+new Gson().toJson(response.body()));
                if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                    usersList = response.body().getData();
                    List<String> stringUserList = new ArrayList<>();
                    for (int i=0; i<usersList.size(); i++){
                        stringUserList.add(usersList.get(i).getFirstName()+" "+usersList.get(i).getLastName());
                    }
                    setArrayAdapter(stringUserList, spinnerPlanUser);
                }
                else{
                    Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<UserListResponse> call, Throwable t) {
//                DataController.getmInstance().dismissProcessDialog();
                Snackbar.make(rootView, t.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });

    }

    public void loadFormIntent(Intent intent){
        textPlanCall.setText(intent.getStringExtra(AppConstants.CALLS));
        textPlanRemark.setText(intent.getStringExtra(AppConstants.REMARK));
        planId = intent.getIntExtra(AppConstants.UPDATE_PLAN_KEY, 0);
        status = intent.getStringExtra(AppConstants.STATUS);
    }

    public void getChemistList(String token){
//        DataController.getmInstance().showProcessDialog(ActivityUpdatePlan.this, false);
        Call<ChemistListResponse> call = apiInterface.chemistList(token);
        call.enqueue(new Callback<ChemistListResponse>() {
            @Override
            public void onResponse(Call<ChemistListResponse> call, Response<ChemistListResponse> response) {
//                DataController.getmInstance().dismissProcessDialog();
                Log.e(TAG, "onResponse: ChemistList"+new Gson().toJson(response.body()));
                if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                    chemistsList = response.body().getData();
                    List<String> stringChemistList = new ArrayList<>();
                    for (int i=0; i<chemistsList.size(); i++){
                        stringChemistList.add(chemistsList.get(i).getFirstName()+" "+chemistsList.get(i).getLastName());
                    }
                    setArrayAdapter(stringChemistList, spinnerPlanChemist);
                }
                else{
                    Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<ChemistListResponse> call, Throwable t) {
//                DataController.getmInstance().dismissProcessDialog();
                Snackbar.make(rootView, t.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });

    }

    public void setArrayAdapter(List<String> listString, Spinner spinner) {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item,
                        listString);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_add_plan:
                uiValidation();
                break;
        }

    }


}

