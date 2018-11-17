package com.planera.mis.planera2.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.models.GooglePlacesModel.GooglePlaces;
import com.planera.mis.planera2.activities.models.MainResponse;
import com.planera.mis.planera2.activities.models.UserData;
import com.planera.mis.planera2.activities.utils.AppConstants;
import com.planera.mis.planera2.activities.utils.InternetConnection;

import java.util.Calendar;

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
    String firstNameStr, middleNameStr, lastNameStr, email1Str, email2Str, dobStr, qualificationStr, dojStr, phone1Str, phone2Str,
            experienceStr, panStr, address1Str, address2Str, address3Str, address4Str, districtStr, cityStr, stateStr, pincodeStr,
            strLoginId;
    int userid;
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
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

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


        if (TextUtils.isEmpty(firstNameStr)) {
            textUserFirstName.requestFocus();
            textUserFirstName.setError(getString(R.string.invalid_input));

        } else if (TextUtils.isEmpty(lastNameStr)) {
            textUserLastName.requestFocus();
            textUserLastName.setError(getString(R.string.invalid_input));
        } else if (TextUtils.isEmpty(dobStr)) {
            textUserEmail.requestFocus();
            textUserEmail.setError(getString(R.string.invalid_input));
        } else if (TextUtils.isEmpty(qualificationStr)) {
            textUserQualification.requestFocus();
            textUserQualification.setError(getString(R.string.invalid_input));
        }
        else if (TextUtils.isEmpty(phone1Str)) {
            textUserPhone.requestFocus();
            textUserPhone.setError(getString(R.string.invalid_input));
        }
        else if (TextUtils.isEmpty(dobStr)) {
            textUserDob.requestFocus();
            textUserDob.setError(getString(R.string.invalid_input));
        }
        else if (TextUtils.isEmpty(dojStr)) {
            textUserDoj.requestFocus();
            textUserDoj.setError(getString(R.string.invalid_input));
        }
        else if (TextUtils.isEmpty(address1Str)) {
            textUserAddress1.requestFocus();
            textUserAddress1.setError(getString(R.string.invalid_input));
        }
        else if (TextUtils.isEmpty(address2Str)) {
            textUserAddress2.requestFocus();
            textUserAddress2.setError(getString(R.string.invalid_input));
        }
        else if (TextUtils.isEmpty(districtStr)) {
            textUserDistrict.requestFocus();
            textUserDistrict.setError(getString(R.string.invalid_input));
        }
        else if (TextUtils.isEmpty(cityStr)) {
            textUserCity.requestFocus();
            textUserCity.setError(getString(R.string.invalid_input));

        }
        else if (TextUtils.isEmpty(stateStr)) {
            textUserState.requestFocus();
            textUserState.setError(getString(R.string.invalid_input));
        }
        else if (TextUtils.isEmpty(pincodeStr)) {
            textUserPincode.requestFocus();
            textUserPincode.setError(getString(R.string.invalid_input));
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
        userid = intent.getIntExtra(AppConstants.UPDATE_USER_KEY, 0);
    }



    public void getAddressLatLong(String input) {
        processDialog.showDialog(ActivityUpdateUser.this, false);
        Call<GooglePlaces> call = apbInterfaceForGooglePlaces.getPlaceLatLong(input, AppConstants.INPUT_TYPE, AppConstants.FIELDS, AppConstants.KEY_GOOGLE_PLACES);
        call.enqueue(new Callback<GooglePlaces>() {
            @Override
            public void onResponse(Call<GooglePlaces> call, Response<GooglePlaces> response) {
                processDialog.dismissDialog();
                Log.e("AddDoctorActivity", "onResponse: " + new Gson().toJson(response.body()));
                if (response.body().getStatus().equals(AppConstants.STATUS_OK)) {
                    if (userData != null) {
                        userData.setLatitude(response.body().getCandidates().get(0).getGeometry().getLocation().getLat() + "");
                        userData.setLatitude(response.body().getCandidates().get(0).getGeometry().getLocation().getLng() + "");
                        Log.e("Doctors Object", "onResponse: " + new Gson().toJson(userData));
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
            public void onFailure(Call<GooglePlaces> call, Throwable t) {
                processDialog.dismissDialog();
                Log.e("AddDoctorActivity", "onFailure: " + t.getMessage());
                Toast.makeText(ActivityUpdateUser.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void updateUserApi(String token ,UserData data){
        processDialog.showDialog(ActivityUpdateUser.this, false);
        Call<MainResponse> call = apiInterface.updateUserDetails(token, data);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                processDialog.dismissDialog();
                if (response.code()== 400){
                    try {
                        Toast.makeText(mContext, response.errorBody().string(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.e(" ", "onResponse: "+ e.getMessage());
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

