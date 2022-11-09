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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Locale;

import constants.Values;
import dataInLists.DataInUser;
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

public class Login extends Activity {
    Activity activity = Login.this;
    SharedPreferences Login;

    Button btn_Login, btn_withoutLogin;
    EditText Mobile, Key, Password;
    TextView Enter, ForgetPassword, HasNewAccount, Register, Conditions;
    ImageView HideAll;
    ProgressBar prog;
    Typeface fontHelvetica, fontMedim, fontLight, fontUltra;
    String lang;
    DataInUser LoginData;
    int ProdID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //  Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(activity, 2));
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
        Intent intent = getIntent();
        if (intent.hasExtra("ProdID")) {
            ProdID = intent.getExtras().getInt("ProdID");
        } else {
            ProdID = 0;
        }
        //*******************************************************
        setContentView(R.layout.activity_login);
        /*if (LangHolder.getInstance().getData().equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }*/
        lang = getSharedPreferences(Values.SharedPreferences_FileNameLangSelect, 0)
                .getString(Values.SharedPreferences_FileNameLangSelect, null);
        lang = Values.SharedPreferences_FileNameLangSelect;
        // ***********************************************
        fontHelvetica = Typeface.createFromAsset(activity.getAssets(), "fonts/Helvetica_33244fbeea10f093ae87de7a994c3697.ttf");
        fontMedim = Typeface.createFromAsset(activity.getAssets(), "fonts/GE_SS_Two_Medium.otf");
        fontLight = Typeface.createFromAsset(activity.getAssets(), "fonts/GE_SS_Two_Light.otf");
        fontUltra = Typeface.createFromAsset(activity.getAssets(), "fonts/ArbFONTS-GESSTextUltraLight-UltraLight.otf");

        Conditions = findViewById(R.id.Conditions);
        Register = findViewById(R.id.Register);
        btn_Login = findViewById(R.id.btn_Set_Login);
        btn_withoutLogin = findViewById(R.id.btn_withoutLogin);
        Enter = findViewById(R.id.Enter);
        ForgetPassword = findViewById(R.id.ForgetPassword);
        HasNewAccount = findViewById(R.id.HasNewAccount);

        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);


        Mobile = findViewById(R.id.edit_Mobile);
        Key = findViewById(R.id.edit_Key);
        Password = findViewById(R.id.edit_Password);

        Enter.setTypeface(fontMedim);
        Conditions.setTypeface(fontMedim);
        Register.setTypeface(fontMedim);
        ForgetPassword.setTypeface(fontMedim);
        HasNewAccount.setTypeface(fontUltra);
        Mobile.setTypeface(fontHelvetica);
        btn_Login.setTypeface(fontMedim);
        Key.setTypeface(fontHelvetica);
        Password.setTypeface(fontHelvetica);

        Conditions.setOnClickListener(v -> {
            Intent i = new Intent(activity, Conditions.class);
            startActivity(i);
        });

        Register.setOnClickListener(v -> {
            Intent i = new Intent(activity, SetRegister.class);
            i.putExtra("ProdID", ProdID);
            startActivity(i);
        });
        ForgetPassword.setOnClickListener(v -> {
            Intent i = new Intent(activity, ForgetPass.class);
            i.putExtra("ProdID", ProdID);
            startActivity(i);
        });

        btn_withoutLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent i = new Intent(activity, AddAddress.class);
                i.putExtra("ProdID", ProdID);
                startActivity(i);*/
//                Intent i = new Intent(activity, AddAddress.class);
//                i.putExtra("ProdID", ProdID);
//                i.putExtra("RequestCode", 12);
//                startActivity(i);

                onBackPressed();
            }
        });


        btn_Login.setOnClickListener(v -> {
            if (Mobile.getText().toString().matches("")) {
                loadMsg(getResources().getString(R.string.EmptyField));
                return;
            }

            HideAll.setVisibility(View.VISIBLE);
            prog.setVisibility(View.VISIBLE);

            OkHttpClient client = new OkHttpClient();
            String Url = Values.Link_service + "auth/login/" + lang + "/v1";
            String json = new StringBuilder()
                    .append("{")
                    .append("\"phone\":\"" + Key.getText().toString() + Mobile.getText().toString() + "\",")
                    .append("\"password\":\"" + Password.getText().toString() + "\",")
                    .append("\"unique_id\":\"" + Settings.Secure.getString(activity.getContentResolver(),
                            Settings.Secure.ANDROID_ID) + "\",")
                    .append("\"type\":\"" + 2 + "\",")
                    .append("\"fcm_token\":\"" + FirebaseInstanceId.getInstance().getToken() + "\"")
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

                        Gson g = new Gson();
                        Type t = new TypeToken<DataInUser>() {
                        }.getType();

                        LoginData = g.fromJson(response.body().string(), t);


                        activity.runOnUiThread(() -> {
                            if (!LoginData.success) {
                                loadMsg(LoginData.message);
                            } else {
                                if (LoginData.data.verified == 1) {
                                    LoginHolder.getInstance().setData("login");
                                    FaceIdHolder.getInstance().setData(LoginData.data.id + "");
                                    UsernameHolder.getInstance().setData(LoginData.data.name + "");
                                    UserMobileHolder.getInstance().setData(LoginData.data.phone + "");
                                    UserEmailHolder.getInstance().setData(LoginData.data.email + "");
                                    UserTokenHolder.getInstance().setData(LoginData.data.token);
                                    Login = getSharedPreferences(Values.SharedPreferences_FileName, 0);
                                    SharedPreferences.Editor editor = Login.edit();
                                    editor.putString("isLogin", "login");
                                    editor.putString("UserID", String.valueOf(LoginData.data.id));
                                    editor.putString("UserName",LoginData.data.name );
                                    editor.putString("UserMobile", LoginData.data.phone);
                                    editor.putString("UserEmail", LoginData.data.email);
                                    editor.putString("Token", String.valueOf(LoginData.data.token));
                                    editor.putString("Token_Type","");
                                    editor.putString("Token_Exp", "");
                                    editor.commit();

                                    if (ProdID > 0) {
                                        Intent i = new Intent(activity, Product.class);
                                        i.putExtra("ID", ProdID);
                                        startActivity(i);
                                    } else {
                                        Intent i = new Intent(activity, Home.class);
                                        startActivity(i);
                                    }

                                } else {
                                    Intent i = new Intent(activity, Activation.class);
                                    i.putExtra("ID", LoginData.data.id);
                                    i.putExtra("Mobile", LoginData.data.phone);
                                    i.putExtra("ProdID", ProdID);
                                    i.putExtra("IsHome", true);
                                    startActivity(i);
                                }

                                // loadMsg(LoginData.data.name + "");
                       /* Intent i = new Intent(activity, Activation.class);
                        i.putExtra("ID", x.data.id);
                        i.putExtra("Mobile", x.data.phone);
                        // i.putExtra("isCart", isCart);
                        startActivity(i);*/
                            }
                        });

                    } catch (Exception e) {
                        Log.i("TestApp_3_Error", e.getMessage() + "");
                    }
                }

            });


            HideAll.setVisibility(View.GONE);
            prog.setVisibility(View.GONE);
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
        // TODO Auto-generated method stub
        try {
            if (OnlineHolder.getInstance().getData().equals("1")) {
                OnlineHolder.getInstance().setData("0");
                finish();
                startActivity(getIntent());

            } else if (NetWork.isNetworkAvailable(activity) == false) {
                NetWork.gotoError(activity);
            }

        } catch (Exception e) {

        }
        try {
            if (LoginHolder.getInstance().getData().equals("login")) {

                startActivity(new Intent(activity, Home.class));
            }
        } catch (Exception e) {
            NetWork.gotoError(activity);
        }
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(activity, Home.class));
    }

    public void gotoBack(View v) {
        onBackPressed();
    }


}
