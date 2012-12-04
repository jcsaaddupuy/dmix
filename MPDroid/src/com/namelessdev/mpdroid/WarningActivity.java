package com.namelessdev.mpdroid;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import com.namelessdev.mpdroid.base.RegisteredActivity;

public class WarningActivity extends RegisteredActivity {
	Activity myWarning;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		myWarning = this;
		setContentView(R.layout.warning);
		Button btnOK = (Button) findViewById(R.id.buttonOK);
		btnOK.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View view) {
				SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(myWarning);
				settings.edit().putBoolean("newWarningShown", true).commit();
				finish();
			}
		});

	}

	@Override
	public void onBackPressed() {

		// eat the event, do nothing
		return;
	}
}
