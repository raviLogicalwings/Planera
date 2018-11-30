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
import com.planera.mis.planera2.utils.AppConstants;
import com.planera.mis.planera2.utils.PreferenceConnector;

import java.util.ArrayList;
import java.util.List;

import static com.planera.mis.planera2.utils.RuntimePermissionCheck.REQUEST_ID_MULTIPLE_PERMISSIONS;


public class SplashScreenActivity extends BaseActivity {
    //public RuntimePermissionCheck permissionCheck;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean isUserLogin;
    public boolean isUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        initUi();
        initData();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                goToNextScreen();
            }
        }, AppConstants.SPLASH_TIME_OUT);

    }

    private void goToNextScreen() {
        Log.e("Check", "mainFunction");
        if (!checkAndRequestPermissions()) {
            return;
        }
        if (!token.isEmpty()) {
            Log.e("Check", "User LogedIn "+isUser + " token"+ token);
            if (isUser) {
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                ActivityCompat.finishAffinity(SplashScreenActivity.this);
                startActivity(intent);
            } else {
                Intent intent = new Intent(SplashScreenActivity.this, AdminPanelActivity.class);
                ActivityCompat.finishAffinity(SplashScreenActivity.this);
                startActivity(intent);
            }
        } else {
            Log.e("Check", "Not login");
            Intent intentLogin = new Intent(SplashScreenActivity.this, LoginActivity.class);
            ActivityCompat.finishAffinity(SplashScreenActivity.this);
            startActivity(intentLogin);
        }
    }

    @Override
    public void initUi() {
        super.initUi();

    }

    @Override
    public void initData() {
        super.initData();
        PreferenceConnector connector = PreferenceConnector.getInstance(this);
        isUser = connector.getBoolean(AppConstants.IS_USER);
        //permissionCheck = new RuntimePermissionCheck(SplashScreenActivity.this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(SplashScreenActivity.this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        goToNextScreen();
                        Toast.makeText(SplashScreenActivity.this, "Permission granted", Toast.LENGTH_LONG).show();
                    }

                } else {
                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(SplashScreenActivity.this, "Permission Denied!", Toast.LENGTH_LONG).show();
                    Toast.makeText(SplashScreenActivity.this, "Please give permission!, close app and re-open", Toast.LENGTH_LONG).show();
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
