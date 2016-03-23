package com.akexorcist.sleepingforless.view.deeplink;

import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.akexorcist.sleepingforless.analytic.EventKey;
import com.akexorcist.sleepingforless.analytic.EventTracking;
import com.akexorcist.sleepingforless.common.SFLActivity;
import com.akexorcist.sleepingforless.constant.Key;
import com.akexorcist.sleepingforless.view.post.PostByPathActivity;

@DeepLink({"http://akexorcist.com/{year}/{month}/{name}", "http://www.akexorcist.com/{year}/{month}/{name}"})
public class LinkActivity extends SFLActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        screenTracking();
        if (getIntent().getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
            Bundle bundle = getIntent().getExtras();
            String year = bundle.getString("year");
            String month = bundle.getString("month");
            String name = bundle.getString("name");
            String postPath = "/" + year + "/" + month + "/" + name;
            deepLinkContentTracking(postPath);
            openPostActivity(postPath);
        }
        finish();
    }

    private void openPostActivity(String postPath) {
        Bundle bundle = new Bundle();
        bundle.putString(Key.POST_PATH, postPath);
        openActivity(PostByPathActivity.class, bundle);
    }

    // Google Analytics
    private void screenTracking() {
        EventTracking.getInstance().addScreen(EventKey.Page.DEEP_LINK);
    }

    private void deepLinkContentTracking(String path) {
        EventTracking.getInstance().addContentTracking(EventKey.Action.OPEN_FROM_DEEP_LINK, path);
    }
}
