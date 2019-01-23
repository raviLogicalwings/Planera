package com.planera.mis.planera2.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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
import com.planera.mis.planera2.models.MainResponse;
import com.planera.mis.planera2.models.UserImport;
import com.planera.mis.planera2.models.UserImportItems;
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
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityUploadUser extends BaseActivity implements View.OnClickListener{

    private LinearLayout cardUploadUserView;
    private ImageView imageSheetView;
    private Button buttonUploadUserSheet;
    private RuntimePermissionCheck permissionCheck;
    private String displayName;
    private TextView textFileName;
    private ImageView imageClose;
    private List<UserImportItems> listUserImport;
    private UserImport userImport;
    private CheckBox isOverrideCheck;
    private UserImportItems userImportItems;
    private Toolbar toolbarImportUsers;
    private Uri uri = null;
    public static final String TAG = ActivityUploadUser.class.getSimpleName();
    private File myFile;
    private boolean isCellNull = false;
    private int isOverride;
    private int OVERRIDE = 1;
    private int DONT_OVERRIDE = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_user);
        initUi();
        initData();


    }

    @Override
    public void initUi() {
        super.initUi();
        toolbarImportUsers = findViewById(R.id.toolbar_upload_user);
        cardUploadUserView = findViewById(R.id.card_upload_user_view);
        imageSheetView = findViewById(R.id.image_sheet_view);
        isOverrideCheck = findViewById(R.id.check_is_override);
        buttonUploadUserSheet = findViewById(R.id.button_upload_user_sheet);
        textFileName = findViewById(R.id.text_file_name);

        setSupportActionBar(toolbarImportUsers);
        toolbarImportUsers.setNavigationIcon(R.drawable.back_arrow_whit);
        getSupportActionBar().setTitle("Import Users");
        toolbarImportUsers.setNavigationOnClickListener(view -> onBackPressed());
        imageSheetView.setOnClickListener(this);
        cardUploadUserView.setOnClickListener(this);
        buttonUploadUserSheet.setOnClickListener(this);
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
                    String uriString = Objects.requireNonNull(uri).toString();
                    myFile = new File(uriString);

//                    readExcelData(uri);
                    String path = myFile.getAbsolutePath();
                    Log.e(TAG, "FILE PATH"+path);
                    displayName = null;



//                    Toast.makeText(ActivityUploadDoctor.this, myFile.toString(), Toast.LENGTH_LONG).show();


                    if (uriString.startsWith("content://")) {
                        Cursor cursor = null;
                        try {
                            cursor = ActivityUploadUser.this.getContentResolver().query(uri, null, null, null, null);
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
        listUserImport = new ArrayList<>();
        userImport = new UserImport();
        permissionCheck = new RuntimePermissionCheck(ActivityUploadUser.this);
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
        processDialog.showDialog(ActivityUploadUser.this, false);
        Toast.makeText(this, "Working fine", Toast.LENGTH_LONG).show();
        try {

//            File fileB = new File(filePath);
            InputStream inputStream = getContentResolver().openInputStream(uri);
//                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//                StringBuilder stringBuilder = new StringBuilder();
//                String line;
//                while ((line = reader.readLine()) != null){
//                    stringBuilder.append(line);
//                }
//
//                inputStream.close();
//              Log.e("String Result", stringBuilder.toString());
//
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowsCount = sheet.getPhysicalNumberOfRows();
//                Toast.makeText(this, "rowCount"+rowsCount, Toast.LENGTH_LONG).show();
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            StringBuilder sb = new StringBuilder();
            //loop, loops through rows
            for (int r = 1; r < rowsCount; r++) {
                Row row = sheet.getRow(r);
                int cellsCount = row.getPhysicalNumberOfCells();
                userImportItems = new UserImportItems();
                //inner loop, loops through columns
                if (isCellNull) {
//                        if (listDoctorsItems != null){
//                            apiImportDoctorsFromExcel(token, listDoctorsItems);
//                        }
                    break;
                }
                for (int c = 0; c < cellsCount; c++) {
                    String value = getCellAsString(row, c, formulaEvaluator);

                    if (c == 0 && value.isEmpty()) {
                        isCellNull = true;

                        break;
                    }
                    if (c == AppConstants.USER_ID_EXCEL) {
                        userImportItems.setUserId(value);
                    }
                    if (c == AppConstants.USER_NAME_EXCEL) {
                        if (!value.isEmpty()) {
                            userImportItems.setLoginId(value);
                        }
                    }
                    if (c == AppConstants.TYPE) {
                        if (!value.isEmpty()) {
                            userImportItems.setFirstName(value);
                        }
                    }
                    if (c == AppConstants.ACTIVE_USER_EXCEL) {
                        if (!value.isEmpty()) {
                            userImportItems.setActive(value);
                        }
                    }
                    if (c == AppConstants.FIRST_NAME_USER_EXCEL) {
                        if (!value.isEmpty()) {
                            userImportItems.setFirstName(value);
                        }
                    }
                    if (c == AppConstants.MIDDLE_NAME_USER_EXCEL) {
                        if (!value.isEmpty()) {
                            userImportItems.setMiddleName(value);
                        }
                    }
                    if (c == AppConstants.LAST_NAME_USER_EXCEL) {
                        if (!value.isEmpty()) {
                            userImportItems.setLastName(value);
                        }
                    }
                    if (c == AppConstants.DOJ_USER_EXCEL) {
                        if (!value.isEmpty()) {
                            userImportItems.setDOJ(value);
                        }
                    }
                    if (c == AppConstants.DOB_USER_EXCEL) {
                        if (!value.isEmpty()) {
                            userImportItems.setDOB(value);
                        }
                    }
                    if (c == AppConstants.QUALIFICATION_USER_EXCEL) {
                        if (!value.isEmpty()) {
                            userImportItems.setQualifications(value);
                        }
                    }
                    if (c == AppConstants.EXPERIENCE_USER_EXCEL) {
                        if (!value.isEmpty()) {
                            userImportItems.setExperienceYears(value);
                        }
                    }
                    if (c == AppConstants.EMAIL1_USER_EXCEL) {
                        if (!value.isEmpty()) {
                            userImportItems.setEmail1(value);
                        }
                    }
                    if (c == AppConstants.EMAIL2_USER_EXCEL) {
                        if (!value.isEmpty()) {
                            userImportItems.setEmail1(value);
                        }
                    }
                    if (c == AppConstants.PHONE1_USER_EXCEL) {
                        if (!value.isEmpty()) {
                            userImportItems.setPhone1(value);
                        }
                    }
                    if (c == AppConstants.PHONE2_USER_EXCEL) {
                        if (!value.isEmpty()) {
                            userImportItems.setPhone2(value);
                        }
                    }
                    if (c == AppConstants.PAN_USER_EXCEL) {
                        if (!value.isEmpty()) {
                            userImportItems.setPANNumber(value);
                        }
                    }
                    if (c == AppConstants.ADDRESS1_USER_EXCEL) {
                        if (!value.isEmpty()) {
                            userImportItems.setAddressLine1(value);
                        }
                    }
                    if (c == AppConstants.ADDRESS2_USER_EXCEL) {
                        if (!value.isEmpty()) {
                            userImportItems.setAddressLine2(value);
                        }
                    }
                    if (c == AppConstants.ADDRESS3_USER_EXCEL) {
                        if (!value.isEmpty()) {
                            userImportItems.setAddressLine3(value);
                        }
                    }

                    if (c == AppConstants.CITY_USER_EXCEL) {
                        if (!value.isEmpty()) {
                            userImportItems.setCity(value);
                        }
                    }
                    if (c == AppConstants.DISTRICT_USER_EXCEL) {
                        if (!value.isEmpty()) {
                            userImportItems.setDistrict(value);
                        }
                    }
                    if (c == AppConstants.STATE_USER_EXCEL) {
                        if (!value.isEmpty()) {
                            userImportItems.setState(value);
                        }
                    }
                    if (c == AppConstants.PIN_USER_EXCEL) {
                        if (!value.isEmpty()) {
                            userImportItems.setPIN(value);
                        }
                    } else {

                        String cellInfo = "r:" + r + "; c:" + c + "; v:" + value;
//                            Log.d(TAG, "readExcelData: Data from row: " + cellInfo);
                        sb.append(value + ", ");
                    }


                }

                listUserImport.add(userImportItems);

            }
            sb.append(":");
            Log.e("list doctors", new Gson().toJson(userImportItems));

            if (listUserImport != null) {
                if (isOverrideCheck.isChecked()) {
                    isOverride = OVERRIDE;
                    Toasty.info(this, "Checked", Toast.LENGTH_SHORT).show();

                } else {
                    isOverride = DONT_OVERRIDE;
                    Toasty.info(this, "Unchecked", Toast.LENGTH_SHORT).show();

                }
                userImport.setOverride(isOverride);
                userImport.setUserList(listUserImport);
                callAPIToImportData(token, userImport);
            }



        }
    catch (Exception e) {
                Log.e(TAG, "readExcelData: FileNotFoundException. " + e.getMessage() );
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

    public void callAPIToImportData(String token, UserImport userImport){
        apiImportDoctorsFromExcel(token,  userImport);
    }

    public void apiImportDoctorsFromExcel(String token,  UserImport userImport){
        Log.e("Import Doctors params", new Gson().toJson(userImport));
        Call<MainResponse> call = apiInterface.importDoctorFromExcel(token,  userImport);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(@NonNull Call<MainResponse> call, @NonNull Response<MainResponse> response) {
                processDialog.dismissDialog();
                if (response.isSuccessful()){
                    assert response.body() != null;
                    if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                        Toasty.success(ActivityUploadUser.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toasty.error(ActivityUploadUser.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MainResponse> call, @NonNull Throwable t) {
                processDialog.dismissDialog();
                Toasty.error(ActivityUploadUser.this, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.card_upload_user_view:
                checkRequiredPermission();
                break;
            case R.id.button_upload_user_sheet:
               checkValidation();
                break;
            case R.id.image_sheet_view:
                checkRequiredPermission();
                break;
            case R.id.text_file_name:
                break;
        }
    }
}
