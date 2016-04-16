package com.akexorcist.sleepingforless.view.post.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.akexorcist.sleepingforless.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class CodePostViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.tv_post_content_code)
    public TextView tvPostContentCode;

    public CodePostViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setCode(String code) {
        tvPostContentCode.setText(code);
    }
}
