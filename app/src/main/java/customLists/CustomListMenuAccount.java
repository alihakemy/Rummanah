package customLists;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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

import androidx.activity.OnBackPressedCallback;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.usmart.com.rummanah.Home;
import com.usmart.com.rummanah.R;

import java.util.ArrayList;

import dataInLists.DataInListIcons;
import helpers.UserMobileHolder;
import helpers.UsernameHolder;

@SuppressLint("ResourceAsColor")
public class CustomListMenuAccount extends ArrayAdapter<DataInListIcons> {
    private Activity Activity;
    private ArrayList<DataInListIcons> Data;
    private DisplayImageOptions options;

    public CustomListMenuAccount(Activity context, ArrayList<DataInListIcons> data) {
        super(context, R.layout.drawer_list_item_title, data);
        // TODO Auto-generated constructor stub
        this.Activity = context;
        this.Data = data;
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.def)
                .showImageForEmptyUri(R.mipmap.def).showImageOnFail(R.mipmap.def)
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
                .build();

    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = Activity.getLayoutInflater();
        View rowView;
        if (position == 0) {
            rowView = inflater.inflate(R.layout.drawer_list_item_title, null, true);
            ImageView Back = rowView.findViewById(R.id.Back);
            TextView UserName = rowView.findViewById(R.id.UserName);
            TextView Mobile = rowView.findViewById(R.id.Mobile);
            Typeface fontMedim = Typeface.createFromAsset(Activity.getAssets(), "fonts/GE_SS_Two_Medium.otf");
            Typeface fontLight = Typeface.createFromAsset(Activity.getAssets(), "fonts/GE_SS_Two_Light.otf");
            UserName.setTypeface(fontMedim);
            //Mobile.setTypeface(fontLight);

            UserName.setText(UsernameHolder.getInstance().getData());
            Mobile.setText(UserMobileHolder.getInstance().getData());

            /*Back.setOnClickListener(v -> Activity.startActivity(new Intent(Activity, Home.class)));*/
            Back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((Activity) inflater.getContext()).onBackPressed();
                }
            });

        } else {
            rowView = inflater.inflate(R.layout.drawer_list_item, null, true);
        }

        if (position > 0) {
            TextView txtTitle = rowView.findViewById(R.id.Menutitle);
            ImageView imageView = rowView.findViewById(R.id.Menuicon);
            Typeface font = Typeface.createFromAsset(Activity.getAssets(), "fonts/GE_SS_Two_Light.otf");
            txtTitle.setText(Data.get(position).name);
            txtTitle.setTypeface(font);
            Glide.with(getContext()).load(Data.get(position).photo)
                            .into(imageView);
        }
        if (position == 5) {
            TextView txtTitle = rowView.findViewById(R.id.Menutitle);
            txtTitle.setTextColor(Color.parseColor("#DB3022"));
        }

        Animation animation = null;
        int mode = 2;

        switch (mode) {
            case 1:
                animation = new ScaleAnimation((float) 1.0, (float) 1.0, (float) 0, (float) 1.0);
                break;

            case 2:
                animation = AnimationUtils.loadAnimation(Activity, R.anim.fade_in);
                break;
            case 3:
                animation = AnimationUtils.loadAnimation(Activity, R.anim.hyperspace_in);
                break;
            case 4:
                animation = AnimationUtils.loadAnimation(Activity, R.anim.hyperspace_out);
                break;
            case 5:
                animation = AnimationUtils.loadAnimation(Activity, R.anim.wave_scale);
                break;
            case 6:
                animation = AnimationUtils.loadAnimation(Activity, R.anim.push_left_in);
                break;
            case 7:
                animation = AnimationUtils.loadAnimation(Activity, R.anim.push_left_out);
                break;
            case 8:
                animation = AnimationUtils.loadAnimation(Activity, R.anim.push_up_in);
                break;
            case 9:
                animation = AnimationUtils.loadAnimation(Activity, R.anim.push_up_out);
                break;
            case 10:
                animation = AnimationUtils.loadAnimation(Activity, R.anim.shake);
                break;
            case 11:
                animation = AnimationUtils.loadAnimation(Activity, R.anim.slide_in_top);
                break;
            case 12:
                animation = AnimationUtils.loadAnimation(Activity, R.anim.slide_top_to_bottom);
                break;
            case 13:
                animation = AnimationUtils.loadAnimation(Activity, R.anim.slide_bottom_to_top);
                break;
        }
        animation.setDuration(500);

        rowView.startAnimation(animation);

        //   animation = null;

        return rowView;
    }


}
