package com.akexorcist.sleepingforless.view.feed;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.database.BookmarkManager;
import com.akexorcist.sleepingforless.network.blogger.model.PostList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_UNKNOWN = -1;
    private final int VIEW_TYPE_CONTENT = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private ItemListener itemListener;
    private LoadMoreListener loadMoreListener;
    private List<PostList.Item> itemList;
    private List<String> bookmarkIdList;
    private boolean isLoadMoreAvailable;
    private String sortType;

    public FeedAdapter() {
        getBookmarkIdList();
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public void setPostListItem(List<PostList.Item> itemList) {
        this.itemList = itemList;
        updateData();
    }

    public List<PostList.Item> getItemList() {
        return itemList;
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
        updateData();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_CONTENT) {
            return new FeedViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_feed_item, parent, false));
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
        return position < itemList.size() ? VIEW_TYPE_CONTENT : VIEW_TYPE_LOADING;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == VIEW_TYPE_CONTENT) {
            addContentView(holder);
        } else if (viewType == VIEW_TYPE_LOADING) {
            addLoadingView(holder);
        }
    }

    private void addContentView(RecyclerView.ViewHolder holder) {
        final FeedViewHolder feedViewHolder = (FeedViewHolder) holder;
        final PostList.Item postItem = itemList.get(holder.getAdapterPosition());
        feedViewHolder.setTitle(postItem.getTitle());
        feedViewHolder.setLabel(postItem.getLabels());
        feedViewHolder.setSortDate(sortType, postItem.getPublished(), postItem.getUpdated());
        feedViewHolder.setImage(postItem.getImages());
        feedViewHolder.setBookmarkIndicator(isBookmark(postItem.getId()));
        feedViewHolder.setFeedClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemListener != null) {
                    itemListener.onItemClick(feedViewHolder, postItem);
                }
            }
        });
        feedViewHolder.setFeedLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (itemListener != null) {
                    itemListener.onItemLongClick(feedViewHolder, postItem);
                }
                return true;
            }
        });
    }

    private void addLoadingView(RecyclerView.ViewHolder holder) {
        LoadingViewHolder feedViewHolder = (LoadingViewHolder) holder;
        feedViewHolder.showLoadingNow();
        if (isLoadMoreAvailable && loadMoreListener != null) {
            loadMoreListener.onLoadMore();
        }
    }

    public void clear() {
        if (itemList != null) {
            itemList.clear();
        }
        updateData();
    }

    public void updateData() {
        getBookmarkIdList();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (itemList == null) {
            return 0;
        }
        return isLoadMoreAvailable ? itemList.size() + 1 : itemList.size();
    }

    public interface ItemListener {
        void onItemClick(FeedViewHolder holder, PostList.Item item);

        void onItemLongClick(FeedViewHolder holder, PostList.Item item);
    }

    public interface LoadMoreListener {
        void onLoadMore();
    }

    private void getBookmarkIdList() {
        bookmarkIdList = BookmarkManager.getInstance().getBookmarkIdList();
    }

    public boolean isBookmark(String postId) {
        for (String bookmarkId : bookmarkIdList) {
            if (postId.equalsIgnoreCase(bookmarkId)) {
                return true;
            }
        }
        return false;
    }
}
