package com.planera.mis.planera2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.models.PatchListResponse;
import com.planera.mis.planera2.models.Patches;
import com.planera.mis.planera2.models.Territories;
import com.planera.mis.planera2.models.TerritoryListResponse;
import com.planera.mis.planera2.utils.AppConstants;
import com.planera.mis.planera2.utils.InternetConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends BaseActivity implements View.OnClickListener {
    private Spinner territorySpinnerHome;
    private Spinner patchSpinnerHome;
    private Button buttonSubmitQuery;
    protected Toolbar toolbar;

    private List<Territories> territoryList;
    private List<Patches> patchesList = null;
    private List<String> stringTerritoryList;
    private List<String> stringPatchesList;

    public static final int DEFAULT_SELECT_VALUE = 1;
    private Integer territoryId;
    private Integer patchId;
    private String patchStr;
    private String territoryStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initUi();
        initData();
    }

    @Override
    public void initUi() {
        super.initUi();
        territorySpinnerHome = findViewById(R.id.territory_spinner_home);
        patchSpinnerHome = findViewById(R.id.patch_spinner_home);
        buttonSubmitQuery = findViewById(R.id.submit_query);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("Select");
        buttonSubmitQuery.setVisibility(View.GONE);
        buttonSubmitQuery.setOnClickListener(this);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Select");
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_logout:
                backToLogin();
                break;
        }
        return true;
    }

    @Override
    public void initData() {
        super.initData();

        getTerritoryList(token);

        territorySpinnerHome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    territoryStr = stringTerritoryList.get(position);
                    territoryId = territoryList.get(position - DEFAULT_SELECT_VALUE).getTerritoryId();
                    if (InternetConnection.isNetworkAvailable(HomeActivity.this)){
                        getPatchList(token, territoryId);
                    }
                    else
                    {
                        Snackbar.make(rootView, R.string.no_internet, Snackbar.LENGTH_LONG).show();
                    }
                }
                else{
                    buttonSubmitQuery.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        patchSpinnerHome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    patchStr = stringPatchesList.get(position);
                    patchId = patchesList.get(position - DEFAULT_SELECT_VALUE).getPatchId();

                    if (territoryId != null) {
                        if (!patchStr.equals(getString(R.string.select)) && !territoryStr.equals(getString(R.string.select))) {
                            buttonSubmitQuery.setVisibility(View.VISIBLE);
                            connector.setInteger(AppConstants.KEY_PATCH_ID, patchId);
                        }
                    }
                } else {
                    buttonSubmitQuery.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    public void getTerritoryList(String token) {
        processDialog.showDialog(HomeActivity.this, false);
        Call<TerritoryListResponse> call = apiInterface.territoryList(token);
        call.enqueue(new Callback<TerritoryListResponse>() {
            @Override
            public void onResponse(@NonNull Call<TerritoryListResponse> call, @NonNull Response<TerritoryListResponse> response) {
                processDialog.dismissDialog();
                assert response.body() != null;
                if (response.body().getStatusCode() == AppConstants.RESULT_OK) {
                    territoryList = response.body().getTerritorysList();
                    if (territoryList.size() > 0) {
                        patchSpinnerHome.setVisibility(View.VISIBLE);

                        stringTerritoryList = new ArrayList<>();
                        stringTerritoryList.add(getString(R.string.select));
                        for (int i = 0; i < territoryList.size(); i++) {
                            stringTerritoryList.add(territoryList.get(i).getTerritoryName());
                        }
                        setArrayAdapter(stringTerritoryList, territorySpinnerHome);
                    }


                } else {
                    Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<TerritoryListResponse> call, @NonNull Throwable t) {
                Toasty.error(HomeActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


    public void getPatchList(String token, int territoryId) {
        processDialog.showDialog(HomeActivity.this, false);
        Call<PatchListResponse> call = apiInterface.patchListByTerritory(token, territoryId);
        call.enqueue(new Callback<PatchListResponse>() {
            @Override
            public void onResponse(@NonNull Call<PatchListResponse> call, @NonNull Response<PatchListResponse> response) {
                processDialog.dismissDialog();
                assert response.body() != null;
                if (response.body().getStatusCode() == AppConstants.RESULT_OK) {
                    patchesList = response.body().getPatchesList();
                    if (!patchesList.isEmpty()) {
                        stringPatchesList = new ArrayList<>();
                        stringPatchesList.add(getString(R.string.select));
                        for (int i = 0; i < patchesList.size(); i++) {
                            stringPatchesList.add(patchesList.get(i).getPatchName());
                        }
                        setArrayAdapter(stringPatchesList, patchSpinnerHome);

                    }
                } else {
                    Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PatchListResponse> call, @NonNull Throwable t) {
                processDialog.dismissDialog();
                Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void setArrayAdapter(List<String> listString, Spinner spinner) {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (this, R.layout.spinner_item,
                        listString);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

    }

    public void backToLogin(){
        connector.setBoolean(AppConstants.IS_LOGIN, false);
        Intent intentLogin = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intentLogin);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_query:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}
