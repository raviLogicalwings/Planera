package com.planera.mis.planera2.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.utils.AppConstants;
import com.planera.mis.planera2.activities.utils.PreferenceConnector;
import com.planera.mis.planera2.activities.utils.RuntimePermissionCheck;

import java.util.ArrayList;
import java.util.List;

import static com.planera.mis.planera2.activities.utils.RuntimePermissionCheck.REQUEST_ID_MULTIPLE_PERMISSIONS;


public class SplashActivity extends BaseActivity {
    public boolean isUserLogin;
    public boolean isUser;
    public RuntimePermissionCheck permissionCheck;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
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
        PreferenceConnector connector = PreferenceConnector.getInstance(this);

        isUserLogin = connector.getBoolean(AppConstants.IS_LOGIN);
        isUser = connector.getBoolean(AppConstants.IS_USER);
        permissionCheck = new RuntimePermissionCheck(SplashActivity.this);

        if (checkAndRequestPermissions()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isUserLogin){
                        if (isUser) {
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            ActivityCompat.finishAffinity(SplashActivity.this);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(SplashActivity.this, AdminPanelActivity.class);
                            ActivityCompat.finishAffinity(SplashActivity.this);
                            startActivity(intent);
                        }
                    }
                    else {
//                Toast.makeText(this, isUserLogin+"", Toast.LENGTH_SHORT).show();
//                        callSplash();
                        Intent intentLogin = new Intent(SplashActivity.this, LoginActivity.class);
                        ActivityCompat.finishAffinity(SplashActivity.this);
                        startActivity(intentLogin);
                    }
                }
            }, AppConstants.SPLASH_TIME_OUT);
        } else {
            checkAndRequestPermissions();
        }



    }

    public void callSplash(){
        new Handler().postDelayed(() -> {
                Intent intentLogin = new Intent(SplashActivity.this, LoginActivity.class);
                intentLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentLogin);
        }, AppConstants.SPLASH_TIME_OUT);

    }



    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(SplashActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(SplashActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(SplashActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(SplashActivity.this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        initData();
                        Toast.makeText(SplashActivity.this, "Permission granted", Toast.LENGTH_LONG).show();
                    }

                } else {
                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(SplashActivity.this, "Permission Denied!", Toast.LENGTH_LONG).show();
                    Toast.makeText(SplashActivity.this, "Please give permission!, close app and re-open", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }

    }

    private boolean checkAndRequestPermissions() {
        int receiveWriteExternal = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int receiveReadExternal = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int receiveLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (receiveWriteExternal != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (receiveReadExternal != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (receiveLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
}
