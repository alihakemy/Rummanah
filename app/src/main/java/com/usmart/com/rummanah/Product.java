package com.usmart.com.rummanah;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;


import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import constants.Values;
import customLists.CustomListOptions;
import customLists.CustomListProductRates;
import customLists.CustomListRelated;
import customLists.CustomSlider;
import dataInLists.DataInGlobal;
import dataInLists.DataInProduct;
import helpers.LangHolder;
import helpers.LoginHolder;
import helpers.NetWork;
import helpers.OnlineHolder;
import helpers.ProductHolder;
import helpers.UserTokenHolder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import utils.ExpandableHeightGridView;

public class Product extends FragmentActivity {
    Activity activity = Product.this;
    private static final String TAG = Product.class.getSimpleName();
    DataInProduct Data = new DataInProduct();

    ArrayList<String> Photos = new ArrayList<>();
    HashMap<String, String> url_maps = new HashMap<>();
    //    HashMap<String, Integer> file_maps = new HashMap<>();
    SliderLayout mDemoSlider;
    CustomListOptions adapterOptions;
    CustomListProductRates adapterRates;

    ImageView HideAll, iv_plus, iv_minus;
    ProgressBar prog;
    RecyclerView lvOffers;
    Button btn_Buy;
    TextView tv_Title, tv_Cat, tv_Desc, tv_Price_Curr, tv_Price, tv_BeforePrice, tv_BeforePrice_Curr, tv_Delivery, tv_Return,
            tv_SeeAlso, tv_CartNum, tv_count, tv_averageRating, tv_totalRates;
    Typeface fontMedim, fontLight, fontUltra, fontAvenir, fontPoppinsMedium;
    int ID, counter = 1;
    ExpandableHeightGridView lvOptions, lvRates;
    RelativeLayout Related;
    FrameLayout Specifications;
    ImageView btn_Favorite, btn_Call;
    String lang;
    boolean IsFav = false;
    ScrollView Scroll;
    RelativeLayout ActionBar;
    RatingBar rb_rating;
    LinearLayout ll_rating;

    private static final int INITIAL_REQUEST = 1337;
    private static final int CALL_REQUEST = INITIAL_REQUEST + 5;
    private static final String[] CALL_PERMS = {
            Manifest.permission.CALL_PHONE
    };

    Locale locale = new Locale("en", "UK");
    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //  Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(activity, 2));
        setContentView(R.layout.activity_product);


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

        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);
        mDemoSlider = findViewById(R.id.slider);
        Related = findViewById(R.id.Related);
        Specifications = findViewById(R.id.Specifications);
        lvOffers = findViewById(R.id.lvOffers);
        lvOptions = findViewById(R.id.lvOptions);
        Scroll = findViewById(R.id.Scroll);
        ActionBar = findViewById(R.id.ActionBarTitle);
        tv_CartNum = findViewById(R.id.tv_CartNum);
        iv_plus = findViewById(R.id.iv_plus);
        iv_minus = findViewById(R.id.iv_minus);
        tv_count = findViewById(R.id.tv_count);
        tv_averageRating = findViewById(R.id.tv_averageRating);
        tv_totalRates = findViewById(R.id.tv_totalRates);
        rb_rating = findViewById(R.id.rb_rating);
        lvRates = findViewById(R.id.lvRates);
        ll_rating = findViewById(R.id.ll_rating);

        lvOptions.setExpanded(true);
        lvRates.setExpanded(true);


        tv_Title = findViewById(R.id.tv_Title);
        tv_Cat = findViewById(R.id.tv_Cat);
        tv_Price_Curr = findViewById(R.id.tv_Price_Curr);
        tv_Price = findViewById(R.id.tv_Price);
        tv_BeforePrice = findViewById(R.id.tv_BeforePrice);
        tv_BeforePrice_Curr = findViewById(R.id.tv_BeforePrice_Curr);
        tv_Return = findViewById(R.id.tv_Return);
        tv_SeeAlso = findViewById(R.id.tv_SeeAlso);
        tv_Delivery = findViewById(R.id.tv_Delivery);
        tv_Desc = findViewById(R.id.tv_Desc);


        btn_Favorite = findViewById(R.id.btn_Favorite);
        btn_Buy = findViewById(R.id.btn_Buy);
        btn_Call = findViewById(R.id.btn_Call);


        tv_Title.setTypeface(fontMedim);
        tv_Cat.setTypeface(fontLight);
        tv_Desc.setTypeface(fontMedim);
        tv_Price_Curr.setTypeface(fontLight);
        tv_BeforePrice_Curr.setTypeface(fontLight);
        tv_Price.setTypeface(fontPoppinsMedium);
        tv_BeforePrice.setTypeface(fontPoppinsMedium);
        tv_Delivery.setTypeface(fontLight);
        tv_Return.setTypeface(fontLight);
        tv_SeeAlso.setTypeface(fontMedim);
        btn_Buy.setTypeface(fontMedim);

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

        iv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter = counter + 1;
                tv_count.setText(String.valueOf(counter));
            }
        });

        iv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter > 1) {
                    counter = counter - 1;
                    tv_count.setText(String.valueOf(counter));
                } else {
                    tv_count.setText(String.valueOf(counter));
                }
            }
        });

        btn_Buy.setOnClickListener(v -> {
            AddCart(ID);

        });

        btn_Call.setOnClickListener(v -> {
           /* Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + Data.data.product));
            startActivity(callIntent);*/
        });


      /*  fav.setOnClickListener(v -> Fav());
        btn_Buy.setOnClickListener(v -> addCart((byte) 1));
        btn_Cart.setOnClickListener(v -> {
            addCart((byte) 0);
        });*/

        tv_Delivery.setOnClickListener(v -> {
            startActivity(new Intent(activity, Delivery.class));
        });

        tv_Return.setOnClickListener(v -> {
            startActivity(new Intent(activity, ReturnPolicy.class));
        });

        btn_Buy.setOnClickListener(v -> {
            AddCart(ID);

        });
        btn_Favorite.setOnClickListener(view -> {
            if (IsFav) {
                RemoveFave(Data.data.product.id, btn_Favorite);
            } else {
                AddFave(Data.data.product.id, btn_Favorite);
            }
        });

        ll_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, ProductRatesActivity.class);
                i.putExtra("ID", ID);
                startActivity(i);
            }
        });

        loadData();

    }

    private void loadData() {
        AsyncHttpClient client = new AsyncHttpClient();
        String Url;

        Url = Values.Link_service + "products/" + ID + "/" + lang + "/v1/";
        client.addHeader("Content-Type", "application/json");
        if (LoginHolder.getInstance().getData().equals("login")) {
            client.addHeader("Authorization", UserTokenHolder.getInstance().getData().access_token);
        } else {
            client.addHeader("Authorization", "" + Values.Authorization_User);
        }

        Log.i("TestApp", Url);
        Log.d(TAG, Url);
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
                Log.i("TestApp", arg1);
                try {
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInProduct>() {
                    }.getType();
                    Data = g.fromJson(arg1, t);
                    tv_Title.setText(Data.data.product.title);
                    tv_Cat.setText(Data.data.product.category_name + " ");
                    tv_Price.setText(String.format("%.3f",Data.data.product.final_price) + " ");
                    tv_averageRating.setText(Data.data.product.rate + " " + getResources().getString(R.string.from) + " 5.0");
                    tv_totalRates.setText(Data.data.product.rate_count + " " + getResources().getString(R.string.rates));
                    rb_rating.setRating((float) Data.data.product.rate);

                    if (Data.data.product.offer == 0) {
                        //   txtDiscount.setVisibility(View.GONE);
                        tv_Price.setText(String.format("%.3f",Data.data.product.final_price) + " ");
                        tv_BeforePrice.setText("");
                        tv_BeforePrice.setVisibility(View.GONE);
                        tv_BeforePrice_Curr.setVisibility(View.GONE);

                    } else {
                        tv_BeforePrice.setPaintFlags(tv_Price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        tv_BeforePrice.setVisibility(View.VISIBLE);
                        tv_BeforePrice_Curr.setVisibility(View.VISIBLE);
                        tv_Price.setText(String.format("%.3f",Data.data.product.final_price) + " ");
                        tv_BeforePrice.setText(String.format("%.3f",Data.data.product.price_before_offer) + " ");
                    }

                    int x = Data.data.product.description.length();
                    if (x >= 250) {
                        tv_Desc.setText(Html.fromHtml(Data.data.product.description.substring(0, 250)) + " ...");
                    } else {
                        tv_Desc.setText(Html.fromHtml(Data.data.product.description) + " ");
                    }
                    //  tv_Desc.setText(Html.fromHtml(Data.data.product.description));


                    tv_Desc.setOnClickListener(v -> {
                        Intent i = new Intent(activity, ProductDesc.class);
                        i.putExtra("Description", Data.data.product.description);
                        startActivity(i);
                    });
                    IsFav = Data.data.product.favorite;
                    if (Data.data.product.favorite) {

                        btn_Favorite.setImageResource(R.mipmap.like_prod);
                    } else {
                        btn_Favorite.setImageResource(R.mipmap.non_like_prod);
                    }


                    loadAds(Data.data.product.images);

                    if (Data.data.product.options.size() > 0) {
                        adapterOptions = new CustomListOptions(activity, Data.data.product.options);
                        lvOptions.setAdapter(adapterOptions);
                        lvOptions.setOnItemClickListener((parent, view, position, id) -> {
                            Intent i = new Intent(activity, ProductOptions.class);
                            i.putExtra("Options", Data.data.product.options);
                            startActivity(i);
                        });
                    } else {
                        Specifications.setVisibility(View.GONE);
                    }

                    if (Data.data.product.rates.size() > 0) {
                        adapterRates = new CustomListProductRates(activity, Data.data.product.rates);
                        lvRates.setAdapter(adapterRates);
                    } else {
                        ll_rating.setVisibility(View.GONE);
                    }

                    if (Data.data.related.size() > 0) {
                        Related.setVisibility(View.VISIBLE);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 2);
                        gridLayoutManager.setSpanCount(2);
                        RecyclerView.LayoutManager layoutManager = new
                                GridLayoutManager(activity, 1, LinearLayoutManager.HORIZONTAL, false);
                        lvOffers.setLayoutManager(layoutManager);

                        CustomListRelated adapterRelated = new CustomListRelated(activity, Data.data.related);
                        lvOffers.setAdapter(adapterRelated);
                    } else {
                        Related.setVisibility(View.GONE);
                    }
                    HideAll.setVisibility(View.GONE);
                    prog.setVisibility(View.GONE);

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

    private void loadAds(ArrayList<String> Data) {

        try {
            int x = 0;
            for (String _Image : Data) {
                url_maps.put(" " + x + " - ", Values.Link_Image + _Image);
                // file_maps.put(" " + x + " - " , x);
                x++;
                Photos.add(_Image + "");
            }

            for (String name : url_maps.keySet()) {
                CustomSlider textSliderView = new CustomSlider(activity);
                textSliderView.getView().findViewById(com.daimajia.slider.library.R.id.description_layout)
                        .setBackgroundColor(Color.TRANSPARENT);
                Log.i("TestApp_Image", url_maps.get(name));
                textSliderView.image(url_maps.get(name)).setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(slider -> {
                            try {
                                loadPhoto();
                            } catch (Exception e) {
                            }
                        });

                // add your extra information
                ///   textSliderView.getBundle().putString("extra", name + x);
                mDemoSlider.addSlider(textSliderView);

            }

            //    mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Tablet);
            mDemoSlider.setCustomIndicator(findViewById(R.id.custom_indicator));
            /// mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mDemoSlider.setCustomAnimation(new DescriptionAnimation());
            mDemoSlider.setDuration(6000);

        } catch (Exception e) {
            Log.i("TestLog", e.getMessage());
        }
    }


    /*  private void loadAds2() {
          Photos.add(Data.photo);

          if (!Data.photo1.isEmpty()) {
              Photos.add(Data.photo1);
          }
          if (!Data.photo2.isEmpty()) {
              Photos.add(Data.photo2);
          }
          if (!Data.photo3.isEmpty()) {
              Photos.add(Data.photo3);
          }
          if (!Data.photo4.isEmpty()) {
              Photos.add(Data.photo4);
          }
          if (!Data.photo5.isEmpty()) {
              Photos.add(Data.photo5);
          }
          if (!Data.photo6.isEmpty()) {
              Photos.add(Data.photo6);
          }
          DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.diff_long)
                  .showImageForEmptyUri(R.mipmap.diff_long).showImageOnFail(R.mipmap.diff_long)
                  .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
                  .build();
          ImageLoader.getInstance().displayImage(Data.photo, Photo, options);
          Photo.setOnClickListener(v -> loadPhoto());
          HideAll.setVisibility(View.GONE);
          prog.setVisibility(View.GONE);

      }*/
    private void AddFave(int ID, ImageView LikeIc) {
        if (LoginHolder.getInstance().getData().equals("logout")) {
            ProductHolder.getInstance().setData(Integer.toString(ID));
            //  Activity.startActivity(new Intent(Activity, Login.class));
            loadMsgLogin(getResources().getString(R.string.LoginFirst));
            return;
        }
        OkHttpClient client = new OkHttpClient();
        String Url = Values.Link_service + "favorites/" + lang + "/v1";
        String json = new StringBuilder()
                .append("{")
                // .append("\"type\":\"" + Type + "\",")
                .append("\"product_id\":\"" + ID + "\"")
                .append("}").toString();

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                json
        );
        Request request = new Request.Builder()
                .url(Url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "" + UserTokenHolder.getInstance().getData().token_type
                        + " " + UserTokenHolder.getInstance().getData().access_token)
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
                    DataInGlobal Result;
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInGlobal>() {
                    }.getType();

                    Result = g.fromJson(response.body().string(), t);


                    runOnUiThread(() -> {
                        if (!Result.success) {
                            if (Result.code == 401) {
                                Intent i = new Intent(activity, Login.class);
                                i.putExtra("ProdID", ID);
                                startActivity(i);
                            } else {
                                loadMsg(Result.message, false);
                            }
                        } else {
                            IsFav = true;
                            LikeIc.setImageResource(R.mipmap.like_prod);
                        }
                    });

                } catch (Exception e) {
                }
            }

        });

    }

    private void RemoveFave(int ID, ImageView LikeIc) {
        if (LoginHolder.getInstance().getData().equals("logout")) {
            // ProductHolder.getInstance().setData(Integer.toString(ID));
            //  Activity.startActivity(new Intent(Activity, Login.class));
            loadMsgLogin(getResources().getString(R.string.LoginFirst));
            return;
        }
        OkHttpClient client = new OkHttpClient();
        String Url = Values.Link_service + "favorites/" + lang + "/v1";
        String json = new StringBuilder()
                .append("{")
                //.append("\"type\":\"" + Type + "\",")
                .append("\"product_id\":\"" + ID + "\"")
                .append("}").toString();

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                json
        );
        Request request = new Request.Builder()
                .url(Url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "" + UserTokenHolder.getInstance().getData().token_type
                        + " " + UserTokenHolder.getInstance().getData().access_token)
                .delete(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("TestApp_5", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    DataInGlobal Result;
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInGlobal>() {
                    }.getType();
                    // Log.i("TestApp_555",response.body().string());
                    Result = g.fromJson(response.body().string(), t);


                    activity.runOnUiThread(() -> {
                        if (!Result.success) {
                            if (Result.code == 401) {
                                Intent i = new Intent(activity, Login.class);
                                i.putExtra("ProdID", ID);
                                startActivity(i);
                            } else {
                                loadMsg(Result.message, false);
                            }
                        } else {
                            Log.i("TestApp_555", "44444");
                            IsFav = false;
                            LikeIc.setImageResource(R.mipmap.non_like_prod);
                        }
                    });

                } catch (Exception e) {
                    Log.i("TestApp_555", e.getMessage());
                }
            }

        });

    }

    private void AddCart(int ID) {

        OkHttpClient client = new OkHttpClient();
        String Url = Values.Link_service + "visitors/cart/add/" + LangHolder.getInstance().getData() + "/v1";
        String json = new StringBuilder()
                .append("{")
                .append("\"unique_id\":\"" + Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID) + "\",")
                .append("\"product_id\":" + ID + ",")
                .append("\"count\":" + counter)
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
                                loadMsg(Result.message, false);
                            }
                        } else {
                            loadMsg(getResources().getString(R.string.AddedToCart), true);
                        }
                    });

                } catch (Exception e) {
                }
            }

        });

    }

    private void loadPhoto() {
        Intent i = new Intent(activity, PhotoGallery.class);
        i.putExtra("Photos", Photos);
        startActivity(i);
        //overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
    }

    private void loadMsgLogin(String MSG) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.msg_noads3);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView Title = dialog.findViewById(R.id.MsgTitle);
        TextView Text = dialog.findViewById(R.id.MsgText);

        Button Yes = dialog.findViewById(R.id.btn_Yes);
        Button No = dialog.findViewById(R.id.btn_No);

        Yes.setText(R.string.OK);
        No.setText(R.string.Login);
        Text.setText(MSG + "");
        // No.setVisibility(View.GONE);


        Yes.setTypeface(fontMedim);
        No.setTypeface(fontMedim);
        Title.setTypeface(fontMedim);
        Text.setTypeface(fontUltra);

        Yes.setOnClickListener(v -> {
            dialog.dismiss();
        });
        No.setOnClickListener(v -> {
            dialog.dismiss();

            Intent i = new Intent(activity, Login.class);
            i.putExtra("ProdID", ID);
            startActivity(i);

        });
        dialog.show();
    }

    private void loadMsg(String MSG, boolean cart) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.msg_noads3);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView Title = dialog.findViewById(R.id.MsgTitle);
        TextView Text = dialog.findViewById(R.id.MsgText);

        Button Yes = dialog.findViewById(R.id.btn_Yes);
        Button No = dialog.findViewById(R.id.btn_No);

        No.setText(R.string.OK);
        Yes.setText(R.string.Login);
        Text.setText(MSG + "");
        Yes.setVisibility(View.GONE);


        Yes.setTypeface(fontMedim);
        No.setTypeface(fontMedim);
        Title.setTypeface(fontMedim);
        Text.setTypeface(fontUltra);

        No.setOnClickListener(v -> {
            dialog.dismiss();
            if (cart) {
                CartCount();
            }
        });
        /*No.setOnClickListener(v -> {
            dialog.dismiss();

            Intent i = new Intent(activity, Login.class);
            i.putExtra("ProdID", ID);
            startActivity(i);

        });*/
        dialog.show();
    }

    public void CartCount() {

        OkHttpClient client = new OkHttpClient();
        String Url = Values.Link_service + "visitors/cart/count/" + LangHolder.getInstance().getData() + "/v1";
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
                                loadMsg(Result.message, false);
                            }
                        } else {
                            tv_CartNum.setText(Result.data.count + "");
                        }
                    });

                } catch (Exception e) {
                }
            }

        });

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
        onBackPressed();
    }

    public void gotoBack() {
        onBackPressed();

    }

    public void gotoCart(View v) {
        startActivity(new Intent(activity, Cart.class));
    }

    public void gotoShare(View v) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.kaizenit.com.mtgr");
        intent.putExtra(Intent.EXTRA_SUBJECT, activity.getResources().getString(R.string.app_name));
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, "Share"));
    }

    public void gotoUpload(View v) {

    }

}
