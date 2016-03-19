package com.akexorcist.sleepingforless.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;

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

    public void copyTextToClipboard(String label, String text) {
        ClipboardManager clipboard = (android.content.ClipboardManager) Contextor.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);
    }
}
