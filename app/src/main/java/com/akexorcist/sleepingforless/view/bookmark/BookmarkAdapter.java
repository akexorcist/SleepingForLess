package com.akexorcist.sleepingforless.view.bookmark;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.view.bookmark.model.Bookmark;

import java.util.List;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class BookmarkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_UNKNOWN = -1;
    private final int VIEW_TYPE_CONTENT = 0;

    private ItemListener itemListener;
    private List<Bookmark> bookmarkList;

    public BookmarkAdapter(List<Bookmark> bookmarkList) {
        this.bookmarkList = bookmarkList;
    }

    public void setItemListener(ItemListener listener) {
        this.itemListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (!isBookmarkListAvailable()) {
            return new BookmarkBlankViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_bookmark_blank, parent, false));
        } else if (viewType == VIEW_TYPE_CONTENT) {
            return new BookmarkViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_bookmark_item, parent, false));
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (bookmarkList == null || bookmarkList.size() == 0) {
            return VIEW_TYPE_UNKNOWN;
        }
        return VIEW_TYPE_CONTENT;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int index = holder.getAdapterPosition();
        int viewType = getItemViewType(index);
        if (viewType == VIEW_TYPE_CONTENT) {
            Bookmark bookmark = bookmarkList.get(index);
            final BookmarkViewHolder bookmarkViewHolder = (BookmarkViewHolder) holder;
            bookmarkViewHolder.setTitle(bookmark.getTitle());
            bookmarkViewHolder.setLabel(bookmark.getLabelList());
            bookmarkViewHolder.setFeedClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemListener != null) {
                        itemListener.onItemClick(bookmarkViewHolder, bookmarkList.get(index));
                    }
                }
            });
            bookmarkViewHolder.setFeedLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (itemListener != null) {
                        itemListener.onItemLongClick(bookmarkViewHolder, bookmarkList.get(index));
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (!isBookmarkListAvailable()) {
            return 1;
        }
        return bookmarkList.size();
    }

    private boolean isBookmarkListAvailable() {
        return bookmarkList != null && bookmarkList.size() > 0;
    }

    public interface ItemListener {
        void onItemClick(BookmarkViewHolder holder, Bookmark item);

        void onItemLongClick(BookmarkViewHolder holder, Bookmark item);
    }
}
