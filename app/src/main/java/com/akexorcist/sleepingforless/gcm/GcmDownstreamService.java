package com.akexorcist.sleepingforless.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.constant.Key;
import com.akexorcist.sleepingforless.network.blogger.model.PostList;
import com.akexorcist.sleepingforless.util.ContentUtility;
import com.akexorcist.sleepingforless.view.bookmark.BookmarkActivity;
import com.akexorcist.sleepingforless.view.post.PostByIdActivity;
import com.google.android.gms.gcm.GcmListenerService;

import org.parceler.Parcels;

/**
 * Created by Akexorcist on 3/19/2016 AD.
 */
public class GcmDownstreamService extends GcmListenerService {
    public static final String KEY_TITLE = "title";
    public static final String KEY_ID = "id";
    public static final String KEY_URL = "url";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String title = data.getString(KEY_TITLE);
        String id = data.getString(KEY_ID);
        String url = data.getString(KEY_URL);
        showNotification(new PostList.Item(title, id, url));
    }

    private void showNotification(PostList.Item postItem) {
        Intent intent = new Intent(this, PostByIdActivity.class);
        intent.putExtra(Key.POST_ITEM, Parcels.wrap(postItem));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_push_notification)
                .setAutoCancel(true)
                .setContentTitle(getString(R.string.title_new_blog_post_available))
                .setContentText(ContentUtility.getInstance().removeLabelFromTitle(postItem.getTitle()))
                .setContentIntent(pendingIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}
