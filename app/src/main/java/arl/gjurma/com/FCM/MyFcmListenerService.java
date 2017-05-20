package arl.gjurma.com.FCM;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.webkit.URLUtil;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;
import java.util.Map;

import arl.gjurma.com.R;

/**
 * Created by Vishal on 14/9/16.
 */
public class MyFcmListenerService extends FirebaseMessagingService {
    String TAG = "MyFcmListenerService";
    @Override
    public void onMessageReceived(RemoteMessage message){
        String from = message.getFrom();
        Map data = message.getData();

        Log.e(TAG, "From: " + from);
        Log.e(TAG, data+"Notification Message Body: " + data.get("message"));
        sendNotification(data);
    }

    private void sendNotification(Map data) {
        String msg = (String) data.get("message");
        String title = (String) data.get("title");
        String link = (String) data.get("url1");
        Log.e(TAG,link);
        if(title==null || title.equals("")){
            title = "Birthday Wishes";
        }

        PendingIntent pendingIntent = null;

        /*Intent intent = new Intent(this, DetailNewsActivity.class);
        intent.putExtra("type",10);
        intent.putExtra("link",link);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/
        if(URLUtil.isValidUrl(link)){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }else {
            Intent intent = new Intent();
            pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }


        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(msg)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify((int) ((new Date().getTime()) % Integer.MAX_VALUE), notificationBuilder.build());
    }

}