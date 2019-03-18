package com.planera.mis.planera2.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.planera.mis.planera2.R;
import com.planera.mis.planera2.models.GooglePlacesModel.GooglePlaces;
import com.planera.mis.planera2.models.MainResponse;
import com.planera.mis.planera2.models.UserData;
import com.planera.mis.planera2.utils.AppConstants;
import com.planera.mis.planera2.utils.InternetConnection;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityUpdateUser extends BaseActivity implements View.OnClickListener{
    private EditText textUserFirstName;
    private EditText textUserMiddleName;
    private EditText textUserLastName;
    private EditText textUserLoginId;
    private EditText textUserDob;
    private EditText textUserDoj;
    private EditText textUserEmail;
    private EditText textUserEmail2;
    private EditText textUserQualification;
    private EditText textUserExperience;
    private EditText textUserPan;
    private EditText textUserPhone;
    private EditText textUserPhone2;
    private EditText textUserAddress1;
    private EditText textUserAddress2;
    private EditText textUserAddress3;
    private EditText textUserAddress4;
    private EditText textUserDistrict;
    private EditText textUserCity;
    private EditText textUserState;
    private EditText textUserPincode;
    private Button buttonAddUser;
    private LinearLayout layoutUserUpdate;
    private Spinner spinnerUserType;
    
    private String firstNameStr;
    private String middleNameStr;
    private String lastNameStr;
    private String email1Str;
    private String email2Str;
    private String dobStr;
    private String qualificationStr;
    private String dojStr;
    private String phone1Str;
    private String phone2Str;
    private String experienceStr;
    private String panStr;
    private String address1Str;
    private String address2Str;
    private String address3Str;
    private String address4Str;
    private String districtStr;
    private String cityStr;
    private String stateStr;
    private String pincodeStr;
    private String strLoginId;
    private int userid;
    private UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
        initUi();
        initData();

    }

    @Override
    public void initData() {
        super.initData();
        initUserTypeArray();
        userData = new UserData();

    }

    @Override
    public void initUi() {
        super.initUi();
        Intent intent = getIntent();
        Toolbar toolbar = findViewById(R.id.toolbar);

        textUserFirstName = findViewById(R.id.text_user_first_name);
        textUserMiddleName = findViewById(R.id.text_user_middle_name);
        textUserLastName = findViewById(R.id.text_user_last_name);
        textUserLoginId = findViewById(R.id.text_user_loginId);
        textUserDob = findViewById(R.id.text_user_dob);
        textUserDoj = findViewById(R.id.text_user_doj);
        textUserEmail = findViewById(R.id.text_user_email);
        textUserEmail2 = findViewById(R.id.text_user_email2);
        textUserQualification = findViewById(R.id.text_user_qualification);
        textUserExperience = findViewById(R.id.text_user_experience);
        textUserPan = findViewById(R.id.text_user_pan);
        textUserPhone = findViewById(R.id.text_user_phone);
        textUserPhone2 = findViewById(R.id.text_user_phone2);
        textUserAddress1 = findViewById(R.id.text_user_address1);
        textUserAddress2 = findViewById(R.id.text_user_address2);
        textUserAddress3 = findViewById(R.id.text_user_address3);
        textUserAddress4 = findViewById(R.id.text_user_address4);
        textUserDistrict = findViewById(R.id.text_user_district);
        textUserCity = findViewById(R.id.text_user_city);
        textUserState = findViewById(R.id.text_user_state);
        textUserPincode = findViewById(R.id.text_user_pincode);
        buttonAddUser = findViewById(R.id.button_add_user);
        layoutUserUpdate = findViewById(R.id.layout_user_update);
        spinnerUserType = findViewById(R.id.spinner_user_type);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        getSupportActionBar().setTitle("Update User");
        buttonAddUser.setText(getString(R.string.update));
        textUserDoj.setOnClickListener(this);
        textUserDob.setOnClickListener(this);
        buttonAddUser.setOnClickListener(this);
        if (intent != null) {
            loadFromIntent(intent);
        }

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ActivityUpdateUser.this, SingleListActivity.class);
        intent.putExtra(AppConstants.KEY_TOUCHED_FRAGMENT, AppConstants.USER_FRAGMENT);
        startActivity(intent);
    }


    public void initUserTypeArray(){
        List<String> listUserType = new ArrayList<>();
        listUserType.add("MR");
        listUserType.add("ZM");
        listUserType.add("AM");

        setArrayAdapter(listUserType, spinnerUserType);
    }

    public void setArrayAdapter(List<String> listString, Spinner spinner) {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item,
                        listString);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

    }

    public void uiValidation() {


        firstNameStr = textUserFirstName.getText().toString().trim();
        middleNameStr = textUserMiddleName.getText().toString().trim();
        lastNameStr = textUserLastName.getText().toString().trim();
        email1Str = textUserEmail.getText().toString().trim();
        email2Str = textUserEmail2.getText().toString().trim();
        qualificationStr = textUserQualification.getText().toString().trim();
        experienceStr = textUserExperience.getText().toString().trim();
        phone1Str = textUserPhone.getText().toString().trim();
        phone2Str = textUserPhone2.getText().toString().trim();
        dobStr = textUserDob.getText().toString().trim();
        address1Str = textUserAddress1.getText().toString().trim();
        address2Str = textUserAddress2.getText().toString().trim();
        address3Str = textUserAddress3.getText().toString().trim();
        address4Str = textUserAddress4.getText().toString().trim();
        dojStr = textUserDoj.getText().toString().trim();
        districtStr = textUserDistrict.getText().toString().trim();
        stateStr = textUserState.getText().toString().trim();
        cityStr = textUserCity.getText().toString().trim();
        pincodeStr = textUserPincode.getText().toString().trim();
        dobStr = textUserDob.getText().toString().trim();
        strLoginId = textUserLoginId.getText().toString().trim();

        switch (spinnerUserType.getSelectedItemPosition()){
            case 0:
                userData.setType(AppConstants.MR_USER_TYPE);
                break;
            case 1:
                userData.setType(AppConstants.ZM_USER_TYPE);
                break;
            case 2:
                userData.setType(AppConstants.AM_USER_TYPE);
                break;
        }


        if (TextUtils.isEmpty(firstNameStr)) {
            textUserFirstName.requestFocus();
            textUserFirstName.setError(getString(R.string.invalid_input));

        } else if (TextUtils.isEmpty(lastNameStr)) {
            textUserLastName.requestFocus();
            textUserLastName.setError(getString(R.string.invalid_input));
        }
        else if (TextUtils.isEmpty(strLoginId)) {
            textUserLoginId.requestFocus();
            textUserLoginId.setError(getString(R.string.invalid_input));
        } else {

            if (userid != 0) {
                userData.setUserId(userid+"");
                userData.setFirstName(firstNameStr);
                userData.setMiddleName(middleNameStr);
                userData.setLastName(lastNameStr);
                userData.setDOB(dobStr);
                userData.setPhone1(phone1Str);
                userData.setPhone2(phone2Str);
                userData.setEmail1(email1Str);
                userData.setEmail2(email2Str);
                userData.setQualifications(qualificationStr);
                userData.setExperienceYear(experienceStr);
                userData.setAddress1(address1Str);
                userData.setAddress2(address2Str);
                userData.setAddress3(address3Str);
                userData.setPAN(panStr);
                userData.setDOJ(dojStr);
                userData.setQualifications(qualificationStr);
                userData.setDistrict(districtStr);
                userData.setCity(cityStr);
                userData.setState(stateStr);
                userData.setPincode(pincodeStr);
                userData.setStatus(1);


                if (InternetConnection.isNetworkAvailable(ActivityUpdateUser.this)) {

                    getAddressLatLong(address1Str + ", " + pincodeStr);

                } else {
                    Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
                }

            }



        }

    }

    public void loadFromIntent(Intent intent) {

        textUserFirstName.setText(intent.getStringExtra(AppConstants.FIRST_NAME));
        textUserMiddleName.setText(intent.getStringExtra(AppConstants.MIDDLE_NAME));
        textUserLastName.setText(intent.getStringExtra(AppConstants.LAST_NAME));
        textUserLoginId.setText(intent.getStringExtra(AppConstants.LOGIN_ID));
        textUserDob.setText(intent.getStringExtra(AppConstants.DOB));
        textUserDoj.setText(intent.getStringExtra(AppConstants.DOJ));
        textUserEmail.setText(intent.getStringExtra(AppConstants.EMAIL1));
        textUserEmail2.setText(intent.getStringExtra(AppConstants.EMAIL2));
        textUserPhone.setText(intent.getStringExtra(AppConstants.PHONE1));
        textUserPhone2.setText(intent.getStringExtra(AppConstants.PHONE2));
        textUserQualification.setText(intent.getStringExtra(AppConstants.QUALIFICATION));
        textUserExperience.setText(intent.getStringExtra(AppConstants.EXPERIENCE_YEAR));
        textUserAddress1.setText(intent.getStringExtra(AppConstants.ADDRESS1));
        textUserAddress2.setText(intent.getStringExtra(AppConstants.ADDRESS2));
        textUserAddress3.setText(intent.getStringExtra(AppConstants.ADDRESS3));
        textUserAddress4.setText(intent.getStringExtra(AppConstants.ADDRESS4));
        textUserDistrict.setText(intent.getStringExtra(AppConstants.DISTRICT));
        textUserCity.setText(intent.getStringExtra(AppConstants.CITY));
        textUserState.setText(intent.getStringExtra(AppConstants.STATE));
        textUserPincode.setText(intent.getStringExtra(AppConstants.PINCODE));
        textUserPan.setText(intent.getStringExtra(AppConstants.PAN));
        int userType = intent.getIntExtra(AppConstants.USER_TYPE, 0);
        switch (userType){
            case AppConstants.MR_USER_TYPE:
                spinnerUserType.setSelection(0);
                break;
            case AppConstants.ZM_USER_TYPE:
                spinnerUserType.setSelection(1);
                break;
            case AppConstants.AM_USER_TYPE:
                spinnerUserType.setSelection(2);
                break;
                default:
                    spinnerUserType.setSelection(0);

        }

        userid = intent.getIntExtra(AppConstants.UPDATE_USER_KEY, 0);
    }



    public void getAddressLatLong(String input) {
        processDialog.showDialog(ActivityUpdateUser.this, false);
        Call<GooglePlaces> call = apbInterfaceForGooglePlaces.getPlaceLatLong(input, AppConstants.INPUT_TYPE, AppConstants.FIELDS, AppConstants.KEY_GOOGLE_PLACES);
        call.enqueue(new Callback<GooglePlaces>() {
            @Override
            public void onResponse(@NonNull Call<GooglePlaces> call, @NonNull Response<GooglePlaces> response) {
                processDialog.dismissDialog();

                if (response.body().getStatus().equals(AppConstants.STATUS_OK)) {
                    if (userData != null) {
                        userData.setLatitude(response.body().getCandidates().get(0).getGeometry().getLocation().getLat() + "");
                        userData.setLatitude(response.body().getCandidates().get(0).getGeometry().getLocation().getLng() + "");
                        updateUserApi(token, userData);
                    }
                } else if (response.body().getStatus().equals(AppConstants.STATUS_ZERO_RESULTS)) {
                    Snackbar.make(rootView, "Address not found, Please Try again", Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(rootView, "Query Over Limit ! You have exceeded your daily request quota for this API. " +
                            "If you did not set a custom daily request quota, verify your project has an active billing account: http://g.co/dev/maps-no-account", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GooglePlaces> call, @NonNull Throwable t) {
                processDialog.dismissDialog();
                Toasty.error(ActivityUpdateUser.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void updateUserApi(String token ,UserData data){
        processDialog.showDialog(ActivityUpdateUser.this, false);
        Call<MainResponse> call = apiInterface.updateUserDetails(token, data);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(@NonNull Call<MainResponse> call, @NonNull Response<MainResponse> response) {
                processDialog.dismissDialog();
                if (response.code()== 400){
                    try {
                        Toasty.error(mContext, Objects.requireNonNull(response.errorBody()).string(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toasty.error(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    if (response.isSuccessful()){
                        if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                            Toast.makeText(ActivityUpdateUser.this, response.body().getMessage() , Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ActivityUpdateUser.this, SingleListActivity.class);
                            intent.putExtra(AppConstants.KEY_TOUCHED_FRAGMENT, AppConstants.USER_FRAGMENT);
                            startActivity(intent);
                            finish();

                        }
                        else{
                            Toast.makeText(ActivityUpdateUser.this, response.body().getMessage() , Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                processDialog.dismissDialog();
                Toast.makeText(ActivityUpdateUser.this, t.getMessage() , Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void getDateFromDialog(TextView textView) {
        Calendar dateOfBirth = Calendar.getInstance();
        int year = dateOfBirth.get(Calendar.YEAR);
        int month = dateOfBirth.get(Calendar.MONTH);
        int day = dateOfBirth.get(Calendar.DATE);
        DatePickerDialog.OnDateSetListener listener = (view, year1, monthOfYear, dayOfMonth) -> {
            monthOfYear++;
            String dateOfBirthString = year1 + "-" + monthOfYear + "-" + dayOfMonth;
            textView.setText(dateOfBirthString);
        };
        DatePickerDialog dpDialog = new DatePickerDialog(ActivityUpdateUser.this, listener, year, month, day);
        dpDialog.show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_add_user:
                uiValidation();
                break;

            case R.id.text_user_dob:
                getDateFromDialog(textUserDob);
                break;

            case R.id.text_user_doj:
                getDateFromDialog(textUserDoj);
                break;

        }
    }
}

