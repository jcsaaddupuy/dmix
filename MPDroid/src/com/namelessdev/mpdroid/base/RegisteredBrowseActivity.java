package com.namelessdev.mpdroid.base;

import org.a0z.mpd.MPD;

import com.namelessdev.mpdroid.BrowseActivity;
import com.namelessdev.mpdroid.MPDApplication;
import com.namelessdev.mpdroid.R;

public abstract class RegisteredBrowseActivity extends BrowseActivity {

	public RegisteredBrowseActivity(int addplaylist, int playlistadded,
			String mpdSearchAlbum) {
		super(R.string.addPlaylist, R.string.playlistAdded,
				MPD.MPD_SEARCH_ALBUM);
	}

	@Override
	protected void onStart() {
		super.onStart();
		MPDApplication app = (MPDApplication) getApplicationContext();
		app.setActivity(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		MPDApplication app = (MPDApplication) getApplicationContext();
		app.unsetActivity(this);
	}

}
