package com.akexorcist.sleepingforless.view.post;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.view.post.constant.PostType;
import com.akexorcist.sleepingforless.view.post.holder.CodePostViewHolder;
import com.akexorcist.sleepingforless.view.post.holder.HeaderPostViewHolder;
import com.akexorcist.sleepingforless.view.post.holder.ImagePostViewHolder;
import com.akexorcist.sleepingforless.view.post.holder.PlainTextPostViewHolder;
import com.akexorcist.sleepingforless.view.post.holder.VideoPostViewHolder;
import com.akexorcist.sleepingforless.view.post.model.BasePost;
import com.akexorcist.sleepingforless.view.post.model.CodePost;
import com.akexorcist.sleepingforless.view.post.model.HeaderPost;
import com.akexorcist.sleepingforless.view.post.model.ImagePost;
import com.akexorcist.sleepingforless.view.post.model.PlainTextPost;
import com.akexorcist.sleepingforless.view.post.model.VideoPost;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private PostClickListener postClickListener;
    private List<BasePost> postList;

    public PostAdapter() {
        this.postList = new ArrayList<>();
    }

    public PostAdapter(List<BasePost> postList) {
        this.postList = postList;
    }

    public void setPostClickListener(PostClickListener postClickListener) {
        this.postClickListener = postClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == PostType.CODE) {
            return new CodePostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_post_content_code, parent, false));
        } else if (viewType == PostType.IMAGE) {
            return new ImagePostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_post_content_image, parent, false));
        } else if (viewType == PostType.VIDEO) {
            return new VideoPostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_post_content_video, parent, false));
        } else if (viewType == PostType.HEADER) {
            return new HeaderPostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_post_content_header, parent, false));
        } else if (viewType == PostType.PLAIN_TEXT) {
            return new PlainTextPostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_post_content_plain_text, parent, false));
        } else if (viewType == PostType.BLANK) {
            return new PlainTextPostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_post_content_blank, parent, false));
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return position < postList.size() ? postList.get(position).getType() : PostType.BLANK;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (position < postList.size()) {
            BasePost basePost = postList.get(position);
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
    }

    @Override
    public int getItemCount() {
        return postList.size() > 0 ? postList.size() + 1 : 0;
    }

    // Plain Text
    private void addPlainTextContent(RecyclerView.ViewHolder holder, BasePost basePost) {
        PlainTextPost post = (PlainTextPost) basePost;
        PlainTextPostViewHolder postViewHolder = (PlainTextPostViewHolder) holder;
        postViewHolder.setText(post);
        postViewHolder.setLinkClickListener(new LinkClickable.LinkClickListener() {
            @Override
            public void onLinkClick(String url) {
                if (postClickListener != null) {
                    postClickListener.onLinkClickListener(url);
                }
            }
        });
    }

    // Header
    private void addHeaderContent(RecyclerView.ViewHolder holder, BasePost basePost) {
        HeaderPost post = (HeaderPost) basePost;
        HeaderPostViewHolder postViewHolder = (HeaderPostViewHolder) holder;
        postViewHolder.setHeaderText(post.getText());
        postViewHolder.setHeaderSize(post.getSize());
    }

    // Code Text
    private void addCodeContent(RecyclerView.ViewHolder holder, BasePost basePost) {
        CodePost post = (CodePost) basePost;
        CodePostViewHolder postViewHolder = (CodePostViewHolder) holder;
        postViewHolder.setCode(post.getCode());
    }

    // Image
    private void addImageContent(RecyclerView.ViewHolder holder, BasePost basePost) {
        final ImagePost post = (ImagePost) basePost;
        ImagePostViewHolder postViewHolder = (ImagePostViewHolder) holder;
        postViewHolder.load(post.getPostUrl());
        postViewHolder.setImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postClickListener != null) {
                    postClickListener.onImageClickListener(post.getFullSizeUrl());
                }
            }
        });
        postViewHolder.setImageLongClickListener(new View.OnLongClickListener() {
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
        final VideoPost post = (VideoPost) basePost;
        final VideoPostViewHolder videoPostViewHolder = (VideoPostViewHolder) holder;
        videoPostViewHolder.setButtonPlayClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postClickListener != null) {
                    postClickListener.onVideoClickListener(post.getUrl());
                }
            }
        });
        videoPostViewHolder.setVideoUrl(post.getUrl());
    }

    public interface PostClickListener {
        void onImageClickListener(String fullUrl);

        void onImageLongClickListener(String fullUrl);

        void onLinkClickListener(String url);

        void onVideoClickListener(String url);
    }
}
