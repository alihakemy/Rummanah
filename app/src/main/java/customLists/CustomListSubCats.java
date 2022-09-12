package customLists;

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
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.usmart.com.rummanah.R;

import java.util.ArrayList;

import constants.Values;
import dataInLists.DataInSubCats;

@SuppressLint("ResourceAsColor")
public class CustomListSubCats extends ArrayAdapter<DataInSubCats.SubCats> {
    private Activity Activity;
    private ArrayList<DataInSubCats.SubCats> Data;
    private DisplayImageOptions options;

    public CustomListSubCats(Activity context, ArrayList<DataInSubCats.SubCats> data) {
        super(context, R.layout.singel_cats_home_list, data);
        // TODO Auto-generated constructor stub
        this.Activity = context;
        this.Data = data;

    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = Activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.singel_cats_home_list, null, true);
        TextView txtTitle = rowView.findViewById(R.id.tv_Title);
        ImageView imageView = rowView.findViewById(R.id.iv_Feeds);

        Typeface fontMedim = Typeface.createFromAsset(Activity.getAssets(), "fonts/GE_SS_Two_Medium.otf");
        Typeface fontLight = Typeface.createFromAsset(Activity.getAssets(), "fonts/GE_SS_Two_Light.otf");
        Typeface fontUltra = Typeface.createFromAsset(Activity.getAssets(), "fonts/ArbFONTS-GESSTextUltraLight-UltraLight.otf");
        txtTitle.setTypeface(fontLight);

        txtTitle.setText(Data.get(position).title);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.def_icon)
                .showImageForEmptyUri(R.mipmap.def_icon)
                .showImageOnFail(R.mipmap.def_icon)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(20))
                .build();
        ImageLoader.getInstance().displayImage(Values.Link_Image + Data.get(position).image, imageView, options);



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

    @Override
    public Filter getFilter() {
        return new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                Data = (ArrayList<DataInSubCats.SubCats>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<DataInSubCats.SubCats> filteredResults = getFilteredResults(constraint);

                FilterResults results = new FilterResults();
                results.values = filteredResults;
                // results.count = filteredResults.size();

                return results;
            }
        };
    }

    @SuppressLint("DefaultLocale")
    private ArrayList<DataInSubCats.SubCats> getFilteredResults(CharSequence constraint) {

        int x = Data.size();
        int y = 0;
        for (int i = 0; i < x; i++) {
            if (Data.get(y).title.contains(constraint) == true) {

            } else {
                Data.remove(y);
                y--;

            }
            y++;

        }
        return Data;
    }

}
