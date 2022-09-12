package com.usmart.com.rummanah;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import dataInLists.DataInLogin;
import dataInLists.DataInUser;
import helpers.FaceIdHolder;
import helpers.LangHolder;
import helpers.LoginHolder;
import helpers.UserTokenHolder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NewPassProfile extends Activity {
    Activity activity = NewPassProfile.this;
    SharedPreferences Login;
    Button btn_Login;
    DataInUser LoginData;
    EditText OldPassword , Password, ConfPassword;
    ImageView HideAll;
    ProgressBar prog;
    Typeface fontHelvetica, fontMedim, fontLight, fontUltra;
    TextView Enter;
    String lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //   Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(activity, 2));
        //*******************************************************
        setContentView(R.layout.activity_new_password_profile);
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

        btn_Login = findViewById(R.id.btn_Set_Login);
        Enter = findViewById(R.id.Enter);

        Password = findViewById(R.id.edit_Password);
        ConfPassword = findViewById(R.id.edit_ConfPassword);
        OldPassword = findViewById(R.id.edit_OldPassword);

        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);


        Enter.setTypeface(fontMedim);
        OldPassword.setTypeface(fontHelvetica);
        Password.setTypeface(fontHelvetica);
        btn_Login.setTypeface(fontMedim);
        ConfPassword.setTypeface(fontHelvetica);


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
            String Url = Values.Link_service + "user/resetpassword/" + lang + "/v1";
            String json = new StringBuilder()
                    .append("{")
                    .append("\"old_password\":\"" + OldPassword.getText().toString() + "\",")
                    .append("\"password\":\"" + Password.getText().toString() + "\"")
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
                        Type t = new TypeToken<DataInUser>() {
                        }.getType();

                        LoginData = g.fromJson(response.body().string(), t);


                        activity.runOnUiThread(() -> {
                            if (!LoginData.success) {
                                loadMsg(LoginData.message);
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
            Intent i = new Intent(activity, UpdateProfile.class);
            startActivity(i);
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
        super.onResume();

    }


}
