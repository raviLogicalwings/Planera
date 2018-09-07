package com.planera.mis.planera2.activities.fragments;

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

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.ProductCategoryActivity;
import com.planera.mis.planera2.activities.Retrofit.ApiClient;
import com.planera.mis.planera2.activities.Retrofit.ApiInterface;
import com.planera.mis.planera2.activities.adapters.BrandsAdapter;
import com.planera.mis.planera2.activities.models.Brands;
import com.planera.mis.planera2.activities.models.BrandsListResponse;
import com.planera.mis.planera2.activities.models.MainResponse;
import com.planera.mis.planera2.activities.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BrandsFragment extends BaseFragment implements ProductCategoryActivity.DataReceivedListener{
    private View view;
    public static final String TAG = "BrandsFragment.TAG";
    private BrandsAdapter adapter;
    private RecyclerView brandsListView;
    private ApiInterface apiInterface;
    private List<Brands> listOfBrands;
    private String token;
    public List<String> brandLevelList;
    String interestedLavelStr;
    int productId;
    List<Integer> itemsPositions;
    private List<String> bindInterestandPositionTogether;





    public BrandsFragment() {
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
        ((ProductCategoryActivity) getActivity()).setDataReceivedListener( this);
    }

    @Override
    protected void initData() {
        super.initData();
        initIntrestedList();
        apiInterface = ApiClient.getInstance();
        itemsPositions = new ArrayList<>();
        bindInterestandPositionTogether = new ArrayList<>();
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

    public void apiAddInputBrands(String token , int productId, String interestedLevel){

        Log.e("Inputs", interestedLevel+" "+productId);
//        processDialog.showDialog(mContext, false);
        Call<MainResponse> call = apiInterface.addInputProduct(token, productId, interestedLevel);

        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                    Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                }
                else {
                    Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
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
                        initAdapter(listOfBrands, adapter, brandsListView, brandLevelList);
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


    public void initAdapter(List<Brands> brandsList, BrandsAdapter adapter, RecyclerView recyclerView, List<String> intrestedLevel){
        adapter = new BrandsAdapter(getContext(), brandsList, intrestedLevel, (pos, interestedLevel, productId) -> {
            if(interestedLevel != 0) {
                itemsPositions.add(pos);
                bindInterestandPositionTogether.add(pos + ":" + interestedLevel);
            }

        });
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

    public void setAdapter(RecyclerView recyclerView, BrandsAdapter adapter){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void initIntrestedList() {
        brandLevelList = new ArrayList<>();
        brandLevelList.add("");
        brandLevelList.add(mContext.getString(R.string.low));
        brandLevelList.add(mContext.getString(R.string.regular));
        brandLevelList.add(mContext.getString(R.string.superr));
    }

    @Override
    public void onReceived() {
        Toast.makeText(mContext, "On Click ", Toast.LENGTH_SHORT).show();
        if(itemsPositions!= null && bindInterestandPositionTogether!= null){
            for (int i = 0 ; i<itemsPositions.size(); i++){
                int currentPos = itemsPositions.get(i);


                        for (int j = 0; j < bindInterestandPositionTogether.size(); j++) {
                            String itemsWithPos = bindInterestandPositionTogether.get(j);
                            String arr[] = itemsWithPos.split(":");
                            int nowIntrestedPos = Integer.parseInt(arr[0]);
                                productId = listOfBrands.get(currentPos).getProductId();
                                interestedLavelStr = arr[1];
                                    apiAddInputBrands(token, productId, interestedLavelStr);

                            }

                        }
                    }
                }





    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
