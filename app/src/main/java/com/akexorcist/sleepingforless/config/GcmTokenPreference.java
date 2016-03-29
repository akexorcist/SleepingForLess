package com.akexorcist.sleepingforless.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.akexorcist.sleepingforless.util.Contextor;

/**
 * Created by Akexorcist on 3/18/2016 AD.
 */
public class GcmTokenPreference {
    private static final String GCM_TOKEN_PREFERENCE = "gcm_token_preference";
    private static final String KEY_TOKEN_ID = "token_id";
    private static final String KEY_TOKEN_SENT = "token_sent";

    private static GcmTokenPreference gcmTokenPreference;

    public static GcmTokenPreference getInstance() {
        if (gcmTokenPreference == null) {
            gcmTokenPreference = new GcmTokenPreference();
        }
        return gcmTokenPreference;
    }

    private SharedPreferences getPreference() {
        return Contextor.getContext().getSharedPreferences(GCM_TOKEN_PREFERENCE, Context.MODE_PRIVATE);
    }

    public boolean isNewToken(String tokenId) {
        boolean isNewToken = !tokenId.equalsIgnoreCase(getTokenId());
        if (isNewToken) {
            clearNewTokenSent();
        }
        return isNewToken;
    }

    public boolean isNewTokenSent() {
        return getPreference().getBoolean(KEY_TOKEN_SENT, false);
    }

    public void setNewTokenSent() {
        getPreference()
                .edit()
                .putBoolean(KEY_TOKEN_SENT, true)
                .apply();
    }

    public void clearNewTokenSent() {
        getPreference()
                .edit()
                .putBoolean(KEY_TOKEN_SENT, false)
                .apply();
    }

    public void setTokenId(String tokenId) {
        getPreference()
                .edit()
                .putString(KEY_TOKEN_ID, tokenId)
                .apply();
    }

    public String getTokenId() {
        return getPreference().getString(KEY_TOKEN_ID, "");
    }
}
