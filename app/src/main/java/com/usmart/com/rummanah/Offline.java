package com.usmart.com.rummanah;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import helpers.NetWork;
import helpers.OnlineHolder;

public class Offline extends Activity {

	ImageView Splach, Splach2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onStart() {
		super.onStart();
		setContentView(R.layout.activity_offline);
		// ************ Custom Action Bar *****************

		// ***********************************************
		Splach2 = (ImageView) findViewById(R.id.Splach2);
		Splach = (ImageView) findViewById(R.id.Splach);

		Splach2.setOnClickListener(v -> {
            if (NetWork.isNetworkAvailable(getApplicationContext())) {
                OnlineHolder.getInstance().setData("1");
                finish();
            }

        });

		Splach.setOnClickListener(v -> {
            if (NetWork.isNetworkAvailable(getApplicationContext())) {
                OnlineHolder.getInstance().setData("1");
                finish();
            }

        });

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

	}

}
