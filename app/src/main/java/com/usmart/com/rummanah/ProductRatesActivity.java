package com.usmart.com.rummanah;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.usmart.com.rummanah.adapters.RatesAdapter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Locale;

import constants.Values;
import customLists.CustomListOptions;
import customLists.CustomListProductRates;
import customLists.CustomListRelated;
import dataInLists.DataInProduct;
import helpers.LangHolder;
import helpers.LoginHolder;
import helpers.NetWork;
import helpers.UserTokenHolder;
import utils.ExpandableHeightGridView;

public class ProductRatesActivity extends FragmentActivity {
    private static final String TAG = ProductRatesActivity.class.getSimpleName();
    Activity activity = ProductRatesActivity.this;
    DataInProduct Data = new DataInProduct();

    CustomListProductRates adapterRates;
    int ID;
    String lang;
    Typeface fontMedim, fontLight, fontUltra, fontAvenir, fontPoppinsMedium;
    ScrollView Scroll;
    ExpandableHeightGridView lvRates;
    TextView noRates, tv_averageRating, tv_totalRates;
    ImageView HideAll, iv_back;
    ProgressBar progressBar1;
    RatingBar rb_rating;
    RecyclerView rv_rates;
    RatesAdapter ratesAdapter;
    GridLayoutManager manager;

    private int current_page = 0;
    private Boolean isLoading = true;
    private int pastVisibleItems, visibleItemCount, totalItemCount, previous_total = 0;
    private int view_threshold = 12;
    private ProgressBar progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_rates);

        // ************ Custom Action Bar *****************
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
      /*  getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);*/
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        // ***********************************************
        /*if (LangHolder.getInstance().getData().equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }*/
        lang = getSharedPreferences(Values.SharedPreferences_FileNameLangSelect, 0)
                .getString(Values.SharedPreferences_FileNameLangSelect, null);
        lang = Values.SharedPreferences_FileNameLangSelect;

        ID = getIntent().getExtras().getInt("ID");
        // ******************** Sliding Menu *****************
        fontAvenir = Typeface.createFromAsset(activity.getAssets(), "fonts/avenir-lt-std-55-roman.otf");
        fontPoppinsMedium = Typeface.createFromAsset(activity.getAssets(), "fonts/Poppins-Medium.ttf");
        fontMedim = Typeface.createFromAsset(getAssets(), "fonts/GE_SS_Two_Medium.otf");
        fontLight = Typeface.createFromAsset(getAssets(), "fonts/GE_SS_Two_Light.otf");
        fontUltra = Typeface.createFromAsset(getAssets(), "fonts/ArbFONTS-GESSTextUltraLight-UltraLight.otf");

        Scroll = findViewById(R.id.Scroll);
        lvRates = findViewById(R.id.lvRates);
        noRates = findViewById(R.id.noRates);
        HideAll = findViewById(R.id.HideAll);
        progressBar1 = findViewById(R.id.progressBar1);
        rb_rating = findViewById(R.id.rb_rating);
        tv_averageRating = findViewById(R.id.tv_averageRating);
        iv_back = findViewById(R.id.iv_back);
        tv_totalRates = findViewById(R.id.tv_totalRates);
        rv_rates = findViewById(R.id.rv_rates);

        lvRates.setExpanded(true);
        rv_rates.setHasFixedSize(true);
        manager = new GridLayoutManager(this, 1);

        Scroll.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == 0) {
                // ActionBar.setBackgroundColor(Color.TRANSPARENT);
                Log.i("TestApp_Pos", "Top");
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            } else {
                Log.i("TestApp_Pos", "Not Top");
                getWindow().setStatusBarColor(Color.parseColor("#3A813D"));
                // ActionBar.setBackgroundColor(Color.parseColor("#2699FB"));
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        loadData();

        rv_rates.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {

                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = manager.getChildCount();
                totalItemCount = manager.getItemCount();
                pastVisibleItems = manager.findFirstVisibleItemPosition();
                if (dy > 0) {
                    if (isLoading) {
                        if (totalItemCount > previous_total) {
                            isLoading = false;
                            previous_total = totalItemCount;
                        }
                    }
                    if (!isLoading && (totalItemCount - visibleItemCount) <= pastVisibleItems + view_threshold) {
                        current_page++;
                        getPagingRates(current_page);
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void loadData() {
        AsyncHttpClient client = new AsyncHttpClient();
        String Url;

        Url = Values.Link_service + "products/rates/" + ID + "/" + lang + "/v1";
        client.addHeader("Content-Type", "application/json");
        if (LoginHolder.getInstance().getData().equals("login")) {
            client.addHeader("Authorization", "" + UserTokenHolder.getInstance().getData().token_type
                    + " " + UserTokenHolder.getInstance().getData().access_token);
        } else {
            client.addHeader("Authorization", "" + Values.Authorization_User);
        }

        Log.i("TestApp", Url);
        client.get(Url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                super.onStart();
                HideAll.setVisibility(View.VISIBLE);
                progressBar1.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(int arg0, String arg1) {
                // TODO Auto-generated method stub
                super.onSuccess(arg0, arg1);
                Log.i("TestApp", arg1);
                try {
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInProduct>() {
                    }.getType();
                    Data = g.fromJson(arg1, t);

                    current_page = Data.data.rates.current_page;
                    tv_averageRating.setText(Data.data.rate + " " + getResources().getString(R.string.from) + " 5.0");
                    rb_rating.setRating((float) Data.data.rate);
                    tv_totalRates.setText(Data.data.rate_count + " " + getResources().getString(R.string.rates));

                    if (Data.data.rates.data.size() > 0) {
                        noRates.setVisibility(View.GONE);
                        adapterRates = new CustomListProductRates(activity, Data.data.rates.data);
                        ratesAdapter = new RatesAdapter(activity, Data.data.rates.data);
                        lvRates.setAdapter(adapterRates);
                        rv_rates.setAdapter(ratesAdapter);
                        rv_rates.setLayoutManager(manager);
                    } else {
                        noRates.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    Log.i("TestApp", e.getMessage());
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
                super.onFinish();

                HideAll.setVisibility(View.GONE);
                progressBar1.setVisibility(View.GONE);

            }
        });

    }

    private void getPagingRates(int page) {
        AsyncHttpClient client = new AsyncHttpClient();
        String Url;

        Url = Values.Link_service + "products/rates/" + ID + "/" + lang + "/v1?page=" + page;
        client.addHeader("Content-Type", "application/json");
        if (LoginHolder.getInstance().getData().equals("login")) {
            client.addHeader("Authorization", "" + UserTokenHolder.getInstance().getData().token_type
                    + " " + UserTokenHolder.getInstance().getData().access_token);
        } else {
            client.addHeader("Authorization", "" + Values.Authorization_User);
        }

        Log.d(TAG, "Pagination Url: " + Url);
        client.get(Url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                super.onStart();
                HideAll.setVisibility(View.VISIBLE);
                progressBar1.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(int arg0, String arg1) {
                // TODO Auto-generated method stub
                super.onSuccess(arg0, arg1);
                Log.i("TestApp", arg1);
                try {
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInProduct>() {
                    }.getType();
                    Data = g.fromJson(arg1, t);

                    ratesAdapter = new RatesAdapter(activity, Data.data.rates.data);
                    rv_rates.setAdapter(ratesAdapter);
                    rv_rates.setLayoutManager(manager);

                    ratesAdapter.addPagingSearch(Data.data.rates.data);
                    rv_rates.setAdapter(ratesAdapter);
                    rv_rates.scrollToPosition(pastVisibleItems);
                    rv_rates.setLayoutManager(manager);

                } catch (Exception e) {
                    Log.i("TestApp", e.getMessage());
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
                super.onFinish();

                HideAll.setVisibility(View.GONE);
                progressBar1.setVisibility(View.GONE);

            }
        });

    }
}