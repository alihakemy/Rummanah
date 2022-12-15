package customLists;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.usmart.com.rummanah.Login;
import com.usmart.com.rummanah.Product;
import com.usmart.com.rummanah.R;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import constants.Values;
import dataInLists.DataInGlobal;
import dataInLists.DataInProduct;
import helpers.LangHolder;
import helpers.UserTokenHolder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CustomListRelated extends RecyclerView.Adapter<CustomListRelated.ViewHolder> {
    private Activity Activity;
    private ArrayList<DataInProduct.Related> Data;
    private DisplayImageOptions options;
    private Typeface fontMedim, fontLight, fontUltra, fontAvenir;
    private int counter = 1;

    public CustomListRelated(Activity context, ArrayList<DataInProduct.Related> data) {
        // TODO Auto-generated constructor stub
        this.Activity = context;
        this.Data = data;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.singel_product_list, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {


        holder.txtPrice.setText(String.format("%.3f",Data.get(position).final_price )+ " " + Activity.getResources().getString(R.string.DK));
        holder.txtDiscount.setText(String.format("%.3f",Data.get(position).price_before_offer )+ " " + Activity.getResources().getString(R.string.DK));
        holder.tv_Cat.setText(Data.get(position).category_name + " ");
        holder.tv_rate.setText("(" + Data.get(position).rate + ")");

        if (Data.get(position).offer == 0) {
            //   txtDiscount.setVisibility(View.GONE);
            holder.txtPrice.setText(String.format("%.3f",Data.get(position).final_price )+ " " + Activity.getResources().getString(R.string.DK));
            holder.txtDiscount.setText("");
            holder.txtDiscountPersent.setVisibility(View.GONE);

        } else {
            holder.txtDiscount.setPaintFlags(holder.txtPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.txtDiscount.setVisibility(View.VISIBLE);
            holder.txtPrice.setText(String.format("%.3f",Data.get(position).final_price )+ " " + Activity.getResources().getString(R.string.DK));
            holder.txtDiscountPersent.setVisibility(View.VISIBLE);

            float d = Data.get(position).offer_percentage;
            if ((d - (int) d) != 0)
                holder.txtDiscountPersent.setText(Data.get(position).offer_percentage + " %");
            else
                holder.txtDiscountPersent.setText((int) Math.round(Data.get(position).offer_percentage) + " %");
        }

     /*   int x = Data.get(position).title.length();
        if (x >= 70) {
            holder.txtTitle.setText(Html.fromHtml(Data.get(position).title.substring(0, 70)) + " ...");
        } else {*/
        holder.txtTitle.setText(Html.fromHtml(Data.get(position).title));
        // }

        if (Data.get(position).favorite == true) {
            holder.img_Fav.setImageResource(R.mipmap.like);
        } else {
            holder.img_Fav.setImageResource(R.mipmap.non_like);
        }
        holder.img_Fav.setOnClickListener(view -> {
            if (Data.get(position).favorite) {
                RemoveFave(Data.get(position).id, holder.img_Fav);
            } else {
                AddFave(Data.get(position).id, holder.img_Fav);
            }
        });

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.def)
                .showImageForEmptyUri(R.mipmap.def)
                .showImageOnFail(R.mipmap.def)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(Values.Link_Image + Data.get(position).image, holder.imageView, options);

        holder.imageView.setOnClickListener(v -> {
            Intent i = new Intent(Activity, Product.class);
            i.putExtra("ID", Data.get(position).id);
            Activity.startActivity(i);
            //    Activity.overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);

        });

        holder.iv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter > 1) {
                    counter = counter - 1;
                    holder.tv_count.setText(String.valueOf(counter));
                }
            }
        });

        holder.iv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter = counter + 1;
                holder.tv_count.setText(String.valueOf(counter));
            }
        });

        holder.tv_addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCart(Data.get(position).id);
            }
        });

    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtPrice, txtDiscount, txtDiscountPersent, tv_Cat, tv_count, tv_rate, tv_addToCart;
        ImageView imageView, img_Fav, img_Cart, iv_minus, iv_plus;
        FrameLayout List;

        public ViewHolder(View rowView) {
            super(rowView);

            // get the reference of item view's
            txtTitle = rowView.findViewById(R.id.tv_Title);
            txtPrice = rowView.findViewById(R.id.tv_MainPrice);
            txtDiscount = rowView.findViewById(R.id.tv_BeforeDiscount);
            txtDiscountPersent = rowView.findViewById(R.id.tv_discount);
            imageView = rowView.findViewById(R.id.iv_Feeds);
            img_Fav = rowView.findViewById(R.id.img_Fav);
            img_Cart = rowView.findViewById(R.id.img_Cart);
            tv_Cat = rowView.findViewById(R.id.tv_Cat);
            iv_minus = rowView.findViewById(R.id.iv_minus);
            tv_count = rowView.findViewById(R.id.tv_count);
            List = rowView.findViewById(R.id.List);
            iv_plus = rowView.findViewById(R.id.iv_plus);
            tv_rate = rowView.findViewById(R.id.tv_rate);
            tv_addToCart = rowView.findViewById(R.id.tv_addToCart);

            fontMedim = Typeface.createFromAsset(Activity.getAssets(), "fonts/GE_SS_Two_Medium.otf");
            fontLight = Typeface.createFromAsset(Activity.getAssets(), "fonts/GE_SS_Two_Light.otf");
            fontUltra = Typeface.createFromAsset(Activity.getAssets(), "fonts/ArbFONTS-GESSTextUltraLight-UltraLight.otf");
            fontAvenir = Typeface.createFromAsset(Activity.getAssets(), "fonts/avenir-lt-std-55-roman.otf");
            txtTitle.setTypeface(fontLight);
            txtPrice.setTypeface(fontAvenir);
            txtDiscount.setTypeface(fontAvenir);
            tv_Cat.setTypeface(fontLight);
            //  img_Cart.setTypeface(fontLight);
            txtDiscountPersent.setTypeface(fontAvenir);
        }
    }

    private void AddFave(int ID, ImageView LikeIc) {
        OkHttpClient client = new OkHttpClient();
        String Url = "https://rummanah.com/api/favorites/en/v1";
        String json = new StringBuilder()
                .append("{")
                .append("\"product_id\":\"" + ID + "\"")
                .append("}").toString();

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                json
        );
        Request request = new Request.Builder()
                .url(Url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", UserTokenHolder.getInstance().getData().access_token)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("TestApp_5", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    DataInGlobal Result;
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInGlobal>() {
                    }.getType();

                    Result = g.fromJson(response.body().string(), t);


                    Activity.runOnUiThread(() -> {
                        if (!Result.success) {
                            if (Result.code == 401) {
                                Intent i = new Intent(Activity, Login.class);
                                Activity.startActivity(i);
                            } else {
                                loadMsg(Result.message_ar);
                            }
                        } else {
                            LikeIc.setImageResource(R.mipmap.like);
                        }
                    });

                } catch (Exception e) {
                }
            }

        });

    }

    private void RemoveFave(int ID, ImageView LikeIc) {
        OkHttpClient client = new OkHttpClient();
        String Url = "https://rummanah.com/api/favorites/en/v1";
        String json = new StringBuilder()
                .append("{")
                .append("\"product_id\":\"" + ID + "\"")
                .append("}").toString();

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                json
        );
        Request request = new Request.Builder()
                .url(Url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization",UserTokenHolder.getInstance().getData().access_token)
                .delete(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("TestApp_5", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    DataInGlobal Result;
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInGlobal>() {
                    }.getType();

                    Result = g.fromJson(response.body().string(), t);


                    Activity.runOnUiThread(() -> {
                        if (!Result.success) {
                            if (Result.code == 401) {
                                Intent i = new Intent(Activity, Login.class);
                                Activity.startActivity(i);
                            } else {
                                loadMsg(Result.message_ar);
                            }
                        } else {
                            LikeIc.setImageResource(R.mipmap.non_like);
                        }
                    });

                } catch (Exception e) {
                }
            }

        });

    }

    private void loadMsg(String MSG) {
        final Dialog dialog = new Dialog(Activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.msg_noads3);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView Title = dialog.findViewById(R.id.MsgTitle);
        TextView Text = dialog.findViewById(R.id.MsgText);

        Button Yes = dialog.findViewById(R.id.btn_Yes);
        Button No = dialog.findViewById(R.id.btn_No);

        Yes.setText(R.string.OK);
        Text.setText(MSG + "");
        No.setVisibility(View.GONE);


        Yes.setTypeface(fontMedim);
        Title.setTypeface(fontMedim);
        Text.setTypeface(fontUltra);

        Yes.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    private void AddCart(int ID) {
        OkHttpClient client = new OkHttpClient();
        String Url = Values.Link_service + "visitors/cart/add/" + LangHolder.getInstance().getData() + "/v1";
        String json = new StringBuilder()
                .append("{")
                .append("\"unique_id\":\"" + Settings.Secure.getString(Activity.getContentResolver(),
                        Settings.Secure.ANDROID_ID) + "\",")
                .append("\"product_id\":" + ID + ",")
                .append("\"count\":" + counter)
                .append("}").toString();

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                json
        );
        Request request = new Request.Builder()
                .url(Url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "" + Values.Authorization_User)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("TestApp_5", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    DataInGlobal Result;
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInGlobal>() {
                    }.getType();

                    Result = g.fromJson(response.body().string(), t);


                    Activity.runOnUiThread(() -> {
                        if (!Result.success) {
                            if (Result.code == 401) {
                                Intent i = new Intent(Activity, Login.class);
                                Activity.startActivity(i);
                            } else {
                                loadMsg(Result.message);
                            }
                        } else {
                            //CartNum.setText(Result.data.count + "");
                            loadMsg(Activity.getResources().getString(R.string.AddedToCart));
                            ((Product) Activity).CartCount();
                        }
                    });

                } catch (Exception e) {
                }
            }

        });

    }

}
