package com.planera.mis.planera2.utils;

        import android.Manifest;
        import android.app.Activity;
        import android.content.Context;
        import android.content.pm.PackageManager;
        import android.os.Build;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.app.Fragment;
        import android.support.v4.content.ContextCompat;
        import android.util.Log;
        import android.widget.Toast;

        import java.util.ArrayList;
        import java.util.List;

/**
 * In Case of fragment you can use
 * Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.frame_container);
 * if (f instanceof MyAccountFragment) {
 * marshMallowPermissionFragment = new MarshMallowPermission(mContext, f);
 * }
 * <p/>
 * In Case of activity
 * marshMallowPermissionFragment = new MarshMallowPermission(mContext);
 * <p/>
 * <p/>
 * Also you should in your Override in your activity or in fragment
 *
 * @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
 * super.onRequestPermissionsResult(requestCode, permissions, grantResults);
 * if (requestCode == marshMallowPermissionFragment.READ_STORAGE_PERMISSION_REQUEST_CODE) {
 * if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
 * // Permission granted.
 * <p/>
 * } else {
 * // User refused to grant permission.
 * }
 * }
 * }
 */
public class RuntimePermissionCheck {
    public static final int RECORD_PERMISSION_REQUEST_CODE = 1;
    public static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 2;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 3;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 4;
    public static final int READ_PHONE_STATE_PERMISSION_REQUEST_CODE = 6;
    public static final int READ_CONTACT_PERMISSION_REQUEST_CODE = 5;
    public static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 7;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 9;
    public static final int READ_SMS_PERMISSION_REQUEST_CODE = 8;
    private final Context context;

    Fragment fragment;
    private String TAG = RuntimePermissionCheck.class.getSimpleName();

    /**
     * @param context
     * @param fragment
     */

    public RuntimePermissionCheck(Context context, Fragment fragment) {

        Log.e(TAG, "MarshMallowPermission() called with: " + "context = [" + context + "], activity = [" + fragment + "]");
        this.fragment = fragment;
        this.context = context;
    }

    /**
     * @param context
     */
    public RuntimePermissionCheck(Context context) {
        this.context = context;
    }

    public void requestPermissionForFragmentRecord() {
        fragment.requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_PERMISSION_REQUEST_CODE);
    }


    public void requestPermissionForRecord() {
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.RECORD_AUDIO},
                RECORD_PERMISSION_REQUEST_CODE);
    }

    public boolean checkPermissionForRecord() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = context.checkSelfPermission(Manifest.permission.RECORD_AUDIO);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }


    public void requestPermissionForFragmentLocation() throws Exception {
        try {
            fragment.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "requestPermissionForFragmentLocation: ", e);
            throw e;
        }
    }

    public void requestPermissionForLocation() throws Exception {
        try {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean checkPermissionForLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }


    public void requestPermissionForFragmentContact() throws Exception {
        try {
            fragment.requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACT_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void requestPermissionForContact() throws Exception {
        try {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_CONTACTS},
                    READ_CONTACT_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean checkPermissionForContact() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = context.checkSelfPermission(Manifest.permission.READ_CONTACTS);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }


    public void requestPermissionForFragmentReadPhone() throws Exception {
        try {
            fragment.requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void requestPermissionForReadPhone() throws Exception {
        try {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_PHONE_STATE},
                    READ_PHONE_STATE_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean checkPermissionForReadPhone() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }


    public void requestPermissionForFragmentReadExtertalStorage() throws Exception {
        try {
            fragment.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void requestPermissionForReadExtertalStorage() throws Exception {
        try {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_STORAGE_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean checkPermissionForReadExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }


    public void requestPermissionForFragmentWriteExternalStorage() throws Exception {
        try {
            fragment.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void requestPermissionForWriteExternalStorage() throws Exception {
        try {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean checkPermissionForWriteExternalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }
    public boolean verifyPermissions(Context context, String[] grantResults) {
        for (String result : grantResults) {
            if (ActivityCompat.checkSelfPermission(context, result) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void requestPermissionForFragmentCamera() throws Exception {

        try {
            fragment.requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void requestPermissionForCamera() throws Exception {
        try {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean checkPermissionForCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = context.checkSelfPermission(Manifest.permission.CAMERA);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }


    public boolean checkPermissionForSMS() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = context.checkSelfPermission(Manifest.permission.READ_SMS);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }


    public void requestPermissionForFragmentSMS() throws Exception {

        try {
            fragment.requestPermissions(new String[]{Manifest.permission.READ_SMS}, READ_SMS_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void requestPermissionForSMS() throws Exception {
        try {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_SMS},
                    READ_SMS_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    public boolean checkPermissionForCall() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = context.checkSelfPermission(Manifest.permission.CALL_PHONE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }
    /*Multiple permission in single dialog*/


    public boolean checkAndRequestMultiplePermission() {

        int permissionReadMessage = ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_SMS);
        int permissionReadCoarseLocation = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionReadFineLocation = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionWriteStorage = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCamera = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        int permissionReadPhoneState = ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_PHONE_STATE);
        int permissionRecordAudio = ContextCompat.checkSelfPermission(context,
                Manifest.permission.RECORD_AUDIO);
        int fineLocationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();

        try {
            if (fineLocationPermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (permissionReadMessage != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_SMS);
            }
            if (permissionReadFineLocation != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (permissionReadCoarseLocation != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }

            if (permissionWriteStorage != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.CAMERA);
            }
            if (permissionReadPhoneState != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (permissionRecordAudio != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!listPermissionsNeeded.isEmpty()) {
            try {
                ActivityCompat.requestPermissions((Activity) context, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        } else {

            return true;
        }

    }

}
