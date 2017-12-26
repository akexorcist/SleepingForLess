package com.akexorcist.sleepingforless.view.feed;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.analytic.EventKey;
import com.akexorcist.sleepingforless.analytic.EventTracking;
import com.akexorcist.sleepingforless.common.SFLActivity;
import com.akexorcist.sleepingforless.config.DeveloperConfig;
import com.akexorcist.sleepingforless.config.FirstLaunchPreference;
import com.akexorcist.sleepingforless.constant.Key;
import com.akexorcist.sleepingforless.database.BookmarkManager;
import com.akexorcist.sleepingforless.database.BookmarkResult;
import com.akexorcist.sleepingforless.gcm.GcmRegisterService;
import com.akexorcist.sleepingforless.network.blogger.BloggerManager;
import com.akexorcist.sleepingforless.network.blogger.model.PostList;
import com.akexorcist.sleepingforless.network.blogger.model.PostListFailure;
import com.akexorcist.sleepingforless.util.AnimationUtility;
import com.akexorcist.sleepingforless.util.Utility;
import com.akexorcist.sleepingforless.util.content.EasterEggUtility;
import com.akexorcist.sleepingforless.view.bookmark.BookmarkActivity;
import com.akexorcist.sleepingforless.view.post.DebugPostActivity;
import com.akexorcist.sleepingforless.view.post.PostByIdActivity;
import com.akexorcist.sleepingforless.view.search.SearchActivity;
import com.akexorcist.sleepingforless.view.search.SearchRequest;
import com.akexorcist.sleepingforless.view.search.SearchResultActivity;
import com.akexorcist.sleepingforless.view.settings.SettingsActivity;
import com.akexorcist.sleepingforless.widget.FabRecyclerViewScrollHelper;
import com.akexorcist.sleepingforless.widget.FloatingActionButtonBehavior;
import com.akexorcist.sleepingforless.widget.MenuButton;
import com.bowyer.app.fabtransitionlayout.FooterLayout;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.MenuSheetView;
import com.squareup.otto.Subscribe;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import org.parceler.Parcels;

import java.util.List;

import it.sephiroth.android.library.tooltip.Tooltip;

public class FeedActivity extends SFLActivity implements FeedAdapter.ItemListener, FeedAdapter.LoadMoreListener, SwipeRefreshLayout.OnRefreshListener, AppBarLayout.OnOffsetChangedListener, FloatingActionButtonBehavior.FabVisibilityChangeListener {
    private static final String KEY_POST_LIST = "key_post_list";
    private static final String KEY_SORT_TYPE = "key_sort_type";
    private static final String KEY_ITEM_LIST = "ket_item_list";

    private static final int TOOLTIPS_ID = 1;

    private Toolbar tbTitle;
    private ImageView ivTitle;
    private AppBarLayout ablTitle;
    private CollapsingToolbarLayout ctlTitle;
    private DilatingDotsProgressBar pbFeedListLoading;
    private RecyclerView rvFeedList;
    private SwipeRefreshLayout srlFeedList;
    private FloatingActionButton fabMenu;
    private FooterLayout flMenu;
    private View viewContentShadow;
    private TextView tvUnavailableDescription;
    private TextView tvUnavailableOpenBookmark;
    private MenuButton btnMenuBookmark;
    private MenuButton btnMenuRefresh;
    private MenuButton btnMenuSearch;
    private MenuButton btnMenuSort;
    private MenuButton btnMenuSettings;
    private BottomSheetLayout bslMenu;

    private FabRecyclerViewScrollHelper fabRecyclerViewScrollHelper;
    private FeedAdapter adapter;
    private PostList postList;
    private String sortType = BloggerManager.SORT_PUBLISHED_DATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        bindView();
        setupView();
        setToolbar();

        if (savedInstanceState == null) {
            addScreenTracking();
            callService();
            setupGcmRegister();
        }
    }

    private void setupGcmRegister() {
        registerReceiver();
        if (Utility.getInstance().checkPlayServices(this)) {
            registerGcm();
        }
    }

    private void bindView() {
        tbTitle = findViewById(R.id.tb_title);
        ivTitle = findViewById(R.id.iv_title);
        ablTitle = findViewById(R.id.abl_title);
        ctlTitle = findViewById(R.id.ctl_title);
        pbFeedListLoading = findViewById(R.id.pb_feed_list_loading);
        rvFeedList = findViewById(R.id.rv_feed_list);
        srlFeedList = findViewById(R.id.srl_feed_list);
        fabMenu = findViewById(R.id.fab_menu);
        flMenu = findViewById(R.id.fl_menu);
        viewContentShadow = findViewById(R.id.view_content_shadow);
        tvUnavailableDescription = findViewById(R.id.tv_network_unavailable_description);
        tvUnavailableOpenBookmark = findViewById(R.id.tv_network_unavailable_open_bookmark);
        btnMenuBookmark = findViewById(R.id.btn_menu_bookmark);
        btnMenuRefresh = findViewById(R.id.btn_menu_refresh);
        btnMenuSearch = findViewById(R.id.btn_menu_search);
        btnMenuSort = findViewById(R.id.btn_menu_sort);
        btnMenuSettings = findViewById(R.id.btn_menu_settings);
        bslMenu = findViewById(R.id.bsl_menu);
    }

    private void setupView() {
        fabMenu.setOnClickListener(view -> openMenu());
        fabMenu.setOnLongClickListener(view -> scrollContentToTop());
        viewContentShadow.setOnClickListener(view -> closeMenu());
        btnMenuBookmark.setOnClickListener(view -> onMenuBookmarkClick());
        btnMenuRefresh.setOnClickListener(view -> onMenuRefreshClick());
        btnMenuSearch.setOnClickListener(view -> onMenuSearchClick());
        btnMenuSort.setOnClickListener(view -> onMenuSortClick());
        btnMenuSettings.setOnClickListener(view -> onMenuSettingsClick());
        tvUnavailableOpenBookmark.setOnClickListener(view -> onMenuOpenBookmarkClick());

        btnMenuRefresh.setVisibility(View.GONE);
        viewContentShadow.setVisibility(View.GONE);
        srlFeedList.setOnRefreshListener(this);
        srlFeedList.setColorSchemeResources(R.color.colorAccent);
        flMenu.setFab(fabMenu);

        adapter = new FeedAdapter();
        adapter.setSortType(sortType);
        adapter.setItemListener(this);
        adapter.setLoadMoreListener(this);
        int columnCount = getResources().getInteger(R.integer.feed_column_count);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setAutoMeasureEnabled(true);
        layoutManager.setMeasurementCacheEnabled(true);
        rvFeedList.setLayoutManager(layoutManager);
        rvFeedList.setAdapter(adapter);
        fabRecyclerViewScrollHelper = new FabRecyclerViewScrollHelper(fabMenu);
        rvFeedList.addOnScrollListener(fabRecyclerViewScrollHelper);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ablTitle.setExpanded(false, false);
        }

        setFabForScrollToTopToolTips();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rvFeedList.removeOnScrollListener(fabRecyclerViewScrollHelper);
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
        setImageTitle();
    }

    private void setImageTitle() {
        // Easter Egg for April Fool Day
        if (EasterEggUtility.newInstance().isAprilFoolDay()) {
            ivTitle.setImageResource(R.mipmap.ic_apple_logo);
        }
    }

    private void setFabForScrollToTopToolTips() {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) fabMenu.getLayoutParams();
        FloatingActionButtonBehavior fabBehavior = (FloatingActionButtonBehavior) params.getBehavior();
        fabBehavior.setFabVisibilityChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
        closeMenu();
        if (adapter != null) {
            adapter.updateData();
        }
        ablTitle.addOnOffsetChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver();
        ablTitle.removeOnOffsetChangedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Subscribe
    public void onPostListSuccess(PostList postList) {
        this.postList = postList;
        addPostList(postList);
    }

    @Subscribe
    public void onBlogFailure(PostListFailure failure) {
        rvFeedList.setVisibility(View.GONE);
        pbFeedListLoading.hideNow();
        showUnavailableMessage();
        cancelSwipeRefresh();
    }

    @Subscribe
    public void onSearchRequest(SearchRequest request) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Key.SEARCH_REQUEST, Parcels.wrap(request));
        openActivity(SearchResultActivity.class, bundle);
    }

    @Subscribe
    public void onBookmarkResult(BookmarkResult result) {
        adapter.updateData();
        showBookmarkAddedMessage();
    }

    @Override
    public void onLoadMore() {
        requestMorePostList(sortType, postList.getNextPageToken());
    }

    @Override
    public void onItemClick(FeedViewHolder holder, PostList.Item item) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Key.POST_ITEM, Parcels.wrap(item));
        openActivity(PostByIdActivity.class, bundle, true);
    }

    @Override
    public void onItemLongClick(FeedViewHolder holder, PostList.Item item) {
//        showBottomSheet();
        if (DeveloperConfig.ALLOW_DEBUG_CONTENT) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Key.POST_ITEM, Parcels.wrap(item));
            openActivity(DebugPostActivity.class, bundle);
        }
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

    @Override
    public void onRefresh() {
        adapter.clear();
        callService();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        srlFeedList.setEnabled(verticalOffset == 0);
    }

    @Override
    public void onFabShow() {
        showScrollToTopTooltips();
    }

    @Override
    public void onFabHide() {
        removeScrollToTopTooltips();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_SORT_TYPE, sortType);
        outState.putParcelable(KEY_POST_LIST, Parcels.wrap(postList));
        outState.putParcelable(KEY_ITEM_LIST, Parcels.wrap(adapter.getItemList()));
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        sortType = savedInstanceState.getString(KEY_SORT_TYPE);
        postList = Parcels.unwrap(savedInstanceState.getParcelable(KEY_POST_LIST));
        List<PostList.Item> itemList = Parcels.unwrap(savedInstanceState.getParcelable(KEY_ITEM_LIST));
        setPostList(itemList, postList);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            ablTitle.setExpanded(false, false);
        }
    }

    public void openMenu() {
        flMenu.expandFab();
        AnimationUtility.getInstance().fadeIn(viewContentShadow, 200);
    }

    public void closeMenu() {
        flMenu.contractFab();
        AnimationUtility.getInstance().fadeOut(viewContentShadow, 200);
    }

    public void onMenuBookmarkClick() {
        closeMenu();
        openActivityDelayed(BookmarkActivity.class);
    }

    public void onMenuRefreshClick() {
        callService();
        closeMenu();
    }

    public void onMenuSearchClick() {
        closeMenu();
        openActivityDelayed(SearchActivity.class);
    }

    public void onMenuSortClick() {
        showSortBottomDialog();
        closeMenu();
    }

    public void onMenuSettingsClick() {
        closeMenu();
        openActivityDelayed(SettingsActivity.class);
    }

    public void onMenuOpenBookmarkClick() {
        openActivity(BookmarkActivity.class);
    }

    public boolean scrollContentToTop() {
        rvFeedList.smoothScrollToPosition(0);
        return true;
    }

    public void addPostList(PostList postList) {
        if (postList != null) {
            adapter.addPostListItem(postList.getItems());
            adapter.setLoadMoreAvailable(postList.getNextPageToken() != null);
            hideLoading();
            hideUnavailableMessageImmediately();
            cancelSwipeRefresh();
        } else {
            showUnavailableMessage();
            cancelSwipeRefresh();
        }
    }

    public void setPostList(List<PostList.Item> itemList, PostList postList) {
        if (postList != null) {
            adapter.setPostListItem(itemList);
            adapter.setLoadMoreAvailable(postList.getNextPageToken() != null);
            hideLoading();
            hideUnavailableMessageImmediately();
            cancelSwipeRefresh();
        } else {
            showUnavailableMessage();
            cancelSwipeRefresh();
        }
    }

    public void showBottomSheet() {
        FeedDetailBottomSheet modalBottomSheet = FeedDetailBottomSheet.newInstance();
        modalBottomSheet.show(getSupportFragmentManager(), "bottom_sheet");
    }

    private void showBookmarkAddedMessage() {
        Snackbar.make(tbTitle, R.string.added_to_bookmark, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_view, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToBookmarkList();
                    }
                }).show();
    }

    private void goToBookmarkList() {
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
        sortPostTracking(sortType);
        adapter.clear();
        adapter.setSortType(sortType);
        this.sortType = sortType;
        callService();
    }

    private void showScrollToTopTooltips() {
        if (!FirstLaunchPreference.getInstance().isGoToTopWasLaunchedBefore()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Tooltip.Builder builder = new Tooltip.Builder(TOOLTIPS_ID)
                            .anchor(fabMenu, Tooltip.Gravity.TOP)
                            .activateDelay(800)
                            .closePolicy(new Tooltip.ClosePolicy()
                                    .insidePolicy(true, true)
                                    .outsidePolicy(true, true), 3000)
                            .showDelay(300)
                            .text(getString(R.string.tooltips_go_to_top_hint))
                            .withArrow(true)
                            .withStyleId(R.style.Tooltips)
                            .withOverlay(false)
                            .floatingAnimation(Tooltip.AnimationBuilder.DEFAULT)
                            .build();
                    Tooltip.make(FeedActivity.this, builder).show();
                    FirstLaunchPreference.getInstance().setGoToTopWasLaunched();
                }
            }, 200);
        }
    }

    private void removeScrollToTopTooltips() {
        Tooltip.remove(this, TOOLTIPS_ID);
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
            AnimationUtility.getInstance().fadeIn(tvUnavailableOpenBookmark);
        } else {
            tvUnavailableOpenBookmark.setVisibility(View.GONE);
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
        try {
            spannableMessage.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return spannableMessage;
    }

    private void hideUnavailableMessage() {
        AnimationUtility.getInstance().fadeOut(tvUnavailableDescription);
        AnimationUtility.getInstance().fadeOut(tvUnavailableOpenBookmark);
    }

    private void hideUnavailableMessageImmediately() {
        tvUnavailableDescription.setVisibility(View.GONE);
        tvUnavailableOpenBookmark.setVisibility(View.GONE);
    }

    private void showFAB() {
        fabMenu.show();
        flMenu.contractFab();
    }

    private void cancelSwipeRefresh() {
        if (srlFeedList.isRefreshing()) {
            srlFeedList.setRefreshing(false);
        }
    }

    // Google Cloud Messaging
    private boolean isReceiverRegistered;

    private void registerGcm() {
        Intent intent = new Intent(this, GcmRegisterService.class);
        startService(intent);
    }

    private BroadcastReceiver mRegistrationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//            boolean sentToken = sharedPreferences.getBoolean(GcmRegisterService.SENT_TOKEN_TO_SERVER, false);
            // TODO Do something here when GCM was registered
        }
    };

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(GcmRegisterService.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    private void unregisterReceiver() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
    }

    // Google Analytics
    private void addScreenTracking() {
        EventTracking.getInstance().addScreen(EventKey.Page.MAIN_PAGE);
    }

    private void sortPostTracking(String sortType) {
        if (sortType.equalsIgnoreCase(BloggerManager.SORT_PUBLISHED_DATE)) {
            EventTracking.getInstance().addSortPostTracking(EventKey.Label.SORT_BY_PUBLISHED);
        } else if (sortType.equalsIgnoreCase(BloggerManager.SORT_UPDATED_DATE)) {
            EventTracking.getInstance().addSortPostTracking(EventKey.Label.SORT_BY_UPDATED);
        }
    }
}
