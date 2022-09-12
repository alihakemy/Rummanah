package com.usmart.com.rummanah.studio;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.usmart.com.rummanah.R;

import java.io.File;
import java.util.ArrayList;

import customLists.VideosStudio;
import dataInLists.DataInGallery;
import helpers.ExceptionHandler;

public class Videos extends Activity {
	ActionBar actionBar;
	Activity activity = Videos.this;
	ArrayList<DataInGallery> Data = new ArrayList<DataInGallery>();
	GridView lv;
	VideosStudio adapter;

	Typeface font;
	TextView Title;
	ArrayList<String> selectedItems = new ArrayList<String>();
	ImageView MenuButton;
	int Max, Code, MaxSize;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(activity, 2));

		setContentView(R.layout.activity_gallery);
		// ***********************************************
		Max = getIntent().getExtras().getInt("Max");
		MaxSize = getIntent().getExtras().getInt("MaxSize");
		Code = getIntent().getExtras().getInt("Code");

		lv = (GridView) findViewById(R.id.listViewOrders);
		Title = (TextView) findViewById(R.id.Title);
		MenuButton = (ImageView) findViewById(R.id.MenuButton);
		adapter = new VideosStudio(activity, Data);

		lv.setAdapter(adapter);
		lv.setFriction(ViewConfiguration.getScrollFriction() * 4);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				File file = new File(adapter.getItem(position).link);
				long length = file.length() / (1024 * 1024);
				if (length <= MaxSize) {
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
								+ getResources().getString(R.string.Videos));
						MenuButton.setImageResource(R.mipmap.selected);
					} else {
						Title.setText("");
						MenuButton.setImageResource(R.mipmap.close);
					}

					adapter.notifyDataSetChanged();
					if (Max == 1) {
						setOnBack();
					}
				}

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
		final String[] columns = { MediaStore.Video.Media.DATA, MediaStore.Video.Media._ID };// get
																								// all
																								// columns
																								// of
																								// type
																								// images
		final String orderBy = MediaStore.Video.Media.DATE_TAKEN;// order data
																	// by date
		@SuppressWarnings("deprecation")
		Cursor imagecursor = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columns, null, null,
				orderBy + " DESC");// get all data in Cursor by sorting in DESC
									// order

		// Loop to cursor count
		for (int i = 0; i < imagecursor.getCount(); i++) {
			imagecursor.moveToPosition(i);
			int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Video.Media.DATA);// get
																							// column
			adapter.add(new DataInGallery(imagecursor.getString(dataColumnIndex), (byte) 0));// get
																								// Image
																								// from
																								// column
																								// index
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
		intent.putExtra("SelectedVideos", selectedItems.toString());
		setResult(Code, intent);
		super.onBackPressed();
		overridePendingTransition(R.anim.right_slide_in2, R.anim.right_slide_out2);
	}

}
