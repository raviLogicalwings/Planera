package com.planera.mis.planera2.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.support.v4.app.Fragment;


import com.planera.mis.planera2.R;
import com.planera.mis.planera2.FragmentDialog.editDialogs.EditProductDialog;
import com.planera.mis.planera2.Retrofit.ApiClient;
import com.planera.mis.planera2.Retrofit.ApiInterface;
import com.planera.mis.planera2.adapters.ProductsAdapter;
import com.planera.mis.planera2.models.Brands;
import com.planera.mis.planera2.models.BrandsListResponse;
import com.planera.mis.planera2.models.MainResponse;
import com.planera.mis.planera2.utils.AppConstants;
import com.planera.mis.planera2.utils.InternetConnection;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFragment extends BaseFragment implements EditProductDialog.OnDismissEditProductDialogListener{

    private View view;
    private ApiInterface apiInterface;
    private List<Brands> productList;
    private RecyclerView listViewProducts;
    private LinearLayout linearNoInternet,linearNoData;
    private Button buttonRetry;

    public ProductFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_product, container, false);
        initUi();
        initData();
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        apiInterface = ApiClient.getInstance();
        if (token != null) {
            if (InternetConnection.isNetworkAvailable(mContext)){
                getProductsListApi(token);
            }
            else
            {
                Snackbar.make(rootView, R.string.no_internet, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void initUi() {
        super.initUi();
        listViewProducts = view.findViewById(R.id.list_products);
        linearNoData = view.findViewById(R.id.linear_no_data);
        linearNoInternet = view.findViewById(R.id.linear_no_internet);
        buttonRetry = view.findViewById(R.id.button_retry);
        buttonRetry.setOnClickListener(v -> {
            if (getFragmentManager() != null) {
                getFragmentManager().beginTransaction().detach(ProductFragment.this).attach(ProductFragment.this).commit();
            }
        });
    }


    public void getProductsListApi(String token) {
        processDialog.showDialog(mContext, false);
        Call<BrandsListResponse> call = apiInterface.brandsListApi(token, AppConstants.BRAND);
        call.enqueue(new Callback<BrandsListResponse>() {
            @Override
            public void onResponse(@NonNull Call<BrandsListResponse> call, @NonNull Response<BrandsListResponse> response) {
                processDialog.dismissDialog();
                assert response.body() != null;
                if (response.body().getStatusCode() == AppConstants.RESULT_OK) {

                    productList = response.body().getData();
                    if (productList != null) {
                        listViewProducts.setVisibility(View.VISIBLE);
                        initAdapter(productList, listViewProducts);
                    } else {
                        linearNoData.setVisibility(View.VISIBLE);
                        listViewProducts.setVisibility(View.GONE);
                    }

                } else {
                    linearNoData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<BrandsListResponse> call, Throwable t) {
                processDialog.dismissDialog();
                linearNoInternet.setVisibility(View.VISIBLE);
                buttonRetry.setVisibility(View.VISIBLE);
            }
        });
    }

    public void initAdapter(List<Brands> brandsData, RecyclerView recyclerView) {
        ProductsAdapter adapter = new ProductsAdapter(mContext, brandsData, (position, view) -> {
            switch (view.getId()) {
                case R.id.img_delete:
                    popupDialog(token, productList.get(position).getProductId());
//                    deleteProductApi(token, productList.get(position).getProductId());
                    break;
                case R.id.img_edit:
                    EditProductDialog dialog = new EditProductDialog();
                    Bundle bundle = new Bundle();
                    bundle.putInt(AppConstants.KEY_PRODUCT_ID, productList.get(position).getProductId());
                    bundle.putString(AppConstants.KEY_IS_BRAND, productList.get(position).getIsBrand());
                    bundle.putString(AppConstants.KEY_PRODUCT_NAME, productList.get(position).getName());
                    dialog.setArguments(bundle);
                    dialog.setTargetFragment(this, 0);
                    dialog.show(manager, "Edit Product");
                    break;
            }
        });
        setAdapter(recyclerView, adapter);

    }

    public void deleteProductApi(String token, int id) {
        processDialog.showDialog(mContext, false);
        Call<MainResponse> call = apiInterface.deleteProduct(token, id);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                processDialog.dismissDialog();
                if (response != null) {
                    if (response.body().getStatusCode() == AppConstants.RESULT_OK) {
                        Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
//
                        if (InternetConnection.isNetworkAvailable(mContext)){
                            getProductsListApi(token);
                        }
                        else
                        {
                            Snackbar.make(rootView, R.string.no_internet, Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }


    public void refreshFragment(Fragment fragment) {
        getFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();
    }


    public void setAdapter(RecyclerView recyclerView, ProductsAdapter adapter) {
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

    public void popupDialog( String token, int productId){
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();

        alertDialog.setMessage("Are you sure you want to delete this?");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (dialogInterface, i) -> {

            dialogInterface.cancel();

            if (InternetConnection.isNetworkAvailable(mContext))
            {
                deleteProductApi(token, productId );
            }
            else
            {
                Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
            }


        });


        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", (dialog, which) -> {
            dialog.cancel();
        });

        alertDialog.show();

    }

    @Override
    public void onDismissEditProductDialog() {
        refreshFragment(this);
    }
}
