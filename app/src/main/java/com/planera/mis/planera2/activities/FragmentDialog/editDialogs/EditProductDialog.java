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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.FragmentDialog.BaseDialogFragment;
import com.planera.mis.planera2.activities.models.MainResponse;
import com.planera.mis.planera2.activities.utils.AppConstants;
import com.planera.mis.planera2.activities.utils.InternetConnection;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProductDialog extends BaseDialogFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{
    private View view;
    public OnDismissEditProductDialogListener listener;
    private Switch switchIsBrand;
    private TextInputLayout inputLayoutUserName;
    private EditText editTextName;
    private Button buttonUpdateProduct;
    private String productName, isBrands;
    private int productId ;





    public EditProductDialog() {
    }
    @SuppressLint("ValidFragment")


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            listener = (OnDismissEditProductDialogListener) getTargetFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dillog_edit_product, container, false);
        initUi();
        initData();
        return view;
    }


    public void uiValidation(){
        inputLayoutUserName.setError(null);
        String strState = editTextName.getText().toString();

        if (TextUtils.isEmpty(strState)){
            inputLayoutUserName.setError(getString(R.string.invalid_input));
            editTextName.requestFocus();
        }
        else{
            if (InternetConnection.isNetworkAvailable(mContext)) {
                editProductApi(token, productId, strState, isBrands);
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


    public void editProductApi(String token, int id, String name, String isBrands){
        processDialog.showDialog(mContext, false);
        Call<MainResponse> call = apiInterface.updateProductDetails   (token, id, isBrands, name);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                processDialog.dismissDialog();
                if (response!= null){
                    if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                        Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        listener.onDismissEditProductDialog();
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
        switchIsBrand = view.findViewById(R.id.switch_is_brand_edit);
        inputLayoutUserName = view.findViewById(R.id.input_layout_user_name);
        editTextName = view.findViewById(R.id.edit_text_name);
        buttonUpdateProduct = view.findViewById(R.id.button_update_product);

        buttonUpdateProduct.setOnClickListener(this);
        switchIsBrand.setOnCheckedChangeListener(this);

    }

    @Override
    protected void initData() {
        super.initData();
        getDialog().setTitle("Update Details");
        Bundle bundle = getArguments();
        productName = bundle.getString(AppConstants.KEY_PRODUCT_NAME);
        productId = bundle.getInt(AppConstants.KEY_PRODUCT_ID);
        isBrands = bundle.getString(AppConstants.KEY_IS_BRAND);

        if (productName!=null && isBrands!= null){
            editTextName.setText(productName);
            if(Integer.parseInt(isBrands)  == 1){
                switchIsBrand.setChecked(true);
            }
           if (Integer.parseInt(isBrands) == 0){
                switchIsBrand.setChecked(false);
           }
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_update_product:
                uiValidation();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch(buttonView.getId()) {
            case  R.id.switch_is_brand_edit:
            if (isChecked) {
                isBrands = "1";
            } else {
                isBrands = "0";
            }

            break;
        }

    }

    public interface OnDismissEditProductDialogListener {
        void onDismissEditProductDialog();
    }
}
