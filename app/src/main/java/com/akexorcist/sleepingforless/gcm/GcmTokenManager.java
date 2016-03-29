package com.akexorcist.sleepingforless.gcm;

import android.os.Build;

import com.akexorcist.sleepingforless.config.GcmTokenPreference;
import com.akexorcist.sleepingforless.network.sfl.SleepingForLessManager;

/**
 * Created by Akexorcist on 3/30/2016 AD.
 */
public class GcmTokenManager implements SleepingForLessManager.GcmTokenCallback {
    private static GcmTokenManager gcmTokenManager;

    public static GcmTokenManager getInstance() {
        if (gcmTokenManager == null) {
            gcmTokenManager = new GcmTokenManager();
        }
        return gcmTokenManager;
    }

    public void sendTokenToServer(String token) {
        if (GcmTokenPreference.getInstance().isNewToken(token)) {
            SleepingForLessManager.getInstance().removeGcmToken(GcmTokenPreference.getInstance().getTokenId(), null);
            SleepingForLessManager.getInstance().addGcmToken(token, Build.SERIAL, this);
        }
    }

    @Override
    public void onTokenAdded(String token) {
        GcmTokenPreference.getInstance().setNewTokenSent();
    }

    @Override
    public void onTokenRemoved(String token) {

    }

    @Override
    public void onTokenFailed(String token) {

    }
}
