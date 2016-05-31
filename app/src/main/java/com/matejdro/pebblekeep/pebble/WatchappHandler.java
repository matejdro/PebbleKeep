package com.matejdro.pebblekeep.pebble;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.StringRes;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.matejdro.pebblecommons.notification.NotificationCenterExtender;
import com.matejdro.pebblekeep.R;

public class WatchappHandler extends BroadcastReceiver
{
    public static final int SUPPORTED_PROTOCOL_VERSION = 1;

    public static final String INTENT_UPDATE_WATCHAPP = "com.matejdro.pebblekeep.UPDATE_WATCHAPP";
    public static final String INTENT_UPDATE_PHONE = "com.matejdro.pebblekeep.UPDATE_PHONE";

    // TODO add actual URLs
    public static final String WATCHAPP_URL = "https://github.com/hexaguin/keePebble";
    public static final String PHONE_URL = "https://github.com/matejdro/PebbleKeep";
    public static final String APPSTORE_URI = "pebble://appstore/PLACEHOLDER";


    public static void openPebbleAppstore(Context context)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);

        intent.setData(Uri.parse(APPSTORE_URI));
        try
        {
            context.startActivity(intent);
        }
        catch (ActivityNotFoundException e)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(R.string.opening_pebble_store_failed).setNegativeButton("OK", null).show();
        }
    }

    public static void showUpdateNotification(Context context, boolean updatePhone)
    {
        @StringRes int title = updatePhone ? R.string.phone_app_outdated : R.string.watch_app_outdated;
        @StringRes int text = updatePhone ? R.string.phone_update_prompt : R.string.watch_app_update_prompt;
        String broadcastAction = updatePhone ? INTENT_UPDATE_PHONE : INTENT_UPDATE_WATCHAPP;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context).setSmallIcon(R.mipmap.ic_launcher)
                        .setLocalOnly(true).extend(new NotificationCenterExtender().setDisableNCNotification(true))
                        .setContentTitle(context.getString(title)).setContentTitle(context.getString(text))
                        .setContentIntent(PendingIntent.getBroadcast(context, 1, new Intent(broadcastAction), PendingIntent.FLAG_CANCEL_CURRENT));
        NotificationManagerCompat.from(context).notify(1, mBuilder.build());
    }

    public static void openWebpage(Context context, String url)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);

        intent.setData(Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try
        {
            context.startActivity(intent);
        }
        catch (ActivityNotFoundException e)
        {
        }
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (INTENT_UPDATE_WATCHAPP.equals(intent.getAction()))
        {
            openWebpage(context, WATCHAPP_URL);
        }
        else if (INTENT_UPDATE_PHONE.equals(intent.getAction()))
        {
            openWebpage(context, PHONE_URL);
        }
    }
}
