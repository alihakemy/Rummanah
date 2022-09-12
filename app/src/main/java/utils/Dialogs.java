package utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.usmart.com.rummanah.Home;
import com.usmart.com.rummanah.MainActivity;
import com.usmart.com.rummanah.R;

import java.util.Locale;

import constants.Values;
import helpers.LangHolder;

public class Dialogs {
    static public void loadOKMsg(Activity activity, String MSG) {
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

        No.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    static public void loadOKMsg(Activity activity, String MSG, Class<?> Target) {
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

        No.setOnClickListener(v -> {
            dialog.dismiss();
            activity.startActivity(new Intent(activity, Target));
        });
        dialog.show();
    }

    static public void loadOKMsg(Activity activity, String MSG, String Button) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.msg_noads3);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView Title = dialog.findViewById(R.id.MsgTitle);
        TextView Text = dialog.findViewById(R.id.MsgText);

        Button Yes = dialog.findViewById(R.id.btn_Yes);
        Button No = dialog.findViewById(R.id.btn_No);

        No.setText(Button + "");
        Text.setText(MSG + "");
        Yes.setVisibility(View.GONE);

        No.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    static public void loadOKMsg(Activity activity, String MSG, String Button, Class<?> Target) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.msg_noads3);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView Title = dialog.findViewById(R.id.MsgTitle);
        TextView Text = dialog.findViewById(R.id.MsgText);

        Button Yes = dialog.findViewById(R.id.btn_Yes);
        Button No = dialog.findViewById(R.id.btn_No);

        No.setText(Button + "");
        Text.setText(MSG + "");
        Yes.setVisibility(View.GONE);

        No.setOnClickListener(v -> {
            dialog.dismiss();
            activity.startActivity(new Intent(activity, Target));
        });
        dialog.show();
    }

    static public void selectLang(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.msg_noads_options);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        TextView Yes = dialog.findViewById(R.id.btn_2);
        TextView No = dialog.findViewById(R.id.btn_1);
        TextView Cancel = dialog.findViewById(R.id.btn_3);

        Yes.setText(R.string.Arabic);
        No.setText(R.string.English);
        Yes.setOnClickListener(v -> {
            /*LangHolder.getInstance().setData("ar");
            SharedPreferences.Editor editor = activity
                    .getSharedPreferences(Values.SharedPreferences_FileNameLangSelect, 0).edit();
            editor.putString("Lang", "ar");
            editor.commit();

            Intent i = new Intent(activity, Home.class);
            activity.finish();
            activity.startActivity(i);
            activity.overridePendingTransition(R.anim.fadein_fast, R.anim.fadeout_fast);*/

            LangHolder.getInstance().setData("ar");
            SharedPreferences.Editor editor = activity
                    .getSharedPreferences(Values.SharedPreferences_FileNameLangSelect, 0).edit();
            editor.putString("Lang", "ar");
            editor.commit();
            Intent intent = new Intent(activity, Home.class);
            activity.finish();
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.fadein_fast, R.anim.fadeout_fast);
            dialog.dismiss();
        });

        No.setOnClickListener(v -> {

            LangHolder.getInstance().setData("en");
            SharedPreferences.Editor editor = activity
                    .getSharedPreferences(Values.SharedPreferences_FileNameLangSelect, 0).edit();
            editor.putString("Lang", "en");
            editor.commit();
            Intent intent = new Intent(activity, Home.class);
            activity.finish();
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.fadein_fast, R.anim.fadeout_fast);

            dialog.dismiss();
        });
        Cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }
}
