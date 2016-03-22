package com.akexorcist.sleepingforless.view.post.holder;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.util.AnimationUtility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import java.io.File;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class ImagePostViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {
    public ImageView ivPostContentPlainImage;
    public DilatingDotsProgressBar pbPostContentImageLoading;

    public ImagePostViewHolder(View itemView) {
        super(itemView);
        ivPostContentPlainImage = (ImageView) itemView.findViewById(R.id.iv_post_content_image);
        pbPostContentImageLoading = (DilatingDotsProgressBar) itemView.findViewById(R.id.pb_post_content_image_loading);
        pbPostContentImageLoading.showNow();
        ivPostContentPlainImage.setOnTouchListener(this);
    }

    public void clearImage() {
        ivPostContentPlainImage.setImageDrawable(null);
    }

    public void load(String url) {
        clearImage();
        Glide.with(itemView.getContext())
                .load(url)
                .override(500, 500)
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        pbPostContentImageLoading.hideNow();
                        return false;
                    }
                })
                .into(ivPostContentPlainImage);
    }

    public void load(File file) {
        clearImage();
        Glide.with(itemView.getContext())
                .load(file)
                .override(500, 500)
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<File, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, File model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, File model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        pbPostContentImageLoading.hideNow();
                        return false;
                    }
                })
                .into(ivPostContentPlainImage);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            scaleImageDown(v);
        } else if (event.getAction() == MotionEvent.ACTION_UP ||
                event.getAction() == MotionEvent.ACTION_CANCEL) {
            scaleImageButtonBack(v);
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
