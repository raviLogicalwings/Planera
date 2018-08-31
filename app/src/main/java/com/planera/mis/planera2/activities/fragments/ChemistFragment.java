package com.planera.mis.planera2.activities.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import com.planera.mis.planera2.activities.ActivityAddChemist;
import com.planera.mis.planera2.activities.ActivityAddDoctor;
import com.planera.mis.planera2.activities.ActivityUpdateChemist;
import com.planera.mis.planera2.activities.Retrofit.ApiClient;
import com.planera.mis.planera2.activities.Retrofit.ApiInterface;
import com.planera.mis.planera2.activities.adapters.ChemistListAdapter;
import com.planera.mis.planera2.activities.adapters.DoctorsListAdapter;
import com.planera.mis.planera2.activities.models.ChemistListResponse;
import com.planera.mis.planera2.activities.models.Chemists;
import com.planera.mis.planera2.activities.models.Doctors;
import com.planera.mis.planera2.activities.models.DoctorsListResponce;
import com.planera.mis.planera2.activities.models.MainResponse;
import com.planera.mis.planera2.activities.utils.AppConstants;
import java.util.List;
import java.util.logging.Level;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ChemistFragment extends BaseFragment{

    public static ChemistFragment instance;
    private View view;
    private ApiInterface apiInterface;
    private List<Chemists> chemistsList;
    private RecyclerView listViewChemist;
    private LinearLayout layoutNoData;
    private int selectedChemist;

    public ChemistFragment() {

    }

    public static ChemistFragment newInstance() {
        if(instance == null){
            instance = new ChemistFragment();
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
        view =  inflater.inflate(R.layout.fragment_chemist, container, false);
        initUi();
        initData();
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        apiInterface = ApiClient.getInstance();
        if(token!=null){
            getDoctorsList(token);
        }
    }

    @Override
    protected void initUi() {
        super.initUi();
        listViewChemist = view.findViewById(R.id.list_chemists);
        layoutNoData = view.findViewById(R.id.layout_no_data);
    }




    public void getDoctorsList(String token){
        processDialog.showDialog(mContext, false);
        Call<ChemistListResponse> call = apiInterface.chemistList(token);
        call.enqueue(new Callback<ChemistListResponse>() {
            @Override
            public void onResponse(Call<ChemistListResponse> call, Response<ChemistListResponse> response) {
                processDialog.dismissDialog();
                Log.e(TAG, "onResponse: "+new Gson().toJson(response.body()));
                if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                    chemistsList = response.body().getData();
                    if (chemistsList!=null) {
                        listViewChemist.setVisibility(View.VISIBLE);
                        layoutNoData.setVisibility(View.GONE);
                        System.out.println(chemistsList.size());

                        initAdapter(chemistsList, listViewChemist);
                    }
                }
                else{
                    layoutNoData.setVisibility(View.VISIBLE);
                    listViewChemist.setVisibility(View.GONE);
                    Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<ChemistListResponse> call, Throwable t) {
                processDialog.dismissDialog();
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void initAdapter(List<Chemists> list, RecyclerView recyclerView){
        ChemistListAdapter adapter = new ChemistListAdapter(mContext, list, (view, position) -> {
            switch (view.getId()){
                case R.id.img_chemist_delete:
                    popupDialog(token, list.get(position).getChemistId());
                    break;

                case R.id.img_chemist_edit:
                    chemistDetailsForUpdate(position, list);

                    break;

            }
        });

        setAdapter(recyclerView, adapter);

    }


    public void chemistDetailsForUpdate(int pos, List<Chemists> chemistData){
        selectedChemist= chemistData.get(pos).getChemistId();
        Intent intentChemistCall = new Intent(mContext, ActivityUpdateChemist.class);
        intentChemistCall.putExtra(AppConstants.UPDATE_DOCTOR_KEY, selectedChemist);
        intentChemistCall.putExtra(AppConstants.FIRST_NAME, chemistData.get(pos).getFirstName());
        intentChemistCall.putExtra(AppConstants.MIDDLE_NAME, chemistData.get(pos).getMiddleName());
        intentChemistCall.putExtra(AppConstants.LAST_NAME, chemistData.get(pos).getLastName());
        intentChemistCall.putExtra(AppConstants.PHONE, chemistData.get(pos).getPhone());
        intentChemistCall.putExtra(AppConstants.EMAIL, chemistData.get(pos).getEmail());
        intentChemistCall.putExtra(AppConstants.COMPANY_NAME, chemistData.get(pos).getCompanyName());
        intentChemistCall.putExtra(AppConstants.SHOP_SIZE, chemistData.get(pos).getShopSize());
        intentChemistCall.putExtra(AppConstants.MONTHLY_VOLUME_POTENTIAL, chemistData.get(pos).getMonthlyVolumePotential());
        intentChemistCall.putExtra(AppConstants.ADDRESS1, chemistData.get(pos).getAddress1());
        intentChemistCall.putExtra(AppConstants.ADDRESS2, chemistData.get(pos).getAddress2());
        intentChemistCall.putExtra(AppConstants.ADDRESS3, chemistData.get(pos).getAddress3());
        intentChemistCall.putExtra(AppConstants.ADDRESS4, chemistData.get(pos).getAddress4());
        intentChemistCall.putExtra(AppConstants.DISTRICT, chemistData.get(pos).getDistrict());
        intentChemistCall.putExtra(AppConstants.STATE, chemistData.get(pos).getState());
        intentChemistCall.putExtra(AppConstants.CITY, chemistData.get(pos).getCity());
        intentChemistCall.putExtra(AppConstants.PINCODE, chemistData.get(pos).getPincode());
        intentChemistCall.putExtra(AppConstants.BILLING_EMAIL, chemistData.get(pos).getBillingEmail());
        intentChemistCall.putExtra(AppConstants.BILLING_PHONE1, chemistData.get(pos).getBillingPhone1());
        intentChemistCall.putExtra(AppConstants.BILLING_PHONE2, chemistData.get(pos).getBillingPhone2());
        intentChemistCall.putExtra(AppConstants.RATING, chemistData.get(pos).getBillingPhone2());

        intentChemistCall.putExtra(AppConstants.UPDATE_CHEMIST_KEY, selectedChemist);
        mContext.startActivity(intentChemistCall);

    }
    public void setAdapter(RecyclerView recyclerView, ChemistListAdapter adapter){
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


    public void popupDialog( String token, int ChemistId){
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();

        alertDialog.setMessage("Are you sure you want to delete this?");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (dialogInterface, i) -> {
            dialogInterface.cancel();
            deleteChemistApi(token, ChemistId );
        });


        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", (dialog, which) -> {
            dialog.cancel();
        });

        alertDialog.show();

    }



    public void deleteChemistApi(String token, int chemistId){
        processDialog.showDialog(mContext, false);
        Call<MainResponse> call = apiInterface.deleteChemist(token, chemistId);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                processDialog.dismissDialog();
                if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                    getFragmentManager().beginTransaction().detach(ChemistFragment.this).attach(ChemistFragment.this).commit();
                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                processDialog.dismissDialog();

                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
