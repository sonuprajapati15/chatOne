package com.example.chatone.permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.chatone.enums.RequestCode;

public class DevicePermission extends AppCompatActivity {

    public void checkDevicePermission(String permission, int requestCode) {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(getApplicationContext(), permission)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat
                    .requestPermissions((Activity) getApplicationContext(),
                            new String[]{permission},
                            requestCode);
        }
    }

    // This function is called when user accept or decline the permission.
// Request Code is used to check which permission called this function.
// This request code is provided when user is prompt for permission.

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        switch (RequestCode.getEnumById(requestCode)){
            case SEND_SMS:
                isGranted(grantResults);
                break;
            case ACCESS_FINE_LOCATION:
                isGranted(grantResults);
                break;
            case ACCESS_NETWORK_STATE:
                isGranted(grantResults);
                break;
            case READ_EXTERNAL_STORAGE:
                isGranted(grantResults);
                break;
            case ACCESS_COARSE_LOCATION:
                isGranted(grantResults);
                break;
            case CAMERA_PERMISSION_CODE:
                isGranted(grantResults);
                break;
            case WRITE_EXTERNAL_STORAGE:
                isGranted(grantResults);
                break;
            case STORAGE_PERMISSION_CODE:
                isGranted(grantResults);
                break;
        }

    }

    private void isGranted(int[] grantResults){
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        } else {
            Toast.makeText(getApplicationContext(), "Camera Permission Denied", Toast.LENGTH_SHORT)
                    .show();
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
