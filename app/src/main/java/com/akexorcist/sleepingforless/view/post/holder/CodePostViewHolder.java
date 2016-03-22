package com.akexorcist.sleepingforless.view.post.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.akexorcist.sleepingforless.R;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class CodePostViewHolder extends RecyclerView.ViewHolder {
    public TextView tvPostContentCode;

    public CodePostViewHolder(View itemView) {
        super(itemView);
        tvPostContentCode = (TextView) itemView.findViewById(R.id.tv_post_content_code);
    }

    public void setCode(String code) {
        tvPostContentCode.setText(code);
    }
}
