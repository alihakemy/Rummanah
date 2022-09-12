package com.usmart.com.rummanah;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

import constants.Values;

import dataInLists.DataInCheckUser;

import helpers.LangHolder;
import helpers.LoginHolder;
import helpers.NetWork;
import helpers.OnlineHolder;
import okhttp3.*;


public class SetRegister extends Activity {
    Activity activity = SetRegister.this;
    EditText ed_Pass, ed_Email, ed_Name, ed_Mobile, ed_Key;
    Button btn_register;
    TextView ForgetPassword, HasNewAccount, Login, Conditions, Enter;
    ImageView HideAll;
    ProgressBar prog;
    Typeface fontHelvetica, fontMedim, fontLight, fontUltra;
    int ProdID;
    DataInCheckUser LoginData;
    String lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(activity, 2));
        //*******************************************************
        setContentView(R.layout.activity_register);
        /*if (LangHolder.getInstance().getData().equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }*/
        lang = getSharedPreferences(Values.SharedPreferences_FileNameLangSelect, 0)
                .getString(Values.SharedPreferences_FileNameLangSelect, null);
        lang = Values.SharedPreferences_FileNameLangSelect;
        // ProdID = getIntent().getExtras().getInt("ProdID");
        Intent intent = getIntent();
        if (intent.hasExtra("ProdID")) {
            ProdID = intent.getExtras().getInt("ProdID");
        } else {
            ProdID = 0;
        }
        // ***********************************************

        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);

        fontHelvetica = Typeface.createFromAsset(activity.getAssets(), "fonts/Helvetica_33244fbeea10f093ae87de7a994c3697.ttf");
        fontMedim = Typeface.createFromAsset(activity.getAssets(), "fonts/GE_SS_Two_Medium.otf");
        fontLight = Typeface.createFromAsset(activity.getAssets(), "fonts/GE_SS_Two_Light.otf");
        fontUltra = Typeface.createFromAsset(activity.getAssets(), "fonts/ArbFONTS-GESSTextUltraLight-UltraLight.otf");

        ed_Name = findViewById(R.id.edit_Name);
        ed_Pass = findViewById(R.id.edit_Password);
        ed_Email = findViewById(R.id.edit_Email);
        ed_Mobile = findViewById(R.id.edit_Mobile);
        ed_Key = findViewById(R.id.edit_Key);
        btn_register = findViewById(R.id.btn_Set_Register);

        Enter = findViewById(R.id.Enter);
        Login = findViewById(R.id.Login);
        ForgetPassword = findViewById(R.id.ForgetPassword);
        HasNewAccount = findViewById(R.id.HasNewAccount);
        Conditions = findViewById(R.id.Conditions);

        ed_Name.setTypeface(fontHelvetica);
        ed_Pass.setTypeface(fontHelvetica);
        ed_Email.setTypeface(fontHelvetica);
        ed_Mobile.setTypeface(fontHelvetica);
        ed_Key.setTypeface(fontHelvetica);
        btn_register.setTypeface(fontMedim);
        Login.setTypeface(fontMedim);
        ForgetPassword.setTypeface(fontLight);
        HasNewAccount.setTypeface(fontUltra);
        Conditions.setTypeface(fontMedim);
        Enter.setTypeface(fontMedim);

        Conditions.setOnClickListener(v -> {
            Intent i = new Intent(activity, Conditions.class);
            startActivity(i);
        });

        ForgetPassword.setOnClickListener(v -> {
            Intent i = new Intent(activity, ForgetPass.class);
            i.putExtra("ProdID", ProdID);
            startActivity(i);
        });

        Login.setOnClickListener(v -> {
            Intent i = new Intent(activity, Login.class);
            i.putExtra("ProdID", ProdID);
            startActivity(i);
            //overridePendingTransition(R.anim.fadein_fast, R.anim.fadeout_fast);
        });

        btn_register.setOnClickListener(v -> {
            if (ed_Name.getText().toString().matches("")
                    || ed_Pass.getText().toString().matches("") || ed_Email.getText().toString().matches("")
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
            String Url = Values.Link_service + "user/checkphoneexistanceandroid/" + lang + "/v1";
            String json = new StringBuilder()
                    .append("{")
                    .append("\"phone\":\"" + ed_Key.getText().toString() + ed_Mobile.getText().toString() + "\",")
                    .append("\"email\":\"" + ed_Email.getText().toString() + "\"")
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
                        Type t = new TypeToken<DataInCheckUser>() {
                        }.getType();
                        String Res = response.body().string();
                        Log.i("TestApp_5", Res);
                        LoginData = g.fromJson(Res, t);

                        activity.runOnUiThread(() -> {
                            if (!LoginData.success) {
                                loadMsg(LoginData.message);
                            } else {
                                if (!LoginData.phone) {
                                    if (!LoginData.email) {
                                       // Log.i("TestApp_5", LoginData.message);
                                        Intent i = new Intent(activity, Activation.class);
                                        i.putExtra("ID", 0);
                                        i.putExtra("Mobile", ed_Key.getText().toString() + ed_Mobile.getText().toString());
                                        i.putExtra("Name", ed_Name.getText().toString());
                                        i.putExtra("Email", ed_Email.getText().toString());
                                        i.putExtra("Password", ed_Pass.getText().toString());
                                        i.putExtra("ProdID", ProdID);
                                        i.putExtra("IsHome", true);

                                        startActivity(i);
                                    } else {
                                        loadMsg(getResources().getString(R.string.ExistEmail));
                                    }

                                } else {
                                    loadMsg(getResources().getString(R.string.ExistPhone));
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
            // TODO: handle exception
        }
        try {
            if (LoginHolder.getInstance().getData().equals("login")) {

                // startActivity(new Intent(activity, Home.class));

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

