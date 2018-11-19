package com.planera.mis.planera2.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.models.DataItem;
import com.planera.mis.planera2.activities.models.Input;
import com.planera.mis.planera2.activities.utils.AppConstants;
import com.planera.mis.planera2.activities.utils.InternetConnection;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class  AddInputActivity extends BaseActivity implements View.OnClickListener {
    private AppBarLayout appBar;
    private Toolbar toolbarTime;
    private TextView textCustomerName;
    private TextView textVisitDate;
    private EditText editStartTime;
    private EditText editEndTime;
    private EditText editFeedback;
    private EditText editEarlierFeedback;
    private TextInputLayout inputLayoutVisitDate;
    private Button buttonSubmitInput;
    private LinearLayout layoutEarlierEntryFeedBack;
    private String startTimeStr, endTimeStr, feedbackStr;
    public static final String TAG = AddInputActivity.class.getSimpleName();
    private boolean isDoctor;
    private double longitude;
    private double latitude;
    private int isInLocation;
    private int planId;
    private int userId;
    private String timeWithDate;
    private String selectedTime;
    private Input input;
    private String chemistId;
    private int doctorId;
    private String currentDate;
    private String selectedDate;
    private String earlierFeedbackStr;
    private String strVisitDate;
    private boolean isUpdateInput;
    private DataItem previousInputObj;
    private String previousInputStr;

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
        inputLayoutVisitDate = findViewById(R.id.input_layout_visit_date);
        buttonSubmitInput = findViewById(R.id.button_submit_input);
        editEarlierFeedback = findViewById(R.id.edit_earlier_feedback);
        layoutEarlierEntryFeedBack = findViewById(R.id.layout_earlier_entry_feedback);
        editStartTime.setFocusable(false);
        editEndTime.setFocusable(false);
        layoutEarlierEntryFeedBack.setVisibility(View.GONE);
        editEarlierFeedback.setVisibility(View.GONE);
        setSupportActionBar(toolbarTime);
        toolbarTime.setNavigationIcon(R.drawable.back_arrow_whit);
        getSupportActionBar().setTitle("Input");
        toolbarTime.setNavigationOnClickListener(view -> onBackPressed());
        textVisitDate.setOnClickListener(this);
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
        earlierFeedbackStr = editEarlierFeedback.getText().toString().trim();
        strVisitDate = textVisitDate.getText().toString().trim();

        if (strVisitDate.equals(getString(R.string.select).replace("-", ""))  || strVisitDate.isEmpty()){
            inputLayoutVisitDate.setError("Please select date of visit.");
            textVisitDate.requestFocus();
        }
        else if (TextUtils.isEmpty(startTimeStr)) {
            editStartTime.requestFocus();
            editStartTime.setError(getString(R.string.invalid_input));
        } else if (TextUtils.isEmpty(endTimeStr)) {
            editEndTime.requestFocus();
            editEndTime.setError(getString(R.string.invalid_input));
        } else if (editEarlierFeedback.isShown()) {
                    if (TextUtils.isEmpty(earlierFeedbackStr)) {
                        editEarlierFeedback.requestFocus();
                        editEarlierFeedback.setError(getString(R.string.invalid_input));
                    }
        } else {
            if (InternetConnection.isNetworkAvailable(AddInputActivity.this)) {
                    input.setStartDate(timeWithDate);
                    input.setEndDate(timeWithDate);

                    input.setComment(feedbackStr);
                    String visitD = textVisitDate.getText().toString();
                    input.setVisitDate(formatDate(visitD));
                    input.setEarlierEntryFeedback(earlierFeedbackStr);
                    Gson gson = new Gson();
                    String passInput = gson.toJson(input);
                    Log.e("Input Params", passInput);
                    Intent intent = new Intent(AddInputActivity.this, ProductCategoryActivity.class);

                        intent.putExtra(AppConstants.PASS_INPUT, passInput);
                        if (isUpdateInput) {
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra(AppConstants.PASS_UPDATE_INPUT, previousInputStr);
                            intent.putExtra(AppConstants.IS_INPUT_UPDATE, true);
                        }
                        else{
                            intent.putExtra(AppConstants.IS_INPUT_UPDATE, false);
                        }

                    startActivity(intent);

//                addInputApi(token, input);
            } else {
                Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
            }
        }

    }


    public void loadFormIntent(Intent intent) {
        isUpdateInput = intent.getBooleanExtra(AppConstants.IS_INPUT_UPDATE, false);
        if (isUpdateInput){
            previousInputStr = intent.getStringExtra(AppConstants.PASS_UPDATE_INPUT);
            previousInputObj = new Gson().fromJson(previousInputStr, DataItem.class);
            input.setLatitude(previousInputObj.getLatitude());
            input.setLongitude(previousInputObj.getLongitude());
            input.setIsInLocation(previousInputObj.getIsInLocation());
            input.setPlanId(previousInputObj.getPlanId());
            input.setUserId(previousInputObj.getUserId());
            input.setInputId(previousInputObj.getInputId());

            //set to this class
            textVisitDate.setText(convertIntoDD_MM_YYYY(previousInputObj.getVisitDate()));
            editStartTime.setText(time24To12Hour(previousInputObj.getStartTime()));
            editEndTime.setText(time24To12Hour(previousInputObj.getEndTime()));
            editFeedback.setText(previousInputObj.getComment());

            if (previousInputObj.getChemistsId().equals("0")){
                input.setDoctorId(previousInputObj.getDoctorId()+"");
                connector.setBoolean(AppConstants.KEY_ROLE, true);
                if (previousInputObj.getDoctorName() != null) {
                    textCustomerName.setText(previousInputObj.getDoctorName());
                }
            }
            else{
                input.setChemistsId(previousInputObj.getChemistsId());
                connector.setBoolean(AppConstants.KEY_ROLE, false);
                if (previousInputObj.getChemistName() != null) {
                    textCustomerName.setText(previousInputObj.getChemistName());
                }
            }
        }
        else {
            isDoctor = intent.getBooleanExtra(AppConstants.KEY_ROLE, false);
            textCustomerName.setText(intent.getStringExtra(AppConstants.CUSTOMER_NAME));
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

            case R.id.text_visit_date:
                chooseDateFromDialog();
        }

    }

    private void chooseDateFromDialog() {
        final Calendar mCurrentDate = Calendar.getInstance();
        int mYear = mCurrentDate.get(Calendar.YEAR);
        int mMonth = mCurrentDate.get(Calendar.MONTH);
        int mDay = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(AddInputActivity.this, (view, year, month, dayOfMonth) -> {
            selectedDate = dayOfMonth + "-" + (month+1)+  "-" + year;
            textVisitDate.setText(selectedDate);

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            try {
                Date date1 = sdf.parse(currentDate);
                Date date2 = sdf.parse(selectedDate);
                Log.e("Compare ", date1.after(date2)+"");

                if (date1.compareTo(date2) != 0) {
                    layoutEarlierEntryFeedBack.setVisibility(View.VISIBLE);
                    editEarlierFeedback.setVisibility(View.VISIBLE);

                } else {

                    layoutEarlierEntryFeedBack.setVisibility(View.GONE);
                    editEarlierFeedback.setVisibility(View.GONE);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }, mYear, mMonth, mDay);
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dialog.show();
    }


    public void getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        currentDate = df.format(c.getTime());
    }

    public String formatDate(String date){

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");//yyyy-MM-dd'T'HH:mm:ss
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Date data = null;
        try {
            data = sdf.parse(date);
        } catch (ParseException e) {
            Log.e("Exp", e.getMessage());
        }
        return dateFormat.format(data);
    }

    public void isTimeAfter() {
        startTimeStr = editStartTime.getText().toString().trim().replaceAll("[^0-9]+", "");
        endTimeStr = editEndTime.getText().toString().trim().replaceAll("[^0-9]+", "");
        String stTime = "2" + startTimeStr;
        String edTime = "2" + endTimeStr;
        Date inTime = new Date(Long.parseLong(stTime));
        Date outTime = new Date(Long.parseLong(edTime));
        if (outTime.before(inTime)) {
            Toast.makeText(AddInputActivity.this, "End time should be greater than start time", Toast.LENGTH_LONG).show();
        } else {
            uiValidation();
        }

    }

    public void getDoctorsReport(String token){

    }

    public String convertIntoDD_MM_YYYY(String date){
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dt = "";
        try {
          dt = dateOnlyFormat.format(dateTimeFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dt;
    }

    public void getDateFromDialog(TextView textView) {
        Calendar mCurrentTime = Calendar.getInstance();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate  = df.format(c.getTime());
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        int sec = mCurrentTime.get(Calendar.SECOND);
        TimePickerDialog timePickerDialog = new TimePickerDialog(AddInputActivity.this,
                (view, hourOfDay, minute1) -> {
                    selectedTime = hourOfDay + ":" + minute1;
                    timeWithDate = todayDate+" "+selectedTime+":00";

                    textView.setText(time24To12Hour(selectedTime));
                }, hour, minute, false);
        timePickerDialog.show();
    }




    public String time24To12Hour(String timeForConvert){
        SimpleDateFormat _24HourTime = new SimpleDateFormat("HH:mm");
        SimpleDateFormat _12HourTime = new SimpleDateFormat("hh:mm a");
        Date _24HourDate = null;
        try {
            _24HourDate = _24HourTime.parse(timeForConvert);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return _12HourTime.format(_24HourDate);
    }
}
