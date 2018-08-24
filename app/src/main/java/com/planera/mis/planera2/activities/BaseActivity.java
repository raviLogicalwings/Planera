package com.planera.mis.planera2.activities;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import com.planera.mis.planera2.activities.Retrofit.ApiClient;
import com.planera.mis.planera2.activities.Retrofit.ApiInterface;
import com.planera.mis.planera2.activities.utils.AppConstants;
import com.planera.mis.planera2.activities.utils.PreferenceConnector;
import com.planera.mis.planera2.activities.utils.ProcessDialog;



public class BaseActivity extends AppCompatActivity {
    public ApiInterface apiInterfaceForGooglePalces;
    public PreferenceConnector connector;
    public ProcessDialog processDialog;
    public View rootView;

    public ApiInterface apiInterface;
    String token;
    public Context mContext;


    public void initData(){
        rootView = getWindow().getDecorView().getRootView();
        connector = PreferenceConnector.getInstance(this);

        apiInterface  = ApiClient.getInstance();
        token = connector.getString(AppConstants.TOKEN);
        apiInterfaceForGooglePalces = ApiClient.getInstanceForLocation();
        processDialog = new ProcessDialog();
        mContext = getBaseContext();

    }

    public void initUi(){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

//    public void showProgressDialog(String title, String message){
//        if(progressDialog!=null && progressDialog.isShowing()){
//            progressDialog.dismiss();
//        }

//        progressDialog = new ProgressDialog(getApplication());
//
//        if(title!= null){
//            new Handler().postDelayed(() -> {
//                progressDialog.setTitle(title);
//                progressDialog.setMessage(message);
//                progressDialog.setCancelable(false);
//                progressDialog.show();
//            },100);
//
//        }
//    }
//
//
//    public void dismissProgressDialog(){
//        if(progressDialog!=null && progressDialog.isShowing()){
//            progressDialog.dismiss();
//        }
//    }


}
