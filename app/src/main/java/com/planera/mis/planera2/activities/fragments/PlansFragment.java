package com.planera.mis.planera2.activities.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.ActivityUpdatePlan;
import com.planera.mis.planera2.activities.Retrofit.ApiClient;
import com.planera.mis.planera2.activities.Retrofit.ApiInterface;
import com.planera.mis.planera2.activities.adapters.PlanListAdapter;
import com.planera.mis.planera2.activities.models.MainResponse;
import com.planera.mis.planera2.activities.models.Plans;
import com.planera.mis.planera2.activities.models.PlansListResponce;
import com.planera.mis.planera2.activities.utils.AppConstants;
import com.planera.mis.planera2.activities.utils.InternetConnection;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class PlansFragment extends BaseFragment{
    public static PlansFragment instance;
    private View view;
    private ApiInterface apiInterface;
    private List<Plans> plansList = null;
    private RecyclerView listViewPlans;
    private LinearLayout layoutNoData;
    private int selectedPlan;


    public PlansFragment() {

    }

    public static Fragment newInstance() {
        if (instance == null) {
            instance = new PlansFragment();
        }

        return instance;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_plans_list, container, false);
        initUi();
        initData();
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        apiInterface = ApiClient.getInstance();
        if (token != null) {
            getStatesList(token);
        }
    }

    @Override
    protected void initUi() {
        super.initUi();
        listViewPlans = view.findViewById(R.id.list_plans);
        layoutNoData = view.findViewById(R.id.layout_no_data);
    }





    public void getStatesList(String token) {
        processDialog.showDialog(mContext, false);
        Call<PlansListResponce> call = apiInterface.planList(token);
        call.enqueue(new Callback<PlansListResponce>() {
            @Override
            public void onResponse(Call<PlansListResponce> call, Response<PlansListResponce> response) {
                processDialog.dismissDialog();
                if (response != null) {
                    if (response.body().getStatusCode() == AppConstants.RESULT_OK) {
                        plansList = response.body().getData();
                        System.out.println(plansList.size());
                        listViewPlans.setVisibility(View.VISIBLE);
                        layoutNoData.setVisibility(View.GONE);
                        initAdapter(plansList, listViewPlans);
                    } else {
                        listViewPlans.setVisibility(View.GONE);
                        layoutNoData.setVisibility(View.VISIBLE);
                    }


                }
            }

            @Override
            public void onFailure(Call<PlansListResponce> call, Throwable t) {
                processDialog.dismissDialog();
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void initAdapter(List<Plans> plansData, RecyclerView recyclerView) {

        PlanListAdapter adapter;
        adapter = new PlanListAdapter(getContext(), plansData, (postion, view) -> {
            switch (view.getId()) {
                case R.id.img_plan_delete:
                    if (InternetConnection.isNetworkAvailable(mContext)){
                        popupDialog(token, Integer.parseInt(plansData.get(postion).getPlanId()));
                    }
                    else{
                        Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
                    }
                    break;

                case R.id.img_plan_edit:
                    planDetailsForUpdate(postion, plansData);
                    break;
            }
        });
        setAdapter(recyclerView, adapter);

    }
    public void planDetailsForUpdate(int pos, List<Plans> plansData){
        selectedPlan= Integer.parseInt(plansData.get(pos).getPlanId());
        Intent intentPlanCall = new Intent(mContext, ActivityUpdatePlan.class);
        intentPlanCall.putExtra(AppConstants.UPDATE_PLAN_KEY, selectedPlan);
        intentPlanCall.putExtra(AppConstants.PATCH_ID, plansData.get(pos).getPatchId());
        intentPlanCall.putExtra(AppConstants.DOCTOR_ID, plansData.get(pos).getDoctorId());
        intentPlanCall.putExtra(AppConstants.CHEMIST_ID, plansData.get(pos).getChemistsId());
        intentPlanCall.putExtra(AppConstants.USER_ID, plansData.get(pos).getUserId());
        intentPlanCall.putExtra(AppConstants.CALLS, plansData.get(pos).getCalls());
        intentPlanCall.putExtra(AppConstants.REMARK, plansData.get(pos).getRemark());
        intentPlanCall.putExtra(AppConstants.MONTH, plansData.get(pos).getMonth());
        intentPlanCall.putExtra(AppConstants.YEAR, plansData.get(pos).getYear());
        intentPlanCall.putExtra(AppConstants.STATUS, plansData.get(pos).getStatus());
        mContext.startActivity(intentPlanCall);

    }

    public void setAdapter(RecyclerView recyclerView, PlanListAdapter adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void deletePlanApi(String token , int planId){
        processDialog.showDialog(mContext, false);
        Call<MainResponse> call = apiInterface.deletePlan(token, planId);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                processDialog.dismissDialog();
                Log.e(TAG, "onResponse: "+ new Gson().toJson(response.body()));
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
            public void onFailure(Call<MainResponse> call, Throwable t) {
                processDialog.dismissDialog();
                Snackbar.make(rootView, t.getMessage(), Snackbar.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public void onDetach() {
        super.onDetach();

    }

    public void popupDialog( String token, int planId){
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();

        alertDialog.setMessage("Are you sure you want to delete this?");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (dialogInterface, i) -> {
            dialogInterface.cancel();
            deletePlanApi(token, planId );
        });


        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", (dialog, which) -> {
            dialog.cancel();
        });

        alertDialog.show();

    }




}
