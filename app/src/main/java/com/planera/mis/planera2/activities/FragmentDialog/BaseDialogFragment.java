package com.planera.mis.planera2.activities.FragmentDialog;


import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.DialogFragment;

import com.planera.mis.planera2.activities.Retrofit.ApiClient;
import com.planera.mis.planera2.activities.Retrofit.ApiInterface;
import com.planera.mis.planera2.activities.utils.AppConstants;
import com.planera.mis.planera2.activities.utils.PreferenceConnector;
import com.planera.mis.planera2.activities.utils.ProcessDialog;


public class BaseDialogFragment extends DialogFragment {
    private ProgressDialog progressDialog;
    public PreferenceConnector connector;
    public Context mContext;
    public ProcessDialog processDialog;
    protected ApiInterface apiInterface;
    protected String token;

    protected void initUi() {

    }

    protected void initData(){
        connector = PreferenceConnector.getInstance(getActivity());
        mContext = getActivity();
        apiInterface = ApiClient.getInstance();
        token = connector.getString(AppConstants.TOKEN);
        processDialog= new ProcessDialog();
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
