package com.akexorcist.sleepingforless.view.bookmark;

import android.os.Bundle;
import android.os.Handler;
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
import com.akexorcist.sleepingforless.config.ContentPreference;
import com.akexorcist.sleepingforless.constant.Key;
import com.akexorcist.sleepingforless.database.BookmarkLabelRealm;
import com.akexorcist.sleepingforless.database.BookmarkRealm;
import com.akexorcist.sleepingforless.database.BookmarkImageRealm;
import com.akexorcist.sleepingforless.util.AnimationUtility;
import com.akexorcist.sleepingforless.util.ContentUtility;
import com.akexorcist.sleepingforless.view.bookmark.model.BookmarkImage;
import com.akexorcist.sleepingforless.view.bookmark.model.Bookmark;
import com.akexorcist.sleepingforless.view.bookmark.model.BookmarkLabel;
import com.akexorcist.sleepingforless.view.offline.OfflinePostActivity;
import com.akexorcist.sleepingforless.widget.MenuButton;
import com.bowyer.app.fabtransitionlayout.FooterLayout;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class BookmarkActivity extends SFLActivity implements View.OnTouchListener, View.OnClickListener, BookmarkAdapter.ItemListener {
    private Realm realm;
    private Toolbar tbTitle;
    private FloatingActionButton fabMenu;
    private FooterLayout flMenu;
    private DilatingDotsProgressBar pbBookmarkList;
    private RecyclerView rvBookmarkList;
    private View viewContentShadow;
    private MenuButton btnUpdateAll;
    private MenuButton btnRemoveAll;
    private MenuButton btnInfo;

    private BookmarkAdapter adapter;
    private List<BookmarkRealm> bookmarkRealmList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        realm = Realm.getDefaultInstance();

        bindView();
        setupView();
        setToolbar();
        callDatabase();
        showWarnOfflineBookmark();
    }

    private void bindView() {
        pbBookmarkList = (DilatingDotsProgressBar) findViewById(R.id.pb_bookmark_list_loading);
        rvBookmarkList = (RecyclerView) findViewById(R.id.rv_bookmark_list);
        viewContentShadow = findViewById(R.id.view_content_shadow);
        tbTitle = (Toolbar) findViewById(R.id.tb_title);
        fabMenu = (FloatingActionButton) findViewById(R.id.fab_menu);
        flMenu = (FooterLayout) findViewById(R.id.fl_menu);
        btnUpdateAll = (MenuButton) findViewById(R.id.btn_menu_update_all);
        btnRemoveAll = (MenuButton) findViewById(R.id.btn_menu_remove_all);
        btnInfo = (MenuButton) findViewById(R.id.btn_menu_info);
    }

    private void setupView() {
        viewContentShadow.setVisibility(View.GONE);
        viewContentShadow.setOnClickListener(this);
        fabMenu.hide();
        fabMenu.setOnClickListener(this);
        btnUpdateAll.setOnClickListener(this);
        btnRemoveAll.setOnClickListener(this);
        btnInfo.setOnClickListener(this);
        btnUpdateAll.setOnTouchListener(this);
        btnRemoveAll.setOnTouchListener(this);
        btnInfo.setOnTouchListener(this);
        flMenu.setFab(fabMenu);
        rvBookmarkList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setToolbar() {
        setSupportActionBar(tbTitle);
        setTitle(ContentUtility.getInstance().removeLabelFromTitle(getString(R.string.title_bookmark_list)));
        tbTitle.setNavigationIcon(R.drawable.vector_ic_back);
        tbTitle.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void callDatabase() {
        showLoading();
        getBookmarkFromDatabase();
    }

    private void getBookmarkFromDatabase() {
        final RealmResults<BookmarkRealm> result = realm.where(BookmarkRealm.class)
                .findAllAsync();
        result.addChangeListener(new RealmChangeListener() {
            @Override
            public void onChange() {
                bookmarkRealmList = result;
                setBookmark(bookmarkRealmList);
                result.removeChangeListeners();
                hideLoading();
                checkBookmarkAvailable();
            }
        });
    }

    private void checkBookmarkAvailable() {
        if (bookmarkRealmList != null && bookmarkRealmList.size() > 0) {
            fabMenu.show();
        }
    }

    private void setBookmark(List<BookmarkRealm> bookmarkRealmList) {
        adapter = new BookmarkAdapter(bookmarkRealmList);
        adapter.setItemListener(this);
        rvBookmarkList.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
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
    public void onClick(View v) {
        if (v == fabMenu) {
            openMenu();
        } else if (v == viewContentShadow) {
            closeMenu();
        } else if (v == btnUpdateAll) {
            onMenuUpdateAllClick();
        } else if (v == btnRemoveAll) {
            onMenuDeleteAllClick();
        } else if (v == btnInfo) {
            onMenuInfoClick();
        }
    }

    @Override
    public void onItemClick(BookmarkViewHolder holder, BookmarkRealm bookmarkRealm) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Key.BOOKMARK, Parcels.wrap(convertBookmark(bookmarkRealm)));
        openActivity(OfflinePostActivity.class, bundle);
    }

    @Override
    public void onItemLongClick(BookmarkViewHolder holder, BookmarkRealm bookmarkRealm) {

    }

    private void onMenuUpdateAllClick() {

    }

    private void onMenuDeleteAllClick() {

    }

    private void onMenuInfoClick() {

    }

    private void openMenu() {
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

    private void showLoading() {
        rvBookmarkList.setVisibility(View.GONE);
        pbBookmarkList.showNow();
    }

    private void hideLoading() {
        rvBookmarkList.setVisibility(View.VISIBLE);
        pbBookmarkList.hideNow();
    }

    private void showWarnOfflineBookmark() {
        if (ContentPreference.getInstance().shouldWarnOfflineBookmark()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(tbTitle, R.string.warn_offline_bookmark, Snackbar.LENGTH_LONG)
                            .setAction(R.string.action_dont_warn, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ContentPreference.getInstance().dontWarnOfflineBookmark();
                                }
                            }).show();
                }
            }, 1000);
        }
    }

    private Bookmark convertBookmark(BookmarkRealm bookmarkRealm) {
        Bookmark bookmark = new Bookmark();
        bookmark.setPostId(bookmarkRealm.getPostId());
        bookmark.setTitle(bookmarkRealm.getTitle());
        bookmark.setUrl(bookmarkRealm.getUrl());
        bookmark.setContent(bookmarkRealm.getContent());
        bookmark.setPublished(bookmarkRealm.getPublished());
        bookmark.setUpdated(bookmarkRealm.getUpdated());
        List<BookmarkImage> bookmarkImageList = new ArrayList<>();
        for (BookmarkImageRealm bookmarkImageRealm : bookmarkRealm.getImageList()) {
            bookmarkImageList.add(new BookmarkImage().setUrl(bookmarkImageRealm.getUrl()));
        }
        List<BookmarkLabel> bookmarkLabelList = new ArrayList<>();
        for (BookmarkLabelRealm bookmarkLabelRealm : bookmarkRealm.getLabelList()) {
            bookmarkLabelList.add(new BookmarkLabel().setLabel(bookmarkLabelRealm.getLabel()));
        }
        bookmark.setImageList(bookmarkImageList);
        bookmark.setLabelList(bookmarkLabelList);
        return bookmark;
    }
}
