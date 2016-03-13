package com.akexorcist.sleepingforless.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;

import com.akexorcist.sleepingforless.R;

/**
 * Created by Akexorcist on 3/8/2016 AD.
 */
public class ExternalBrowserUtility {
    private static ExternalBrowserUtility externalBrowserUtility;

    public static ExternalBrowserUtility getInstance() {
        if (externalBrowserUtility == null) {
            externalBrowserUtility = new ExternalBrowserUtility();
        }
        return externalBrowserUtility;
    }

    private CustomTabsSession mCustomTabsSession;

    public void open(Activity activity, String url) {
        Uri fbbWebUri = Uri.parse(url);
        try {
            CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder(mCustomTabsSession)
                    .setToolbarColor(activity.getResources().getColor(R.color.colorPrimary))
                    .build();
            customTabsIntent.launchUrl(activity, fbbWebUri);

        } catch (Exception e) {
            Intent intent = new Intent(Intent.ACTION_VIEW, fbbWebUri);
            activity.startActivity(intent);
        }
    }

    public void bindService(Context context) {
        try {
            CustomTabsClient.bindCustomTabsService(context,
                    "com.android.chrome",
                    customTabsServiceConnection);
        } catch (Exception e) {
        }
    }

    public void unbindService(Context context) {
        try {
            context.unbindService(customTabsServiceConnection);
        } catch (Exception e) {
        }
    }

    private CustomTabsServiceConnection customTabsServiceConnection = new CustomTabsServiceConnection() {
        @Override
        public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {
            customTabsClient.warmup(0L);
            mCustomTabsSession = customTabsClient.newSession(null);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mCustomTabsSession = null;
        }
    };

}
