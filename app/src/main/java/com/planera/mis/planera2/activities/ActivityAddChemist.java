package com.planera.mis.planera2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.models.ChemistResponse;
import com.planera.mis.planera2.activities.models.Chemists;
import com.planera.mis.planera2.activities.models.GooglePlacesModel.GooglePlaces;
import com.planera.mis.planera2.activities.models.PatchListResponse;
import com.planera.mis.planera2.activities.models.Patches;
import com.planera.mis.planera2.activities.utils.AppConstants;
import com.planera.mis.planera2.activities.utils.InternetConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityAddChemist extends BaseActivity implements View.OnClickListener{
    private EditText textChemistCompanyName;
    private EditText textChemistMonthlyVolume;
    private EditText textChemistShopSize;
    private EditText textChemistFirstName;
    private EditText textChemistMiddleName;
    private EditText textChemistLastName;
    private EditText textChemistEmail;
    private Spinner spinnerMeetTime;
    private Spinner spinnerPatchId;
    private EditText textChemistPhone;
    private EditText textChemistAddress1;
    private EditText textChemistAddress2;
    private EditText textChemistAddress3;
    private EditText textChemistAddress4;
    private EditText textChemistDistrict;
    private EditText textChemistCity;
    private EditText textChemistState;
    private EditText textChemistPincode;
    private EditText textChemistBillingPhone1;
    private EditText textChemistBillingPhone2;
    private EditText textChemistBillingEmail;
    private EditText textChemistRating;
    private TextInputLayout inputLayoutCompanyNameChemist;
    private TextInputLayout inputLayoutMonthlyVolumeChemist;
    private TextInputLayout inputLayoutShopSizeChemist;
    private TextInputLayout inputLayoutFirstNameChemist;
    private TextInputLayout inputLayoutMiddleNameChemist;
    private TextInputLayout inputLayoutLastNameChemist;
    private TextInputLayout inputLayoutEmailChemist;
    private TextInputLayout inputLayoutPhoneChemist;
    private TextInputLayout inputLayoutAddress1Chemist;
    private TextInputLayout inputLayoutAddress2Chemist;
    private TextInputLayout inputLayoutAddress3Chemist;
    private TextInputLayout inputLayoutAddress4Chemist;
    private TextInputLayout inputLayoutDistrictChemist;
    private TextInputLayout inputLayoutCityChemist;
    private TextInputLayout inputLayoutStateChemist;
    private TextInputLayout inputLayoutPincodeChemist;
    private TextInputLayout inputLayoutBillingPhone1Chemist;
    private TextInputLayout inputLayoutBillingPhone2Chemist;
    private TextInputLayout inputLayoutBillingEmailChemist;
    private TextInputLayout inputLayoutRatingChemist;
    private List<String> prefferdMeetTime;
    private int meetTime;
    private List<Patches> patches;
    private int patchId;
    private Chemists chemists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chemist);
        initUi();
        initData();
    }

    @Override
    public void initUi() {
        super.initUi();

        Toolbar toolbar = findViewById(R.id.toolbar);


        inputLayoutCompanyNameChemist = findViewById(R.id.input_layout_company_name_chemist);
        inputLayoutMonthlyVolumeChemist = findViewById(R.id.input_layout_monthly_volume_chemist);
        inputLayoutShopSizeChemist = findViewById(R.id.input_layout_shop_size_chemist);
        inputLayoutFirstNameChemist = findViewById(R.id.input_layout_first_name_chemist);
        inputLayoutMiddleNameChemist = findViewById(R.id.input_layout_middle_name_chemist);
        inputLayoutLastNameChemist = findViewById(R.id.input_layout_last_name_chemist);
        inputLayoutEmailChemist = findViewById(R.id.input_layout_email_chemist);
        inputLayoutPhoneChemist = findViewById(R.id.input_layout_phone_chemist);
        inputLayoutAddress1Chemist = findViewById(R.id.input_layout_address_1_chemist);
        inputLayoutAddress2Chemist = findViewById(R.id.input_layout_address_2_chemist);
        inputLayoutAddress3Chemist = findViewById(R.id.input_layout_address_3_chemist);
        inputLayoutAddress4Chemist = findViewById(R.id.input_layout_address_4_chemist);
        inputLayoutDistrictChemist = findViewById(R.id.input_layout_district_chemist);
        inputLayoutCityChemist = findViewById(R.id.input_layout_city_chemist);
        inputLayoutStateChemist = findViewById(R.id.input_layout_state_chemist);
        inputLayoutPincodeChemist = findViewById(R.id.input_layout_pincode_chemist);
        inputLayoutBillingPhone1Chemist = findViewById(R.id.input_layout_billing_phone1_chemist);
        inputLayoutBillingPhone2Chemist = findViewById(R.id.input_layout_billing_phone2_chemist);
        inputLayoutBillingEmailChemist = findViewById(R.id.input_layout_billing_email_chemist);
        inputLayoutRatingChemist = findViewById(R.id.input_layout_rating_chemist);


        textChemistCompanyName = findViewById(R.id.text_chemist_company_name);
        textChemistMonthlyVolume = findViewById(R.id.text_chemist_monthly_volume);
        textChemistShopSize = findViewById(R.id.text_chemist_shop_size);
        textChemistFirstName = findViewById(R.id.text_chemist_first_name);
        textChemistMiddleName = findViewById(R.id.text_chemist_middle_name);
        textChemistLastName = findViewById(R.id.text_chemist_last_name);
        textChemistEmail = findViewById(R.id.text_chemist_email);
        spinnerMeetTime = findViewById(R.id.spinner_meet_time);
        spinnerPatchId = findViewById(R.id.spinner_patch_id);
        textChemistPhone = findViewById(R.id.text_chemist_phone);
        textChemistAddress1 = findViewById(R.id.text_chemist_address1);
        textChemistAddress2 = findViewById(R.id.text_chemist_address2);
        textChemistAddress3 = findViewById(R.id.text_chemist_address3);
        textChemistAddress4 = findViewById(R.id.text_chemist_address4);
        textChemistDistrict = findViewById(R.id.text_chemist_district);
        textChemistCity = findViewById(R.id.text_chemist_city);
        textChemistState = findViewById(R.id.text_chemist_state);
        textChemistPincode = findViewById(R.id.text_chemist_pincode);
        textChemistBillingPhone1 = findViewById(R.id.text_chemist_billing_phone1);
        textChemistBillingPhone2 = findViewById(R.id.text_chemist_billing_phone2);
        textChemistBillingEmail = findViewById(R.id.text_chemist_billing_email);
        textChemistRating = findViewById(R.id.text_chemist_rating);
        Button buttonAddChemist = findViewById(R.id.button_add_chemist);
        buttonAddChemist.setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Chemist's Detail");
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        prefferdMeetTime = new ArrayList<>();
        prefferdMeetTime.add("Morning");
        prefferdMeetTime.add("Evening");
        setArrayAdapter(prefferdMeetTime, spinnerMeetTime);

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ActivityAddChemist.this, SingleListActivity.class);
        intent.putExtra(AppConstants.KEY_TOUCHED_FRAGMENT, AppConstants.CHEMIST_FRAGMENT);
        startActivity(intent);
    }

    public void uiValidation(){
        String strCompanyName = textChemistCompanyName.getText().toString().trim();
        String strChemistMonthlyVolume = textChemistMonthlyVolume.getText().toString().trim();
        String strChemistShopSize = textChemistShopSize.getText().toString().trim();
        String strFirstName = textChemistFirstName.getText().toString().trim();
        String strMiddleName = textChemistMiddleName.getText().toString().trim();
        String strLastName = textChemistLastName.getText().toString().trim();
        String strChemistEmail = textChemistEmail.getText().toString().trim();
        String strChemistPhone = textChemistPhone.getText().toString().trim();
        String strAddress1 = textChemistAddress1.getText().toString().trim();
        String strAddress2 = textChemistAddress2.getText().toString().trim();
        String strAddress3 = textChemistAddress3.getText().toString().trim();
        String strAddress4 = textChemistAddress4.getText().toString().trim();
        String strChemistDistrict = textChemistDistrict.getText().toString().trim();
        String strChemistCity = textChemistCity.getText().toString().trim();
        String strChemistState = textChemistState.getText().toString().trim();
        String strChemistPincode = textChemistPincode.getText().toString().trim();
        String strChemistBillingEmail = textChemistBillingEmail.getText().toString().trim();
        String strChemistBillingPhone1 = textChemistBillingPhone1.getText().toString().trim();
        String strChemistBillingPhone2 = textChemistBillingPhone2.getText().toString().trim();
        String strChemistRating = textChemistRating.getText().toString().trim();


        if (TextUtils.isEmpty(strCompanyName)){
            textChemistCompanyName.requestFocus();
            inputLayoutCompanyNameChemist.setError(getString(R.string.invalid_input));
        }
        else if (TextUtils.isEmpty(strChemistMonthlyVolume)){
            textChemistMonthlyVolume.requestFocus();
            inputLayoutMonthlyVolumeChemist.setError(getString(R.string.invalid_input));
        }
        else if (TextUtils.isEmpty(strChemistShopSize)){
            textChemistShopSize.requestFocus();
            inputLayoutShopSizeChemist.setError(getString(R.string.invalid_input));
        }
        else if (TextUtils.isEmpty(strFirstName)){
            textChemistFirstName.requestFocus();
            inputLayoutFirstNameChemist.setError(getString(R.string.invalid_input));
        }
        else if (TextUtils.isEmpty(strMiddleName)){
            textChemistMiddleName.requestFocus();
            inputLayoutMiddleNameChemist.setError(getString(R.string.invalid_input));
        }
        else if (TextUtils.isEmpty(strLastName)){
            textChemistLastName.requestFocus();
            inputLayoutLastNameChemist.setError(getString(R.string.invalid_input));
        }
        else if (TextUtils.isEmpty(strChemistEmail)){
            textChemistEmail.requestFocus();
            inputLayoutEmailChemist.setError(getString(R.string.invalid_input));
        }
        else if (!isValidEmailId(strChemistEmail)){
            textChemistEmail.requestFocus();
            inputLayoutEmailChemist.setError("Invalid Email");
        }
        else if (TextUtils.isEmpty(strChemistPhone)){
            textChemistPhone.requestFocus();
            inputLayoutPhoneChemist.setError(getString(R.string.invalid_input));
        }
        else if (TextUtils.isEmpty(strAddress1)){
            textChemistAddress1.requestFocus();
            inputLayoutAddress1Chemist.setError(getString(R.string.invalid_input));
        }
        else if (TextUtils.isEmpty(strAddress2)){
            textChemistAddress2.requestFocus();
            inputLayoutAddress2Chemist.setError(getString(R.string.invalid_input));
        }
        else if (TextUtils.isEmpty(strAddress3)){
            textChemistAddress3.requestFocus();
            inputLayoutAddress3Chemist.setError(getString(R.string.invalid_input));
        }
        else if (TextUtils.isEmpty(strAddress4)){
            textChemistAddress4.requestFocus();
            inputLayoutAddress4Chemist.setError(getString(R.string.invalid_input));
        }
        else if (TextUtils.isEmpty(strChemistDistrict)){
            textChemistDistrict.requestFocus();
            inputLayoutDistrictChemist.setError(getString(R.string.invalid_input));
        }
        else if (TextUtils.isEmpty(strChemistCity)){
            textChemistCity.requestFocus();
            inputLayoutCityChemist.setError(getString(R.string.invalid_input));
        }
        else if (TextUtils.isEmpty(strChemistState)){
            textChemistState.requestFocus();
            inputLayoutStateChemist.setError(getString(R.string.invalid_input));
        }
        else if (TextUtils.isEmpty(strChemistPincode)){
            textChemistPincode.requestFocus();
            inputLayoutPincodeChemist.setError(getString(R.string.invalid_input));
        }
        else if (TextUtils.isEmpty(strChemistBillingEmail)){
            textChemistBillingEmail.requestFocus();
            inputLayoutBillingEmailChemist.setError(getString(R.string.invalid_input));
        }
        else if (!isValidEmailId(strChemistBillingEmail)){
            textChemistBillingEmail.requestFocus();
            inputLayoutBillingEmailChemist.setError("Invalid Email");
        }
        else if (TextUtils.isEmpty(strChemistBillingPhone1)){
            textChemistBillingPhone1.requestFocus();
            inputLayoutBillingPhone1Chemist.setError(getString(R.string.invalid_input));
        }
        else if (strChemistBillingPhone1.length()<10){
            textChemistBillingPhone1.requestFocus();
            inputLayoutBillingPhone1Chemist.setError("Invalid phone number.");
        }
        else if (TextUtils.isEmpty(strChemistBillingPhone2)){
            textChemistBillingPhone2.requestFocus();
            inputLayoutBillingPhone2Chemist.setError(getString(R.string.invalid_input));
        }
        else if (strChemistBillingPhone2.length()<10){
            textChemistBillingPhone2.requestFocus();
            inputLayoutBillingPhone2Chemist.setError("Invalid phone number.");
        }
        else if (TextUtils.isEmpty(strChemistRating)){
            textChemistRating.requestFocus();
            inputLayoutRatingChemist.setError(getString(R.string.invalid_input));
        }
        else{
            chemists.setCompanyName(strCompanyName);
            chemists.setMonthlyVolumePotential(strChemistMonthlyVolume);
            chemists.setShopSize(strChemistShopSize);
            chemists.setFirstName(strFirstName);
            chemists.setMiddleName(strMiddleName);
            chemists.setLastName(strLastName);
            chemists.setEmail(strChemistEmail);
            chemists.setPhone(strChemistPhone);
            chemists.setPatchId(patchId+"");
            chemists.setAddress1(strAddress1);
            chemists.setAddress2(strAddress2);
            chemists.setAddress3(strAddress3);
            chemists.setAddress4(strAddress4);
            chemists.setDistrict(strChemistDistrict);
            chemists.setCity(strChemistCity);
            chemists.setState(strChemistState);
            chemists.setPincode(strChemistPincode);
            chemists.setBillingEmail(strChemistBillingEmail);
            chemists.setBillingPhone1(strChemistBillingPhone1);
            chemists.setBillingPhone2(strChemistBillingPhone2);
            chemists.setRating(strChemistRating);
            chemists.setStatus("1");
            chemists.setPreferredMeetTime(prefferdMeetTime+"");
            if (InternetConnection.isNetworkAvailable(ActivityAddChemist.this)){
                getAddressLatLong(strAddress1 + ", "+ strChemistPincode);
            }
            else{
                Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
            }
        }
    }


    private boolean isValidEmailId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
    @Override
    public void initData() {
        super.initData();
        chemists = new Chemists();
        if (InternetConnection.isNetworkAvailable(ActivityAddChemist.this)){
            getPatchList(token);
        }
        else{
            Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
        }
        spinnerMeetTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (prefferdMeetTime.get(i).equals(AppConstants.KEY_MORNING_TIME)) {
                    meetTime = AppConstants.MORNING;
                } else {
                    meetTime = AppConstants.EVENING;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                int position = adapterView.getSelectedItemPosition();
                if (prefferdMeetTime.get(position).equals(AppConstants.KEY_MORNING_TIME)) {
                    meetTime = AppConstants.MORNING;
                } else {
                    meetTime = AppConstants.EVENING;
                }


            }
        });


        spinnerPatchId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                patchId = patches.get(i).getPatchId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                int pos = adapterView.getSelectedItemPosition();
                patchId = patches.get(pos).getPatchId();
            }
        });

    }

    public void setArrayAdapter(List<String> listString, Spinner spinner) {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item,
                        listString);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

    }


    public void getAddressLatLong(String input) {
        processDialog.showDialog(ActivityAddChemist.this, false);
        Call<GooglePlaces> call = apbInterfaceForGooglePlaces.getPlaceLatLong(input, AppConstants.INPUT_TYPE, AppConstants.FIELDS, AppConstants.KEY_GOOGLE_PLACES);
        call.enqueue(new Callback<GooglePlaces>() {
            @Override
            public void onResponse(Call<GooglePlaces> call, Response<GooglePlaces> response) {
                processDialog.dismissDialog();
                Log.e("AddDoctorActivity", "onResponse: " + new Gson().toJson(response.body()));
                if (response.body().getStatus().equals(AppConstants.STATUS_OK)) {
                    if (chemists != null) {
                        chemists.setLatitude(response.body().getCandidates().get(0).getGeometry().getLocation().getLat() + "");
                        chemists.setLongitude(response.body().getCandidates().get(0).getGeometry().getLocation().getLng() + "");
                        Log.e("Patches Object", "onResponse: " + new Gson().toJson(chemists));
                        addChemistDetailsApi(token, chemists);
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
                Toast.makeText(ActivityAddChemist.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getPatchList(String token) {
        Call<PatchListResponse> call = apiInterface.patchList(token);
        call.enqueue(new Callback<PatchListResponse>() {
            @Override
            public void onResponse(Call<PatchListResponse> call, Response<PatchListResponse> response) {
                if (response != null) {
                    if (response.body().getStatusCode() == AppConstants.RESULT_OK) {
                        List<String> tempList = new ArrayList<>();
                        patches = response.body().getPatchesList();
                        for (int i = 0; i < patches.size(); i++) {
                            tempList.add(patches.get(i).getPatchName() + ", " + patches.get(i).getTerritoryName());
                        }

                        setArrayAdapter(tempList, spinnerPatchId);

                    }
                }
            }

            @Override
            public void onFailure(Call<PatchListResponse> call, Throwable t) {
                Toast.makeText(ActivityAddChemist.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


    public void addChemistDetailsApi(String token, Chemists chemists){
        processDialog.showDialog(ActivityAddChemist.this, false);
        Call<ChemistResponse> call = apiInterface.addChemist(token, chemists);
        call.enqueue(new Callback<ChemistResponse>() {
            @Override
            public void onResponse(Call<ChemistResponse> call, Response<ChemistResponse> response) {
                processDialog.dismissDialog();
                if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                    Intent intentSingleList = new Intent(ActivityAddChemist.this, SingleListActivity.class);
                    intentSingleList.putExtra(AppConstants.KEY_TOUCHED_FRAGMENT, AppConstants.CHEMIST_FRAGMENT);
                    startActivity(intentSingleList);
                    finish();
                }
                else{
                    Toast.makeText(ActivityAddChemist.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ChemistResponse> call, Throwable t) {
                processDialog.dismissDialog();
                Toast.makeText(ActivityAddChemist.this, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_add_chemist:
                uiValidation();
                break;

        }

    }






}
