package com.planera.mis.planera2.activities.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
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
import com.planera.mis.planera2.activities.FragmentDialog.editDialogs.EditStateDialog;
import com.planera.mis.planera2.activities.Retrofit.ApiClient;
import com.planera.mis.planera2.activities.Retrofit.ApiInterface;
import com.planera.mis.planera2.activities.adapters.StateAdapter;
import com.planera.mis.planera2.activities.models.MainResponse;
import com.planera.mis.planera2.activities.models.Plans;
import com.planera.mis.planera2.activities.models.PlansListResponce;
import com.planera.mis.planera2.activities.models.StateListResponse;
import com.planera.mis.planera2.activities.models.States;
import com.planera.mis.planera2.activities.utils.AppConstants;

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
        listViewPlans = view.findViewById(R.id.list_state);
        layoutNoData = view.findViewById(R.id.layout_no_data);
    }


    public void deleteStateApi(String token, int stateId){
        processDialog.showDialog(mContext, false);
        Call<MainResponse> call = apiInterface.deleteState(token, stateId);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                processDialog.dismissDialog();
                if (response!= null){
                    if (response.body().getStatusCode()== AppConstants.RESULT_OK){
                        Toast.makeText(mContext, response.body().getMessage(),Toast.LENGTH_LONG).show();
                        manager.beginTransaction().detach(PlansFragment.this).attach(PlansFragment.this).commit();
                    }
                    else{
                        Toast.makeText(mContext, response.body().getMessage(),Toast.LENGTH_LONG).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                processDialog.dismissDialog();
                Toast.makeText(mContext, t.getMessage(),Toast.LENGTH_LONG).show();

            }
        });

    }



    public void getStatesList(String token) {
        processDialog.showDialog(mContext, false);
        Call<PlansListResponce> call = apiInterface.planList(token);
        call.enqueue(new Callback<PlansListResponce>() {
            @Override
            public void onResponse(Call<PlansListResponce> call, Response<PlansListResponce> response) {
                processDialog.dismissDialog();
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response != null) {
                    if (response.body().getStatusCode() == AppConstants.RESULT_OK) {
                        plansList = response.body().getData();
                        System.out.println(plansList.size());
                        listViewPlans.setVisibility(View.VISIBLE);
                        layoutNoData.setVisibility(View.GONE);
//                        initAdapter(plansList, listViewPlans);
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

//    public void initAdapter(List<Plans> plansData, RecyclerView recyclerView) {
//
//        StateAdapter adapter = new StateAdapter(getContext(), plansData, (postion, view) -> {
//            switch (view.getId()) {
//                case R.id.img_delete:
////                    Log.e(TAG, "Clicked Item of State List: "+statesList.get(postion).getStateId() );
//                    popupDialog(token, plansData.get(postion).getPlanId());
////                    deleteStateApi(token, statesList.get(postion).getStateId());
//                    break;
//
//                case R.id.img_edit:
//                    EditStateDialog editStateDialog = new EditStateDialog();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("item", plansData.get(postion).get);
////                    bundle.putInt("id", statesList.get(postion).getStateId());
////                    editStateDialog.setArguments(bundle);
//                    editStateDialog.setTargetFragment(this, 0);
//                    editStateDialog.show(getFragmentManager(), "Edit State");
//                    break;
//            }
//        });
//        setAdapter(recyclerView, adapter);
//
//    }


    public void setAdapter(RecyclerView recyclerView, StateAdapter adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public void onDetach() {
        super.onDetach();

    }

    public void popupDialog( String token, int stateId){
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();

        alertDialog.setMessage("Are you sure you want to delete this?");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (dialogInterface, i) -> {
            dialogInterface.cancel();
            deleteStateApi(token, stateId );
        });


        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", (dialog, which) -> {
            dialog.cancel();
        });

        alertDialog.show();

    }




}
