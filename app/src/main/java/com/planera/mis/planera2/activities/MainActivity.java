package com.planera.mis.planera2.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.fragments.HomeFragment;
import com.planera.mis.planera2.activities.fragments.ProfileFragment;
import com.planera.mis.planera2.activities.utils.BottomNavigationViewHelper;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTextMessage;
    private  BottomNavigationView navigation;
    public FragmentManager manager;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                   manager.beginTransaction().replace(R.id.container, new HomeFragment()).commit();
                    return true;
                case R.id.navigation_meeting:
                    return true;
                case R.id.navigation_profile:
                   manager.beginTransaction().replace(R.id.container, new ProfileFragment()).commit();
                    return true;
                case R.id.navigation_report:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initUi();

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void initUi() {
        super.initUi();
        navigation  = findViewById(R.id.navigation);
        BottomNavigationViewHelper.removeShiftMode(navigation);
        mTextMessage = findViewById(R.id.message);
    }

    @Override
    public void initData() {
        super.initData();
        manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.container, new HomeFragment()).commit();
    }

    @Override
    public void onClick(View view) {


    }
}
