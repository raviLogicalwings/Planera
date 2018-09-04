package com.planera.mis.planera2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.Retrofit.ApiClient;
import com.planera.mis.planera2.activities.Retrofit.ApiInterface;
import com.planera.mis.planera2.activities.models.LoginResponse;
import com.planera.mis.planera2.activities.models.UserData;
import com.planera.mis.planera2.activities.utils.AppConstants;
import com.planera.mis.planera2.activities.utils.InternetConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private TextInputLayout inputLayoutUserName;
    private EditText editUserName;
    private TextInputLayout inputLayoutPassword;
    private EditText editPassword;
    private ApiInterface apiInterface;
    private UserData userData;
    public boolean isUserLogin;

    @Override
    public void initData() {
        super.initData();
        apiInterface = ApiClient.getInstance();
        userData = new UserData();
        isUserLogin = connector.getBoolean(AppConstants.IS_LOGIN);
    }

    @Override
    public void initUi() {
        super.initUi();
        ImageView appIcon = findViewById(R.id.appIcon);
        inputLayoutUserName = findViewById(R.id.input_layout_user_name);
        editUserName = findViewById(R.id.edit_user_name);
        inputLayoutPassword = findViewById(R.id.input_layout_password);
        editPassword = findViewById(R.id.edit_password);
        Button buttonSignIn = findViewById(R.id.button_sign_in);
        TextView textSignUp = findViewById(R.id.text_sign_up);
        TextView textForgetPassword = findViewById(R.id.text_forget_password);
        buttonSignIn.setOnClickListener(this);
        textForgetPassword.setOnClickListener(this);
        textSignUp.setOnClickListener(this);
    }


    public void uiValidation(){
        inputLayoutPassword.setError(null);
        inputLayoutUserName.setError(null);
        String emailIdStr = editUserName.getText().toString();
        String passwordStr = editPassword.getText().toString();
            if(!isEmailValid(emailIdStr)){
                inputLayoutUserName.setError(getString(R.string.invalid_input));
            }
            else if(TextUtils.isEmpty(passwordStr)){
               inputLayoutPassword.setError(getString(R.string.invalid_input));
            }
            else{
                userData.setLoginId(emailIdStr);
                userData.setPassword(passwordStr);

                if (InternetConnection.isNetworkAvailable(LoginActivity.this)){
                    callUserLoginApi(userData);
                }
                else
                {
                    Snackbar.make(rootView, R.string.no_internet, Snackbar.LENGTH_LONG).show();
                }

            }


    }

    public boolean isEmailValid(String target){
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public void callUserLoginApi(UserData userData){
//        showProgressDialog("Xyz", "Loading...");
        Call<LoginResponse> call = apiInterface.userLoginApi(userData);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                processDialog.dismissDialog();
                if (response.body().getStatusCode()== AppConstants.RESULT_OK){
//                    Toast.makeText(LoginActivity.this, response.body().getData().getToken(), Toast.LENGTH_LONG).show();
                    connector.setString(AppConstants.TOKEN, response.body().getData().getToken());
                    connector.setBoolean(AppConstants.IS_LOGIN, true);
                    Intent intentHome = new Intent(LoginActivity.this, AdminPanelActivity.class);
                    startActivity(intentHome);
                }
                else{
                    Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                processDialog.dismissDialog();
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUi();
        initData();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.text_sign_up:
                Intent intentSignup = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intentSignup);
                break;
            case R.id.button_sign_in:
               uiValidation();
                break;

            case R.id.text_forget_password:
                Intent intentForget = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intentForget);
        }

    }
}
