package com.akexorcist.sleepingforless.view.post.holder;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.util.AnimationUtility;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class VideoPostViewHolder extends RecyclerView.ViewHolder {
    public ImageView ivYouTubePlay;
    public LinearLayout layoutVideoThumbnail;
    public TextView tvVideoThumbnail;

    public VideoPostViewHolder(View itemView) {
        super(itemView);
        bindView(itemView);
        setupView();
    }

    private void bindView(View view) {
        ivYouTubePlay = view.findViewById(R.id.iv_youtube_play);
        layoutVideoThumbnail = view.findViewById(R.id.layout_video_thumbnail);
        tvVideoThumbnail = view.findViewById(R.id.tv_video_thumbnail);
    }

    private void setupView() {
        layoutVideoThumbnail.setOnTouchListener(this::onVideoThumbnailTouch);
    }

    private boolean onVideoThumbnailTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            scaleMenuButtonUp(ivYouTubePlay);
        } else if (event.getAction() == MotionEvent.ACTION_UP ||
                event.getAction() == MotionEvent.ACTION_CANCEL) {
            scaleMenuButtonBack(ivYouTubePlay);
        }
        return false;
    }

    public void setVideoUrl(String url) {
        tvVideoThumbnail.setText(url);
    }

    private void scaleMenuButtonUp(View v) {
        AnimationUtility.getInstance().scaleUp(v, 200);
    }

    private void scaleMenuButtonBack(View v) {
        AnimationUtility.getInstance().scaleBack(v, 200);
    }

    public void setButtonPlayClickListener(View.OnClickListener listener) {
        layoutVideoThumbnail.setOnClickListener(listener);
    }
}
