package com.planera.mis.planera2.activities;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.planera.mis.planera2.Retrofit.ApiClient;
import com.planera.mis.planera2.Retrofit.ApiInterface;
import com.planera.mis.planera2.controller.DataController;
import com.planera.mis.planera2.utils.AppConstants;
import com.planera.mis.planera2.utils.PreferenceConnector;
import com.planera.mis.planera2.utils.ProcessDialog;

import io.fabric.sdk.android.Fabric;


public class BaseActivity extends AppCompatActivity {
    public ApiInterface apbInterfaceForGooglePlaces;
    public PreferenceConnector connector;
    public ProcessDialog processDialog;
    public View rootView;
    //public RuntimePermissionCheck permissionCheck;

    public ApiInterface apiInterface;
    public String token;
    public Context mContext;


    public void initData() {

        Fabric.with(this, new Crashlytics());
        rootView = getWindow().getDecorView().getRootView();
        connector = PreferenceConnector.getInstance(this);
        //permissionCheck = new RuntimePermissionCheck(getBaseContext());
        apiInterface  = ApiClient.getInstance();
        token = connector.getString(AppConstants.TOKEN);
        apbInterfaceForGooglePlaces = ApiClient.getInstanceForLocation();
        processDialog = new ProcessDialog();
        mContext = getBaseContext();
        DataController.getmInstance();

    }

    public void initUi(){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
