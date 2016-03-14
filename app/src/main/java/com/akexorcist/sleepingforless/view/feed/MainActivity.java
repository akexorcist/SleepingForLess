package com.akexorcist.sleepingforless.view.feed;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
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
import com.bowyer.app.fabtransitionlayout.FooterLayout;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.MenuSheetView;
import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

import rebus.bottomdialog.BottomDialog;

public class MainActivity extends SFLActivity implements View.OnClickListener, FeedAdapter.ItemListener, View.OnTouchListener, BottomDialog.OnItemSelectedListener {
    private Toolbar tbTitle;
    private FeedAdapter adapter;
    private RecyclerView rvFeedList;
    private GridLayoutManager layoutManager;
    private FloatingActionButton fabMenu;
    private FooterLayout flMenu;
    private View viewContentShadow;
    private ImageView ivMenuSearch;
    private ImageView ivMenuOrder;
    private ImageView ivMenuSettings;
    private BottomSheetLayout bslOrderMenu;

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
        ivMenuSearch = (ImageView) findViewById(R.id.iv_menu_search);
        ivMenuOrder = (ImageView) findViewById(R.id.iv_menu_order);
        ivMenuSettings = (ImageView) findViewById(R.id.iv_menu_settings);
        bslOrderMenu = (BottomSheetLayout) findViewById(R.id.bsl_order_menu);

        viewContentShadow.setVisibility(View.GONE);
        viewContentShadow.setOnClickListener(this);
        fabMenu.setOnClickListener(this);
        ivMenuSearch.setOnClickListener(this);
        ivMenuOrder.setOnClickListener(this);
        ivMenuSettings.setOnClickListener(this);
        ivMenuSearch.setOnTouchListener(this);
        ivMenuOrder.setOnTouchListener(this);
        ivMenuSettings.setOnTouchListener(this);
        flMenu.setFab(fabMenu);

        adapter = new FeedAdapter();
        adapter.setItemListener(this);
        layoutManager = new GridLayoutManager(this, 1);
        rvFeedList.setLayoutManager(layoutManager);

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
        } else if (v == ivMenuSearch) {
            onMenuSearchClick();
        } else if (v == ivMenuOrder) {
            onMenuOrderClick();
        } else if (v == ivMenuSettings) {
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

    public void onMenuOrderClick() {
        showOrderBottomDialog();
        closeMenu();
    }

    public void onMenuSettingsClick() {
        Log.e("Check", "onMenuSettingsClick");
    }

    private void showOrderBottomDialog() {
//        BottomDialog bottomDialog = new BottomDialog(MainActivity.this);
//        bottomDialog.title("Order by");
//        bottomDialog.canceledOnTouchOutside(true);
//        bottomDialog.cancelable(true);
//        bottomDialog.inflateMenu(R.menu.menu_post_list);
//        bottomDialog.setOnItemSelectedListener(this);
//        bottomDialog.show();
        MenuSheetView menuSheetView = new MenuSheetView(this, MenuSheetView.MenuType.LIST, "Order by...", new MenuSheetView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (bslOrderMenu.isSheetShowing()) {
                    bslOrderMenu.dismissSheet();
                }
                if (item.getItemId() == R.id.action_order_by_published_date) {
                    requestPostList(BloggerManager.ORDER_PUBLISHED_DATE);
                } else if (item.getItemId() == R.id.action_order_by_updated_date) {
                    requestPostList(BloggerManager.ORDER_UPDATED_DATE);
                }
                return true;
            }
        });
        menuSheetView.inflateMenu(R.menu.menu_post_list);
        bslOrderMenu.showWithSheetView(menuSheetView);
    }

    @Subscribe
    public void onSearchRequest(SearchRequest request) {
        Log.e("Check", "Keyword : " + request.getKeyword());
    }

    @Override
    public boolean onItemSelected(int id) {
        if (id == R.id.action_order_by_published_date) {
            requestPostList(BloggerManager.ORDER_PUBLISHED_DATE);
        } else if (id == R.id.action_order_by_updated_date) {
            requestPostList(BloggerManager.ORDER_UPDATED_DATE);
        }
        return true;
    }
}
