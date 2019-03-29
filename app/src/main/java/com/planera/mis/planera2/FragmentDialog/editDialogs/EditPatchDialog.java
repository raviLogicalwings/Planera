package com.planera.mis.planera2.FragmentDialog.editDialogs;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.planera.mis.planera2.R;
import com.planera.mis.planera2.FragmentDialog.BaseDialogFragment;
import com.planera.mis.planera2.adapters.CustomSpinnerPatchAdapter;
import com.planera.mis.planera2.models.MainResponse;
import com.planera.mis.planera2.models.Territories;
import com.planera.mis.planera2.models.TerritoryListResponse;
import com.planera.mis.planera2.utils.AppConstants;
import com.planera.mis.planera2.utils.InternetConnection;

import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPatchDialog extends BaseDialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = EditPatchDialog.class.getSimpleName() ;
    public OnDismissEditPatchDialogListener listener;


    private View view;
    private TextInputLayout inputLayoutUserName;
    private EditText editTextName;
    private Button buttonUpdatePatch;
    private Spinner spinnerTerritory;

    private List<Territories> territoryList;
    private CustomSpinnerPatchAdapter spinnerPatchAdapter;

    private int patchId;
    private int territoryId ;
    private String patchName;

    public EditPatchDialog() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            listener = (OnDismissEditPatchDialogListener) getTargetFragment();
        }catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement OnAddFriendListener");
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_edit_patch, container, false);
        initUi();
        initData();
        return view;
    }


    public void uiValidation(){
        inputLayoutUserName.setError(null);
        String strState = editTextName.getText().toString().trim();

        if (TextUtils.isEmpty(strState)){
            inputLayoutUserName.setError("Patch name is required");
            editTextName.requestFocus();
        }
        else{
            if (InternetConnection.isNetworkAvailable(mContext)) {
                editPatchApi(token, patchId, strState, territoryId);
            }
            else
            {
                Toasty.warning(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;

    }


    public void editPatchApi(String token, int patchId, String name, int territoryId){
        processDialog.showDialog(mContext, false);
        Call<MainResponse> call = apiInterface.updatePatchDetails   (token,  patchId, name, territoryId);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(@NonNull Call<MainResponse> call, @NonNull Response<MainResponse> response) {
                processDialog.dismissDialog();
                if (response!= null){
                    if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                        Log.e(TAG, "onResponse: "+new Gson().toJson(response.body()));
                        Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        listener.onDismissEditPatchDialog();
                        dismiss();
                    }
                    else{
                        Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MainResponse> call, Throwable t) {
                processDialog.dismissDialog();
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }


    public void getTerritoryList(String token) {
        processDialog.showDialog(mContext, false);
        Call<TerritoryListResponse> call = apiInterface.territoryList(token);
        call.enqueue(new Callback<TerritoryListResponse>() {
            @Override
            public void onResponse(@NonNull Call<TerritoryListResponse> call, @NonNull Response<TerritoryListResponse> response) {
                processDialog.dismissDialog();
                if (Objects.requireNonNull(response.body()).getStatusCode() == AppConstants.RESULT_OK) {
                    territoryList = Objects.requireNonNull(response.body()).getTerritorysList();
                    spinnerPatchAdapter = new CustomSpinnerPatchAdapter(mContext, territoryList);
                    spinnerTerritory.setAdapter(spinnerPatchAdapter);
                    for (int i = 0 ; i< territoryList.size(); i++)
                    {
                        if (territoryList.get(i).getTerritoryId() == territoryId){
                            spinnerTerritory.setSelection(i);
                        }
                    }

                } else {
                    Toast.makeText(mContext, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<TerritoryListResponse> call, Throwable t) {
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void initUi() {
        super.initUi();
        inputLayoutUserName = view.findViewById(R.id.input_layout_user_name);
        editTextName = view.findViewById(R.id.edit_text_name);
        buttonUpdatePatch = view.findViewById(R.id.button_update_patch);
        spinnerTerritory = view.findViewById(R.id.spinner_territory_update_patch);

        spinnerTerritory.setOnItemSelectedListener(this);

        buttonUpdatePatch.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        super.initData();
        Bundle bundle = getArguments();
        patchId = bundle.getInt(AppConstants.KEY_PATCH_ID);
        patchName = bundle.getString(AppConstants.KEY_PATCH_NAME);
        territoryId = bundle.getInt(AppConstants.KEY_TERRITORY_ID);

        if (patchName!=null){
            editTextName.setText(patchName);
            editTextName.setSelection(editTextName.getText().length());
        }
        if (InternetConnection.isNetworkAvailable(mContext)){
            getTerritoryList(token);
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_update_patch:
                uiValidation();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        territoryId = territoryList.get(i).getTerritoryId();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public interface OnDismissEditPatchDialogListener {
        void onDismissEditPatchDialog();
    }
}
