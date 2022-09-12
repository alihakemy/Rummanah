package customLists;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.usmart.com.rummanah.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;

import dataInLists.DataInGallery;

@SuppressLint("ResourceAsColor")
public class VideosStudio extends ArrayAdapter<DataInGallery> {
	private Activity Activity;
	private ArrayList<DataInGallery> Data;
	private DisplayImageOptions options;

	public VideosStudio(Activity context, ArrayList<DataInGallery> data) {
		super(context, R.layout.item_multi_videos, data);
		// TODO Auto-generated constructor stub
		this.Activity = context;
		this.Data = data;
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.mipmap.def2)
				.showImageForEmptyUri(R.mipmap.def2)
				.showImageOnFail(R.mipmap.def2)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new RoundedBitmapDisplayer(10)).build();

	}

	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = Activity.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.item_multi_videos, null, true);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.image);
		ImageView select = (ImageView) rowView.findViewById(R.id.select);

		ImageLoader.getInstance().displayImage("file://" + Data.get(position).link, imageView, options);

		if (Data.get(position).t == 1) {
			imageView.setAlpha(0.7f);
			select.setVisibility(View.VISIBLE);
		} else {
			imageView.setAlpha(1.0f);
			select.setVisibility(View.GONE);
		}

		return rowView;
	}

}
