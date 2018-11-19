package com.planera.mis.planera2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.gson.Gson;
import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.adapters.InputListAdapter;
import com.planera.mis.planera2.activities.models.DataItem;
import com.planera.mis.planera2.activities.models.ObtainReport;
import com.planera.mis.planera2.activities.models.ReportListResponce;
import com.planera.mis.planera2.activities.utils.AppConstants;
import com.planera.mis.planera2.activities.utils.InternetConnection;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInputListActivity extends BaseActivity implements InputListAdapter.OnInputItemClickListener{
    private Toolbar toolbar;
    private RecyclerView recycleListOfInput;

    private LinearLayout layouNoData;

    private List<DataItem> dataItemsList;
    private InputListAdapter.OnInputItemClickListener onInputItemClickListener;

    private InputListAdapter inputListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_input_list);
        initUi();
        initData();
        onInputItemClickListener = this;
    }


    @Override
    public void initUi() {
        super.initUi();
        toolbar = findViewById(R.id.toolbar);
        recycleListOfInput = findViewById(R.id.recycle_list_of_input);
        layouNoData = findViewById(R.id.layout_no_data);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("User Input List");
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
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
        }


    }



    public void getUserReport(String token, ObtainReport report){
        Log.e("Obtain Report User"+token, new Gson().toJson(report));
        processDialog.showDialog(UserInputListActivity.this, false);


        Call<ReportListResponce> call = apiInterface.reportListUser(token, report);
        if (call != null){
            call.enqueue(new Callback<ReportListResponce>() {
                @Override
                public void onResponse(Call<ReportListResponce> call, Response<ReportListResponce> response) {
                    processDialog.dismissDialog();
                    Log.e("Report List", new Gson().toJson(response.body()));

                    if (response.code() == 400){
                        Toast.makeText(UserInputListActivity.this, "Error Code", Toast.LENGTH_LONG).show();
                    }
                    if (response.body().getStatuscode() == AppConstants.RESULT_OK){
                        Log.e("Data of Items", new Gson().toJson(response.body()));
                        dataItemsList = response.body().getData();

                        if (dataItemsList != null){
                            initAdapter(dataItemsList, recycleListOfInput);
                        }

                    }
                    else{
                        Log.e("User Reports Response", response.body().getMessage());
                        Toast.makeText(UserInputListActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ReportListResponce> call, Throwable t) {
                    processDialog.dismissDialog();
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
        detailsForUpdateInput(item);
        Log.e("Actual Data", new Gson().toJson(item));
    }

    public void detailsForUpdateInput(DataItem sample){
        Intent intent = new Intent(UserInputListActivity.this, AddInputActivity.class);
        intent.putExtra(AppConstants.IS_INPUT_UPDATE, true);
        String passInputDetails = new Gson().toJson(sample);
        intent.putExtra(AppConstants.PASS_UPDATE_INPUT, passInputDetails);
        startActivity(intent);
    }
}
