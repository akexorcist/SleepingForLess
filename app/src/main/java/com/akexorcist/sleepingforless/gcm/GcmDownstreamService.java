package com.akexorcist.sleepingforless.gcm;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by Akexorcist on 3/19/2016 AD.
 */
public class GcmDownstreamService extends GcmListenerService {
    private static final String TAG = "DcmDownstreamService";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        // TODO Do something here
    }
}
