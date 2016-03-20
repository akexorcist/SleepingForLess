package com.akexorcist.sleepingforless.util;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class Utility {
    private static Utility utility;

    public static Utility getInstance() {
        if (utility == null) {
            utility = new Utility();
        }
        return utility;
    }

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public void copyTextToClipboard(String label, String text) {
        ClipboardManager clipboard = (android.content.ClipboardManager) Contextor.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);
    }

    public boolean checkPlayServices(Activity activity) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(activity, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            return false;
        }
        return true;
    }

    public int getDrawableResource(Context context, String filename) {
        return getResourceByFilename(context, "drawable", filename);
    }

    public int getMipmapResource(Context context, String filename) {
        return getResourceByFilename(context, "mipmap", filename);
    }

    private int getResourceByFilename(Context context, String resourceType, String filename) {
        return context.getResources().getIdentifier(filename, resourceType, context.getPackageName());
    }
}
