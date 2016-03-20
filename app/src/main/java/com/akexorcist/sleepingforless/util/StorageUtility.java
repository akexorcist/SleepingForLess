package com.akexorcist.sleepingforless.util;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class StorageUtility {
    private static StorageUtility utility;

    public static StorageUtility getInstance() {
        if (utility == null) {
            utility = new StorageUtility();
        }
        return utility;
    }

    public void copyToDownloadsDirectory(File source, File destination) {
        try {
            InputStream inputStream = new FileInputStream(source);
            OutputStream outputStream = new FileOutputStream(destination);

            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
        }
    }

    public File getDownloadsDirectory() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    }

    public void updateImageToMediaScanner(File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        Contextor.getContext().sendBroadcast(intent);
    }
}
