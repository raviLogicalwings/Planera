package com.planera.mis.planera2.activities.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.ActivityUploadChemist;
import com.planera.mis.planera2.activities.ActivityUploadDoctor;

public class UploadDataFragment extends BaseFragment implements View.OnClickListener{
    public  View view;
    private CardView cardUploadPlans;
    private CardView cardUploadDoctor;
    private CardView cardUploadChemist;

    public static UploadDataFragment instance;

    public static UploadDataFragment getInstance(){
        if(instance== null){
            instance = new UploadDataFragment();
        }
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=  inflater.inflate(R.layout.fragment_upload_data, container, false);
        initUi();
        initData();
        return view;
    }



    public UploadDataFragment() {
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initUi() {
        super.initUi();
        cardUploadPlans = view.findViewById(R.id.card_upload_plans);
        cardUploadDoctor = view.findViewById(R.id.card_upload_doctor);
        cardUploadChemist = view.findViewById(R.id.card_upload_chemist);

        cardUploadChemist.setOnClickListener(this);
        cardUploadDoctor.setOnClickListener(this);
        cardUploadPlans.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.card_upload_plans:

                break;
            case R.id.card_upload_doctor:
                Intent intentDoctorUpload = new Intent(mContext, ActivityUploadDoctor.class);
                startActivity(intentDoctorUpload);
                break;
            case R.id.card_upload_chemist:
                Intent intentChemistUpload = new Intent(mContext, ActivityUploadChemist.class);
                startActivity(intentChemistUpload);
                break;
        }

    }

}
