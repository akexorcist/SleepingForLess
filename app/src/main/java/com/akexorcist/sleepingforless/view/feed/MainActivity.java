package com.akexorcist.sleepingforless.view.feed;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.common.SFLActivity;
import com.akexorcist.sleepingforless.constant.Key;
import com.akexorcist.sleepingforless.network.BloggerManager;
import com.akexorcist.sleepingforless.network.model.Blog;
import com.akexorcist.sleepingforless.network.model.Failure;
import com.akexorcist.sleepingforless.network.model.PostList;
import com.akexorcist.sleepingforless.util.AnimationUtility;
import com.akexorcist.sleepingforless.view.post.DebugPostActivity;
import com.akexorcist.sleepingforless.view.post.PostActivity;
import com.akexorcist.sleepingforless.view.search.SearchActivity;
import com.akexorcist.sleepingforless.view.search.SearchRequest;
import com.akexorcist.sleepingforless.view.search.SearchResultActivity;
import com.akexorcist.sleepingforless.widget.MenuButton;
import com.bowyer.app.fabtransitionlayout.FooterLayout;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.MenuSheetView;
import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

public class MainActivity extends SFLActivity implements View.OnClickListener, FeedAdapter.ItemListener, View.OnTouchListener {
    private Toolbar tbTitle;
    private FeedAdapter adapter;
    private RecyclerView rvFeedList;
    private FloatingActionButton fabMenu;
    private FooterLayout flMenu;
    private View viewContentShadow;
    private MenuButton btnMenuSearch;
    private MenuButton btnMenuSort;
    private MenuButton btnMenuSettings;
    private BottomSheetLayout bslMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Check", "onCreate");
        setContentView(R.layout.activity_main);

        tbTitle = (Toolbar) findViewById(R.id.tb_title);
        rvFeedList = (RecyclerView) findViewById(R.id.rv_feed_list);
        fabMenu = (FloatingActionButton) findViewById(R.id.fab_menu);
        viewContentShadow = findViewById(R.id.view_content_shadow);
        flMenu = (FooterLayout) findViewById(R.id.fl_menu);
        bslMenu = (BottomSheetLayout) findViewById(R.id.bsl_menu);
        btnMenuSearch = (MenuButton) findViewById(R.id.btn_menu_search);
        btnMenuSort = (MenuButton) findViewById(R.id.btn_menu_sort);
        btnMenuSettings = (MenuButton) findViewById(R.id.btn_menu_settings);

        viewContentShadow.setVisibility(View.GONE);
        viewContentShadow.setOnClickListener(this);
        fabMenu.setOnClickListener(this);
        btnMenuSearch.setOnClickListener(this);
        btnMenuSort.setOnClickListener(this);
        btnMenuSettings.setOnClickListener(this);
        btnMenuSearch.setOnTouchListener(this);
        btnMenuSort.setOnTouchListener(this);
        btnMenuSettings.setOnTouchListener(this);
        flMenu.setFab(fabMenu);

        adapter = new FeedAdapter();
        adapter.setItemListener(this);
        rvFeedList.setLayoutManager(new LinearLayoutManager(this));

        setToolbar();
        requestPostList(BloggerManager.ORDER_PUBLISHED_DATE);
    }

    private void requestPostList(String orderBy) {
        adapter.clear();
        BloggerManager.getInstance().getPostList(orderBy);
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
        if (v == fabMenu) {
            openMenu();
        } else if (v == viewContentShadow) {
            closeMenu();
        } else if (v == btnMenuSearch) {
            onMenuSearchClick();
        } else if (v == btnMenuSort) {
            onMenuSortClick();
        } else if (v == btnMenuSettings) {
            onMenuSettingsClick();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        closeMenu();
    }

    public void setPostList(PostList postList) {
        if (postList != null) {
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
        openActivity(PostActivity.class, bundle);
    }

    @Override
    public void onItemLongClick(FeedViewHolder holder, PostList.Item item) {
//        showBottomSheet();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Key.POST_ID, Parcels.wrap(item));
        openActivity(DebugPostActivity.class, bundle);
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

    public void openMenu() {
        flMenu.expandFab();
        AnimationUtility.getInstance().fadeIn(viewContentShadow, 200);
    }

    public void closeMenu() {
        flMenu.contractFab();
        AnimationUtility.getInstance().fadeOut(viewContentShadow, 200);
    }

    public void scaleMenuButtonUp(View v) {
        AnimationUtility.getInstance().scaleUp(v, 200);
    }

    public void scaleMenuButtonBack(View v) {
        AnimationUtility.getInstance().scaleBack(v, 200);
    }

    public void onMenuSearchClick() {
        openActivity(SearchActivity.class);
        closeMenu();
    }

    public void onMenuSortClick() {
        showSortBottomDialog();
        closeMenu();
    }

    public void onMenuSettingsClick() {
        Log.e("Check", "onMenuSettingsClick");
    }

    private void showSortBottomDialog() {
        MenuSheetView menuSheetView = new MenuSheetView(this, MenuSheetView.MenuType.LIST, "Sort by...", new MenuSheetView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (bslMenu.isSheetShowing()) {
                    bslMenu.dismissSheet();
                }
                if (item.getItemId() == R.id.action_sort_by_published_date) {
                    requestPostList(BloggerManager.ORDER_PUBLISHED_DATE);
                } else if (item.getItemId() == R.id.action_sort_by_updated_date) {
                    requestPostList(BloggerManager.ORDER_UPDATED_DATE);
                }
                return true;
            }
        });
        menuSheetView.inflateMenu(R.menu.menu_post_list);
        bslMenu.showWithSheetView(menuSheetView);
    }

    @Subscribe
    public void onSearchRequest(SearchRequest request) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Key.SEARCH_REQUEST, Parcels.wrap(request));
        openActivity(SearchResultActivity.class, bundle);
    }
}
