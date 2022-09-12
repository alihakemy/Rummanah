package com.usmart.com.rummanah;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;

import constants.Values;
import dataInLists.DataInReservation;
import helpers.LangHolder;
import helpers.NetWork;
import helpers.OnlineHolder;

public class FinishOrderOnline extends Activity {

    Activity activity = FinishOrderOnline.this;
    ImageView HideAll;
    ProgressBar prog;
    DataInReservation d = new DataInReservation();
    TextView Price, Price_Curr, Finish;
    Button btn_Continue ;
    ImageView CancelAll ;
    String lang;
    Typeface fontMedim, fontLight, fontUltra, fontAvenir55;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //  Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(activity, 2));
        //*******************************************************
        Resources activityRes = getResources();
        Configuration activityConf = activityRes.getConfiguration();
        Locale newLocale = new Locale(Values.SharedPreferences_FileNameLangSelect);
        activityConf.setLocale(newLocale);
        activityRes.updateConfiguration(activityConf, activityRes.getDisplayMetrics());

        Resources applicationRes = getApplicationContext().getResources();
        Configuration applicationConf = applicationRes.getConfiguration();
        applicationConf.setLocale(newLocale);
        applicationRes.updateConfiguration(applicationConf,
                applicationRes.getDisplayMetrics());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // **********************************************************
        setContentView(R.layout.activity_finish);
        /*if (LangHolder.getInstance().getData().equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }*/
        lang = getSharedPreferences(Values.SharedPreferences_FileNameLangSelect, 0)
                .getString(Values.SharedPreferences_FileNameLangSelect, null);
        lang = Values.SharedPreferences_FileNameLangSelect;
        // **********************************************************
        d = (DataInReservation) getIntent().getExtras().get("ResData");

        fontMedim = Typeface.createFromAsset(activity.getAssets(), "fonts/GE_SS_Two_Medium.otf");
        fontLight = Typeface.createFromAsset(activity.getAssets(), "fonts/GE_SS_Two_Light.otf");
        fontUltra = Typeface.createFromAsset(activity.getAssets(), "fonts/ArbFONTS-GESSTextUltraLight-UltraLight.otf");
        fontAvenir55 = Typeface.createFromAsset(activity.getAssets(), "fonts/avenir-lt-std-55-roman.otf");


        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);

        btn_Continue = findViewById(R.id.btn_Continue);
        CancelAll = findViewById(R.id.CancelAll);
        Finish = findViewById(R.id.tv_Finish);
        Price = findViewById(R.id.tv_Price);
        Price_Curr = findViewById(R.id.tv_Price_Curr);

        Price.setVisibility(View.GONE);
        Price_Curr.setVisibility(View.GONE);


        btn_Continue.setTypeface(fontLight);
        Finish.setTypeface(fontLight);
        Price.setTypeface(fontAvenir55);
        Price_Curr.setTypeface(fontLight);

        btn_Continue.setOnClickListener(v -> startActivity(new Intent(activity , Home.class)));
        CancelAll.setOnClickListener(v -> startActivity(new Intent(activity , Home.class)));

        loadData();
    }

    private void loadData() {
        Price.setText(d.androidData.total_cost + " " );

    }

    private void loadMsg(String MSG) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.msg_noads3);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView Title = dialog.findViewById(R.id.MsgTitle);
        TextView Text = dialog.findViewById(R.id.MsgText);

        Button Yes = dialog.findViewById(R.id.btn_Yes);
        Button No = dialog.findViewById(R.id.btn_No);

        Yes.setText(R.string.OK);
        Text.setText(MSG + "");
        No.setVisibility(View.GONE);


        Yes.setTypeface(fontMedim);
        Title.setTypeface(fontMedim);
        Text.setTypeface(fontUltra);

        Yes.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    protected void onResume() {

        try {
            if (OnlineHolder.getInstance().getData().equals("1")) {
                OnlineHolder.getInstance().setData("0");
                finish();
                startActivity(getIntent());
                //overridePendingTransition(R.anim.fadein_fast, R.anim.fadeout_fast);

            } else if (NetWork.isNetworkAvailable(activity) == false) {
                NetWork.gotoError(activity);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        super.onResume();
    }

    @Override
    public void onBackPressed() {

    }

    public void gotoBack(View v) {

    }

    public void gotoUpload(View v) {

    }


}
