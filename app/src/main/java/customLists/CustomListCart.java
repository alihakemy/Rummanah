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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.usmart.com.rummanah.Login;
import com.usmart.com.rummanah.R;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import constants.Values;
import dataInLists.DataInCart;
import dataInLists.DataInGlobal;
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
public class CustomListCart extends ArrayAdapter<DataInCart.Carts> {
    private Activity Activity;
    private ArrayList<DataInCart.Carts> Data;
    private DisplayImageOptions options;
    private Typeface fontMedim, fontLight, fontUltra, fontAvenir, fontPoppinsMed;

    public CustomListCart(Activity context, ArrayList<DataInCart.Carts> data) {
        super(context, R.layout.singel_cart_list, data);
        // TODO Auto-generated constructor stub
        this.Activity = context;
        this.Data = data;

    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = Activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.singel_cart_list, null, true);
        TextView txtTitle = rowView.findViewById(R.id.tv_Title);
        TextView txtMainPrice = rowView.findViewById(R.id.tv_MainPrice);
        TextView txtBeforePrice = rowView.findViewById(R.id.tv_BeforeDiscount);
        TextView txtAmount = rowView.findViewById(R.id.tv_amount);
        TextView txtDelFromCart = rowView.findViewById(R.id.tv_DelFromCart);
        TextView txtFav = rowView.findViewById(R.id.tv_Fav);
        ImageView Add = rowView.findViewById(R.id.iv_Add);
        ImageView Remove = rowView.findViewById(R.id.iv_Remove);
        ImageView imageView = rowView.findViewById(R.id.iv_Feeds);

        fontMedim = Typeface.createFromAsset(Activity.getAssets(), "fonts/GE_SS_Two_Medium.otf");
        fontLight = Typeface.createFromAsset(Activity.getAssets(), "fonts/GE_SS_Two_Light.otf");
        fontUltra = Typeface.createFromAsset(Activity.getAssets(), "fonts/ArbFONTS-GESSTextUltraLight-UltraLight.otf");
        fontPoppinsMed = Typeface.createFromAsset(Activity.getAssets(), "fonts/Poppins-Medium.ttf");
        fontAvenir = Typeface.createFromAsset(Activity.getAssets(), "fonts/avenir-lt-std-55-roman.otf");


        txtTitle.setTypeface(fontMedim);
        txtMainPrice.setTypeface(fontPoppinsMed);
        txtBeforePrice.setTypeface(fontPoppinsMed);
        txtAmount.setTypeface(fontPoppinsMed);
        txtDelFromCart.setTypeface(fontLight);
        txtFav.setTypeface(fontLight);

        txtTitle.setText(Data.get(position).title);
        txtAmount.setText(Data.get(position).count + "");

        if (Data.get(position).price_before_offer == 0) {
            txtMainPrice.setText(String.format("%.3f", Data.get(position).final_price) + " " + Activity.getResources().getString(R.string.DK));
            txtBeforePrice.setText("");
            txtBeforePrice.setVisibility(View.GONE);

        } else {
            txtBeforePrice.setPaintFlags(txtMainPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            txtBeforePrice.setVisibility(View.VISIBLE);
            txtMainPrice.setText(String.format("%.3f",Data.get(position).final_price) + " " + Activity.getResources().getString(R.string.DK));
            txtBeforePrice.setText(String.format("%.3f",Data.get(position).price_before_offer) + " " + Activity.getResources().getString(R.string.DK));
            txtBeforePrice.setVisibility(View.VISIBLE);
        }

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
        Glide.with(imageView.getContext())
                        .load(Values.Link_Image + Data.get(position).image)
                                .into(imageView);

        txtDelFromCart.setOnClickListener(v -> {
                    loadWantDelete(Data.get(position).id);
                }
        );

        Add.setOnClickListener(v ->
        {
            UpdateCartNumber(Data.get(position).id,
                    (Integer.parseInt(txtAmount.getText().toString()) + 1));
        });

        if (Data.get(position).count > 1) {
            Remove.setOnClickListener(v -> UpdateCartNumber(Data.get(position).id,
                    (Integer.parseInt(txtAmount.getText().toString()) - 1)));
        }

        if (Data.get(position).favorite == true) {
            txtFav.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.like, 0);
        } else {
            txtFav.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.non_like, 0);
        }

        txtFav.setOnClickListener(view -> {
            if (Data.get(position).favorite) {
                RemoveFave(Data.get(position).id, txtFav);
            } else {
                AddFave(Data.get(position).id, txtFav);
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

                Data = (ArrayList<DataInCart.Carts>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<DataInCart.Carts> filteredResults = getFilteredResults(constraint);

                FilterResults results = new FilterResults();
                results.values = filteredResults;
                // results.count = filteredResults.size();

                return results;
            }
        };
    }

    @SuppressLint("DefaultLocale")
    private ArrayList<DataInCart.Carts> getFilteredResults(CharSequence constraint) {

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

    private void UpdateCartNumber(int ID, int newCount) {

        OkHttpClient client = new OkHttpClient();
        String Url = Values.Link_service + "visitors/cart/changecount/" + LangHolder.getInstance().getData() + "/v1";
        String json = new StringBuilder()
                .append("{")
                .append("\"unique_id\":\"" + Settings.Secure.getString(Activity.getContentResolver(),
                        Settings.Secure.ANDROID_ID) + "\",")
                .append("\"new_count\":\"" + newCount + "\",")
                .append("\"product_id\":\"" + ID + "\"")
                .append("}").toString();

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                json
        );
        Request request = new Request.Builder()
                .url(Url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "" + Values.Authorization_User)
                .put(body)
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
                    //   Log.i("TestApp", response.body().string());

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
                            // loadMsg(Activity.getResources().getString(R.string.AddedToCart));
                            Activity.finish();
                            Activity.overridePendingTransition(0, 0);
                            Activity.startActivity(Activity.getIntent());
                            Activity.overridePendingTransition(0, 0);
                        }
                    });

                } catch (Exception e) {
                }
            }

        });

    }

    private void DeleteCart(int ID) {

        OkHttpClient client = new OkHttpClient();
        String Url = Values.Link_service + "visitors/cart/delete/" + LangHolder.getInstance().getData() + "/v1";
        String json = new StringBuilder()
                .append("{")
                .append("\"unique_id\":\"" + Settings.Secure.getString(Activity.getContentResolver(),
                        Settings.Secure.ANDROID_ID) + "\",")
                .append("\"product_id\":\"" + ID + "\"")
                .append("}").toString();

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                json
        );
        Request request = new Request.Builder()
                .url(Url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "" + Values.Authorization_User)
                .delete(body)
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
                    //Log.i("TestApp", response.body().string());


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
                            // loadMsg(Activity.getResources().getString(R.string.AddedToCart));
                            Activity.finish();
                            Activity.overridePendingTransition(0, 0);
                            Activity.startActivity(Activity.getIntent());
                            Activity.overridePendingTransition(0, 0);
                        }
                    });

                } catch (Exception e) {
                }
            }

        });

    }

    private void AddFave(int ID, TextView LikeIc) {
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
                                loadMsg(Result.message);
                            }
                        } else {
                            LikeIc.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.like, 0);
                        }
                    });

                } catch (Exception e) {
                }
            }

        });

    }

    private void RemoveFave(int ID, TextView LikeIc) {
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
                .addHeader("Authorization", UserTokenHolder.getInstance().getData().access_token)
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
                            LikeIc.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.non_like, 0);
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

    private void loadWantDelete(int ID) {
        final Dialog dialog = new Dialog(Activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.msg_noads3);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView Title = dialog.findViewById(R.id.MsgTitle);
        TextView Text = dialog.findViewById(R.id.MsgText);

        Button Yes = dialog.findViewById(R.id.btn_Yes);
        Button No = dialog.findViewById(R.id.btn_No);

        Yes.setText(R.string.Yes);
        Text.setText(R.string.WantDelete);
        //   No.setVisibility(View.GONE);


        Yes.setTypeface(fontMedim);
        No.setTypeface(fontMedim);
        Title.setTypeface(fontMedim);
        Text.setTypeface(fontUltra);

        Yes.setOnClickListener(v -> {
            DeleteCart(ID);
            dialog.dismiss();
            Activity.finish();
            Activity.overridePendingTransition(0, 0);
            Activity.startActivity(Activity.getIntent());
            Activity.overridePendingTransition(0, 0);
        });
        No.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

}
