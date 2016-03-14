package com.akexorcist.sleepingforless.view.feed;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.network.model.PostList;
import com.akexorcist.sleepingforless.util.AnimationUtility;
import com.akexorcist.sleepingforless.util.ContentUtility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class FeedAdapter extends RecyclerView.Adapter<FeedViewHolder> {
    private ItemListener itemListener;
    private List<PostList.Item> itemList;

    public FeedAdapter() {
    }

    public FeedAdapter(List<PostList.Item> itemList) {
        this.itemList = itemList;
    }

    public void setPostListItem(List<PostList.Item> itemList) {
        this.itemList = itemList;
    }

    public void setItemListener(ItemListener listener) {
        this.itemListener = listener;
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FeedViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_feed_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final FeedViewHolder holder, int position) {
        PostList.Item postItem = itemList.get(position);
        setTitle(holder.tvTitle, postItem.getTitle());
        setLabel(holder.tvLabel, postItem.getLabels());
        setPublishedDate(holder.tvPublishedDate, postItem.getPublished());
        holder.ivTitle.setImageDrawable(null);
        if (postItem.isImageAvailable()) {
            String url = postItem.getImages().get(0).getUrl();
            loadItemResource(holder, url);
        } else {
            // TODO Do something
        }
        holder.mrlFeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemListener != null) {
                    itemListener.onItemClick(holder, itemList.get(holder.getAdapterPosition()));
                }
            }
        });
        holder.mrlFeedButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (itemListener != null) {
                    itemListener.onItemLongClick(holder, itemList.get(holder.getAdapterPosition()));
                }
                return true;
            }
        });
    }

    private void setTitle(TextView tvTitle, String title) {
        title = ContentUtility.getInstance().removeLabelFromTitle(title);
        tvTitle.setText(title);
    }

    private void setLabel(TextView tvLabel, List<String> labelList) {
        if (labelList != null) {
            String label = "";
            for (int i = 0; i < labelList.size(); i++) {
                label += labelList.get(i);
                if (i < labelList.size() - 1) {
                    label += ", ";
                }
            }
            tvLabel.setText(label);
        }
    }

    private void setPublishedDate(TextView tvPublishedDate, String publishedDate) {
        String[] date = publishedDate.split("T")[0].split("-");
        tvPublishedDate.setText("Published " + date[2] + "/" + date[1] + "/" + date[0]);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                AnimationUtility.getInstance().fadeIn(v);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                AnimationUtility.getInstance().fadeOut(v);
            } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                AnimationUtility.getInstance().fadeOut(v);
            }
            return true;
        }
    };

    private void loadItemResource(final FeedViewHolder holder, String url) {
        Glide.with(holder.ivTitle.getContext())
                .load(url)
                .crossFade(200)
                .thumbnail(0.2f)
                .centerCrop()
                .into(holder.ivTitle);
    }

    public void clear() {
        if (itemList != null) {
            itemList.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public interface ItemListener {
        void onItemClick(FeedViewHolder holder, PostList.Item item);

        void onItemLongClick(FeedViewHolder holder, PostList.Item item);
    }
}
