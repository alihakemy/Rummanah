package customLists;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.usmart.com.rummanah.Product;
import com.usmart.com.rummanah.Products;
import com.usmart.com.rummanah.R;

import java.util.ArrayList;
import java.util.List;

import constants.Values;
import dataInLists.DataInOffers;


@SuppressLint("ResourceAsColor")
public class CustomListOffers extends RecyclerView.Adapter<CustomListOffers.ViewHolder> {
    private Activity Activity;
    private ArrayList<List<DataInOffers.OfferContent>> Data;
    private DisplayImageOptions options;

    public CustomListOffers(Activity context, ArrayList<List<DataInOffers.OfferContent>> data) {
        // TODO Auto-generated constructor stub
        this.Activity = context;
        this.Data = data;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.singel_offer_list, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.def_icon)
                .showImageForEmptyUri(R.mipmap.def_icon)
                .showImageOnFail(R.mipmap.def_icon)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        if (Data.get(position).get(0).size == 1) {
            ImageLoader.getInstance().displayImage(Values.Link_Image + Data.get(position).get(0).image, holder.FullAd, options);
            holder.FullAd.setVisibility(View.VISIBLE);
            holder.MarginAd.setVisibility(View.GONE);
            holder.SmallAd.setVisibility(View.GONE);

            holder.FullAd.setOnClickListener(v -> {
                if (Data.get(position).get(0).type == 1) {
                    Intent i = new Intent(Activity, Product.class);
                    i.putExtra("ID", Integer.parseInt(Data.get(position).get(0).target_id));
                    Activity.startActivity(i);
                } else if (Data.get(position).get(0).type == 2) {
                    Intent i = new Intent(Activity, Products.class);
                    i.putExtra("MainID", Integer.parseInt(Data.get(position).get(0).target_id));
                    Activity.startActivity(i);
                } else {
                    Intent browserIntent3 = new Intent(Intent.ACTION_VIEW, Uri.parse(
                            Data.get(position).get(0).target_id));
                    Activity.startActivity(browserIntent3);
                }

            });
        } else if (Data.get(position).get(0).size == 2) {
            Glide.with(holder.MarginAd.getContext()).load(Values.Link_Image + Data.get(position).get(0).image)
                            .into(holder.MarginAd);
            holder.FullAd.setVisibility(View.GONE);
            holder.MarginAd.setVisibility(View.VISIBLE);
            holder.SmallAd.setVisibility(View.GONE);

            holder.MarginAd.setOnClickListener(v -> {
                if (Data.get(position).get(0).type == 1) {
                    Intent i = new Intent(Activity, Product.class);
                    i.putExtra("ID", Data.get(position).get(0).target_id);
                    Activity.startActivity(i);
                } else if (Data.get(position).get(0).type == 2) {
                    Intent i = new Intent(Activity, Products.class);
                    i.putExtra("MainID", Integer.parseInt(Data.get(position).get(0).target_id));
                    Activity.startActivity(i);
                } else {
                    Intent browserIntent3 = new Intent(Intent.ACTION_VIEW, Uri.parse(
                            Data.get(position).get(0).target_id));
                    Activity.startActivity(browserIntent3);
                }
            });
        } else if (Data.get(position).get(0).size == 3) {
            holder.FullAd.setVisibility(View.GONE);
            holder.MarginAd.setVisibility(View.GONE);
            holder.SmallAd.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(Values.Link_Image + Data.get(position).get(0).image, holder.SmallAd_1, options);
            ImageLoader.getInstance().displayImage(Values.Link_Image + Data.get(position).get(1).image, holder.SmallAd_2, options);


            holder.SmallAd_1.setOnClickListener(v -> {
                if (Data.get(position).get(0).type == 1) {
                    Intent i = new Intent(Activity, Product.class);
                    i.putExtra("ID", Integer.parseInt(Data.get(position).get(0).target_id));
                    Activity.startActivity(i);
                } else if (Data.get(position).get(0).type == 2) {
                    Intent i = new Intent(Activity, Products.class);
                    i.putExtra("MainID", Integer.parseInt(Data.get(position).get(0).target_id));
                    Activity.startActivity(i);
                } else {
                    Intent browserIntent3 = new Intent(Intent.ACTION_VIEW, Uri.parse(
                            Data.get(position).get(0).target_id));
                    Activity.startActivity(browserIntent3);
                }
            });
            holder.SmallAd_2.setOnClickListener(v -> {
                if (Data.get(position).get(1).id > 0) {
                    if (Data.get(position).get(1).type == 1) {
                        Intent i = new Intent(Activity, Product.class);
                        i.putExtra("ID", Integer.parseInt(Data.get(position).get(1).target_id));
                        Activity.startActivity(i);
                    } else if (Data.get(position).get(1).type == 2) {
                        Intent i = new Intent(Activity, Products.class);
                        i.putExtra("MainID", Integer.parseInt(Data.get(position).get(1).target_id));
                        Activity.startActivity(i);
                    } else {
                        Intent browserIntent3 = new Intent(Intent.ACTION_VIEW, Uri.parse(
                                Data.get(position).get(1).target_id));
                        Activity.startActivity(browserIntent3);
                    }
                } else {

                }

            });
        }


    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView FullAd, MarginAd, SmallAd_1, SmallAd_2;
        RelativeLayout List;
        LinearLayout SmallAd;

        public ViewHolder(View rowView) {
            super(rowView);

            // get the reference of item view's
            FullAd = rowView.findViewById(R.id.FullAd);
            MarginAd = rowView.findViewById(R.id.MarginAd);
            SmallAd_1 = rowView.findViewById(R.id.SmallAd_1);
            SmallAd_2 = rowView.findViewById(R.id.SmallAd_2);
            List = rowView.findViewById(R.id.List);
            SmallAd = rowView.findViewById(R.id.SmallAd);
        }
    }

}
