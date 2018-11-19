package com.planera.mis.planera2.activities.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.FragmentDialog.editDialogs.EditGiftDialog;
import com.planera.mis.planera2.activities.Retrofit.ApiClient;
import com.planera.mis.planera2.activities.Retrofit.ApiInterface;
import com.planera.mis.planera2.activities.adapters.AdminGiftAdapter;
import com.planera.mis.planera2.activities.models.GiftListResponse;
import com.planera.mis.planera2.activities.models.GiftsData;
import com.planera.mis.planera2.activities.models.MainResponse;
import com.planera.mis.planera2.activities.utils.AppConstants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class GiftListFragment extends BaseFragment implements EditGiftDialog.OnDismissEditGiftDialogListener {


    public static GiftListFragment instance;
    private View view;
    private ApiInterface apiInterface;
    private List<GiftsData> giftList;
    private RecyclerView listViewGifts;
    private LinearLayout linearNoData, linearNoInternet;
    private Button buttonRetry;

    public GiftListFragment() {
    }

    public static GiftListFragment newInstance() {
        if (instance == null) {
            instance = new GiftListFragment();
        }

        return instance;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_state_list, container, false);
        initUi();
        initData();
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        apiInterface = ApiClient.getInstance();
        if (token != null) {
            getGiftList(token);
        }
    }

    @Override
    protected void initUi() {
        super.initUi();
        listViewGifts = view.findViewById(R.id.list_state);
        linearNoData = view.findViewById(R.id.linear_no_data);
        linearNoInternet = view.findViewById(R.id.linear_no_internet);
        buttonRetry = view.findViewById(R.id.button_retry);

        buttonRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().detach(GiftListFragment.this).attach(GiftListFragment.this).commit();
                }
            }
        });
    }

    public void getGiftList(String token) {
        processDialog.showDialog(mContext, false);
        Call<GiftListResponse> call = apiInterface.giftListApi(token);
        call.enqueue(new Callback<GiftListResponse>() {
            @Override
            public void onResponse(Call<GiftListResponse> call, Response<GiftListResponse> response) {
                processDialog.dismissDialog();
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response != null) {
                    if (response.body().getStatusCode() == AppConstants.RESULT_OK) {
                        giftList = response.body().getGiftsData();
                        System.out.println(giftList.size());
                        listViewGifts.setVisibility(View.VISIBLE);
                        initAdapter(giftList, listViewGifts);

                    } else {
                        listViewGifts.setVisibility(View.GONE);
                        linearNoData.setVisibility(View.VISIBLE);
                    }


                }
            }

            @Override
            public void onFailure(Call<GiftListResponse> call, Throwable t) {
                processDialog.dismissDialog();
                linearNoInternet.setVisibility(View.VISIBLE);
                buttonRetry.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void initAdapter(List<GiftsData> giftsData, RecyclerView recyclerView) {
        AdminGiftAdapter adapter = new AdminGiftAdapter(mContext, giftsData, new AdminGiftAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                switch (view.getId()) {
                    case R.id.img_delete:
                        popupDialog(token, giftsData.get(position).getGiftId());
//                    deleteGiftApi(token, giftsData.get(position).getGiftId());
                        break;

                    case R.id.img_edit:
                        EditGiftDialog dialog = new EditGiftDialog();
                        Bundle bundle = new Bundle();
                        bundle.putString(AppConstants.KEY_GIFT_NAME, giftsData.get(position).getName());
                        bundle.putInt(AppConstants.KEY_GIFT_ID, giftsData.get(position).getGiftId());
                        dialog.setTargetFragment(GiftListFragment.this, 0);
                        dialog.setArguments(bundle);
                        dialog.show(getFragmentManager(), "Update Gift");
                        break;

                }
            }
        });


        setAdapter(recyclerView, adapter);
    }

    public void deleteGiftApi(String token, int id) {
        processDialog.showDialog(mContext, false);
        Call<MainResponse> call = apiInterface.deleteGift(token, id);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                processDialog.dismissDialog();
                if (response != null) {
                    if (response.body().getStatusCode() == AppConstants.RESULT_OK) {
                        Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        manager.beginTransaction().detach(GiftListFragment.this).attach(GiftListFragment.this).commit();
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


    public void setAdapter(RecyclerView recyclerView, AdminGiftAdapter adapter) {
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

    public void popupDialog(String token, int giftId) {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();

        alertDialog.setMessage("Are you sure you want to delete this?");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (dialogInterface, i) -> {
            dialogInterface.cancel();
            deleteGiftApi(token, giftId);
        });


        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", (dialog, which) -> {
            dialog.cancel();
        });

        alertDialog.show();

    }


    @Override
    public void onDismissEditGiftDialog() {
        refreshFragment(this);
    }
}
