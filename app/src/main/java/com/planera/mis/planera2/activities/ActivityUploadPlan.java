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

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.utils.RuntimePermissionCheck;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;

public class ActivityUploadPlan extends BaseActivity implements View.OnClickListener{

    public static ActivityUploadPlan instance;
    private CardView cardUploadPlanView;
    private ImageView imageSheetView;
    private Button buttonUploadPlanSheet;
    private RuntimePermissionCheck permissionCheck;
    private String displayName;
    private TextView textFileName;
    private ImageView imageClose;
    public static final String TAG = ActivityUploadPlan.class.getSimpleName();
    private File myFile;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragmend_upload_plan);
        initUi();
        initData();


    }

    @Override
    public void initUi() {
        super.initUi();
        cardUploadPlanView = findViewById(R.id.card_upload_Plan_view);
        imageSheetView = findViewById(R.id.image_sheet_view);
        buttonUploadPlanSheet = findViewById(R.id.button_upload_plan_sheet);
        textFileName = findViewById(R.id.text_file_name);
        imageClose = findViewById(R.id.img_close);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 100:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = null;
                    if (data != null) {
                        uri = data.getData();
                    }
                    String uriString = uri.toString();
                    myFile = new File(uriString);

                    readExcelData(myFile);
                    String path = myFile.getAbsolutePath();
                    Log.e(TAG, "FILE PATH"+path);
                    displayName = null;



//                    Toast.makeText(ActivityUploadPlan.this, myFile.toString(), Toast.LENGTH_LONG).show();


                    if (uriString.startsWith("content://")) {
                        Cursor cursor = null;
                        try {
                            cursor = ActivityUploadPlan.this.getContentResolver().query(uri, null, null, null, null);
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
            cardUploadPlanView.setVisibility(View.GONE);
            imageSheetView.setVisibility(View.VISIBLE);
            textFileName.setVisibility(View.VISIBLE);
            textFileName.setText(name);

        }
    }

    @Override
    public void initData() {
        super.initData();
        permissionCheck = new RuntimePermissionCheck(ActivityUploadPlan.this);
    }


    private void readExcelData(File filePath) {
        Log.e(TAG, filePath.toString());

        if(filePath.toString().isEmpty()){
            Toast.makeText(this, "Not exist", Toast.LENGTH_SHORT).show();
        }

        else{
            Toast.makeText(this, "Working fine", Toast.LENGTH_LONG).show();
            try {
//            File fileB = new File(filePath);
                FileInputStream inputStream = new FileInputStream(filePath);
                XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                XSSFSheet sheet = workbook.getSheetAt(0);
                int rowsCount = sheet.getPhysicalNumberOfRows();
                Toast.makeText(this, "rowCount"+rowsCount, Toast.LENGTH_LONG).show();
                FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
                StringBuilder sb = new StringBuilder();

                //loop, loops through rows
                for (int r = 1; r < rowsCount; r++) {
                    Row row = sheet.getRow(r);
                    int cellsCount = row.getPhysicalNumberOfCells();
                    //inner loop, loops through columns
                    for (int c = 0; c < cellsCount; c++) {

                        String value = getCellAsString(row, c, formulaEvaluator);
                        String cellInfo = "r:" + r + "; c:" + c + "; v:" + value;
                        Log.d(TAG, "readExcelData: Data from row: " + cellInfo);
                        sb.append(value + ", ");
                    }
                }
                sb.append(":");

            }catch (Exception e) {
                Log.e(TAG, "readExcelData: FileNotFoundException. " + e.getMessage() );
            }
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
                    double numericValue = cellValue.getNumberValue();
                    if(HSSFDateUtil.isCellDateFormatted(cell)) {
                        double date = cellValue.getNumberValue();
                        SimpleDateFormat formatter =
                                new SimpleDateFormat("MM/dd/yy");
                        value = formatter.format(HSSFDateUtil.getJavaDate(date));
                    } else {
                        value = ""+numericValue;
                    }
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
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.card_upload_Plan_view:
                checkRequiredPermission();
                break;
            case R.id.button_upload_plan_sheet:
                break;
            case R.id.image_sheet_view:
                checkRequiredPermission();
                break;
            case R.id.text_file_name:
                break;
        }
    }
}
