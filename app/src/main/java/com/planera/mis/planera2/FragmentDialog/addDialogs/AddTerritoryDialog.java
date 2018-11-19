package com.planera.mis.planera2.FragmentDialog.addDialogs;

import android.app.Dialog;
import android.os.Bundle;
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
import android.widget.Toast;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.FragmentDialog.BaseDialogFragment;
import com.planera.mis.planera2.adapters.CustomSpinnerTerritoryAdapter;
import com.planera.mis.planera2.models.MainResponse;
import com.planera.mis.planera2.models.StateListResponse;
import com.planera.mis.planera2.models.States;
import com.planera.mis.planera2.utils.AppConstants;
import com.planera.mis.planera2.utils.InternetConnection;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTerritoryDialog extends BaseDialogFragment implements View.OnClickListener{
    private View view;
    private Spinner spinnerStates;
    private Dialog dialog;
    private TextInputLayout inputLayoutUserName;
    private EditText editTextName;
    private Button buttonStateAdd;
    private CustomSpinnerTerritoryAdapter customSpinnerAdapter;
    private List<States> statesList;
    int stateId;
    private OnAddTerritoryDialogDismissListener onAddTerritoryDialogDismissListener;

    public AddTerritoryDialog() {
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            onAddTerritoryDialogDismissListener = (OnAddTerritoryDialogDismissListener) getActivity();
        }
        catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement OnAddFriendListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_add_territory, container, false);
        initUi();
        initData();
        return view;
    }


    public void uiValidation(){
        inputLayoutUserName.setError(null);
        String strTerritory = editTextName.getText().toString().trim();

        if (TextUtils.isEmpty(strTerritory)){
            inputLayoutUserName.setError("Territory name is required.");
            editTextName.requestFocus();
        }
        else{
            if(InternetConnection.isNetworkAvailable(mContext)){
                addTerritoryApi(token, strTerritory, stateId);
            }
            else{
                Snackbar.make(getActivity().getWindow().getDecorView().getRootView(), getString(R.string.no_internet), Snackbar.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;

    }


    public void addTerritoryApi(String token, String name, int stateId){
        processDialog.showDialog(mContext, false);
        Call<MainResponse> call = apiInterface.addTerritory(token, name, stateId);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                processDialog.dismissDialog();
                if (response!= null){
                    if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                        Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        onAddTerritoryDialogDismissListener.onAddTerritoryDialogDismiss();
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
        spinnerStates = view.findViewById(R.id.spinner_state);
        inputLayoutUserName = view.findViewById(R.id.input_layout_user_name);
        editTextName = view.findViewById(R.id.edit_text_name);
        buttonStateAdd = view.findViewById(R.id.button_state_add);

        buttonStateAdd.setOnClickListener(this);
    }


    public void getStateList(String token){
        Call<StateListResponse> call = apiInterface.statesListApi(token);
        call.enqueue(new Callback<StateListResponse>() {
            @Override
            public void onResponse(Call<StateListResponse> call, Response<StateListResponse> response) {
                if (response.body().getStatusCode()== AppConstants.RESULT_OK){
                    statesList = response.body().getData();
                  customSpinnerAdapter = new CustomSpinnerTerritoryAdapter(mContext, statesList);
                  spinnerStates.setAdapter(customSpinnerAdapter);

                }
                else{
                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<StateListResponse> call, Throwable t) {
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    protected void initData() {
        super.initData();
        getDialog().setTitle("Add Territory");
        getStateList(token);
        onSpinerItemClicked();

    }


    public int onSpinerItemClicked(){

        spinnerStates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("You select ","=====> "+statesList.get(i).getStateId());
                stateId= statesList.get(i).getStateId();
            }

            @Override            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return stateId;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_state_add:
                uiValidation();
                break;
        }
    }

    public interface OnAddTerritoryDialogDismissListener{
        public void onAddTerritoryDialogDismiss();
    }
}
