package com.akexorcist.sleepingforless.view.search;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.analytic.EventKey;
import com.akexorcist.sleepingforless.analytic.EventTracking;
import com.akexorcist.sleepingforless.common.SFLActivity;
import com.akexorcist.sleepingforless.constant.Key;
import com.akexorcist.sleepingforless.network.blogger.BloggerManager;
import com.akexorcist.sleepingforless.network.blogger.model.PostList;
import com.akexorcist.sleepingforless.network.blogger.model.PostListFailure;
import com.akexorcist.sleepingforless.util.AnimationUtility;
import com.akexorcist.sleepingforless.util.content.ContentUtility;
import com.akexorcist.sleepingforless.view.feed.FeedAdapter;
import com.akexorcist.sleepingforless.view.feed.FeedViewHolder;
import com.akexorcist.sleepingforless.view.post.PostByIdActivity;
import com.squareup.otto.Subscribe;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import org.parceler.Parcels;

public class SearchResultActivity extends SFLActivity implements View.OnClickListener, FeedAdapter.ItemListener, FeedAdapter.LoadMoreListener {
    private static final String KEY_POST_LIST = "key_post_list";
    private static final String KEY_SEARCH_REQUEST = "key_search_request";

    private Toolbar tbTitle;
    private FloatingActionButton fabMenu;
    private DilatingDotsProgressBar pbSearchResultList;
    private RecyclerView rvSearchResultList;
    private View viewContentShadow;
    private TextView tvSearchResultNotFound;
    private TextView tvUnavailableDescription;
    private TextView tvOpenBookmark;

    private FeedAdapter adapter;
    private PostList postList;
    private SearchRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        if (savedInstanceState == null) {
            restoreIntentData();
        }

        bindView();
        setupView();
        setToolbar();

        if (savedInstanceState == null) {
            screenTracking();
            setTitle(request);
            callService();
        }
    }

    public void callService() {
        showLoading();
        hideUnavailableMessageImmediately();
        searchPost(request.getKeyword());
    }

    private void bindView() {
        pbSearchResultList = (DilatingDotsProgressBar) findViewById(R.id.pb_search_result_list_loading);
        rvSearchResultList = (RecyclerView) findViewById(R.id.rv_search_result_list);
        viewContentShadow = findViewById(R.id.view_content_shadow);
        tvSearchResultNotFound = (TextView) findViewById(R.id.tv_search_result_not_found);
        tvUnavailableDescription = (TextView) findViewById(R.id.tv_network_unavailable_description);
        tvOpenBookmark = (TextView) findViewById(R.id.tv_network_unavailable_open_bookmark);
        tbTitle = (Toolbar) findViewById(R.id.tb_title);
        fabMenu = (FloatingActionButton) findViewById(R.id.fab_menu);
    }

    private void setupView() {
        viewContentShadow.setVisibility(View.GONE);
        viewContentShadow.setOnClickListener(this);
        tvOpenBookmark.setVisibility(View.GONE);
        fabMenu.setOnClickListener(this);
        adapter = new FeedAdapter();
        adapter.setItemListener(this);
        adapter.setLoadMoreListener(this);
        int columnCount = getResources().getInteger(R.integer.search_result_column_count);
        rvSearchResultList.setLayoutManager(new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL));
        rvSearchResultList.setAdapter(adapter);
    }

    private void setToolbar() {
        setSupportActionBar(tbTitle);
        tbTitle.setNavigationIcon(R.drawable.vector_ic_back);
        tbTitle.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setTitle(SearchRequest request) {
        setTitle(ContentUtility.getInstance().removeLabelFromTitle(getString(R.string.title_search_result, request.getKeyword())));
    }

    private void restoreIntentData() {
        request = Parcels.unwrap(getIntent().getParcelableExtra(Key.SEARCH_REQUEST));
    }

    private void searchPost(String keyword) {
        BloggerManager.getInstance().searchPost(keyword);
    }

    private void searchMorePost(String nextPageToken) {
        BloggerManager.getInstance().getNextPostList(nextPageToken, false);
    }

    @Subscribe
    public void onPostListSuccess(PostList postList) {
        this.postList = postList;
        setPostList(postList);
    }

    @Subscribe
    public void onPostListFailure(PostListFailure failure) {
        rvSearchResultList.setVisibility(View.GONE);
        pbSearchResultList.hideNow();
        fabMenu.show();
        showUnavailableMessage();
    }

    @Subscribe
    public void onSearchRequest(SearchRequest request) {
        this.request = request;
        setTitle(request);
        callService();
    }

    @Override
    public void onClick(View v) {
        if (v == fabMenu) {
            onMenuSearchClick();
        }
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
//        Bundle bundle = new Bundle();
//        bundle.putParcelable(Key.POST_ITEM, Parcels.wrap(item));
//        openActivity(DebugPostActivity.class, bundle);
    }

    @Override
    public void onLoadMore() {
        searchMorePost(postList.getNextPageToken());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_POST_LIST, Parcels.wrap(postList));
        outState.putParcelable(KEY_SEARCH_REQUEST, Parcels.wrap(request));
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        postList = Parcels.unwrap(savedInstanceState.getParcelable(KEY_POST_LIST));
        request = Parcels.unwrap(savedInstanceState.getParcelable(KEY_SEARCH_REQUEST));
        onPostListSuccess(postList);
        setTitle(request);
    }

    public void setPostList(PostList postList) {
        if (postList != null && postList.getItems() != null && postList.getItems().size() > 0) {
            adapter.setPostListItem(postList.getItems());
//            adapter.setLoadMoreAvailable(postList.getNextPageToken() != null);
            adapter.setLoadMoreAvailable(false);
            showContentFound();
        } else {
            showContentNotFound();
        }
        hideLoading();
        hideUnavailableMessage();
        fabMenu.show();
    }

    public void onMenuSearchClick() {
        openActivity(SearchActivity.class);
    }

    private void showLoading() {
        fabMenu.hide();
        pbSearchResultList.showNow();
        tvSearchResultNotFound.setVisibility(View.GONE);
        rvSearchResultList.setVisibility(View.GONE);
    }

    private void hideLoading() {
        pbSearchResultList.hideNow();
    }

    private void showContentFound() {
        tvSearchResultNotFound.setVisibility(View.GONE);
        rvSearchResultList.setVisibility(View.VISIBLE);
    }

    private void showContentNotFound() {
        tvSearchResultNotFound.setVisibility(View.VISIBLE);
        rvSearchResultList.setVisibility(View.GONE);
    }

    private void showUnavailableMessage() {
        tvUnavailableDescription.setText(R.string.network_unavailable);
        AnimationUtility.getInstance().fadeIn(tvUnavailableDescription);
    }

    private void hideUnavailableMessage() {
        AnimationUtility.getInstance().fadeOut(tvUnavailableDescription);
    }

    private void hideUnavailableMessageImmediately() {
        tvUnavailableDescription.setVisibility(View.GONE);
    }

    // Google Analytics
    private void screenTracking() {
        EventTracking.getInstance().addScreen(EventKey.Page.SEARCH);
    }
}
