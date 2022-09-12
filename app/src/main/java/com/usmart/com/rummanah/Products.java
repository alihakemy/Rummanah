package com.usmart.com.rummanah;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.ViewSkeletonScreen;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

import constants.Values;
import customLists.CustomListProducts;
import dataInLists.DataInCats;
import dataInLists.DataInGlobal;
import dataInLists.DataInLogin;
import dataInLists.DataInProducts;
import dataInLists.DataInSubCats;
import de.hdodenhof.circleimageview.CircleImageView;
import helpers.FaceIdHolder;
import helpers.LangHolder;
import helpers.LoginHolder;
import helpers.NetWork;
import helpers.OnlineHolder;
import helpers.UserEmailHolder;
import helpers.UserMobileHolder;
import helpers.UserTokenHolder;
import helpers.UsernameHolder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Products extends Activity {

    Activity activity = Products.this;
    private static final String TAG = Products.class.getSimpleName();
    SharedPreferences Login;
    DataInProducts _Data = new DataInProducts();
    DataInSubCats _DataSubCats;
    DataInCats _DataCats;

    GridView lv;
    CustomListProducts adapter2;
    Typeface fontMedim, fontLight, fontUltra, fontPoppinsMed;
    TextView MainTitle, tv_basketCount;
    ViewSkeletonScreen skeletonScreen;
    TabLayout MainTap, SubTab;
    ImageView HideAll;
    ProgressBar prog;
    int index = 0;
    int SubIndex = 0;
    int MainSelected = 0;
    int SubSelected = 0;
    int MainID, SubCatID;
    String lang;
    boolean First = true;
    private LinearLayout ll_profile, ll_offers, ll_home, ll_categories;
    private RelativeLayout rl_basket;

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
        getWindow().setStatusBarColor(Color.WHITE);
        // ***********************************************
        setContentView(R.layout.activity_gridview_products);
        /*if (LangHolder.getInstance().getData().equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }*/
        lang = getSharedPreferences(Values.SharedPreferences_FileNameLangSelect, 0)
                .getString(Values.SharedPreferences_FileNameLangSelect, null);
        lang = Values.SharedPreferences_FileNameLangSelect;

        // = getIntent().getExtras().getInt("MainID");
        // SubCatID = getIntent().getExtras().getInt("SubCatID");

        Intent intent = getIntent();
        if (intent.hasExtra("MainID")) {
            MainID = intent.getExtras().getInt("MainID");
        } else {
            MainID = 0;
        }


        if (intent.hasExtra("SubCatID")) {
            SubCatID = intent.getExtras().getInt("SubCatID");
        } else {
            SubCatID = 0;
        }

        fontMedim = Typeface.createFromAsset(activity.getAssets(), "fonts/GE_SS_Two_Medium.otf");
        fontLight = Typeface.createFromAsset(activity.getAssets(), "fonts/GE_SS_Two_Light.otf");
        fontUltra = Typeface.createFromAsset(activity.getAssets(), "fonts/ArbFONTS-GESSTextUltraLight-UltraLight.otf");

        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);

        MainTitle = findViewById(R.id.MainTitle);

        lv = findViewById(R.id.listViewOrders);

        adapter2 = new CustomListProducts(activity, new ArrayList<>());
        lv.setAdapter(adapter2);

        //MainTitle = findViewById(R.id.MainTitle);
        MainTap = findViewById(R.id.Tab);
        SubTab = findViewById(R.id.TabSub);
        tv_basketCount = findViewById(R.id.tv_basketCount);
        ll_profile = findViewById(R.id.ll_profile);
        rl_basket = findViewById(R.id.rl_basket);
        ll_offers = findViewById(R.id.ll_offers);
        ll_categories = findViewById(R.id.ll_categories);
        ll_home = findViewById(R.id.ll_home);

        lv.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(activity, Product.class);
            i.putExtra("ID", _Data.data.products.data.get(position).id);
            startActivity(i);
        });

        MainTap.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView CatTitle = tab.getCustomView().findViewById(R.id.tv_Title);
                CatTitle.setTextColor(Color.parseColor("#3A813D"));
                CatTitle.setTypeface(fontLight);
                MainSelected = tab.getPosition();
                // MainID = _DataCats.data.get(MainSelected).id;
                //  SubCatID = 0;
                if (index == (_DataCats.data.size())) {
                    loadSubCats(_DataCats.data.get(MainSelected).id);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView CatTitle = tab.getCustomView().findViewById(R.id.tv_Title);
                CatTitle.setTextColor(Color.parseColor("#3A813D"));
                CatTitle.setTypeface(fontLight);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        SubTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                LinearLayout List = tab.getCustomView().findViewById(R.id.List);
                TextView CatTitle = tab.getCustomView().findViewById(R.id.tv_Title);
                CatTitle.setTextColor(Color.parseColor("#02A89E"));
                CatTitle.setTypeface(fontLight);
                List.setBackgroundResource(R.drawable.btn_lightblue_radius15_75);

                SubSelected = tab.getPosition();
                // SubCatID = _DataCats.data.get(SubSelected).id;
                //  if (SubIndex == SubTab.getTabCount()) {
                if (SubCatID == 0 || First == false) {
                    if (SubSelected == 0) {
                        loadProducts(_DataCats.data.get(MainSelected).id, 0);
                    } else {
                        //  SubSelected = SubSelected - 1;
                        loadProducts(_DataCats.data.get(MainSelected).id, _DataSubCats.data.sub_categories.get(SubSelected).id);
                    }
                } else {
                    if (SubIndex == SubTab.getTabCount()) {
                        First = false;
                        loadProducts(_DataCats.data.get(MainSelected).id, SubCatID);
                    }
                }
                // }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                LinearLayout List = tab.getCustomView().findViewById(R.id.List);
                TextView CatTitle = tab.getCustomView().findViewById(R.id.tv_Title);
                CatTitle.setTextColor(Color.parseColor("#545454"));
                CatTitle.setTypeface(fontLight);
                List.setBackgroundResource(R.drawable.btn_white_radius15_75);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        ll_categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Products.this, AllCats.class);
                startActivity(intent);
            }
        });

        ll_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Products.this, AccountMenu.class);
                startActivity(intent);
            }
        });

        ll_offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Products.this, Offers.class);
                startActivity(intent);
            }
        });

        rl_basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Products.this, Cart.class);
                startActivity(intent);
            }
        });

        ll_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Products.this, Home.class);
                startActivity(intent);
            }
        });

        loadMainCats();

    }


    private void loadMainCats() {
        AsyncHttpClient client = new AsyncHttpClient();
        String Url;

        Url = Values.Link_service + "categories/" + lang + "/v1";
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
                skeletonScreen = Skeleton.bind(lv)
                        .load(R.layout.singel_load_list)
                        .show();
            }

            @Override
            public void onSuccess(int arg0, String arg1) {
                // TODO Auto-generated method stub

                super.onSuccess(arg0, arg1);
                try {
                    index = 0;
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInCats>() {
                    }.getType();
                    _DataCats = g.fromJson(arg1, t);
                    // _DataCats.data.get(0).selected = true;


                    if (MainID == 0) {
                        MainID = _DataCats.data.get(0).id;
                    }


                    TabLayout.Tab fTab;
                    DisplayImageOptions options;
                    options = new DisplayImageOptions.Builder()
                            .showImageOnLoading(R.mipmap.def_icon)
                            .showImageForEmptyUri(R.mipmap.def_icon)
                            .showImageOnFail(R.mipmap.def_icon)
                            .cacheInMemory(true)
                            .cacheOnDisk(true)
                            .considerExifParams(true)
                            .bitmapConfig(Bitmap.Config.RGB_565)
                            .build();

                    for (DataInCats.MainData _data : _DataCats.data) {
                        fTab = MainTap.newTab();
                        fTab.setCustomView(R.layout.singel_cat_tap);
                        LinearLayout List = fTab.getCustomView().findViewById(R.id.List);
                        TextView CatTitle = fTab.getCustomView().findViewById(R.id.tv_Title);
                        ImageView CatImg = fTab.getCustomView().findViewById(R.id.iv_Feeds);
                        //  CatImg.setVisibility(View.GONE);
                        ImageLoader.getInstance().displayImage(Values.Link_Image + _data.image, CatImg, options);
                        CatTitle.setTypeface(fontLight);
                        CatTitle.setText(_data.title + "");

                        if (_data.id == MainID) {
                            MainSelected = index;
                        }
                        MainTap.addTab(fTab, index);
                        index++;
                    }


                    new Handler().postDelayed(() -> {
                        TextView CatTitle = MainTap.getTabAt(MainSelected).getCustomView().findViewById(R.id.tv_Title);
                        CatTitle.setTextColor(Color.parseColor("#3A813D"));
                        CatTitle.setTypeface(fontLight);
                        MainTap.getTabAt(MainSelected).select();
                        if (MainSelected == 0) {
                            loadSubCats(MainID);
                        }
                    }, 100);


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
                skeletonScreen.hide();
            }
        });

    }

    private void loadSubCats(int CatID) {
        AsyncHttpClient client = new AsyncHttpClient();
        String Url;

        Url = Values.Link_service + "sub_categories/" + CatID + "/" + lang + "/v1";
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
                skeletonScreen = Skeleton.bind(lv)
                        .load(R.layout.singel_load_list)
                        .show();
            }

            @Override
            public void onSuccess(int arg0, String arg1) {
                // TODO Auto-generated method stub

                super.onSuccess(arg0, arg1);
                // try {
                SubIndex = 0;
                Gson g = new Gson();
                Type t = new TypeToken<DataInSubCats>() {
                }.getType();
                _DataSubCats = g.fromJson(arg1, t);

                DataInSubCats Bo = new DataInSubCats();
                DataInSubCats.SubCats Obj = Bo.new SubCats(0, ""
                        , getResources().getString(R.string.AllCats));
                _DataSubCats.data.sub_categories.add(0, Obj);

                if (_DataSubCats.data.sub_categories.size() > 0) {
                    SubTab.removeAllTabs();
                    SubTab.setVisibility(View.VISIBLE);
                    TabLayout.Tab fTab;
                    DisplayImageOptions options;
                    options = new DisplayImageOptions.Builder()
                            .showImageOnLoading(R.mipmap.def_icon)
                            .showImageForEmptyUri(R.mipmap.def_icon)
                            .showImageOnFail(R.mipmap.def_icon)
                            .cacheInMemory(true)
                            .cacheOnDisk(true)
                            .considerExifParams(true)
                            .bitmapConfig(Bitmap.Config.RGB_565)
                            .build();
                    for (DataInSubCats.SubCats _data : _DataSubCats.data.sub_categories) {
                        fTab = SubTab.newTab();
                        fTab.setCustomView(R.layout.singel_cat_tap);
                        LinearLayout List = fTab.getCustomView().findViewById(R.id.List);
                        TextView CatTitle = fTab.getCustomView().findViewById(R.id.tv_Title);
                        CircleImageView CatImg = fTab.getCustomView().findViewById(R.id.iv_Feeds);
                        CatImg.setVisibility(View.GONE);
                        List.setBackgroundResource(R.drawable.btn_white_radius15_75);

                        ImageLoader.getInstance().displayImage(Values.Link_Image + _data.image, CatImg, options);
                        if (_data.id == 0) {
                            CatImg.setVisibility(View.GONE);
                        }
                        CatTitle.setTypeface(fontLight);
                        CatTitle.setText(_data.title + "");


                        if (_data.id == SubCatID) {
                            SubSelected = SubIndex;
                        }
                        SubTab.addTab(fTab, SubIndex);

                        SubIndex++;

                    }

                    new Handler().postDelayed(() -> {
                        SubTab.getTabAt(SubSelected).select();
                        LinearLayout List = SubTab.getTabAt(SubSelected).getCustomView().findViewById(R.id.List);
                        TextView CatTitle = SubTab.getTabAt(SubSelected).getCustomView().findViewById(R.id.tv_Title);
                        CatTitle.setTextColor(Color.parseColor("#3A813D"));
                        CatTitle.setTypeface(fontLight);
                        List.setBackgroundResource(R.drawable.btn_lightblue_radius15_75);
                    }, 100);

                } else {
                    SubTab.setVisibility(View.GONE);
                }
               /* } catch (Exception e) {
                    Log.i("TestApp", e.getMessage());
                }*/

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
                skeletonScreen.hide();
                //loadProducts(MainID, SubCatID);
            }
        });

    }

    private void loadProducts(int CatID, int SubCat) {
        // SubCat = 0;

        AsyncHttpClient client = new AsyncHttpClient();
        String Url;

        Url = Values.Link_service + "products/" + lang + "/v1?category_id=" + CatID + "&sub_category_id=" + SubCat;

        Log.i("TestApp", Url);
        Log.d(TAG, Url);
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
                skeletonScreen = Skeleton.bind(lv)
                        .load(R.layout.singel_load_list)
                        .show();
            }

            @Override
            public void onSuccess(int arg0, String arg1) {
                // TODO Auto-generated method stub
                super.onSuccess(arg0, arg1);
                //  Log.i("TestApp", arg1);
                try {
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInProducts>() {
                    }.getType();
                    _Data = g.fromJson(arg1, t);
                    adapter2.clear();
                    adapter2.notifyDataSetChanged();

                    adapter2.addAll(_Data.data.products.data);
                    adapter2.notifyDataSetChanged();

                } catch (Exception e) {
                    Log.i("TestApp_error", e.getMessage());
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
                skeletonScreen.hide();
                super.onFinish();

            }
        });

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
                            tv_basketCount.setText(Result.data.count + "");
                        }
                    });

                } catch (Exception e) {
                }
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
        CartCount();
        BottomNavigationViewEx navigation = findViewById(R.id.navigation);
        navigation.enableAnimation(false);
        navigation.enableShiftingMode(false);
        navigation.setTypeface(fontLight);
        navigation.setTextSize(12);
        navigation.enableItemShiftingMode(false);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_cats);
        try {
            if (LoginHolder.getInstance().getData().equals("login")) {
                SharedPreferences.Editor editor = Login.edit();
                editor.putString("isLogin", "login");
                editor.putString("UserID", FaceIdHolder.getInstance().getData());
                editor.putString("UserName", UsernameHolder.getInstance().getData());
                editor.putString("UserMobile", UserMobileHolder.getInstance().getData());
                editor.putString("UserEmail", UserEmailHolder.getInstance().getData());
                editor.putString("Token", UserTokenHolder.getInstance().getData().access_token);
                editor.putString("Token_Type", UserTokenHolder.getInstance().getData().token_type);
                editor.putString("Token_Exp", UserTokenHolder.getInstance().getData().expires_in);
                editor.commit();

            } else {
                Login = getSharedPreferences(Values.SharedPreferences_FileName, 0);
                LoginHolder.getInstance().setData("logout");
                FaceIdHolder.getInstance().setData("0");
                SharedPreferences.Editor editor = Login.edit();
                editor.putString("isLogin", "logout");
                editor.putString("UserID", "0");
                editor.putString("UserName", "");
                editor.putString("UserMobile", "");
                editor.putString("UserEmail", "");
                editor.putString("Token", "");
                editor.putString("Token_Type", "");
                editor.putString("Token_Exp", "");
                editor.commit();
                DataInLogin.Tokens Token = new DataInLogin.Tokens();
                Token.access_token = "";
                Token.token_type = "";
                Token.expires_in = "";
                UserTokenHolder.getInstance().setData(Token);
            }
        } catch (Exception e) {

        }
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
                startActivity(new Intent(activity, Home.class));
                return true;
            case R.id.navigation_cats:
                //startActivity(new Intent(activity, AllCats.class));
                return true;
            case R.id.navigation_cart:
                startActivity(new Intent(activity, Cart.class));
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
        onBackPressed();
    }

    public void gotoSearch(View v) {
        startActivity(new Intent(activity, Search.class));
    }


}
