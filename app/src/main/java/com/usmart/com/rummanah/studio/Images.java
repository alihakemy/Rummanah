package com.usmart.com.rummanah.studio;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.usmart.com.rummanah.R;

import java.util.ArrayList;

import customLists.ImagesStudio;
import dataInLists.DataInGallery;

public class Images extends Activity {
	Activity activity = Images.this;
	ArrayList<DataInGallery> Data = new ArrayList<DataInGallery>();
	GridView lv;
	ImagesStudio adapter;

	TextView Title;
	ArrayList<String> selectedItems = new ArrayList<String>();
	ImageView MenuButton;
	int Max, Code;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	//	Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(activity, 2));

		setContentView(R.layout.activity_gallery);
		// ***********************************************
		Max = getIntent().getExtras().getInt("Max");
		Code = getIntent().getExtras().getInt("Code");

		lv = findViewById(R.id.listViewOrders);
		Title = findViewById(R.id.Title);
		MenuButton = findViewById(R.id.MenuButton);
		adapter = new ImagesStudio(activity, Data);
		lv.setAdapter(adapter);
		lv.setFriction(ViewConfiguration.getScrollFriction() * 8);

		lv.setOnItemClickListener((arg0, arg1, position, arg3) -> {
			if (adapter.getItem(position).t == 0) {
				if (selectedItems.size() <= (Max - 1)) {
					adapter.getItem(position).t = 1;
					selectedItems.add(adapter.getItem(position).link);
				} else {
					Toast.makeText(activity, R.string.NoMore, Toast.LENGTH_SHORT).show();
				}

			} else {
				adapter.getItem(position).t = 0;
				selectedItems.remove(adapter.getItem(position).link);
			}
			if (selectedItems.size() > 0) {
				Title.setText(getResources().getString(R.string.Selected) + " " + selectedItems.size() + " "
						+ getResources().getString(R.string.Image));
				MenuButton.setImageResource(R.mipmap.selected);
			} else {
				Title.setText("");
				MenuButton.setImageResource(R.mipmap.close);
			}

			adapter.notifyDataSetChanged();
			if (Max == 1) {
				setOnBack();
			}
		});
		MenuButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				setOnBack();
			}
		});

	}

	private void fetchGalleryImages() {
		final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };// get
																								// all
																								// columns
																								// of
																								// type
																								// images
		final String orderBy = MediaStore.Images.Media.DATE_TAKEN;// order data
																	// by date
		@SuppressWarnings("deprecation")
		Cursor imagecursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null,
				orderBy + " DESC");// get all data in Cursor by sorting in DESC
									// order

		// Loop to cursor count
		for (int i = 0; i < imagecursor.getCount(); i++) {
			imagecursor.moveToPosition(i);
			int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);// get
			int dataColumnIndex2 = imagecursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);																				// column
																							// index
			//adapter.add(new DataInGallery(imagecursor.getString(dataColumnIndex), (byte) 0));
			adapter.add(new DataInGallery(imagecursor.getString(dataColumnIndex), (byte) 0,imagecursor.getString(dataColumnIndex2)));

		}
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onResume() {
		fetchGalleryImages();
		super.onResume();

	}

	public void setOnBack() {
		Intent intent = new Intent();
		intent.putExtra("SelectedImages", selectedItems.toString());
		setResult(Code, intent);
		super.onBackPressed();
		overridePendingTransition(R.anim.right_slide_in2, R.anim.right_slide_out2);
	}

}
