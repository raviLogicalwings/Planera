package com.planera.mis.planera2.activities.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.FragmentDialog.editDialogs.EditPatchDialog;
import com.planera.mis.planera2.activities.Retrofit.ApiClient;
import com.planera.mis.planera2.activities.Retrofit.ApiInterface;
import com.planera.mis.planera2.activities.adapters.PatchAdapter;
import com.planera.mis.planera2.activities.models.MainResponse;
import com.planera.mis.planera2.activities.models.PatchListResponse;
import com.planera.mis.planera2.activities.models.Patches;
import com.planera.mis.planera2.activities.utils.AppConstants;
import com.planera.mis.planera2.activities.utils.InternetConnection;


import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

public class PatchListFragment extends BaseFragment implements EditPatchDialog.OnDismissEditPatchDialogListener{
    public static PatchListFragment instance;
    private View view;
    private ApiInterface apiInterface;
    private List<Patches> patchesList;
    private RecyclerView listViewStates;
    private LinearLayout linearNodata, linearNoInternet;
    private Button buttonRetry;

    public PatchListFragment() {

    }

    public static PatchListFragment newInstance() {
        if(instance == null){
            instance = new PatchListFragment();
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
        view =  inflater.inflate(R.layout.fragment_state_list, container, false);
        initUi();
        initData();
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        apiInterface = ApiClient.getInstance();
        if(token!=null){
            getPatchList(token);
        }
    }

    @Override
    protected void initUi() {
        super.initUi();
        listViewStates = view.findViewById(R.id.list_state);
        linearNodata = view.findViewById(R.id.linear_no_data);
        linearNoInternet = view.findViewById(R.id.linear_no_internet);
        buttonRetry = view.findViewById(R.id.button_retry);

        buttonRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().detach(PatchListFragment.this).attach(PatchListFragment.this).commit();
                }
            }
        });
    }

    public void deletePatchApi(String token, int patchId){
        Log.d(TAG, "deletePatchApi() called with: token = [" + token + "], patchId = [" + patchId + "]");
        processDialog.showDialog(mContext, false);
        Call<MainResponse> call = apiInterface.deletePatch(token, patchId);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                processDialog.dismissDialog();
                if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    getFragmentManager().beginTransaction().detach(PatchListFragment.this).attach(PatchListFragment.this).commit();
                }
                else{
                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }



    public void getPatchList(String token){
        processDialog.showDialog(mContext, false);
        Call<PatchListResponse> call = apiInterface.patchList(token);
        call.enqueue(new Callback<PatchListResponse>() {
            @Override
            public void onResponse(Call<PatchListResponse> call, Response<PatchListResponse> response) {
                processDialog.dismissDialog();
                if (response!=null){
                    if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                        patchesList = response.body().getPatchesList();
                        initAdapter(patchesList, listViewStates);
                        Log.e("Patch Id", "onResponse: "+patchesList.get(0).getPatchId());

                    }
                    else{
                        listViewStates.setVisibility(View.GONE);
                        linearNodata.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<PatchListResponse> call, Throwable t) {
                processDialog.dismissDialog();
                linearNoInternet.setVisibility(View.VISIBLE);
                buttonRetry.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void initAdapter(List<Patches> territorysList, RecyclerView recyclerView){
        PatchAdapter adapter = new PatchAdapter(getContext(), territorysList, (View view, int position) -> {
            switch (view.getId()){
                case R.id.img_delete:
                    if (InternetConnection.isNetworkAvailable(mContext)){
                        popupDialog(token, patchesList.get(position).getPatchId());
//                        deletePatchApi(token, patchesList.get(position).getPatchId());
                    }
                    else{
                        Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
                    }

                    break;

                case R.id.img_edit:
                    EditPatchDialog dialog = new EditPatchDialog();
                    Bundle bundle = new Bundle();
                    bundle.putInt(AppConstants.KEY_TERRITORY_ID, patchesList.get(position).getTerritoryId());
                    bundle.putInt(AppConstants.KEY_PATCH_ID, patchesList.get(position).getPatchId());
                    bundle.putString(AppConstants.KEY_PATCH_NAME, patchesList.get(position).getPatchName());
                    dialog.setTargetFragment(PatchListFragment.this, 0);
                    dialog.setArguments(bundle);
                    dialog.show(Objects.requireNonNull(getFragmentManager()), "Update Patch");
                    break;

            }
        });
        setAdapter(recyclerView, adapter);

    }

    public void refreshFragment(Fragment fragment){
        getFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();
    }

    public void setAdapter(RecyclerView recyclerView, PatchAdapter adapter){
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

    public void popupDialog( String token, int patchId){
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();

        alertDialog.setMessage("Are you sure you want to delete this?");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (dialogInterface, i) -> {
            dialogInterface.cancel();
            deletePatchApi(token, patchId );
        });


        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", (dialog, which) -> {
            dialog.cancel();
        });

        alertDialog.show();

    }


    @Override
    public void onDismissEditPatchDialog() {
        refreshFragment(PatchListFragment.this);
    }
}
