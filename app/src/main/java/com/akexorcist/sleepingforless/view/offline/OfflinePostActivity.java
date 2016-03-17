package com.akexorcist.sleepingforless.view.offline;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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
import com.akexorcist.sleepingforless.util.AnimationUtility;
import com.akexorcist.sleepingforless.util.ContentUtility;
import com.akexorcist.sleepingforless.util.ExternalBrowserUtility;
import com.akexorcist.sleepingforless.view.bookmark.model.Bookmark;
import com.akexorcist.sleepingforless.view.post.model.BasePost;
import com.akexorcist.sleepingforless.widget.MenuButton;
import com.bowyer.app.fabtransitionlayout.FooterLayout;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import org.parceler.Parcels;

import java.util.List;

public class OfflinePostActivity extends SFLActivity implements View.OnClickListener, View.OnTouchListener, OfflinePostAdapter.PostClickListener {
    private Toolbar tbTitle;
    private FloatingActionButton fabMenu;
    private FooterLayout flMenu;
    private View viewContentShadow;
    private DilatingDotsProgressBar pbPostLoading;
    private MenuButton btnMenuUpdate;
    private MenuButton btnMenuDelete;
    private BottomSheetLayout bslMenu;
    private RecyclerView rvPostList;
    private OfflinePostAdapter adapter;

    private Bookmark bookmark;
    private List<BasePost> postList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_post);

        if (savedInstanceState == null) {
            restoreIntentData();
        }

        bindView();
        setupView();
        setToolbar();
        setBookmark();
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
        pbPostLoading = (DilatingDotsProgressBar) findViewById(R.id.pb_offline_post_loading);
        btnMenuUpdate = (MenuButton) findViewById(R.id.btn_menu_update);
        btnMenuDelete = (MenuButton) findViewById(R.id.btn_menu_delete);
        bslMenu = (BottomSheetLayout) findViewById(R.id.bsl_menu);
        rvPostList = (RecyclerView) findViewById(R.id.rv_offline_post_list);
    }

    private void setupView() {
        viewContentShadow.setVisibility(View.GONE);
        viewContentShadow.setOnClickListener(this);
        fabMenu.setOnClickListener(this);
        btnMenuUpdate.setOnClickListener(this);
        btnMenuDelete.setOnClickListener(this);
        btnMenuUpdate.setOnTouchListener(this);
        btnMenuDelete.setOnTouchListener(this);
        flMenu.setFab(fabMenu);
    }

    private void setToolbar() {
        setSupportActionBar(tbTitle);
        setTitle(ContentUtility.getInstance().removeLabelFromTitle(bookmark.getTitle()));
        tbTitle.setNavigationIcon(R.drawable.vector_ic_back);
        tbTitle.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setBookmark() {
        showLoading();
        setPost(bookmark);
    }

    private void restoreIntentData() {
        bookmark = Parcels.unwrap(getIntent().getParcelableExtra(Key.BOOKMARK));
        Log.e("Check", "Bookmark : " + (bookmark != null));
    }

    @Override
    public void onClick(View v) {
        if (v == fabMenu) {
            openMenu();
        } else if (v == viewContentShadow) {
            closeMenu();
        } else if (v == btnMenuUpdate) {
            onMenuUpdateClick();
        } else if (v == btnMenuDelete) {
            onMenuDeleteClick();
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
    public void onImageClickListener(String url) {
        Bundle bundle = new Bundle();
        bundle.putString(Key.IMAGE_PATH, url);
        bundle.putString(Key.POST_ID, bookmark.getPostId());
        openActivity(OfflineImagePostPreviewActivity.class, bundle);
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

    private void onMenuUpdateClick() {

    }

    private void onMenuDeleteClick() {

    }

    private void copyFullUrl(String fullUrl) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Image URL", fullUrl);
        clipboard.setPrimaryClip(clip);
    }

    private void setPost(Bookmark bookmark) {
        if (bookmark != null) {
            postList = ContentUtility.getInstance().convertPost(bookmark.getContent());
            adapter = new OfflinePostAdapter(bookmark.getPostId(), postList);
            adapter.setPostClickListener(this);
            rvPostList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            rvPostList.setAdapter(adapter);
            hideLoading();
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
