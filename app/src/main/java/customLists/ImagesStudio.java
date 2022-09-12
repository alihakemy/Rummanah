package customLists;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.usmart.com.rummanah.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import dataInLists.DataInGallery;

@SuppressLint("ResourceAsColor")
public class ImagesStudio extends ArrayAdapter<DataInGallery> {
	private Activity Activity;
	private ArrayList<DataInGallery> Data;
	private DisplayImageOptions options;

	public ImagesStudio(Activity context, ArrayList<DataInGallery> data) {
		super(context, R.layout.item_multi_images, data);
		// TODO Auto-generated constructor stub
		this.Activity = context;
		this.Data = data;
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.mipmap.def2)
				.showImageForEmptyUri(R.mipmap.def2)
				.showImageOnFail(R.mipmap.def2)
				.cacheInMemory(false)
				.resetViewBeforeLoading(true)
				.cacheOnDisk(false)
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();

	}

	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = Activity.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.item_multi_images, null, true);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.image);
		ImageView select = (ImageView) rowView.findViewById(R.id.select);

		ImageLoader.getInstance().displayImage("file://"+Data.get(position).thumb, imageView, options);

		if (Data.get(position).t == 1) {
			imageView.setAlpha(0.7f);
			select.setVisibility(View.VISIBLE);
		} else {
			imageView.setAlpha(1.0f);
			select.setVisibility(View.GONE);
		}

		return rowView;
	}

	public Uri getImageUri(Context inContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);

		return Uri.parse(path);
	}

	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = {MediaStore.Images.Media.DATA};
		Cursor cursor = Activity.managedQuery(contentUri, proj, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}


}
