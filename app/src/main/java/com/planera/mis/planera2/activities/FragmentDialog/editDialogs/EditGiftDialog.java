package com.planera.mis.planera2.activities.FragmentDialog.editDialogs;



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
import com.planera.mis.planera2.activities.FragmentDialog.BaseDialogFragment;
import com.planera.mis.planera2.activities.models.MainResponse;
import com.planera.mis.planera2.activities.utils.AppConstants;
import com.planera.mis.planera2.activities.utils.InternetConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditGiftDialog extends BaseDialogFragment implements View.OnClickListener{
    private View view;
    public OnDismissEditGiftDialogListener listener;
    private TextInputLayout inputLayoutUserName;
    private EditText editTextName;
    private TextView textTerritoryPatch;
    private Button buttonUpdateGift;
    private int giftId;
    private String giftName;





    public EditGiftDialog() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            listener = (OnDismissEditGiftDialogListener) getTargetFragment();
        }catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement OnAddFriendListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_edit_gift, container, false);
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
        String strState = editTextName.getText().toString().trim();

        if (TextUtils.isEmpty(strState)){
            inputLayoutUserName.setError("Gift name is required.");
            editTextName.requestFocus();
        }
        else{
            if (InternetConnection.isNetworkAvailable(mContext)) {
                editGiftApi(token, giftId, strState);
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


    public void editGiftApi(String token, int giftId, String name){
        processDialog.showDialog(mContext, false);
        Call<MainResponse> call = apiInterface.updateGItDetails   (token,  giftId, name);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                processDialog.dismissDialog();
                if (response!= null){
                    if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                        Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        listener.onDismissEditGiftDialog();
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


        textTerritoryPatch = view.findViewById(R.id.text_territory_patch);
        inputLayoutUserName = view.findViewById(R.id.input_layout_user_name);
        editTextName = view.findViewById(R.id.edit_text_name);
        buttonUpdateGift = view.findViewById(R.id.button_update_gift);
        textTerritoryPatch.setVisibility(View.GONE);


        buttonUpdateGift.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        super.initData();
        getDialog().setTitle("Update Details");
        Bundle bundle = getArguments();
        giftId = bundle.getInt(AppConstants.KEY_GIFT_ID);
        giftName = bundle.getString(AppConstants.KEY_GIFT_NAME);


        if (giftName!=null){
            editTextName.setText(giftName);
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_update_gift:
                uiValidation();
                break;
        }
    }

    public interface OnDismissEditGiftDialogListener {
        void onDismissEditGiftDialog();
    }
}
