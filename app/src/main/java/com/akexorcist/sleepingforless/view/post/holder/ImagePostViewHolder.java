package com.akexorcist.sleepingforless.view.post.holder;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.util.AnimationUtility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import java.io.File;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class ImagePostViewHolder extends RecyclerView.ViewHolder {
    public ImageView ivPostContentPlainImage;
    public DilatingDotsProgressBar pbPostContentImageLoading;

    public ImagePostViewHolder(View itemView) {
        super(itemView);
        bindView(itemView);
        setupView();
    }

    private void bindView(View view) {
        ivPostContentPlainImage = view.findViewById(R.id.iv_post_content_image);
        pbPostContentImageLoading = view.findViewById(R.id.pb_post_content_image_loading);
    }

    private void setupView() {
        ivPostContentPlainImage.setOnTouchListener(this::onContentImageTouch);
        pbPostContentImageLoading.showNow();
    }

    public void clearImage() {
        ivPostContentPlainImage.setImageDrawable(null);
    }

    public void load(String url) {
        clearImage();
        RequestOptions requestOptions = new RequestOptions()
                .override(500, 500)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(itemView.getContext())
                .load(url)
                .thumbnail(0.1f)
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        pbPostContentImageLoading.hide();
                        return false;
                    }
                })
                .into(ivPostContentPlainImage);
    }

    public void load(File file) {
        clearImage();
        RequestOptions requestOptions = new RequestOptions()
                .override(500, 500)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(itemView.getContext())
                .load(file)
                .thumbnail(0.1f)
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        pbPostContentImageLoading.hide();
                        return false;
                    }
                })
                .into(ivPostContentPlainImage);
    }

    public boolean onContentImageTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            scaleImageDown(view);
        } else if (event.getAction() == MotionEvent.ACTION_UP ||
                event.getAction() == MotionEvent.ACTION_CANCEL) {
            scaleImageButtonBack(view);
        }
        return false;
    }

    private void scaleImageDown(View v) {
        AnimationUtility.getInstance().scaleDown(v, 200);
    }

    private void scaleImageButtonBack(View v) {
        AnimationUtility.getInstance().scaleBack(v, 200);
    }

    public void setImageClickListener(View.OnClickListener listener) {
        ivPostContentPlainImage.setOnClickListener(listener);
    }

    public void setImageLongClickListener(View.OnLongClickListener listener) {
        ivPostContentPlainImage.setOnLongClickListener(listener);
    }
}
