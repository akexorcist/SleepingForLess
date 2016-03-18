package com.akexorcist.sleepingforless.view.offline;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.database.BookmarkManager;
import com.akexorcist.sleepingforless.view.offline.holder.CodeOfflinePostViewHolder;
import com.akexorcist.sleepingforless.view.offline.holder.HeaderOfflinePostViewHolder;
import com.akexorcist.sleepingforless.view.offline.holder.ImageOfflinePostViewHolder;
import com.akexorcist.sleepingforless.view.offline.holder.PlainTextOfflinePostViewHolder;
import com.akexorcist.sleepingforless.view.offline.holder.VideoOfflinePostViewHolder;
import com.akexorcist.sleepingforless.view.post.constant.PostType;
import com.akexorcist.sleepingforless.view.post.model.BasePost;
import com.akexorcist.sleepingforless.view.post.model.CodePost;
import com.akexorcist.sleepingforless.view.post.model.HeaderPost;
import com.akexorcist.sleepingforless.view.post.model.ImagePost;
import com.akexorcist.sleepingforless.view.post.model.PlainTextPost;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class OfflinePostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private PostClickListener postClickListener;
    private List<BasePost> postList;
    private String bookmarkPostId;

    public OfflinePostAdapter(String bookmarkPostId, List<BasePost> postList) {
        this.bookmarkPostId = bookmarkPostId;
        this.postList = postList;
    }

    public void setPostClickListener(PostClickListener postClickListener) {
        this.postClickListener = postClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == PostType.CODE) {
            return new CodeOfflinePostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_post_content_code, parent, false));
        } else if (viewType == PostType.IMAGE) {
            return new ImageOfflinePostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_post_content_image, parent, false));
        } else if (viewType == PostType.VIDEO) {
            return new VideoOfflinePostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_post_content_video, parent, false));
        } else if (viewType == PostType.HEADER) {
            return new HeaderOfflinePostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_post_content_header, parent, false));
        } else if (viewType == PostType.PLAIN_TEXT) {
            return new PlainTextOfflinePostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_post_content_plain_text, parent, false));
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return postList.get(position).getType();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        BasePost basePost = postList.get(holder.getAdapterPosition());
        if (viewType == PostType.PLAIN_TEXT) {
            addPlainTextContent(holder, basePost);
        } else if (viewType == PostType.HEADER) {
            addHeaderContent(holder, basePost);
        } else if (viewType == PostType.IMAGE) {
            addImageContent(holder, basePost);
        } else if (viewType == PostType.CODE) {
            addCodeContent(holder, basePost);
        } else if (viewType == PostType.VIDEO) {
            addVideoContent(holder, basePost);
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    // Plain Text
    private void addPlainTextContent(RecyclerView.ViewHolder holder, BasePost basePost) {
        PlainTextPost post = (PlainTextPost) basePost;
        PlainTextOfflinePostViewHolder postViewHolder = (PlainTextOfflinePostViewHolder) holder;
        postViewHolder.tvPostContentPlainText.setText(setSpannable(post));
        if (post.isLinkAvailable()) {
            postViewHolder.tvPostContentPlainText.setMovementMethod(new LinkMovementMethod());
        }
    }

    private Spannable setSpannable(PlainTextPost plainTextPost) {
        Spannable spanText = Spannable.Factory.getInstance().newSpannable(plainTextPost.getText());
        for (PlainTextPost.Highlight highlight : plainTextPost.getHighlightList()) {
            spanText.setSpan(new ForegroundColorSpan(highlight.getColor()), highlight.getStart(), highlight.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        for (PlainTextPost.Link link : plainTextPost.getLinkList()) {
            spanText.setSpan(new OfflineLinkClickable(link.getUrl(), new OfflineLinkClickable.LinkClickListener() {
                @Override
                public void onLinkClick(String url) {
                    if (postClickListener != null) {
                        postClickListener.onLinkClickListener(url);
                    }
                }
            }), link.getStart(), link.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spanText;
    }

    // Header
    private void addHeaderContent(RecyclerView.ViewHolder holder, BasePost basePost) {
        HeaderPost post = (HeaderPost) basePost;
        HeaderOfflinePostViewHolder postViewHolder = (HeaderOfflinePostViewHolder) holder;
        postViewHolder.tvPostContentHeader.setText(post.getText());
        Resources resources = postViewHolder.itemView.getContext().getResources();
        if (post.getSize() == 1) {
            postViewHolder.tvPostContentHeader.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.default_text_size_h1));
        } else if (post.getSize() == 2) {
            postViewHolder.tvPostContentHeader.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.default_text_size_h2));
        } else if (post.getSize() == 3) {
            postViewHolder.tvPostContentHeader.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.default_text_size_h3));
        } else if (post.getSize() == 4) {
            postViewHolder.tvPostContentHeader.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.default_text_size_h4));
        }
    }

    // Code Text
    private void addCodeContent(RecyclerView.ViewHolder holder, BasePost basePost) {
        CodePost post = (CodePost) basePost;
        CodeOfflinePostViewHolder postViewHolder = (CodeOfflinePostViewHolder) holder;
        postViewHolder.tvPostContentCode.setText(post.getCode());
    }

    // Image
    private void addImageContent(RecyclerView.ViewHolder holder, BasePost basePost) {
        final ImagePost post = (ImagePost) basePost;
        ImageOfflinePostViewHolder postViewHolder = (ImageOfflinePostViewHolder) holder;
        postViewHolder.ivPostContentPlainImage.setImageDrawable(null);
        Glide.with(holder.itemView.getContext())
                .load(BookmarkManager.getInstance().getBookmarkImageFile(bookmarkPostId, post.getPostUrl()))
                .override(500, 500)
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(postViewHolder.ivPostContentPlainImage);
        postViewHolder.ivPostContentPlainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postClickListener != null) {
                    postClickListener.onImageClickListener(post.getPostUrl());
                }
            }
        });
        postViewHolder.ivPostContentPlainImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (postClickListener != null) {
                    postClickListener.onImageLongClickListener(post.getFullSizeUrl());
                }
                return true;
            }
        });
    }

    // Video
    private void addVideoContent(RecyclerView.ViewHolder holder, BasePost basePost) {
//        VideoPost post = (VideoPost) basePost;
        VideoOfflinePostViewHolder postViewHolder = (VideoOfflinePostViewHolder) holder;
    }

    public interface PostClickListener {
        void onImageClickListener(String fullUrl);

        void onImageLongClickListener(String fullUrl);

        void onLinkClickListener(String url);
    }
}
