package com.planera.mis.planera2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.FragmentDialog.addDialogs.AddGiftDialog;
import com.planera.mis.planera2.FragmentDialog.addDialogs.AddPatchDialog;
import com.planera.mis.planera2.FragmentDialog.addDialogs.AddProductDialog;
import com.planera.mis.planera2.FragmentDialog.addDialogs.AddStateDialog;
import com.planera.mis.planera2.FragmentDialog.addDialogs.AddTerritoryDialog;
import com.planera.mis.planera2.fragments.ChemistFragment;
import com.planera.mis.planera2.fragments.DoctorsFragment;
import com.planera.mis.planera2.fragments.GiftListFragment;
import com.planera.mis.planera2.fragments.PatchListFragment;
import com.planera.mis.planera2.fragments.PlansFragment;
import com.planera.mis.planera2.fragments.ProductFragment;
import com.planera.mis.planera2.fragments.StateListFragment;
import com.planera.mis.planera2.fragments.TerritoryListFragment;
import com.planera.mis.planera2.fragments.UsersFragment;
import com.planera.mis.planera2.utils.AppConstants;

import java.util.Objects;


public class SingleListActivity extends BaseActivity implements View.OnClickListener,
        AddProductDialog.OnAddProductDialogDismissListener,
        AddStateDialog.OnStateDialogDismissListener,
        AddGiftDialog.OnAddGiftDialogDismissListener,
        AddPatchDialog.OnAddPatchDialogDismissListener, AddTerritoryDialog.OnAddTerritoryDialogDismissListener{
    int comingFragment;
    private Fragment fragment;
    private MenuInflater inflater;
    private MenuItem menuItem;
    private Menu menu;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_list);
        initUi();
        initData();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
       inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.actiion_add:
                menuItem = item;
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
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    @Override
    public void initData() {
        super.initData();
        Intent getData = getIntent();
        comingFragment = getData.getIntExtra(AppConstants.KEY_TOUCHED_FRAGMENT, 0);
        if (comingFragment!= 0) {
            loadFragment(comingFragment);
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


    public void loadFragment(int type){
        switch (type){

            case AppConstants.STATE_FRAGMENT:
                fragment =  new StateListFragment();
                Objects.requireNonNull(getSupportActionBar()).setTitle("States");
                break;

            case AppConstants.TERRITORY_FRAGMENT:
                fragment = new TerritoryListFragment();
                Objects.requireNonNull(getSupportActionBar()).setTitle("Territory");
                break;

            case AppConstants.GIFT_FRAGMENT:
                fragment = new GiftListFragment();
                Objects.requireNonNull(getSupportActionBar()).setTitle("Gift");
                break;

            case AppConstants.PATCH_FRAGMENT:
                fragment = new PatchListFragment();
                Objects.requireNonNull(getSupportActionBar()).setTitle("Patch");
                break;


            case AppConstants.DOCTOR_FRAGMENT:
                fragment  = new DoctorsFragment();
                Objects.requireNonNull(getSupportActionBar()).setTitle("Doctors");
                break;

            case AppConstants.PRODUCT_FRAGMENT:
                fragment = new ProductFragment();
                Objects.requireNonNull(getSupportActionBar()).setTitle("Products");
                break;

            case AppConstants.CHEMIST_FRAGMENT:
                fragment = new ChemistFragment();
                Objects.requireNonNull(getSupportActionBar()).setTitle("Chemists");
                break;

            case AppConstants.USER_FRAGMENT:

                fragment = new UsersFragment();
                Objects.requireNonNull(getSupportActionBar()).setTitle("Users");

                break;

            case AppConstants.PLAN_FRAGMENT:
                fragment = new PlansFragment();
                Objects.requireNonNull(getSupportActionBar()).setTitle("Plans");

                break;
        }

      getSupportFragmentManager().beginTransaction().replace(R.id.containerSingle, fragment).commit();
    }

    @Override
    public void onClick(View view) {

    }


    @Override
    public void onDialogDismiss() {
        loadFragment(AppConstants.STATE_FRAGMENT);
    }

    @Override
    public void onAddTerritoryDialogDismiss() {
        loadFragment(AppConstants.TERRITORY_FRAGMENT);
    }

    @Override
    public void onAddGiftDialogDismiss() {
        loadFragment(AppConstants.GIFT_FRAGMENT);
    }

    @Override
    public void onAddProductDialogDismiss() {
        loadFragment(AppConstants.PRODUCT_FRAGMENT);
    }

    @Override
    public void onAddPatchPatchDialogDismiss() {
        loadFragment(AppConstants.PATCH_FRAGMENT);
    }

}
