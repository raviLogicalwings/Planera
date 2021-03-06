package com.planera.mis.planera2.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.Retrofit.ApiClient;
import com.planera.mis.planera2.Retrofit.ApiInterface;
import com.planera.mis.planera2.activities.ActivityUpdateUser;
import com.planera.mis.planera2.adapters.UsersListAdapter;
import com.planera.mis.planera2.models.MainResponse;
import com.planera.mis.planera2.models.UserData;
import com.planera.mis.planera2.models.UserListResponse;
import com.planera.mis.planera2.utils.AppConstants;
import com.planera.mis.planera2.utils.InternetConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersFragment extends BaseFragment implements SearchView.OnQueryTextListener{

    public static UsersFragment instance;
    private View view;
    private ApiInterface apiInterface;
    private List<UserData> userDataList;
    private RecyclerView listViewUsers;
    private LinearLayout layoutNoData;
    private UsersListAdapter adapter;

    public UsersFragment() {

    }

    public static UsersFragment newInstance() {
        if(instance == null){
            instance = new UsersFragment();
        }

        return instance;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_users, container, false);
        initUi();
        initData();
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        apiInterface = ApiClient.getInstance();
        if(token!=null){
            if (InternetConnection.isNetworkAvailable(mContext)){
                getUsersList(token);
            }
            else
            {
                Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void initUi() {
        super.initUi();
        listViewUsers = view.findViewById(R.id.list_users);
        SearchView searchViewUser = view.findViewById(R.id.search_view_users);
        layoutNoData = view.findViewById(R.id.layout_no_data);
        searchViewUser.setActivated(true);
        searchViewUser.onActionViewExpanded();
        searchViewUser.setIconified(false);
        searchViewUser.clearFocus();

        searchViewUser.setOnQueryTextListener(this);
    }




    public void getUsersList(String token){
        processDialog.showDialog(mContext, false);
        Call<UserListResponse> call = apiInterface.usersList(token);
        call.enqueue(new Callback<UserListResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserListResponse> call, @NonNull Response<UserListResponse> response) {
                processDialog.dismissDialog();
                assert response.body() != null;
                if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                    userDataList = response.body().getData();
                    if (userDataList!=null) {
                        listViewUsers.setVisibility(View.VISIBLE);
                        layoutNoData.setVisibility(View.GONE);
                        System.out.println(userDataList.size());

                        initAdapter(userDataList, listViewUsers);
                    }
            }
            else{
                    layoutNoData.setVisibility(View.VISIBLE);
                    listViewUsers.setVisibility(View.GONE);
                    Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(@NonNull Call<UserListResponse> call, @NonNull Throwable t) {
                processDialog.dismissDialog();
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    void filter(String text){
        List<UserData> temp = new ArrayList<>();
        for(UserData d: userDataList){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getFirstName().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }
        }
        adapter.updateList(temp);
    }

    public void initAdapter(List<UserData> list, RecyclerView recyclerView){

        adapter = new UsersListAdapter(mContext, list, (view, user) -> {
            switch (view.getId()){
                case R.id.img_user_delete:
                    popupDialog(token, user.getUserId());
//                    deleteUserApi(token, list.get(position).getUserId());
                    break;

                case R.id.img_user_edit:
                    userDetailsForUpdate(user);
                    Objects.requireNonNull(UsersFragment.this.getActivity()).finish();

                    break;

            }
        });

        setAdapter(recyclerView, adapter);

    }


    public void userDetailsForUpdate(UserData user){
        int selectedUser = Integer.parseInt(user.getUserId());
        Intent intentDoctorCall = new Intent(mContext, ActivityUpdateUser.class);
        intentDoctorCall.putExtra(AppConstants.UPDATE_DOCTOR_KEY, selectedUser);
        intentDoctorCall.putExtra(AppConstants.FIRST_NAME, user.getFirstName());
        intentDoctorCall.putExtra(AppConstants.MIDDLE_NAME, user.getMiddleName());
        intentDoctorCall.putExtra(AppConstants.LAST_NAME, user.getLastName());
        intentDoctorCall.putExtra(AppConstants.LOGIN_ID, user.getLoginId());
        intentDoctorCall.putExtra(AppConstants.PASSWORD, user.getPassword());
        intentDoctorCall.putExtra(AppConstants.PHONE1, user.getPhone1());
        intentDoctorCall.putExtra(AppConstants.EMAIL1, user.getEmail1());
        intentDoctorCall.putExtra(AppConstants.PHONE2, user.getPhone2());
        intentDoctorCall.putExtra(AppConstants.EMAIL2, user.getEmail2());
        intentDoctorCall.putExtra(AppConstants.QUALIFICATION, user.getQualifications());
        intentDoctorCall.putExtra(AppConstants.EXPERIENCE_YEAR, user.getExperienceYear());
        intentDoctorCall.putExtra(AppConstants.DOJ, user.getDOJ());
        intentDoctorCall.putExtra(AppConstants.PAN, user.getPAN());
        intentDoctorCall.putExtra(AppConstants.DOB, user.getDOB());
        intentDoctorCall.putExtra(AppConstants.ADDRESS1, user.getAddress1());
        intentDoctorCall.putExtra(AppConstants.ADDRESS2, user.getAddress2());
        intentDoctorCall.putExtra(AppConstants.ADDRESS3, user.getAddress3());
        intentDoctorCall.putExtra(AppConstants.ADDRESS4, user.getAddress4());
        intentDoctorCall.putExtra(AppConstants.DISTRICT, user.getDistrict());
        intentDoctorCall.putExtra(AppConstants.STATE, user.getState());
        intentDoctorCall.putExtra(AppConstants.CITY, user.getCity());
        intentDoctorCall.putExtra(AppConstants.PINCODE, user.getPincode());
        intentDoctorCall.putExtra(AppConstants.USER_TYPE, user.getType());
        intentDoctorCall.putExtra(AppConstants.UPDATE_USER_KEY, selectedUser);
        mContext.startActivity(intentDoctorCall);

    }
    public void setAdapter(RecyclerView recyclerView, UsersListAdapter adapter){
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

    public void popupDialog( String token, String userId){
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();

        alertDialog.setMessage("Are you sure you want to delete this?");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (dialogInterface, i) -> {
            dialogInterface.cancel();

            if (InternetConnection.isNetworkAvailable(mContext)){
                deleteUserApi(token, userId+"" );
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



    public void deleteUserApi(String token, String doctorId){
        processDialog.showDialog(mContext, false);
        int temp = Integer.parseInt(doctorId);
        Call<MainResponse> call = apiInterface.deleteUser(token, temp);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(@NonNull Call<MainResponse> call, @NonNull Response<MainResponse> response) {
                processDialog.dismissDialog();
                if (response.body().getStatusCode() == AppConstants.RESULT_OK){
//                    getFragmentManager().beginTransaction().detach(UsersFragment.this).attach(UsersFragment.this).commit();
                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    if (InternetConnection.isNetworkAvailable(mContext)){
                        getUsersList(token);
                    }
                    else
                    {
                        Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MainResponse> call, @NonNull Throwable t) {
                processDialog.dismissDialog();

                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        filter(newText);
        return false;
    }
}
