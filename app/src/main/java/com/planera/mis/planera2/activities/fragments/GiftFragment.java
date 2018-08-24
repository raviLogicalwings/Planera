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
import com.planera.mis.planera2.activities.adapters.GiftsAdapter;
import com.planera.mis.planera2.activities.models.GiftListResponse;
import com.planera.mis.planera2.activities.models.GiftsData;
import com.planera.mis.planera2.activities.utils.AppConstants;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GiftFragment extends BaseFragment {
    private RecyclerView recyclerViewGifts;
    private View view;
    private GiftsAdapter giftsAdapter;
    private ApiInterface apiInterface;
    private List<GiftsData> giftsDataList;
    private String token;



    public GiftFragment() {
    }

    public static GiftFragment newInstance(String param1, String param2) {
        GiftFragment fragment = new GiftFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       view =  inflater.inflate(R.layout.fragment_gift, container, false);
       initUi();
       initData();
       return view;
    }

    @Override
    protected void initData() {
        super.initData();
        apiInterface = ApiClient.getInstance();
        token = connector.getString(AppConstants.TOKEN);
        if(token!=null){
            getGiftListApi(token);
        }

    }

    @Override
    protected void initUi() {
        super.initUi();
       recyclerViewGifts = view.findViewById(R.id.recycler_gift_items);

    }

    public void getGiftListApi(String token){
        Call<GiftListResponse> call = apiInterface.giftListApi(token);
        call.enqueue(new Callback<GiftListResponse>() {
            @Override
            public void onResponse(Call<GiftListResponse> call, Response<GiftListResponse> response) {
                if (response!=null){
                    if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                        giftsDataList = response.body().getGiftsData();
                        if (giftsDataList!= null){
                            initAdapter(giftsDataList, giftsAdapter, recyclerViewGifts);
                        }
                    }
                    else{
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GiftListResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onButtonPressed(Uri uri) {
    }

    public void initAdapter(List<GiftsData> giftsData, GiftsAdapter adapter, RecyclerView recyclerView){
            adapter = new GiftsAdapter(getContext(), giftsData);
                setAdapter(recyclerView, adapter);

        }





    public void setAdapter(RecyclerView recyclerView, GiftsAdapter adapter){
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}

