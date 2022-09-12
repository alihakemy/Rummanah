package com.usmart.com.rummanah;

import android.app.ActionBar;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Locale;

import adapters.TabPagerAdapterPhoto;
import constants.Values;
import helpers.LangHolder;

public class PhotoGallery extends FragmentActivity {

    private ActionBar actionBar;
    ViewPager Tab;
    TabPagerAdapterPhoto TabAdapter;
    ArrayList<String> _Data = new ArrayList<>();
    String lang;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);
        // ***************************************************
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
        //**********************************************************
		/*if (LangHolder.getInstance().getData().equals("ar")) {
			lang = 2;
		} else {
			lang = 1;
		}*/
        lang = getSharedPreferences(Values.SharedPreferences_FileNameLangSelect, 0)
                .getString(Values.SharedPreferences_FileNameLangSelect, null);
        lang = Values.SharedPreferences_FileNameLangSelect;

        _Data = (ArrayList<String>) getIntent().getExtras().get("Photos");

        TabAdapter = new TabPagerAdapterPhoto(getSupportFragmentManager(), _Data);
        Tab = findViewById(R.id.pagerGallery);
        Tab.setOffscreenPageLimit(_Data.size());
        Tab.setAdapter(TabAdapter);

    }

    public void gotoBack(View v) {
        super.onBackPressed();
    }

}
