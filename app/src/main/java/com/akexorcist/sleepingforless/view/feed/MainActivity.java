package com.akexorcist.sleepingforless.view.feed;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.common.SFLActivity;
import com.akexorcist.sleepingforless.constant.Key;
import com.akexorcist.sleepingforless.network.BloggerManager;
import com.akexorcist.sleepingforless.network.model.Blog;
import com.akexorcist.sleepingforless.network.model.Failure;
import com.akexorcist.sleepingforless.network.model.PostList;
import com.akexorcist.sleepingforless.util.AnimationUtility;
import com.akexorcist.sleepingforless.view.post.PostReaderActivity;
import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

public class MainActivity extends SFLActivity implements View.OnClickListener, FeedAdapter.ItemListener {
    private Toolbar tbTitle;
    private Button btnOk;
    private FeedAdapter adapter;
    private RecyclerView rvFeedList;
    private GridLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tbTitle = (Toolbar) findViewById(R.id.tb_title);
        layoutManager = new GridLayoutManager(this, 1);
        adapter = new FeedAdapter();
        adapter.setItemListener(this);
        rvFeedList = (RecyclerView) findViewById(R.id.rv_feed_list);
        rvFeedList.setLayoutManager(layoutManager);

        setToolbar();

        BloggerManager.getInstance().getPostList();
    }

    private void setToolbar() {
        setSupportActionBar(tbTitle);
        setTitle(getString(R.string.app_name));
    }

    @Subscribe
    public void onBlogSuccess(Blog blog) {
        Log.e("Check", "onBlogSuccess");
        Log.e("Check", "Name : " + blog.getName());
    }

    @Subscribe
    public void onPostListSuccess(PostList postList) {
        Log.e("Check", "onBlogSuccess");

        setPostList(postList);
        PostList.Item item = postList.getItems().get(0);
        Log.e("Check", "Title : " + item.getTitle());
        Log.e("Check", "Image : " + item.getImages().get(0).getUrl());
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

    public void setPostList(PostList postList) {
        if (postList != null) {
            Log.e("Check", "Size : " + postList.getItems().size());
            adapter.setPostListItem(postList.getItems());
            adapter.notifyDataSetChanged();
            rvFeedList.setAdapter(adapter);
        }
    }

    public void showBottomSheet() {
        FeedDetailBottomSheet modalBottomSheet = FeedDetailBottomSheet.newInstance();
        modalBottomSheet.show(getSupportFragmentManager(), "bottom sheet");
    }

    @Override
    public void onItemClick(FeedViewHolder holder, PostList.Item item) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Key.POST_ID, Parcels.wrap(item));
        openActivity(PostReaderActivity.class, bundle);
    }

    @Override
    public void onItemLongClick(FeedViewHolder holder, PostList.Item item) {
        showBottomSheet();
    }
}
