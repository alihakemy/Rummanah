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
import android.widget.TextView;

import com.usmart.com.rummanah.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import dataInLists.DataInOrders;

@SuppressLint("ResourceAsColor")
public class CustomListOrders extends ArrayAdapter<DataInOrders.Orders> {
    private Activity Activity;
    private ArrayList<DataInOrders.Orders> Data;

    public CustomListOrders(Activity context, ArrayList<DataInOrders.Orders> data) {
        super(context, R.layout.singel_order_list, data);
        // TODO Auto-generated constructor stub
        this.Activity = context;
        this.Data = data;

    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Locale locale = new Locale("en", "UK");
        String pattern = "###.###";
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(locale);
        decimalFormat.applyPattern(pattern);

        LayoutInflater inflater = Activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.singel_order_list, null, true);
        TextView tv_OrderIDTxt = rowView.findViewById(R.id.tv_OrderIDTxt);
        TextView tv_Order = rowView.findViewById(R.id.tv_Order);
        TextView tv_Date = rowView.findViewById(R.id.tv_Date);
        TextView tv_OrderSysTxt = rowView.findViewById(R.id.tv_OrderSysTxt);
        TextView tv_OrderSys = rowView.findViewById(R.id.tv_OrderSys);
        TextView tv_CountTxt = rowView.findViewById(R.id.tv_CountTxt);
        TextView tv_Count = rowView.findViewById(R.id.tv_Count);
        TextView tv_PriceTxt = rowView.findViewById(R.id.tv_PriceTxt);
        TextView tv_Price = rowView.findViewById(R.id.tv_Price);
        TextView tv_Price_Curr = rowView.findViewById(R.id.tv_Price_Curr);
        TextView tv_Details = rowView.findViewById(R.id.tv_Details);
        TextView tv_Status = rowView.findViewById(R.id.tv_Status);


        Typeface fontMedim = Typeface.createFromAsset(Activity.getAssets(), "fonts/GE_SS_Two_Medium.otf");
        Typeface fontLight = Typeface.createFromAsset(Activity.getAssets(), "fonts/GE_SS_Two_Light.otf");
        Typeface fontUltra = Typeface.createFromAsset(Activity.getAssets(), "fonts/ArbFONTS-GESSTextUltraLight-UltraLight.otf");
        Typeface fontCeraPro = Typeface.createFromAsset(Activity.getAssets(), "fonts/cera_pro_med.ttf");


        tv_OrderIDTxt.setTypeface(fontMedim);
        //tv_Order.setTypeface(fontCeraPro);
      //  tv_Date.setTypeface(fontLight);
        tv_OrderSysTxt.setTypeface(fontLight);
       // tv_OrderSys.setTypeface(fontCeraPro);
        tv_CountTxt.setTypeface(fontLight);
       // tv_Count.setTypeface(fontCeraPro);
        tv_PriceTxt.setTypeface(fontLight);
       // tv_Price.setTypeface(fontCeraPro);
        tv_Price_Curr.setTypeface(fontMedim);
        tv_Details.setTypeface(fontLight);
        tv_Status.setTypeface(fontLight);

        tv_Order.setText(Data.get(position).order_number + "");
        tv_Date.setText(Data.get(position).date + "");
        tv_OrderSys.setText(Data.get(position).order_number + "");
        tv_Count.setText(Data.get(position).count + "");
        tv_Price.setText(decimalFormat.format(Data.get(position).total_price) + "");

        if (Data.get(position).status == 1) {
            tv_Status.setText(R.string.OrderCurrent);
        } else if (Data.get(position).status == 2) {
            tv_Status.setText(R.string.OrderDone);
        } else if (Data.get(position).status == 3) {
            tv_Status.setText(R.string.OrderCanceled);
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

                Data = (ArrayList<DataInOrders.Orders>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<DataInOrders.Orders> filteredResults = getFilteredResults(constraint);

                FilterResults results = new FilterResults();
                results.values = filteredResults;
                // results.count = filteredResults.size();

                return results;
            }
        };
    }

    @SuppressLint("DefaultLocale")
    private ArrayList<DataInOrders.Orders> getFilteredResults(CharSequence constraint) {

        int x = Data.size();
        int y = 0;
        for (int i = 0; i < x; i++) {
            if (Data.get(y).order_number.contains(constraint) == true) {

            } else {
                Data.remove(y);
                y--;

            }
            y++;

        }
        return Data;
    }

}
