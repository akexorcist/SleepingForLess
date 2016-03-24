package com.akexorcist.sleepingforless.view.bookmark;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.analytic.EventKey;
import com.akexorcist.sleepingforless.analytic.EventTracking;
import com.akexorcist.sleepingforless.common.SFLActivity;
import com.akexorcist.sleepingforless.config.ContentPreference;
import com.akexorcist.sleepingforless.constant.Key;
import com.akexorcist.sleepingforless.database.BookmarkManager;
import com.akexorcist.sleepingforless.util.AnimationUtility;
import com.akexorcist.sleepingforless.util.ContentUtility;
import com.akexorcist.sleepingforless.view.bookmark.model.Bookmark;
import com.akexorcist.sleepingforless.view.bookmark.model.BookmarkRemoveEvent;
import com.akexorcist.sleepingforless.view.offline.OfflinePostActivity;
import com.akexorcist.sleepingforless.widget.MenuButton;
import com.bowyer.app.fabtransitionlayout.FooterLayout;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Duration;
import com.squareup.otto.Subscribe;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import org.parceler.Parcels;

import java.util.List;

public class BookmarkActivity extends SFLActivity implements View.OnTouchListener, View.OnClickListener, BookmarkAdapter.ItemListener {
    private static final String KEY_BOOKMARK_LIST = "key_bookmark_list";

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
    private List<Bookmark> bookmarkList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        bindView();
        setupView();
        setToolbar();

        if (savedInstanceState == null) {
            addScreenTracking();
            callDatabase();
            showWarnOfflineBookmark();
        }
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
        btnUpdateAll.setVisibility(View.GONE);
        btnInfo.setVisibility(View.GONE);
        btnUpdateAll.setOnClickListener(this);
        btnRemoveAll.setOnClickListener(this);
        btnInfo.setOnClickListener(this);
        btnUpdateAll.setOnTouchListener(this);
        btnRemoveAll.setOnTouchListener(this);
        btnInfo.setOnTouchListener(this);
        flMenu.setFab(fabMenu);
        int columnCount = getResources().getInteger(R.integer.bookmark_column_count);
        rvBookmarkList.setLayoutManager(new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL));
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
        bookmarkList = BookmarkManager.getInstance().getBookmarkList();
        setBookmark(bookmarkList);
    }

    private void checkBookmarkAvailable() {
        if (bookmarkList != null && bookmarkList.size() > 0) {
            fabMenu.show();
        }
    }

    private void setBookmark(List<Bookmark> bookmarkList) {
        adapter = new BookmarkAdapter(bookmarkList);
        adapter.setItemListener(this);
        rvBookmarkList.setAdapter(adapter);
        hideLoading();
        checkBookmarkAvailable();
    }

    @Override
    protected void onResume() {
        super.onResume();
        notifyDataChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Subscribe
    public void onBookmarkRemoveEvent(BookmarkRemoveEvent event) {
        notifyRemoveBookmark(event.getPostId());
        showSnackbar(R.string.removed_from_bookmark);
    }

    private void notifyRemoveBookmark(String postId) {
        for (int i = 0; i < bookmarkList.size(); i++) {
            if (bookmarkList.get(i).getPostId().equalsIgnoreCase(postId)) {
                bookmarkList.remove(i);
                adapter.notifyItemRemoved(i);
            }
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
    public void onBackPressed() {
        if (flMenu.isFabExpanded()) {
            closeMenu();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onItemClick(BookmarkViewHolder holder, Bookmark bookmark) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Key.BOOKMARK, Parcels.wrap(bookmark));
        openActivity(OfflinePostActivity.class, bundle);
    }

    @Override
    public void onItemLongClick(BookmarkViewHolder holder, Bookmark bookmark) {
        // TODO Do something (No idea now)
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_BOOKMARK_LIST, Parcels.wrap(bookmarkList));
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        bookmarkList = Parcels.unwrap(savedInstanceState.getParcelable(KEY_BOOKMARK_LIST));
        setBookmark(bookmarkList);
    }

    private void onMenuUpdateAllClick() {
        // TODO Update all bookmark content
    }

    private void onMenuDeleteAllClick() {
        new MaterialStyledDialog(this)
                .setCancelable(true)
                .setTitle(getString(R.string.remove_all_confirm_title))
                .setDescription(getString(R.string.remove_all_confirm_description))
                .withDialogAnimation(true, Duration.FAST)
                .withIconAnimation(true)
                .setHeaderColor(R.color.colorAccent)
                .setIcon(R.drawable.vector_ic_warning)
                .setPositive(getString(R.string.remove_confirm_yes), new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        removeAllBookmark();
                    }
                })
                .setNegative(getString(R.string.remove_confirm_no), new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .build()
                .show();
        closeMenu();
    }

    private void onMenuInfoClick() {
        // TODO Show bookmark info
    }

    private void removeAllBookmark() {
        if (bookmarkList != null) {
            for (Bookmark bookmark : bookmarkList) {
                BookmarkManager.getInstance().removeBookmark(bookmark.getPostId());
                removeBookmarkTracking(ContentUtility.getInstance().removeLabelFromTitle(bookmark.getTitle()));
            }
            bookmarkList.clear();
            notifyDataChanged();
            closeMenu();
            fabMenu.hide();
            showSnackbar(R.string.removed_all_bookmark);
        }
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

    private void notifyDataChanged() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void showSnackbar(int messageResId) {
        Snackbar.make(tbTitle, messageResId, Snackbar.LENGTH_SHORT).show();
    }

    // Google Analytics
    private void addScreenTracking() {
        EventTracking.getInstance().addScreen(EventKey.Page.BOOKMARK_LIST);
    }

    private void removeBookmarkTracking(String title) {
        EventTracking.getInstance().addContentTracking(EventKey.Action.REMOVE_BOOKMARK, title);
    }
}
