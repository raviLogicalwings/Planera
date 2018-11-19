package com.planera.mis.planera2.FragmentDialog.editDialogs;


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
import android.widget.TextView;
import android.widget.Toast;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.FragmentDialog.BaseDialogFragment;
import com.planera.mis.planera2.models.MainResponse;
import com.planera.mis.planera2.models.Territories;
import com.planera.mis.planera2.utils.AppConstants;
import com.planera.mis.planera2.utils.InternetConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditTerritoryDialog extends BaseDialogFragment implements View.OnClickListener{
    private View view;
    private TextView textStateEdit;
    private TextInputLayout inputLayoutUserName;
    private EditText editTextName;
    private Button buttonTerritoryUpdate;
    public int territoryId, stateId ;
    private Territories territories;
    public OnDismissEditTerritoryDialogListener onDismissEditDialogListener;

    public EditTerritoryDialog() {
    }
    @SuppressLint("ValidFragment")


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            onDismissEditDialogListener = (OnDismissEditTerritoryDialogListener) getTargetFragment();
        }catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement OnAddFriendListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_edit_territory, container, false);
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
        String strTerritory = editTextName.getText().toString().trim();

        if (TextUtils.isEmpty(strTerritory)){
            inputLayoutUserName.setError("Territory name is required.");
            editTextName.requestFocus();
        }
        else{
            if (InternetConnection.isNetworkAvailable(mContext)) {
                updateTerritoryDetailsApi(token, territoryId, stateId , strTerritory);
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


    public void updateTerritoryDetailsApi(String token, int territoryId, int stateId,  String name){
        processDialog.showDialog(mContext, false);
        Call<MainResponse> call = apiInterface.updateTerritoryDetails   (token,territoryId, stateId, name);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                processDialog.dismissDialog();
                if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                        Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        onDismissEditDialogListener.onDismissEditTerritoryDialog();
                        dismiss();
                    }
                    else{
                        Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
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


        textStateEdit = view.findViewById(R.id.text_state_edit);
        inputLayoutUserName = view.findViewById(R.id.input_layout_user_name);
        editTextName = view.findViewById(R.id.edit_text_name);
        buttonTerritoryUpdate = view.findViewById(R.id.button_territory_update);
        buttonTerritoryUpdate.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        super.initData();
        territories = new Territories();
        getDialog().setTitle("Update Territories");
        Bundle bundle = getArguments();
        String territoryName = bundle.getString(AppConstants.KEY_TERRITORY_NAME);
        territoryId = bundle.getInt(AppConstants.KEY_TERRITORY_ID);
        stateId = bundle.getInt(AppConstants.KEY_STATE_ID);
        String stateName = bundle.getString(AppConstants.KEY_STATE_NAME);

        if (territoryName!=null && stateName!=null) {
         textStateEdit.setText(stateName);
            editTextName.setText(territoryName);
            editTextName.setSelection(editTextName.getText().length());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_territory_update:
                uiValidation();
                break;
        }
    }

    public interface OnDismissEditTerritoryDialogListener {
        void onDismissEditTerritoryDialog();
    }
}
