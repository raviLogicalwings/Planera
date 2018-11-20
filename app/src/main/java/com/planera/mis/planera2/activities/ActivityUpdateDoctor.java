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
import com.planera.mis.planera2.models.Doctors;
import com.planera.mis.planera2.models.GooglePlacesModel.GooglePlaces;
import com.planera.mis.planera2.models.MainResponse;
import com.planera.mis.planera2.models.PatchListResponse;
import com.planera.mis.planera2.models.Patches;
import com.planera.mis.planera2.utils.AppConstants;
import com.planera.mis.planera2.utils.InternetConnection;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityUpdateDoctor extends BaseActivity implements View.OnClickListener{
    private Doctors doctors;
    private List<String> prefferedMeetTime;
    private List<String> meetFequrency;
    private List<Patches> patches;
    private AppBarLayout appBar;
    private Toolbar toolbarDoctor;
    private EditText textDoctorFirstName;
    private EditText textDoctorMiddleName;
    private EditText textDoctorLastName;
    private EditText textDoctorDob;
    private EditText textDoctorEmail;
    private EditText textDoctorQualification;
    private EditText textDoctorSpecialization;
    private Spinner spinnerMeetTime;
    private Spinner spinnerFrequency;
    private Spinner spinnerPatchId;
    private EditText textDoctorPhone;
    private EditText textDoctorAddress1;
    private EditText textDoctorAddress2;
    private EditText textDoctorAddress3;
    private EditText textDoctorAddress4;
    private EditText textDoctorDistrict;
    private EditText textDoctorCity;
    private EditText textDoctorState;
    private EditText textDoctorPincode;
    private Button buttonAddDoctor;
    private String dateOfBirthString;
    int meetTime;
    int meetFreq;
    int patchId;
    int doctorId;
    String firstNameStr, middleNameStr, lastNameStr, emailStr, dobStr, qualificationStr, specializationStr, phoneStr,
            address1Str, address2Str, address3Str, address4Str, districtStr, cityStr, stateStr, pincodeStr;
    private String previousPatchId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor);
        initUi();
        initData();
    }

    @Override
    public void initUi() {
        super.initUi();
        Intent intent = getIntent();
        appBar = findViewById(R.id.appBar);
        toolbarDoctor = findViewById(R.id.toolbarDoctor);
        textDoctorFirstName = findViewById(R.id.text_doctor_first_name);
        textDoctorMiddleName = findViewById(R.id.text_doctor_middle_name);
        textDoctorLastName = findViewById(R.id.text_doctor_last_name);
        textDoctorDob = findViewById(R.id.text_doctor_dob);
        textDoctorEmail = findViewById(R.id.text_doctor_email);
        textDoctorQualification = findViewById(R.id.text_doctor_qualification);
        textDoctorSpecialization = findViewById(R.id.text_doctor_specialization);
        spinnerMeetTime = findViewById(R.id.spinner_meet_time);
        spinnerFrequency = findViewById(R.id.spinner_frequency);
        spinnerPatchId = findViewById(R.id.spinner_patch_id);
        textDoctorPhone = findViewById(R.id.text_doctor_phone);
        textDoctorAddress1 = findViewById(R.id.text_doctor_address1);
        textDoctorAddress2 = findViewById(R.id.text_doctor_address2);
        textDoctorAddress3 = findViewById(R.id.text_doctor_address3);
        textDoctorAddress4 = findViewById(R.id.text_doctor_address4);
        textDoctorDistrict = findViewById(R.id.text_doctor_district);
        textDoctorCity = findViewById(R.id.text_doctor_city);
        textDoctorState = findViewById(R.id.text_doctor_state);
        textDoctorPincode = findViewById(R.id.text_doctor_pincode);
        buttonAddDoctor = findViewById(R.id.button_add_doctor);
        buttonAddDoctor.setText(getString(R.string.update));
        buttonAddDoctor.setOnClickListener(this);
        textDoctorDob.setOnClickListener(this);
        setSupportActionBar(toolbarDoctor);
        getSupportActionBar().setTitle("Update Doctor's Detail");
        prefferedMeetTime = new ArrayList<>();
        prefferedMeetTime.add("Morning");
        prefferedMeetTime.add("Evening");
        meetFequrency = new ArrayList<>();
        meetFequrency.add("1");
        meetFequrency.add("2");
        meetFequrency.add("3");
        meetFequrency.add("4");
        meetFequrency.add("5");

        setArrayAdapter(meetFequrency, spinnerFrequency);
        setArrayAdapter(prefferedMeetTime, spinnerMeetTime);

        if (intent != null) {
            loadFromIntent(intent);
        }
    }

    @Override
    public void initData() {
        super.initData();
        doctors = new Doctors();



        spinnerMeetTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (prefferedMeetTime.get(i).equals(AppConstants.KEY_MORNING_TIME)) {
                    meetTime = AppConstants.MORNING;
                } else {
                    meetTime = AppConstants.EVENING;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                if (prefferedMeetTime.get(adapterView.getSelectedItemPosition()).equals(AppConstants.KEY_MORNING_TIME)) {
                    meetTime = AppConstants.MORNING;
                } else {
                    meetTime = AppConstants.EVENING;
                }
            }
        });


        spinnerFrequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                meetFreq = Integer.parseInt(meetFequrency.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                meetFreq = Integer.parseInt(meetFequrency.get(adapterView.getSelectedItemPosition()));

            }
        });

        getPatchList(token);


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
        Intent intent = new Intent(ActivityUpdateDoctor.this, SingleListActivity.class);
        intent.putExtra(AppConstants.KEY_TOUCHED_FRAGMENT, AppConstants.DOCTOR_FRAGMENT);
        startActivity(intent);
    }

    public void uiValidation() {
        firstNameStr = textDoctorFirstName.getText().toString().trim();
        middleNameStr = textDoctorMiddleName.getText().toString().trim();
        lastNameStr = textDoctorLastName.getText().toString().trim();
        emailStr = textDoctorEmail.getText().toString().trim();
        qualificationStr = textDoctorQualification.getText().toString().trim();
        specializationStr = textDoctorSpecialization.getText().toString().trim();
        phoneStr = textDoctorPhone.getText().toString().trim();
        address1Str = textDoctorAddress1.getText().toString().trim();
        address2Str = textDoctorAddress2.getText().toString().trim();
        address3Str = textDoctorAddress3.getText().toString().trim();
        address4Str = textDoctorAddress4.getText().toString().trim();
        districtStr = textDoctorDistrict.getText().toString().trim();
        stateStr = textDoctorState.getText().toString().trim();
        cityStr = textDoctorCity.getText().toString().trim();
        pincodeStr = textDoctorPincode.getText().toString().trim();
        dobStr = textDoctorDob.getText().toString().trim();


        if (TextUtils.isEmpty(firstNameStr)) {
            textDoctorFirstName.requestFocus();
            textDoctorFirstName.setError(getString(R.string.invalid_input));
        }
        else if (TextUtils.isEmpty(lastNameStr)) {
            textDoctorLastName.requestFocus();
            textDoctorLastName.setError(getString(R.string.invalid_input));
        } else if (TextUtils.isEmpty(dobStr)) {
            textDoctorDob.requestFocus();
            textDoctorDob.setError(getString(R.string.invalid_input));
        } else if (TextUtils.isEmpty(specializationStr)) {
            textDoctorSpecialization.requestFocus();
            textDoctorSpecialization.setError(getString(R.string.invalid_input));
        } else if (TextUtils.isEmpty(qualificationStr)) {
            textDoctorQualification.requestFocus();
            textDoctorQualification.setError(getString(R.string.invalid_input));
        }
        /*else if (TextUtils.isEmpty(middleNameStr)) {
            textDoctorMiddleName.requestFocus();
            textDoctorMiddleName.setError(getString(R.string.invalid_input));
        }*/
        else if (TextUtils.isEmpty(emailStr)) {
            textDoctorEmail.requestFocus();
            textDoctorEmail.setError(getString(R.string.invalid_input));
        }
        else if (TextUtils.isEmpty(phoneStr)) {
            textDoctorPhone.requestFocus();
            textDoctorPhone.setError(getString(R.string.invalid_input));
        }
        else if (TextUtils.isEmpty(address1Str)) {
            textDoctorAddress1.requestFocus();
            textDoctorAddress1.setError(getString(R.string.invalid_input));
        } else if (TextUtils.isEmpty(address2Str)) {
            textDoctorAddress2.requestFocus();
            textDoctorAddress2.setError(getString(R.string.invalid_input));
        } else if (TextUtils.isEmpty(address3Str)) {
            textDoctorAddress3.requestFocus();
            textDoctorAddress3.setError(getString(R.string.invalid_input));
        } else if (TextUtils.isEmpty(address4Str)) {
            textDoctorAddress4.requestFocus();
            textDoctorAddress4.setError(getString(R.string.invalid_input));
        } else if (TextUtils.isEmpty(districtStr)) {
            textDoctorDistrict.requestFocus();
            textDoctorDistrict.setError(getString(R.string.invalid_input));
        } else if (TextUtils.isEmpty(cityStr)) {
            textDoctorCity.requestFocus();
            textDoctorCity.setError(getString(R.string.invalid_input));
        } else if (TextUtils.isEmpty(stateStr)) {
            textDoctorState.requestFocus();
            textDoctorState.setError(getString(R.string.invalid_input));
        } else if (TextUtils.isEmpty(qualificationStr)) {
            textDoctorQualification.requestFocus();
            textDoctorQualification.setError(getString(R.string.invalid_input));
        } else if (TextUtils.isEmpty(phoneStr)) {
            textDoctorPincode.requestFocus();
            textDoctorPincode.setError(getString(R.string.invalid_input));
        } else {

            if (doctorId != 0){
                doctors.setDoctorId(doctorId);
            }

            doctors.setMeetFrequency(meetFreq + "");
            doctors.setPreferredMeetTime(meetTime + "");
            doctors.setFirstName(firstNameStr);
            doctors.setMiddleName(middleNameStr);
            doctors.setLastName(lastNameStr);
            doctors.setDOB(dobStr);
            doctors.setPhone(phoneStr);
            doctors.setEmail(emailStr);
            doctors.setPhone(phoneStr);
            doctors.setAddress1(address1Str);
            doctors.setAddress2(address2Str);
            doctors.setAddress3(address3Str);
            doctors.setAddress4(address4Str);
            doctors.setCity(cityStr);
            doctors.setDistrict(districtStr);
            doctors.setQualifications(qualificationStr);
            doctors.setSpecializations(specializationStr);
            doctors.setPincode(pincodeStr);
            doctors.setState(stateStr);
            doctors.setPatchId(patchId + "");
            doctors.setStatus("1");
            doctors.setCRM("1");

            if (InternetConnection.isNetworkAvailable(ActivityUpdateDoctor.this)) {

                getAddressLatLong(address1Str + ", " + pincodeStr);

            } else {
                Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
            }
        }

    }

    public void getPatchList(String token) {
        processDialog.showDialog(ActivityUpdateDoctor.this, false);
        Call<PatchListResponse> call = apiInterface.patchList(token);
        call.enqueue(new Callback<PatchListResponse>() {
            @Override
            public void onResponse(Call<PatchListResponse> call, Response<PatchListResponse> response) {
               processDialog.dismissDialog();
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
                processDialog.dismissDialog();
                Toast.makeText(ActivityUpdateDoctor.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }



    public void updateDoctorsDetails(String token, Doctors doctors){
        processDialog.showDialog(ActivityUpdateDoctor.this, false);
        Call<MainResponse> call = apiInterface.updateDoctorDetails(token, doctors);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                processDialog.dismissDialog();
                if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                    Intent intent = new Intent(ActivityUpdateDoctor.this, SingleListActivity.class);
                    intent.putExtra(AppConstants.KEY_TOUCHED_FRAGMENT, AppConstants.DOCTOR_FRAGMENT);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(ActivityUpdateDoctor.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                processDialog.dismissDialog();
                Toast.makeText(ActivityUpdateDoctor.this, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    public void getAddressLatLong(String input) {
        processDialog.showDialog(ActivityUpdateDoctor.this, false);
        Call<GooglePlaces> call = apbInterfaceForGooglePlaces.getPlaceLatLong(input, AppConstants.INPUT_TYPE, AppConstants.FIELDS, AppConstants.KEY_GOOGLE_PLACES);
        call.enqueue(new Callback<GooglePlaces>() {
            @Override
            public void onResponse(Call<GooglePlaces> call, Response<GooglePlaces> response) {
                processDialog.dismissDialog();
                Log.e("AddDoctorActivity", "onResponse: " + new Gson().toJson(response.body()));
                if (response.body().getStatus().equals(AppConstants.STATUS_OK)) {
                    if (doctors != null) {
                        doctors.setLatitude(response.body().getCandidates().get(0).getGeometry().getLocation().getLat() + "");
                        doctors.setLongitude(response.body().getCandidates().get(0).getGeometry().getLocation().getLng() + "");
                        Log.e("Doctors Object", "onResponse: " + new Gson().toJson(doctors));

                            updateDoctorsDetails(token, doctors);

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
                Toast.makeText(ActivityUpdateDoctor.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void loadFromIntent(Intent intent) {
        textDoctorFirstName.setText(intent.getStringExtra(AppConstants.FIRST_NAME));
        textDoctorMiddleName.setText(intent.getStringExtra(AppConstants.MIDDLE_NAME));
        textDoctorLastName.setText(intent.getStringExtra(AppConstants.LAST_NAME));
        textDoctorDob.setText(intent.getStringExtra(AppConstants.DOB));
        textDoctorEmail.setText(intent.getStringExtra(AppConstants.EMAIL));
        textDoctorPhone.setText(intent.getStringExtra(AppConstants.PHONE));
        spinnerFrequency.setSelection((Integer.parseInt(intent.getStringExtra(AppConstants.MEET_FREQUENCY))-1));
        spinnerMeetTime.setSelection(Integer.parseInt(intent.getStringExtra(AppConstants.PREFERRED_MEET_TIME))-1);
        previousPatchId = intent.getStringExtra(AppConstants.PATCH_ID);
        textDoctorQualification.setText(intent.getStringExtra(AppConstants.QUALIFICATION));
        textDoctorSpecialization.setText(intent.getStringExtra(AppConstants.SPECIALIZATION));
        textDoctorAddress1.setText(intent.getStringExtra(AppConstants.ADDRESS1));
        textDoctorAddress2.setText(intent.getStringExtra(AppConstants.ADDRESS2));
        textDoctorAddress3.setText(intent.getStringExtra(AppConstants.ADDRESS3));
        textDoctorAddress4.setText(intent.getStringExtra(AppConstants.ADDRESS4));
        textDoctorDistrict.setText(intent.getStringExtra(AppConstants.DISTRICT));
        textDoctorCity.setText(intent.getStringExtra(AppConstants.CITY));
        textDoctorState.setText(intent.getStringExtra(AppConstants.STATE));
        textDoctorPincode.setText(intent.getStringExtra(AppConstants.PINCODE));
        doctorId = intent.getIntExtra(AppConstants.UPDATE_DOCTOR_KEY, 0);
    }


    public void setArrayAdapter(List<String> listString, Spinner spinner) {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item,
                        listString);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

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
            textDoctorDob.setText(dateOfBirthString);
        };
        DatePickerDialog dpDialog = new DatePickerDialog(ActivityUpdateDoctor.this, listener, year, month, day);
        dpDialog.show();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add_doctor:
                uiValidation();
                break;

            case R.id.text_doctor_dob:
                getDateFromDialog();
                break;


        }
    }


}
