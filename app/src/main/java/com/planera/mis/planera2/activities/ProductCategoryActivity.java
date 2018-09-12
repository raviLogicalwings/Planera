package com.planera.mis.planera2.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.adapters.ChemistTabsPagerAdapter;
import com.planera.mis.planera2.activities.adapters.DoctorTabsPagerAdapter;
import com.planera.mis.planera2.activities.controller.DataController;
import com.planera.mis.planera2.activities.fragments.BrandsFragment;
import com.planera.mis.planera2.activities.fragments.GiftFragment;
import com.planera.mis.planera2.activities.models.InputGiftResponce;
import com.planera.mis.planera2.activities.models.InputOrders;
import com.planera.mis.planera2.activities.models.MainResponse;
import com.planera.mis.planera2.activities.utils.AppConstants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductCategoryActivity extends BaseActivity implements View.OnClickListener{
    private DoctorTabsPagerAdapter doctorTabsPagerAdapter;
    private ChemistTabsPagerAdapter chemistTabsPagerAdapter;
    private FragmentManager fragmentManager;
    private TabLayout tabLayout;
    private ViewPager pager;
    private Toolbar toolbarProduct;
    private Button buttonConfirm;
    DataReceivedListener listener;
    private boolean isDoctor;
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_category);
        initData();
        initUi();
    }

    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        isDoctor = intent.getBooleanExtra(AppConstants.KEY_ROLE, true);
        fragmentManager = getSupportFragmentManager();

    }

    @Override
    public void initUi() {
        super.initUi();

        toolbarProduct = findViewById(R.id.toolbarProduct);
        buttonConfirm = findViewById(R.id.button_confirm_product);
        tabLayout = findViewById(R.id.tabLayout);
        pager = findViewById(R.id.swiper);
        setSupportActionBar(toolbarProduct);
        toolbarProduct.setNavigationIcon(R.drawable.back_arrow_whit);

        toolbarProduct.setNavigationOnClickListener(v -> onBackPressed());
        buttonConfirm.setOnClickListener(this);

        if (isDoctor) {
            doctorTabsPagerAdapter = new DoctorTabsPagerAdapter(fragmentManager, ProductCategoryActivity.this);
            pager.setAdapter(doctorTabsPagerAdapter);
        }
        else{
            chemistTabsPagerAdapter = new ChemistTabsPagerAdapter(fragmentManager, ProductCategoryActivity.this);
            pager.setAdapter(chemistTabsPagerAdapter);
        }

        tabLayout.setupWithViewPager(pager);

    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_confirm_product:
               if (DataController.getmInstance().getInputGiftList()!= null){
                   addInputgiftApi(token);
               }
               if(DataController.getmInstance().getOrderListSelected()!= null){
                   apiAddInputBrands(token, DataController.getmInstance().getOrderListSelected());
               }
                if(DataController.getmInstance().getOrderPODList()!= null){
                    apiAddInputBrands(token, DataController.getmInstance().getOrderPODList());
                }

                break;
        }
    }

    public void callAllFragmentApi(){

        BrandsFragment brandsFragment = new BrandsFragment();
        brandsFragment.apiAddInputBrands(token);
        GiftFragment giftFragment  = new GiftFragment();
        giftFragment.addInputgiftApi(token);

    }

    public void addInputgiftApi(String token){
        Log.e("AddInputGifts : " , new Gson().toJson(DataController.getmInstance().getInputGiftList()));
        processDialog.showDialog(ProductCategoryActivity.this, false);
        Call<InputGiftResponce> call = apiInterface.addInputGift(token, DataController.getmInstance().getInputGiftList());
        call.enqueue(new Callback<InputGiftResponce>() {
            @Override
            public void onResponse(Call<InputGiftResponce> call, Response<InputGiftResponce> response) {
                processDialog.dismissDialog();
                if (response.isSuccessful()){
                    if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                        Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                    else{
                        Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<InputGiftResponce> call, Throwable t) {
                processDialog.dismissDialog();
                Snackbar.make(rootView, t.getMessage(), Snackbar.LENGTH_LONG).show();

            }
        });
    }


    public void apiAddInputBrands(String token, List<InputOrders> inputOrders){


        Log.e("Inputs", new Gson().toJson(DataController.getmInstance().getOrderListSelected()));
        processDialog.showDialog(ProductCategoryActivity.this, false);
        Call<MainResponse> call = apiInterface.addInputProductList(token, inputOrders);

        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                processDialog.dismissDialog();
                if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                    Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                }
                else {
                    Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                processDialog.dismissDialog();
                Snackbar.make(rootView, t.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }






    public void setDataReceivedListener(DataReceivedListener listener) {
        this.listener = listener;
    }

    public interface DataReceivedListener {
        void onReceived();
    }


}
