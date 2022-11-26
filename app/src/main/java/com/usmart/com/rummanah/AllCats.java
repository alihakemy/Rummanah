package com.usmart.com.rummanah;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.ViewSkeletonScreen;
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
import customLists.CustomListMainCats;
import customLists.CustomListSubCats;
import dataInLists.DataInCats;
import dataInLists.DataInGlobal;
import dataInLists.DataInSubCats;
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
import utils.ExpandableHeightGridView;

public class AllCats extends Activity {
    Activity activity = AllCats.this;
    DataInCats _Data;
    DataInSubCats _DataCats;
    Typeface fontMedim, fontLight, fontUltra;
    ViewSkeletonScreen skeletonScreen;
    ImageView HideAll;
    ProgressBar prog;
    TextView MainTitle, CatsIcon, CatsAll, tv_basketCount;
    ExpandableHeightGridView lvCats, lvMain;
    CustomListMainCats adapterCats;
    CustomListSubCats adapterSubCats;
    SearchView searchV;
    int MainCat;
    String lang;
    private LinearLayout ll_profile, ll_offers, ll_home, ll_categories;
    private RelativeLayout rl_basket;

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
        setContentView(R.layout.activity_allcats);
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

        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);

        MainTitle = findViewById(R.id.MainTitle);
        lvMain = findViewById(R.id.lvMain);
        lvCats = findViewById(R.id.lvCats);
        CatsIcon = findViewById(R.id.CatsIcon);
        CatsAll = findViewById(R.id.CatsAll);
        tv_basketCount = findViewById(R.id.tv_basketCount);
        ll_profile = findViewById(R.id.ll_profile);
        rl_basket = findViewById(R.id.rl_basket);
        ll_offers = findViewById(R.id.ll_offers);
        ll_categories = findViewById(R.id.ll_categories);
        ll_home = findViewById(R.id.ll_home);

        adapterCats = new CustomListMainCats(activity, new ArrayList<>());
        adapterSubCats = new CustomListSubCats(activity, new ArrayList<>());

        lvMain.setExpanded(true);
        lvCats.setExpanded(true);

        CatsIcon.setTypeface(fontMedim);
        CatsAll.setTypeface(fontLight);

        lvMain.setAdapter(adapterCats);
        lvCats.setAdapter(adapterSubCats);

        CatsAll.setOnClickListener(v -> {
            Intent i = new Intent(activity, Products.class);
            i.putExtra("MainID", MainCat);
            startActivity(i);
        });

        ll_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllCats.this, Home.class);
                startActivity(intent);
            }
        });

        ll_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllCats.this, AccountMenu.class);
                startActivity(intent);
            }
        });

        ll_offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllCats.this, Offers.class);
                startActivity(intent);
            }
        });

        rl_basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllCats.this, Cart.class);
                startActivity(intent);
            }
        });

        lvMain.setOnItemClickListener((parent, view, position, id) -> {

            adapterSubCats.clear();
            adapterSubCats.notifyDataSetChanged();
            for (int i = 0; i < _Data.data.size(); i++) {
                _Data.data.get(i).selected = false;
            }
            _Data.data.get(position).selected = true;
            MainCat = _Data.data.get(position).id;
            adapterCats.notifyDataSetChanged();
            CatsIcon.setText(_Data.data.get(position).title);
            loadSubCats(_Data.data.get(position).id);
        });

        lvCats.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(activity, Products.class);
            i.putExtra("MainID", MainCat);
            i.putExtra("SubCatID", _DataCats.data.sub_categories.get(position).id);
            startActivity(i);
        });
        searchV = findViewById(R.id.SearchCity);

        MainTitle.setText(R.string.Cats);
        MainTitle.setTypeface(fontMedim);

        setupSearchView();
        loadMainCats();
    }

    private void setupSearchView() {
        searchV.setBackgroundResource(R.drawable.btn_gray_radius5);
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
                    adapterSubCats.clear();
                    adapterSubCats.addAll(_DataCats.data.sub_categories);
                } else {
                    adapterSubCats.getFilter().filter(cs);
                }

                return true;
            }
        });
        searchV.setSubmitButtonEnabled(false);
        // searchV.setFocusable(true);
        searchV.setFocusableInTouchMode(true);
        searchV.setIconified(false);
        //searchV.setQueryHint(getResources().getString(R.string.SearchTxt));

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchV.getWindowToken(), 0);
        searchV.clearFocus();
    }


    private void loadMainCats() {
        AsyncHttpClient client = new AsyncHttpClient();
        String Url;

        Url = Values.Link_service + "categories/" + lang + "/v1/";
        Log.i("TestApp", Url);
        client.addHeader("Content-Type", "application/json");
        try {
            if (LoginHolder.getInstance().getData().equals("login")) {
                client.addHeader("Authorization",  UserTokenHolder.getInstance().getData().access_token);
            } else {
                client.addHeader("Authorization", "" + Values.Authorization_User);
            }
        }catch (Exception e){
            client.addHeader("Authorization", "" + Values.Authorization_User);

        }


        client.get(Url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                skeletonScreen = Skeleton.bind(lvCats)
                        .load(R.layout.singel_load_list)
                        .show();
            }

            @Override
            public void onSuccess(int arg0, String arg1) {
                // TODO Auto-generated method stub
                Log.i("TestApp", arg1);
                super.onSuccess(arg0, arg1);
                try {
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInCats>() {
                    }.getType();
                    _Data = g.fromJson(arg1, t);
                    _Data.data.get(0).selected = true;
                    MainCat = _Data.data.get(0).id;
                    adapterCats.addAll(_Data.data);
                    adapterCats.notifyDataSetChanged();

                    CatsIcon.setText(_Data.data.get(0).title);
                    loadSubCats(_Data.data.get(0).id);

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
        Log.i("TestApp", Url);
        client.addHeader("Content-Type", "application/json");
        if (LoginHolder.getInstance().getData().equals("login")) {
            client.addHeader("Authorization",  UserTokenHolder.getInstance().getData().access_token);
        } else {
            client.addHeader("Authorization", "" + Values.Authorization_User);
        }

        client.get(Url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                skeletonScreen = Skeleton.bind(lvCats)
                        .load(R.layout.singel_load_list)
                        .show();
            }

            @Override
            public void onSuccess(int arg0, String arg1) {
                // TODO Auto-generated method stub
                Log.i("TestApp", arg1);
                super.onSuccess(arg0, arg1);
                try {
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInSubCats>() {
                    }.getType();
                    _DataCats = g.fromJson(arg1, t);
                    adapterSubCats.addAll(_DataCats.data.sub_categories);
                    adapterSubCats.notifyDataSetChanged();

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


    private void CartCount() {
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
        super.onBackPressed();
    }

    public void gotoMenu(View v) {
        startActivity(new Intent(activity, Menu.class));
    }

    public void gotoCart(View v) {

    }

    public void gotoSearch(View v) {

    }
}
