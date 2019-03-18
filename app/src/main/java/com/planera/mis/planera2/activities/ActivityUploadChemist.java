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
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import com.planera.mis.planera2.models.ChemistImport;
import com.planera.mis.planera2.models.ChemistImportItems;
import com.planera.mis.planera2.models.MainResponse;
import com.planera.mis.planera2.utils.AppConstants;
import com.planera.mis.planera2.utils.InternetConnection;
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

import static com.planera.mis.planera2.utils.AppConstants.PATCH_ID_CHEMIST_EXCEL;

public class ActivityUploadChemist extends BaseActivity implements View.OnClickListener {

    public static ActivityUploadDoctor instance;
    private LinearLayout cardUploadChemistView;
    private ImageView imageSheetView;
    private Button buttonUploadChemistSheet;
    private RuntimePermissionCheck permissionCheck;
    private String displayName;
    private Toolbar toolbarUploadChemist;
    private ReadFileAsyncTask readFileAsyncTask;
    private TextView textFileName;
    private ImageView imageClose;
    private CheckBox isOverrideCheck;
    private ChemistImport chemistImport;
    private List<ChemistImportItems> chemistImportItemsList;
    private Uri uri = null;
    public static  int OVERRIDE = 1;
    public static  int DONT_OVERRIDE = 2;
    int isOverride;
    public static final String TAG = ActivityUploadDoctor.class.getSimpleName();
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
        toolbarUploadChemist = findViewById(R.id.toolbar_upload_chemist);
        buttonUploadChemistSheet = findViewById(R.id.button_upload_chemist_sheet);
        isOverrideCheck = findViewById(R.id.check_is_override);
        textFileName = findViewById(R.id.text_file_name);

        setSupportActionBar(toolbarUploadChemist);
        getSupportActionBar().setTitle("Upload Chemist");
        toolbarUploadChemist.setNavigationIcon(R.drawable.back_arrow_whit);
        toolbarUploadChemist.setNavigationOnClickListener(view -> onBackPressed());
        cardUploadChemistView.setOnClickListener(this);
        buttonUploadChemistSheet.setOnClickListener(this);
    }

    public void checkRequiredPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            pickFile();
        } else {
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
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"), 100);
        } catch (android.content.ActivityNotFoundException ex) {
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
                    processDialog.showDialog(ActivityUploadChemist.this, false);

                    if (data != null) {
                        uri = data.getData();
                    }
                    assert uri != null;
                    String uriString = uri.toString();
                    File myFile = new File(uriString);

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
                            assert cursor != null;
                            cursor.close();
                        }
                    } else if (uriString.startsWith("file://")) {
                        displayName = myFile.getName();
                        toggleView(displayName);

                    }
                    processDialog.dismissDialog();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void toggleView(String name) {
        if (name != null) {
            imageSheetView.setVisibility(View.VISIBLE);
            textFileName.setVisibility(View.VISIBLE);
            textFileName.setText(name);

        }
    }

    @Override
    public void initData() {
        super.initData();
        chemistImportItemsList = new ArrayList<>();
        chemistImport = new ChemistImport();
        permissionCheck = new RuntimePermissionCheck(ActivityUploadChemist.this);
    }



    public void checkValidation(){
//        buttonUploadChemistSheet.setEnabled(false);
        String text = textFileName.getText().toString();
        if (text.equals(getString(R.string.select_file))){
            buttonUploadChemistSheet.setEnabled(true);

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
            XSSFSheet sheet = workbook.getSheetAt(CHEMIST_SHEET);
            int rowsCount = sheet.getPhysicalNumberOfRows();
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            StringBuilder sb = new StringBuilder();
            //loop, loops through rows
            for (int r = 1; r < rowsCount; r++) {
                Row row = sheet.getRow(r);
                int cellsCount = row.getPhysicalNumberOfCells();
                ChemistImportItems chemists = new ChemistImportItems();
                //inner loop, loops through columns
                if (isCellNull){
                    break;
                }
                for (int c = 0; c < cellsCount; c++) {
                    String value = getCellAsString(row, c, formulaEvaluator);

                    if (c== PATCH_ID_CHEMIST_EXCEL && value.isEmpty()){
                        isCellNull = true;
                        break;
                    }
                    if (c == AppConstants.CHEMIST_ID_EXCEL){
                        chemists.setChemistId(value);
                    }
                    if (c == PATCH_ID_CHEMIST_EXCEL){
                        chemists.setPatchId(value);
                    }
                    if (c == AppConstants.ACTIVE_CHEMIST_EXCEL){
                        if (!value.isEmpty()){
                            chemists.setActive(value);
                        }
                    }
                    if (c == AppConstants.COMPANY_NAME_CHEMIST_EXCEL){
                        if (!value.isEmpty()){
                                chemists.setCompanyName(value);
                        }
                    }
                    if (c == AppConstants.MONTHLY_VOLUME_POTENTIAL_CHEMIST_EXCEL){
                        if (!value.isEmpty()){
                            chemists.setMonthlyVolumePotential(value);
                        }
                    }
                    if (c == AppConstants.SHOP_SIZE_CHEMIST_EXCEL){
                        if (!value.isEmpty()){
                                chemists.setShopSize(value);
                        }
                    }
                    if (c == AppConstants.ADDRESS1_CHEMIST_EXCEL){
                        if (!value.isEmpty()){
                                chemists.setAddressLine1(value);
                        }
                    }
                    if (c == AppConstants.ADDRESS2_CHEMIST_EXCEL){
                        if (!value.isEmpty()){
                            chemists.setAddressLine2(value);
                        }
                    }
                    if (c == AppConstants.ADDRESS3_CHEMIST_EXCEL){
                        if (!value.isEmpty()){
                            chemists.setAddressLine3(value);
                        }
                    }
                    if (c == AppConstants.BILLING_PHONE1_CHEMIST_EXCEL){
                        if (!value.isEmpty()){
                            chemists.setBillingPhone1(value);
                        }
                    }
                    if (c == AppConstants.BILLING_PHONE2_CHEMIST_EXCEL){
                        if (!value.isEmpty()){
                                chemists.setBillingPhone2(value);
                        }
                    }
                    if (c == AppConstants.BILLING_EMAIL_CHEMIST_EXCEL){
                        if (!value.isEmpty()){
                            chemists.setBillingEmail(value);
                        }
                    }
                    if (c == AppConstants.CITY_CHEMIST_EXCEL){
                        if (!value.isEmpty()){
                            chemists.setCity(value);
                        }
                    }
                    if (c == AppConstants.DISTRICT_CHEMIST_EXCEL){
                        if (!value.isEmpty()){
                            chemists.setDistrict(value);
                        }
                    }
                    if (c == AppConstants.STATE_CHEMIST_EXCEL){
                        if (!value.isEmpty()){
                            chemists.setState(value);
                        }
                    }

                    if (c == AppConstants.PIN_CHEMIST_EXCEL){
                        if (!value.isEmpty()){
                            chemists.setPIN(value);
                        }
                    }

                    if (c == AppConstants.FIRST_NAME_CHEMSIT_EXCEL){
                        if (!value.isEmpty()){
                            chemists.setFirstName(value);
                        }
                    }

                    if (c == AppConstants.MIDDLE_NAME_CHEMSIT_EXCEL){
                        if (!value.isEmpty()){
                            chemists.setMiddleName(value);
                        }
                    }

                    if (c == AppConstants.LAST_NAME_CHEMSIT_EXCEL){
                        if (!value.isEmpty()){
                            chemists.setLastName(value);
                        }
                    }

                    if (c == AppConstants.OWNER_PHONE_CHEMSIT_EXCEL){
                        if (!value.isEmpty()){
                            chemists.setOnwerPhone(value);
                        }
                    }

                    if (c == AppConstants.OWNER_EMAIL_CHEMSIT_EXCEL){
                        if (!value.isEmpty()){
                            chemists.setOnwerEmail(value);
                        }
                    }

                    if (c == AppConstants.PREFFERED_MEET_TIME_CHEMIST_EXCEL){
                        if (!value.isEmpty()){
                            chemists.setPreferredMeetTime(value);
                        }
                    }
                    else{

                        String cellInfo = "r:" + r + "; c:" + c + "; v:" + value;
                        Log.d(TAG, "readExcelData: Data from row: " + cellInfo);
                        sb.append(value + ", ");
//                        Toast.makeText(this, new String(sb), Toast.LENGTH_SHORT).show();
                    }

                }

                chemistImportItemsList.add(chemists);
            }
            sb.append(":");



        }catch (Exception e) {

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


    public void callChemistImportApi(String token, ChemistImport chemistImports){

        if (chemistImports != null){
            if (InternetConnection.isNetworkAvailable(ActivityUploadChemist.this)){
                apiImportChemistFromExcel(token,   chemistImports);
            }
            else {
                Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
            }
    }
    }


    public void apiImportChemistFromExcel(String token,  ChemistImport chemistImportList){
        processDialog.showDialog(ActivityUploadChemist.this, false);
        Log.e("Import Doctors params", new Gson().toJson(chemistImportList));
        Call<MainResponse> call = apiInterface.importChemistFromExcel(token,   chemistImportList);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(@NonNull Call<MainResponse> call, @NonNull Response<MainResponse> response) {
                processDialog.dismissDialog();
                if (response.isSuccessful()){
                    assert response.body() != null;
                    if (response.body().getStatusCode() == AppConstants.RESULT_OK){
                        buttonUploadChemistSheet.setEnabled(true);
                        Log.e("Import api response", new Gson().toJson(response.body()));
                    }
                    else{
                        buttonUploadChemistSheet.setEnabled(true);
                        Log.e("Import api response", new Gson().toJson(response.body()));
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<MainResponse> call, @NonNull Throwable t) {
                processDialog.dismissDialog();
                Toasty.error(ActivityUploadChemist.this, t.getMessage(), Toast.LENGTH_LONG).show();
                buttonUploadChemistSheet.setActivated(true);
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

//            processDialog.showDialog(ActivityUploadChemist.this, false);
        }

        @Override
        protected String doInBackground(Void... voids) {

            checkValidation();

            return "Success";
        }

        @Override
        protected void onPostExecute(String onlineVersion) {
            super.onPostExecute(onlineVersion);

            if (onlineVersion.equals("Success") && chemistImportItemsList != null) {
                new Handler().postDelayed(() -> {
                    if (chemistImportItemsList != null){

                        if (isOverrideCheck.isChecked()){
                            isOverride = OVERRIDE;

                        }
                        else{
                            isOverride = DONT_OVERRIDE;
                        }
                        chemistImport.setChemistImportList(chemistImportItemsList);
                        chemistImport.setIsOverride(isOverride);
                        callChemistImportApi(token, chemistImport);
                    }
                }, 500);

            }
            else{
                processDialog.dismissDialog();
            }
        }
    }
}
