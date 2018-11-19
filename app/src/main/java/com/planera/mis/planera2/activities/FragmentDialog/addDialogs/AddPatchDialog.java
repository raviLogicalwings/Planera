package com.planera.mis.planera2.activities.FragmentDialog.addDialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.FragmentDialog.BaseDialogFragment;
import com.planera.mis.planera2.activities.adapters.CustomSpinnerPatchAdapter;
import com.planera.mis.planera2.activities.models.MainResponse;
import com.planera.mis.planera2.activities.models.Territories;
import com.planera.mis.planera2.activities.models.TerritoryListResponse;
import com.planera.mis.planera2.activities.utils.AppConstants;


import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class AddPatchDialog extends BaseDialogFragment implements View.OnClickListener{
    public OnAddPatchDialogDismissListener onAddPatchDialogDismissListener;
    private View view;
    private Spinner spinnerTerritory;
    private Dialog dialog;
    private TextInputLayout inputLayoutUserName;
    private EditText editTextPatch;
    private Button buttonPatchAdd;
    private CustomSpinnerPatchAdapter spinnerPatchAdapter;
    private List<Territories> patchesList;
    int patchId;

    public AddPatchDialog() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            onAddPatchDialogDismissListener = (OnAddPatchDialogDismissListener) getActivity();
        }catch (Exception e){
            throw new ClassCastException("Calling Fragment must implement onAddPatchDialogDismissListener");
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
        String strTerritory = editTextPatch.getText().toString().trim();

        if (TextUtils.isEmpty(strTerritory)){
            inputLayoutUserName.setError("Patch name is required.");
            editTextPatch.requestFocus();
        }
        else{
            addPatchApi(token, strTerritory, patchId);
        }

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Objects.requireNonNull(dialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;

    }
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);
        }
    }


    public void addPatchApi(String token, String name, int territoryId){
        processDialog.showDialog(mContext, false);
        Call<MainResponse> call = apiInterface.addPatch(token, name, territoryId);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(@NonNull Call<MainResponse> call, @NonNull Response<MainResponse> response) {
               processDialog.dismissDialog();
                if (response!= null){
                    if (response.body() != null) {
                        if (Objects.requireNonNull(response.body()).getStatusCode() == AppConstants.RESULT_OK){
                            Log.e(TAG, "onResponse: "+ Objects.requireNonNull(response.body()).getMessage() );
                            onAddPatchDialogDismissListener.onAddPatchPatchDialogDismiss();
                            dismiss();
                        }
                        else{
                            Toast.makeText(mContext, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
                        }
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
        spinnerTerritory = view.findViewById(R.id.spinner_state);
        inputLayoutUserName = view.findViewById(R.id.input_layout_user_name);
        editTextPatch = view.findViewById(R.id.edit_text_name);
        buttonPatchAdd = view.findViewById(R.id.button_state_add);
        inputLayoutUserName.setHint(getString(R.string.enter_patch_name));
        buttonPatchAdd.setOnClickListener(this);
    }


    public void getTerritoryList(String token){
        processDialog.showDialog(mContext, false);
        Call<TerritoryListResponse> call = apiInterface.territoryList(token);
        call.enqueue(new Callback<TerritoryListResponse>() {
            @Override
            public void onResponse(Call<TerritoryListResponse> call, Response<TerritoryListResponse> response) {
                processDialog.dismissDialog();
                if (Objects.requireNonNull(response.body()).getStatusCode()== AppConstants.RESULT_OK){
                    patchesList = Objects.requireNonNull(response.body()).getTerritorysList();
                    spinnerPatchAdapter = new CustomSpinnerPatchAdapter(mContext, patchesList);
                    spinnerTerritory.setAdapter(spinnerPatchAdapter);

                }
                else{
                    Toast.makeText(mContext, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<TerritoryListResponse> call, Throwable t) {
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    protected void initData() {
        super.initData();
        getDialog().setTitle("Add Patch");
        getTerritoryList(token);
        onSpinnerItemClicked();

    }


    public int onSpinnerItemClicked(){

        spinnerTerritory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("You select ","=====> "+patchesList.get(i).getStateId());
                patchId= patchesList.get(i).getTerritoryId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        return patchId;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_state_add:
                uiValidation();
                break;
        }
    }

    public interface OnAddPatchDialogDismissListener {
        void onAddPatchPatchDialogDismiss();
    }
}
