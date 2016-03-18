package com.akexorcist.sleepingforless.view.feed;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.common.SFLActivity;
import com.akexorcist.sleepingforless.constant.Key;
import com.akexorcist.sleepingforless.network.BloggerManager;
import com.akexorcist.sleepingforless.network.model.Failure;
import com.akexorcist.sleepingforless.network.model.PostList;
import com.akexorcist.sleepingforless.util.AnimationUtility;
import com.akexorcist.sleepingforless.database.BookmarkManager;
import com.akexorcist.sleepingforless.view.bookmark.BookmarkActivity;
import com.akexorcist.sleepingforless.view.post.DebugPostActivity;
import com.akexorcist.sleepingforless.view.post.PostByIdActivity;
import com.akexorcist.sleepingforless.view.search.SearchActivity;
import com.akexorcist.sleepingforless.view.search.SearchRequest;
import com.akexorcist.sleepingforless.view.search.SearchResultActivity;
import com.akexorcist.sleepingforless.widget.MenuButton;
import com.bowyer.app.fabtransitionlayout.FooterLayout;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.MenuSheetView;
import com.squareup.otto.Subscribe;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import org.parceler.Parcels;

public class MainActivity extends SFLActivity implements View.OnClickListener, FeedAdapter.ItemListener, View.OnTouchListener, FeedAdapter.LoadMoreListener {
    private Toolbar tbTitle;
    private FeedAdapter adapter;
    private DilatingDotsProgressBar pbFeedListLoading;
    private RecyclerView rvFeedList;
    private FloatingActionButton fabMenu;
    private FooterLayout flMenu;
    private View viewContentShadow;
    private TextView tvUnavailableDescription;
    private TextView tvOpenBookmark;
    private MenuButton btnMenuBookmark;
    private MenuButton btnMenuRefresh;
    private MenuButton btnMenuSearch;
    private MenuButton btnMenuSort;
    private MenuButton btnMenuSettings;
    private BottomSheetLayout bslMenu;

    private PostList postList;
    private String sortType = BloggerManager.SORT_PUBLISHED_DATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindView();
        setupView();
        setToolbar();
        callService();
    }

    private void bindView() {
        tbTitle = (Toolbar) findViewById(R.id.tb_title);
        rvFeedList = (RecyclerView) findViewById(R.id.rv_feed_list);
        pbFeedListLoading = (DilatingDotsProgressBar) findViewById(R.id.pb_feed_list_loading);
        fabMenu = (FloatingActionButton) findViewById(R.id.fab_menu);
        viewContentShadow = findViewById(R.id.view_content_shadow);
        flMenu = (FooterLayout) findViewById(R.id.fl_menu);
        bslMenu = (BottomSheetLayout) findViewById(R.id.bsl_menu);
        tvUnavailableDescription = (TextView) findViewById(R.id.tv_network_unavailable_description);
        tvOpenBookmark = (TextView) findViewById(R.id.tv_network_unavailable_open_bookmark);
        btnMenuBookmark = (MenuButton) findViewById(R.id.btn_menu_bookmark);
        btnMenuRefresh = (MenuButton) findViewById(R.id.btn_menu_refresh);
        btnMenuSearch = (MenuButton) findViewById(R.id.btn_menu_search);
        btnMenuSort = (MenuButton) findViewById(R.id.btn_menu_sort);
        btnMenuSettings = (MenuButton) findViewById(R.id.btn_menu_settings);
    }

    private void setupView() {
        viewContentShadow.setVisibility(View.GONE);
        viewContentShadow.setOnClickListener(this);
        fabMenu.setOnClickListener(this);
        tvOpenBookmark.setOnClickListener(this);
        btnMenuBookmark.setOnClickListener(this);
        btnMenuRefresh.setOnClickListener(this);
        btnMenuSearch.setOnClickListener(this);
        btnMenuSort.setOnClickListener(this);
        btnMenuSettings.setOnClickListener(this);
        btnMenuBookmark.setOnTouchListener(this);
        btnMenuRefresh.setOnTouchListener(this);
        btnMenuSearch.setOnTouchListener(this);
        btnMenuSort.setOnTouchListener(this);
        btnMenuSettings.setOnTouchListener(this);
        flMenu.setFab(fabMenu);

        adapter = new FeedAdapter();
        adapter.setSortType(sortType);
        adapter.setItemListener(this);
        adapter.setLoadMoreListener(this);
        rvFeedList.setLayoutManager(new LinearLayoutManager(this));
        rvFeedList.setAdapter(adapter);
    }

    private void callService() {
        showLoading();
        hideUnavailableMessageImmediately();
        requestPostList(sortType);
    }

    private void requestPostList(String sortBy) {
        BloggerManager.getInstance().getPostList(sortBy);
    }

    private void requestMorePostList(String sortBy, String nextPageToken) {
        BloggerManager.getInstance().getNextPostList(sortBy, nextPageToken);
    }

    private void setToolbar() {
        setSupportActionBar(tbTitle);
        setTitle(getString(R.string.app_name));
    }

    @Override
    protected void onResume() {
        super.onResume();
        closeMenu();
        fabMenu.show();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Subscribe
    public void onPostListSuccess(PostList postList) {
        Log.e("Check", "onBlogSuccess");
        this.postList = postList;
        setPostList(postList);
        hideLoading();
        hideUnavailableMessage();
    }

    @Subscribe
    public void onBlogFailure(Failure failure) {
        Log.e("Check", "onBlogFailure");
        rvFeedList.setVisibility(View.GONE);
        pbFeedListLoading.hideNow();
        showUnavailableMessage();
    }

    @Subscribe
    public void onSearchRequest(SearchRequest request) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Key.SEARCH_REQUEST, Parcels.wrap(request));
        openActivity(SearchResultActivity.class, bundle);
    }

    @Override
    public void onClick(View v) {
        if (v == fabMenu) {
            openMenu();
        } else if (v == viewContentShadow) {
            closeMenu();
        } else if (v == btnMenuBookmark) {
            onMenuBookmarkClick();
        } else if (v == btnMenuRefresh) {
            onMenuRefreshClick();
        } else if (v == btnMenuSearch) {
            onMenuSearchClick();
        } else if (v == btnMenuSort) {
            onMenuSortClick();
        } else if (v == btnMenuSettings) {
            onMenuSettingsClick();
        } else if (v == tvOpenBookmark) {
            onMenuOpenBookmarkClick();
        }
    }

    @Override
    public void onLoadMore() {
        requestMorePostList(sortType, postList.getNextPageToken());
    }

    @Override
    public void onItemClick(FeedViewHolder holder, PostList.Item item) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Key.POST_ITEM, Parcels.wrap(item));
        openActivity(PostByIdActivity.class, bundle);
    }

    @Override
    public void onItemLongClick(FeedViewHolder holder, PostList.Item item) {
//        showBottomSheet();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Key.POST_ITEM, Parcels.wrap(item));
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

    @Override
    public void onBackPressed() {
        if (bslMenu.isSheetShowing()) {
            bslMenu.dismissSheet();
        } else if (flMenu.isFabExpanded()) {
            closeMenu();
        } else {
            super.onBackPressed();
        }
    }

    public void setPostList(PostList postList) {
        if (postList != null) {
            adapter.addPostListItem(postList.getItems());
            adapter.setLoadMoreAvailable(postList.getNextPageToken() != null);
        }
    }

    public void showBottomSheet() {
        FeedDetailBottomSheet modalBottomSheet = FeedDetailBottomSheet.newInstance();
        modalBottomSheet.show(getSupportFragmentManager(), "bottom_sheet");
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

    public void onMenuBookmarkClick() {
        openActivity(BookmarkActivity.class);
        closeMenu();
    }

    public void onMenuRefreshClick() {
        callService();
        closeMenu();
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

    public void onMenuOpenBookmarkClick() {
        openActivity(BookmarkActivity.class);
    }

    private void showSortBottomDialog() {
        MenuSheetView menuSheetView = new MenuSheetView(this, MenuSheetView.MenuType.LIST, R.string.title_sort_by, new MenuSheetView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (bslMenu.isSheetShowing()) {
                    bslMenu.dismissSheet();
                }
                if (item.getItemId() == R.id.action_sort_by_published_date &&
                        !sortType.equalsIgnoreCase(BloggerManager.SORT_PUBLISHED_DATE)) {
                    changeFeedSortType(BloggerManager.SORT_PUBLISHED_DATE);
                } else if (item.getItemId() == R.id.action_sort_by_updated_date &&
                        !sortType.equalsIgnoreCase(BloggerManager.SORT_UPDATED_DATE)) {
                    changeFeedSortType(BloggerManager.SORT_UPDATED_DATE);
                }
                return true;
            }
        });
        menuSheetView.inflateMenu(R.menu.menu_post_list);
        bslMenu.showWithSheetView(menuSheetView);
    }

    private void changeFeedSortType(String sortType) {
        adapter.clear();
        adapter.setSortType(sortType);
        this.sortType = sortType;
        callService();
    }

    private void showLoading() {
        rvFeedList.setVisibility(View.GONE);
        pbFeedListLoading.showNow();
    }

    private void hideLoading() {
        rvFeedList.setVisibility(View.VISIBLE);
        pbFeedListLoading.hideNow();
    }

    private void showUnavailableMessage() {
        if (BookmarkManager.getInstance().getBookmarkCount() > 0) {
            tvUnavailableDescription.setText(R.string.network_unavailable_with_bookmark);
            AnimationUtility.getInstance().fadeIn(tvUnavailableDescription);
            AnimationUtility.getInstance().fadeIn(tvOpenBookmark);
        } else {
            tvOpenBookmark.setVisibility(View.GONE);
            String message = getString(R.string.network_unavailable_no_bookmark);
            String highlightText = "Bookmark";
            tvUnavailableDescription.setText(highlightText(message, highlightText));
            AnimationUtility.getInstance().fadeIn(tvUnavailableDescription);
        }
    }

    private Spannable highlightText(String message, String highlight) {
        int start = message.indexOf(highlight);
        int end = start + highlight.length();
        Spannable spannableMessage = new SpannableString(message);
        spannableMessage.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableMessage;
    }

    private void hideUnavailableMessage() {
        AnimationUtility.getInstance().fadeOut(tvUnavailableDescription);
        AnimationUtility.getInstance().fadeOut(tvOpenBookmark);
    }

    private void hideUnavailableMessageImmediately() {
        tvUnavailableDescription.setVisibility(View.GONE);
        tvOpenBookmark.setVisibility(View.GONE);
    }
}
