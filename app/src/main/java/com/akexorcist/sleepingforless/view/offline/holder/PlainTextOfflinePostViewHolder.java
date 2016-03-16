package com.akexorcist.sleepingforless.view.offline.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.akexorcist.sleepingforless.R;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class PlainTextOfflinePostViewHolder extends RecyclerView.ViewHolder {
    public TextView tvPostContentPlainText;

    public PlainTextOfflinePostViewHolder(View itemView) {
        super(itemView);
        tvPostContentPlainText = (TextView) itemView.findViewById(R.id.tv_post_content_plain_text);
    }
}
