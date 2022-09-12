package helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import broadcast.ConnectionChangeReceiver;

public class NetWork {

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	public static void gotoError(Activity act){
		ConnectionChangeReceiver myReceiver = new ConnectionChangeReceiver();
		IntentFilter intentFilter = new IntentFilter("broadcast.ConnectionChangeReceiver");
		act.registerReceiver(myReceiver, intentFilter);

		Intent intent = new Intent("broadcast.ConnectionChangeReceiver");

		act.sendBroadcast(intent);
	}

}
