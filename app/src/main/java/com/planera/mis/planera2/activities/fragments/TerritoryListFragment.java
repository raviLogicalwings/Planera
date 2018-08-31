package com.planera.mis.planera2.activities.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.FragmentDialog.editDialogs.EditTerritoryDialog;
import com.planera.mis.planera2.activities.Retrofit.ApiClient;
import com.planera.mis.planera2.activities.Retrofit.ApiInterface;
import com.planera.mis.planera2.activities.adapters.TerritoryAdapter;
import com.planera.mis.planera2.activities.models.MainResponse;
import com.planera.mis.planera2.activities.models.Territories;
import com.planera.mis.planera2.activities.models.TerritoryListResponse;
import com.planera.mis.planera2.activities.utils.AppConstants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TerritoryListFragment extends BaseFragment implements EditTerritoryDialog.OnDismissEditTerritoryDialogListener {
    public static TerritoryListFragment instance;
    private View view;
    private ApiInterface apiInterface;
    private List<Territories> statesList;
    private RecyclerView listViewStates;
    private LinearLayout layoutNoData;

    public TerritoryListFragment() {

    }

    public static TerritoryListFragment newInstance() {
        if(instance == null){
            instance = new TerritoryListFragment();
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
            getTerritoryList(token);
        }
    }

    @Override
    protected void initUi() {
        super.initUi();
        listViewStates = view.findViewById(R.id.list_state);
        layoutNoData = view.findViewById(R.id.layout_no_data);
    }



    public void deleteTerritory(String token, int territoryId){
        processDialog.showDialog(mContext, false);
        Call<MainResponse> call = apiInterface.deleteTerritory(token, territoryId);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                processDialog.dismissDialog();
                if (response.body().getStatusCode()  == AppConstants.RESULT_OK){
                    manager.beginTransaction().detach(TerritoryListFragment.this).attach(TerritoryListFragment.this).commit();
                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }


    public void getTerritoryList(String token){
        processDialog.showDialog(mContext, false);
        Call<TerritoryListResponse> call = apiInterface.territoryList(token);
        call.enqueue(new Callback<TerritoryListResponse>() {
            @Override
            public void onResponse(Call<TerritoryListResponse> call, Response<TerritoryListResponse> response) {
                processDialog.dismissDialog();
                if (response!=null){
                    if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                        statesList = response.body().getTerritorysList();
                        initAdapter(statesList, listViewStates);

                    }
                    else{
                        listViewStates.setVisibility(View.GONE);
                        layoutNoData.setVisibility(View.VISIBLE);
                    }


                }
            }

            @Override
            public void onFailure(Call<TerritoryListResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void initAdapter(List<Territories> territorysList, RecyclerView recyclerView){
        TerritoryAdapter adapter = new TerritoryAdapter(getContext(), territorysList, (view, position) -> {
            switch (view.getId()){
                case R.id.img_delete:
                    popupDialog(token, territorysList.get(position).getTerritoryId());
//                    deleteTerritory(token, territorysList.get(position).getTerritoryId());
                    break;

                case R.id.img_edit:
                    EditTerritoryDialog editTerritoryDialog = new EditTerritoryDialog();
                    Bundle bundle= new Bundle();
                    bundle.putInt(AppConstants.KEY_STATE_ID, territorysList.get(position).getStateId());
                    bundle.putString(AppConstants.KEY_TERRITORY_NAME, territorysList.get(position).getTerritoryName());
                    bundle.putString(AppConstants.KEY_STATE_NAME, territorysList.get(position).getStateName());
                    bundle.putInt(AppConstants.KEY_TERRITORY_ID, territorysList.get(position).getTerritoryId());
                    editTerritoryDialog.setArguments(bundle);
                    editTerritoryDialog.setTargetFragment(this, 0);
                    editTerritoryDialog.show(getFragmentManager(), "Edit Territory");
                    break;
            }
        });
        setAdapter(recyclerView, adapter);

    }

    public void setAdapter(RecyclerView recyclerView, TerritoryAdapter adapter){
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


    public void popupDialog( String token, int territoryId){
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();

        alertDialog.setMessage("Are you sure you want to delete this?");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (dialogInterface, i) -> {
            dialogInterface.cancel();
            deleteTerritory(token, territoryId );
        });


        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", (dialog, which) -> {
            dialog.cancel();
        });

        alertDialog.show();

    }


    @Override
    public void onDismissEditTerritoryDialog() {
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }
}
