package com.planera.mis.planera2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.models.MainResponse;
import com.planera.mis.planera2.models.UserData;
import com.planera.mis.planera2.utils.AppConstants;

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
        buttonSubmitOtp = findViewById(R.id.button_submit_otp);
        layoutEmailForget = findViewById(R.id.layoutEmailForget);
        layoutOtpForget = findViewById(R.id.layoutOtpForget);
        buttonSubmitReqForForget= findViewById(R.id.button_submit_req_for_forget);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back_arrow_whit));
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        buttonSubmitReqForForget.setOnClickListener(this);
        buttonSubmitOtp.setOnClickListener(this);

    }

    @Override
    public void initData() {
        super.initData();
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
                    Log.e("Api Response", response.body().toString());
                    if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                        layoutOtpForget.setVisibility(View.VISIBLE);
                        layoutEmailForget.setVisibility(View.GONE);
                        Toasty.success(ForgetPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toasty.error(ForgetPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MainResponse> call, @NonNull Throwable t) {
                processDialog.dismissDialog();
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
          callForgetPasswordApi(strEditEmail);
      }
      if (TextUtils.isEmpty(strEditEmail)){
          Toasty.error(ForgetPasswordActivity.this, "Email Can't be empty.", Toast.LENGTH_SHORT).show();
      }
      if (!isEmailValid(strEditEmail)){
          Toasty.error(ForgetPasswordActivity.this, "Please enter a valid email address..", Toast.LENGTH_SHORT).show();

      }
    }


    public void validateOtp(){
        String newPasswordStr = editNewPassword.getText().toString().trim();
        String confirmPasswordStr = editConfirmPassword.getText().toString().trim();
        String otpStr=  editOtp.getText().toString().trim();
        if (TextUtils.isEmpty(otpStr)){
            Toasty.error(ForgetPasswordActivity.this, "OTP can't be empty.", Toast.LENGTH_SHORT).show();

        }
        else if(otpStr.length() < 6){

            Toasty.error(ForgetPasswordActivity.this, "Please enter correct OTP.", Toast.LENGTH_SHORT).show();

        }

        else if(TextUtils.isEmpty(newPasswordStr)){
            Toasty.error(ForgetPasswordActivity.this, "New password field can't be empty.", Toast.LENGTH_SHORT).show();

        }
        else if (newPasswordStr.length() <8){
            Toasty.error(ForgetPasswordActivity.this, "Minimum 8 characters.", Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(confirmPasswordStr)){
            Toasty.error(ForgetPasswordActivity.this, "Confirm password field can't be empty.", Toast.LENGTH_SHORT).show();

        }
        else if (confirmPasswordStr.length() <8){
            Toasty.error(ForgetPasswordActivity.this, "Minimum 8 characters.", Toast.LENGTH_SHORT).show();

        }
        else{
            userData = new UserData();
            userData.setOtp(otpStr);
            userData.setPassword(newPasswordStr);
            userData.setConfirmPassword(confirmPasswordStr);
            callSetPasswordApi(token, userData);

        }

    }

    public void callSetPasswordApi(String token, UserData userData){
        processDialog.showDialog(ForgetPasswordActivity.this, false);
        Call<MainResponse> call = apiInterface.setPassword(token, userData);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(@NonNull Call<MainResponse> call, @NonNull Response<MainResponse> response) {
                processDialog.dismissDialog();
                if (response.isSuccessful()){
                    if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                        Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                        startActivity(intent);
                        Toasty.success(ForgetPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toasty.error(ForgetPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                processDialog.dismissDialog();
                Toasty.error(ForgetPasswordActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_submit_req_for_forget:
             uiValidation();
             break;
            case R.id.button_submit_otp:
                validateOtp();
             break;
        }

    }
}
