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
        return new BookmarkViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_bookmark_item, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int index = holder.getAdapterPosition();
        final Bookmark bookmark = bookmarkList.get(index);
        final BookmarkViewHolder bookmarkViewHolder = (BookmarkViewHolder) holder;
        bookmarkViewHolder.setTitle(bookmark.getTitle());
        bookmarkViewHolder.setLabel(bookmark.getLabelList());
        bookmarkViewHolder.setFeedClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemListener != null) {
                    itemListener.onItemClick(bookmarkViewHolder, bookmark);
                }
            }
        });
        bookmarkViewHolder.setFeedLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (itemListener != null) {
                    itemListener.onItemLongClick(bookmarkViewHolder, bookmark);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookmarkList != null ? bookmarkList.size() : 0;
    }

    private boolean isBookmarkListAvailable() {
        return bookmarkList != null && bookmarkList.size() > 0;
    }

    public interface ItemListener {
        void onItemClick(BookmarkViewHolder holder, Bookmark item);

        void onItemLongClick(BookmarkViewHolder holder, Bookmark item);
    }
}
