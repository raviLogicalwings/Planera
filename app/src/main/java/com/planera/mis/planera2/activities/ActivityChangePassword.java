package com.planera.mis.planera2.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.models.MainResponse;
import com.planera.mis.planera2.activities.utils.AppConstants;
import com.planera.mis.planera2.activities.utils.InternetConnection;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityChangePassword extends BaseActivity implements View.OnClickListener{
    private Toolbar toolbarChangePassword;
    private TextInputLayout inputLayoutOldPassword;
    private EditText editOldPassword;
    private TextInputLayout inputLayoutConfirmPassword;
    private EditText editConfirmPassword;
    private TextInputLayout inputLayoutNewPassword;
    private EditText editNewPassword;
    private Button buttonChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        initUi();
        initData();


    }

    @Override
    public void initUi() {
        super.initUi();


        toolbarChangePassword = findViewById(R.id.toolbarChangePassword);
        inputLayoutOldPassword = findViewById(R.id.input_layout_old_password);
        editOldPassword = findViewById(R.id.edit_old_password);
        inputLayoutNewPassword = findViewById(R.id.input_layout_new_password);
        editNewPassword = findViewById(R.id.edit_new_password);
        inputLayoutConfirmPassword = findViewById(R.id.input_layout_confirm_password);
        editConfirmPassword = findViewById(R.id.edit_confirm_password);
        buttonChangePassword = findViewById(R.id.button_change_password);

        buttonChangePassword.setOnClickListener(this);
        setSupportActionBar(toolbarChangePassword);
        getSupportActionBar().setTitle("Change Password");
    }

    @Override
    public void initData() {
        super.initData();
    }

    public void uiValidation(){
        String oldPassword = editOldPassword.getText().toString().trim();
        String newPassword = editNewPassword.getText().toString().trim();
        String confirmPassword = editConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(oldPassword)){
            editOldPassword.requestFocus();
            inputLayoutOldPassword.setError(getString(R.string.invalid_input));
        }
        else if (TextUtils.isEmpty(newPassword)){
            editNewPassword.requestFocus();
            inputLayoutNewPassword.setError(getString(R.string.invalid_input));
        }

        else if (TextUtils.isEmpty(confirmPassword)){
            editConfirmPassword.requestFocus();
            inputLayoutConfirmPassword.setError(getString(R.string.invalid_input));
        }

        else if (!newPassword.equals(confirmPassword)){
            editConfirmPassword.requestFocus();
            inputLayoutConfirmPassword.setError("Password does not match.");
        }
        else if (!InternetConnection.isNetworkAvailable(this)){
            Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_SHORT).show();
        }
        else{
            apiChangePassword(token, oldPassword, newPassword, confirmPassword);
        }
    }


    public void apiChangePassword(String token, String oldPassword, String newPassword, String confPassword){
        processDialog.showDialog(this, false);
        Call<MainResponse> call = apiInterface.changePasswordApi(token, oldPassword, newPassword, confPassword);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(@NonNull Call<MainResponse> call, @NonNull Response<MainResponse> response) {
                Log.e("Response: ", new Gson().toJson(response.body()));
                processDialog.dismissDialog();
                if (response.code() == 400){
                    Log.e("Error Body", new Gson().toJson(response.errorBody()));
                }
                if (response.isSuccessful()){
                    if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                      Toasty.success(ActivityChangePassword.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toasty.error(ActivityChangePassword.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MainResponse> call, @NonNull Throwable t) {
                Log.e("Response: ", new Gson().toJson(t));
                processDialog.dismissDialog();
                Toasty.error(ActivityChangePassword.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_change_password:
                uiValidation();
                break;
        }
    }
}
