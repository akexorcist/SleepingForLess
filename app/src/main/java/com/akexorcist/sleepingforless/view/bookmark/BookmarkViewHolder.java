package com.akexorcist.sleepingforless.view.bookmark;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.util.ContentUtility;
import com.akexorcist.sleepingforless.view.bookmark.model.BookmarkLabel;
import com.balysv.materialripple.MaterialRippleLayout;

import java.util.List;

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
        ivTitle.setVisibility(View.GONE);
    }

    public void setTitle(String title) {
        title = ContentUtility.getInstance().removeLabelFromTitle(title);
        tvTitle.setText(title);
    }

    public void setLabel(List<BookmarkLabel> labelList) {
        if (labelList != null && labelList.size() > 0) {
            String label = "";
            for (int i = 0; i < labelList.size(); i++) {
                label += labelList.get(i).getLabel();
                if (i < labelList.size() - 1) {
                    label += ", ";
                }
            }
            tvLabel.setText(label);
        } else {
            tvLabel.setVisibility(View.GONE);
        }
    }

    public void setFeedClickListener(View.OnClickListener listener) {
        mrlFeedButton.setOnClickListener(listener);
    }

    public void setFeedLongClickListener(View.OnLongClickListener listener) {
        mrlFeedButton.setOnLongClickListener(listener);
    }
}
