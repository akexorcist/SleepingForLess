package com.akexorcist.sleepingforless.view.bookmark;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.sleepingforless.R;
import com.balysv.materialripple.MaterialRippleLayout;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class BookmarkViewHolder extends RecyclerView.ViewHolder {
    public TextView tvTitle;
    public TextView tvLabel;
    public ImageView ivTitle;
    public MaterialRippleLayout mrlFeedButton;

    public BookmarkViewHolder(View itemView) {
        super(itemView);
        tvTitle = (TextView) itemView.findViewById(R.id.tv_bookmark_title);
        tvLabel = (TextView) itemView.findViewById(R.id.tv_bookmark_label);
        ivTitle = (ImageView) itemView.findViewById(R.id.iv_bookmark_title);
        mrlFeedButton = (MaterialRippleLayout) itemView.findViewById(R.id.mrl_bookmark_button);
    }
}
