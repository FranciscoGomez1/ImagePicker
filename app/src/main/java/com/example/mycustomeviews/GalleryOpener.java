package com.example.mycustomeviews;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;


import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class GalleryOpener extends AppCompatActivity{
    //need the uri
    public static Uri galleryUri = null;
    private Uri cam_uri;

    private ComponentActivity activity;
    private ActivityResultLauncher<String> mGetContent;

    private ActivityResultLauncher<Intent> startCamera;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    private ImageHasbeenSelected imageHasbeenSelected;

    private final int ALL_CAMERA_PERMISSIONS = 101;
    private final int ALL_GALLERY_PERMISSIONS = 202;
    private final String[] camaraPermissions;
    private final String[] galleryPermissions;



    public GalleryOpener(ComponentActivity activity) {
        this.activity = activity;

        camaraPermissions = new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
        galleryPermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};

        this.mGetContent = activity.registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    // Handle the returned Uri
                    //This is used in case the user hits the back button without selecting an image
                    if( uri != null) {
                        galleryUri = uri;
                        //Call Listener
                        imageHasbeenSelected.imageUpdate();
                    }

                });

        this.startCamera = activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        // There are no request codes
                        galleryUri = cam_uri;
                        imageHasbeenSelected.imageUpdate();

                    }
                });

        this.requestPermissionLauncher =
                activity.registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        // Permission is granted. Continue the action or workflow in your
                        // app.
                        mGetContent.launch("image/*");

                    } else {
                        // Explain to the user that the feature is unavailable because the
                        // features requires a permission that the user has denied. At the
                        // same time, respect the user's decision. Don't link to system
                        // settings in an effort to convince the user to change their
                        // decision.
                    }
                });


    }
    public void launchGallery(){
        Log.e("LAUNCHGALLERY", "YES");
        requestReadingPermissionsAndLaunchGallery();
    }

    private void requestReadingPermissionsAndLaunchGallery(){
        if (hasPermissions(galleryPermissions)) {
            mGetContent.launch("image/*");
            Log.e("PERMISSIONS", "GRANTED");

        } else {
            /*Log.e("PERMISSIONS", "NOT_GRANTED");
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(activity, galleryPermissions, ALL_GALLERY_PERMISSIONS);
            }*/
            requestPermissionLauncher.launch(
                    Manifest.permission.CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case ALL_GALLERY_PERMISSIONS:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mGetContent.launch("image/*");

                    // Permission is granted. Continue the action or workflow
                    // in your app.
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                }
                return;
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    }


    public void requestCameraPermission(){

    }

    public void openPhotoApp(){
        requestCameraApp();
    }

    public void requestCameraApp(){
        if (hasPermissions(camaraPermissions)) {
            pickCamera();

        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                activity.requestPermissions(camaraPermissions, ALL_CAMERA_PERMISSIONS);
            }
        }
    }

    private boolean hasPermissions(String[] permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }




    public void pickCamera() {
        Log.d("OPENCAMERA", "openCamera");
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        cam_uri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cam_uri);
        startCamera.launch(cameraIntent);

    }


    public void galleryOpenerListener( ImageHasbeenSelected imageHasbeenSelected){
        this.imageHasbeenSelected = imageHasbeenSelected;
    }

    public interface ImageHasbeenSelected{
        void imageUpdate();
    }

}
