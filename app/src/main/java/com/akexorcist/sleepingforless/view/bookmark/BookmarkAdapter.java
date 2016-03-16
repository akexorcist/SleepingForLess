package com.akexorcist.sleepingforless.view.bookmark;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.network.model.PostList;
import com.akexorcist.sleepingforless.util.ContentUtility;
import com.akexorcist.sleepingforless.view.feed.LoadingViewHolder;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class BookmarkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_UNKNOWN = -1;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private ItemListener itemListener;
    private LoadMoreListener loadMoreListener;
    private List<PostList.Item> itemList;
    private boolean isLoadMoreAvailable;

    public BookmarkAdapter() {
    }

    public BookmarkAdapter(List<PostList.Item> itemList) {
        this.itemList = itemList;
    }

    public void setPostListItem(List<PostList.Item> itemList) {
        this.itemList = itemList;
    }

    public void setItemListener(ItemListener listener) {
        this.itemListener = listener;
    }

    public void setLoadMoreListener(LoadMoreListener listener) {
        this.loadMoreListener = listener;
    }

    public void setLoadMoreAvailable(boolean available) {
        isLoadMoreAvailable = available;
    }

    public void addPostListItem(List<PostList.Item> itemList) {
        if (itemList == null) {
            return;
        }
        if (this.itemList == null) {
            this.itemList = new ArrayList<>();
        }
        this.itemList.addAll(itemList);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (itemList == null || itemList.size() == 0) {
            return new LoadingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_feed_blank, parent, false));
        } else if (viewType == VIEW_TYPE_ITEM) {
            return new BookmarkViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_feed_item, parent, false));
        } else if (viewType == VIEW_TYPE_LOADING) {
            return new LoadingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_feed_loading, parent, false));
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (itemList == null || itemList.size() == 0) {
            return VIEW_TYPE_UNKNOWN;
        }
        return position < itemList.size() ? VIEW_TYPE_ITEM : VIEW_TYPE_LOADING;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == VIEW_TYPE_ITEM) {
            final BookmarkViewHolder bookmarkViewHolder = (BookmarkViewHolder) holder;
            PostList.Item postItem = itemList.get(holder.getAdapterPosition());
            setTitle(bookmarkViewHolder.tvTitle, postItem.getTitle());
            setLabel(bookmarkViewHolder.tvLabel, postItem.getLabels());
            setPublishedDate(bookmarkViewHolder.tvPublishedDate, postItem.getPublished());
            bookmarkViewHolder.ivTitle.setImageDrawable(null);
            if (postItem.isImageAvailable()) {
                bookmarkViewHolder.ivTitle.setVisibility(View.VISIBLE);
                String url = postItem.getImages().get(0).getUrl();
                loadItemResource(bookmarkViewHolder, url);
            } else {
                bookmarkViewHolder.ivTitle.setVisibility(View.GONE);
            }
            bookmarkViewHolder.mrlFeedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemListener != null) {
                        itemListener.onItemClick(bookmarkViewHolder, itemList.get(bookmarkViewHolder.getAdapterPosition()));
                    }
                }
            });
            bookmarkViewHolder.mrlFeedButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (itemListener != null) {
                        itemListener.onItemLongClick(bookmarkViewHolder, itemList.get(bookmarkViewHolder.getAdapterPosition()));
                    }
                    return true;
                }
            });
        } else if (viewType == VIEW_TYPE_LOADING) {
            LoadingViewHolder feedViewHolder = (LoadingViewHolder) holder;
//            feedViewHolder.pbPostListLoading.showNow();
            if (isLoadMoreAvailable && loadMoreListener != null) {
                loadMoreListener.onLoadMore();
            }
        }
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

    private void loadItemResource(final BookmarkViewHolder holder, String url) {
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
        if (itemList == null) {
            return 1;
        }
        return isLoadMoreAvailable ? itemList.size() + 1 : itemList.size();
    }

    public interface ItemListener {
        void onItemClick(BookmarkViewHolder holder, PostList.Item item);

        void onItemLongClick(BookmarkViewHolder holder, PostList.Item item);
    }

    public interface LoadMoreListener {
        void onLoadMore();
    }
}
