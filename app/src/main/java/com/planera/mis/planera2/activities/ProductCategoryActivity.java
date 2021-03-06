package com.planera.mis.planera2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.planera.mis.planera2.adapters.BrandsAdapter;
import com.planera.mis.planera2.adapters.ChemistTabsPagerAdapter;
import com.planera.mis.planera2.adapters.DoctorTabsPagerAdapter;
import com.planera.mis.planera2.adapters.GiftsAdapter;
import com.planera.mis.planera2.adapters.PODAdapter;
import com.planera.mis.planera2.adapters.SampleListAdapter;
import com.planera.mis.planera2.controller.DataController;
import com.planera.mis.planera2.models.DataItem;
import com.planera.mis.planera2.models.Input;
import com.planera.mis.planera2.models.InputGift;
import com.planera.mis.planera2.models.InputGiftResponce;
import com.planera.mis.planera2.models.InputOrders;
import com.planera.mis.planera2.models.InputResponce;
import com.planera.mis.planera2.models.MainResponse;
import com.planera.mis.planera2.utils.AppConstants;
import com.planera.mis.planera2.utils.InternetConnection;

import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductCategoryActivity extends BaseActivity implements View.OnClickListener {
    private String INPUT_ID = null;
    protected DoctorTabsPagerAdapter doctorTabsPagerAdapter;
    protected ChemistTabsPagerAdapter chemistTabsPagerAdapter;
    protected FragmentManager fragmentManager;
    private TabLayout tabLayout;
    private ViewPager pager;
    protected Toolbar toolbarProduct;
    protected Button buttonConfirm;
    private Input input;
    protected DataReceivedListener listener;
    private boolean isDoctor;
    public static final String TAG = ProductCategoryActivity.class.getSimpleName();
    private boolean isUpdateInput;
    protected DataItem dataForUpdate;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_category);
        initUi();
        initData();
    }

    @Override
    public void initData() {
        super.initData();
        isDoctor = connector.getBoolean(AppConstants.KEY_ROLE);
        fragmentManager = getSupportFragmentManager();
        String getInput = getIntent().getStringExtra(AppConstants.PASS_INPUT);
//        Log.e("Input", getInput);
        input = new Gson().fromJson(getInput, Input.class);
        isUpdateInput = getIntent().getBooleanExtra(AppConstants.IS_INPUT_UPDATE, false);


        if (isUpdateInput) {
            String strData = getIntent().getStringExtra(AppConstants.PASS_UPDATE_INPUT);
            dataForUpdate = new Gson().fromJson(strData, DataItem.class);
            Bundle bundle = new Bundle();
            bundle.putString(AppConstants.PASS_UPDATE_INPUT, strData);

            if (dataForUpdate.getChemistsId().equals("0")) {
                doctorTabsPagerAdapter = new DoctorTabsPagerAdapter(fragmentManager, ProductCategoryActivity.this, bundle);
                pager.setAdapter(doctorTabsPagerAdapter);


            } else {
//                tabLayout.setVisibility(View.GONE);
//                PODFragment podFragment = new PODFragment();
//                podFragment.setArguments(bundle);
                chemistTabsPagerAdapter = new ChemistTabsPagerAdapter(fragmentManager, ProductCategoryActivity.this, bundle);
                pager.setAdapter(chemistTabsPagerAdapter);
            }

            tabLayout.setupWithViewPager(pager);
        } else {

            if (isDoctor) {
                doctorTabsPagerAdapter = new DoctorTabsPagerAdapter(fragmentManager, ProductCategoryActivity.this);
                pager.setAdapter(doctorTabsPagerAdapter);
            } else {
//                tabLayout.setVisibility(View.GONE);
//                PODFragment podFragment = new PODFragment();
//                fragmentManager.beginTransaction().replace(R.id.swiper, podFragment).commit();
                chemistTabsPagerAdapter = new ChemistTabsPagerAdapter(fragmentManager, ProductCategoryActivity.this);
                pager.setAdapter(chemistTabsPagerAdapter);
            }

            tabLayout.setupWithViewPager(pager);
        }

    }

    @Override
    public void initUi() {
        super.initUi();

        toolbarProduct = findViewById(R.id.toolbarProduct);
        buttonConfirm = findViewById(R.id.button_confirm_product);
        tabLayout = findViewById(R.id.tabLayout);
        pager = findViewById(R.id.swiper);
        pager.setOffscreenPageLimit(3);
        setSupportActionBar(toolbarProduct);
        toolbarProduct.setNavigationIcon(R.drawable.back_arrow_whit);

        toolbarProduct.setNavigationOnClickListener(v -> onBackPressed());
        buttonConfirm.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_confirm_product:
                if (isUpdateInput){
                    input.setGiftDetails(new GiftsAdapter().getInputGiftList());
                    if (new PODAdapter().getPOBOrdersList() != null){
                        input.setProductDetalis(new PODAdapter().getPOBOrdersList());
                        for( int i=0; i<input.getProductDetalis().size(); i++)  {
                            String inputId = input.getInputId();
                            input.getProductDetalis().get(i).setInputId(inputId);
                        }
                    }
                    if (new SampleListAdapter().getSampleListSelected()!= null){
                        input.setProductDetalis(new SampleListAdapter().getSampleListSelected());
                        for( int i=0; i<input.getProductDetalis().size(); i++)  {
                            String inputId = input.getInputId();
                            input.getProductDetalis().get(i).setInputId(inputId);
                        }
                    }
                    if (InternetConnection.isNetworkAvailable(ProductCategoryActivity.this))
                    {
                        input.setJointUserList(dataForUpdate.getJointUserList());
                        apiUpdateMrInput(token, input);
                    }
                    else {
                        Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
                    }

                }
                else if (InternetConnection.isNetworkAvailable(ProductCategoryActivity.this)) {
                    addInputApi(token, input);
                } else {
                    Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_SHORT).show();
                }

                break;
        }

    }


    public void addInputGiftApi(String token, List<InputGift> inputGifts) {
        processDialog.showDialog(ProductCategoryActivity.this, false);
        Call<InputGiftResponce> call = apiInterface.addInputGift(token, inputGifts);
        call.enqueue(new Callback<InputGiftResponce>() {
            @Override
            public void onResponse(@NonNull Call<InputGiftResponce> call, @NonNull Response<InputGiftResponce> response) {
                processDialog.dismissDialog();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (Objects.requireNonNull(response.body()).getStatusCode() == AppConstants.RESULT_OK) {
                            new GiftsAdapter().setInputGiftList(null);
                            DataController.getmInstance().setInputGiftList(null);

                            Intent startMainActivity = new Intent(ProductCategoryActivity.this, MainActivity.class);
                            startActivity(startMainActivity);

                            Toast.makeText(ProductCategoryActivity.this, Objects.requireNonNull(response.body()).getMessage(), Snackbar.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(ProductCategoryActivity.this, Objects.requireNonNull(response.body()).getMessage(), Snackbar.LENGTH_LONG).show();

                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<InputGiftResponce> call, @NonNull Throwable t) {
                processDialog.dismissDialog();
               Toasty.error(ProductCategoryActivity.this, t.getMessage(), Snackbar.LENGTH_LONG).show();

            }
        });
    }


    public void apiAddInputSamples(String token, List<InputOrders> inputOrders) {


        processDialog.showDialog(ProductCategoryActivity.this, false);
        Call<MainResponse> call = apiInterface.addInputProductList(token, inputOrders);

        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(@NonNull Call<MainResponse> call, Response<MainResponse> response) {
                processDialog.dismissDialog();
                if (response.body().getStatusCode() == AppConstants.RESULT_OK) {
                    Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                    // Set Array Lis to null
                    //----------
                    new SampleListAdapter().setSampleListSelected(null);
                    DataController.getmInstance().setOrderListSelected(null);

                    // On Response of Input Brand Calling add input Gift Api
                    //---------------
                    Intent startMainActivity = new Intent(ProductCategoryActivity.this, MainActivity.class);
                    startActivity(startMainActivity);

                } else {
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



    public void apiUpdateMrInput(String token, Input input){
//        Log.e("Update Input", new Gson().toJson(input));
        processDialog.showDialog(ProductCategoryActivity.this, false);
        Call<MainResponse> call = apiInterface.updateMrInput(token, input);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(@NonNull Call<MainResponse> call, @NonNull Response<MainResponse> response) {
                processDialog.dismissDialog();
                if (response.isSuccessful()){
                    if (Objects.requireNonNull(response.body()).getStatusCode() == AppConstants.RESULT_OK){
                        Toasty.success(ProductCategoryActivity.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
                        new SampleListAdapter().setSampleListSelected(null);
                        new PODAdapter().setPOBOrdersList(null);
                        new GiftsAdapter().setInputGiftList(null);
                        Intent inUserReportList = new Intent(ProductCategoryActivity.this, SearchDateWiseInputActivity.class);
                        inUserReportList.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(inUserReportList);
                        finish();
                    }
                    else{
                        Toasty.error(ProductCategoryActivity.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MainResponse> call, @NonNull Throwable t) {
                processDialog.dismissDialog();
                Toasty.error(ProductCategoryActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    public void apiAddInputBrands(String token, List<InputOrders> inputOrders) {
        processDialog.showDialog(ProductCategoryActivity.this, false);
        Call<MainResponse> call = apiInterface.addInputProductList(token, inputOrders);

        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(@NonNull Call<MainResponse> call, @NonNull Response<MainResponse> response) {
                processDialog.dismissDialog();
                if (response.body().getStatusCode() == AppConstants.RESULT_OK) {
                    Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                    // Set Array List to null
                    //----------
                    new BrandsAdapter().setOrderListSelected(null);
                    DataController.getmInstance().setOrderListSelected(null);

                    Intent startMainActivity = new Intent(ProductCategoryActivity.this, MainActivity.class);
                    startActivity(startMainActivity);
                    //---------------

                } else {
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


    public void callingAllApi() {

        if (isDoctor) {

                if (new BrandsAdapter().getOrderListSelected() != null) {
                    List<InputOrders> inputB = new BrandsAdapter().getOrderListSelected();
                    if (inputB.size() > 0) {
                        for (int i = 0; i < inputB.size(); i++) {
                            inputB.get(i).setInputId(INPUT_ID);
                            inputB.get(i).setIsJoint(input.getIsJoint());
//                            Log.e("Brands", new Gson().toJson(inputB));
                        }
                        if(InternetConnection.isNetworkAvailable(mContext))
                        {
                            apiAddInputBrands(token, inputB);
                        }
                        else
                        {
                            Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
                        }
                    }
                }


                if (new GiftsAdapter().getInputGiftList() != null) {
                    List<InputGift> inputGifts = new GiftsAdapter().getInputGiftList();
                    if (inputGifts.size() > 0) {
                        for (int i = 0; i < inputGifts.size(); i++) {
                            inputGifts.get(i).setInputId(INPUT_ID);
                            inputGifts.get(i).setIsJoint(input.getIsJoint());
                        }

                        if (InternetConnection.isNetworkAvailable(ProductCategoryActivity.this))
                        {
                            addInputGiftApi(token, inputGifts);
                        }
                        else
                        {
                            Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
                        }
                    }
                }

                if (new SampleListAdapter().getSampleListSelected()!= null) {
                    List<InputOrders> inputSamples = new SampleListAdapter().getSampleListSelected();
                    if (inputSamples.size() > 0) {
                        for (int i = 0; i < inputSamples.size(); i++) {
                            inputSamples.get(i).setInputId(INPUT_ID);
                            inputSamples.get(i).setIsJoint(input.getIsJoint());

                        }
                        if (InternetConnection.isNetworkAvailable(ProductCategoryActivity.this)){
                            apiAddInputSamples(token, inputSamples);
                        }
                        else
                        {
                            Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
                        }
                    }
                }

        }
        else{
            if (new PODAdapter().getPOBOrdersList() != null) {
                List<InputOrders> inputOrders = new PODAdapter().getPOBOrdersList();
                if (inputOrders.size() > 0) {
                    for (int i = 0; i < inputOrders.size(); i++) {
                        inputOrders.get(i).setInputId(INPUT_ID);
                        inputOrders.get(i).setIsJoint(input.getIsJoint());

                    }
                    apiAddInputBrands(token, inputOrders);
                }
            }
        }
    }

    public void addInputApi(String token, Input input) {
        Log.e(TAG, new Gson().toJson(input));
        processDialog.showDialog(ProductCategoryActivity.this, false);
        Call<InputResponce> call = apiInterface.addInput(token, input);
        call.enqueue(new Callback<InputResponce>() {
            @Override
            public void onResponse(@NonNull Call<InputResponce> call, @NonNull Response<InputResponce> response) {
                processDialog.dismissDialog();
                if (response.isSuccessful()) {

                    if (response.code() == 200) {
                        if (Objects.requireNonNull(response.body()).getStatusCode() == AppConstants.RESULT_OK) {
                            // Calling all api, product, gift, and Pod
                            INPUT_ID = response.body().getData().getInputId();
                            new Handler().postDelayed(() -> callingAllApi(), 3000);
                        } else {
                            Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_INDEFINITE).show();

                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<InputResponce> call, @NonNull Throwable t) {
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
