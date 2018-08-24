package com.planera.mis.planera2.activities.FragmentDialog.addDialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.FragmentDialog.BaseDialogFragment;
import com.planera.mis.planera2.activities.models.MainResponse;
import com.planera.mis.planera2.activities.utils.AppConstants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddStateDialog  extends BaseDialogFragment implements View.OnClickListener{
    private View view;
    private Dialog dialog;
    private TextInputLayout inputLayoutUserName;
    private EditText editTextName;
    private Button buttonStateAdd;
    private Spinner farziSpinner;
    public OnStateDialogDismissListener onDialogDismissListener;

    public AddStateDialog() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            onDialogDismissListener = (OnStateDialogDismissListener) getActivity();
        }
        catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement OnAddFriendListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_add_state, container, false);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
        initUi();
        initData();
        return view;
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

    public void uiValidation(){
        inputLayoutUserName.setError(null);
        String strState = editTextName.getText().toString();

        if (TextUtils.isEmpty(strState)){
          inputLayoutUserName.setError(getString(R.string.invalid_input));
          editTextName.requestFocus();
        }
        else{

            addStateApi(token, strState);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);

    }


    public void addStateApi(String token, String states){
        processDialog.showDialog(mContext, false);
        Call<MainResponse> call = apiInterface.addState(token, states);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                processDialog.dismissDialog();
                if (response!= null){
                    if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                        Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        onDialogDismissListener.onDialogDismiss();
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

        inputLayoutUserName = view.findViewById(R.id.input_layout_user_name);
        editTextName = view.findViewById(R.id.edit_text_name);
        buttonStateAdd = view.findViewById(R.id.button_state_add);
        farziSpinner = view.findViewById(R.id.spinner_state);
        buttonStateAdd.setOnClickListener(this);

        farziSpinner.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        super.initData();
       getDialog().setTitle("Add State");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_state_add:
                uiValidation();
                break;
        }
    }

    public interface OnStateDialogDismissListener {
        public void onDialogDismiss();
    }
}
