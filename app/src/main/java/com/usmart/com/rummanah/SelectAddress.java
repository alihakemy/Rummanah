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
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.os.Bundle;

import com.ethanhua.skeleton.ViewSkeletonScreen;

import android.view.View;
import android.view.WindowManager;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

import constants.Values;
import customLists.CustomListAddress;
import dataInLists.DataInAddress;
import dataInLists.DataInGlobal;
import dataInLists.DataInLogin;
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

public class SelectAddress extends Activity {

    Activity activity = SelectAddress.this;
    SharedPreferences Login;
    DataInAddress _Data = new DataInAddress();

    ListView lv;
    CustomListAddress adapter2;
    Typeface fontMedim, fontLight, fontUltra, fontPoppinsMed;
    String AddressID, AddressName;
    TextView MainTitle;
    ViewSkeletonScreen skeletonScreen;
    ImageView HideAll, btn_Add;
    TextView noData;
    ProgressBar prog;
    SearchView searchV;
    String lang;

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
        setContentView(R.layout.activity_listview);
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


        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);

        MainTitle = findViewById(R.id.MainTitle);

        searchV = findViewById(R.id.SearchCity);
        noData = findViewById(R.id.noData);
        btn_Add = findViewById(R.id.btn_Add);

        searchV.setVisibility(View.GONE);
        AddressID = Integer.toString(0);
        AddressName = "";
        lv = findViewById(R.id.listViewOrders);

        adapter2 = new CustomListAddress(activity, new ArrayList<>());
        lv.setAdapter(adapter2);

        MainTitle = findViewById(R.id.MainTitle);

        MainTitle.setText(R.string.MyAddress);
        MainTitle.setTypeface(fontMedim);
        noData.setTypeface(fontMedim);
        btn_Add.setVisibility(View.VISIBLE);

        btn_Add.setOnClickListener(v -> {
            /*Intent i = new Intent(activity, AddAddress.class);
            startActivityForResult(i, 11);*/
            Intent i = new Intent(activity, AddAddress.class);
            i.putExtra("RequestCode", 11);
            startActivity(i);
        });

        lv.setOnItemClickListener((parent, view, position, id) -> {
            if (_Data.data.get(position).is_default) {
                selectAddress(position);
            } else {
                selectType(position);
            }

        });

        loadAddress();

    }

    private void loadAddress() {
        AsyncHttpClient client = new AsyncHttpClient();
        String Url;

        Url = Values.Link_service + "addresses/" + lang + "/v1";

        try {
            if (LoginHolder.getInstance().getData().equals("login")) {
                client.addHeader("Authorization", "" + UserTokenHolder.getInstance().getData().token_type
                        + " " + UserTokenHolder.getInstance().getData().access_token);
            } else {
                client.addHeader("Authorization", "" + Values.Authorization_User);
            }
        }catch (Exception e){
            client.addHeader("Authorization", "" + Values.Authorization_User);

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
                    _Data = g.fromJson(arg1, t);
                    adapter2.clear();
                    adapter2.notifyDataSetChanged();
                    if (_Data.data.size() > 0) {
                        adapter2.addAll(_Data.data);
                        adapter2.notifyDataSetChanged();
                        lv.setVisibility(View.VISIBLE);
                        noData.setVisibility(View.GONE);
                    } else {
                        noData.setVisibility(View.VISIBLE);
                        lv.setVisibility(View.GONE);
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

    private void setDefAddress(int Address) {
        OkHttpClient client = new OkHttpClient();
        String Url = Values.Link_service + "addresses/setdefault/" + lang + "/v1";
        String json = new StringBuilder()
                .append("{")
                .append("\"address_id\":\"" + Address + "\"")
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
                    DataInGlobal Res;
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInGlobal>() {
                    }.getType();

                    Res = g.fromJson(response.body().string(), t);

                    runOnUiThread(() -> {
                        if (!Res.success) {
                            if (Res.code == 401) {
                                Intent i = new Intent(activity, Login.class);
                                startActivity(i);
                            } else {
                                loadMsg(Res.message);
                            }
                        } else {


                        }
                    });

                } catch (Exception e) {
                }
            }

        });

        HideAll.setVisibility(View.GONE);
        prog.setVisibility(View.GONE);

    }

    private void RemoveAddress(int Pos) {

        OkHttpClient client = new OkHttpClient();
        String Url = Values.Link_service + "addresses/" + LangHolder.getInstance().getData() + "/v1";
        String json = new StringBuilder()
                .append("{")
                .append("\"address_id\":\"" + _Data.data.get(Pos).id + "\"")
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
                    DataInAddress Result;
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInAddress>() {
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
                            adapter2.remove(_Data.data.get(Pos));
                            adapter2.notifyDataSetChanged();
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

    private void selectType(int Position) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.msg_noads3);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView Title = dialog.findViewById(R.id.MsgTitle);
        TextView Text = dialog.findViewById(R.id.MsgText);

        Button Yes = dialog.findViewById(R.id.btn_Yes);
        Button No = dialog.findViewById(R.id.btn_No);

        Yes.setText(R.string.SetDefault);
        No.setText(R.string.DeleteAddress);
        Title.setText(R.string.SelectOption);
        Text.setText("");


        Yes.setTypeface(fontMedim);
        No.setTypeface(fontMedim);
        Title.setTypeface(fontMedim);
        Text.setTypeface(fontUltra);

        Yes.setOnClickListener(v -> {
            AddressID = Integer.toString(_Data.data.get(Position).id);
            AddressName = _Data.data.get(Position).title;
            for (int i = 0; i < _Data.data.size(); i++) {
                _Data.data.get(i).is_default = false;
            }
            _Data.data.get(Position).is_default = true;
            adapter2.notifyDataSetChanged();

            setDefAddress(_Data.data.get(Position).id);
            gotoCart();

            dialog.dismiss();
        });
        No.setOnClickListener(v -> {
            RemoveAddress(Position);
            adapter2.notifyDataSetChanged();
            dialog.dismiss();
        });
        dialog.show();
    }

    private void selectAddress(int Position) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.msg_noads3);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView Title = dialog.findViewById(R.id.MsgTitle);
        TextView Text = dialog.findViewById(R.id.MsgText);

        Button Yes = dialog.findViewById(R.id.btn_Yes);
        Button No = dialog.findViewById(R.id.btn_No);

        Yes.setText(R.string.SelectAddress);
        No.setText(R.string.SelectAddress);
        Title.setText(R.string.SelectOption);
        Text.setText("");


        Yes.setVisibility(View.GONE);
        No.setTypeface(fontMedim);
        Title.setTypeface(fontMedim);
        Text.setTypeface(fontUltra);

        No.setOnClickListener(v -> {
            AddressID = Integer.toString(_Data.data.get(Position).id);
            AddressName = _Data.data.get(Position).title;
           /* for (int i = 0; i < _Data.data.size(); i++) {
                _Data.data.get(i).is_default = false;
            }
            _Data.data.get(Position).is_default = true;
            adapter2.notifyDataSetChanged();
            setDefAddress(_Data.data.get(Position).id);*/
            //setOnBack();
            gotoCart();
            dialog.dismiss();
        });
        Yes.setOnClickListener(v -> {
            RemoveAddress(Position);
            adapter2.notifyDataSetChanged();
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11) {
            AddressID = data.getStringExtra("AddressID");
            AddressName = data.getStringExtra("AddressName");
            if (AddressName.equals("")) {
                AddressName = getResources().getString(R.string.SetAddressDef);
            }
            loadAddress();
        }
    }


    @Override
    protected void onResume() {

        try {
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

        }

        super.onResume();

    }


    public void gotoBack(View v) {
        setOnBack();
    }

    public void gotoSearch(View v) {

    }

    public void gotoCart() {
        Intent i = new Intent(activity, Checkout.class);
        i.putExtra("AddressID", Integer.parseInt(AddressID));
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        setOnBack();
    }

    public void setOnBack() {
        /*Intent intent = new Intent();
        intent.putExtra("AddressID", AddressID);
        intent.putExtra("AddressName", AddressName);
        setResult(11, intent);*/
        /*Intent i = new Intent(activity, Checkout.class);
        i.putExtra("AddressID", Integer.parseInt(AddressID));
        startActivity(i);*/
        super.onBackPressed();
    }


}
