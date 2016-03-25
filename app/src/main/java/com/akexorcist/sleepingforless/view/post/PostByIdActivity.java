package com.akexorcist.sleepingforless.view.post;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.analytic.EventKey;
import com.akexorcist.sleepingforless.analytic.EventTracking;
import com.akexorcist.sleepingforless.common.SFLActivity;
import com.akexorcist.sleepingforless.constant.Key;
import com.akexorcist.sleepingforless.database.BookmarkResult;
import com.akexorcist.sleepingforless.network.blogger.BloggerManager;
import com.akexorcist.sleepingforless.network.blogger.model.Post;
import com.akexorcist.sleepingforless.network.blogger.model.PostById;
import com.akexorcist.sleepingforless.network.blogger.model.PostByIdFailure;
import com.akexorcist.sleepingforless.network.blogger.model.PostList;
import com.akexorcist.sleepingforless.util.AnimationUtility;
import com.akexorcist.sleepingforless.database.BookmarkManager;
import com.akexorcist.sleepingforless.util.content.ContentConverter;
import com.akexorcist.sleepingforless.util.content.ContentResult;
import com.akexorcist.sleepingforless.util.content.ContentUtility;
import com.akexorcist.sleepingforless.util.ExternalBrowserUtility;
import com.akexorcist.sleepingforless.util.Utility;
import com.akexorcist.sleepingforless.view.bookmark.BookmarkActivity;
import com.akexorcist.sleepingforless.view.post.model.BasePost;
import com.akexorcist.sleepingforless.widget.MenuButton;
import com.bowyer.app.fabtransitionlayout.FooterLayout;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.IntentPickerSheetView;
import com.squareup.otto.Subscribe;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import org.parceler.Parcels;

import java.util.List;

public class PostByIdActivity extends SFLActivity implements View.OnClickListener, View.OnTouchListener, PostAdapter.PostClickListener, BookmarkManager.DownloadCallback {
    private static final String KEY_IS_BOOKMARKING = "key_is_bookmarking";
    private static final String KEY_POST_ITEM = "key_post_item";
    private static final String KEY_POST = "key_post";
    private static final String KEY_POST_LIST = "key_post_list";

    private Toolbar tbTitle;
    private FloatingActionButton fabMenu;
    private FooterLayout flMenu;
    private View viewContentShadow;
    private DilatingDotsProgressBar pbPostLoading;
    private DilatingDotsProgressBar pbPostBookmarkLoading;
    private LinearLayout layoutPostBookmarkLoading;
    private View viewPostBookmarkLoading;
    private TextView tvUnavailableDescription;
    private TextView tvOpenBookmark;
    private MenuButton btnMenuBookmark;
    private MenuButton btnMenuShare;
    private BottomSheetLayout bslMenu;
    private RecyclerView rvPostList;
    private PostAdapter adapter;

    private boolean isBookmarking;
    private PostList.Item postItem;
    private Post post;
    private List<BasePost> postList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        if (savedInstanceState == null) {
            restoreIntentData();
        }

        bindView();
        setupView();
        setToolbar();

        if (savedInstanceState == null) {
            screenTracking();
            readContentTracking();
            callService();
            checkIsBookmarked(postItem.getId());
            setTitle(postItem);
            hideBookmarkLoadingImmediately();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        notifyBookmarkChange();
    }

    @Override
    protected void onStart() {
        super.onStart();
        ExternalBrowserUtility.getInstance().bindService(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ExternalBrowserUtility.getInstance().unbindService(this);
    }

    private void bindView() {
        tbTitle = (Toolbar) findViewById(R.id.tb_title);
        fabMenu = (FloatingActionButton) findViewById(R.id.fab_menu);
        flMenu = (FooterLayout) findViewById(R.id.fl_menu);
        viewContentShadow = findViewById(R.id.view_content_shadow);
        pbPostLoading = (DilatingDotsProgressBar) findViewById(R.id.pb_post_loading);
        pbPostBookmarkLoading = (DilatingDotsProgressBar) findViewById(R.id.pb_post_bookmark_loading);
        layoutPostBookmarkLoading = (LinearLayout) findViewById(R.id.layout_post_bookmark_loading);
        viewPostBookmarkLoading = findViewById(R.id.view_post_bookmark_loading);
        tvUnavailableDescription = (TextView) findViewById(R.id.tv_network_unavailable_description);
        tvOpenBookmark = (TextView) findViewById(R.id.tv_network_unavailable_open_bookmark);
        btnMenuBookmark = (MenuButton) findViewById(R.id.btn_menu_bookmark);
        btnMenuShare = (MenuButton) findViewById(R.id.btn_menu_share);
        bslMenu = (BottomSheetLayout) findViewById(R.id.bsl_menu);
        rvPostList = (RecyclerView) findViewById(R.id.rv_post_list);
    }

    private void setupView() {
        viewContentShadow.setVisibility(View.GONE);
        viewContentShadow.setOnClickListener(this);
        fabMenu.setOnClickListener(this);
        tvOpenBookmark.setOnClickListener(this);
        btnMenuBookmark.setOnClickListener(this);
        btnMenuShare.setOnClickListener(this);
        viewPostBookmarkLoading.setOnClickListener(this);
        btnMenuBookmark.setOnTouchListener(this);
        btnMenuShare.setOnTouchListener(this);
        flMenu.setFab(fabMenu);
        rvPostList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        hideBookmarkLoadingImmediately();
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

    private void setTitle(PostList.Item postItem) {
        setTitle(ContentUtility.getInstance().removeLabelFromTitle(postItem.getTitle()));
    }

    private void restoreIntentData() {
        postItem = Parcels.unwrap(getIntent().getParcelableExtra(Key.POST_ITEM));
    }

    private void callService() {
        showLoading();
        hideUnavailableMessageImmediately();
        BloggerManager.getInstance().getPostById(postItem.getId());
    }

    @Subscribe
    public void onPostSuccess(PostById post) {
        this.post = post;
        setPost(post);
        fabMenu.show();
    }

    @Subscribe
    public void onPostFailure(PostByIdFailure failure) {
        pbPostLoading.hideNow();
        showUnavailableMessage();
    }

    @Override
    public void onClick(View v) {
        if (v == fabMenu) {
            openMenu();
        } else if (v == viewContentShadow) {
            closeMenu();
        } else if (v == btnMenuBookmark) {
            onMenuBookmarkClick();
        } else if (v == btnMenuShare) {
            onMenuShareClick();
        } else if (v == tvOpenBookmark) {
            onMenuOpenBookmarkClick();
        } else if (v == viewPostBookmarkLoading) {
            closeMenu();
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

    @Override
    public void onBackPressed() {
        if (bslMenu.isSheetShowing()) {
            bslMenu.dismissSheet();
        } else if (flMenu.isFabExpanded()) {
            closeMenu();
        } else if (isBookmarking) {
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onImageClickListener(String fullUrl) {
        Bundle bundle = new Bundle();
        bundle.putString(Key.KEY_FULL_URL, fullUrl);
        openActivity(ImagePostPreviewActivity.class, bundle);
    }

    @Override
    public void onImageLongClickListener(String fullUrl) {
        copyFullUrl(fullUrl);
        Snackbar.make(tbTitle, R.string.copy_image_url_to_clipboard, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onLinkClickListener(String url) {
        ExternalBrowserUtility.getInstance().open(this, url);
    }

    @Override
    public void onVideoClickListener(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    @Override
    public void onDownloadSuccess() {
//        hideBookmarkLoading();
//        setBookmark(true);
//        showBookmarkAddedMessage();
    }

    @Subscribe
    public void onDownloadResult(BookmarkResult result) {
        isBookmarking = false;
        hideBookmarkLoading();
        setBookmark(true);
        showBookmarkAddedMessage();
    }

    @Subscribe
    public void onContentConvertResult(ContentResult result) {
        postList = result.getBasePostList();
        setPostList(postList);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(KEY_IS_BOOKMARKING, isBookmarking);
        outState.putParcelable(KEY_POST_ITEM, Parcels.wrap(postItem));
        outState.putParcelable(KEY_POST, Parcels.wrap(post));
        outState.putParcelable(KEY_POST_LIST, Parcels.wrap(postList));
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isBookmarking = savedInstanceState.getBoolean(KEY_IS_BOOKMARKING);
        postItem = Parcels.unwrap(savedInstanceState.getParcelable(KEY_POST_ITEM));
        post = Parcels.unwrap(savedInstanceState.getParcelable(KEY_POST));
        postList = Parcels.unwrap(savedInstanceState.getParcelable(KEY_POST_LIST));

        setBookmarking(isBookmarking);
        checkIsBookmarked(postItem.getId());
        setPostList(postList);
        setTitle(postItem);
    }

    private void setBookmarking(boolean isBookmarking) {
        if (isBookmarking) {
            showBookmarkLoadingImmediately();
        } else {
            hideBookmarkLoadingImmediately();
        }
    }

    private void copyFullUrl(String fullUrl) {
        Utility.getInstance().copyTextToClipboard("Image URL", fullUrl);
    }

    private void setPost(Post post) {
        if (post != null) {
            ContentConverter.getInstance().convert(post.getContent());
        }
    }

    private void setPostList(List<BasePost> postList) {
        if (postList != null) {
            adapter = new PostAdapter(postList);
            adapter.setPostClickListener(this);
            rvPostList.setAdapter(adapter);
            hideLoading();
            hideUnavailableMessageImmediately();
        }
    }

    public void openMenu() {
        flMenu.expandFab();
        AnimationUtility.getInstance().fadeIn(viewContentShadow, 200);
    }

    private void closeMenu() {
        flMenu.contractFab();
        AnimationUtility.getInstance().fadeOut(viewContentShadow, 200);
    }

    private void scaleMenuButtonUp(View v) {
        AnimationUtility.getInstance().scaleUp(v, 200);
    }

    private void scaleMenuButtonBack(View v) {
        AnimationUtility.getInstance().scaleBack(v, 200);
    }

    private void onMenuBookmarkClick() {
        if (isBookmark()) {
            removeBookmark();
        } else {
            addBookmark();
        }
        closeMenu();
    }

    private void onMenuShareClick() {
        sharePost(postItem.getUrl());
        closeMenu();
    }

    public void onMenuOpenBookmarkClick() {
        openActivity(BookmarkActivity.class);
        finish();
    }

    private void sharePost(String url) {
        shareContentTracking();
        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        shareIntent.setType("text/plain");
        IntentPickerSheetView intentPickerSheet = new IntentPickerSheetView(this, shareIntent, "Share via...", new IntentPickerSheetView.OnIntentPickedListener() {
            @Override
            public void onIntentPicked(IntentPickerSheetView.ActivityInfo activityInfo) {
                bslMenu.dismissSheet();
                startActivity(activityInfo.getConcreteIntent(shareIntent));
            }
        });
        bslMenu.showWithSheetView(intentPickerSheet);
    }

    private void addBookmark() {
        isBookmarking = true;
        addBookmarkTracking();
        showBookmarkLoading();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                addBookmarkToDatabase();
                downloadImageToBookmark();
            }
        }, 700);
    }

    private void removeBookmark() {
        removeBookmarkTracking();
        removePostFromBookmark();
        BookmarkManager.getInstance().removeBookmarkImageFile(post.getId());
        setBookmark(false);
        showBookmarkRemovedMessage();
    }

    private void removePostFromBookmark() {
        BookmarkManager.getInstance().removeBookmark(post.getId());
    }

    private void addBookmarkToDatabase() {
        BookmarkManager.getInstance().addBookmark(post);
    }

    private void downloadImageToBookmark() {
        BookmarkManager.getInstance().downloadImageToBookmark(post.getId(), postList, this);
    }

    private void notifyBookmarkChange() {
        if (post != null) {
            checkIsBookmarked(post.getId());
        }
    }

    private void checkIsBookmarked(String postId) {
        setBookmark(BookmarkManager.getInstance().isBookmark(postId));
    }

    private void setBookmark(boolean state) {
        int drawableResourceId = (state) ? R.drawable.vector_ic_bookmark_check : R.drawable.vector_ic_bookmark_uncheck;
        int text = (state) ? R.string.menu_remove_bookmark : R.string.menu_add_bookmark;
        btnMenuBookmark.setIconResource(drawableResourceId);
        btnMenuBookmark.setTag(drawableResourceId);
        btnMenuBookmark.setText(text);
    }

    private boolean isBookmark() {
        if (btnMenuBookmark.getTag() == null) {
            btnMenuBookmark.setTag(R.drawable.vector_ic_bookmark_check);
        }
        return (int) btnMenuBookmark.getTag() == R.drawable.vector_ic_bookmark_check;
    }

    private void showLoading() {
        fabMenu.hide();
        pbPostLoading.showNow();
        rvPostList.setVisibility(View.GONE);
    }

    private void hideLoading() {
        pbPostLoading.hideNow();
        rvPostList.setVisibility(View.VISIBLE);
    }

    private void showBookmarkLoading() {
        fabMenu.hide();
        flMenu.hide();
        pbPostBookmarkLoading.showNow();
        AnimationUtility.getInstance().fadeIn(layoutPostBookmarkLoading);
        AnimationUtility.getInstance().fadeIn(viewPostBookmarkLoading);
    }

    private void showBookmarkLoadingImmediately() {
        fabMenu.hide();
        flMenu.hide();
        pbPostBookmarkLoading.showNow();
        layoutPostBookmarkLoading.setVisibility(View.VISIBLE);
        viewPostBookmarkLoading.setVisibility(View.VISIBLE);
    }

    private void hideBookmarkLoading() {
        fabMenu.show();
        fabMenu.show();
        pbPostBookmarkLoading.hideNow();
        AnimationUtility.getInstance().fadeOut(layoutPostBookmarkLoading);
        AnimationUtility.getInstance().fadeOut(viewPostBookmarkLoading);
    }

    private void hideBookmarkLoadingImmediately() {
        pbPostBookmarkLoading.hideNow();
        layoutPostBookmarkLoading.setVisibility(View.GONE);
        viewPostBookmarkLoading.setVisibility(View.GONE);
    }

    private void showBookmarkRemovedMessage() {
        Snackbar.make(tbTitle, R.string.removed_from_bookmark, Snackbar.LENGTH_SHORT).show();
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

    // Google Analytics
    private void screenTracking() {
        EventTracking.getInstance().addScreen(EventKey.Page.READ_POST);
    }

    private void addBookmarkTracking() {
        EventTracking.getInstance().addContentTracking(EventKey.Action.ADD_BOOKMARK, getPostTitle());
    }

    private void removeBookmarkTracking() {
        EventTracking.getInstance().addContentTracking(EventKey.Action.REMOVE_BOOKMARK, getPostTitle());
    }

    private void shareContentTracking() {
        EventTracking.getInstance().addContentTracking(EventKey.Action.SHARE, getPostTitle());
    }

    private void readContentTracking() {
        EventTracking.getInstance().addContentTracking(EventKey.Action.READ, getPostTitle());
    }

    private String getPostTitle() {
        if (postItem != null) {
            return ContentUtility.getInstance().removeLabelFromTitle(postItem.getTitle());
        }
        return null;
    }
}
