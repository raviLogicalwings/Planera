package com.planera.mis.planera2.fragments;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.ActivityUploadChemist;
import com.planera.mis.planera2.activities.ActivityUploadDoctor;
import com.planera.mis.planera2.mailSender.GMail;
import com.planera.mis.planera2.mailSender.SendMailTask;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Objects;

import javax.mail.MessagingException;

import es.dmoral.toasty.Toasty;

import static android.app.Activity.RESULT_OK;

public class UploadDataFragment extends BaseFragment implements View.OnClickListener {
    public View view;
    private ImageView closeContent, iconAttachFile;
    private CardView layoutEmailContent;
    public static UploadDataFragment instance;
    private FloatingActionButton buttonSend;
    private EditText editRecepientName;
    private EditText editMessage;
    private EditText editSubject;
    private TextView textFileName;
    private String strReceipient, strSubject, strMessage, displayName, path;
    private Uri uri;
    private ArrayList<Uri> multipleUri;
    private File myFile;
    public static final String FROM = "ravi.logicalwings@gmail.com";
    public static final String PASSWORD = "1993@ravi";
    public static final int REQUEST_CODE = 100;


    public static UploadDataFragment getInstance() {
        if (instance == null) {
            instance = new UploadDataFragment();
        }
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_upload_data, container, false);
        initUi();
        initData();
        return view;
    }


    public UploadDataFragment() {
    }

    @Override
    protected void initData() {
        super.initData();
        strReceipient = editRecepientName.getText().toString().trim();
        strMessage = editMessage.getText().toString().trim();
        strSubject = editSubject.getText().toString().trim();


    }

    public void uiValidation(){
        strReceipient = editRecepientName.getText().toString().trim();
        strMessage = editMessage.getText().toString().trim();
        strSubject = editSubject.getText().toString().trim();


        if (TextUtils.isEmpty(strReceipient)){
            Toasty.error(mContext, "Please enter a email address.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(strMessage)){
            Toasty.error(mContext, "Please enter a message.", Toast.LENGTH_SHORT).show();
        }
        else if(!isEmail(strReceipient)){
            Toasty.error(mContext, "Invalid email address.", Toast.LENGTH_SHORT).show();
        }

        else{
            sendMail(strReceipient, strMessage, strSubject, myFile);
        }
    }


    public boolean isEmail(String email){
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
                    if (data != null) {
                        multipleUri = new ArrayList<>();
                          ClipData clipData = data.getClipData();
                            for (int i=0; i<clipData.getItemCount() ; i++){
                                uri = data.getClipData().getItemAt(i).getUri();
                                multipleUri.add(uri);
                            }
                            if (!multipleUri.isEmpty()){
                                getAllSelectedFiles(multipleUri);
                            }

                    }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void initUi() {
        super.initUi();
        iconAttachFile = view.findViewById(R.id.icon_add_attachment);
        buttonSend = view.findViewById(R.id.float_send_button);
        editMessage = view.findViewById(R.id.edit_email_message);
        editSubject = view.findViewById(R.id.edit_email_subject);
        editRecepientName = view.findViewById(R.id.edit_receiver_email);
        closeContent = view.findViewById(R.id.close_email_content);
        layoutEmailContent = view.findViewById(R.id.card_email_content);
        CardView cardSendToMail = view.findViewById(R.id.card_send_file_to_mail);
        CardView cardUploadDoctor = view.findViewById(R.id.card_upload_doctor);
        CardView cardUploadChemist = view.findViewById(R.id.card_upload_chemist);
        textFileName = view.findViewById(R.id.text_file);

        cardUploadChemist.setOnClickListener(this);
        cardUploadDoctor.setOnClickListener(this);
        cardSendToMail.setOnClickListener(this);
        closeContent.setOnClickListener(this);
        buttonSend.setOnClickListener(this);
        iconAttachFile.setOnClickListener(this);

    }

    public void pickFile(){

            Intent intent = new Intent();
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.ACTION_GET_CONTENT,  true);
            startActivityForResult(Intent.createChooser(intent, "Select file"),REQUEST_CODE);
    }


    public void sendMail(String to, String message, String subject, File file){
        new SendMailTask(mContext).execute(FROM,
                PASSWORD, to, subject, message, file);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.card_send_file_to_mail:
                layoutEmailContent.setVisibility(View.VISIBLE);
                break;
            case R.id.card_upload_doctor:
                Intent intentDoctorUpload = new Intent(mContext, ActivityUploadDoctor.class);
                startActivity(intentDoctorUpload);
                break;
            case R.id.card_upload_chemist:
                Intent intentChemistUpload = new Intent(mContext, ActivityUploadChemist.class);
                startActivity(intentChemistUpload);
                break;
            case R.id.close_email_content:
                layoutEmailContent.setVisibility(View.INVISIBLE);
                break;
            case R.id.float_send_button:
                uiValidation();
                break;
            case R.id.icon_add_attachment:
                pickFile();
                break;
        }

    }


    public void getAllSelectedFiles(ArrayList<Uri> multipleUri) {

        for (Uri uri : multipleUri) {
            assert uri != null;
            String uriString = uri.toString();
            myFile = new File(uriString);
            displayName = null;


            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = mContext.getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))+", "+displayName;
                        textFileName.setVisibility(View.VISIBLE);
                        textFileName.setText(displayName);
                    }
                } finally {
                    assert cursor != null;
                    cursor.close();
                }
            } else if (uriString.startsWith("file://")) {
                displayName = myFile.getName()+", " +displayName;
            }
        }
        textFileName.setText(displayName);

    }

}
