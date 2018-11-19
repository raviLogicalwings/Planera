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
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.planera.mis.planera2.R;
import com.planera.mis.planera2.models.ChemistImport;
import com.planera.mis.planera2.models.MainResponse;
import com.planera.mis.planera2.utils.AppConstants;
import com.planera.mis.planera2.utils.RuntimePermissionCheck;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityUploadChemist extends BaseActivity implements View.OnClickListener {

    public static ActivityUploadDoctor instance;
    private CardView cardUploadChemistView;
    private ImageView imageSheetView;
    private Button buttonUploadChemistSheet;
    private RuntimePermissionCheck permissionCheck;
    private String displayName;
    private TextView textFileName;
    private ImageView imageClose;
    private List<ChemistImport> chemistImportList;
    private ChemistImport chemists;
    private Uri uri = null;
    public static final String TAG = ActivityUploadDoctor.class.getSimpleName();
    private File myFile;
    private boolean isCellNull = false;
    public static final int CHEMIST_SHEET = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_chemist);
        initUi();
        initData();


    }

    @Override
    public void initUi() {
        super.initUi();
        cardUploadChemistView = findViewById(R.id.card_upload_chemist_view);
        imageSheetView = findViewById(R.id.image_sheet_view);
        buttonUploadChemistSheet = findViewById(R.id.button_upload_chemist_sheet);
        textFileName = findViewById(R.id.text_file_name);
        imageClose = findViewById(R.id.img_close);
        imageSheetView.setOnClickListener(this);
        cardUploadChemistView.setOnClickListener(this);
        buttonUploadChemistSheet.setOnClickListener(this);
    }

    public void checkRequiredPermission() {
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

    public void pickFile() {
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
                    String uriString = uri.toString();
                    myFile = new File(uriString);

//                    readExcelData(uri);
                    String path = myFile.getAbsolutePath();
                    Log.e(TAG, "FILE PATH" + path);
                    displayName = null;


//                    Toast.makeText(ActivityUploadDoctor.this, myFile.toString(), Toast.LENGTH_LONG).show();


                    if (uriString.startsWith("content://")) {
                        Cursor cursor = null;
                        try {
                            cursor = ActivityUploadChemist.this.getContentResolver().query(uri, null, null, null, null);
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


    public void toggleView(String name) {
        if (name != null) {
            cardUploadChemistView.setVisibility(View.GONE);
            imageSheetView.setVisibility(View.VISIBLE);
            textFileName.setVisibility(View.VISIBLE);
            textFileName.setText(name);

        }
    }

    @Override
    public void initData() {
        super.initData();
        chemistImportList = new ArrayList<>();

        permissionCheck = new RuntimePermissionCheck(ActivityUploadChemist.this);
    }


    private void readExcelData(Uri uri) {

        try {
            // Creating Input Stream
            Log.e("File Path", uri.toString());
//            File file = new File (uri.getPath());
            InputStream inputStream = getContentResolver().openInputStream(uri);

            // Create a workbook using the File System
            XSSFWorkbook myWorkBook = null;
            if (inputStream != null) {
                myWorkBook = new XSSFWorkbook(inputStream);
            }

            // Get the first sheet from workbook
            XSSFSheet mySheet = myWorkBook.getSheetAt(CHEMIST_SHEET);

            /** We now need something to iterate through the cells.**/
            Iterator<Row> rowIter = mySheet.rowIterator();

            while (rowIter.hasNext()) {
                HSSFRow myRow = (HSSFRow) rowIter.next();
                Iterator<Cell> cellIter = myRow.cellIterator();
                while (cellIter.hasNext()) {
                    HSSFCell myCell = (HSSFCell) cellIter.next();
                    Log.w("FileUtils", "Cell Value: " + myCell.toString());
                    Toast.makeText(ActivityUploadChemist.this, "cell Value: " + myCell.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }



//        Toast.makeText(this, "Working fine", Toast.LENGTH_LONG).show();
//        try {
////            File fileB = new File(filePath);
//            InputStream inputStream = getContentResolver().openInputStream(uri);
////                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
////                StringBuilder stringBuilder = new StringBuilder();
////                String line;
////                while ((line = reader.readLine()) != null){
////                    stringBuilder.append(line);
////                }
////
////                inputStream.close();
////              Log.e("String Result", stringBuilder.toString());
////
//            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
//            XSSFSheet sheet = workbook.getSheetAt(CHEMIST_SHEET);
//            int rowsCount = sheet.getPhysicalNumberOfRows();
//            Toast.makeText(this, "rowCount"+rowsCount, Toast.LENGTH_LONG).show();
//            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
//            StringBuilder sb = new StringBuilder();
//            //loop, loops through rows
//            for (int r = 1; r < rowsCount; r++) {
//                Row row = sheet.getRow(r);
//                int cellsCount = row.getPhysicalNumberOfCells();
//                chemists = new ChemistImport();
//                //inner loop, loops through columns
//                if (isCellNull){
//                    break;
//                }
//                for (int c = 0; c < cellsCount; c++) {
//                    String value = getCellAsString(row, c, formulaEvaluator);
//
//                    if (c==0 && value.isEmpty()){
//                        isCellNull = true;
//                        break;
//                    }
//                    if (c == AppConstants.DOCTOR_ID_EXCEL){
//                        chemists.setChemistId(value);
//                    }
//                    if (c == AppConstants.PATCH_ID_EXCEL){
//                        if (!value.isEmpty()){
//                            chemists.setPatchId(value);
//                        }
//                    }
//                    if (c == AppConstants.FIRST_NAME_EXCEL){
//                        if (!value.isEmpty()){
//                            chemists.setFirstName(value);
//                        }
//                    }
//                    if (c == AppConstants.MIDDLE_NAME_EXCEL){
//                        if (!value.isEmpty()){
////                                chemists.setMiddleName(value);
//                        }
//                    }
//                    if (c == AppConstants.LAST_NAME_EXCEL){
//                        if (!value.isEmpty()){
//                            chemists.setLastName(value);
//                        }
//                    }
//                    if (c == AppConstants.DOB_EXCEL){
//                        if (!value.isEmpty()){
////                                chemists.setDOB(value);
//                        }
//                    }
//                    if (c == AppConstants.EMAIL_EXCEL){
//                        if (!value.isEmpty()){
////                                chemists.setEmail(value);
//                        }
//                    }
//                    if (c == AppConstants.ACTIVE_EXCEL){
//                        if (!value.isEmpty()){
//                            chemists.setActive(value);
//                        }
//                    }
//                    if (c == AppConstants.PREFERRED_MEET_TIME_EXCEL){
//                        if (!value.isEmpty()){
//                            chemists.setPreferredMeetTime(value);
//                        }
//                    }
//                    if (c == AppConstants.COMPANY_NAME_EXCEL){
//                        if (!value.isEmpty()){
//                            chemists.setCompanyName(value);
//                        }
//                    }
//                    if (c == AppConstants.PHONE_EXCEL){
//                        if (!value.isEmpty()){
////                                chemists.setPhone(value);
//                        }
//                    }
//                    if (c == AppConstants.ADDRESS_1_EXCEL){
//                        if (!value.isEmpty()){
//                            chemists.setAddressLine1(value);
//                        }
//                    }
//                    if (c == AppConstants.ADDRESS_2_EXCEL){
//                        if (!value.isEmpty()){
//                            chemists.setAddressLine2(value);
//                        }
//                    }
//                    if (c == AppConstants.ADDRESS_3_EXCEL){
//                        if (!value.isEmpty()){
//                            chemists.setAddressLine3(value);
//                        }
//                    }
//                    if (c == AppConstants.CITY_EXCEL){
//                        if (!value.isEmpty()){
//                            chemists.setCity(value);
//                        }
//                    }
//                    if (c == AppConstants.DISTRICT_EXCEL){
//                        if (!value.isEmpty()){
//                            chemists.setDistrict(value);
//                        }
//                    }
//                    if (c == AppConstants.STATE_EXCEL){
//                        if (!value.isEmpty()){
//                            chemists.setState(value);
//                        }
//                    }
//
//                    if (c == AppConstants.PINCODE_EXCEL){
//                        if (!value.isEmpty()){
//                            chemists.setPIN(value);
//                        }
//                    }
//
//                    else{
//
//                        String cellInfo = "r:" + r + "; c:" + c + "; v:" + value;
//                        Log.d(TAG, "readExcelData: Data from row: " + cellInfo);
//                        sb.append(value + ", ");
//                        Toast.makeText(this, new String(sb), Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//
//                chemistImportList.add(chemists);
//            }
//            sb.append(":");
//
//            if (chemistImportList != null){
//                apiImportChemistFromExcel(token, chemistImportList);
//            }
//
//        }catch (Exception e) {
//            Log.e(TAG, "readExcelData: FileNotFoundException. " + e.getMessage() );
//        }


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
                    int numericValue = (int)cellValue.getNumberValue();
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


    public void apiImportChemistFromExcel(String token, List<ChemistImport> chemistImportList){
        Log.e("Import Doctors params", new Gson().toJson(chemistImportList));
//        processDialog.showDialog(ActivityUploadDoctor.this, false);
        Call<MainResponse> call = apiInterface.importChemistFromExcel(token, chemistImportList);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(@NonNull Call<MainResponse> call, @NonNull Response<MainResponse> response) {
//                processDialog.dismissDialog();
                if (response.isSuccessful()){
                    if (response.body().getStatusCode() == AppConstants.RESULT_OK){

                        Log.d("Import api response", new Gson().toJson(response.body()));
                    }
                    else{
                        Log.d("Import api response", new Gson().toJson(response.body()));
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<MainResponse> call, @NonNull Throwable t) {
//                processDialog.dismissDialog();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.card_upload_chemist_view:
                checkRequiredPermission();
                break;
            case R.id.button_upload_chemist_sheet:
                readExcelData(uri);
                break;
            case R.id.image_sheet_view:
                checkRequiredPermission();
                break;
            case R.id.text_file_name:
                break;
        }
    }
}
