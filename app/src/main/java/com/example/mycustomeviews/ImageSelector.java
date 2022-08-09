package com.example.mycustomeviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.activity.ComponentActivity;

public class ImageSelector extends RelativeLayout {
    private GalleryOpener galleryOpener;
    private ComponentActivity activity;
    private final int DEFAULT_BUTTON_COLOR = 0xFFA4C639;
    private final int DEFAULT_BUTTON_TEXT_COLOR = 0xFFFFFFFF;
    private final String DEFAULT_BUTTON_TEXT = "Button Text";

    private Button selectorBtn;

    public ImageSelector(Context context) {
        this(context,null);
    }


    public ImageSelector(Context context, AttributeSet attrs) {
        this(context, attrs,0);

    }

    public ImageSelector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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

            setButtonColor(a.getInt(R.styleable.ImageSelector_button_color,DEFAULT_BUTTON_COLOR
                    ));
            setTextButtonColor(a.getInt(R.styleable.ImageSelector_button_text_color, DEFAULT_BUTTON_TEXT_COLOR));

            setButtonText(a.getString(R.styleable.ImageSelector_button_text));

            a.recycle();
        }
    }

    private void init(){
        selectorBtn = findViewById(R.id.image_selector_btm);
        selectorBtn.setOnClickListener(view -> openGallery());
        findViewById(R.id.image).setOnClickListener(view -> takePhoto());
        galleryOpener.galleryOpenerListener(() -> ((ImageView) findViewById(R.id.image)).setImageURI(GalleryOpener.galleryUri));

    }

    public void setButtonColor(int color){
        selectorBtn.setBackgroundColor(color);

    }

    public void setTextButtonColor(int textColor) {
        selectorBtn.setTextColor(textColor);

    }

    public void setButtonText(String text){
        if(text != null){
            selectorBtn.setText(text);
        }else{
            selectorBtn.setText(DEFAULT_BUTTON_TEXT);
        }
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
