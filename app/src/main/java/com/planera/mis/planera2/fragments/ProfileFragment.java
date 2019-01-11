package com.planera.mis.planera2.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.ActivityChangePassword;
import com.planera.mis.planera2.models.UserData;
import com.planera.mis.planera2.utils.AppConstants;

import static com.planera.mis.planera2.utils.AppConstants.USER_PROFILE;


public class ProfileFragment extends BaseFragment implements View.OnClickListener{
    private View view;
    private TextView textUserName;
    private TextView textUserEmail;
    private TextView textUserPhone;
    private TextView textUserAddress;
    private Button buttonChangePassword;

    public static ProfileFragment instance;

    public static ProfileFragment newInstance(){
        if (instance == null){
            instance = new ProfileFragment();
        }
        return instance;
    }


    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.fragment_profile, container, false);
        initUi();
        initData();
        return view;
    }

    @Override
    protected void initUi() {
        super.initUi();


        textUserName = view.findViewById(R.id.text_user_name);
        textUserEmail = view.findViewById(R.id.text_user_email);
        textUserPhone = view.findViewById(R.id.text_user_phone);
        textUserAddress = view.findViewById(R.id.text_user_address);
        buttonChangePassword = view.findViewById(R.id.button_change_password);

        Button buttonChangePassword = view.findViewById(R.id.button_change_password);
        buttonChangePassword.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        super.initData();
        String userData = connector.getString(USER_PROFILE);
        UserData userDetails = new Gson().fromJson(userData, UserData.class);

        if (userDetails != null){
            textUserName.setText(userDetails.getFirstName());
            textUserEmail.setText(userDetails.getEmail1());
            textUserPhone.setText(userDetails.getPhone1());
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_change_password:
                Intent intentChangePassword = new Intent(mContext, ActivityChangePassword.class);
                mContext.startActivity(intentChangePassword);
                break;
        }
    }



    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
