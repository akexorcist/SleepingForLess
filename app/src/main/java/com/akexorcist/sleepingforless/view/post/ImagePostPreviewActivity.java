package com.akexorcist.sleepingforless.view.post;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.common.SFLActivity;
import com.akexorcist.sleepingforless.constant.Key;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

/**
 * Created by Akexorcist on 3/13/2016 AD.
 */
public class ImagePostPreviewActivity extends SFLActivity implements View.OnClickListener {
    private SubsamplingScaleImageView ivPreview;
    private Button btnPreviewClose;
    private String fullUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_reader_image_preview);
        ivPreview = (SubsamplingScaleImageView) findViewById(R.id.iv_preview);
        btnPreviewClose = (Button) findViewById(R.id.btn_preview_close);
        btnPreviewClose.setOnClickListener(this);

        getBundleFromIntent();

        Glide.with(this).load(fullUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                ivPreview.setImage(ImageSource.bitmap(resource));
            }
        });
    }

    public void getBundleFromIntent() {
        fullUrl = getIntent().getStringExtra(Key.KEY_FULL_URL);
    }

    @Override
    public void onClick(View v) {
        if (v == btnPreviewClose) {
            finish();
        }
    }
}
