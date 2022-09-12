package notifications;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.usmart.com.rummanah.Home;
import com.usmart.com.rummanah.MainActivity;
import com.usmart.com.rummanah.Menu;
import com.usmart.com.rummanah.R;

public class GcmMessageHandler extends IntentService {

    String title;
    String mes, page;
    private Handler handler;

    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        handler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

      //  GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = "";//gcm.getMessageType(intent);

        title = extras.getString("title");
        mes = extras.getString("message");
        page = extras.getString("page");
        // showToast();
        showNotification();
        GcmBroadcastReceiver.completeWakefulIntent(intent);

    }

    public void showToast() {
        handler.post(new Runnable() {
            @SuppressWarnings("deprecation")
            public void run() {
                Toast.makeText(getApplicationContext(), mes, Toast.LENGTH_LONG).show();
                final int NOTIF_ID = 1234;

                NotificationManager notifManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Notification note = new Notification(R.mipmap.ic_launcher, title, System.currentTimeMillis());
                note.defaults |= Notification.DEFAULT_SOUND;
                note.defaults |= Notification.DEFAULT_VIBRATE;

                PendingIntent intent = PendingIntent.getActivity(getApplicationContext(), 0,
                        new Intent(getApplicationContext(), MainActivity.class), 0);

                //	note.setLatestEventInfo(getApplicationContext(), title, mes, intent);

                notifManager.notify(NOTIF_ID, note);
                // notifManager.cancel(NOTIF_ID);
            }
        });

    }

    public void showNotification2() {

        // define sound URI, the sound to be played when there's a notification
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // intent triggered, you can add other intent for other actions
        PendingIntent pIntent;

        Intent intent = new Intent(getApplicationContext(), Menu.class);
        pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);


        // this is it, we'll build the notification!
        // in the addAction method, if you don't want any icon, just set the
        // first param to 0
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Notification mNotification;

        mNotification = new Notification.Builder(this).setContentTitle(title).setContentText(mes)
                .setLargeIcon(largeIcon).setSmallIcon(R.mipmap.checked).setContentIntent(pIntent).setSound(soundUri)
                // .addAction(R.drawable.noti_icon,
                // getResources().getString(R.string.Show), pIntent)
                // .addAction(R.drawable.checked, "Option 2", pIntent)
                .build();


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // If you want to hide the notification after it was selected, do the
        // code below
        // myNotification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, mNotification);
    }

    public void showNotification() {

        // define sound URI, the sound to be played when there's a notification
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // intent triggered, you can add other intent for other actions
        PendingIntent pIntent;

        Intent intent = new Intent(getApplicationContext(), Home.class);
        pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);


        // this is it, we'll build the notification!
        // in the addAction method, if you don't want any icon, just set the
        // first param to 0
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Notification mNotification;

        mNotification = new Notification.Builder(this).setContentTitle(title).setContentText(mes)
                .setLargeIcon(largeIcon).setSmallIcon(R.mipmap.checked3).setContentIntent(pIntent).setSound(soundUri)
                // .addAction(R.drawable.noti_icon,
                // getResources().getString(R.string.Show), pIntent)
                // .addAction(R.drawable.checked, "Option 2", pIntent)
                .build();


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // If you want to hide the notification after it was selected, do the
        // code below
        // myNotification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, mNotification);
    }
}