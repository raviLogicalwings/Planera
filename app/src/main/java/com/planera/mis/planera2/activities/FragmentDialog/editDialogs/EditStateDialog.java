package com.planera.mis.planera2.activities.FragmentDialog.editDialogs;


import android.annotation.SuppressLint;
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
import android.widget.Toast;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.FragmentDialog.BaseDialogFragment;
import com.planera.mis.planera2.activities.models.MainResponse;
import com.planera.mis.planera2.activities.models.States;
import com.planera.mis.planera2.activities.utils.AppConstants;
import com.planera.mis.planera2.activities.utils.InternetConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditStateDialog  extends BaseDialogFragment implements View.OnClickListener{
    private View view;
    private TextInputLayout inputLayoutUserName;
    private EditText editTextName;
    private Button buttonStateUpdate;
    public int STATE_ID ;
    private States states;
    public OnDismissEditDialogListener onDismissEditDialogListener;


    public EditStateDialog() {
    }
    @SuppressLint("ValidFragment")


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            onDismissEditDialogListener = (OnDismissEditDialogListener) getTargetFragment();
        }catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement OnAddFriendListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_edit_state, container, false);
        initUi();
        initData();
        return view;
    }


    public void uiValidation(){
        inputLayoutUserName.setError(null);
        String strState = editTextName.getText().toString().trim();

        if (TextUtils.isEmpty(strState)){
            inputLayoutUserName.setError("State name is required.");
            editTextName.requestFocus();
        }
        else{
            if (InternetConnection.isNetworkAvailable(mContext)) {
                editStateApi(token, STATE_ID, strState);
            }
            else
            {

            }
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;

    }


    public void editStateApi(String token, int id, String name){
        processDialog.showDialog(mContext, false);
        Call<MainResponse> call = apiInterface.updateStateDetails   (token, id, name);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                processDialog.dismissDialog();
                if (response!= null){
                    if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                        Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        onDismissEditDialogListener.onDismissEditDialog();
                        dismiss();
                    }
                    else{
                        Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                processDialog.dismissDialog();
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    protected void initUi() {
        super.initUi();

        inputLayoutUserName = view.findViewById(R.id.input_layout_user_name);
        editTextName = view.findViewById(R.id.edit_text_name);
        buttonStateUpdate = view.findViewById(R.id.button_state_update);
        buttonStateUpdate.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        super.initData();
        states = new States();
        getDialog().setTitle("Add State");
        Bundle bundle = getArguments();
        String item = bundle.getString("item");
        STATE_ID = bundle.getInt("id");

        if (item!=null)
        editTextName.setText(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_state_update:
                uiValidation();
                break;
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

    public interface OnDismissEditDialogListener{
        public void onDismissEditDialog();
    }
}
