package com.example.mycustomeviews;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.activity.ComponentActivity;
import androidx.fragment.app.Fragment;

public class ImageSelector extends RelativeLayout {
    private GalleryOpener galleryOpener;
    public ImageSelector(Context context) {
        super(context);
    }

    private ComponentActivity activity;

    public ImageSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.activity= (ComponentActivity) context;
        galleryOpener = new GalleryOpener(activity);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.image_selector, this);


        init();
        if (attrs!=null) {
            TypedArray a=getContext()
                    .obtainStyledAttributes(attrs,
                            R.styleable.ImageSelector,
                            0, 0);

            setColor(a.getInt(R.styleable.ImageSelector_buttonColor,
                    0xFFA4C639));
            a.recycle();
        }

    }

    public ImageSelector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    private void init(){
        findViewById(R.id.image_selector_btm).setOnClickListener(view -> openGallery());
        findViewById(R.id.image).setOnClickListener(view -> takePhoto());
        galleryOpener.galleryOpenerListener(() -> ((ImageView) findViewById(R.id.image)).setImageURI(GalleryOpener.galleryUri));

    }

    private void setColor(int color){
       findViewById(R.id.image_selector_btm).setBackgroundColor(color);

    }

    private void takePhoto(){
        galleryOpener.openPhotoApp();
    }

    private void openGallery(){

        galleryOpener.launchGallery();

    }

    //Return the imageView from the ImagesSelector groupViews
    //With this the Image of ImageView can programmatically
    public ImageView getSelectorImage(){
        return findViewById(R.id.image);
    }
    public  void setBackground(Integer drawable){
        ((ImageView) findViewById(R.id.image)).setImageResource(drawable);
    }

}
