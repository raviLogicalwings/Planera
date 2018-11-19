package com.planera.mis.planera2.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.planera.mis.planera2.R;
import com.planera.mis.planera2.Retrofit.ApiClient;
import com.planera.mis.planera2.Retrofit.ApiInterface;
import com.planera.mis.planera2.adapters.SampleListAdapter;
import com.planera.mis.planera2.models.Brands;
import com.planera.mis.planera2.models.BrandsListResponse;
import com.planera.mis.planera2.models.DataItem;
import com.planera.mis.planera2.models.InputOrders;
import com.planera.mis.planera2.models.MainResponse;
import com.planera.mis.planera2.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SampleFragment extends BaseFragment {
    private View view;
    public static final String TAG = "SampleFragment.TAG";
    private SampleListAdapter adapter;
    private RecyclerView brandsListView;
    private ApiInterface apiInterface;
    private List<Brands> listOfBrands;
    private String token;
    public List<String> brandLevelList;
    public List<InputOrders> orderList;
    private InputOrders orders;
    int productId;
    public static final int SAMPLE = 1;
    List<Integer> itemsPositions;
    private String strPreviousInput;
    private DataItem dataItemForUpdate;


    public SampleFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_brands, container, false);
        initUi();
        initData();
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void initData() {
        super.initData();
        initInterestedList();
        if (getArguments() != null){
            Bundle bundle = getArguments();
            strPreviousInput = bundle.getString(AppConstants.PASS_UPDATE_INPUT);
            dataItemForUpdate = new Gson().fromJson(strPreviousInput, DataItem.class);
        }
        apiInterface = ApiClient.getInstance();
        itemsPositions = new ArrayList<>();
        orders = new InputOrders();
        orderList = new ArrayList<>();
        token = connector.getString(AppConstants.TOKEN);
        if (token!=null){
            getBrandsListApi(token, AppConstants.BRAND);
        }

    }

    @Override
    protected void initUi() {
        super.initUi();
        brandsListView = view.findViewById(R.id.recycle_view_brand);
    }

    @Override
    public void onPause() {
        super.onPause();
//        Toast.makeText(mContext, "On Pause", Toast.LENGTH_LONG).show();
    }

    public void apiAddInputBrands(String token, List<InputOrders> sampleListSelected){


        Log.e("Inputs", new Gson().toJson(orders));
        processDialog.showDialog(mContext, false);
        Call<MainResponse> call = apiInterface.addInputProductList(token, sampleListSelected);

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


    public void getBrandsListApi(String token, int isBrand){
        Call<BrandsListResponse> call = apiInterface.brandsListApi(token, isBrand);
        call.enqueue(new Callback<BrandsListResponse>() {
            @Override
            public void onResponse(Call<BrandsListResponse> call, Response<BrandsListResponse> response) {
                if (response!= null){
                    if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                        listOfBrands = response.body().getData();
                        initAdapter(listOfBrands, brandsListView, brandLevelList, dataItemForUpdate);
                    }
                    else{
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<BrandsListResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void initAdapter(List<Brands> brandsList, RecyclerView recyclerView, List<String> intrestedLevel, DataItem dataItemForUpdate){

        adapter = new SampleListAdapter(mContext,brandsList, dataItemForUpdate);

        setAdapter(recyclerView, adapter);

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setAdapter(RecyclerView recyclerView, SampleListAdapter adapter){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void initInterestedList() {
        brandLevelList = new ArrayList<>();
        brandLevelList.add("");
        brandLevelList.add(mContext.getString(R.string.low));
        brandLevelList.add(mContext.getString(R.string.regular));
        brandLevelList.add(mContext.getString(R.string.superr));
    }




    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}

