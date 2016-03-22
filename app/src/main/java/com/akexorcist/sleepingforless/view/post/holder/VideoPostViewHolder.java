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
public class VideoPostViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {
    public ImageView ivYouTubePlay;
    public LinearLayout layoutVideoThumbnail;
    public TextView tvVideoThumbnail;

    public VideoPostViewHolder(View itemView) {
        super(itemView);
        ivYouTubePlay = (ImageView) itemView.findViewById(R.id.iv_youtube_play);
        tvVideoThumbnail = (TextView) itemView.findViewById(R.id.tv_video_thumbnail);
        layoutVideoThumbnail = (LinearLayout) itemView.findViewById(R.id.layout_video_thumbnail);
        layoutVideoThumbnail.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
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
