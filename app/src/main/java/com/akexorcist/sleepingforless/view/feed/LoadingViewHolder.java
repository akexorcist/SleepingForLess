package com.akexorcist.sleepingforless.view.feed;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.akexorcist.sleepingforless.R;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class LoadingViewHolder extends RecyclerView.ViewHolder {
    DilatingDotsProgressBar pbPostListLoading;

    public LoadingViewHolder(View itemView) {
        super(itemView);
        pbPostListLoading = (DilatingDotsProgressBar) itemView.findViewById(R.id.pb_post_list_loading);
    }
}
