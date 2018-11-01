package com.planera.mis.planera2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.FragmentDialog.addDialogs.AddGiftDialog;
import com.planera.mis.planera2.activities.FragmentDialog.addDialogs.AddPatchDialog;
import com.planera.mis.planera2.activities.FragmentDialog.addDialogs.AddProductDialog;
import com.planera.mis.planera2.activities.FragmentDialog.addDialogs.AddStateDialog;
import com.planera.mis.planera2.activities.FragmentDialog.addDialogs.AddTerritoryDialog;
import com.planera.mis.planera2.activities.fragments.ChemistFragment;
import com.planera.mis.planera2.activities.fragments.DoctorsFragment;
import com.planera.mis.planera2.activities.fragments.GiftListFragment;
import com.planera.mis.planera2.activities.fragments.PatchListFragment;
import com.planera.mis.planera2.activities.fragments.PlansFragment;
import com.planera.mis.planera2.activities.fragments.ProductFragment;
import com.planera.mis.planera2.activities.fragments.StateListFragment;
import com.planera.mis.planera2.activities.fragments.TerritoryListFragment;
import com.planera.mis.planera2.activities.fragments.UsersFragment;
import com.planera.mis.planera2.activities.utils.AppConstants;


public class SingleListActivity extends BaseActivity implements View.OnClickListener,
        AddProductDialog.OnAddProductDialogDismissListener,
        AddStateDialog.OnStateDialogDismissListener,
        AddGiftDialog.OnAddGiftDialogDismissListener,
        AddPatchDialog.OnAddPatchDialogDismissListener, AddTerritoryDialog.OnAddTerritoryDialogDismissListener{
    int comingFragment;
    private Fragment fragment;
    public static final int REQUEST_CODE_DOCTOR = 101;
    public static final int REQUEST_CODE_CHEMIST = 102;
    public static final int REQUEST_CODE_PLAN = 103;
    private int revertFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_list);
        initUi();
        initData();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.actiion_add:
               if(comingFragment == AppConstants.STATE_FRAGMENT){
                   AddStateDialog dialog = new AddStateDialog();
                   dialog.show(getSupportFragmentManager(), "States");
               }
               else if (comingFragment == AppConstants.TERRITORY_FRAGMENT){
                   AddTerritoryDialog dialog = new AddTerritoryDialog();
                   dialog.show(getSupportFragmentManager(), "Territory");
               }

               else if (comingFragment == AppConstants.GIFT_FRAGMENT)
               {
                   AddGiftDialog dialog = new AddGiftDialog();
                   dialog.show(getSupportFragmentManager(), "Gift");
               }

               else if (comingFragment == AppConstants.PATCH_FRAGMENT){
                   AddPatchDialog dialog = new AddPatchDialog();
                   dialog.show(getSupportFragmentManager(), "Patch");

               }

               else if(comingFragment == AppConstants.PRODUCT_FRAGMENT) {
                   AddProductDialog dialog = new AddProductDialog();
                   dialog.show(getSupportFragmentManager(), "Product");
               }

               else if(comingFragment == AppConstants.DOCTOR_FRAGMENT){
                   Intent intentAddDoctor = new Intent(SingleListActivity.this, ActivityAddDoctor.class);
                   startActivity(intentAddDoctor);

               }

               else if(comingFragment == AppConstants.CHEMIST_FRAGMENT){
                   Intent intentAddChemist = new Intent(SingleListActivity.this, ActivityAddChemist.class);
                   startActivity(intentAddChemist);

                }

                else if (comingFragment == AppConstants.PLAN_FRAGMENT){
                   Intent intentPlan = new Intent(SingleListActivity.this, ActivityCretePlan.class);
                   startActivity(intentPlan);
               }
               break;
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void initUi() {
        super.initUi();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> {
            onBackPressed();
        });
    }

    @Override
    public void initData() {
        super.initData();
        Intent getData = getIntent();
        comingFragment = getData.getIntExtra(AppConstants.KEY_TOUCHED_FRAGMENT, 0);
        if (comingFragment!= 0) {
            loadFragment(comingFragment);
            refreshFragment(comingFragment);
        }

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SingleListActivity.this, AdminPanelActivity.class);
        startActivity(intent);
    }

    public void refreshFragment(int type){
        switch (type){
            case AppConstants.STATE_FRAGMENT:
                getSupportFragmentManager().beginTransaction().
                        detach(new StateListFragment()).
                        attach(new StateListFragment()).commitAllowingStateLoss();
                break;

            case AppConstants.PATCH_FRAGMENT:
                getSupportFragmentManager().beginTransaction().
                        detach(new PatchListFragment()).
                        attach(new PatchListFragment()).commitAllowingStateLoss();
                break;

            case AppConstants.TERRITORY_FRAGMENT:
                getSupportFragmentManager().beginTransaction().
                        detach(new TerritoryListFragment()).
                        attach(new TerritoryListFragment()).commitAllowingStateLoss();
                break;

            case AppConstants.GIFT_FRAGMENT:
                getSupportFragmentManager().beginTransaction().
                        detach(new GiftListFragment()).
                        attach(new GiftListFragment()).commitAllowingStateLoss();
                break;

            case AppConstants.PRODUCT_FRAGMENT:
                getSupportFragmentManager().beginTransaction().
                        detach(new ProductFragment()).
                        attach(new ProductFragment()).commitAllowingStateLoss();
                break;

            case AppConstants.DOCTOR_FRAGMENT:
                getSupportFragmentManager().beginTransaction().
                        detach(new DoctorsFragment()).
                        attach(new DoctorsFragment()).commitAllowingStateLoss();
                break;

            case AppConstants.CHEMIST_FRAGMENT:
                getSupportFragmentManager().beginTransaction().
                        detach(new ChemistFragment()).
                        attach(new ChemistFragment()).commitAllowingStateLoss();

            case AppConstants.USER_FRAGMENT:
                getSupportFragmentManager().beginTransaction().
                        detach( new UsersFragment()).
                        attach(new UsersFragment()).commitAllowingStateLoss();
                break;

            case AppConstants.PLAN_FRAGMENT:
                getSupportFragmentManager().beginTransaction().
                        detach(new PatchListFragment()).
                        attach(new PatchListFragment()).commitAllowingStateLoss();
                        break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        switch (requestCode){
            case REQUEST_CODE_PLAN:
                refreshFragment(AppConstants.PLAN_FRAGMENT);
                break;
            case REQUEST_CODE_CHEMIST:
                refreshFragment(AppConstants.CHEMIST_FRAGMENT);
                break;
            case REQUEST_CODE_DOCTOR:
                refreshFragment(AppConstants.DOCTOR_FRAGMENT);
                break;
        }
    }

    public void loadFragment(int type){
        switch (type){

            case AppConstants.STATE_FRAGMENT:
                fragment =  new StateListFragment();
                getSupportActionBar().setTitle("States");
                break;

            case AppConstants.TERRITORY_FRAGMENT:
                fragment = new TerritoryListFragment();
                getSupportActionBar().setTitle("Territory");
                break;

            case AppConstants.GIFT_FRAGMENT:
                fragment = new GiftListFragment();
                getSupportActionBar().setTitle("Gift");
                break;

            case AppConstants.PATCH_FRAGMENT:
                fragment = new PatchListFragment();
                getSupportActionBar().setTitle("Patch");
                break;


            case AppConstants.DOCTOR_FRAGMENT:
                fragment  = new DoctorsFragment();
                getSupportActionBar().setTitle("Doctors");
                break;

            case AppConstants.PRODUCT_FRAGMENT:
                fragment = new ProductFragment();
                getSupportActionBar().setTitle("Products");
                break;

            case AppConstants.CHEMIST_FRAGMENT:
                fragment = new ChemistFragment();
                getSupportActionBar().setTitle("Chemists");
                break;

            case AppConstants.USER_FRAGMENT:
                fragment = new UsersFragment();
                getSupportActionBar().setTitle("Users");
                break;

            case AppConstants.PLAN_FRAGMENT:
                fragment = new PlansFragment();
                getSupportActionBar().setTitle("Plans");
                break;
        }

      getSupportFragmentManager().beginTransaction().replace(R.id.containerSingle, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commitAllowingStateLoss();
    }

    @Override
    public void onClick(View view) {

    }


    @Override
    public void onDialogDismiss() {
        Toast.makeText(this, "Getting Intraction !!", Toast.LENGTH_SHORT).show();
        loadFragment(AppConstants.STATE_FRAGMENT);
    }

    @Override
    public void onAddTerritoryDialogDismiss() {
        refreshFragment(AppConstants.TERRITORY_FRAGMENT);
    }

    @Override
    public void onAddGiftDialogDismiss() {
        refreshFragment(AppConstants.GIFT_FRAGMENT);
    }

    @Override
    public void onAddProductDialogDismiss() {
        refreshFragment(AppConstants.PRODUCT_FRAGMENT);
    }

    @Override
    public void onAddPatchPatchDialogDismiss() {
        refreshFragment(AppConstants.PATCH_FRAGMENT);
    }

}
