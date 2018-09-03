package com.planera.mis.planera2.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.utils.RuntimePermissionCheck;

import java.io.File;

public class ActivityUploadPlan extends BaseActivity implements View.OnClickListener{

    public static ActivityUploadPlan instance;
    private CardView cardUploadPlanView;
    private ImageView imageSheetView;
    private Button buttonUploadPlanSheet;
    private RuntimePermissionCheck permissionCheck;

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
        imageSheetView.setOnClickListener(this);
        cardUploadPlanView.setOnClickListener(this);
        buttonUploadPlanSheet.setOnClickListener(this);
    }

    public void checkRequiredPermission(){
        if (permissionCheck.checkPermissionForReadExtertalStorage() && permissionCheck.checkPermissionForWriteExternalStorage()){
            pickFile();
        }
        else{
            try {
                permissionCheck.requestPermissionForWriteExternalStorage();
                permissionCheck.requestPermissionForReadExtertalStorage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void pickFile(){
        Intent intent = new Intent((Intent.ACTION_GET_CONTENT));
//     Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

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
            case 1:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    String uriString = uri.toString();
                    File myFile = new File(uriString);
                    String path = myFile.getAbsolutePath();
                    String displayName = null;

                    if (uriString.startsWith("content://")) {
                        Cursor cursor = null;
                        try {
                            cursor = ActivityUploadPlan.this.getContentResolver().query(uri, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                            }
                        } finally {
                            cursor.close();
                        }
                    } else if (uriString.startsWith("file://")) {
                        displayName = myFile.getName();

                        Toast.makeText(this, displayName, Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void initData() {
        super.initData();
        permissionCheck = new RuntimePermissionCheck(ActivityUploadPlan.this);
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
                break;
        }
    }
}
