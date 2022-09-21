package com.usmart.com.rummanah;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.ViewSkeletonScreen;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

import constants.Values;
import customLists.CustomListCart;
import dataInLists.DataInAddress;
import dataInLists.DataInCart;
import dataInLists.DataInGlobal;
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

public class Cart extends Activity {
    Activity activity = Cart.this;
    private static final String TAG = Cart.class.getSimpleName();
    DataInCart _Data = new DataInCart();
    DataInAddress _Address = new DataInAddress();
    Typeface fontMedim, fontLight, fontUltra, fontPoppinsMed;
    ViewSkeletonScreen skeletonScreen;
    ImageView HideAll;
    ProgressBar prog;
    TextView MainTitle, Number_1, CartItems, Number_2, Number_3, tv_PriceDetails, tv_ProductCount, tv_Products,
            tv_TotalOrdersTxt, tv_DeliveryTxt, tv_TotalTxt, tv_TotalOrdersPrice, tv_TotalOrders_Curr, tv_Price_Delivery,
            tv_Price_Delivery_Curr, tv_Total, tv_Total_Curr, DeliveryAddress, tv_AddnewAddress, tv_minimumOrder;
    Button btn_SelectAddress, btn_Set_Order;
    ExpandableHeightListView lvCart;
    CustomListCart adapterCarts;
    String AddressID, AddressName;
    String lang;
    float minimumOrder, TotalOrdersPrice;

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
        setContentView(R.layout.activity_cart);
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

        AddressID = "0";

        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);


        MainTitle = findViewById(R.id.MainTitle);
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
        DeliveryAddress = findViewById(R.id.DeliveryAddress);
        tv_AddnewAddress = findViewById(R.id.tv_AddnewAddress);
        btn_SelectAddress = findViewById(R.id.btn_SelectAddress);
        btn_Set_Order = findViewById(R.id.btn_Set_Order);
        tv_minimumOrder = findViewById(R.id.tv_minimumOrder);


        lvCart = findViewById(R.id.lvItems);
        adapterCarts = new CustomListCart(activity, new ArrayList<>());

        lvCart.setExpanded(true);
        lvCart.setAdapter(adapterCarts);


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
        DeliveryAddress.setTypeface(fontMedim);
        tv_AddnewAddress.setTypeface(fontMedim);
        btn_SelectAddress.setTypeface(fontLight);
        btn_Set_Order.setTypeface(fontLight);


        loadAddress();
        loadData();

        btn_SelectAddress.setOnClickListener(v -> {
            Intent i = new Intent(activity, SelectAddress.class);
            startActivityForResult(i, 11);
        });

        tv_AddnewAddress.setOnClickListener(v -> {
            Intent i = new Intent(activity, AddAddress.class);
            startActivityForResult(i, 11);
        });

        btn_Set_Order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TotalOrdersPrice < minimumOrder) {
                    Toast.makeText(Cart.this, " الحد الأدنى للطلب " + minimumOrder + " د.ك ", Toast.LENGTH_SHORT).show();
                } else {
                    if (!LoginHolder.getInstance().getData().equals("login")) {
                        Intent i = new Intent(activity, Login.class);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(activity, SelectAddress.class);
                        //i.putExtra("AddressID", Integer.parseInt(AddressID));
                        startActivity(i);
                    }
                }
            }
        });
    }

    private void loadData() {
        skeletonScreen = Skeleton.bind(lvCart)
                .load(R.layout.singel_load_list)
                .show();

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
        try {
            if (LoginHolder.getInstance().getData().equals("login")) {
                Auth = UserTokenHolder.getInstance().getData().token_type
                        + " " + UserTokenHolder.getInstance().getData().access_token;
            } else {
                Auth = Values.Authorization_User;
            }
        }catch (Exception e){
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
                            if (_Data.code == 401) {
                                Intent i = new Intent(activity, Login.class);
                                startActivity(i);
                            } else {
                                loadMsg(_Data.message);
                            }
                        } else {
                            adapterCarts.addAll(_Data.data.cart);
                            adapterCarts.notifyDataSetChanged();

                            tv_minimumOrder.setText(getResources().getString(R.string.minimumRequest)
                                    + " " + _Data.data.min_order + " " + getResources().getString(R.string.DK));
                            tv_ProductCount.setText("( " + _Data.data.count + " )");
                            tv_TotalOrdersPrice.setText(_Data.data.subtotal_price + "");
                            tv_Total.setText(_Data.data.subtotal_price + "");

                            minimumOrder = Float.parseFloat(_Data.data.min_order);
                            TotalOrdersPrice = _Data.data.subtotal_price;
                            skeletonScreen.hide();
                        }
                    });

                } catch (Exception e) {
                    Log.i("TestApp_5", e.getMessage());
                }
            }

        });


    }

    private void getDeliveryCost(int AdID) {
        AsyncHttpClient client = new AsyncHttpClient();
        String Url;

        Url = Values.Link_service + "delivery_price/" + lang + "/v1?address_id=" + AdID;
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Authorization", "" + Values.Authorization_User);

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

    private void loadAddress() {
        AsyncHttpClient client = new AsyncHttpClient();
        String Url;

        Url = Values.Link_service + "addresses/" + lang + "/v1";

        try {
            if(LoginHolder.getInstance().getData()!=null){
                if (LoginHolder.getInstance().getData().equals("login")) {
                    client.addHeader("Authorization", "" + UserTokenHolder.getInstance().getData().token_type
                            + " " + UserTokenHolder.getInstance().getData().access_token);
                } else {
                    client.addHeader("Authorization", "" + Values.Authorization_User);
                }
            }else{
                client.addHeader("Authorization", "" + Values.Authorization_User);

            }
        }catch (Exception e){

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

                try {
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInAddress>() {
                    }.getType();
                    _Address = g.fromJson(arg1, t);
                    if (_Address.data.size() > 0) {
                        for (int x = 0; x < _Address.data.size(); x++) {
                            boolean defaultAddress = _Address.data.get(x).is_default;
                            if (defaultAddress) {
                                AddressID = String.valueOf(_Address.data.get(x).id);
                                Log.d(TAG, "Default Address Id: " + AddressID);
                            }
                        }
                    } else {
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
                HideAll.setVisibility(View.GONE);
                prog.setVisibility(View.GONE);
                super.onFinish();

            }
        });

    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 11) {
            AddressID = data.getStringExtra("AddressID");
            AddressName = data.getStringExtra("AddressName");
            getDeliveryCost(Integer.parseInt(AddressID));
            if (AddressName.equals("")) {
                AddressName = getResources().getString(R.string.SetAddressDef);
            }
            btn_SelectAddress.setText(AddressName + "");
        }
    }*/

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

    public void gotoMenu(View v) {
        startActivity(new Intent(activity, Menu.class));
    }

    public void gotoCart(View v) {

    }

    public void gotoSearch(View v) {

    }
}
