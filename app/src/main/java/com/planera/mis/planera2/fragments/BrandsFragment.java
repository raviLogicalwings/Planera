package com.planera.mis.planera2.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.planera.mis.planera2.R;
import com.planera.mis.planera2.Retrofit.ApiClient;
import com.planera.mis.planera2.Retrofit.ApiInterface;
import com.planera.mis.planera2.activities.ProductCategoryActivity;
import com.planera.mis.planera2.adapters.BrandsAdapter;
import com.planera.mis.planera2.models.Brands;
import com.planera.mis.planera2.models.BrandsListResponse;
import com.planera.mis.planera2.models.DataItem;
import com.planera.mis.planera2.models.InputOrders;
import com.planera.mis.planera2.models.MainResponse;
import com.planera.mis.planera2.utils.AppConstants;
import com.planera.mis.planera2.utils.InternetConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BrandsFragment extends BaseFragment implements ProductCategoryActivity.DataReceivedListener{
    public static final String TAG = BrandsFragment.class.getSimpleName();

    private List<Brands> listOfBrands;
    public List<String> brandLevelList;
    public List<InputOrders> orderList;

    private View view;
    private BrandsAdapter adapter;
    private RecyclerView brandsListView;
    private ApiInterface apiInterface;
    private DataItem dataItemForUpdate;


    private String token;
    protected String strPreviousInput;


    public BrandsFragment() {
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_brands, container, false);
        initUi();
        initData();
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((ProductCategoryActivity) Objects.requireNonNull(getActivity())).setDataReceivedListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        initInterestedList();
        apiInterface = ApiClient.getInstance();
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
    }

    public void apiAddInputBrands(String token){

//        Log.e("Inputs", new Gson().toJson(orders));
      processDialog.showDialog(mContext, false);
        Call<MainResponse> call = apiInterface.addInputProductList(token, adapter.getOrderListSelected());

        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(@NonNull Call<MainResponse> call, @NonNull Response<MainResponse> response) {
                processDialog.dismissDialog();
                assert response.body() != null;
                if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                    Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                }
                else {
                    Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MainResponse> call, @NonNull Throwable t) {
                processDialog.dismissDialog();
                Snackbar.make(rootView, t.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }


    public void getBrandsListApi(String token, int isBrand){
        Call<BrandsListResponse> call = apiInterface.brandsListApi(token, isBrand);
        call.enqueue(new Callback<BrandsListResponse>() {
            @Override
            public void onResponse(@NonNull Call<BrandsListResponse> call, @NonNull Response<BrandsListResponse> response) {

                assert response.body() != null;
                if (response.body().getStatusCode() == AppConstants.RESULT_OK) {
                    listOfBrands = response.body().getData();
                    initAdapter(listOfBrands, brandsListView, brandLevelList);
                } else {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BrandsListResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void initAdapter(List<Brands> brandsList, RecyclerView recyclerView, List<String> interestedLevel){

        adapter = new BrandsAdapter(mContext,brandsList, interestedLevel,dataItemForUpdate );

        setAdapter(recyclerView, adapter);

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setAdapter(RecyclerView recyclerView, BrandsAdapter adapter){
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


    @Override
    public void onReceived() {
        if (InternetConnection.isNetworkAvailable(mContext)){
            apiAddInputBrands(token);
        }
        else
        {
            Snackbar.make(rootView, R.string.no_internet, Snackbar.LENGTH_LONG).show();
        }
    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
