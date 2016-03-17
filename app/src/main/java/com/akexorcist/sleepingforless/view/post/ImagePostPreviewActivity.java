package com.akexorcist.sleepingforless.view.post;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
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
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.IntentPickerSheetView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Akexorcist on 3/13/2016 AD.
 */
public class ImagePostPreviewActivity extends SFLActivity implements View.OnClickListener {
    private SubsamplingScaleImageView ivPreview;
    private FloatingActionButton fabClose;
    private FloatingActionButton fabDownload;
    private String fullUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_image_preview);

        getBundleFromIntent();
        bindView();
        setupView();
        downloadImageToPreview();
    }

    private void bindView() {
        ivPreview = (SubsamplingScaleImageView) findViewById(R.id.iv_preview);
        fabClose = (FloatingActionButton) findViewById(R.id.fab_preview_close);
        fabDownload = (FloatingActionButton) findViewById(R.id.fab_preview_download);
    }

    private void setupView() {
        fabClose.setOnClickListener(this);
        fabDownload.setOnClickListener(this);
    }

    private void downloadImageToPreview() {
        Glide.with(this)
                .load(fullUrl)
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
        fullUrl = getIntent().getStringExtra(Key.KEY_FULL_URL);
    }

    @Override
    public void onClick(View v) {
        if (v == fabClose) {
            finish();
        } else if (v == fabDownload) {
            downloadImage();
        }
    }

    private void downloadImage() {
        download(fullUrl, new SimpleTarget<File>() {
            @Override
            public void onResourceReady(File file, GlideAnimation<? super File> glideAnimation) {
                File destinationFile = new File(getDownloadsDirectory(), file.getName().replaceAll(".", "_") + ".jpg");
                copyToDownloadsDirectory(file, destinationFile);
                updateImageToMediaScanner(destinationFile);
                showSavedMessage("Saved image to Downloads directory.", destinationFile);
            }
        });
    }

    public void updateImageToMediaScanner(File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);
    }

    private void download(String url, SimpleTarget<File> simpleTarget) {
        Glide.with(this)
                .load(url)
                .downloadOnly(simpleTarget);
    }

    private File getDownloadsDirectory() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    }

    private void copyToDownloadsDirectory(File source, File destination) {
        try {
            InputStream inputStream = new FileInputStream(source);
            OutputStream outputStream = new FileOutputStream(destination);

            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
        }
    }

    private void showSavedMessage(String message, final File file) {
        Snackbar.make(ivPreview, message, Snackbar.LENGTH_LONG).setAction("View", new View.OnClickListener() {
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
