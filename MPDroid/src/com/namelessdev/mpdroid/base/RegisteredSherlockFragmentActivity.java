package com.namelessdev.mpdroid.base;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.namelessdev.mpdroid.MPDApplication;

;

public class RegisteredSherlockFragmentActivity extends
		SherlockFragmentActivity {
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
