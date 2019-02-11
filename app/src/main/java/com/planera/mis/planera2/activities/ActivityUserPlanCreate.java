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
import com.planera.mis.planera2.utils.AppConstants;
import com.planera.mis.planera2.utils.InternetConnection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ActivityUserPlanCreate extends BaseActivity implements View.OnClickListener{
//    private RadioGroup radioGroupSelect;
//    private RadioButton radioDoctor;
//    private RadioButton radioChemist;
    private Spinner spinnerPlanTerritory;
    private Spinner spinnerPlanPatch;
//    private Spinner spinnerPlanDoctor;
//    private Spinner spinnerPlanChemist;
    private Spinner spinnerPlanMonth;
    private Spinner spinnerPlanYear;
    private EditText textPlanCall;
    private EditText textPlanRemark;
    private Button buttonAddPlan;
    private Toolbar toolbar;
    private LinearLayout layoutDoctorSpinner, layoutChemistSpinner, layoutTerritorySpinner;

    private Plans plans;
    private List<Patches> patchesList = null;
    private List<UserData> usersList = null;
    private List<Chemists> chemistsList = null;
    private List<Doctors> doctorsList = null;
    private List<Territories> territorysList;
    private List<String> years;
    private List<String> months;

    private String callStr;
    private String remarkStr, yearStr;
    boolean isDoctorRadioChecked = true;
    private String doctorId;
    private String chemistId;
    int patchId, selectedMonth;
    private int runningYear;
    private int territoryId = 0;
    private String loggedInUser;

    public static final int DEFAULT_SELECT_VALUE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_plan_create);
        initUi();
        initData();
        loadSpinners();


    }


    @Override
    public void initUi() {
        super.initUi();
        toolbar = findViewById(R.id.toolbarUserPlan);
        layoutChemistSpinner = findViewById(R.id.chemist_spinner_layout);
        layoutDoctorSpinner = findViewById(R.id.doctor_spinner_layout);
//        radioGroupSelect = findViewById(R.id.radio_group_select);
//        spinnerPlanDoctor = findViewById(R.id.spinner_plan_doctor);
//        spinnerPlanChemist = findViewById(R.id.spinner_plan_chemist);
        spinnerPlanPatch = findViewById(R.id.spinner_plan_patch);
        spinnerPlanMonth = findViewById(R.id.spinner_plan_month);
        spinnerPlanYear = findViewById(R.id.spinner_plan_year);
        spinnerPlanTerritory = findViewById(R.id.spinner_plan_territory);
//        radioDoctor = findViewById(R.id.radio_doctor);
//        radioChemist = findViewById(R.id.radio_chemist);
        textPlanCall = findViewById(R.id.text_plan_call);
        textPlanRemark = findViewById(R.id.text_plan_remark);
        buttonAddPlan = findViewById(R.id.button_add_plan);
//        if (isDoctorRadioChecked) {
//            radioDoctor.setChecked(isDoctorRadioChecked);
//            layoutChemistSpinner.setVisibility(View.GONE);
//        }
        buttonAddPlan.setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Plan");
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

    }

    @Override
    public void initData() {
        super.initData();

//        chemistsList = new ArrayList<>();
//        doctorsList = new ArrayList<>();
        patchesList = new ArrayList<>();
        usersList = new ArrayList<>();
        loggedInUser = connector.getString(AppConstants.USER_ID);
        runningYear = Calendar.getInstance().get(Calendar.YEAR);
//        radioGroupSelect.setOnCheckedChangeListener((group, checkedId) -> {
//            switch (checkedId) {
//                case R.id.radio_chemist:
//                    layoutDoctorSpinner.setVisibility(View.GONE);
//                    layoutChemistSpinner.setVisibility(View.VISIBLE);
//                    isDoctorRadioChecked = false;
//                    break;
//                case R.id.radio_doctor:
//                    layoutDoctorSpinner.setVisibility(View.VISIBLE);
//                    layoutChemistSpinner.setVisibility(View.GONE);
//                    isDoctorRadioChecked = true;
//                    break;
//            }
//        });


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
                selectedMonth = position + DEFAULT_SELECT_VALUE;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedMonth = parent.getSelectedItemPosition() + DEFAULT_SELECT_VALUE;
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
//                    if (isDoctorRadioChecked) {
//                        getDoctorsList(token, patchId);
//                    } else {
//                        getChemistList(token, patchId);
//                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    public void uiValidation() {
        callStr = textPlanCall.getText().toString().trim();
        remarkStr = textPlanRemark.getText().toString().trim();
        plans = new Plans();
//        if (isDoctorRadioChecked) {
//            if (!doctorsList.isEmpty()) {
//                if (spinnerPlanDoctor.getSelectedItemPosition()!=0) {
//                    doctorId = doctorsList.get(spinnerPlanDoctor.getSelectedItemPosition()- DEFAULT_SELECT_VALUE).getDoctorId() + "";
//                    chemistId = null;
//                }
//            } else {
//                Toast.makeText(ActivityUserPlanCreate.this, "Can create plan, doctor's list not found", Toast.LENGTH_LONG).show();
//            }
//        } else if (!isDoctorRadioChecked) {
//            if (!chemistsList.isEmpty()) {
//                if (spinnerPlanChemist.getSelectedItemPosition()!= 0) {
//                    chemistId = chemistsList.get(spinnerPlanChemist.getSelectedItemPosition()- DEFAULT_SELECT_VALUE).getChemistId() + "";
//                    doctorId = null;
//                }
//            } else {
//                Toast.makeText(ActivityUserPlanCreate.this, "Can create plan, chemist's list not found", Toast.LENGTH_LONG).show();
//
//            }
//        }
        if (TextUtils.isEmpty(callStr)) {
            textPlanCall.requestFocus();
            textPlanCall.setError(getString(R.string.invalid_input));
        } else if (TextUtils.isEmpty(remarkStr)) {
            textPlanRemark.requestFocus();
            textPlanRemark.setError(getString(R.string.invalid_input));
        } else {
            plans.setCalls(callStr);
//            plans.setChemistsId(chemistId);
//            plans.setDoctorId(doctorId);
            plans.setUserId(loggedInUser);
            plans.setPatchId(patchId + "");
            plans.setYear(yearStr);
            plans.setMonth(selectedMonth + "");
            plans.setRemark(remarkStr);

            if (InternetConnection.isNetworkAvailable(ActivityUserPlanCreate.this)) {
                createPlanApi(token, plans);
            } else {
                Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
            }

        }
    }


    public void getPatchList(String token, int territoryId) {
        processDialog.showDialog(ActivityUserPlanCreate.this, false);
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
                Toast.makeText(ActivityUserPlanCreate.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


    public void getTerritoryList(String token) {
        processDialog.showDialog(ActivityUserPlanCreate.this, false);
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
                Toast.makeText(ActivityUserPlanCreate.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void getDoctorsList(String token, int patchId) {
        processDialog.showDialog(ActivityUserPlanCreate.this, false);
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
//                        setArrayAdapter(stringDoctorsList, spinnerPlanDoctor);
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
                Toast.makeText(ActivityUserPlanCreate.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


    public void getChemistList(String token, int patchId) {
        processDialog.showDialog(ActivityUserPlanCreate.this, false);
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
//                    setArrayAdapter(stringChemistList, spinnerPlanChemist);
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
    public void createPlanApi(String token, Plans plans) {
        Log.e("Plan Object", new Gson().toJson(plans));
        processDialog.showDialog(ActivityUserPlanCreate.this, false);
        Call<MainResponse> call = apiInterface.addPlan(token, plans);

        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                Log.e(TAG, new Gson().toJson(response.body()));
                if (response.code() == 400) {
                    try {
                        Toast.makeText(ActivityUserPlanCreate.this, response.errorBody().string(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (response.body().getStatusCode() == AppConstants.RESULT_OK) {
                        Intent intent = new Intent(ActivityUserPlanCreate.this, MainActivity.class);
                        startActivity(intent);
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


    private void loadSpinners() {
//        getChemistList(token);
          getTerritoryList(token);
//        getPatchList(token);


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
