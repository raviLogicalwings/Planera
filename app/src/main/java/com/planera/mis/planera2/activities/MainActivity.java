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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.fragments.HomeFragment;
import com.planera.mis.planera2.fragments.ProfileFragment;
import com.planera.mis.planera2.utils.AppConstants;
import com.planera.mis.planera2.utils.BottomNavigationViewHelper;
import com.planera.mis.planera2.utils.InternetConnection;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class MainActivity extends BaseActivity implements
        SearchView.OnQueryTextListener {

    private ImageView imageAddPlan;
    private BottomNavigationView navigation;
    private Toolbar toolbarProduct;
    private Fragment fragment;
    private SearchView searchView;
    private OnSearchQueryListener searchQueryListener;

    private AppBarLayout appBar;
    public FragmentManager manager;
    private Intent intent;


    @Override
    public void initData() {
        super.initData();
        manager = getSupportFragmentManager();
        loadFragment(AppConstants.HOME_FRAGMENT);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                loadFragment(AppConstants.HOME_FRAGMENT);
                Objects.requireNonNull(getSupportActionBar()).setTitle("Visit Plan");
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
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(this);
       return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                if (InternetConnection.isNetworkAvailable(MainActivity.this)) {
                    intent = new Intent(MainActivity.this, ActivityUserPlanCreate.class);
                    startActivity(intent);
                } else {
                    Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_SHORT).show();
                }
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount()>0){
          getFragmentManager().popBackStack();
        }
        super.onBackPressed();
    }

    public void loadFragment(int type) {
        switch (type) {

            case AppConstants.HOME_FRAGMENT:
                fragment = new HomeFragment();
//               getSupportActionBar().setTitle("Home");
                break;

            case AppConstants.PROFILE_FRAGMENT:
                fragment = ProfileFragment.newInstance();
                Objects.requireNonNull(getSupportActionBar()).setTitle("Profile");
                break;


        }

        manager.beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
    }



    @Override
    public void initUi() {
        super.initUi();
        navigation = findViewById(R.id.navigation);
        toolbarProduct = findViewById(R.id.toolbarProduct);
        appBar = findViewById(R.id.appBar);


        setSupportActionBar(toolbarProduct);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Visit Plan");
        BottomNavigationViewHelper.removeShiftMode(navigation);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void backToLogin() {
        connector.clearValue(AppConstants.USER_TYPE);
        connector.setBoolean(AppConstants.IS_LOGIN, false);
        intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void popupDialog() {
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
    public boolean onQueryTextSubmit(String query) {
        searchQueryListener.onSearchQuery(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        searchQueryListener.onSearchQuery(newText);
        return false;
    }


    public void setSearchQueryListener(OnSearchQueryListener searchQueryListener)
    {
        this.searchQueryListener =searchQueryListener;
    }
    public interface OnSearchQueryListener{
        void onSearchQuery(String query);
    }
}
