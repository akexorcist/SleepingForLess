package com.akexorcist.sleepingforless.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.config.SettingsPreference;
import com.akexorcist.sleepingforless.constant.Key;
import com.akexorcist.sleepingforless.network.blogger.model.PostList;
import com.akexorcist.sleepingforless.util.content.ContentUtility;
import com.akexorcist.sleepingforless.util.Contextor;
import com.akexorcist.sleepingforless.util.Utility;
import com.akexorcist.sleepingforless.view.message.MessageActivity;
import com.akexorcist.sleepingforless.view.post.PostByIdActivity;
import com.google.android.gms.gcm.GcmListenerService;

import org.parceler.Parcels;

import java.util.Random;

/**
 * Created by Akexorcist on 3/19/2016 AD.
 */
public class GcmDownstreamService extends GcmListenerService {
    public static final String KEY_TITLE = "title";
    public static final String KEY_ID = "id";
    public static final String KEY_URL = "url";
    public static final String KEY_ICON = "icon";
    public static final String KEY_TYPE = "type";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_CONTENT = "content";

    public static final String TYPE_NOTIFICATION = "notification";
    public static final String TYPE_MESSAGE_WITH_DIALOG = "message_with_dialog";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String type = data.getString(KEY_TYPE);

        if (type != null &&
                type.equalsIgnoreCase(TYPE_NOTIFICATION) &&
                SettingsPreference.getInstance().isNotificationEnable()) {
            createNotification(data);
        } else if (type != null &&
                type.equalsIgnoreCase(TYPE_MESSAGE_WITH_DIALOG)) {
            createMessageWithDialog(data);
        }
    }

    private void createNotification(Bundle data) {
        String title = data.getString(KEY_TITLE);
        String id = data.getString(KEY_ID);
        String url = data.getString(KEY_URL);
        String icon = data.getString(KEY_ICON);
        showNotification(new PostList.Item(title, id, url), icon);
    }

    private void createMessageWithDialog(Bundle data) {
        String title = data.getString(KEY_TITLE);
        String message = data.getString(KEY_MESSAGE);
        String content = data.getString(KEY_CONTENT);
        showMessage(title, message, content);
    }

    private void showNotification(PostList.Item postItem, String icon) {
        Intent intent = new Intent(this, PostByIdActivity.class);
        intent.putExtra(Key.POST_ITEM, Parcels.wrap(postItem));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(getString(R.string.title_new_blog_post_available))
                .setContentText(ContentUtility.getInstance().removeLabelFromTitle(postItem.getTitle()))
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_small_push_notification)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        int iconResourceId;
        if (icon != null && !icon.isEmpty()) {
            iconResourceId = Utility.getInstance().getMipmapResource(Contextor.getContext(), icon);
        } else {
            iconResourceId = randomNotificationIcon();
        }
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), iconResourceId);
        builder.setLargeIcon(bitmap);
        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(new Random().nextInt(100000), notification);
    }

    private void showMessage(String title, String message, String content) {
        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra(MessageActivity.KEY_MESSAGE, content);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_small_push_notification)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_push_notification_02);
        builder.setLargeIcon(bitmap);
        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(new Random().nextInt(100000), notification);
    }

    private int randomNotificationIcon() {
        int[] iconList = {
                R.mipmap.ic_push_notification_01, R.mipmap.ic_push_notification_02,
                R.mipmap.ic_push_notification_03, R.mipmap.ic_push_notification_04,
                R.mipmap.ic_push_notification_05, R.mipmap.ic_push_notification_06,
                R.mipmap.ic_push_notification_07, R.mipmap.ic_push_notification_08,
                R.mipmap.ic_push_notification_09, R.mipmap.ic_push_notification_10,
                R.mipmap.ic_push_notification_11, R.mipmap.ic_push_notification_12,
                R.mipmap.ic_push_notification_13, R.mipmap.ic_push_notification_14,
                R.mipmap.ic_push_notification_15, R.mipmap.ic_push_notification_16,
                R.mipmap.ic_push_notification_17, R.mipmap.ic_push_notification_18,
                R.mipmap.ic_push_notification_19, R.mipmap.ic_push_notification_20,
                R.mipmap.ic_push_notification_21, R.mipmap.ic_push_notification_22
        };
        int index = new Random().nextInt(iconList.length);
        return iconList[index];
    }
}
