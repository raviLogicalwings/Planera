package com.planera.mis.planera2.activities.FragmentDialog.addDialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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

public class AddProductDialog extends BaseDialogFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{
    public OnAddProductDialogDismissListener onAddProductDialogDismissListener;
    private View view;
    private Switch switchIsBrand;
    private TextInputLayout inputLayoutUserName;
    private EditText editTextName;
    private Button buttonAddProduct;
    int isBrand;

    public AddProductDialog(){
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.dialog_add_product, container, false);
       initUi();
       initData();
       return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            onAddProductDialogDismissListener = (OnAddProductDialogDismissListener) getActivity();
        }
        catch (Exception e) {
            throw new ClassCastException("Calling Fragment must implement OnAddFriendListener");
        }
    }

    @Override
    protected void initData() {
        super.initData();
        getDialog().setTitle("Add Product");
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


    public void addProductApi(String token , String name , int isBrand){
        processDialog.showDialog(mContext, false);
        Call<MainResponse> call = apiInterface.addProduct(token, name, isBrand);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                processDialog.dismissDialog();
                if(response.body().getStatusCode()  == AppConstants.RESULT_OK){
                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    onAddProductDialogDismissListener.onAddProductDialogDismiss();
                    dismiss();

                }
                else{
                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
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

        switchIsBrand = view.findViewById(R.id.switch_is_brand);
        inputLayoutUserName = view.findViewById(R.id.input_layout_user_name);
        editTextName = view.findViewById(R.id.edit_text_name);
        buttonAddProduct = view.findViewById(R.id.button_add_product);
        buttonAddProduct.setOnClickListener(this);
        switchIsBrand.setOnCheckedChangeListener(this);

    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_add_product:
                uiValidation();
                break;

        }

    }

    private void uiValidation() {
        inputLayoutUserName.setError(null);
        String productStr= editTextName.getText().toString().trim();


        if (!TextUtils.isEmpty(productStr)){
            if (InternetConnection.isNetworkAvailable(mContext)){
                addProductApi(token, productStr, isBrand);
            }
            else{
                Snackbar.make(buttonAddProduct, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
            }

        }
        else {
            inputLayoutUserName.setError("Product name is required.");
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()){
            case R.id.switch_is_brand:
                if (b){
                    isBrand = 1;
                    Toast.makeText(mContext, "True", Toast.LENGTH_SHORT).show();
                }
                else{
                    isBrand = 0;
                    Toast.makeText(mContext, "False", Toast.LENGTH_SHORT).show();
                }
        }

    }

    public interface OnAddProductDialogDismissListener {
        void onAddProductDialogDismiss();
    }
}
