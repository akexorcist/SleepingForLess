package com.akexorcist.sleepingforless.view.post.holder;

import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.akexorcist.sleepingforless.R;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class HeaderPostViewHolder extends RecyclerView.ViewHolder {
    public TextView tvPostContentHeader;

    public HeaderPostViewHolder(View itemView) {
        super(itemView);
        tvPostContentHeader = (TextView) itemView.findViewById(R.id.tv_post_content_header);
    }

    public void setHeaderText(String text) {
        tvPostContentHeader.setText(text);
    }

    public void setHeaderSize(int size) {
        if (size == 1) {
            setTextSize(R.dimen.default_text_size_h1);
        } else if (size == 2) {
            setTextSize(R.dimen.default_text_size_h2);
        } else if (size == 3) {
            setTextSize(R.dimen.default_text_size_h3);
        } else if (size == 4) {
            setTextSize(R.dimen.default_text_size_h4);
        }
    }

    private void setTextSize(int dimenResourceId) {
        tvPostContentHeader.setTextSize(TypedValue.COMPLEX_UNIT_PX, tvPostContentHeader.getResources().getDimension(dimenResourceId));
    }
}
