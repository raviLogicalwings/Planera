package com.planera.mis.planera2.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.planera.mis.planera2.R;
import com.planera.mis.planera2.models.ObtainReport;
import com.planera.mis.planera2.utils.AppConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SearchDateWiseInputActivity extends BaseActivity implements View.OnClickListener {
    Toolbar toolbar;
    private EditText editStartTime;
    private EditText editEndTime;
    private Button buttonSubmitForList;
    public ObtainReport obtainReport;
    private String strPickedDate;
    private String userId;
    String objectToString;
    private TextInputLayout inputLayoutStartDate;
    private TextInputLayout inputLayoutEndDate;
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
        inputLayoutStartDate = findViewById(R.id.input_layout_start_date);
        inputLayoutEndDate = findViewById(R.id.input_layout_end_date);

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
            inputLayoutStartDate.setError(getString(R.string.invalid_input));
        }
       else if (TextUtils.isEmpty(srtEndDate)){
            editEndTime.requestFocus();
            inputLayoutEndDate.setError(getString(R.string.invalid_input));
        }

        else {
            obtainReport.setStartDate(formatDate(strStartDate));
           if (formatDate(srtEndDate) != null){
               String a[]  = formatDate(srtEndDate).split(" ");
               String end = a[0]+ " 23:59:59";
               obtainReport.setEndDate(end);
           }


            obtainReport.setUserId(userId);
            objectToString = new Gson().toJson(obtainReport);
            Intent intent = new Intent(SearchDateWiseInputActivity.this, UserInputListActivity.class);
            intent.putExtra(AppConstants.OBTAIN_REPORT, objectToString);
            startActivity(intent);
        }
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
        int mMonth = mCurrentTime.get(Calendar.MONTH);
        int mDay = mCurrentTime.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(SearchDateWiseInputActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                strPickedDate = dayOfMonth+"-"+(month+1)+"-"+year;
                editText.setText(strPickedDate);

            }
        },mYear, mMonth, mDay );

        //code for select on current date and onwards.
        // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//        Toast.makeText(ActivityAdminReports.this, System.currentTimeMillis()+"", Toast.LENGTH_LONG).show();
//        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();

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
