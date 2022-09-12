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
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

import constants.Values;
import customLists.CustomListMenu;
import dataInLists.DataInCall;
import dataInLists.DataInGlobal;
import dataInLists.DataInListIcons;
import helpers.LangHolder;
import helpers.NetWork;
import helpers.OnlineHolder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import utils.Dialogs;


public class Menu extends Activity {
    SharedPreferences Login;
    Activity activity = Menu.this;

    ArrayList<DataInListIcons> _Data = new ArrayList<>();
    ListView lv;
    CustomListMenu adapter2;
    String lang;
    Typeface fontMedim, fontLight, fontUltra;
    TextView App_Version, tv_basketCount;
    private LinearLayout ll_profile, ll_offers, ll_home, ll_categories;
    private RelativeLayout rl_basket;
    private SharedPreferences prefs;

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
        /*if (LangHolder.getInstance().getData().equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }*/

        lang = getSharedPreferences(Values.SharedPreferences_FileNameLangSelect, 0)
                .getString(Values.SharedPreferences_FileNameLangSelect, null);
        lang = Values.SharedPreferences_FileNameLangSelect;

        fontMedim = Typeface.createFromAsset(activity.getAssets(), "fonts/GE_SS_Two_Medium.otf");
        fontLight = Typeface.createFromAsset(activity.getAssets(), "fonts/GE_SS_Two_Light.otf");
        fontUltra = Typeface.createFromAsset(activity.getAssets(), "fonts/ArbFONTS-GESSTextUltraLight-UltraLight.otf");


        lv = findViewById(R.id.listViewOrders);
        App_Version = findViewById(R.id.App_Version);
        tv_basketCount = findViewById(R.id.tv_basketCount);
        tv_basketCount = findViewById(R.id.tv_basketCount);
        ll_profile = findViewById(R.id.ll_profile);
        rl_basket = findViewById(R.id.rl_basket);
        ll_offers = findViewById(R.id.ll_offers);
        ll_categories = findViewById(R.id.ll_categories);
        ll_home = findViewById(R.id.ll_home);

        App_Version.setTypeface(fontUltra);

        App_Version.setText(getResources().getString(R.string.App_Version) + " : " + Values.App_Version + "");
        adapter2 = new CustomListMenu(activity, _Data);

        lv.setAdapter(adapter2);
        lv.setOnItemClickListener((parent, view, position, id) -> {

            if (adapter2.getItem(position).id == 1) {
                Intent i = new Intent(activity, Home.class);
                startActivity(i);
                //overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
            }
            if (adapter2.getItem(position).id == 2) {
                Intent i = new Intent(activity, AccountMenu.class);
                startActivity(i);
                //overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
            }
            if (adapter2.getItem(position).id == 3) {
                Intent i = new Intent(activity, About.class);
                startActivity(i);
                //overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
            }
            if (adapter2.getItem(position).id == 4) {
                Intent i = new Intent(activity, AllCats.class);
                startActivity(i);
                //overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
            }
            if (adapter2.getItem(position).id == 5) {
                Intent i = new Intent(activity, Offers.class);
                startActivity(i);
                //overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
            }
            if (adapter2.getItem(position).id == 6) {
                Intent i = new Intent(activity, Cart.class);
                startActivity(i);
                //overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
            }
            if (adapter2.getItem(position).id == 7) {
                Intent i = new Intent(activity, ReturnPolicy.class);
                startActivity(i);
                //overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
            }
            if (adapter2.getItem(position).id == 8) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.usmart.com.rummanah");
                intent.putExtra(Intent.EXTRA_SUBJECT, activity.getResources().getString(R.string.app_name));
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, "Share"));
            }

            if (adapter2.getItem(position).id == 9) {
                Intent i = new Intent(activity, Conditions.class);
                startActivity(i);
                //overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
            }

            if (adapter2.getItem(position).id == 10) {
                Intent i = new Intent(activity, Notis.class);
                startActivity(i);
                //overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
            }

            if (adapter2.getItem(position).id == 11) {
                Intent i = new Intent(activity, Contact.class);
                startActivity(i);
                //overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
            }
            if (adapter2.getItem(position).id == 12) {
                //Toast.makeText(Menu.this, "سيتم تغيير لغة التطبيق", Toast.LENGTH_SHORT).show();
                //Dialogs.selectLang(activity);
                selectLang(activity);
            }
            if (adapter2.getItem(position).id == 13) {
                //Toast.makeText(Menu.this, "سيتم تشغيل طريقة تقييم التطبيق", Toast.LENGTH_SHORT).show();
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

        ll_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, Home.class);
                startActivity(intent);
            }
        });

        ll_categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, AllCats.class);
                startActivity(intent);
            }
        });

        ll_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, AccountMenu.class);
                startActivity(intent);
            }
        });

        ll_offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, Offers.class);
                startActivity(intent);
            }
        });

        rl_basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, Cart.class);
                startActivity(intent);
            }
        });

        loadData();

    }

    private void loadData() {
        String[] navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);


        adapter2.add(new DataInListIcons(navMenuTitles[0],
                "drawable://" + R.mipmap.menu_01, 0));

        adapter2.add(new DataInListIcons(navMenuTitles[1],
                "drawable://" + R.mipmap.menu_01, 1));

        adapter2.add(new DataInListIcons(navMenuTitles[5],
                "drawable://" + R.mipmap.menu_05, 5));

        adapter2.add(new DataInListIcons(navMenuTitles[4],
                "drawable://" + R.mipmap.menu_04, 4));

        adapter2.add(new DataInListIcons(navMenuTitles[6],
                "drawable://" + R.mipmap.menu_06, 6));

        adapter2.add(new DataInListIcons(navMenuTitles[2],
                "drawable://" + R.mipmap.menu_02, 2));

        adapter2.add(new DataInListIcons(navMenuTitles[10],
                "drawable://" + R.mipmap.menu_10, 10));

        adapter2.add(new DataInListIcons(navMenuTitles[12],
                "drawable://" + R.mipmap.translate, 12));

        adapter2.add(new DataInListIcons(navMenuTitles[13],
                "drawable://" + R.mipmap.review, 13));

        adapter2.add(new DataInListIcons(navMenuTitles[8],
                "drawable://" + R.mipmap.menu_08, 8));

        adapter2.add(new DataInListIcons(navMenuTitles[7],
                "drawable://" + R.mipmap.menu_07, 7));

        adapter2.add(new DataInListIcons(navMenuTitles[9],
                "drawable://" + R.mipmap.menu_09, 9));

        adapter2.add(new DataInListIcons(navMenuTitles[3],
                "drawable://" + R.mipmap.menu_03, 3));

        adapter2.add(new DataInListIcons(navMenuTitles[11],
                "drawable://" + R.mipmap.menu_11, 11));


        adapter2.notifyDataSetChanged();


    }

    public void selectLang(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.msg_noads_options);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        TextView Yes = dialog.findViewById(R.id.btn_2);
        TextView No = dialog.findViewById(R.id.btn_1);
        TextView Cancel = dialog.findViewById(R.id.btn_3);

        Yes.setText(R.string.Arabic);
        No.setText(R.string.English);
        Yes.setOnClickListener(v -> {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Locale arLocale = new Locale("ar");
                    Locale.setDefault(arLocale);
                    Configuration configuration = new Configuration();
                    configuration.locale = arLocale;
                    getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

                    prefs = getSharedPreferences(Values.SharedPreferences_FileName, 0);
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putString(Values.SharedPreferences_FileName, "ar");
                    edit.apply();
                    Values.SharedPreferences_FileNameLangSelect = "ar";

                    /*Intent intent = new Intent(activity, Home.class);
                    activity.finish();
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.fadein_fast, R.anim.fadeout_fast);*/
                    Intent i = new Intent(activity, Home.class);
                    startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            }, 500);
            dialog.dismiss();
        });

        No.setOnClickListener(v -> {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Locale enLocale = new Locale("en");
                    Locale.setDefault(enLocale);
                    Configuration configuration = new Configuration();
                    configuration.locale = enLocale;
                    getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

                    prefs = getSharedPreferences(Values.SharedPreferences_FileName, 0);
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putString(Values.SharedPreferences_FileName, "en");
                    edit.apply();
                    Values.SharedPreferences_FileNameLangSelect = "en";

                    /*Intent intent = new Intent(activity, Home.class);
                    activity.finish();
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.fadein_fast, R.anim.fadeout_fast);*/
                    Intent i = new Intent(activity, Home.class);
                    startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            }, 500);
            dialog.dismiss();
        });
        Cancel.setOnClickListener(v -> {
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
        navigation.setSelectedItemId(R.id.navigation_account);

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
        Intent i = new Intent(activity, Menu.class);
        startActivity(i);
        //overridePendingTransition(R.anim.fadein_fast, R.anim.fadeout_fast);
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

}
