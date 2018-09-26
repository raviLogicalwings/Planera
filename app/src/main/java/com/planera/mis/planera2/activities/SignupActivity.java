package com.planera.mis.planera2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.Retrofit.ApiClient;
import com.planera.mis.planera2.activities.Retrofit.ApiInterface;
import com.planera.mis.planera2.activities.models.RegistrationResponse;
import com.planera.mis.planera2.activities.models.UserData;
import com.planera.mis.planera2.activities.utils.InternetConnection;

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
    private String userNameStr, passwordStr, confirmPasswordStr, phoneStr, emailStr;
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
        inputLayoutUserName.setError(null);
        inputLayoutPassword.setError(null);
        inputLayoutConfirmPassword.setError(null);
        inputLayoutEmail.setError(null);
        inputLayoutMobile.setError(null);
        userNameStr = editUserName.getText().toString();
        emailStr = editEmail.getText().toString();
        phoneStr = editMobile.getText().toString();
        passwordStr = editPassword.getText().toString();
        confirmPasswordStr = editConfirmPassword.getText().toString();
        if (TextUtils.isEmpty(userNameStr)){
            inputLayoutUserName.setError("*");
            Snackbar.make(rootView, "This field can't be empty", Snackbar.LENGTH_LONG).show();
        }

        else if (TextUtils.isEmpty(passwordStr) || passwordStr.length()<8){
            inputLayoutPassword.setError("*");
            Snackbar.make(rootView, "This field can't be empty", Snackbar.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(confirmPasswordStr) || passwordStr.length()<8){
            inputLayoutConfirmPassword.setError("*");
            Snackbar.make(rootView, "This field can't be empty", Snackbar.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(emailStr)  || !isEmailValid(emailStr)){
            inputLayoutEmail.setError("*");
            Snackbar.make(rootView, getString(R.string.invalid_input), Snackbar.LENGTH_LONG).show();
        }

        else if (TextUtils.isEmpty(phoneStr)){
            inputLayoutMobile.setError("*");
            Snackbar.make(rootView, "This field can't be empty", Snackbar.LENGTH_LONG).show();
        }

        else if (phoneStr.length()<10){
            inputLayoutMobile.setError("*");
            Snackbar.make(rootView, "Enter 10 Digit mobile number.", Snackbar.LENGTH_LONG).show();
        }
        else if(!passwordStr.equals(confirmPasswordStr)  || passwordStr.length()<8){
            inputLayoutConfirmPassword.setError("Password does not matched");
        }
        else{
            user.setLoginId(emailStr);
            user.setFirstName(userNameStr);
            user.setEmail1(emailStr);
            user.setPhone1(phoneStr);
            user.setPassword(passwordStr);
            user.setStatus(ON_HOLD);
            Toast.makeText(this, new Gson().toJson(user), Toast.LENGTH_LONG).show();
            try {
                if (InternetConnection.isNetworkAvailable(SignupActivity.this)){
                callUserRegistrationApi(user);}
                else
                {
                    Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
                }
            }catch (Exception e){
                System.out.print(e.getCause() );
            }

            Intent in = new Intent(SignupActivity.this, AddInputActivity.class);
            startActivity(in);
        }
    }

    public boolean isEmailValid(String target){
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    public void callUserRegistrationApi(UserData user){
        Call<RegistrationResponse> call = apiInterface.userRegistrationApi(user);
        call.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                Toast.makeText(SignupActivity.this, new Gson().toJson(response.body()), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                Toast.makeText(SignupActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
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
