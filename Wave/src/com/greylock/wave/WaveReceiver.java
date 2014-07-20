package com.greylock.wave;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class WaveReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Intent resultIntent = new Intent(context, WaveFeedActivity.class);
		resultIntent.putExtra("data", intent.getExtras().getString("com.parse.Data"));
		
		String channel = intent.getExtras().getString("com.parse.Channel");
		resultIntent.putExtra("channel", channel);
		JSONObject json = null;
		String message = null;
		try {
			json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Log.i("WaveReceiver json", json.toString());
		
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
		String ar = sharedPreferences.getString(channel, null);
		JSONArray arr = null;
		if (ar == null) {
			arr = new JSONArray();
		} else {
			try {
				arr = new JSONArray(ar);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			message = json.getString("message");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		arr.put(message);
		sharedPreferences.edit().putString(channel,  arr.toString()).commit();
		resultIntent.setAction(Long.toString(System.currentTimeMillis()));
		// Because clicking the notification opens a new ("special") activity, there's
		// no need to create an artificial back stack.
		PendingIntent resultPendingIntent =
		    PendingIntent.getActivity(
		    context,
		    0,
		    resultIntent,
		    PendingIntent.FLAG_UPDATE_CURRENT
		);
		NotificationCompat.Builder mBuilder =
			    new NotificationCompat.Builder(context)
			    .setSmallIcon(R.drawable.wave_icon)
			    .setContentTitle("New Wave: " + channel)
			    .setContentText(String.format("%d new message(s).", arr.length()))
			    .setContentIntent(resultPendingIntent);
		NotificationManager mNotifyMgr = 
		        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		// Builds the notification and issues it.
		Notification notification = mBuilder.build();
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		mNotifyMgr.notify(channel.hashCode(), notification);
	}

}
