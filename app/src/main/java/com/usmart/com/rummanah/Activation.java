package com.usmart.com.rummanah;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;


import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Random;

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
import utils.OtpEditText;

public class Activation extends Activity {
    Activity activity = Activation.this;
    SharedPreferences Login;

    Button btn_Login;

    OtpEditText Mobile;
    ImageView HideAll;
    ProgressBar prog;
    Typeface fontHelvetica, fontMedim, fontLight, fontUltra;
    TextView Enter, MobileNumer, ChaneNumer, ReSend, Timer, Conditions;
    int ID, ProdID;
    boolean IsHome;
    String Phone, Password, Email, Name;
    DataInUser LoginData;
    String verificationId;
    int Second = 60;
    String lang;
    String CodeX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        //  Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(activity, 2));
        super.onCreate(savedInstanceState);
        //*******************************************************
        setContentView(R.layout.activity_sms_code);
        init();

        //************* Start Send Code **************
        //   sendVerificationCode(Phone);
        //*******************************************

        MobileNumer.setText(Phone + "");

        sendVerificationCode(Phone.replace("+", ""));
        Conditions.setOnClickListener(v -> {
            Intent i = new Intent(activity, Conditions.class);
            startActivity(i);
        });

        ChaneNumer.setOnClickListener(v -> {
            Intent i = new Intent(activity, SetRegister.class);
            startActivity(i);

        });

        btn_Login.setOnClickListener(v -> {
            String str = Mobile.getText().toString();
            if (str.length() == 6) {
                verifyCode(Mobile.getText().toString());
            }
        });
        Timer.setOnClickListener(view -> {
            if (Second == 0) {
                sendVerificationCode(Phone);
            }
        });

        //******************** auto Check Code ***************
        Mobile.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = Mobile.getText().toString();
                if (str.length() == 6) {
                    verifyCode(Mobile.getText().toString());

                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        //**********************************************

    }

    private void init() {
        /*if (LangHolder.getInstance().getData().equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }*/
        lang = getSharedPreferences(Values.SharedPreferences_FileNameLangSelect, 0)
                .getString(Values.SharedPreferences_FileNameLangSelect, null);
        lang = Values.SharedPreferences_FileNameLangSelect;
        // ******************** Receive data from previous activity ***************************
        ProdID = getIntent().getExtras().getInt("ProdID");
        ID = getIntent().getExtras().getInt("ID");
        Name = getIntent().getExtras().getString("Name");
        Email = getIntent().getExtras().getString("Email");
        Password = getIntent().getExtras().getString("Password");
        Phone = getIntent().getExtras().getString("Mobile");
        IsHome = getIntent().getExtras().getBoolean("IsHome");

        // ********************* Call Views  *****************
        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);

        btn_Login = findViewById(R.id.btn_Set_Login);
        Mobile = findViewById(R.id.edit_Code);
        Enter = findViewById(R.id.Enter);
        MobileNumer = findViewById(R.id.MobileNumer);
        ChaneNumer = findViewById(R.id.ChaneNumer);
        ReSend = findViewById(R.id.ReSend);
        Timer = findViewById(R.id.Timer);
        Conditions = findViewById(R.id.Conditions);

        // ********************* Set Fonts *****************

        fontHelvetica = Typeface.createFromAsset(activity.getAssets(), "fonts/Helvetica_33244fbeea10f093ae87de7a994c3697.ttf");
        fontMedim = Typeface.createFromAsset(activity.getAssets(), "fonts/GE_SS_Two_Medium.otf");
        fontLight = Typeface.createFromAsset(activity.getAssets(), "fonts/GE_SS_Two_Light.otf");
        fontUltra = Typeface.createFromAsset(activity.getAssets(), "fonts/ArbFONTS-GESSTextUltraLight-UltraLight.otf");

        //********************* Pass Fonts *****************
        btn_Login.setTypeface(fontMedim);
        Mobile.setTypeface(fontHelvetica);
        Enter.setTypeface(fontLight);
        MobileNumer.setTypeface(fontHelvetica);
        ChaneNumer.setTypeface(fontLight);
        ReSend.setTypeface(fontLight);
        Timer.setTypeface(fontLight);
        Conditions.setTypeface(fontMedim);
    }

    public String convertToArabic(String value) {
        String newValue = (((((((((((value + "")
                .replaceAll("1", "١")).replaceAll("2", "٢"))
                .replaceAll("3", "٣")).replaceAll("4", "٤"))
                .replaceAll("5", "٥")).replaceAll("6", "٦"))
                .replaceAll("7", "٧")).replaceAll("8", "٨"))
                .replaceAll("9", "٩")).replaceAll("0", "٠"));
        return newValue;
    }

    private void verifyCode(String code) {
        Log.d("TestApp_1", code + "");
        Log.d("TestApp_2", convertToArabic(CodeX) + "");
        if (code.equals(CodeX)) {
            if (IsHome) {
                register();
            } else {
                Intent i = new Intent(activity, NewPass.class);
                i.putExtra("ProdID", ProdID);
                i.putExtra("Mobile", Phone);
                startActivity(i);
            }
        }
    }

    private void sendVerificationCode(String number) {
        AsyncHttpClient client = new AsyncHttpClient();
        String Url;

        CodeX = getRandomNumberString();

        Url="https://www.smsbox.com/SMSGateway/Services/Messaging.asmx/Http_SendSMS?username=Rummanah" +
                "&password=KH.Rummana@2816&customerid=2954&sendertext=Rummanah" +
                "&messageBody=Code%20Is%20:" + CodeX +
                "recipientNumbers="+ number +"&defdate=&isBlink=false&isFlash=false";



        Log.i("TestApp", Url);
        Log.d("TAG", "Send Message Url: " + Url);
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
                    reSendTimer();

                } catch (Exception e) {
                    Log.i("TestApp_3_Error", e.getMessage() + "");
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
                HideAll.setVisibility(View.GONE);
                prog.setVisibility(View.GONE);
                super.onFinish();

            }
        });

    }

    public static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format(java.util.Locale.US, "%06d", number);
    }


    private void reSendTimer() {

        Handler handler = new Handler();
        int delay = 1000; //milliseconds
        Second = 60;
        handler.postDelayed(new Runnable() {
            public void run() {

                Timer.setText(Second + " " + getResources().getString(R.string.Second));
                Second--;
                if (Second > 0) {
                    handler.postDelayed(this, delay);
                } else {
                    Timer.setText(R.string.ResendClick);
                }
            }
        }, delay);

    }

    public void register() {
        HideAll.setVisibility(View.VISIBLE);
        prog.setVisibility(View.VISIBLE);
        OkHttpClient client = new OkHttpClient();
        String Url = Values.Link_service + "auth/register/" + lang + "/v1";
        String json = new StringBuilder()
                .append("{")
                .append("\"name\":\"" + Name + "\",")
                .append("\"phone\":\"" + Phone + "\",")
                .append("\"email\":\"" + Email + "\",")
                .append("\"password\":\"" + Password + "\",")
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

                    LoginData = g.fromJson(response.body().string(), t);

                    activity.runOnUiThread(() -> {
                        if (!LoginData.success) {
                            loadMsg(LoginData.message);
                        } else {
                            LoginHolder.getInstance().setData("login");
                            FaceIdHolder.getInstance().setData(ID + "");
                            UsernameHolder.getInstance().setData(LoginData.data.name + "");
                            UserMobileHolder.getInstance().setData(LoginData.data.phone + "");
                            UserEmailHolder.getInstance().setData(LoginData.data.email + "");
                            UserTokenHolder.getInstance().setData(LoginData.data.token);
                            Login = getSharedPreferences(Values.SharedPreferences_FileName, 0);
                            SharedPreferences.Editor editor = Login.edit();
                            editor.putString("isLogin", "login");
                            editor.putString("UserID", FaceIdHolder.getInstance().getData());
                            editor.putString("UserName", UsernameHolder.getInstance().getData());
                            editor.putString("UserMobile", UserMobileHolder.getInstance().getData());
                            editor.putString("UserEmail", UserEmailHolder.getInstance().getData());
                            editor.putString("Token", UserTokenHolder.getInstance().getData().access_token);
                            editor.putString("Token_Type", UserTokenHolder.getInstance().getData().token_type);
                            editor.putString("Token_Exp", UserTokenHolder.getInstance().getData().expires_in);
                            editor.commit();


                            if (ProdID > 0) {
                                Intent i = new Intent(activity, Product.class);
                                i.putExtra("ID", ProdID);
                                startActivity(i);
                            } else {
                                Intent i = new Intent(activity, Home.class);
                                startActivity(i);
                            }

                        }
                    });

                } catch (Exception e) {
                    Log.i("TestApp_3_Error", e.getMessage() + "");
                }
            }

        });
        HideAll.setVisibility(View.GONE);
        prog.setVisibility(View.GONE);
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
            if (LoginHolder.getInstance().getData().equals("login")) {

                startActivity(new Intent(activity, Login.class));
            }
        } catch (Exception e) {

        }
        try {
        } catch (Exception e) {
            NetWork.gotoError(activity);
        }
        super.onResume();

    }

    public void gotoBack(View v) {
        super.onBackPressed();
    }


}
