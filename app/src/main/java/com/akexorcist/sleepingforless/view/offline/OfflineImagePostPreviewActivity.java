package com.akexorcist.sleepingforless.view.offline;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.common.SFLActivity;
import com.akexorcist.sleepingforless.constant.Key;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

/**
 * Created by Akexorcist on 3/13/2016 AD.
 */
public class OfflineImagePostPreviewActivity extends SFLActivity implements View.OnClickListener {
    private SubsamplingScaleImageView ivPreview;
    private FloatingActionButton fabPreviewClose;
    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_image_preview);
        ivPreview = (SubsamplingScaleImageView) findViewById(R.id.iv_preview);
        fabPreviewClose = (FloatingActionButton) findViewById(R.id.fab_preview_close);

        fabPreviewClose.setOnClickListener(this);

        getBundleFromIntent();

        Glide.with(this)
                .load(url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.1f)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        ivPreview.setImage(ImageSource.bitmap(resource));
                    }
                });
    }

    public void getBundleFromIntent() {
        url = getIntent().getStringExtra(Key.KEY_FULL_URL);
    }

    @Override
    public void onClick(View v) {
        if (v == fabPreviewClose) {
            finish();
        }
    }
}
