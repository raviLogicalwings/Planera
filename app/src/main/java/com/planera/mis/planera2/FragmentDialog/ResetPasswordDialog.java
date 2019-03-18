package com.planera.mis.planera2.FragmentDialog;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.LoginActivity;
import com.planera.mis.planera2.models.MainResponse;
import com.planera.mis.planera2.models.UserData;
import com.planera.mis.planera2.utils.AppConstants;
import com.planera.mis.planera2.utils.InternetConnection;

import java.util.Objects;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordDialog extends BaseDialogFragment implements View.OnClickListener {
    private TextView textInfoText;
    private EditText editOtp;
    private EditText editNewPassword;
    private EditText editConfirmPassword;
    private Button buttonSubmitOtp;

    private View view;

    private UserData userData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_reset_password, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUi();
    }

    @Override
    protected void initUi() {
        super.initUi();
        textInfoText = view.findViewById(R.id.text_info_text);
        editOtp = view.findViewById(R.id.edit_otp);
        editNewPassword = view.findViewById(R.id.edit_new_password);
        editConfirmPassword = view.findViewById(R.id.edit_confirm_password);
        buttonSubmitOtp = view.findViewById(R.id.button_submit_otp);

        buttonSubmitOtp.setOnClickListener(this);
    }

    public void validateOtp(){
        String newPasswordStr = editNewPassword.getText().toString().trim();
        String confirmPasswordStr = editConfirmPassword.getText().toString().trim();
        String otpStr=  editOtp.getText().toString().trim();
        if (TextUtils.isEmpty(otpStr)){
            Toasty.error(mContext, "OTP can't be empty.", Toast.LENGTH_SHORT).show();

        }
        else if(otpStr.length() < 6){

            Toasty.error(mContext, "Please enter correct OTP.", Toast.LENGTH_SHORT).show();

        }

        else if(TextUtils.isEmpty(newPasswordStr)){
            Toasty.error(mContext, "New password field can't be empty.", Toast.LENGTH_SHORT).show();

        }
        else if (newPasswordStr.length() <8){
            Toasty.error(mContext, "Minimum 8 characters.", Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(confirmPasswordStr)){
            Toasty.error(mContext, "Confirm password field can't be empty.", Toast.LENGTH_SHORT).show();

        }
        else if (confirmPasswordStr.length() <8){
            Toasty.error(mContext, "Minimum 8 characters.", Toast.LENGTH_SHORT).show();

        }
        else{
            userData = new UserData();
            userData.setOtp(otpStr);
            userData.setPassword(newPasswordStr);
            userData.setConfirmPassword(confirmPasswordStr);
            if (InternetConnection.isNetworkAvailable(mContext)){
                callSetPasswordApi(token, userData);
            }
            else
                {
                Toasty.warning(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG).show();
            }

        }

    }


    public void callSetPasswordApi(String token, UserData userData){
        processDialog.showDialog(mContext, false);
        Call<MainResponse> call = apiInterface.setPassword(token, userData);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(@NonNull Call<MainResponse> call, @NonNull Response<MainResponse> response) {
                processDialog.dismissDialog();
                if (response.isSuccessful()){
                    if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        startActivity(intent);
                        Toasty.success(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toasty.error(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MainResponse> call, @NonNull Throwable t) {
                processDialog.dismissDialog();
                Toasty.error(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Objects.requireNonNull(dialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_submit_otp:
                validateOtp();
                break;
        }
    }
}
