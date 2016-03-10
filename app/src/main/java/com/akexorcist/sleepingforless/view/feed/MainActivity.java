package com.akexorcist.sleepingforless.view.feed;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.common.SFLActivity;
import com.akexorcist.sleepingforless.network.BloggerManager;
import com.akexorcist.sleepingforless.network.model.Blog;
import com.akexorcist.sleepingforless.network.model.Failure;
import com.squareup.otto.Subscribe;

public class MainActivity extends SFLActivity implements View.OnClickListener {
    private Toolbar tbTitle;
    private Button btnOk;
    private RecyclerView rvFeedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tbTitle = (Toolbar) findViewById(R.id.tb_title);
        rvFeedList = (RecyclerView) findViewById(R.id.rv_feed_list);
        btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);

        setToolbar();

        BloggerManager.getInstance().getBlog();
    }

    private void setToolbar() {
        setSupportActionBar(tbTitle);
        setTitle(getString(R.string.app_name));
    }

    @Subscribe
    public void onBlogSuccess(Blog blog) {
        Log.e("Check", "onBlogSuccess : " + blog.getName());
    }

    @Subscribe
    public void onBlogFailure(Failure failure) {
        Log.e("Check", "onBlogFailure");
    }

    @Override
    public void onClick(View v) {
        if (v == btnOk) {
            Snackbar.make(btnOk, "Hello", Snackbar.LENGTH_SHORT).show();
        }
    }
}
