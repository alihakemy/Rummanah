package broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import helpers.NetWork;
import helpers.ProductHolder;

public class ConnectionChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (activeNetInfo != null) {
        } else if (mobNetInfo != null) {
        } else {
        }
        if (NetWork.isNetworkAvailable(context) == false && ProductHolder.getInstance().getData() != null) {
            Intent i = new Intent();
            i.setClassName("com.usmart.com.mainapp", "com.usmart.com.mainapp.Offline");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } else {
        }
    }

}
