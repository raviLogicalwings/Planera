package com.planera.mis.planera2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.gson.Gson;
import com.planera.mis.planera2.FragmentDialog.UsersListDialog;
import com.planera.mis.planera2.R;
import com.planera.mis.planera2.adapters.InputListAdapter;
import com.planera.mis.planera2.models.AMs;
import com.planera.mis.planera2.models.DataItem;
import com.planera.mis.planera2.models.MRs;
import com.planera.mis.planera2.models.ObtainReport;
import com.planera.mis.planera2.models.ReportListResponce;
import com.planera.mis.planera2.utils.AppConstants;
import com.planera.mis.planera2.utils.InternetConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInputListActivity extends BaseActivity implements InputListAdapter.OnInputItemClickListener, UsersListDialog.OnSelectUserListener {
    private Toolbar toolbar;
    private RecyclerView recycleListOfInput;

    private LinearLayout linearNoData;
    private LinearLayout linearNoInternet;
    private Button buttonRetry;
    private List<DataItem> dataItemsList;
    private List<MRs> jointUserList;
    private InputListAdapter.OnInputItemClickListener onInputItemClickListener;

    protected InputListAdapter inputListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_input_list);
        initUi();
        initData();
        onInputItemClickListener = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void initUi() {
        super.initUi();
        toolbar = findViewById(R.id.toolbar);
        recycleListOfInput = findViewById(R.id.recycle_list_of_input);
        linearNoData = findViewById(R.id.linear_no_data);
        linearNoInternet = findViewById(R.id.linear_no_internet);
        buttonRetry = findViewById(R.id.button_retry);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.title_user_input_list);

        if (!InternetConnection.isNetworkAvailable(UserInputListActivity.this)) {
            linearNoInternet.setVisibility(View.VISIBLE);
            buttonRetry.setVisibility(View.VISIBLE);
        }

        buttonRetry.setOnClickListener(v -> {
        finish();
        startActivity(getIntent());
    });
    }

    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        String obtainReport = intent.getStringExtra(AppConstants.OBTAIN_REPORT);
        ObtainReport report = new Gson().fromJson(obtainReport, ObtainReport.class);
        if (report != null){
            if (InternetConnection.isNetworkAvailable(this)){
                Log.e("Param of List Report", new Gson().toJson(report));
                getUserReport(token, report);
            }
            else
            {
                Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
            }
        }
    }

    public void getUserReport(String token, ObtainReport report){
        processDialog.showDialog(UserInputListActivity.this, false);
        Call<ReportListResponce> call = apiInterface.reportListUser(token, report);
        if (call != null){
            call.enqueue(new Callback<ReportListResponce>() {
                @Override
                public void onResponse(@NonNull Call<ReportListResponce> call, Response<ReportListResponce> response) {
                    processDialog.dismissDialog();

                    if (response.code() == 400){
                        Toast.makeText(UserInputListActivity.this, "Error Code", Toast.LENGTH_LONG).show();
                    }
                    assert response.body() != null;
                    if (response.body().getStatuscode() == AppConstants.RESULT_OK){
                        Log.e("Data of Items", new Gson().toJson(response.body()));
                        dataItemsList = response.body().getData();

                        if (dataItemsList != null){
                            initAdapter(dataItemsList, recycleListOfInput);
                        }
                    }
                    else{
                        linearNoData.setVisibility(View.VISIBLE);
                        Toast.makeText(UserInputListActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ReportListResponce> call, Throwable t) {
                    processDialog.dismissDialog();
                    linearNoInternet.setVisibility(View.VISIBLE);
                    buttonRetry.setVisibility(View.VISIBLE);
                    Toast.makeText(UserInputListActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void initAdapter(List<DataItem> dataItemsList, RecyclerView recyclerView){
        inputListAdapter = new InputListAdapter(UserInputListActivity.this, dataItemsList, onInputItemClickListener);
        setAdapter(recyclerView, inputListAdapter);

    }

    public void setAdapter(RecyclerView recyclerView, InputListAdapter adapter){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onInputItemClick(DataItem item) {

        item.setJointUserList(jointUserList);
        detailsForUpdateInput(item);
    }

    public void detailsForUpdateInput(DataItem sample){
        Intent intent = new Intent(UserInputListActivity.this, AddInputActivity.class);
        intent.putExtra(AppConstants.IS_INPUT_UPDATE, true);
        String passInputDetails = new Gson().toJson(sample);
        intent.putExtra(AppConstants.PASS_UPDATE_INPUT, passInputDetails);
        startActivity(intent);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onSelectUser(List<MRs> mrs) {
        jointUserList = mrs;

        inputListAdapter.setJointUserList(jointUserList);
        inputListAdapter.notifyDataSetChanged();

    }

}
