package com.planera.mis.planera2.activities.fragments;

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
import com.planera.mis.planera2.activities.Retrofit.ApiClient;
import com.planera.mis.planera2.activities.Retrofit.ApiInterface;
import com.planera.mis.planera2.activities.adapters.GiftsAdapter;
import com.planera.mis.planera2.activities.models.DataItem;
import com.planera.mis.planera2.activities.models.GiftListResponse;
import com.planera.mis.planera2.activities.models.GiftsData;
import com.planera.mis.planera2.activities.models.InputGiftResponce;
import com.planera.mis.planera2.activities.utils.AppConstants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GiftFragment extends BaseFragment{
    private RecyclerView recyclerViewGifts;
    private View view;
    private GiftsAdapter giftsAdapter;
    private ApiInterface apiInterface;
    private List<GiftsData> giftsDataList;
    private String token;
    private String strPreviousInput;
    private DataItem dataItemForUpdate;


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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void initData() {
        super.initData();
        apiInterface = ApiClient.getInstance();
        token = connector.getString(AppConstants.TOKEN);
        if (getArguments() != null){
            Bundle bundle = getArguments();
            strPreviousInput = bundle.getString(AppConstants.PASS_UPDATE_INPUT);
            dataItemForUpdate = new Gson().fromJson(strPreviousInput, DataItem.class);
        }
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
                            initAdapter(giftsDataList, recyclerViewGifts, dataItemForUpdate);
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

    @Override
    public void onPause() {
        super.onPause();
//        Toast.makeText(mContext, "on Pause Gift", Toast.LENGTH_LONG).show();
    }

    public void addInputgiftApi(String token){
//        Log.e("AddInputGifts : " , new Gson().toJson(inputGifts));
        processDialog.showDialog(mContext, false);
        Call<InputGiftResponce> call = apiInterface.addInputGift(token, giftsAdapter.getInputGiftList());
        call.enqueue(new Callback<InputGiftResponce>() {
            @Override
            public void onResponse(Call<InputGiftResponce> call, Response<InputGiftResponce> response) {
                processDialog.dismissDialog();
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
            public void onFailure(Call<InputGiftResponce> call, Throwable t) {
                processDialog.dismissDialog();
                Snackbar.make(rootView, t.getMessage(), Snackbar.LENGTH_LONG).show();

            }
        });
    }

    public void onButtonPressed(Uri uri) {
    }

    public void initAdapter(List<GiftsData> giftsData, RecyclerView recyclerView, DataItem previousInputUpdate){
            giftsAdapter = new GiftsAdapter(getContext(), giftsData, previousInputUpdate);
                setAdapter(recyclerView, giftsAdapter);

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

//    @Override
//    public void onCheckInButtonClicked() {
//        Toast.makeText(mContext, "Check In Button Clicked : Gift Fragment", Toast.LENGTH_LONG).show();
//    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}

