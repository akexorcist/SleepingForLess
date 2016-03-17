package com.akexorcist.sleepingforless.util;

import android.graphics.Bitmap;
import android.util.Log;

import com.akexorcist.sleepingforless.view.post.model.BasePost;
import com.akexorcist.sleepingforless.view.post.model.ImagePost;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Akexorcist on 3/17/2016 AD.
 */
public class BookmarkManager {

    private static BookmarkManager bookmarkManager;

    public static BookmarkManager getInstance() {
        if (bookmarkManager == null) {
            bookmarkManager = new BookmarkManager();
        }
        return bookmarkManager;
    }

    private int bookmarkImageCount = 0;

    public void downloadImageToBookmark(final String postId, List<BasePost> postList, final DownloadCallback callback) {
        bookmarkImageCount = 0;
        for (BasePost basePost : postList) {
            if (basePost instanceof ImagePost) {
                bookmarkImageCount++;
                ImagePost imagePost = (ImagePost) basePost;
                final String url = imagePost.getPostUrl();
                final long startTime = System.currentTimeMillis();
                BookmarkManager.getInstance().downloadImage(url, new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        saveBookmarkImageBitmap(resource, postId, url);
                        Log.e("Check", "Duration  : " + (System.currentTimeMillis() - startTime) + " ms");
                        bookmarkImageCount--;
                        if (bookmarkImageCount <= 0 && callback != null) {
                            callback.onDownloadSuccess();
                        }
                    }
                });
            }
        }
    }

    public void removeBookmarkImageFile(String bookmarkPostId) {
        String internalDirectoryPath = Contextor.getContext().getFilesDir().getAbsolutePath();
        File bookmarkDirectory = new File(internalDirectoryPath + "/Bookmark/" + bookmarkPostId);
        if (bookmarkDirectory.exists()) {
            deleteRecursive(bookmarkDirectory);
        }
    }

    private void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }
        fileOrDirectory.delete();
    }

    private void downloadImage(String url, SimpleTarget<Bitmap> targetCallback) {
        Glide.with(Contextor.getContext()).load(url).asBitmap().into(targetCallback);
    }

    public File getBookmarkImageFile(String bookmarkPostId, String filename) {
        String internalDirectoryPath = Contextor.getContext().getFilesDir().getAbsolutePath();
        return new File(internalDirectoryPath + "/Bookmark/" + bookmarkPostId + "/" + convertFilename(filename));
    }

    private String saveBookmarkImageBitmap(Bitmap bitmap, String postId, String filename) {
        try {
            File file = createBookmarkImageFile(postId, filename);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String createBookmarkDirectory() {
        String bookmarkPath = Contextor.getContext().getFilesDir().getAbsoluteFile() + "/Bookmark";
        File bookmarkDirectory = new File(bookmarkPath);
        bookmarkDirectory.mkdir();
        return bookmarkDirectory.getAbsolutePath();
    }

    private File createBookmarkImageFile(String postId, String filename) {
        new File(createBookmarkDirectory() + "/" + postId).mkdir();
        return new File(createBookmarkDirectory() + "/" + postId + "/" + convertFilename(filename));
    }

    private String convertFilename(String filename) {
        return filename.replaceAll("[:/.]", "_")
                .replaceAll("(png$|gif$|jpg$|jpeg$|bmp$|PNG$|GIF$|JPG$|JPEG$|BMP$)", "");
    }

    public interface DownloadCallback {
        void onDownloadSuccess();
    }

}
