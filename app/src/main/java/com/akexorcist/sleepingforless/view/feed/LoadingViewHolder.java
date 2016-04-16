package com.akexorcist.sleepingforless.view.feed;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.akexorcist.sleepingforless.R;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class LoadingViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.pb_post_list_loading)
    public DilatingDotsProgressBar pbPostListLoading;

    public LoadingViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void showLoadingNow() {
        pbPostListLoading.showNow();
    }
}
