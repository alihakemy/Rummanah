package Fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.usmart.com.rummanah.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;


@SuppressLint("ValidFragment")
public class PGallery extends Fragment {

	private String Obj ;
	private ImageView pGallery;
	public PGallery(String id) {
		this.Obj = id;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
       
		View PG = inflater.inflate(R.layout.activity_photo, container, false);
		pGallery = (ImageView) PG.findViewById(R.id.image);
	//	Picasso.with(getActivity()).load(Obj).into(pGallery);

		DisplayImageOptions options;
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.def)
				.showImageForEmptyUri(R.mipmap.def).showImageOnFail(R.mipmap.def)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();
		ImageLoader.getInstance().displayImage(Obj, pGallery, options);

	//	Download.setOnClickListener(v -> new SaveWallpaperAsync(activity).execute(Photo));


		return PG;
	}
}
