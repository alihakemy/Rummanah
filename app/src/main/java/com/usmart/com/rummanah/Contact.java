package com.usmart.com.rummanah;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Locale;

import constants.Values;
import dataInLists.DataInCall;
import dataInLists.DataInSetting;
import helpers.LangHolder;
import helpers.NetWork;
import helpers.OnlineHolder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Contact extends Activity {
    Activity activity = Contact.this;
    EditText ed_Mobile, ed_Msg;
    Button btn_Send;
    ImageView HideAll;
    ProgressBar prog;
    Typeface fontHelvetica, fontMedim, fontLight, fontUltra;
    TextView Enter, ForMore, Call;
    String lang;
    TextView MainTitle;
    DataInSetting DataSent = new DataInSetting();
    DataInCall Data = new DataInCall();
    private static final int INITIAL_REQUEST = 1337;
    private static final int CALL_REQUEST = INITIAL_REQUEST + 5;
    private static final String[] CALL_PERMS = {
            Manifest.permission.CALL_PHONE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        //*******************************************************
        setContentView(R.layout.activity_contact);
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

        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);

        MainTitle = findViewById(R.id.MainTitle);
        Enter = findViewById(R.id.Enter);
        ForMore = findViewById(R.id.ForMore);
        Call = findViewById(R.id.Call);
        ed_Msg = findViewById(R.id.edit_Msg);
        ed_Mobile = findViewById(R.id.edit_Mobile);
        btn_Send = findViewById(R.id.btn_Send);

        MainTitle.setText(R.string.Contact);


        MainTitle.setTypeface(fontLight);
        Enter.setTypeface(fontLight);
        ForMore.setTypeface(fontLight);
        Call.setTypeface(fontHelvetica);
        ed_Msg.setTypeface(fontLight);
        ed_Mobile.setTypeface(fontHelvetica);
        btn_Send.setTypeface(fontLight);


        btn_Send.setOnClickListener(v -> {
            if (ed_Mobile.getText().toString().matches("") || ed_Msg.getText().toString().matches("")) {
                Toast.makeText(activity, R.string.EmptyField, Toast.LENGTH_SHORT).show();
                return;
            }

            if (Patterns.PHONE.matcher(ed_Mobile.getText().toString()).matches() == false) {
                Toast.makeText(activity, R.string.ValidMobile, Toast.LENGTH_SHORT).show();
                return;
            }

            HideAll.setVisibility(View.VISIBLE);
            prog.setVisibility(View.VISIBLE);

            OkHttpClient client = new OkHttpClient();
            String Url = Values.Link_service + "contactus/" + lang + "/v1";
            String json = new StringBuilder()
                    .append("{")
                    .append("\"message\":\"" + ed_Msg.getText().toString() + "\",")
                    .append("\"phone\":\"" + ed_Mobile.getText().toString() + "\"")
                    .append("}").toString();

            RequestBody body = RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"),
                    json
            );
            Request request = new Request.Builder()
                    .url(Url)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", Values.Authorization_User)
                    .post(body)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    Log.i("TestApp_5", e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {

                        Gson g = new Gson();
                        Type t = new TypeToken<DataInSetting>() {
                        }.getType();

                        DataSent = g.fromJson(response.body().string(), t);

                        activity.runOnUiThread(() -> {
                            if (!DataSent.success) {
                                loadMsg(DataSent.message);
                            } else {
                                loadMsg(DataSent.data.message + "");
                                ed_Mobile.setText("");
                                ed_Msg.setText("");

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

        Call.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + Data.data));
            startActivity(callIntent);
        });

        loadData();
    }

    private void loadData() {
        AsyncHttpClient client = new AsyncHttpClient();
        String Url;

        Url = Values.Link_service + "getappnumber/" + lang + "/v1";
        Log.i("TestApp", Url);
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Authorization", "" + Values.Authorization_User);

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
                HideAll.setVisibility(View.GONE);
                prog.setVisibility(View.GONE);
                Log.i("TestApp", arg1+"");
                try {
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInCall>() {
                    }.getType();
                    Data = g.fromJson(arg1, t);
                    Call.setText(Data.data + "");

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
        if (!canCall()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(CALL_PERMS, CALL_REQUEST);
            }
        }
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
        } catch (Exception e) {
            NetWork.gotoError(activity);
        }
        super.onResume();

    }

    public void gotoBack(View v) {
        super.onBackPressed();
    }


}
