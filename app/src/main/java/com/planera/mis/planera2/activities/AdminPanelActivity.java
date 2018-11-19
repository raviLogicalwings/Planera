package com.planera.mis.planera2.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.fragments.AdminAccessFragment;
import com.planera.mis.planera2.activities.fragments.UploadDataFragment;
import com.planera.mis.planera2.activities.utils.AppConstants;


public class AdminPanelActivity extends BaseActivity {
    private BottomNavigationView adminNavigation;
    private FragmentManager fragmentManager;
    public static final int FRAGMENT_ADMIN_DASHBOARD = 1;
    public static final int FRAGMENT_UPLOAD = 2;
    public static final String KEY_ADMIN_DASHBOARD = "Admin_Dashboard";
    public static final String KEY_UPLOAD_EXCEL= "uploads";
    int currentFragment ;
    private Fragment fragment;
    String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        initUi();
        initData();
    }

    @Override
    public void onBackPressed() {
        ActivityCompat.finishAffinity(AdminPanelActivity.this);
    }

    @Override
    public void initUi() {
        super.initUi();
        adminNavigation = findViewById(R.id.admin_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
//        toolbar.setNavigationIcon(R.drawable.back_arrow_whit);
        adminNavigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        setSupportActionBar(toolbar);
//        toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    @Override
    public void initData() {
        super.initData();
        fragmentManager = getSupportFragmentManager();
        loadFragment(FRAGMENT_ADMIN_DASHBOARD);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            item -> {
                switch (item.getItemId()) {
                    case R.id.dashBoard:
                        loadFragment(FRAGMENT_ADMIN_DASHBOARD);
                        return true;
                    case R.id.uploads:
                        loadFragment(FRAGMENT_UPLOAD);
                        return true;

                    case R.id.logout_admin:
                        popupDialog();
                        return true;
                }
                return false;
            };

    private void backToLogin() {
        Intent intentLogin = new Intent(AdminPanelActivity.this, LoginActivity.class);
        connector.setBoolean(AppConstants.IS_LOGIN, false);
        startActivity(intentLogin);
        finish();
    }

    public void popupDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(AdminPanelActivity.this).create();

        alertDialog.setMessage("Are you sure you want to logout?");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (dialogInterface, i) -> {
            backToLogin();
        });


        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", (dialog, which) -> {
            dialog.cancel();
        });
        alertDialog.show();

    }


    public void loadFragment(int type){
        currentFragment = type;
        switch (currentFragment){
            case FRAGMENT_ADMIN_DASHBOARD:
                TAG = KEY_ADMIN_DASHBOARD;
                 fragment= new AdminAccessFragment();
                getSupportActionBar().setTitle("Dashboard");
                break;
            case FRAGMENT_UPLOAD:
                TAG = KEY_UPLOAD_EXCEL;
                fragment = UploadDataFragment.getInstance();
                getSupportActionBar().setTitle("Upload");
                break;

        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment, TAG)
                .commit();
    }
}
