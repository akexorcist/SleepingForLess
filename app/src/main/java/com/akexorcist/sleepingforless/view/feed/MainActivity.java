package com.akexorcist.sleepingforless.view.feed;

import android.os.Bundle;
import android.util.Log;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.common.SFLActivity;
import com.akexorcist.sleepingforless.network.BloggerManager;
import com.akexorcist.sleepingforless.network.model.Blog;
import com.akexorcist.sleepingforless.network.model.Failure;
import com.squareup.otto.Subscribe;

public class MainActivity extends SFLActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BloggerManager.getInstance().getBlog();
    }

    @Subscribe
    public void onBlogSuccess(Blog blog) {
        Log.e("Check", "onBlogSuccess : " + blog.getName());
    }

    @Subscribe
    public void onBlogFailure(Failure failure) {
        Log.e("Check", "onBlogFailure");
    }
}
