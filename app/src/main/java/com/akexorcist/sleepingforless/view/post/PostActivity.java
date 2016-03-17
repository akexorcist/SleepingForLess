package com.akexorcist.sleepingforless.view.post;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.common.SFLActivity;
import com.akexorcist.sleepingforless.constant.Key;
import com.akexorcist.sleepingforless.database.BookmarkImageRealm;
import com.akexorcist.sleepingforless.database.BookmarkLabelRealm;
import com.akexorcist.sleepingforless.database.BookmarkRealm;
import com.akexorcist.sleepingforless.network.BloggerManager;
import com.akexorcist.sleepingforless.network.model.Failure;
import com.akexorcist.sleepingforless.network.model.Post;
import com.akexorcist.sleepingforless.network.model.PostList;
import com.akexorcist.sleepingforless.util.AnimationUtility;
import com.akexorcist.sleepingforless.util.BookmarkManager;
import com.akexorcist.sleepingforless.util.ContentUtility;
import com.akexorcist.sleepingforless.util.ExternalBrowserUtility;
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

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;

public class PostActivity extends SFLActivity implements View.OnClickListener, View.OnTouchListener, PostAdapter.PostClickListener, BookmarkManager.DownloadCallback {
    private Realm realm;
    private Toolbar tbTitle;
    private FloatingActionButton fabMenu;
    private FooterLayout flMenu;
    private View viewContentShadow;
    private DilatingDotsProgressBar pbPostLoading;
    private DilatingDotsProgressBar pbPostBookmarkLoading;
    private LinearLayout layoutPostBookmarkLoading;
    private View viewPostBookmarkLoading;
    private MenuButton btnMenuBookmark;
    private MenuButton btnMenuShare;
    private BottomSheetLayout bslMenu;
    private RecyclerView rvPostList;
    private PostAdapter adapter;

    private PostList.Item postItem;
    private Post post;
    private List<BasePost> postList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        realm = Realm.getDefaultInstance();

        if (savedInstanceState == null) {
            restoreIntentData();
        }

        bindView();
        setupView();
        setToolbar();
        callService();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
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
        btnMenuBookmark = (MenuButton) findViewById(R.id.btn_menu_bookmark);
        btnMenuShare = (MenuButton) findViewById(R.id.btn_menu_share);
        bslMenu = (BottomSheetLayout) findViewById(R.id.bsl_menu);
        rvPostList = (RecyclerView) findViewById(R.id.rv_post_list);
    }

    private void setupView() {
        viewContentShadow.setVisibility(View.GONE);
        viewContentShadow.setOnClickListener(this);
        fabMenu.setOnClickListener(this);
        btnMenuBookmark.setOnClickListener(this);
        btnMenuShare.setOnClickListener(this);
        btnMenuBookmark.setOnTouchListener(this);
        btnMenuShare.setOnTouchListener(this);
        flMenu.setFab(fabMenu);
        checkIsBookmarked(postItem.getId());
        hideBookmarkLoading();
    }

    private void setToolbar() {
        setSupportActionBar(tbTitle);
        setTitle(ContentUtility.getInstance().removeLabelFromTitle(postItem.getTitle()));
        tbTitle.setNavigationIcon(R.drawable.vector_ic_back);
        tbTitle.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void restoreIntentData() {
        postItem = Parcels.unwrap(getIntent().getParcelableExtra(Key.POST_ID));
    }

    private void callService() {
        showLoading();
        BloggerManager.getInstance().getPost(postItem.getId());
    }

    @Subscribe
    public void onPostSuccess(Post post) {
        this.post = post;
        setPost(post);
        hideLoading();
    }

    @Subscribe
    public void onPostFailure(Failure failure) {
        Log.e("Check", "onPostFailure");
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
    public void onDownloadSuccess() {
        hideBookmarkLoading();
        setBookmark(true);
        showBookmarkAddedMessage();
    }

    private void copyFullUrl(String fullUrl) {
        ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Image URL", fullUrl);
        clipboard.setPrimaryClip(clip);
    }

    private void setPost(Post post) {
        if (post != null) {
            postList = ContentUtility.getInstance().convertPost(post.getContent());
            adapter = new PostAdapter(postList);
            adapter.setPostClickListener(this);
            rvPostList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            rvPostList.setAdapter(adapter);
//            rvPostList.setAdapter(adapter);
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
            removePostFromBookmark();
            BookmarkManager.getInstance().removeBookmarkImageFile(post.getId());
            setBookmark(false);
            showBookmarkRemovedMessage();
        } else {
            showBookmarkLoading();
            addBookmarkToDatabase();
            downloadImageToBookmark();
        }
        closeMenu();
    }

    private void onMenuShareClick() {
        sharePost(postItem.getUrl());
        closeMenu();
    }

    private void sharePost(String url) {
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

    private void removePostFromBookmark() {
        realm.beginTransaction();
        RealmResults<BookmarkRealm> result = realm.where(BookmarkRealm.class)
                .equalTo("postId", postItem.getId())
                .findAll();
        result.clear();
        realm.commitTransaction();
    }

    private void addBookmarkToDatabase() {
        realm.beginTransaction();
        BookmarkRealm postOffline = realm.createObject(BookmarkRealm.class);
        postOffline.setPostId(post.getId());
        postOffline.setTitle(post.getTitle());
        postOffline.setContent(post.getContent());
        postOffline.setPublished(post.getPublished());
        postOffline.setUpdated(post.getUpdated());
        postOffline.setUrl(post.getUrl());
        RealmList<BookmarkLabelRealm> labelList = new RealmList<>();
        if (post.getLabels() != null) {
            for (String label : post.getLabels()) {
                BookmarkLabelRealm bookmarkLabelRealm = realm.createObject(BookmarkLabelRealm.class);
                bookmarkLabelRealm.setLabel(label);
                labelList.add(bookmarkLabelRealm);
            }
        }
        postOffline.setLabelList(labelList);
        RealmList<BookmarkImageRealm> imageList = new RealmList<>();
        if (postItem.getImages() != null) {
            for (PostList.Image image : postItem.getImages()) {
                BookmarkImageRealm bookmarkImageRealm = realm.createObject(BookmarkImageRealm.class);
                bookmarkImageRealm.setUrl(image.getUrl());
                imageList.add(bookmarkImageRealm);
            }
        }
        postOffline.setImageList(imageList);
        realm.copyToRealm(postOffline);
        realm.commitTransaction();
    }

    private void downloadImageToBookmark() {
        BookmarkManager.getInstance().downloadImageToBookmark(post.getId(), postList, this);
    }

    private void checkIsBookmarked(String postId) {
        final RealmResults<BookmarkRealm> result = realm.where(BookmarkRealm.class)
                .equalTo("postId", postId)
                .findAllAsync();
        result.addChangeListener(new RealmChangeListener() {
            @Override
            public void onChange() {
                setBookmark(result.size() > 0);
                result.removeChangeListeners();
            }
        });
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
        pbPostLoading.showNow();
        rvPostList.setVisibility(View.GONE);
    }

    private void hideLoading() {
        pbPostLoading.hideNow();
        rvPostList.setVisibility(View.VISIBLE);
    }

    private void showBookmarkLoading() {
        pbPostBookmarkLoading.showNow();
        layoutPostBookmarkLoading.setVisibility(View.VISIBLE);
        viewPostBookmarkLoading.setVisibility(View.VISIBLE);
    }

    private void hideBookmarkLoading() {
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
}
