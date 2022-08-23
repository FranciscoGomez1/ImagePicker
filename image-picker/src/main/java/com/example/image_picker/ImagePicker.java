package com.example.image_picker;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.activity.ComponentActivity;
import android.util.Log;


import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ImagePicker extends AppCompatActivity {
    //need the uri
    private Uri photoUri = null;
    private Uri cam_uri;

    private ComponentActivity activity;
    private ActivityResultLauncher<String> mGetContent;
    private ActivityResultLauncher<Intent> startCamera;
    private ActivityResultLauncher<String[]> requestPermissionCameraLauncher;
    private ActivityResultLauncher<String[]> requestPermissionGalleryLauncher;
    private ImageHasbeenSelected imageHasbeenSelected;

    private final int ALL_CAMERA_PERMISSIONS = 101;
    private final int ALL_GALLERY_PERMISSIONS = 202;
    private final String[] camaraPermissions;
    private final String[] galleryPermissions;

    public ImagePicker(ComponentActivity activity) {
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
                        photoUri = uri;
                        //Call Listener
                        imageHasbeenSelected.imageUpdate();
                    }

                });

        this.startCamera = activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        photoUri = cam_uri;
                        imageHasbeenSelected.imageUpdate();

                    }
                });

        this.requestPermissionCameraLauncher =  activity.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                isGranted ->{
                    Log.d("PERMISSIONS", "Launcher result: " + isGranted.toString());
                    if (isGranted.containsValue(false)) {
                        Log.d("PERMISSIONS", "At least one of the permissions was not granted, launching again...");
                    }else{
                        pickCamera();
                    }
                });

        this.requestPermissionGalleryLauncher =  activity.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                isGranted ->{
                    Log.d("PERMISSIONS", "Launcher result: " + isGranted.toString());
                    if (isGranted.containsValue(false)) {
                        Log.d("PERMISSIONS", "At least one of the permissions was not granted, launching again...");
                    }else{
                        openGalleryApp();
                    }
                });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case ALL_GALLERY_PERMISSIONS:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGalleryApp();


                }
                // the else is commented out because it has empty body
/*                else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                }*/
            case ALL_CAMERA_PERMISSIONS:
                if (grantResults.length > 0 && hasPermissions(galleryPermissions)) {
                    pickCamera();
                }
                // the else is commented out because it has empty body
/*                else {

                }*/
        }
    }

    public void launchGallery(){
        requestGalleryApp();
    }

    public void openPhotoApp(){
        requestCameraApp();
    }


    private void requestGalleryApp(){
        if (hasPermissions(galleryPermissions)) {
            openGalleryApp();
            Log.e("PERMISSIONS", "GRANTED");

        } else {
            requestPermissionGalleryLauncher.launch(galleryPermissions);
        }
    }


    public void requestCameraApp(){

        if (hasPermissions(camaraPermissions)) {
            pickCamera();
            Log.e("PERMISSIONS", "GRANTED");

        } else {
            requestPermissionCameraLauncher.launch(camaraPermissions);
        }
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

    private void openGalleryApp() {
        mGetContent.launch("image/*");
    }

    //Check if all the the permissions are granted from a nonnull array of permissions
    private boolean hasPermissions(String[] permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    //if one of the permission is not granted return false
                    if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                        return false;
                    }
                }
            }
            return true;
        }
        //If the array is null it will return false
        return false;
    }

    public Uri getPhotoUri(){
        return photoUri;
    }

    public void galleryOpenerListener( ImageHasbeenSelected imageHasbeenSelected){
        this.imageHasbeenSelected = imageHasbeenSelected;
    }

    public interface ImageHasbeenSelected{
        void imageUpdate();
    }

}
