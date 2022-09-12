package customLists;


import java.util.ArrayList;

import constants.Values;
import dataInLists.DataInNoti;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.usmart.com.rummanah.R;

public class CustomListNoti extends ArrayAdapter<DataInNoti.Notification> {
    private Activity Activity;
    private ArrayList<DataInNoti.Notification> Data;
    private DisplayImageOptions options;

    public CustomListNoti(Activity context, ArrayList<DataInNoti.Notification> data) {
        super(context, R.layout.singel_noti_list, data);
        // TODO Auto-generated constructor stub
        this.Activity = context;
        this.Data = data;

    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = Activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.singel_noti_list, null, true);

        TextView txtTitle = rowView.findViewById(R.id.tv_title);
        TextView txtDesc = rowView.findViewById(R.id.tv_body);
        TextView txtDate = rowView.findViewById(R.id.tv_date);
        ImageView imageView = rowView.findViewById(R.id.iv_Feeds);

        Typeface fontMedim = Typeface.createFromAsset(Activity.getAssets(), "fonts/GE_SS_Two_Medium.otf");
        Typeface fontLight = Typeface.createFromAsset(Activity.getAssets(), "fonts/GE_SS_Two_Light.otf");
        Typeface fontUltra = Typeface.createFromAsset(Activity.getAssets(), "fonts/ArbFONTS-GESSTextUltraLight-UltraLight.otf");
        Typeface fontHelvetica = Typeface.createFromAsset(Activity.getAssets(), "fonts/Helvetica_33244fbeea10f093ae87de7a994c3697.ttf");

        txtTitle.setTypeface(fontLight);
        txtDesc.setTypeface(fontLight);
        txtDate.setTypeface(fontHelvetica);

        txtTitle.setText(Data.get(position).title);
        txtDesc.setText(Data.get(position).body);
        txtDate.setText(Data.get(position).created_at);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.def_icon)
                .showImageForEmptyUri(R.mipmap.def_icon)
                .showImageOnFail(R.mipmap.def_icon)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(Values.Link_Image + Data.get(position).image, imageView, options);


        Animation animation = null;
        byte mode = 1;

        switch (mode) {
            case 1:
                animation = new ScaleAnimation((float) 1.0, (float) 1.0, (float) 0,
                        (float) 1.0);
                break;

            case 2:
                animation = AnimationUtils.loadAnimation(Activity, R.anim.fade_in);
                break;
            case 3:
                animation = AnimationUtils.loadAnimation(Activity,
                        R.anim.hyperspace_in);
                break;
            case 4:
                animation = AnimationUtils.loadAnimation(Activity,
                        R.anim.hyperspace_out);
                break;
            case 5:
                animation = AnimationUtils.loadAnimation(Activity,
                        R.anim.wave_scale);
                break;
            case 6:
                animation = AnimationUtils.loadAnimation(Activity,
                        R.anim.push_left_in);
                break;
            case 7:
                animation = AnimationUtils.loadAnimation(Activity,
                        R.anim.push_left_out);
                break;
            case 8:
                animation = AnimationUtils.loadAnimation(Activity,
                        R.anim.push_up_in);
                break;
            case 9:
                animation = AnimationUtils.loadAnimation(Activity,
                        R.anim.push_up_out);
                break;
            case 10:
                animation = AnimationUtils.loadAnimation(Activity, R.anim.shake);
                break;
            case 11:
                animation = AnimationUtils.loadAnimation(Activity,
                        R.anim.slide_in_top);
                break;
            case 12:
                animation = AnimationUtils.loadAnimation(Activity,
                        R.anim.slide_top_to_bottom);
                break;
            case 13:
                animation = AnimationUtils.loadAnimation(Activity,
                        R.anim.slide_bottom_to_top);
                break;
        }
        animation.setDuration(500);

        rowView.startAnimation(animation);

        animation = null;

        return rowView;
    }

}
