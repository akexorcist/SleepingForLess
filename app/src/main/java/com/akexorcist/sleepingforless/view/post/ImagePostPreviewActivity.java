package com.akexorcist.sleepingforless.view.post;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.afollestad.assent.Assent;
import com.afollestad.assent.AssentCallback;
import com.afollestad.assent.PermissionResultSet;
import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.analytic.EventKey;
import com.akexorcist.sleepingforless.analytic.EventTracking;
import com.akexorcist.sleepingforless.common.SFLActivity;
import com.akexorcist.sleepingforless.constant.Key;
import com.akexorcist.sleepingforless.util.StorageUtility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import java.io.File;

/**
 * Created by Akexorcist on 3/13/2016 AD.
 */
public class ImagePostPreviewActivity extends SFLActivity {
    private static final String KEY_FULL_URL = "key_full_url";

    private SubsamplingScaleImageView ivPreview;
    private FloatingActionButton fabClose;
    private FloatingActionButton fabDownload;
    private DilatingDotsProgressBar pbImagePreviewLoading;

    private String fullUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_image_preview);

        if (savedInstanceState == null) {
            getBundleFromIntent();
        }

        screenTracking();
        bindView();
        setupView();
        initRuntimePermissionRequest();

        if (savedInstanceState == null) {
            downloadImageToPreview();
        }
    }

    private void bindView() {
        ivPreview = findViewById(R.id.iv_preview);
        fabClose = findViewById(R.id.fab_preview_close);
        fabDownload = findViewById(R.id.fab_preview_download);
        pbImagePreviewLoading = findViewById(R.id.pb_image_preview_loading);
    }

    private void setupView() {
        fabDownload.setOnClickListener(view -> downloadImage());
        fabClose.setOnClickListener(view -> closePreview());

        pbImagePreviewLoading.showNow();
    }

    private void initRuntimePermissionRequest() {
        Assent.setActivity(this, this);
    }

    public void getBundleFromIntent() {
        fullUrl = getIntent().getStringExtra(Key.KEY_FULL_URL);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_FULL_URL, fullUrl);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        fullUrl = savedInstanceState.getString(KEY_FULL_URL);
        downloadImageToPreview();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Assent.handleResult(permissions, grantResults);
    }

    public void downloadImage() {
        grantExternalStoragePermission();
    }

    public void closePreview() {
        finish();
    }

    private void downloadImageToPreview() {
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(this)
                .asBitmap()
                .thumbnail(0.1f)
                .load(fullUrl)
                .apply(requestOptions)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        pbImagePreviewLoading.hideNow();
                        ivPreview.setImage(ImageSource.bitmap(resource));
                    }
                });
    }

    public void grantExternalStoragePermission() {
        if (!Assent.isPermissionGranted(Assent.WRITE_EXTERNAL_STORAGE)) {
            Assent.requestPermissions(new AssentCallback() {
                @Override
                public void onPermissionResult(PermissionResultSet result) {
                    if (result.isGranted(Assent.WRITE_EXTERNAL_STORAGE)) {
                        startDownload();
                    } else {
                        Snackbar.make(ivPreview, R.string.please_accept_write_external_storage_permission, Snackbar.LENGTH_LONG).show();
                    }
                }
            }, 5, Assent.WRITE_EXTERNAL_STORAGE);
        } else {
            startDownload();
        }
    }

    private void startDownload() {
        download(fullUrl, new SimpleTarget<File>() {
            @Override
            public void onResourceReady(File resource, Transition<? super File> transition) {
                File destinationFile = new File(StorageUtility.getInstance().getDownloadsDirectory(), resource.getName().replaceAll("\\.", "_") + ".jpg");
                StorageUtility.getInstance().copyToDownloadsDirectory(resource, destinationFile);
                StorageUtility.getInstance().updateImageToMediaScanner(destinationFile);
                showSavedMessage(destinationFile);
            }
        });
    }

    private void download(String url, SimpleTarget<File> simpleTarget) {
        Glide.with(this)
                .downloadOnly()
                .load(url)
                .into(simpleTarget);
    }

    private void showSavedMessage(final File file) {
        Snackbar.make(ivPreview, R.string.download_image_successful, Snackbar.LENGTH_LONG).setAction(R.string.action_view, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), "image/*");
                startActivity(intent);
            }
        }).show();
    }

    // Google Analytics
    private void screenTracking() {
        EventTracking.getInstance().addScreen(EventKey.Page.IMAGE_PREVIEW);
    }
}
