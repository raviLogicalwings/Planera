package com.planera.mis.planera2.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
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
import com.planera.mis.planera2.activities.ProductCategoryActivity;
import com.planera.mis.planera2.adapters.PODAdapter;
import com.planera.mis.planera2.models.Brands;
import com.planera.mis.planera2.models.BrandsListResponse;
import com.planera.mis.planera2.models.DataItem;
import com.planera.mis.planera2.models.InputOrders;
import com.planera.mis.planera2.models.MainResponse;
import com.planera.mis.planera2.utils.AppConstants;
import com.planera.mis.planera2.utils.InternetConnection;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PODFragment extends BaseFragment implements ProductCategoryActivity.DataReceivedListener{

    private RecyclerView recycleViewPob;
    private View view;
    private List<Brands> productList;
    public static final int NOT_BRAND = 0;
    public static final String TAG = "PODFragment";
    private InputOrders orders;
    private List<InputOrders> ordersList;
    private String strPreviousInput;
    private DataItem dataItemForUpdate;

    public PODFragment() {
    }


    public static PODFragment newInstance(String param1, String param2) {
        PODFragment fragment = new PODFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            Bundle bundle = getArguments();
            strPreviousInput = bundle.getString(AppConstants.PASS_UPDATE_INPUT);
            dataItemForUpdate = new Gson().fromJson(strPreviousInput, DataItem.class);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.fragment_pod, container, false);
        initUi();
        initData();
        return view;
    }

    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((ProductCategoryActivity) getActivity()).setDataReceivedListener(this);
    }

    @Override
    protected void initUi() {
        super.initUi();
        recycleViewPob = view.findViewById(R.id.recycle_view_pob);
    }

    @Override
    protected void initData() {
        super.initData();
        if(InternetConnection.isNetworkAvailable(mContext)){
            getAllProducts(token, NOT_BRAND);
            orders = new InputOrders();
            ordersList = new ArrayList<>();
        }
        else
        {
            Snackbar.make(rootView, R.string.no_internet, Snackbar.LENGTH_LONG).show();
        }
    }

    public void setAdapter(RecyclerView recyclerView, PODAdapter adapter){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }



    // Api for get all Products
    public void getAllProducts(String token , int isBrand){
        processDialog.showDialog(mContext, false);
        Call<BrandsListResponse> call = apiInterface.brandsListApi(token, isBrand);
            call.enqueue(new Callback<BrandsListResponse>() {
                @Override
                public void onResponse(Call<BrandsListResponse> call, Response<BrandsListResponse> response) {
//                    Log.e(TAG, "getAllProducts: "+new Gson().toJson(response.body()));
                    processDialog.dismissDialog();
                    if (response.isSuccessful()){
                        if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                            if (response.body().getData()  != null) {
                                productList = response.body().getData();
                                initAdapter(productList, recycleViewPob);
                            }
                            else{
                                Snackbar.make(rootView, "POB List not available", Snackbar.LENGTH_LONG).show();
                            }

                        }
                        else{
                            Snackbar.make(rootView, response.body().getMessage() , Snackbar.LENGTH_LONG).show();
                        }
                    }

                }

                @Override
                public void onFailure(Call<BrandsListResponse> call, Throwable t) {
                    processDialog.dismissDialog();
                    Snackbar.make(rootView, t.getMessage(), BaseTransientBottomBar.LENGTH_LONG).show();

                }
            });
    }



    //Init and Setup RecyclerView Adapter
    public void initAdapter(List<Brands> productData, RecyclerView recyclerView){

        PODAdapter adapter = new PODAdapter(getContext(), productData, (holder, pos) -> {
            if (holder.editPodProductValue.getText()!= null) {
                orders.setInputId(productList.get(pos).getProductId()+"");
                orders.setQuantity(holder.editPodProductValue.getText().toString());
                ordersList.add(orders);

            }
        }, dataItemForUpdate);
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

    public void uploadProduct(String token, List<InputOrders> ordersList){
       processDialog.showDialog(mContext, false);
       Call<MainResponse> call = apiInterface.addInputProductList(token, ordersList);
       call.enqueue(new Callback<MainResponse>() {
           @Override
           public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
               processDialog.dismissDialog();
               if (response.isSuccessful()){
                   if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                       Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                   }
                   else{
                       Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                   }
               }
           }

           @Override
           public void onFailure(Call<MainResponse> call, Throwable t) {
               processDialog.dismissDialog();
               Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_LONG).show();

           }
       });
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onReceived() {
        if (InternetConnection.isNetworkAvailable(mContext))
        {
            uploadProduct(token, ordersList);
        }
        else
        {
            Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
        }
    }
}
