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
import com.planera.mis.planera2.activities.models.Territories;
import com.planera.mis.planera2.activities.models.TerritoryListResponse;
import com.planera.mis.planera2.activities.models.UserData;
import com.planera.mis.planera2.activities.models.UserListResponse;
import com.planera.mis.planera2.activities.utils.AppConstants;
import com.planera.mis.planera2.activities.utils.InternetConnection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ActivityCretePlan extends BaseActivity implements View.OnClickListener {
    private Spinner spinnerPlanDoctor;
    private Spinner spinnerPlanChemist;
    public static final int DEFAULT_SELECT_VALUE = 1;
    private Spinner spinnerPlanUser;
    private Spinner spinnerPlanPatch;
    private Spinner spinnerPlanMonth;
    private Spinner spinnerPlanTerritory;
    private RadioGroup radioGroupSelect;
    private EditText textPlanYear;
    private EditText textPlanCall;
    private EditText textPlanRemark;
    private List<Patches> patchesList = null;
    private List<UserData> usersList = null;
    private List<Chemists> chemistsList = null;
    private List<Doctors> doctorsList = null;
    private List<Territories> territorysList;
    private Button buttonAddPlan;
    private RadioButton radioDoctor;
    private RadioButton radioChemist;
    private LinearLayout layoutDoctorSpinner, layoutChemistSpinner, layoutTerritorySpinner;
    private Plans plans;
    private List<String> months;
    int patchId, selectedMonth;
    String doctorId, chemistId;
    private String userIdStr;
    String yearStr, callStr, remarkStr;
    boolean isDoctorRadioChecked = true;
    private int territoryId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crete_plan);
        initUi();
        initData();
        loadSpinners();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void uiValidation() {
        yearStr = textPlanYear.getText().toString().trim();
        callStr = textPlanCall.getText().toString().trim();
        remarkStr = textPlanRemark.getText().toString().trim();
        plans = new Plans();
        if (isDoctorRadioChecked) {
            if (!doctorsList.isEmpty()) {
                if (spinnerPlanDoctor.getSelectedItemPosition()!=0) {
                    doctorId = doctorsList.get(spinnerPlanDoctor.getSelectedItemPosition()- DEFAULT_SELECT_VALUE).getDoctorId() + "";
                    chemistId = null;
                }
            } else {
                Toast.makeText(ActivityCretePlan.this, "Can create plan, doctor's list not found", Toast.LENGTH_LONG).show();
            }
        } else if (!isDoctorRadioChecked) {
            if (!chemistsList.isEmpty()) {
                if (spinnerPlanChemist.getSelectedItemPosition()!= 0) {
                    chemistId = chemistsList.get(spinnerPlanChemist.getSelectedItemPosition()- DEFAULT_SELECT_VALUE).getChemistId() + "";
                    doctorId = null;
                }
            } else {
                Toast.makeText(ActivityCretePlan.this, "Can create plan, chemist's list not found", Toast.LENGTH_LONG).show();

            }
        }
        if (TextUtils.isEmpty(yearStr)) {
            textPlanYear.requestFocus();
            textPlanYear.setError(getString(R.string.invalid_input));
        } else if (TextUtils.isEmpty(callStr)) {
            textPlanCall.requestFocus();
            textPlanCall.setError(getString(R.string.invalid_input));
        } else if (TextUtils.isEmpty(remarkStr)) {
            textPlanRemark.requestFocus();
            textPlanRemark.setError(getString(R.string.invalid_input));
        } else {
            plans.setCalls(callStr);
            plans.setChemistsId(chemistId);
            plans.setDoctorId(doctorId);
            plans.setUserId(userIdStr);
            plans.setPatchId(patchId + "");
            plans.setYear(yearStr);
            plans.setMonth(selectedMonth + "");
            plans.setRemark(remarkStr);

            if (InternetConnection.isNetworkAvailable(ActivityCretePlan.this)) {
                createPlanApi(token, plans);
            } else {
                Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public void initUi() {
        super.initUi();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        layoutChemistSpinner = findViewById(R.id.chemist_spinner_layout);
        layoutDoctorSpinner = findViewById(R.id.doctor_spinner_layout);
        radioGroupSelect = findViewById(R.id.radio_group_select);
        spinnerPlanDoctor = findViewById(R.id.spinner_plan_doctor);
        spinnerPlanChemist = findViewById(R.id.spinner_plan_chemist);
        spinnerPlanUser = findViewById(R.id.spinner_plan_user);
        spinnerPlanPatch = findViewById(R.id.spinner_plan_patch);
        spinnerPlanMonth = findViewById(R.id.spinner_plan_month);
        spinnerPlanTerritory = findViewById(R.id.spinner_plan_territory);
        radioDoctor = findViewById(R.id.radio_doctor);
        radioChemist = findViewById(R.id.radio_chemist);
        textPlanYear = findViewById(R.id.text_plan_year);
        textPlanCall = findViewById(R.id.text_plan_call);
        textPlanRemark = findViewById(R.id.text_plan_remark);
        buttonAddPlan = findViewById(R.id.button_add_plan);
        if (isDoctorRadioChecked) {
            radioDoctor.setChecked(isDoctorRadioChecked);
            layoutChemistSpinner.setVisibility(View.GONE);
        }
        buttonAddPlan.setOnClickListener(this);
        getSupportActionBar().setTitle("Add Plan");
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

    }


    public void createPlanApi(String token, Plans plans) {
        Log.e("Plan Object", plans.getUserId());
        processDialog.showDialog(ActivityCretePlan.this, false);
        Call<MainResponse> call = apiInterface.addPlan(token, plans);

        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                Log.e(TAG, new Gson().toJson(response.body()));
                if (response.code() == 400) {
                    try {
                        Toast.makeText(ActivityCretePlan.this, response.errorBody().string(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (response.body().getStatusCode() == AppConstants.RESULT_OK) {
                        Intent intent = new Intent(ActivityCretePlan.this, SingleListActivity.class);
                        setResult(SingleListActivity.REQUEST_CODE_PLAN, intent);
                        finish();
                    } else {
                        Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                Snackbar.make(rootView, t.getMessage(), Snackbar.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        chemistsList = new ArrayList<>();
        doctorsList = new ArrayList<>();
        patchesList = new ArrayList<>();
        usersList = new ArrayList<>();
        radioGroupSelect.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radio_chemist:
                    layoutDoctorSpinner.setVisibility(View.GONE);
                    layoutChemistSpinner.setVisibility(View.VISIBLE);
                    isDoctorRadioChecked = false;
                    break;
                case R.id.radio_doctor:
                    layoutDoctorSpinner.setVisibility(View.VISIBLE);
                    layoutChemistSpinner.setVisibility(View.GONE);
                    isDoctorRadioChecked = true;
                    break;
            }
        });


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


        spinnerPlanMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMonth = position + DEFAULT_SELECT_VALUE;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedMonth = parent.getSelectedItemPosition() + DEFAULT_SELECT_VALUE;
            }
        });
        spinnerPlanUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    userIdStr = usersList.get(position- DEFAULT_SELECT_VALUE).getUserId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                TextView textPostionUser = (TextView) spinnerPlanUser.getSelectedItem();
//                textPostionUser.setError("User Not Selected");
//                textPostionUser.requestFocus();

            }
        });

        spinnerPlanTerritory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position!= 0) {
                    territoryId = territorysList.get(position- DEFAULT_SELECT_VALUE).getTerritoryId();
                    getPatchList(token, territoryId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerPlanPatch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    patchId = patchesList.get(position- DEFAULT_SELECT_VALUE).getPatchId();
                    if (isDoctorRadioChecked) {
                        getDoctorsList(token, patchId);
                    } else {
                        getChemistList(token, patchId);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }


    private void loadSpinners() {
//        getChemistList(token);
        getUsersList(token);
        getTerritoryList(token);
//        getPatchList(token);


    }


    public void getTerritoryList(String token) {
        processDialog.showDialog(ActivityCretePlan.this, false);
        Call<TerritoryListResponse> call = apiInterface.territoryList(token);
        call.enqueue(new Callback<TerritoryListResponse>() {
            @Override
            public void onResponse(Call<TerritoryListResponse> call, Response<TerritoryListResponse> response) {
                processDialog.dismissDialog();
                if (response != null) {
                    if (response.body().getStatusCode() == AppConstants.RESULT_OK) {
                        territorysList = response.body().getTerritorysList();
                        List<String> stringTerritoryList = new ArrayList<>();
                        stringTerritoryList.add(getString(R.string.select));
                        for (int i = 0; i < territorysList.size(); i++) {
                                stringTerritoryList.add(territorysList.get(i).getTerritoryName());
                        }
                        setArrayAdapter(stringTerritoryList, spinnerPlanTerritory);


                    } else {
                        Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<TerritoryListResponse> call, Throwable t) {
                Toast.makeText(ActivityCretePlan.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void getPatchList(String token, int territoryId) {
        processDialog.showDialog(ActivityCretePlan.this, false);
        Call<PatchListResponse> call = apiInterface.patchListByTerritory(token, territoryId);
        call.enqueue(new Callback<PatchListResponse>() {
            @Override
            public void onResponse(Call<PatchListResponse> call, Response<PatchListResponse> response) {
                processDialog.dismissDialog();
                if (response != null) {
                    if (response.body().getStatusCode() == AppConstants.RESULT_OK) {
                        patchesList = response.body().getPatchesList();
                        if (!patchesList.isEmpty()) {
                            List<String> stringPatchesList = new ArrayList<>();
                            stringPatchesList.add(getString(R.string.select));
                            for (int i = 0; i < patchesList.size(); i++) {
                                    stringPatchesList.add(patchesList.get(i).getPatchName());
                            }
                            setArrayAdapter(stringPatchesList, spinnerPlanPatch);


                        }
                    } else {
                        Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<PatchListResponse> call, Throwable t) {
                processDialog.dismissDialog();
                Toast.makeText(ActivityCretePlan.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void getDoctorsList(String token, int patchId) {
        processDialog.showDialog(ActivityCretePlan.this, false);
        Call<DoctorsListResponce> call = apiInterface.patchesWiseDoctorList(token, patchId);
        call.enqueue(new Callback<DoctorsListResponce>() {
            @Override
            public void onResponse(Call<DoctorsListResponce> call, Response<DoctorsListResponce> response) {
                processDialog.dismissDialog();
                Log.e(TAG, "onResponse: DoctorsList" + new Gson().toJson(response.body()));
                if (response.body().getStatusCode() == AppConstants.RESULT_OK) {
                    doctorsList = response.body().getData();
                    if (!doctorsList.isEmpty()) {
                        List<String> stringDoctorsList = new ArrayList<>();
                        stringDoctorsList.add(getString(R.string.select));
                        for (int i = 0; i < doctorsList.size(); i++) {
                                String docName = doctorsList.get(i).getFirstName();
                                if (doctorsList.get(i).getMiddleName() != null) {
                                    docName += " " + doctorsList.get(i).getMiddleName();
                                }
                                if (doctorsList.get(i).getLastName() != null) {
                                    docName += " " + doctorsList.get(i).getLastName();
                                }

                                stringDoctorsList.add(docName);
                        }
                        setArrayAdapter(stringDoctorsList, spinnerPlanDoctor);
                    } else {
                        Snackbar.make(rootView, "No Doctors Found", Snackbar.LENGTH_LONG).show();
                    }

                } else {
                    Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<DoctorsListResponce> call, Throwable t) {
                processDialog.dismissDialog();
                Toast.makeText(ActivityCretePlan.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void getUsersList(String token) {
        Call<UserListResponse> call = apiInterface.usersList(token);
        call.enqueue(new Callback<UserListResponse>() {
            @Override
            public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {
                if (response.body().getStatusCode() == AppConstants.RESULT_OK) {
                    usersList = response.body().getData();
                    List<String> stringUserList = new ArrayList<>();
                    stringUserList.add(getString(R.string.select));
                    for (int i = 0; i < usersList.size(); i++) {
                            String userName = usersList.get(i).getFirstName();
                            if (usersList.get(i).getMiddleName() != null) {
                                userName += " " + usersList.get(i).getMiddleName();
                            }
                            if (usersList.get(i).getLastName() != null) {
                                userName += " " + usersList.get(i).getLastName();
                            }
                            stringUserList.add(userName);
                    }
                    setArrayAdapter(stringUserList, spinnerPlanUser);
                } else {
                    Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<UserListResponse> call, Throwable t) {
                Snackbar.make(rootView, t.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });

    }

    public void getChemistList(String token, int patchId) {
        processDialog.showDialog(ActivityCretePlan.this, false);
        Call<ChemistListResponse> call = apiInterface.patchesWiseChemistList(token, patchId);
        call.enqueue(new Callback<ChemistListResponse>() {
            @Override
            public void onResponse(@NonNull Call<ChemistListResponse> call, Response<ChemistListResponse> response) {
                processDialog.dismissDialog();
                if (response.body().getStatusCode() == AppConstants.RESULT_OK) {
                    chemistsList = response.body().getData();
                    List<String> stringChemistList = new ArrayList<>();
                        stringChemistList.add(getString(R.string.select));
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
            case R.id.button_add_plan:
                uiValidation();
                break;
        }

    }


}

