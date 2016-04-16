package com.akexorcist.sleepingforless.view.post;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.common.SFLActivity;
import com.akexorcist.sleepingforless.constant.Key;
import com.akexorcist.sleepingforless.network.blogger.BloggerManager;
import com.akexorcist.sleepingforless.network.blogger.model.Failure;
import com.akexorcist.sleepingforless.network.blogger.model.Post;
import com.akexorcist.sleepingforless.network.blogger.model.PostList;
import com.akexorcist.sleepingforless.util.content.ContentUtility;
import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class DebugPostActivity extends SFLActivity {
    @Bind(R.id.layout_post_content)
    LinearLayout layoutPostContent;

    @Bind(R.id.cb_plain_text)
    CheckBox cbPlainText;

    @Bind(R.id.cb_header)
    CheckBox cbHeader;

    @Bind(R.id.cb_image)
    CheckBox cbImage;

    @Bind(R.id.cb_code)
    CheckBox cbCode;

    @Bind(R.id.cb_other)
    CheckBox cbOther;

    PostList.Item postItem;
    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_debug);

        if (savedInstanceState == null) {
            setupFirstRun();
        }
        ButterKnife.bind(this);
    }

    private void setupFirstRun() {
        postItem = Parcels.unwrap(getIntent().getParcelableExtra(Key.POST_ITEM));
        BloggerManager.getInstance().getPostById(postItem.getId());
    }

    @Subscribe
    public void onPostSucess(Post post) {
        this.post = post;
        setPostContent(post);
    }

    @Subscribe
    public void onPostFailure(Failure failure) {
        Log.e("Check", "onPostFailure");
    }

    private void setPostContent(Post post) {
        layoutPostContent.removeAllViews();
        List<String> textList = ContentUtility.getInstance().wrapContent(post.getContent());
        for (String text : textList) {
            if (ContentUtility.getInstance().isCode(text)) {
                if (cbCode.isChecked()) {
                    addContent(text, Color.parseColor("#e62150"));
                }
            } else if (ContentUtility.getInstance().isImage(text)) {
                if (cbImage.isChecked()) {
                    addContent(text, Color.parseColor("#1d6292"));
                }
            } else if (ContentUtility.getInstance().isHeaderText(text)) {
                if (cbHeader.isChecked()) {
                    addContent(text, Color.parseColor("#4b820b"));
                }
            } else if (ContentUtility.getInstance().isPlainText(text)) {
                if (cbPlainText.isChecked()) {
                    addContent("      " + text, Color.parseColor("#5a6a6c"));
                }
            } else {
                if (cbOther.isChecked()) {
                    addContent(text, Color.parseColor("#b3b3b3"));
                }
            }
        }
    }

    private void addContent(String plainText, int textColor) {
        View view = LayoutInflater.from(this).inflate(R.layout.view_post_content_plain_text, layoutPostContent, false);
        TextView tvPostContentPlainText = (TextView) view.findViewById(R.id.tv_post_content_plain_text);
        tvPostContentPlainText.setText(plainText);
        tvPostContentPlainText.setTextColor(textColor);
        layoutPostContent.addView(view);
    }

    @OnCheckedChanged({R.id.cb_plain_text, R.id.cb_header, R.id.cb_image, R.id.cb_code, R.id.cb_other})
    public void contentTypeChanged() {
        setPostContent(post);
    }
}
