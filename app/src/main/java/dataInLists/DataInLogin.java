package dataInLists;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.Serializable;
import java.util.ArrayList;

import constants.Values;

import helpers.MyApp;

public class DataInLogin implements Serializable {



    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public Data datas = new Data();

    public boolean success=true;
    public int code=1;
    public String message_en="";
    public String message_ar="";

    private Context context;
    public DataInLogin(Context context) {
        this.context=context;
        SharedPreferences login = context.getSharedPreferences(Values.SharedPreferences_FileName, 0);
        login.getString("isLogin", "login");
        datas.id= Integer.parseInt(login.getString("UserID", "0"));
        datas.name= login.getString("UserName","" );
        datas.phone= login.getString("UserMobile", "");
        datas.email= login.getString("UserEmail", "");
        datas.token.access_token=   login.getString("Token","12");
        datas.token.token_type=login.getString("Token_Type","");
        login.getString("Token_Exp", "");
    }



    public class Data {
        public int id=0;
        public Tokens token = new Tokens();
        public String name="";
        public String phone="";
        public String email="";
        public String password="";
        public String fcm_token="";
        public String verification_code="";
        public String gender="";
        public String updated_at="";
        public String created_at="";
    }

    public static class Tokens {
        SharedPreferences login = MyApp.context.getSharedPreferences(Values.SharedPreferences_FileName, 0);

        public Tokens() {

            access_token =login.getString("Token","12");
            token_type = "";
            expires_in = "";
        }

        public String access_token = "bearer "+ login.getString("Token","12");
        public String token_type = "";
        public String expires_in ="";
    }


}


