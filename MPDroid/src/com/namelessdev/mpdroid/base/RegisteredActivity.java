package com.namelessdev.mpdroid.base;

import com.namelessdev.mpdroid.MPDApplication;

import android.app.Activity;

public class RegisteredActivity extends Activity {
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
