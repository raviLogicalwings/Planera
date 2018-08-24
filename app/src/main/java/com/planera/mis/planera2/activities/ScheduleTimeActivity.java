package com.planera.mis.planera2.activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;


import com.planera.mis.planera2.R;

import java.util.Calendar;

public class ScheduleTimeActivity extends BaseActivity implements View.OnClickListener {
    private AppBarLayout appBar;
    private Toolbar toolbarTime;
    private TextView textVisitDate;
    private Spinner spinnerSession;
    private TextView textTime;
    private EditText editFeedback;
    private Button buttonSubmit;

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


        appBar = findViewById(R.id.appBar);
        toolbarTime = findViewById(R.id.toolbarTime);


        appBar = findViewById(R.id.appBar);
        toolbarTime = findViewById(R.id.toolbarTime);
        textVisitDate = findViewById(R.id.text_visit_date);
        spinnerSession = findViewById(R.id.spinner_session);
        textTime = findViewById(R.id.text_time);
        editFeedback = findViewById(R.id.edit_feedback);
        buttonSubmit = findViewById(R.id.button_submit);

        setSupportActionBar(toolbarTime);
        getSupportActionBar().setTitle("Schedule");
        toolbarTime.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        buttonSubmit.setOnClickListener(this);

    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_submit:
                Intent intentProduct = new Intent(ScheduleTimeActivity.this, AdminPanelActivity.class);
                startActivity(intentProduct);
                break;
        }

    }



//    public String getDateFromDialog(){
//        Calendar mCurrentTime = Calendar.getInstance();
//        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
//        int minute = mCurrentTime.get(Calendar.MINUTE);
//        TimePickerDialog timePickerDialog = new TimePickerDialog(ScheduleTimeActivity.this,
//                new TimePickerDialog.OnTimeSetListener() {
//
//                    @Override
//                    public void onTimeSet(TimePicker view, int hourOfDay,
//                                          int minute) {
//                        if (hourOfDay>12){
//                           int temHourOfDay = hourOfDay-12;
//                            time = temHourOfDay + ":" + minute + "PM";
//                            Log.d(time, "time");
//                        }
//                        else
//                        {
//                            time = hourOfDay + ":" + minute+ " AM";
//                        }
//                    }
//                }, hour, minute, false);
//        timePickerDialog.show();
//        return time;
//    }
}
