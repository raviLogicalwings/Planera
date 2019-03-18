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
import android.widget.Toast;

import com.planera.mis.planera2.FragmentDialog.editDialogs.EditStateDialog;
import com.planera.mis.planera2.R;
import com.planera.mis.planera2.Retrofit.ApiClient;
import com.planera.mis.planera2.Retrofit.ApiInterface;
import com.planera.mis.planera2.adapters.StateAdapter;
import com.planera.mis.planera2.models.MainResponse;
import com.planera.mis.planera2.models.StateListResponse;
import com.planera.mis.planera2.models.States;
import com.planera.mis.planera2.utils.AppConstants;
import com.planera.mis.planera2.utils.InternetConnection;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StateListFragment extends BaseFragment implements EditStateDialog.OnDismissEditDialogListener {
    private View view;
    private ApiInterface apiInterface;
    private List<States> statesList = null;
    private RecyclerView listViewStates;
    private LinearLayout linearNoInternet, linearNoData;
    private Button buttonRetry;

    public StateListFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_state_list, container, false);
        initUi();
        initData();

        buttonRetry.setOnClickListener(v -> {
            if (getFragmentManager() != null) {
                getFragmentManager().beginTransaction().detach(StateListFragment.this).attach(StateListFragment.this).commit();
            }
        });
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        apiInterface = ApiClient.getInstance();
        if (token != null) {
            if (InternetConnection.isNetworkAvailable(mContext)) {
                getStatesList(token);
            } else {
                Snackbar.make(rootView, R.string.no_internet, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void initUi() {
        super.initUi();
        listViewStates = view.findViewById(R.id.list_state);
        linearNoInternet = view.findViewById(R.id.linear_no_internet);
        linearNoData = view.findViewById(R.id.linear_no_data);
        buttonRetry = view.findViewById(R.id.button_retry);
    }

    public void deleteStateApi(String token, int stateId) {
        processDialog.showDialog(mContext, false);
        Call<MainResponse> call = apiInterface.deleteState(token, stateId);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(@NonNull Call<MainResponse> call, @NonNull Response<MainResponse> response) {
                processDialog.dismissDialog();
                assert response.body() != null;
                if (response.body().getStatusCode() == AppConstants.RESULT_OK) {
                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    if (InternetConnection.isNetworkAvailable(mContext)) {
                        getStatesList(token);
                    }
                    else
                    {
                        Snackbar.make(rootView, R.string.no_internet, Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(@NonNull Call<MainResponse> call, @NonNull Throwable t) {
                processDialog.dismissDialog();

            }
        });

    }

    public void getStatesList(String token) {
        processDialog.showDialog(mContext, false);
        Call<StateListResponse> call = apiInterface.statesListApi(token);
        call.enqueue(new Callback<StateListResponse>() {
            @Override
            public void onResponse(@NonNull Call<StateListResponse> call, @NonNull Response<StateListResponse> response) {
                processDialog.dismissDialog();
                assert response.body() != null;
                if (response.body().getStatusCode() == AppConstants.RESULT_OK) {
                    statesList = response.body().getData();
                    System.out.println(statesList.size());
                    listViewStates.setVisibility(View.VISIBLE);
                    initAdapter(statesList, listViewStates);
                } else {
                    linearNoData.setVisibility(View.VISIBLE);
                    listViewStates.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(@NonNull Call<StateListResponse> call, @NonNull Throwable t) {
                processDialog.dismissDialog();
                linearNoInternet.setVisibility(View.VISIBLE);
                buttonRetry.setVisibility(View.VISIBLE);
            }
        });

    }

    public void initAdapter(List<States> statesData, RecyclerView recyclerView) {

        StateAdapter adapter = new StateAdapter(getContext(), statesData, (postion, view) -> {
            switch (view.getId()) {
                case R.id.img_delete:
//                    Log.e(TAG, "Clicked Item of State List: "+statesList.get(postion).getStateId() );
                    popupDialog(token, statesList.get(postion).getStateId());
//                    deleteStateApi(token, statesList.get(postion).getStateId());
                    break;

                case R.id.img_edit:
                    EditStateDialog editStateDialog = new EditStateDialog();
                    Bundle bundle = new Bundle();
                    bundle.putString("item", statesList.get(postion).getName());
                    bundle.putInt("id", statesList.get(postion).getStateId());
                    editStateDialog.setArguments(bundle);
                    editStateDialog.setTargetFragment(this, 0);
                    assert getFragmentManager() != null;
                    editStateDialog.show(getFragmentManager(), "Edit State");
                    break;
            }
        });
        setAdapter(recyclerView, adapter);

    }


    public void setAdapter(RecyclerView recyclerView, StateAdapter adapter) {
        recyclerView.hasFixedSize();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

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

    public void popupDialog(String token, int stateId) {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();

        alertDialog.setMessage("Are you sure you want to delete this?");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (dialogInterface, i) -> {
            dialogInterface.cancel();
            if (InternetConnection.isNetworkAvailable(mContext))
            {
                deleteStateApi(token, stateId);
            }
            else
            {
                Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
            }
        });


        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", (dialog, which) -> dialog.cancel());

        alertDialog.show();

    }


    @Override
    public void onDismissEditDialog() {
        assert getFragmentManager() != null;
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }

}
