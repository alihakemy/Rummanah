package com.usmart.com.rummanah;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.ViewSkeletonScreen;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

import constants.Values;
import customLists.CustomListNoti;
import dataInLists.DataInNoti;
import helpers.LangHolder;
import helpers.LoginHolder;
import helpers.NetWork;
import helpers.OnlineHolder;
import helpers.UserTokenHolder;

public class Notis extends Activity {
    Activity activity = Notis.this;

    DataInNoti _Data = new DataInNoti();
    ListView lv;
    CustomListNoti adapter2;
    Typeface fontMedim, fontLight, fontUltra;
    ViewSkeletonScreen skeletonScreen;
    ImageView HideAll;
    ProgressBar prog;
    TextView noData, MainTitle;
    String lang;
    int Page = 1;


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
        setContentView(R.layout.activity_listview_noti);
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

        MainTitle = findViewById(R.id.MainTitle);
        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);
        lv = findViewById(R.id.listViewOrders);
        noData = findViewById(R.id.noData);


        adapter2 = new CustomListNoti(activity, new ArrayList<>());
        lv.setAdapter(adapter2);


        noData.setTypeface(fontUltra);
        MainTitle.setTypeface(fontLight);
        MainTitle.setText(R.string.Notifications);
        lv.setOnItemClickListener((parent1, view, position, id) -> {
          /*  Intent i = new Intent(activity, Doctor.class);
            i.putExtra("ID", _Data.data.data.get(position).id);
            startActivity(i);*/
            //  Log.i("TestApp_Doc", _Data.data.data.get(position).id + "");
            //overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);

        });


        lv.setOnScrollListener(new AbsListView.OnScrollListener() {

            private int currentFirstVisibleItem;
            private int currentVisibleItemCount;
            private int totalItemCount;
            private int currentScrollState;

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                this.currentFirstVisibleItem = firstVisibleItem;
                this.currentVisibleItemCount = visibleItemCount;
                this.totalItemCount = totalItemCount;
            }

            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                this.currentScrollState = scrollState;
                this.isScrollCompleted();

            }

            private void isScrollCompleted() {
                if (this.currentVisibleItemCount > 0 && this.currentScrollState == SCROLL_STATE_IDLE
                        && this.totalItemCount == (currentFirstVisibleItem + currentVisibleItemCount)) {
                   /* if (_Data.data.next_page_url != null) {
                        Page++;
                        //  loadCats(Page, Sort);
                    }*/
                }
                if (this.currentScrollState == 0) {

                }

            }
        });
        loadCats();

    }


    private void loadCats() {
        AsyncHttpClient client = new AsyncHttpClient();
        String Url;

        Url = Values.Link_service + "user/notifications/" + lang + "/v1";
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
                //   HideAll.setVisibility(View.VISIBLE);
                //   prog.setVisibility(View.VISIBLE);
                skeletonScreen = Skeleton.bind(lv)
                        .load(R.layout.singel_load_list)
                        .show();
            }

            @Override
            public void onSuccess(int arg0, String arg1) {
                // TODO Auto-generated method stub

                super.onSuccess(arg0, arg1);
                Log.i("TestApp", arg1);
                try {
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInNoti>() {
                    }.getType();
                   /* adapter2.clear();
                    adapter2.notifyDataSetChanged();*/

                    _Data = g.fromJson(arg1, t);
                    if (_Data.code == 200) {
                        if (_Data.data.size() > 0) {
                            lv.setVisibility(View.VISIBLE);
                            noData.setVisibility(View.GONE);
                            adapter2.addAll(_Data.data);
                            adapter2.notifyDataSetChanged();
                        } else {
                            lv.setVisibility(View.GONE);
                            noData.setVisibility(View.VISIBLE);
                        }
                    } else {
                        loadMsg(_Data.message);


                    }


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
                //  HideAll.setVisibility(View.GONE);
                //  prog.setVisibility(View.GONE);
                skeletonScreen.hide();
                super.onFinish();

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
        super.onBackPressed();
    }

}
