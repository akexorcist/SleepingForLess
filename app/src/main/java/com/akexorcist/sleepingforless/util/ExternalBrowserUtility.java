package com.akexorcist.sleepingforless.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.util.Log;

import com.akexorcist.sleepingforless.R;

import java.util.List;

/**
 * Created by Akexorcist on 3/8/2016 AD.
 */
public class ExternalBrowserUtility {
    private static final String SERVICE_ACTION = "android.support.customtabs.action.CustomTabsService";
    private static final String CHROME_PACKAGE = "com.android.chrome";

    private static ExternalBrowserUtility externalBrowserUtility;

    public static ExternalBrowserUtility getInstance() {
        if (externalBrowserUtility == null) {
            externalBrowserUtility = new ExternalBrowserUtility();
        }
        return externalBrowserUtility;
    }

    private CustomTabsSession mCustomTabsSession;

    public void open(Activity activity, String url) {
        Uri uri = Uri.parse(url);
        Log.d("Check", "open: " + url);
        if (isChromeCustomTabsSupported() &&
                (!url.startsWith("http://www.akexorcist.com") &&
                        !url.startsWith("http://akexorcist.com"))) {
            CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder(mCustomTabsSession)
                    .setToolbarColor(activity.getResources().getColor(R.color.colorPrimary))
                    .setSecondaryToolbarColor(activity.getResources().getColor(R.color.colorPrimary))
                    .build();
            customTabsIntent.launchUrl(activity, uri);
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
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

    private boolean isChromeCustomTabsSupported() {
        Intent serviceIntent = new Intent(SERVICE_ACTION);
        serviceIntent.setPackage(CHROME_PACKAGE);
        List<ResolveInfo> resolveInfoList = Contextor.getContext().getPackageManager().queryIntentServices(serviceIntent, 0);
        return !(resolveInfoList == null || resolveInfoList.isEmpty());
    }

}
