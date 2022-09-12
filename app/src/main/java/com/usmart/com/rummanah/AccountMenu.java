package com.usmart.com.rummanah;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

import constants.Values;
import customLists.CustomListMenuAccount;
import dataInLists.DataInGlobal;
import dataInLists.DataInListIcons;
import dataInLists.DataInLogin;
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


public class AccountMenu extends Activity {
    SharedPreferences Login;
    Activity activity = AccountMenu.this;
    ArrayList<DataInListIcons> _Data = new ArrayList<>();
    ListView lv;
    CustomListMenuAccount adapter2;
    String lang;
    Typeface fontMedim, fontLight, fontUltra;
    TextView App_Version, tv_basketCount;
    private LinearLayout ll_profile, ll_offers, ll_home, ll_categories;
    private RelativeLayout rl_basket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        //*******************************************************
        setContentView(R.layout.activity_menu);
        init();

        App_Version.setText(getResources().getString(R.string.App_Version) + " : " + Values.App_Version + "");

        //**************** Set Adapter ***********************
        adapter2 = new CustomListMenuAccount(activity, _Data);
        lv.setAdapter(adapter2);
        //****************************** On Item Click ************
        lv.setOnItemClickListener((parent, view, position, id) -> {

            if (adapter2.getItem(position).id == 1) {
                Intent i = new Intent(activity, MyOrders.class);
                startActivity(i);
                //overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
            }
            if (adapter2.getItem(position).id == 2) {
                Intent i = new Intent(activity, MyFavs.class);
                startActivity(i);
                //overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
            }
            if (adapter2.getItem(position).id == 3) {
                Intent i = new Intent(activity, SelectAddress.class);
                startActivity(i);
                //overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
            }
            if (adapter2.getItem(position).id == 4) {
                Intent i = new Intent(activity, UpdateProfile.class);
                startActivity(i);
                //overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
            }


            if (adapter2.getItem(position).id == 5) {
                LoginHolder.getInstance().setData("logout");
                FaceIdHolder.getInstance().setData("0");

                Login = getSharedPreferences(Values.SharedPreferences_FileName, 0);
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
                Intent i = new Intent(activity, Home.class);
                startActivity(i);
                //overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
            }


        });

        ll_categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountMenu.this, AllCats.class);
                startActivity(intent);
            }
        });

        ll_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountMenu.this, Home.class);
                startActivity(intent);
            }
        });

        ll_offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountMenu.this, Offers.class);
                startActivity(intent);
            }
        });

        rl_basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountMenu.this, Cart.class);
                startActivity(intent);
            }
        });
        //*********************************************************
        loadMenuItems();
    }

    private void init() {
        /*if (LangHolder.getInstance().getData().equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }*/
        lang = getSharedPreferences(Values.SharedPreferences_FileNameLangSelect, 0)
                .getString(Values.SharedPreferences_FileNameLangSelect, null);
        lang = Values.SharedPreferences_FileNameLangSelect;
        // ********************* Call Views  *****************
        lv = findViewById(R.id.listViewOrders);
        App_Version = findViewById(R.id.App_Version);
        tv_basketCount = findViewById(R.id.tv_basketCount);
        ll_profile = findViewById(R.id.ll_profile);
        rl_basket = findViewById(R.id.rl_basket);
        ll_offers = findViewById(R.id.ll_offers);
        ll_categories = findViewById(R.id.ll_categories);
        ll_home = findViewById(R.id.ll_home);

        // ********************* Set Fonts *****************
        fontMedim = Typeface.createFromAsset(activity.getAssets(), "fonts/GE_SS_Two_Medium.otf");
        fontLight = Typeface.createFromAsset(activity.getAssets(), "fonts/GE_SS_Two_Light.otf");
        fontUltra = Typeface.createFromAsset(activity.getAssets(), "fonts/ArbFONTS-GESSTextUltraLight-UltraLight.otf");

        //********************* Pass Fonts *****************
        App_Version.setTypeface(fontUltra);
        // *************************************************
    }

    private void loadMenuItems() {
        String[] navMenuTitles = getResources().getStringArray(R.array.nav_account);

        adapter2.add(new DataInListIcons(navMenuTitles[0],
                "drawable://" + R.mipmap.menu_a_01, 0));

        adapter2.add(new DataInListIcons(navMenuTitles[1],
                "drawable://" + R.mipmap.menu_a_01, 1));

        adapter2.add(new DataInListIcons(navMenuTitles[2],
                "drawable://" + R.mipmap.menu_a_02, 2));

        adapter2.add(new DataInListIcons(navMenuTitles[3],
                "drawable://" + R.mipmap.menu_a_03, 3));

        adapter2.add(new DataInListIcons(navMenuTitles[4],
                "drawable://" + R.mipmap.menu_a_04, 4));

        adapter2.add(new DataInListIcons(navMenuTitles[5],
                "drawable://" + R.mipmap.menu_a_05, 5));


        adapter2.notifyDataSetChanged();


    }


    @Override
    protected void onResume() {
        CartCount();
        //********************** Footer Menu  ********************
        BottomNavigationViewEx navigation = findViewById(R.id.navigation);
        navigation.enableAnimation(false);
        navigation.enableShiftingMode(false);
        navigation.setTypeface(fontLight);
        navigation.setTextSize(12);
        navigation.enableItemShiftingMode(false);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_account);

        //************************** Login Mode **********************
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
                startActivity(new Intent(activity, Login.class));
            }
        } catch (Exception e) {

        }
        //******************* Offline Mode **********************
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

    private void CartCount() {
        OkHttpClient client = new OkHttpClient();
        String Url = Values.Link_service + "visitors/cart/count/" + LangHolder.getInstance().getData() + "/v1";
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

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                startActivity(new Intent(activity, Home.class));
                return true;
            case R.id.navigation_cats:
                startActivity(new Intent(activity, AllCats.class));
                return true;
            case R.id.navigation_cart:
                startActivity(new Intent(activity, Cart.class));
                return true;
            case R.id.navigation_offers:
                startActivity(new Intent(activity, Offers.class));
                // overridePendingTransition(R.anim.slide_bottom_to_top, R.anim.slide_top_to_bottom);
                return true;
            case R.id.navigation_account:
                // startActivity(new Intent(activity, AccountMenu.class));
                // overridePendingTransition(R.anim.slide_bottom_to_top, R.anim.slide_top_to_bottom);
                return true;
        }
        return false;
    };


    public void gotoBack(View v) {
        onBackPressed();
    }

    public void gotoUpload(View v) {
        Intent i = new Intent(activity, AccountMenu.class);
        startActivity(i);
        //overridePendingTransition(R.anim.fadein_fast, R.anim.fadeout_fast);
    }

}
