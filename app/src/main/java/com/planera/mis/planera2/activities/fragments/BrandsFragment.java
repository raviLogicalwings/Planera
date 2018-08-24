package com.planera.mis.planera2.activities.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.Retrofit.ApiClient;
import com.planera.mis.planera2.activities.Retrofit.ApiInterface;
import com.planera.mis.planera2.activities.adapters.BrandsAdapter;
import com.planera.mis.planera2.activities.models.Brands;
import com.planera.mis.planera2.activities.models.BrandsListResponse;
import com.planera.mis.planera2.activities.utils.AppConstants;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BrandsFragment extends BaseFragment {
    private View view;
    private BrandsAdapter adapter;
    private RecyclerView brandsListView;
    private ApiInterface apiInterface;
    private List<Brands> listOfBrands;
    private String token;



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
    protected void initData() {
        super.initData();
        apiInterface = ApiClient.getInstance();
        token = connector.getString(AppConstants.TOKEN);
        if (token!=null){
            getBrandsListApi(token);
        }

    }

    @Override
    protected void initUi() {
        super.initUi();
        brandsListView = view.findViewById(R.id.recycle_view_brand);
    }

    public void getBrandsListApi(String token){
        Call<BrandsListResponse> call = apiInterface.brandsListApi(token);
        call.enqueue(new Callback<BrandsListResponse>() {
            @Override
            public void onResponse(Call<BrandsListResponse> call, Response<BrandsListResponse> response) {
                if (response!= null){
                    if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                        listOfBrands = response.body().getData();
                        initAdapter(listOfBrands, adapter, brandsListView);
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


    public void initAdapter(List<Brands> brandsList, BrandsAdapter adapter, RecyclerView recyclerView){
        adapter = new BrandsAdapter(getContext(), brandsList);
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



    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
