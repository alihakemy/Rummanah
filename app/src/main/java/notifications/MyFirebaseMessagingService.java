package notifications;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import constants.Values;
import helpers.FaceIdHolder;
import helpers.LangHolder;
import helpers.LoginHolder;
import helpers.ProductHolder;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.usmart.com.rummanah.Home;
import com.usmart.com.rummanah.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "TestApp_Notifi";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        mainData();
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData().get("message"));

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            // Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Log.d(TAG, "Message Notification " + remoteMessage + "");


        // Log.d(TAG, "Message Image " + remoteMessage.getNotification().getTitle() + "");
        sendNotification(remoteMessage);

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]


    // [START on_new_token]

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "MyFirebaseMsgService Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }
    // [END on_new_token]

    /**
     * Schedule async work using WorkManager.
     */
    private void scheduleJob() {
        // [START dispatch_job]
		/*OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
				.build();
		WorkManager.getInstance().beginWith(work).enqueue();*/
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }


    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    @SuppressLint("ResourceAsColor")
    private void sendNotification(RemoteMessage messageBody) {
        // String Page = messageBody.get("page").toString();
        String Page = "Home";
        Intent intent;
        if (Page.equals("product")) {
            intent = new Intent(getApplicationContext(), Home.class);
            // intent.putExtra("ID", Integer.parseInt(messageBody.get("main_id").toString()));
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        } else {
            intent = new Intent(getApplicationContext(), Home.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }


        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,
                PendingIntent.FLAG_IMMUTABLE);

        String channelId = getString(R.string.default_notification_channel_id);
        String imageUri = messageBody.getNotification().getImageUrl() + "";
        Log.d(TAG, "Message Image " + imageUri + "");
        Bitmap bitmap = getBitmapfromUrl(imageUri);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        @SuppressLint("ResourceAsColor") NotificationCompat.Builder notificationBuilder;
        if (imageUri.isEmpty()) {
            Log.d(TAG, "Message Image " + messageBody.getNotification().getImageUrl() + "");
            notificationBuilder =
                    new NotificationCompat.Builder(getApplicationContext(), channelId)
                            .setSmallIcon(R.mipmap.checked4)
                            .setContentTitle(messageBody.getNotification().getTitle() + "")
                            .setContentText(messageBody.getNotification().getBody() + "")
                            .setColor(R.color.colorAccent)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);
        } else {
            Log.d(TAG, "Message Title " + messageBody.getNotification().getTitle() + "");
            notificationBuilder =
                    new NotificationCompat.Builder(getApplicationContext(), channelId)
                            .setSmallIcon(R.mipmap.checked4)
                            .setContentTitle(messageBody.getNotification().getTitle() + "")
                            .setContentText(messageBody.getNotification().getBody() + "")
                            .setStyle(new NotificationCompat.BigPictureStyle()
                                    .bigPicture(bitmap))
                            .setColor(R.color.colorAccent)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);
        }


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }

    public void mainData() {
        String fileName = Values.SharedPreferences_FileName;
        SharedPreferences Login;
        String status, Userid;

        ProductHolder.getInstance().setData(Integer.toString(0));
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getBaseContext()));
        if (Locale.getDefault().getISO3Language().equals("ara")) {
            LangHolder.getInstance().setData("ar");
        } else {
            LangHolder.getInstance().setData("en");
            // LangHolder.getInstance().setData("ar");
        }


        Login = getSharedPreferences(fileName, 0);

        status = Login.getString("isLogin", "logout");
        Userid = Login.getString("UserID", "0");

        if (status.equals("logout")) {
            LoginHolder.getInstance().setData("logout");
            FaceIdHolder.getInstance().setData("0");
            SharedPreferences.Editor editor = Login.edit();
            editor.putString("isLogin", "logout");
            editor.putString("UserID", "0");
            editor.commit();

            //	getDeviceToken(FaceIdHolder.getInstance().getData(), FirebaseInstanceId.getInstance().getToken());

        } else {
            LoginHolder.getInstance().setData("login");
            FaceIdHolder.getInstance().setData(Userid);
            //	getDeviceToken(FaceIdHolder.getInstance().getData(), FirebaseInstanceId.getInstance().getToken());
        }
    }

}