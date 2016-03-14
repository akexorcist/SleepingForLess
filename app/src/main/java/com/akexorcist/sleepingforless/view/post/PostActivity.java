package com.akexorcist.sleepingforless.view.post;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.common.SFLActivity;
import com.akexorcist.sleepingforless.constant.Key;
import com.akexorcist.sleepingforless.database.Bookmark;
import com.akexorcist.sleepingforless.network.BloggerManager;
import com.akexorcist.sleepingforless.network.model.Failure;
import com.akexorcist.sleepingforless.network.model.Post;
import com.akexorcist.sleepingforless.network.model.PostList;
import com.akexorcist.sleepingforless.util.AnimationUtility;
import com.akexorcist.sleepingforless.util.ContentUtility;
import com.akexorcist.sleepingforless.util.ExternalBrowserUtility;
import com.akexorcist.sleepingforless.view.post.model.CodePost;
import com.akexorcist.sleepingforless.view.post.model.HeaderPost;
import com.akexorcist.sleepingforless.view.post.model.ImagePost;
import com.akexorcist.sleepingforless.view.post.model.PlainTextPost;
import com.akexorcist.sleepingforless.view.search.SearchActivity;
import com.bowyer.app.fabtransitionlayout.FooterLayout;
import com.bumptech.glide.Glide;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.IntentPickerSheetView;
import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

import java.util.Comparator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class PostActivity extends SFLActivity implements LinkClickable.LinkClickListener, View.OnClickListener, View.OnTouchListener {
    private Realm realm;
    private Toolbar tbTitle;
    private FloatingActionButton fabMenu;
    private FooterLayout flMenu;
    private View viewContentShadow;
    private ImageView ivMenuOfflineSave;
    private ImageView ivMenuBookmark;
    private ImageView ivMenuShare;
    private ImageView ivMenuSettings;
    private BottomSheetLayout bslMenu;
    private LinearLayout layoutPostContent;

    private PostList.Item postItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_reader);

        tbTitle = (Toolbar) findViewById(R.id.tb_title);
        fabMenu = (FloatingActionButton) findViewById(R.id.fab_menu);
        flMenu = (FooterLayout) findViewById(R.id.fl_menu);
        viewContentShadow = findViewById(R.id.view_content_shadow);
        ivMenuOfflineSave = (ImageView) findViewById(R.id.iv_menu_offline_save);
        ivMenuBookmark = (ImageView) findViewById(R.id.iv_menu_bookmark);
        ivMenuShare = (ImageView) findViewById(R.id.iv_menu_share);
        ivMenuSettings = (ImageView) findViewById(R.id.iv_menu_settings);
        bslMenu = (BottomSheetLayout) findViewById(R.id.bsl_menu);
        layoutPostContent = (LinearLayout) findViewById(R.id.layout_post_content);

        realm = Realm.getDefaultInstance();

        if (savedInstanceState == null) {
            setupFirstRun();
        }

        viewContentShadow.setVisibility(View.GONE);
        viewContentShadow.setOnClickListener(this);
        fabMenu.setOnClickListener(this);
        ivMenuBookmark.setOnClickListener(this);
        ivMenuShare.setOnClickListener(this);
        ivMenuSettings.setOnClickListener(this);
        ivMenuBookmark.setOnTouchListener(this);
        ivMenuShare.setOnTouchListener(this);
        ivMenuSettings.setOnTouchListener(this);
        flMenu.setFab(fabMenu);

        setToolbar(postItem.getTitle());
        checkIsBookmarked(postItem.getId());
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

    private void setToolbar(String title) {
        setSupportActionBar(tbTitle);
        setTitle(ContentUtility.getInstance().removeLabelFromTitle(title));
        tbTitle.setNavigationIcon(R.drawable.vector_ic_back);
        tbTitle.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupFirstRun() {
        postItem = Parcels.unwrap(getIntent().getParcelableExtra(Key.POST_ID));
        BloggerManager.getInstance().getPost(postItem.getId());
    }

    @Subscribe
    public void onPostSucess(Post post) {
        List<String> textList = ContentUtility.getInstance().wrapContent(post.getContent());
//        for (String text : textList) {
//            Log.d("Check", text);
//        }
        for (String text : textList) {
//            Log.e("Check", text);
            if (ContentUtility.getInstance().isCode(text)) {
                addCodeContent(text);
            } else if (ContentUtility.getInstance().isImage(text)) {
                addImageContent(text);
            } else if (ContentUtility.getInstance().isHeaderText(text)) {
                addHeaderContent(text);
            } else {
                addPlainTextContent("    " + text);
            }
        }
    }

    @Subscribe
    public void onPostFailure(Failure failure) {
        Log.e("Check", "onPostFailure");
    }

    private void addPlainTextContent(String plainText) {
        PlainTextPost plainTextPost = ContentUtility.getInstance().convertPlainText(plainText);
        View view = LayoutInflater.from(this).inflate(R.layout.view_post_content_plain_text, layoutPostContent, false);
        TextView tvPostContentPlainText = (TextView) view.findViewById(R.id.tv_post_content_plain_text);
        tvPostContentPlainText.setText(setSpannable(plainTextPost));
        if (plainTextPost.isLinkAvailable()) {
            tvPostContentPlainText.setMovementMethod(new LinkMovementMethod());
        }
        layoutPostContent.addView(view);
    }

    private Spannable setSpannable(PlainTextPost plainTextPost) {
        Spannable spanText = Spannable.Factory.getInstance().newSpannable(plainTextPost.getText());
        for (PlainTextPost.Highlight highlight : plainTextPost.getHighlightList()) {
            spanText.setSpan(new ForegroundColorSpan(highlight.getColor()), highlight.getStart(), highlight.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        for (PlainTextPost.Link link : plainTextPost.getLinkList()) {
            spanText.setSpan(new LinkClickable(link.getUrl(), this), link.getStart(), link.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spanText;
    }

    private void addHeaderContent(String header) {
        HeaderPost headerPost = ContentUtility.getInstance().convertHeaderPost(header);
        View view = LayoutInflater.from(this).inflate(R.layout.view_post_content_header, layoutPostContent, false);
        TextView tvPostContentHeader = (TextView) view.findViewById(R.id.tv_post_content_header);
        tvPostContentHeader.setText(headerPost.getText());
        if (headerPost.getSize() == 1) {
            tvPostContentHeader.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.default_text_size_h1));
        } else if (headerPost.getSize() == 2) {
            tvPostContentHeader.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.default_text_size_h2));
        } else if (headerPost.getSize() == 3) {
            tvPostContentHeader.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.default_text_size_h3));
        } else if (headerPost.getSize() == 4) {
            tvPostContentHeader.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.default_text_size_h4));
        }
        layoutPostContent.addView(view);
    }

    private void addCodeContent(String code) {
        CodePost codePost = ContentUtility.getInstance().convertCodePost(code);
        View view = LayoutInflater.from(this).inflate(R.layout.view_post_content_code, layoutPostContent, false);
        TextView tvPostContentPlainText = (TextView) view.findViewById(R.id.tv_post_content_code);
        tvPostContentPlainText.setText(codePost.getCode());
        layoutPostContent.addView(view);
    }

    private void addImageContent(String image) {
        final ImagePost imagePost = ContentUtility.getInstance().convertImagePost(image);
        View view = LayoutInflater.from(this).inflate(R.layout.view_post_content_image, layoutPostContent, false);
        ImageView ivPostContentPlainImage = (ImageView) view.findViewById(R.id.iv_post_content_image);
        Glide.with(this)
                .load(imagePost.getPostUrl())
                .override(500, 500)
                .thumbnail(0.2f)
                .into(ivPostContentPlainImage);
        ivPostContentPlainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(Key.KEY_FULL_URL, imagePost.getFullSizeUrl());
                openActivity(ImagePostPreviewActivity.class, bundle);
            }
        });
        ivPostContentPlainImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                copyFullUrl(imagePost.getFullSizeUrl());
                Snackbar.make(v, "Copy image URL to clipboard.", Snackbar.LENGTH_SHORT).show();
                return true;
            }

            private void copyFullUrl(String fullUrl) {
                ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Image URL", fullUrl);
                clipboard.setPrimaryClip(clip);
            }
        });
        layoutPostContent.addView(view);
    }

    @Override
    public void onLinkClick(String url) {
        Log.e("Check", "Link Click");
        ExternalBrowserUtility.getInstance().open(this, url);
    }

    @Override
    public void onClick(View v) {
        if (v == fabMenu) {
            openMenu();
        } else if (v == viewContentShadow) {
            closeMenu();
        } else if (v == ivMenuBookmark) {
            onMenuBookmarkClick();
        } else if (v == ivMenuOfflineSave) {
            onMenuOfflineSaveClick();
        } else if (v == ivMenuShare) {
            onMenuShareClick();
        } else if (v == ivMenuSettings) {
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
        } else {
            super.onBackPressed();
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
        Log.e("Check", "onMenuBookmarkClick");
        if (isBookmark()) {
            removePostFromBookmark();
        } else {
            addPostToBookmark();
        }
        closeMenu();
    }

    public void onMenuOfflineSaveClick() {
        Log.e("Check", "onMenuOfflineSaveClick");
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

    public void checkIsBookmarked(String postId) {
        final RealmResults<Bookmark> result = realm.where(Bookmark.class)
                .equalTo("postId", postId)
                .findAllAsync();
        result.addChangeListener(new RealmChangeListener() {
            @Override
            public void onChange() {
                setBookmark(result.size() > 0);
            }
        });
    }

    public void setBookmark(boolean state) {
        int drawableResourceId = (state) ? R.drawable.vector_ic_bookmark_check : R.drawable.vector_ic_bookmark_uncheck;
        ivMenuBookmark.setImageDrawable(getResources().getDrawable(drawableResourceId));
        ivMenuBookmark.setTag(drawableResourceId);
    }

    public boolean isBookmark() {
        return (int) ivMenuBookmark.getTag() == R.drawable.vector_ic_bookmark_check;
    }

    public void showSnackbar(String message) {
        Snackbar.make(tbTitle, message, Snackbar.LENGTH_SHORT).show();
    }


}
