package com.akexorcist.sleepingforless.view.post;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.common.SFLDraggerActivity;
import com.akexorcist.sleepingforless.constant.Key;
import com.akexorcist.sleepingforless.network.BloggerManager;
import com.akexorcist.sleepingforless.network.model.Failure;
import com.akexorcist.sleepingforless.network.model.Post;
import com.akexorcist.sleepingforless.network.model.PostList;
import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

public class PostReaderActivity extends SFLDraggerActivity {

    private WebView wvPostDetail;
    private PostList.Item postItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setShadowView(R.layout.activity_post_reader_shadow);
        setContentView(R.layout.activity_post_reader);
        setupDragger();

        if (savedInstanceState == null) {
            setupFirstRun();
        }

        wvPostDetail = (WebView) findViewById(R.id.wv_post_detail);
        wvPostDetail.getSettings().setJavaScriptEnabled(true);
        wvPostDetail.getSettings().setLoadWithOverviewMode(true);
        wvPostDetail.getSettings().setUseWideViewPort(true);
        wvPostDetail.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        wvPostDetail.setScrollbarFadingEnabled(true);
        wvPostDetail.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
        });
    }

    private void setupDragger() {
        setSlideEnabled(false);
        setFriction(100);
        setTension(800);
    }

    private void setupFirstRun() {
        postItem = Parcels.unwrap(getIntent().getParcelableExtra(Key.POST_ID));
        BloggerManager.getInstance().getPost(postItem.getId());
    }

    @Subscribe
    public void onPostSucess(Post post) {
        Log.e("Check", "onPostSucess");
        wvPostDetail.loadData(post.getContent(), "text/html; charset=UTF-8", "UTF-8");
    }

    @Subscribe
    public void onPostFailure(Failure failure) {
        Log.e("Check", "onPostFailure");
    }
}
