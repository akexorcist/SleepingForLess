package com.akexorcist.sleepingforless.view.offline.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.akexorcist.sleepingforless.R;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class ImageOfflinePostViewHolder extends RecyclerView.ViewHolder {
    public ImageView ivPostContentPlainImage;

    public ImageOfflinePostViewHolder(View itemView) {
        super(itemView);
        ivPostContentPlainImage = (ImageView) itemView.findViewById(R.id.iv_post_content_image);
    }
}
