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
import android.widget.Filter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.usmart.com.rummanah.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

import dataInLists.DataInCats;

@SuppressLint("ResourceAsColor")
public class CustomListMainCats extends ArrayAdapter<DataInCats.MainData> {
    private Activity Activity;
    private ArrayList<DataInCats.MainData> Data;
    private DisplayImageOptions options;

    public CustomListMainCats(Activity context, ArrayList<DataInCats.MainData> data) {
        super(context, R.layout.singel_text, data);
        // TODO Auto-generated constructor stub
        this.Activity = context;
        this.Data = data;

    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = Activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.singel_text, null, true);
        TextView txtTitle = rowView.findViewById(R.id.tv_Title);
        RelativeLayout List = rowView.findViewById(R.id.List);

        Typeface fontMedim = Typeface.createFromAsset(Activity.getAssets(), "fonts/GE_SS_Two_Medium.otf");
        Typeface fontLight = Typeface.createFromAsset(Activity.getAssets(), "fonts/GE_SS_Two_Light.otf");
        Typeface fontUltra = Typeface.createFromAsset(Activity.getAssets(), "fonts/ArbFONTS-GESSTextUltraLight-UltraLight.otf");
        txtTitle.setTypeface(fontLight);

        txtTitle.setText(Data.get(position).title);

        if(Data.get(position).selected){
            List.setBackground(Activity.getResources().getDrawable(R.drawable.border_right));
        }else{
            List.setBackground(Activity.getResources().getDrawable(R.drawable.empty));
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

        animation = null;

        return rowView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                Data = (ArrayList<DataInCats.MainData>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<DataInCats.MainData> filteredResults = getFilteredResults(constraint);

                FilterResults results = new FilterResults();
                results.values = filteredResults;
                // results.count = filteredResults.size();

                return results;
            }
        };
    }

    @SuppressLint("DefaultLocale")
    private ArrayList<DataInCats.MainData> getFilteredResults(CharSequence constraint) {

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
