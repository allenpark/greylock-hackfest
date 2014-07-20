package com.greylock.wave;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class SubscribedTabListFragment extends ListFragment {
	public static Fragment newInstance() {
		return new SubscribedTabListFragment();
	}
	List<String> userAmount;
	List<String> channelNames;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {


		



		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public void onResume() {
		super.onResume();
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Channel");
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> channelList, ParseException e) {
				if (e == null) {
					Log.d("score", "Retrieved " + channelList.size()
							+ " scores");
					buildChannelNames();
				} else {
					Log.d("score", "Error: " + e.getMessage());
				}
			}
		});



	}

	private void buildChannelNames() {
		ParseInstallation currentInstall = ParseInstallation.getCurrentInstallation();
		ParseGeoPoint loc = currentInstall.getParseGeoPoint("currentLocation");
		userAmount = new ArrayList<String>();
		channelNames = currentInstall.getList("channels");
		//channelNames.get(0);
		if (channelNames == null) {
			channelNames = new LinkedList<String>();
		}
		HttpGetter get = new HttpGetter();
		loc.getLatitude();
		String [] url = new String[channelNames.size()];
		for (int i = 0; i < channelNames.size(); i++) {
			url[i] = "http://wave-greylock.herokuapp.com/api/getNumUsersOnChannelInRadius/"+ channelNames.get(i)+"/"+loc.getLatitude()+"/"+loc.getLongitude()+"/1";
			Log.d("yo", "url test" + url[i]);

		}
		get.execute(url);
		
		setListAdapter(new SubscribedBaseAdapter(getActivity(), channelNames, userAmount )); }
	}
	
	private class HttpGetter extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... requestString) {
			// TODO Auto-generated method stub
			StringBuilder builder = new StringBuilder();
			HttpClient client = new DefaultHttpClient();
			for(int i = 0; i<requestString.length; i++) {
				HttpGet httpGet = new HttpGet(requestString[i]);
				builder = new StringBuilder();
				try {
					HttpResponse response = client.execute(httpGet);
					StatusLine statusLine = response.getStatusLine();
					int statusCode = statusLine.getStatusCode();
					if (statusCode == 200) {
						HttpEntity entity = response.getEntity();
						InputStream content = entity.getContent();
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(content));
						String line;
						while ((line = reader.readLine()) != null) {
							builder.append(line);
						}
						try {
							JSONObject json = new JSONObject(builder.toString());
							userAmount.add(""+json.get("num_users"));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Log.v("Getter", "Your data: " + builder.toString()); //response data
					} else {
						Log.e("Getter", "Failed to download file");
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			
			setListAdapter(new SubscribedBaseAdapter(getActivity(), channelNames, userAmount ));

		}
	}




}
