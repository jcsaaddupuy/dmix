package com.namelessdev.mpdroid.notifications;

import org.a0z.mpd.MPDStatus;
import org.a0z.mpd.Music;
import org.a0z.mpd.event.StatusChangeListener;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;

import com.namelessdev.mpdroid.MPDApplication;
import com.namelessdev.mpdroid.MainMenuActivity;
import com.namelessdev.mpdroid.R;
import com.namelessdev.mpdroid.helpers.CoverAsyncHelper.CoverDownloadListener;
import com.namelessdev.mpdroid.widgets.WidgetHelperService;

public class MPDroidNotificationManager implements StatusChangeListener,
		CoverDownloadListener {

	public static final int NOTIFICATION_ID = 0;
	private static MPDroidNotificationManager instance;

	private boolean isEnable;
	Notification noti;
	private Context context;
	private Bitmap lastBitmap;
	private MPDStatus lastMpdStatus;

	public static MPDroidNotificationManager getInstance(Context context) {
		if (instance == null) {
			instance = new MPDroidNotificationManager(context);
		}
		return instance;
	}

	public static MPDroidNotificationManager getInstance(Context context,
			MPDStatus initialstatus) {
		getInstance(context);
		instance.lastMpdStatus = initialstatus;
		return instance;
	}

	private MPDroidNotificationManager(Context context) {
		isEnable = false;
		this.context = context;
		this.lastBitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.icon);
	}

	@Override
	public void onCoverDownloaded(Bitmap cover) {
		lastBitmap = cover;
		this.notifyChange();
	}

	@Override
	public void onCoverNotFound() {
		this.lastBitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.icon);
		this.notifyChange();
	}

	@Override
	public void volumeChanged(MPDStatus mpdStatus, int oldVolume) {
		// TODO Auto-generated method stub

	}

	@Override
	public void playlistChanged(MPDStatus mpdStatus, int oldPlaylistVersion) {

	}

	@Override
	public void trackChanged(MPDStatus mpdStatus, int oldTrack) {
		this.lastMpdStatus = mpdStatus;
		this.notifyChange();
	}

	@Override
	public void stateChanged(MPDStatus mpdStatus, String oldState) {
		this.lastMpdStatus = mpdStatus;
		this.notifyChange();
	}

	@Override
	public void repeatChanged(boolean repeating) {
		// TODO Auto-generated method stub
	}

	@Override
	public void randomChanged(boolean random) {
		// TODO Auto-generated method stub
	}

	@Override
	public void libraryStateChanged(boolean updating) {
		// TODO Auto-generated method stub

	}

	@Override
	public void connectionStateChanged(boolean connected, boolean connectionLost) {
		this.isEnable = connected && !connectionLost;

	}

	public void notifyChange() {
		if (!this.isEnable) {
			return;
		}
		if (this.lastMpdStatus == null) {
			return;
		}
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);

		boolean shoulDisplayStop = settings.getBoolean("enableStopButton",
				false);
		Music actSong = null;
		actSong = ((MPDApplication) this.context.getApplicationContext()).oMPDAsyncHelper.oMPD
				.getPlaylist().getByIndex(this.lastMpdStatus.getSongPos());

		boolean isPlaying = this.lastMpdStatus.getState().equalsIgnoreCase(
				MPDStatus.MPD_STATE_PLAYING);
		// Prepare intent which is triggered if the
		// notification is selected
		Intent intentApp = new Intent(context, MainMenuActivity.class);
		PendingIntent pIntentApp = PendingIntent.getActivity(context, 0,
				intentApp, PendingIntent.FLAG_UPDATE_CURRENT);

		Intent iPrev = new Intent(context, WidgetHelperService.class);
		iPrev.setAction(WidgetHelperService.CMD_PREV);
		PendingIntent pIntentPrev = PendingIntent.getService(context, 0, iPrev,
				PendingIntent.FLAG_UPDATE_CURRENT);

		Intent iNext = new Intent(context, WidgetHelperService.class);
		iNext.setAction(WidgetHelperService.CMD_NEXT);
		PendingIntent pIntentNext = PendingIntent.getService(context, 0, iNext,
				PendingIntent.FLAG_UPDATE_CURRENT);

		Intent iPlayPause = new Intent(context, WidgetHelperService.class);
		iPlayPause.setAction(WidgetHelperService.CMD_PLAYPAUSE);
		PendingIntent pIntentPlayPause = PendingIntent.getService(context, 0,
				iPlayPause, PendingIntent.FLAG_UPDATE_CURRENT);

		Builder nb = new NotificationCompat.Builder(context)
				.setContentTitle(actSong.getTitle())
				.setContentText(
						actSong.getArtist() + " - " + actSong.getAlbum())
				.setSmallIcon(R.drawable.icon)
				.setLargeIcon(this.lastBitmap)
				.setContentIntent(pIntentApp)
				.addAction(R.drawable.ic_appwidget_music_prev, "", pIntentPrev)
				.addAction(
						isPlaying ? R.drawable.ic_appwidget_music_pause
								: R.drawable.ic_appwidget_music_play, "",
						pIntentPlayPause);
		// Seem thats we are limited to 3 action in the notification bar :/
		/*
		 * if (shoulDisplayStop) { Intent iStop = new Intent(context,
		 * WidgetHelperService.class);
		 * iPlayPause.setAction(WidgetHelperService.CMD_STOP); PendingIntent
		 * pIntentStop = PendingIntent.getService(context, 0, iStop,
		 * PendingIntent.FLAG_UPDATE_CURRENT);
		 * nb.addAction(R.drawable.ic_media_stop, "", pIntentStop); }
		 */
		nb.addAction(R.drawable.ic_appwidget_music_next, "", pIntentNext);
		// Build notification
		noti = nb.build();

		noti.flags = Notification.FLAG_ONGOING_EVENT;
		NotificationManager nm = getNotificationManager(context);
		if (nm != null) {
			nm.notify(NOTIFICATION_ID, noti);
		}
	}

	public static NotificationManager getNotificationManager(Context context) {
		return (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

	}

	public static void cancel(Context context) {
		NotificationManager nm = getNotificationManager(context);
		if (nm != null) {
			nm.cancel(NOTIFICATION_ID);
		}
	}

	public static void cancelAll(Context context) {
		NotificationManager nm = getNotificationManager(context);
		if (nm != null) {
			nm.cancelAll();
		}
	}
}
