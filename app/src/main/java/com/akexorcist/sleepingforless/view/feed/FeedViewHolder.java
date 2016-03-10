package com.akexorcist.sleepingforless.view.feed;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.sleepingforless.R;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class FeedViewHolder extends RecyclerView.ViewHolder {
    TextView tvTitle;
    ImageView ivTitle;

    public FeedViewHolder(View itemView) {
        super(itemView);
        tvTitle = (TextView) itemView.findViewById(R.id.tv_feed_title);
        ivTitle = (ImageView) itemView.findViewById(R.id.iv_feed_title);
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public ImageView getIvTitle() {
        return ivTitle;
    }
}
