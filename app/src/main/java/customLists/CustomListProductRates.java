package customLists;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.usmart.com.rummanah.R;

import java.util.ArrayList;

import dataInLists.DataInProduct;

public class CustomListProductRates extends ArrayAdapter<DataInProduct.Rates> {
    private android.app.Activity Activity;
    private ArrayList<DataInProduct.Rates> Data;

    public CustomListProductRates(Activity context, ArrayList<DataInProduct.Rates> data) {
        super(context, R.layout.singel_option_list, data);
        // TODO Auto-generated constructor stub
        this.Activity = context;
        this.Data = data;

    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = Activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.single_high_rate, null, true);
        TextView tv_name = rowView.findViewById(R.id.tv_name);
        TextView tv_date = rowView.findViewById(R.id.tv_date);
        RatingBar rb_rating = rowView.findViewById(R.id.rb_rating);
        TextView tv_content = rowView.findViewById(R.id.tv_content);


        Typeface fontMedim = Typeface.createFromAsset(Activity.getAssets(), "fonts/GE_SS_Two_Medium.otf");
        Typeface fontLight = Typeface.createFromAsset(Activity.getAssets(), "fonts/GE_SS_Two_Light.otf");
        Typeface fontUltra = Typeface.createFromAsset(Activity.getAssets(), "fonts/ArbFONTS-GESSTextUltraLight-UltraLight.otf");

        //tv_name.setTypeface(fontLight);
        //tv_date.setTypeface(fontLight);

        tv_name.setText(Data.get(position).user.name + "");
        tv_date.setText(Data.get(position).created_at + "");
        rb_rating.setRating((float) Data.get(position).rate);
        tv_content.setText(Data.get(position).text);


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

        animation = null;

        return rowView;
    }
}
