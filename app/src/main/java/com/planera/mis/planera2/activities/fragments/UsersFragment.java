package com.planera.mis.planera2.activities.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.Retrofit.ApiClient;
import com.planera.mis.planera2.activities.Retrofit.ApiInterface;
import com.planera.mis.planera2.activities.ActivityUpdateUser;
import com.planera.mis.planera2.activities.adapters.UsersListAdapter;
import com.planera.mis.planera2.activities.models.MainResponse;
import com.planera.mis.planera2.activities.models.UserData;
import com.planera.mis.planera2.activities.models.UserListResponse;
import com.planera.mis.planera2.activities.utils.AppConstants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class UsersFragment extends BaseFragment{

    public static UsersFragment instance;
    private View view;
    private ApiInterface apiInterface;
    private List<UserData> userDataList;
    private RecyclerView listViewUsers;
    private LinearLayout layoutNoData;
    private int selectedUser;

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
            getUsersList(token);
        }
    }

    @Override
    protected void initUi() {
        super.initUi();
        listViewUsers = view.findViewById(R.id.list_users);
        layoutNoData = view.findViewById(R.id.layout_no_data);
    }




    public void getUsersList(String token){
        processDialog.showDialog(mContext, false);
        Call<UserListResponse> call = apiInterface.usersList(token);
        call.enqueue(new Callback<UserListResponse>() {
            @Override
            public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {
                processDialog.dismissDialog();
                Log.e(TAG, "onResponse: "+new Gson().toJson(response.body()));
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
            public void onFailure(Call<UserListResponse> call, Throwable t) {
                processDialog.dismissDialog();
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void initAdapter(List<UserData> list, RecyclerView recyclerView){
        UsersListAdapter adapter = new UsersListAdapter(mContext, list, (view, position) -> {
            switch (view.getId()){
                case R.id.img_user_delete:
                    popupDialog(token, list.get(position).getUserId());
//                    deleteUserApi(token, list.get(position).getUserId());
                    break;

                case R.id.img_user_edit:
                    userDetailsForUpdate(position, list);

                    break;

            }
        });

        setAdapter(recyclerView, adapter);

    }


    public void userDetailsForUpdate(int pos, List<UserData> usersData){
        selectedUser= Integer.parseInt(usersData.get(pos).getUserId());
        Intent intentDoctorCall = new Intent(mContext, ActivityUpdateUser.class);
        intentDoctorCall.putExtra(AppConstants.UPDATE_DOCTOR_KEY, selectedUser);
        intentDoctorCall.putExtra(AppConstants.FIRST_NAME, usersData.get(pos).getFirstName());
        intentDoctorCall.putExtra(AppConstants.MIDDLE_NAME, usersData.get(pos).getMiddleName());
        intentDoctorCall.putExtra(AppConstants.LAST_NAME, usersData.get(pos).getLastName());
        intentDoctorCall.putExtra(AppConstants.LOGIN_ID, usersData.get(pos).getLoginId());
        intentDoctorCall.putExtra(AppConstants.PASSWORD, usersData.get(pos).getPassword());
        intentDoctorCall.putExtra(AppConstants.PHONE1, usersData.get(pos).getPhone1());
        intentDoctorCall.putExtra(AppConstants.EMAIL1, usersData.get(pos).getEmail1());
        intentDoctorCall.putExtra(AppConstants.PHONE2, usersData.get(pos).getPhone2());
        intentDoctorCall.putExtra(AppConstants.EMAIL2, usersData.get(pos).getEmail2());
        intentDoctorCall.putExtra(AppConstants.QUALIFICATION, usersData.get(pos).getQualifications());
        intentDoctorCall.putExtra(AppConstants.EXPERIENCE_YEAR, usersData.get(pos).getExperienceYear());
        intentDoctorCall.putExtra(AppConstants.DOJ, usersData.get(pos).getDOJ());
        intentDoctorCall.putExtra(AppConstants.PAN, usersData.get(pos).getPAN());
        intentDoctorCall.putExtra(AppConstants.DOB, usersData.get(pos).getDOB());
        intentDoctorCall.putExtra(AppConstants.ADDRESS1, usersData.get(pos).getAddress1());
        intentDoctorCall.putExtra(AppConstants.ADDRESS2, usersData.get(pos).getAddress2());
        intentDoctorCall.putExtra(AppConstants.ADDRESS3, usersData.get(pos).getAddress3());
        intentDoctorCall.putExtra(AppConstants.ADDRESS4, usersData.get(pos).getAddress4());
        intentDoctorCall.putExtra(AppConstants.DISTRICT, usersData.get(pos).getDistrict());
        intentDoctorCall.putExtra(AppConstants.STATE, usersData.get(pos).getState());
        intentDoctorCall.putExtra(AppConstants.CITY, usersData.get(pos).getCity());
        intentDoctorCall.putExtra(AppConstants.PINCODE, usersData.get(pos).getPincode());
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
            deleteUserApi(token, userId+"" );
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
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                processDialog.dismissDialog();
                if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                    getFragmentManager().beginTransaction().detach(UsersFragment.this).attach(UsersFragment.this).commit();
                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                processDialog.dismissDialog();

                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
