package com.planera.mis.planera2.activities.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.planera.mis.planera2.activities.Retrofit.ApiClient;
import com.planera.mis.planera2.activities.Retrofit.ApiInterface;
import com.planera.mis.planera2.activities.utils.AppConstants;
import com.planera.mis.planera2.activities.utils.PreferenceConnector;
import com.planera.mis.planera2.activities.utils.ProcessDialog;

public class BaseFragment extends Fragment {
    protected View rootView;
    protected FragmentManager manager;
    private ProgressDialog progressDialog;
    public ProcessDialog processDialog;
    public PreferenceConnector connector;
    public String token;
    public Context mContext;
    public ApiInterface apiInterface;

    protected void initData() {
        manager = getFragmentManager();
        rootView = getActivity().getWindow().getDecorView().getRootView();
        connector = PreferenceConnector.getInstance(getContext());
        token = connector.getString(AppConstants.TOKEN);
        mContext = getContext();
        processDialog = new ProcessDialog();
        apiInterface = ApiClient.getInstance();
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
