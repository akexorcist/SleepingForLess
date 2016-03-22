package com.akexorcist.sleepingforless.view.post.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.akexorcist.sleepingforless.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class ImagePostViewHolder extends RecyclerView.ViewHolder {
    public ImageView ivPostContentPlainImage;

    public ImagePostViewHolder(View itemView) {
        super(itemView);
        ivPostContentPlainImage = (ImageView) itemView.findViewById(R.id.iv_post_content_image);
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
                .into(ivPostContentPlainImage);
    }

    public void setImageClickListener(View.OnClickListener listener) {
        ivPostContentPlainImage.setOnClickListener(listener);
    }

    public void setImageLongClickListener(View.OnLongClickListener listener) {
        ivPostContentPlainImage.setOnLongClickListener(listener);
    }
}
