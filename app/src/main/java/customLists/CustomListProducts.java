package customLists;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.usmart.com.rummanah.Home;
import com.usmart.com.rummanah.Login;
import com.usmart.com.rummanah.Product;
import com.usmart.com.rummanah.Products;
import com.usmart.com.rummanah.R;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import constants.Values;
import dataInLists.DataInGlobal;
import dataInLists.DataInProducts;
import helpers.LangHolder;
import helpers.LoginHolder;
import helpers.UserTokenHolder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@SuppressLint("ResourceAsColor")
public class CustomListProducts extends ArrayAdapter<DataInProducts.ProductDetails> {
    private Activity Activity;
    private ArrayList<DataInProducts.ProductDetails> Data;
    private DisplayImageOptions options;
    private Typeface fontMedim, fontLight, fontUltra, fontAvenir;
    private int counter = 1;

    public CustomListProducts(Activity context, ArrayList<DataInProducts.ProductDetails> data) {
        super(context, R.layout.singel_product_list2, data);
        // TODO Auto-generated constructor stub
        this.Activity = context;
        this.Data = data;

    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = Activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.singel_product_list2, null, true);

        TextView txtTitle = rowView.findViewById(R.id.tv_Title);
        TextView txtPrice = rowView.findViewById(R.id.tv_MainPrice);
        TextView txtDiscount = rowView.findViewById(R.id.tv_BeforeDiscount);
        TextView txtDiscountPersent = rowView.findViewById(R.id.tv_discount);
        ImageView imageView = rowView.findViewById(R.id.iv_Feeds);
        ImageView img_Fav = rowView.findViewById(R.id.img_Fav);
        ImageView img_Cart = rowView.findViewById(R.id.img_Cart);
        TextView tv_Cat = rowView.findViewById(R.id.tv_Cat);
        TextView tv_addToCart = rowView.findViewById(R.id.tv_addToCart);
        FrameLayout List = rowView.findViewById(R.id.List);
        ImageView iv_minus = rowView.findViewById(R.id.iv_minus);
        TextView tv_count = rowView.findViewById(R.id.tv_count);
        ImageView iv_plus = rowView.findViewById(R.id.iv_plus);
        TextView tv_rate = rowView.findViewById(R.id.tv_rate);


        fontMedim = Typeface.createFromAsset(Activity.getAssets(), "fonts/GE_SS_Two_Medium.otf");
        fontLight = Typeface.createFromAsset(Activity.getAssets(), "fonts/GE_SS_Two_Light.otf");
        fontUltra = Typeface.createFromAsset(Activity.getAssets(), "fonts/ArbFONTS-GESSTextUltraLight-UltraLight.otf");
        fontAvenir = Typeface.createFromAsset(Activity.getAssets(), "fonts/avenir-lt-std-55-roman.otf");


        txtTitle.setTypeface(fontLight);
        txtPrice.setTypeface(fontAvenir);
        txtDiscount.setTypeface(fontAvenir);
        tv_Cat.setTypeface(fontLight);
        txtDiscountPersent.setTypeface(fontAvenir);

        txtPrice.setText(String.format("%.3f",Data.get(position).final_price) + " " + Activity.getResources().getString(R.string.DK));
        txtDiscount.setText(String.format("%.3f",Data.get(position).price_before_offer) + " " + Activity.getResources().getString(R.string.DK));
        tv_Cat.setText(Data.get(position).category_name + " ");
        tv_rate.setText(String.valueOf(Data.get(position).rate));

        if (Data.get(position).offer == 0) {
            //   txtDiscount.setVisibility(View.GONE);
            txtPrice.setText(String.format("%.3f",Data.get(position).final_price) + " " + Activity.getResources().getString(R.string.DK));
            txtDiscount.setText("");
            txtDiscountPersent.setVisibility(View.GONE);

        } else {
            txtDiscount.setPaintFlags(txtPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            txtDiscount.setVisibility(View.VISIBLE);
            txtPrice.setText(String.format("%.3f",Data.get(position).final_price) + " " + Activity.getResources().getString(R.string.DK));
            txtDiscount.setText(Data.get(position).price_before_offer + " " + Activity.getResources().getString(R.string.DK));
            txtDiscountPersent.setVisibility(View.VISIBLE);

            float d = Data.get(position).offer_percentage;
            if ((d - (int) d) != 0)
                txtDiscountPersent.setText(" - " + Data.get(position).offer_percentage + " %");
            else
                txtDiscountPersent.setText(" - " + (int) Math.round(Data.get(position).offer_percentage) + " %");
        }

        int x = Data.get(position).title.length();
        if (x >= 70) {
            txtTitle.setText(Html.fromHtml(Data.get(position).title.substring(0, 70)) + " ...");
        } else {
            txtTitle.setText(Html.fromHtml(Data.get(position).title));
        }

        if (Data.get(position).favorite == true) {
            img_Fav.setImageResource(R.mipmap.like);
        } else {
            img_Fav.setImageResource(R.mipmap.non_like);
        }
        img_Fav.setOnClickListener(view -> {
            if (Data.get(position).favorite) {
                RemoveFave(Data.get(position).id, img_Fav);
            } else {
                AddFave(Data.get(position).id, img_Fav);
            }
        });

        img_Cart.setOnClickListener(view -> {
            AddCart(Data.get(position).id);
            //((Products)activity).CartCount();
        });

        tv_addToCart.setOnClickListener(view -> {
            AddCart(Data.get(position).id);
            //((Products)activity).CartCount();
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
        ImageLoader.getInstance().displayImage(Values.Link_Image + Data.get(position).image, imageView, options);

        imageView.setOnClickListener(v -> {
            Intent i = new Intent(Activity, Product.class);
            i.putExtra("ID", Data.get(position).id);
            Activity.startActivity(i);
            //    Activity.overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);

        });

        iv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter = counter + 1;
                tv_count.setText(String.valueOf(counter));
            }
        });

        iv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter > 1) {
                    counter = counter - 1;
                    tv_count.setText(String.valueOf(counter));
                }
            }
        });


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

                Data = (ArrayList<DataInProducts.ProductDetails>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<DataInProducts.ProductDetails> filteredResults = getFilteredResults(constraint);

                FilterResults results = new FilterResults();
                results.values = filteredResults;
                // results.count = filteredResults.size();

                return results;
            }
        };
    }

    @SuppressLint("DefaultLocale")
    private ArrayList<DataInProducts.ProductDetails> getFilteredResults(CharSequence constraint) {

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


    private void AddFave(int ID, ImageView LikeIc) {
        if (LoginHolder.getInstance().getData().equals("logout")) {
            // ProductHolder.getInstance().setData(Integer.toString(ID));
            //  Activity.startActivity(new Intent(Activity, Login.class));
            loadMsgLogin(Activity.getResources().getString(R.string.LoginFirst));
            return;
        }
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
                            LikeIc.setImageResource(R.mipmap.like);
                        }
                    });

                } catch (Exception e) {
                }
            }

        });

    }

    private void RemoveFave(int ID, ImageView LikeIc) {
        if (LoginHolder.getInstance().getData().equals("logout")) {
            // ProductHolder.getInstance().setData(Integer.toString(ID));
            //  Activity.startActivity(new Intent(Activity, Login.class));
            loadMsgLogin(Activity.getResources().getString(R.string.LoginFirst));
            return;
        }
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
                                loadMsg(Result.message);
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
                        }
                    });

                } catch (Exception e) {
                }
            }

        });

    }

    private void loadMsgLogin(String MSG) {
        final Dialog dialog = new Dialog(Activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.msg_noads3);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView Title = dialog.findViewById(R.id.MsgTitle);
        TextView Text = dialog.findViewById(R.id.MsgText);

        Button Yes = dialog.findViewById(R.id.btn_Yes);
        Button No = dialog.findViewById(R.id.btn_No);

        Yes.setText(R.string.OK);
        No.setText(R.string.Login);
        Text.setText(MSG + "");
        // No.setVisibility(View.GONE);


        Yes.setTypeface(fontMedim);
        No.setTypeface(fontMedim);
        Title.setTypeface(fontMedim);
        Text.setTypeface(fontUltra);

        Yes.setOnClickListener(v -> {
            dialog.dismiss();
        });
        No.setOnClickListener(v -> {
            dialog.dismiss();

            Intent i = new Intent(Activity, Login.class);
            Activity.startActivity(i);

        });
        dialog.show();
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

}
