package com.akexorcist.sleepingforless.util;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

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

    public void clearAllCache() {
        clearCache(Contextor.getContext().getCacheDir());
        clearCache(Contextor.getContext().getExternalCacheDir());
    }

    private void clearCache(File cacheDirectory) {
        try {
            if (cacheDirectory != null && cacheDirectory.isDirectory()) {
                deleteDir(cacheDirectory);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean deleteDir(File directory) {
        if (directory != null && directory.isDirectory()) {
            String[] childrenList = directory.list();
            for (String children : childrenList) {
                boolean success = deleteDir(new File(directory, children));
                if (!success) {
                    return false;
                }
            }
            return directory.delete();
        }
        return false;
    }

    public String getAllCacheSize() {
        return readableFileSize(getCacheSize(Contextor.getContext().getCacheDir()) + getCacheSize(Contextor.getContext().getExternalCacheDir()));
    }

    private long getCacheSize(File cacheDirectory) {
        long length = 0;
        for (File file : cacheDirectory.listFiles()) {
            if (file.isFile())
                length += file.length();
            else
                length += getCacheSize(file);
        }
        return length;
    }

    public String readableFileSize(long size) {
        if (size <= 0) return "0 Bytes";
        final String[] units = new String[]{"Bytes", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
