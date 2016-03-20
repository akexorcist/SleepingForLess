package com.akexorcist.sleepingforless.view.offline;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.common.SFLActivity;
import com.akexorcist.sleepingforless.constant.Key;
import com.akexorcist.sleepingforless.database.BookmarkManager;
import com.akexorcist.sleepingforless.util.StorageUtility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Akexorcist on 3/13/2016 AD.
 */
public class OfflineImagePostPreviewActivity extends SFLActivity implements View.OnClickListener {
    private SubsamplingScaleImageView ivPreview;
    private FloatingActionButton fabPreviewClose;
    private FloatingActionButton fabPreviewDownload;
    private String url;
    private String postId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_image_preview);

        getBundleFromIntent();

        bindView();
        setupView();
        setImagePreview();
    }

    private void bindView() {
        fabPreviewClose = (FloatingActionButton) findViewById(R.id.fab_preview_close);
        fabPreviewDownload = (FloatingActionButton) findViewById(R.id.fab_preview_download);
        ivPreview = (SubsamplingScaleImageView) findViewById(R.id.iv_preview);
    }

    private void setupView() {
        fabPreviewClose.setOnClickListener(this);
        fabPreviewDownload.setOnClickListener(this);
    }

    public void getBundleFromIntent() {
        url = getIntent().getStringExtra(Key.IMAGE_PATH);
        postId = getIntent().getStringExtra(Key.POST_ID);
    }

    @Override
    public void onClick(View v) {
        if (v == fabPreviewClose) {
            finish();
        } else if (v == fabPreviewDownload) {
            downloadImage();
        }
    }

    private void setImagePreview() {
        Glide.with(this)
                .load(BookmarkManager.getInstance().getBookmarkImageFile(postId, url))
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .thumbnail(0.1f)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        ivPreview.setImage(ImageSource.bitmap(resource));
                    }
                });
    }

    private void downloadImage() {
        download(BookmarkManager.getInstance().getBookmarkImageFile(postId, url), new SimpleTarget<File>() {
            @Override
            public void onResourceReady(File file, GlideAnimation<? super File> glideAnimation) {
                File destinationFile = new File(StorageUtility.getInstance().getDownloadsDirectory(), file.getName().replaceAll(".", "_") + ".jpg");
                StorageUtility.getInstance().copyToDownloadsDirectory(file, destinationFile);
                StorageUtility.getInstance().updateImageToMediaScanner(destinationFile);
                showSavedMessage(destinationFile);
            }
        });
    }

    private void download(File file, SimpleTarget<File> simpleTarget) {
        Glide.with(this)
                .load(file)
                .downloadOnly(simpleTarget);
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
}
