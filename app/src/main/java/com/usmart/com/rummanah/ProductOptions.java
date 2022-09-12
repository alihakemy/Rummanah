package com.usmart.com.rummanah;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ethanhua.skeleton.ViewSkeletonScreen;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Locale;

import constants.Values;
import customLists.CustomListOptions;
import dataInLists.DataInProduct;
import helpers.LangHolder;
import helpers.NetWork;
import helpers.OnlineHolder;

public class ProductOptions extends Activity {
    Activity activity = ProductOptions.this;

    DataInProduct _Main = new DataInProduct();
    ArrayList<DataInProduct.Options> _Data = new ArrayList<>();
    ListView lv;
    CustomListOptions adapter2;
    Typeface fontMedim, fontLight, fontUltra;
    ViewSkeletonScreen skeletonScreen;
    ImageView HideAll;
    ProgressBar prog;
    TextView noData, MainTitle;
    String lang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //   Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(activity, 2));
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
        // ***********************************************
        setContentView(R.layout.activity_listview_options);
        fontMedim = Typeface.createFromAsset(activity.getAssets(), "fonts/GE_SS_Two_Medium.otf");
        fontLight = Typeface.createFromAsset(activity.getAssets(), "fonts/GE_SS_Two_Light.otf");
        fontUltra = Typeface.createFromAsset(activity.getAssets(), "fonts/ArbFONTS-GESSTextUltraLight-UltraLight.otf");
        /*if (LangHolder.getInstance().getData().equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }*/
        lang = getSharedPreferences(Values.SharedPreferences_FileNameLangSelect, 0)
                .getString(Values.SharedPreferences_FileNameLangSelect, null);
        lang = Values.SharedPreferences_FileNameLangSelect;
        _Data = (ArrayList<DataInProduct.Options>) getIntent().getExtras().get("Options");

        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);
        lv = findViewById(R.id.listViewOrders);
        noData = findViewById(R.id.noData);
        MainTitle = findViewById(R.id.MainTitle);

        adapter2 = new CustomListOptions(activity, _Data);
        lv.setAdapter(adapter2);


        noData.setTypeface(fontUltra);
        MainTitle.setTypeface(fontUltra);
        MainTitle.setText(R.string.Options);
        lv.setOnItemClickListener((parent1, view, position, id) -> {

        });


    }

    @Override
    protected void onResume() {

        try {
            if (OnlineHolder.getInstance().getData().equals("1")) {
                OnlineHolder.getInstance().setData("0");
                finish();
                startActivity(getIntent());

            } else if (NetWork.isNetworkAvailable(activity) == false) {
                NetWork.gotoError(activity);
            }
        } catch (Exception e) {

        }
        super.onResume();

    }

    public void gotoBack(View v) {
        super.onBackPressed();
    }
}
