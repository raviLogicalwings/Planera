package com.planera.mis.planera2.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.gson.Gson;
import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.models.ObtainReport;
import com.planera.mis.planera2.activities.utils.AppConstants;

import java.util.Calendar;

public class SearchDateWiseInputActivity extends BaseActivity implements View.OnClickListener {
    Toolbar toolbar;
    private EditText editStartTime;
    private EditText editEndTime;
    private Button buttonSubmitForList;
    public ObtainReport obtainReport;
    private String strPickedDate;
    private String userId;
    String objectToString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_date_wise_input);
        initUi();
        initData();


    }

    @Override
    public void initUi() {
        super.initUi();

        toolbar = findViewById(R.id.toolbar);
        editStartTime = findViewById(R.id.edit_start_time);
        editEndTime = findViewById(R.id.edit_end_time);
        buttonSubmitForList = findViewById(R.id.button_submit_for_list);


        editEndTime.setOnClickListener(this);
        editStartTime.setOnClickListener(this);
        buttonSubmitForList.setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Set Details");

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }


    public void uiValidation(){
        String strStartDate = editStartTime.getText().toString().trim();
        String srtEndDate = editEndTime.getText().toString().trim();

        if (TextUtils.isEmpty(strStartDate)){
            editStartTime.requestFocus();
            editEndTime.setError(getString(R.string.invalid_input));
        }
        if (TextUtils.isEmpty(srtEndDate)){
            editEndTime.requestFocus();
            editEndTime.setError(getString(R.string.invalid_input));
        }

        obtainReport.setStartDate(strStartDate);
        obtainReport.setEndDate(srtEndDate);
        obtainReport.setUserId(userId);


        objectToString = new Gson().toJson(obtainReport);
        Intent intent  = new Intent(SearchDateWiseInputActivity.this, UserInputListActivity.class);
        intent.putExtra(AppConstants.OBTAIN_REPORT, objectToString);
        startActivity(intent);

    }

    @Override
    public void initData() {
        super.initData();
        obtainReport = new ObtainReport();
        userId = connector.getString(AppConstants.USER_ID);

    }

    public void pickDateFromDialog(EditText editText){
        Calendar mCurrentTime = Calendar.getInstance();
        int mYear = mCurrentTime.get(Calendar.YEAR);
        // need to add one because month's initial index is 0.
        int mMonth = mCurrentTime.get(Calendar.MONTH)+1;
        int mDay = mCurrentTime.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(SearchDateWiseInputActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                strPickedDate = year+"-"+month+"-"+dayOfMonth;
                editText.setText(strPickedDate);

            }
        },mYear, mMonth, mDay );

        //code for select on current date and onwards.
        // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//        Toast.makeText(ActivityAdminReports.this, System.currentTimeMillis()+"", Toast.LENGTH_LONG).show();
//        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_start_time:
                pickDateFromDialog(editStartTime);
                break;
            case R.id.edit_end_time:
                pickDateFromDialog(editEndTime);
                break;
            case R.id.button_submit_for_list:
                uiValidation();
                break;
        }

    }
}
