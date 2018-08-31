package com.planera.mis.planera2.activities.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.SingleListActivity;
import com.planera.mis.planera2.activities.utils.AppConstants;


public class AdminAccessFragment extends BaseFragment implements View.OnClickListener{
    private View view;
    private CardView cardState;
    private CardView cardTeritory;
    private CardView cardPatch;
    private CardView cardGift;
    private CardView cardProduct;
    private CardView cardDoctor;
    private CardView cardChemist;
    private CardView cardUser;
    private CardView cardPlans;
    public static AdminAccessFragment instance;


    public static AdminAccessFragment getInstance(){
        if(instance== null){
            instance = new AdminAccessFragment();
        }
        return instance;
    }



    public AdminAccessFragment() {
        // Required empty public constructor
    }



    public static AdminAccessFragment newInstance(String param1, String param2) {
        AdminAccessFragment fragment = new AdminAccessFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=  inflater.inflate(R.layout.fragment_admin_access, container, false);
        initUi();
        initData();
        return view;
    }


    @Override
    protected void initUi() {
        super.initUi();

        cardState = view.findViewById(R.id.card_state);
        cardTeritory = view.findViewById(R.id.card_teritory);
        cardPatch = view.findViewById(R.id.card_patch);
        cardGift = view.findViewById(R.id.card_gift);
        cardProduct = view.findViewById(R.id.card_product);
        cardDoctor = view.findViewById(R.id.card_doctor);
        cardChemist = view.findViewById(R.id.card_chemist);
        cardUser = view.findViewById(R.id.card_user);
        cardPlans = view.findViewById(R.id.card_plans);

        cardState.setOnClickListener(this);
        cardTeritory.setOnClickListener(this);
        cardPatch.setOnClickListener(this);
        cardGift.setOnClickListener(this);
        cardProduct.setOnClickListener(this);
        cardDoctor.setOnClickListener(this);
        cardChemist.setOnClickListener(this);
        cardUser.setOnClickListener(this);
        cardPlans.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.card_product:
                callIntent(AppConstants.KEY_TOUCHED_FRAGMENT, AppConstants.PRODUCT_FRAGMENT);
                break;
            case R.id.card_doctor:
                callIntent(AppConstants.KEY_TOUCHED_FRAGMENT, AppConstants.DOCTOR_FRAGMENT);
                break;
            case R.id.card_patch:
                callIntent(AppConstants.KEY_TOUCHED_FRAGMENT, AppConstants.PATCH_FRAGMENT);
                break;
            case R.id.card_gift:
                callIntent(AppConstants.KEY_TOUCHED_FRAGMENT, AppConstants.GIFT_FRAGMENT);
                break;
            case R.id.card_state:
              callIntent(AppConstants.KEY_TOUCHED_FRAGMENT, AppConstants.STATE_FRAGMENT);
                break;
            case R.id.card_teritory:
                callIntent(AppConstants.KEY_TOUCHED_FRAGMENT, AppConstants.TERITORY_FRAGMENT);
                break;

            case R.id.card_chemist:
                callIntent(AppConstants.KEY_TOUCHED_FRAGMENT, AppConstants.CHEMIST_FRAGMENT);
                break;

            case R.id.card_user:
                callIntent(AppConstants.KEY_TOUCHED_FRAGMENT, AppConstants.USER_FRAGMENT);
                break;

            case R.id.card_plans:
                callIntent(AppConstants.KEY_TOUCHED_FRAGMENT, AppConstants.PLAN_FRAGMENT);
        }

    }


    public void callIntent(String key, int fragment){
        Intent intent = new Intent(getContext(), SingleListActivity.class);
        intent.putExtra(key, fragment);
        getActivity().startActivity(intent);
    }
}
