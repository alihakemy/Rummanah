package com.usmart.com.rummanah;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import constants.Values;

public class Explane4 extends Activity {
    Activity activity = Explane4.this;
    Button btn_Next ;
    TextView MainText , Title;
    Typeface fontMedim , fontLight;



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
        setContentView(R.layout.activity_explan4);
        fontMedim = Typeface.createFromAsset(activity.getAssets(), "fonts/GE_SS_Two_Medium.otf");
        fontLight = Typeface.createFromAsset(activity.getAssets(), "fonts/GE_SS_Two_Light.otf");

        Title = findViewById(R.id.Title);
        MainText = findViewById(R.id.MainText);
        btn_Next = findViewById(R.id.btn_Next);


        Title.setTypeface(fontMedim);
        MainText.setTypeface(fontLight);
        btn_Next.setTypeface(fontMedim);

        btn_Next.setOnClickListener(v -> {
            Intent intent = new Intent(activity, Home.class);
            finish();
            startActivity(intent);
            overridePendingTransition(R.anim.fadein_fast, R.anim.fadeout_fast);
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
