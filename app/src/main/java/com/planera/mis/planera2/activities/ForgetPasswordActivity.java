package com.planera.mis.planera2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.planera.mis.planera2.FragmentDialog.ResetPasswordDialog;
import com.planera.mis.planera2.R;
import com.planera.mis.planera2.models.MainResponse;
import com.planera.mis.planera2.models.UserData;
import com.planera.mis.planera2.utils.AppConstants;
import com.planera.mis.planera2.utils.InternetConnection;

import org.apache.xmlbeans.InterfaceExtension;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private EditText editEmail;
    private EditText editOtp;
    private EditText editNewPassword;
    private EditText editConfirmPassword;
    private Button buttonSubmitReqForForget, buttonSubmitOtp;
    private UserData userData;
    private LinearLayout layoutOtpForget, layoutEmailForget;

    private ResetPasswordDialog resetPasswordDialog;
    String strEditEmail;
    String strOTP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initUi();
        initData();
    }

    @Override
    public void initUi() {
        super.initUi();
        toolbar = findViewById(R.id.toolbar_forget_password);
        editEmail = findViewById(R.id.edit_forget_email);
        editOtp = findViewById(R.id.edit_otp);
        editNewPassword = findViewById(R.id.edit_new_password);
        editConfirmPassword = findViewById(R.id.edit_confirm_password);
        layoutEmailForget = findViewById(R.id.layoutEmailForget);
        buttonSubmitReqForForget= findViewById(R.id.button_submit_req_for_forget);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back_arrow_whit));
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        buttonSubmitReqForForget.setOnClickListener(this);


    }

    @Override
    public void initData() {
        super.initData();
        resetPasswordDialog = new ResetPasswordDialog();
    }

    public void callForgetPasswordApi(String email){
        Call<MainResponse> call = apiInterface.forgetPassword( email);
        processDialog.showDialog(ForgetPasswordActivity.this, false);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(@NonNull Call<MainResponse> call, @NonNull Response<MainResponse> response) {
                processDialog.dismissDialog();
                if (response.isSuccessful()){
                    assert response.body() != null;
                    if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                        Toasty.success(ForgetPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        resetPasswordDialog.show(getSupportFragmentManager(), "Reset password");
                    }
                    else{
                        Toasty.error(ForgetPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MainResponse> call, @NonNull Throwable t) {
                processDialog.dismissDialog();
                Log.e("Error", t.getMessage());
                Toasty.error(ForgetPasswordActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public boolean isEmailValid(String target){
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public void uiValidation(){
      strEditEmail = editEmail.getText().toString().trim();
      if (!TextUtils.isEmpty(strEditEmail) && isEmailValid(strEditEmail)){
          if (InternetConnection.isNetworkAvailable(ForgetPasswordActivity.this)){

              callForgetPasswordApi(strEditEmail);
          }
          else
          {
              Snackbar.make(rootView, R.string.no_internet, Snackbar.LENGTH_LONG).show();
          }
      }
      if (TextUtils.isEmpty(strEditEmail)){
          Toasty.error(ForgetPasswordActivity.this, "Email Can't be empty.", Toast.LENGTH_SHORT).show();
      }
      if (!isEmailValid(strEditEmail)){
          Toasty.error(ForgetPasswordActivity.this, "Please enter a valid email address..", Toast.LENGTH_SHORT).show();

      }
    }





    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_submit_req_for_forget:
             uiValidation();
             break;
            case R.id.button_submit_otp:
//                validateOtp();
             break;
        }

    }
}
