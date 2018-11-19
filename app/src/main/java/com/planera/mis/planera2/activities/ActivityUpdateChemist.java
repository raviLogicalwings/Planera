package com.planera.mis.planera2.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
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
import com.planera.mis.planera2.fragments.ChemistFragment;
import com.planera.mis.planera2.models.Chemists;
import com.planera.mis.planera2.models.DoctorResponse;
import com.planera.mis.planera2.models.Doctors;
import com.planera.mis.planera2.models.GooglePlacesModel.GooglePlaces;
import com.planera.mis.planera2.models.MainResponse;
import com.planera.mis.planera2.models.PatchListResponse;
import com.planera.mis.planera2.models.Patches;
import com.planera.mis.planera2.utils.AppConstants;
import com.planera.mis.planera2.utils.InternetConnection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityUpdateChemist extends BaseActivity implements View.OnClickListener{
    private Chemists chemists;
    private List<String> prefferdMeetTime;
    private List<String> meetFequrency;
    private List<Patches> patches;
    private AppBarLayout appBar;
    private Toolbar toolbarChemist;
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
    private Button buttonAddChemist;
    private String dateOfBirthString;
    int meetTime;
    int meetFreq;
    int patchId;
    int chemistId;
    private boolean isUpdateCall;
    String firstNameStr, middleNameStr, lastNameStr, emailStr, dobStr, companyNameStr, monthtlyVolumeStr,shopSizeStr,  billingEmailStr, billingPhone2Str, billingPhone1Str, ratingStr ,specializationStr, phoneStr,
            address1Str, address2Str, address3Str, address4Str, districtStr, cityStr, stateStr, pincodeStr;
    private String previousPatchId;

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
        Intent intent = getIntent();
        appBar = findViewById(R.id.appBar);
        toolbarChemist = findViewById(R.id.toolbar);
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
        buttonAddChemist = findViewById(R.id.button_add_chemist);

        buttonAddChemist.setText(getString(R.string.update));

        buttonAddChemist.setOnClickListener(this);
        setSupportActionBar(toolbarChemist);
        getSupportActionBar().setTitle("Update Chemist's Detail");
        prefferdMeetTime = new ArrayList<>();
        prefferdMeetTime.add("Morning");
        prefferdMeetTime.add("Evening");
        meetFequrency = new ArrayList<>();
        meetFequrency.add("1");
        meetFequrency.add("2");
        meetFequrency.add("3");
        meetFequrency.add("4");
        meetFequrency.add("5");

        setArrayAdapter(prefferdMeetTime, spinnerMeetTime);

        if (intent != null) {
            loadFromIntent(intent);
        }


    }

    @Override
    public void initData() {
        super.initData();

        getPatchList(token);
        chemists = new Chemists();
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
                if (prefferdMeetTime.get(adapterView.getSelectedItemPosition()).equals(AppConstants.KEY_MORNING_TIME)) {
                    meetTime = AppConstants.MORNING;
                } else {
                    meetTime = AppConstants.EVENING;
                }
            }
        });



//        getPatchList(token);

        spinnerPatchId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                patchId = patches.get(i).getPatchId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                patchId = patches.get(adapterView.getSelectedItemPosition()).getPatchId();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ActivityUpdateChemist.this, SingleListActivity.class);
        intent.putExtra(AppConstants.KEY_TOUCHED_FRAGMENT, AppConstants.CHEMIST_FRAGMENT);
        startActivity(intent);
    }

    public void uiValidation() {
        firstNameStr = textChemistFirstName.getText().toString().trim();
        middleNameStr = textChemistMiddleName.getText().toString().trim();
        lastNameStr = textChemistLastName.getText().toString().trim();
        emailStr = textChemistEmail.getText().toString().trim();
        ratingStr = textChemistRating.getText().toString().trim();
        billingEmailStr = textChemistBillingEmail.getText().toString().trim();
        billingPhone1Str = textChemistBillingPhone1.getText().toString().trim();
        billingPhone2Str = textChemistBillingPhone2.getText().toString().trim();
        shopSizeStr = textChemistShopSize.getText().toString().trim();
        companyNameStr = textChemistCompanyName.getText().toString().trim();
        monthtlyVolumeStr = textChemistMonthlyVolume.getText().toString().trim();
        phoneStr = textChemistPhone.getText().toString().trim();
        address1Str = textChemistAddress1.getText().toString().trim();
        address2Str = textChemistAddress2.getText().toString().trim();
        address3Str = textChemistAddress3.getText().toString().trim();
        address4Str = textChemistAddress4.getText().toString().trim();
        districtStr = textChemistDistrict.getText().toString().trim();
        stateStr = textChemistState.getText().toString().trim();
        cityStr = textChemistCity.getText().toString().trim();
        pincodeStr = textChemistPincode.getText().toString().trim();


        if (TextUtils.isEmpty(firstNameStr)) {
            textChemistFirstName.requestFocus();
            textChemistFirstName.setError(getString(R.string.invalid_input));
        } else if (TextUtils.isEmpty(middleNameStr)) {
            textChemistMiddleName.requestFocus();
            textChemistMiddleName.setError(getString(R.string.invalid_input));
        } else if (TextUtils.isEmpty(lastNameStr)) {
            textChemistLastName.requestFocus();
            textChemistLastName.setError(getString(R.string.invalid_input));
        } else if (TextUtils.isEmpty(shopSizeStr)) {
            textChemistShopSize.requestFocus();
            textChemistShopSize.setError(getString(R.string.invalid_input));
        } else if (TextUtils.isEmpty(monthtlyVolumeStr)) {
            textChemistMonthlyVolume.requestFocus();
            textChemistMonthlyVolume.setError(getString(R.string.invalid_input));
        } else if (TextUtils.isEmpty(companyNameStr)) {
            textChemistCompanyName.requestFocus();
            textChemistCompanyName.setError(getString(R.string.invalid_input));
        } else if (TextUtils.isEmpty(middleNameStr)) {
            textChemistMiddleName.requestFocus();
            textChemistMiddleName.setError(getString(R.string.invalid_input));
        } else if (TextUtils.isEmpty(emailStr)) {
            textChemistEmail.requestFocus();
            textChemistEmail.setError(getString(R.string.invalid_input));
        } else if (TextUtils.isEmpty(phoneStr)) {
            textChemistPhone.requestFocus();
            textChemistPhone.setError(getString(R.string.invalid_input));
        } else if (TextUtils.isEmpty(address1Str)) {
            textChemistAddress1.requestFocus();
            textChemistAddress1.setError(getString(R.string.invalid_input));
        } else if (TextUtils.isEmpty(address2Str)) {
            textChemistAddress2.requestFocus();
            textChemistAddress2.setError(getString(R.string.invalid_input));
        } else if (TextUtils.isEmpty(address3Str)) {
            textChemistAddress3.requestFocus();
            textChemistAddress3.setError(getString(R.string.invalid_input));
        } else if (TextUtils.isEmpty(address4Str)) {
            textChemistAddress4.requestFocus();
            textChemistAddress4.setError(getString(R.string.invalid_input));
        } else if (TextUtils.isEmpty(districtStr)) {
            textChemistDistrict.requestFocus();
            textChemistDistrict.setError(getString(R.string.invalid_input));
        } else if (TextUtils.isEmpty(cityStr)) {
            textChemistCity.requestFocus();
            textChemistCity.setError(getString(R.string.invalid_input));
        } else if (TextUtils.isEmpty(stateStr)) {
            textChemistState.requestFocus();
            textChemistState.setError(getString(R.string.invalid_input));
        } else if (TextUtils.isEmpty(billingEmailStr)) {
            textChemistBillingEmail.requestFocus();
            textChemistBillingEmail.setError(getString(R.string.invalid_input));
        } else if (TextUtils.isEmpty(phoneStr)) {
            textChemistPincode.requestFocus();
            textChemistPincode.setError(getString(R.string.invalid_input));
        }

        else if (TextUtils.isEmpty(billingPhone1Str)) {
            textChemistBillingPhone1.requestFocus();
            textChemistBillingPhone1.setError(getString(R.string.invalid_input));
        }

        else if (TextUtils.isEmpty(billingPhone2Str)) {
            textChemistBillingPhone2.requestFocus();
            textChemistBillingPhone2.setError(getString(R.string.invalid_input));
        }
        else if (TextUtils.isEmpty(ratingStr)) {
            textChemistRating.requestFocus();
            textChemistRating.setError(getString(R.string.invalid_input));
        }

        else {

            if (chemistId !=0){
                chemists.setChemistId(chemistId);

            }
        }
        chemists.setShopSize(shopSizeStr);
        chemists.setBillingEmail(billingEmailStr);
        chemists.setBillingPhone1(billingPhone1Str);
        chemists.setBillingPhone2(billingPhone2Str);
        chemists.setRating(ratingStr);
        chemists.setMonthlyVolumePotential(monthtlyVolumeStr);
        chemists.setCompanyName(companyNameStr);
        chemists.setPreferredMeetTime(meetTime + "");
        chemists.setFirstName(firstNameStr);
        chemists.setMiddleName(middleNameStr);
        chemists.setLastName(lastNameStr);
        chemists.setPhone(phoneStr);
        chemists.setEmail(emailStr);
        chemists.setPhone(phoneStr);
        chemists.setAddress1(address1Str);
        chemists.setAddress2(address2Str);
        chemists.setAddress3(address3Str);
        chemists.setAddress4(address4Str);
        chemists.setCity(cityStr);
        chemists.setDistrict(districtStr);
        chemists.setPincode(pincodeStr);
        chemists.setState(stateStr);
        chemists.setPatchId(patchId + "");
        chemists.setStatus("1");


        if (InternetConnection.isNetworkAvailable(ActivityUpdateChemist.this)) {

            getAddressLatLong(address1Str + ", " + pincodeStr);

        } else {
            Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
        }

    }


    public void addDoctorsApi(String token, Doctors chemists) {
        processDialog.showDialog(mContext, false);
        Call<DoctorResponse> call = apiInterface.addDoctor(token, chemists);
        call.enqueue(new Callback<DoctorResponse>() {
            @Override
            public void onResponse(Call<DoctorResponse> call, Response<DoctorResponse> response) {
                processDialog.dismissDialog();
                Log.e("Add Doctor", "onResponse: " + new Gson().toJson(response.body()));
                if (response.body().getStatusCode() == AppConstants.RESULT_OK) {
                    Intent intent = new Intent(ActivityUpdateChemist.this, SingleListActivity.class);
                    intent.putExtra(AppConstants.KEY_TOUCHED_FRAGMENT, AppConstants.DOCTOR_FRAGMENT);
                    intent.putExtra(AppConstants.IS_CHANGES, true);
                    startActivity(intent);
                    Toast.makeText(ActivityUpdateChemist.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(ActivityUpdateChemist.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DoctorResponse> call, Throwable t) {
                processDialog.dismissDialog();
                Toast.makeText(ActivityUpdateChemist.this, t.getMessage(), Toast.LENGTH_LONG).show();

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
                Toast.makeText(ActivityUpdateChemist.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }



    public void updateChemistDetails(String token, Chemists chemists){
        processDialog.showDialog(ActivityUpdateChemist.this, false);
        Call<MainResponse> call = apiInterface.updateChemist(token, chemists);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                processDialog.dismissDialog();
                if (response.code()==400){

                    try {
                        Toast.makeText(mContext, response.errorBody().string(), Toast.LENGTH_LONG).show();
                        getSupportFragmentManager().beginTransaction().detach(ChemistFragment.newInstance()).attach(ChemistFragment.newInstance()).commitAllowingStateLoss();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                else{


                    if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                        Log.e("Chemsit Update fragment", new Gson().toJson(response.body()));
                        Intent intent = new Intent(ActivityUpdateChemist.this, SingleListActivity.class);
                        intent.putExtra(AppConstants.KEY_TOUCHED_FRAGMENT, AppConstants.CHEMIST_FRAGMENT);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(ActivityUpdateChemist.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                    }

                }
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                processDialog.dismissDialog();
                Toast.makeText(ActivityUpdateChemist.this, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    public void getAddressLatLong(String input) {
        processDialog.showDialog(ActivityUpdateChemist.this, false);
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
                        Log.e("Doctors Object", "onResponse: " + new Gson().toJson(chemists));

                            updateChemistDetails(token, chemists);

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
                Toast.makeText(ActivityUpdateChemist.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void loadFromIntent(Intent intent) {
        textChemistFirstName.setText(intent.getStringExtra(AppConstants.FIRST_NAME));
        textChemistMiddleName.setText(intent.getStringExtra(AppConstants.MIDDLE_NAME));
        textChemistLastName.setText(intent.getStringExtra(AppConstants.LAST_NAME));
        textChemistShopSize.setText(intent.getStringExtra(AppConstants.SHOP_SIZE));
        textChemistMonthlyVolume.setText(intent.getStringExtra(AppConstants.MONTHLY_VOLUME_POTENTIAL));
        textChemistCompanyName.setText(intent.getStringExtra(AppConstants.COMPANY_NAME));
        textChemistBillingEmail.setText(intent.getStringExtra(AppConstants.BILLING_EMAIL));
        spinnerMeetTime.setSelection(Integer.parseInt(intent.getStringExtra(AppConstants.PREFERRED_MEET_TIME))-1);
        textChemistEmail.setText(intent.getStringExtra(AppConstants.EMAIL));
        textChemistPhone.setText(intent.getStringExtra(AppConstants.PHONE));
        textChemistBillingPhone2.setText(intent.getStringExtra(AppConstants.BILLING_PHONE2));
        textChemistBillingPhone1.setText(intent.getStringExtra(AppConstants.BILLING_PHONE2));
        textChemistAddress1.setText(intent.getStringExtra(AppConstants.ADDRESS1));
        textChemistAddress2.setText(intent.getStringExtra(AppConstants.ADDRESS2));
        textChemistAddress3.setText(intent.getStringExtra(AppConstants.ADDRESS3));
        textChemistAddress4.setText(intent.getStringExtra(AppConstants.ADDRESS4));
        textChemistDistrict.setText(intent.getStringExtra(AppConstants.DISTRICT));
        textChemistCity.setText(intent.getStringExtra(AppConstants.CITY));
        textChemistState.setText(intent.getStringExtra(AppConstants.STATE));
        textChemistPincode.setText(intent.getStringExtra(AppConstants.PINCODE));
        textChemistRating.setText(intent.getStringExtra(AppConstants.RATING));
        chemistId = intent.getIntExtra(AppConstants.UPDATE_CHEMIST_KEY, 0);
        previousPatchId = intent.getStringExtra(AppConstants.PATCH_ID);
    }


    public void setArrayAdapter(List<String> listString, Spinner spinner) {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item,
                        listString);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

        //set adapter position of previous input value of previous data.
        if (patches != null) {
            for (int i = 0; i < patches.size(); i++) {
                if (patches.get(i).getPatchId() == Integer.parseInt(previousPatchId)) {
                    spinnerPatchId.setSelection(i);
                }
            }
        }

    }

    public void getDateFromDialog() {
        Calendar dateOfBirth = Calendar.getInstance();
        int year = dateOfBirth.get(Calendar.YEAR);
        int month = dateOfBirth.get(Calendar.MONTH);
        int day = dateOfBirth.get(Calendar.DATE);
        DatePickerDialog.OnDateSetListener listener = (view, year1, monthOfYear, dayOfMonth) -> {
            monthOfYear++;
            dateOfBirthString = year1 + "-" + monthOfYear + "-" + dayOfMonth;
//            text.setText(dateOfBirthString);
        };
        DatePickerDialog dpDialog = new DatePickerDialog(ActivityUpdateChemist.this, listener, year, month, day);
        dpDialog.show();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add_chemist:
                uiValidation();
                break;
                


        }
    }


}
