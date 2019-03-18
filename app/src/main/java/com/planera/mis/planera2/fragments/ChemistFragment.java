package com.planera.mis.planera2.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.planera.mis.planera2.R;
import com.planera.mis.planera2.Retrofit.ApiClient;
import com.planera.mis.planera2.Retrofit.ApiInterface;
import com.planera.mis.planera2.activities.ActivityUpdateChemist;
import com.planera.mis.planera2.adapters.ChemistListAdapter;
import com.planera.mis.planera2.models.ChemistListResponse;
import com.planera.mis.planera2.models.Chemists;
import com.planera.mis.planera2.models.MainResponse;
import com.planera.mis.planera2.utils.AppConstants;
import com.planera.mis.planera2.utils.InternetConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ChemistFragment extends BaseFragment implements SearchView.OnQueryTextListener{

    public static ChemistFragment instance;
    private View view;
    private ApiInterface apiInterface;
    private List<Chemists> chemistsList;
    private RecyclerView listViewChemist;
    private LinearLayout visibleLayout;
    private SearchView searchViewChemist;
    private ChemistListAdapter adapter;
    private int selectedChemist;
    private LinearLayout linearNoData, linearNoInternet;
    private Button buttonRetry;
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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
            if (InternetConnection.isNetworkAvailable(mContext)){
                getChemistsList(token);
            }
            else{
                Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void initUi() {
        super.initUi();
        listViewChemist = view.findViewById(R.id.list_chemists);
        linearNoData = view.findViewById(R.id.linear_no_data);
        linearNoInternet = view.findViewById(R.id.linear_no_internet);
        buttonRetry = view.findViewById(R.id.button_retry);
        visibleLayout = view.findViewById(R.id.visible_layout_chemist);
        searchViewChemist = view.findViewById(R.id.search_view_chemist);
        searchViewChemist.setActivated(true);
        searchViewChemist.onActionViewExpanded();
        searchViewChemist.setIconified(false);
        searchViewChemist.clearFocus();
        searchViewChemist.setOnQueryTextListener(this);

        buttonRetry.setOnClickListener(v -> {
            if (getFragmentManager() != null) {
                getFragmentManager().beginTransaction().detach(ChemistFragment.this).attach(ChemistFragment.this).commit();
            }
        });
    }

    public void getChemistsList(String token){
        processDialog.showDialog(mContext, false);
        Call<ChemistListResponse> call = apiInterface.chemistList(token);
        call.enqueue(new Callback<ChemistListResponse>() {
            @Override
            public void onResponse(@NonNull Call<ChemistListResponse> call, @NonNull Response<ChemistListResponse> response) {
                processDialog.dismissDialog();
                Log.e(TAG, "onResponse: "+new Gson().toJson(response.body()));
                assert response.body() != null;
                if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                    chemistsList = response.body().getData();
                    if (chemistsList!=null) {
                        visibleLayout.setVisibility(View.VISIBLE);
                        System.out.println(chemistsList.size());
                        initAdapter(chemistsList, listViewChemist);
                    }
                }
                else{
                    linearNoData.setVisibility(View.VISIBLE);
                    visibleLayout.setVisibility(View.GONE);
                    Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ChemistListResponse> call, @NonNull Throwable t) {
                processDialog.dismissDialog();
                linearNoInternet.setVisibility(View.VISIBLE);
                buttonRetry.setVisibility(View.VISIBLE);
            }
        });

    }

    public void initAdapter(List<Chemists> list, RecyclerView recyclerView){

        adapter = new ChemistListAdapter(mContext, list, (view, chemists) -> {
            switch (view.getId()){
                case R.id.img_chemist_delete:
                    popupDialog(token, chemists.getChemistId());
                    break;

                case R.id.img_chemist_edit:
                    chemistDetailsForUpdate(chemists);
                    Objects.requireNonNull(ChemistFragment.this.getActivity()).finish();

                    break;

            }
        });

        setAdapter(recyclerView, adapter);

    }


    public void chemistDetailsForUpdate(Chemists chemists){
        selectedChemist= chemists.getChemistId();
        Intent intentChemistCall = new Intent(mContext, ActivityUpdateChemist.class);
        intentChemistCall.putExtra(AppConstants.UPDATE_DOCTOR_KEY, selectedChemist);
        intentChemistCall.putExtra(AppConstants.FIRST_NAME, chemists.getFirstName());
        intentChemistCall.putExtra(AppConstants.MIDDLE_NAME, chemists.getMiddleName());
        intentChemistCall.putExtra(AppConstants.LAST_NAME, chemists.getLastName());
        intentChemistCall.putExtra(AppConstants.PHONE, chemists.getPhone());
        intentChemistCall.putExtra(AppConstants.EMAIL, chemists.getEmail());
        intentChemistCall.putExtra(AppConstants.COMPANY_NAME, chemists.getCompanyName());
        intentChemistCall.putExtra(AppConstants.SHOP_SIZE, chemists.getShopSize());
        intentChemistCall.putExtra(AppConstants.PREFERRED_MEET_TIME, chemists.getPreferredMeetTime());
        intentChemistCall.putExtra(AppConstants.MONTHLY_VOLUME_POTENTIAL, chemists.getMonthlyVolumePotential());
        intentChemistCall.putExtra(AppConstants.PATCH_ID, chemists.getPatchId());
        intentChemistCall.putExtra(AppConstants.ADDRESS1, chemists.getAddress1());
        intentChemistCall.putExtra(AppConstants.ADDRESS2, chemists.getAddress2());
        intentChemistCall.putExtra(AppConstants.ADDRESS3, chemists.getAddress3());
        intentChemistCall.putExtra(AppConstants.ADDRESS4, chemists.getAddress4());
        intentChemistCall.putExtra(AppConstants.DISTRICT, chemists.getDistrict());
        intentChemistCall.putExtra(AppConstants.STATE, chemists.getState());
        intentChemistCall.putExtra(AppConstants.CITY, chemists.getCity());
        intentChemistCall.putExtra(AppConstants.PINCODE, chemists.getPincode());
        intentChemistCall.putExtra(AppConstants.BILLING_EMAIL, chemists.getBillingEmail());
        intentChemistCall.putExtra(AppConstants.BILLING_PHONE1, chemists.getBillingPhone1());
        intentChemistCall.putExtra(AppConstants.BILLING_PHONE2, chemists.getBillingPhone2());
        intentChemistCall.putExtra(AppConstants.RATING, chemists.getRating());

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

    void filter(String text){
        List<Chemists> temp = new ArrayList<>();
        for(Chemists d: chemistsList){
            if(d.getFirstName().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }
        }
        adapter.updateList(temp);
    }


    public void popupDialog( String token, int ChemistId){
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();

        alertDialog.setMessage("Are you sure you want to delete this?");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (dialogInterface, i) -> {
            dialogInterface.cancel();

            if (InternetConnection.isNetworkAvailable(mContext)){
                deleteChemistApi(token, ChemistId );
            }
            else
            {
                Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
            }
        });


        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", (dialog, which) -> dialog.cancel());

        alertDialog.show();

    }



    public void deleteChemistApi(String token, int chemistId){
        processDialog.showDialog(mContext, false);
        Call<MainResponse> call = apiInterface.deleteChemist(token, chemistId);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(@NonNull Call<MainResponse> call, @NonNull Response<MainResponse> response) {
                processDialog.dismissDialog();
                assert response.body() != null;
                if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                    if (InternetConnection.isNetworkAvailable(mContext)){
                        getChemistsList(token);
                    }
                    else{
                        Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
                    }
                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MainResponse> call, @NonNull Throwable t) {
                processDialog.dismissDialog();

                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        filter(newText.trim());
        return false;
    }
}
