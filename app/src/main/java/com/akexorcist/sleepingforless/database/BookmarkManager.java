package com.akexorcist.sleepingforless.database;

import android.graphics.Bitmap;

import com.akexorcist.sleepingforless.network.blogger.model.Post;
import com.akexorcist.sleepingforless.util.Contextor;
import com.akexorcist.sleepingforless.view.bookmark.model.Bookmark;
import com.akexorcist.sleepingforless.view.bookmark.model.BookmarkImage;
import com.akexorcist.sleepingforless.view.bookmark.model.BookmarkLabel;
import com.akexorcist.sleepingforless.view.post.model.BasePost;
import com.akexorcist.sleepingforless.view.post.model.ImagePost;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

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
                BookmarkManager.getInstance().downloadImage(url, new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        saveBookmarkImageBitmap(resource, postId, url);
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
            if (file.exists()) {
                file.delete();
            }
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
        if (!bookmarkDirectory.exists()) {
            bookmarkDirectory.mkdir();
        }
        return bookmarkDirectory.getAbsolutePath();
    }

    private File createBookmarkImageFile(String postId, String filename) {
        File postDirectory = new File(createBookmarkDirectory() + "/" + postId);
        if (!postDirectory.exists()) {
            postDirectory.mkdir();
        }
        return new File(createBookmarkDirectory() + "/" + postId + "/" + convertFilename(filename));
    }

    private String convertFilename(String filename) {
        return filename.replaceAll("[:/.]", "_")
                .replaceAll("(png$|gif$|jpg$|jpeg$|bmp$|PNG$|GIF$|JPG$|JPEG$|BMP$)", "");
    }

    public boolean isBookmark(String postId) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<BookmarkRealm> result = realm.where(BookmarkRealm.class)
                .equalTo("postId", postId)
                .findAll();
        boolean isBookmark = result != null && result.size() > 0;
        realm.close();
        return isBookmark;
    }

    public int getBookmarkCount() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<BookmarkRealm> result = realm.where(BookmarkRealm.class)
                .findAll();
        int bookmarkCount = (result != null) ? result.size() : 0;
        realm.close();
        return bookmarkCount;
    }

    public void removeBookmark(String postId) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<BookmarkRealm> result = realm.where(BookmarkRealm.class)
                .equalTo("postId", postId)
                .findAll();
        result.clear();
        realm.commitTransaction();
        realm.close();
    }

    public void addBookmark(Post post) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        BookmarkRealm postOffline = realm.createObject(BookmarkRealm.class);
        postOffline.setPostId(post.getId());
        postOffline.setTitle(post.getTitle());
        postOffline.setContent(post.getContent());
        postOffline.setPublished(post.getPublished());
        postOffline.setUpdated(post.getUpdated());
        postOffline.setUrl(post.getUrl());
        RealmList<BookmarkLabelRealm> labelList = new RealmList<>();
        if (post.getLabels() != null) {
            for (String label : post.getLabels()) {
                BookmarkLabelRealm bookmarkLabelRealm = realm.createObject(BookmarkLabelRealm.class);
                bookmarkLabelRealm.setLabel(label);
                labelList.add(bookmarkLabelRealm);
            }
        }
        postOffline.setLabelList(labelList);
        realm.copyToRealm(postOffline);
        realm.commitTransaction();
        realm.close();
    }

    public List<Bookmark> getBookmarkList() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        final RealmResults<BookmarkRealm> result = realm.where(BookmarkRealm.class)
                .findAll();
        result.sort("published", Sort.DESCENDING);
        List<Bookmark> bookmarkList = new ArrayList<>();
        for (BookmarkRealm bookmarkRealm : result) {
            bookmarkList.add(convertBookmark(bookmarkRealm));
        }
        realm.commitTransaction();
        realm.close();
        return bookmarkList;
    }

    public Bookmark convertBookmark(BookmarkRealm bookmarkRealm) {
        Bookmark bookmark = new Bookmark();
        bookmark.setPostId(bookmarkRealm.getPostId());
        bookmark.setTitle(bookmarkRealm.getTitle());
        bookmark.setUrl(bookmarkRealm.getUrl());
        bookmark.setContent(bookmarkRealm.getContent());
        bookmark.setPublished(bookmarkRealm.getPublished());
        bookmark.setUpdated(bookmarkRealm.getUpdated());
        List<BookmarkImage> bookmarkImageList = new ArrayList<>();
        List<BookmarkLabel> bookmarkLabelList = new ArrayList<>();
        for (BookmarkLabelRealm bookmarkLabelRealm : bookmarkRealm.getLabelList()) {
            bookmarkLabelList.add(new BookmarkLabel().setLabel(bookmarkLabelRealm.getLabel()));
        }
        bookmark.setImageList(bookmarkImageList);
        bookmark.setLabelList(bookmarkLabelList);
        return bookmark;
    }

    public interface DownloadCallback {
        void onDownloadSuccess();
    }

}
