package com.planera.mis.planera2.activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.models.Input;
import com.planera.mis.planera2.activities.models.MainResponse;
import com.planera.mis.planera2.activities.utils.AppConstants;
import com.planera.mis.planera2.activities.utils.InternetConnection;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private int isInLocation;
    private int planId;
    private int userId;
    private String startTime;
    private String endTime;
    private String selectedTime;
    private Input input;
    private int chemistId;
    private int docterId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_time);

        initUi();
        initData();
    }

    @Override
    public void initUi() {
        super.initUi();

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


        buttonSubmitInput.setOnClickListener(this);
        editStartTime.setOnClickListener(this);
        editEndTime.setOnClickListener(this);


    }

    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        input = new Input();
        loadFormIntent(intent);
        getCurrnetDate();

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
               input.setStartDate(startTimeStr);
               input.setEndDate(endTimeStr);
               input.setComment(feedbackStr);
                addInputApi(token, input);
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
        docterId = intent.getIntExtra(AppConstants.DOCTOR_ID, -10);
        chemistId = intent.getIntExtra(AppConstants.CHEMIST_ID , -10);
        userId = intent.getIntExtra(AppConstants.KEY_USER_ID, -10);
        planId = intent.getIntExtra(AppConstants.KEY_PLAN_ID, -10);

        input.setLatitude(Double.toString(latitude));
        input.setLongitude(longitude+"");
        input.setIsInLocation(isInLocation+"");
        if(isDoctor){
            input.setDoctorId(docterId+"");
        }
        else{
            input.setChemistsId(chemistId+"");
        }
        input.setPlanId(planId+"");
        input.setUserId(userId+"");


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.button_submit_input:
                startTimeStr = editStartTime.getText().toString().trim();
                endTimeStr = editEndTime.getText().toString().trim();
                String stTime = "20101212" + startTimeStr.replace(":", "");
                String edTime = "20101212" + endTimeStr.replace(":", "");
                Date inTime = new Date(Long.parseLong(stTime));
                Date outTime = new Date(Long.parseLong(edTime));
                if(outTime.before(inTime))
                {
                    Toast.makeText(ScheduleTimeActivity.this, "End time should be greater than start time", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Intent intentProduct = new Intent(ScheduleTimeActivity.this, ProductCategoryActivity.class);
                    startActivity(intentProduct);
                }
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

    public void addInputApi(String token, Input input){
        processDialog.showDialog(ScheduleTimeActivity.this, false);
        Call<MainResponse> call  = apiInterface.addInput(token, input);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
               processDialog.dismissDialog();
                if (response.isSuccessful()){

                    if (response.code() == 200){
                        if(response.body().getStatusCode() == AppConstants.RESULT_OK){
                            Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_INDEFINITE).show();
                        }
                        else{
                            Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_INDEFINITE).show();

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                processDialog.dismissDialog();
                Snackbar.make(rootView, t.getMessage(), Snackbar.LENGTH_INDEFINITE).show();

            }
        });
    }

    public void getCurrnetDate(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        textVisitDate.setText(df.format(c));
    }

    boolean isTimeAfter(Date startTime, Date endTime) {
        if (endTime.before(startTime)) { //Same way you can check with after() method also.
            return false;
        } else {
            return true;
        }
    }

    public String getDateFromDialog(){
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(ScheduleTimeActivity.this,
                (view, hourOfDay, minute1) -> {
                    selectedTime = hourOfDay+":"+minute1;
                }, hour, minute, false);
        timePickerDialog.show();
        return selectedTime;
    }
}
