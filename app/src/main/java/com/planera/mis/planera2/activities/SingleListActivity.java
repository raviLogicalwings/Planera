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
               else if (comingFragment == AppConstants.TERITORY_FRAGMENT){
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
                   startActivityForResult(intentAddDoctor, REQUEST_CODE_DOCTOR);
               }

               else if(comingFragment == AppConstants.CHEMIST_FRAGMENT){
                   Intent intentAddChemist = new Intent(SingleListActivity.this, ActivityAddChemist.class);
                   startActivityForResult(intentAddChemist, REQUEST_CODE_CHEMIST);
                }

                else if (comingFragment == AppConstants.PLAN_FRAGMENT){
                   Intent intentPlan = new Intent(SingleListActivity.this, ActivityCretePlan.class);
                   startActivityForResult(intentPlan, REQUEST_CODE_PLAN);
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
        loadFragment(comingFragment);
    }


    public void refreshFragment(int type){
        switch (type){
            case AppConstants.STATE_FRAGMENT:
                getSupportFragmentManager().beginTransaction().
                        detach(StateListFragment.newInstance()).
                        attach(StateListFragment.newInstance()).commitAllowingStateLoss();
                break;

            case AppConstants.PATCH_FRAGMENT:
                getSupportFragmentManager().beginTransaction().
                        detach(PatchListFragment.newInstance()).
                        attach(PatchListFragment.newInstance()).commitAllowingStateLoss();
                break;

            case AppConstants.TERITORY_FRAGMENT:
                getSupportFragmentManager().beginTransaction().
                        detach(TerritoryListFragment.newInstance()).
                        attach(TerritoryListFragment.newInstance()).commitAllowingStateLoss();
                break;

            case AppConstants.GIFT_FRAGMENT:
                getSupportFragmentManager().beginTransaction().
                        detach(GiftListFragment.newInstance()).
                        attach(GiftListFragment.newInstance()).commitAllowingStateLoss();
                break;

            case AppConstants.PRODUCT_FRAGMENT:
                getSupportFragmentManager().beginTransaction().
                        detach(ProductFragment.newInstance()).
                        attach(ProductFragment.newInstance()).commitAllowingStateLoss();
                break;

            case AppConstants.DOCTOR_FRAGMENT:
                getSupportFragmentManager().beginTransaction().
                        detach(DoctorsFragment.newInstance()).
                        attach(DoctorsFragment.newInstance()).commitAllowingStateLoss();
                break;

            case AppConstants.CHEMIST_FRAGMENT:
                getSupportFragmentManager().beginTransaction().
                        detach(ChemistFragment.newInstance()).
                        attach(ChemistFragment.newInstance()).commitAllowingStateLoss();

            case AppConstants.USER_FRAGMENT:
                getSupportFragmentManager().beginTransaction().
                        detach(UsersFragment.newInstance()).
                        attach(UsersFragment.newInstance()).commitAllowingStateLoss();
                break;

            case AppConstants.PLAN_FRAGMENT:
                getSupportFragmentManager().beginTransaction().
                        detach(PlansFragment.newInstance()).
                        attach(PlansFragment.newInstance()).commitAllowingStateLoss();
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
                fragment = StateListFragment.newInstance();
                getSupportActionBar().setTitle("States");
                break;

            case AppConstants.TERITORY_FRAGMENT:
                fragment = TerritoryListFragment.newInstance();
                getSupportActionBar().setTitle("Territory");
                break;

            case AppConstants.GIFT_FRAGMENT:
                fragment = GiftListFragment.newInstance();
                getSupportActionBar().setTitle("Gift");
                break;

            case AppConstants.PATCH_FRAGMENT:
                fragment = PatchListFragment.newInstance();
                getSupportActionBar().setTitle("Patch");
                break;


            case AppConstants.DOCTOR_FRAGMENT:
                fragment  = DoctorsFragment.newInstance();
                getSupportActionBar().setTitle("Doctors");
                break;

            case AppConstants.PRODUCT_FRAGMENT:
                fragment = ProductFragment.newInstance();
                getSupportActionBar().setTitle("Products");
                break;

            case AppConstants.CHEMIST_FRAGMENT:
                fragment = ChemistFragment.newInstance();
                getSupportActionBar().setTitle("Chemists");
                break;

            case AppConstants.USER_FRAGMENT:
                fragment = UsersFragment.newInstance();
                getSupportActionBar().setTitle("Users");
                break;

            case AppConstants.PLAN_FRAGMENT:
                fragment = PlansFragment.newInstance();
                getSupportActionBar().setTitle("Plans");
                break;
        }

      getSupportFragmentManager().beginTransaction().replace(R.id.containerSingle, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
    }

    @Override
    public void onClick(View view) {

    }


    @Override
    public void onDialogDismiss() {
        refreshFragment(AppConstants.STATE_FRAGMENT);
    }

    @Override
    public void onAddTerritoryDialogDismiss() {
        refreshFragment(AppConstants.TERITORY_FRAGMENT);
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
