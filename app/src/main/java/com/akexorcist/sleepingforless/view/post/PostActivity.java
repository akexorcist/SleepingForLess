package com.akexorcist.sleepingforless.view.post;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.common.SFLActivity;
import com.akexorcist.sleepingforless.common.SFLDraggerActivity;
import com.akexorcist.sleepingforless.constant.Key;
import com.akexorcist.sleepingforless.network.BloggerManager;
import com.akexorcist.sleepingforless.network.model.Failure;
import com.akexorcist.sleepingforless.network.model.Post;
import com.akexorcist.sleepingforless.network.model.PostList;
import com.akexorcist.sleepingforless.util.ContentUtility;
import com.akexorcist.sleepingforless.view.post.model.HeaderPost;
import com.akexorcist.sleepingforless.view.post.model.ImagePost;
import com.bumptech.glide.Glide;
import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

import java.util.List;

public class PostActivity extends SFLActivity {
    private LinearLayout layoutPostContent;
    private PostList.Item postItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_reader);

        layoutPostContent = (LinearLayout) findViewById(R.id.layout_post_content);

        if (savedInstanceState == null) {
            setupFirstRun();
        }
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
        View view = LayoutInflater.from(this).inflate(R.layout.view_post_content_plain_text, layoutPostContent, false);
        TextView tvPostContentPlainText = (TextView) view.findViewById(R.id.tv_post_content_plain_text);
        tvPostContentPlainText.setText(plainText);
        layoutPostContent.addView(view);
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

    private void addCodeContent(String plainText) {
        View view = LayoutInflater.from(this).inflate(R.layout.view_post_content_code, layoutPostContent, false);
        TextView tvPostContentPlainText = (TextView) view.findViewById(R.id.tv_post_content_code);
        tvPostContentPlainText.setText(plainText);
        layoutPostContent.addView(view);
    }

    private void addImageContent(String image) {
        ImagePost imagePost = ContentUtility.getInstance().convertImagePost(image);
        View view = LayoutInflater.from(this).inflate(R.layout.view_post_content_image, layoutPostContent, false);
        ImageView ivPostContentPlainImage = (ImageView) view.findViewById(R.id.iv_post_content_image);
        Glide.with(this).load(imagePost.getPostUrl()).override(500, 500).into(ivPostContentPlainImage);
        layoutPostContent.addView(view);
    }
}
