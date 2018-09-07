package com.planera.mis.planera2.activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.utils.AppConstants;
import com.planera.mis.planera2.activities.utils.InternetConnection;

import java.util.Calendar;

public class ScheduleTimeActivity extends BaseActivity implements View.OnClickListener {
    private AppBarLayout appBar;
    private Toolbar toolbarTime;
    private TextView textCustomerName;
    private TextView textVisitDate;
    private EditText editStartTime;
    private EditText editEndTime;
    private EditText editFeedback;
    private Button buttonSubmitInput;
    private String startTimeStr, endTimeStr, feedbackStr;
    private boolean isDoctor;
    private double longitude;
    private double latitude;
    private int customeId;
    private int isInLocation;
    private String startTime;
    private String endTime;
    private String selectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_time);
        initUi();
        initUi();
    }

    @Override
    public void initUi() {
        super.initUi();
        Intent intent = getIntent();
        appBar = findViewById(R.id.appBar);
        toolbarTime = findViewById(R.id.toolbarTime);
        textCustomerName = findViewById(R.id.text_customer_name);
        textVisitDate = findViewById(R.id.text_visit_date);
        editStartTime = findViewById(R.id.edit_start_time);
        editEndTime = findViewById(R.id.edit_end_time);
        editFeedback = findViewById(R.id.edit_feedback);
        buttonSubmitInput = findViewById(R.id.button_submit_input);


        setSupportActionBar(toolbarTime);
        getSupportActionBar().setTitle("Schedule");
        toolbarTime.setNavigationOnClickListener(view -> onBackPressed());
        loadFormIntent(intent);

        buttonSubmitInput.setOnClickListener(this);
        editStartTime.setOnClickListener(this);
        editEndTime.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();

    }


    public void uiValidation(){
        startTimeStr = editStartTime.getText().toString().trim();
        endTimeStr = editEndTime.getText().toString().trim();
        feedbackStr = editFeedback.getText().toString().trim();

        if(TextUtils.isEmpty(feedbackStr)){
            editFeedback.requestFocus();
            editFeedback.setError(getString(R.string.invalid_input));
        }
        else if(TextUtils.isEmpty(endTimeStr)){
            editEndTime.requestFocus();
            editEndTime.setError(getString(R.string.invalid_input));
        }
        else if(TextUtils.isEmpty(startTimeStr)){
            editStartTime.requestFocus();
            editStartTime.setError(getString(R.string.invalid_input));
        }
        else{
            if (InternetConnection.isNetworkAvailable(ScheduleTimeActivity.this)){

            }
            else{
                Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
            }
        }

    }


    public void loadFormIntent(Intent intent){
        textCustomerName.setText(intent.getStringExtra(AppConstants.CUSTOMER_NAME));
        textVisitDate.setText(intent.getStringExtra(AppConstants.VISIT_DATE));
        isDoctor = intent.getBooleanExtra(AppConstants.KEY_ROLE, true);
        longitude = intent.getDoubleExtra(AppConstants.LATITUDE, 0.0);
        latitude  = intent.getDoubleExtra(AppConstants.LATITUDE, 0.0);
        isInLocation = intent.getIntExtra(AppConstants.KEY_IN_LOCATION, -1);
        customeId = intent.getIntExtra(AppConstants.DOCTOR_ID, 0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_submit_input:
                Intent intentProduct = new Intent(ScheduleTimeActivity.this, ProductCategoryActivity.class);
                startActivity(intentProduct);
                break;

            case R.id.edit_start_time:
                startTime = getDateFromDialog();
                editStartTime.setText(startTime);
                break;

            case R.id.edit_end_time:
                endTime= getDateFromDialog();
                editEndTime.setText(endTime);
                break;
        }

    }



    public String getDateFromDialog(){
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(ScheduleTimeActivity.this,
                (view, hourOfDay, minute1) -> {

                    if (hourOfDay>12){
                       int temHourOfDay = hourOfDay-12;
                        selectedTime= temHourOfDay + ":" + minute1 + "PM";
                        Log.d(selectedTime, "time");
                    }
                    else
                    {
                        selectedTime = hourOfDay + ":" + minute1 + " AM";
                    }
                }, hour, minute, false);
        timePickerDialog.show();
        return selectedTime;
    }
}
