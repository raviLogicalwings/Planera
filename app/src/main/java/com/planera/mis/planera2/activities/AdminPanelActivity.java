package com.planera.mis.planera2.activities;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.fragments.AdminAccessFragment;


public class AdminPanelActivity extends BaseActivity {
    private BottomNavigationView adminNavigation;
    private FragmentManager fragmentManager;
    public static final int FRAGMENT_ADMIN_DASHBOARD = 1;
    public static final int FRAGMENT_USER_CONTROL = 2;
    public static final String KEY_ADMIN_DASHBOARD = "Admin_Dashboard";
    public static final String KEY_USER_CONTROL = "User_Control";
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
    public void initUi() {
        super.initUi();
        adminNavigation = findViewById(R.id.admin_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow_whit);
        adminNavigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    @Override
    public void initData() {
        super.initData();
        fragmentManager = getFragmentManager();
        loadFragment(FRAGMENT_ADMIN_DASHBOARD);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            item -> {
                switch (item.getItemId()) {
                    case R.id.dashBoard:
                        loadFragment(FRAGMENT_ADMIN_DASHBOARD);
                        return true;
                    case R.id.userControl:
                        return true;
                }
                return false;
            };



    public void loadFragment(int type){
        currentFragment = type;
        switch (currentFragment){
            case FRAGMENT_ADMIN_DASHBOARD:
                TAG = KEY_ADMIN_DASHBOARD;
                 fragment= AdminAccessFragment.getInstance();
                getSupportActionBar().setTitle("Dashboard");
                break;
            case FRAGMENT_USER_CONTROL:
                TAG = KEY_USER_CONTROL;
                break;

        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment, TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }
}
