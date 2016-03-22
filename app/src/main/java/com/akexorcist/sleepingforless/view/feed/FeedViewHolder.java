package com.akexorcist.sleepingforless.view.feed;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.database.BookmarkManager;
import com.akexorcist.sleepingforless.network.blogger.BloggerManager;
import com.akexorcist.sleepingforless.network.blogger.model.PostList;
import com.akexorcist.sleepingforless.util.ContentUtility;
import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class FeedViewHolder extends RecyclerView.ViewHolder {
    public TextView tvTitle;
    public TextView tvLabel;
    public TextView tvDate;
    public ImageView ivTitle;
    public ImageView ivBookmarkIndicator;
    public MaterialRippleLayout mrlFeedButton;

    public FeedViewHolder(View itemView) {
        super(itemView);
        tvTitle = (TextView) itemView.findViewById(R.id.tv_feed_title);
        tvLabel = (TextView) itemView.findViewById(R.id.tv_feed_label);
        tvDate = (TextView) itemView.findViewById(R.id.tv_feed_date);
        ivTitle = (ImageView) itemView.findViewById(R.id.iv_feed_title);
        ivBookmarkIndicator = (ImageView) itemView.findViewById(R.id.iv_feed_bookmark_indicator);
        mrlFeedButton = (MaterialRippleLayout) itemView.findViewById(R.id.mrl_feed_button);
    }

    public void setTitle(String title) {
        title = ContentUtility.getInstance().removeLabelFromTitle(title);
        tvTitle.setText(title);
    }

    public void setLabel(List<String> labelList) {
        if (labelList != null && labelList.size() > 0) {
            String label = "";
            for (int i = 0; i < labelList.size(); i++) {
                label += labelList.get(i);
                if (i < labelList.size() - 1) {
                    label += ", ";
                }
            }
            tvLabel.setText(label);
        } else {
            tvLabel.setVisibility(View.GONE);
        }
    }

    public void setSortDate(String sortType, String publishedDate, String updateDate) {
        if (sortType != null && sortType.equalsIgnoreCase(BloggerManager.SORT_UPDATED_DATE)) {
            setDate(tvDate, updateDate, R.string.updated_date);
        } else {
            setDate(tvDate, publishedDate, R.string.published_date);
        }
    }

    private void setDate(TextView tvPublishedDate, String date, int messageResId) {
        String[] dates = date.split("T")[0].split("-");
        String publishedDetail = tvPublishedDate.getContext().getString(messageResId);
        tvPublishedDate.setText(String.format(publishedDetail, dates[2], dates[1], dates[0]));
    }

    public void setImage(List<PostList.Image> imageList) {
        if (imageList != null && !imageList.isEmpty() && imageList.size() > 0) {
            ivTitle.setImageDrawable(null);
            ivTitle.setVisibility(View.VISIBLE);
            String url = imageList.get(0).getUrl();
            loadItemResource(ivTitle, url);
        } else {
            ivTitle.setVisibility(View.GONE);
        }
    }

    private void loadItemResource(ImageView ivTitle, String url) {
        Glide.with(ivTitle.getContext())
                .load(url)
                .crossFade(200)
                .thumbnail(0.1f)
                .centerCrop()
                .into(ivTitle);
    }

    public void setBookmarkIndicator(String postId) {
        if (BookmarkManager.getInstance().isBookmark(postId)) {
            ivBookmarkIndicator.setVisibility(View.VISIBLE);
        } else {
            ivBookmarkIndicator.setVisibility(View.GONE);
        }
    }

    public void setFeedClickListener(View.OnClickListener listener) {
        mrlFeedButton.setOnClickListener(listener);
    }

    public void setFeedLongClickListener(View.OnLongClickListener listener) {
        mrlFeedButton.setOnLongClickListener(listener);
    }


}
