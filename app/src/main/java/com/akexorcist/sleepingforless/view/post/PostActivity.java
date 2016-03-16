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

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.common.SFLActivity;
import com.akexorcist.sleepingforless.constant.Key;
import com.akexorcist.sleepingforless.database.Bookmark;
import com.akexorcist.sleepingforless.database.LabelOffline;
import com.akexorcist.sleepingforless.database.PostOffline;
import com.akexorcist.sleepingforless.network.BloggerManager;
import com.akexorcist.sleepingforless.network.model.Failure;
import com.akexorcist.sleepingforless.network.model.Post;
import com.akexorcist.sleepingforless.network.model.PostList;
import com.akexorcist.sleepingforless.util.AnimationUtility;
import com.akexorcist.sleepingforless.util.ContentUtility;
import com.akexorcist.sleepingforless.util.ExternalBrowserUtility;
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

public class PostActivity extends SFLActivity implements View.OnClickListener, View.OnTouchListener, PostAdapter.PostClickListener {
    private Realm realm;
    private Toolbar tbTitle;
    private FloatingActionButton fabMenu;
    private FooterLayout flMenu;
    private View viewContentShadow;
    private MenuButton btnMenuOfflineSave;
    private DilatingDotsProgressBar pbPostLoading;
    private MenuButton btnMenuBookmark;
    private MenuButton btnMenuShare;
    private MenuButton btnMenuSettings;
    private BottomSheetLayout bslMenu;
    private RecyclerView rvPostList;
    private PostAdapter adapter;

    private PostList.Item postItem;
    private Post post;
    private List<BasePost> postList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_reader);
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
        btnMenuOfflineSave = (MenuButton) findViewById(R.id.btn_menu_offline_save);
        btnMenuBookmark = (MenuButton) findViewById(R.id.btn_menu_bookmark);
        btnMenuShare = (MenuButton) findViewById(R.id.btn_menu_share);
        btnMenuSettings = (MenuButton) findViewById(R.id.btn_menu_settings);
        bslMenu = (BottomSheetLayout) findViewById(R.id.bsl_menu);
        rvPostList = (RecyclerView) findViewById(R.id.rv_post_list);
    }

    private void setupView() {
        viewContentShadow.setVisibility(View.GONE);
        viewContentShadow.setOnClickListener(this);
        fabMenu.setOnClickListener(this);
        btnMenuOfflineSave.setOnClickListener(this);
        btnMenuBookmark.setOnClickListener(this);
        btnMenuShare.setOnClickListener(this);
        btnMenuSettings.setOnClickListener(this);
        btnMenuOfflineSave.setOnTouchListener(this);
        btnMenuBookmark.setOnTouchListener(this);
        btnMenuShare.setOnTouchListener(this);
        btnMenuSettings.setOnTouchListener(this);
        flMenu.setFab(fabMenu);
        checkIsBookmarked(postItem.getId());
        checkIsOfflineSaved(postItem.getId());
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
        } else if (v == btnMenuOfflineSave) {
            onMenuOfflineSaveClick();
        } else if (v == btnMenuShare) {
            onMenuShareClick();
        } else if (v == btnMenuSettings) {
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
        Snackbar.make(tbTitle, "Copy image URL to clipboard.", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onLinkClickListener(String url) {
        ExternalBrowserUtility.getInstance().open(this, url);
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
        if (isBookmark()) {
            removePostFromBookmark();
        } else {
            addPostToBookmark();
        }
        closeMenu();
    }

    public void onMenuOfflineSaveClick() {
        if (isOfflineSave()) {
            saveOfflineData();
        } else {
            syncOfflineData();
        }
        closeMenu();
    }

    public void onMenuShareClick() {
        sharePost(postItem.getUrl());
        closeMenu();
    }

    public void onMenuSettingsClick() {
        Log.e("Check", "onMenuSettingsClick");
    }

    public void sharePost(String url) {
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

    public void addPostToBookmark() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Bookmark bookmark = realm.createObject(Bookmark.class);
                bookmark.setPostId(postItem.getId());
                bookmark.setTitle(postItem.getTitle());
                setBookmark(true);
                showSnackbar("Added to bookmark.");
            }
        });
    }

    public void removePostFromBookmark() {
        RealmResults<Bookmark> result = realm.where(Bookmark.class)
                .equalTo("postId", postItem.getId())
                .findAllAsync();
        realm.beginTransaction();
        result.clear();
        realm.commitTransaction();
        setBookmark(false);
        showSnackbar("Removed from bookmark.");
    }

    public void saveOfflineDataToRealm() {
        realm.beginTransaction();
        PostOffline postOffline = realm.createObject(PostOffline.class);
        postOffline.setPostId(postItem.getId());
        postOffline.setTitle(postItem.getTitle());
        postOffline.setContent(postItem.getContent());
        postOffline.setPublished(postItem.getPublished());
        postOffline.setUpdated(postItem.getUpdated());
        postOffline.setUrl(postItem.getUrl());
        RealmList<LabelOffline> labelList = new RealmList<>();
        if (postItem != null) {
            for (String label : postItem.getLabels()) {
                LabelOffline labelOffline = realm.createObject(LabelOffline.class);
                labelOffline.setLabel(label);
                labelList.add(labelOffline);
            }
        }
        postOffline.setLabels(labelList);

        realm.copyToRealm(postOffline);
        realm.commitTransaction();
    }

    public void saveOfflineData() {
        saveOfflineDataToRealm();
        setOfflineSaveAvailable(true);
        showSnackbar("Offline saved.");
    }

    public void syncOfflineData() {
        syncOfflineDataToRealm();
        saveOfflineData();
        setOfflineSaveAvailable(false);
        showSnackbar("Synced.");
    }

    public void syncOfflineDataToRealm() {
        RealmResults<Bookmark> result = realm.where(Bookmark.class)
                .equalTo("postId", postItem.getId())
                .findAllAsync();
        realm.beginTransaction();
        result.clear();
        realm.commitTransaction();
    }

    public void checkIsBookmarked(String postId) {
        final RealmResults<Bookmark> result = realm.where(Bookmark.class)
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

    public void setBookmark(boolean state) {
        int drawableResourceId = (state) ? R.drawable.vector_ic_bookmark_check : R.drawable.vector_ic_bookmark_uncheck;
        btnMenuBookmark.setIconResource(drawableResourceId);
        btnMenuBookmark.setTag(drawableResourceId);
    }

    public boolean isBookmark() {
        return (int) btnMenuBookmark.getTag() == R.drawable.vector_ic_bookmark_check;
    }

    public void checkIsOfflineSaved(String postId) {
        final RealmResults<PostOffline> result = realm.where(PostOffline.class)
                .equalTo("postId", postId)
                .findAllAsync();
        result.addChangeListener(new RealmChangeListener() {
            @Override
            public void onChange() {
                setOfflineSaveAvailable(result.size() == 0);
                result.removeChangeListeners();
            }
        });
    }

    public void setOfflineSaveAvailable(boolean state) {
        int iconResourceId = (state) ? R.drawable.vector_ic_offline_save : R.drawable.vector_ic_sync;
        String text = (state) ? "Save" : "Sync";
        btnMenuOfflineSave.setIconResource(iconResourceId);
        btnMenuOfflineSave.setTag(iconResourceId);
        btnMenuOfflineSave.setText(text);
    }

    public boolean isOfflineSave() {
        return (int) btnMenuBookmark.getTag() == R.drawable.vector_ic_offline_save;
    }

    public void showSnackbar(String message) {
        Snackbar.make(tbTitle, message, Snackbar.LENGTH_SHORT).show();
    }

    private void showLoading() {
        pbPostLoading.showNow();
        rvPostList.setVisibility(View.GONE);
    }

    private void hideLoading() {
        pbPostLoading.hideNow();
        rvPostList.setVisibility(View.VISIBLE);
    }
}
