package com.planera.mis.planera2.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.planera.mis.planera2.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ActivityAdminReports extends BaseActivity implements View.OnClickListener{
    private RelativeLayout parentPanel;
    private AppBarLayout appBar;
    private Toolbar toolbarReport;
    private Spinner spinnerRoleType;
    private EditText editStartTime;
    private EditText editEndTime;
    private Button buttonSubmitReport;
    private String strPickedDate;
    private List<String> listOfRoles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_reports);
    }

    @Override
    public void initUi() {
        super.initUi();



        parentPanel = findViewById(R.id.parentPanel);
        appBar = findViewById(R.id.appBar);
        toolbarReport = findViewById(R.id.toolbarReport);
        spinnerRoleType = findViewById(R.id.spinner_role_type);
        editStartTime = findViewById(R.id.edit_start_time);
        editEndTime = findViewById(R.id.edit_end_time);
        buttonSubmitReport = findViewById(R.id.button_submit_report);

        setSupportActionBar(toolbarReport);
        getSupportActionBar().setTitle("Reports");
    }

    @Override
    public void initData() {
        super.initData();
        initRoles();
    }



    public void pickDateFromDialog(){
        Calendar mCurrentTime = Calendar.getInstance();
        int mYear = mCurrentTime.get(Calendar.YEAR);
        int mMonth = mCurrentTime.get(Calendar.MONTH);
        int mDay = mCurrentTime.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityAdminReports.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                strPickedDate = year+"-"+month+"-"+dayOfMonth;

            }
        },mYear, mMonth, mDay );
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_submit_report:
                break;
            case R.id.edit_start_time:
                break;
            case R.id.edit_end_time:
                break;
        }

    }
}
