package com.usmart.com.rummanah;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
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
import dataInLists.DataInUser;
import helpers.LangHolder;
import helpers.LoginHolder;
import helpers.NetWork;
import helpers.OnlineHolder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NewPass extends Activity {
    Activity activity = NewPass.this;

    Button btn_Login;
    DataInUser LoginData;
    EditText Password, ConfPassword;
    ImageView HideAll;
    ProgressBar prog;
    Typeface fontHelvetica, fontMedim, fontLight, fontUltra;
    TextView Enter, Conditions;
    int ProdID;
    String Phone;
    String lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //   Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(activity, 2));
        //*******************************************************
        setContentView(R.layout.activity_new_password);

        /*if (LangHolder.getInstance().getData().equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }*/

        lang = getSharedPreferences(Values.SharedPreferences_FileNameLangSelect, 0)
                .getString(Values.SharedPreferences_FileNameLangSelect, null);
        lang = Values.SharedPreferences_FileNameLangSelect;

        Intent intent = getIntent();
        if (intent.hasExtra("DocLawID")) {
            ProdID = intent.getExtras().getInt("ProdID");
        } else {
            ProdID = 0;
        }
        Phone = getIntent().getExtras().getString("Mobile");

        // ***********************************************
        fontHelvetica = Typeface.createFromAsset(activity.getAssets(), "fonts/Helvetica_33244fbeea10f093ae87de7a994c3697.ttf");
        fontMedim = Typeface.createFromAsset(activity.getAssets(), "fonts/GE_SS_Two_Medium.otf");
        fontLight = Typeface.createFromAsset(activity.getAssets(), "fonts/GE_SS_Two_Light.otf");
        fontUltra = Typeface.createFromAsset(activity.getAssets(), "fonts/ArbFONTS-GESSTextUltraLight-UltraLight.otf");

        Conditions = findViewById(R.id.Conditions);
        btn_Login = findViewById(R.id.btn_Set_Login);
        Enter = findViewById(R.id.Enter);

        Password = findViewById(R.id.edit_Password);
        ConfPassword = findViewById(R.id.edit_ConfPassword);

        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);


        Enter.setTypeface(fontMedim);
        Conditions.setTypeface(fontMedim);
        Password.setTypeface(fontHelvetica);
        btn_Login.setTypeface(fontMedim);
        ConfPassword.setTypeface(fontHelvetica);

        Conditions.setOnClickListener(v -> {
            Intent i = new Intent(activity, Conditions.class);
            startActivity(i);
        });

        btn_Login.setOnClickListener(v -> {
            if (Password.getText().toString().matches("") || ConfPassword.getText().toString().matches("")) {
                loadMsg(getResources().getString(R.string.EmptyField));
                return;
            }
            if (!Password.getText().toString().equals(ConfPassword.getText().toString())) {
                loadMsg(getResources().getString(R.string.PassValid));
                return;
            }

            HideAll.setVisibility(View.VISIBLE);
            prog.setVisibility(View.VISIBLE);

            OkHttpClient client = new OkHttpClient();
            String Url = Values.Link_service + "user/resetforgettenpassword/" + lang + "/v1";
            String json = new StringBuilder()
                    .append("{")
                    .append("\"password\":\"" + Password.getText().toString() + "\",")
                    .append("\"phone\":\"" + Phone + "\"")
                    .append("}").toString();

            RequestBody body = RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"),
                    json
            );
            Request request = new Request.Builder()
                    .url(Url)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "" + Values.Authorization_User)
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
                        Type t = new TypeToken<DataInUser>() {
                        }.getType();

                        LoginData = g.fromJson(response.body().string(), t);


                        activity.runOnUiThread(() -> {
                            if (!LoginData.success) {
                                loadMsg(LoginData.message);
                            } else {
                                Intent i = new Intent(activity, Login.class);
                                i.putExtra("ProdID", ProdID);
                                startActivity(i);
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


}
