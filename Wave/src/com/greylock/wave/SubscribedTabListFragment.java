package com.greylock.wave;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SubscribedTabListFragment extends ListFragment {
	public static Fragment newInstance() {
		return new SubscribedTabListFragment();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ParseInstallation currentInstall = ParseInstallation.getCurrentInstallation();

		ParseQuery<ParseObject> query = ParseQuery.getQuery("Installation");
		//query.whereEqualTo("objectId", currentInstall.getObjectId());
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> channelList, ParseException e) {
				if (e == null) {
					Log.d("score", "Retrieved " + channelList.size()
							+ " scores");
					buildChannelNames(channelList);
				} else {
					Log.d("score", "Error: " + e.getMessage());
				}
			}
		});
		


		return super.onCreateView(inflater, container, savedInstanceState);
	}


	private void buildChannelNames(List<ParseObject> list) {
		List<String> channelNames = new ArrayList<String>();
		
		List<String> tempNames = list.get(0).getList("channels");
		for (int i = 0; i < tempNames.size(); i++) {
			channelNames.add(tempNames.get(i));
		}

		setListAdapter(new SubscribedBaseAdapter(getActivity(), channelNames));

	}

}
