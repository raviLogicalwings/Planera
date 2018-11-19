package com.planera.mis.planera2.activities;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
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


public class ActivityAdminReports extends BaseActivity implements View.OnClickListener {
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
    private TextInputLayout inputLayoutRoleType;
    private TextInputLayout inputLayoutTerritoryReport;
    private TextInputLayout inputLayoutPatchReport;
    private TextInputLayout inputLayoutChemistReport;
    private TextInputLayout inputLayoutDoctorReport;
    private TextInputLayout inputLayoutUserReport;
    private LinearLayout layoutChemistReport, layoutDoctorReport, layoutUserReport;
    private RecyclerView reportsListView;
    private List<Patches> patchesList;
    private List<Territories> territoriesList;
    private List<Chemists> chemistsList;
    private List<Doctors> doctorsList;
    private List<UserData> usersList;
    private List<DataItem> dataItemsList;
    private FloatingActionButton buttonExport;
    public static final int DEFAULT_SELECT_VALUE = 1;
    public ObtainReport obtainReport;
    public int COLUMN_ID = 0;


    private String strPickedDate;
    private List<String> listOfRoles;
    private int territoryId;
    private int patchId;
    private String selectedRole;
    private String userId;
    private ReportListAdapter.OnItemDeleteListener listener;
    private List<String> stringDoctorsList;
    private List<String> stringChemistList;
    private List<String> stringUserList;
    private List<String> stringPatchesList;
    private List<String> stringTerritoryList;
    private String doctorId;
    private String chemistId;
    private TableLayout mainTableLayout;
    private TableRow tr_head;

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
        spinnerTerritoryReport = findViewById(R.id.spinner_territory_report);
        spinnerPatchReport = findViewById(R.id.spinner_patch_report);
        spinnerChemistReport = findViewById(R.id.spinner_chemist_report);
        spinnerDoctorReport = findViewById(R.id.spinner_doctor_report);
        spinnerUserReport = findViewById(R.id.spinner_user_report);
        inputLayoutRoleType = findViewById(R.id.input_layout_role_type);
        inputLayoutTerritoryReport = findViewById(R.id.input_layout_territory_report);
        inputLayoutPatchReport = findViewById(R.id.input_layout_patch_report);
        inputLayoutDoctorReport = findViewById(R.id.input_layout_doctor_report);
        inputLayoutChemistReport = findViewById(R.id.input_layout_chemist_report);
        inputLayoutUserReport = findViewById(R.id.input_layout_user_report);
        reportsListView = findViewById(R.id.reports_list_view);
        layoutChemistReport = findViewById(R.id.layout_chemist_report);
        layoutDoctorReport = findViewById(R.id.layout_doctor_report);
        layoutUserReport = findViewById(R.id.layout_user_report);
        buttonExport = findViewById(R.id.button_export);
        mainTableLayout = findViewById(R.id.main_table);

        tr_head = new TableRow(this);
        tr_head.setBackgroundResource(R.drawable.bg_input_box);
        tr_head.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

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
        initRoles();
        obtainReport = new ObtainReport();
        chemistsList = new ArrayList<>();
        doctorsList = new ArrayList<>();
        patchesList = new ArrayList<>();
        usersList = new ArrayList<>();


        spinnerTerritoryReport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    territoryId = territoriesList.get(position - DEFAULT_SELECT_VALUE).getTerritoryId();
                    inputLayoutTerritoryReport.setError(null);
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
                if (position != 0) {
                    inputLayoutPatchReport.setError(null);
                    patchId = patchesList.get(position - DEFAULT_SELECT_VALUE).getPatchId();
                    if (selectedRole.equals("1")) {
                        getDoctorsList(token, patchId);

                    }
                    if (selectedRole.equals("2")) {

                        getChemistList(token, patchId);
                    }
                    if (selectedRole.equals("3")) {

                        getUsersList(token);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerDoctorReport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    inputLayoutDoctorReport.setError(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerChemistReport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    inputLayoutChemistReport.setError(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerUserReport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    inputLayoutUserReport.setError(null);
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
                if (position == DEFAULT_SELECT_VALUE - 1) {
                    selectedRole = "1";
                    layoutDoctorReport.setVisibility(View.VISIBLE);
                    layoutChemistReport.setVisibility(View.GONE);
                    layoutUserReport.setVisibility(View.GONE);
                } else if (position == DEFAULT_SELECT_VALUE) {
                    selectedRole = "2";
                    layoutChemistReport.setVisibility(View.VISIBLE);
                    layoutUserReport.setVisibility(View.GONE);
                    layoutDoctorReport.setVisibility(View.GONE);
                } else {
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


    public void uiValidation() {
        String strStartDate = editStartTime.getText().toString().trim();
        String strEndDate = editEndTime.getText().toString().trim();
        int pos = spinnerRoleType.getSelectedItemPosition();

        if (spinnerTerritoryReport.getSelectedItemPosition() == 0) {
            inputLayoutTerritoryReport.setError("Please select territory.");
        } else if (spinnerPatchReport.getSelectedItemPosition() == 0) {
            inputLayoutPatchReport.setError("Please select patch.");
        } else if (spinnerDoctorReport.isShown() && spinnerDoctorReport.getSelectedItemPosition() == 0) {
            inputLayoutDoctorReport.setError("Please select doctor.");
        } else if (spinnerChemistReport.isShown() && spinnerChemistReport.getSelectedItemPosition() == 0) {
            inputLayoutChemistReport.setError("Please select chemist");
        } else if (spinnerUserReport.getSelectedItemPosition() == 0) {
            inputLayoutUserReport.setError("Please select chemist");
        } else if (TextUtils.isEmpty(strStartDate)) {
            editStartTime.setError(getString(R.string.invalid_input));
            editStartTime.requestFocus();

        } else if (TextUtils.isEmpty(strEndDate)) {
            editEndTime.setError(getString(R.string.invalid_input));
            editEndTime.requestFocus();

        } else {


            if (InternetConnection.isNetworkAvailable(ActivityAdminReports.this)) {
                obtainReport.setStartDate(strStartDate);
                obtainReport.setEndDate(strEndDate);
                if (selectedRole.equals("1")) {
                    if (doctorsList != null) {
                        doctorId = doctorsList.get(spinnerDoctorReport.getSelectedItemPosition() - DEFAULT_SELECT_VALUE).getDoctorId() + "";
                        obtainReport.setDoctorId(doctorId);
                        getDoctorsReport(token, obtainReport);
                    }
                }

                if (selectedRole.equals("2")) {
                    chemistId = chemistsList.get(spinnerChemistReport.getSelectedItemPosition() - DEFAULT_SELECT_VALUE).getChemistId() + "";
                    obtainReport.setChemistId(chemistId);
                    getChemistReportList(token, obtainReport);
                }

                if (selectedRole.equals("3")) {
                    userId = usersList.get(spinnerUserReport.getSelectedItemPosition() - DEFAULT_SELECT_VALUE).getUserId();
                    obtainReport.setUserId(userId);
                    getUserReport(token, obtainReport);
                }


                if (userId != null) {


                }
                if (doctorId != null) {


                }
                if (chemistId != null) {

                }
            }
        }
    }


    public void pickDateFromDialog(EditText editText) {
        Calendar mCurrentTime = Calendar.getInstance();
        int mYear = mCurrentTime.get(Calendar.YEAR);
        int mMonth = mCurrentTime.get(Calendar.MONTH);
        int mDay = mCurrentTime.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityAdminReports.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                strPickedDate = year + "-" + (month + 1) + "-" + dayOfMonth;

                editText.setText(strPickedDate);

            }
        }, mYear, mMonth, mDay);

        //code for select on current date and onwards.
        // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//        Toast.makeText(ActivityAdminReports.this, System.currentTimeMillis()+"", Toast.LENGTH_LONG).show();
        datePickerDialog.show();

    }

    public void initRoles() {
        listOfRoles = new ArrayList<>();
        listOfRoles.add("Doctor");
        listOfRoles.add("Chemist");
        listOfRoles.add("User/MR");
        loadSpinner(spinnerRoleType, listOfRoles);
    }

    public void loadSpinner(Spinner spinner, List<String> listRole) {

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item,
                        listRole);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
    }


    public void setAdapter(RecyclerView recyclerView, ReportListAdapter adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public File makeFolder() {
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
                Toast.makeText(mContext, pos + "'th item deleted  SuccessFully !!", Toast.LENGTH_SHORT).show();
            }
        });
        setAdapter(recyclerView, listAdapter);
    }

    public void getUsersList(String token) {
        processDialog.showDialog(ActivityAdminReports.this, false);
        Call<UserListResponse> call = apiInterface.usersList(token);
        call.enqueue(new Callback<UserListResponse>() {
            @Override
            public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {
                processDialog.dismissDialog();
                if (response.body().getStatusCode() == AppConstants.RESULT_OK) {
                    usersList = response.body().getData();
                    stringUserList = new ArrayList<>();
                    stringUserList.add(getString(R.string.select));
                    for (int i = 0; i < usersList.size(); i++) {
                        String userName = usersList.get(i).getFirstName();
//                        if (usersList.get(i).getMiddleName() != null) {
//                            userName += " " + usersList.get(i).getMiddleName();
//                        }
//                        if (usersList.get(i).getLastName() != null) {
//                            userName += " " + usersList.get(i).getLastName();
//                        }
                        stringUserList.add(userName);
                    }
                    loadSpinner(spinnerUserReport, stringUserList);
                } else {
                    Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<UserListResponse> call, Throwable t) {
                processDialog.dismissDialog();
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
                        territoriesList = response.body().getTerritorysList();
                        stringTerritoryList = new ArrayList<>();
                        stringTerritoryList.add(getString(R.string.select));
                        for (int i = 0; i < territoriesList.size(); i++) {
                            stringTerritoryList.add(territoriesList.get(i).getTerritoryName());
                        }
                        loadSpinner(spinnerTerritoryReport, stringTerritoryList);


                    } else {
                        Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<TerritoryListResponse> call, Throwable t) {
                processDialog.dismissDialog();
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
                        loadSpinner(spinnerDoctorReport, stringDoctorsList);
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

    public void getChemistReportList(String token, ObtainReport report) {
        Log.e("Obtain Report", new Gson().toJson(report));
        processDialog.showDialog(ActivityAdminReports.this, false);


        Call<ReportListResponce> call = apiInterface.reportListChemist(token, report);
        if (call != null) {
            call.enqueue(new Callback<ReportListResponce>() {
                @Override
                public void onResponse(Call<ReportListResponce> call, Response<ReportListResponce> response) {
                    processDialog.dismissDialog();

                    if (response.code() == 400) {
                        processDialog.dismissDialog();
                        Toast.makeText(ActivityAdminReports.this, "Error Code", Toast.LENGTH_LONG).show();
                    }
                    if (response.body().getStatuscode() == AppConstants.RESULT_OK) {
                        Log.e("Data of Items", new Gson().toJson(response.body()));
                        dataItemsList = response.body().getData();
                        if (dataItemsList != null) {
                            chemistDataTable(dataItemsList);
//                            initAdapter(dataItemsList, reportsListView);
                            destroyTable();
                        }


                    } else {
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


    public void getDoctorsReport(String token, ObtainReport report) {
        Log.e("Obtain Report Doctor", new Gson().toJson(report));
        processDialog.showDialog(ActivityAdminReports.this, false);


        Call<ReportListResponce> call = apiInterface.reportListDoctor(token, report);
        if (call != null) {
            call.enqueue(new Callback<ReportListResponce>() {
                @Override
                public void onResponse(Call<ReportListResponce> call, Response<ReportListResponce> response) {
                    processDialog.dismissDialog();

                    if (response.code() == 400) {
                        Toast.makeText(ActivityAdminReports.this, "Error Code", Toast.LENGTH_LONG).show();
                    }
                    if (response.body().getStatuscode() == AppConstants.RESULT_OK) {
                        Log.e("Data of Doctor Report", new Gson().toJson(response.body()));
                        dataItemsList = response.body().getData();

                        if (dataItemsList != null) {
                            doctorDataTable(dataItemsList);
                            initAdapter(dataItemsList, reportsListView);
                            destroyTable();
                        }


                    } else {
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


    public void getUserReport(String token, ObtainReport report) {
        Log.e("Obtain Report User", new Gson().toJson(report));
        processDialog.showDialog(ActivityAdminReports.this, false);


        Call<ReportListResponce> call = apiInterface.reportListUser(token, report);
        if (call != null) {
            call.enqueue(new Callback<ReportListResponce>() {
                @Override
                public void onResponse(Call<ReportListResponce> call, Response<ReportListResponce> response) {
                    processDialog.dismissDialog();

                    if (response.code() == 400) {
                        Toast.makeText(ActivityAdminReports.this, "Error Code", Toast.LENGTH_LONG).show();
                    }
                    if (response.body().getStatuscode() == AppConstants.RESULT_OK) {
                        Log.e("Data of Items", new Gson().toJson(response.body()));
                        dataItemsList = response.body().getData();

                        if (dataItemsList != null) {
                            userDataTable(dataItemsList);
                            initAdapter(dataItemsList, reportsListView);
                            destroyTable();
                        }


                    } else {
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


    public void chemistDataTable(List<DataItem> dataItemsList) {
        TextView label_number = new TextView(this);
        label_number.setText("S. No.");
        label_number.setId(COLUMN_ID);
        label_number.setTextColor(Color.BLACK);
        label_number.setPadding(8, 8, 8, 8);
        tr_head.addView(label_number);

        TextView label_user = new TextView(this);
        label_user.setText("Mr/User");
        label_user.setId(COLUMN_ID + 1);
        label_user.setTextColor(Color.BLACK);
        label_user.setPadding(8, 8, 8, 8);
        tr_head.addView(label_user);

        TextView lable_start_date = new TextView(this);
        lable_start_date.setText("Start Date");
        lable_start_date.setId(COLUMN_ID + 2);
        lable_start_date.setGravity(View.TEXT_ALIGNMENT_CENTER);
        lable_start_date.setTextColor(Color.BLACK);
        lable_start_date.setPadding(8, 8, 8, 8);
        tr_head.addView(lable_start_date);

        TextView lable_end_date = new TextView(this);
        lable_end_date.setText("End Date");
        lable_end_date.setId(COLUMN_ID + 3);
        lable_end_date.setTextColor(Color.BLACK);
        lable_end_date.setPadding(8, 8, 8, 8);
        tr_head.addView(lable_end_date);

        TextView lable_pob = new TextView(this);
        lable_pob.setText("POB");
        lable_pob.setId(COLUMN_ID + 4);
        lable_pob.setTextColor(Color.BLACK);
        lable_pob.setPadding(8, 8, 8, 8);
        tr_head.addView(lable_pob);


        mainTableLayout.addView(tr_head, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,                    //part4
                TableLayout.LayoutParams.MATCH_PARENT));


        TableRow[] tr_headObj = new TableRow[dataItemsList.size()];
        TextView[] textArray = new TextView[5];

        for (int i = 0; i < dataItemsList.size(); i++) {
            tr_headObj[i] = new TableRow(this);
            tr_headObj[i].setId(i + 1);
            if (i % 2 == 0) {
                tr_headObj[i].setBackgroundColor(getResources().getColor(R.color.lightGrayColor));
            } else {
                tr_headObj[i].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
            tr_headObj[i].setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            textArray[0] = new TextView(this);
            textArray[0].setId(i + 111);
            textArray[0].setText(i + 1 + "");
            textArray[0].setTextColor(Color.WHITE);
            textArray[0].setPadding(8, 8, 8, 8);
            tr_headObj[i].addView(textArray[0]);

            textArray[1] = new TextView(this);
            textArray[1].setId(i + 111);
            textArray[1].setText(dataItemsList.get(i).getUserName());
            textArray[1].setTextColor(Color.WHITE);
            textArray[1].setPadding(8, 8, 8, 8);
            tr_headObj[i].addView(textArray[1]);

            textArray[2] = new TextView(this);
            textArray[2].setId(i + 111);
            textArray[2].setText(dataItemsList.get(i).getStartDate());
            textArray[2].setTextColor(Color.WHITE);
            textArray[2].setPadding(8, 8, 8, 8);
            tr_headObj[i].addView(textArray[2]);

            textArray[3] = new TextView(this);
            textArray[3].setId(i + 111);
            textArray[3].setText(dataItemsList.get(i).getEndDate());
            textArray[3].setTextColor(Color.WHITE);
            textArray[3].setPadding(8, 8, 8, 8);
            tr_headObj[i].addView(textArray[3]);

            textArray[4] = new TextView(this);
            textArray[4].setId(i + 111);
            String productDetail = "";
            for (int productIterettor = 0; productIterettor < dataItemsList.get(i).getProductDetails().size(); productIterettor++) {
                productDetail += dataItemsList.get(i).getProductDetails().get(productIterettor).getProductName() + "(" + dataItemsList.get(i).getProductDetails().get(productIterettor).getQuantity() + ")";
            }
            textArray[4].setText(productDetail);
            textArray[4].setTextColor(Color.WHITE);
            textArray[4].setPadding(8, 8, 8, 8);
            tr_headObj[i].addView(textArray[4]);

            mainTableLayout.addView(tr_headObj[i], new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

        }


    }


    public void doctorDataTable(List<DataItem> dataItemsList) {
        TextView label_number = new TextView(this);
        label_number.setText("S. No.");
        label_number.setId(COLUMN_ID + 5);
        label_number.setTextColor(Color.BLACK);
        label_number.setPadding(8, 8, 8, 8);
        tr_head.addView(label_number);

        TextView lable_user = new TextView(this);
        lable_user.setText("Mr/User");
        lable_user.setId(COLUMN_ID + 6);
        lable_user.setTextColor(Color.BLACK);
        lable_user.setPadding(8, 8, 8, 8);
        tr_head.addView(lable_user);

        TextView lable_start_date = new TextView(this);
        lable_start_date.setText("Start Date");
        lable_start_date.setId(COLUMN_ID + 7);
        lable_start_date.setGravity(View.TEXT_ALIGNMENT_CENTER);
        lable_start_date.setTextColor(Color.BLACK);
        lable_start_date.setPadding(8, 8, 8, 8);
        tr_head.addView(lable_start_date);

        TextView lable_end_date = new TextView(this);
        lable_end_date.setText("End Date");
        lable_end_date.setId(COLUMN_ID + 8);
        lable_end_date.setTextColor(Color.BLACK);
        lable_end_date.setPadding(8, 8, 8, 8);
        tr_head.addView(lable_end_date);

        TextView lebel_sample = new TextView(this);
        lebel_sample.setText("Sample Product");
        lebel_sample.setId(COLUMN_ID + 9);
        lebel_sample.setTextColor(Color.BLACK);
        lebel_sample.setPadding(8, 8, 8, 8);
        tr_head.addView(lebel_sample);

        TextView lebel_interest = new TextView(this);
        lebel_interest.setText("Brand Interest");
        lebel_interest.setId(COLUMN_ID + 10);
        lebel_interest.setTextColor(Color.BLACK);
        lebel_interest.setPadding(8, 8, 8, 8);
        tr_head.addView(lebel_interest);

        mainTableLayout.addView(tr_head, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,                    //part4
                TableLayout.LayoutParams.MATCH_PARENT));


        TableRow[] tr_headObj = new TableRow[dataItemsList.size()];
        TextView[] textArray = new TextView[6];

        for (int i = 0; i < dataItemsList.size(); i++) {
            tr_headObj[i] = new TableRow(this);
            tr_headObj[i].setId(i + 1);
            if (i % 2 == 0) {
                tr_headObj[i].setBackgroundColor(getResources().getColor(R.color.lightGrayColor));
            } else {
                tr_headObj[i].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
            tr_headObj[i].setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            textArray[0] = new TextView(this);
            textArray[0].setId(i + 111);
            textArray[0].setText(i + 1 + "");
            textArray[0].setTextColor(Color.WHITE);
            textArray[0].setPadding(8, 8, 8, 8);
            tr_headObj[i].addView(textArray[0]);

            textArray[1] = new TextView(this);
            textArray[1].setId(i + 111);
            textArray[1].setText(dataItemsList.get(i).getDoctorName());
            textArray[1].setTextColor(Color.WHITE);
            textArray[1].setPadding(8, 8, 8, 8);
            tr_headObj[i].addView(textArray[1]);

            textArray[2] = new TextView(this);
            textArray[2].setId(i + 111);
            textArray[2].setText(dataItemsList.get(i).getStartTime());
            textArray[2].setTextColor(Color.WHITE);
            textArray[2].setPadding(8, 8, 8, 8);
            tr_headObj[i].addView(textArray[2]);

            textArray[3] = new TextView(this);
            textArray[3].setId(i + 111);
            textArray[3].setText(dataItemsList.get(i).getEndTime());
            textArray[3].setTextColor(Color.WHITE);
            textArray[3].setPadding(8, 8, 8, 8);
            tr_headObj[i].addView(textArray[3]);

            textArray[4] = new TextView(this);
            textArray[4].setId(i + 111);
            String productDetail = "";
            if ( dataItemsList.get(i).getProductDetails() != null) {
                for (int productIterettor = 0; productIterettor < dataItemsList.get(i).getProductDetails().size(); productIterettor++) {
                    productDetail += dataItemsList.get(i).getProductDetails().get(productIterettor).getProductName() + "(" + dataItemsList.get(i).getProductDetails().get(productIterettor).getQuantity() + ")";
                }
                textArray[4].setText(productDetail);
            }
            textArray[4].setTextColor(Color.WHITE);
            textArray[4].setPadding(8, 8, 8, 8);
            tr_headObj[i].addView(textArray[4]);


            textArray[5] = new TextView(this);
            textArray[5].setId(i + 111);
            if (dataItemsList.get(i).getInterestedLevel() != null) {
                for (int productIterettor = 0; productIterettor < dataItemsList.get(i).getProductDetails().size(); productIterettor++) {
                    if (dataItemsList.get(i).getProductDetails().get(productIterettor).equals("3")) {
                        textArray[5].setText(dataItemsList.get(i).getProductName() + "(Low)");
                    }
                    if (dataItemsList.get(i).getProductDetails().get(productIterettor).equals("2")) {
                        textArray[5].setText(dataItemsList.get(i).getProductName() + "(Regular)");

                    }
                    if (dataItemsList.get(i).getProductDetails().get(productIterettor).equals("1")) {
                        textArray[5].setText(dataItemsList.get(i).getProductName() + "(Super)");

                    }
                }
            } else {
                textArray[5].setText("---");
            }
            textArray[5].setTextColor(Color.WHITE);
            textArray[5].setPadding(8, 8, 8, 8);
            tr_headObj[i].addView(textArray[5]);

            mainTableLayout.addView(tr_headObj[i], new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

        }


    }

    public void userDataTable(List<DataItem> dataItemsList) {
        Log.e("User input report", new Gson().toJson(dataItemsList));
        TextView label_number = new TextView(this);
        label_number.setText("S. No.");
        label_number.setId(COLUMN_ID + 11);
        label_number.setTextColor(Color.BLACK);
        label_number.setPadding(8, 8, 8, 8);
        tr_head.addView(label_number);

        TextView label_chemist = new TextView(this);
        label_chemist.setText("Chemist");
        label_chemist.setId(COLUMN_ID + 12);
        label_chemist.setTextColor(Color.BLACK);
        label_chemist.setPadding(8, 8, 8, 8);
        tr_head.addView(label_chemist);

        TextView label_doctor = new TextView(this);
        label_doctor.setText("Doctor");
        label_doctor.setId(COLUMN_ID + 13);
        label_doctor.setGravity(View.TEXT_ALIGNMENT_CENTER);
        label_doctor.setTextColor(Color.BLACK);
        label_doctor.setPadding(8, 8, 8, 8);
        tr_head.addView(label_doctor);

        TextView label_start_date = new TextView(this);
        label_start_date.setText("Start Date");
        label_start_date.setId(COLUMN_ID + 14);
        label_start_date.setTextColor(Color.BLACK);
        label_start_date.setPadding(8, 8, 8, 8);
        tr_head.addView(label_start_date);

        TextView label_end_date = new TextView(this);
        label_end_date.setText("End Date");
        label_end_date.setId(COLUMN_ID + 15);
        label_end_date.setTextColor(Color.BLACK);
        label_end_date.setPadding(8, 8, 8, 8);
        tr_head.addView(label_end_date);

        TextView label_location = new TextView(this);
        label_location.setText("Is in Location");
        label_location.setId(COLUMN_ID + 16);
        label_location.setTextColor(Color.BLACK);
        label_location.setPadding(8, 8, 8, 8);
        tr_head.addView(label_location);

        TextView label_sample = new TextView(this);
        label_sample.setText("Sample");
        label_sample.setId(COLUMN_ID + 17);
        label_sample.setTextColor(Color.BLACK);
        label_sample.setPadding(8, 8, 8, 8);
        tr_head.addView(label_sample);

        TextView label_brand_interest = new TextView(this);
        label_brand_interest.setText("Brand Interest");
        label_brand_interest.setId(COLUMN_ID + 18);
        label_brand_interest.setTextColor(Color.BLACK);
        label_brand_interest.setPadding(8, 8, 8, 8);
        tr_head.addView(label_brand_interest);

        TextView label_gift = new TextView(this);
        label_gift.setText("Gift");
        label_gift.setId(COLUMN_ID + 19);
        label_gift.setTextColor(Color.BLACK);
        label_gift.setPadding(8, 8, 8, 8);
        tr_head.addView(label_gift);

        TextView label_POB = new TextView(this);
        label_POB.setText("POB");
        label_POB.setId(COLUMN_ID + 20);
        label_POB.setTextColor(Color.BLACK);
        label_POB.setPadding(8, 8, 8, 8);
        tr_head.addView(label_POB);

        mainTableLayout.addView(tr_head, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,                    //part4
                TableLayout.LayoutParams.MATCH_PARENT));


        TableRow[] tr_headObj = new TableRow[dataItemsList.size()];
        TextView[] textArray = new TextView[10];

        for (int i = 0; i < dataItemsList.size(); i++) {
            tr_headObj[i] = new TableRow(this);
            tr_headObj[i].setId(i + 1);
            if (i % 2 == 0) {
                tr_headObj[i].setBackgroundColor(getResources().getColor(R.color.lightGrayColor));
            } else {
                tr_headObj[i].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
            tr_headObj[i].setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            textArray[0] = new TextView(this);
            textArray[0].setId(i + 111);
            textArray[0].setText(i + 1 + "");
            textArray[0].setTextColor(Color.WHITE);
            textArray[0].setPadding(8, 8, 8, 8);
            tr_headObj[i].addView(textArray[0]);

            // Chemist
            textArray[1] = new TextView(this);
            textArray[1].setId(i + 111);
            textArray[1].setText(dataItemsList.get(i).getChemistName());
            textArray[1].setTextColor(Color.WHITE);
            textArray[1].setPadding(8, 8, 8, 8);
            tr_headObj[i].addView(textArray[1]);

            // Doctor
            textArray[2] = new TextView(this);
            textArray[2].setId(i + 111);
            textArray[2].setText(dataItemsList.get(i).getDoctorName());
            textArray[2].setTextColor(Color.WHITE);
            textArray[2].setPadding(8, 8, 8, 8);
            tr_headObj[i].addView(textArray[2]);

            // StartDate
            textArray[3] = new TextView(this);
            textArray[3].setId(i + 111);
            textArray[3].setText(dataItemsList.get(i).getStartTime());
            textArray[3].setTextColor(Color.WHITE);
            textArray[3].setPadding(8, 8, 8, 8);
            tr_headObj[i].addView(textArray[3]);

            textArray[4] = new TextView(this);
            textArray[4].setId(i + 111);
            textArray[4].setText(dataItemsList.get(i).getEndTime());
            textArray[4].setTextColor(Color.WHITE);
            textArray[4].setPadding(8, 8, 8, 8);
            tr_headObj[i].addView(textArray[4]);

            textArray[5] = new TextView(this);
            textArray[5].setId(i + 111);
            if (dataItemsList.get(i).getIsInLocation().equals("1")) {
                textArray[5].setText("Yes");
            } else {
                textArray[5].setText("No");
            }
            textArray[5].setTextColor(Color.WHITE);
            textArray[5].setPadding(8, 8, 8, 8);
            tr_headObj[i].addView(textArray[5]);

            textArray[6] = new TextView(this);
            textArray[6].setId(i + 111);
//            if (dataItemsList.get(i).getIsBrand().equals("1")) {
            String productDetail = "";
            if (dataItemsList.get(i).getProductDetails() != null) {
                for (int productIterettor = 0; productIterettor < dataItemsList.get(i).getProductDetails().size(); productIterettor++) {
                    productDetail += dataItemsList.get(i).getProductDetails().get(productIterettor).getProductName() + " (" + dataItemsList.get(i).getProductDetails().get(productIterettor).getQuantity() + ") , \n";
                }
            }
            textArray[6].setText(productDetail);
//            } else {
//                textArray[6].setText("---");
//            }
            textArray[6].setTextColor(Color.WHITE);
            textArray[6].setPadding(8, 8, 8, 8);
            tr_headObj[i].addView(textArray[6]);


            textArray[7] = new TextView(this);
            textArray[7].setId(i + 111);
            if (dataItemsList.get(i).getIsBrand().equals("1")) {
                textArray[7].setText(dataItemsList.get(i).getProductName()
                        + "(" + dataItemsList.get(i).getInterestedLevel() + ")");
            } else {
                textArray[7].setText("---");
            }
            textArray[7].setTextColor(Color.WHITE);
            textArray[7].setPadding(8, 8, 8, 8);
            tr_headObj[i].addView(textArray[7]);


            textArray[8] = new TextView(this);
            textArray[8].setId(i + 111);
            String giftDetalis = "";
            if (dataItemsList.get(i).getGiftDetails() != null) {
                for (int giftIterettor = 0; giftIterettor < dataItemsList.get(i).getGiftDetails().size(); giftIterettor++) {
                    giftDetalis += dataItemsList.get(i).getGiftDetails().get(giftIterettor).getGiftName() + " (" + dataItemsList.get(i).getGiftDetails().get(giftIterettor).getQuantity() + ") , \n";
                }
                textArray[8].setText(giftDetalis);
            } else {
                textArray[8].setText("---");
            }
            textArray[8].setTextColor(Color.WHITE);
            textArray[8].setPadding(8, 8, 8, 8);
            tr_headObj[i].addView(textArray[8]);

            textArray[9] = new TextView(this);
            textArray[9].setId(i + 111);
            if (dataItemsList.get(i).getIsBrand().equals("0")) {
                textArray[9].setText(dataItemsList.get(i).getProductName()
                        + "(" + dataItemsList.get(i).getProductQty() + ")");
            } else {
                textArray[9].setText("---");
            }
            textArray[9].setTextColor(Color.WHITE);
            textArray[9].setPadding(8, 8, 8, 8);
            tr_headObj[i].addView(textArray[9]);

            mainTableLayout.addView(tr_headObj[i], new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

        }


    }

    public void destroyTable() {
        mainTableLayout.removeAllViews();
        tr_head = null;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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

                if (dataItemsList != null) {

                    FileCreation fileCreation = new FileCreation();
                    File toCreate = makeFolder();
                    fileCreation.exportReport(dataItemsList, toCreate, ActivityAdminReports.this, Integer.parseInt(selectedRole));
                    destroyTable();
                } else {
                    Snackbar.make(rootView, "No data available to export", Snackbar.LENGTH_SHORT).show();
                }
                break;
        }

    }
}
