package com.planera.mis.planera2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.planera.mis.planera2.R;
import com.planera.mis.planera2.Retrofit.ApiClient;
import com.planera.mis.planera2.Retrofit.ApiInterface;
import com.planera.mis.planera2.models.RegistrationResponse;
import com.planera.mis.planera2.models.UserData;
import com.planera.mis.planera2.utils.AppConstants;
import com.planera.mis.planera2.utils.InternetConnection;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends BaseActivity implements View.OnClickListener {
    private TextInputLayout inputLayoutUserName;
    private TextInputEditText editUserName;
    private TextInputLayout inputLayoutPassword;
    private TextInputEditText editPassword;
    private TextInputLayout inputLayoutConfirmPassword;
    private TextInputEditText editConfirmPassword;
    private TextInputLayout inputLayoutEmail;
    private TextInputEditText editEmail;
    private TextInputLayout inputLayoutMobile;
    private TextInputEditText editMobile;
    private Button buttonSignIn;
    private TextView textSignUp;
    private ApiInterface apiInterface;
    private String userNameStr;
    private String passwordStr;
    private String confirmPasswordStr;
    private String phoneStr;
    private String emailStr;
    public UserData user;
    public static int ON_HOLD = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initUi();
        initData();
    }

    @Override
    public void initUi() {
        super.initUi();
        inputLayoutUserName = findViewById(R.id.input_layout_user_name);
        editUserName = findViewById(R.id.edit_user_name);
        inputLayoutPassword = findViewById(R.id.input_layout_password);
        editPassword = findViewById(R.id.edit_password);
        inputLayoutConfirmPassword = findViewById(R.id.input_layout_confirm_password);
        editConfirmPassword = findViewById(R.id.edit_confirm_password);
        inputLayoutEmail = findViewById(R.id.input_layout_email);
        editEmail = findViewById(R.id.edit_email);
        inputLayoutMobile = findViewById(R.id.input_layout_mobile);
        editMobile = findViewById(R.id.edit_mobile);

        buttonSignIn = findViewById(R.id.button_sign_in);
        textSignUp = findViewById(R.id.text_sign_up);


        buttonSignIn.setOnClickListener(this);
        textSignUp.setOnClickListener(this);

    }

    @Override
    public void initData() {
        super.initData();
        user = new UserData();
        apiInterface = ApiClient.getInstance();
    }

    public void inputValidation(){
        userNameStr = editUserName.getText().toString();
        emailStr = editEmail.getText().toString();
        phoneStr = editMobile.getText().toString();
        passwordStr = editPassword.getText().toString();
        confirmPasswordStr = editConfirmPassword.getText().toString();
        if (TextUtils.isEmpty(userNameStr)){
            inputLayoutUserName.setError("Username is required. *");
        }

        else if (TextUtils.isEmpty(passwordStr)){
            inputLayoutPassword.setError(" Password is required. *");
        }
        else if (passwordStr.length()<8){
            inputLayoutPassword.setError("Minimum 8 characters required. *");
        }
        else if (TextUtils.isEmpty(confirmPasswordStr)){
            inputLayoutConfirmPassword.setError("Confirm Password is required. *");
        }
        else if (confirmPasswordStr.length()<8){
            inputLayoutConfirmPassword.setError("Minimum 8 characters required. *");
        }
        else if(!passwordStr.equals(confirmPasswordStr)){
            inputLayoutConfirmPassword.setError("Password does not match.");
        }
        else if (TextUtils.isEmpty(emailStr)){
            inputLayoutEmail.setError("Email is required. *");
        }
        else if (!isEmailValid(emailStr)){
            inputLayoutEmail.setError("Invalid email address. *");
        }

        else if (TextUtils.isEmpty(phoneStr)){
            inputLayoutMobile.setError("Phone number is required. *");
        }

        else if (phoneStr.length()<10){
            inputLayoutMobile.setError("Invalid phone number. *");
        }

        else{
            user.setLoginId(emailStr);
            user.setFirstName(userNameStr);
            user.setEmail1(emailStr);
            user.setPhone1(phoneStr);
            user.setPassword(passwordStr);
            user.setStatus(ON_HOLD);
            user.setType(AppConstants.MR_USER_TYPE);
            try {
                if (InternetConnection.isNetworkAvailable(SignupActivity.this)){
                    callUserRegistrationApi(user);
                }
                else {
                    Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
                }
            }catch (Exception e){
                System.out.print(e.getCause() );
            }

        }
    }

    public boolean isEmailValid(String target){
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    public void callUserRegistrationApi(UserData user){
        Log.e("User Data", new Gson().toJson(user));
        Call<RegistrationResponse> call = apiInterface.userRegistrationApi(user);
        call.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(@NonNull Call<RegistrationResponse> call, @NonNull Response<RegistrationResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                        connector.setBoolean(AppConstants.IS_LOGIN, true);
                        Toasty.success(SignupActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        Intent intentMain = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intentMain);
                    }
                    else{
                        Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("response", response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                Snackbar.make(rootView,  t.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_sign_in:
                inputValidation();
                break;

            case R.id.text_sign_up:
                backToLogin();
                break;

        }

    }

    private void backToLogin() {
        Intent intentLogin =  new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intentLogin);
    }
}
