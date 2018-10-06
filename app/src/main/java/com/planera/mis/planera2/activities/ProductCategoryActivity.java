package com.planera.mis.planera2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.adapters.BrandsAdapter;
import com.planera.mis.planera2.activities.adapters.ChemistTabsPagerAdapter;
import com.planera.mis.planera2.activities.adapters.DoctorTabsPagerAdapter;
import com.planera.mis.planera2.activities.adapters.GiftsAdapter;
import com.planera.mis.planera2.activities.adapters.PODAdapter;
import com.planera.mis.planera2.activities.adapters.SampleListAdapter;
import com.planera.mis.planera2.activities.controller.DataController;
import com.planera.mis.planera2.activities.models.Input;
import com.planera.mis.planera2.activities.models.InputGift;
import com.planera.mis.planera2.activities.models.InputGiftResponce;
import com.planera.mis.planera2.activities.models.InputOrders;
import com.planera.mis.planera2.activities.models.InputResponce;
import com.planera.mis.planera2.activities.models.MainResponse;
import com.planera.mis.planera2.activities.utils.AppConstants;
import com.planera.mis.planera2.activities.utils.InternetConnection;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductCategoryActivity extends BaseActivity implements View.OnClickListener{
    private DoctorTabsPagerAdapter doctorTabsPagerAdapter;
    private ChemistTabsPagerAdapter chemistTabsPagerAdapter;
    private FragmentManager fragmentManager;
    private TabLayout tabLayout;
    private ViewPager pager;
    private Toolbar toolbarProduct;
    private Button buttonConfirm;
    private Input input;
    DataReceivedListener listener;
    private boolean isDoctor;
    public static final String TAG = ProductCategoryActivity.class.getSimpleName();
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_category);
        initData();
        initUi();
    }

    @Override
    public void initData() {
        super.initData();
        isDoctor = connector.getBoolean(AppConstants.KEY_ROLE);
        fragmentManager = getSupportFragmentManager();
        String getInput = connector.getString(AppConstants.PASS_INPUT);
        input = new Gson().fromJson(getInput, Input.class);

    }

    @Override
    public void initUi() {
        super.initUi();

        toolbarProduct = findViewById(R.id.toolbarProduct);
        buttonConfirm = findViewById(R.id.button_confirm_product);
        tabLayout = findViewById(R.id.tabLayout);
        pager = findViewById(R.id.swiper);
        setSupportActionBar(toolbarProduct);
        toolbarProduct.setNavigationIcon(R.drawable.back_arrow_whit);

        toolbarProduct.setNavigationOnClickListener(v -> onBackPressed());
        buttonConfirm.setOnClickListener(this);

        if (isDoctor) {
            doctorTabsPagerAdapter = new DoctorTabsPagerAdapter(fragmentManager, ProductCategoryActivity.this);
            pager.setAdapter(doctorTabsPagerAdapter);
        }
        else{
            chemistTabsPagerAdapter = new ChemistTabsPagerAdapter(fragmentManager, ProductCategoryActivity.this);
            pager.setAdapter(chemistTabsPagerAdapter);
        }

        tabLayout.setupWithViewPager(pager);

    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_confirm_product:
                if (InternetConnection.isNetworkAvailable(ProductCategoryActivity.this)){
                    addInputApi(token, input);
                }
                else{
                    Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_SHORT).show();
                }

                break;
        }

    }


    public void addInputGiftApi(String token, List<InputGift> inputGifts){
        Log.e("AddInputGifts : " , new Gson().toJson(inputGifts));
        processDialog.showDialog(ProductCategoryActivity.this, false);
        Call<InputGiftResponce> call = apiInterface.addInputGift(token, inputGifts);
        call.enqueue(new Callback<InputGiftResponce>() {
            @Override
            public void onResponse(@NonNull Call<InputGiftResponce> call, Response<InputGiftResponce> response) {
                processDialog.dismissDialog();
                if (response.isSuccessful()){
                    if (response.body() != null) {
                        if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                            // Set Array Lis to null
                            //----------
                            new  GiftsAdapter().setInputGiftList(null);
                            DataController.getmInstance().setInputGiftList(null);
                            //---------------Call Samples Add Api after getting responce
                            List<InputOrders> inputSamples = new SampleListAdapter().getSampleListSelected();
                            if (inputSamples != null) {
                                if (inputSamples.size() > 0) {
                                    apiAddInputSamples(token, inputSamples);
                                }
                            }

                            Intent startMainActivity = new Intent(ProductCategoryActivity.this, MainActivity.class);
                            startActivity(startMainActivity);

                            Toast.makeText(ProductCategoryActivity.this, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(ProductCategoryActivity.this, response.body().getMessage(), Snackbar.LENGTH_LONG).show();

                        }
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


    public void apiAddInputSamples(String token, List<InputOrders> inputOrders){


        Log.e("Inputs", new Gson().toJson(DataController.getmInstance().getOrderListSelected()));
        processDialog.showDialog(ProductCategoryActivity.this, false);
        Call<MainResponse> call = apiInterface.addInputProductList(token, inputOrders);

        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                processDialog.dismissDialog();
                if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                    Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                    // Set Array Lis to null
                    //----------
                    new SampleListAdapter().setSampleListSelected(null);
                    DataController.getmInstance().setOrderListSelected(null);

                    // On Responce of Input Brand Calling add input Gift Api
                    //---------------
                    Intent startMainActivity = new Intent(ProductCategoryActivity.this, MainActivity.class);
                    startActivity(startMainActivity);

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

    public void apiAddInputBrands(String token, List<InputOrders> inputOrders){


        Log.e("Inputs", new Gson().toJson(DataController.getmInstance().getOrderListSelected()));
        processDialog.showDialog(ProductCategoryActivity.this, false);
        Call<MainResponse> call = apiInterface.addInputProductList(token, inputOrders);

        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                processDialog.dismissDialog();
                if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                    Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                    // Set Array Lis to null
                    //----------
                    new BrandsAdapter().setOrderListSelected(null);
                    DataController.getmInstance().setOrderListSelected(null);



                    // On Responce of Input Brand Calling add input Gift Api
                        List<InputGift> inputGifts = new GiftsAdapter().getInputGiftList();
                        if (inputGifts != null) {
                            if (inputGifts.size() > 0) {
                                addInputGiftApi(token, inputGifts);
                            }
                            else{
                                List<InputOrders> inputSamples = new SampleListAdapter().getSampleListSelected();
                                if (inputSamples.size()>0) {
                                    apiAddInputBrands(token, inputSamples);
                                }
                            }
                        }
                        else{
                            List<InputOrders> inputSamples = new SampleListAdapter().getSampleListSelected();
                            if (inputSamples != null) {
                                if (inputSamples.size() > 0) {
                                    apiAddInputBrands(token, inputSamples);
                                }
                            }
                        }
                    Intent startMainActivity = new Intent(ProductCategoryActivity.this, MainActivity.class);
                    startActivity(startMainActivity);
                    //---------------

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



    public void callingAllApi(){

        if (isDoctor) {

            if (new BrandsAdapter().getOrderListSelected() != null) {
                List<InputOrders> inputB = new BrandsAdapter().getOrderListSelected();
                if (inputB.size() > 0) {
                    apiAddInputBrands(token, inputB);
                } else {
                    if (new GiftsAdapter().getInputGiftList() != null) {
                        List<InputGift> inputGIfts = new GiftsAdapter().getInputGiftList();
                        if (inputGIfts.size() > 0) {
                            addInputGiftApi(token, inputGIfts);
                        } else {
                            if (new SampleListAdapter().getSampleListSelected() != null) {
                                List<InputOrders> inputSamples = new SampleListAdapter().getSampleListSelected();
                                if (inputSamples.size() > 0) {
                                    apiAddInputSamples(token, inputSamples);
                                }
                            }
                        }
                    }
                }
            } else {
                if (new GiftsAdapter().getInputGiftList() != null) {
                    List<InputGift> inputGifts = new GiftsAdapter().getInputGiftList();
                    if (inputGifts.size() > 0) {
                        addInputGiftApi(token, inputGifts);
                    } else {
                        if (new SampleListAdapter().getSampleListSelected() != null) {
                            List<InputOrders> inputSamples = new SampleListAdapter().getSampleListSelected();
                            if (inputSamples.size() > 0) {
                                apiAddInputSamples(token, inputSamples);
                            }
                        }
                    }
                } else {
                    if (new SampleListAdapter().getSampleListSelected() != null) {
                        List<InputOrders> inputSamples = new SampleListAdapter().getSampleListSelected();
                        if (inputSamples.size() > 0) {
                            apiAddInputSamples(token, inputSamples);
                        }
                    } else {
                        Toast.makeText(ProductCategoryActivity.this, "Nothing updated", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }

        else {
            if (new PODAdapter().getPOBOrdersList() != null) {
                List<InputOrders> inputOrders = new PODAdapter().getPOBOrdersList();
                if (inputOrders.size() > 0) {
                    Log.e("POB" , new Gson().toJson( new PODAdapter().getPOBOrdersList()));
                    apiAddInputBrands(token, new PODAdapter().getPOBOrdersList());
                }
            }
        }
    }

    public void addInputApi(String token, Input input){
        Log.e("Input Object", new Gson().toJson(input));
        processDialog.showDialog(ProductCategoryActivity.this, false);
        Call<InputResponce> call  = apiInterface.addInput(token, input);
        call.enqueue(new Callback<InputResponce>() {
            @Override
            public void onResponse(Call<InputResponce> call, Response<InputResponce> response) {
                Log.e(TAG, "onResponse: "+ new Gson().toJson(response.body()));
                Log.e(TAG, new Gson().toJson(input));
                processDialog.dismissDialog();
                if (response.isSuccessful()){

                    if (response.code() == 200){
                        if(response.body().getStatusCode() == AppConstants.RESULT_OK){
                           // Calling all api, product, gift, and Pod
                            callingAllApi();
                        }
                        else{
                            Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_INDEFINITE).show();

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<InputResponce> call, Throwable t) {
                processDialog.dismissDialog();
                Snackbar.make(rootView, t.getMessage(), Snackbar.LENGTH_INDEFINITE).show();

            }
        });
    }






    public void setDataReceivedListener(DataReceivedListener listener) {
        this.listener = listener;
    }

    public interface DataReceivedListener {
        void onReceived();
    }


}
