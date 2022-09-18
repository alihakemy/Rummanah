package com.usmart.com.rummanah;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.ethanhua.skeleton.ViewSkeletonScreen;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;


import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import constants.Values;
import customLists.CustomListHomeBrands;
import customLists.CustomListHomeCats;
import customLists.CustomListHomeOffer;
import customLists.CustomSlider;
import dataInLists.DataInCall;
import dataInLists.DataInGlobal;
import dataInLists.DataInHome;
import helpers.LangHolder;
import helpers.LoginHolder;
import helpers.NetWork;
import helpers.OnlineHolder;
import helpers.UserTokenHolder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Home extends Activity {
    Activity activity = Home.this;
    private static final String TAG = Home.class.getSimpleName();

    DataInHome _Data = new DataInHome();
    DataInCall DataCall = new DataInCall();
    Typeface fontMedim, fontLight, fontUltra;
    ViewSkeletonScreen skeletonScreen;
    ImageView HideAll, Call;
    ProgressBar prog;
    HashMap<String, String> url_maps = new HashMap<>();
    HashMap<String, Integer> file_maps = new HashMap<>();
    LinearLayout Container;
    TextView MainTitle, tv_CartNum, tv_basketCount;
    private static final int INITIAL_REQUEST = 1337;
    private static final int CALL_REQUEST = INITIAL_REQUEST + 5;
    private static final String[] CALL_PERMS = {
            Manifest.permission.CALL_PHONE
    };
    String lang, DeviceId;
    private LinearLayout ll_profile, ll_offers, ll_home, ll_categories;
    private RelativeLayout rl_basket;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
        getWindow().setStatusBarColor(getResources().getColor(R.color.green));
        // ***********************************************
        setContentView(R.layout.activity_home);
        fontMedim = Typeface.createFromAsset(activity.getAssets(), "fonts/GE_SS_Two_Medium.otf");
        fontLight = Typeface.createFromAsset(activity.getAssets(), "fonts/GE_SS_Two_Light.otf");
        fontUltra = Typeface.createFromAsset(activity.getAssets(), "fonts/ArbFONTS-GESSTextUltraLight-UltraLight.otf");
        /*if (LangHolder.getInstance().getData().equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }*/
        lang = Values.SharedPreferences_FileNameLangSelect;
        Log.d(TAG, "value of selected language: " + Values.SharedPreferences_FileNameLangSelect);

        DeviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d(TAG, "Device Id: " + DeviceId);

        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);

        Call = findViewById(R.id.Call);
        Container = findViewById(R.id.Container);

        MainTitle = findViewById(R.id.MainTitle);
        tv_CartNum = findViewById(R.id.tv_CartNum);
        tv_basketCount = findViewById(R.id.tv_basketCount);
        ll_profile = findViewById(R.id.ll_profile);
        rl_basket = findViewById(R.id.rl_basket);
        ll_offers = findViewById(R.id.ll_offers);
        ll_categories = findViewById(R.id.ll_categories);
        ll_home = findViewById(R.id.ll_home);

        Call.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + DataCall.data));
            startActivity(callIntent);
        });

        MainTitle.setTypeface(fontMedim);

        loadHome();

        ll_categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, AllCats.class);
                startActivity(intent);
            }
        });

        ll_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, AccountMenu.class);
                startActivity(intent);
            }
        });

        ll_offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Offers.class);
                startActivity(intent);
            }
        });

        rl_basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Cart.class);
                startActivity(intent);
            }
        });
    }


    private void loadHome() {
        AsyncHttpClient client = new AsyncHttpClient();
        String Url;

        Url = Values.Link_service + "home/" + lang + "/v1/";
        Log.i("TestApp", Url);
        client.addHeader("Content-Type", "application/json");
        if (LoginHolder.getInstance().getData().equals("login")) {
            client.addHeader("Authorization", "" + UserTokenHolder.getInstance().getData().token_type
                    + " " + UserTokenHolder.getInstance().getData().access_token);
        } else {
            client.addHeader("Authorization", "" + Values.Authorization_User);
        }

        client.get(Url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                HideAll.setVisibility(View.VISIBLE);
                prog.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(int arg0, String arg1) {
                // TODO Auto-generated method stub
                Log.i("TestApp", arg1);
                super.onSuccess(arg0, arg1);
                try {
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInHome>() {
                    }.getType();
                    _Data = g.fromJson(arg1, t);

                    if (_Data.data.size() > 0) {
                        for (DataInHome.HomeData MainSection : _Data.data) {
                            if (MainSection.type == 1) {
                                View view = LayoutInflater.from(activity).inflate(R.layout.layout_slider, null);
                                SliderLayout Slider = view.findViewById(R.id.slider);
                                PagerIndicator Indicator = view.findViewById(R.id.custom_indicator);

                                loadAds(MainSection.data, Slider, Indicator);
                                Container.addView(view);
                            } else if (MainSection.type == 2) {
                                View view = LayoutInflater.from(activity).inflate(R.layout.layout_cats, null);
                                TextView CatsIcon = view.findViewById(R.id.CatsIcon);
                                TextView CatsAll = view.findViewById(R.id.CatsAll);
                                RecyclerView lvCats = view.findViewById(R.id.lvCats);

                                GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 4);
                                gridLayoutManager.setSpanCount(4);
                                RecyclerView.LayoutManager layoutManager = new
                                        GridLayoutManager(activity, 1, LinearLayoutManager.HORIZONTAL, false);

                                lvCats.setLayoutManager(layoutManager);

                                CatsIcon.setText(getResources().getString(R.string.Cats) + "");
                                CatsIcon.setTypeface(fontMedim);
                                CatsAll.setTypeface(fontLight);
                                CustomListHomeCats adapterCats = new CustomListHomeCats(activity, MainSection.data);
                                lvCats.setAdapter(adapterCats);

                                CatsAll.setOnClickListener(v -> startActivity(new Intent(activity, AllCats.class)));

                                Container.addView(view);
                            } else if (MainSection.type == 3) {
                                View view = LayoutInflater.from(activity).inflate(R.layout.layout_brands, null);
                                TextView TopBrandsIcon = view.findViewById(R.id.TopBrandsIcon);
                                TextView BrandsAll = view.findViewById(R.id.BrandsAll);
                                RecyclerView lvTopBrands = view.findViewById(R.id.lvTopBrands);

                                GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 1);
                                gridLayoutManager.setSpanCount(1);
                                RecyclerView.LayoutManager layoutManager = new
                                        GridLayoutManager(activity, 1, LinearLayoutManager.HORIZONTAL, false);
                                lvTopBrands.setLayoutManager(layoutManager);

                                TopBrandsIcon.setText(getResources().getString(R.string.Brands));
                                TopBrandsIcon.setTypeface(fontMedim);
                                BrandsAll.setTypeface(fontLight);
                                CustomListHomeBrands adapterBrands = new CustomListHomeBrands(activity, MainSection.data);
                                lvTopBrands.setAdapter(adapterBrands);

                                BrandsAll.setOnClickListener(v -> startActivity(new Intent(activity, Brands.class)));
                                BrandsAll.setOnClickListener(v -> {
                                    Intent i = new Intent(activity, Brands.class);
                                    i.putExtra("ID", MainSection.id);
                                    startActivity(i);
                                });

                                Container.addView(view);
                            } else if (MainSection.type == 4) {
                                View view = LayoutInflater.from(activity).inflate(R.layout.layout_offers, null);
                                TextView OffersIcon = view.findViewById(R.id.OffersIcon);
                                TextView OffersAll = view.findViewById(R.id.OffersAll);
                                RecyclerView lvOffers = view.findViewById(R.id.lvOffers);

                                GridLayoutManager gridLayoutManager4 = new GridLayoutManager(activity, 2);
                                gridLayoutManager4.setSpanCount(2);
                                RecyclerView.LayoutManager layoutManager4 = new
                                        GridLayoutManager(activity, 1, LinearLayoutManager.HORIZONTAL, false);
                                lvOffers.setLayoutManager(layoutManager4);

                                OffersIcon.setText(MainSection.title + "");
                                OffersIcon.setTypeface(fontMedim);
                                OffersAll.setTypeface(fontLight);
                                CustomListHomeOffer adapterOffer = new CustomListHomeOffer(activity, MainSection.data, tv_CartNum);
                                lvOffers.setAdapter(adapterOffer);

                                OffersAll.setOnClickListener(v -> {
                                    Intent i = new Intent(activity, ProductsOffers.class);
                                    i.putExtra("ID", MainSection.id);
                                    i.putExtra("Data_Name", MainSection.title);
                                    startActivity(i);
                                });

                                Container.addView(view);
                            } else if (MainSection.type == 5) {
                                View view = LayoutInflater.from(activity).inflate(R.layout.layout_ad, null);
                                ImageView Ad = view.findViewById(R.id.Ad);

                                DisplayImageOptions options;
                                options = new DisplayImageOptions.Builder()
                                        .showImageOnLoading(R.mipmap.def)
                                        .showImageForEmptyUri(R.mipmap.def)
                                        .showImageOnFail(R.mipmap.def)
                                        .cacheInMemory(true)
                                        .cacheOnDisk(true)
                                        .considerExifParams(true)
                                        .bitmapConfig(Bitmap.Config.RGB_565)
                                        .displayer(new RoundedBitmapDisplayer(5))
                                        .build();
                                ImageLoader.getInstance().displayImage(Values.Link_Image + MainSection.data.get(0).image, Ad, options);

                                Container.addView(view);
                            }

                        }
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
                prog.setVisibility(View.GONE);
            }
        });

    }

    private void loadAds(ArrayList<DataInHome.HomeContent> Data, SliderLayout mDemoSlider, PagerIndicator Indicator) {

        try {
            int x = 0;
            for (DataInHome.HomeContent _data : Data) {
                url_maps.put(" " + x + " - " + _data.title, Values.Link_Image + _data.image);
                file_maps.put(" " + x + " - " + _data.title, x);
                x++;
            }

            for (String name : url_maps.keySet()) {
                CustomSlider textSliderView = new CustomSlider(activity);
                textSliderView.getView().findViewById(com.daimajia.slider.library.R.id.description_layout)
                        .setBackgroundColor(Color.TRANSPARENT);
                ImageView Img = textSliderView.getView().findViewById(com.daimajia.slider.library.R.id.daimajia_slider_image);
                DisplayImageOptions options;
                Img.setBackgroundResource(R.drawable.btn_blue_radius50);

                textSliderView.image(url_maps.get(name)).setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(slider -> {
                            try {
                                if (Data.get(file_maps.get(name)).type == 1) {
                                    Intent i = new Intent(activity, Product.class);
                                    i.putExtra("ID", Integer.parseInt(Data.get(file_maps.get(name)).content));
                                    startActivity(i);
                                } else {
                                    Intent browserIntent3 = new Intent(Intent.ACTION_VIEW, Uri.parse(
                                            Data.get(file_maps.get(name)).content));
                                    startActivity(browserIntent3);
                                }

                            } catch (Exception e) {
                            }
                        });

                // add your extra information
                ///   textSliderView.getBundle().putString("extra", name + x);
                mDemoSlider.addSlider(textSliderView);

            }


            //    mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Tablet);
            mDemoSlider.setCustomIndicator(Indicator);
            /// mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mDemoSlider.setCustomAnimation(new DescriptionAnimation());
            mDemoSlider.setDuration(6000);

        } catch (Exception e) {
            Log.i("TestLog", e.getMessage());
        }
    }

    public void CartCount() {

        OkHttpClient client = new OkHttpClient();
        String Url = Values.Link_service + "visitors/cart/count/" + lang + "/v1";
        Log.i("TestApp", Url);
        String json = new StringBuilder()
                .append("{")
                .append("\"unique_id\":\"" + Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID) + "\"")
                .append("}").toString();

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                json
        );
        Request request = new Request.Builder()
                .url(Url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "" + Values.Authorization_User)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("TestApp_5", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.i("TestAppCart",response.body().string());
                try {
                    DataInGlobal Result;
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInGlobal>() {
                    }.getType();

                    Result = g.fromJson(response.body().string(), t);


                    runOnUiThread(() -> {
                        if (!Result.success) {
                            if (Result.code == 401) {
                                Intent i = new Intent(activity, Login.class);
                                startActivity(i);
                            } else {
                                loadMsg(Result.message);
                            }
                        } else {
                            tv_CartNum.setText(Result.data.count + "");
                            tv_basketCount.setText(Result.data.count + "");
                        }
                    });

                } catch (Exception e) {
                }
            }

        });

    }

    private void loadMobile() {
        AsyncHttpClient client = new AsyncHttpClient();
        String Url;

        Url = Values.Link_service + "getappnumber/" + lang + "/v1";
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Authorization", "" + Values.Authorization_User);

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
                HideAll.setVisibility(View.GONE);
                prog.setVisibility(View.GONE);
                try {
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInCall>() {
                    }.getType();
                    DataCall = g.fromJson(arg1, t);


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
                super.onFinish();
                HideAll.setVisibility(View.GONE);
                prog.setVisibility(View.GONE);

            }
        });

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

        No.setText(R.string.OK);
        Text.setText(MSG + "");
        Yes.setVisibility(View.GONE);


        No.setTypeface(fontMedim);
        Title.setTypeface(fontMedim);
        Text.setTypeface(fontUltra);

        No.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    private boolean canCall() {
        return (hasPermission(Manifest.permission.CALL_PHONE));
    }

    private boolean hasPermission(String perm) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return (PackageManager.PERMISSION_GRANTED == checkSelfPermission(perm));
        }
        return false;
    }

    @Override
    protected void onResume() {
        CartCount();
        loadMobile();

        if (!canCall()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(CALL_PERMS, CALL_REQUEST);
            }
        }
        BottomNavigationViewEx navigation = findViewById(R.id.navigation);
        navigation.enableAnimation(false);
        navigation.enableShiftingMode(false);
        navigation.setTypeface(fontLight);
        navigation.setTextSize(12);
        navigation.enableItemShiftingMode(false);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);
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

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                //startActivity(new Intent(activity, Home.class));
                return true;
            case R.id.navigation_cats:
                startActivity(new Intent(activity, AllCats.class));
                return true;
            case R.id.navigation_offers:
                startActivity(new Intent(activity, Offers.class));
                // overridePendingTransition(R.anim.slide_bottom_to_top, R.anim.slide_top_to_bottom);
                return true;
            case R.id.navigation_account:
                startActivity(new Intent(activity, AccountMenu.class));
                // overridePendingTransition(R.anim.slide_bottom_to_top, R.anim.slide_top_to_bottom);
                return true;
        }
        return false;
    };

    public void gotoBack(View v) {
        super.onBackPressed();
    }

    public void gotoMenu(View v) {
        startActivity(new Intent(activity, Menu.class));
    }

    public void gotoCart(View v) {
        startActivity(new Intent(activity, Cart.class));
    }

    public void gotoSearch(View v) {
        startActivity(new Intent(activity, Search.class));
    }
}
