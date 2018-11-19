package com.planera.mis.planera2.FragmentDialog.addDialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.FragmentDialog.BaseDialogFragment;
import com.planera.mis.planera2.models.MainResponse;
import com.planera.mis.planera2.utils.AppConstants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddGiftDialog  extends BaseDialogFragment implements View.OnClickListener{
    public OnAddGiftDialogDismissListener onAddGiftDialogDismissListener;
    private View view;
    private Dialog dialog;
    private TextInputLayout inputLayoutUserName;
    private EditText editTextName;
    private Button buttonStateAdd;
    private Spinner spinner;

    public AddGiftDialog(){}



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            onAddGiftDialogDismissListener = (OnAddGiftDialogDismissListener) getActivity();
        } catch (Exception e) {
            throw new ClassCastException("Calling Fragment must implement OnAddFriendListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_add_state, container, false);
        initUi();
        initData();
        return view;
    }



    public void uiValidation(){
        inputLayoutUserName.setError(null);
        String strGift = editTextName.getText().toString().trim();

        if (TextUtils.isEmpty(strGift)){
            inputLayoutUserName.setError("Gift name is required.");
            editTextName.requestFocus();
        }
        else{

            addGiftApi(token, strGift);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
       Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }


    public void addGiftApi(String token, String gift){
        processDialog.showDialog(mContext, false);
        Call<MainResponse> call = apiInterface.addGift(token, gift);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                processDialog.dismissDialog();
                if (response!= null){
                    if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                        Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        onAddGiftDialogDismissListener.onAddGiftDialogDismiss();
                        dismiss();

                    }
                    else{
                        Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    protected void initUi() {
        super.initUi();
        spinner  =view.findViewById(R.id.spinner_state);
        inputLayoutUserName = view.findViewById(R.id.input_layout_user_name);
        editTextName = view.findViewById(R.id.edit_text_name);
        buttonStateAdd = view.findViewById(R.id.button_state_add);
        inputLayoutUserName.setHint(getString(R.string.enter_gift_name));
        spinner.setVisibility(View.GONE);
        buttonStateAdd.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        dialog = new Dialog(mContext);
        getDialog().setTitle("Add Gift");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_state_add:
                uiValidation();
                break;
        }
    }

    public interface OnAddGiftDialogDismissListener {
        void onAddGiftDialogDismiss();
    }

}
