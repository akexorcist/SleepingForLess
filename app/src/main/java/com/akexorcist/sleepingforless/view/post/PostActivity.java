package com.akexorcist.sleepingforless.view.post;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.common.SFLActivity;
import com.akexorcist.sleepingforless.constant.Key;
import com.akexorcist.sleepingforless.network.BloggerManager;
import com.akexorcist.sleepingforless.network.model.Failure;
import com.akexorcist.sleepingforless.network.model.Post;
import com.akexorcist.sleepingforless.network.model.PostList;
import com.akexorcist.sleepingforless.util.ContentUtility;
import com.akexorcist.sleepingforless.util.ExternalBrowserUtility;
import com.akexorcist.sleepingforless.view.post.model.CodePost;
import com.akexorcist.sleepingforless.view.post.model.HeaderPost;
import com.akexorcist.sleepingforless.view.post.model.ImagePost;
import com.akexorcist.sleepingforless.view.post.model.PlainTextPost;
import com.bumptech.glide.Glide;
import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

import java.util.List;

public class PostActivity extends SFLActivity implements LinkClickable.LinkClickListener {
    private Toolbar tbTitle;
    private FloatingActionButton fabSearch;
    private LinearLayout layoutPostContent;
    private PostList.Item postItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_reader);

        tbTitle = (Toolbar) findViewById(R.id.tb_title);
        fabSearch = (FloatingActionButton) findViewById(R.id.fab_search);
        layoutPostContent = (LinearLayout) findViewById(R.id.layout_post_content);

        if (savedInstanceState == null) {
            setupFirstRun();
        }

        setToolbar(postItem.getTitle());
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
                addPlainTextContent("      " + text);
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
        Glide.with(this).load(imagePost.getPostUrl()).override(500, 500).thumbnail(0.2f).into(ivPostContentPlainImage);
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
}
