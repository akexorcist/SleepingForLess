package com.akexorcist.sleepingforless.view.post.holder;

import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.view.post.LinkClickable;
import com.akexorcist.sleepingforless.view.post.model.PlainTextPost;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class PlainTextPostViewHolder extends RecyclerView.ViewHolder {
    public TextView tvPostContentPlainText;
    public LinkClickable.LinkClickListener linkClickListener;

    public PlainTextPostViewHolder(View itemView) {
        super(itemView);
        tvPostContentPlainText = (TextView) itemView.findViewById(R.id.tv_post_content_plain_text);
    }

    public void setText(PlainTextPost plainTextPost) {
        tvPostContentPlainText.setText(setSpannable(plainTextPost));
        if (plainTextPost.isLinkAvailable()) {
            tvPostContentPlainText.setMovementMethod(new LinkMovementMethod());
        }
    }

    public void setLinkClickListener(LinkClickable.LinkClickListener linkClickListener) {
        this.linkClickListener = linkClickListener;
    }

    private Spannable setSpannable(PlainTextPost plainTextPost) {
        Spannable spanText = Spannable.Factory.getInstance().newSpannable(plainTextPost.getText());
        for (PlainTextPost.Highlight highlight : plainTextPost.getHighlightList()) {
            spanText.setSpan(new ForegroundColorSpan(highlight.getColor()), highlight.getStart(), highlight.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        for (PlainTextPost.Link link : plainTextPost.getLinkList()) {
            spanText.setSpan(new LinkClickable(link.getUrl(), linkClickListener), link.getStart(), link.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spanText;
    }
}
