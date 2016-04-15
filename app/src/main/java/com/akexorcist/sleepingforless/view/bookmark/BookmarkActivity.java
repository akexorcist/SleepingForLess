package com.akexorcist.sleepingforless.view.bookmark;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

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
import com.akexorcist.sleepingforless.util.content.ContentUtility;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class BookmarkActivity extends SFLActivity implements BookmarkAdapter.ItemListener {
    private static final String KEY_BOOKMARK_LIST = "key_bookmark_list";

    @Bind(R.id.tb_title)
    Toolbar tbTitle;

    @Bind(R.id.fab_menu)
    FloatingActionButton fabMenu;

    @Bind(R.id.fl_menu)
    FooterLayout flMenu;

    @Bind(R.id.pb_bookmark_list_loading)
    DilatingDotsProgressBar pbBookmarkList;

    @Bind(R.id.rv_bookmark_list)
    RecyclerView rvBookmarkList;

    @Bind(R.id.view_content_shadow)
    View viewContentShadow;

    @Bind(R.id.tv_bookmark_not_found)
    TextView tvBookmarkNotFound;

    @Bind(R.id.btn_menu_update_all)
    MenuButton btnUpdateAll;

    @Bind(R.id.btn_menu_remove_all)
    MenuButton btnRemoveAll;

    @Bind(R.id.btn_menu_info)
    MenuButton btnInfo;

    BookmarkAdapter adapter;
    List<Bookmark> bookmarkList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        ButterKnife.bind(this);
        setupView();
        setToolbar();

        if (savedInstanceState == null) {
            addScreenTracking();
            callDatabase();
            showWarnOfflineBookmark();
        }
    }

    private void setupView() {
        viewContentShadow.setVisibility(View.GONE);
        fabMenu.hide();
        btnUpdateAll.setVisibility(View.GONE);
        btnInfo.setVisibility(View.GONE);
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
        if (bookmarkList != null && bookmarkList.size() > 0) {
            adapter = new BookmarkAdapter(bookmarkList);
            adapter.setItemListener(this);
            rvBookmarkList.setAdapter(adapter);
            hideLoading();
            showContentFound();
            checkBookmarkAvailable();
        } else {
            hideLoading();
            showContentNotFound();
        }
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

    @OnClick(R.id.btn_menu_update_all)
    public void onMenuUpdateAllClick() {
        // TODO Update all bookmark content
    }

    @OnClick(R.id.btn_menu_remove_all)
    public void onMenuDeleteAllClick() {
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

    @OnClick(R.id.btn_menu_info)
    public void onMenuInfoClick() {
        // TODO Show bookmark info
    }

    @OnClick(R.id.fab_menu)
    public void openMenu() {
        flMenu.expandFab();
        AnimationUtility.getInstance().fadeIn(viewContentShadow, 200);
    }

    @OnClick(R.id.view_content_shadow)
    public void closeMenu() {
        flMenu.contractFab();
        AnimationUtility.getInstance().fadeOut(viewContentShadow, 200);
    }

    @OnLongClick(R.id.fab_menu)
    public boolean scrollContentToTop() {
        rvBookmarkList.smoothScrollToPosition(0);
        return true;
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
            showContentNotFound();
            showSnackbar(R.string.removed_all_bookmark);
        }
    }

    private void showLoading() {
        pbBookmarkList.showNow();
    }

    private void hideLoading() {
        pbBookmarkList.hideNow();
    }

    private void showContentFound() {
        rvBookmarkList.setVisibility(View.VISIBLE);
        tvBookmarkNotFound.setVisibility(View.GONE);
    }

    private void showContentNotFound() {
        rvBookmarkList.setVisibility(View.GONE);
        tvBookmarkNotFound.setVisibility(View.VISIBLE);
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
