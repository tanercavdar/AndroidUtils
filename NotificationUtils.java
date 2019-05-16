package tr.com.bracket.aiku360.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import tr.com.bracket.aiku360.BuildConfig;
import tr.com.bracket.aiku360.R;
import tr.com.bracket.aiku360.activities.MainActivity;

public class NotificationUtils {

    private static Context _context;


    public static void init(Context context) {
        _context = context;
    }


    public static void notify(int notifyID, String channelID, String title, String text) {
        Intent activityIntent = new Intent(_context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(_context, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager nm = (NotificationManager) _context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(_context, channelID)
                .setSmallIcon(R.drawable.ic_launcher)
                .setChannelId(channelID)
                .setContentTitle(title)
                .setContentText(text)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
                .build();
        //.setAutoCancel(true)
        nm.notify(notifyID, notification);
    }

    public static void createNotifyChannel(String channelID, String channelName, String channelDescription) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) _context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel mChannel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
            mChannel.setDescription(channelDescription);
            mChannel.enableLights(true);
            nm.createNotificationChannel(mChannel);
        }
    }


    public static void openNotificationSettings(Activity activity, String channelID) {
        //activity. startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID)));
        Intent intent = new Intent();
        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            intent.setAction(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
            //if (!channelID.isEmpty()) {
            //    intent.putExtra(Settings.EXTRA_CHANNEL_ID, channelID);
            //}
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, BuildConfig.APPLICATION_ID);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            //if (!channelID.isEmpty()) {
            //    intent.putExtra(Settings.EXTRA_CHANNEL_ID, channelID);
            //}
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, BuildConfig.APPLICATION_ID);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, BuildConfig.APPLICATION_ID);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra("app_package", BuildConfig.APPLICATION_ID);
            intent.putExtra("app_uid", activity.getApplicationInfo().uid);
        }
        // else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
        //   intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        //   intent.addCategory(Intent.CATEGORY_DEFAULT);
        //   intent.setData(Uri.parse("package:" + BuildConfig.APPLICATION_ID));
        //}
        activity.startActivity(intent);
    }


}
