package com.planera.mis.planera2.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.adapters.ChemistTabsPagerAdapter;
import com.planera.mis.planera2.activities.adapters.DoctorTabsPagerAdapter;
import com.planera.mis.planera2.activities.utils.AppConstants;

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
        toolbarProduct.setNavigationIcon(R.drawable.icon_forward_arrow);
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
                listener.onReceived();

                break;
        }
    }






    public void setDataReceivedListener(DataReceivedListener listener) {
        this.listener = listener;
    }

    public interface DataReceivedListener {
        void onReceived();
    }


}
