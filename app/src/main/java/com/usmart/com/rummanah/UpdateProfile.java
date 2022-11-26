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
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Locale;

import constants.Values;
import dataInLists.DataInLogin;
import dataInLists.DataInUserProfile;
import helpers.FaceIdHolder;
import helpers.LoginHolder;
import helpers.UserTokenHolder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class UpdateProfile extends Activity {
    Activity activity = UpdateProfile.this;
    SharedPreferences Login;
    EditText ed_DOB, ed_Email, ed_Name, ed_Mobile;
    Button btn_Save;
    TextView ChangePass, MainTitle;
    ImageView HideAll;
    ProgressBar prog;
    Typeface fontHelvetica, fontMedim, fontLight, fontUltra;
    DataInLogin LoginData;
    DataInUserProfile _Data = new DataInUserProfile();
    String lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        getWindow().setStatusBarColor(Color.WHITE);
        // ***********************************************
        setContentView(R.layout.activity_profile);

        lang = getSharedPreferences(Values.SharedPreferences_FileNameLangSelect, 0)
                .getString(Values.SharedPreferences_FileNameLangSelect, null);
        lang = Values.SharedPreferences_FileNameLangSelect;

        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);

        fontHelvetica = Typeface.createFromAsset(activity.getAssets(), "fonts/Helvetica_33244fbeea10f093ae87de7a994c3697.ttf");
        fontMedim = Typeface.createFromAsset(activity.getAssets(), "fonts/GE_SS_Two_Medium.otf");
        fontLight = Typeface.createFromAsset(activity.getAssets(), "fonts/GE_SS_Two_Light.otf");
        fontUltra = Typeface.createFromAsset(activity.getAssets(), "fonts/ArbFONTS-GESSTextUltraLight-UltraLight.otf");

        ed_Name = findViewById(R.id.edit_Name);
        ed_DOB = findViewById(R.id.edit_DOB);
        ed_Email = findViewById(R.id.edit_Email);
        ed_Mobile = findViewById(R.id.edit_Mobile);
        btn_Save = findViewById(R.id.btn_Set_Save);
        MainTitle = findViewById(R.id.MainTitle);

        ChangePass = findViewById(R.id.ChangePassword);

        MainTitle.setText(R.string.EditProfile);

        MainTitle.setTypeface(fontLight);
        ed_Name.setTypeface(fontLight);
        ed_DOB.setTypeface(fontHelvetica);
        ed_Email.setTypeface(fontHelvetica);
        ed_Mobile.setTypeface(fontHelvetica);
//        ed_Key.setTypeface(fontHelvetica);
        btn_Save.setTypeface(fontMedim);
        ChangePass.setTypeface(fontLight);

        ChangePass.setOnClickListener(view -> {
            Intent i = new Intent(activity, NewPassProfile.class);
            startActivity(i);
        });

        btn_Save.setOnClickListener(v -> {
            if (ed_Name.getText().toString().matches("")
                    || ed_DOB.getText().toString().matches("") || ed_Email.getText().toString().matches("")
                    || ed_Mobile.getText().toString().matches("")) {
                loadMsg(getResources().getString(R.string.EmptyField));
                return;
            }
            if (Patterns.EMAIL_ADDRESS.matcher(ed_Email.getText().toString()).matches() == false) {
                loadMsg(getResources().getString(R.string.ValidMail));
                return;
            }
            if (Patterns.PHONE.matcher(ed_Mobile.getText().toString()).matches() == false) {
                loadMsg(getResources().getString(R.string.ValidMobile));
                return;
            }

            HideAll.setVisibility(View.VISIBLE);
            prog.setVisibility(View.VISIBLE);

            OkHttpClient client = new OkHttpClient();
            String Url = Values.Link_service + "user/updateprofile/" + lang + "/v1";
            String json = new StringBuilder()
                    .append("{")
                    .append("\"name\":\"" + ed_Name.getText().toString() + "\",")
                    .append("\"phone\":\"" + ed_Mobile.getText().toString() + "\",")
                    .append("\"email\":\"" + ed_Email.getText().toString() + "\",")
                    .append("\"date_of_birth\":\"" + ed_DOB.getText().toString() + "\",")
                    .append("\"gender\":\"\"")
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
                    .put(body)
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
                        Type t = new TypeToken<DataInLogin>() {
                        }.getType();

                        LoginData = g.fromJson(response.body().string(), t);

                        activity.runOnUiThread(() -> {
                            if (!LoginData.success) {
                                loadMsg(LoginData.message_ar);
                            } else {
                                loadMsg(getResources().getString(R.string.UpdateDone));
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

    private void loadProfile() {
        AsyncHttpClient client = new AsyncHttpClient();
        String Url;

        Url = Values.Link_service + "user/profile/" + lang + "/v1";
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Authorization",  UserTokenHolder.getInstance().getData().access_token);
        client.get(Url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
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
                    Type t = new TypeToken<DataInUserProfile>() {
                    }.getType();
                    _Data = g.fromJson(arg1, t);

                    ed_Name.setText(_Data.data.user_name + "");
                    ed_Mobile.setText(_Data.data.phone + "");
                    ed_Email.setText(_Data.data.email + "");
                    // ed_DOB.setText(_Data.data.dob + "");

                } catch (Exception e) {
                    Log.i("TestApp", e.getMessage());
                }

            }

            @SuppressWarnings("deprecation")
            @Override
            public void onFailure(Throwable arg0) {
                // TODO Auto-generated method stub
                super.onFailure(arg0);
                Log.i("TestApp", arg0.getMessage());
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
        loadProfile();
        super.onResume();

    }

    public void gotoBack(View v) {
        super.onBackPressed();
    }
}

