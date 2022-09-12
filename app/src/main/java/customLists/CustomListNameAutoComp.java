package customLists;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.usmart.com.rummanah.Product;
import com.usmart.com.rummanah.R;

import java.util.ArrayList;

import constants.Values;
import dataInLists.DataInName;
public class CustomListNameAutoComp extends ArrayAdapter<DataInName.MainData> {
    private ArrayList<DataInName.MainData> items;
    private ArrayList<DataInName.MainData> itemsAll;
    private ArrayList<DataInName.MainData> suggestions;
    private Activity Activity;

    public CustomListNameAutoComp(Activity context, ArrayList<DataInName.MainData> items) {
        super(context, R.layout.singel_product_list3, items);
        this.items = items;
        this.itemsAll = (ArrayList<DataInName.MainData>) items.clone();
        this.suggestions = new ArrayList<>();
        this.Activity = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.singel_product_list3, null);
        }
        TextView txtTitle = rowView.findViewById(R.id.tv_Title);
        TextView txtPrice = rowView.findViewById(R.id.tv_MainPrice);
        TextView txtDiscount = rowView.findViewById(R.id.tv_BeforeDiscount);
        TextView txtDiscountPersent = rowView.findViewById(R.id.tv_discount);
        ImageView imageView = rowView.findViewById(R.id.iv_Feeds);


        FrameLayout List = rowView.findViewById(R.id.List);


        Typeface fontMedim = Typeface.createFromAsset(Activity.getAssets(), "fonts/GE_SS_Two_Medium.otf");
        Typeface fontLight = Typeface.createFromAsset(Activity.getAssets(), "fonts/GE_SS_Two_Light.otf");
        Typeface fontUltra = Typeface.createFromAsset(Activity.getAssets(), "fonts/ArbFONTS-GESSTextUltraLight-UltraLight.otf");
        Typeface fontAvenir = Typeface.createFromAsset(Activity.getAssets(), "fonts/avenir-lt-std-55-roman.otf");

        txtTitle.setTypeface(fontLight);
        txtPrice.setTypeface(fontAvenir);
        txtDiscount.setTypeface(fontAvenir);
        txtDiscountPersent.setTypeface(fontAvenir);

        int customer = items.get(position).id ;
        if (customer != 0) {


           txtPrice.setText(items.get(position).final_price + " " + Activity.getResources().getString(R.string.DK));
            txtDiscount.setText(items.get(position).price_before_offer + " " + Activity.getResources().getString(R.string.DK));

            if (items.get(position).offer == 0) {
                //   txtDiscount.setVisibility(View.GONE);
                txtPrice.setText(items.get(position).final_price + " " + Activity.getResources().getString(R.string.DK));
                txtDiscount.setText("");
                txtDiscountPersent.setVisibility(View.GONE);

            } else {
                txtDiscount.setPaintFlags(txtPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                txtDiscount.setVisibility(View.VISIBLE);
                txtPrice.setText(items.get(position).final_price + " " + Activity.getResources().getString(R.string.DK));
                txtDiscount.setText(items.get(position).price_before_offer + " " + Activity.getResources().getString(R.string.DK));
                txtDiscountPersent.setVisibility(View.VISIBLE);

                float d = items.get(position).offer_percentage;
                if ((d - (int) d) != 0)
                    txtDiscountPersent.setText(" - " + items.get(position).offer_percentage + " %");
                else
                    txtDiscountPersent.setText(" - " + (int) Math.round(items.get(position).offer_percentage) + " %");
            }

            int x = items.get(position).title.length();
            if (x >= 70) {
                txtTitle.setText(Html.fromHtml(items.get(position).title.substring(0, 70)) + " ...");
            } else {
                txtTitle.setText(Html.fromHtml(items.get(position).title));
            }

            DisplayImageOptions options;
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.def)
                    .showImageForEmptyUri(R.mipmap.def)
                    .showImageOnFail(R.mipmap.def)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            ImageLoader.getInstance().displayImage(Values.Link_Image + items.get(position).image, imageView, options);

            List.setOnClickListener(v -> {
                Intent i = new Intent(Activity, Product.class);
                i.putExtra("ID", items.get(position).id);
                Activity.startActivity(i);
                //    Activity.overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);

            });
        }else {
            txtTitle.setText(Html.fromHtml(items.get(position).title));
            imageView.setVisibility(View.GONE);
            txtDiscountPersent.setVisibility(View.GONE);
            txtDiscount.setVisibility(View.GONE);
            txtPrice.setVisibility(View.GONE);

        }
        return rowView;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            DataInName.MainData str = ((DataInName.MainData) (resultValue));
            return str.title ;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (DataInName.MainData item : itemsAll) {
                    if (item.title.toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(item);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<DataInName.MainData> filteredList = (ArrayList<DataInName.MainData>) results.values;
            try {
                if (results != null && results.count > 0) {
                    clear();
                    for (DataInName.MainData c : filteredList) {
                        add(c);
                    }
                    notifyDataSetChanged();
                }
            } catch (Exception e) {

            }
        }
    };

}