package com.planera.mis.planera2.activities;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.adapters.TabsPagerAdapter;

public class ProductCategoryActivity extends BaseActivity {
    private TabsPagerAdapter pagerAdapter;
    private FragmentManager fragmentManager;
    private TabLayout tabLayout;
    private ViewPager pager;
    private Toolbar toolbarProduct;

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
        fragmentManager = getSupportFragmentManager();
        pagerAdapter = new TabsPagerAdapter( fragmentManager, ProductCategoryActivity.this);
    }

    @Override
    public void initUi() {
        super.initUi();
        toolbarProduct = findViewById(R.id.toolbarProduct);
        tabLayout = findViewById(R.id.tabLayout);
        pager = findViewById(R.id.swiper);
        setSupportActionBar(toolbarProduct);
        toolbarProduct.setNavigationIcon(R.drawable.icon_forward_arrow);
        pager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(pager);
    }
}
