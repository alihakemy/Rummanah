package com.usmart.com.rummanah;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.ethanhua.skeleton.ViewSkeletonScreen;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

import constants.Values;
import customLists.CustomListBrands;
import dataInLists.DataInBrands;
import helpers.LangHolder;
import helpers.LoginHolder;
import helpers.NetWork;
import helpers.OnlineHolder;
import helpers.UserTokenHolder;


public class Brands extends Activity {

    Activity activity = Brands.this;
    SharedPreferences Login;
    DataInBrands _Data = new DataInBrands();

    GridView lv;
    CustomListBrands adapter2;
    Typeface fontMedim, fontLight, fontUltra, fontPoppinsMed;
    TextView MainTitle;
    ViewSkeletonScreen skeletonScreen;
    ImageView HideAll;
    ProgressBar prog;
    SearchView searchV;
    String lang;
    int SecID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //	Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(activity, 2));
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
        setContentView(R.layout.activity_gridview);
        /*if (LangHolder.getInstance().getData().equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }*/
        lang = getSharedPreferences(Values.SharedPreferences_FileNameLangSelect, 0)
                .getString(Values.SharedPreferences_FileNameLangSelect, null);
        lang = Values.SharedPreferences_FileNameLangSelect;

        SecID = getIntent().getExtras().getInt("ID");

        fontMedim = Typeface.createFromAsset(activity.getAssets(), "fonts/GE_SS_Two_Medium.otf");
        fontLight = Typeface.createFromAsset(activity.getAssets(), "fonts/GE_SS_Two_Light.otf");
        fontUltra = Typeface.createFromAsset(activity.getAssets(), "fonts/ArbFONTS-GESSTextUltraLight-UltraLight.otf");


        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);

        MainTitle = findViewById(R.id.MainTitle);

        searchV = findViewById(R.id.SearchCity);
        lv = findViewById(R.id.listViewOrders);

        lv.setNumColumns(3);
      //  searchV.setVisibility(View.GONE);
        adapter2 = new CustomListBrands(activity, new ArrayList<>());
        lv.setAdapter(adapter2);

        MainTitle = findViewById(R.id.MainTitle);

        MainTitle.setText(R.string.Brands);
        MainTitle.setTypeface(fontMedim);

        lv.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(activity, ProductsBrand.class);
            i.putExtra("BrandID", _Data.data.get(position).id);
            i.putExtra("BrandName", _Data.data.get(position).title);
            startActivity(i);
        });
        setupSearchView();
        loadBrands();

    }

    private void setupSearchView() {
        searchV.setBackgroundResource(R.drawable.btn_light_gray_radius4);
        int searchPlateId = searchV.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlateView = searchV.findViewById(searchPlateId);

        if (searchPlateView != null) {
            searchPlateView.setBackgroundColor(Color.TRANSPARENT);
        }

        AutoCompleteTextView searchTextContent = searchV.findViewById(
                searchV.getContext().getResources().getIdentifier("android:id/search_src_text", null, null));

        searchTextContent.setTextSize(11); // Set the text size
        searchTextContent.setTypeface(fontLight);
        searchTextContent.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL); // Set its gravity to
        // bottom

        searchV.setIconifiedByDefault(false);

        searchV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                // TODO Auto-generated method stub
                CharSequence cs = newText;
                if (TextUtils.isEmpty(cs)) {
                    adapter2.clear();
                    adapter2.addAll(_Data.data);
                } else {
                    adapter2.getFilter().filter(cs);
                }

                return true;
            }
        });
        searchV.setSubmitButtonEnabled(false);
        // searchV.setFocusable(true);
        searchV.setFocusableInTouchMode(true);
        searchV.setIconified(false);
        searchV.setQueryHint(getResources().getString(R.string.Search));

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchV.getWindowToken(), 0);
        searchV.clearFocus();
    }

    private void loadBrands() {
        AsyncHttpClient client = new AsyncHttpClient();
        String Url;

        Url = Values.Link_service + "famous_brands/" + SecID + "/" + lang + "/v1";
        Log.i("TestApp", Url);
        if (LoginHolder.getInstance().getData().equals("login")) {
            client.addHeader("Authorization", "" + UserTokenHolder.getInstance().getData().token_type
                    + " " + UserTokenHolder.getInstance().getData().access_token);
        } else {
            client.addHeader("Authorization", "" + Values.Authorization_User);
        }

        client.get(Url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                super.onStart();
                HideAll.setVisibility(View.VISIBLE);
                prog.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(int arg0, String arg1) {
                // TODO Auto-generated method stub
                super.onSuccess(arg0, arg1);
                Log.i("TestApp", arg1);
                try {
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInBrands>() {
                    }.getType();
                    _Data = g.fromJson(arg1, t);
                    adapter2.addAll(_Data.data);
                    adapter2.notifyDataSetChanged();

                } catch (Exception e) {

                }

            }

            @SuppressWarnings("deprecation")
            @Override
            public void onFailure(Throwable arg0) {
                // TODO Auto-generated method stub
                super.onFailure(arg0);
                NetWork.gotoError(activity);
            }

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub
                HideAll.setVisibility(View.GONE);
                prog.setVisibility(View.GONE);
                super.onFinish();

            }
        });

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

        }

        super.onResume();

    }


    public void gotoBack(View v) {
        onBackPressed();
    }


}
