package com.usmart.com.rummanah;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import constants.Values;
import dataInLists.DataInLogin;
import dataInLists.DataInUser;
import helpers.FaceIdHolder;
import helpers.LangHolder;
import helpers.LoginHolder;
import helpers.NetWork;
import helpers.ProductHolder;
import helpers.UserEmailHolder;
import helpers.UserMobileHolder;
import helpers.UserPhotoHolder;
import helpers.UserTypeHolder;
import helpers.UserTokenHolder;
import helpers.UsernameHolder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();
    SharedPreferences Login, prefs;
    DataInLogin.Tokens Token;
    String lang, status, Userid, UserName, UserMobile, UserEmail, UserToken, UserToken_Type, UserToken_Exp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splach);
        // ***********************************************
       /* FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    String token = task.getResult().getToken();
                    Log.d("MyFirebaseMsgService", token + "");
                });*/

        lang = getSharedPreferences(Values.SharedPreferences_FileName, 0)
                .getString(Values.SharedPreferences_FileName, null);
        Log.d(TAG, "Value of selected language in splash: " + lang);
        //lang = Values.SharedPreferences_FileNameLangSelect;
        FirebaseMessaging.getInstance().subscribeToTopic("JoinToRumanhh");
        FirebaseMessaging.getInstance().subscribeToTopic("USMART_MTGR");
        SharedPreferences login = getSharedPreferences(Values.SharedPreferences_FileName, 0);
        login.getString("isLogin", "login");

        try {
            FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {

                    Log.d(TAG, "retrieve token successful : " + token);

            }).addOnFailureListener(e -> {
                //handle e
            }).addOnCanceledListener(() -> {
                //handle cancel
            }).addOnCompleteListener(task -> Log.v(TAG, "This is the token : " + task.getResult()));
        }catch (Exception e){

        }


        UserMobileHolder.getInstance().setData("");
        UserPhotoHolder.getInstance().setData("");
        ProductHolder.getInstance().setData(Integer.toString(0));
        UserTypeHolder.getInstance().setData("user");



        UserTokenHolder.getInstance().getData().access_token=   login.getString("Token","12");
        UserTokenHolder.getInstance().getData().token_type=login.getString("Token_Type","");
        login.getString("Token_Exp", "");

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getBaseContext()));
        if (lang == "en") {
            LangHolder.getInstance().setData("en");
        } else if (lang == "ar") {
            LangHolder.getInstance().setData("ar");
        }

        if (lang != null) {
            Values.SharedPreferences_FileNameLangSelect = lang;
            Locale enLocale = new Locale(lang);
            Locale.setDefault(enLocale);
            Configuration configuration = new Configuration();
            configuration.locale = enLocale;
            getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

        } else {
            prefs = getSharedPreferences(Values.SharedPreferences_FileName, 0);
            SharedPreferences.Editor edit = prefs.edit();
            if (Locale.getDefault().getDisplayLanguage().equals("English")) {
                Values.SharedPreferences_FileNameLangSelect = "en";
                edit.putString(Values.SharedPreferences_FileName, "en");
                edit.apply();
            } else if (Locale.getDefault().getDisplayLanguage().equals("العربية")) {
                Values.SharedPreferences_FileNameLangSelect = "ar";
                edit.putString(Values.SharedPreferences_FileName, "ar");
                edit.apply();
            }
        }
        /*if (lang != "") {
            Values.SharedPreferences_FileNameLangSelect = lang;
            Locale enLocale = new Locale(lang);
            Locale.setDefault(enLocale);
            Configuration configuration = new Configuration();
            configuration.locale = enLocale;
            getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

        } else if (lang == "") {
            SharedPreferences.Editor edit = prefs.edit();
            if (Locale.getDefault().getDisplayLanguage().equals("en")) {
                //Values.SharedPreferences_FileNameLangSelect = "en";
                edit.putString(Values.SharedPreferences_FileNameLangSelect, "en");
                edit.apply();
            } else if (Locale.getDefault().getDisplayLanguage().equals("ar")) {
                //Values.SharedPreferences_FileNameLangSelect = "ar";
                edit.putString(Values.SharedPreferences_FileNameLangSelect, "ar");
                edit.apply();
            }
        }*/


        new Timer().schedule(new TimerTask() {
            public void run() {

                register();
                Login = getSharedPreferences(Values.SharedPreferences_FileName, 0);
                status = Login.getString("isLogin", "logout");
                Userid = Login.getString("UserID", "0");
                UserName = Login.getString("UserName", "");
                UserMobile = Login.getString("UserMobile", "");
                UserEmail = Login.getString("UserEmail", "");
                UserToken = Login.getString("Token", "");
                UserToken_Type = Login.getString("Token_Type", "");
                UserToken_Exp = Login.getString("Token_Exp", "");
                Token = new DataInLogin.Tokens();
                Token.access_token = UserToken;
                Token.token_type = UserToken_Type;
                Token.expires_in = UserToken_Exp;
                if (status.equals("logout")) {
                    LoginHolder.getInstance().setData("logout");
                    FaceIdHolder.getInstance().setData("0");
                    SharedPreferences.Editor editor = Login.edit();
                    editor.putString("isLogin", "logout");
                    editor.putString("UserID", "");
                    editor.putString("UserName", "");
                    editor.putString("UserMobile", "");
                    editor.putString("UserEmail", "");
                    editor.putString("Token", "");
                    editor.putString("Token_Type", "");
                    editor.putString("Token_Exp", "");
                    editor.commit();
                    SharedPreferences splash = getSharedPreferences("Splash", 0);


                    if (NetWork.isNetworkAvailable(getApplicationContext()) == true) {
                        if(splash.getString("Splash","").equals("")){
                            Intent intent = new Intent(MainActivity.this, Explane2.class);
                            finish();
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_bottom_to_top, R.anim.slide_top_to_bottom);
                        }else{
                            Intent intent = new Intent(MainActivity.this, Home.class);
                            finish();
                            startActivity(intent);
                            overridePendingTransition(R.anim.fadein_fast, R.anim.fadeout_fast);
                        }


                    } else {
                        NetWork.gotoError(MainActivity.this);
                    }

                } else {
                    LoginHolder.getInstance().setData("login");
                    FaceIdHolder.getInstance().setData(Userid);
                    UsernameHolder.getInstance().setData(UserName);
                    UserMobileHolder.getInstance().setData(UserMobile);
                    UserEmailHolder.getInstance().setData(UserEmail);
                    UserTokenHolder.getInstance().setData(Token);
                    SharedPreferences splash = getSharedPreferences("Splash", 0);

                    if (NetWork.isNetworkAvailable(getApplicationContext()) == true) {
                        if(splash.getString("Splash","").equals("")){
                            Intent intent = new Intent(MainActivity.this, Explane2.class);
                            finish();
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_bottom_to_top, R.anim.slide_top_to_bottom);
                        }else{
                            Intent intent = new Intent(MainActivity.this, Home.class);
                            finish();
                            startActivity(intent);
                            overridePendingTransition(R.anim.fadein_fast, R.anim.fadeout_fast);
                        }
                    } else {
                        NetWork.gotoError(MainActivity.this);
                    }
                }

            }
        }, 3000);
    }

    public void register() {

        OkHttpClient client = new OkHttpClient();
        String Url = Values.Link_service + "visitors/create/en/v1";
        String json = new StringBuilder()
                .append("{")
                .append("\"unique_id\":\"" + Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID) + "\",")
                .append("\"type\":\"" + 2 + "\"")
                .append("}").toString();

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                json
        );
        Request request = new Request.Builder()
                .url(Url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", Values.Authorization_User + "")
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
                    Type t = new TypeToken<DataInUser>() {
                    }.getType();


                } catch (Exception e) {
                    Log.i("TestApp_3_Error", e.getMessage() + "");
                }
            }

        });
    }

}
