package helpers;


import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.usmart.com.rummanah.MainActivity;
import com.usmart.com.rummanah.R;


public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
	private final Activity myContext;
	private int page;

	public ExceptionHandler(Activity context, int x) {
		myContext = context;
		page = x;
	}

	public void uncaughtException(Thread thread, Throwable exception) {

		Toast.makeText(myContext, R.string.UnKnownError, Toast.LENGTH_LONG).show();
		switch (page) {
		case 1:
			Toast.makeText(myContext.getApplicationContext(), R.string.Error, Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(myContext, MainActivity.class);
			myContext.startActivity(intent);
			break;

		default:
			Toast.makeText(myContext.getApplicationContext(), R.string.Error, Toast.LENGTH_SHORT).show();
			Intent intent2 = new Intent(myContext, MainActivity.class);
			myContext.startActivity(intent2);
			break;
		}

		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(10);
	}

}