package com.planera.mis.planera2.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.planera.mis.planera2.R;
import com.planera.mis.planera2.models.ObtainReport;
import com.planera.mis.planera2.utils.AppConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class SearchDateWiseInputActivity extends BaseActivity implements View.OnClickListener {
    private static final String DD_MM_YY = "dd-MM-yyyy";
    private static final String HH_MM_SS = "yyyy-MM-dd HH-mm-ss";

    protected Button buttonSubmitForList;
    protected Toolbar toolbar;
    private EditText editEndTime;
    private EditText editStartTime;
    private TextInputLayout inputLayoutStartDate;
    private TextInputLayout inputLayoutEndDate;

    public ObtainReport obtainReport;

    private String strPickedDate;
    private String userId;
    private  String TAG;
    protected String objectToString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_date_wise_input);

        initData();

        initUi();
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
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.set_details));
        TAG = SearchDateWiseInputActivity.class.getSimpleName();
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
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(AppConstants.OBTAIN_REPORT, objectToString);
            startActivity(intent);
            finish();
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
        int mMonth = mCurrentTime.get(Calendar.MONTH);
        int mDay = mCurrentTime.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(SearchDateWiseInputActivity.this, (view, year, month, dayOfMonth) -> {

            strPickedDate = dayOfMonth+"-"+(month+1)+"-"+year;
            editText.setText(strPickedDate);

        },mYear, mMonth, mDay );

        datePickerDialog.show();

    }

    public String formatDate(String date){

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(DD_MM_YY);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat(HH_MM_SS);
        Date data = null;
        try {
            data = sdf.parse(date);
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
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
