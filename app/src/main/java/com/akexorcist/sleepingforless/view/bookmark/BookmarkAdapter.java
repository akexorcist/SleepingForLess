package com.akexorcist.sleepingforless.view.bookmark;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.util.ContentUtility;
import com.akexorcist.sleepingforless.view.bookmark.model.Bookmark;
import com.akexorcist.sleepingforless.view.bookmark.model.BookmarkLabel;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class BookmarkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_UNKNOWN = -1;
    private final int VIEW_TYPE_ITEM = 0;

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
        } else if (viewType == VIEW_TYPE_ITEM) {
            return new BookmarkViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_bookmark_item, parent, false));
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (bookmarkList == null || bookmarkList.size() == 0) {
            return VIEW_TYPE_UNKNOWN;
        }
        return VIEW_TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == VIEW_TYPE_ITEM) {
            final BookmarkViewHolder bookmarkViewHolder = (BookmarkViewHolder) holder;
            Bookmark bookmark = bookmarkList.get(holder.getAdapterPosition());
            setTitle(bookmarkViewHolder.tvTitle, bookmark.getTitle());
            setLabel(bookmarkViewHolder.tvLabel, bookmark.getLabelList());
            bookmarkViewHolder.ivTitle.setVisibility(View.GONE);
            bookmarkViewHolder.mrlFeedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemListener != null) {
                        itemListener.onItemClick(bookmarkViewHolder, bookmarkList.get(bookmarkViewHolder.getAdapterPosition()));
                    }
                }
            });
            bookmarkViewHolder.mrlFeedButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (itemListener != null) {
                        itemListener.onItemLongClick(bookmarkViewHolder, bookmarkList.get(bookmarkViewHolder.getAdapterPosition()));
                    }
                    return true;
                }
            });
        }
    }

    private void setTitle(TextView tvTitle, String title) {
        title = ContentUtility.getInstance().removeLabelFromTitle(title);
        tvTitle.setText(title);
    }

    private void setLabel(TextView tvLabel, List<BookmarkLabel> labelList) {
        if (labelList != null) {
            String label = "";
            for (int i = 0; i < labelList.size(); i++) {
                label += labelList.get(i).getLabel();
                if (i < labelList.size() - 1) {
                    label += ", ";
                }
            }
            tvLabel.setText(label);
        }
    }

    private void loadItemResource(ImageView ivTitle, String url) {
        Glide.with(ivTitle.getContext())
                .load(url)
                .crossFade(200)
                .thumbnail(0.2f)
                .centerCrop()
                .into(ivTitle);
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
