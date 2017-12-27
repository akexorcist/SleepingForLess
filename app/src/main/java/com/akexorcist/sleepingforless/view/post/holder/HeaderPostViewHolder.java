package com.akexorcist.sleepingforless.view.post.holder;

import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.util.content.EasterEggUtility;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class HeaderPostViewHolder extends RecyclerView.ViewHolder {
    public TextView tvPostContentHeader;

    public HeaderPostViewHolder(View itemView) {
        super(itemView);
        bindView(itemView);
    }

    private void bindView(View view) {
        tvPostContentHeader = view.findViewById(R.id.tv_post_content_header);
    }

    public void setHeaderText(String text) {
        // Easter Egg for April Fool Day
        if (EasterEggUtility.newInstance().isAprilFoolDay()) {
            text = text.replaceAll("Android", " iOS ");
            text = text.replaceAll("แอนดรอยด์", " iOS ");
        }
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
