package com.usmart.com.rummanah;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Locale;

import constants.Values;

public class Explane2 extends Activity {
    Activity activity = Explane2.this;
    private static final String TAG = Explane2.class.getSimpleName();
    TextView Welcome, MainText, Title, btn_Skip, btn_Next;
    Typeface fontMedim, fontLight;
    String lang, DeviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //*******************************************************
        Resources res = getApplicationContext().getResources();
        Locale locale = new Locale(Values.SharedPreferences_FileNameLangSelect);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //*******************************************************
        setContentView(R.layout.activity_explan2);
        fontMedim = Typeface.createFromAsset(activity.getAssets(), "fonts/GE_SS_Two_Medium.otf");
        fontLight = Typeface.createFromAsset(activity.getAssets(), "fonts/GE_SS_Two_Light.otf");
        SharedPreferences splash = getSharedPreferences("Splash", 0);
        SharedPreferences.Editor editor = splash.edit();
        editor.putString("isLogin", "login");
        lang = Values.SharedPreferences_FileNameLangSelect;
        Log.d(TAG, "value of selected language: " + Values.SharedPreferences_FileNameLangSelect);

        Title = findViewById(R.id.Title);
        MainText = findViewById(R.id.MainText);
        btn_Next = findViewById(R.id.btn_Next);
        btn_Skip = findViewById(R.id.btn_Skip);

        Title.setTypeface(fontMedim);
        MainText.setTypeface(fontLight);
        btn_Skip.setTypeface(fontLight);
        btn_Next.setTypeface(fontMedim);

        btn_Next.setOnClickListener(v -> {
            Intent intent = new Intent(activity, Explane3.class);
            finish();
            startActivity(intent);
            overridePendingTransition(R.anim.fadein_fast, R.anim.fadeout_fast);
            splash.edit().putString("Splash","Splash").apply();
        });
        btn_Skip.setOnClickListener(v -> {
            Intent intent = new Intent(activity, Home.class);
            finish();
            startActivity(intent);
            overridePendingTransition(R.anim.fadein_fast, R.anim.fadeout_fast);
            splash.edit().putString("Splash","Splash").apply();

        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        finish();
        startActivity(intent);
        overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
    }

    public void gotoBack(View v) {
        super.onBackPressed();
    }

}
