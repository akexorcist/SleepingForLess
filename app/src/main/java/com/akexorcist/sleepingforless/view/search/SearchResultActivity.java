package com.akexorcist.sleepingforless.view.search;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.common.SFLActivity;
import com.akexorcist.sleepingforless.constant.Key;
import com.akexorcist.sleepingforless.network.BloggerManager;
import com.akexorcist.sleepingforless.network.model.PostList;
import com.akexorcist.sleepingforless.util.AnimationUtility;
import com.akexorcist.sleepingforless.util.ContentUtility;
import com.akexorcist.sleepingforless.view.feed.FeedAdapter;
import com.akexorcist.sleepingforless.view.feed.FeedViewHolder;
import com.akexorcist.sleepingforless.view.post.DebugPostActivity;
import com.akexorcist.sleepingforless.view.post.PostActivity;
import com.bowyer.app.fabtransitionlayout.FooterLayout;
import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

public class SearchResultActivity extends SFLActivity implements View.OnClickListener, View.OnTouchListener, FeedAdapter.ItemListener {
    private Toolbar tbTitle;
    private FloatingActionButton fabMenu;
    private FooterLayout flMenu;
    private RecyclerView rvSearchResultList;
    private View viewContentShadow;
    private ImageView ivMenuSearch;
    private ImageView ivMenuSettings;

    private FeedAdapter adapter;

    private SearchRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        rvSearchResultList = (RecyclerView) findViewById(R.id.rv_search_result_list);
        viewContentShadow = findViewById(R.id.view_content_shadow);
        tbTitle = (Toolbar) findViewById(R.id.tb_title);
        fabMenu = (FloatingActionButton) findViewById(R.id.fab_menu);
        flMenu = (FooterLayout) findViewById(R.id.fl_menu);
        ivMenuSearch = (ImageView) findViewById(R.id.iv_menu_search);
        ivMenuSettings = (ImageView) findViewById(R.id.iv_menu_settings);

        if (savedInstanceState == null) {
            setupFirstRun();
        }

        viewContentShadow.setVisibility(View.GONE);
        viewContentShadow.setOnClickListener(this);
        fabMenu.setOnClickListener(this);
        ivMenuSearch.setOnClickListener(this);
        ivMenuSettings.setOnClickListener(this);
        ivMenuSearch.setOnTouchListener(this);
        ivMenuSettings.setOnTouchListener(this);
        flMenu.setFab(fabMenu);
        adapter = new FeedAdapter();
        adapter.setItemListener(this);
        rvSearchResultList.setLayoutManager(new LinearLayoutManager(this));

        setToolbar("Search Result : " + request.getKeyword());
    }

    private void setToolbar(String title) {
        setSupportActionBar(tbTitle);
        setTitle(ContentUtility.getInstance().removeLabelFromTitle(title));
        tbTitle.setNavigationIcon(R.drawable.vector_ic_back);
        tbTitle.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupFirstRun() {
        request = Parcels.unwrap(getIntent().getParcelableExtra(Key.SEARCH_REQUEST));
        searchPost();
    }
    private void searchPost() {
        BloggerManager.getInstance().searchPost(request.getKeyword());
    }

    @Subscribe
    public void onPostListSuccess(PostList postList) {
        Log.e("Check", "onPostListSuccess");
        setPostList(postList);
    }

    public void setPostList(PostList postList) {
        if (postList != null) {
            adapter.setPostListItem(postList.getItems());
            adapter.notifyDataSetChanged();
            rvSearchResultList.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == fabMenu) {
            openMenu();
        } else if (v == viewContentShadow) {
            closeMenu();
        } else if (v == ivMenuSearch) {
            onMenuSearchClick();
        } else if (v == ivMenuSettings) {
            onMenuSettingsClick();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            scaleMenuButtonUp(v);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            scaleMenuButtonBack(v);
        }
        return false;
    }

    public void scaleMenuButtonUp(View v) {
        AnimationUtility.getInstance().scaleUp(v, 200);
    }

    public void scaleMenuButtonBack(View v) {
        AnimationUtility.getInstance().scaleBack(v, 200);
    }

    public void openMenu() {
        flMenu.expandFab();
        AnimationUtility.getInstance().fadeIn(viewContentShadow, 200);
    }

    public void closeMenu() {
        flMenu.contractFab();
        AnimationUtility.getInstance().fadeOut(viewContentShadow, 200);
    }

    public void onMenuSearchClick() {
        openActivity(SearchActivity.class);
        closeMenu();
    }

    public void onMenuSettingsClick() {
        Log.e("Check", "onMenuSettingsClick");
    }

    @Subscribe
    public void onSearchRequest(SearchRequest request) {
        this.request = request;
        searchPost();
    }

    @Override
    public void onItemClick(FeedViewHolder holder, PostList.Item item) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Key.POST_ID, Parcels.wrap(item));
        openActivity(PostActivity.class, bundle);
    }

    @Override
    public void onItemLongClick(FeedViewHolder holder, PostList.Item item) {
//        showBottomSheet();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Key.POST_ID, Parcels.wrap(item));
        openActivity(DebugPostActivity.class, bundle);
    }
}
