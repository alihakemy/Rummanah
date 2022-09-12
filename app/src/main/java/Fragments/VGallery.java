package Fragments;

import android.app.Fragment;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.usmart.com.rummanah.R;

import interfaces.FragmentLifecycle;

public class VGallery extends Fragment implements FragmentLifecycle {

	private String Obj;
	private VideoView mDemoSlider;
	private Context Context;
	View PG;

	public VGallery(/*String id, Context cont*/) {
		/*this.Obj = id;
		this.Context = cont;*/
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		PG = inflater.inflate(R.layout.video_gallery_frag, container, false);
		mDemoSlider = (VideoView) PG.findViewById(R.id.VideoView);
		String LINK = Obj;
		Uri uriVideo = Uri.parse(LINK);

		MediaController mediaController = new MediaController(Context);
		mediaController.setAnchorView(mDemoSlider);
		mDemoSlider.setMediaController(mediaController);
		mDemoSlider.setVideoURI(uriVideo);
		mDemoSlider.requestFocus();

		mDemoSlider.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				//mDemoSlider.start();
			}
		});

		return PG;
	}

	@Override
	public void onPauseFragment() {
		// mDemoSlider.pause();
	}

	@Override
	public void onResumeFragment() {

	}

}
