package com.planera.mis.planera2.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.planera.mis.planera2.models.DataItem;
import com.planera.mis.planera2.models.Input;
import com.planera.mis.planera2.models.MRs;
import com.planera.mis.planera2.utils.AppConstants;
import com.planera.mis.planera2.utils.InternetConnection;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class  AddInputActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = AddInputActivity.class.getSimpleName();
    private TextView textCustomerName;
    private TextView textVisitDate;
    private EditText editStartTime;
    private EditText editEndTime;
    private EditText editFeedback;
    private EditText editEarlierFeedback;
    private TextInputLayout inputLayoutVisitDate;
    private TextInputLayout inputLayoutReason;
    private LinearLayout layoutEarlierEntryFeedBack;
    private String startTimeStr, endTimeStr, feedbackStr;

    private ArrayList<MRs> jointUserList;

    protected double longitude;
    protected double latitude;
    protected int isInLocation;
    protected int planId;
    protected int userId;
    private String timeWithDate;
    private String selectedTime;
    private Input input;
    protected String chemistId;
    protected int doctorId;
    private String currentDate;
    private String selectedDate;
    private String earlierFeedbackStr;
    private boolean isUpdateInput;
    private String previousInputStr;
    private int jointUserId;
    private int isJoint;

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

        Toolbar toolbarTime = findViewById(R.id.toolbarTime);
        textCustomerName = findViewById(R.id.text_customer_name);
        textVisitDate = findViewById(R.id.text_visit_date);
        editStartTime = findViewById(R.id.edit_start_time);
        editEndTime = findViewById(R.id.edit_end_time);
        editFeedback = findViewById(R.id.edit_feedback);
        inputLayoutReason = findViewById(R.id.text_input_layout_reason);
        inputLayoutVisitDate = findViewById(R.id.input_layout_visit_date);
        Button buttonSubmitInput = findViewById(R.id.button_submit_input);
        editEarlierFeedback = findViewById(R.id.edit_earlier_feedback);
        layoutEarlierEntryFeedBack = findViewById(R.id.layout_earlier_entry_feedback);
        editStartTime.setFocusable(false);
        editEndTime.setFocusable(false);
        layoutEarlierEntryFeedBack.setVisibility(View.GONE);
        inputLayoutReason.setVisibility(View.GONE);
        setSupportActionBar(toolbarTime);
        toolbarTime.setNavigationIcon(R.drawable.back_arrow_whit);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Input");
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
        jointUserId = 0;
        loadFormIntent(intent);
        getCurrentDate();

    }


    public void uiValidation() {
        startTimeStr = editStartTime.getText().toString().trim();
        endTimeStr = editEndTime.getText().toString().trim();
        feedbackStr = editFeedback.getText().toString().trim();
        earlierFeedbackStr = editEarlierFeedback.getText().toString().trim();
        String strVisitDate = textVisitDate.getText().toString().trim();

        if (strVisitDate.equals(getString(R.string.select).replace("-", ""))  || strVisitDate.isEmpty()){
            inputLayoutVisitDate.setError("Please select date of visit.");
            textVisitDate.requestFocus();
        }
        else if (TextUtils.isEmpty(startTimeStr)) {
            editStartTime.requestFocus();
            editStartTime.setError(getString(R.string.invalid_input));
        }
        else if (TextUtils.isEmpty(endTimeStr)) {
            editEndTime.requestFocus();
            editEndTime.setError(getString(R.string.invalid_input));
        }
        else if (inputLayoutReason.getVisibility() != View.VISIBLE) {
            earlierFeedbackStr = null;
            proceedInput();
        }
        else {
               if (TextUtils.isEmpty(earlierFeedbackStr)){
                   inputLayoutReason.requestFocus();
                   inputLayoutReason.setError("Please enter a reason.");
               }
               else{
                   proceedInput();
               }
            }
        }


        public void proceedInput(){
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
                } else {
                    intent.putExtra(AppConstants.IS_INPUT_UPDATE, false);
                }

                startActivity(intent);

//                addInputApi(token, input);
            } else {
                Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
            }
        }


    public void loadFormIntent(Intent intent) {
        isUpdateInput = intent.getBooleanExtra(AppConstants.IS_INPUT_UPDATE, false);
        if (isUpdateInput){

            previousInputStr = intent.getStringExtra(AppConstants.PASS_UPDATE_INPUT);
            DataItem previousInputObj = new Gson().fromJson(previousInputStr, DataItem.class);
            Log.e("OP", new Gson().toJson(previousInputObj));
            input.setLatitude(previousInputObj.getLatitude());
            input.setLongitude(previousInputObj.getLongitude());
            input.setIsInLocation(previousInputObj.getIsInLocation());
            input.setPlanId(previousInputObj.getPlanId());
            input.setUserId(previousInputObj.getUserId());
            input.setInputId(previousInputObj.getInputId());

            //set to this class
            textVisitDate.setText(convertIntoDD_MM_YYYY(previousInputObj.getVisitDate()));
            editStartTime.setText(previousInputObj.getStartTime());
            editEndTime.setText(previousInputObj.getEndTime());
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
            boolean isDoctor = intent.getBooleanExtra(AppConstants.KEY_ROLE, false);
            textCustomerName.setText(intent.getStringExtra(AppConstants.CUSTOMER_NAME));
            longitude = intent.getDoubleExtra(AppConstants.LATITUDE, 0.0);
            latitude = intent.getDoubleExtra(AppConstants.LATITUDE, 0.0);
            isInLocation = intent.getIntExtra(AppConstants.KEY_IN_LOCATION, 0);
            doctorId = intent.getIntExtra(AppConstants.DOCTOR_ID, 0);
            chemistId = intent.getStringExtra(AppConstants.CHEMIST_ID);
            userId = intent.getIntExtra(AppConstants.KEY_USER_ID, 0);
            planId = intent.getIntExtra(AppConstants.KEY_PLAN_ID, 0);
            isJoint = intent.getIntExtra(AppConstants.KEY_IS_JOINT, 0);
            jointUserList  = (ArrayList<MRs>)intent.getExtras().getSerializable(AppConstants.KEY_JOINT_USER);
           if (jointUserList != null) {
               input.setJointUserList(jointUserList);
           }
            input.setLatitude(Double.toString(latitude));
            input.setLongitude(longitude + "");
            input.setIsInLocation(isInLocation);
            input.setIsJoint(isJoint);
            input.setPlanId(planId + "");
            input.setUserId(userId + "");
            if (isDoctor) {
                input.setDoctorId(doctorId + "");
            } else {
                input.setChemistsId(chemistId);
            }
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

            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            try {
                Date date1 = sdf.parse(currentDate);
                Date date2 = sdf.parse(selectedDate);
                Log.e("Compare ", date1.after(date2)+"");

                if (date1.compareTo(date2) != 0) {
                    layoutEarlierEntryFeedBack.setVisibility(View.VISIBLE);
                    inputLayoutReason.setVisibility(View.VISIBLE);

                } else {
                    layoutEarlierEntryFeedBack.setVisibility(View.GONE);
                    inputLayoutReason.setVisibility(View.GONE);
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
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        currentDate = df.format(c.getTime());
    }

    public String formatDate(String date){

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");//yyyy-MM-dd'T'HH:mm:ss
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Date data = null;
        try {
            data = sdf.parse(date);
        } catch (ParseException e) {
            Log.e("Exp", e.getMessage());
        }
        return dateFormat.format(data);
    }

    public void isTimeAfter() {
        startTimeStr = editStartTime.getText().toString().trim();
        endTimeStr = editEndTime.getText().toString().trim();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat fullFormet = new SimpleDateFormat("HH:mm");
        try {
            Date inTime = displayFormat.parse(startTimeStr);
            Date outTime = displayFormat.parse(endTimeStr);
            String tempTimeStr = fullFormet.format(inTime).substring(0,2);
            String tempTimeEnd = fullFormet.format(outTime).substring(0, 2);


            if (Integer.parseInt(tempTimeEnd)<Integer.parseInt(tempTimeStr)) {
                Toasty.error(AddInputActivity.this, "End time should be greater than start time", Toast.LENGTH_LONG).show();
            } else {
                uiValidation();
            }
        }catch (ParseException e){
            Log.e("Parse Exception", e.getMessage());
        }

    }

    public String convertIntoDD_MM_YYYY(String date){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("dd-MM-yyyy");
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
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate  = df.format(c.getTime());
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(AddInputActivity.this,
                (view, hourOfDay, minute1) -> {
                    selectedTime = hourOfDay + ":" + minute1;
                    timeWithDate = todayDate+" "+selectedTime+":00";

                    textView.setText(time24To12Hour(selectedTime));
                }, hour, minute, false);
        timePickerDialog.show();
    }




    public String time24To12Hour(String timeForConvert){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat _24HourTime = new SimpleDateFormat("HH:mm");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat _12HourTime = new SimpleDateFormat("hh:mm a");
        Date _24HourDate = null;
        try {
            _24HourDate = _24HourTime.parse(timeForConvert);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return _12HourTime.format(_24HourDate);
    }
}
