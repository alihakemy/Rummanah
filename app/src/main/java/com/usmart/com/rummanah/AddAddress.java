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
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Locale;

import constants.Values;
import dataInLists.DataInAddressItem;
import dataInLists.DataInGlobal;
import dataInLists.DataInLogin;
import helpers.FaceIdHolder;
import helpers.LangHolder;
import helpers.LoginHolder;
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


public class AddAddress extends FragmentActivity {
    Activity activity = AddAddress.this;
    private static final String TAG = AddAddress.class.getSimpleName();
    SharedPreferences Login;
    EditText ed_Name, ed_Mobile, ed_Block, ed_Street, ed_Gada, ed_House, ed_Floor,
            ed_Room, ed_Desc, ed_Key;
    TextView tv_GetLocation, tv_Name, tv_Area, tv_Type, tv_Block, tv_Street, tv_Gada, tv_House, tv_Floor,
            tv_Room, tv_Desc, tv_Mobile, ed_Area, ed_Type;

    DataInAddressItem _Data = new DataInAddressItem();
    Button btn_Add;
    TextView MainTitle;
    ImageView HideAll, Back;
    ProgressBar prog;
    Typeface fontHelvetica, fontMedim, fontLight, fontUltra;
    double Lat, Lng;
    String AreaName;
    int AreaID = 0, AddressID = 0, RequestCode;
    int AddressType = 0;
    String lang;
    String AddressName = "";

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
        // ***********************************************
        //*******************************************************
        setContentView(R.layout.activity_add_address);
        // ***********************************************
        /*if (LangHolder.getInstance().getData().equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }*/
        lang = getSharedPreferences(Values.SharedPreferences_FileNameLangSelect, 0)
                .getString(Values.SharedPreferences_FileNameLangSelect, null);
        lang = Values.SharedPreferences_FileNameLangSelect;

        try {
            // AreaName = getIntent().getExtras().getString("AreaName");
            RequestCode = getIntent().getIntExtra("RequestCode", 0);
            Lat = getIntent().getExtras().getDouble("Lat");
            Lng = getIntent().getExtras().getDouble("Lng");
        } catch (Exception e) {
            //AreaName = "";
            Lat = 0;
            Lng = 0;
        }

        String DeviceId = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.d(TAG, "My Device Id: " + DeviceId);


        //AddressID = Integer.toString(0);
        //AddressName = "";

        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);

        fontHelvetica = Typeface.createFromAsset(activity.getAssets(), "fonts/Helvetica_33244fbeea10f093ae87de7a994c3697.ttf");
        fontMedim = Typeface.createFromAsset(activity.getAssets(), "fonts/GE_SS_Two_Medium.otf");
        fontLight = Typeface.createFromAsset(activity.getAssets(), "fonts/GE_SS_Two_Light.otf");
        fontUltra = Typeface.createFromAsset(activity.getAssets(), "fonts/ArbFONTS-GESSTextUltraLight-UltraLight.otf");

        MainTitle = findViewById(R.id.MainTitle);

        ed_Name = findViewById(R.id.edit_Name);
        ed_Area = findViewById(R.id.edit_Area);
        ed_Block = findViewById(R.id.edit_Block);
        ed_Street = findViewById(R.id.edit_Street);
        ed_Gada = findViewById(R.id.edit_Gada);
        ed_House = findViewById(R.id.edit_House);
        ed_Floor = findViewById(R.id.edit_Floor);
        ed_Room = findViewById(R.id.edit_Room);
        ed_Desc = findViewById(R.id.edit_Desc);
        ed_Mobile = findViewById(R.id.edit_Mobile);
        ed_Key = findViewById(R.id.edit_Key);
        ed_Type = findViewById(R.id.edit_Type);
        btn_Add = findViewById(R.id.btn_Add);
        Back = findViewById(R.id.Back);
        MainTitle = findViewById(R.id.MainTitle);

        tv_Name = findViewById(R.id.tv_Name);
        tv_Area = findViewById(R.id.tv_Area);
        tv_Block = findViewById(R.id.tv_Block);
        tv_Street = findViewById(R.id.tv_Street);
        tv_Gada = findViewById(R.id.tv_Gada);
        tv_House = findViewById(R.id.tv_House);
        tv_Floor = findViewById(R.id.tv_Floor);
        tv_Room = findViewById(R.id.tv_Room);
        tv_Desc = findViewById(R.id.tv_Desc);
        tv_Mobile = findViewById(R.id.tv_Mobile);
        tv_Type = findViewById(R.id.tv_Type);
        tv_GetLocation = findViewById(R.id.tv_GetLocation);


        MainTitle.setTypeface(fontMedim);
        ed_Name.setTypeface(fontLight);
        ed_Area.setTypeface(fontLight);
        tv_Area.setTypeface(fontLight);
        ed_Block.setTypeface(fontLight);
        ed_Mobile.setTypeface(fontHelvetica);
        ed_Key.setTypeface(fontHelvetica);
        btn_Add.setTypeface(fontLight);
        ed_Street.setTypeface(fontLight);
        ed_Gada.setTypeface(fontLight);
        ed_House.setTypeface(fontHelvetica);
        ed_Floor.setTypeface(fontHelvetica);
        ed_Room.setTypeface(fontHelvetica);
        ed_Desc.setTypeface(fontLight);
        ed_Type.setTypeface(fontLight);
        tv_GetLocation.setTypeface(fontLight);

        tv_Name.setTypeface(fontLight);
        tv_Area.setTypeface(fontLight);
        tv_Block.setTypeface(fontLight);
        tv_Mobile.setTypeface(fontLight);
        tv_Street.setTypeface(fontLight);
        tv_Gada.setTypeface(fontLight);
        tv_House.setTypeface(fontLight);
        tv_Floor.setTypeface(fontLight);
        tv_Room.setTypeface(fontLight);
        tv_Desc.setTypeface(fontLight);
        tv_Type.setTypeface(fontLight);


        MainTitle.setText(R.string.AddAddress);
        tv_GetLocation.setOnClickListener(view -> {
            Intent i = new Intent(activity, MapArea.class);
            startActivity(i);
        });

        ed_Area.setOnClickListener(v -> {
            Intent i = new Intent(activity, SelectArea.class);
            startActivityForResult(i, 10);
        });
        ed_Type.setOnClickListener(v -> selectType());

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_Add.setOnClickListener(v -> {
            if (ed_Name.getText().toString().matches("")
                    || ed_Block.getText().toString().matches("") || ed_Street.getText().toString().matches("")
                    || ed_House.getText().toString().matches("") || ed_Floor.getText().toString().matches("")
                    || ed_Mobile.getText().toString().matches("")) {
                loadMsg(getResources().getString(R.string.EmptyField), false);
                return;
            }
            if (Patterns.PHONE.matcher(ed_Key.getText().toString() + ed_Mobile.getText().toString()).matches() == false) {
                loadMsg(getResources().getString(R.string.ValidMobile), false);
                return;
            }

            if (AreaID == 0) {
                loadMsg(getResources().getString(R.string.ValidArea), false);
                return;
            }

            if (AddressType == 0) {
                loadMsg(getResources().getString(R.string.ValidType), false);
                return;
            }

            HideAll.setVisibility(View.VISIBLE);
            prog.setVisibility(View.VISIBLE);
            btn_Add.setEnabled(false);

            OkHttpClient client = new OkHttpClient();
            String Url = Values.Link_service + "addresses/" + lang + "/v1";
            String Auth, json;
            if (LoginHolder.getInstance().getData().equals("login")) {
                try {
                    Auth = UserTokenHolder.getInstance().getData().token_type
                            + " " + UserTokenHolder.getInstance().getData().access_token;
                }catch (Exception  e){
                    Auth = "";
                }

                json = new StringBuilder()
                        .append("{")
                        .append("\"address_type\":" + AddressType + ",")
                        .append("\"area_id\":" + AreaID + ",")
                        .append("\"title\":\"" + ed_Name.getText().toString() + "\",")
                        .append("\"phone\":\"" + ed_Mobile.getText().toString() + "\",")
                        .append("\"piece\":\"" + ed_Block.getText().toString() + "\",")
                        .append("\"gaddah\":\"" + ed_Gada.getText().toString() + "\",")
                        .append("\"building\":\"" + ed_House.getText().toString() + "\",")
                        .append("\"floor\":\"" + ed_Floor.getText().toString() + "\",")
                        .append("\"apartment_number\":\"" + ed_Room.getText().toString() + "\",")
                        .append("\"street\":\"" + ed_Street.getText().toString() + "\",")
                        .append("\"extra_details\":\"" + ed_Desc.getText().toString() + "\",")
                        .append("\"latitude\":\"" + Lat + "\",")
                        .append("\"longitude\":\"" + Lng + "\",")
                        .append("\"unique_id\":\"" + "\"}").toString();
                Log.d(TAG, "Logged user json address: " + json);
            } else {
                Auth = Values.Authorization_User;
                json = new StringBuilder()
                        .append("{")
                        .append("\"address_type\":" + AddressType + ",")
                        .append("\"area_id\":" + AreaID + ",")
                        .append("\"title\":\"" + ed_Name.getText().toString() + "\",")
                        .append("\"phone\":\"" + ed_Mobile.getText().toString() + "\",")
                        .append("\"piece\":\"" + ed_Block.getText().toString() + "\",")
                        .append("\"gaddah\":\"" + ed_Gada.getText().toString() + "\",")
                        .append("\"building\":\"" + ed_House.getText().toString() + "\",")
                        .append("\"floor\":\"" + ed_Floor.getText().toString() + "\",")
                        .append("\"apartment_number\":\"" + ed_Room.getText().toString() + "\",")
                        .append("\"street\":\"" + ed_Street.getText().toString() + "\",")
                        .append("\"extra_details\":\"" + ed_Desc.getText().toString() + "\",")
                        .append("\"latitude\":\"" + Lat + "\",")
                        .append("\"longitude\":\"" + Lng + "\",")
                        .append("\"unique_id\":\"" + Settings.Secure.getString(activity.getContentResolver(),
                                Settings.Secure.ANDROID_ID) + "\"")
                        .append("}").toString();
                Log.d(TAG, "guest json address: " + json);
            }

            RequestBody body = RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"),
                    json
            );
            Request request = new Request.Builder()
                    .url(Url)
                    .addHeader("Content-Type", "application/json")
                    /*.addHeader("Authorization", "" + UserTokenHolder.getInstance().getData().token_type
                            + " " + UserTokenHolder.getInstance().getData().access_token)*/
                    .addHeader("Authorization", Auth)
                    .post(body)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("TestApp_5", e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //Log.i("TestApp",response.body().string());
                    try {
                        Gson g = new Gson();
                        Type t = new TypeToken<DataInAddressItem>() {
                        }.getType();

                        _Data = g.fromJson(response.body().string(), t);

                        activity.runOnUiThread(() -> {
                            if (!_Data.success) {
                                loadMsg(_Data.message, false);
                            } else {
                                AddressID = _Data.data.id;
                                AddressName = _Data.data.title;
                                Log.d(TAG, "Value of AddressId:" + AddressID);
                                Log.d(TAG, "Value of AddressName:" + AddressName);
                                loadMsg(getResources().getString(R.string.AddressAdded), true);
                                /*Intent i = new Intent(activity, AddAddress.class);
                                i.putExtra("AddressID", AddressID);
                                startActivity(i);*/
                            }
                        });

                    } catch (Exception e) {
                        Log.i("TestApp_3_Error", e.getMessage() + "");
                    }
                }

            });

            btn_Add.setEnabled(true);
            HideAll.setVisibility(View.GONE);
            prog.setVisibility(View.GONE);
        });
    }

    private void setDefAddress(int Address) {
        OkHttpClient client = new OkHttpClient();
        String Url = Values.Link_service + "addresses/setdefault/" + lang + "/v1";
        String json = new StringBuilder()
                .append("{")
                .append("\"address_id\":" + Address + "}").toString();
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
                                //Intent i = new Intent(activity, Login.class);
                                //startActivity(i);
                            } else {
                                loadMsg(Res.message, false);
                            }
                        } else {
                            /*Intent i = new Intent(activity, Checkout.class);
                            i.putExtra("AddressID", AddressID);
                            i.putExtra("AddressName", AddressName);
                            startActivity(i);*/
                            loadMsg(Res.message, true);
                        }
                    });

                } catch (Exception e) {
                }
            }

        });

        HideAll.setVisibility(View.GONE);
        prog.setVisibility(View.GONE);

    }

    private void selectType() {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.msg_noads3);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView Title = dialog.findViewById(R.id.MsgTitle);
        TextView Text = dialog.findViewById(R.id.MsgText);

        Button Yes = dialog.findViewById(R.id.btn_Yes);
        Button No = dialog.findViewById(R.id.btn_No);

        Yes.setText(R.string.House);
        No.setText(R.string.Work);
        Title.setText(R.string.HouseType);
        Text.setText(R.string.SelectHouseType);


        Yes.setTypeface(fontMedim);
        No.setTypeface(fontMedim);
        Title.setTypeface(fontMedim);
        Text.setTypeface(fontUltra);

        Yes.setOnClickListener(v -> {
            AddressType = 1;
            ed_Type.setText(R.string.House);
            dialog.dismiss();
        });
        No.setOnClickListener(v -> {
            AddressType = 2;
            ed_Type.setText(R.string.Work);
            dialog.dismiss();
        });
        dialog.show();
    }

    private void loadMsg(String MSG, boolean t) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.msg_noads3);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
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
            if (t) {
                AddressID = _Data.data.id;
                AddressName = _Data.data.title;
                //setDefAddress(AddressID);
                setOnBack();
                /*Intent intent = new Intent(AddAddress.this, Checkout.class);
                intent.putExtra("AddressID", AddressID);
                intent.putExtra("AddressName", AddressName);
                startActivity(intent);*/
            }
        });
        dialog.show();
    }

    public void setOnBack() {
        if (RequestCode == 11) {
            Intent intent = new Intent(AddAddress.this, SelectAddress.class);
            intent.putExtra("AddressID", AddressID);
            intent.putExtra("AddressName", AddressName);
            startActivity(intent);
        } else if (RequestCode == 12) {
            Intent intent = new Intent(AddAddress.this, Checkout.class);
            intent.putExtra("AddressID", AddressID);
            intent.putExtra("AddressName", AddressName);
            startActivity(intent);
        }
    }

    private void loadMsgDone(String MSG) {
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
            startActivity(new Intent(activity, Cart.class));
        });
        dialog.show();
    }

    @Override
    protected void onResume() {

        /*try {
            if (LoginHolder.getInstance().getData().equals("login")) {
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

            } else {
                Login = getSharedPreferences(Values.SharedPreferences_FileName, 0);
                LoginHolder.getInstance().setData("logout");
                FaceIdHolder.getInstance().setData("0");
                SharedPreferences.Editor editor = Login.edit();
                editor.putString("isLogin", "logout");
                editor.putString("UserID", "0");
                editor.putString("UserName", "");
                editor.putString("UserMobile", "");
                editor.putString("UserEmail", "");
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

        }*/
        super.onResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            AreaID = data.getIntExtra("AreaID", 0);
            AreaName = data.getStringExtra("AreaName");
            if (AreaName.equals("")) {
                AreaName = getResources().getString(R.string.Area);
            }
            ed_Area.setText(AreaName + "");
        }
    }

    @Override
    public void onBackPressed() {
        setOnBack();
    }

    public void gotoBack(View v) {
        setOnBack();
    }
}

