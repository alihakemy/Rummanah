package customLists;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;

import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.usmart.com.rummanah.ProductsBrand;
import com.usmart.com.rummanah.R;

import java.util.ArrayList;

import constants.Values;
import dataInLists.DataInHome;

@SuppressLint("ResourceAsColor")
public class CustomListHomeBrands extends RecyclerView.Adapter<CustomListHomeBrands.ViewHolder> {
    private Activity Activity;
    private ArrayList<DataInHome.HomeContent> Data;
    private DisplayImageOptions options;

    public CustomListHomeBrands(Activity context, ArrayList<DataInHome.HomeContent> data) {
        // TODO Auto-generated constructor stub
        this.Activity = context;
        this.Data = data;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.singel_brand_list, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        holder.txtTitle.setText(Html.fromHtml(Data.get(position).title));

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.def)
                .showImageForEmptyUri(R.mipmap.def)
                .showImageOnFail(R.mipmap.def)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(20))
                .build();
        ImageLoader.getInstance().displayImage(Values.Link_Image + Data.get(position).image, holder.imageView, options);

        holder.List.setOnClickListener(v -> {
            Intent i = new Intent(Activity, ProductsBrand.class);
            i.putExtra("BrandID", Data.get(position).id);
            i.putExtra("BrandName", Data.get(position).title);
            Activity.startActivity(i);

        });

    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        ImageView imageView;
        RelativeLayout List;

        public ViewHolder(View rowView) {
            super(rowView);

            // get the reference of item view's
            txtTitle = rowView.findViewById(R.id.tv_Title);
            imageView = rowView.findViewById(R.id.iv_Feeds);
            List = rowView.findViewById(R.id.List);
            Typeface font = Typeface.createFromAsset(Activity.getAssets(), "fonts/GE_SS_Two_Light.otf");
            txtTitle.setTypeface(font);
        }
    }


}
