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
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ethanhua.skeleton.ViewSkeletonScreen;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Locale;

import constants.Values;
import dataInLists.DataInAddressItem;
import dataInLists.DataInCart;
import dataInLists.DataInGlobal;
import dataInLists.DataInLogin;
import dataInLists.DataInReservation;
import helpers.FaceIdHolder;
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

public class Checkout extends Activity {
    Activity activity = Checkout.this;
    private static final String TAG = Checkout.class.getSimpleName();
    SharedPreferences Login;
    DataInCart _Data = new DataInCart();
    DataInReservation _DataRes = new DataInReservation();
    Typeface fontMedim, fontLight, fontUltra, fontPoppinsMed;
    ViewSkeletonScreen skeletonScreen;
    ImageView HideAll, iv_KNet, iv_KNet_Home, CancelALL;
    ProgressBar prog;
    TextView MainTitle, Number_1, CartItems, Number_2, Number_3, tv_PriceDetails, tv_ProductCount, tv_Products,
            tv_TotalOrdersTxt, tv_DeliveryTxt, tv_TotalTxt, tv_TotalOrdersPrice, tv_TotalOrders_Curr, tv_Price_Delivery,
            tv_Price_Delivery_Curr, tv_Total, tv_Total_Curr, tv_Address, tv_City, tv_ShippingDetails,
            tv_Payment, tv_KNet, tv_KNet_Home, tv_Cash;
    CheckBox Chk_KNet, Chk_KNet_Home, Chk_Cash;
    Button btn_Set_Checkout;
    int AddressID, PaymentMethod;

    String lang, AddressName;


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
        setContentView(R.layout.activity_checkout);
        fontMedim = Typeface.createFromAsset(activity.getAssets(), "fonts/GE_SS_Two_Medium.otf");
        fontLight = Typeface.createFromAsset(activity.getAssets(), "fonts/GE_SS_Two_Light.otf");
        fontUltra = Typeface.createFromAsset(activity.getAssets(), "fonts/ArbFONTS-GESSTextUltraLight-UltraLight.otf");
        fontPoppinsMed = Typeface.createFromAsset(activity.getAssets(), "fonts/Poppins-Medium.ttf");
        /*if (LangHolder.getInstance().getData().equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }*/
        lang = getSharedPreferences(Values.SharedPreferences_FileNameLangSelect, 0)
                .getString(Values.SharedPreferences_FileNameLangSelect, null);
        lang = Values.SharedPreferences_FileNameLangSelect;

        PaymentMethod = 0;
        AddressID = getIntent().getIntExtra("AddressID", 0);
        Log.d(TAG, "Value of AddressId: " + AddressID);

        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);

        MainTitle = findViewById(R.id.MainTitle);
        CancelALL = findViewById(R.id.CancelAll);
        Number_1 = findViewById(R.id.Number_1);
        CartItems = findViewById(R.id.CartItems);
        Number_2 = findViewById(R.id.Number_2);
        Number_3 = findViewById(R.id.Number_3);
        tv_PriceDetails = findViewById(R.id.tv_PriceDetails);
        tv_ProductCount = findViewById(R.id.tv_ProductCount);
        tv_Products = findViewById(R.id.tv_Products);
        tv_TotalOrdersTxt = findViewById(R.id.tv_TotalOrdersTxt);
        tv_DeliveryTxt = findViewById(R.id.tv_DeliveryTxt);
        tv_TotalTxt = findViewById(R.id.tv_TotalTxt);
        tv_TotalOrdersPrice = findViewById(R.id.tv_TotalOrdersPrice);
        tv_TotalOrders_Curr = findViewById(R.id.tv_TotalOrders_Curr);
        tv_Price_Delivery = findViewById(R.id.tv_Price_Delivery);
        tv_Price_Delivery_Curr = findViewById(R.id.tv_Price_Delivery_Curr);
        tv_Total = findViewById(R.id.tv_Total);
        tv_Total_Curr = findViewById(R.id.tv_Total_Curr);
        tv_ShippingDetails = findViewById(R.id.tv_ShippingDetails);
        tv_Address = findViewById(R.id.tv_Address);
        tv_City = findViewById(R.id.tv_City);
        btn_Set_Checkout = findViewById(R.id.btn_Set_Checkout);
        tv_Payment = findViewById(R.id.tv_Payment);
        tv_KNet = findViewById(R.id.tv_KNet);
        tv_KNet_Home = findViewById(R.id.tv_KNet_Home);
        tv_Cash = findViewById(R.id.tv_Cash);
        iv_KNet = findViewById(R.id.iv_KNet);
        iv_KNet_Home = findViewById(R.id.iv_KNet_Home);
        Chk_KNet = findViewById(R.id.Chk_KNet);
        Chk_KNet_Home = findViewById(R.id.Chk_KNet_Home);
        Chk_Cash = findViewById(R.id.Chk_Cash);


        MainTitle.setTypeface(fontMedim);

        Number_1.setTypeface(fontPoppinsMed);
        CartItems.setTypeface(fontLight);
        Number_2.setTypeface(fontPoppinsMed);
        Number_3.setTypeface(fontPoppinsMed);
        tv_PriceDetails.setTypeface(fontMedim);
        tv_ProductCount.setTypeface(fontPoppinsMed);
        tv_Products.setTypeface(fontMedim);
        tv_TotalOrdersTxt.setTypeface(fontLight);
        tv_DeliveryTxt.setTypeface(fontLight);
        tv_TotalTxt.setTypeface(fontMedim);
        tv_TotalOrdersPrice.setTypeface(fontPoppinsMed);
        tv_TotalOrders_Curr.setTypeface(fontLight);
        tv_Price_Delivery.setTypeface(fontPoppinsMed);
        tv_Price_Delivery_Curr.setTypeface(fontLight);
        tv_Total.setTypeface(fontPoppinsMed);
        tv_Total_Curr.setTypeface(fontMedim);
        tv_ShippingDetails.setTypeface(fontMedim);
        tv_Address.setTypeface(fontLight);
        tv_City.setTypeface(fontLight);
        //   btn_Set_Checkout.setTypeface(fontLight);
        tv_Payment.setTypeface(fontMedim);
        tv_KNet.setTypeface(fontLight);
        tv_KNet_Home.setTypeface(fontLight);
        tv_Cash.setTypeface(fontLight);
        //*********************************************************
        tv_KNet.setOnClickListener(v -> {
            PaymentMethod = 1;
            Chk_KNet.setChecked(true);
            Chk_KNet_Home.setChecked(false);
            Chk_Cash.setChecked(false);

        });
        Chk_KNet.setOnClickListener(v -> {
            PaymentMethod = 1;
            Chk_KNet.setChecked(true);
            Chk_KNet_Home.setChecked(false);
            Chk_Cash.setChecked(false);

        });
        iv_KNet.setOnClickListener(v -> {
            PaymentMethod = 1;
            Chk_KNet.setChecked(true);
            Chk_KNet_Home.setChecked(false);
            Chk_Cash.setChecked(false);

        });

        //*********************

        tv_KNet_Home.setOnClickListener(v -> {
            PaymentMethod = 2;
            Chk_KNet.setChecked(false);
            Chk_KNet_Home.setChecked(true);
            Chk_Cash.setChecked(false);

        });
        Chk_KNet_Home.setOnClickListener(v -> {
            PaymentMethod = 2;
            Chk_KNet.setChecked(false);
            Chk_KNet_Home.setChecked(true);
            Chk_Cash.setChecked(false);

        });
        iv_KNet_Home.setOnClickListener(v -> {
            PaymentMethod = 2;
            Chk_KNet.setChecked(false);
            Chk_KNet_Home.setChecked(true);
            Chk_Cash.setChecked(false);

        });
        //*************************

        tv_Cash.setOnClickListener(v -> {
            PaymentMethod = 3;
            Chk_KNet.setChecked(false);
            Chk_KNet_Home.setChecked(false);
            Chk_Cash.setChecked(true);

        });
        Chk_Cash.setOnClickListener(v -> {
            PaymentMethod = 3;
            Chk_KNet.setChecked(false);
            Chk_KNet_Home.setChecked(false);
            Chk_Cash.setChecked(true);

        });

        //******************************************************
        btn_Set_Checkout.setOnClickListener(v -> {
            if (PaymentMethod == 0) {
                loadMsg(getResources().getString(R.string.ValidPayment));
            } else {
                //createOrder();
                sendCreateOrder();
            }
        });

        CancelALL.setOnClickListener(view -> {
            onBackPressed();
        });

        if (AddressID != 0) {
            getAddressItem(AddressID);
        } else {
            Intent i = new Intent(activity, SelectAddress.class);
            startActivityForResult(i, 11);
        }
        loadData();
    }

    private void loadData() {

        HideAll.setVisibility(View.VISIBLE);
        prog.setVisibility(View.VISIBLE);

        OkHttpClient client = new OkHttpClient();
        String Url = Values.Link_service + "visitors/cart/get/" + lang + "/v1";
        String json = new StringBuilder()
                .append("{")
                .append("\"unique_id\":\"" + Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID) + "\"")
                .append("}").toString();
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                json
        );
        String Auth;
        if (LoginHolder.getInstance().getData().equals("login")) {
            Auth = UserTokenHolder.getInstance().getData().token_type
                    + " " + UserTokenHolder.getInstance().getData().access_token;
        } else {
            Auth = Values.Authorization_User;
        }
        Request request = new Request.Builder()
                .url(Url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "" + Auth)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("TestApp_5", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInCart>() {
                    }.getType();

                    _Data = g.fromJson(response.body().string(), t);

                    runOnUiThread(() -> {
                        if (!_Data.success) {
                            /*if (_Data.code == 401) {
                                Intent i = new Intent(activity, Login.class);
                                startActivity(i);
                            } else {
                                loadMsg(_Data.message);
                            }*/
                        } else {
                            tv_ProductCount.setText("( " + _Data.data.count + " )");
                            tv_TotalOrdersPrice.setText(_Data.data.subtotal_price + "");
                            btn_Set_Checkout.setText(getResources().getString(R.string.Pay) + " " +
                                    String.format(java.util.Locale.US, "%.3f", _Data.data.subtotal_price) + " " + getResources().getString(R.string.DK));

                            getDeliveryCost(AddressID);
                        }
                    });

                } catch (Exception e) {
                }
            }

        });

        HideAll.setVisibility(View.GONE);
        prog.setVisibility(View.GONE);

    }

    private void getDeliveryCost(int AdID) {
        AsyncHttpClient client = new AsyncHttpClient();
        String Url;

        Url = Values.Link_service + "delivery_price/" + lang + "/v1?address_id=" + AdID;
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Authorization", "" + Values.Authorization_User);


        Log.i("TestApp", Url);
        client.get(Url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                super.onStart();

            }

            @Override
            public void onSuccess(int arg0, String arg1) {
                // TODO Auto-generated method stub
                super.onSuccess(arg0, arg1);
                DataInGlobal Res;
                try {
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInGlobal>() {
                    }.getType();
                    Res = g.fromJson(arg1, t);
                    tv_Price_Delivery.setText(Res.data.delivery_cost + "");

                    float Total = _Data.data.subtotal_price + Res.data.delivery_cost;
                    tv_Total.setText(Total + "");

                    btn_Set_Checkout.setText(getResources().getString(R.string.Pay) + " " +
                            String.format(java.util.Locale.US, "%.3f", Total) + " " + getResources().getString(R.string.DK));
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

    private void getAddressItem(int AdID) {
        AsyncHttpClient client = new AsyncHttpClient();
        String Url;

        String Auth;
        if (LoginHolder.getInstance().getData().equals("login")) {
            Auth = UserTokenHolder.getInstance().getData().token_type
                    + " " + UserTokenHolder.getInstance().getData().access_token;
        } else {
            Auth = Values.Authorization_User;
        }

        Url = Values.Link_service + "addresses/details/" + AdID + "/" + lang + "/v1";
        client.addHeader("Content-Type", "application/json");
        /*client.addHeader("Authorization", "" + UserTokenHolder.getInstance().getData().token_type
                + " " + UserTokenHolder.getInstance().getData().access_token);*/
        client.addHeader("Authorization", Auth);


        Log.i("TestApp", Url);
        client.get(Url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                super.onStart();

            }

            @Override
            public void onSuccess(int arg0, String arg1) {
                // TODO Auto-generated method stub
                super.onSuccess(arg0, arg1);
                DataInAddressItem Res;
                try {
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInAddressItem>() {
                    }.getType();
                    Res = g.fromJson(arg1, t);

                    tv_City.setText(Res.data.area_name);
                    tv_Address.setText(Res.data.street + " , " + Res.data.building + " , " + Res.data.gaddah);

                    loadData();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11) {
            AddressID = Integer.parseInt(data.getStringExtra("AddressID"));
            AddressName = data.getStringExtra("AddressName");
            getAddressItem(AddressID);
            if (AddressName.equals("")) {
                AddressName = getResources().getString(R.string.SetAddressDef);
            }
        }
    }

    private void createOrder() {

        HideAll.setVisibility(View.VISIBLE);
        prog.setVisibility(View.VISIBLE);
        btn_Set_Checkout.setEnabled(false);

        OkHttpClient client = new OkHttpClient();
        String Url = Values.Link_service + "orders/create/" + lang + "/v1";
        String json = new StringBuilder()
                .append("{")
                .append("\"unique_id\":\"" + Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID) + "\",")
                .append("\"address_id\":" + AddressID + ",")
                .append("\"payment_method\":" + PaymentMethod + "}").toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Log.d("TAG", "payment url: " + Url);
        Log.d("TAG", "payment body: " + json);

        String Auth;
        if (LoginHolder.getInstance().getData().equals("login")) {
            Auth = UserTokenHolder.getInstance().getData().token_type
                    + " " + UserTokenHolder.getInstance().getData().access_token;
        } else {
            Auth = Values.Authorization_User;
        }

        Request request = new Request.Builder()
                .url(Url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "" + Auth)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("TestApp_5", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInReservation>() {
                    }.getType();

                    //Log.i("TestApp",response.body().string()+"");
                    _DataRes.androidData = g.fromJson(response.body().string(), t);
                    Log.d("TAG", "feedback payment url: " + _DataRes.androidData.url);

                    runOnUiThread(() -> {
                        if (!_Data.success) {
                            if (_Data.code == 401) {
                                Intent i = new Intent(activity, Login.class);
                                startActivity(i);
                            } else {
                                loadMsg(_Data.message);
                            }
                        } else {
                            Log.d("TAG", "feedback payment url: " + _DataRes.androidData.url);
                            String paymentUrl = _DataRes.androidData.url;
                            if (PaymentMethod == 1) {
                                Intent i = new Intent(activity, Payment.class);
                                i.putExtra("ResData", paymentUrl);
                                startActivity(i);
                            } else {
                                Intent i = new Intent(activity, FinishOrder.class);
                                i.putExtra("ResData", _DataRes);
                                startActivity(i);
                            }

                        }
                    });

                } catch (Exception e) {
                }
            }

        });
        btn_Set_Checkout.setEnabled(true);
        HideAll.setVisibility(View.GONE);
        prog.setVisibility(View.GONE);

    }

    private void sendCreateOrder() {
        OkHttpClient client = new OkHttpClient();
        String Url = Values.Link_service + "orders/create/" + lang + "/v1";
        String json = new StringBuilder()
                .append("{")
                .append("\"unique_id\":\"" + Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID) + "\",")
                .append("\"address_id\":" + AddressID + ",")
                .append("\"payment_method\":" + PaymentMethod + "}").toString();
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                json
        );
        Log.d("TAG", "payment url: " + Url);
        Log.d("TAG", "payment body: " + json);

        String Auth;
        if (LoginHolder.getInstance().getData().equals("login")) {
            Auth = UserTokenHolder.getInstance().getData().token_type
                    + " " + UserTokenHolder.getInstance().getData().access_token;
        } else {
            Auth = Values.Authorization_User;
        }

        Request request = new Request.Builder()
                .url(Url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "" + Auth)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.d(TAG, response.body().string());
                final String mMessage = response.body().string();
                Log.d(TAG, "this returned data: " + mMessage);
                JSONObject parent = null;
                try {
                    parent = new JSONObject(mMessage);
                    boolean result = parent.getBoolean("success");
                    if (result) {
                        Integer orderId = parent.getJSONObject("data").getInt("order_id");
                        if (PaymentMethod == 1) {
                            JSONObject androidData = parent.getJSONObject("androidData");
                            String paymentUrl = androidData.getString("url");
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent i = new Intent(activity, Payment.class);
                                            i.putExtra("ID", orderId);
                                            i.putExtra("ResData", paymentUrl);
                                            startActivity(i);
                                        }
                                    }, 200);
                                }
                            });
                        } else {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent i = new Intent(activity, OrderDetails.class);
                                            i.putExtra("ID", orderId);
                                            startActivity(i);
                                        }
                                    }, 200);
                                }
                            });
                        }
                    } else {
                        final String reply = parent.getString("message");
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadMsg(reply);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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

        /*try {
            if (LoginHolder.getInstance().getData().equals("login")) {
                SharedPreferences.Editor editor = Login.edit();
                editor.putString("isLogin", "login");
                editor.putString("UserID", FaceIdHolder.getInstance().getData());
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

        }*/
        super.onResume();

    }

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
