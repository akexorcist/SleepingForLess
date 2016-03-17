package com.akexorcist.sleepingforless.view.feed;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.sleepingforless.R;
import com.balysv.materialripple.MaterialRippleLayout;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class FeedViewHolder extends RecyclerView.ViewHolder {
    TextView tvTitle;
    TextView tvLabel;
    TextView tvDate;
    ImageView ivTitle;
    ImageView ivBookmarkIndicator;
    MaterialRippleLayout mrlFeedButton;

    public FeedViewHolder(View itemView) {
        super(itemView);
        tvTitle = (TextView) itemView.findViewById(R.id.tv_feed_title);
        tvLabel = (TextView) itemView.findViewById(R.id.tv_feed_label);
        tvDate = (TextView) itemView.findViewById(R.id.tv_feed_date);
        ivTitle = (ImageView) itemView.findViewById(R.id.iv_feed_title);
        ivBookmarkIndicator = (ImageView) itemView.findViewById(R.id.iv_feed_bookmark_indicator);
        mrlFeedButton = (MaterialRippleLayout) itemView.findViewById(R.id.mrl_feed_button);
    }
}
