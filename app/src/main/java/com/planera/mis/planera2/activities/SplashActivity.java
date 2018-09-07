package com.planera.mis.planera2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.utils.AppConstants;
import com.planera.mis.planera2.activities.utils.RuntimePermissionCheck;


public class SplashActivity extends BaseActivity {
    public boolean isUserLogin;
    public boolean isUser;
    public RuntimePermissionCheck permissionCheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initUi();
        initData();

    }

    @Override
    public void initUi() {
        super.initUi();

    }

    @Override
    public void initData() {
        super.initData();
        isUserLogin = connector.getBoolean(AppConstants.IS_LOGIN);
        isUser = connector.getBoolean(AppConstants.IS_USER);
        permissionCheck = new RuntimePermissionCheck(SplashActivity.this);

        if (isUserLogin){
            if(isUser) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            }
            else{
                Intent intent = new Intent(SplashActivity.this, AdminPanelActivity.class);
                startActivity(intent);
            }
        }
        else{
            callSplash();
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
