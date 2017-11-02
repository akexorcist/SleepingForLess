package com.akexorcist.sleepingforless.view.feed;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.network.blogger.BloggerManager;
import com.akexorcist.sleepingforless.network.blogger.model.PostList;
import com.akexorcist.sleepingforless.util.content.ContentUtility;
import com.akexorcist.sleepingforless.util.content.EasterEggUtility;
import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

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
    public DilatingDotsProgressBar pbFeedImageLoading;

    public FeedViewHolder(View itemView) {
        super(itemView);
        bindView(itemView);
    }

    private void bindView(View view) {
        tvTitle = view.findViewById(R.id.tv_feed_title);
        tvLabel = view.findViewById(R.id.tv_feed_label);
        tvDate = view.findViewById(R.id.tv_feed_date);
        ivTitle = view.findViewById(R.id.iv_feed_title);
        ivBookmarkIndicator = view.findViewById(R.id.iv_feed_bookmark_indicator);
        mrlFeedButton = view.findViewById(R.id.mrl_feed_button);
        pbFeedImageLoading = view.findViewById(R.id.pb_feed_image_loading);
    }

    public void setTitle(String title) {
        title = ContentUtility.getInstance().removeLabelFromTitle(title);

        // Easter Egg for April Fool Day
        if (EasterEggUtility.newInstance().isAprilFoolDay()) {
            title = title.replaceAll("Android", " iOS ");
            title = title.replaceAll("แอนดรอยด์", " iOS ");
        }

        tvTitle.setText(title);
    }

    public void setLabel(List<String> labelList) {
        if (labelList != null && labelList.size() > 0) {
            String resultLabel = "";
            for (String label : labelList) {
                resultLabel += label + ", ";
            }
            resultLabel.replaceAll(", $", "");

            // Easter Egg for April Fool Day
            if (EasterEggUtility.newInstance().isAprilFoolDay()) {
                resultLabel = resultLabel.replaceAll("Android", "iOS");
            }

            tvLabel.setText(resultLabel);
            tvLabel.setVisibility(View.VISIBLE);
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
            pbFeedImageLoading.showNow();
            ivTitle.setImageDrawable(null);
            ivTitle.setVisibility(View.VISIBLE);
            String url = imageList.get(0).getUrl();
            loadItemResource(ivTitle, url);
        } else {
            ivTitle.setVisibility(View.GONE);
        }
    }

    private void loadItemResource(ImageView ivTitle, String url) {
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .override(500, 500);
        Glide.with(ivTitle.getContext())
                .load(url)
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        pbFeedImageLoading.hideNow();
                        return false;
                    }
                })
                .into(ivTitle);
    }

    public void setBookmarkIndicator(boolean isBookmark) {
        if (isBookmark) {
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
