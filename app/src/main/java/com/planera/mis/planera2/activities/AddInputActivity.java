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

import com.google.gson.Gson;
import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.models.Input;
import com.planera.mis.planera2.activities.utils.AppConstants;
import com.planera.mis.planera2.activities.utils.InternetConnection;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddInputActivity extends BaseActivity implements View.OnClickListener {
    private AppBarLayout appBar;
    private Toolbar toolbarTime;
    private TextView textCustomerName;
    private TextView textVisitDate;
    private EditText editStartTime;
    private EditText editEndTime;
    private EditText editFeedback;
    private Button buttonSubmitInput;
    private String startTimeStr, endTimeStr, feedbackStr;
    public static final String TAG = AddInputActivity.class.getSimpleName();
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
    private String chemistId;
    private int doctorId;
    private String currentdate;

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
        editStartTime.setFocusable(false);
        editEndTime.setFocusable(false);


        setSupportActionBar(toolbarTime);
        toolbarTime.setNavigationIcon(R.drawable.back_arrow_whit);
        getSupportActionBar().setTitle("Input");
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
        getCurrentDate();

    }


    public void uiValidation() {
        startTimeStr = editStartTime.getText().toString().trim();
        endTimeStr = editEndTime.getText().toString().trim();
        feedbackStr = editFeedback.getText().toString().trim();

        if (TextUtils.isEmpty(startTimeStr)) {
            editStartTime.requestFocus();
            editStartTime.setError(getString(R.string.invalid_input));
        } else if (TextUtils.isEmpty(endTimeStr)) {
            editEndTime.requestFocus();
            editEndTime.setError(getString(R.string.invalid_input));
        } else if (TextUtils.isEmpty(feedbackStr)) {
            editFeedback.requestFocus();
            editFeedback.setError(getString(R.string.invalid_input));
        } else {
            if (InternetConnection.isNetworkAvailable(AddInputActivity.this)) {
                String stTime = currentdate + " " + startTimeStr + ":00";
                String edTime = currentdate + " " + endTimeStr + ":00";
                input.setStartDate(stTime);
                input.setEndDate(edTime);
                input.setComment(feedbackStr);
                Gson gson = new Gson();
                String passInput = gson.toJson(input);
                connector.setString(AppConstants.PASS_INPUT, passInput);
                Intent intent = new Intent(AddInputActivity.this, ProductCategoryActivity.class);
                startActivity(intent);
//                addInputApi(token, input);
            } else {
                Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
            }
        }

    }


    public void loadFormIntent(Intent intent) {
        textCustomerName.setText(intent.getStringExtra(AppConstants.CUSTOMER_NAME));
        textVisitDate.setText(intent.getStringExtra(AppConstants.VISIT_DATE));
        isDoctor = intent.getBooleanExtra(AppConstants.KEY_ROLE, true);
        longitude = intent.getDoubleExtra(AppConstants.LATITUDE, 0.0);
        latitude = intent.getDoubleExtra(AppConstants.LATITUDE, 0.0);
        isInLocation = intent.getIntExtra(AppConstants.KEY_IN_LOCATION, 0);
        doctorId = intent.getIntExtra(AppConstants.DOCTOR_ID, 0);
        chemistId = intent.getStringExtra(AppConstants.CHEMIST_ID);
        userId = intent.getIntExtra(AppConstants.KEY_USER_ID, 0);
        planId = intent.getIntExtra(AppConstants.KEY_PLAN_ID, 0);

        input.setLatitude(Double.toString(latitude));
        input.setLongitude(longitude + "");
        input.setIsInLocation(isInLocation + "");
        if (isDoctor) {
            input.setDoctorId(doctorId + "");
        } else {
            input.setChemistsId(chemistId);
        }
        input.setPlanId(planId + "");
        input.setUserId(userId + "");


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button_submit_input:
                isTimeAfter();
                break;

            case R.id.edit_start_time:
                getDateFromDialog(editStartTime);
                break;

            case R.id.edit_end_time:
                getDateFromDialog(editEndTime);
                break;
        }

    }


    public void getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        textVisitDate.setText(df.format(c));

        SimpleDateFormat reqFor = new SimpleDateFormat("yyyy-MM-dd");
        currentdate = reqFor.format(c.getTime());
    }

    public void isTimeAfter() {
        startTimeStr = editStartTime.getText().toString().trim();
        endTimeStr = editEndTime.getText().toString().trim();
        String stTime = "20101212" + startTimeStr.replace(":", "");
        String edTime = "20101212" + endTimeStr.replace(":", "");
        Date inTime = new Date(Long.parseLong(stTime));
        Date outTime = new Date(Long.parseLong(edTime));
        if (outTime.before(inTime)) {
            Toast.makeText(AddInputActivity.this, "End time should be greater than start time", Toast.LENGTH_LONG).show();
        } else {
            uiValidation();
        }

    }

    public void getDateFromDialog(TextView textView) {
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);

        int minute = mCurrentTime.get(Calendar.MINUTE);
        int sec = mCurrentTime.get(Calendar.SECOND);
        TimePickerDialog timePickerDialog = new TimePickerDialog(AddInputActivity.this,
                (view, hourOfDay, minute1) -> {
                    selectedTime = hourOfDay + ":" + minute1;
                    textView.setText(selectedTime);
                }, hour, minute, false);
        timePickerDialog.show();
    }
}
