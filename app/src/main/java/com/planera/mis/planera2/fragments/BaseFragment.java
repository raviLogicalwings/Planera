package com.planera.mis.planera2.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.planera.mis.planera2.Retrofit.ApiClient;
import com.planera.mis.planera2.Retrofit.ApiInterface;
import com.planera.mis.planera2.utils.AppConstants;
import com.planera.mis.planera2.utils.PreferenceConnector;
import com.planera.mis.planera2.utils.ProcessDialog;

public class BaseFragment extends Fragment {
    protected View rootView;
    protected FragmentManager manager;
    private ProgressDialog progressDialog;
    public ProcessDialog processDialog;
    public PreferenceConnector connector;
    public String token;
    public Context mContext;
    public ApiInterface apiInterface;
    public Bundle bundle;

    protected void initData() {
        manager = getFragmentManager();
        rootView = getActivity().getWindow().getDecorView().getRootView();
        connector = PreferenceConnector.getInstance(getContext());
        token = connector.getString(AppConstants.TOKEN);
        mContext = getContext();
        processDialog = new ProcessDialog();
        apiInterface = ApiClient.getInstance();
        bundle = new Bundle();
    }

    protected void initUi() {

    }


    protected void showProgressDialog(String title, String message) {

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        progressDialog = new ProgressDialog(getActivity());

        if (title != null)
            progressDialog.setTitle(title);

        progressDialog.setMessage(message);

        progressDialog.setCancelable(false);

        progressDialog.show();

    }

    protected void cancelProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
