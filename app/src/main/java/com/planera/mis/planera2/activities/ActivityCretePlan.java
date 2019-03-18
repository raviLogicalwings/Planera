package com.planera.mis.planera2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.planera.mis.planera2.R;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ActivityCretePlan extends BaseActivity implements View.OnClickListener {

    public static final int DEFAULT_SELECT_VALUE = 1;
    private Spinner spinnerPlanUser;
    private Spinner spinnerPlanPatch;
    private Spinner spinnerPlanMonth;
    private Spinner spinnerPlanTerritory;
    private Spinner spinnerPlanYear;
    private TextInputLayout inputLayoutTerritoryPlan;
    private TextInputLayout inputLayoutPatchPlan;
    private TextInputLayout inputLayoutUserPlan;
    private EditText textPlanCall;
    private EditText textPlanRemark;
    protected Button buttonAddPlan;

    private List<Patches> patchesList = null;
    private List<UserData> usersList = null;
    private List<Territories> territoriesList;
    protected List<String> months;
    private List<String> years;

    protected Plans plans;

    private int patchId;
    private int selectedMonth;
    protected int runningYear;
    private int territoryId = 0;
    private String userIdStr;
    private String yearStr;
    protected String callStr;
    protected String remarkStr;

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
        callStr = textPlanCall.getText().toString().trim();
        remarkStr = textPlanRemark.getText().toString().trim();
        plans = new Plans();

        if (spinnerPlanTerritory.getSelectedItemPosition() == 0 ){
            inputLayoutTerritoryPlan.setError("Please select territory.");
            inputLayoutTerritoryPlan.requestFocus();
        }
        else if(spinnerPlanPatch.getSelectedItemPosition() == 0 ){
            inputLayoutPatchPlan.setError("Please select patch.");
            inputLayoutTerritoryPlan.requestFocus();
        }
        else if (spinnerPlanUser.getSelectedItemPosition() == 0){
            inputLayoutUserPlan.setError("Please select user.");
        }
        else if (TextUtils.isEmpty(callStr)) {
            textPlanCall.requestFocus();
            textPlanCall.setError(getString(R.string.invalid_input));
        } else if (TextUtils.isEmpty(remarkStr)) {
            textPlanRemark.requestFocus();
            textPlanRemark.setError(getString(R.string.invalid_input));
        }


        else {
            plans.setCalls(callStr);
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

        spinnerPlanUser = findViewById(R.id.spinner_plan_user);
        spinnerPlanPatch = findViewById(R.id.spinner_plan_patch);
        spinnerPlanMonth = findViewById(R.id.spinner_plan_month);
        spinnerPlanYear = findViewById(R.id.spinner_plan_year);
        spinnerPlanTerritory = findViewById(R.id.spinner_plan_territory);


        inputLayoutTerritoryPlan = findViewById(R.id.input_layout_territory_plan);
        inputLayoutPatchPlan = findViewById(R.id.input_layout_patch_plan);
        inputLayoutUserPlan = findViewById(R.id.input_layout_user_plan);


        textPlanCall = findViewById(R.id.text_plan_call);
        textPlanRemark = findViewById(R.id.text_plan_remark);

        buttonAddPlan = findViewById(R.id.button_create_plan);
        buttonAddPlan.setOnClickListener(this);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.title_add_plan);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

    }


    public void createPlanApi(String token, Plans plans) {
        Log.e("Plans", new Gson().toJson(plans));
        processDialog.showDialog(ActivityCretePlan.this, false);
        Call<MainResponse> call = apiInterface.addPlan(token, plans);

        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(@NonNull Call<MainResponse> call, @NonNull Response<MainResponse> response) {
                processDialog.dismissDialog();
                Log.e(TAG, new Gson().toJson(response.body()));
                if (response.code() == 400) {
                    try {
                        assert response.errorBody() != null;
                        Toast.makeText(ActivityCretePlan.this, response.errorBody().string(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    assert response.body() != null;
                    if (response.body().getStatusCode() == AppConstants.RESULT_OK) {
                        Intent intent = new Intent(ActivityCretePlan.this, SingleListActivity.class);
                        intent.putExtra(AppConstants.KEY_TOUCHED_FRAGMENT, AppConstants.PLAN_FRAGMENT);
                        startActivity(intent);
                        finish();
                    } else {
                        Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MainResponse> call, @NonNull Throwable t) {
                processDialog.dismissDialog();
                Snackbar.make(rootView, t.getMessage(), Snackbar.LENGTH_LONG).show();

            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ActivityCretePlan.this, SingleListActivity.class);
        intent.putExtra(AppConstants.KEY_TOUCHED_FRAGMENT, AppConstants.PLAN_FRAGMENT);
        startActivity(intent);
    }

    @Override
    public void initData() {
        super.initData();
        patchesList = new ArrayList<>();
        usersList = new ArrayList<>();
        runningYear = Calendar.getInstance().get(Calendar.YEAR);



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
        spinnerPlanUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    inputLayoutUserPlan.setError(null);
                    userIdStr = usersList.get(position - DEFAULT_SELECT_VALUE).getUserId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerPlanTerritory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    inputLayoutTerritoryPlan.setError(null);
                    territoryId = territoriesList.get(position - DEFAULT_SELECT_VALUE).getTerritoryId();
                    if (InternetConnection.isNetworkAvailable(ActivityCretePlan.this)){
                        getPatchList(token, territoryId);
                    }
                    else
                    {
                        Snackbar.make(rootView, R.string.no_internet, Snackbar.LENGTH_LONG).show();
                    }

                }
                else{
                    spinnerPlanTerritory.setSelection(0, false);
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
                    inputLayoutPatchPlan.setError(null);
                    patchId = patchesList.get(position - DEFAULT_SELECT_VALUE).getPatchId();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }


    private void loadSpinners() {


        if (InternetConnection.isNetworkAvailable(ActivityCretePlan.this)){
            getUsersList(token);
            getTerritoryList(token);
        }
        else{
            Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
        }


    }


    public void getTerritoryList(String token) {
        processDialog.showDialog(ActivityCretePlan.this, false);
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
                    setArrayAdapter(stringTerritoryList, spinnerPlanTerritory);


                } else {
                    Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<TerritoryListResponse> call, @NonNull Throwable t) {
                Toast.makeText(ActivityCretePlan.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void getPatchList(String token, int territoryId) {
        processDialog.showDialog(ActivityCretePlan.this, false);
        Call<PatchListResponse> call = apiInterface.patchListByTerritory(token, territoryId);
        call.enqueue(new Callback<PatchListResponse>() {
            @Override
            public void onResponse(@NonNull Call<PatchListResponse> call, @NonNull Response<PatchListResponse> response) {
                processDialog.dismissDialog();
                assert response.body() != null;
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

            @Override
            public void onFailure(@NonNull Call<PatchListResponse> call, @NonNull Throwable t) {
                processDialog.dismissDialog();
                Toast.makeText(ActivityCretePlan.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void getUsersList(String token) {
        Call<UserListResponse> call = apiInterface.usersList(token);
        call.enqueue(new Callback<UserListResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserListResponse> call, @NonNull Response<UserListResponse> response) {
                assert response.body() != null;
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
            public void onFailure(@NonNull Call<UserListResponse> call, @NonNull Throwable t) {
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

