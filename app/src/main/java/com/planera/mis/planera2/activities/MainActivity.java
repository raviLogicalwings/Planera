package com.planera.mis.planera2.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.fragments.HomeFragment;
import com.planera.mis.planera2.fragments.ProfileFragment;
import com.planera.mis.planera2.utils.AppConstants;
import com.planera.mis.planera2.utils.BottomNavigationViewHelper;
import com.planera.mis.planera2.utils.InternetConnection;

import java.util.Objects;

public class MainActivity extends BaseActivity implements
        View.OnClickListener {

    private TextView mTextMessage;
    private  BottomNavigationView navigation;
    public FragmentManager manager;
    private Fragment fragment;
    private AppBarLayout appBar;
    private Toolbar toolbarProduct;
    private ImageView imageAddPlan;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                      loadFragment(AppConstants.HOME_FRAGMENT);
                      getSupportActionBar().setTitle("Visit Plan");
                        return true;
                    case R.id.navigation_meeting:
                        Intent intent = new Intent(MainActivity.this, SearchDateWiseInputActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.navigation_profile:
                     loadFragment(AppConstants.PROFILE_FRAGMENT);
                        return true;
                    case R.id.navigation_logout:
                        popupDialog();
                        return true;
                }
                return false;
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
    public void onBackPressed() {
      super.onBackPressed();
        }


    public void loadFragment(int type){
        switch (type){

            case AppConstants.HOME_FRAGMENT:
                fragment = new HomeFragment();
//               getSupportActionBar().setTitle("Home");
                break;

            case AppConstants.PROFILE_FRAGMENT:
                fragment = ProfileFragment.newInstance();
                Objects.requireNonNull(getSupportActionBar()).setTitle("Profile");
                break;


        }

        manager.beginTransaction().replace(R.id.container, fragment).commit();
    }
    @Override
    public void initUi() {
        super.initUi();
        navigation  = findViewById(R.id.navigation);
        BottomNavigationViewHelper.removeShiftMode(navigation);
        mTextMessage = findViewById(R.id.message);
        imageAddPlan = findViewById(R.id.img_action_add);

        appBar = findViewById(R.id.appBar);
        toolbarProduct = findViewById(R.id.toolbarProduct);
        setSupportActionBar(toolbarProduct);
        getSupportActionBar().setTitle("Visit Plan");
        imageAddPlan.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void backToLogin(){
        connector.setBoolean(AppConstants.IS_LOGIN, false);
        Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intentLogin);
        finish();
    }

    public void popupDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

        alertDialog.setMessage("Are you sure you want to logout?");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (dialogInterface, i) -> {
            backToLogin();

        });


        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", (dialog, which) -> {
            dialog.cancel();
        });
        alertDialog.show();

    }
    @Override
    public void initData() {
        super.initData();
        manager = getSupportFragmentManager();
       loadFragment(AppConstants.HOME_FRAGMENT);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_action_add:
                if (InternetConnection.isNetworkAvailable(MainActivity.this)){
                    Intent intent = new Intent(MainActivity.this, ActivityUserPlanCreate.class);
                    startActivity(intent);
                }
                else{
                    Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
