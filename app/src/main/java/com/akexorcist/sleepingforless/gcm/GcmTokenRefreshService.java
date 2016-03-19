package com.akexorcist.sleepingforless.gcm;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Akexorcist on 3/19/2016 AD.
 */
public class GcmTokenRefreshService extends InstanceIDListenerService {
    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, GcmRegisterService.class);
        startService(intent);
    }
}
