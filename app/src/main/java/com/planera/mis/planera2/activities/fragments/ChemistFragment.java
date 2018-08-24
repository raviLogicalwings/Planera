package com.planera.mis.planera2.activities.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.Retrofit.ApiInterface;
import com.planera.mis.planera2.activities.models.GiftsData;

import java.util.List;

public class ChemistFragment extends BaseFragment{
    private View view;
    private ApiInterface apiInterface;
    private List<GiftsData> giftList;
    private RecyclerView listViewGifts;
    private LinearLayout layoutNoData;
    public static ChemistFragment instance;

    public static ChemistFragment newInstance() {
        if (instance==null){
            instance = new ChemistFragment();
        }
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chemist_list, container, false);
        initUi();
        initData();
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initUi() {
        super.initUi();
    }
}
