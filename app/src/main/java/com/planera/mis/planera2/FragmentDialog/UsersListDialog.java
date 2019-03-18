package com.planera.mis.planera2.FragmentDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.adapters.AreaManagersAdapter;
import com.planera.mis.planera2.adapters.UserListPatchesAdapter;
import com.planera.mis.planera2.models.AMs;
import com.planera.mis.planera2.models.MRs;
import com.planera.mis.planera2.models.RoleWiseUsersResponse;
import com.planera.mis.planera2.utils.AppConstants;
import com.planera.mis.planera2.utils.InternetConnection;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.planera.mis.planera2.utils.AppConstants.JOINT_WORKING;
import static com.planera.mis.planera2.utils.AppConstants.PATCH_ID;

public class UsersListDialog extends BaseDialogFragment implements View.OnClickListener {

    private View view;
    protected RecyclerView recyclerViewUsers;
    protected RecyclerView recyclerViewAreaManager;
    protected Button buttonSelectJoint;

    private AreaManagersAdapter areaManagersAdapter;
    private UserListPatchesAdapter adapterMr;

    private List<MRs> mRsList;
    private List<AMs> listAM;
    private List<MRs> jointMembersList;


    private int patchId = 0;
    private int selectedMrPos;
    private int selectedAMPos;

    private OnSelectUserListener onSelectUserListener;
    private AreaManagersAdapter.OnSelcetAreaManagerListener onSelcetAreaManagerListener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initData();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog   dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assert getArguments() != null;
        if (getArguments().getInt(AppConstants.KEY_IS_JOINT) == JOINT_WORKING){
            try{
//                onSelectUserListenerFragment = (OnSelectUserListener) getTargetFragment();
                onSelectUserListener = (OnSelectUserListener)getActivity();
            }catch (ClassCastException e) {
                throw new ClassCastException("Calling Fragment must implement OnAddFriendListener");
            }
        }
        else{
            try{
                onSelectUserListener = (OnSelectUserListener) getTargetFragment();
//                onSelectUserListenerActivity = (OnSelectUserListener) getActivity();
            }catch (ClassCastException e) {
                throw new ClassCastException("Calling Fragment must implement OnAddFriendListener");
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_select:
                if (selectedMrPos != -1){
                    jointMembersList.add(mRsList.get(selectedMrPos));
                }
                if (selectedAMPos != -1 && listAM != null && listAM.size()>0)
                {
                    MRs tempMRs = new MRs();
                    tempMRs.setFirstName(listAM.get(selectedAMPos).getFirstName());
                    tempMRs.setUserId(listAM.get(selectedAMPos).getUserId());
                    jointMembersList.add(tempMRs);
                }
                onSelectUserListener.onSelectUser(jointMembersList);

                    dismiss();
                break;
        }

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.dialog_user_list, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUi();
        updateUi();

    }

    public void updateUi() {
        if (mRsList != null && mRsList.size()>0 ) {
            adapterMr.setMrDataList(mRsList);
            adapterMr.notifyDataSetChanged();

        }
        if(listAM != null && listAM.size()>0){
            areaManagersAdapter.setListAreaManagers(listAM);
            areaManagersAdapter.notifyDataSetChanged();
        }
    }





    @Override
    protected void initData() {
        super.initData();
        mRsList = new ArrayList<>();
        jointMembersList = new ArrayList<>();
        assert getArguments() != null;
        patchId = getArguments().getInt(PATCH_ID);


    }

    @Override
    protected void initUi() {
        super.initUi();

        recyclerViewUsers = view.findViewById(R.id.recycler_view_users);
        recyclerViewAreaManager = view.findViewById(R.id.recycler_view_area_managers);
        buttonSelectJoint = view.findViewById(R.id.button_select);
        buttonSelectJoint.setOnClickListener(this);

        /**
         * set adapter to recyclerview
         * */
        areaManagersAdapter = new AreaManagersAdapter(mContext, position -> selectedAMPos = position);
        adapterMr = new UserListPatchesAdapter(mContext, pos -> selectedMrPos = pos);

        recyclerViewAreaManager.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewAreaManager.setHasFixedSize(true);
        recyclerViewAreaManager.setAdapter(areaManagersAdapter);


        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewUsers.setHasFixedSize(true);
        recyclerViewUsers.setAdapter(adapterMr);

        adapterMr.setMrDataList(mRsList);
        areaManagersAdapter.setListAreaManagers(listAM);

        if (view != null){
            if (InternetConnection.isNetworkAvailable(mContext)){
                getUserListForJointWorking(token,patchId);
            }
            else
            {
                Toasty.warning(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG).show();
            }
        }


    }


    public void getUserListForJointWorking(String token, int patchId)
    {

        Call<RoleWiseUsersResponse> call = apiInterface.userListByPathches(token, patchId);
        call.enqueue(new Callback<RoleWiseUsersResponse>() {

            @Override
            public void onResponse(@NonNull Call<RoleWiseUsersResponse> call, @NonNull Response<RoleWiseUsersResponse> response) {

                Log.e("Working", response.body().getData().getAmDataList()+"");

                if (response.isSuccessful()){
                    if (response.body() != null){
                        if (response.body().getData() != null){
                            mRsList = response.body().getData().getMrDataList();
                            listAM = response.body().getData().getAmDataList();

                        }
                        updateUi();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RoleWiseUsersResponse> call, @NonNull Throwable t) {

               Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


    public interface OnSelectUserListener{
        void onSelectUser(List<MRs>  mrs);
    }
}
