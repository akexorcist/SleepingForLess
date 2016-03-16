package com.akexorcist.sleepingforless.view.offline;

import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by Akexorcist on 3/13/2016 AD.
 */
public class OfflineLinkClickable extends ClickableSpan {
    private String url;
    private LinkClickListener listener;

    public OfflineLinkClickable(String url, LinkClickListener listener) {
        this.url = url;
        this.listener = listener;
    }

    @Override
    public void onClick(View widget) {
        if (listener != null) {
            listener.onLinkClick(url);
        }
    }

    public interface LinkClickListener {
        void onLinkClick(String url);
    }
}
