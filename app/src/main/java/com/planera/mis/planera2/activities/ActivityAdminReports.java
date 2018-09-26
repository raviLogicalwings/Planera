package com.planera.mis.planera2.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.adapters.ReportListAdapter;
import com.planera.mis.planera2.activities.models.ChemistListResponse;
import com.planera.mis.planera2.activities.models.Chemists;
import com.planera.mis.planera2.activities.models.DataItem;
import com.planera.mis.planera2.activities.models.Doctors;
import com.planera.mis.planera2.activities.models.DoctorsListResponce;
import com.planera.mis.planera2.activities.models.ObtainReport;
import com.planera.mis.planera2.activities.models.PatchListResponse;
import com.planera.mis.planera2.activities.models.Patches;
import com.planera.mis.planera2.activities.models.ReportListResponce;
import com.planera.mis.planera2.activities.models.Territories;
import com.planera.mis.planera2.activities.models.TerritoryListResponse;
import com.planera.mis.planera2.activities.models.UserData;
import com.planera.mis.planera2.activities.models.UserListResponse;
import com.planera.mis.planera2.activities.utils.AppConstants;
import com.planera.mis.planera2.activities.utils.FileCreation;
import com.planera.mis.planera2.activities.utils.InternetConnection;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;


public class ActivityAdminReports extends BaseActivity implements View.OnClickListener{
    private RelativeLayout parentPanel;
    private AppBarLayout appBar;
    private Toolbar toolbarReport;
    private Spinner spinnerRoleType;
    private EditText editStartTime;
    private EditText editEndTime;
    private Button buttonSubmitReport;
    private Spinner spinnerTerritoryReport;
    private Spinner spinnerPatchReport;
    private Spinner spinnerChemistReport;
    private Spinner spinnerDoctorReport;
    private Spinner spinnerUserReport;
    private LinearLayout layoutChemistReport, layoutDoctorReport, layoutUserReport;
    private RecyclerView reportsListView;
    private List<Patches> patchesList;
    private List<Territories> territorysList;
    private List<Chemists> chemistsList;
    private List<Doctors> doctorsList;
    private List<UserData> usersList;
    private List<DataItem> dataItemsList;
    private FloatingActionButton buttonExport;
    public static final int DEFAULT_SELECT_VALUE= 1;
    public ObtainReport obtainReport;




    private String strPickedDate;
    private List<String> listOfRoles;
    private int territoryId;
    private int patchId;
    private String selectedRole;
    private String id;
    private ReportListAdapter.OnItemDeleteListener listener;
    private List<String> stringDoctorsList;
    private List<String> stringChemistList;
    private List<String> stringUserList;
    private List<String> stringPatchesList;
    private List<String> stringTerritoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_reports);
        initUi();
        initData();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void initUi() {
        super.initUi();
        parentPanel = findViewById(R.id.parentPanel);
        appBar = findViewById(R.id.appBar);
        toolbarReport = findViewById(R.id.toolbarReports);
        spinnerRoleType = findViewById(R.id.spinner_role_type);
        editStartTime = findViewById(R.id.edit_start_time);
        editEndTime = findViewById(R.id.edit_end_time);
        buttonSubmitReport = findViewById(R.id.button_submit_report);
        spinnerTerritoryReport = (Spinner) findViewById(R.id.spinner_territory_report);
        spinnerPatchReport = (Spinner) findViewById(R.id.spinner_patch_report);
        spinnerChemistReport = (Spinner) findViewById(R.id.spinner_chemist_report);
        spinnerDoctorReport = (Spinner) findViewById(R.id.spinner_doctor_report);
        spinnerUserReport = (Spinner) findViewById(R.id.spinner_user_report);
        reportsListView = (RecyclerView) findViewById(R.id.reports_list_view);
        layoutChemistReport = findViewById(R.id.layout_chemist_report);
        layoutDoctorReport = findViewById(R.id.layout_doctor_report);
        layoutUserReport = findViewById(R.id.layout_user_report);
        buttonExport = findViewById(R.id.button_export);
        buttonSubmitReport.setOnClickListener(this);
        editEndTime.setOnClickListener(this);
        editStartTime.setOnClickListener(this);

        buttonExport.setOnClickListener(this);
        setSupportActionBar(toolbarReport);
        getSupportActionBar().setTitle("Reports");
        toolbarReport.setNavigationOnClickListener(view -> onBackPressed());
    }

    @Override
    public void initData() {
        super.initData();
        obtainReport = new ObtainReport();
        chemistsList = new ArrayList<>();
        doctorsList = new ArrayList<>();
        patchesList = new ArrayList<>();
        usersList = new ArrayList<>();

        initRoles();

        spinnerTerritoryReport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        spinnerPatchReport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    patchId = patchesList.get(position - DEFAULT_SELECT_VALUE).getPatchId();
                    if (selectedRole.equals("1")){
                        getDoctorsList(token, patchId);

                    }
                    if (selectedRole.equals("2")){

                        getChemistList(token, patchId);
                    }
                    if (selectedRole.equals("3")){

                        getUsersList(token);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinnerRoleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getTerritoryList(token);
                spinnerPatchReport.setAdapter(null);
                spinnerDoctorReport.setAdapter(null);
                spinnerUserReport.setAdapter(null);
                spinnerChemistReport.setAdapter(null);
                    if (position == DEFAULT_SELECT_VALUE - 1 ){
                        selectedRole = "1";
                        layoutDoctorReport.setVisibility(View.VISIBLE);
                        layoutChemistReport.setVisibility(View.GONE);
                        layoutUserReport.setVisibility(View.GONE);
                    }
                    else if (position == DEFAULT_SELECT_VALUE){
                        selectedRole = "2";
                        layoutChemistReport.setVisibility(View.VISIBLE);
                        layoutUserReport.setVisibility(View.GONE);
                        layoutDoctorReport.setVisibility(View.GONE);
                    }

                    else{
                        selectedRole = "3";
                        layoutUserReport.setVisibility(View.VISIBLE);
                        layoutChemistReport.setVisibility(View.GONE);
                        layoutDoctorReport.setVisibility(View.GONE);
                    }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void uiValidation(){
        String strStartDate = editStartTime.getText().toString().trim();
        String strEndDate = editEndTime.getText().toString().trim();
        int pos =spinnerRoleType.getSelectedItemPosition();
        if (TextUtils.isEmpty(strStartDate)){
            editStartTime.setError(getString(R.string.invalid_input));
            editStartTime.requestFocus();

        }
        else if (TextUtils.isEmpty(strEndDate)){
            editEndTime.setError(getString(R.string.invalid_input));
            editEndTime.requestFocus();

        }
        else{


            if (InternetConnection.isNetworkAvailable(ActivityAdminReports.this)){

                if (selectedRole.equals("1")){
                        id = doctorsList.get(spinnerDoctorReport.getSelectedItemPosition() - DEFAULT_SELECT_VALUE).getDoctorId() + "";
                }

                if (selectedRole.equals("2")){
                    id = chemistsList.get(spinnerChemistReport.getSelectedItemPosition() - DEFAULT_SELECT_VALUE).getChemistId()+"";

                }

                if (selectedRole.equals("3")){
                    id = usersList.get(spinnerUserReport.getSelectedItemPosition() - DEFAULT_SELECT_VALUE).getUserId();
                }

                obtainReport.setStartDate(strStartDate);
                obtainReport.setEndDate(strEndDate );
                obtainReport.setType(selectedRole);
                obtainReport.setId(id);
                getReportList(token, obtainReport);
            }
        }
    }



    public void pickDateFromDialog(EditText editText){
        Calendar mCurrentTime = Calendar.getInstance();
        int mYear = mCurrentTime.get(Calendar.YEAR);
        int mMonth = mCurrentTime.get(Calendar.MONTH);
        int mDay = mCurrentTime.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityAdminReports.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                strPickedDate = year+"-"+month+"-"+dayOfMonth;
                editText.setText(strPickedDate);

            }
        },mYear, mMonth, mDay );

        //code for select on current date and onwards.
       // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//        Toast.makeText(ActivityAdminReports.this, System.currentTimeMillis()+"", Toast.LENGTH_LONG).show();
        datePickerDialog.show();

    }

    public void initRoles(){
        listOfRoles = new ArrayList<>();
        listOfRoles.add("Doctor");
        listOfRoles.add("Chemist");
        listOfRoles.add("User/MR");
        loadSpinner(spinnerRoleType, listOfRoles);
    }

    public void loadSpinner(Spinner spinner, List<String> listRole){

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item,
                        listRole);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
    }


    public void setAdapter(RecyclerView recyclerView, ReportListAdapter adapter){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public File makeFolder(){
        String folder = "Planera Reports";
        File directoryFolder = new File(Environment.getExternalStorageDirectory(), folder);
        //create directory if not exist
        if (!directoryFolder.isDirectory()) {
            directoryFolder.mkdirs();
        }


        return directoryFolder;
    }


    public void initAdapter(List<DataItem> reportData, RecyclerView recyclerView) {
       ReportListAdapter listAdapter = new ReportListAdapter(reportData, this, new ReportListAdapter.OnItemDeleteListener() {
           @Override
           public void onItemDelete(int pos) {
               reportData.remove(pos);
               Toast.makeText(mContext, pos+"'th item deleted  SuccessFully !!", Toast.LENGTH_SHORT).show();
           }
       });
        setAdapter(recyclerView, listAdapter);
    }

    public void getUsersList(String token) {
        Call<UserListResponse> call = apiInterface.usersList(token);
        call.enqueue(new Callback<UserListResponse>() {
            @Override
            public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {
                if (response.body().getStatusCode() == AppConstants.RESULT_OK) {
                    usersList = response.body().getData();
                     stringUserList = new ArrayList<>();
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
                    loadSpinner( spinnerUserReport, stringUserList);
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


    public void getTerritoryList(String token) {
        processDialog.showDialog(ActivityAdminReports.this, false);
        Call<TerritoryListResponse> call = apiInterface.territoryList(token);
        call.enqueue(new Callback<TerritoryListResponse>() {
            @Override
            public void onResponse(Call<TerritoryListResponse> call, Response<TerritoryListResponse> response) {
                processDialog.dismissDialog();
                if (response != null) {
                    if (response.body().getStatusCode() == AppConstants.RESULT_OK) {
                        territorysList = response.body().getTerritorysList();
                        stringTerritoryList = new ArrayList<>();
                        stringTerritoryList.add(getString(R.string.select));
                        for (int i = 0; i < territorysList.size(); i++) {
                            stringTerritoryList.add(territorysList.get(i).getTerritoryName());
                        }
                        loadSpinner(spinnerTerritoryReport, stringTerritoryList);


                    } else {
                        Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<TerritoryListResponse> call, Throwable t) {
                Toast.makeText(ActivityAdminReports.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void getPatchList(String token, int territoryId) {
        processDialog.showDialog(ActivityAdminReports.this, false);
        Call<PatchListResponse> call = apiInterface.patchListByTerritory(token, territoryId);
        call.enqueue(new Callback<PatchListResponse>() {
            @Override
            public void onResponse(Call<PatchListResponse> call, Response<PatchListResponse> response) {
                processDialog.dismissDialog();
                if (response != null) {
                    if (response.body().getStatusCode() == AppConstants.RESULT_OK) {
                        patchesList = response.body().getPatchesList();
                        if (!patchesList.isEmpty()) {
                           stringPatchesList = new ArrayList<>();
                            stringPatchesList.add(getString(R.string.select));
                            for (int i = 0; i < patchesList.size(); i++) {
                                stringPatchesList.add(patchesList.get(i).getPatchName());
                            }
                           loadSpinner(spinnerPatchReport, stringPatchesList);


                        }
                    } else {
                        Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<PatchListResponse> call, Throwable t) {
                processDialog.dismissDialog();
                Toast.makeText(ActivityAdminReports.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
    public void getDoctorsList(String token, int patchId) {
        processDialog.showDialog(ActivityAdminReports.this, false);
        Call<DoctorsListResponce> call = apiInterface.patchesWiseDoctorList(token, patchId);
        call.enqueue(new Callback<DoctorsListResponce>() {
            @Override
            public void onResponse(Call<DoctorsListResponce> call, Response<DoctorsListResponce> response) {
                processDialog.dismissDialog();
                Log.e(TAG, "onResponse: DoctorsList" + new Gson().toJson(response.body()));
                if (response.body().getStatusCode() == AppConstants.RESULT_OK) {
                    doctorsList = response.body().getData();
                    if (!doctorsList.isEmpty()) {
                        stringDoctorsList = new ArrayList<>();
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
                        loadSpinner( spinnerDoctorReport ,  stringDoctorsList);
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
                Toast.makeText(ActivityAdminReports.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void getChemistList(String token, int patchId) {
        processDialog.showDialog(ActivityAdminReports.this, false);
        Call<ChemistListResponse> call = apiInterface.patchesWiseChemistList(token, patchId);
        call.enqueue(new Callback<ChemistListResponse>() {
            @Override
            public void onResponse(@NonNull Call<ChemistListResponse> call, Response<ChemistListResponse> response) {
                processDialog.dismissDialog();
                if (response.body().getStatusCode() == AppConstants.RESULT_OK) {
                    chemistsList = response.body().getData();
                    stringChemistList = new ArrayList<>();
                    stringChemistList.add(getString(R.string.select));
                    for (int i = 0; i < chemistsList.size(); i++) {
                        stringChemistList.add(chemistsList.get(i).getFirstName() + " " + chemistsList.get(i).getLastName());
                    }
                  loadSpinner(spinnerChemistReport, stringChemistList);
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

    public void getReportList(String token, ObtainReport report){
        Log.e("Obtain Report", new Gson().toJson(report));
        processDialog.showDialog(ActivityAdminReports.this, false);


        Call<ReportListResponce> call = apiInterface.reportList(token, report);
        if (call != null){
            call.enqueue(new Callback<ReportListResponce>() {
                @Override
                public void onResponse(Call<ReportListResponce> call, Response<ReportListResponce> response) {
                    processDialog.dismissDialog();

                    if (response.code() == 400){
                       Toast.makeText(ActivityAdminReports.this, "Error Code", Toast.LENGTH_LONG).show();
                    }
                    if (response.body().getStatuscode() == AppConstants.RESULT_OK){
                      Log.e("Data of Items", new Gson().toJson(response.body()));
                      dataItemsList = response.body().getData();

                      if (dataItemsList != null){
                          initAdapter(dataItemsList, reportsListView);
                      }



                    }
                    else{
                        Toast.makeText(ActivityAdminReports.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ReportListResponce> call, Throwable t) {
                    processDialog.dismissDialog();
                    Toast.makeText(ActivityAdminReports.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_submit_report:
                uiValidation();
                break;
            case R.id.edit_start_time:
                pickDateFromDialog(editStartTime);
                break;
            case R.id.edit_end_time:
                pickDateFromDialog(editEndTime);
                break;
            case R.id.button_export:
                FileCreation fileCreation = new FileCreation();
                File toCreate = makeFolder();
                fileCreation.exportReport(dataItemsList , toCreate, ActivityAdminReports.this, Integer.parseInt(selectedRole));
                break;
        }

    }
}
