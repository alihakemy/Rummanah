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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ethanhua.skeleton.ViewSkeletonScreen;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import constants.Values;
import customLists.CustomListOrderProducts;
import dataInLists.DataInLogin;
import dataInLists.DataInOrderItem;
import helpers.FaceIdHolder;
import helpers.LangHolder;
import helpers.LoginHolder;
import helpers.NetWork;
import helpers.OnlineHolder;
import helpers.UserTokenHolder;


public class OrderDetails extends Activity {

    Activity activity = OrderDetails.this;
    SharedPreferences Login;
    DataInOrderItem _Data = new DataInOrderItem();

    ExpandableHeightListView lv;
    CustomListOrderProducts adapter2;
    Typeface fontMedim, fontLight, fontUltra, fontPoppinsMed, fontCeraPro;
    TextView MainTitle, tv_OrderIDTxt, tv_Order, tv_Date, tv_OrderSysTxt, tv_OrderSys, tv_CountTxt, tv_Count,
            tv_OrderDetails, tv_AddressTxt, tv_Address, tv_PaymentTxt, tv_Payment, tv_TotalOrdersTxt, tv_TotalOrders,
            tv_TotalOrders_Curr, tv_Total, tv_TotalTxt, tv_Total_Curr, tv_TotalDeliveryTxt, tv_TotalDelivery_Curr,
            tv_TotalDelivery, tv_Status, tvStatus;
    ViewSkeletonScreen skeletonScreen;
    ImageView HideAll;
    ProgressBar prog;
    public int ID;
    public String lang;

    Locale locale = new Locale("en", "UK");

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
        setContentView(R.layout.activity_order_details);
        /*if (LangHolder.getInstance().getData().equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }*/
        lang = getSharedPreferences(Values.SharedPreferences_FileNameLangSelect, 0)
                .getString(Values.SharedPreferences_FileNameLangSelect, null);
        lang = Values.SharedPreferences_FileNameLangSelect;
        ID = getIntent().getExtras().getInt("ID");

        fontMedim = Typeface.createFromAsset(getAssets(), "fonts/GE_SS_Two_Medium.otf");
        fontLight = Typeface.createFromAsset(getAssets(), "fonts/GE_SS_Two_Light.otf");
        fontUltra = Typeface.createFromAsset(getAssets(), "fonts/ArbFONTS-GESSTextUltraLight-UltraLight.otf");
        fontCeraPro = Typeface.createFromAsset(getAssets(), "fonts/cera_pro_med.ttf");

        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);

        MainTitle = findViewById(R.id.MainTitle);
        lv = findViewById(R.id.lvItems);
        lv.setExpanded(true);

        adapter2 = new CustomListOrderProducts(activity, new ArrayList<>());
        lv.setAdapter(adapter2);

        MainTitle = findViewById(R.id.MainTitle);
        tv_OrderIDTxt = findViewById(R.id.tv_OrderIDTxt);
        tv_Order = findViewById(R.id.tv_Order);
        tv_Date = findViewById(R.id.tv_Date);
        tv_OrderSysTxt = findViewById(R.id.tv_OrderSysTxt);
        tv_OrderSys = findViewById(R.id.tv_OrderSys);
        tv_CountTxt = findViewById(R.id.tv_CountTxt);
        tv_Count = findViewById(R.id.tv_Count);
        tv_OrderDetails = findViewById(R.id.tv_OrderDetails);
        tv_AddressTxt = findViewById(R.id.tv_AddressTxt);
        tv_Address = findViewById(R.id.tv_Address);
        tv_PaymentTxt = findViewById(R.id.tv_PaymentTxt);
        tv_Payment = findViewById(R.id.tv_Payment);
        tv_TotalOrdersTxt = findViewById(R.id.tv_TotalOrdersTxt);
        tv_TotalOrders = findViewById(R.id.tv_TotalOrders);
        tv_TotalOrders_Curr = findViewById(R.id.tv_TotalOrders_Curr);
        tv_TotalTxt = findViewById(R.id.tv_TotalTxt);
        tv_Total = findViewById(R.id.tv_Total);
        tv_Total_Curr = findViewById(R.id.tv_Total_Curr);
        tv_TotalDeliveryTxt = findViewById(R.id.tv_TotalDeliveryTxt);
        tv_TotalDelivery_Curr = findViewById(R.id.tv_TotalDelivery_Curr);
        tv_TotalDelivery = findViewById(R.id.tv_TotalDelivery);
        tv_Status = findViewById(R.id.tv_Status);
        tvStatus = findViewById(R.id.tvStatus);

        MainTitle.setText(R.string.OrderDetails);

        MainTitle.setTypeface(fontMedim);
        tv_OrderIDTxt.setTypeface(fontMedim);
        //  tv_Order.setTypeface(fontCeraPro);
        // tv_Date.setTypeface(fontLight);
        tv_OrderSysTxt.setTypeface(fontLight);
        // tv_OrderSys.setTypeface(fontCeraPro);
        tv_CountTxt.setTypeface(fontLight);
        //  tv_Count.setTypeface(fontCeraPro);
        tv_OrderDetails.setTypeface(fontLight);
        tv_AddressTxt.setTypeface(fontLight);
        tv_Address.setTypeface(fontLight);
        tv_PaymentTxt.setTypeface(fontLight);
        tv_Payment.setTypeface(fontLight);
        tv_TotalOrdersTxt.setTypeface(fontLight);
        // tv_TotalOrders.setTypeface(fontCeraPro);
        tv_TotalOrders_Curr.setTypeface(fontLight);
        tv_TotalTxt.setTypeface(fontLight);
        tv_Total_Curr.setTypeface(fontLight);
        //   tv_Total.setTypeface(fontCeraPro);
        tv_TotalDeliveryTxt.setTypeface(fontLight);
        tv_TotalDelivery_Curr.setTypeface(fontLight);
        tv_Status.setTypeface(fontLight);
        //  tv_TotalDelivery.setTypeface(fontCeraPro);

        loadOrder();

    }


    private void loadOrder() {
        AsyncHttpClient client = new AsyncHttpClient();
        String Url;

        Url = Values.Link_service + "orders/" + ID + "/" + lang + "/v1";

        client.addHeader("Authorization",  UserTokenHolder.getInstance().getData().access_token);

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

                try {
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInOrderItem>() {
                    }.getType();
                    _Data = g.fromJson(arg1, t);

                    tv_Order.setText(_Data.data.order_number + "");
                    tv_Date.setText(_Data.data.date + "");
                    tv_OrderSys.setText(_Data.data.order_number + "");
                    tv_Count.setText(_Data.data.products.size() + "");
                    tv_Address.setText(_Data.data.address.street + " " + _Data.data.address.building + " " +
                            _Data.data.address.gaddah);
                    tv_TotalOrders.setText(String.format("%.3f",_Data.data.subtotal_price) + "");
                    tv_TotalDelivery.setText(String.format("%.3f",_Data.data.delivery_cost) + "");
                    tv_Total.setText(String.format("%.3f",_Data.data.total_price) + "");

                    if (_Data.data.status == 1) {
                        tvStatus.setText(R.string.OrderCurrent);
                    } else if (_Data.data.status == 2) {
                        tvStatus.setText(R.string.OrderDone);
                    } else if (_Data.data.status == 3) {
                        tvStatus.setText(R.string.OrderCanceled);
                    }

                    if (_Data.data.payment_method == 1) {
                        tv_Payment.setText(R.string.KNet);
                    } else if (_Data.data.payment_method == 2) {
                        tv_Payment.setText(R.string.KNetHome);
                    } else if (_Data.data.payment_method == 3) {
                        tv_Payment.setText(R.string.Cash);
                    }


                    adapter2.addAll(_Data.data.products);
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
//
//        try {
//            if (LoginHolder.getInstance().getData().equals("login")) {
//                SharedPreferences.Editor editor = Login.edit();
//                editor.putString("isLogin", "login");
//                editor.putString("UserID", FaceIdHolder.getInstance().getData());
//                editor.putString("Token", UserTokenHolder.getInstance().getData().access_token);
//                editor.putString("Token_Type", UserTokenHolder.getInstance().getData().token_type);
//                editor.putString("Token_Exp", UserTokenHolder.getInstance().getData().expires_in);
//                editor.commit();
//
//            } else {
//                Login = getSharedPreferences(Values.SharedPreferences_FileName, 0);
//                LoginHolder.getInstance().setData("logout");
//                FaceIdHolder.getInstance().setData("0");
//                SharedPreferences.Editor editor = Login.edit();
//                editor.putString("isLogin", "logout");
//                editor.putString("UserID", "0");
//                editor.putString("Token", "");
//                editor.putString("Token_Type", "");
//                editor.putString("Token_Exp", "");
//                editor.commit();
//                DataInLogin.Tokens Token = new DataInLogin.Tokens();
//                Token.access_token = "";
//                Token.token_type = "";
//                Token.expires_in = "";
//                UserTokenHolder.getInstance().setData(Token);
//                startActivity(new Intent(activity, Login.class));
//            }
//        } catch (Exception e) {
//
//        }
//        try {
//            if (OnlineHolder.getInstance().getData().equals("1")) {
//                OnlineHolder.getInstance().setData("0");
//                finish();
//                startActivity(getIntent());
//                //overridePendingTransition(R.anim.fadein_fast, R.anim.fadeout_fast);
//
//            } else if (NetWork.isNetworkAvailable(activity) == false) {
//                NetWork.gotoError(activity);
//            }
//        } catch (Exception e) {
//
//        }

        super.onResume();

    }


    public void gotoBack(View v) {

        startActivity(new Intent(activity , Home.class));


    }


}
