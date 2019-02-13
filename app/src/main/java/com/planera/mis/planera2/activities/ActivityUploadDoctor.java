package com.planera.mis.planera2.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.planera.mis.planera2.R;
import com.planera.mis.planera2.models.DoctorImport;
import com.planera.mis.planera2.models.DoctorImportItems;
import com.planera.mis.planera2.models.MainResponse;
import com.planera.mis.planera2.utils.AppConstants;
import com.planera.mis.planera2.utils.RuntimePermissionCheck;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityUploadDoctor extends BaseActivity implements View.OnClickListener{

    private LinearLayout cardUploadPlanView;
    private ImageView imageSheetView;
    private Button buttonUploadPlanSheet;
    private RuntimePermissionCheck permissionCheck;
    private String displayName;
    private TextView textFileName;
    private ImageView imageClose;
    private List<DoctorImportItems> listDoctorsItems;
    private DoctorImport doctorImport;
    private CheckBox isOverrideCheck;
    private DoctorImportItems doctors;
    private Toolbar toolbarImportDoctors;
    private Uri uri = null;
    private ReadFileAsyncTask readFileAsyncTask;
    public static final String TAG = ActivityUploadDoctor.class.getSimpleName();
    private File myFile;
    private boolean isCellNull = false;
    private int isOverride;
    private int OVERRIDE = 1;
    private int DONT_OVERRIDE = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_doctor);
        initUi();
        initData();


    }

    @Override
    public void initUi() {
        super.initUi();
        toolbarImportDoctors = findViewById(R.id.toolbar_upload_doctor);
        cardUploadPlanView = findViewById(R.id.card_upload_doctor_view);
        imageSheetView = findViewById(R.id.image_sheet_view);
        isOverrideCheck = findViewById(R.id.check_is_override);
        buttonUploadPlanSheet = findViewById(R.id.button_upload_doctor_sheet);
        textFileName = findViewById(R.id.text_file_name);

        setSupportActionBar(toolbarImportDoctors);
        toolbarImportDoctors.setNavigationIcon(R.drawable.back_arrow_whit);
        getSupportActionBar().setTitle("Import Doctors");
        toolbarImportDoctors.setNavigationOnClickListener(view -> onBackPressed());
        imageSheetView.setOnClickListener(this);
        cardUploadPlanView.setOnClickListener(this);
        buttonUploadPlanSheet.setOnClickListener(this);
    }

    public void checkRequiredPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Do the file write
            pickFile();
        } else {
            // Request permission from the user
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
            try {
                permissionCheck.requestPermissionForWriteExternalStorage();
                permissionCheck.requestPermissionForReadExtertalStorage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    public void pickFile(){
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // Set your required file type
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"), 100);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
//        startActivityForResult(Intent.createChooser(intent, "Choose CSV"), 100);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (readFileAsyncTask != null){
            readFileAsyncTask.cancel(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 100:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file

                    if (data != null) {
                        uri = data.getData();
                    }
                    String uriString = uri.toString();
                    myFile = new File(uriString);

//                    readExcelData(uri);
                    String path = myFile.getAbsolutePath();
                    Log.e(TAG, "FILE PATH"+path);
                    displayName = null;



//                    Toast.makeText(ActivityUploadDoctor.this, myFile.toString(), Toast.LENGTH_LONG).show();


                    if (uriString.startsWith("content://")) {
                        Cursor cursor = null;
                        try {
                            cursor = ActivityUploadDoctor.this.getContentResolver().query(uri, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                                toggleView(displayName);
                            }
                        } finally {
                            cursor.close();
                        }
                    } else if (uriString.startsWith("file://")) {
                        displayName = myFile.getName();
                        toggleView(displayName);

//                        Toast.makeText(this, displayName, Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void toggleView(String name){
        if(name!= null){
            imageSheetView.setVisibility(View.VISIBLE);
            textFileName.setVisibility(View.VISIBLE);
            textFileName.setText(name);

        }
    }

    @Override
    public void initData() {
        super.initData();
        listDoctorsItems = new ArrayList<>();
        doctorImport = new DoctorImport();
        permissionCheck = new RuntimePermissionCheck(ActivityUploadDoctor.this);
    }


    public void checkValidation(){
        String text = textFileName.getText().toString();
        if (text.equals(getString(R.string.select_file))){
            Toasty.warning(this, "Please Select file.", Toast.LENGTH_SHORT).show();
        }
        else{
            readExcelData(uri);
        }
    }


    private void readExcelData(Uri uri) {
        try {

            InputStream inputStream = getContentResolver().openInputStream(uri);
            assert inputStream != null;
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowsCount = sheet.getPhysicalNumberOfRows();
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            for (int r = 1; r < rowsCount; r++) {
                Row row = sheet.getRow(r);
                int cellsCount = row.getPhysicalNumberOfCells();
                doctors = new DoctorImportItems();
                if (isCellNull) {
//
                    processDialog.dismissDialog();
                    break;
                }
                for (int c = 0; c < cellsCount; c++) {
                    String value = getCellAsString(row, c, formulaEvaluator);

                    if (c == 0 && value.isEmpty()) {
                        isCellNull = true;
                        break;
                    }
                    if (c == AppConstants.DOCTOR_ID_DOCTOR_EXCEL) {
                        doctors.setDocId(value);
                    }
                    if (c == AppConstants.PATCH_ID_CHEMIST_EXCEL) {
                        if (!value.isEmpty()) {
                            doctors.setPatchId(value);
                        }
                    }
                    if (c == AppConstants.FIRST_NAME_DOCTOR_EXCEL) {
                        if (!value.isEmpty()) {
                            doctors.setFirstName(value);
                        }
                    }
                    if (c == AppConstants.MIDDLE_NAME_DOCTOR_EXCEL) {
                        if (!value.isEmpty()) {
                                doctors.setMiddleName(value);
                        }
                    }
                    if (c == AppConstants.LAST_NAME_DOCTOR_EXCEL) {
                        if (!value.isEmpty()) {
                            doctors.setLastName(value);
                        }
                    }
                    if (c == AppConstants.DOB_DOCTOR_EXCEL) {
                        if (!value.isEmpty()) {
                                doctors.setDOB(value);
                        }
                    }
                    if (c == AppConstants.EMAIL_DOCTOR_EXCEL) {
                        if (!value.isEmpty()) {
                                doctors.setEmail(value);
                        }
                    }
                    if (c == AppConstants.QUALIFICATION_DOCTOR_EXCEL) {
                        if (!value.isEmpty()) {
                            doctors.setQualifications(value);
                        }
                    }
                    if (c == AppConstants.SPECIALIZATION_DOCTOR_EXCEL) {
                        if (!value.isEmpty()) {
                            doctors.setSpecializations(value);
                        }
                    }
                    if (c == AppConstants.PREFERRED_MEET_TIME_DOCTOR_EXCEL) {
                        if (!value.isEmpty()) {
                            doctors.setPreferredMeetTime(value);
                        }
                    }
                    if (c == AppConstants.PREFERRED_MEET_TIME_DOCTOR_EXCEL) {
                        if (!value.isEmpty()) {
                            doctors.setMeetFrequency(value);
                        }
                    }
                    if (c == AppConstants.PHONE_DOCTOR_EXCEL) {
                        if (!value.isEmpty()) {
                                doctors.setPhone(value);
                        }
                    }
                    if (c == AppConstants.ADDRESS_1_DOCTOR_EXCEL) {
                        if (!value.isEmpty()) {
                            doctors.setAddressLine1(value);
                        }
                    }
                    if (c == AppConstants.ADDRESS_2_DOCTOR_EXCEL) {
                        if (!value.isEmpty()) {
                            doctors.setAddressLine2(value);
                        }
                    }
                    if (c == AppConstants.ADDRESS_3_DOCTOR_EXCEL) {
                        if (!value.isEmpty()) {
                            doctors.setAddressLine3(value);
                        }
                    }
                    if (c == AppConstants.ADDRESS_4_DOCTOR_EXCEL) {
                        if (!value.isEmpty()) {
                            doctors.setAddressLine4(value);
                        }
                    }
                    if (c == AppConstants.CITY_DOCTOR_EXCEL) {
                        if (!value.isEmpty()) {
                            doctors.setCity(value);
                        }
                    }
                    if (c == AppConstants.DISTRICT_DOCTOR_EXCEL) {
                        if (!value.isEmpty()) {
                            doctors.setDistrict(value);
                        }
                    }
                    if (c == AppConstants.STATE_DOCTOR_EXCEL) {
                        if (!value.isEmpty()) {
                            doctors.setState(value);
                        }
                    }

                    if (c == AppConstants.PINCODE_DOCTOR_EXCEL) {
                        if (!value.isEmpty()) {
                            doctors.setPIN(value);
                        }
                    } else {

                        String cellInfo = "r:" + r + "; c:" + c + "; v:" + value;
                            Log.d(TAG, "readExcelData: Data from row: " + cellInfo);
                    }


                }

                listDoctorsItems.add(doctors);

            }
            Log.e("list doctors", new Gson().toJson(listDoctorsItems));



//


        } catch (Exception e) {
            Log.e(TAG, "readExcelData: FileNotFoundException. " + e.getMessage());
        }
    }


    public void uploadDoctorsDataApi(List<DoctorImportItems> listDoctorsItems){
            if (listDoctorsItems != null) {
                if (isOverrideCheck.isChecked()) {
                    isOverride = OVERRIDE;
                } else {
                    isOverride = DONT_OVERRIDE;

                }
                doctorImport.setOverride(isOverride);
                doctorImport.setDoctorImportList(listDoctorsItems);
                apiImportDoctorsFromExcel(token,  doctorImport);

            }

    }


        private String getCellAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {
        String value = "";
        try {
            Cell cell = row.getCell(c);
            CellValue cellValue = formulaEvaluator.evaluate(cell);
            switch (cellValue.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    value = ""+cellValue.getBooleanValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    int numericValue = (int) cellValue.getNumberValue();
                        value = ""+numericValue;
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = ""+cellValue.getStringValue();
                    break;
                default:
            }
        } catch (NullPointerException e) {

            Log.e(TAG, "getCellAsString: NullPointerException: " + e.getMessage() );
        }
        return value;
    }


    public void apiImportDoctorsFromExcel(String token,  DoctorImport doctorImport){
        Log.e("Import Doctors params", new Gson().toJson(doctorImport));
        Call<MainResponse> call = apiInterface.importDoctorFromExcel(token,  doctorImport);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(@NonNull Call<MainResponse> call, @NonNull Response<MainResponse> response) {
                if (response.isSuccessful()){
                    assert response.body() != null;
                    if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                        processDialog.dismissDialog();
                        Toasty.success(ActivityUploadDoctor.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                   }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MainResponse> call, @NonNull Throwable t) {
                processDialog.dismissDialog();
                Toasty.error(ActivityUploadDoctor.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.card_upload_doctor_view:
                checkRequiredPermission();
                break;
            case R.id.button_upload_doctor_sheet:
               readFileAsyncTask = new ReadFileAsyncTask();
               readFileAsyncTask.execute();
                break;
            case R.id.image_sheet_view:
                checkRequiredPermission();
                break;
            case R.id.text_file_name:
                break;
        }
    }



    @SuppressLint("StaticFieldLeak")
    class ReadFileAsyncTask extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            processDialog.showDialog(ActivityUploadDoctor.this, false);
        }

        @Override
        protected String doInBackground(Void... voids) {

            checkValidation();

            return "Success";
        }

        @Override
        protected void onPostExecute(String onlineVersion) {
            super.onPostExecute(onlineVersion);

            if (onlineVersion.equals("Success") && listDoctorsItems != null) {
                new Handler().postDelayed(() -> uploadDoctorsDataApi(listDoctorsItems), 3000);

            }
            else{
                processDialog.dismissDialog();
            }
        }
    }
}
