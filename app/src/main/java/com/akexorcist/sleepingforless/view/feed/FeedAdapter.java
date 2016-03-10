package com.akexorcist.sleepingforless.view.feed;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.network.model.PostList;
import com.akexorcist.sleepingforless.util.AnimationUtility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class FeedAdapter extends RecyclerView.Adapter<FeedViewHolder> {
    private List<PostList.Item> itemList;

    public FeedAdapter(List<PostList.Item> itemList) {
        this.itemList = itemList;
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FeedViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_feed_item, parent, false));
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        String title = removeLabelFromTitle(itemList.get(position).getTitle());
        holder.tvTitle.setText(title);
        PostList.Item postItem = itemList.get(position);
        if (postItem.isImageAvailable()) {
            String url = postItem.getImages().get(0).getUrl();
            loadItemResource(holder, url);
        } else {
            // TODO Do something
        }
        holder.itemView.setOnClickListener(onClickListener);
        holder.tvTitle.setOnTouchListener(onTouchListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                AnimationUtility.getInstance().fadeIn(v);
            } else if(event.getAction() == MotionEvent.ACTION_UP) {
                AnimationUtility.getInstance().fadeOut(v);
            } else if(event.getAction() == MotionEvent.ACTION_CANCEL) {
                AnimationUtility.getInstance().fadeOut(v);
            }
            return true;
        }
    };

    private String removeLabelFromTitle(String title) {
        return title.replace("[Android Code]", "")
                .replace("[Android Design]", "")
                .replace("[Android Dev Tips]", "")
                .trim();
    }

    private void loadItemResource(final FeedViewHolder holder, String url) {
        Glide.with(holder.getIvTitle().getContext())
                .load(url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        holder.getIvTitle().setImageBitmap(resource);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
