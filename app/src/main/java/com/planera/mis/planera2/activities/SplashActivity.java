package com.planera.mis.planera2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.utils.AppConstants;
import com.planera.mis.planera2.activities.utils.PreferenceConnector;
import com.planera.mis.planera2.activities.utils.RuntimePermissionCheck;


public class SplashActivity extends BaseActivity {
    public boolean isUserLogin;
    public boolean isUser;
    private PreferenceConnector connector;
    public RuntimePermissionCheck permissionCheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initUi();
        try {
            initData();
        } catch (Exception e) {
            Log.e("Exception ", e.getMessage());
        }

    }

    @Override
    public void initUi() {
        super.initUi();

    }

    @Override
    public void initData(){
        super.initData();
        connector = PreferenceConnector.getInstance(this);

        isUserLogin = connector.getBoolean(AppConstants.IS_LOGIN);
        isUser = connector.getBoolean(AppConstants.IS_USER);
        permissionCheck = new RuntimePermissionCheck(SplashActivity.this);
        if(permissionCheck.checkPermissionForWriteExternalStorage() && permissionCheck.checkPermissionForReadExtertalStorage()) {

            if (isUserLogin){
                if (isUser) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SplashActivity.this, AdminPanelActivity.class);
                    startActivity(intent);
                }
            }
            else {
                Toast.makeText(this, isUserLogin+"", Toast.LENGTH_SHORT).show();
                callSplash();
            }
        }
        else{
            try {
                permissionCheck.requestPermissionForWriteExternalStorage();
                permissionCheck.requestPermissionForReadExtertalStorage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


    public void callSplash(){
        new Handler().postDelayed(() -> {

                Intent intentLogin = new Intent(SplashActivity.this, LoginActivity.class);
                intentLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentLogin);


        }, AppConstants.SPLASH_TIME_OUT);

    }
}
