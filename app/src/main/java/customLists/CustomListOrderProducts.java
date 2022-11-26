package customLists;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.usmart.com.rummanah.Checkout;
import com.usmart.com.rummanah.Home;
import com.usmart.com.rummanah.Login;
import com.usmart.com.rummanah.OrderDetails;
import com.usmart.com.rummanah.R;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

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

@SuppressLint("ResourceAsColor")
public class CustomListOrderProducts extends ArrayAdapter<DataInProduct.Product> {
    private Activity Activity;
    private static final String TAG = CustomListOrderProducts.class.getSimpleName();
    private ArrayList<DataInProduct.Product> Data;
    private DisplayImageOptions options;
    String lang = Values.SharedPreferences_FileNameLangSelect;

    public CustomListOrderProducts(Activity context, ArrayList<DataInProduct.Product> data) {
        super(context, R.layout.singel_order_product_list, data);
        // TODO Auto-generated constructor stub
        this.Activity = context;
        this.Data = data;

    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Locale locale = new Locale("en", "UK");

        LayoutInflater inflater = Activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.singel_order_product_list, null, true);
        TextView tv_Title = rowView.findViewById(R.id.tv_Title);
        TextView tv_CountTxt = rowView.findViewById(R.id.tv_CountTxt);
        TextView tv_Count = rowView.findViewById(R.id.tv_Count);
        TextView tv_Price = rowView.findViewById(R.id.tv_Price);
        ImageView imageView = rowView.findViewById(R.id.iv_Feeds);
        ImageView iv_rate = rowView.findViewById(R.id.iv_rate);


        Typeface fontMedim = Typeface.createFromAsset(Activity.getAssets(), "fonts/GE_SS_Two_Medium.otf");
        Typeface fontLight = Typeface.createFromAsset(Activity.getAssets(), "fonts/GE_SS_Two_Light.otf");
        Typeface fontUltra = Typeface.createFromAsset(Activity.getAssets(), "fonts/ArbFONTS-GESSTextUltraLight-UltraLight.otf");
        Typeface fontCeraPro = Typeface.createFromAsset(Activity.getAssets(), "fonts/cera_pro_med.ttf");


        //tv_Title.setTypeface(fontMedim);
        tv_CountTxt.setTypeface(fontLight);
        //  tv_Count.setTypeface(fontCeraPro);
        //  tv_Price.setTypeface(fontCeraPro);


        tv_Title.setText(Data.get(position).title + "");
        tv_Count.setText(Data.get(position).count + "");
        tv_Price.setText(String.format("%.3f",Data.get(position).final_price) + " " + Activity.getResources().getString(R.string.DK));

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.def_icon)
                .showImageForEmptyUri(R.mipmap.def_icon)
                .showImageOnFail(R.mipmap.def_icon)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(5))
                .build();
        ImageLoader.getInstance().displayImage(Values.Link_Image + Data.get(position).image, imageView, options);
        String ImageUrl = Values.Link_Image + Data.get(position).image;

        iv_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddProductRate(ImageUrl, Data.get(position).title, Data.get(position).id, Data.get(position).order_id);
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

                Data = (ArrayList<DataInProduct.Product>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<DataInProduct.Product> filteredResults = getFilteredResults(constraint);

                FilterResults results = new FilterResults();
                results.values = filteredResults;
                // results.count = filteredResults.size();

                return results;
            }
        };
    }

    @SuppressLint("DefaultLocale")
    private ArrayList<DataInProduct.Product> getFilteredResults(CharSequence constraint) {

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

    private void AddProductRate(String img, String title, int productId, int orderId) {
        final Dialog dialog = new Dialog(Activity);
        // Include dialog.xml file
        dialog.setContentView(R.layout.select_rate);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //set values for custom dialog components
        final RatingBar rb_user_rating = dialog.findViewById(R.id.rb_user_rating);
        final EditText et_user_notes = dialog.findViewById(R.id.et_user_notes);
        TextView tv_add = dialog.findViewById(R.id.tv_add);
        TextView tv_title = dialog.findViewById(R.id.tv_title);
        ImageView iv_product = dialog.findViewById(R.id.iv_product);


        //showing dialog
        dialog.show();

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.def_icon)
                .showImageForEmptyUri(R.mipmap.def_icon)
                .showImageOnFail(R.mipmap.def_icon)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(5))
                .build();
        ImageLoader.getInstance().displayImage(img, iv_product, options);

        tv_title.setText(title);

        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rb_user_rating.getRating() == 0.0) {
                    Toast.makeText(Activity, Activity.getResources().getString(R.string.select_stars), Toast.LENGTH_LONG).show();
                } else if (et_user_notes.getText().length() == 0) {
                    Toast.makeText(Activity, Activity.getResources().getString(R.string.addComment), Toast.LENGTH_LONG).show();
                } else {
                    //PostProductRate(rb_user_rating.getRating(), et_user_notes.getText().toString(), orderId, productId);
                    OkHttpClient client = new OkHttpClient();
                    String Url = Values.Link_service + "rate/" + lang + "/v1";
                    String json = new StringBuilder()
                            .append("{")
                            .append("\"rate\":" + rb_user_rating.getRating() + ",")
                            .append("\"text\":\"" + et_user_notes.getText().toString() + "\",")
                            .append("\"order_id\":" + orderId + ",")
                            .append("\"product_id\":" + productId)
                            .append("}").toString();
                    Log.d(TAG, json);

                    RequestBody body = RequestBody.create(
                            MediaType.parse("application/json; charset=utf-8"),
                            json
                    );
                    Request request = new Request.Builder()
                            .url(Url)
                            .addHeader("Content-Type", "application/json")
                            //.addHeader("Authorization", "" + Values.Authorization_User)
                            .addHeader("Authorization",  UserTokenHolder.getInstance().getData().access_token)
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
                                        if (Result.code == 400) {
                                            Toast.makeText(Activity, Result.message, Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(Activity, Result.message, Toast.LENGTH_LONG).show();
                                        }
                                        dialog.dismiss();
                                    } else {
                                        dialog.dismiss();
                                    }
                                });

                            } catch (Exception e) {
                            }
                        }
                    });
                }
            }
        });
    }

    private void PostProductRate(float rate, String text, int OID, int PID) {
        OkHttpClient client = new OkHttpClient();
        String Url = Values.Link_service + "rate/" + lang + "/v1";
        String json = new StringBuilder()
                .append("{")
                .append("\"rate\":" + rate + ",")
                .append("\"text\":\"" + text + "\",")
                .append("\"order_id\":" + OID + ",")
                .append("\"product_id\":" + PID)
                .append("}").toString();
        Log.d(TAG, json);

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                json
        );
        Request request = new Request.Builder()
                .url(Url)
                .addHeader("Content-Type", "application/json")
                //.addHeader("Authorization", "" + Values.Authorization_User)
                .addHeader("Authorization",  UserTokenHolder.getInstance().getData().access_token)
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
                                /*Intent i = new Intent(Activity, Login.class);
                                Activity.startActivity(i);*/
                            } else {
                                //loadMsg(Result.message);
                            }
                        } else {
                            //CartNum.setText(Result.data.count + "");
                            //loadMsg(Activity.getResources().getString(R.string.AddedToCart));
                        }
                    });

                } catch (Exception e) {
                }
            }
        });
    }


}
