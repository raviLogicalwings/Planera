package com.planera.mis.planera2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
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
import com.planera.mis.planera2.models.ChemistListResponse;
import com.planera.mis.planera2.models.Chemists;
import com.planera.mis.planera2.models.Doctors;
import com.planera.mis.planera2.models.DoctorsListResponce;
import com.planera.mis.planera2.models.MainResponse;
import com.planera.mis.planera2.models.PatchListResponse;
import com.planera.mis.planera2.models.Patches;
import com.planera.mis.planera2.models.Plans;
import com.planera.mis.planera2.models.Territories;
import com.planera.mis.planera2.models.TerritoryListResponse;
import com.planera.mis.planera2.models.UserData;
import com.planera.mis.planera2.models.UserListResponse;
import com.planera.mis.planera2.utils.AppConstants;
import com.planera.mis.planera2.utils.InternetConnection;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ActivityUpdatePlan extends BaseActivity implements View.OnClickListener {
    private Spinner spinnerPlanDoctor;
    private Spinner spinnerPlanChemist;
    private Spinner spinnerPlanUser;
    private Spinner spinnerPlanPatch;
    private Spinner spinnerPlanMonth;
    private Spinner spinnerPlanYear;
    private Spinner spinnerTerritory;
    private EditText textPlanCall;
    private EditText textPlanRemark;
    private LinearLayout doctorSpinnerLayout;
    private LinearLayout chemistSpinnerLayout;
    private LinearLayout linearNoInternet;
    private RadioGroup radioGroupSelect;
    protected RadioButton radioDoctor;
    protected RadioButton radioChemist;
    protected Button buttonAddPlan;
    private Button buttonRetry;
    private CardView cardView;

    private List<Patches> patchesList;
    private List<UserData> usersList;
    private List<Chemists> chemistsList;
    private List<Doctors> doctorsList;
    protected List<String> months;
    private List<String> years;
    private List<Territories> territoriesList;

    protected Plans plans;

    private int patchId;
    private int selectedMonth;
    private int planId;
    protected int runningYear;
    private int territoryId;

    protected String chemistId;
    protected String doctorId;
    protected String userId;
    protected String status;
    protected String yearStr;
    protected String callStr;
    protected String remarkStr;

    private boolean isDoctorRadioChecked = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crete_plan);
        initUi();
        initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void uiValidation() {
        callStr = textPlanCall.getText().toString().trim();
        remarkStr = textPlanRemark.getText().toString().trim();
        plans = new Plans();
        if (isDoctorRadioChecked) {
            doctorId = doctorsList.get(spinnerPlanDoctor.getSelectedItemPosition()).getDoctorId() + "";
            chemistId = null;
        } else {
            chemistId = chemistsList.get(spinnerPlanChemist.getSelectedItemPosition()).getChemistId() + "";
            doctorId = null;
        }
        if (TextUtils.isEmpty(callStr)) {
            textPlanCall.requestFocus();
            textPlanCall.setError(getString(R.string.invalid_input));
        } else if (TextUtils.isEmpty(remarkStr)) {
            textPlanRemark.requestFocus();
            textPlanRemark.setError(getString(R.string.invalid_input));
        } else {
            plans.setPlanId(planId + "");
            plans.setCalls(callStr);
            plans.setChemistsId(chemistId);
            plans.setDoctorId(doctorId);
            plans.setUserId(userId + "");
            plans.setPatchId(patchId + "");
            plans.setYear(yearStr);
            plans.setMonth(selectedMonth + "");
            plans.setRemark(remarkStr);
            plans.setStatus(status);

            if (InternetConnection.isNetworkAvailable(ActivityUpdatePlan.this)) {
                updatePlanDetailsApi(token, plans);
            } else {
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
        spinnerTerritory = findViewById(R.id.spinner_plan_territory);
        textPlanCall = findViewById(R.id.text_plan_call);
        textPlanRemark = findViewById(R.id.text_plan_remark);
        buttonAddPlan = findViewById(R.id.button_create_plan);
        buttonRetry = findViewById(R.id.button_retry);
        linearNoInternet = findViewById(R.id.linear_no_internet);
        cardView = findViewById(R.id.card_view);
        buttonAddPlan.setText(getString(R.string.update));
        if (isDoctorRadioChecked) {
            radioDoctor.setChecked(true);
            chemistSpinnerLayout.setVisibility(View.GONE);
        }
        buttonAddPlan.setOnClickListener(this);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.title_update_plan);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        loadFormIntent(intent);

        buttonRetry.setOnClickListener(v -> {
            finish();
            startActivity(getIntent());
        });
    }


    public void updatePlanDetailsApi(String token, Plans plans) {
        processDialog.showDialog(ActivityUpdatePlan.this, false);
        Call<MainResponse> call = apiInterface.updatePlanDetails(token, plans);

        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(@NonNull Call<MainResponse> call, @NonNull Response<MainResponse> response) {
                processDialog.dismissDialog();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatusCode() == AppConstants.RESULT_OK) {
                        Intent intent = new Intent(ActivityUpdatePlan.this, SingleListActivity.class);
                        intent.putExtra(AppConstants.KEY_TOUCHED_FRAGMENT, AppConstants.PLAN_FRAGMENT);
                        startActivity(intent);
                        finish();
                    } else {
                        Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<MainResponse> call, @NonNull Throwable t) {
                Snackbar.make(rootView, t.getMessage(), Snackbar.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ActivityUpdatePlan.this, SingleListActivity.class);
        intent.putExtra(AppConstants.KEY_TOUCHED_FRAGMENT, AppConstants.PLAN_FRAGMENT);
        startActivity(intent);
    }

    @Override
    public void initData() {
        super.initData();

        radioGroupSelect.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radio_chemist:
                    doctorSpinnerLayout.setVisibility(View.GONE);
                    chemistSpinnerLayout.setVisibility(View.VISIBLE);
                    isDoctorRadioChecked = false;
                    break;
                case R.id.radio_doctor:
                    chemistSpinnerLayout.setVisibility(View.VISIBLE);
                    doctorSpinnerLayout.setVisibility(View.GONE);
                    isDoctorRadioChecked = true;
                    break;
            }
        });
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
        years.add(runningYear + "");
        years.add(runningYear + 1 + "");
        years.add(runningYear + 2 + "");
        years.add(runningYear + 3 + "");
        years.add(runningYear + 4 + "");
        setArrayAdapter(years, spinnerPlanYear);

        spinnerPlanYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                yearStr = years.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerPlanMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMonth = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerPlanUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userId = usersList.get(position).getUserId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerTerritory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                territoryId = territoriesList.get(position).getTerritoryId();
                if (territoryId != 0) {
                    if (InternetConnection.isNetworkAvailable(ActivityUpdatePlan.this)) {
                        getPatchList(token, territoryId);
                    } else {
                        Snackbar.make(rootView, R.string.no_internet, Snackbar.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerPlanPatch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                patchId = patchesList.get(position).getPatchId();

                if (isDoctorRadioChecked) {
                    if (InternetConnection.isNetworkAvailable(ActivityUpdatePlan.this)) {
                        getDoctorsList(token, patchId);
                    } else {
                        Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
                    }
                } else {

                    if (InternetConnection.isNetworkAvailable(ActivityUpdatePlan.this)) {
                        getChemistList(token, patchId);
                    } else {
                        Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
                    }

                }
                /**
                 * *
                 getting user's list after filling chemist/doctor's spinner./
                 */
                if (InternetConnection.isNetworkAvailable(ActivityUpdatePlan.this)){
                    getUsersList(token);
                }
                else
                {
                    Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        radioGroupSelect.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
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
        if (InternetConnection.isNetworkAvailable(ActivityUpdatePlan.this)) {
            getTerritoryList(token);
        } else {
            Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
        }

    }

    public void getTerritoryList(String token) {
        processDialog.showDialog(ActivityUpdatePlan.this, false);
        Call<TerritoryListResponse> call = apiInterface.territoryList(token);
        call.enqueue(new Callback<TerritoryListResponse>() {
            @Override
            public void onResponse(@NonNull Call<TerritoryListResponse> call, @NonNull Response<TerritoryListResponse> response) {
                processDialog.dismissDialog();
                assert response.body() != null;
                if (response.body().getStatusCode() == AppConstants.RESULT_OK) {
                    territoriesList = response.body().getTerritorysList();
                    List<String> stringTerritoryList = new ArrayList<>();
                    stringTerritoryList.add(getString(R.string.select));
                    for (int i = 0; i < territoriesList.size(); i++) {
                        stringTerritoryList.add(territoriesList.get(i).getTerritoryName());
                    }
                    setArrayAdapter(stringTerritoryList, spinnerTerritory);

                } else {
                    Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<TerritoryListResponse> call, @NonNull Throwable t) {
                processDialog.dismissDialog();
                cardView.setVisibility(View.GONE);
                linearNoInternet.setVisibility(View.VISIBLE);
                buttonRetry.setVisibility(View.VISIBLE);
                Toast.makeText(ActivityUpdatePlan.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void getPatchList(String token, int terId) {
        Call<PatchListResponse> call = apiInterface.patchListByTerritory(token, terId);
        call.enqueue(new Callback<PatchListResponse>() {
            @Override
            public void onResponse(@NonNull Call<PatchListResponse> call, @NonNull Response<PatchListResponse> response) {
                assert response.body() != null;
                if (response.body().getStatusCode() == AppConstants.RESULT_OK)
                {
                    patchesList = response.body().getPatchesList();
                    List<String> stringPatchesList = new ArrayList<>();
                    for (int i = 0; i < patchesList.size(); i++)
                    {
                        stringPatchesList.add(patchesList.get(i).getPatchName());
                    }
                    setArrayAdapter(stringPatchesList, spinnerPlanPatch);


                } else
                    {
                    Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PatchListResponse> call, @NonNull Throwable t) {
                processDialog.dismissDialog();
                cardView.setVisibility(View.GONE);
                linearNoInternet.setVisibility(View.VISIBLE);
                buttonRetry.setVisibility(View.VISIBLE);
                Toast.makeText(ActivityUpdatePlan.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void getDoctorsList(String token, int patchId) {
        Call<DoctorsListResponce> call = apiInterface.patchesWiseDoctorList(token, patchId);
        call.enqueue(new Callback<DoctorsListResponce>() {
            @Override
            public void onResponse(@NonNull Call<DoctorsListResponce> call, @NonNull Response<DoctorsListResponce> response) {
                assert response.body() != null;
                if (response.body().getStatusCode() == AppConstants.RESULT_OK) {
                    doctorsList = response.body().getData();
                    List<String> stringDoctorsList = new ArrayList<>();
                    for (int i = 0; i < doctorsList.size(); i++) {
                        stringDoctorsList.add(doctorsList.get(i).getFirstName() + " " + doctorsList.get(i).getLastName());
                    }
                    setArrayAdapter(stringDoctorsList, spinnerPlanDoctor);
                } else {
                    Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(@NonNull Call<DoctorsListResponce> call, @NonNull Throwable t) {
                processDialog.dismissDialog();
                cardView.setVisibility(View.GONE);
                linearNoInternet.setVisibility(View.VISIBLE);
                buttonRetry.setVisibility(View.VISIBLE);
//                DataController.getmInstance().dismissProcessDialog();
                Toast.makeText(ActivityUpdatePlan.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void getUsersList(String token) {
//        DataController.getmInstance().showProcessDialog(ActivityUpdatePlan.this, false);
        Call<UserListResponse> call = apiInterface.usersList(token);
        call.enqueue(new Callback<UserListResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserListResponse> call, Response<UserListResponse> response) {
//                DataController.getmInstance().dismissProcessDialog();
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.body().getStatusCode() == AppConstants.RESULT_OK) {
                    usersList = response.body().getData();
                    List<String> stringUserList = new ArrayList<>();
                    for (int i = 0; i < usersList.size(); i++) {
                        stringUserList.add(usersList.get(i).getFirstName() + " " + usersList.get(i).getLastName());
                    }
                    setArrayAdapter(stringUserList, spinnerPlanUser);
                } else {
                    Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<UserListResponse> call, Throwable t) {
                processDialog.dismissDialog();
                cardView.setVisibility(View.GONE);
                linearNoInternet.setVisibility(View.VISIBLE);
                buttonRetry.setVisibility(View.VISIBLE);
//                DataController.getmInstance().dismissProcessDialog();
                Snackbar.make(rootView, t.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });

    }

    public void loadFormIntent(Intent intent) {
        textPlanCall.setText(intent.getStringExtra(AppConstants.CALLS));
        textPlanRemark.setText(intent.getStringExtra(AppConstants.REMARK));
        planId = intent.getIntExtra(AppConstants.UPDATE_PLAN_KEY, 0);
        status = intent.getStringExtra(AppConstants.STATUS);
    }

    public void getChemistList(String token, int patchId) {
        Call<ChemistListResponse> call = apiInterface.patchesWiseChemistList(token, patchId);
        call.enqueue(new Callback<ChemistListResponse>() {
            @Override
            public void onResponse(@NonNull Call<ChemistListResponse> call, @NonNull Response<ChemistListResponse> response) {
                Log.e(TAG, "onResponse: ChemistList" + new Gson().toJson(response.body()));
                if (response.body().getStatusCode() == AppConstants.RESULT_OK) {
                    chemistsList = response.body().getData();
                    List<String> stringChemistList = new ArrayList<>();
                    for (int i = 0; i < chemistsList.size(); i++) {
                        stringChemistList.add(chemistsList.get(i).getFirstName() + " " + chemistsList.get(i).getLastName());
                    }
                    setArrayAdapter(stringChemistList, spinnerPlanChemist);
                } else {
                    Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<ChemistListResponse> call, Throwable t) {
                processDialog.dismissDialog();
                cardView.setVisibility(View.GONE);
                linearNoInternet.setVisibility(View.VISIBLE);
                buttonRetry.setVisibility(View.VISIBLE);
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
        switch (v.getId()) {
            case R.id.button_create_plan:
                uiValidation();
                break;
        }

    }


}

